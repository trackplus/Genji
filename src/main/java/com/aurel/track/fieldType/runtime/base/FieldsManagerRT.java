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



package com.aurel.track.fieldType.runtime.base;

import java.util.ArrayList;
import java.util.Collection;
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
import java.util.TreeSet;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadItemIDListItems;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.project.ProjectConfigBL;
import com.aurel.track.admin.project.release.ReleaseBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.attachment.AttachBL;
import com.aurel.track.attachment.AttachBLException;
import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TLinkTypeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.cluster.ClusterBL.CHANGE_TYPE;
import com.aurel.track.cluster.ClusterMarkChangesBL;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.WorkItemDAO;
import com.aurel.track.dao.WorkItemLinkDAO;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.errors.ErrorParameter;
import com.aurel.track.exchange.ImportCounts;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.fieldType.runtime.validators.Validator;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.item.ItemPersisterException;
import com.aurel.track.item.action.IPluginItemAction;
import com.aurel.track.item.action.ItemActionUtil;
import com.aurel.track.item.action.PluginItemActionException;

import com.aurel.track.item.budgetCost.AccountingBL;
import com.aurel.track.item.budgetCost.AccountingForm;
import com.aurel.track.item.consInf.ConsInfBL;
import com.aurel.track.item.consInf.ConsInfShow;
import com.aurel.track.item.consInf.RaciRole;
import com.aurel.track.item.consInf.RaciRoleBL;
import com.aurel.track.item.history.HistoryLoaderBL;
import com.aurel.track.item.history.HistoryLoaderBL.LONG_TEXT_TYPE;
import com.aurel.track.item.history.HistorySaverBL;
import com.aurel.track.item.link.InlineItemLinkBL;
import com.aurel.track.item.link.ItemLinkBL;
import com.aurel.track.item.workflow.execute.StatusWorkflow;
import com.aurel.track.itemNavigator.ItemNavigatorTaskBean;
import com.aurel.track.linkType.ILinkType;
import com.aurel.track.linkType.LinkTypeBL;
import com.aurel.track.lucene.index.associatedFields.AttachmentIndexer;
import com.aurel.track.move.ItemMoveBL;
import com.aurel.track.plugin.ItemActionDescription;
import com.aurel.track.plugin.PluginManager;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.screen.SystemActions;
import com.aurel.track.screen.item.bl.runtime.ScreenRuntimeBL;
import com.aurel.track.util.CalendarUtil;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.emailHandling.EmailAttachment;
import com.aurel.track.util.event.EventPublisher;
import com.aurel.track.util.event.IEventSubscriber;
import com.aurel.track.util.event.parameters.AfterItemSaveEventParam;


/**
 * Manager class for defining the business logic for fields at runtime
 * @author Tamas Ruff
 *
 */
public class FieldsManagerRT {

	private static final Logger LOGGER = LogManager.getLogger(FieldsManagerRT.class);
	private static WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();
	private static WorkItemLinkDAO workItemLinkDAO = DAOFactory.getFactory().getWorkItemLinkDAO();

	/**
	 * Creating a workItemContext for importing workItems from external sources
	 * TODO do we need to prepare the validation code?
	 * @param workItemBean
	 * @param person
	 * @param locale
	 */
	public static WorkItemContext getWorkItemContext(TWorkItemBean workItemBean, Integer person, Locale locale){
		WorkItemContext workItemContext = prepareWorkItemContext(person, locale, null, null, null);
		workItemContext.setWorkItemBean(workItemBean);
		if (workItemBean.getObjectID()!=null) {
			workItemContext.setWorkItemBeanOriginal(workItemBean.copyShallow());
		}
		return workItemContext;
	}

	/**
	 * Creats a workItemContext for editing or creating items from item navigator.
	 * @param itemNavigatorTaskBean
	 * @param personBean
	 * @param locale
	 * @param dropDownNeeded
	 * @return
	 */
	public static WorkItemContext getWorkItemContext(ItemNavigatorTaskBean itemNavigatorTaskBean, TPersonBean personBean, Locale locale, boolean dropDownNeeded) {
		WorkItemContext workItemContext = null;
		if(itemNavigatorTaskBean.isNew()) {
			ItemActionDescription itemPluginDescriptor=ItemActionUtil.getDescriptor(String.valueOf(SystemActions.NEW));
			IPluginItemAction itemActionPlugin=null;
			if(itemPluginDescriptor.getTheClassName()!=null){
				itemActionPlugin=ItemActionUtil.getPlugin(itemPluginDescriptor.getTheClassName());
			}
			if (itemActionPlugin == null) {
				return null;
			}
			Map<String,Object> params = new HashMap<String, Object>();
			String[] projectIDArr = {itemNavigatorTaskBean.getProjectID().toString()};
			String[] issueTypeIDArr = {itemNavigatorTaskBean.getIssueTypeID().toString()};
			params.put("projectID", projectIDArr);
			params.put("issueTypeID", issueTypeIDArr);
			//In case of Add linked item (Item navigator context menu):
			//The following code block handle link config from client (form parameters) and
			//puts into params map as a HashMap
			Map<String, Object>newlyCreatedLinkSettings = new HashMap<String, Object>();
			newlyCreatedLinkSettings.put("linkedWorkItemID", null);
			newlyCreatedLinkSettings.put("linkTypeWithDirection", null);
			newlyCreatedLinkSettings.put("parametersMap", null);
			newlyCreatedLinkSettings.put("description", null);
			params.put("newlyCreatedLinkSettings", newlyCreatedLinkSettings);
			try {
				workItemContext = itemActionPlugin.next(locale, personBean, itemNavigatorTaskBean.getWorkItemID(), null, params, null, null);
			}catch(PluginItemActionException ex) {
				ex.printStackTrace();
			}
		}else {
			Boolean accessLevelFlag = Boolean.FALSE;
			Integer stateID=null;
			workItemContext = ItemBL.getWorkItemContext(SystemActions.EDIT, itemNavigatorTaskBean.getWorkItemID(), itemNavigatorTaskBean.getProjectID(),
					itemNavigatorTaskBean.getIssueTypeID(), stateID, accessLevelFlag, personBean.getObjectID(), locale, dropDownNeeded);
		}
		return workItemContext;
	}

	/**
	 * Creating a workItemContext for importing workItems from external sources
	 * TODO do we need to prepare the validation code?
	 * @param workItemBean
	 * @param person
	 * @param locale
	 * @param presentFields
	 */
	public static WorkItemContext getExchangeContextForExisting(TWorkItemBean workItemBean, Integer person, Locale locale, Set<Integer> presentFields){
		WorkItemContext workItemContext = prepareWorkItemContext(person, locale, null, null, null);
		//set the workItemBean in workItemContext
		workItemContext.setWorkItemBean(workItemBean);
		//save a copy of the original workItem in workItemContext for later use
		workItemContext.setWorkItemBeanOriginal(workItemBean.copyShallow());
		workItemContext.setPresentFieldIDs(presentFields);
		workItemContext.setExchangeImport(true);
		return workItemContext;
	}

	/**
	 * Creating a workItemContext for importing workItems from external sources
	 * TODO do we need to prepare the validation code?
	 * @param person
	 * @param locale
	 * @param presentFields
	 * @return
	 */
	public static WorkItemContext getExchangeContextForNew(Integer person,
			 Locale locale, Set<Integer> presentFields) {
		WorkItemContext workItemContext = prepareWorkItemContext(person, locale, null, null, null);
		//creating a new WorkItemBean
		TWorkItemBean workItemBean = new TWorkItemBean();
		//set the workItemBean in workItemContext
		workItemContext.setWorkItemBean(workItemBean);
		//resets the original workItem in workItemContext
		workItemContext.setWorkItemBeanOriginal(null);
		workItemContext.setPresentFieldIDs(presentFields);
		workItemContext.setExchangeImport(true);
		return workItemContext;
	}

	/**
	 * Creates and configures new workItemContexts for workItems
	 * @param workItemBeansList
	 * @param presentFieldIDs
	 * @param person
	 * @param locale
	 * @param projectsIssueTypesFieldConfigsMap
	 * @param projectsIssueTypesFieldSettingsMap
	 * @return
	 */
	public static Map<Integer, WorkItemContext> createImportContext(Collection<TWorkItemBean> workItemBeansList,
			Set<Integer> presentFieldIDs, Map<Integer, Map<Integer, Map<Integer, TFieldConfigBean>>> projectsIssueTypesFieldConfigsMap,
			Map<Integer, Map<Integer, Map<String, Object>>> projectsIssueTypesFieldSettingsMap,
			Integer selectedProjectID, Integer selectedIssueTypeID, Integer person, Locale locale){
		Map<Integer, WorkItemContext> contextMap = new HashMap<Integer, WorkItemContext>();
		for (Iterator<TWorkItemBean> iterator = workItemBeansList.iterator(); iterator.hasNext();) {
			TWorkItemBean workItemBean = iterator.next();
			if (workItemBean!=null) {
				Integer workItemID = workItemBean.getObjectID();
				Integer projectID;
				if (selectedProjectID==null) {
					projectID = workItemBean.getProjectID();
				} else {
					projectID = selectedProjectID;
				}
				Integer issueType;
				if (selectedIssueTypeID==null) {
					issueType = workItemBean.getListTypeID();
				} else {
					issueType = selectedIssueTypeID;
				}
				Map<Integer, TFieldConfigBean> fieldConfigsMap = FieldRuntimeBL.getFieldConfigsForProjectIssueType(
						projectsIssueTypesFieldConfigsMap, projectID, issueType);
				Map<String, Object> fieldSettingsMap = FieldRuntimeBL.getFieldSettingsForProjectIssueType(
						projectsIssueTypesFieldSettingsMap, projectID, issueType);
				if (workItemID!=null) {
					contextMap.put(workItemID, createImportContext(workItemBean, presentFieldIDs, person,
							locale, fieldConfigsMap, fieldSettingsMap));
				}
			}
		}
		return contextMap;
	}

	/**
	 * Creates and configures new workItemContext: for Excel and MSProject import
	 * @param workItemBean
	 * @param presentFieldIDs
	 * @param person
	 * @param locale
	 * @param projectsIssueTypesFieldConfigsMap
	 * @param projectsIssueTypesFieldSettingsMap
	 * @return
	 */
	public static WorkItemContext createImportContext(TWorkItemBean workItemBean,
			Set<Integer> presentFieldIDs, Integer person, Locale locale,
			Map<Integer, TFieldConfigBean> projectsIssueTypesFieldConfigsMap,
			Map<String, Object> projectsIssueTypesFieldSettingsMap){
		WorkItemContext workItemContext = new WorkItemContext();
		workItemContext.setPerson(person);
		workItemContext.setLocale(locale);
		workItemContext.setWorkItemBean(workItemBean);
		Integer workItemID = workItemBean.getObjectID();
		if (workItemID==null) {
			workItemContext.setWorkItemBeanOriginal(null);
		} else {
			TWorkItemBean workItemBeanOriginal = ItemBL.loadWorkItemWithCustomFields(workItemID);
			workItemContext.setWorkItemBeanOriginal(workItemBeanOriginal);
		}
		workItemContext.setPresentFieldIDs(presentFieldIDs);
		workItemContext.setFieldConfigs(projectsIssueTypesFieldConfigsMap);
		workItemContext.setFieldSettings(projectsIssueTypesFieldSettingsMap);
		return workItemContext;
	}

	/**
	 * Call the field validators
	 * @param workItemBeansList
	 * @param presentFieldIDs
	 * @param personID
	 * @param locale
	 * @return
	 */
	public static SortedMap<Integer, List<ErrorData>> validateWorkItems(Collection<TWorkItemBean> workItemBeansList,
			Set<Integer> presentFieldIDs, Map<Integer, WorkItemContext> existingWorkItemsContextMap,
			Map<Integer, Map<Integer, Map<Integer, TFieldConfigBean>>> projectsIssueTypesFieldConfigsMap,
			Map<Integer, Map<Integer, Map<String, Object>>> projectsIssueTypesFieldSettingsMap,
			Integer personID, Locale locale) {
		SortedMap<Integer, List<ErrorData>> validationErrorsMap =  new TreeMap<Integer, List<ErrorData>>();
		Iterator<TWorkItemBean> itrWorkItemBean = workItemBeansList.iterator();
		int row = 1;
		while (itrWorkItemBean.hasNext()) {
			TWorkItemBean workItemBean = itrWorkItemBean.next();
			Integer workItemID = workItemBean.getObjectID();
			row++;
			WorkItemContext workItemContext = null;
			if (workItemID!=null) {
				//existing issue
				workItemContext = existingWorkItemsContextMap.get(workItemID);
			}
			if (workItemContext==null) {
				workItemContext = FieldsManagerRT.createImportContext(workItemBean,
					presentFieldIDs, personID, locale,
					projectsIssueTypesFieldConfigsMap.get(workItemBean.getProjectID()).get(workItemBean.getListTypeID()),
					projectsIssueTypesFieldSettingsMap.get(workItemBean.getProjectID()).get(workItemBean.getListTypeID()));
			}
			List<ErrorData> validationErrorsListForRow = validate(workItemBean, workItemContext, false, false, null, false, false);
			if (!validationErrorsListForRow.isEmpty()) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("WorkItem " + workItemBean.getObjectID() + " from row " + row + " has validation errors");
				}
				validationErrorsMap.put(Integer.valueOf(row), validationErrorsListForRow);
			}
		}
		return validationErrorsMap;
	}

	/**
	 * Save the workItems
	 * @param workItemBeansMap
	 * @param presentFieldIDs
	 * @param personID
	 * @param locale
	 * @return
	 */
	public static ImportCounts saveWorkItems(Map<Integer, TWorkItemBean> workItemBeansMap,
			Set<Integer> presentFieldIDs, Map<Integer, WorkItemContext> existingWorkItemsContextMap,
			Map<Integer, Map<Integer, Map<Integer, TFieldConfigBean>>> projectsIssueTypesFieldConfigsMap,
			Map<Integer, Map<Integer, Map<String, Object>>> projectsIssueTypesFieldSettingsMap,
			Map<Integer, Map<Integer, List<Integer>>> rowNoToPseudoFieldsOriginal,
			Map<Integer, Map<Integer, List<Integer>>> rowNoToPseudoFieldsExcel, Map<Integer, Integer> rowToParentRow,
			Integer personID, Locale locale, List<ErrorData> errorsList) {
		ImportCounts importCounts = new ImportCounts();
		int row = 1;
		for (Map.Entry<Integer, TWorkItemBean> workItemEntry : workItemBeansMap.entrySet()) {
			Integer rowNo = workItemEntry.getKey();
			TWorkItemBean workItemBean = workItemEntry.getValue();
			if (workItemBean.getSuperiorworkitem()==null) {
				Integer parentRowNo = rowToParentRow.get(rowNo);
				if (parentRowNo!=null) {
					TWorkItemBean parentWorkItemBean = workItemBeansMap.get(parentRowNo);
					if (parentWorkItemBean!=null) {
						//parent workItem should be already saved previously in this loop
						Integer parentID = parentWorkItemBean.getObjectID();
						if (parentID!=null) {
							workItemBean.setSuperiorworkitem(parentID);
						}
					}
				}
			}
			Integer workItemID = workItemBean.getObjectID();
			row++;
			boolean isNew = (workItemBean.getObjectID()==null);
			WorkItemContext workItemContext = null;
			if (workItemID!=null) {
				//existing issue
				workItemContext = existingWorkItemsContextMap.get(workItemID);
			}
			if (workItemContext==null) {
				workItemContext = FieldsManagerRT.createImportContext(workItemBean,
					presentFieldIDs, personID, locale,
					projectsIssueTypesFieldConfigsMap.get(workItemBean.getProjectID()).get(workItemBean.getListTypeID()),
					projectsIssueTypesFieldSettingsMap.get(workItemBean.getProjectID()).get(workItemBean.getListTypeID()));
			}
			List<ErrorData> itemErrorList = new ArrayList<ErrorData>();
			boolean saveNeeded = FieldsManagerRT.performSave(workItemContext, itemErrorList, false, false/*, false*/);
			//errors detected after save
			if (!itemErrorList.isEmpty()) {
				errorsList.addAll(itemErrorList);
			} else {
				Integer savedWorkItemID = workItemContext.getWorkItemBean().getObjectID();
				boolean watcherSaved = ConsInfBL.updateExcelWatchers(rowNo, savedWorkItemID, rowNoToPseudoFieldsOriginal, rowNoToPseudoFieldsExcel);
				if (saveNeeded || watcherSaved) {
					if (isNew) {
						importCounts.setNoOfCreatedIssues(importCounts.getNoOfCreatedIssues()+1);
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("WorkItem " + savedWorkItemID + " from row " + row + " was created");
						}
					} else {
						importCounts.setNoOfUpdatedIssues(importCounts.getNoOfUpdatedIssues()+1);
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("WorkItem " + savedWorkItemID + " from row " + row + " was updated");
						}
					}
				}
			}
		}
		return importCounts;
	}

	/**
	 * Loading of an existing workItem for adding a comment
	 * @param workItemID
	 * @param person
	 * @param locale
	 */
	public static WorkItemContext inlineComment(Integer workItemID, Integer person,
			Locale locale, Integer commentID, String comment){
		WorkItemContext workItemContext = editOneField(person, workItemID, locale, SystemFields.INTEGER_COMMENT);
		workItemContext.setFieldChangeID(commentID);
		//workItemContext.setUpdateLastEdit(false);
		TWorkItemBean workItemBean = workItemContext.getWorkItemBean();
		if (workItemBean!=null) {
			//comment can be null: in this case commentID should be
			//not null and it will be interpreted as delete comment,
			//otherwise it will be interpreted as add or change depending on commentID
			workItemBean.setComment(comment);
		}
		if (commentID!=null) {
			//needed for edit comment
			TWorkItemBean workItemBeanOriginal = workItemContext.getWorkItemBeanOriginal();
			if (workItemBeanOriginal!=null) {
				//very important to set the old value because this will make the
				//difference between adding a comment and changing the comment
				workItemBeanOriginal.setComment(HistoryLoaderBL.getLongTextField(commentID, true, LONG_TEXT_TYPE.ISFULLHTML));
			}
		}
		return workItemContext;
	}

	/**
	 * Loading of an existing workItem for adding a comment
	 * @param workItemID
	 * @param person
	 * @param locale
	 */
	public static WorkItemContext attachment(Integer workItemID, Integer person, Integer fieldID,
			Locale locale, String newFileNameDescription, String oldFileNameDescription){
		WorkItemContext workItemContext = editOneField(person, workItemID, locale, fieldID);
		//set to a not null value to avoid unnecessary processing in processSave()
		//but in case of attachment change this fieldChangeID does not have any relevance
		//because it is not a system/custom field and it will be saved separately
		//(not in InputFieldTypeRT.processHistorySave(), but in HistorySaverBL.insertFieldChange())
		workItemContext.setFieldChangeID(fieldID);
		//workItemContext.setUpdateLastEdit(false);
		TWorkItemBean workItemBean = workItemContext.getWorkItemBean();
		if (workItemBean!=null) {
			workItemBean.setAttachment(newFileNameDescription);
		}
		TWorkItemBean workItemBeanOriginal = workItemContext.getWorkItemBeanOriginal();
		if (workItemBeanOriginal!=null) {
			workItemBeanOriginal.setAttachment(oldFileNameDescription);
		}
		return workItemContext;
	}


	/**
	 * Creating a workItemContext for changing of a single field in a single workItem: some database operations can be spared.
	 * The presentFields should be set subsequently in the workItemContext because it is not set here!
	 * @param person
	 * @param workItemID
	 * @param locale
	 * @param fieldID
	 */
	public static WorkItemContext editOneField(Integer person, Integer workItemID, Locale locale, Integer fieldID){
		WorkItemContext workItemContext= load(person, workItemID, locale, null);
		Set<Integer> presentFieldIDs = new HashSet<Integer>();
		presentFieldIDs.add(fieldID);
		workItemContext.setPresentFieldIDs(presentFieldIDs);
		updateModifyWorkItem(workItemContext, false, false, false);
		return workItemContext;
	}

	/**
	 * Loading of an existing workItem for screen independent edit
	 * @param person
	 * @param workItemID
	 * @param locale
	 */
	public static WorkItemContext loadAllFields(Integer person, Integer workItemID, Locale locale) {
		WorkItemContext workItemContext= load(person, workItemID, locale, null);
		List<TFieldBean> presentFieldsBeans=FieldBL.loadAll();
		if (presentFieldsBeans!=null) {
			Set<Integer> presentFieldIDs=new HashSet<Integer>();
			Map<Integer,TFieldBean> presentFieldBeansMap=new HashMap<Integer, TFieldBean>();
			for (TFieldBean fieldBean : presentFieldsBeans) {
				presentFieldIDs.add(fieldBean.getObjectID());
				presentFieldBeansMap.put(fieldBean.getObjectID(), fieldBean);
			}
			workItemContext.setPresentFieldIDs(presentFieldIDs);
			workItemContext.setPresentFieldBeans(presentFieldBeansMap);
		}
		updateModifyWorkItem(workItemContext, false, false, true);
		return workItemContext;
	}

	/**
	 * Loading of an existing workItem for edit or view (same screen)
	 * Normally by view the dropDown containers should not be loaded but
	 * the show values are taken from the dropDown based getShowValue() methods
	 * in both cases. So now the dropDowns are loaded anyway, the only difference
	 * (configured through isView)is in presetting the composite parent lists
	 * in edit mode with values (if no default values are set)
	 * @param person
	 * @param workItemID
	 * @param isView
	 * @param locale
	 */
	public static WorkItemContext viewOrEdit(Integer person, Integer workItemID, boolean isView, Locale locale,boolean dropDownsNeeded){
		WorkItemContext workItemContext= load(person, workItemID, locale, SystemActions.EDIT);
		//if(dropDownsNeeded){
			updateModifyWorkItem(workItemContext, isView, false, dropDownsNeeded);
		//}
		return workItemContext;
	}

	/**
	 * Loading of an existing workItem for an action
	 * @param person
	 * @param workItemID
	 * @param locale
	 */
	public static WorkItemContext loadWorkItem(Integer person, Integer workItemID, Locale locale,Integer actionID){
		WorkItemContext workItemContext= load(person, workItemID, locale, actionID);
		updateModifyWorkItem(workItemContext, false, false, true);
		return workItemContext;
	}

	/**
	 * Loading of an existing workItem for status change
	 * @param person
	 * @param workItemID
	 * @param locale
	 */
	public static WorkItemContext changeStatus(Integer person, Integer workItemID, Locale locale){
		WorkItemContext workItemContext= load(person, workItemID, locale, SystemActions.CHANGE_STATUS);
		updateModifyWorkItem(workItemContext, false, false, true);
		return workItemContext;
	}

	/**
	 * Loading of an existing workItem for status change
	 * @param workItemID
	 * @param person
	 * @param locale
	 * @param projectID
	 * @param issueTypeID
	 */
	public static WorkItemContext move(Integer workItemID,Integer person,Locale locale,Integer projectID,Integer issueTypeID,Integer stateID){
		WorkItemContext workItemContext= load(person, workItemID, locale, SystemActions.MOVE);
		TWorkItemBean workItemBean=workItemContext.getWorkItemBean();
		workItemContext.setWorkItemBeanOriginal(workItemBean.copyShallow());
		boolean projectChanged = false;
		if (!workItemBean.getProjectID().equals(projectID)) {
			projectChanged = true;
		}
		workItemBean.setProjectID(projectID);
		if (projectChanged) {
			//remove the old subproject and class (inherited from track+ 3.x.x). Otherwise the old project can't be deleted because subsystem and class foreign keys remain linked with items from the new project)
			//the moved items will not be deleted of course, but then the old subsystem and class get stuck (will not be deleted) so the old project also can't be deleted
			if (workItemBean.getProjectCategoryID()!=null) {
				workItemBean.setProjectCategoryID(null);
			}
			if (workItemBean.getClassID()!=null) {
				workItemBean.setClassID(null);
			}
			if (workItemBean.getReleaseNoticedID()!=null || workItemBean.getReleaseScheduledID()!=null) {
				//set the first release from the project only if a corresponding release was already selected
				//we should guarantee that the new release is one
				//from the new project even if the release fields are not present on the move form
				List<TReleaseBean> releaseBeanList = ReleaseBL.loadMainNotClosedByProject(projectID);
				if (releaseBeanList!=null && !releaseBeanList.isEmpty()) {
					if (workItemBean.getReleaseNoticedID()!=null) {
						workItemBean.setReleaseNoticedID(releaseBeanList.get(0).getObjectID());
					} else {
						workItemBean.setReleaseNoticedID(null);
					}
					if (workItemBean.getReleaseScheduledID()!=null) {
						workItemBean.setReleaseScheduledID(releaseBeanList.get(0).getObjectID());
					} else {
						workItemBean.setReleaseScheduledID(null);
					}
				}
			}
		}
		workItemBean.setListTypeID(issueTypeID);
		//only if state change was needed during the first step
		if (stateID!=null) {
			workItemBean.setStateID(stateID);
		}
		//updateModifyWorkItem(workItemContext, false, projectChanged, true);
		prepareConfigsAndRestrictions(workItemContext, false);
		setComputedFields(workItemBean, workItemContext.getPresentFieldIDs(), workItemContext.getFieldConfigs(), workItemContext.getFieldSettings());
		prepareDropDownContainerAndDependences(workItemContext, false, false, projectChanged);
		return workItemContext;
	}

	/**
	 * Loads a and configures a workItemContext by editing an issue
	 * @param person
	 * @param workItemID
	 * @param locale
	 * @param actionID not null for single issue change, but null for
	 * 	mass operation change because then the screen is not used
	 * 	In this case the presentFields should be set explicitly!
	 * @return
	 */
	private static WorkItemContext load(Integer person, Integer workItemID, Locale locale, Integer actionID){
		Date start = null;
		if (LOGGER.isDebugEnabled()) {
			start = new Date();
		}
		//load also the custom attributes (not only the present ones on the screen
		//because the workItemBean will be used also by sending an email, and there we need all fields)
		TWorkItemBean workItemBean = ItemBL.loadWorkItemWithCustomFields(workItemID);
		if (workItemBean==null) {
			LOGGER.warn("No workItem found for key " + workItemID);
			return new WorkItemContext();
		}
		WorkItemContext workItemContext = prepareWorkItemContext(person, locale, actionID, workItemBean.getProjectID(), workItemBean.getListTypeID());
		workItemContext.setWorkItemBean(workItemBean);
		//save a copy of the original workItem in workItemContext for later use
		//workItemContext.setWorkItemBeanOriginal(workItemBean.copyShallow());
		if (LOGGER.isDebugEnabled() && start!=null) {
			Date end = new Date();
			LOGGER.debug("Loading the context for itemID " + workItemID+ " lasted " + new Long(end.getTime()-start.getTime()).toString() + " ms");
		}
		return workItemContext;
	}

	/**
	 * Configures the workItemContext by editing an issue
	 * @param workItemContext
	 * @param projectChanged
	 * @param dropDownsNeeded for single issue the dropdown lists and some other data is needed,
	 * 	but for mass operation this data is not needed
	 */
	private static void updateModifyWorkItem(WorkItemContext workItemContext,
			boolean isView, boolean projectChanged, boolean dropDownsNeeded){
		TWorkItemBean workItemBean=workItemContext.getWorkItemBean();
		prepareConfigsAndRestrictions(workItemContext, false);
		setComputedFields(workItemBean, workItemContext.getPresentFieldIDs(), workItemContext.getFieldConfigs(), workItemContext.getFieldSettings());
		workItemContext.setWorkItemBeanOriginal(workItemBean.copyShallow());
		if (dropDownsNeeded) {
			prepareDropDownContainerAndDependences(workItemContext, false, isView, projectChanged);
		}
	}

	/**
	 * Loading of an existing workItem for copy
	 * It is a combination of the create and edit
	 * @param person
	 * @param workItemID
	 * @param locale
	 */
	public static WorkItemContext editCopy(Integer person, Integer workItemID, Locale locale){
		//screen for create action: create-like behavior
		WorkItemContext workItemContext= load(person, workItemID, locale, SystemActions.NEW);
		//false because the requiredSystemFields should be present in the workItem copied from: edit-like behavior
		prepareConfigsAndRestrictions(workItemContext, false);
		setComputedFields(workItemContext.getWorkItemBean(), workItemContext.getPresentFieldIDs(), workItemContext.getFieldConfigs(), workItemContext.getFieldSettings());
		workItemContext.setWorkItemBeanOriginal(workItemContext.getWorkItemBean().copyShallow());
		//false because the original values should appear in the dropdowns: edit-like behavior
		prepareDropDownContainerAndDependences(workItemContext, false, false, false);
		TWorkItemBean workItemBean = workItemContext.getWorkItemBean();
		workItemBean.setCreated(null);
		return workItemContext;
	}

	/**
	 * Creates and configures new workItemContext by creating a new issue
	 * @param person
	 * @param project
	 * @param issueType
	 * @param accessLevelFlag
	 * @param locale
	 * @return
	 */
	public static WorkItemContext create(Integer person, Integer project, Integer issueType, boolean accessLevelFlag,
			Locale locale){
		return create(person, project, issueType, accessLevelFlag, locale, true);
	}
	public static WorkItemContext create(Integer person, Integer project, Integer issueType, boolean accessLevelFlag,Locale locale,boolean includeDropDown){
		return create(person, project, issueType, accessLevelFlag, locale, SystemActions.NEW,includeDropDown);
	}

	/**
	 * Creates and configures new workItemContext by creating a new child issue
	 * @param person
	 * @param project
	 * @param issueType
	 * @param accessLevelFlag
	 * @param locale
	 * @return
	 */
	public static WorkItemContext createChild(Integer person, Integer project, Integer issueType, boolean accessLevelFlag, Locale locale){
		return createChild(person, project, issueType, accessLevelFlag, locale, true);
	}
	public static WorkItemContext createChild(Integer person, Integer project, Integer issueType, boolean accessLevelFlag, Locale locale,boolean includeDropDown){
		WorkItemContext ctx= create(person, project, issueType, accessLevelFlag, locale, SystemActions.NEW_CHILD,includeDropDown);
		return ctx;
	}


	/**
	 * Creates and configures new workItemContext
	 * @param person
	 * @param project
	 * @param issueType
	 * @param accessLevelFlag
	 * @param locale
	 * @param actionID
	 * @return
	 */
	private static WorkItemContext create(Integer person, Integer project, Integer issueType, boolean accessLevelFlag,
			 Locale locale, Integer actionID,boolean includeDropDown){

		WorkItemContext workItemContext = prepareWorkItemContext(person, locale, actionID, project, issueType);

		Set<Integer> presentFields = workItemContext.getPresentFieldIDs();

		//creating a new WorkItemBean
		TWorkItemBean workItemBean = new TWorkItemBean();
		workItemBean.setProjectID(project);
		workItemBean.setListTypeID(issueType);
		workItemBean.setAccessLevelFlag(accessLevelFlag);
		workItemBean.setOriginatorID(person);
		//set the workItemBean in workItemContext
		workItemContext.setWorkItemBean(workItemBean);

		//boolean projectDefaults = false;
		Integer prefill = null;
		TProjectBean projectBean = LookupContainer.getProjectBean(project);
		if (projectBean!=null) {
			prefill = ProjectConfigBL.getPrefillValue(projectBean);
			/*if (prefill!=null && prefill.intValue()==TProjectBean.PREFILL.PROJECTDEFAULT) {
				projectDefaults = true;
			}*/
		}

		prepareConfigsAndRestrictions(workItemContext, true);

		//set the workitem fields with default values
		setDefaultValues(workItemBean, presentFields, workItemContext.getFieldConfigs(),
				workItemContext.getFieldSettings(), projectBean, prefill);

		//resets the original workItem in workItemContext
		workItemContext.setWorkItemBeanOriginal(null);
		if(includeDropDown){
		//prepares a select context for loading the select datasources
			prepareDropDownContainerAndDependences(workItemContext, true, false, false);
		}

		SelectContext selectContext = new SelectContext();
		selectContext.setPersonID(workItemContext.getPerson());
		selectContext.setLocale(workItemContext.getLocale());
		selectContext.setWorkItemBean(workItemBean);
		selectContext.setCreate(true);
		//force the required system fields to a default value when they are not present in the screen
		forceSystemRequired(workItemBean, selectContext, workItemContext.getDropDownContainer(),includeDropDown);
		forceSystemDates(workItemBean, presentFields, locale);
		setComputedFields(workItemBean, workItemContext.getPresentFieldIDs(), workItemContext.getFieldConfigs(), workItemContext.getFieldSettings());
		return workItemContext;
	}

	/**
	 * Force the startDate and endDate according to the default settings independently whether they are on the screen or not
	 * @param workItemBean
	 * @param presentFields
	 * @param locale
	 */
	public static void forceSystemDates(TWorkItemBean workItemBean, Set<Integer> presentFields, Locale locale) {
		Set<Integer> systemDateFields = new HashSet<Integer>();
		systemDateFields.add(SystemFields.INTEGER_STARTDATE);
		systemDateFields.add(SystemFields.INTEGER_ENDDATE);
		systemDateFields.add(SystemFields.INTEGER_TOP_DOWN_START_DATE);
		systemDateFields.add(SystemFields.INTEGER_TOP_DOWN_END_DATE);
		for (Iterator<Integer> iterator = systemDateFields.iterator(); iterator.hasNext();) {
			Integer dateFieldID = iterator.next();
			if (presentFields.contains(dateFieldID) || workItemBean.getAttribute(dateFieldID)!=null) {
				//if field present on the screen then it is set already according to default settings
				iterator.remove();
			}
		}
		if (!systemDateFields.isEmpty()) {
			//set the default date value as it would be present on the screen
			Map<Integer, TFieldConfigBean> fieldConfigs = FieldRuntimeBL.getLocalizedFieldConfigs(systemDateFields,
					workItemBean.getProjectID(), workItemBean.getListTypeID(), locale);
			//get the field settings
			Map<String, Object> fieldSettings = FieldRuntimeBL.getFieldSettings(fieldConfigs);
			for (Integer fieldID : systemDateFields) {
				TFieldConfigBean validConfig = fieldConfigs.get(fieldID);
				IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
				if (fieldTypeRT!=null) {
					try {
						fieldTypeRT.processDefaultValue(fieldID, null, validConfig.getObjectID(), fieldSettings, workItemBean);
					} catch (Exception e) {
						LOGGER.warn("Processing the default date value for field " + fieldID + " failed with " + e.getMessage());
						LOGGER.debug(ExceptionUtils.getStackTrace(e));
					}
				}
			}
		}
	}

	/**
	 * When a required system field is not present in the new issue screen then it should be set to a default value
	 * The hopefully only exception is the synopsis which will not be set to a default value even if it is null
	 * An error message will be shown in this case
	 * @param workItemBean
	 */
	private static void forceSystemRequired(TWorkItemBean workItemBean, SelectContext selectContext, DropDownContainer dropDownContainer,boolean includeDropDown) {
		Set<Integer> requiredSystemFields = getRequiredSystemFieldsList();
		for (Integer fieldID : requiredSystemFields) {
			if (workItemBean.getAttribute(fieldID)==null) {
				IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
				//for manager and responsible (try to get the defaults for project)
				//try also for synopsis but it will remain null
				fieldTypeRT.processDefaultValue(fieldID, null, null, null, workItemBean);
				if (includeDropDown&&workItemBean.getAttribute(fieldID)==null) {
					//for state, priority the loadCreateDataSource() method
					//also sets a value for the field in workItem
					selectContext.setFieldID(fieldID);
					fieldTypeRT.processLoadDataSource(selectContext, dropDownContainer);
				}
			}
		}
	}

	/**
	 *
	 * The custom label field values are the same in edit context as in  create context
	 * (Loaded not from TAttributeValue but from settings)
	 * @param workItem
	 * @param showOnScreenList
	 * @param fieldConfigs
	 * @param fieldSettings
	 */
	private static void setComputedFields(TWorkItemBean workItem, Set<Integer> showOnScreenList,
			Map<Integer, TFieldConfigBean> fieldConfigs, Map<String, Object> fieldSettings) {
		for (Integer fieldID : showOnScreenList) {
			TFieldConfigBean validConfig = fieldConfigs.get(fieldID);
			if (validConfig!=null) {
				IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
				if (fieldTypeRT!=null && fieldTypeRT.isComputed(fieldID, null)) {
					try {
						fieldTypeRT.processDefaultValue(fieldID, null, validConfig.getObjectID(), fieldSettings, workItem);
					} catch (Exception e) {
						LOGGER.warn("Processing the default value for custom label field " + fieldID + " failed with " + e.getMessage());
						LOGGER.debug(ExceptionUtils.getStackTrace(e));
					}
				}
			}
		}
	}

	/**
	 * Sets the dropdown entries from the session if such a workItem exists in the session
	 * The other fields are set to their default values
	 * @param workItem
	 * @return
	 */
	private static void setDefaultValues(TWorkItemBean workItem, Set<Integer> showOnScreenList,
			Map<Integer, TFieldConfigBean> fieldConfigs, Map<String, Object> fieldSettings, TProjectBean projectBean, Integer prefill) {
		TWorkItemBean lastWorkItemBean = null;
		boolean prefillByLastItem = true;
		if (prefill!=null && prefill.intValue()==TProjectBean.PREFILL.PROJECTDEFAULT && projectBean!=null) {
			prefillByLastItem = false;
		} else {
			lastWorkItemBean = workItemDAO.loadLastCreatedByProjectIssueType(
					workItem.getOriginatorID(), workItem.getProjectID(), workItem.getListTypeID());
		}
		Set<Integer> defaultSettableFields = getSystemPrefillFields();
		if (showOnScreenList!=null) {
			for (Integer fieldID : showOnScreenList) {
				try {
					TFieldConfigBean validConfig = fieldConfigs.get(fieldID);
					if (defaultSettableFields.contains(fieldID)) {
						if (prefillByLastItem && lastWorkItemBean!=null) {
							//typically not a problem when the default value set is not contained in the resulting dropDown list
							//because after the first submit the new submitted value will be stored.
							//The problem can be when the dropdown list is empty, consequently no value will be submitted,
							//which means it remains to the default value set from defaultWorkItemBean
							workItem.setAttribute(fieldID, null, lastWorkItemBean.getAttribute(fieldID, null));
						} else {
							if (SystemFields.INTEGER_BUILD.equals(fieldID)) {
								IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
								fieldTypeRT.processDefaultValue(fieldID, null, validConfig.getObjectID(), fieldSettings, workItem);
							} else {
								workItem.setAttribute(fieldID, null, ProjectConfigBL.getDefaultFieldValueForProject(fieldID,
									projectBean, workItem.getOriginatorID(), workItem.getListTypeID(), validConfig));
							}
						}
					} else {
						IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
						if (fieldTypeRT!=null) {
							fieldTypeRT.processDefaultValue(fieldID, null, validConfig.getObjectID(), fieldSettings, workItem);
						}
					}
				} catch (Exception e) {
					LOGGER.warn("Processing the default value for field " + fieldID + " failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
		}
	}

	/**
	 * Get those fields which will be initialized with default values according
	 * to the previously created item (which had the same project and issueType)
	 * @return
	 */
	private static Set<Integer> getSystemPrefillFields() {
		Set<Integer> defaultFields = new HashSet<Integer>();
		defaultFields.add(SystemFields.INTEGER_MANAGER);
		defaultFields.add(SystemFields.INTEGER_RESPONSIBLE);
		defaultFields.add(SystemFields.INTEGER_RELEASENOTICED);
		defaultFields.add(SystemFields.INTEGER_RELEASESCHEDULED);
		defaultFields.add(SystemFields.INTEGER_PRIORITY);
		defaultFields.add(SystemFields.INTEGER_SEVERITY);
		defaultFields.add(SystemFields.INTEGER_BUILD);
		return defaultFields;
	}

	/**
	 * Creates a new workItemContext and sets the person and locale
	 * If action is specified sets also the action, screen and present fields
	 * Otherwise the present fields should be specified thereafter explicitly
	 * @param person
	 * @param locale
	 * @param actionID
	 * @param projectID
	 * @param listTypeID
	 * @return
	 */
	private static WorkItemContext prepareWorkItemContext(Integer person, Locale locale,
			Integer actionID, Integer projectID, Integer listTypeID) {
		WorkItemContext workItemContext = new WorkItemContext();
		workItemContext.setPerson(person);
		workItemContext.setLocale(locale);
		//workItemContext.setProject(projectBean);
		List<TFieldBean> presentFieldsBeans = null;
		//load the screen based on the project and issueType of the workItemBean
		if (actionID!=null) {
			workItemContext.setActionID(actionID);
			//it is not null in case of single issue change (for mass operation no screen is needed)
			Integer	screenID=ScreenRuntimeBL.getInstance().findScreenID(actionID, projectID, listTypeID);
			if (screenID!=null) {
				workItemContext.setScreenID(screenID);
				//get the present fields from the screen
				presentFieldsBeans=FieldBL.loadAllFields(screenID);

				//add the project and issueType from the first screen as present on the second screen
				boolean projectIncluded = false;
				boolean issueTypeIncluded = false;
				boolean itemIDIncluded = false;
				boolean synopsysIncluded = false;
				boolean stateIncluded = false;
				if (presentFieldsBeans!=null) {
					for (TFieldBean fieldBean : presentFieldsBeans) {
						if (fieldBean.getObjectID().equals(SystemFields.INTEGER_PROJECT)) {
							projectIncluded = true;
						}
						if (fieldBean.getObjectID().equals(SystemFields.INTEGER_ISSUETYPE)) {
							issueTypeIncluded = true;
						}
						if (fieldBean.getObjectID().equals(SystemFields.INTEGER_ISSUENO)) {
							itemIDIncluded = true;
						}
						if (fieldBean.getObjectID().equals(SystemFields.INTEGER_SYNOPSIS)) {
							synopsysIncluded = true;
						}
						if (fieldBean.getObjectID().equals(SystemFields.INTEGER_STATE)) {
							stateIncluded = true;
						}
						if (projectIncluded && issueTypeIncluded && itemIDIncluded && synopsysIncluded&&stateIncluded) {
							break;
						}
					}
					if (!projectIncluded) {
						TFieldBean fieldBean = FieldBL.loadByPrimaryKey(SystemFields.INTEGER_PROJECT);
						if (fieldBean!=null) {
							presentFieldsBeans.add(fieldBean);
						}
					}
					if (!issueTypeIncluded) {
						TFieldBean fieldBean = FieldBL.loadByPrimaryKey(SystemFields.INTEGER_ISSUETYPE);
						if (fieldBean!=null) {
							presentFieldsBeans.add(fieldBean);
						}
					}
					if (!itemIDIncluded) {
						TFieldBean fieldBean = FieldBL.loadByPrimaryKey(SystemFields.INTEGER_ISSUENO);
						if (fieldBean!=null) {
							presentFieldsBeans.add(fieldBean);
						}
					}
					if (!synopsysIncluded) {
						TFieldBean fieldBean = FieldBL.loadByPrimaryKey(SystemFields.INTEGER_SYNOPSIS);
						if (fieldBean!=null) {
							presentFieldsBeans.add(fieldBean);
						}
					}
					if (!stateIncluded) {
						TFieldBean fieldBean = FieldBL.loadByPrimaryKey(SystemFields.INTEGER_STATE);
						if (fieldBean!=null) {
							presentFieldsBeans.add(fieldBean);
						}
					}
				}
			}
		}
		if (presentFieldsBeans!=null) {
			Set<Integer> presentFieldIDs=new HashSet<Integer>();
			Map<Integer, TFieldBean> presentFieldBeansMap=new HashMap<Integer, TFieldBean>();
			for (TFieldBean fieldBean : presentFieldsBeans) {
				presentFieldIDs.add(fieldBean.getObjectID());
				presentFieldBeansMap.put(fieldBean.getObjectID(), fieldBean);
			}
			workItemContext.setPresentFieldIDs(presentFieldIDs);
			workItemContext.setPresentFieldBeans(presentFieldBeansMap);
		}
		return workItemContext;
	}


	/**
	 * Sets the fieldConfigs, fieldSettings and fieldRestrictions on the workItemContext
	 * @param workItemContext
	 * @param create
	 */
	private static void prepareConfigsAndRestrictions(WorkItemContext workItemContext, boolean create) {
		Date start = null;
		if (LOGGER.isDebugEnabled()) {
			start = new Date();
		}
		TWorkItemBean workItemBean = workItemContext.getWorkItemBean();
		Set<Integer> presentFieldIDs = workItemContext.getPresentFieldIDs();
		if (presentFieldIDs==null) {
			presentFieldIDs = new HashSet<Integer>();
			workItemContext.setPresentFieldIDs(presentFieldIDs);
		}
		if (workItemBean==null) {
			LOGGER.warn("WorkItemBean not set at the prepareConfigsAndRestrictions ");
			return;
		}
		//get the best matching field configurations
		//Set requiredSystemFields = null;
		if (create) {
			//add the configs also for the required system fields even if they are not present on the screen
			//(it is important just by creating a new issue screen, while for existing issues when a
			//required system field is not present on the screen it means that the field remains unchanged)
			//actually it is needed just for synopsis
			//because all other required system fields receive a "forced" default value when not present on the screen,
			presentFieldIDs.addAll(getRequiredSystemFieldsList());
			/*if (projectDefaults) {
				//if project default values is set for the project include the default
				//releases and class even if not present on the screen
				presentFieldIDs.addAll(getNotRequiredProjectDefaultSystemFieldsList());
			}*/

		}
		Map<Integer, TFieldConfigBean> fieldConfigs = FieldRuntimeBL.getLocalizedFieldConfigs(presentFieldIDs,
				workItemBean.getProjectID(), workItemBean.getListTypeID(), workItemContext.getLocale());
		//set the field configurations in workItemContext for later use
		workItemContext.setFieldConfigs(fieldConfigs);

		//get the field settings
		Map<String, Object> fieldSettings = FieldRuntimeBL.getFieldSettings(fieldConfigs);
		//set the field settings in workItemContext for later use
		workItemContext.setFieldSettings(fieldSettings);

		//get the field restrictions
		Map<Integer, Integer> fieldRestrictions = AccessBeans.getRestrictedFieldsAndBottomUpDates(workItemContext);
		workItemContext.setFieldRestrictions(fieldRestrictions);
		if (LOGGER.isDebugEnabled() && start!=null) {
			Date end = new Date();
			LOGGER.debug("Loading configs and restrictions for item lasted " + new Long(end.getTime()-start.getTime()).toString() + " ms");
		}
	}

	/**
	 * Sets the dropDown conatiner, and the dependences map
	 * Resets the cons/inf and accounting data in order to force
	 * the loading of them by selecting the corresponding tab the first time
	 * @param workItemContext
	 * @param isCreate
	 * @param isView
	 * @param projectChanged
	 */
	private static void prepareDropDownContainerAndDependences(WorkItemContext workItemContext,
			boolean isCreate, boolean isView, boolean projectChanged) {
		Date start = null;
		if (LOGGER.isDebugEnabled()) {
			start = new Date();
		}
		TWorkItemBean workItemBean = workItemContext.getWorkItemBean();
		Set<Integer> presentFieldIDs = workItemContext.getPresentFieldIDs();
		if (workItemBean==null) {
			LOGGER.warn("WorkItemBean not set at the prepareConfigsAndRestrictions ");
			return;
		}
		//prepares a select context for loading the select data sources
		SelectContext selectContext = new SelectContext();
		selectContext.setPersonID(workItemContext.getPerson());
		selectContext.setLocale(workItemContext.getLocale());
		selectContext.setWorkItemBean(workItemBean);
		selectContext.setView(isView);

		//loads the dropDownContainer for edit/move
		workItemContext.setDropDownContainer(loadDataSource(selectContext, presentFieldIDs,
				workItemContext.getFieldConfigs(), isCreate, projectChanged));

		//sets the dependences map for later use in refresh
		//workItemContext.setDependencesMap(getDependences(presentFieldIDs));

		//force the cons/inf data to be loaded first time
		//by changing to cons/inf tab (prepare method)
		workItemContext.setConsInfShow(null);
		//loads the consultant/informant persons/groups
		//ConsInfBL.loadConsInfFromDb(workItemBean.getObjectID(), workItemContext.getPerson(), workItemContext.getConsInfShow());

		//force the cost/effort data to be loaded first time
		//by changing to cost/effort tab (prepare method)
		workItemContext.setAccountingForm(null);
		//AccountingBL.loadAllFromDb(workItemContext.getAccountingForm(), workItemBean,
		//		personDAO.loadByPrimaryKey1(workItemContext.getPerson()), workItemContext.getLocale());
		if (LOGGER.isDebugEnabled() && start!=null) {
			Date end = new Date();
			LOGGER.debug("preparing drop downs for item lasted " + new Long(end.getTime()-start.getTime()).toString() + " ms");
		}
	}

	public static void refreshField(Integer fieldID, WorkItemContext workItemContext) {
		Map<Integer, TFieldConfigBean> fieldConfigs = workItemContext.getFieldConfigs();
		Map<String, Object> fieldSettings = workItemContext.getFieldSettings();
		TWorkItemBean workItemBean = workItemContext.getWorkItemBean();
		TFieldConfigBean validConfig = fieldConfigs.get(fieldID);
		SelectContext selectContext = new SelectContext();
		selectContext.setPersonID(workItemContext.getPerson());
		selectContext.setLocale(workItemContext.getLocale());
		selectContext.setWorkItemBean(workItemBean);
		selectContext.setFieldID(fieldID);
		selectContext.setFieldConfigBean(validConfig);
		selectContext.setCreate(workItemBean.getObjectID()==null);
		selectContext.setDependencyRefresh(true);
		IFieldTypeRT fieldType = FieldTypeManager.getFieldTypeRT(fieldID);
		fieldType.processLoadDataSource(selectContext, workItemContext.getDropDownContainer());
		fieldType.processBeforeSave(fieldID, null, validConfig.getObjectID(), fieldSettings, workItemBean,
				workItemContext.getWorkItemBeanOriginal(), workItemContext.getExtraFormValues());
		/*refreshField(selectContext, fieldID, null,
				workItemBean, workItemContext.getDropDownContainer(), validConfig.getObjectID(), fieldSettings);*/
	}

	/**
	 * What should be done by refreshing the dependent control:
	 * 1. changing the datasource for a dependent select
	 * 2. recalculate a dependent calculated field
	 * @param fieldID
	 * @param parameterCode
	 * @param workItemBean
	 */
	/*private static void refreshField(SelectContext selectContext, Integer fieldID, Integer parameterCode,
			TWorkItemBean workItemBean, DropDownContainer dropDownContainer, Integer configID, Map<String, Object> fieldSettings) {
		IFieldTypeRT fieldType = FieldTypeManager.getFieldTypeRT(fieldID);
		//load the create datasource (not the edit), independently whether the refresh
		//occurred during the create or edit of and issue:
		//the current option may disappear if not member of the refreshed datasource
		//fieldType.processDefaultValue(fieldID, parameterCode, configID, fieldSettings, workItemBean);
		fieldType.processLoadDataSource(selectContext, dropDownContainer);

		if (fieldType instanceof ILoad && ((ILoad)fieldType).isCalculated(fieldID, parameterCode)) {
			ILoad calculate = (ILoad)fieldType;
			calculate.loadAttribute(fieldID, parameterCode, workItemBean, null);
		}
	}*/

	/**
	 * Should be called by changing a field with dependent fields
	 * Commented out because we use AJAX refresh as many times as needed
	 * and we do not refresh (using recursive refreshes when it is the case) the entire page at once
	 * Comment it out if in the future entire refresh for the entire page is needed.
	 * @param srcFieldID
	 * @param srcParameterCode
	 * @param fieldParameterSet
	 */
	/*public static void refresh(Integer srcFieldID, Integer srcParameterCode, WorkItemContext workItemContext) {
		List presentFields = workItemContext.getPresentFields();
		Map fieldConfigs = workItemContext.getFieldConfigs();
		Map fieldSettings = workItemContext.getFieldSettings();
		TWorkItemBean workItemBean = workItemContext.getWorkItemBean();
		//valid config for the dependent field
		TFieldConfigBean validConfig;

		//fieldID of the dependent field
		Integer dependentFieldID;
		//parameter code of the dependent subfield
		Integer destParameterCode;
		//old attribute value
		Object oldValue;
		//new attribute value
		Object newValue;

		String dependencyKey = MergeUtil.mergeKey(srcFieldID, srcParameterCode);
		String dependentKey;
		String[] dependentKeyParts;
		List dependencesList = (List)workItemContext.getDependencesMap().get(dependencyKey);

		if (dependencesList!=null) {
			//load only those custom fields which are really needed according to the current screen
			//add the fields in the custom map from workItem
			Iterator itrDependencesList = dependencesList.iterator();
			while (itrDependencesList.hasNext()) {
				dependentKey = (String)itrDependencesList.next();
				dependentKeyParts = MergeUtil.splitKey(dependentKey);
				dependentFieldID = new Integer(dependentKeyParts[0]);
				if (dependentKeyParts.length > 1) {
					destParameterCode = new Integer(dependentKeyParts[1]);
				} else {
					destParameterCode = null;
				}
				// try to reload the dependents with higher priority because lower
				// priority dependents can depend also on higher priority dependents
				if (presentFields.contains(dependentFieldID)) { //&& (!fieldParameterSet.contains(fieldParameterBean)))
					validConfig = (TFieldConfigBean)fieldConfigs.get(srcFieldID);
					//refresh the dependent field itself
					oldValue = workItemBean.getAttribute(dependentFieldID, destParameterCode);
					SelectContext selectContext = new SelectContext();
					selectContext.setPerson(workItemContext.getPerson());
					selectContext.setLocale(workItemContext.getLocale());
					selectContext.setWorkItemBean(workItemBean);
					selectContext.setFieldID(dependentFieldID);
					selectContext.setConfigID(validConfig.getObjectID());
					selectContext.setParameterCode(destParameterCode);

					refreshField(selectContext, dependentFieldID, destParameterCode,
							workItemBean, workItemContext.getDropDownContainer(), validConfig.getObjectID(), fieldSettings);
					//new value would differ from the oldValue if it is set expicitely
					//to a new value in the refreshField() -> i.e. processLoadDataSource().
					//But if we choose the AJAX refresh then the new value is selected
					//according to the isDefault value if the resulting TOptionBeans see RefreshList.refreshCascade()
					//and in the processLoadDataSource() there is no need to set it explicitely.
					//The above behaviour is typically for cascading selects.
					//For other custom fields it might make sense to set the newValue exlicitely
					newValue = workItemBean.getAttribute(dependentFieldID, destParameterCode);

					//The following code (cascading the refreshes along the dependency hierarchy)
					//would make sense if a refresh results in reloading the entire page
					//BUT if the refreshed dataSource is sent through AJAX (which is the case now)
					//then the dataSource for cascading fields should be refreshed
					//also through AJAX and sent back to the browser
					//(it makes not very much sense to refresh also the cascading fields now
					//but not send the refreshed datasources of the cascading fields/parts)
					//typical case SelectParentChildGrandchild: parent changes child dataSource
					//but it should cascade to change the grandChild datasource

					//nevertheless the code below is not commented out because other custom fields might use it


					//after refreshing the field it can happen that there are dependent fields on the refreshed field (child->grandchild)
					IFieldTypeRT fieldTypeRT = FieldTypeManager.getInstance().getType(dependentFieldID).getFieldTypeRT();
					if (destParameterCode!=null) {
						//whether it is a composite field
						CustomCompositeFieldTypeRT compositeFieldTypeRT = (CustomCompositeFieldTypeRT)fieldTypeRT;
						fieldTypeRT = compositeFieldTypeRT.getCustomFieldType(destParameterCode.intValue());
					}
					//the cascading selects will not execute the cascading refresh below because the oldValue is equal with the newValue
					//(the new value isn't set explicit in the refreshField, but using AJAX)
					if (fieldTypeRT!=null && fieldTypeRT.valueModified(oldValue, newValue)) {
						refresh(dependentFieldID, destParameterCode, workItemContext);
					}

				}
			}
		}
	}*/


	/**
	 * Saving of a moved workItemBean
	 * @param workItemContext
	 * @param errorList
	 * @param withNotify for single issue the drop down lists and some other data is needed,
	 * 	but for mass operation this data is not needed
	 */
	public static Integer move(WorkItemContext workItemContext, boolean confirm, List<ErrorData> errorList, boolean withNotify/*, boolean cascadeChanges*/) {
		TWorkItemBean workItemBean = workItemContext.getWorkItemBean();
		List<ErrorData> validationErrors = validate(workItemBean, workItemContext, confirm, false, null, false, true);
		if (!validationErrors.isEmpty()) {
			//no save but validation errors
			errorList.addAll(validationErrors);
			return null;
		}
		performSave(workItemContext, errorList, false, withNotify/*, cascadeChanges*/);
		TWorkItemBean workItemBeanOriginal = workItemContext.getWorkItemBeanOriginal();
		if (workItemBeanOriginal!=null) {
			Integer originalProjectID =  workItemBeanOriginal.getProjectID();
			Integer originalItemTypeID =  workItemBeanOriginal.getListTypeID();
			Integer modifiedProjectID = workItemBean.getProjectID();
			Integer modifiedItemTypeID = workItemBean.getListTypeID();
			boolean projectChanged = EqualUtils.notEqual(modifiedProjectID, originalProjectID);
			LOGGER.debug("Project changed: " + projectChanged);
			boolean itemTypeChanged = EqualUtils.notEqual(workItemBean.getListTypeID(), originalItemTypeID);
			LOGGER.debug("Item type changed: " + itemTypeChanged);
			if (workItemBean.isMoveChildren() && (projectChanged || itemTypeChanged)){
				List<TWorkItemBean> childrenList = ItemBL.getChildren(workItemBean.getObjectID());
				List<TWorkItemBean> filteredChildrenList = LoadItemIDListItems.getWorkItemBeansByWorkItemIDs(
						GeneralUtils.createIntArrFromIntegerArr(GeneralUtils.getBeanIDs(childrenList)),
						workItemContext.getPerson(), false, true, false);
				if (filteredChildrenList!=null) {
					for (TWorkItemBean childBean : filteredChildrenList) {
						childBean.setMoveChildren(true);
						workItemContext.setWorkItemBeanOriginal(childBean.copyShallow());
						if (projectChanged) {
							LOGGER.debug("Project changed for child " + childBean.getObjectID());
							childBean.setProjectID(modifiedProjectID);
						}
						if (itemTypeChanged && EqualUtils.equal(originalItemTypeID, childBean.getListTypeID())) {
							LOGGER.debug("Item type changed for child " + childBean.getObjectID());
							childBean.setListTypeID(modifiedItemTypeID);
						}
						workItemContext.setWorkItemBean(childBean);
						move(workItemContext, confirm, errorList, false/*, true*/);
					}
				}
			}
		}
		return workItemContext.getWorkItemBean().getObjectID();
	}


	/**
	 * Saving of a workItemBean
	 * @param workItemContext
	 * @param errorList
	 * @param withNotify for single issue the dropdown lists and some other data is needed,
	 * 	but for mass operation this data is not needed
	 */
	public static Integer save(WorkItemContext workItemContext, boolean confirm, List<ErrorData> errorList, boolean withNotify) {
		TWorkItemBean workItemBean = workItemContext.getWorkItemBean();
		List<ErrorData> validationErrors = validate(workItemBean, workItemContext, confirm, false, null, false, true);
		if (!validationErrors.isEmpty()) {
			//no save but validation errors
			errorList.addAll(validationErrors);
			return null;
		}
		performSave(workItemContext, errorList, false, withNotify/*, cascadeChanges*/);
		return workItemContext.getWorkItemBean().getObjectID();
	}

	/**
	 * Saving of a workItemBean without validation
	 * @param workItemContext
	 * @param errorList
	 * @param withNotify for single issue the dropdown lists and some other data is needed,
	 * 	but for mass operation this data is not needed
	 */
	public static Integer saveWithoutValidate(WorkItemContext workItemContext, List<ErrorData> errorList, boolean withNotify) {
		performSave(workItemContext, errorList, false, withNotify/*, false*/);
		return workItemContext.getWorkItemBean().getObjectID();
	}

	/**
	 * Saving of a single field
	 * @param personID
	 * @param workItemID
	 * @param locale
	 * @param fieldID
	 * @param newValue
	 * @param errorList
	 * @return
	 */
	public static Integer saveOneField(Integer personID, Integer workItemID, Locale locale, boolean confirm,
			Integer fieldID, Object newValue, boolean withNotify,
			Map<String, Object> contextInformation, List<ErrorData> errorList) {
		WorkItemContext workItemContext = editOneField(personID, workItemID, locale, fieldID);
		workItemContext.setExtraFormValues(contextInformation);
		TWorkItemBean workItemBean = workItemContext.getWorkItemBean();
		workItemBean.setAttribute(fieldID, null, newValue);
		save(workItemContext, confirm, errorList, withNotify);
		return workItemBean.getObjectID();
	}

	/**
	 * Copying a workItemBean
	 * @param workItemContext
	 * @param selectedChildren
	 * @param originalToCopyWorkItemIDs
	 * @param errorList
	 * @param validate always called with true, except by copying the children (the recursive call)
	 * (the unmodified children are not validated, but simply copied)
	 * @param withNotify
	 * @return
	 */
	public static Integer copy(WorkItemContext workItemContext, Set<Integer> selectedChildren,
			Map<Integer, Integer> originalToCopyWorkItemIDs, List<ErrorData> errorList, boolean validate, boolean withNotify) {
		TWorkItemBean workItemBeanOriginal = workItemContext.getWorkItemBeanOriginal();
		if (workItemBeanOriginal==null) {
			return null;
		}
		TWorkItemBean workItemBeanCopy = workItemContext.getWorkItemBean();
		if (validate) {
			List<ErrorData> validationErrors = validate(workItemBeanCopy, workItemContext, false, true, null, false, true);
			if (!validationErrors.isEmpty()){
				//no save but validation errors
				errorList.addAll(validationErrors);
				return null;
			}
		}
		if (workItemBeanCopy.isDeepCopy()) {
			if (workItemBeanCopy.getObjectID()==null) {
				//if copy with edit the objectID was explicitly set to null
				//set it back for deep copy
				workItemBeanCopy.setObjectID(workItemBeanOriginal.getObjectID());
			}
			workItemBeanCopy = workItemBeanCopy.copyDeep(); // copy the original
		} else {
			// create a new one and fill it from the form
			workItemBeanCopy = workItemBeanCopy.copyShallow(); // copy the original
			//the shallow copy copies all attributes (also new and objectID)
			//set the bean as new
			workItemBeanCopy.setNew(true);
			//reset the original objectID
			workItemBeanCopy.setObjectID(null);
		}
		Integer originalParentID = workItemBeanCopy.getSuperiorworkitem();
		if (originalParentID!=null) {
			if (originalToCopyWorkItemIDs!=null && originalToCopyWorkItemIDs.get(originalParentID)!=null) {
				//the parent was selected and already copied
				workItemBeanCopy.setSuperiorworkitem(originalToCopyWorkItemIDs.get(originalParentID));
			}
		}
		workItemContext.setWorkItemBean(workItemBeanCopy);
		//prepare the workItemBeanCopy
		//set the original workItem as parent of the copied workItem
		/*if (copyAsChild) {
			workItemBeanCopy.setSuperiorworkitem(workItemContext.getWorkItemBeanOriginal().getObjectID());
		}*/
		//overwrite the originator of the original item
		//with the id of the currently logged in person
		workItemBeanCopy.setOriginatorID(workItemContext.getPerson());
		//set the workItemBeanOriginal to null again in order to save the custom attributes
		//(when the values are the same in the original and new workItem no save happens)
		//workItemContext.setWorkItemBeanOriginal(null);

		if (workItemBeanCopy.isCopyChildren()){
			performSave(workItemContext, errorList, true, false/*, false*/);
		}else{
			performSave(workItemContext, errorList, true, withNotify/*, false*/);
		}
		if (workItemBeanCopy.isCopyAttachments()) {
			if (workItemBeanCopy.getObjectID()==null) {
				//if copy with edit the objectID was explicitly set to null
				//set it back for deep copy
				workItemBeanCopy.setObjectID(workItemBeanOriginal.getObjectID());
			}
			try {
				AttachBL.copyAttachments(workItemBeanOriginal.getObjectID(), workItemBeanCopy.getObjectID(),
						workItemContext.getPerson().toString(),workItemContext.getPerson());
			} catch (AttachBLException e) {
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}
		}
		if (workItemBeanCopy.isCopyWatchers()) {
			List<TPersonBean> consulted = PersonBL.getDirectConsultants(workItemBeanOriginal.getObjectID());
			if (consulted!=null) {
				for (TPersonBean personBean : consulted) {
					ConsInfBL.insertByWorkItemAndPersonAndRaciRole(workItemBeanCopy.getObjectID(), personBean.getObjectID(), RaciRole.CONSULTANT);
				}
			}
			List<TPersonBean> informed = PersonBL.getDirectInformants(workItemBeanOriginal.getObjectID());
			if (informed!=null) {
				for (TPersonBean personBean : informed) {
					ConsInfBL.insertByWorkItemAndPersonAndRaciRole(workItemBeanCopy.getObjectID(), personBean.getObjectID(), RaciRole.INFORMANT);
				}
			}
		}
		if (workItemBeanCopy.isCopyChildren()){
			List<TWorkItemBean> childrenList = ItemBL.getChildren(workItemBeanOriginal.getObjectID());
			List<TWorkItemBean> filteredChildrenList = LoadItemIDListItems.getWorkItemBeansByWorkItemIDs(
					GeneralUtils.createIntArrFromIntegerArr(GeneralUtils.getBeanIDs(childrenList)),
					workItemContext.getPerson(), false, true, false);
			Iterator<TWorkItemBean> itrChildren = filteredChildrenList.iterator();
			while(itrChildren.hasNext()){
				TWorkItemBean childBean = itrChildren.next();
				if (selectedChildren==null || !selectedChildren.contains(childBean.getObjectID())) {
					//avoid copying the selected bean two times: once as selected workItem and once
					//as a child of another selected workItem.
					//it will be copied only as selected workItem to allow changing of some attributes before copy
					childBean.setSuperiorworkitem(workItemBeanCopy.getObjectID());
					childBean.setDeepCopy(workItemBeanCopy.isDeepCopy());
					childBean.setCopyAttachments(workItemBeanCopy.isCopyAttachments());
					childBean.setCopyWatchers(workItemBeanCopy.isCopyWatchers());
					childBean.setCopyChildren(true);
					workItemContext.setWorkItemBeanOriginal(childBean.copyShallow());
					//if the original children were the in same project as the original parent
					//and the project was changed for the parent copy then change the project
					//for the child copies also
					Integer originalParentProject = workItemBeanOriginal.getProjectID();
					Integer originalChildProject = childBean.getProjectID();
					Integer newParentProject = workItemBeanCopy.getProjectID();
					if (EqualUtils.equal(originalParentProject, originalChildProject) &&
							EqualUtils.notEqual(originalParentProject, newParentProject)) {
						//childBean.setProjectCategoryID(workItemBeanCopy.getProjectCategoryID());
						//set for recursive EqualUtils test
						childBean.setProjectID(newParentProject);
					}
					workItemContext.setWorkItemBean(childBean);
					copy(workItemContext, selectedChildren, originalToCopyWorkItemIDs, errorList, false, withNotify);
				}
			}
			//set the context back to the parent: needed only when a single issue will be copied (not bulk copy)
			workItemContext.setWorkItemBean(workItemBeanCopy);
			workItemContext.setWorkItemBeanOriginal(workItemBeanOriginal);
		}
		if (originalToCopyWorkItemIDs!=null) {
			originalToCopyWorkItemIDs.put(workItemBeanOriginal.getObjectID(), workItemBeanCopy.getObjectID());
		}
		return workItemBeanCopy.getObjectID();
	}

	/**
	 * Save the custom  attributes for the workItem
	 * @param workItemBean
	 */
	private static void saveWorkItemCustomAttributes(TWorkItemBean workItemBean,
			TWorkItemBean workItemBeanOriginal, Set<Integer> presentFields) {
		if (presentFields!=null) {
			for (Integer fieldID : presentFields) {
				IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
				fieldTypeRT.processSave(fieldID, null, workItemBean, workItemBeanOriginal);
			}
		}
	}

	/**
	 * Save the custom  attributes for the workItem: all custom fields should
	 * be saved independently whether they appear in the screen or not
	 * @param workItemBean
	 */
	private static void saveWorkItemCustomAttributesByCopy(TWorkItemBean workItemBean,
			TWorkItemBean workItemBeanOriginal) {
		Set<Integer> customFields = workItemBean.getCustomFieldIDSet();
		if (customFields!=null) {
			for (Integer fieldID : customFields) {
				IFieldTypeRT fieldType = FieldTypeManager.getFieldTypeRT(fieldID);
				if (fieldType!=null) {
					//if adding consultants and informants by mass copy the fieldType is null
					fieldType.processSave(fieldID, null, workItemBean, workItemBeanOriginal);
				}
			}
		}
	}

	/**
	 * Validate the workItem fields (system and custom)
	 * (also the system fields could be validated if they extend IValidate)
	 * @param workItemBean
	 * @param workItemContext
	 * @param isCopy
	 * @param applyLinkTypeValidation validation conditioned by link
	 * 	types will be applied only by saving a single issue, because during import
	 * 	it may give a validation error in a certain moment but thereafter
	 *  by modifying also the linked item the resulting state would be valid
	 * @return
	 */
	public static List<ErrorData> validate(TWorkItemBean workItemBean,
			WorkItemContext workItemContext, boolean confirm, boolean isCopy, List<Integer> selectedChildrenList,
			boolean parentIsSelected, boolean applyLinkTypeValidation) {
		List<ErrorData> validationErrors = new ArrayList<ErrorData>();
		TWorkItemBean workItemBeanOriginal = workItemContext.getWorkItemBeanOriginal();
		Integer personID = workItemContext.getPerson();
		Set<Integer> presentFields = workItemContext.getPresentFieldIDs();
		Map<Integer, TFieldConfigBean> fieldConfigs = workItemContext.getFieldConfigs();
		Map<String, Object> fieldSettings = workItemContext.getFieldSettings();
		Locale locale = workItemContext.getLocale();
		boolean isCreate = isCreate(workItemBean, isCopy);
		/*---------------------------------------------------------
		 * create or copy: check permission for create.
		 *---------------------------------------------------------*/
		if (isCreate || isCopy) {
			if (!AccessBeans.isAllowedToCreate(personID, workItemBean.getProjectID(),
							workItemBean.getListTypeID())) {
				ErrorData errorData = new ErrorData("item.err.createNotAllowed");
				validationErrors.add(errorData);
				return validationErrors;
			}
		}

		//1. check for required fields and consistency between fields

		//a. check for required system fields independently whether they are present or not on the screen!
		validationErrors.addAll(requiredValidate(workItemBean, fieldConfigs, presentFields));

		//b. hardcoded business logic validations which are not covered by the other validators
		validationErrors.addAll(businessLogicValidate(workItemBean, workItemBeanOriginal, personID, locale));

		//c. check whether close is allowed (when it is the case)
		validationErrors.addAll(closePossibleValidate(workItemBean, workItemBeanOriginal, confirm, selectedChildrenList, parentIsSelected, personID, workItemContext.getLocale()));

		//d. execute the plugin issue link types dependent validation
		if (applyLinkTypeValidation) {
			validationErrors.addAll(linkTypeValidation(workItemContext, workItemBean, workItemBeanOriginal, confirm, personID, locale));
		}

		//e. workflow validation
		if (!isCopy) {
			//a worflow script may contain a copyItem method which would lead to infinite cycle of creating copies.
			//so to be sure we should not execute workflow activities by copy
			List<ErrorData> workflowErrors = StatusWorkflow.executeWorkflowActivity(workItemContext);
			if (workflowErrors!=null && !workflowErrors.isEmpty()) {
				LOGGER.debug("Workflow activity error");
				validationErrors.addAll(workflowErrors);
			}
		}

		//2. now loop over the field specific validators
		//fieldConfigs == null for issue created through email submission
		if (fieldConfigs!=null) {
			for (Integer fieldID : presentFields) {
				IFieldTypeRT fieldType = FieldTypeManager.getFieldTypeRT(fieldID);
				TFieldConfigBean validConfig = fieldConfigs.get(fieldID);
				if (validConfig==null) {
					//if the a present field was added after the load(...) was called
					//then the fieldConfigs and  fieldSetting for these fields are not loaded,
					//but they are probably not needed because  they were validated previously elsewhere.
					//see MoveItemAction.editMoved() for post-added present field,
					continue;
				}
				Object fieldSetting=null;
				if(fieldSettings!=null) {
					fieldSetting = fieldSettings.get(MergeUtil.mergeKey(fieldID, null));
				}
				//get the defined validators for the fieldType
				Map<Integer, List<Validator>> validatorsMap = fieldType.procesLoadValidators(fieldID,
						validConfig, null,fieldSetting,workItemBean);
				if (validatorsMap!=null) {
					for (Integer parameterCode : validatorsMap.keySet()) {
						List<Validator> validatorsList = validatorsMap.get(parameterCode);
						if (Integer.valueOf(0).equals(parameterCode)) {
							parameterCode = null;
						}
						if (validatorsList!=null) {
							for (Validator validator : validatorsList) {
								Object value=workItemBean.getAttribute(fieldID,parameterCode);
								ErrorData errorData=validator.validateField(value);
								if (errorData!=null){
									errorData.setFieldID(fieldID);
									List<ErrorParameter> resourceParameters=errorData.getResourceParameters();
									if (resourceParameters==null) {
										resourceParameters = new LinkedList<ErrorParameter>();
										errorData.setResourceParameters(resourceParameters);
									}
									resourceParameters.add(0, new ErrorParameter(validConfig.getLabel()));
									validationErrors.add(errorData);
									//the first failing validator shorts the next validators
									break;
								}
							}
						}
					}
				}
			}
		}
		return validationErrors;
	}

	/**
	 * Executes a field specific logic before saving the item
	 * @param workItemBean
	 * @param workItemContext
	 * @return
	 */
	private static void processBeforeSave(TWorkItemBean workItemBean,
			WorkItemContext workItemContext) {
		Set<Integer> presentFields = workItemContext.getPresentFieldIDs();
		Map<Integer, TFieldConfigBean> fieldConfigs = workItemContext.getFieldConfigs();
		Map<String, Object> fieldSettings = workItemContext.getFieldSettings();
		if (fieldConfigs!=null) {
			for (Integer fieldID : presentFields) {
				IFieldTypeRT fieldType = FieldTypeManager.getFieldTypeRT(fieldID);
				TFieldConfigBean validConfig = fieldConfigs.get(fieldID);
				if (validConfig==null) {
					//if the a present field was added after the load(...) was called
					//then the fieldConfigs and  fieldSetting for these fields are not loaded,
					//but they are probably not needed because  they were validated previously elsewhere.
					//see MoveItemAction.editMoved() for post-added present field,
					continue;
				}
				//get the defined validators for the fieldType
				fieldType.processBeforeSave(fieldID, null, validConfig.getObjectID(), fieldSettings, workItemBean,
						workItemContext.getWorkItemBeanOriginal(), workItemContext.getExtraFormValues());
			}
		}
	}

	/**
	 * Load the data source of the dropdowns by a create/edit/move operation
	 * Move is similar to edit except for project dependent dropdowns:
	 * the ReleaseNoticed, ReleaseScheduled, Subproject and Class should be loaded with dataSource for create
	 * and the rest of the dropdowns with dataSource for edit.
	 * The current ReleaseNoticed/ReleaseScheduled/Subproject/Class can't remain if the project has changed
	 * ReleaseNoticed/ReleaseScheduled
	 * 	-	edit: we should include actual release even if it is not included implicitly in datasorce
	 * 		for example because the release was closed
	 * 	-	move: if the project changed the actual release should not be included in the datasource.
	 * 		The user should be forced to choose from the releases related to the new project
	 * Subproject and Class: the edit and create data source differs only slightly because the current value is not included by edit
	 * 		So, it is not too important whether the edit- or the create dataSource will be loaded
	 * @param selectContext
	 * @param presentFields
	 * @param fieldConfigs
	 * @param projectChange
	 * @return
	 */
	private static DropDownContainer loadDataSource(SelectContext selectContext,
			Set<Integer> presentFields, Map<Integer, TFieldConfigBean> fieldConfigs, boolean isCreate, boolean projectChange) {
		DropDownContainer dropDownContainer = new DropDownContainer();
		for (Integer fieldID : presentFields) {
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
			if (fieldTypeRT!=null) {
				TFieldConfigBean validConfig = fieldConfigs.get(fieldID);
				selectContext.setFieldID(fieldID);
				selectContext.setFieldConfigBean(validConfig);
				selectContext.setParameterCode(null);
				selectContext.setCreate(isCreate);
				if (projectChange) {
					if (SystemFields.RELEASENOTICED==fieldID.intValue() ||
							SystemFields.RELEASESCHEDULED==fieldID.intValue()) {
						//as a result of a project change the old values for releases
						//should not be preserved, so force calling the loadCreateDataSource() instead of loadEditDataSource()
						selectContext.setCreate(true);
					}
				}
				fieldTypeRT.processLoadDataSource(selectContext, dropDownContainer);
			}
		}
		return dropDownContainer;
	}

	/**
	 * Is new but not a copy
	 * @param workItemBean
	 * @param isCopy
	 * @return
	 */
	private static boolean isCreate(TWorkItemBean workItemBean, boolean isCopy) {
		return (workItemBean.getObjectID()==null) && !isCopy;
	}

	/**
	 * Saves a modified or a new (copied or newly created) item
	 * @param workItemContext
	 * @param errorsList
	 * @param isCopy is it copy of the existing item
	 * @param withNotify to send notification messages to the listener
	 * @return
	 */
	public static boolean performSave(WorkItemContext workItemContext,
			List<ErrorData> errorsList, boolean isCopy, boolean withNotify/*, boolean cascadeChanges*/) {
		Set<Integer> presentFields = workItemContext.getPresentFieldIDs();
		TWorkItemBean workItemBean = workItemContext.getWorkItemBean();
		//force comment field if this is present in item bean
		if(workItemBean.getComment()!=null && workItemBean.getComment().length()>0){
			presentFields.add(SystemFields.INTEGER_COMMENT);
		}
		TWorkItemBean workItemBeanOriginal = workItemContext.getWorkItemBeanOriginal();
		Integer personID = workItemContext.getPerson();
		Locale locale = workItemContext.getLocale();
		//fieldChangeID not null means editing/deleting only a history entry, typically comment
		Integer fieldChangeID = workItemContext.getFieldChangeID();
		boolean updateLastEdit=workItemContext.isUpdateLastEdit();
		boolean exchangeImport = workItemContext.isExchangeImport();
		//the list of events to be triggered during the save
		List<Integer> events = new LinkedList<Integer>();
		//The item has been changed (except state- and date-change: they are dealt with explicitely)
		boolean isChanged = false;
		//Whether it is a new item
		boolean isCreate = isCreate(workItemBean, isCopy);
		boolean saveNeeded = false;
		Integer workItemID = workItemBean.getObjectID();
		Integer parentID = workItemBean.getSuperiorworkitem();
		boolean archivedOrDeleted = workItemBean.isArchivedOrDeleted();
		Integer originalParentID = null;
		boolean archivedOrDeletedOriginal = false;
		if (workItemBeanOriginal!=null) {
			originalParentID = workItemBeanOriginal.getSuperiorworkitem();
			archivedOrDeletedOriginal = workItemBeanOriginal.isArchivedOrDeleted();
		}
		//errors found
		if (!errorsList.isEmpty()) {
			return saveNeeded;
		}
		// Everything seems o.k. to update the workItem and associated tables
		Date now = new Date();
		workItemBean.setChangedByID(personID);
		ErrorData errorData;
		/*---------------------------------------------------------
		 * create or copy: check permission for create.
		 *---------------------------------------------------------*/
		//set the created timestamp only if not yet preset (for example by excel import it could be set already)
		if (isCopy || (isCreate && workItemBean.getCreated()==null)) {
			workItemBean.setCreated(now);
		}
		/**
		 * Set some field values before saving the item: typical case for extensible select
		 * It should be called before hasChanged because it might change the workItemBean
		 *
		 */
		processBeforeSave(workItemBean, workItemContext);
		/*-----------------------------------------------------------
		 * Save the item
		 *-----------------------------------------------------------*/
		isChanged = hasChanged(workItemBean, workItemBeanOriginal, presentFields, null, isCopy);
		if (isCreate || isCopy || isChanged) {
			saveNeeded = true;
			if (updateLastEdit) {
				//editing only a history entry (comment,attachment)
				//should not actualize the workItem's lastEdit
				workItemBean.setLastEdit(now);
			}
			//set the originator only if not yet preset (for example by excel import it could be set already)
			if (isCopy || (isCreate && workItemBean.getOriginatorID()==null)) {
				workItemBean.setOriginatorID(personID);
			}
			//try to save the workItem to the database
			Integer workItemKey = null;
			try {
				workItemKey = workItemDAO.save(workItemBean);
			} catch (ItemPersisterException e) {
				LOGGER.error("Saving of the workItem failed with " + e.getMessage(), e);
			}
			if (workItemKey==null) {
				//the save failed for some reason (see log files)
					errorData = new ErrorData("item.err.saveFailed", workItemBean.getSynopsis());
					errorsList.add(errorData);
					return saveNeeded;
			}
			workItemBean.setObjectID(workItemKey);
			//if fieldChangeID is not null there is no reason for future
			//save processing because only a history entry changes
			//but the workItem itself was saved only for calling the
			//lucene indexer for the new comment
			if (fieldChangeID==null) {
				//save the custom attributes also
				if (isCopy) {
					//get the workItemBeanOriginal as null in order to save the custom attributes
					//(when the values are the same in the original and new (copied) workItem no database save happens)
					//and all custom fields should be saved not just those present in the current screen
					saveWorkItemCustomAttributesByCopy(workItemBean, null);
				} else {
					saveWorkItemCustomAttributes(workItemBean, workItemBeanOriginal, presentFields);
				}
				if (exchangeImport) {
					//save only the workItem itself but
					//the attachment and history data is saved in another place
					return saveNeeded;
				}
				boolean haveNewAttachments=false;
				if (isCreate) {
					//move attachments from sessionID temporary directory to issue directory
					List<TAttachmentBean> attachList=workItemContext.getAttachmentsList();
					String sessionID=workItemContext.getSessionID();
					if (sessionID!=null&&attachList!=null && !attachList.isEmpty()) {
						//save form web interface (not from email submission)
						AttachBL.approve(attachList,sessionID,workItemBean.getObjectID());
						haveNewAttachments=true;
					}
				}
				// move the email attachments from temporary email directory to issue directory
				List<EmailAttachment> emailAttachmentList = workItemContext.getEmailAttachmentList();
				List<Integer> emailAttachmentIDList=null;
				if (emailAttachmentList!=null && !emailAttachmentList.isEmpty()) {
					emailAttachmentIDList=AttachBL.storeEmailAttachments(emailAttachmentList, workItemBean.getObjectID());
					if(isCreate){
						AttachBL.replaceInlineImagesDescription(workItemBean.getObjectID(), emailAttachmentList, emailAttachmentIDList);
					}else{
						workItemBean.setComment(AttachBL.replaceInlineImagesText(emailAttachmentList, emailAttachmentIDList, workItemID, workItemBean.getComment()));
					}
					haveNewAttachments=true;
				}
				if(haveNewAttachments){
					//add the attachments of the workItem to the attachments index
					//LuceneIndexer.addToAttachmentIndex(workItemBean);
					List<TAttachmentBean> attachments = AttachBL.getAttachments(workItemBean.getObjectID());
					if (attachments!=null && !attachments.isEmpty()) {
						for (TAttachmentBean attachmentBean : attachments) {
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug("Add attachment " + attachmentBean.getObjectID() + " to the new workItem " + workItemBean.getObjectID());
							}
							AttachmentIndexer.getInstance().addToIndex(attachmentBean, true);
							//possible lucene update in other cluster nodes
							ClusterMarkChangesBL.markDirtyAttachmentInCluster(attachmentBean.getObjectID(), CHANGE_TYPE.ADD_TO_INDEX);
						}
					}
				}
			}
		}

		/**
		 * Prepare the argument for saving  the item
		 */
		AfterItemSaveEventParam afterItemSaveEventParam = new AfterItemSaveEventParam();
		afterItemSaveEventParam.setWorkItemNew(workItemBean);
		afterItemSaveEventParam.setWorkItemOld(workItemBeanOriginal);
		afterItemSaveEventParam.setFieldConfigs(FieldRuntimeBL.getFieldConfigsMap(workItemBean.getProjectID(), workItemBean.getListTypeID(), locale));
		afterItemSaveEventParam.setLocale(locale);
		//don't forget to set the interesting fields (setInterestingFields()) in the context of
		//the actual operation before calling the getLocalizedFieldChanges()
		Set<Integer> interestingFieldsForHistory = new HashSet<Integer>();
		List<Integer> longFields = getLongFields(presentFields);
		SortedMap<Integer, FieldChange> fieldsChangesMap;
		boolean systemDateChanged = false;
		boolean startDateChanged = false;
		boolean requestedStartDateChanged = false;
		boolean endDateChanged = false;
		boolean requestedEndDateChanged = false;
		boolean parentChanged = false;
		if (isCreate || isCopy) {
			//send create event
			if (isCreate) {
				events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_CREATE));
			}
			//send copy event
			if (isCopy) {
				events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_COPY));
			}
			//for the history text for the new/copied items we are interested only in Comment for history
			//(the rare case when the Comment field is present on the create screen)
			interestingFieldsForHistory = new HashSet<Integer>();
			if (isCreate) {
				//create
				interestingFieldsForHistory.add(SystemFields.INTEGER_COMMENT);
			} else {
				//copy: we need mainly the state change for history
				interestingFieldsForHistory = copyPresentFieldsForHistory(presentFields);
				Set<Integer> excludeFields = getExcludeFieldsForHistory();
				interestingFieldsForHistory.removeAll(excludeFields);
			}
			afterItemSaveEventParam.setInterestingFields(interestingFieldsForHistory);
			fieldsChangesMap = HistorySaverBL.getLocalizedFieldChanges(afterItemSaveEventParam, locale, true);
		} else {
			//for the history text for existing items we are interested in the change of any field present
			//in the screen except some special system fields
			interestingFieldsForHistory = copyPresentFieldsForHistory(presentFields);
			Set<Integer> excludeFields = getExcludeFieldsForHistory();
			interestingFieldsForHistory.removeAll(excludeFields);
			afterItemSaveEventParam.setInterestingFields(interestingFieldsForHistory);
			fieldsChangesMap = HistorySaverBL.getLocalizedFieldChanges(afterItemSaveEventParam, locale, true);
			/*--------------------------------------------------------------------------------------------------------------
			 * send field specific events: state change, move, date change, assignResponsible, assignManager, addComment, "general" update
			 *--------------------------------------------------------------------------------------------------------------*/
			// status change: send events for either "close" or "reopen" or "general status change"
			if (workItemBeanOriginal != null && workItemBean.getStateID()!=null && workItemBeanOriginal.getStateID()!=null &&
					workItemBean.getStateID().intValue()!= workItemBeanOriginal.getStateID().intValue()) {
				TStateBean stateBeanNew = LookupContainer.getStatusBean(workItemBean.getStateID());
				TStateBean stateBeanOld = LookupContainer.getStatusBean(workItemBeanOriginal.getStateID());
				Integer newStatusFlag = stateBeanNew.getStateflag();
				Integer oldStatusFlag = stateBeanOld.getStateflag();
				if (newStatusFlag.intValue()==1 && oldStatusFlag.intValue()!=1) {
					//send close event
					events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_CLOSE));
				} else {
					//send reopen event
					if (newStatusFlag.intValue()!=1 && oldStatusFlag.intValue()==1) {
						events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_REOPEN));
					}
				}
				//just "ordinary" state change event
				events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_CHANGESTATUS));
				excludeFields.add(SystemFields.INTEGER_STATE);
			}
			//move event
			FieldChange projectChange = fieldsChangesMap.get(SystemFields.INTEGER_PROJECT);
			FieldChange issueTypeChange = fieldsChangesMap.get(SystemFields.INTEGER_ISSUETYPE);
			if (projectChange!=null && projectChange.isChanged() ||
				issueTypeChange!=null && issueTypeChange.isChanged()) {
				events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_MOVE));
				excludeFields.add(SystemFields.INTEGER_PROJECT);
				excludeFields.add(SystemFields.INTEGER_ISSUETYPE);
			}
			//date change event
			FieldChange startDateChange = fieldsChangesMap.get(SystemFields.INTEGER_STARTDATE);
			FieldChange endDateChange = fieldsChangesMap.get(SystemFields.INTEGER_ENDDATE);
			FieldChange requestedStartDateChange = fieldsChangesMap.get(SystemFields.INTEGER_TOP_DOWN_START_DATE);
			FieldChange requestedEndDateChange =  fieldsChangesMap.get(SystemFields.INTEGER_TOP_DOWN_END_DATE);
			startDateChanged = startDateChange!=null && startDateChange.isChanged();
			endDateChanged = endDateChange!=null && endDateChange.isChanged();
			requestedStartDateChanged = requestedStartDateChange!=null && requestedStartDateChange.isChanged();
			requestedEndDateChanged = requestedEndDateChange!=null && requestedEndDateChange.isChanged();
			if (startDateChanged || endDateChanged || requestedStartDateChanged || requestedEndDateChanged) {
				systemDateChanged = true;
				events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_CHANGEDATE));
				excludeFields.add(SystemFields.INTEGER_STARTDATE);
				excludeFields.add(SystemFields.INTEGER_ENDDATE);
				excludeFields.add(SystemFields.INTEGER_TOP_DOWN_START_DATE);
				excludeFields.add(SystemFields.INTEGER_TOP_DOWN_END_DATE);
			}
			//parent change
			FieldChange parentChange = fieldsChangesMap.get(SystemFields.INTEGER_SUPERIORWORKITEM);
			parentChanged = parentChange!=null && parentChange.isChanged();
			//responsible change event
			FieldChange responsibleChange = fieldsChangesMap.get(SystemFields.INTEGER_RESPONSIBLE);
			if (responsibleChange!=null && responsibleChange.isChanged()) {
				events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_ASSIGNRESPONSIBLE));
				excludeFields.add(SystemFields.INTEGER_RESPONSIBLE);
			}
			//manager change event
			FieldChange managerChange = fieldsChangesMap.get(SystemFields.INTEGER_MANAGER);
			if (managerChange!=null && managerChange.isChanged()) {
				events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_ASSIGNMANAGER));
				excludeFields.add(SystemFields.INTEGER_MANAGER);
			}
			//comment event
			FieldChange commentAddedChange = fieldsChangesMap.get(SystemFields.INTEGER_COMMENT);
			if (commentAddedChange!=null) {
				if (fieldChangeID==null) {
					events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_ADDCOMMENT));
				} else {
					String newComment = commentAddedChange.getNewShowValue();
					if (newComment!=null && !"".equals(newComment)) {
						events.add(new Integer(IEventSubscriber.EVENT_POST_ISSUE_EDITCOMMENT));
					} else {
						events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_DELETECOMMENT));
					}
				}
				excludeFields.add(SystemFields.INTEGER_COMMENT);
			}
			//attachment add event
			FieldChange attachmentAdd = fieldsChangesMap.get(SystemFields.INTEGER_ATTACHMENT_ADD_HISTORY_FIELD);
			if (attachmentAdd!=null && attachmentAdd.isChanged()) {
				events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_ADDATTACHMENT));
				excludeFields.add(SystemFields.INTEGER_ATTACHMENT_ADD_HISTORY_FIELD);
			}
			//attachment modify event
			FieldChange attachmentModify = fieldsChangesMap.get(SystemFields.INTEGER_ATTACHMENT_MODIFY_HISTORY_FIELD);
			if (attachmentModify!=null && attachmentModify.isChanged()) {
				events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_MODIFYATTACHMENT));
				excludeFields.add(SystemFields.INTEGER_ATTACHMENT_MODIFY_HISTORY_FIELD);
			}
			//attachment remove event
			FieldChange attachmentDelete = fieldsChangesMap.get(SystemFields.INTEGER_ATTACHMENT_DELETE_HISTORY_FIELD);
			if (attachmentDelete!=null && attachmentDelete.isChanged()) {
				events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_REMOVEATTACHMENT));
				excludeFields.add(SystemFields.INTEGER_ATTACHMENT_DELETE_HISTORY_FIELD);
			}
			//any other field change which was not excluded previously
			if (hasChanged(workItemBean, workItemBeanOriginal, presentFields, excludeFields, isCopy)) {
				events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_UPDATE));
			}
		}
		boolean mightTriggerEmail = HistorySaverBL.saveHistory(afterItemSaveEventParam, locale, personID, longFields, isCreate, isCopy, fieldChangeID);
		//save consultants/informants and budgets/efforts/costs for create mode
		//(in edit mode they are saved directly in the database,
		//but in create mode we have a workItemKey first now)
		if (isCreate && !isCopy) {
			//save consultants/informants from session to db
			ConsInfShow consInfShow = workItemContext.getConsInfShow();
			if (consInfShow!=null) {
				//the cons/inf tab was at least once selected (the consInfShow was initialized)
				RaciRoleBL.saveFromSessionToDb(workItemBean.getObjectID(), consInfShow);
			}
			//save budgets, costs/efforts from session to db
			AccountingForm accountingForm = workItemContext.getAccountingForm();
			if (accountingForm!=null) {
				//the accounting tab was at least once selected (the accountingForm was initialized)
				AccountingBL.saveAllFromSessionToDb(accountingForm, workItemContext.getWorkItemBean(),
						LookupContainer.getPersonBean(personID));
			}
			SortedMap<Integer, TWorkItemLinkBean> workItemsLinksMap = workItemContext.getWorkItemsLinksMap();
			if (workItemsLinksMap!=null) {
				ItemLinkBL.saveAllFromSessionToDb(workItemBean.getObjectID(), workItemsLinksMap);
			}
		}
		//adjust the ancestor item's bottom up dates to the dates of this child if it is the earliest/latest
		if (parentChanged ||
				(parentID!=null && (systemDateChanged || isCreate || isCopy || archivedOrDeletedOriginal!=archivedOrDeleted))) {
			/**
			 * Possible ancestor bottom up start or end date changes:
			 * 1. parent change
			 * 2. start or end date changed in leaf issue or create/copy of a new issue
			 * 3. archived/deleted flag changed
			 */
			Set<Integer> bottomUpFields = null;
			Map<Integer, Object> newValuesMap = new HashMap<Integer, Object>();
			Map<Integer, Object> oldValuesMap = new HashMap<Integer, Object>();
			if (parentChanged) {
				bottomUpFields = FieldRuntimeBL.getPossibleBottomUpFields();
				if (originalParentID!=null) {
					//similar as the child date would be set to null for the old parent: recalculate of bottom up dates for the original parent
					//set only the oldValuesMap
					if (workItemBeanOriginal!=null) {
						for (Integer fieldID : bottomUpFields) {
							oldValuesMap.put(fieldID, workItemBeanOriginal.getAttribute(fieldID));
						}
					}
					actualizeAncestorBottomUpDate(originalParentID, bottomUpFields, newValuesMap, oldValuesMap, LookupContainer.getPersonBean(personID), locale);
				}
				if (parentID!=null) {
					//similar as a new child workItem would be created for parentID
					//set only the newValuesMap
					for (Integer fieldID : bottomUpFields) {
						newValuesMap.put(fieldID, workItemBean.getAttribute(fieldID));
					}
					actualizeAncestorBottomUpDate(parentID, bottomUpFields, newValuesMap, oldValuesMap, LookupContainer.getPersonBean(personID), locale);
				}
			} else {
				if (isCreate || isCopy || archivedOrDeletedOriginal!=archivedOrDeleted) {
					bottomUpFields = FieldRuntimeBL.getPossibleBottomUpFields();
				} else {
					bottomUpFields = new HashSet<Integer>();
					if (startDateChanged) {
						bottomUpFields.add(SystemFields.INTEGER_STARTDATE);
					}
					if (endDateChanged) {
						bottomUpFields.add(SystemFields.INTEGER_ENDDATE);
					}
					if (requestedStartDateChanged) {
						bottomUpFields.add(SystemFields.INTEGER_TOP_DOWN_START_DATE);
					}
					if (requestedEndDateChanged) {
						bottomUpFields.add(SystemFields.INTEGER_TOP_DOWN_END_DATE);
					}
				}
				for (Integer fieldID : bottomUpFields) {
					newValuesMap.put(fieldID, workItemBean.getAttribute(fieldID));
					if (workItemBeanOriginal!=null) {
						oldValuesMap.put(fieldID, workItemBeanOriginal.getAttribute(fieldID));
					}
				}
				actualizeAncestorBottomUpDate(parentID, bottomUpFields, newValuesMap, oldValuesMap, LookupContainer.getPersonBean(personID), locale);
			}
			/*if (parentChanged) {
				if (parentOriginal!=null) {
					//similar as the startDate and endDate would be set to null in the "old location", triggering the recalculation of bottom up dates
					FieldsManagerRT.actualizeAncestorBottomUpDates(parentOriginal, null, null, startDateOriginal, endDateOriginal);
				}
				if (parentID!=null) {
					//as a new child workItem would be created for parentID
					FieldsManagerRT.actualizeAncestorBottomUpDates(parentID, startDate, endDate, null, null);
				}
			} else {
				//start or end date change
				actualizeAncestorBottomUpDates(parentID, startDate, endDate, startDateOriginal, endDateOriginal);
			}*/
		}
		/*if (systemDateChanged && cascadeChanges) {
			Map<String, List<TWorkItemLinkBean>> successorWorkItemLinksByLinkTypeMap = workItemContext.getSuccessorWorkItemLinksByLinkTypeMap();
			Map<String, List<TWorkItemLinkBean>> predecessorWorkItemLinksByLinkTypeMap = workItemContext.getPredecessorWorkItemLinksByLinkTypeMap();
			Set<String> workItemsLinksOfTypesFound = new HashSet<String>();
			if (successorWorkItemLinksByLinkTypeMap!=null) {
				workItemsLinksOfTypesFound.addAll(successorWorkItemLinksByLinkTypeMap.keySet());
			}
			if (predecessorWorkItemLinksByLinkTypeMap!=null) {
				workItemsLinksOfTypesFound.addAll(predecessorWorkItemLinksByLinkTypeMap.keySet());
			}
			for (String linkTypePlugin : workItemsLinksOfTypesFound) {
				ILinkType linkType = (ILinkType)PluginManager.getInstance().getPluginClass(PluginManager.LINKTYPE_ELEMENT, linkTypePlugin);
				linkType.afterIssueSave(workItemBean, workItemBeanOriginal, personID, successorWorkItemLinksByLinkTypeMap.get(linkTypePlugin), predecessorWorkItemLinksByLinkTypeMap.get(linkTypePlugin), locale);
			}
		}*/
		if (ApplicationBean.getInstance().getSiteBean().getSummaryItemsBehavior()) {
			if (parentChanged || archivedOrDeletedOriginal!=archivedOrDeleted) {
				/**
				 * Possible ancestor planned value changes:
				 * 1. parent change
				 * 2. archived/deleted flag changed
				 */
				AccountingBL.actualizeAncestorValues(workItemBean, parentID, originalParentID, personID);
			}
		}

		//add inline items link
		List<Integer> inlineItems=workItemContext.getInlineItems();
		//Saving inlineItems
		if (inlineItems!=null && !inlineItems.isEmpty()) {
			InlineItemLinkBL.saveInlineItemLinks(inlineItems, workItemContext.getWorkItemBean().getObjectID(), workItemContext.getRootItemID());
		}

		//set the mails only after the consultants/informants are also set because they should also receive the email
		EventPublisher eventPublisher = EventPublisher.getInstance();
		if (eventPublisher!=null && withNotify && mightTriggerEmail) {
			afterItemSaveEventParam.setInterestingFields(getInterestingFieldsForMail(fieldsChangesMap, workItemBean.getObjectID()));
			eventPublisher.notify(events, afterItemSaveEventParam);
		}
		return saveNeeded;
	}

	/**
	 * Once a date was changed on a leaf workItem or a new leaf workItem
	 * was created or copied or the parent was changed the ancestor dates should be actualized
	 * @param parentID
	 * @param bottomUpFieldIDs
	 * @param newValuesMap
	 * @param oldValuesMap
	 * @param locale
	 */
	private static synchronized void actualizeAncestorBottomUpDate(Integer parentID, Set<Integer> bottomUpFieldIDs,
			Map<Integer, Object> newValuesMap, Map<Integer, Object> oldValuesMap, TPersonBean person, Locale locale) {
		if (bottomUpFieldIDs!=null && !bottomUpFieldIDs.isEmpty()) {
			Set<Integer> visistedAscendentsSet = new HashSet<Integer>();
			while (parentID!=null) {
				TWorkItemBean parentWorkItem = null;
				try {
					parentWorkItem = ItemBL.loadWorkItem(parentID);
				} catch (ItemLoaderException e) {
				}
				if (parentWorkItem==null) {
					LOGGER.debug("Item with id " + parentID + " not found");
					return;
				}
				Map<Integer, TFieldConfigBean> fieldConfigs = FieldRuntimeBL.getLocalizedFieldConfigs(bottomUpFieldIDs,
						parentWorkItem.getProjectID(), parentWorkItem.getListTypeID(), locale);
				//get the field settings
				Map<String, Object> fieldSettings = FieldRuntimeBL.getFieldSettings(fieldConfigs);
				boolean parentValueChanged = false;
				Date originalStartDate = ItemMoveBL.getStartDate(parentWorkItem);
				Date originalEndDate =  ItemMoveBL.getEndDate(parentWorkItem);
				for (Integer fieldID : bottomUpFieldIDs) {
					TFieldConfigBean fieldConfigBean = fieldConfigs.get(fieldID);
					Object fieldSettingsObject = fieldSettings.get(MergeUtil.mergeKey(fieldID, null));
					IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
					if (fieldTypeRT.setBottomUpParentValue(parentWorkItem, fieldID, newValuesMap.get(fieldID), oldValuesMap.get(fieldID), fieldConfigBean, fieldSettingsObject)) {
						LOGGER.debug("Date " + fieldID + " for item " + parentWorkItem.getSynopsis() + "(" + parentID + ") set to " + parentWorkItem.getAttribute(fieldID));
						parentValueChanged = true;
					}
				}
				parentID = null;
				if (parentValueChanged) {
					try {
						/*Date newStartDate = ItemMoveBL.getStartDate(parentWorkItem);
						Date newEndDate =  ItemMoveBL.getEndDate(parentWorkItem);
						if(originalStartDate != null && originalEndDate != null
								&& newStartDate != null && newEndDate != null) {
							TWorkItemBean workItemBeanOriginal = parentWorkItem.copyShallow();
							ItemMoveBL.setStartDate(workItemBeanOriginal, originalStartDate);
							ItemMoveBL.setEndDate(workItemBeanOriginal, originalEndDate);
							System.out.println("**** Call for parent: " + parentWorkItem.getObjectID());
							ItemMoveBL.moveItem(workItemBeanOriginal, newStartDate, newEndDate, true, true, null, person, locale);
						}*/
						//WorkItemContext workItemContext = createImportContext(parentWorkItem, bottomUpFieldIDs, person, locale, fieldConfigs, fieldSettings);
						workItemDAO.saveSimple(parentWorkItem);
						//go up the ascendent hierarchy only if further ancestor exists and date change was needed
						parentID = parentWorkItem.getSuperiorworkitem();
						if (parentID!=null) {
							if (!visistedAscendentsSet.contains(parentID)) {
								visistedAscendentsSet.add(parentID);
							} else {
								//circular reference
								LOGGER.warn("Parent " + parentID + " has circular reference to the current item");
								parentID = null;
							}
						}
					} catch (ItemPersisterException e) {
					}
				}
			}
		}
	}

	/**
	 * Get the interesting fields for history: clone the present fields
	 * @param presentFields
	 * @return
	 */
	private static Set<Integer> copyPresentFieldsForHistory(Set<Integer> presentFields) {
		Set<Integer> interestingFieldsForHistory = new HashSet<Integer>();
		if (presentFields!=null && !presentFields.isEmpty()) {
			//make a local copy of the present fields
			for (Integer fieldID : presentFields) {
				interestingFieldsForHistory.add(fieldID);
			}
		}
		return interestingFieldsForHistory;
	}

	private static Set<Integer> getExcludeFieldsForHistory() {
		Set<Integer> excludeFields = new HashSet<Integer>();
		excludeFields.add(SystemFields.INTEGER_CREATEDATE);
		excludeFields.add(SystemFields.INTEGER_LASTMODIFIEDDATE);
		excludeFields.add(SystemFields.INTEGER_CHANGEDBY);
		excludeFields.add(SystemFields.INTEGER_ISSUENO);
		excludeFields.add(SystemFields.INTEGER_PROJECT_SPECIFIC_ISSUENO);
		excludeFields.add(SystemFields.INTEGER_WBS);
		return excludeFields;
	}

	/**
	 * Get the long field from the present fields because they should be rendered in a different way
	 * @param presentFieldIDs
	 * @return
	 */
	public static List<Integer> getLongFields(Set<Integer> presentFieldIDs) {
		List<Integer> longFields = new LinkedList<Integer>();
		if (presentFieldIDs!=null) {
			for (Integer fieldID : presentFieldIDs) {
				if (fieldID.intValue()>0) {
					IFieldTypeRT fieldTypeRT  = FieldTypeManager.getFieldTypeRT(fieldID);
					if (fieldTypeRT!=null) {
						if (fieldTypeRT.isLong()) {
							longFields.add(fieldID);
						}
					}
				} else {
					switch (fieldID.intValue()) {
					case SystemFields.ATTACHMENT_ADD_HISTORY_FIELD:
					case SystemFields.ATTACHMENT_MODIFY_HISTORY_FIELD:
					case SystemFields.ATTACHMENT_DELETE_HISTORY_FIELD:
					case SystemFields.ADD_INFORMED:
					case SystemFields.DELETE_INFORMED:
					case SystemFields.ADD_CONSULTED:
					case SystemFields.DELETE_CONSULTED:
						longFields.add(fieldID);
					}
				}
			}
		}
		return longFields;
	}

	/**
	 * Get the interesting fields for notification mail: most of the system fields and the specified custom fields
	 * @param workItemID
	 * @return
	 */
	private static Set<Integer> getInterestingFieldsForMail(
			SortedMap<Integer, FieldChange> fieldsChangesMap, Integer workItemID) {
		Set<Integer> interestingFields = new HashSet<Integer>();
		//add the not deprecated system fields
		List<TFieldBean> allSystemFields = FieldBL.loadSystem();
		for (TFieldBean fieldBean : allSystemFields) {
			Integer fieldID = fieldBean.getObjectID();
			if (!fieldBean.isDeprecatedString()) {
				interestingFields.add(fieldID);
			}
		}
		//custom fields which are specified and not deprecated for workItem
		List<TFieldBean> specifiedCustomFieldBeans = FieldBL.loadSpecifiedCustomFields(workItemID);
		if (specifiedCustomFieldBeans!=null) {
			for (TFieldBean fieldBean : specifiedCustomFieldBeans) {
				Integer fieldID = fieldBean.getObjectID();
				if (!fieldBean.isDeprecatedString()) {
					interestingFields.add(fieldID);
				}
			}
		}
		//custom fields which were changed by this edit (possibly also deprecated values from "old" screens)
		for (FieldChange fieldChange : fieldsChangesMap.values()) {
			Integer fieldID = fieldChange.getFieldID();
			if (fieldChange.isChanged()) {
				interestingFields.add(fieldID);
			}
		}
		//remove some system fields
		interestingFields.remove(SystemFields.INTEGER_LASTMODIFIEDDATE);
		interestingFields.remove(SystemFields.INTEGER_CHANGEDBY);
		interestingFields.remove(SystemFields.INTEGER_ISSUENO);
		interestingFields.remove(SystemFields.INTEGER_ACCESSLEVEL);
		interestingFields.remove(SystemFields.INTEGER_ARCHIVELEVEL);
		interestingFields.remove(SystemFields.INTEGER_TASK_IS_MILESTONE);
		interestingFields.remove(SystemFields.INTEGER_WBS);
		interestingFields.remove(SystemFields.INTEGER_PROJECT_SPECIFIC_ISSUENO);
		return interestingFields;
	}

	/**
	 * Validate the presence of some required system fields independently whether they are present on the screen or not!
	 * (may be the screen designer forgot some required system fields to put on the screen)
	 * If these fields are present on the screen they are hardcoded as required in the TFieldConfig so
	 * the required validator should guarantee the presence of them even without calling this method.
	 * @param workItemBean
	 * @param presentFields when presents on the screen they are set to required anyway,
	 * 		so avoid adding the same error message two times
	 * @return
	 */
	private static List<ErrorData> requiredValidate(TWorkItemBean workItemBean, Map<Integer, TFieldConfigBean> fieldConfigs, Set<Integer> presentFields) {
		List<ErrorData> errorsList = new LinkedList<ErrorData>();
		Set<Integer> requiredSystemFields = getRequiredSystemFieldsList();
		for (Integer fieldID : requiredSystemFields) {
			Object attribute = workItemBean.getAttribute(fieldID, null);
			if ((attribute==null || "".equals(attribute)) && !(presentFields.contains(fieldID))){
				TFieldConfigBean fieldConfigBean = null;
				if (fieldConfigs!=null) {
					fieldConfigBean = fieldConfigs.get(fieldID);;
				}
				if (fieldConfigBean==null) {
					LOGGER.warn("Field config not found for required field " + fieldID);
				} else {
					ErrorData errorData = new ErrorData(fieldID,"common.err.required", fieldConfigBean.getLabel());
					errorsList.add(errorData);
				}
			}
		}
		return errorsList;
	}

	public static Set<Integer> getRequiredSystemFieldsList() {
		Set<Integer> requiredSystemFieldIDs = new TreeSet<Integer>();
		requiredSystemFieldIDs.add(SystemFields.INTEGER_MANAGER);
		requiredSystemFieldIDs.add(SystemFields.INTEGER_RESPONSIBLE);
		requiredSystemFieldIDs.add(SystemFields.INTEGER_ISSUETYPE);
		requiredSystemFieldIDs.add(SystemFields.INTEGER_STATE);
		requiredSystemFieldIDs.add(SystemFields.INTEGER_PRIORITY);
		requiredSystemFieldIDs.add(SystemFields.INTEGER_SYNOPSIS);
		return requiredSystemFieldIDs;
	}

	public static Set<Integer> getNotRequiredProjectDefaultSystemFieldsList() {
		Set<Integer> requiredSystemFieldIDs = new TreeSet<Integer>();
		requiredSystemFieldIDs.add(SystemFields.INTEGER_RELEASENOTICED);
		requiredSystemFieldIDs.add(SystemFields.INTEGER_RELEASESCHEDULED);
		return requiredSystemFieldIDs;
	}

	/**
	 * Hardcoded business logic validations which are not covered by the other validators
	 * @param workItemBean
	 * @return
	 */
	private static List<ErrorData> businessLogicValidate(TWorkItemBean workItemBean, TWorkItemBean workItemBeanOriginal, Integer personID, Locale locale) {
		List<ErrorData> errorsList = new LinkedList<ErrorData>();
		Date startDate = workItemBean.getStartDate();
		Date endDate = workItemBean.getEndDate();
		Date targetStartDate = workItemBean.getTopDownStartDate();
		Date targetEndDate = workItemBean.getTopDownEndDate();
		if (startDate!=null && endDate!=null &&
				startDate.after(endDate) && !CalendarUtil.sameDay(startDate, endDate)) {
			Set<Integer> presentFields = new HashSet<Integer>();
			presentFields.add(SystemFields.INTEGER_STARTDATE);
			presentFields.add(SystemFields.INTEGER_ENDDATE);
			Map<Integer, TFieldConfigBean> fieldConfigsMap = FieldRuntimeBL.getLocalizedFieldConfigs(presentFields, workItemBean.getProjectID(), workItemBean.getListTypeID(), locale);
			ErrorData errorData = new ErrorData(SystemFields.INTEGER_ENDDATE,"item.err.invalidEndDate");
			List<ErrorParameter> errorParameters = new LinkedList<ErrorParameter>();
			ErrorParameter errorParameterStartDate = new ErrorParameter(DateTimeUtils.getInstance().formatGUIDate(startDate, locale));
			ErrorParameter errorParameterEndDate = new ErrorParameter(DateTimeUtils.getInstance().formatGUIDate(endDate, locale));
			errorParameters.add(errorParameterEndDate);
			errorParameters.add(errorParameterStartDate);
			errorParameters.add(new ErrorParameter(fieldConfigsMap.get(SystemFields.INTEGER_ENDDATE).getLabel()));
			errorParameters.add(new ErrorParameter(fieldConfigsMap.get(SystemFields.INTEGER_STARTDATE).getLabel()));
			errorData.setResourceParameters(errorParameters);
			errorsList.add(errorData);
		}
		if (targetStartDate!=null && targetEndDate!=null &&
				targetStartDate.after(targetEndDate) && !CalendarUtil.sameDay(targetStartDate, targetEndDate)) {
			Set<Integer> presentFields = new HashSet<Integer>();
			presentFields.add(SystemFields.INTEGER_TOP_DOWN_START_DATE);
			presentFields.add(SystemFields.INTEGER_TOP_DOWN_END_DATE);
			Map<Integer, TFieldConfigBean> fieldConfigsMap = FieldRuntimeBL.getLocalizedFieldConfigs(presentFields, workItemBean.getProjectID(), workItemBean.getListTypeID(), locale);
			ErrorData errorData = new ErrorData(SystemFields.INTEGER_TOP_DOWN_END_DATE, "item.err.invalidEndDate");
			List<ErrorParameter> errorParameters = new LinkedList<ErrorParameter>();
			ErrorParameter errorParameterTragetStartDate = new ErrorParameter(DateTimeUtils.getInstance().formatGUIDate(targetStartDate, locale));
			ErrorParameter errorParameterTargetEndDate = new ErrorParameter(DateTimeUtils.getInstance().formatGUIDate(targetEndDate, locale));
			errorParameters.add(errorParameterTargetEndDate);
			errorParameters.add(errorParameterTragetStartDate);
			errorParameters.add(new ErrorParameter(fieldConfigsMap.get(SystemFields.INTEGER_TOP_DOWN_END_DATE).getLabel()));
			errorParameters.add(new ErrorParameter(fieldConfigsMap.get(SystemFields.INTEGER_TOP_DOWN_START_DATE).getLabel()));
			errorData.setResourceParameters(errorParameters);
			errorsList.add(errorData);
		}

		if (workItemBean.isMilestone()) {
			if (endDate!=null && EqualUtils.notEqualDateNeglectTime(endDate, startDate)) {
				ErrorData errorData = new ErrorData(SystemFields.INTEGER_ENDDATE,"item.err.notNullEndDate");
				errorsList.add(errorData);
			}
			if (targetEndDate!=null && EqualUtils.notEqualDateNeglectTime(targetEndDate, targetStartDate)) {
				ErrorData errorData = new ErrorData(SystemFields.INTEGER_TOP_DOWN_END_DATE,"item.err.notNullEndDate");
				errorsList.add(errorData);
			}
		}

		Integer parentWorkItemKey = workItemBean.getSuperiorworkitem();
		if (parentWorkItemKey!=null) {
            try {
                workItemDAO.loadByPrimaryKey(parentWorkItemKey);
            } catch (ItemLoaderException e) {
                //silently change the parentID for the not any more existing parent to null
                LOGGER.warn("The parent workItem " + parentWorkItemKey + " for the workItem " + workItemBean.getObjectID() + " does not exist and it will be set to null");
                workItemBean.setSuperiorworkitem(null);
            }
			if (workItemBean.getObjectID()!=null && ItemBL.isAscendant(workItemBean.getObjectID(), parentWorkItemKey)) {
				ErrorData errorData = new ErrorData(SystemFields.INTEGER_SUPERIORWORKITEM,"item.err.parentIsDescendant");
				errorsList.add(errorData);
			}
		}
		if (workItemBeanOriginal!=null) {
			Integer originalProject = workItemBeanOriginal.getProjectID();
			Integer actualProject = workItemBean.getProjectID();
			if (EqualUtils.notEqual(originalProject, actualProject)) {
				//move to another project
				TProjectBean projectBean = LookupContainer.getProjectBean(actualProject);
				if (projectBean!=null && projectBean.isPrivate() &&
						EqualUtils.equal(projectBean.getDefaultManagerID(), personID)) {
					if (EqualUtils.notEqual(workItemBean.getOriginatorID(), personID)) {
						ErrorData errorData = new ErrorData(SystemFields.INTEGER_ORIGINATOR,"item.err.privateProject.otherOriginator");
						errorsList.add(errorData);
					}
					if (EqualUtils.notEqual(workItemBean.getOwnerID(), personID)) {
						ErrorData errorData = new ErrorData(SystemFields.INTEGER_MANAGER,"item.err.privateProject.otherManager");
						errorsList.add(errorData);
					}
					if (EqualUtils.notEqual(workItemBean.getResponsibleID(), personID)) {
						ErrorData errorData = new ErrorData(SystemFields.INTEGER_RESPONSIBLE,"item.err.privateProject.otherResponsible");
						errorsList.add(errorData);
					}
					int noOfWatchers = ConsInfBL.countDirectWatcherPersons(workItemBean.getObjectID());
					if (noOfWatchers>0) {
						ErrorData errorData = new ErrorData("item.err.privateProject.watcher");
						errorsList.add(errorData);
					}
				}
			}
		}
		return errorsList;
	}

	/**
	 * Check permission to close and verify open children.
	 * @param workItemBean
	 * @param workItemBeanOriginal
	 * @param person
	 * @return
	 */
	private static List<ErrorData> closePossibleValidate(TWorkItemBean workItemBean,
			TWorkItemBean workItemBeanOriginal, boolean confirm, List<Integer> selectedChildrenList, boolean parentIsSelected,  Integer person, Locale locale) {
		List<ErrorData> errorsList = new ArrayList<ErrorData>();
		ErrorData errorData;
		Integer workItemID = workItemBean.getObjectID();
		if (workItemBeanOriginal!=null && workItemBean.getStateID()!=null && workItemBeanOriginal.getStateID()!=null &&
				workItemBean.getStateID().intValue()!= workItemBeanOriginal.getStateID().intValue()) {
			TStateBean stateBeanNew = LookupContainer.getStatusBean(workItemBean.getStateID());
			TStateBean stateBeanOld = LookupContainer.getStatusBean(workItemBeanOriginal.getStateID());
			Integer newStatusFlag = stateBeanNew.getStateflag();
			Integer oldStatusFlag = stateBeanOld.getStateflag();
			if (newStatusFlag.intValue()==TStateBean.STATEFLAGS.CLOSED && oldStatusFlag.intValue()!=TStateBean.STATEFLAGS.CLOSED) {
				//test whether is it allowed to close an item: a private issue can be closed anyway
				if (!workItemBean.isAccessLevelFlag() && !AccessBeans.isAllowedToClose(workItemBean, person)) {
					errorData = new ErrorData("item.err.closeNotAllowed");
					errorsList.add(errorData);
				}
				//test whether is has open children
				if (workItemID!=null) {
					List<TWorkItemBean> openedChildren = workItemDAO.getOpenChildren(workItemID, selectedChildrenList);
					if (openedChildren!=null && !openedChildren.isEmpty()) {
						if (confirm) {
							//confirmed by user
							for (TWorkItemBean childItemBean : openedChildren) {
								//try to close each child item one by one
								//because of confirm if will recursively call the children's descendants
								FieldsManagerRT.saveOneField(person, childItemBean.getObjectID(), locale, confirm,
										SystemFields.INTEGER_STATE, workItemBean.getStateID(), true, null, errorsList);
							}
							openedChildren = workItemDAO.getOpenChildren(workItemID, selectedChildrenList);
							if (openedChildren!=null && !openedChildren.isEmpty()) {
								//for some reason (workflow/no right to close) at least one child can't be closed:
								//report this as "not closed children exist" error to force the user to close the child manually
								errorData = new ErrorData("item.err.closeOpenChildren");
								errorsList.add(errorData);
							}
						} else {
							//trigger confirmation message on the UI
							errorData = new ErrorData("item.conf.closeOpenChildren");
							errorData.setConfirm(true);
							//errorData.setConfirmationMessageKey("item.conf.closeOpenChildren");
							errorsList.add(errorData);
						}
					}
				}
			} else {
				if (newStatusFlag.intValue()!=TStateBean.STATEFLAGS.CLOSED && oldStatusFlag.intValue()==TStateBean.STATEFLAGS.CLOSED) {
					//reopen an issue
					if (workItemID!=null) {
						Integer parentID = workItemBean.getSuperiorworkitem();
						if (parentID!=null) {
							//if it has parent, test whether the parent is open
							if (parentIsSelected) {
								//from mass operation
								//parent is also selected for reopen
								return errorsList;
							}
							if (isParentClosed(parentID)) {
								if (confirm) {
									//confirmed by user
									//because of confirm if will recursively call the upper ascendants
									FieldsManagerRT.saveOneField(person, parentID, locale, confirm,
											SystemFields.INTEGER_STATE, workItemBean.getStateID(), true, null, errorsList);
									if (isParentClosed(parentID)) {
										errorData = new ErrorData("item.err.openClosedParent");
										errorsList.add(errorData);
									}
								} else {
									//trigger confirmation message on the UI
									errorData = new ErrorData("item.conf.openClosedParent");
									errorData.setConfirm(true);
									//errorData.setConfirmationMessageKey("item.conf.openClosedParent");
									errorsList.add(errorData);
								}
							}
						}
					}
				}
			}
		} else {
			if (workItemBeanOriginal==null) {
				Integer parentID = workItemBean.getSuperiorworkitem();
				if (parentID!=null && isParentClosed(parentID)) {
						errorData = new ErrorData("item.err.addToClosedParent");
						errorsList.add(errorData);
				}
			}
		}
		return errorsList;
	}

	private static boolean isParentClosed(Integer parentID) {
		if (parentID!=null) {
			TWorkItemBean parentWorkItemBean = null;
			try {
				parentWorkItemBean = workItemDAO.loadByPrimaryKey(parentID);
			} catch (ItemLoaderException e) {
				LOGGER.error("Loading the parent " + parentID + " failed with " + e.getMessage(), e);
			}
			if (parentWorkItemBean!=null) {
				TStateBean parentStateBean = LookupContainer.getStatusBean(parentWorkItemBean.getStateID());
				return parentStateBean.getStateflag().intValue()==TStateBean.STATEFLAGS.CLOSED;
			}
		}
		return false;
	}
	/**
	 * Execute the link type validation
	 * @param workItemBean
	 * @param workItemBeanOriginal
	 * @param confirm whether the user confirmed
	 * @param person
	 * @param locale
	 * @return
	 */
	private static List<ErrorData> linkTypeValidation(WorkItemContext workItemContext, TWorkItemBean workItemBean,
			TWorkItemBean workItemBeanOriginal, boolean confirm, Integer person, Locale locale) {
		List<ErrorData> errorsList = new ArrayList<ErrorData>();
		Integer workItemID = workItemBean.getObjectID();
		if (workItemID==null) {
			//new workItem
			return errorsList;
		}

		//the successors list (for bidirectional link types)
		List<TWorkItemLinkBean> successorList=workItemLinkDAO.loadByWorkItemPred(workItemID);
		//the predecessors list (for unidirectional inward link types)
		List<TWorkItemLinkBean> predecessorList=workItemLinkDAO.loadByWorkItemSucc(workItemID);
		if ((successorList==null || successorList.isEmpty()) &&
				(predecessorList==null || predecessorList.isEmpty())) {
			//no links to this workItem
			return errorsList;
		}
		//get all link types defined
		Map<Integer, TLinkTypeBean> linkTypeMap = GeneralUtils.createMapFromList(LinkTypeBL.loadAll());
		//group all linked workItems by linkType to pass list of workItemsLinks of the same type to the validateBeforeIssueSave()
		//because if validateBeforeIssueSave() contains database operations it should be made at once for all workItemsLinks of
		//the same type (if it would be made for each workItemsLink separately it could be an expensive operation)
		Map<String, List<TWorkItemLinkBean>> successorWorkItemLinksByLinkTypeMap = new HashMap<String, List<TWorkItemLinkBean>>();
		Map<String, List<TWorkItemLinkBean>> predecessorWorkItemLinksByLinkTypeMap = new HashMap<String, List<TWorkItemLinkBean>>();
		if (successorList!=null) {
			for (TWorkItemLinkBean workItemLinkBean : successorList) {
				TLinkTypeBean linkTypeBean = linkTypeMap.get(workItemLinkBean.getLinkType());
				if (linkTypeBean!=null) {
					String linkTypePlugin = linkTypeBean.getLinkTypePlugin();
					List<TWorkItemLinkBean> workItemLinkBeanList = successorWorkItemLinksByLinkTypeMap.get(linkTypePlugin);
					if (workItemLinkBeanList==null) {
						workItemLinkBeanList = new ArrayList<TWorkItemLinkBean>();
						successorWorkItemLinksByLinkTypeMap.put(linkTypePlugin, workItemLinkBeanList);
					}
					workItemLinkBeanList.add(workItemLinkBean);
				}
			}
			workItemContext.setSuccessorWorkItemLinksByLinkTypeMap(successorWorkItemLinksByLinkTypeMap);
		}
		if (predecessorList!=null) {
			for (TWorkItemLinkBean workItemLinkBean : predecessorList) {
				TLinkTypeBean linkTypeBean = linkTypeMap.get(workItemLinkBean.getLinkType());
				if (linkTypeBean!=null) {
					String linkTypePlugin = linkTypeBean.getLinkTypePlugin();
					List<TWorkItemLinkBean> workItemLinkBeanList = predecessorWorkItemLinksByLinkTypeMap.get(linkTypePlugin);
					if (workItemLinkBeanList==null) {
						workItemLinkBeanList = new ArrayList<TWorkItemLinkBean>();
						predecessorWorkItemLinksByLinkTypeMap.put(linkTypePlugin, workItemLinkBeanList);
					}
					workItemLinkBeanList.add(workItemLinkBean);
				}
			}
			workItemContext.setPredecessorWorkItemLinksByLinkTypeMap(predecessorWorkItemLinksByLinkTypeMap);
		}
		//get all possible linkTypes which have workItemLinks
		Set<String> workItemsLinksOfTypesFound = new HashSet<String>();
		workItemsLinksOfTypesFound.addAll(successorWorkItemLinksByLinkTypeMap.keySet());
		workItemsLinksOfTypesFound.addAll(predecessorWorkItemLinksByLinkTypeMap.keySet());
		for (String linkTypePlugin : workItemsLinksOfTypesFound) {
			ILinkType linkType = (ILinkType)PluginManager.getInstance().getPluginClass(PluginManager.LINKTYPE_ELEMENT, linkTypePlugin);
			List<ErrorData> errorData = linkType.validateBeforeIssueSave(workItemBean, workItemBeanOriginal, person,
					successorWorkItemLinksByLinkTypeMap.get(linkTypePlugin), predecessorWorkItemLinksByLinkTypeMap.get(linkTypePlugin), confirm, locale);
			if (errorData!=null) {
				errorsList.addAll(errorData);
			}
		}
		/*if(ItemMoveBL.hasWorkItemBeanStartAndEndDate(workItemBeanOriginal)) {
			Set<Integer> problems = ItemMoveBL.moveItem(workItemBeanOriginal, ItemMoveBL.getStartDate(workItemBean), ItemMoveBL.getEndDate(workItemBean), false, false, null, PersonBL.loadByPrimaryKey(person), locale);
			if (!problems.isEmpty()) {
				if (confirm) {
					LOGGER.debug("MS Proj. dep violation: confirmation received, delete not valid links");
					ItemMoveBL.removeViolatedParentDependencies(problems);
				} else {
					LOGGER.debug("MS Proj. dep violation: request confirmation");
					ErrorData errorData = new ErrorData("itemov.ganttView.dependency.violation");
					errorData.setConfirm(true);
					errorsList.add(errorData);
				}
			}
		}*/
		return errorsList;
	}

	private static boolean hasChanged(TWorkItemBean workItemBean, TWorkItemBean workItemBeanOriginal,
			Set<Integer> presentFields, Set<Integer> excludeFields, boolean copy) {
		if(workItemBeanOriginal==null || presentFields==null) {
			return false;
		}
		for (Integer fieldID : presentFields) {
			if (excludeFields==null || !excludeFields.contains(fieldID)) {
				IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
				if (fieldTypeRT==null) {
					//not a real field (but a pseudo field or cons/inf from the bulk operation)
					switch(fieldID.intValue()) {
					//attachment was added
					case SystemFields.ATTACHMENT_ADD_HISTORY_FIELD:
					//attachment was mofified
					case SystemFields.ATTACHMENT_MODIFY_HISTORY_FIELD:
						//attachment was deleted
					case SystemFields.ATTACHMENT_DELETE_HISTORY_FIELD:
						//we should return true to actualize the last edited date of the item
						return true;
					}
					continue;

				}
				Object newValue = workItemBean.getAttribute(fieldID);
				Object oldValue = workItemBeanOriginal.getAttribute(fieldID);
				if (fieldTypeRT.valueModified(newValue, oldValue)) {
					if (LOGGER.isDebugEnabled()) {
						Integer workItemID = null;
						if (copy && workItemBeanOriginal!=null) {
							workItemID = workItemBeanOriginal.getObjectID();
						} else {
							workItemID = workItemBean.getObjectID();
						}
						LOGGER.debug("Field " + fieldID + " for workitem " + workItemID +
								" changed from " + oldValue + " to " + newValue);
					}
					return true;
				}
			}
		}
		return false;
	}

}
