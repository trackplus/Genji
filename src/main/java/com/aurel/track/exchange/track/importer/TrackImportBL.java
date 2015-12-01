/**
 * Genji Scrum Tool and Issue Tracker
 * Copyright (C) 2015 Steinbeis GmbH & Co. KG Task Management Solutions

 * <a href="http://www.trackplus.com">Genji Scrum Tool</a>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/* $Id:$ */


package com.aurel.track.exchange.track.importer;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.account.AccountBL;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadItemIDListItems;
import com.aurel.track.admin.customize.lists.ListBL;
import com.aurel.track.admin.customize.lists.customOption.OptionBL;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.customize.lists.systemOption.PriorityBL;
import com.aurel.track.admin.customize.lists.systemOption.SeverityBL;
import com.aurel.track.admin.customize.lists.systemOption.StatusBL;
import com.aurel.track.admin.customize.objectStatus.SystemStatusBL;
import com.aurel.track.admin.customize.projectType.ProjectTypesBL;
import com.aurel.track.admin.customize.role.RoleBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldConfigBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldDesignBL;
import com.aurel.track.admin.customize.treeConfig.field.OptionSettingsBL;
import com.aurel.track.admin.customize.treeConfig.field.TextBoxSettingsBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.project.release.ReleaseBL;
import com.aurel.track.admin.user.department.DepartmentBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.attachment.AttachBL;
import com.aurel.track.beans.IHierarchicalBean;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.ISerializableLabelBean;
import com.aurel.track.beans.TAccessControlListBean;
import com.aurel.track.beans.TActualEstimatedBudgetBean;
import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.beans.TBudgetBean;
import com.aurel.track.beans.TCostBean;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TFieldChangeBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.THistoryTransactionBean;
import com.aurel.track.beans.TListBean;
import com.aurel.track.beans.TOptionBean;
import com.aurel.track.beans.TOptionSettingsBean;
import com.aurel.track.beans.TTextBoxSettingsBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.WorkItemDAO;
import com.aurel.track.dbase.HandleHome;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.errors.ErrorParameter;
import com.aurel.track.exchange.ImportCounts;
import com.aurel.track.exchange.track.ExchangeFieldNames;
import com.aurel.track.exchange.track.ExchangeHistoryFieldEntry;
import com.aurel.track.exchange.track.ExchangeHistoryTransactionEntry;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.FieldsManagerRT;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.callbackInterfaces.ILookup;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.ItemPersisterException;
import com.aurel.track.item.budgetCost.ComputedValueBL;
import com.aurel.track.item.budgetCost.ExpenseBL;
import com.aurel.track.item.budgetCost.RemainingPlanBL;
import com.aurel.track.item.consInf.ConsInfBL;
import com.aurel.track.item.consInf.RaciRole;
import com.aurel.track.item.history.FieldChangeBL;
import com.aurel.track.item.history.HistoryDAOUtils;
import com.aurel.track.item.history.HistoryLoaderBL;
import com.aurel.track.item.history.HistoryLoaderBL.LONG_TEXT_TYPE;
import com.aurel.track.item.history.HistoryTransactionBL;
import com.aurel.track.item.history.HistoryValues;
import com.aurel.track.persist.ReportBeanHistoryLoader;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeanWithHistory;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.StringArrayParameterUtils;
import com.aurel.track.util.TagReplacer;

public class TrackImportBL {

	private static final Logger LOGGER = LogManager.getLogger(TrackImportBL.class);
	private static WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();

	private static final int NOSAVE = 0;
	private static final int CREATED = 1;
	private static final int UPGRADED = 2;

	/**
	 * Prevent instance of this class
	 */
	private TrackImportBL() {

	}

	/**
	 * Import a List of workItems
	 * 
	 * @param externalReportBeansList
	 * @param externalDropDowns
	 * @param personID
	 * @param locale
	 */
	public static ImportCounts importWorkItems(List<ExchangeWorkItem> externalReportBeansList,
			SortedMap<String, List<ISerializableLabelBean>> externalDropDowns, Map<Integer, Integer> fieldsMatcherMap, Integer personID, Locale locale)
			throws ImportExceptionList, ImportException {
		ImportCounts importCounts = new ImportCounts();
		if (externalReportBeansList == null || externalReportBeansList.isEmpty()) {
			throw new ImportException("admin.actions.importTp.err.noWorkItems");
		}
		ImportExceptionList importExceptionList = new ImportExceptionList();
		LOGGER.debug("Number of workItems to import " + externalReportBeansList.size());
		// get the external uuids list
		List<String> externalUuidsList = new ArrayList<String>();
		for (ExchangeWorkItem exchangeWorkItem : externalReportBeansList) {
			externalUuidsList.add(exchangeWorkItem.getUuid());
		}
		// get the internal workItems by external uuids
		List<TWorkItemBean> internalWorkItemsByUuids = workItemDAO.loadByUuids(externalUuidsList);
		LOGGER.debug("Number of already existing workItems " + internalWorkItemsByUuids.size());
		int[] workItemIDs = GeneralUtils.createIntArrFromIntegerList(GeneralUtils.createIntegerListFromBeanList(internalWorkItemsByUuids));
		// get all internal history transactions for the existing workItems
		List<THistoryTransactionBean> historyTransactions = HistoryTransactionBL.getByWorkItemsAndFields(workItemIDs, null, true, null, null, null);

		// categorize the history transactions by workItem, and get the
		// corresponding existing transaction uuids
		Map<Integer, Set<String>> transactionUuidsForWorkitems = new HashMap<Integer, Set<String>>();
		for (THistoryTransactionBean historyTransactionBean : historyTransactions) {
			Integer workItemID = historyTransactionBean.getWorkItem();
			Set<String> transactionUuidsForWorkitem = transactionUuidsForWorkitems.get(workItemID);
			if (transactionUuidsForWorkitem == null) {
				transactionUuidsForWorkitem = new HashSet<String>();
				transactionUuidsForWorkitems.put(workItemID, transactionUuidsForWorkitem);
			}
			transactionUuidsForWorkitem.add(historyTransactionBean.getUuid());
		}
		List<ReportBean> reportBeanList = LoadItemIDListItems.getReportBeansByWorkItemIDs(workItemIDs, false, personID, locale, true, true, true, true, true,
				true, false, false, false);
		// do not include the history field changes but include the comments
		List<ReportBeanWithHistory> reportBeansWithHistoryList = ReportBeanHistoryLoader.getReportBeanWithHistoryList(reportBeanList, locale, false, true,
				false, null, true, true, true, false, true, personID, null, null, null, true, LONG_TEXT_TYPE.ISFULLHTML);

		// put the existing workItems into a uuid based map
		Map<String, ReportBeanWithHistory> reportBeanWithHistoryMap = new HashMap<String, ReportBeanWithHistory>();
		for (ReportBeanWithHistory reportBeansWithHistory : reportBeansWithHistoryList) {
			reportBeanWithHistoryMap.put(reportBeansWithHistory.getWorkItemBean().getUuid(), reportBeansWithHistory);
		}
		// load the internal drop downs for the external one
		Map<String, List<ISerializableLabelBean>> internalDropDowns = getInternalDropdowns();
		// gather the present fields: all system fieldIDs (same fieldID for
		// internal and external)
		// and the used custom internal fieldID (different fieldID for internal
		// and external)
		Set<Integer> presentFieldIDs = new HashSet<Integer>();
		Iterator<Integer> itrInteranalFieldIDs = fieldsMatcherMap.values().iterator();
		while (itrInteranalFieldIDs.hasNext()) {
			presentFieldIDs.add(itrInteranalFieldIDs.next());
		}
		// match the internal and external drop downs
		Map<String, Map<Integer, Integer>> dropDownMatcherMap = getDropDownMatcherMap(externalDropDowns, internalDropDowns, fieldsMatcherMap, presentFieldIDs);

		String changedByByFieldID = MergeUtil.mergeKey(SystemFields.INTEGER_CHANGEDBY, null);
		String personByFieldID = MergeUtil.mergeKey(SystemFields.INTEGER_PERSON, null);
		// gather the workItems which contain issue links: parentIDs, links in
		// description and later issue links
		List<String> workItemUuidsWithLinks = new ArrayList<String>();
		// gather the transactions which contain issue links: parentIDs, links
		// in description and later issue links
		List<String> transcationUuidsWithLinks = new ArrayList<String>();
		// This map contains all imported (saved as new) work item key data: new
		// and old work item key.
		Map<Integer, String> externalWorkItemNrToInternalWkItemNr = new HashMap<Integer, String>();
		// add/upgrade the workItems one by one
		for (ExchangeWorkItem exchangeWorkItem : externalReportBeansList) {
			Map<String, Object> actualFieldValuesMap = exchangeWorkItem.getActualFieldValuesMap();
			Integer workItemID = exchangeWorkItem.getWorkItemID();
			String strChangedBy = (String) actualFieldValuesMap.get(changedByByFieldID);
			Integer changedByExternal = null;
			try {
				changedByExternal = new Integer(strChangedBy);
			} catch (Exception e) {
				LOGGER.warn("No changed by person specified by workItemID" + workItemID, e);
			}
			Integer changedByInternal = dropDownMatcherMap.get(personByFieldID).get(changedByExternal);
			String uuid = exchangeWorkItem.getUuid();
			ReportBeanWithHistory reportBeansWithHistory = reportBeanWithHistoryMap.get(uuid);
			Set<String> transactionUuidsForInternalWorkitem = null;
			WorkItemContext workItemContext = null;
			if (reportBeansWithHistory == null) {
				reportBeansWithHistory = new ReportBeanWithHistory(new ReportBean());
				workItemContext = FieldsManagerRT.getExchangeContextForNew(changedByInternal, locale, presentFieldIDs);
				LOGGER.debug("New workItem for workItemID " + workItemID + " and uuid " + exchangeWorkItem.getUuid());
			} else {
				// the actual code does not prepares the validation structure
				// (updateModifyWorkItem)
				workItemContext = FieldsManagerRT.getExchangeContextForExisting(reportBeansWithHistory.getWorkItemBean(), changedByInternal, locale,
						presentFieldIDs);
				// get the existing history transactions for existing workItems
				transactionUuidsForInternalWorkitem = transactionUuidsForWorkitems.get(reportBeansWithHistory.getWorkItemBean().getObjectID());
				LOGGER.debug("Existing workItem for workItemID " + workItemID + " and uuid " + exchangeWorkItem.getUuid());
			}
			try {
				int result = addUpgradeWorkItem(exchangeWorkItem, reportBeansWithHistory, workItemContext, transactionUuidsForInternalWorkitem,
						dropDownMatcherMap, workItemUuidsWithLinks, transcationUuidsWithLinks, externalWorkItemNrToInternalWkItemNr);
				switch (result) {
				case CREATED:
					importCounts.setNoOfCreatedIssues(importCounts.getNoOfCreatedIssues() + 1);
					break;
				case UPGRADED:
					importCounts.setNoOfUpdatedIssues(importCounts.getNoOfUpdatedIssues() + 1);
					break;
				default:
				}
			} catch (ImportExceptionList e) {
				importExceptionList.getErrorDataList().addAll(e.getErrorDataList());
			}
		}

		/**
		 * set the linked values: parents and issue links in long text and later
		 * the issue links
		 */
		// get the internal workItemIDs which have links (parent or link in
		// description)
		Map<Integer, Integer> workItemIDsMatcherMap = dropDownMatcherMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_ISSUENO, null));
		/**
		 * replace the parent or issue links from description in the actual
		 * values
		 */
		if (!workItemUuidsWithLinks.isEmpty()) {
			Map<Integer, ILabelBean> internalWorkItemsWithLinksMap = GeneralUtils.createMapFromList(workItemDAO.loadByUuids(workItemUuidsWithLinks));
			String parentFieldID = MergeUtil.mergeKey(SystemFields.INTEGER_SUPERIORWORKITEM, null);
			String descriptionFieldID = MergeUtil.mergeKey(SystemFields.INTEGER_DESCRIPTION, null);
			for (ExchangeWorkItem exchangeWorkItem  : externalReportBeansList) {
				Integer externalWorkItemID = exchangeWorkItem.getWorkItemID();
				Integer internalWorkItemID = workItemIDsMatcherMap.get(externalWorkItemID);
				if (internalWorkItemID == null) {
					LOGGER.warn("No internal workItem found for externalWorkItemID " + externalWorkItemID);
					continue;
				}
				TWorkItemBean internalWorkItemBean = (TWorkItemBean) internalWorkItemsWithLinksMap.get(internalWorkItemID);
				if (internalWorkItemBean != null) {
					Map<String, Object> actualFieldValuesMap = exchangeWorkItem.getActualFieldValuesMap();
					boolean changed = false;
					String strExternalParentID = (String) actualFieldValuesMap.get(parentFieldID);
					if (strExternalParentID != null) {
						// replace the parent
						Integer externalParentID = new Integer(strExternalParentID);
						Integer internalParentID = workItemIDsMatcherMap.get(externalParentID);
						if (internalParentID == null) {
							LOGGER.warn("No internal workItem found for externalParentID " + externalParentID);
						} else {
							internalWorkItemBean.setSuperiorworkitem(internalParentID);
							changed = true;
							LOGGER.debug("Replacing the parent " + externalParentID + " of external workItem " + externalWorkItemID + " with the parent "
									+ internalParentID + " of the internal workItem  " + internalWorkItemID);
						}
					}
					String description = (String) actualFieldValuesMap.get(descriptionFieldID);
					if (description != null && !TagReplacer.gatherIssueLinks(new StringBuilder(description)).isEmpty()) {
						// replace the issue links in description
						StringBuilder descriptionBuffer = new StringBuilder(description);
						if (TagReplacer.replaceIssueLinks(descriptionBuffer, workItemIDsMatcherMap)) {
							internalWorkItemBean.setDescription(descriptionBuffer.toString());
							changed = true;
							LOGGER.debug("Replacing the issue link of external workItem " + externalWorkItemID
									+ " with the issue link of the internal workItem  " + internalWorkItemID);
						}
					}
					if (changed) {
						try {
							workItemDAO.save(internalWorkItemBean);
						} catch (ItemPersisterException e) {
							LOGGER.error("Saving of the workItem by setting the parent failed with" + e.getMessage());
							LOGGER.debug(ExceptionUtils.getStackTrace(e));
						}
					}
				}
			}
		}
		/**
		 * replace the parent or issue links from description in the history
		 */
		if (!transcationUuidsWithLinks.isEmpty()) {
			List<TFieldChangeBean> internalFieldChangesWithLinks = FieldChangeBL.loadByTransactionUUIDS(transcationUuidsWithLinks);
			Set<Integer> pseudoHistoryFields = HistoryLoaderBL.getPseudoHistoryFields();
			for (TFieldChangeBean fieldChangeBean : internalFieldChangesWithLinks) {
				Integer fieldID = fieldChangeBean.getFieldKey();
				boolean changed = false;
				if (SystemFields.INTEGER_SUPERIORWORKITEM.equals(fieldID)) {
					// explicit history for parent
					Integer extarnalNewParent = fieldChangeBean.getNewSystemOptionID();
					if (extarnalNewParent != null && workItemIDsMatcherMap.get(extarnalNewParent) != null) {
						fieldChangeBean.setNewSystemOptionID(workItemIDsMatcherMap.get(extarnalNewParent));
						changed = true;
						LOGGER.debug("Replacing the new parent in the transaction " + fieldChangeBean.getHistoryTransaction() + " from external parent "
								+ extarnalNewParent + " to internal parent " + workItemIDsMatcherMap.get(extarnalNewParent));
					}
					Integer extarnalOldParent = fieldChangeBean.getOldSystemOptionID();
					if (extarnalOldParent != null && workItemIDsMatcherMap.get(extarnalOldParent) != null) {
						fieldChangeBean.setOldSystemOptionID(workItemIDsMatcherMap.get(extarnalOldParent));
						changed = true;
						LOGGER.debug("Replacing the old parent in the transaction " + fieldChangeBean.getHistoryTransaction() + " from external parent "
								+ extarnalNewParent + " to internal parent " + workItemIDsMatcherMap.get(extarnalNewParent));
					}
				} else {
					IFieldTypeRT fieldTypeRT;
					if (pseudoHistoryFields.contains(fieldID)) {
						fieldTypeRT = FieldTypeManager.getFieldTypeRT(SystemFields.DESCRIPTION);
					} else {
						fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
					}
					if (fieldTypeRT != null && fieldTypeRT.getValueType() == ValueType.LONGTEXT) {
						String externalNewText = fieldChangeBean.getNewLongTextValue();
						if (externalNewText != null && !TagReplacer.gatherIssueLinks(new StringBuilder(externalNewText)).isEmpty()) {
							// replace the issue links in text
							StringBuilder textBuffer = new StringBuilder(externalNewText);
							if (TagReplacer.replaceIssueLinks(textBuffer, workItemIDsMatcherMap)) {
								fieldChangeBean.setNewLongTextValue(textBuffer.toString());
								changed = true;
								LOGGER.debug("Replacing the new text in the transaction " + fieldChangeBean.getHistoryTransaction() + " for fieldID " + fieldID);
							}
						}
						String extarnalOldText = fieldChangeBean.getOldLongTextValue();
						if (extarnalOldText != null && !TagReplacer.gatherIssueLinks(new StringBuilder(externalNewText)).isEmpty()) {
							// replace the issue links in text
							StringBuilder textBuffer = new StringBuilder(extarnalOldText);
							if (TagReplacer.replaceIssueLinks(textBuffer, workItemIDsMatcherMap)) {
								fieldChangeBean.setOldLongTextValue(textBuffer.toString());
								changed = true;
								LOGGER.debug("Replacing the old text in the transaction " + fieldChangeBean.getHistoryTransaction() + " for fieldID " + fieldID);
							}
						}
					}
				}
				if (changed) {
					try {
						FieldChangeBL.save(fieldChangeBean);
					} catch (ItemPersisterException e) {
						LOGGER.error("Saving of the field change failed with" + e.getMessage());
						LOGGER.debug(ExceptionUtils.getStackTrace(e));
					}
				}
			}
		}

		String tempDirectoryPath = AttachBL.getAttachDirBase() + File.separator + AttachBL.tmpAttachments;
		AttachBL.deleteDirectory(new File(tempDirectoryPath));

		if (importExceptionList.containsException()) {
			throw importExceptionList;
		}
		LOGGER.debug("Import done");
		if (!externalWorkItemNrToInternalWkItemNr.isEmpty()) {
			writeFileWithNewlyImportedData(externalWorkItemNrToInternalWkItemNr);
		}
		return importCounts;
	}

	public static void writeFileWithNewlyImportedData(Map<Integer, String> data) {
		final String dirNAME = "dataImportedFromOtherTrack";
		final String fileNAME = "importNewOldId";
		String tpHome = HandleHome.getTrackplus_Home();
		tpHome = tpHome.replace("\\", "/");
		tpHome = StringArrayParameterUtils.appendSlashToURLString(tpHome);
		String path = tpHome + dirNAME;
		File f = new File(path);
		if (!f.exists()) {
			f.mkdir();
		}
		PrintWriter writer = null;
		try {

			int nr = 1;
			String fileName = fileNAME + nr + ".csv";
			while (new File(path + "/" + fileName).exists()) {
				nr++;
				fileName = fileNAME + nr + ".csv";
			}
			path += "/" + fileName;
			writer = new PrintWriter(path, "UTF-8");
			for (Map.Entry<Integer, String> entry : data.entrySet()) {
				String tmp = entry.getKey() + "," + entry.getValue();
				writer.println(tmp);
			}

		} catch (IOException ex) {
			LOGGER.debug(ExceptionUtils.getStackTrace(ex), ex);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	private static void addOldValueAsCustomAttribute(WorkItemContext workItemContext, String value) {
		final String FIELD_NAME_TO_SEARCH = "OldTrackID";
		List<TFieldBean> beans = FieldDesignBL.loadByName(FIELD_NAME_TO_SEARCH);
		if (beans != null && !beans.isEmpty()) {
			TFieldBean aBean = beans.get(0);
			workItemContext.getPresentFieldIDs().add(aBean.getObjectID());
			workItemContext.getWorkItemBean().setAttribute(aBean.getObjectID(), value);
		}
	}

	/**
	 * add/upgrade of the workItem and the history
	 * 
	 * @param exchangeWorkItem
	 * @param internalWorkItemBean
	 * @param transactionUuidsForInternalWorkitem
	 * @param dropDownMatcherMap
	 */
	private static int addUpgradeWorkItem(ExchangeWorkItem exchangeWorkItem, ReportBeanWithHistory reportBeanWithHistory, WorkItemContext workItemContext,
			Set<String> transactionUuidsForInternalWorkitem, Map<String, Map<Integer, Integer>> dropDownMatcherMap, List<String> externalWorkItemUuidsWithLink,
			List<String> externalTransactionUuidsWithLink, Map<Integer, String> externalWorkItemNrToInternalWkItemNr) throws ImportExceptionList {

		int result = NOSAVE;
		TWorkItemBean internalWorkItemBean = workItemContext.getWorkItemBean();
		boolean isNew = true;
		if (internalWorkItemBean.getObjectID() != null) {
			isNew = false;
		}
		Map<Integer, Integer> fieldsMatcherMap = dropDownMatcherMap.get(ExchangeFieldNames.FIELD);

		/**
		 * sets/updates the workItem actual values
		 */
		Map<String, Object> actualFieldValuesMap = exchangeWorkItem.getActualFieldValuesMap();
		Integer externalWorkItemID = exchangeWorkItem.getWorkItemID();
		String externalUUID = exchangeWorkItem.getUuid();
		if (internalWorkItemBean.getUuid() == null) {
			// set the uuid of the internal workItem to the external workItem's
			// uuid (for new workItems)
			internalWorkItemBean.setUuid(externalUUID);
		}
		String strExteranalLastEditDate = (String) actualFieldValuesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_LASTMODIFIEDDATE, null));
		Date externalLastEdit = DateTimeUtils.getInstance().parseISODateTime(strExteranalLastEditDate);
		if (actualFieldValuesMap != null && externalIsMoreRecent(externalLastEdit, internalWorkItemBean.getLastEdit(), "workItem", externalWorkItemID)) {
			for (String externalKey : actualFieldValuesMap.keySet()) {
				Integer[] parts = MergeUtil.getParts(externalKey);
				Integer externalFieldID = parts[0];
				Integer parameterCode = parts[1];
				if (!SystemFields.INTEGER_ISSUENO.equals(externalFieldID) && !SystemFields.INTEGER_SUPERIORWORKITEM.equals(externalFieldID)) {
					// do not set the issueNo and parentNo values because they
					// will be different from the external values
					Integer internalFieldID;
					if (fieldsMatcherMap.get(externalFieldID) != null) {
						// replace the external fieldID with the internal
						// fieldID
						internalFieldID = fieldsMatcherMap.get(externalFieldID);
					} else {
						LOGGER.warn("No internal fieldID found for external fieldID " + externalFieldID + " and external workItem " + externalWorkItemID
								+ ". The field cannot be saved");
						continue;
					}
					// the issue number shouldn't be set!
					Object exchangeAttribut = actualFieldValuesMap.get(externalKey);
					// do not set the issue number (because it either exists or
					// will be generated)
					IFieldTypeRT internalFieldTypeRT = FieldTypeManager.getFieldTypeRT(internalFieldID, parameterCode);
					if (internalFieldTypeRT != null) {
						if (exchangeAttribut != null
								&& (internalFieldTypeRT.getValueType() == ValueType.CUSTOMOPTION || internalFieldTypeRT.getValueType() == ValueType.SYSTEMOPTION)) {
							// lookup value
							String internalKey;
							if (internalFieldTypeRT.getValueType() == ValueType.CUSTOMOPTION) {
								// custom option: we do not differentiate by
								// fieldID_parameterCode between
								// the different custom option fields because if
								// a "not compatible" (assigned to naother list)
								// optionsettings is already present it will be
								// leaved as it is
								// but the options should be set to the imported
								// values independently of the optionsettings
								internalKey = ExchangeFieldNames.OPTION;
							} else {
								// system option
								internalKey = MergeUtil.mergeKey(internalFieldID, parameterCode);
								ILookup lookup = (ILookup) internalFieldTypeRT;
								Integer dropdownFieldKey = lookup.getDropDownMapFieldKey(internalFieldID);
								if (!dropdownFieldKey.equals(internalFieldID)) {
									internalKey = MergeUtil.mergeKey(dropdownFieldKey, parameterCode);
								}
							}
							Map<Integer, Integer> dropDownMap = dropDownMatcherMap.get(internalKey);
							if (dropDownMap != null) {
								Integer externalDropDown = null;
								if (internalFieldTypeRT.isMultipleValues()) {
									// custom select
									List<String> multipleValuesList = (List<String>) exchangeAttribut;
									Integer[] internalDropDownArr = null;
									if (multipleValuesList != null) {
										internalDropDownArr = new Integer[multipleValuesList.size()];
										Iterator<String> itrMultiple = multipleValuesList.iterator();
										int i = 0;
										while (itrMultiple.hasNext()) {
											String externalStringValue = itrMultiple.next();
											Integer parsedExternalDropDownValue = (Integer) internalFieldTypeRT.parseISOValue(externalStringValue);
											Integer internalDropDownValue = dropDownMap.get(parsedExternalDropDownValue);
											internalDropDownArr[i++] = internalDropDownValue;
										}
									}
									exchangeAttribut = internalDropDownArr;
								} else {
									// system select
									externalDropDown = (Integer) internalFieldTypeRT.parseISOValue(exchangeAttribut);
									exchangeAttribut = dropDownMap.get(externalDropDown);
								}
								internalWorkItemBean.setAttribute(internalFieldID, parameterCode, exchangeAttribut);
							}
						} else {
							// direct value
							internalWorkItemBean.setAttribute(internalFieldID, parameterCode, internalFieldTypeRT.parseISOValue(exchangeAttribut));
							if (internalFieldTypeRT.getValueType() == ValueType.LONGTEXT) {
								String longText = (String) exchangeAttribut;
								if (longText != null) {
									List<Integer> issueIDsInNew = TagReplacer.gatherIssueLinks(new StringBuilder(longText));
									// if the description contains issue links
									// the workItem should be post-processed
									// later
									// now it contains the wrong (external)
									// issue links
									// mark it as post processing needed
									if (!issueIDsInNew.isEmpty()) {
										externalWorkItemUuidsWithLink.add(externalUUID);
									}
								}
							}
						}
					}
				} else {
					if (SystemFields.INTEGER_SUPERIORWORKITEM.equals(externalFieldID)) {
						// if the parentID is specified the workItem should be
						// post-processed later
						// the external parentID is not set at all because by
						// saving the
						// issue it could cause foreign key problems
						// mark it as post processing needed
						Object exchangeAttribut = actualFieldValuesMap.get(externalKey);
						if (exchangeAttribut != null) {
							externalWorkItemUuidsWithLink.add(externalUUID);
						}
					}
				}
			}
			List<ErrorData> workItemErrorList = new ArrayList<ErrorData>();

			if (isNew) {
				addOldValueAsCustomAttribute(workItemContext, externalWorkItemID.toString());
			}
			boolean saveNeeded = FieldsManagerRT.performSave(workItemContext, workItemErrorList, false, false/*, false*/);
			if (workItemErrorList.isEmpty()) {
				if (saveNeeded) {
					LOGGER.info("WorkItem externalWorkItemID " + externalWorkItemID + " and internalWorkItemID " + internalWorkItemBean.getObjectID() + " was "
							+ (isNew ? "created" : "updated"));
					if (isNew) {
						result = CREATED;
						externalWorkItemNrToInternalWkItemNr.put(internalWorkItemBean.getObjectID(),
								externalWorkItemID + "," + internalWorkItemBean.getSynopsis());
					} else {
						result = UPGRADED;
					}
				}
			} else {
				ImportExceptionList importExceptionList = new ImportExceptionList();
				importExceptionList.setErrorDataList(workItemErrorList);
				throw importExceptionList;
			}
		}

		// add the workItemIDs to the matcherMap
		Integer internalWorkItemID = internalWorkItemBean.getObjectID();
		if (internalWorkItemID == null) {
			return result;
		}
		Map<Integer, Integer> workItemIDsMatcherMap = dropDownMatcherMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_ISSUENO, null));
		workItemIDsMatcherMap.put(externalWorkItemID, internalWorkItemID);
		Map<Integer, Integer> personMap = dropDownMatcherMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_PERSON, null));
		/**
		 * add history values
		 */
		Set<Integer> pseudoHistoryFields = HistoryLoaderBL.getPseudoHistoryFields();
		List<ExchangeHistoryTransactionEntry> historyValuesList = exchangeWorkItem.getHistoryValues();
		if (historyValuesList != null) {
			for (ExchangeHistoryTransactionEntry exchangeHistoryTransactionEntry : historyValuesList) {
				String externalTransactionUuid = exchangeHistoryTransactionEntry.getUuid();
				String strChangedAt = exchangeHistoryTransactionEntry.getChangedAt();
				Date changedAt = null;
				if (strChangedAt != null) {
					changedAt = DateTimeUtils.getInstance().parseISODateTime(strChangedAt);
				}
				List<ExchangeHistoryFieldEntry> externalFieldChanges = exchangeHistoryTransactionEntry.getHistoryFieldChanges();
				if (externalFieldChanges != null) {
					if (transactionUuidsForInternalWorkitem != null && transactionUuidsForInternalWorkitem.contains(externalTransactionUuid)) {
						// found external and internal transaction with the same
						// uuid:
						// the only possible modification is by the comment
						// in the reportBeanWithHistory only the comments are
						// loaded
						for (ExchangeHistoryFieldEntry exchangeHistoryFieldEntry : externalFieldChanges) {
							Integer fieldID = new Integer(exchangeHistoryFieldEntry.getFieldID());
							if (fieldID.equals(SystemFields.INTEGER_COMMENT)) {
								List<HistoryValues> internalComments = reportBeanWithHistory.getComments();
								if (internalComments != null) {
									Iterator<HistoryValues> itrInternalComments = internalComments.iterator();
									while (itrInternalComments.hasNext()) {
										HistoryValues historyValues = itrInternalComments.next();
										if (historyValues != null) {
											String internalComment = historyValues.getNewShowValue();
											Integer internalTimesEdited = historyValues.getTimesEdited();
											String internalTransactionUUID = historyValues.getTransactionUuid();
											if (externalTransactionUuid.equals(internalTransactionUUID)) {
												String externalComment = exchangeHistoryFieldEntry.getNewStringValue();
												String strExternalTimesEdited = exchangeHistoryFieldEntry.getTimesEdited();
												Integer externalTimesEdited = null;
												if (strExternalTimesEdited != null) {
													externalTimesEdited = new Integer(strExternalTimesEdited);
												}
												if (EqualUtils.notEqual(internalComment, externalComment) && externalTimesEdited != null
														&& (internalTimesEdited == null || externalTimesEdited.intValue() > internalTimesEdited.intValue())) {
													try {
														HistoryDAOUtils.updateFieldChange(FieldChangeBL.loadByPrimaryKey(historyValues.getObjectID()),
																externalComment, ValueType.LONGTEXT);
													} catch (ItemPersisterException e) {
														LOGGER.warn("Updating the comment for workItem externalWorkItemID " + externalWorkItemID
																+ " and internalWorkItemID " + internalWorkItemID + ", historyTransactionID "
																+ internalTransactionUUID + " failed with " + e.getMessage());
														LOGGER.debug(ExceptionUtils.getStackTrace(e));
													}
													LOGGER.info("Comment modified for workItem externalWorkItemID " + externalWorkItemID
															+ " and internalWorkItemID " + internalWorkItemID);
													if (externalComment != null) {
														List<Integer> issueIDsInComment = TagReplacer.gatherIssueLinks(new StringBuilder(externalComment));
														if (!issueIDsInComment.isEmpty()) {
															// remember the
															// transactionUUID
															// for the comment
															externalTransactionUuidsWithLink.add(externalTransactionUuid);
														}
													}
												}
											}
										}
									}
								}
							}
						}
					} else {
						// the external transaction uuid is not found in the
						// internal history
						Integer externalPerson = new Integer(exchangeHistoryTransactionEntry.getChangedBy());
						Integer historyTransactionID = HistoryTransactionBL.saveHistoryTransaction(internalWorkItemID,
								personMap.get(externalPerson), changedAt, externalTransactionUuid);
						if (historyTransactionID!=null) {
							for (ExchangeHistoryFieldEntry exchangeHistoryFieldEntry : externalFieldChanges) {
								Integer externalFieldID = new Integer(exchangeHistoryFieldEntry.getFieldID());
								Integer internalFieldID;
								if (fieldsMatcherMap.get(externalFieldID) != null) {
									// replace the external fieldID with the
									// internal fieldID for the custom fields
									internalFieldID = fieldsMatcherMap.get(externalFieldID);
								} else {
									// for the system fields the internal and
									// external fieldIDs are the same and does
									// not appear in this map
									internalFieldID = externalFieldID;
								}
								Integer parameterCode = null;
								if (exchangeHistoryFieldEntry.getParameterCode() != null) {
									parameterCode = new Integer(exchangeHistoryFieldEntry.getParameterCode());
								}
								IFieldTypeRT internalFieldTypeRT = null;
								if (pseudoHistoryFields.contains(internalFieldID)) {
									internalFieldTypeRT = FieldTypeManager.getFieldTypeRT(SystemFields.INTEGER_DESCRIPTION, null);
								} else {
									internalFieldTypeRT = FieldTypeManager.getFieldTypeRT(internalFieldID, parameterCode);
								}
								if (internalFieldTypeRT != null) {
									String newStringValue = exchangeHistoryFieldEntry.getNewStringValue();
									Object newValue = internalFieldTypeRT.parseISOValue(newStringValue);
									String oldStringValue = exchangeHistoryFieldEntry.getOldStringValue();
									Object oldValue = internalFieldTypeRT.parseISOValue(oldStringValue);
									if (internalFieldTypeRT.getValueType() == ValueType.CUSTOMOPTION
											|| (internalFieldTypeRT.getValueType() == ValueType.SYSTEMOPTION && !SystemFields.INTEGER_SUPERIORWORKITEM
													.equals(externalFieldID))) {
										// exclude the parent item because it
										// will be set at the end
										String internalKey;
										if (internalFieldTypeRT.getValueType() == ValueType.CUSTOMOPTION) {
											// custom option: we do not
											// differentiate by
											// fieldID_parameterCode between
											// the different custom option
											// fields because if a
											// "not compatible" (assigned to
											// naother list)
											// optionsettings is already present
											// it will be leaved as it is
											// but the options should be set to
											// the imported values independently
											// of the optionsettings
											internalKey = ExchangeFieldNames.OPTION;
										} else {
											/*
											 * if
											 * (SystemFields.INTEGER_ISSUENO.equals
											 * (externalFieldID) ||
											 * SystemFields.
											 * INTEGER_SUPERIORWORKITEM
											 * .equals(externalFieldID)) {
											 * //superior workitem explicit
											 * history: //TODO it does not work
											 * if the parent is not yet added in
											 * the dropDownMatcherMap
											 * internalKey =
											 * MergeUtil.mergeKey(SystemFields
											 * .INTEGER_ISSUENO, parameterCode);
											 * } else {
											 */
											// system option
											internalKey = MergeUtil.mergeKey(internalFieldID, parameterCode);
											ILookup lookup = (ILookup) internalFieldTypeRT;
											Integer dropdownFieldKey = lookup.getDropDownMapFieldKey(internalFieldID);
											if (!dropdownFieldKey.equals(internalFieldID)) {
												internalKey = MergeUtil.mergeKey(dropdownFieldKey, parameterCode);
											}
											// }
										}
										// the history values are always stored
										// one by one (no multiple values as in
										// actual values)
										Map<Integer, Integer> dropDownMap = dropDownMatcherMap.get(internalKey);
										if (dropDownMap != null) {
											newValue = dropDownMap.get(newValue);
											oldValue = dropDownMap.get(oldValue);
										}
									}
									// save the fieldChange
									try {
										HistoryDAOUtils.insertFieldChange(internalFieldID, parameterCode, historyTransactionID, newValue, oldValue,
												internalFieldTypeRT.getValueType(), internalFieldTypeRT.getSystemOptionType());
									} catch (ItemPersisterException e) {
										LOGGER.warn("Inserting the  fieldChange for workItem externalWorkItemID " + externalWorkItemID
												+ " and internalWorkItemID " + internalWorkItemID + " for field " + internalFieldID + ", historyTransactionID "
												+ historyTransactionID + " failed with " + e.getMessage());
										LOGGER.debug(ExceptionUtils.getStackTrace(e));
									}
									LOGGER.debug("History added for workItem externalWorkItemID " + externalWorkItemID + " and internalWorkItemID "
											+ internalWorkItemID + " and field " + internalFieldID);
									// remember the transactionUUIDs for the
									// descriptions/comments/common
									// history/other long texts
									// which contain issueIDs because they
									// should be replaced later
									if (internalFieldTypeRT.getValueType() == ValueType.LONGTEXT) {
										if (newStringValue != null) {
											List<Integer> issueIDsInNew = TagReplacer.gatherIssueLinks(new StringBuilder(newStringValue));
											if (!issueIDsInNew.isEmpty()) {
												externalTransactionUuidsWithLink.add(externalTransactionUuid);
											} else {
												if (oldStringValue != null) {
													List<Integer> issueIDsInOld = TagReplacer.gatherIssueLinks(new StringBuilder(oldStringValue));
													if (!issueIDsInOld.isEmpty()) {
														externalTransactionUuidsWithLink.add(externalTransactionUuid);
													}
												}
											}
										}
									} else {
										// explicit history for parent
										// remember the transactionUUIDs with
										// parentIDs because they should be
										// replaced later
										// now the wrong (external) value is
										// saved but it does not cause foreign
										// key problems because
										// the system fields from history are
										// not linked with foreign keys
										if (SystemFields.INTEGER_SUPERIORWORKITEM.equals(externalFieldID)) {
											if (newValue != null) {
												externalTransactionUuidsWithLink.add(externalTransactionUuid);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		/**
		 * add consultants/informants
		 */
		addRaciRole(exchangeWorkItem.getConsultedList(), reportBeanWithHistory.getConsultedList(), internalWorkItemID, RaciRole.CONSULTANT, personMap);
		addRaciRole(exchangeWorkItem.getInformedList(), reportBeanWithHistory.getInformedList(), internalWorkItemID, RaciRole.INFORMANT, personMap);
		/**
		 * add budget values
		 */
		List<TBudgetBean> externalBudgetBeanList = exchangeWorkItem.getBudgetBeanList();
		List<TBudgetBean> internalBudgetBeanList = reportBeanWithHistory.getBudgetHistory();
		Map<String, TBudgetBean> internalBudgetBeanMap = new HashMap<String, TBudgetBean>();
		if (externalBudgetBeanList != null) {
			Date lastBudget = null;
			if (internalBudgetBeanList != null) {
				for (TBudgetBean budgetBean : internalBudgetBeanList) {
					if (lastBudget == null || lastBudget.before(budgetBean.getLastEdit())) {
						lastBudget = budgetBean.getLastEdit();
					}
					internalBudgetBeanMap.put(budgetBean.getUuid(), budgetBean);
				}
			}
			for (TBudgetBean externalBudgetBean : externalBudgetBeanList) {
				String uuid = externalBudgetBean.getUuid();
				TBudgetBean internalBudgetBean = internalBudgetBeanMap.get(uuid);
				if (internalBudgetBean == null) {
					// it is a new object
					externalBudgetBean.setObjectID(null);
					externalBudgetBean.saveBean(externalBudgetBean, dropDownMatcherMap);
					LOGGER.info("Budget added");
					if (externalIsMoreRecent(externalBudgetBean.getLastEdit(), lastBudget, "budget", externalBudgetBean.getObjectID())) {
						lastBudget = externalBudgetBean.getLastEdit();
						// actualize the computed values table if more recent
						ComputedValueBL.computeBudgetOrPlan(externalBudgetBean);
						LOGGER.info("Budget recomputed");
					}
				}
			}
		}
		/**
		 * add plan values
		 */
		List<TBudgetBean> externalPlanBeanList = exchangeWorkItem.getPlannedValueBeanList();
		List<TBudgetBean> internalPlanBeanList = reportBeanWithHistory.getPlannedValueHistory();
		Map<String, TBudgetBean> internalPlanBeanMap = new HashMap<String, TBudgetBean>();
		if (externalPlanBeanList != null) {
			Date lastBudget = null;
			if (internalPlanBeanList != null) {
				for (TBudgetBean budgetBean : internalPlanBeanList) {
					if (lastBudget == null || lastBudget.before(budgetBean.getLastEdit())) {
						lastBudget = budgetBean.getLastEdit();
					}
					internalPlanBeanMap.put(budgetBean.getUuid(), budgetBean);
				}
			}
			for (TBudgetBean externalBudgetBean : externalPlanBeanList) {
				String uuid = externalBudgetBean.getUuid();
				TBudgetBean internalBudgetBean = internalPlanBeanMap.get(uuid);
				if (internalBudgetBean == null) {
					// it is a new object
					externalBudgetBean.setObjectID(null);
					externalBudgetBean.saveBean(externalBudgetBean, dropDownMatcherMap);
					LOGGER.info("Plan added");
					if (externalIsMoreRecent(externalBudgetBean.getLastEdit(), lastBudget, "plan", externalBudgetBean.getObjectID())) {
						lastBudget = externalBudgetBean.getLastEdit();
						// actualize the computed values table if more recent
						ComputedValueBL.computeBudgetOrPlan(externalBudgetBean);
						LOGGER.info("Plan recomputed");
					}
				}
			}
		}
		/**
		 * add/modify remaining budget
		 */
		TActualEstimatedBudgetBean externalRemainingBudget = exchangeWorkItem.getActualEstimatedBudgetBean();
		TActualEstimatedBudgetBean internalRemainingBudget = reportBeanWithHistory.getActualEstimatedBudgetBean();
		if (externalRemainingBudget != null) {
			if (internalRemainingBudget == null) {
				// it is a new object
				externalRemainingBudget.setObjectID(null);
				externalRemainingBudget.saveBean(externalRemainingBudget, dropDownMatcherMap);
				LOGGER.info("Remaining budget added");
			} else {
				// remaining budget found
				if (externalIsMoreRecent(externalRemainingBudget.getLastEdit(), internalRemainingBudget.getLastEdit(), "remaining budget",
						externalRemainingBudget.getObjectID())) {
					externalRemainingBudget.replaceLookup(dropDownMatcherMap);
					if (externalRemainingBudget.hasChanged(internalRemainingBudget)) {
						// modify if changed
						externalRemainingBudget.copy(internalRemainingBudget);
						RemainingPlanBL.save(internalRemainingBudget);
						LOGGER.info("Remaining budget modified");
					}
				}
			}
		}
		/**
		 * add/modify cost values
		 */
		List<TCostBean> externalCostBeanList = exchangeWorkItem.getExpenseBeanList();
		List<TCostBean> internalCostBeanList = reportBeanWithHistory.getCosts();
		Map<String, ISerializableLabelBean> internalCostBeanMap = new HashMap<String, ISerializableLabelBean>();
		if (externalCostBeanList != null) {
			if (internalCostBeanList != null) {
				internalCostBeanMap = GeneralUtils.createUUIDMapFromSerializableBean((List) internalCostBeanList);
			}
			Iterator<TCostBean> itrExternalCostBeans = externalCostBeanList.iterator();
			while (itrExternalCostBeans.hasNext()) {
				TCostBean externalCostBean = itrExternalCostBeans.next();
				String uuid = externalCostBean.getUuid();
				TCostBean internalCostBean = (TCostBean) internalCostBeanMap.get(uuid);
				if (internalCostBean == null) {
					// it is a new object
					externalCostBean.setObjectID(null);
					externalCostBean.saveBean(externalCostBean, dropDownMatcherMap);
					// TODO do not recompute always but only the last for each
					// person
					ComputedValueBL.computeExpenses(externalCostBean.getWorkitem(), externalCostBean.getPerson());
					LOGGER.info("Expense added and recomputed");
				} else {
					// cost found
					if (externalIsMoreRecent(externalCostBean.getLastEdit(), internalCostBean.getLastEdit(), "expense", externalCostBean.getObjectID())) {
						externalCostBean.replaceLookup(dropDownMatcherMap);
						if (externalCostBean.hasChanged(internalCostBean)) {
							// modify if changed
							externalCostBean.copy(internalCostBean);
							ExpenseBL.saveCostBean(internalCostBean);
							// TODO do not recompute always but only the last
							// for each person
							ComputedValueBL.computeExpenses(internalCostBean.getWorkitem(), internalCostBean.getPerson());
							LOGGER.info("Expense modified and recomputed");
						}
					}
				}
			}
		}

		/**
		 * add/modify the attachments
		 */
		List<TAttachmentBean> externalAttachmentBeanList = exchangeWorkItem.getAttachmentBeanList();
		List<TAttachmentBean> internalAttachmentBeanList = reportBeanWithHistory.getAttachments();
		if (externalAttachmentBeanList != null) {
			Map<String, ISerializableLabelBean> internalAttachmentBeanMap = new HashMap<String, ISerializableLabelBean>();
			if (internalAttachmentBeanList != null) {
				internalAttachmentBeanMap = GeneralUtils.createUUIDMapFromSerializableBean((List) internalAttachmentBeanList);
			}
			Iterator<TAttachmentBean> itrExternalAttachmentBeans = externalAttachmentBeanList.iterator();
			while (itrExternalAttachmentBeans.hasNext()) {
				TAttachmentBean externalAttachmentBean = itrExternalAttachmentBeans.next();
				String uuid = externalAttachmentBean.getUuid();
				TAttachmentBean internalAttachmentBean = (TAttachmentBean) internalAttachmentBeanMap.get(uuid);
				boolean foundMatch = false;
				if (internalAttachmentBean == null) {
					// not found by uuid, try by considerAsSame
					if (internalAttachmentBeanList != null) {
						Iterator<TAttachmentBean> tAttachmentBean = internalAttachmentBeanList.iterator();
						while (tAttachmentBean.hasNext()) {
							internalAttachmentBean = tAttachmentBean.next();
							if (externalAttachmentBean.considerAsSame(internalAttachmentBean, dropDownMatcherMap)) {
								foundMatch = true;
								break;
							}
						}
					}
				} else {
					foundMatch = true;
				}

				if (foundMatch) {
					// attachment found
					if (internalAttachmentBean != null
							&& externalIsMoreRecent(externalAttachmentBean.getLastEdit(), internalAttachmentBean.getLastEdit(), "attachment",
									externalAttachmentBean.getObjectID())) {
						if (externalAttachmentBean.hasChanged(internalAttachmentBean)) {
							internalAttachmentBean.setDescription(externalAttachmentBean.getDescription());
							internalAttachmentBean.setLastEdit(externalAttachmentBean.getLastEdit());
							AttachBL.save(internalAttachmentBean);
							LOGGER.info("Attachment description modified: " + externalAttachmentBean.getFileName());
						}
					}
				} else {
					// new attachment
					Integer internalAttachmentNo;
					Integer externalAttachmentNo = externalAttachmentBean.getObjectID();
					externalAttachmentBean.setObjectID(null);
					internalAttachmentNo = externalAttachmentBean.saveBean(externalAttachmentBean, dropDownMatcherMap);
					LOGGER.info("Attachment added: " + externalAttachmentBean.getFileName());

					String attachmentsPath = AttachBL.getAttachDirBase();
					String tempDirectoryPath = attachmentsPath + File.separator + AttachBL.tmpAttachments;
					String workItemAttachmentPath = attachmentsPath + File.separator + internalWorkItemID;

					// add also the files
					File attachmentFile = new File(tempDirectoryPath, AttachBL.getFileNameAttachment(externalAttachmentNo, externalWorkItemID));

					// parent directory
					File workItemAttachmentFile = new File(workItemAttachmentPath);
					if (!workItemAttachmentFile.exists()) {
						workItemAttachmentFile.mkdirs();
					}
					// Move file to new directory
					boolean success = attachmentFile.renameTo(new File(workItemAttachmentFile, AttachBL.getFileNameAttachment(internalAttachmentNo,
							externalWorkItemID)));
					if (!success) {
						LOGGER.error("Moving the file  " + externalAttachmentBean.getFileName() + " failed");
					}
				}
			}
		}
		return result;
	}

	private static boolean externalIsMoreRecent(Date externalLastEditDate, Date internalLastEditedDate, String entity, Integer value) {
		if (externalLastEditDate == null) {
			LOGGER.warn("No external lastEdit value found for entity " + entity + " and value " + value);
			return false;
		}
		if (internalLastEditedDate == null || externalLastEditDate.after(internalLastEditedDate)) {
			// new workItem or the external is more recent
			return true;
		}
		return false;
	}

	/**
	 * Add the raci roles into the database
	 * 
	 * @param externalRaciList
	 * @param internalRaciList
	 * @param workItemID
	 * @param raciRole
	 * @param personMap
	 */
	private static void addRaciRole(List<Integer> externalRaciList, Set<Integer> internalRaciList, Integer workItemID, String raciRole,
			Map<Integer, Integer> personMap) {
		int i = 0;
		if (externalRaciList != null) {
			Iterator<Integer> itrExternal = externalRaciList.iterator();
			while (itrExternal.hasNext()) {
				Integer externalPerson = itrExternal.next();
				Integer internalPerson = personMap.get(externalPerson);
				if (internalPerson != null && (internalRaciList == null || !internalRaciList.contains(internalPerson))) {
					ConsInfBL.insertByWorkItemAndPersonAndRaciRole(workItemID, internalPerson, raciRole);
					i++;
				}
			}
		}
		if (i > 0 && LOGGER.isInfoEnabled()) {
			LOGGER.info("Number of " + raciRole + " persons added: " + i);
		}
	}

	/**
	 * Get the key based matches for the dropdowns
	 * 
	 * @param externalDropDowns
	 * @param internalDropDowns
	 * @return
	 */
	private static Map<String, Map<Integer, Integer>> getDropDownMatcherMap(SortedMap<String, List<ISerializableLabelBean>> externalDropDowns,
			Map<String, List<ISerializableLabelBean>> internalDropDowns, Map<Integer, Integer> fieldsMatcherMap, Set<Integer> presentFieldIDs)
			throws ImportExceptionList {

		ImportExceptionList importExceptionList = new ImportExceptionList();
		Map<String, Map<Integer, Integer>> matcherMap = new HashMap<String, Map<Integer, Integer>>();
		matcherMap.put(ExchangeFieldNames.FIELD, fieldsMatcherMap);

		/**
		 * the roles assignments will be added only to the new projects
		 */
		List<Integer> internalProjectIDsBeforeMatch = GeneralUtils.createIntegerListFromBeanList(internalDropDowns.get(MergeUtil.mergeKey(
				SystemFields.INTEGER_PROJECT, null)));

		/**
		 * the custom lists should be in the correct order because of the
		 * hierarchical relations between them
		 */
		List<ISerializableLabelBean> listBeans = externalDropDowns.get(ExchangeFieldNames.LIST);
		if (listBeans != null) {
			Collections.sort(listBeans, new Comparator<ISerializableLabelBean>() {
				public int compare(ISerializableLabelBean o1, ISerializableLabelBean o2) {
					// suppose the objectID order is enough
					return o1.getObjectID().compareTo(o2.getObjectID());
				}
			});
		}
		externalDropDowns.put(ExchangeFieldNames.LIST, listBeans);

		/**
		 * prepare the options by list
		 */
		Map<Integer, List<ISerializableLabelBean>> externalOptionsMap = new HashMap<Integer, List<ISerializableLabelBean>>();
		List<ISerializableLabelBean> optionBeans = externalDropDowns.get(ExchangeFieldNames.OPTION);
		if (optionBeans != null) {
			Iterator<ISerializableLabelBean> itrOptionBeans = optionBeans.iterator();
			while (itrOptionBeans.hasNext()) {
				TOptionBean optionBean = (TOptionBean) itrOptionBeans.next();
				Integer listID = optionBean.getList();
				List<ISerializableLabelBean> optionBeansList = externalOptionsMap.get(listID);
				if (optionBeansList == null) {
					optionBeansList = new ArrayList<ISerializableLabelBean>();
					externalOptionsMap.put(listID, optionBeansList);
				}
				optionBeansList.add(optionBean);
			}
		}
		List<ISerializableLabelBean> externalList;
		List<ISerializableLabelBean> internalList;
		List<String> firstKeys = new ArrayList<String>();
		// projectType should be matched before project
		firstKeys.add(ExchangeFieldNames.PROJECTTYPE);
		// systemStates should be matched before account, release, project
		firstKeys.add(ExchangeFieldNames.SYSTEMSTATE);
		// costcenter should be matched before account and department!
		firstKeys.add(ExchangeFieldNames.COSTCENTER);
		// account
		firstKeys.add(ExchangeFieldNames.ACCOUNT);
		// department should be matched before person but after costcenter
		firstKeys.add(ExchangeFieldNames.DEPARTMENT);
		// role should be matched before role assignments
		firstKeys.add(ExchangeFieldNames.ROLE);
		// priority and severity should be matched before person (email reminder
		// level)
		firstKeys.add(MergeUtil.mergeKey(SystemFields.INTEGER_PRIORITY, null));
		firstKeys.add(MergeUtil.mergeKey(SystemFields.INTEGER_SEVERITY, null));
		// status should be matched before project (initial status)
		firstKeys.add(MergeUtil.mergeKey(SystemFields.INTEGER_STATE, null));
		// issue types (and project and projectType) should be matched before
		// fieldConfig
		firstKeys.add(MergeUtil.mergeKey(SystemFields.INTEGER_ISSUETYPE, null));
		// person should be matched before project (default responsible, default
		// manager)
		firstKeys.add(MergeUtil.mergeKey(SystemFields.INTEGER_PERSON, null));
		// project should be matched before release, but after person, state,
		// issueType, priority, severity system state and project type
		firstKeys.add(MergeUtil.mergeKey(SystemFields.INTEGER_PROJECT, null));
		firstKeys.add(MergeUtil.mergeKey(SystemFields.INTEGER_RELEASE, null));
		// lists should be matched after project (the project specific lists)
		firstKeys.add(ExchangeFieldNames.LIST);

		/**
		 * process the entities which should be processed first
		 */
		for (String key : firstKeys) {
			externalList = externalDropDowns.get(key);
			// remove the processed entities to not to process again in
			// remaining entities loop
			// externalDropDowns.remove(key);
			internalList = internalDropDowns.get(key);
			try {
				addEntityMatchMap(externalList, internalList, key, matcherMap);
			} catch (ImportExceptionList e) {
				importExceptionList.getErrorDataList().addAll(e.getErrorDataList());
			}
		}
		/**
		 * all additional (non-fieldType specific) entities should be processed
		 * now because they can't be broken in parts by MergeUtil like the
		 * remaining entities
		 */

		/**
		 * process the custom list options (after the lists are matched)
		 */
		if (listBeans != null) {
			// by now the lists are already matched
			Map<Integer, Integer> listMatches = matcherMap.get(ExchangeFieldNames.LIST);
			Iterator<ISerializableLabelBean> itrListBean = listBeans.iterator();
			while (itrListBean.hasNext()) {
				TListBean listBean = (TListBean) itrListBean.next();
				Integer externalListID = listBean.getObjectID();
				Integer internalListID = listMatches.get(externalListID);
				// options are not loaded in the internal dropdowns but are
				// loaded now for each list separately
				try {
					addEntityMatchMap(externalOptionsMap.get(externalListID), (List) OptionBL.loadDataSourceByList(internalListID), ExchangeFieldNames.OPTION,
							matcherMap);
				} catch (ImportExceptionList e) {
					importExceptionList.getErrorDataList().addAll(e.getErrorDataList());
				}
			}
			// externalDropDowns.remove(ExchangeFieldNames.OPTION);
		}

		/**
		 * process the fieldConfigs
		 */
		List<ISerializableLabelBean> externalFieldConfigBeans = externalDropDowns.get(ExchangeFieldNames.FIELDCONFIG);
		List<TFieldConfigBean> internalFieldConfigBeans = FieldConfigBL.loadAllForFields(presentFieldIDs);
		try {
			addEntityMatchMap(externalFieldConfigBeans, (List) internalFieldConfigBeans, ExchangeFieldNames.FIELDCONFIG, matcherMap);
		} catch (ImportExceptionList e) {
			importExceptionList.getErrorDataList().addAll(e.getErrorDataList());
		}
		Map<Integer, Integer> fieldConfigMatch = matcherMap.get(ExchangeFieldNames.FIELDCONFIG);
		// externalDropDowns.remove(ExchangeFieldNames.FIELDCONFIG);
		// gather the internalFieldConfigIDs for the field settings
		List<Integer> internalFieldConfigIDs = new ArrayList<Integer>();
		if (fieldConfigMatch != null) {
			Iterator<Integer> itrFieldConfig = fieldConfigMatch.values().iterator();
			while (itrFieldConfig.hasNext()) {
				Integer fieldConfigID = itrFieldConfig.next();
				internalFieldConfigIDs.add(fieldConfigID);
			}
		}

		/**
		 * process the fieldSettings
		 */
		List<ISerializableLabelBean> externalOptionSettingsBeans = externalDropDowns.get(ExchangeFieldNames.OPTIONSETTINGS);
		List<TOptionSettingsBean> internalOptionSettingsBeans = OptionSettingsBL.loadByConfigList(internalFieldConfigIDs);
		try {
			addEntityMatchMap(externalOptionSettingsBeans, (List) internalOptionSettingsBeans, ExchangeFieldNames.OPTIONSETTINGS, matcherMap);
		} catch (ImportExceptionList e) {
			importExceptionList.getErrorDataList().addAll(e.getErrorDataList());
		}
		// externalDropDowns.remove(ExchangeFieldNames.OPTIONSETTINGS);

		List<ISerializableLabelBean> externalTextBoxSettingsBeans = externalDropDowns.get(ExchangeFieldNames.TEXTBOXSETTINGS);
		List<TTextBoxSettingsBean> internalTextBoxSettingsBeans = TextBoxSettingsBL.loadByConfigList(internalFieldConfigIDs);
		try {
			addEntityMatchMap(externalTextBoxSettingsBeans, (List) internalTextBoxSettingsBeans, ExchangeFieldNames.TEXTBOXSETTINGS, matcherMap);
		} catch (ImportExceptionList e) {
			importExceptionList.getErrorDataList().addAll(e.getErrorDataList());
		}
		// externalDropDowns.remove(ExchangeFieldNames.TEXTBOXSETTINGS);

		// TODO implement special handling for general settings: specific for
		// each custom picker
		/*
		 * List<ISerializableLabelBean> externalGeneralSettingsBeans =
		 * externalDropDowns.get(ExchangeFieldNames.GENERALSETTINGS);
		 * List<TGeneralSettingsBean> internalGeneralSettingsBeans =
		 * generalSettingsDAO.loadByConfigList(internalFieldConfigIDs);
		 * addEntityMatchMap(externalGeneralSettingsBeans,
		 * (List)internalGeneralSettingsBeans,
		 * ExchangeFieldNames.GENERALSETTINGS, matcherMap);
		 */
		// externalDropDowns.remove(ExchangeFieldNames.GENERALSETTINGS);

		/**
		 * process role assignments (after role, person and project match)
		 */
		List<Integer> internalProjectIDsAfterMatch = GeneralUtils.createIntegerListFromBeanList(ProjectBL.loadAll());
		// add the role assignments only for new projects
		internalProjectIDsAfterMatch.removeAll(internalProjectIDsBeforeMatch);
		if (internalProjectIDsAfterMatch != null && !internalProjectIDsAfterMatch.isEmpty()) {
			Map<Integer, Integer> projectMatch = matcherMap.get(MergeUtil.mergeKey(SystemFields.PROJECT, null));
			List<ISerializableLabelBean> externalAccessControlLists = externalDropDowns.get(ExchangeFieldNames.ACL);
			if (externalAccessControlLists != null) {
				for (ISerializableLabelBean serializableLabelBean : externalAccessControlLists) {
					TAccessControlListBean externalAccessControlListBean = (TAccessControlListBean) serializableLabelBean;
					Integer externalProjectID = externalAccessControlListBean.getProjectID();
					Integer internalProjectID = projectMatch.get(externalProjectID);
					if (internalProjectID != null && internalProjectIDsAfterMatch.contains(internalProjectID)) {
						externalAccessControlListBean.saveBean(externalAccessControlListBean, matcherMap);
					}
				}
			}
		}

		// the workItemIDs of the exported issues will be added during the
		// processing of each workItem
		Map<Integer, Integer> workItemIDsMatcherMap = new HashMap<Integer, Integer>();
		matcherMap.put(MergeUtil.mergeKey(SystemFields.INTEGER_ISSUENO, null), workItemIDsMatcherMap);
		// but an attempt will be made to match the only linked but not
		// explicitly exported workItemIDs through the uuids
		List<ISerializableLabelBean> externalLinkedItems = externalDropDowns.get(ExchangeFieldNames.LINKED_ITEMS);
		if (externalLinkedItems != null) {
			// get the uuid list of the only linked external workItems
			List<String> externalLinkedUuidsList = new ArrayList<String>();
			Iterator<ISerializableLabelBean> itrExternalLinkedItems = externalLinkedItems.iterator();
			while (itrExternalLinkedItems.hasNext()) {
				ISerializableLabelBean externalLinkedItem = itrExternalLinkedItems.next();
				externalLinkedUuidsList.add(externalLinkedItem.getUuid());
			}
			if (!externalLinkedUuidsList.isEmpty()) {
				// get the internal workItems by the linked external uuids
				Map<String, Integer> internalUuidToObjectIDMap = new HashMap<String, Integer>();
				List<TWorkItemBean> internalWorkItemBeanList = workItemDAO.loadByUuids(externalLinkedUuidsList);
				if (internalWorkItemBeanList != null && !internalWorkItemBeanList.isEmpty()) {
					// found internal workItems by external uuids
					Iterator<TWorkItemBean> itrWorkItemBean = internalWorkItemBeanList.iterator();
					while (itrWorkItemBean.hasNext()) {
						TWorkItemBean workItemBean = itrWorkItemBean.next();
						internalUuidToObjectIDMap.put(workItemBean.getUuid(), workItemBean.getObjectID());
					}
					itrExternalLinkedItems = externalLinkedItems.iterator();
					while (itrExternalLinkedItems.hasNext()) {
						// match the extarnal and internal workItemIDs by uuids
						ISerializableLabelBean externalLinkedItem = itrExternalLinkedItems.next();
						Integer externObjectID = externalLinkedItem.getObjectID();
						Integer internObjectID = internalUuidToObjectIDMap.get(externalLinkedItem.getUuid());
						if (internObjectID != null) {
							workItemIDsMatcherMap.put(externObjectID, internObjectID);
						}
					}
				}
			}
		}
		// force reloading all lists
		LookupContainer.resetAll();

		if (importExceptionList.containsException()) {
			throw importExceptionList;
		}
		return matcherMap;
	}

	/**
	 * Get the key based matches for the dropdowns
	 * 
	 * @param externalDropDowns
	 * @param internalDropDowns
	 * @return
	 */
	private static Map<Integer, Integer> addEntityMatchMap(List<ISerializableLabelBean> externalList, List<ISerializableLabelBean> internalList, String key,
			Map<String, Map<Integer, Integer>> matcherMap) throws ImportExceptionList {
		if (externalList == null) {
			return null;
		}
		if (MergeUtil.mergeKey(SystemFields.INTEGER_PROJECT, null).equals(key) || MergeUtil.mergeKey(SystemFields.INTEGER_RELEASE, null).equals(key)) {
			// reorder according to the hierarchy
			externalList = setHierarchycalOrder(externalList);
		}

		if (internalList == null) {
			internalList = new ArrayList<ISerializableLabelBean>();
		}
		ImportExceptionList importExceptionList = new ImportExceptionList();
		Map<Integer, Integer> entityMatcherMap = matcherMap.get(key);
		if (entityMatcherMap == null) {
			entityMatcherMap = new HashMap<Integer, Integer>();
			matcherMap.put(key, entityMatcherMap);
		}
		Map<String, ISerializableLabelBean> internalMap = GeneralUtils.createUUIDMapFromSerializableBean(internalList);
		Iterator<ISerializableLabelBean> itrExt = externalList.iterator();
		while (itrExt.hasNext()) {
			ISerializableLabelBean labelBeanExt = itrExt.next();
			String externUuid = labelBeanExt.getUuid();
			boolean foundMatch = false;
			if (externUuid == null) {
				LOGGER.debug("The entity of type " + key + " with ID " + labelBeanExt.getObjectID() + " and label " + labelBeanExt.getLabel()
						+ " has no external uuid");
			} else {
				// iterate over internal beans for uuid match
				if (internalMap.containsKey(externUuid)) {
					ISerializableLabelBean labelBeanInt = internalMap.get(externUuid);
					if (labelBeanInt != null) {
						entityMatcherMap.put(labelBeanExt.getObjectID(), labelBeanInt.getObjectID());
						foundMatch = true;
					}
				}
			}
			if (!foundMatch) {
				// iterate again over all internal beans for "consider as same"
				// match
				Iterator<ISerializableLabelBean> itrInt = internalList.iterator();
				while (itrInt.hasNext()) {
					ISerializableLabelBean labelBeanInt = itrInt.next();
					if (labelBeanExt.considerAsSame(labelBeanInt, matcherMap)) {
						entityMatcherMap.put(labelBeanExt.getObjectID(), labelBeanInt.getObjectID());
						foundMatch = true;
						break;
					}
				}
			}
			// no match found a new bean should be created
			if (!foundMatch) {
				Integer exteriorObjectID = labelBeanExt.getObjectID();
				// set the objectID to null because it will be saved as a new
				// object
				labelBeanExt.setObjectID(null);
				Integer interiorObjectID = labelBeanExt.saveBean(labelBeanExt, matcherMap);
				// the object ID is set to null in save, it should be set back
				// to the original value
				// for further use (see for ex. saving custom list options we
				// need the original externalListIDs)
				labelBeanExt.setObjectID(exteriorObjectID);
				if (interiorObjectID != null) {
					LOGGER.debug("New dropdown object with ID " + interiorObjectID + " and label " + labelBeanExt.getLabel() + " of type " + key
							+ " was added for external ID " + exteriorObjectID);
					entityMatcherMap.put(exteriorObjectID, interiorObjectID);
				} else {
					LOGGER.warn("Dropdown object of type " + key + " was not added for external ID " + exteriorObjectID);
					List<ErrorParameter> errorParameterList = new ArrayList<ErrorParameter>();
					errorParameterList.add(new ErrorParameter(key));
					errorParameterList.add(new ErrorParameter(exteriorObjectID));
					importExceptionList.getErrorDataList().add(new ErrorData("admin.actions.importTp.err.dropDown", errorParameterList));
				}
			}
		}
		if (importExceptionList.containsException()) {
			throw importExceptionList;
		}
		return entityMatcherMap;
	}

	/**
	 * For hierarchical entities (project/release) the order of import is
	 * important because the parent entities should be matched before the child
	 * entities to maintain the hierarchy
	 * 
	 * @param externalList
	 * @return
	 */
	private static List<ISerializableLabelBean> setHierarchycalOrder(List<ISerializableLabelBean> externalList) {
		Set<Integer> externalProjectIDs = new HashSet<Integer>();
		for (ISerializableLabelBean serializableLabelBean : externalList) {
			externalProjectIDs.add(serializableLabelBean.getObjectID());
		}
		Map<Integer, List<Integer>> parentToChildren = new HashMap<Integer, List<Integer>>();
		List<ISerializableLabelBean> list = new ArrayList<ISerializableLabelBean>();
		Map<Integer, ISerializableLabelBean> externalMap = new HashMap<Integer, ISerializableLabelBean>();
		for (Iterator<ISerializableLabelBean> iterator = externalList.iterator(); iterator.hasNext();) {
			ISerializableLabelBean serializableLabelBean = iterator.next();
			Integer objectID = serializableLabelBean.getObjectID();
			externalMap.put(objectID, serializableLabelBean);
			IHierarchicalBean hierarchicalBean = (IHierarchicalBean) serializableLabelBean;
			Integer parentID = hierarchicalBean.getParent();
			if (parentID != null && externalProjectIDs.contains(parentID)) {
				// has parentID and the parent project is also present in
				// external list
				List<Integer> children = parentToChildren.get(parentID);
				if (children == null) {
					children = new LinkedList<Integer>();
					parentToChildren.put(parentID, children);
				}
				children.add(objectID);
			} else {
				// add non child projects first
				// (non child even if a parentID is specified but the parent
				// project is not specified in externalList)
				list.add(serializableLabelBean);
				iterator.remove();
			}
		}
		List<ISerializableLabelBean> previousLevel = list;
		while (!parentToChildren.isEmpty()) {
			List<ISerializableLabelBean> nextLevel = new LinkedList<ISerializableLabelBean>();
			for (ISerializableLabelBean serializableLabelBean : previousLevel) {
				Integer objectID = serializableLabelBean.getObjectID();
				List<Integer> children = parentToChildren.get(objectID);
				if (children != null) {
					for (Integer child : children) {
						ISerializableLabelBean childBean = externalMap.get(child);
						nextLevel.add(childBean);
					}
					parentToChildren.remove(objectID);
				}
			}
			previousLevel = nextLevel;
			if (previousLevel.isEmpty()) {
				LOGGER.warn("Wrong hierarchy");
				break;
			}
			list.addAll(previousLevel);
		}
		return list;
	}

	/**
	 * Get the fieldID matches: if needed add new fields and also the default
	 * configs for them
	 * 
	 * @param externalFieldsAndFieldConfigs
	 * @return
	 */
	public static Map<Integer, Integer> getFieldMatchMap(List<ISerializableLabelBean> externalCustomFieldBeans) throws ImportExceptionList {
		LOGGER.debug("Field matcher started...");
		Map<Integer, Integer> fieldMatches = new HashMap<Integer, Integer>();
		// system fields
		List<TFieldBean> systemFieldsBeans = FieldBL.loadSystem();
		Iterator<TFieldBean> itrSystemFields = systemFieldsBeans.iterator();
		while (itrSystemFields.hasNext()) {
			TFieldBean fieldBean = itrSystemFields.next();
			Integer fieldID = fieldBean.getObjectID();
			// for system fieldIDs same fieldID for internal and external
			// fields, without any comparison
			fieldMatches.put(fieldID, fieldID);
		}
		// custom fields
		List<ISerializableLabelBean> internalCustomFieldBeans = (List) FieldBL.loadCustom();
		if (externalCustomFieldBeans == null) {
			return fieldMatches;
		}
		if (internalCustomFieldBeans == null) {
			internalCustomFieldBeans = new ArrayList<ISerializableLabelBean>();
		}
		Map<String, Map<Integer, Integer>> matcherMap = new HashMap<String, Map<Integer, Integer>>();
		matcherMap.put(ExchangeFieldNames.FIELD, fieldMatches);
		Map<Integer, Integer> fieldMatcherMap = addEntityMatchMap(externalCustomFieldBeans, internalCustomFieldBeans, ExchangeFieldNames.FIELD, matcherMap);
		LOGGER.debug("Field matcher done");
		return fieldMatcherMap;
	}

	/**
	 * Load the internal dropdowns based on external dropdowns TODO load not all
	 * dropdowns but based on extarnalDropdowns
	 * 
	 * @param externalDropDowns
	 * @return
	 */
	private static Map<String, List<ISerializableLabelBean>> getInternalDropdowns() {
		Map<String, List<ISerializableLabelBean>> internalDropDowns = new HashMap<String, List<ISerializableLabelBean>>();
		internalDropDowns.put(MergeUtil.mergeKey(SystemFields.INTEGER_ISSUETYPE, null), (List) IssueTypeBL.loadAll());
		internalDropDowns.put(MergeUtil.mergeKey(SystemFields.INTEGER_STATE, null), (List) StatusBL.loadAll());
		internalDropDowns.put(MergeUtil.mergeKey(SystemFields.INTEGER_PRIORITY, null), (List) PriorityBL.loadAll());
		internalDropDowns.put(MergeUtil.mergeKey(SystemFields.INTEGER_SEVERITY, null), (List) SeverityBL.loadAll());
		internalDropDowns.put(MergeUtil.mergeKey(SystemFields.INTEGER_PERSON, null), (List) PersonBL.loadPersonsAndGroups());
		internalDropDowns.put(MergeUtil.mergeKey(SystemFields.INTEGER_PROJECT, null), (List) ProjectBL.loadAll());
		internalDropDowns.put(MergeUtil.mergeKey(SystemFields.INTEGER_RELEASE, null), (List) ReleaseBL.loadAll());
		internalDropDowns.put(ExchangeFieldNames.ACCOUNT, (List) AccountBL.getAllAccounts());
		internalDropDowns.put(ExchangeFieldNames.COSTCENTER, (List) AccountBL.getAllCostcenters());
		internalDropDowns.put(ExchangeFieldNames.DEPARTMENT, (List) DepartmentBL.getAllDepartments());
		internalDropDowns.put(ExchangeFieldNames.SYSTEMSTATE, (List) SystemStatusBL.loadAll());
		internalDropDowns.put(ExchangeFieldNames.PROJECTTYPE, (List) ProjectTypesBL.loadAll());
		internalDropDowns.put(ExchangeFieldNames.LIST, (List) ListBL.loadAll());
		internalDropDowns.put(ExchangeFieldNames.ROLE, (List) RoleBL.loadAll());
		return internalDropDowns;
	}

}
