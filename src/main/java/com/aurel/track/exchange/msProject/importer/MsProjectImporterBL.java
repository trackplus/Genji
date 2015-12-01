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

package com.aurel.track.exchange.msProject.importer;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.ProjectHeader;
import net.sf.mpxj.Relation;
import net.sf.mpxj.Resource;
import net.sf.mpxj.ResourceAssignment;
import net.sf.mpxj.Task;
import net.sf.mpxj.TimeUnit;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Element;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.lists.systemOption.StatusBL;
import com.aurel.track.admin.customize.role.FieldsRestrictionsToRoleBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.project.ProjectConfigBL;
import com.aurel.track.admin.project.release.ReleaseBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TActualEstimatedBudgetBean;
import com.aurel.track.beans.TBudgetBean;
import com.aurel.track.beans.TBudgetBean.BUDGET_TYPE;
import com.aurel.track.beans.TComputedValuesBean;
import com.aurel.track.beans.TCostBean;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TMSProjectTaskBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.MsProjectTaskDAO;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.errors.ErrorHandlerJSONAdapter;
import com.aurel.track.errors.ErrorParameter;
import com.aurel.track.exchange.ImportCounts;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeBL;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDOMHelper;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDataStoreBean;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDataStoreBean.ASSIGNMENT_ELEMENTS;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDataStoreBean.COMMON_ELEMENTS;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDataStoreBean.LAG_FORMAT;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDataStoreBean.PREDECESSOR_CROSS_PROJECT;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDataStoreBean.PROJECT_ELEMENTS;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDataStoreBean.TASK_TYPE;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.FieldsManagerRT;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.runtime.system.select.SystemManagerRT;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.budgetCost.AccountingBL;
import com.aurel.track.item.budgetCost.AccountingBL.TIMEUNITS;
import com.aurel.track.item.budgetCost.BudgetBL;
import com.aurel.track.item.budgetCost.ComputedValueBL;
import com.aurel.track.item.budgetCost.ExpenseBL;
import com.aurel.track.item.budgetCost.RemainingPlanBL;
import com.aurel.track.item.history.HistoryLoaderBL;
import com.aurel.track.item.history.HistoryLoaderBL.LONG_TEXT_TYPE;
import com.aurel.track.item.history.HistoryValues;
import com.aurel.track.item.link.ItemLinkBL;
import com.aurel.track.linkType.LinkTypeBL;
import com.aurel.track.linkType.MsProjectLinkType;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.report.execute.ComputedValuesLoaderBL;
import com.aurel.track.resources.LocalizationKeyPrefixes;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.screen.SystemActions;
import com.aurel.track.screen.item.bl.runtime.ScreenRuntimeBL;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.PropertiesHelper;

public class MsProjectImporterBL {
	private static final Logger LOGGER = LogManager.getLogger(MsProjectImporterBL.class);
	private static MsProjectTaskDAO msProjectTaskDAO = DAOFactory.getFactory().getMsProjectTaskDAO();
	
	public interface EXCHANGE_DIRECTION {
		static public int IMPORT = 1;
		static public int EXPORT = 2;
	}
	
	public interface CONFLICT_RESOLUTION {
		static public Boolean OVERWRITE = Boolean.valueOf(true);
		static public Boolean LEAVE = Boolean.valueOf(false);
	}
	
	public interface DELETE_RESOLUTION {
		static public Boolean LEAVE = Boolean.valueOf(false);
		static public Boolean DELETE = Boolean.valueOf(true);
	}
	
	public interface UNDELETE_RESOLUTION {
		static public Boolean LEAVE = Boolean.valueOf(false);
		static public Boolean UNDELETE = Boolean.valueOf(true);
	}
	
	public interface CONFLICT_MAP_ENTRY {
		public static Integer UID = Integer.valueOf(0);
		public static Integer WORKITEM_ID = Integer.valueOf(1);
		public static Integer SYNOPSIS = Integer.valueOf(2);
		public static Integer MS_PROJECT_VALUE = Integer.valueOf(3);
		public static Integer TRACKPLUS_VALUE = Integer.valueOf(4);
		public static Integer ITEM_ID = Integer.valueOf(5);
	}
			
	public interface CONFLICT_ON {
		public static final int BUDGET = 1;
		public static final int STARTDATE = 2;
		public static final int ENDDATE = 3;
		public static final int DELETED = 4;
		public static final int UNDELETED = 5;
	}
	
	/**
	 * Prepares the resources match
	 * @return
	 */
	static String prepareResourceMatch(MsProjectExchangeDataStoreBean msProjectExchangeDataStoreBean, Locale locale) {
		//there are some resources in the msProject file
		List<TPersonBean> personsList = MSProjectResourceMappingBL.getTrackPlusPersons();
		List<IntegerStringBean> trackPlusPersons = new ArrayList<IntegerStringBean>();
		//Map<String, Integer> trackLoginNameToPersonIDMap = new HashMap<String, Integer>();
		Map<String, ILabelBean> trackPersonFullnameToPersonBeanMap = new HashMap<String, ILabelBean>();
		for (TPersonBean trackPlusPerson : personsList) {
			String fullName = trackPlusPerson.getName();
			trackPersonFullnameToPersonBeanMap.put(fullName, trackPlusPerson);
			trackPlusPersons.add(new IntegerStringBean(
					trackPlusPerson.getLoginName() + " (" + fullName + ")", trackPlusPerson.getObjectID()));
		}
		trackPlusPersons.add(0, new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
				"admin.actions.importMSProject.lbl.addResourceAsNewUser", locale),	MSProjectResourceMappingBL.ADD_AS_NEW));
		Map<Integer, Integer> resourceUIDToPersonIDMap = msProjectExchangeDataStoreBean.getResourceUIDToPersonIDMap();
		//first rendering try to match by similar names		
		SortedMap<String, Integer> resourceNameToResourceUIDMap=MsProjectImporterBL.getResourceNameToResourceUIDMap(msProjectExchangeDataStoreBean.getWorkResources());
		if (resourceUIDToPersonIDMap==null) {
			//no previous import -> no previous resource to person mapping stored in the database
			resourceUIDToPersonIDMap = new HashMap<Integer, Integer>();
		}
		Iterator<String> itrResourceNameToUID = resourceNameToResourceUIDMap.keySet().iterator();
		while (itrResourceNameToUID.hasNext()) {
			String name = itrResourceNameToUID.next();
			Integer UID = resourceNameToResourceUIDMap.get(name);
			if (resourceUIDToPersonIDMap.get(UID)==null) {
				Integer personID =new SystemManagerRT().getLookupIDByLabel(SystemFields.INTEGER_PERSON, 
						null, null, locale, name, trackPersonFullnameToPersonBeanMap, null);
				if (personID==null) {
					resourceUIDToPersonIDMap.put(UID, MSProjectResourceMappingBL.ADD_AS_NEW);
				} else {
					resourceUIDToPersonIDMap.put(UID, personID);
				}
			}
		}
		return MsProjectImportJSON.getResourceMappingJSON(trackPlusPersons, resourceNameToResourceUIDMap, resourceUIDToPersonIDMap);
	}
	
	/**
	 * Imports the tasks from the msProjectImporterBean into the trackPlus database
	 * @param msProjectExchangeDataStoreBean
	 * @param resourceUIDsToPersonIDs
	 * @param errorsMap
	 * @param workItemContextsMap
	 * @return
	 */
	public static SortedMap<Integer, List<ErrorData>> validateAndPrepareTasks(
			MsProjectExchangeDataStoreBean msProjectExchangeDataStoreBean,
			Map<Integer, TWorkItemBean> existingTrackPlusWorkItems,
			Map<Integer, TMSProjectTaskBean> existingTrackPlusTasks,
			Map<Integer, WorkItemContext> workItemContextsMap) {
		
		SortedMap<Integer, List<ErrorData>> errorsMap = new TreeMap<Integer, List<ErrorData>>();
		Integer issueTypeID = msProjectExchangeDataStoreBean.getIssueType();
		Integer projectID = msProjectExchangeDataStoreBean.getProjectID();
		TPersonBean personBean = msProjectExchangeDataStoreBean.getPersonBean();
		Integer personID = personBean.getObjectID();
		Locale locale = msProjectExchangeDataStoreBean.getLocale();
		Set<Integer> presentFieldIDs = getPresentFields(projectID, issueTypeID);
		Map<Integer, TFieldConfigBean> fieldConfigsMap = FieldRuntimeBL.getLocalizedFieldConfigs(
				presentFieldIDs, projectID, issueTypeID, locale);
		
		Map<Integer, TWorkItemBean> workItemBeanMap = prepareWorkItemBeans(msProjectExchangeDataStoreBean, 
				existingTrackPlusTasks, existingTrackPlusWorkItems, fieldConfigsMap);
		Map<String, Object> fieldSettings = FieldRuntimeBL.getFieldSettings(fieldConfigsMap);
		//build the workItemContext for each workItemBean and validate the workItemBeans before save
		Iterator<Integer> itrUID = workItemBeanMap.keySet().iterator();
		while (itrUID.hasNext()) {
			Integer UID = itrUID.next();
			TWorkItemBean workItemBean = workItemBeanMap.get(UID);
			WorkItemContext workItemContext = FieldsManagerRT.createImportContext(workItemBean,
					presentFieldIDs, personID, locale,
					fieldConfigsMap, fieldSettings);
			workItemContextsMap.put(UID, workItemContext);
			List<ErrorData> validationErrorList = FieldsManagerRT.validate(workItemBean, workItemContext, false, false, null, false, false);
			if (validationErrorList!=null && !validationErrorList.isEmpty()) {
				errorsMap.put(UID, validationErrorList);
			}
		}
		return errorsMap;
	}
	
	/**
	 * Gets the project specific IDs map
	 * @param workItemIDs
	 * @param entityID
	 * @param entityType
	 * @return
	 */
	static Map<Integer, String> getProjectSpecificItemIDs(int[] workItemIDs, Integer entityID, Integer entityType) {
		Map<Integer, String> projectSpecificItemIDsMap = null;
		if (ApplicationBean.getInstance().getSiteBean().getProjectSpecificIDsOn()) {
			projectSpecificItemIDsMap = ItemBL.getProjectSpecificIssueIDsMap(
					ItemBL.loadByWorkItemKeys(workItemIDs));
		}
		return projectSpecificItemIDsMap;
	}
	
	/**
	 * Get the conflicted budgets map: those budgets which are different in Genji
	 * and the one found in import file since the last edit of the MS Project file
	 * Whether the budget was changed in MS Project file or in Genji not has no importance
	 * @param msProjectExchangeDataStoreBean
	 * @param taskUIDToWorkItemIDMap
	 * @param overwriteBudgetMap
	 * @param budgetConflictValuesMap
	 */
	static void budgetConflictResolution(
			MsProjectExchangeDataStoreBean msProjectExchangeDataStoreBean,
			Double hoursPerWorkday,
			Map<Integer, Integer> taskUIDToWorkItemIDMap,
			Map<Integer, Boolean> overwriteBudgetMap,
			Map<Integer, Map<Integer, String>> budgetConflictValuesMap,
			Map<Integer, String> projectSpecificItemIDsMap) {
		
		List<Task> msProjectTasks = msProjectExchangeDataStoreBean.getTasks();
		Locale locale = msProjectExchangeDataStoreBean.getLocale();
		Date projectLastSavedDate = msProjectExchangeDataStoreBean.getLastSavedDate(); 	
		String strHours = LocalizeUtil.getLocalizedTextFromApplicationResources(
				LocalizationKeyPrefixes.TIME_UNIT_KEY_PREFIX + AccountingBL.TIMEUNITS.HOURS, locale);
		String strWorkdays = LocalizeUtil.getLocalizedTextFromApplicationResources(
				LocalizationKeyPrefixes.TIME_UNIT_KEY_PREFIX + AccountingBL.TIMEUNITS.WORKDAYS, locale);
		
		//budget conflict handling
		//budgets modified after project modification date, no matter who has modified (personID=null)
		Collection<Integer> existingWorkItems = taskUIDToWorkItemIDMap.values();
		SortedMap<Integer, TBudgetBean> workItemIDToConflictingBudgetBeanMap = BudgetBL.loadLastBudgetForWorkItemsMap(
				GeneralUtils.createIntArrFromIntegerList(GeneralUtils.createIntegerListFromCollection(existingWorkItems)), 
				null, null, projectLastSavedDate, null);
		Iterator<Task> itrTaskElement = msProjectTasks.iterator();
		while (itrTaskElement.hasNext()) {
			Task taskElement = itrTaskElement.next();
			Double UIDTmp = (Double) taskElement.getFieldByAlias("trackPlusId");
			Integer UID;
			if(UIDTmp != null) {
				UID = UIDTmp.intValue();
			}else {
				UID = taskElement.getUniqueID();
			}
			Integer workItemID = taskUIDToWorkItemIDMap.get(UID);
			
			if (workItemID!=null) {
				//workItem exists in track+ ...
				TBudgetBean budgetBeanTrackPlus = workItemIDToConflictingBudgetBeanMap.get(workItemID);
				if (budgetBeanTrackPlus!=null) {
					//...and track+ budget exists: possible conflict
					//transform the track+ side budget to hours because the msProject duration is in hours
					//TODO see whether msProject duration is always in hours
					Double transformedHours = AccountingBL.transformToTimeUnits(budgetBeanTrackPlus.getEstimatedHours(), 
							hoursPerWorkday, budgetBeanTrackPlus.getTimeUnit(), AccountingBL.TIMEUNITS.HOURS);
					//set this values for hasChanged
					budgetBeanTrackPlus.setTimeUnit(AccountingBL.TIMEUNITS.HOURS);
					budgetBeanTrackPlus.setEstimatedHours(transformedHours);
										
					TBudgetBean budgetBeanMsProject = new TBudgetBean();
					budgetBeanMsProject.setEstimatedHours(taskElement.getWork().getDuration());
					budgetBeanMsProject.setTimeUnit(TIMEUNITS.HOURS);
					//leave the original value for fields which are not present in 
					//MS Project to not cause the hasChanged function to return erroneously true 
					budgetBeanMsProject.setEstimatedCost(budgetBeanTrackPlus.getEstimatedCost());
					budgetBeanMsProject.setChangeDescription(budgetBeanTrackPlus.getChangeDescription());
					if (budgetBeanMsProject.hasChanged(budgetBeanTrackPlus)) {
						Boolean overwriteBudget = overwriteBudgetMap.get(workItemID);
						if (overwriteBudget==null) {
							//conflict was not handled (it should be rendered)
							//add as conflict							
							Map<Integer, String> conflictValuesMapForTask = new HashMap<Integer, String>();
							budgetConflictValuesMap.put(workItemID, conflictValuesMapForTask);
							conflictValuesMapForTask.put(CONFLICT_MAP_ENTRY.UID, UID.toString());
							conflictValuesMapForTask.put(CONFLICT_MAP_ENTRY.WORKITEM_ID, workItemID.toString());
							if (projectSpecificItemIDsMap!=null) {
								conflictValuesMapForTask.put(CONFLICT_MAP_ENTRY.ITEM_ID, projectSpecificItemIDsMap.get(workItemID));
							}
							conflictValuesMapForTask.put(CONFLICT_MAP_ENTRY.SYNOPSIS, taskElement.getName());
							conflictValuesMapForTask.put(CONFLICT_MAP_ENTRY.MS_PROJECT_VALUE, 
									AccountingBL.getFormattedDouble(budgetBeanMsProject.getEstimatedHours(), locale, true) + " " + strHours+ "");
							String timeUnit;
							if (budgetBeanTrackPlus.getTimeUnit()==null || budgetBeanTrackPlus.getTimeUnit().equals(TIMEUNITS.HOURS)) {
								timeUnit = strHours;	
							} else {
								timeUnit = strWorkdays;
							}
							conflictValuesMapForTask.put(CONFLICT_MAP_ENTRY.TRACKPLUS_VALUE, 
									AccountingBL.getFormattedDouble(budgetBeanTrackPlus.getEstimatedHours(), locale, true) + " " + timeUnit);
							//initialize with overwrite
							overwriteBudgetMap.put(workItemID, Boolean.valueOf(CONFLICT_RESOLUTION.OVERWRITE));
						}
					}
				}
			}
		}
	}
	
	/**
	 * Get the conflicted start and end dates map: those dates which were changed in Genji to a value 
	 * different from the ones found in import file since the last modification of the MS Project file
	 * Whether the date was changed in MS Project file or not has no importance 
	 * @param msProjectExchangeDataStoreBean
	 * @param existingTrackPlusWorkItems
	 * @param taskUIDToWorkItemIDMap
	 * @param overwriteStartDateMap
	 * @param overwriteEndDateMap
	 * @param startDateConflictValuesMap
	 * @param endDateConflictValuesMap
	 * @param workItemContextsMap
	 */
	static void dateConflictResolution(
			MsProjectExchangeDataStoreBean msProjectExchangeDataStoreBean,
			Map<Integer, TWorkItemBean> existingTrackPlusWorkItems,
			Map<Integer, Integer> taskUIDToWorkItemIDMap,
			Map<Integer, Boolean> overwriteStartDateMap,
			Map<Integer, Boolean> overwriteEndDateMap,
			Map<Integer, Map<Integer, String>> startDateConflictValuesMap,
			Map<Integer, Map<Integer, String>> endDateConflictValuesMap,
			Map<Integer, WorkItemContext> workItemContextsMap,
			Map<Integer, String> projectSpecificItemIDsMap) {
		
		List<Task> msProjectTasks = msProjectExchangeDataStoreBean.getTasks();
		Locale locale = msProjectExchangeDataStoreBean.getLocale();

		Date projectLastSavedDate = msProjectExchangeDataStoreBean.getLastSavedDate();
		
		Map<Integer, SortedMap<Integer, Map<Integer, HistoryValues>>> conflictingStartDates = 
			HistoryLoaderBL.getWorkItemsHistory(
					GeneralUtils.createIntArrFromSet(existingTrackPlusWorkItems.keySet()), 
					new Integer[] {SystemFields.INTEGER_STARTDATE}, 
					true, null, projectLastSavedDate, null, locale, false, LONG_TEXT_TYPE.ISFULLHTML, false, null);
		Map<Integer, SortedMap<Integer, Map<Integer, HistoryValues>>> conflictingEndDates = 
		HistoryLoaderBL.getWorkItemsHistory(
					GeneralUtils.createIntArrFromSet(existingTrackPlusWorkItems.keySet()), 
					new Integer[] {SystemFields.INTEGER_ENDDATE}, 
					true, null, projectLastSavedDate, null, locale, false, LONG_TEXT_TYPE.ISFULLHTML, false, null);
		Iterator<Task> itrTaskElement = msProjectTasks.iterator();
		HashSet<Integer> taskUIDSet = new HashSet<Integer>(taskUIDToWorkItemIDMap.values());
		while (itrTaskElement.hasNext()) {
			Task taskElement = itrTaskElement.next();
			Double UIDTmp = (Double) taskElement.getFieldByAlias("trackPlusId");
			Integer UID;
			if(UIDTmp != null) {
				UID = UIDTmp.intValue();
			}else {
				UID = taskElement.getUniqueID();
			}
			Integer workItemID = null;
			if(taskUIDSet.contains(UID)) {
				workItemID = UID;
			}
			WorkItemContext workItemContext = workItemContextsMap.get(UID);
			if (workItemID!=null) {
				//date history exists in track+ ... (the HistoryValues is not 
				//important because the dates are taken from the workItemBeanOriginal)
				Map<Integer, Map<Integer, HistoryValues>> startDateConflict = conflictingStartDates.get(workItemID);
				Map<Integer, Map<Integer, HistoryValues>> endDateConflict = conflictingEndDates.get(workItemID);
				//we should take the original one because by preparing the workItemBean it was overwritten 
				TWorkItemBean workItemBeanOriginal = workItemContext.getWorkItemBeanOriginal();
				if (workItemBeanOriginal!=null) {
					if (startDateConflict!=null) {
						Date startDateTrackplus = workItemBeanOriginal.getStartDate();
						Date startDateMsProject = taskElement.getStart();
						if (EqualUtils.notEqualDateNeglectTime(startDateTrackplus, startDateMsProject)) {
							Boolean overwriteStartDate = overwriteStartDateMap.get(workItemID);
							if (overwriteStartDate==null) {
								//conflict wasnot handled (it should be rendered)
								//add as conflict
								Map<Integer, String> conflictValuesMapForTask = new HashMap<Integer, String>();
								startDateConflictValuesMap.put(workItemID, conflictValuesMapForTask);
								conflictValuesMapForTask.put(CONFLICT_MAP_ENTRY.UID, UID.toString());
								conflictValuesMapForTask.put(CONFLICT_MAP_ENTRY.WORKITEM_ID, workItemID.toString());
								if (projectSpecificItemIDsMap!=null) {
									conflictValuesMapForTask.put(CONFLICT_MAP_ENTRY.ITEM_ID, projectSpecificItemIDsMap.get(workItemID));
								}
								conflictValuesMapForTask.put(CONFLICT_MAP_ENTRY.SYNOPSIS, taskElement.getName());
								conflictValuesMapForTask.put(CONFLICT_MAP_ENTRY.MS_PROJECT_VALUE, 
										DateTimeUtils.getInstance().formatGUIDate(startDateMsProject, locale));
								conflictValuesMapForTask.put(CONFLICT_MAP_ENTRY.TRACKPLUS_VALUE, 
										DateTimeUtils.getInstance().formatGUIDate(startDateTrackplus, locale));
								//initialize with overwrite
								overwriteStartDateMap.put(workItemID, Boolean.valueOf(CONFLICT_RESOLUTION.OVERWRITE));
							}
						}
					}
					if (endDateConflict!=null) {
						Date endDateTrackplus = workItemBeanOriginal.getEndDate();
						Date endDateMsProject = taskElement.getFinish();
						if (EqualUtils.notEqualDateNeglectTime(endDateTrackplus, endDateMsProject)) {
							Boolean overwriteEndDate = overwriteEndDateMap.get(workItemID);
							if (overwriteEndDate==null) {
								//conflict wasnot handled (it should be rendered)
								//add as conflict
								Map<Integer, String> conflictValuesMapForTask = new HashMap<Integer, String>();
								endDateConflictValuesMap.put(workItemID, conflictValuesMapForTask);
								conflictValuesMapForTask.put(CONFLICT_MAP_ENTRY.UID, UID.toString());
								conflictValuesMapForTask.put(CONFLICT_MAP_ENTRY.WORKITEM_ID, workItemID.toString());
								conflictValuesMapForTask.put(CONFLICT_MAP_ENTRY.SYNOPSIS, taskElement.getName());
								conflictValuesMapForTask.put(CONFLICT_MAP_ENTRY.MS_PROJECT_VALUE, 
										DateTimeUtils.getInstance().formatShortDate(endDateMsProject, locale));
								conflictValuesMapForTask.put(CONFLICT_MAP_ENTRY.TRACKPLUS_VALUE, 
										DateTimeUtils.getInstance().formatShortDate(endDateTrackplus, locale));
								//initialize with overwrite
								overwriteEndDateMap.put(workItemID, Boolean.valueOf(CONFLICT_RESOLUTION.OVERWRITE));
							} 
						}
					}
				}
			}
		}		
	}
	
 /**
	* Get the questionable issues to delete or undelete 
	* @param msProjectExchangeDataStoreBean
	* @param existingTrackPlusTaskWorkItems
	* @param taskUIDToWorkItemIDMap
	* @param deleteValuesMap
	* @param deleteTaskMap
	* @param undeleteValuesMap
	* @param undeleteTaskMap
	*/
	public static void getDeleteUndeleteTrackWorkItems(
			MsProjectExchangeDataStoreBean msProjectExchangeDataStoreBean,
			Map<Integer, TWorkItemBean> existingTrackPlusTaskWorkItems,
			Map<Integer, Integer> taskUIDToWorkItemIDMap, 
			Map<Integer, Map<Integer, String>> deleteValuesMap,
			Map<Integer, Boolean> deleteTaskMap, 
			Map<Integer, Map<Integer, String>> undeleteValuesMap,
			Map<Integer, Boolean> undeleteTaskMap, Map<Integer, String> projectSpecificItemIDsMap) {
		Date projectLastSavedDate = msProjectExchangeDataStoreBean.getLastSavedDate();
		Map<Integer, TStateBean> stateBeansMap = GeneralUtils.createMapFromList(StatusBL.loadAll());
		//MS Project side UIDs
		List<Task> tasks = msProjectExchangeDataStoreBean.getTasks();
		Set<Integer> msProjectSideUIDs = new HashSet<Integer>();
		Iterator<Task> itrTaskElement = tasks.iterator();
		while (itrTaskElement.hasNext()) {
			Task task = itrTaskElement.next();
			Double UIDTmp = (Double) task.getFieldByAlias("trackPlusId");
			Integer UID;
			if(UIDTmp != null) {
				UID = UIDTmp.intValue();
			}else {
				UID = task.getUniqueID();
			}
			msProjectSideUIDs.add(UID);
		}
		//set the workItems deleted from the msProject import file to deleted
		Set<Integer> trackplusSideUIDs = new HashSet<Integer>(taskUIDToWorkItemIDMap.values());
		Iterator<Integer> itrTrackPlusUIDs = trackplusSideUIDs.iterator();
		while (itrTrackPlusUIDs.hasNext()) {
			Integer UID = itrTrackPlusUIDs.next();
			Integer workItemID = UID;
			TWorkItemBean workItemBean = existingTrackPlusTaskWorkItems.get(workItemID);
			if (workItemBean!=null) {
				if (msProjectSideUIDs.contains(UID)) {
					//UID is found MS Project file 
					if (workItemBean.isArchivedOrDeleted()) {
						//if the issue is archived or deletced:
						//it should be asked whether to unarchive/undelete them. 
						//Typically it happens when an older MS project is imported 
						//after a newer MS Project import deleted the issue (or the Genji user deleted/archived the issue)
						Boolean undeleteTask = undeleteTaskMap.get(workItemID);
						if (undeleteTask==null) {
							Map<Integer, String> undeleteValuesMapForTask = new HashMap<Integer, String>();
							undeleteValuesMap.put(workItemID, undeleteValuesMapForTask);
							undeleteValuesMapForTask.put(CONFLICT_MAP_ENTRY.UID, UID.toString());
							undeleteValuesMapForTask.put(CONFLICT_MAP_ENTRY.WORKITEM_ID, workItemID.toString());
							if (projectSpecificItemIDsMap!=null) {
								undeleteValuesMapForTask.put(CONFLICT_MAP_ENTRY.ITEM_ID, projectSpecificItemIDsMap.get(workItemID));
							}
							undeleteValuesMapForTask.put(CONFLICT_MAP_ENTRY.SYNOPSIS, workItemBean.getSynopsis());
							//initialize with leave
							
							undeleteTaskMap.put(workItemID, Boolean.valueOf(UNDELETE_RESOLUTION.LEAVE));
						}					
					}
				} else {
					//the UID found in Genji is missing from MS Project import file.
					//The project creation date typically has no importance because: we have UID (MsProjectTask) for 
					//the issue (otherwise the workItem would not be in existingTrackPlusTaskWorkItems Tasks)
					//it means it was already imported or exported and by importing the most actual Ms Project file:
					//projectLastSavedDate.after(workItemBean.getCreated()) is trueexcept when
					//1. in the Genji side a task workItem was created after this date but in another Project/Release
					//and now it is moved to this Project/Release, but still we shouldn't ask for deleting it
					//2. importing an older MSProject file in can bethat the task workItem was created after 
					//the MS Project file creation. It this case we will not ask whether to set as deleted this
					//"newer" (from the point of view of the older MS Project file) issues to get to a previous version
					int stateFlag = TStateBean.STATEFLAGS.ACTIVE;
					TStateBean stateBean = stateBeansMap.get(workItemBean.getStateID());
					if (stateBean!=null && stateBean.getStateflag()!=null) {
						stateFlag = stateBean.getStateflag().intValue();
					}
					if (projectLastSavedDate.after(workItemBean.getCreated()) && !workItemBean.isArchivedOrDeleted() && 
							stateFlag!=TStateBean.STATEFLAGS.CLOSED) {
						//if a missing UID from MS Project refers to an active (unarchived/undeleted and not closed) issue
						//it should be asked whether to set it as deleted. Closed issues will not be asked to set as deleted 
						Boolean deleteTask = deleteTaskMap.get(workItemID);
						if (deleteTask==null) {
							Map<Integer, String> deleteValuesMapForTask = new HashMap<Integer, String>();
							deleteValuesMap.put(workItemID, deleteValuesMapForTask);
							deleteValuesMapForTask.put(CONFLICT_MAP_ENTRY.UID, UID.toString());
							deleteValuesMapForTask.put(CONFLICT_MAP_ENTRY.WORKITEM_ID, workItemID.toString());
							if (projectSpecificItemIDsMap!=null) {
								deleteValuesMapForTask.put(CONFLICT_MAP_ENTRY.ITEM_ID, projectSpecificItemIDsMap.get(workItemID));
							}
							deleteValuesMapForTask.put(CONFLICT_MAP_ENTRY.SYNOPSIS, workItemBean.getSynopsis());
							//initialize with leave
							deleteTaskMap.put(workItemID, Boolean.valueOf(DELETE_RESOLUTION.LEAVE));
						}		
					}
				}
			}
		}
	}

	/**
	 * Imports the tasks from the msProjectImporterBean into the trackPlus database
	 * @param msProjectExchangeDataStoreBean
	 * @param existingTrackPlusWorkItems
     * @param existingTrackPlusTasks
     * @param hoursPerWorkday
	 * @param errorsMap
     * @param taskUIDToWorkItemIDMap@p
     * @param errorsMap
	 * @param workItemContextsMap
     * @param overwriteBudgetMap
     * @param overwriteStartDateMap
     * @param overwriteEndDateMap
     * @param deleteTaskMap
     * @param undeleteTaskMap
	 * @return	 
	 */
	public static ImportCounts importTasks(
			MsProjectExchangeDataStoreBean msProjectExchangeDataStoreBean,
			Map<Integer, TWorkItemBean> existingTrackPlusWorkItems, 
			Map<Integer, TMSProjectTaskBean> existingTrackPlusTasks,
			Double hoursPerWorkday, Map<Integer, Integer> taskUIDToWorkItemIDMap,
			SortedMap<Integer, List<ErrorData>> errorsMap, Map<Integer, WorkItemContext> workItemContextsMap,
			Map<Integer, Boolean> overwriteBudgetMap, 
			Map<Integer, Boolean> overwriteStartDateMap,
			Map<Integer, Boolean> overwriteEndDateMap, 
			Map<Integer, Boolean> deleteTaskMap,
			Map<Integer, Boolean> undeleteTaskMap,
			Map<Integer, TCostBean> workItemIdToCostBean) {
		
		ImportCounts importCounts = new ImportCounts();
		TPersonBean personBean = msProjectExchangeDataStoreBean.getPersonBean();
		Integer personID = personBean.getObjectID();
		Locale locale = msProjectExchangeDataStoreBean.getLocale();
		
		List<Task> msProjectTasks = msProjectExchangeDataStoreBean.getTasks();
		String firstLevelPatternExpr = "^[0-9]+$";
		Pattern pattern = Pattern.compile(firstLevelPatternExpr);
		Matcher matcher = null;	 
		for(Iterator<Task> iterator = msProjectTasks.iterator(); iterator.hasNext();){
			Task taskElement = iterator.next();
			Double UIDTmp = (Double) taskElement.getFieldByAlias("trackPlusId");
			Integer UID;
			if(UIDTmp != null) {
				UID = UIDTmp.intValue();
			}else {
				UID = taskElement.getUniqueID();
			}
			if (MsProjectExchangeDOMHelper.UIDIsValidMpxjApiTemp(taskElement)) {
				String outlineNumber = taskElement.getOutlineNumber();
				if (outlineNumber!=null) {
					matcher = pattern.matcher(outlineNumber);
					//first add the items from the first level (issues without a parent)
					if (matcher.find()) {
						saveTasks(msProjectExchangeDataStoreBean, taskElement, UID, 
								outlineNumber, null, workItemContextsMap, /*existingTrackPlusWorkItems,*/ existingTrackPlusTasks,
								taskUIDToWorkItemIDMap, overwriteStartDateMap, overwriteEndDateMap, errorsMap, importCounts);
					}
				}
			} else {
				//remove the invalid (dummy) task
				iterator.remove();
			}			
		}		
		//set the tasks deleted in MSProject as deleted
		deleteTasks(existingTrackPlusWorkItems, deleteTaskMap, undeleteTaskMap, importCounts, personID, locale);
		//import the budgets

		importBudgets(msProjectExchangeDataStoreBean, hoursPerWorkday, taskUIDToWorkItemIDMap, overwriteBudgetMap, importCounts, workItemIdToCostBean);
		
		importRemainingBudgets(msProjectExchangeDataStoreBean, workItemContextsMap, taskUIDToWorkItemIDMap, overwriteBudgetMap);
		
		//the links can be added only after each issue is saved to have the corresponding UID to WorkItemID mappings
		importTaskDependencyLinks(msProjectExchangeDataStoreBean, taskUIDToWorkItemIDMap, importCounts);
		//save the hoursPerworkDay from import if differs
		saveProjectReleaseParameters(msProjectExchangeDataStoreBean);
		return importCounts;
	}
	
	/**
	 * Get the hours per day according to the imported project
	 * @param rootElement
	 * @return
	 */
	public static Double getHoursPerWorkday(ProjectHeader header) {
		Integer minutesPerDay = header.getMinutesPerDay().intValue();
		
		if (minutesPerDay==null || minutesPerDay.intValue()==0) {
			//for not valid values fall back to default
			minutesPerDay = Double.valueOf(60*AccountingBL.DEFAULTHOURSPERWORKINGDAY).intValue();
		}
		return AccountingBL.roundToDecimalDigits(new Double(minutesPerDay)/60, true);
	}
	
	/**
	 * Saves project specific data
	 * @param msProjectExchangeDataStoreBean
	 */
	private static void saveProjectReleaseParameters(
			MsProjectExchangeDataStoreBean msProjectExchangeDataStoreBean) {
		Element rootElement = msProjectExchangeDataStoreBean.getRootElement();
		TProjectBean projectBean = msProjectExchangeDataStoreBean.getProjectBean();
		Integer releaseScheduledID = msProjectExchangeDataStoreBean.getReleaseScheduledID();
		if (projectBean!=null) {
			//TODO overwrite currency symbol?
			String currencySymbol = msProjectExchangeDataStoreBean.getProject().getProjectHeader().getCurrencySymbol();
			if (currencySymbol!=null && EqualUtils.notEqual(projectBean.getCurrencySymbol(), currencySymbol)) {
				projectBean.setCurrencySymbol(currencySymbol);
			}
			
			Integer minutesPerDay = msProjectExchangeDataStoreBean.getProject().getProjectHeader().getMinutesPerDay().intValue();
			if (minutesPerDay==null || minutesPerDay.intValue()==0) {
				//for not valid values fall back to default
				minutesPerDay = Double.valueOf(60*AccountingBL.DEFAULTHOURSPERWORKINGDAY).intValue();
			}
			projectBean.setMoreProps(PropertiesHelper.setProperty(
					projectBean.getMoreProps(), TProjectBean.MOREPPROPS.MINUTES_PER_DAY,
					minutesPerDay.toString()));
			projectBean.setHoursPerWorkDay(AccountingBL.roundToDecimalDigits(new Double(minutesPerDay)/60, true));
			Integer minutesPerWeek = MsProjectExchangeDOMHelper.getSubelementInteger(rootElement, PROJECT_ELEMENTS.MINUTESPERWEEK);
			if (minutesPerWeek==null || minutesPerWeek.intValue()==0) {
				minutesPerWeek = Double.valueOf(60*AccountingBL.DEFAULTHOURSPERWORKINGDAY*5).intValue();
			}			
			projectBean.setMoreProps(PropertiesHelper.setProperty(
					projectBean.getMoreProps(), TProjectBean.MOREPPROPS.MINUTES_PER_WEEK,
					minutesPerWeek.toString()));
			
			Integer daysPerMonth = msProjectExchangeDataStoreBean.getProject().getProjectHeader().getDaysPerMonth().intValue();
			if (daysPerMonth==null) {
				daysPerMonth = Integer.valueOf(20);
			}
			projectBean.setMoreProps(PropertiesHelper.setProperty(
					projectBean.getMoreProps(), TProjectBean.MOREPPROPS.DAYS_PER_MONTH,
					daysPerMonth.toString()));
			
			Integer defaultTaskType = msProjectExchangeDataStoreBean.getProject().getProjectHeader().getDefaultTaskType().getValue();
			if (defaultTaskType==null) {
				defaultTaskType = TASK_TYPE.FIXED_UNITS;
			}
			projectBean.setMoreProps(PropertiesHelper.setProperty(
					projectBean.getMoreProps(), TProjectBean.MOREPPROPS.DEFAULT_TASK_TYPE,
					defaultTaskType.toString()));
			
			Integer durationFormat = msProjectExchangeDataStoreBean.getProject().getProjectHeader().getDuration().getUnits().getValue();
			if (durationFormat==null) {
				durationFormat = Integer.valueOf(LAG_FORMAT.d);
			}
			projectBean.setMoreProps(PropertiesHelper.setProperty(
					projectBean.getMoreProps(), TProjectBean.MOREPPROPS.DURATION_FORMAT,
					durationFormat.toString()));
			
			//saves the resource to person mapping either in release or in project moreProps 
			Map<Integer, Integer> resourceUIDToPersonIDMap =msProjectExchangeDataStoreBean.getResourceUIDToPersonIDMap();
			String resourceUIDToPersonIDStr = MsProjectExchangeBL.transformResourceMappingsToString(resourceUIDToPersonIDMap);
			if (resourceUIDToPersonIDStr!=null) {
				if (releaseScheduledID==null) {
						//project level import -> save in project 
						projectBean.setMoreProps(PropertiesHelper.setProperty(
								projectBean.getMoreProps(), TProjectBean.MOREPPROPS.RESOURCE_PERSON_MAPPINGS,
								resourceUIDToPersonIDStr));
					}
				 else {
					//release level import -> save in release
					TReleaseBean releaseBean = LookupContainer.getReleaseBean(releaseScheduledID);
					if (releaseBean!=null) {
						releaseBean.setMoreProps(PropertiesHelper.setProperty(
								releaseBean.getMoreProps(), TReleaseBean.MOREPPROPS.RESOURCE_PERSON_MAPPINGS,
								resourceUIDToPersonIDStr));
						ReleaseBL.saveSimple(releaseBean);
					}
				}
			}			
			ProjectBL.saveSimple(projectBean);
		}
	}
	
	private static void deleteTasks(Map<Integer, TWorkItemBean> existingTrackPlusWorkItems, 
			Map<Integer, Boolean> deleteTaskMap, Map<Integer, Boolean> undeleteTaskMap, 
			ImportCounts importCounts, Integer personID, Locale locale) {
		
		if (deleteTaskMap!=null) {
			Iterator<Integer> iterator = deleteTaskMap.keySet().iterator();
			while (iterator.hasNext()) {
				Integer workItemID = iterator.next();
				Boolean delete = deleteTaskMap.get(workItemID);
				if (delete!=null && delete.booleanValue()) {
					TWorkItemBean workItemBean = existingTrackPlusWorkItems.get(workItemID);
					if (workItemBean!=null) {
						FieldsManagerRT.saveOneField(personID, workItemBean.getObjectID(), locale, false, 
							SystemFields.INTEGER_ARCHIVELEVEL, TWorkItemBean.ARCHIVE_LEVEL_DELETED, false, null, new ArrayList<ErrorData>());
						importCounts.setNoOfDeletedIssues(importCounts.getNoOfDeletedIssues()+1);
					}
				}
			}
		}
		if (undeleteTaskMap!=null) {
			Iterator<Integer> iterator = undeleteTaskMap.keySet().iterator();
			while (iterator.hasNext()) {
				Integer workItemID = iterator.next();
				Boolean undelete = undeleteTaskMap.get(workItemID);
				if (undelete!=null && undelete.booleanValue()) {
					TWorkItemBean workItemBean = existingTrackPlusWorkItems.get(workItemID);
					if (workItemBean!=null) {
						FieldsManagerRT.saveOneField(personID, workItemBean.getObjectID(), locale, false,
							SystemFields.INTEGER_ARCHIVELEVEL, TWorkItemBean.ARCHIVE_LEVEL_UNARCHIVED, false, null, new ArrayList<ErrorData>());						
					}
				}
			}
		} 
	}
	
	/**
	 * Imports the budgets after the conflict handling is done
	 * @param msProjectExchangeDataStoreBean
	 * @param existingTrackPlusWorkItems
	 * @param taskUIDToWorkItemIDMap
	 * @param overwiriteBudgetMap
	 */
	private static void importBudgets(
			MsProjectExchangeDataStoreBean msProjectExchangeDataStoreBean, 
			Double hoursPerWorkday,
			Map<Integer, Integer> taskUIDToWorkItemIDMap, 
			Map<Integer, Boolean> overwiriteBudgetMap, ImportCounts importCounts,
			Map<Integer, TCostBean> workItemIdToCostBean) {

		List<Task> msProjectTasks = msProjectExchangeDataStoreBean.getTasks();
		TPersonBean personBean = msProjectExchangeDataStoreBean.getPersonBean();
		//Integer personID = personBean.getObjectID();		
		Collection<Integer> existingWorkItems = taskUIDToWorkItemIDMap.values(); 
		//get the last budgets no matter the last modification date: some of them are conflicting, others not
		//but those conflicting should be present in overwrite map 
		/*SortedMap<Integer, TBudgetBean> workItemIDToBudgetBeanMap = BudgetBL.loadLastBudgetForWorkItemsMap(
				GeneralUtils.createIntArrFromIntegerList(GeneralUtils.createIntegerListFromCollection(existingWorkItems)), 
				null, null, null);*/
		List<TWorkItemBean> existingWorkItemBeans = ItemBL.loadByWorkItemKeys(
				GeneralUtils.createIntArrFromIntegerCollection(existingWorkItems));

		List<Integer> fieldIDs = new LinkedList<Integer>();

		fieldIDs.add(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.PLAN);
		Map<Integer, Map<Integer, Map<Integer, Integer>>> fieldRestrictions = 
				AccessBeans.getFieldRestrictions(msProjectExchangeDataStoreBean.getPersonBean().getObjectID(), existingWorkItemBeans, fieldIDs, false);
		List<TComputedValuesBean> plannedTimesList = ComputedValuesLoaderBL.loadByWorkItemKeys(existingWorkItemBeans,
				fieldRestrictions,
				TComputedValuesBean.EFFORTTYPE.TIME, TComputedValuesBean.COMPUTEDVALUETYPE.PLAN,
				null, false, FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.PLAN);

		Map<Integer, TComputedValuesBean> plannedTimesMap = new HashMap<Integer, TComputedValuesBean>();
		for (TComputedValuesBean computedValuesBean : plannedTimesList) {
			plannedTimesMap.put(computedValuesBean.getWorkitemKey(), computedValuesBean);
		}
		List<TComputedValuesBean> plannedCostList = ComputedValuesLoaderBL.loadByWorkItemKeys(existingWorkItemBeans,
				fieldRestrictions,
				TComputedValuesBean.EFFORTTYPE.COST, TComputedValuesBean.COMPUTEDVALUETYPE.PLAN,
				null, false, FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.PLAN);
		Map<Integer, TComputedValuesBean> plannedCostMap = new HashMap<Integer, TComputedValuesBean>();
		for (TComputedValuesBean computedValuesBean : plannedCostList) {
			plannedCostMap.put(computedValuesBean.getWorkitemKey(), computedValuesBean);
		}

		Iterator<Task> itrTaskElement = msProjectTasks.iterator();
		while (itrTaskElement.hasNext()) {
			Task taskElement = itrTaskElement.next();
			//			Integer UID = MsProjectExchangeDOMHelper.getSubelementInteger(taskElement, COMMON_ELEMENTS.UID);
			//			Integer UID = taskElement.getUniqueID();

			Double UIDTmp = (Double) taskElement.getFieldByAlias("trackPlusId");
			Integer UID;
			if(UIDTmp != null) {
				UID = UIDTmp.intValue();
			}else {
				UID = taskElement.getUniqueID();
			}	Integer workItemID = taskUIDToWorkItemIDMap.get(UID);

			if (workItemID!=null) {
				TComputedValuesBean plannedTimeTrackPlus = plannedTimesMap.get(workItemID);//is it a conflicting budget?
				TComputedValuesBean plannedCostTrackPlus = plannedCostMap.get(workItemID);//is it a conflicting budget?
				Boolean overwrite = overwiriteBudgetMap.get(workItemID);
				//if not conflicting (which means budget is new or created before MS Project creation), or conflicting but overwrite
				//				if (overwrite==null || overwrite.booleanValue() ) {
				//Not an Ancestor work item
				TBudgetBean budgetBeanMsProject = new TBudgetBean();

				budgetBeanMsProject.setWorkItemID(workItemID);
				//in hours
				budgetBeanMsProject.setEstimatedHours(taskElement.getWork().getDuration());

				budgetBeanMsProject.setTimeUnit(TIMEUNITS.HOURS);

				boolean insertBudget = false;
				if (taskElement.getChildTasks().isEmpty()) {
					boolean insertPlanned = false;
					boolean insertActualWork = false;
					TCostBean costBeanMSProject = new TCostBean();
					TBudgetBean planValueBeanMsProject = new TBudgetBean();
					Double hoursPerWorkingday = msProjectExchangeDataStoreBean.getProjectBean().getHoursPerWorkDay();

					//Set Actual work
					if(taskElement.getActualDuration() != null) {
						int resourceAssignments = 0;
						if(taskElement.getResourceAssignments() != null) {
							resourceAssignments = taskElement.getResourceAssignments().size();
						}

						if(resourceAssignments == 0) {
							resourceAssignments = 1;
						}
						costBeanMSProject.setWorkitem(workItemID);
						costBeanMSProject.setLastEdit(new Date());
						costBeanMSProject.setPerson(personBean.getObjectID());

						Double work = null;
						if(taskElement.getActualDuration().getUnits().getName().equals("d")) {
							work = taskElement.getActualDuration().getDuration() * hoursPerWorkday;
						}else {
							work = taskElement.getActualDuration().getDuration();
						}
						costBeanMSProject.setHours(work * resourceAssignments);
						costBeanMSProject.setTimeUnit(TimeUnit.HOURS.getValue());
						costBeanMSProject.setPerson(personBean.getObjectID());
						insertActualWork = true;
					}

					//new task estimated remaining value
					TActualEstimatedBudgetBean actualEstimatedBudgetBean = RemainingPlanBL.loadByWorkItemID(workItemID);
					if(actualEstimatedBudgetBean == null) {
						actualEstimatedBudgetBean = new TActualEstimatedBudgetBean();
					}
					Double remainingHours = null;
					if(taskElement.getRemainingDuration().getUnits().getName().equals("d")) {
						remainingHours = hoursPerWorkday * taskElement.getRemainingDuration().getDuration();
					}else {
						remainingHours = taskElement.getRemainingDuration().getDuration();
					}
					if(remainingHours != null) {
						actualEstimatedBudgetBean.setEstimatedHours(remainingHours / hoursPerWorkday);
						actualEstimatedBudgetBean.setTimeUnit(TIMEUNITS.WORKDAYS);
						actualEstimatedBudgetBean.setWorkItemID(workItemID);
					}

					if(personBean != null) {
						actualEstimatedBudgetBean.setChangedByID(personBean.getObjectID());
					}
					actualEstimatedBudgetBean.setLastEdit(new Date());
					actualEstimatedBudgetBean.setChangedByID(personBean.getObjectID());
					RemainingPlanBL.save(actualEstimatedBudgetBean);
					//Planned value
					if(taskElement.getDuration() != null) {
						int resourceAssignments = 0;
						if(taskElement.getResourceAssignments() != null) {
							resourceAssignments = taskElement.getResourceAssignments().size();
						}

						if(resourceAssignments == 0) {
							resourceAssignments = 1;
						}

						if(taskElement.getDuration().getUnits().getName().equals("d")) {
							planValueBeanMsProject.setTimeUnit(TIMEUNITS.WORKDAYS);
						}else {
							planValueBeanMsProject.setTimeUnit(TIMEUNITS.HOURS);
						}
						planValueBeanMsProject.setEstimatedHours(taskElement.getDuration().getDuration() * resourceAssignments);
						insertPlanned = true;
						//New task planned value
						budgetBeanMsProject.setBudgetType(BUDGET_TYPE.PLANNED_VALUE);
						planValueBeanMsProject.setWorkItemID(workItemID);
						planValueBeanMsProject.setLastEdit(new Date());
						planValueBeanMsProject.setChangedByID(personBean.getObjectID());

					}
					//Budget value
					if(taskElement.getDuration() != null) {
						int resourceAssignments = 0;
						if(taskElement.getResourceAssignments() != null) {
							resourceAssignments = taskElement.getResourceAssignments().size();
						}
						if(resourceAssignments == 0) {
							resourceAssignments = 1;
						}
						if(taskElement.getDuration().getUnits().getName().equals("d")) {
							budgetBeanMsProject.setTimeUnit(TIMEUNITS.WORKDAYS);
						}else {
							budgetBeanMsProject.setTimeUnit(TIMEUNITS.HOURS);
						}
						budgetBeanMsProject.setEstimatedHours(taskElement.getDuration().getDuration() * resourceAssignments);
						insertBudget = true;
						//New task planned value
						budgetBeanMsProject.setWorkItemID(workItemID);
						budgetBeanMsProject.setLastEdit(new Date());
						budgetBeanMsProject.setBudgetType(BUDGET_TYPE.BUDGET);
						budgetBeanMsProject.setChangedByID(personBean.getObjectID());
					}
					/*if (plannedTimeTrackPlus != null) {
								//a track+ budget exists (conflicting or not conflicting)
								//transform the track+ side budget to hours because the msProject duration is in hours
								Double transformedHours = AccountingBL.transformToTimeUnits(plannedTimeTrackPlus.getComputedValue(), 
										hoursPerWorkday, plannedTimeTrackPlus.getMeasurementUnit(), AccountingBL.TIMEUNITS.HOURS);
								//prepare this transformed values for hasChanged because the MSProject work seems to be in hours TODO?
								plannedTimeTrackPlus.setMeasurementUnit(AccountingBL.TIMEUNITS.HOURS);
								plannedTimeTrackPlus.setComputedValue(transformedHours);
								changed = EqualUtils.notEqual(plannedTimeTrackPlus.getComputedValue(), planValueBeanMsProject.getEstimatedHours());
								//leave the original value for fields which are not present in 
								//MS Project to not cause the hasChanged function to return true unnecessarily

								//budgetBeanMsProject.setChangeDescription(plannedTimeTrackPlus.getChangeDescription());
							} else {
								changed = planValueBeanMsProject.getEstimatedHours()!=null;
							}
							if (plannedCostTrackPlus!=null) {
								//budgetBeanMsProject.setEstimatedCost(plannedCostTrackPlus.getComputedValue());
								changed = EqualUtils.notEqual(plannedCostTrackPlus.getComputedValue(), planValueBeanMsProject.getEstimatedCost());
							} else {
								changed = planValueBeanMsProject.getEstimatedCost()!=null;
							}*/

					//if no track+ budget exists at all or is changed then save a new budget
					//new entry saved because it is historized
					if(insertPlanned) {
						BudgetBL.saveBudgetBean(planValueBeanMsProject);
					}

					if(insertActualWork && workItemIdToCostBean.get(workItemID) == null) {
						ExpenseBL.saveCostBean(costBeanMSProject);
						LOGGER.debug(workItemID + " Actual work inserted!");
					}else {
						LOGGER.debug(workItemID + " Actual work NOT inserted!");
					}
					if(insertBudget) {
						BudgetBL.saveBudgetBean(budgetBeanMsProject);
					}
					/*if(actualEstimatedBudgetBean.getWorkItemID() != null) {
							ActualEstimatedBudgetDAO actualEstimatedBudgetDAO = DAOFactory.getFactory().getActualEstimatedBudgetDAO(); 
							actualEstimatedBudgetDAO.save(actualEstimatedBudgetBean);
						}*/
					ComputedValueBL.computeBudgetOrPlan(planValueBeanMsProject);
					if(personBean != null) {
						ComputedValueBL.computeExpenses(workItemID, personBean.getObjectID());
					}
					/*if (!(plannedTimeTrackPlus==null &&
								(planValueBeanMsProject.getEstimatedHours()==null || 
								planValueBeanMsProject.getEstimatedHours().doubleValue()==0.0))) {
							//do not count if form null is set to 0.0
							if (LOGGER.isDebugEnabled()) {
								Double fromValue = null;
								if (plannedTimeTrackPlus!=null) {
									fromValue = plannedTimeTrackPlus.getComputedValue();
								}
								LOGGER.debug("Budget for workItem " + workItemID + " changed from " + fromValue + " to " + planValueBeanMsProject.getEstimatedHours());
							}
							importCounts.setNoOfPlannedWorks(importCounts.getNoOfPlannedWorks()+1);
						}*/
				}else {
					//Budget value
					if(taskElement.getDuration() != null) {
						int resourceAssignments = 0;
						if(taskElement.getResourceAssignments() != null) {
							resourceAssignments = taskElement.getResourceAssignments().size();
						}
						if(resourceAssignments == 0) {
							resourceAssignments = 1;
						}
						if(taskElement.getDuration().getUnits().getName().equals("d")) {
							budgetBeanMsProject.setTimeUnit(TIMEUNITS.WORKDAYS);
						}else {
							budgetBeanMsProject.setTimeUnit(TIMEUNITS.HOURS);
						}
						budgetBeanMsProject.setEstimatedHours(taskElement.getDuration().getDuration() * resourceAssignments);
						insertBudget = true;
						//New task planned value
						budgetBeanMsProject.setWorkItemID(workItemID);
						budgetBeanMsProject.setLastEdit(new Date());
						budgetBeanMsProject.setBudgetType(BUDGET_TYPE.BUDGET);
					}
					if(insertBudget) {
						BudgetBL.saveBudgetBean(budgetBeanMsProject);
					}
				}
			}
			//TODO only for the last leaf
			//Actualize PARENT work items
			if(taskElement.getParentTask() != null) {
				try {
					TWorkItemBean workItemBean = ItemBL.loadWorkItem(workItemID);
					Integer UIDParent = workItemBean.getSuperiorworkitem();
					if (UIDParent!=null && ApplicationBean.getInstance().getSiteBean().getSummaryItemsBehavior()) {
						AccountingBL.actualizeAncestorValues(workItemBean, UIDParent, UIDParent, personBean.getObjectID());
					}
				}catch(Exception ex) {
					LOGGER.error("Error with actualize: " + ex.getMessage());
				}
			}
			//			}
		}
	}
	
	/**
	 * Imports the budgets after the conflict handling is done
	 * @param msProjectExchangeDataStoreBean
	 * @param existingTrackPlusWorkItems
	 * @param taskUIDToWorkItemIDMap
	 * @param overwiriteBudgetMap
	 */
	private static void importRemainingBudgets(
			MsProjectExchangeDataStoreBean msProjectExchangeDataStoreBean, 
			Map<Integer, WorkItemContext> workItemContextsMap,
			Map<Integer, Integer> taskUIDToWorkItemIDMap, 
			Map<Integer, Boolean> overwriteBudgetMap) {
		
		
		List<Task> msProjectTasks = msProjectExchangeDataStoreBean.getTasks();
		TPersonBean personBean = msProjectExchangeDataStoreBean.getPersonBean();
		Collection<Integer> existingWorkItems = taskUIDToWorkItemIDMap.values();
		int[] workItemIDs = GeneralUtils.createIntArrFromIntegerList(GeneralUtils.createIntegerListFromCollection(existingWorkItems));
		Map<Integer, TActualEstimatedBudgetBean> workItemIDToRemainingBudgetBeanMap = RemainingPlanBL.loadRemainingBudgetMapByItemIDs(workItemIDs);		
		Iterator<Task> itrTaskElement = msProjectTasks.iterator();
		while (itrTaskElement.hasNext()) {
			Task taskElement = itrTaskElement.next();
			Double UIDTmp = (Double) taskElement.getFieldByAlias("trackPlusId");
			Integer UID;
			if(UIDTmp != null) {
				UID = UIDTmp.intValue();
			}else {
				UID = taskElement.getUniqueID();
			}
			Integer workItemID = taskUIDToWorkItemIDMap.get(UID);
			if (workItemID!=null) {	
				WorkItemContext workItemContext = workItemContextsMap.get(UID);
				Boolean overwrite = overwriteBudgetMap.get(workItemID);
				//if not conflicting (which means budget is new or created before MS Project creation), or conflicting but overwrite
				if (overwrite==null || overwrite.booleanValue()) {
					TActualEstimatedBudgetBean remainingBudgetBeanMsProject = new TActualEstimatedBudgetBean();
					remainingBudgetBeanMsProject.setWorkItemID(workItemID);
					remainingBudgetBeanMsProject.setEstimatedHours(taskElement.getRemainingDuration().convertUnits(TimeUnit.HOURS, msProjectExchangeDataStoreBean.getProject().getProjectHeader()).getDuration());
					
					if( taskElement.getRemainingWork() != null) {
						remainingBudgetBeanMsProject.setEstimatedHours(taskElement.getRemainingWork().getDuration());
					}
					remainingBudgetBeanMsProject.setTimeUnit(TIMEUNITS.HOURS);
					TActualEstimatedBudgetBean remainingBudgetBeanTrackPlus = workItemIDToRemainingBudgetBeanMap.get(workItemID);//is it a conflicting budget?					
					if (remainingBudgetBeanTrackPlus==null) {
						remainingBudgetBeanTrackPlus = new TActualEstimatedBudgetBean();
					}
					remainingBudgetBeanMsProject.setEstimatedCost(remainingBudgetBeanTrackPlus.getEstimatedCost());
					RemainingPlanBL.saveRemainingPlanAndNotify(remainingBudgetBeanMsProject,
							remainingBudgetBeanTrackPlus, personBean, workItemContext.getWorkItemBean(), false);
				}
			}
		}
	}
	
	/**
	 * Import (add/update/delete) the task dependency links
	 * Update or delete a link only if his lastEdit is before the MSProject import file creation
	 * @param msProjectExchangeDataStoreBean
	 * @param UIDToWorkItemID
	 * @param importCounts
	 */
	private static void importTaskDependencyLinks(
			MsProjectExchangeDataStoreBean msProjectExchangeDataStoreBean, 
			Map<Integer, Integer> UIDToWorkItemID, ImportCounts importCounts) {

		Date lastSavedDate = msProjectExchangeDataStoreBean.getLastSavedDate();
		Map<Integer, Map<Integer, Relation>> dependentToPredecessorLinksMap =
				MsProjectExchangeDOMHelper.getDependentPredecessorLinksMap(msProjectExchangeDataStoreBean.getTasks());
		
		MsProjectLinkType msProjectLinkType = MsProjectLinkType.getInstance();
		List<Integer> msProjectLinkTypeIDs = LinkTypeBL.getLinkTypesByPluginClass(msProjectLinkType);
		if (msProjectLinkTypeIDs==null || msProjectLinkTypeIDs.isEmpty()) {
			return;
		}
		Integer trackPlusLinkType = msProjectLinkTypeIDs.get(0);
		//get all msProject specific links from track+
		List<TWorkItemLinkBean> existingTrackPlusWorkItemLinks = ItemLinkBL.loadUnidirectionalLinksByWorkItemsAndLinkTypes(
				GeneralUtils.createIntegerListFromCollection(UIDToWorkItemID.values()), 
				msProjectLinkTypeIDs, msProjectLinkType.getPossibleDirection());
		Map<Integer, Map<Integer, TWorkItemLinkBean>> trackPlusPredToSuccToWorkItemLinksMap = new HashMap<Integer, Map<Integer, TWorkItemLinkBean>>();		
		Iterator<TWorkItemLinkBean> itrWorkItemLinks = existingTrackPlusWorkItemLinks.iterator();
		while (itrWorkItemLinks.hasNext()) {
			TWorkItemLinkBean workItemLinkBean = itrWorkItemLinks.next();
			Integer predLink = workItemLinkBean.getLinkPred();
			Integer succLink = workItemLinkBean.getLinkSucc();
			Map<Integer, TWorkItemLinkBean> succLinksMap = trackPlusPredToSuccToWorkItemLinksMap.get(predLink);
			if (succLinksMap==null) {
				succLinksMap = new HashMap<Integer, TWorkItemLinkBean>();
				trackPlusPredToSuccToWorkItemLinksMap.put(predLink, succLinksMap);
			}
			succLinksMap.put(succLink, workItemLinkBean);
		}
		
		//go through msProject imported links
		Iterator<Integer> itrDependentUIDs = dependentToPredecessorLinksMap.keySet().iterator();
		while (itrDependentUIDs.hasNext()) {
			Integer dependentUID = itrDependentUIDs.next();
			Map<Integer, Relation> predecessorLinks = dependentToPredecessorLinksMap.get(dependentUID);
			if (predecessorLinks!=null) {
				Iterator<Integer> itrPredecessorLinks = predecessorLinks.keySet().iterator();
				while (itrPredecessorLinks.hasNext()) {
					Integer predecessorUID = itrPredecessorLinks.next();
					Relation predecessorLinkElement = predecessorLinks.get(predecessorUID);
					
					/*Integer type = MsProjectExchangeDOMHelper.getSubelementInteger(predecessorLinkElement, COMMON_ELEMENTS.Type);
					Integer crossProject = MsProjectExchangeDOMHelper.getSubelementInteger(predecessorLinkElement, PREDECESSOR_ELEMENTS.CrossProject);
					Double linkLag = MsProjectExchangeDOMHelper.getSubelementDouble(predecessorLinkElement, PREDECESSOR_ELEMENTS.LinkLag);
					Integer lagFormat = MsProjectExchangeDOMHelper.getSubelementInteger(predecessorLinkElement, PREDECESSOR_ELEMENTS.LagFormat);*/
					
					Integer type = predecessorLinkElement.getType().getValue();
					Integer crossProject = 0; //TODO set real value 
					Double linkLag = predecessorLinkElement.getLag().getDuration();
					Integer lagFormatMsProject = predecessorLinkElement.getLag().getUnits().getValue();
					Integer lagFormatTrack = convertMsProjectLagToTrackPlusLagFormat(lagFormatMsProject);
					
					Double hoursPerWorkDay = ProjectBL.getHoursPerWorkingDay(msProjectExchangeDataStoreBean.getProjectBean().getObjectID());
					Integer convertedLinkLag =  LinkLagBL.getMinutesFromUILinkLag(linkLag, lagFormatTrack, hoursPerWorkDay);
					
					//In case of lag format is year we should convert into days.
					if(lagFormatTrack == -1) {
						if(checkLeapYearForConvert(linkLag.intValue())) {
							convertedLinkLag = convertedLinkLag * 366;
							lagFormatTrack = 7;
						}else {
							convertedLinkLag = convertedLinkLag * 240;
							lagFormatTrack = 7;
						}
					}else if(lagFormatTrack == -2) {
						//default value, if imported link lag does not exists in Genji
						lagFormatTrack = 7;
					}
					//dependent in MS Project sense: predLink in track+ sense
					//Integer predLink = UIDToWorkItemID.get(dependentUID);
					Integer predLink = UIDToWorkItemID.get(predecessorUID);
					//predecessor in MS Project sense: succLink in track+ sense 
					//Integer succLink = UIDToWorkItemID.get(predecessorUID);
					Integer succLink = UIDToWorkItemID.get(dependentUID);
					//the ID of the corresponding trackPlus link type
					if (predLink!=null && succLink!=null && trackPlusLinkType!=null) {
						//handle the link only if both dependentUID, predecessorUID and type 
						//can be mapped to predLink, succLink and trackPlusLinkType 
						//get the existing links between the workItems
						TWorkItemLinkBean workItemLinkBean = null;
						Map<Integer, TWorkItemLinkBean> succLinksMap = trackPlusPredToSuccToWorkItemLinksMap.get(predLink);
						if (succLinksMap!=null) {
							workItemLinkBean = succLinksMap.get(succLink);
						}						
						TWorkItemLinkBean copyOfOldBean = null;
						if (workItemLinkBean==null) {
							//new link in msProject file
							workItemLinkBean = new TWorkItemLinkBean();
						} else {
							//exists in both track+ and msProject import file
							//remove from this map because it is found 
							succLinksMap.remove(succLink);
							if (succLinksMap.isEmpty()) {
								trackPlusPredToSuccToWorkItemLinksMap.remove(predLink);
							}
							copyOfOldBean = workItemLinkBean.copyToNew();
						}
						boolean crossProjectBool = Integer.valueOf(PREDECESSOR_CROSS_PROJECT.CROSS_PROJECT).equals(crossProject);
						if (crossProjectBool) {
							workItemLinkBean.setLinkIsCrossProject(BooleanFields.TRUE_VALUE);
						} else {
							workItemLinkBean.setLinkIsCrossProject(BooleanFields.FALSE_VALUE);
						}	
						//because in track+ the current workItem is linkPred 
						//and the linked workItem is the linkSucc
						workItemLinkBean.setLinkPred(predLink);
						workItemLinkBean.setLinkSucc(succLink);
						workItemLinkBean.setLinkType(trackPlusLinkType);
						workItemLinkBean.setLinkLag(convertedLinkLag.doubleValue());
						workItemLinkBean.setLinkLagFormat(lagFormatTrack);
						workItemLinkBean.setIntegerValue1(type);
						workItemLinkBean.setLinkDirection(new MsProjectLinkType().getPossibleDirection());
						if (copyOfOldBean==null || 
								(workItemLinkBean.hasChanged(copyOfOldBean) && lastSavedDate.after(workItemLinkBean.getLastEdit()))) {
							//save only if link is new or the last change was before the last MS Project export 
							if (copyOfOldBean==null) {
								importCounts.setNoOfCreatedLinks(importCounts.getNoOfCreatedLinks()+1);
							} else {
								importCounts.setNoOfUpdatedLinks(importCounts.getNoOfUpdatedLinks()+1);
							}
							ItemLinkBL.saveLink(workItemLinkBean);
						}
					}
				}
			}
		}
		//all links which remained in this map are present only in track+ and not in msProject file: they will be removed
		if (!trackPlusPredToSuccToWorkItemLinksMap.isEmpty()) {
			Iterator<Integer> itrPredLinks = trackPlusPredToSuccToWorkItemLinksMap.keySet().iterator();
			while (itrPredLinks.hasNext()) {
				Integer predLink = itrPredLinks.next();
				Map<Integer, TWorkItemLinkBean> succMap = trackPlusPredToSuccToWorkItemLinksMap.get(predLink);
				if (succMap!=null) {
					Iterator<TWorkItemLinkBean> itrSuccLinks = succMap.values().iterator();
					while (itrSuccLinks.hasNext()) {
						TWorkItemLinkBean workItemLinkBean = itrSuccLinks.next();
						Date lastEdit = workItemLinkBean.getLastEdit();
						if (lastSavedDate!=null && lastEdit!=null && lastSavedDate.compareTo(lastEdit) >= 0) {
							//delete if it was added/modified before the last modification of the MsPproject 
							ItemLinkBL.deleteLink(workItemLinkBean.getObjectID());
							importCounts.setNoOfDeletedLinks(importCounts.getNoOfDeletedLinks()+1);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Get the present fields taken into account by saving the tasks as workItems
	 * based on mandatory fields and the presence of some optional fields on the edit map for tasks
	 * @param projectBean
	 * @param issueTypeID
	 * @return
	 */
	private static Set<Integer> getPresentFields(Integer projectID, Integer issueTypeID) {
		Set<Integer> presentFields = new HashSet<Integer>();
		Integer	screenID=ScreenRuntimeBL.getInstance().findScreenID(SystemActions.EDIT, projectID, issueTypeID);
		if (screenID!=null) {
			//get the present fields from the screen
			List<TFieldBean> presentFieldsBeans=FieldBL.loadAllFields(screenID);
			Iterator<TFieldBean> iterator = presentFieldsBeans.iterator();
			while (iterator.hasNext()) {
				//add some optional system fields only if they are present in the screen
				//because can be that they are configured as required by field config 
				//but still not present in the screen which means normally no validation error occurs
				//but if we include them forced as present fields then validation error occurs 
				TFieldBean fieldBean = iterator.next();
				Integer fieldID = fieldBean.getObjectID();
				if (fieldID.intValue()<50) {
					presentFields.add(fieldID);
				}
			}
		}
		//mandatory fields
		presentFields.add(SystemFields.PROJECT);
		presentFields.add(SystemFields.ISSUETYPE);
		presentFields.add(SystemFields.STATE);
		presentFields.add(SystemFields.MANAGER);
		presentFields.add(SystemFields.RESPONSIBLE);
		presentFields.add(SystemFields.PRIORITY);
		presentFields.add(SystemFields.ORIGINATOR);
		presentFields.add(SystemFields.CHANGEDBY);
		presentFields.add(SystemFields.SYNOPSIS);
		presentFields.add(SystemFields.STARTDATE);
		presentFields.add(SystemFields.ENDDATE);
		presentFields.add(SystemFields.DESCRIPTION);
		return presentFields;
	}

	/**
	 * This method recursively generates all task elements
	 * @param msProjectExchangeDataStoreBean
	 * @param msProjectTaskElement
	 * @param UID
	 * @param parentOutlineNumber
	 * @param parentID
	 * @param workItemContextsMap
	 * @param taskUIDToWorkItemIDMap
	 * @param errorsMap
	 * @param importCounts	
	 */
	private static void saveTasks(
			MsProjectExchangeDataStoreBean msProjectExchangeDataStoreBean,
			Task msProjectTaskElement, Integer UID, String parentOutlineNumber, 
			Integer parentID,Map<Integer, WorkItemContext> workItemContextsMap,
			//Map<Integer, TWorkItemBean> existingTrackPlusWorkItems,
			Map<Integer, TMSProjectTaskBean> existingTrackPlusTasks,
			Map<Integer, Integer> taskUIDToWorkItemIDMap, 
			Map<Integer, Boolean> overwriteStartDateMap,
			Map<Integer, Boolean> overwriteEndDateMap,
			SortedMap<Integer, List<ErrorData>> errorsMap, 
			ImportCounts importCounts) {
		
		List<Task> msProjectTasks = msProjectExchangeDataStoreBean.getTasks();
		//TPersonBean personBean = msProjectImporterBean.getPersonBean();
		Integer entityID = msProjectExchangeDataStoreBean.getEntityID();
		int entityType = msProjectExchangeDataStoreBean.getEntityType();
		//save the workItemBean 
		WorkItemContext workItemContext = workItemContextsMap.get(UID);
		TWorkItemBean workItemBean = workItemContext.getWorkItemBean();
		//reset the start and end date to original values if not overwrite
		TWorkItemBean workItemBeanOriginal = workItemContext.getWorkItemBeanOriginal();
		if (workItemBeanOriginal!=null) {
			Boolean overwriteStartDate = overwriteStartDateMap.get(workItemBean.getObjectID());
			if (overwriteStartDate!=null && !overwriteStartDate.booleanValue()) {
				workItemBean.setStartDate(workItemBeanOriginal.getStartDate());
			}
			Boolean overwriteEndDate = overwriteEndDateMap.get(workItemBean.getObjectID());
			if (overwriteEndDate!=null && !overwriteEndDate.booleanValue()) {
				workItemBean.setEndDate(workItemBeanOriginal.getEndDate());
			}
		} 
		if (parentID!=null) {
			//If in MSProject a Task has a parent then it will be forced also in Genji
			//Otherwise, if the Task has in Genji a "non"-Task or deleted or closed parent
			//this may be retained (not set to null) 
			workItemBean.setSuperiorworkitem(parentID);
		}
		Integer workItemID = workItemBean.getObjectID();
		/*if (workItemID!=null) {
			existingTrackPlusWorkItems.remove(workItemID);
		}*/
		boolean isNew = (workItemID == null);
		List<ErrorData> itemErrorList = new ArrayList<ErrorData>();
		boolean saveNeeded = FieldsManagerRT.performSave(workItemContext, itemErrorList, false, false/*, false*/);
		workItemID = workItemBean.getObjectID();
		if (workItemID==null) {
			//not succeeded to save the workItem
			return;
		}
		//errors detected after save
		if (!itemErrorList.isEmpty()) {
			errorsMap.put(UID, itemErrorList);
		} else {
			if (saveNeeded) {
				if (isNew) {
					importCounts.setNoOfCreatedIssues(importCounts.getNoOfCreatedIssues()+1);
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("New issue created for UID=" + UID + " new workitemID=" + workItemID);
					}
				} else {
					importCounts.setNoOfUpdatedIssues(importCounts.getNoOfUpdatedIssues()+1);
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Issue modified UID=" + UID + " workitemID=" + workItemID);
					}
				}
			}
		}
				
		TMSProjectTaskBean msProjectTaskBean = saveMSProjectTask(msProjectTaskElement, 
				workItemID, entityID, entityType, existingTrackPlusTasks.get(UID), msProjectExchangeDataStoreBean.getProject());
		
		taskUIDToWorkItemIDMap.put(msProjectTaskBean.getUniqueID(), workItemID);
		//saveBudget(msProjectTaskElement, workItemBean, personBean);
		//saveRemainingBuget(msProjectTaskElement, workItemBean, personBean);
		//saveFirstActualWork(taskBasedAssignments.get(UID), workItemBean, personBean, msProjectImporterBean.getDefaultAccount(), resourceUIDsToPersonIDs);
		
		String childPatternExpr = new String("^"+parentOutlineNumber+"\\.[0-9]+$");
		Pattern childPattern = Pattern.compile(childPatternExpr);
		Matcher childMatcher = null;
		for(Iterator<Task> iterator = msProjectTasks.iterator(); iterator.hasNext();){
			Task taskElement = iterator.next();
			String outlineNumber = taskElement.getOutlineNumber();
			
			if (outlineNumber!=null) {
				childMatcher = childPattern.matcher(outlineNumber);
				if (childMatcher.find()) {
					Double UIDTmp = (Double) taskElement.getFieldByAlias("trackPlusId");
					Integer UIDParam;
					if(UIDTmp != null) {
						UIDParam = UIDTmp.intValue();
					}else {
						UIDParam = taskElement.getUniqueID();
					}
					saveTasks(msProjectExchangeDataStoreBean, taskElement,
							UIDParam, outlineNumber, 
							workItemID, workItemContextsMap, /*existingTrackPlusWorkItems,*/ existingTrackPlusTasks,
							taskUIDToWorkItemIDMap, overwriteStartDateMap, overwriteEndDateMap, errorsMap, importCounts);
				}
			}	 
		}
	}

	/**
	 * Prepares the new or existing workItems for save
	 * @param msProjectExchangeDataStoreBean
	 * @param trackPlusTasksMap
	 * @param existingWorkItemsMap
	 * @param fieldConfigsMap
	 * @return
	 */
	private static Map<Integer, TWorkItemBean> prepareWorkItemBeans(
			MsProjectExchangeDataStoreBean msProjectExchangeDataStoreBean,
			Map<Integer, TMSProjectTaskBean> trackPlusTasksMap, 
			Map<Integer, TWorkItemBean> existingWorkItemsMap,
			Map<Integer, TFieldConfigBean> fieldConfigsMap) {
		
		List<Task> msProjectTasks = msProjectExchangeDataStoreBean.getTasks();
		TProjectBean projectBean = msProjectExchangeDataStoreBean.getProjectBean();
		Integer personID = msProjectExchangeDataStoreBean.getPersonBean().getObjectID();
		Integer projectID = msProjectExchangeDataStoreBean.getProjectID();
		Integer releaseScheduledID = msProjectExchangeDataStoreBean.getReleaseScheduledID();
		Integer issueTypeID = msProjectExchangeDataStoreBean.getIssueType();
		Map<Integer, Integer> resourceUIDsToPersonIDs = msProjectExchangeDataStoreBean.getResourceUIDToPersonIDMap();
		List<Resource> workResourcesList = msProjectExchangeDataStoreBean.getWorkResources();
		Map<Integer, Resource> workResourcesMap = MsProjectExchangeDOMHelper.createIntegerElementMapFromList(workResourcesList, COMMON_ELEMENTS.UID);
		Map<Integer, TWorkItemBean> workItemBeanMap = new HashMap<Integer, TWorkItemBean>();
		
		Integer statusID = projectBean.getDefaultInitStateID();
		//TODO Contact as manager pro workItem?
		Integer managerID = projectBean.getDefaultManagerID();
		Integer responsibleID = null;		
		if (responsibleID==null) {
			responsibleID = ProjectConfigBL.getDefaultFieldValueForProject(SystemFields.INTEGER_RESPONSIBLE, projectBean, personID, issueTypeID, null);
		}		
		/*Integer theClass = ProjectConfigBL.getDefaultFieldValueForProject(SystemFields.INTEGER_CLASS,
				projectBean, personID, issueTypeID, fieldConfigsMap.get(SystemFields.INTEGER_CLASS));*/
		Integer releaseNoticed = ProjectConfigBL.getDefaultFieldValueForProject(SystemFields.INTEGER_RELEASENOTICED,
				projectBean, personID, issueTypeID, fieldConfigsMap.get(SystemFields.INTEGER_RELEASENOTICED));		
		if (releaseScheduledID==null) {
			//null when import entire project instead of release
			releaseScheduledID = ProjectConfigBL.getDefaultFieldValueForProject(SystemFields.INTEGER_RELEASESCHEDULED,
					projectBean, personID, issueTypeID, fieldConfigsMap.get(SystemFields.INTEGER_RELEASESCHEDULED));
			if (releaseScheduledID==null) {
				List<TReleaseBean> releases = ReleaseBL.loadActiveByProject(projectID);
				if (releases!=null && !releases.isEmpty()) {
					TReleaseBean releaseBean = releases.get(0);
					releaseScheduledID = releaseBean.getObjectID();
				}
			}
		} 
		Integer priorityID = ProjectConfigBL.getDefaultFieldValueForProject(SystemFields.INTEGER_PRIORITY,
				projectBean, personID, issueTypeID, null);
		Integer severityID = ProjectConfigBL.getDefaultFieldValueForProject(SystemFields.INTEGER_SEVERITY,
				projectBean, personID, issueTypeID, fieldConfigsMap.get(SystemFields.INTEGER_SEVERITY));
		
		Map<Integer, List<ResourceAssignment>> taskUIDBasedAssignments = MsProjectExchangeDOMHelper.createIntegerElementListMapFromList(
				msProjectExchangeDataStoreBean.getAssignments(), ASSIGNMENT_ELEMENTS.TASKUID);
		for(Iterator<Task> iterator = msProjectTasks.iterator(); iterator.hasNext();){
			Task taskElement = iterator.next();
			if (MsProjectExchangeDOMHelper.UIDIsValidMpxjApiTemp(taskElement)) {
				Double UIDTmp = (Double) taskElement.getFieldByAlias("trackPlusId");
				Integer UID;
				if(UIDTmp != null) {
					UID = UIDTmp.intValue();
				}else {
					UID = taskElement.getUniqueID();
				}
				
				TMSProjectTaskBean msProjectTaskBean = trackPlusTasksMap.get(UID);
				Integer workItemID = null;
				TWorkItemBean workItemBean = null;
				if (msProjectTaskBean!=null) {
					workItemID = msProjectTaskBean.getWorkitem();
					//is null when in trackPlus not yet exists
					workItemBean = existingWorkItemsMap.get(workItemID);
				}
				
				boolean isNew = false;
				if (workItemBean==null) {
					isNew = true;
					workItemBean = new TWorkItemBean();
				}
				workItemBean.setProjectID(projectID);
				if (isNew) {
					workItemBean.setListTypeID(issueTypeID);
					//workItemBean.setProjectCategoryID(subprojectID);
					workItemBean.setStateID(statusID);
				}
				//TODO Contact as manager?
				if (isNew) {
					Integer workItemSpecificManager = null;
					if (workItemSpecificManager==null) {
						workItemSpecificManager = managerID;
					}
					workItemBean.setOwnerID(workItemSpecificManager);
				}
				if (isNew) {
					Integer workItemSpecificResponsibleID = getResponsibleFromAssignments(
							taskUIDBasedAssignments.get(UID), workResourcesMap, resourceUIDsToPersonIDs);
					
					if (workItemSpecificResponsibleID==null) {
						workItemSpecificResponsibleID = responsibleID;
					}
					workItemBean.setResponsibleID(workItemSpecificResponsibleID);
				}
				if (isNew) {
					workItemBean.setReleaseNoticedID(releaseNoticed);
					workItemBean.setReleaseScheduledID(releaseScheduledID);
					workItemBean.setPriorityID(priorityID);
					workItemBean.setSeverityID(severityID);
					workItemBean.setOriginatorID(personID);
				}
				workItemBean.setChangedByID(personID);
				workItemBean.setSynopsis(taskElement.getName());
				if (isNew) {
					//if null in FieldManagerRT it will be set anyway
					workItemBean.setCreated(workItemBean.getCreated());
				}
				String notes = taskElement.getNotes();
				if (notes!=null && notes.length()>0) {
					workItemBean.setDescription(notes);
				}
				
				workItemBean.setStartDate(taskElement.getStart());
				workItemBean.setEndDate(taskElement.getFinish());
				
				workItemBean.setTopDownStartDate(taskElement.getActualStart());
				workItemBean.setTopDownEndDate(taskElement.getActualFinish());
				
				if(taskElement.getStart() == null && taskElement.getActualStart() != null) {
					workItemBean.setStartDate(taskElement.getActualStart());
				}
				if(taskElement.getFinish() == null && taskElement.getActualFinish() != null) {
					workItemBean.setEndDate(taskElement.getActualFinish());
				}
				if(taskElement.getMilestone()) {
					workItemBean.setMilestone(true);
					workItemBean.setEndDate(null);
					workItemBean.setTopDownEndDate(null);
				}
				workItemBeanMap.put(UID, workItemBean);
			}
		}
		return workItemBeanMap;
	}
	
	/**
	 * Set as responsible the person with the greatest work assigned on a task 
	 * @param taskBasedAssignmentList
	 * @param workResourcesMap
	 * @param resourceUIDsToPersonIDs
	 * @return
	 */
	private static Integer getResponsibleFromAssignments(List<ResourceAssignment> taskBasedAssignmentList,
			Map<Integer, Resource> workResourcesMap, Map<Integer, Integer> resourceUIDsToPersonIDs) {
		if (taskBasedAssignmentList!=null && 
				workResourcesMap!=null && resourceUIDsToPersonIDs!=null) {
			Integer resouceID = null;
			Integer resourceIDWithMaximalWork = null;
			Double maximalWork = null;
			for(int i = 0; i < taskBasedAssignmentList.size(); i++) {
				ResourceAssignment assignmentElement = taskBasedAssignmentList.get(i);
				resouceID = assignmentElement.getResourceUniqueID();
				if (resourceIDWithMaximalWork==null) {
					resourceIDWithMaximalWork = resouceID;
				}
				Resource resourceElement = workResourcesMap.get(resouceID);
				//resource is work resource (not material) 
				if (assignmentElement!=null ) {
					if(assignmentElement.getWork() != null) {
						Double work = assignmentElement.getWork().getDuration();
						if (work!=null) {
							if (maximalWork==null) {
								 maximalWork = work;
								 resourceIDWithMaximalWork = resouceID;
							} else {
								if (work>maximalWork) {
									maximalWork=work;
									resourceIDWithMaximalWork = resouceID;
								}
							}
						}
					}
				}
			}
			if (resourceIDWithMaximalWork!=null) {
				return resourceUIDsToPersonIDs.get(resourceIDWithMaximalWork);
			}
		}
		return null;
	}
	
	/**
	 * Saves the msProjectTaskBean
	 * @param msProjectTaskElement
	 * @param workItemBean
	 * @param entityID
	 * @param entityType
	 * @return
	 */
	private static TMSProjectTaskBean saveMSProjectTask(Task msProjectTaskElement, 
			Integer workItemID, Integer entityID, int entityType, TMSProjectTaskBean msProjectTaskBean,
			ProjectFile project) {
		Double UIDTmp = (Double)msProjectTaskElement.getFieldByAlias("trackPlusId");
		Integer UID;
		if(UIDTmp != null) {
			UID = UIDTmp.intValue();
		}else {
			UID = msProjectTaskElement.getUniqueID();
		}
		
		//TMSProjectTaskBean msProjectTaskBean = msProjectTaskDAO.loadByUIDAndRelease(UID, entityID, entityType);
		if (msProjectTaskBean==null) {
			msProjectTaskBean = new TMSProjectTaskBean();
		}
		
		msProjectTaskBean.setWorkitem(workItemID);
		msProjectTaskBean.setUniqueID(UID);		
		
		msProjectTaskBean.setTaskType(msProjectTaskElement.getType().getValue());	
		msProjectTaskBean.setContact(msProjectTaskElement.getContact());
		msProjectTaskBean.setWBS(msProjectTaskElement.getWBS());
		msProjectTaskBean.setOutlineNumber(msProjectTaskElement.getOutlineNumber());
		msProjectTaskBean.setDuration(msProjectTaskElement.getDuration().convertUnits(TimeUnit.HOURS, project.getProjectHeader()).toString());
		msProjectTaskBean.setDurationFormat(MsProjectExchangeDataStoreBean.LAG_FORMAT.d);
		msProjectTaskBean.setEstimated(BooleanFields.fromBooleanToString(msProjectTaskElement.getEstimated()));//!
		msProjectTaskBean.setMilestone(BooleanFields.fromBooleanToString(msProjectTaskElement.getMilestone()));
		msProjectTaskBean.setSummary(BooleanFields.fromBooleanToString(msProjectTaskElement.getSummary()));
		msProjectTaskBean.setActualDuration(msProjectTaskElement.getActualDuration().convertUnits(TimeUnit.HOURS, project.getProjectHeader()).toString());
		msProjectTaskBean.setRemainingDuration(msProjectTaskElement.getRemainingDuration().convertUnits(TimeUnit.HOURS, project.getProjectHeader()).toString());
		msProjectTaskBean.setConstraintType(msProjectTaskElement.getConstraintType().getValue());
		msProjectTaskBean.setConstraintDate(msProjectTaskElement.getConstraintDate());
		msProjectTaskBean.setDeadline(msProjectTaskElement.getDeadline());
		msProjectTaskDAO.save(msProjectTaskBean);
		return msProjectTaskBean;
	}
	
	/**
	 * Gets the resource name to UID map
	 * @param workResources
	 * @return
	 */
	public static SortedMap<String, Integer> getResourceNameToResourceUIDMap(List<Resource> workResources) {
		SortedMap<String, Integer> resourceNameToUIDMap=new TreeMap<String, Integer>();
		if (workResources!=null) {
			for(int i = 0; i < workResources.size(); i++) {
				Resource resource = workResources.get(i);
				if (MsProjectExchangeDOMHelper.UIDIsValidMpxjApiTempResource(resource)) {
					String name = resource.getName();
					Integer UID = resource.getUniqueID();
					if(name != null && UID != null) {
						resourceNameToUIDMap.put(name, UID);
					}
				}
			}
		}
		return resourceNameToUIDMap;
	}
	
	/**
	 * Gets the resource UID to name map
	 * @param workResources
	 * @return
	 */
	public static Map<Integer, String> getResourceUIDToResourceNameMap(List<Resource> workResources) {
		Map<Integer, String> resourceUIDToNameMap=new TreeMap<Integer, String>();
		if (workResources!=null) {
			for(int i = 0; i < workResources.size(); i++) {
				Resource resource = workResources.get(i);
				if (MsProjectExchangeDOMHelper.UIDIsValidMpxjApiTempResource(resource)) {
					String name = resource.getName();
					Integer UID = resource.getUniqueID();
					resourceUIDToNameMap.put(UID, name);
				}
			}
		}
		return resourceUIDToNameMap;
	}
	
	/**
	 * Gets the user creation error list
	 * @param errorMap
	 * @param resourceUIDToNameMap
	 * @param locale
	 * @return
	 */
	public static List<ErrorData> handleResourceErrorMap(SortedMap<Integer, List<ErrorData>> errorMap, 
			Map<Integer, String> resourceUIDToNameMap, Locale locale) {
		List<ErrorData> errors = new LinkedList<ErrorData>();
		Iterator<Integer> iterator = errorMap.keySet().iterator();
		while (iterator.hasNext()) {
			Integer resourceUID = iterator.next();
			List<ErrorData> errorList = errorMap.get(resourceUID);
			String resourceName = resourceUIDToNameMap.get(resourceUID);
			List<ErrorParameter> resourceParameters = new ArrayList<ErrorParameter>();
			resourceParameters.add(new ErrorParameter(resourceName));
			resourceParameters.add(new ErrorParameter(resourceUID));
			StringBuffer stringBuffer = new StringBuffer();
			Iterator<ErrorData> itrError = errorList.iterator();
			while (itrError.hasNext()) {
				ErrorData errorData = itrError.next();
				stringBuffer.append(ErrorHandlerJSONAdapter.createMessage(errorData, locale));
				stringBuffer.append(" ");
			}
			resourceParameters.add(new ErrorParameter(stringBuffer));
			ErrorData modifiedErrorData = new ErrorData("admin.actions.importMSProject.err.resourceError", resourceParameters);
			errors.add(modifiedErrorData);
		}
		return errors;
	}
	
	/**
	 * Gets the validation error list
	 * @param errorMap
	 * @param workItemContextsMap
	 * @param locale
	 * @return
	 */
	public static List<ErrorData> handleTaskValidationErrorMap(SortedMap<Integer, List<ErrorData>> errorMap, 
			Map<Integer, WorkItemContext> workItemContextsMap, Locale locale) {
		List<ErrorData> errors = new LinkedList<ErrorData>();
		Iterator<Integer> iterator = errorMap.keySet().iterator();
		while (iterator.hasNext()) {
			Integer taskUID = iterator.next();
			List<ErrorData> errorList = errorMap.get(taskUID);
			WorkItemContext workItemContext = workItemContextsMap.get(taskUID);
			Map<Integer, TFieldConfigBean> fieldConfigs = workItemContext.getFieldConfigs();
			List<ErrorParameter> resourceParameters = new ArrayList<ErrorParameter>();
			resourceParameters.add(new ErrorParameter(workItemContext.getWorkItemBean().getSynopsis()));
			resourceParameters.add(new ErrorParameter(taskUID));
			StringBuffer stringBuffer = new StringBuffer();
			Iterator<ErrorData> itrError = errorList.iterator();
			while (itrError.hasNext()) {
				ErrorData errorData = itrError.next();
				stringBuffer.append(ErrorHandlerJSONAdapter.createFieldMessage(errorData, fieldConfigs, locale));
				stringBuffer.append(" ");
			}
			resourceParameters.add(new ErrorParameter(stringBuffer));
			ErrorData modifiedErrorData = new ErrorData("admin.actions.importMSProject.err.taskError", resourceParameters);
			errors.add(modifiedErrorData);
		}
		return errors;
	}
	
	/**
	 * This method convert MPXJ lag format to Genji lag format
	 * @param lag
	 * @return
	 */
	public static Integer convertMsProjectLagToTrackPlusLagFormat(Integer lag) {
		switch(lag) {
		//Day
		case 2:
			return MsProjectExchangeDataStoreBean.LAG_FORMAT.d;
		//elapsed day
		case 9:
			return MsProjectExchangeDataStoreBean.LAG_FORMAT.ed;
		//elapsed hours
		case 8:
			return MsProjectExchangeDataStoreBean.LAG_FORMAT.eh;
		//elapsed minutes
		case 7:
			return MsProjectExchangeDataStoreBean.LAG_FORMAT.em;
		//elapsed months
		case 11:
			return MsProjectExchangeDataStoreBean.LAG_FORMAT.emo;
		//elapsed percent
		case 13:
			return MsProjectExchangeDataStoreBean.LAG_FORMAT.ePERCENT;
		//elapsed weeks
		case 10:
			return MsProjectExchangeDataStoreBean.LAG_FORMAT.ew;
		//hours
		case 1:
			return MsProjectExchangeDataStoreBean.LAG_FORMAT.h;
		//minutes
		case 0:
			return MsProjectExchangeDataStoreBean.LAG_FORMAT.m;
		//months
		case 4:
			return MsProjectExchangeDataStoreBean.LAG_FORMAT.mo;
		//percent
		case 5:
			return MsProjectExchangeDataStoreBean.LAG_FORMAT.PERCENT;
		//weeks
		case 3:
			return MsProjectExchangeDataStoreBean.LAG_FORMAT.w;
		//year does not exists in Track.
		case 6:
			return -1;
		default:
			return -2;
		}
	}
	
	public static boolean checkLeapYearForConvert(int year) {
		boolean leaped = false;
	    if (year%4==0){
	        leaped = true;
	        if(year>1582){
	            if (year%100==0&&year%400!=0){
	                leaped=false;
	            }
	        }
	    }
	    return leaped;
	}
}
