/**
 * Genji Scrum Tool and Issue Tracker
 * Copyright (C) 2015 Steinbeis GmbH & Co. KG Task Management Solutions

 * <a href="http://www.trackplus.com">Genji Scrum Tool</a>

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/* $Id:$ */

package com.aurel.track.item.history;

import java.util.ArrayList;
import java.util.Collections;
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
import java.util.TreeMap;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.projectType.ProjectTypesBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldConfigBL;
import com.aurel.track.beans.TActualEstimatedBudgetBean;
import com.aurel.track.beans.TBudgetBean;
import com.aurel.track.beans.TCostBean;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TFieldChangeBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.THistoryTransactionBean;
import com.aurel.track.beans.TProjectTypeBean;
import com.aurel.track.beans.TRoleFieldBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.LocalLookupContainer;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemDetailBL;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.item.budgetCost.AccountingBL;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.HTMLDiff;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.emailHandling.Html2Text;
import com.aurel.track.util.numberFormatter.DoubleNumberFormatUtil;

/**
 * a business logic class for history
 */
public class HistoryLoaderBL {
	private static final Logger LOGGER = LogManager.getLogger(HistoryLoaderBL.class);	
	
	public static enum LONG_TEXT_TYPE { ISPLAIN, ISSIMPLIFIEDHTML, ISFULLHTML };
	public static String COMMON_FIELD_KEY = "common.lbl.common";
	public static String ATTACHMENT_ADDED_FIELD_KEY = "common.lbl.attachment.added";
	public static String ATTACHMENT_DELETED_FIELD_KEY = "common.lbl.attachment.deleted";
	public static String ATTACHMENT_MODIFIED_FIELD_KEY = "common.lbl.attachment.modified";
	public static String ATTACHMENT_FILE = "common.lbl.attachment.file";
	public static String ATTACHMENT_URL = "URL";
	public static String ATTACHMENT_DESCRIPTION = "common.lbl.attachment.description";
	public static String ATTACHMENT_SIZE = "common.lbl.size";
	public static String BUDGET = "common.lbl.budget";
	public static String PLAN = "common.lbl.plan";
	public static String COST = "common.lbl.cost";
	
	public static String VERSION_CONTROL = "common.lbl.versionControl";
	
	public static String COMMENT_DELETED_FIELD_KEY = "common.lbl.comment.deleted";
	public static String COMMENT_MODIFIED_FIELD_KEY = "common.lbl.comment.modified";
	
	
	/**
	 * Gets the possible change types
	 * @param locale
	 * @return
	 */
	public static List<IntegerStringBean> getPossibleHistoryFields(Locale locale) {
		List<IntegerStringBean> historyFields = new LinkedList<IntegerStringBean>();
		//all fields with explicit history
		Map<Integer, String> historyFieldLabelsMap = FieldConfigBL.loadAllWithExplicitHistory(locale);
		for (Map.Entry<Integer, String> historyFieldEntry : historyFieldLabelsMap.entrySet()) {
			historyFields.add(new IntegerStringBean(historyFieldEntry.getValue(), historyFieldEntry.getKey()));
		}
		//compound fields (string value concatenated of all fields without explicit history)
		historyFields.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(HistoryLoaderBL.COMMON_FIELD_KEY, locale), TFieldChangeBean.COMPOUND_HISTORY_FIELD));
		//attachment changes
		historyFields.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(HistoryLoaderBL.ATTACHMENT_ADDED_FIELD_KEY, locale), SystemFields.ATTACHMENT_ADD_HISTORY_FIELD));
		historyFields.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(HistoryLoaderBL.ATTACHMENT_MODIFIED_FIELD_KEY, locale), SystemFields.ATTACHMENT_MODIFY_HISTORY_FIELD));
		historyFields.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(HistoryLoaderBL.ATTACHMENT_DELETED_FIELD_KEY, locale), SystemFields.ATTACHMENT_DELETE_HISTORY_FIELD));
		//comment changes
		if (historyFieldLabelsMap.containsKey(SystemFields.INTEGER_COMMENT)) {
			//comment is historized
			historyFields.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(HistoryLoaderBL.COMMENT_MODIFIED_FIELD_KEY, locale), SystemFields.COMMENT_MODIFY_HISTORY_FIELD));
			historyFields.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(HistoryLoaderBL.COMMENT_DELETED_FIELD_KEY, locale), SystemFields.COMMENT_DELETE_HISTORY_FIELD));
		} else {
			//comment is not historized but that means that modification and delete of comments is not historized. Adding a comment should always appear in history
			TFieldConfigBean fieldConfigBean = FieldRuntimeBL.getDefaultFieldConfig(SystemFields.INTEGER_COMMENT, locale);
			historyFields.add(new IntegerStringBean(fieldConfigBean.getLabel(), SystemFields.INTEGER_COMMENT));
		}
		List<TProjectTypeBean> projectBeans = ProjectTypesBL.loadAll();
		boolean planBudgetExpense = false;
		boolean versionControl = false;
		if (projectBeans!=null) {
			for (TProjectTypeBean projectTypeBean : projectBeans) {
				if (projectTypeBean.getUseAccounting()) {
					planBudgetExpense = true;
				}
				if (projectTypeBean.getUseVersionControl()) {
					versionControl = true;
				}
				if (planBudgetExpense && versionControl) {
					break;
				}
			}
		}
		if (!ApplicationBean.getApplicationBean().isGenji() && planBudgetExpense) {
			historyFields.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(HistoryLoaderBL.COST, locale), SystemFields.INTEGER_COST_HISTORY));
			historyFields.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(HistoryLoaderBL.PLAN, locale), SystemFields.INTEGER_PLAN_HISTORY));
			if (ApplicationBean.getApplicationBean().getSiteBean().getSummaryItemsBehavior()) {
				historyFields.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(HistoryLoaderBL.BUDGET, locale), SystemFields.INTEGER_BUDGET_HISTORY));
			}
		}
		if (versionControl) {
			historyFields.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(HistoryLoaderBL.VERSION_CONTROL, locale), SystemFields.INTEGER_VERSION_CONTROL));
		}
		Collections.sort(historyFields);
		return historyFields;
	}
	
	/**
	 * Add the names of the changed by persons 
	 * @param historyTransactionBeanList
	 */
	public static void addPersonNamesToHistoryTransactionBeans(List<THistoryTransactionBean> historyTransactionBeanList) {
		if (historyTransactionBeanList!=null) {
			/*Set<Integer> personsSet = new HashSet<Integer>();
			for (THistoryTransactionBean historyTransactionBean : historyTransactionBeanList) {
				personsSet.add(historyTransactionBean.getChangedByID());
			}
			List<TPersonBean> personBeansList = PersonBL.loadByKeys(
					GeneralUtils.createIntegerListFromCollection(personsSet));
			Map<Integer, String> personIDToName = new HashMap<Integer, String>();
			for (TPersonBean personBean : personBeansList) {
				personIDToName.put(personBean.getObjectID(), personBean.getLabel());
			}*/
			for (THistoryTransactionBean historyTransactionBean : historyTransactionBeanList) {
				Integer personID = historyTransactionBean.getChangedByID();
				historyTransactionBean.setChangedByName(LookupContainer.getNotLocalizedLabelBeanLabel(SystemFields.INTEGER_PERSON, personID));
			}
		}
	}
	
	/**
	 * Get the raw history (only the values, no show values, field names) for a workItemID
	 * @param workItemID
	 * @param filterFieldIDs
	 * @param personID
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public static SortedMap<Integer, Map<Integer, HistoryValues>> getWorkItemRawHistory(
			Integer workItemID, Integer[] filterFieldIDs, List<Integer> personIDs, 
			Date fromDate, Date toDate) {
		
		int[] workItemIDs = new int[] {workItemID};
		List<THistoryTransactionBean> historyTransactionBeanList = HistoryTransactionBL.getByWorkItemsAndFields(
				workItemIDs, filterFieldIDs, true, personIDs, fromDate, toDate);
		addPersonNamesToHistoryTransactionBeans(historyTransactionBeanList);
		Map<Integer, THistoryTransactionBean> historyTransactionBeanMap =
			GeneralUtils.createMapFromList(historyTransactionBeanList);
		List<TFieldChangeBean> fieldChangeBeanList = 
				FieldChangeBL.getByWorkItemsAndFields(workItemIDs, filterFieldIDs, true, personIDs, fromDate, toDate);
		Map<Integer, Map<Integer, Map<String, Object>>> workItemsFieldChangeBeansMap = 
			getFieldChangeBeansByWorkItemAndTransactionAndMergedField(historyTransactionBeanMap, fieldChangeBeanList);
		Map<Integer, SortedMap<Integer, Map<Integer, HistoryValues>>> workItemsHistoryValuesMap = 
			new HashMap<Integer, SortedMap<Integer, Map<Integer,HistoryValues>>>();
		Iterator<Integer> workItemIDsIterator = workItemsFieldChangeBeansMap.keySet().iterator();
		while (workItemIDsIterator.hasNext()) {
			workItemID = workItemIDsIterator.next();
			Map<Integer, Map<String, Object>> workItemFieldChangeBeansMap = workItemsFieldChangeBeansMap.get(workItemID);
			Iterator<Integer> transactionIDsIterator = workItemFieldChangeBeansMap.keySet().iterator();
			while (transactionIDsIterator.hasNext()) {
				Integer historyTransactionID = transactionIDsIterator.next();
				Map<String, Object> transactionFieldChangeBeansMap = workItemFieldChangeBeansMap.get(historyTransactionID);
				Iterator<String> mergeKeysIterator = transactionFieldChangeBeansMap.keySet().iterator();
				Map<String, Object> newValuesHistoryMap = new HashMap<String, Object>();
				Map<String, Object> oldValuesHistoryMap = new HashMap<String, Object>();
				while (mergeKeysIterator.hasNext()) {
					String mergeKey = mergeKeysIterator.next();
					Integer[] parts = MergeUtil.getParts(mergeKey);
					Integer fieldID = parts[0];
					Integer parameterCode = parts[1];
					if (parameterCode==null || parameterCode.intValue()==1) {
						IFieldTypeRT fieldTypeRT =null;
						if (TFieldChangeBean.COMPOUND_HISTORY_FIELD.equals(fieldID)) {
							//load the Compound field is loaded similar like Description (longText)  
							fieldTypeRT = FieldTypeManager.getFieldTypeRT(SystemFields.INTEGER_DESCRIPTION);
						} else {
							fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
						}
						if (fieldTypeRT==null) {
							LOGGER.info("Fieldtype unknown for field " + fieldID);
							continue;
						}
						fieldTypeRT.processHistoryLoad(fieldID, null, transactionFieldChangeBeansMap, newValuesHistoryMap, oldValuesHistoryMap);						
						SortedMap<Integer, Map<Integer, HistoryValues>> workItemHistoryValuesMap = workItemsHistoryValuesMap.get(workItemID);
						if (workItemHistoryValuesMap==null) {
							workItemHistoryValuesMap = new TreeMap<Integer, Map<Integer, HistoryValues>>();
							workItemsHistoryValuesMap.put(workItemID, workItemHistoryValuesMap);
						}
						Map<Integer, HistoryValues> historyTransactionMap = workItemHistoryValuesMap.get(historyTransactionID);
						if (historyTransactionMap==null) {
							historyTransactionMap = new TreeMap<Integer, HistoryValues>();
							workItemHistoryValuesMap.put(historyTransactionID, historyTransactionMap);
						}
						HistoryValues historyValues = initHistoryValues(historyTransactionBeanMap.get(historyTransactionID));
						historyValues.setDate(fieldTypeRT.getValueType()==ValueType.DATE || 
								fieldTypeRT.getValueType()==ValueType.DATETIME);
						historyValues.setObjectID(getFirstFieldChangeID(
								fieldID, parameterCode, transactionFieldChangeBeansMap.get(mergeKey)));
						historyValues.setTimesEdited(getTimesEdited(fieldID, transactionFieldChangeBeansMap.get(mergeKey)));
						historyValues.setTransactionID(historyTransactionID);
						historyValues.setFieldID(fieldID);
						historyValues.setLongField(fieldTypeRT.isLong());
						Object newValue = getAttribute(newValuesHistoryMap, fieldID, fieldTypeRT);
						Object oldValue = getAttribute(oldValuesHistoryMap, fieldID, fieldTypeRT);
						historyValues.setNewValue(newValue);
						historyValues.setOldValue(oldValue);
						historyTransactionMap.put(fieldID, historyValues);
					}
				}
			}
		}
		return workItemsHistoryValuesMap.get(workItemID);
	}
	
	/**
	 * Dropdowns are needed for system and custom options 
	 * @param includeField
	 * @param filterFieldIDs
	 * @return
	 */
	/*private static boolean dropDownNeeded(boolean includeField, Integer[] filterFieldIDs) {
		if (filterFieldIDs==null || !includeField) {
			//if all fields the included dropDowns are needed
			return true;
		}
		for (int i = 0; i < filterFieldIDs.length; i++) {
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(filterFieldIDs[i]);
			if (fieldTypeRT!=null) {
				int valueType = fieldTypeRT.getValueType();
				//dropdown label or only the locale needed from dropdown container
				if (valueType==ValueType.SYSTEMOPTION || valueType==ValueType.CUSTOMOPTION || 
						valueType==ValueType.DATE || valueType==ValueType.DATETIME || 
						valueType==ValueType.DOUBLE) {
					return true;
				}
			}
		}
		//none of the fields 
		return false;
	}*/
	
	/**
	 * Gets the list of comments as history values for each item
	 * @param workItemIDs
	 * @param personID
	 * @param locale
	 * @return
	 */
	public static Map<Integer, List<HistoryValues>> getComments(
			int[] workItemIDs, Integer personID, Locale locale) {
		Map<Integer, List<HistoryValues>> commentMap = new HashMap<Integer, List<HistoryValues>>();
		Map<Integer, SortedMap<Integer, Map<Integer, HistoryValues>>> historyMap = getWorkItemsHistory(
					workItemIDs, new Integer[] {SystemFields.INTEGER_COMMENT}, true, null, null, null, locale, false, LONG_TEXT_TYPE.ISFULLHTML, true, personID);
		if (historyMap!=null) {
			for (Map.Entry<Integer, SortedMap<Integer, Map<Integer, HistoryValues>>> itemEntry: historyMap.entrySet()) {
				Integer itemID = itemEntry.getKey();
				SortedMap<Integer, Map<Integer, HistoryValues>> commentTransaction = itemEntry.getValue();
				if (commentTransaction!=null) {
					for (Map.Entry<Integer,  Map<Integer, HistoryValues>> transactionEntry : commentTransaction.entrySet()) {
						Map<Integer, HistoryValues> commentFieldEntry = transactionEntry.getValue();
						if (commentFieldEntry!=null) {
							for (Map.Entry<Integer, HistoryValues> commentEntry : commentFieldEntry.entrySet()) {
								HistoryValues historyValues = commentEntry.getValue();
								if (historyValues!=null) {
									List<HistoryValues> commentsForItem = commentMap.get(itemID);
									if (commentsForItem==null) {
										commentsForItem = new LinkedList<HistoryValues>();
										commentMap.put(itemID, commentsForItem);
									}
									commentsForItem.add(historyValues);
								}
							}
						}
					}
				}
			}
		}
		return commentMap;
	}
	
	/**
	 * Get the history for an array of workItemIDs
	 * @param workItemIDs
	 * @param filterFieldIDs
	 * @param includeField
	 * @param filterHistoryPersonIDs
	 * @param fromDate
	 * @param toDate
	 * @param locale
	 * @param isISO
	 * @param longTextIsPlain
	 * @param excludeHidden
	 * @param loggedPersonID
	 * @return
	 */
	public static Map<Integer, SortedMap<Integer, Map<Integer, HistoryValues>>> getWorkItemsHistory(
			int[] workItemIDs, Integer[] filterFieldIDs, boolean includeField, List<Integer> filterHistoryPersonIDs, 
			Date fromDate, Date toDate, Locale locale, boolean isISO, LONG_TEXT_TYPE longTextIsPlain, boolean excludeHidden, Integer loggedPersonID) {
		List<THistoryTransactionBean> historyTransactionBeanList = HistoryTransactionBL.getByWorkItemsAndFields(
				workItemIDs, filterFieldIDs, includeField, filterHistoryPersonIDs, fromDate, toDate);
		List<TFieldChangeBean> fieldChangeBeanList = 
				FieldChangeBL.getByWorkItemsAndFields(workItemIDs, filterFieldIDs, includeField, filterHistoryPersonIDs, fromDate, toDate);
		Integer[] accessFields = null;
		if (includeField) {
			accessFields = filterFieldIDs;
		}
		return getWorkItemsHistory(historyTransactionBeanList, fieldChangeBeanList, accessFields, filterHistoryPersonIDs, locale, isISO, longTextIsPlain, excludeHidden, loggedPersonID);
	}
	
	/**
	 * Get the HistoryValues map for a list of of historyTransactions and the corresponding fieldChangeBeanList
	 * @param workItemIDs
	 * @param filterFieldID if null do not filter by it
	 * @param includeField not null: if true include this field, if false exclude this field
	 * @param filterHistoryPersonID if null do not filter by it
	 * @param fromDate
	 * @param toDate
	 * @param locale
	 * @param isISO
	 * @param longTextIsPlain
	 * @param excludeHidden exclude the fields not visible by current user
	 * @param loggedPersonID
	 * @return
	 */
	public static Map<Integer, SortedMap<Integer, Map<Integer, HistoryValues>>> getWorkItemsHistory(
			List<THistoryTransactionBean> historyTransactionBeanList, List<TFieldChangeBean> fieldChangeBeanList,
			Integer[] filterFieldIDs, List<Integer> filterHistoryPersonIDs, 
			Locale locale, boolean isISO, LONG_TEXT_TYPE longTextIsPlain, boolean excludeHidden, Integer loggedPersonID) {
		Date start = null;
		Date loadingHistoryData = null;
		Date populatingHistoryData = null;
		if (LOGGER.isDebugEnabled()) {
			start = new Date();
		}
		Map<Integer, Map<Integer, Map<Integer, Integer>>> fieldRestrictions = null;
		Map<Integer, TWorkItemBean> workItemBeansMap = null;
		Set<Integer> itemIDSet = new HashSet<Integer>();
		
		if (historyTransactionBeanList!=null) {
			for (THistoryTransactionBean historyTransactionBean : historyTransactionBeanList) {
				Integer workItemID = historyTransactionBean.getWorkItem();
				itemIDSet.add(workItemID);
			}
		}
		List<TWorkItemBean> workItemBeansList = ItemBL.loadByWorkItemKeys(GeneralUtils.createIntArrFromSet(itemIDSet));
		LocalLookupContainer localLookupContainer = ItemBL.getItemHierarchyContainer(workItemBeansList);
		if (excludeHidden) {
			workItemBeansMap = GeneralUtils.createMapFromList(workItemBeansList);
			Map<Integer, Set<Integer>> projectToIssueTypesMap = AccessBeans.getProjectToIssueTypesMap(workItemBeansList);
			List<Integer> fieldIDs = null;
			if (filterFieldIDs!=null && filterFieldIDs.length>0) {
				fieldIDs = GeneralUtils.createIntegerListFromIntegerArr(filterFieldIDs);
			}
			fieldRestrictions = AccessBeans.getFieldRestrictions(loggedPersonID, projectToIssueTypesMap, fieldIDs, false);
			if (fieldRestrictions!=null) {
				//whether the Common field from history should be restricted or not. This is a conservative restriction:
				//if any field which is restricted has not explicit history set (i.e. it might appear in Common changes) then the Common field is hidden
				Set<Integer> restrictedFieldsInAnyContext = new HashSet<Integer>();
				for (Map<Integer, Map<Integer, Integer>> projectFieldRestrictions : fieldRestrictions.values()) {
					if (projectFieldRestrictions!=null) {
						for (Map<Integer, Integer> itemTypeFieldRestrictions : projectFieldRestrictions.values()) {
							if (itemTypeFieldRestrictions!=null) {
								for (Integer fieldID : itemTypeFieldRestrictions.keySet()) {
									restrictedFieldsInAnyContext.add(fieldID);
								}
							}
						}
					}
				}
				if (!restrictedFieldsInAnyContext.isEmpty()) {
					LOGGER.debug("Field restrictions found for person " + loggedPersonID + " for " + restrictedFieldsInAnyContext.size() + " fields");
					Map<Integer, Map<Integer, Map<Integer, TFieldConfigBean>>> projectsIssueTypesFieldConfigsMap =
						FieldRuntimeBL.loadFieldConfigsInContextsAndTargetProjectAndIssueType(
								projectToIssueTypesMap, restrictedFieldsInAnyContext, locale, null, null);
					for (Map.Entry<Integer, Map<Integer, Map<Integer, Integer>>> projectFieldRestrictionsEntry : fieldRestrictions.entrySet()) {
						Integer projectID = projectFieldRestrictionsEntry.getKey();
						Map<Integer, Map<Integer, Integer>> projectFieldRestrictions = projectFieldRestrictionsEntry.getValue();
						Map<Integer, Map<Integer, TFieldConfigBean>> projectFieldConfigs = projectsIssueTypesFieldConfigsMap.get(projectID);
						if (projectFieldConfigs!=null && projectFieldRestrictions!=null) {
							for (Map.Entry<Integer, Map<Integer, Integer>> itemTypeFieldRestrictionsEntry : projectFieldRestrictions.entrySet()) {
								Integer itemType = itemTypeFieldRestrictionsEntry.getKey();
								Map<Integer, Integer> itemTypeFieldRestrictions = itemTypeFieldRestrictionsEntry.getValue();
								Map<Integer, TFieldConfigBean> itemTypeFieldConfigs = projectFieldConfigs.get(itemType);
								if (itemTypeFieldConfigs!=null && itemTypeFieldRestrictions!=null) {
									for (Integer fieldID : itemTypeFieldRestrictions.keySet()) {
										TFieldConfigBean fieldConfigBean = itemTypeFieldConfigs.get(fieldID);
										if (fieldConfigBean!=null && !fieldConfigBean.isHistoryString()) {
											LOGGER.debug("The restricted field " + fieldConfigBean.getLabel() + " (" + fieldID + ") has no explict history so the Commons history field is also disabled");
											itemTypeFieldRestrictions.put(TFieldChangeBean.COMPOUND_HISTORY_FIELD, TRoleFieldBean.ACCESSFLAG.NOACCESS);
											break;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		Map<Integer, TFieldBean> fieldsMap = new HashMap<Integer, TFieldBean>(1);
		Map<Integer, String> fieldConfigLabelsMap = new HashMap<Integer, String>(1);
		if (isISO) {
			fieldsMap = GeneralUtils.createMapFromList(FieldBL.loadAll());
		} else {
			fieldConfigLabelsMap = LocalizeUtil.getLocalizedFieldConfigLables(FieldConfigBL.loadDefault(), locale);
		}
		/*List<THistoryTransactionBean> historyTransactionBeanList = HistoryTransactionBL.getByWorkItemsAndFields(
				workItemIDs, filterFieldIDs, includeField, filterHistoryPersonIDs, fromDate, toDate);*/
		addPersonNamesToHistoryTransactionBeans(historyTransactionBeanList);
		Map<Integer, THistoryTransactionBean> historyTransactionBeanMap =
			GeneralUtils.createMapFromList(historyTransactionBeanList);
		/*List<TFieldChangeBean> fieldChangeBeanList = 
				FieldChangeBL.getByWorkItemsAndFields(workItemIDs, filterFieldIDs, includeField, filterHistoryPersonIDs, fromDate, toDate);*/
		if (LOGGER.isDebugEnabled() && start!=null) {
			loadingHistoryData = new Date();
			LOGGER.debug("Loading the history values from database for " + 
					" ReportBeanWithHistory lasted " + Long.toString(loadingHistoryData.getTime()-start.getTime()) + " ms");
		}
		Map<Integer, Map<Integer, Map<String, Object>>> workItemsFieldChangeBeansMap = getFieldChangeBeansByWorkItemAndTransactionAndMergedField(historyTransactionBeanMap, fieldChangeBeanList);		
		Map<Integer, SortedMap<Integer, Map<Integer, HistoryValues>>> workItemsHistoryValuesMap = 
			new HashMap<Integer, SortedMap<Integer, Map<Integer,HistoryValues>>>();
		Set<Integer> pseudoHistoryFields = getPseudoHistoryFields();
		for (Map.Entry<Integer, Map<Integer, Map<String, Object>>> entry : workItemsFieldChangeBeansMap.entrySet()) {
			Integer workItemID = entry.getKey();
			Map<Integer, Map<String, Object>> workItemFieldChangeBeansMap = entry.getValue();
			Map<Integer, Integer> issueTypeFieldRestrictions = null; 
			Integer projectID = null;
			Integer issueTypeID = null;
			if (fieldRestrictions!=null && workItemBeansMap!=null) {
				TWorkItemBean workItemBean = workItemBeansMap.get(workItemID);
				if (workItemBean!=null) {
					projectID = workItemBean.getProjectID();
					issueTypeID = workItemBean.getListTypeID();
					Map<Integer, Map<Integer, Integer>> projectFieldRestrictions = fieldRestrictions.get(projectID);
					if (projectFieldRestrictions!=null) {
						issueTypeFieldRestrictions = projectFieldRestrictions.get(issueTypeID);
					}
				}
			}
			for (Map.Entry<Integer, Map<String, Object>> itemEntry : workItemFieldChangeBeansMap.entrySet()) {
				Integer historyTransactionID = itemEntry.getKey();
				Map<String, Object> transactionFieldChangeBeansMap = itemEntry.getValue();
				Iterator<String> mergeKeysIterator = transactionFieldChangeBeansMap.keySet().iterator();
				Map<String, Object> newValuesHistoryMap = new HashMap<String, Object>();
				Map<String, Object> oldValuesHistoryMap = new HashMap<String, Object>();
				while (mergeKeysIterator.hasNext()) {
					String mergeKey = mergeKeysIterator.next();
					Integer[] parts = MergeUtil.getParts(mergeKey);
					Integer fieldKey = parts[0];
					if (issueTypeFieldRestrictions!=null && issueTypeFieldRestrictions.containsKey(fieldKey)) {
						LOGGER.debug("Field " + fieldKey + " is hidden for person " + loggedPersonID +
								" in project "+ projectID + " and issueType " + issueTypeID);
						continue;
					}
					Integer parameterCode = parts[1];
					if (parameterCode==null || parameterCode.intValue()==1) {
						//execute for simple fields and one times for the composite fields (not for each part)
						IFieldTypeRT fieldTypeRT = null;
						if (pseudoHistoryFields.contains(fieldKey)) {
							//load the Compound and Attachment fields the same as Description (longText)  
							fieldTypeRT = FieldTypeManager.getFieldTypeRT(SystemFields.INTEGER_DESCRIPTION);
						} else {
							fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldKey);
						}
						if (fieldTypeRT==null) {
							LOGGER.warn("Fieldtype unknown for field " + fieldKey);
							continue;
						}
						fieldTypeRT.processHistoryLoad(fieldKey, null, transactionFieldChangeBeansMap, newValuesHistoryMap, oldValuesHistoryMap);
						SortedMap<Integer, Map<Integer, HistoryValues>>  workItemHistoryValuesMap = workItemsHistoryValuesMap.get(workItemID);
						if (workItemHistoryValuesMap==null) {
							workItemHistoryValuesMap = new TreeMap<Integer, Map<Integer, HistoryValues>>();
							workItemsHistoryValuesMap.put(workItemID, workItemHistoryValuesMap);
						}
						Map<Integer, HistoryValues> historyTransactionMap = workItemHistoryValuesMap.get(historyTransactionID);
						if (historyTransactionMap==null) {
							historyTransactionMap = new TreeMap<Integer, HistoryValues>();
							workItemHistoryValuesMap.put(historyTransactionID, historyTransactionMap);
						}
						HistoryValues historyValues = initHistoryValues(historyTransactionBeanMap.get(historyTransactionID));
						historyValues.setDate(fieldTypeRT.getValueType()==ValueType.DATE || 
								fieldTypeRT.getValueType()==ValueType.DATETIME);
						historyValues.setObjectID(getFirstFieldChangeID(
								fieldKey, parameterCode, transactionFieldChangeBeansMap.get(mergeKey)));
						historyValues.setTimesEdited(getTimesEdited(fieldKey, transactionFieldChangeBeansMap.get(mergeKey)));
						historyValues.setTransactionID(historyTransactionID);
						historyValues.setFieldID(fieldKey);
						historyValues.setLongField(fieldTypeRT.isLong());
						if (isISO) {
							TFieldBean fieldBean = fieldsMap.get(fieldKey);
							if (fieldBean!=null) {
								historyValues.setFieldName(fieldBean.getName());
							} else {
								if (pseudoHistoryFields.contains(fieldKey)) {
									historyValues.setFieldName(LocalizeUtil.getLocalizedTextFromApplicationResources(getHistoryFieldKey(fieldKey), Locale.ENGLISH));
								}
							}
						} else {
							String fieldConfigLabel = fieldConfigLabelsMap.get(fieldKey);
							if (fieldConfigLabel!=null) {
								historyValues.setFieldName(fieldConfigLabel);
							} else {
								if (pseudoHistoryFields.contains(fieldKey)) {
									historyValues.setFieldName(LocalizeUtil.getLocalizedTextFromApplicationResources(getHistoryFieldKey(fieldKey), locale));
								}
							}
						}
						Object newValue = getAttribute(newValuesHistoryMap, fieldKey, fieldTypeRT);
						Object oldValue = getAttribute(oldValuesHistoryMap, fieldKey, fieldTypeRT);
						historyValues.setNewValue(newValue);
						historyValues.setOldValue(oldValue);
						if (fieldTypeRT.isLong() && (longTextIsPlain==LONG_TEXT_TYPE.ISPLAIN || longTextIsPlain==LONG_TEXT_TYPE.ISFULLHTML)) {
							if (longTextIsPlain==LONG_TEXT_TYPE.ISPLAIN) {
								try {
									historyValues.setNewShowValue(Html2Text.getNewInstance().convert((String)newValue));
								} catch (Exception e) {
									LOGGER.info("Transforming the new HTML value to plain text for workItemID " + workItemID + " and field " +
											fieldKey + " failed with " + e);
								}
								try {
									historyValues.setOldShowValue(Html2Text.getNewInstance().convert((String)oldValue));
								} catch (Exception e) {
									LOGGER.info("Transforming the old HTML value to plain text for workItemID " + workItemID + " and field " +
											fieldKey + " failed with " + e);
								}
							} else {
								//leave the full HTML as it is
								historyValues.setNewShowValue((String)newValue);
								historyValues.setOldShowValue((String)oldValue);
							}
							//historyValues.setFieldName("");
						} else {
							String newShowValue;
							String oldShowValue;
							if (isISO) {
								newShowValue = fieldTypeRT.getShowISOValue(fieldKey, 
										null, newValue, workItemID, localLookupContainer, locale);
								oldShowValue = fieldTypeRT.getShowISOValue(fieldKey, 
										null, oldValue, workItemID, localLookupContainer, locale);
							} else {
								//item hierarchy container may be null because changing the
								//fields depending on that (wbs or project specific ID) is not historized
								newShowValue = fieldTypeRT.getShowValue(fieldKey, 
										null, newValue, workItemID, null, locale);
								oldShowValue = fieldTypeRT.getShowValue(fieldKey, 
										null, oldValue, workItemID, null, locale);
							}
							historyValues.setNewShowValue(newShowValue);
							historyValues.setOldShowValue(oldShowValue);
						}
						historyTransactionMap.put(fieldKey, historyValues);
					}
				}
			}
		}
		if (LOGGER.isDebugEnabled() && loadingHistoryData!=null) {
			populatingHistoryData = new Date();
			LOGGER.debug("Populating the history values for " +
					" ReportBeanWithHistory lasted " +  Long.toString(populatingHistoryData.getTime()-loadingHistoryData.getTime())+ " ms");
		}
		return workItemsHistoryValuesMap;
	}

	/**
	 * Gets the raw TFieldChangeBean by workItemID, transactionID and fieldID_parametercode
	 * Object can be a  TFieldChangeBean or a  List of TFieldChangeBean (for multiple values)
	 * @param workItemIDs
	 * @param filterFieldIDs
	 * @param includeField
	 * @param personID
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	private static Map<Integer, Map<Integer, Map<String, Object>>> getFieldChangeBeansByWorkItemAndTransactionAndMergedField(
			Map<Integer, THistoryTransactionBean> historyTransactionBeansMap, List<TFieldChangeBean> fieldChangeBeansList) {
		Map<Integer, Map<Integer, Map<String, Object>>> workItemsFieldChangeBeansMap = 
			new HashMap<Integer, Map<Integer, Map<String,Object>>>();
		Iterator<TFieldChangeBean> fieldChangeBeanItr = fieldChangeBeansList.iterator();
		//field changes map: key -> fieldID_parameterCode value -> TFieldChangeBean or list of fieldChange beans (multiple value)
		Map<String, Object> transactionFieldChangeBeansMap;
		//first load the workItemFieldChangeBeansMap entirely with TFieldChangeBeans (or list of TFieldChangeBeans)
		//to be sure that by processHistoryLoad (the second loop) the transactionFieldChangeBeansMap
		//is fully loaded (because of the composite and multiple fields)
		while (fieldChangeBeanItr.hasNext()) {
			TFieldChangeBean fieldChangeBean = fieldChangeBeanItr.next();
			Integer historyTransactionID = fieldChangeBean.getHistoryTransaction();
			THistoryTransactionBean historyTransactionBean = historyTransactionBeansMap.get(historyTransactionID);
			if (historyTransactionBean!=null) {
				Integer workItemID = historyTransactionBean.getWorkItem();
				Map<Integer, Map<String, Object>> workItemFieldChangeBeansMap = workItemsFieldChangeBeansMap.get(workItemID);
				if (workItemFieldChangeBeansMap==null) {
					workItemFieldChangeBeansMap = new HashMap<Integer, Map<String,Object>>();
					workItemsFieldChangeBeansMap.put(workItemID, workItemFieldChangeBeansMap);
				}
				transactionFieldChangeBeansMap = workItemFieldChangeBeansMap.get(historyTransactionID);
				if (transactionFieldChangeBeansMap==null) {
					transactionFieldChangeBeansMap = new HashMap<String, Object>();
					workItemFieldChangeBeansMap.put(historyTransactionID, transactionFieldChangeBeansMap);
				}
				loadFieldChangeBean(fieldChangeBean, transactionFieldChangeBeansMap);
			}
		}
		return workItemsFieldChangeBeansMap;
	}
	
	/**
	 * Add the multiple values in the map as a list of fieldChangeBeans, and the single values directly in the map 
	 * @param fieldChangeBean
	 * @param transactionFieldChangeBeansMap
	 */
	private static void loadFieldChangeBean(TFieldChangeBean fieldChangeBean, Map<String, Object> transactionFieldChangeBeansMap) {
		Integer fieldKey = fieldChangeBean.getFieldKey();
		Integer parameterCode = fieldChangeBean.getParameterCode();	
		String mergeKey = MergeUtil.mergeKey(fieldKey, parameterCode);
		IFieldTypeRT fieldTypeRT = null;
		Set<Integer> pseudoHistoryFields = HistoryLoaderBL.getPseudoHistoryFields();
		if (!pseudoHistoryFields.contains(fieldKey)) {
			//do not take compound field types bust just simple field types or compound components field types!
			fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldKey, parameterCode);
		}
		if (fieldTypeRT!=null && fieldTypeRT.isMultipleValues()) {
			List multipleValues = (List)transactionFieldChangeBeansMap.get(mergeKey);
			if (multipleValues==null) {
				multipleValues = new ArrayList();
				transactionFieldChangeBeansMap.put(mergeKey, multipleValues);
			}
			multipleValues.add(fieldChangeBean);
		} else {
			transactionFieldChangeBeansMap.put(mergeKey, fieldChangeBean);
		}
	}
	
	
	/**
	 * Get the object id of the TFieldChangeBean
	 * @param fieldKey
	 * @param parameterCode
	 * @param fieldChangeObject TFieldChangeBean or list of TFieldChangeBeans
	 * @return
	 */
	private static Integer getFirstFieldChangeID(Integer fieldKey, Integer parameterCode, Object fieldChangeObject) {
		if (fieldChangeObject==null) {
			return null;
		}
		Set<Integer> pseudoHistoryFields = HistoryLoaderBL.getPseudoHistoryFields();
		TFieldChangeBean fieldChangeBean;
		IFieldTypeRT fieldTypeRT = null;
		if (!pseudoHistoryFields.contains(fieldKey)) {
			//if no system or custom field do not try to get the fieldType because it is a costly operation  
			//do not take compound field types bust just simple field types or compound components field types!
			fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldKey, parameterCode);
		}
		try {
			if (fieldTypeRT!=null && fieldTypeRT.isMultipleValues()) {
				List fieldChangeBeanList = (List)fieldChangeObject;
				if (!fieldChangeBeanList.isEmpty()) {
					fieldChangeBean = (TFieldChangeBean)fieldChangeBeanList.get(0);
					return fieldChangeBean.getObjectID();
				}
			} else {
				//single fields or the common history
				fieldChangeBean = (TFieldChangeBean)fieldChangeObject;
				return fieldChangeBean.getObjectID();
			}
		} catch (Exception e) {
			LOGGER.warn("Getting the objectID of the history entry failed with " + e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * Get the object id of the TFieldChangeBean
	 * @param fieldKey	
	 * @param fieldChangeObject TFieldChangeBean or list of TFieldChangeBeans
	 * @return
	 */
	private static Integer getTimesEdited(Integer fieldKey, Object fieldChangeObject) {
		TFieldChangeBean fieldChangeBean;
		if (SystemFields.INTEGER_COMMENT.equals(fieldKey) && fieldChangeObject!=null) {
			try {
				fieldChangeBean = (TFieldChangeBean)fieldChangeObject;
				return fieldChangeBean.getTimesEdited();
			} catch (Exception e) {
				LOGGER.debug("FieldChangeObject for comment is " + fieldChangeObject.getClass().getName() + " " + e.getMessage(), e);
			}
		}
		return null;
	}
	
	
	/**
	 * Get the history for a field for an array of workItemIDs
	 * @param workItemIDs is null or empty get for all
	 * @param fieldID 
	 * @param longTextIsPlain
	 * @return
	 */
	public static Map<Integer, StringBuffer> getByWorkItemsLongTextField(List<Integer> workItemIDs, 
			Integer fieldID, LONG_TEXT_TYPE longTextIsPlain) {
		Map<Integer, StringBuffer> commentsMap = new HashMap<Integer, StringBuffer>();
		List<IntegerStringBean> commentList = HistoryTransactionBL.getByWorkItemsLongTextField(workItemIDs, fieldID); 
		if (commentList!=null) {
			Iterator<IntegerStringBean> iterator = commentList.iterator();
			while (iterator.hasNext()) {
				IntegerStringBean integerStringBean = iterator.next();
				Integer workItemID = integerStringBean.getValue();
				String newComment = integerStringBean.getLabel();
				if (newComment!=null && !"".equals(newComment)) {
					StringBuffer commentsBuffer = commentsMap.get(workItemID);
					if (commentsBuffer==null) {
						commentsBuffer = new StringBuffer();
						commentsMap.put(workItemID, commentsBuffer);
					}
					String processedText;
					switch (longTextIsPlain) {
					case ISPLAIN:
						try {
								processedText = Html2Text.getNewInstance().convert(newComment);
							} catch (Exception e) {
								processedText = newComment;
							}
						break;
					case ISSIMPLIFIEDHTML:
						try {
								processedText = Html2Text.getCustomInstance().convert(newComment);
							} catch (Exception e) {
								processedText = newComment;
							}
						break;
					default:
						processedText = newComment;
						break;
					}
					commentsBuffer.append(processedText);
				}
			}
		}
		return commentsMap;
	}
	
	/**
	 * Get the history for an array of workItemIDs
	 * @param workItemIDs
	 * @param filterFieldID if null do not filter by it
	 * @param includeField not null: if true include this field, if false exclude this field
	 * @param personBean if null or not external do not filter by it
	 * @param loaderResourceBundleMessages
	 * @param longTextIsPlain
	 * @return
	 */
	public static String getLongTextField(Integer objectID, 
			boolean newValue, LONG_TEXT_TYPE longTextIsPlain) {
		TFieldChangeBean fieldChangeBean = FieldChangeBL.loadByPrimaryKey(objectID);
		String processedText = null;
		String value = null;
		if (newValue) {
			value = fieldChangeBean.getNewLongTextValue();
		} else {
			value = fieldChangeBean.getOldLongTextValue();
		}
		if (fieldChangeBean!=null && value!=null) {
			switch (longTextIsPlain) {
			case ISPLAIN:
				try {
						processedText = Html2Text.getNewInstance().convert(value);
					} catch (Exception e) {
						processedText = value;
					}
				break;
			case ISSIMPLIFIEDHTML:
				try {
						processedText = Html2Text.getCustomInstance().convert(value);
					} catch (Exception e) {
						processedText = value;
					}
				break;
			default:
				processedText = value;
				break;
			}	
		}
		return processedText;
	}
	
	/**
	 * Get the list of HistoryValues from the map of Map<Integer, Map<Integer, HistoryValues>>
	 * @param allHistoryMap
	 * @param showCommentsInHistory whether the comment fields should be melt together with the most specific field 
	 * from the transaction in this case allHistoryMap contains all history values (history and comment) 
	 * @return
	 */
	public static List<HistoryValues> getHistoryValuesList(Map<Integer, 
			Map<Integer, HistoryValues>> allHistoryMap, boolean showCommentsInHistory) {
		List<HistoryValues> historyList=new ArrayList<HistoryValues>();
		if (allHistoryMap!=null) {
			Iterator<Integer> itrHistoryMap=allHistoryMap.keySet().iterator();
			while (itrHistoryMap.hasNext()) {
				Integer transactionID = itrHistoryMap.next();
				Map<Integer, HistoryValues> changesMap=allHistoryMap.get(transactionID);
				Set<Integer> changedFieldIDs = changesMap.keySet();
				Integer mostSpecificField = null;
				String transactionComment = null;
				if (changedFieldIDs.contains(SystemFields.INTEGER_COMMENT)) {
					//the transaction comments are shown in the comment column of the most specific fields's row
					//single comments, like any other explicit field are shown in a separate row but only if showCommentsInHistory = true
					if (changedFieldIDs.size()>1) {
						//a transaction which contains both a comment and other field changes: 
						//save the comment to the transaction's most specific fields comment column
						//but remove it as a row
						HistoryValues commentHistoryValue = changesMap.remove(SystemFields.INTEGER_COMMENT);
						transactionComment = (String)commentHistoryValue.getNewValue();
						mostSpecificField = getMostSpecificFieldForComment(changedFieldIDs);
					}
					if (!showCommentsInHistory) {
						//this comment is single one: remove if the comments are not shown in the history 
						changesMap.remove(SystemFields.INTEGER_COMMENT);
					}
				}
				Iterator<Integer> itrChanges=changedFieldIDs.iterator();
				while (itrChanges.hasNext()) {
					Integer fieldID = itrChanges.next();
					HistoryValues histValues = changesMap.get(fieldID);
					if (mostSpecificField!=null && fieldID.equals(mostSpecificField)) {
						histValues.setTransactionComment(transactionComment);
					}
					historyList.add(histValues);
				}
			}
		}
		if (historyList.size() > 0) {
			// sort all history beans chronologically
			Collections.sort(historyList, new HistoryComparator());
		}
		return historyList;
	}
	
	/**
	 * Get the list of HistoryValues from the map of Map<Integer, Map<Integer, HistoryValues>>
	 * @param allHistoryMap
	 * @return
	 */
	public static List<FlatHistoryBean> getFlatHistoryValuesListForWorkItems(
			Map<Integer, SortedMap<Integer, Map<Integer, HistoryValues>>> allHistoryForWorkItemsMap, 
			Map<Integer, TWorkItemBean> workItemsMap, Locale locale) {
		List<FlatHistoryBean> workItemsHistoryList=new ArrayList<FlatHistoryBean>();
		Iterator<Integer> workItemsIterartor = workItemsMap.keySet().iterator();
		while (workItemsIterartor.hasNext()) {
			Integer workItemID = workItemsIterartor.next();
			SortedMap<Integer, Map<Integer, HistoryValues>> allHistoryForWorkItemMap = allHistoryForWorkItemsMap.get(workItemID);
			if (allHistoryForWorkItemMap!=null) {
				List<FlatHistoryBean> workItemHistoryList = getFlatHistoryValuesList(
						allHistoryForWorkItemMap, workItemsMap, workItemID, locale, true, true);
				workItemsHistoryList.addAll(workItemHistoryList);
			}
		}
		return workItemsHistoryList;
	}
	
	private static boolean isLong(Integer fieldID) {
		if (fieldID==null) {
			return false;
		} else {
			int intFieldID = fieldID.intValue();
			return (intFieldID==TFieldChangeBean.COMPOUND_HISTORY_FIELD ||
					intFieldID==SystemFields.COMMENT ||
					intFieldID==SystemFields.COMMENT_MODIFY_HISTORY_FIELD || 
					intFieldID==SystemFields.COMMENT_DELETE_HISTORY_FIELD ||
					intFieldID==SystemFields.DESCRIPTION);
		}
		
	}
	
	/**
	 * Get the list of HistoryValues from the map of Map<Integer, Map<Integer, HistoryValues>>
	 * @param allHistoryMap
	 * @param workItemBeansMap
	 * @param workItemID
	 * @param locale
	 * @param longEntriesSeparate
	 * @return
	 */
	public static List<FlatHistoryBean> getFlatHistoryValuesList(
			SortedMap<Integer, Map<Integer, HistoryValues>> allHistoryMap,
			Map<Integer, TWorkItemBean> workItemBeansMap, Integer workItemID, 
			Locale locale, boolean longEntriesSeparate, boolean withChildren) {
		List<FlatHistoryBean> historyList=new ArrayList<FlatHistoryBean>();
		if (allHistoryMap!=null) {
			Iterator<Integer> itrHistoryMap=allHistoryMap.keySet().iterator();
			while (itrHistoryMap.hasNext()) {
				Integer transactionID = itrHistoryMap.next();
				Map<Integer, HistoryValues> changesMap=allHistoryMap.get(transactionID);
				Set<Integer> changedFieldIDs = changesMap.keySet();
				int type = getType(changedFieldIDs);
				HistoryValues histValues = null;
				List<HistoryEntry> historyEntries = new ArrayList<HistoryEntry>();
				List<HistoryEntry> historyLongEntries = new ArrayList<HistoryEntry>();
				Iterator<Integer> itrChanges=changedFieldIDs.iterator();
				while (itrChanges.hasNext()) {
					Integer fieldID = itrChanges.next();
					histValues = changesMap.get(fieldID);
					HistoryEntry historyEntry = new HistoryEntry();
					historyEntry.setFieldLabel(histValues.getFieldName());
					String newValue = histValues.getNewShowValue();
					String oldValue = histValues.getOldShowValue();
					//for flat history with children only the new values are shown but for date change
					//the old value will also be included in a parenthesis
					if (withChildren && (SystemFields.INTEGER_STARTDATE.equals(fieldID) ||
							SystemFields.INTEGER_ENDDATE.equals(fieldID))) {
						if (oldValue!=null && !"".equals(oldValue.trim())) {
							newValue = newValue + " (" + oldValue + ")";
						}
					}
					boolean isLongField=isLong(fieldID);
					if (isLongField){
						//if(!SystemFields.INTEGER_COMMENT.equals(fieldID)){
							String diff=null;
							try{
								String newValueFormatted=ItemDetailBL.formatDescription(newValue,locale);
								String oldValueFormatted=ItemDetailBL.formatDescription(oldValue,locale);
								diff=HTMLDiff.makeDiff(newValueFormatted, oldValueFormatted, locale);
								diff=ItemDetailBL.formatDescription(diff,locale);
							}catch (Exception ex){
								LOGGER.error(" can't create diff: "+ex.getMessage());
								if(LOGGER.isDebugEnabled()){
									LOGGER.error(ExceptionUtils.getStackTrace(ex));
								}
							}
							historyEntry.setDiff(diff);
						//}
					}
					String changedText =null;
					if(type==HistoryBean.HISTORY_TYPE.ATTACHMENT){
						changedText="<strong>"+histValues.getFieldName()+"</strong> ";
						if(newValue!=null&&newValue.length()>0){
							changedText+="<span class=\"histNewValue\">"+histValues.getNewShowValue()+"</span>";
						}else{
							changedText+="<span class=\"histOldValue\">"+histValues.getOldShowValue()+"</span>";
						}
					}else {
						String tkey = "item.history.changeTextSimpleField";
						if (isLongField) {
							tkey = "item.history.changeTextLongField";
						}
						if (newValue != null && newValue.length() > 0 && oldValue != null && oldValue.length() > 0) {
							changedText = LocalizeUtil.getParametrizedString(tkey,
									new Object[]{histValues.getFieldName(),
											histValues.getOldShowValue(),
											histValues.getNewShowValue()}, locale
							);
						} else if (newValue != null && newValue.length() > 0) {
							tkey = tkey + ".oldValueNull";
							changedText = LocalizeUtil.getParametrizedString(tkey,
									new Object[]{histValues.getFieldName(),
											histValues.getNewShowValue()}, locale
							);
						} else {
							tkey = tkey + ".newValueNull";
							changedText = LocalizeUtil.getParametrizedString(tkey,
									new Object[]{histValues.getFieldName(),
											histValues.getOldShowValue()}, locale
							);
						}
					}
					historyEntry.setNewValue(newValue);
					historyEntry.setChangedText(changedText);
					historyEntry.setOldValue(oldValue);
					if(longEntriesSeparate && fieldID.intValue()==TFieldChangeBean.COMPOUND_HISTORY_FIELD){
						historyEntry.setFieldLabel(null);
					}
					boolean fieldIsLong = isLong(fieldID);
					if (fieldIsLong) {
						historyEntry.setNewValue(ItemDetailBL.formatDescription(historyEntry.getNewValue(), locale));
					}
					if(longEntriesSeparate && fieldIsLong){
						//TODO whether the not pseudo field is of type long 
						historyLongEntries.add(historyEntry);
					}else{
						//long fields and short fields are treated same: format the long field content already here (not in the jsp) 
						historyEntries.add(historyEntry);
					}
				}
				if (histValues!=null) {
					FlatHistoryBean flatHistoryBean = new FlatHistoryBean();
					flatHistoryBean.setChangedByName(histValues.getChangedByName());
					flatHistoryBean.setPersonID(histValues.getChangedByID());
					flatHistoryBean.setLastEdit(histValues.getLastEdit());
					flatHistoryBean.setHistoryEntries(historyEntries);
					flatHistoryBean.setHistoryLongEntries(historyLongEntries);
					flatHistoryBean.setType(type);
					flatHistoryBean.setIconName(getIconByType(type));
					addWorkItemToFlatHistoryBean(flatHistoryBean, workItemBeansMap, workItemID, FlatHistoryBean.RENDER_TYPE.HISTORY_VALUES);										
					historyList.add(flatHistoryBean);
				}
			}
		}
		if (historyList.size() > 0) {
			// sort all history beans chronologically
			Collections.sort(historyList, new HistoryComparator());
		}		
		return historyList;
	}	
	
	public static int getType(Set<Integer> fieldIDs) {
		if (fieldIDs==null) {
			return HistoryBean.HISTORY_TYPE.COMMON_HISTORYVALUES;
		}
		if (fieldIDs.contains(SystemFields.INTEGER_STATE)) {
			return HistoryBean.HISTORY_TYPE.STATE_CHANGE;
		}		
		if (fieldIDs.contains(SystemFields.INTEGER_STARTDATE) || 
				fieldIDs.contains(SystemFields.INTEGER_ENDDATE)) {
			return HistoryBean.HISTORY_TYPE.BASELINE_CHANGE;
		}
		if (fieldIDs.contains(SystemFields.ATTACHMENT_ADD_HISTORY_FIELD) || 
				fieldIDs.contains(SystemFields.ATTACHMENT_MODIFY_HISTORY_FIELD) ||
				fieldIDs.contains(SystemFields.ATTACHMENT_DELETE_HISTORY_FIELD)) {
			return HistoryBean.HISTORY_TYPE.ATTACHMENT;
		}
		Iterator<Integer> iterator = fieldIDs.iterator();
		while (iterator.hasNext()) {
			Integer fieldID = iterator.next();
			//not common and not comment but probably explicit history field
			if (!fieldID.equals(TFieldChangeBean.COMPOUND_HISTORY_FIELD) && 
					!fieldID.equals(SystemFields.INTEGER_COMMENT) && 
					!fieldIDs.contains(SystemFields.COMMENT_MODIFY_HISTORY_FIELD) &&
					!fieldIDs.contains(SystemFields.COMMENT_DELETE_HISTORY_FIELD)) {
				return HistoryBean.HISTORY_TYPE.OTHER_EXPLICIT_HISTORY;
			}
		}		
		if (fieldIDs.contains(SystemFields.INTEGER_COMMENT) || 
				fieldIDs.contains(SystemFields.COMMENT_MODIFY_HISTORY_FIELD) || 
				fieldIDs.contains(SystemFields.COMMENT_DELETE_HISTORY_FIELD)) {
			//comment
			return HistoryBean.HISTORY_TYPE.COMMENT;
		}
		//common
		return HistoryBean.HISTORY_TYPE.COMMON_HISTORYVALUES;
	}
	
	public static String getIconByType(int type){
		switch (type) {
		case HistoryBean.HISTORY_TYPE.COMMON_HISTORYVALUES:
			return "trail.gif";
		case HistoryBean.HISTORY_TYPE.STATE_CHANGE:
			return "stateChange.gif";
		case HistoryBean.HISTORY_TYPE.COMMENT:
			return "comment.png";
		/*case HistoryBean.HISTORY_TYPE.BUDGET_CHANGE:
			return "budget.gif";*/
		case HistoryBean.HISTORY_TYPE.BASELINE_CHANGE:
			return "calendar.png";
		case HistoryBean.HISTORY_TYPE.COST:
			return "cost.gif";
		case HistoryBean.HISTORY_TYPE.ATTACHMENT:
			return "attachment.png";
		case HistoryBean.HISTORY_TYPE.OTHER_EXPLICIT_HISTORY:
			return "customField.gif";
		default:
			return "";
		}
	}
	
	/**
	 * Try to guess the most important field the comment will be linked with 
	 * @param fieldIDsSet
	 * @return
	 */
	private static Integer getMostSpecificFieldForComment(Set<Integer> fieldIDsSet) {
		List<Integer> fieldPriorityList = new ArrayList<Integer>();
		fieldPriorityList.add(SystemFields.INTEGER_STATE);
		fieldPriorityList.add(SystemFields.INTEGER_PROJECT);
		fieldPriorityList.add(SystemFields.INTEGER_ISSUETYPE);
		fieldPriorityList.add(SystemFields.INTEGER_ENDDATE);
		fieldPriorityList.add(SystemFields.INTEGER_STARTDATE);
		fieldPriorityList.add(SystemFields.INTEGER_MANAGER);
		fieldPriorityList.add(SystemFields.INTEGER_RESPONSIBLE);
		Iterator<Integer> itrFieldPriorityList = fieldPriorityList.iterator();
		while (itrFieldPriorityList.hasNext()) {
			Integer fieldID = itrFieldPriorityList.next();
			if (fieldIDsSet.contains(fieldID)) {
				return fieldID;
			}
		}
		Iterator<Integer> itrFieldIDsSet = fieldIDsSet.iterator();
		return itrFieldIDsSet.next();
	}
	
	public static List<HistoryValues> setTransactionLimits(List<HistoryValues> historyList) {
		Integer oldTransactionID = null;
		Integer newTransactionID;
		if (historyList!=null) {
			Iterator<HistoryValues> iterator = historyList.iterator();
			while (iterator.hasNext()) {
				HistoryValues historyValues = iterator.next();
				newTransactionID = historyValues.getTransactionID();
				historyValues.setNewTransction(EqualUtils.notEqual(newTransactionID, oldTransactionID));
				oldTransactionID = newTransactionID;
			}
		}
		return historyList;
	}
	
	/**
	 * Get the history for a workItemID
	 * @param workItemID
	 * @param filterFieldID if null do not filter by it
	 * @param includeField: if filterFieldID not null: if true include this field, if false exclude this field
	 * @param personID personBean if null or not external do not filter by it
	 * @param locale
	 * @param longTextIsPlain
	 * @return
	 */
	public static SortedMap<Integer, Map<Integer, HistoryValues>> getWorkItemHistory(Integer workItemID, Integer filterFieldID, 
			boolean includeField, Integer personID, Locale locale, boolean isISO, LONG_TEXT_TYPE longTextIsPlain) {
		Integer[] filterFieldIDs = null;
		if ( filterFieldID!=null) {
			filterFieldIDs = new Integer[] {filterFieldID};
		}
		Map<Integer, SortedMap<Integer, Map<Integer, HistoryValues>>> historyValues = 
			getWorkItemsHistory(new int[]{workItemID}, filterFieldIDs, includeField, 
					null, null, null, locale, isISO, longTextIsPlain, true, personID);
		return historyValues.get(workItemID);
	}
	
	/**
	 * Gets the comments form the history
	 * @param personID
	 * @param workItemBean
	 * @param locale
	 * @param isISO
	 * @param longTextIsPlain
	 * @return
	 */
	public static List<HistoryValues> getRestrictedWorkItemComments(Integer personID,
			Integer workItemID, Locale locale, boolean isISO, LONG_TEXT_TYPE longTextIsPlain) {
		List<HistoryValues> comments = null;
		try {
			TWorkItemBean workItemBean = ItemBL.loadWorkItem(workItemID);
			if (workItemBean!=null) {
				return getRestrictedWorkItemComments(personID, workItemBean, locale, isISO, longTextIsPlain);
			}
		} catch (ItemLoaderException e) {
		}
		return comments;
	}
	
	private static List<HistoryValues> getRestrictedWorkItemComments(Integer personID,
			TWorkItemBean workItemBean, Locale locale, boolean isISO, LONG_TEXT_TYPE longTextIsPlain) {
		List<HistoryValues> comments = null;
		Map<Integer, Integer> fieldRestrictions = AccessBeans.getFieldRestrictions(personID,
				workItemBean.getProjectID(), workItemBean.getListTypeID(), false);
		if (fieldRestrictions==null || !fieldRestrictions.containsKey(SystemFields.INTEGER_COMMENT)) {
			return getWorkItemComments(personID, workItemBean,locale,isISO,longTextIsPlain);
		}
		return comments;
	}
	
	private static List<HistoryValues> getWorkItemComments(Integer personID,
			TWorkItemBean workItemBean, Locale locale, boolean isISO, LONG_TEXT_TYPE longTextIsPlain) {
		List<HistoryValues> comments=HistoryLoaderBL.getHistoryValuesList(
					HistoryLoaderBL.getWorkItemHistory(workItemBean.getObjectID(), SystemFields.INTEGER_COMMENT,
							true, personID, locale, isISO, longTextIsPlain), true);
		return comments;
	}
	
	/**
	 * Get the history for a workItemID
	 * @param workItemID
	 * @param filterFieldIDs if null do not filter by it
	 * @param includeField: if filterFieldID not null: if true include this field, if false exclude this field
	 * @param personID personBean if null or not external do not filter by it
	 * @param locale
	 * @param longTextIsPlain
	 * @return
	 */
	public static SortedMap<Integer, Map<Integer, HistoryValues>> getRestrictedWorkItemHistory(Integer personID,
			TWorkItemBean workItemBean, Locale locale, boolean isISO, LONG_TEXT_TYPE longTextIsPlain) {
		Map<Integer, SortedMap<Integer, Map<Integer, HistoryValues>>> historyValues = null;
		if (workItemBean!=null) {
		historyValues = getWorkItemsHistory(new int[]{workItemBean.getObjectID()},
				null, false, null, null, null, locale, isISO, longTextIsPlain, true, personID);
			return historyValues.get(workItemBean.getObjectID());
		} else {
			return null;
		}
	}
	
	
	private static HistoryValues initHistoryValues(THistoryTransactionBean historyTransactionBean) {
		HistoryValues historyValues = new HistoryValues();
		if (historyTransactionBean!=null) {
			historyValues.setChangedByID(historyTransactionBean.getChangedByID());
			historyValues.setChangedByName(historyTransactionBean.getChangedByName());
			historyValues.setWorkItemID(historyTransactionBean.getWorkItem());
			historyValues.setLastEdit(historyTransactionBean.getLastEdit());
			historyValues.setTransactionUuid(historyTransactionBean.getUuid());
		}
		return historyValues;
	}
	
	/**
	 * Get the attribute value(s) for a fieldID
	 * If composite gather the parts in a list (@see TWorkItemBean.getAttribute(Integer fieldID)})
	 * 
	 * @param historyValuesMap key: fieldID_parameterCode, value: Object or Object[]
	 * @param fieldID
	 * @return
	 */
	private static Object getAttribute(Map<String, Object> historyValuesMap,
			Integer fieldID, IFieldTypeRT fieldTypeRT) {
		if (historyValuesMap==null || historyValuesMap.isEmpty()) {
			return null;
		}
		if (fieldTypeRT.isComposite()) {
			//composite custom field
			int parameterCode=1;
			String mergeKey = MergeUtil.mergeKey(fieldID, Integer.valueOf(parameterCode));
			Map<Integer, Object> compositeValueMap = new HashMap<Integer, Object>();
			//get the parts till keys are found (no problem if the value for key is null)
			while (historyValuesMap.containsKey(mergeKey)) {
				compositeValueMap.put(Integer.valueOf(parameterCode), historyValuesMap.get(mergeKey));
				parameterCode++;
				mergeKey = MergeUtil.mergeKey(fieldID, Integer.valueOf(parameterCode));
			}
			if (!compositeValueMap.isEmpty()) {
				return compositeValueMap;
			}
		} else {
			//single custom field
			return historyValuesMap.get(MergeUtil.mergeKey(fieldID, null));
		}
		return null;
	}
		
	/**
	 * Get the resource key for attachmentHistoryField 
	 * @param historyFieldKey
	 * @return
	 */
	public static String getHistoryFieldKey(Integer historyFieldKey) {
		if (historyFieldKey==null) {
			return COMMON_FIELD_KEY;
		}
		switch (historyFieldKey.intValue()) {
		case SystemFields.ATTACHMENT_ADD_HISTORY_FIELD:
			return ATTACHMENT_ADDED_FIELD_KEY;
		case SystemFields.ATTACHMENT_MODIFY_HISTORY_FIELD:
			return ATTACHMENT_MODIFIED_FIELD_KEY;
		case SystemFields.ATTACHMENT_DELETE_HISTORY_FIELD:
			return ATTACHMENT_DELETED_FIELD_KEY;
		case SystemFields.COMMENT_MODIFY_HISTORY_FIELD:
			return COMMENT_MODIFIED_FIELD_KEY;
		case SystemFields.COMMENT_DELETE_HISTORY_FIELD:
			return COMMENT_DELETED_FIELD_KEY;
		/**
		 * !!!Explicit for Revenue Solutions!!!
		 */	
		/*case SystemFields.MIGRATION_ADD_HISTORY_FIELD:
			return MigrateBL.MIGRATION_HISTORY_ADDED;
		case SystemFields.MIGRATION_MODIFY_HISTORY_FIELD:
			return MigrateBL.MIGRATION_HISTORY_MODIFIED;
		case SystemFields.MIGRATION_DELETE_HISTORY_FIELD:
			return MigrateBL.MIGRATION_HISTORY_DELETED;*/
		/**
		 * !!!Explicit for Revenue Solutions!!!
		 */
		default: 
			return COMMON_FIELD_KEY;
		}
	}
	
	/**
	 * Only for history with children (in the history for a single issue no extra issue identifier is needed )
	 * @param flatHistoryBean
	 * @param workItemBeansMap
	 * @param workItemID
	 * @param renderType
	 */
	public static void addWorkItemToFlatHistoryBean(FlatHistoryBean flatHistoryBean,
			Map<Integer, TWorkItemBean> workItemBeansMap, Integer workItemID, int renderType) {
		if (workItemBeansMap!=null) {
			TWorkItemBean workItemBean = workItemBeansMap.get(workItemID);
			if (workItemBean!=null) {
				flatHistoryBean.setWorkItemID(workItemBean.getObjectID());
				flatHistoryBean.setTitle(workItemBean.getSynopsis());
				flatHistoryBean.setRenderType(renderType);
			}
		}
	}
	
	public static String formatEffort(TBudgetBean bugetBean, Locale locale){
		DoubleNumberFormatUtil doubleNumberFormatUtil = DoubleNumberFormatUtil.getInstance();
		String effortString = "";
		if (bugetBean!=null && bugetBean.getEstimatedHours()!=null) {
			effortString = doubleNumberFormatUtil.formatGUI(bugetBean.getEstimatedHours(), locale);
			String timeUnitString = AccountingBL.getTimeUnitOption(bugetBean.getTimeUnit(), locale);
			if (timeUnitString!=null) {
				effortString +=	" " + timeUnitString;
			}
		}
		return effortString;
	}
	
	
	public static String formatCost(TBudgetBean budgetBean, Locale locale){
		DoubleNumberFormatUtil doubleNumberFormatUtil = DoubleNumberFormatUtil.getInstance();
		String costString = "";
		if (budgetBean!=null && budgetBean.getEstimatedCost()!=null) {
			costString = doubleNumberFormatUtil.formatGUI(budgetBean.getEstimatedCost(), locale);
			if (budgetBean.getCurrency()!=null){
				costString += " " + budgetBean.getCurrency();
			}
		}
		return costString;
	}
	
	
	public static String formatEffort(TCostBean costBean, Locale locale){
		DoubleNumberFormatUtil doubleNumberFormatUtil = DoubleNumberFormatUtil.getInstance();
		String effortString = "";
		if (costBean!=null && costBean.getHours()!=null) {
			effortString = doubleNumberFormatUtil.formatGUI(costBean.getHours(), locale);
			String timeUnitString = AccountingBL.getTimeUnitOption(AccountingBL.TIMEUNITS.HOURS, locale);
			if (timeUnitString!=null){
				effortString +=	" " + timeUnitString;
			}
		}
		return effortString;
	}
	
	
	public static String formatCost(TCostBean costBean, Locale locale){
		DoubleNumberFormatUtil doubleNumberFormatUtil = DoubleNumberFormatUtil.getInstance();
		String costString = "";
		if (costBean!=null && costBean.getCost()!=null) {
			costString = doubleNumberFormatUtil.formatGUI(costBean.getCost(), locale);
			if (costBean.getCurrency()!=null){
				costString += " " + costBean.getCurrency();
			}
		}
		return costString;
	}
	
	public static String formatEffort(TActualEstimatedBudgetBean actualEstimatedBudgetBean, Locale locale){
		DoubleNumberFormatUtil doubleNumberFormatUtil = DoubleNumberFormatUtil.getInstance();
		String effortString = "";
		if (actualEstimatedBudgetBean!=null && actualEstimatedBudgetBean.getEstimatedHours()!=null) {
			effortString = doubleNumberFormatUtil.formatGUI(actualEstimatedBudgetBean.getEstimatedHours(), locale);
			String timeUnitString = AccountingBL.getTimeUnitOption(actualEstimatedBudgetBean.getTimeUnit(), locale);
			if (timeUnitString!=null){
				effortString +=	" " + timeUnitString;
			}
		}
		return effortString;
	}
	
	
	public static String formatCost(TActualEstimatedBudgetBean actualEstimatedBudgetBean, Locale locale){
		DoubleNumberFormatUtil doubleNumberFormatUtil = DoubleNumberFormatUtil.getInstance();
		String costString = "";
		if (actualEstimatedBudgetBean!=null && actualEstimatedBudgetBean.getEstimatedCost()!=null) {
			costString = doubleNumberFormatUtil.formatGUI(actualEstimatedBudgetBean.getEstimatedCost(), locale);
			if (actualEstimatedBudgetBean.getCurrency()!=null){
				costString += " " + actualEstimatedBudgetBean.getCurrency();
			}
		}
		return costString;
	}

	public static Set<Integer> getAttachmentHistoryFields() {
		//we force all the attachment changes to have explicit history
		Set<Integer> attachmentHistoryFields = new HashSet<Integer>();
		attachmentHistoryFields.add(SystemFields.INTEGER_ATTACHMENT_ADD_HISTORY_FIELD);
		attachmentHistoryFields.add(SystemFields.INTEGER_ATTACHMENT_MODIFY_HISTORY_FIELD);
		attachmentHistoryFields.add(SystemFields.INTEGER_ATTACHMENT_DELETE_HISTORY_FIELD);
		/**
		 * !!!Explicit for Revenue Solutions!!!
		 */
		//attachmentHistoryFields.add(SystemFields.MIGRATION_ADD_HISTORY_FIELD);
		//attachmentHistoryFields.add(SystemFields.MIGRATION_MODIFY_HISTORY_FIELD);
		//attachmentHistoryFields.add(SystemFields.MIGRATION_DELETE_HISTORY_FIELD);
		/**
		 * !!!Explicit for Revenue Solutions!!!
		 */
		return attachmentHistoryFields;
	}
	
	public static Set<Integer> getCommentHistoryFields() {
		//we force all the attachment changes to have explicit history
		Set<Integer> commentHistoryFields = new HashSet<Integer>();		
		commentHistoryFields.add(SystemFields.INTEGER_COMMENT_MODIFY_HISTORY_FIELD);
		commentHistoryFields.add(SystemFields.INTEGER_COMMENT_DELETE_HISTORY_FIELD);
		return commentHistoryFields;
	}
	
	public static Set<Integer> getPseudoHistoryFields() {
		Set<Integer> pseudoHistoryFields = getAttachmentHistoryFields();
		pseudoHistoryFields.addAll(getCommentHistoryFields());
		pseudoHistoryFields.add(TFieldChangeBean.COMPOUND_HISTORY_FIELD);
		return pseudoHistoryFields;
	}
}
