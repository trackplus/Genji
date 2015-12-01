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

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import net.sf.mpxj.Resource;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.attachment.AttachBL;
import com.aurel.track.beans.TCostBean;
import com.aurel.track.beans.TMSProjectTaskBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.dao.CostDAO;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.errors.ErrorHandlerJSONAdapter;
import com.aurel.track.exchange.ImportCounts;
import com.aurel.track.exchange.ImportJSON;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeBL;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDataStoreBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.item.budgetCost.ExpenseBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.util.GeneralUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * Importing from an msProject xml file
 * 
 * @author Tamas
 * 
 */
public class MsProjectImportAction extends ActionSupport implements Preparable,
		SessionAware, ServletResponseAware {

	private static final long serialVersionUID = 1L;
	private static String MS_PROJECT_IMPORTER_BEAN = "msProjectImporterBean";
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private Locale locale;
	private String fileName;

	// the submitted selected project (negative value) or release (positive
	// value)
	private Integer projectOrReleaseID;
	private TPersonBean personBean;
	private Map<Integer, Integer> resourceUIDToPersonIDMap;
	private Map<Integer, String> resourceUIDToUsernameMap;
	private Map<Integer, String> resourceUIDToEmailMap;

	// coming from a certain project from project configuration
	// used only to preserve the value when we get back to the previous page
	// because of wrong file
	private Integer fromProject;
	private Map<Integer, Boolean> budgetOverwriteMap;
	private Map<Integer, Boolean> startDateOverwriteMap;
	private Map<Integer, Boolean> endDateOverwriteMap;
	private Map<Integer, Boolean> deleteTaskMap;
	private Map<Integer, Boolean> undeleteTaskMap;

	/**
	 * Prepares the standard data
	 */
	@Override
	public void prepare() throws Exception {
		locale = (Locale) session.get(Constants.LOCALE_KEY);
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
	}

	/**
	 * Render the resource to track+ user match
	 */
	@Override
	public String execute() {
		String msProjectDirectory = AttachBL.getMsProjectImportDirBase()
				+ projectOrReleaseID;
		File file = new File(msProjectDirectory, fileName);
		MsProjectExchangeDataStoreBean msProjectExchangeDataStoreBean;
		try {
			// initialize a new MsProjectImporterBean
			
			msProjectExchangeDataStoreBean = MsProjectExchangeBL.initMsProjectExchangeBeanForImport(
					projectOrReleaseID, personBean, file, locale);
			session.put(MS_PROJECT_IMPORTER_BEAN,
					msProjectExchangeDataStoreBean);
		} catch (MSProjectImportException e) {
			// addActionError(getText(e.getMessage()));zzz
			// error creating the MsProjectImporterBean: delete the source file
			if (file != null) {
				file.delete();
				File parentFile = new File(msProjectDirectory);
				String[] files = parentFile.list();
				if (files == null || files.length == 0) {
					parentFile.delete();
				}
			}			
			JSONUtility.encodeJSON(servletResponse, ImportJSON
					.importMessageJSON(false, getText(e.getMessage()), true,
							locale), false);
			return null;
		}
		SortedMap<String, Integer> resourceNameToResourceUIDMap = MsProjectImporterBL
				.getResourceNameToResourceUIDMap(msProjectExchangeDataStoreBean
						.getWorkResources());
		
		if (resourceNameToResourceUIDMap.isEmpty()) {
			// no resources in msProject, import directly
			return doImport();
		} else {
			// there are some resources: render the resource to user mapping,
			// even if by a previous import it was already mapped because it
			// might be changed
			JSONUtility.encodeJSON(servletResponse, MsProjectImporterBL
					.prepareResourceMatch(msProjectExchangeDataStoreBean,
							locale), false);
			
			return null;
		}
	}

	/**
	 * Receives the resourceUIDToPersonIDMap submit
	 * 
	 * @return
	 */
	public String submitResourceMapping() {
		MsProjectExchangeDataStoreBean msProjectExchangeDataStoreBean = (MsProjectExchangeDataStoreBean) session
				.get(MS_PROJECT_IMPORTER_BEAN);
		// save the current submissions, first only in bean, and later also in
		// the database if import is done
		msProjectExchangeDataStoreBean.setResourceUIDToPersonIDMap(resourceUIDToPersonIDMap);
		// get the resourceUID to personID mappings, eventually by adding new users
		List<Resource> workResources = msProjectExchangeDataStoreBean
				.getWorkResources();
		SortedMap<Integer, List<ErrorData>> errorsMap = new TreeMap<Integer, List<ErrorData>>();
		ApplicationBean applicationBean = ApplicationBean.getInstance();

		Map<Integer, String> resourceUIDToResourceNameMap = MsProjectImporterBL
				.getResourceUIDToResourceNameMap(workResources);
		errorsMap = MSProjectResourceMappingBL.verifyNewUsers(workResources,
				resourceUIDToResourceNameMap, resourceUIDToPersonIDMap,
				resourceUIDToUsernameMap, resourceUIDToEmailMap);
		if (errorsMap.size() > 0) {
			List<ErrorData> errors = MsProjectImporterBL
					.handleResourceErrorMap(errorsMap,
							resourceUIDToResourceNameMap, locale);
			JSONUtility.encodeJSON(servletResponse, ImportJSON
					.importErrorMessageListJSON(ErrorHandlerJSONAdapter
							.handleErrorList(errors, locale), null, false));
			return null;
		}
		int numberOfUsersCreated = MSProjectResourceMappingBL.createNewUsers(
				workResources, resourceUIDToResourceNameMap,
				resourceUIDToPersonIDMap, resourceUIDToUsernameMap,
				resourceUIDToEmailMap);
		if (numberOfUsersCreated > 0) {
			applicationBean.setActualUsers();
		}
		return doImport();
	}

	/**
	 * Save the field mappings for the next use
	 * 
	 * @return
	 * @throws MSProjectImportException
	 */
	public String doImport() {
		MsProjectExchangeDataStoreBean msProjectExchangeDataStoreBean = (MsProjectExchangeDataStoreBean) session
				.get(MS_PROJECT_IMPORTER_BEAN);
		Map<Integer, WorkItemContext> workItemContextsMap = new HashMap<Integer, WorkItemContext>();
		Integer entityID = msProjectExchangeDataStoreBean.getEntityID();
		int entityType = msProjectExchangeDataStoreBean.getEntityType();

		// task workItems already existing in track+
		Map<Integer, TWorkItemBean> existingTrackPlusWorkItemsMap = MsProjectExchangeBL
				.getTaskWorkItemsForImport(entityID, entityType);
		// TMSProjectTaskBeans already existing in track+
		Map<Integer, TMSProjectTaskBean> existingTrackPlusTasks = MsProjectExchangeBL
				.getMsProjectTasksForImport(entityID, entityType);
		// the taskUID to workItemID mapping for existing tasks
		Map<Integer, Integer> taskUIDToWorkItemIDMap = MsProjectExchangeBL
				.getTaskUIDToWorkItemIDMap(existingTrackPlusTasks);
		
		// prepare the workItemContextsMap and get the validation errors
		SortedMap<Integer, List<ErrorData>> validationErrorsMap = MsProjectImporterBL
				.validateAndPrepareTasks(msProjectExchangeDataStoreBean,
						existingTrackPlusWorkItemsMap, existingTrackPlusTasks,
						workItemContextsMap);
	
		if (validationErrorsMap.size() > 0) {
			List<ErrorData> errors = MsProjectImporterBL
					.handleTaskValidationErrorMap(validationErrorsMap,
							workItemContextsMap, locale);
			JSONUtility.encodeJSON(servletResponse, ImportJSON
					.importErrorMessageListJSON(ErrorHandlerJSONAdapter
							.handleErrorList(errors, locale),
							ImportJSON.ERROR_CODES.ERROR_MESSAGES, false));
			return null;
		}
		Double hoursPerWorkday = MsProjectImporterBL
				.getHoursPerWorkday(msProjectExchangeDataStoreBean.getProject().getProjectHeader());
		Map<Integer, Map<Integer, Map<Integer, String>>> conflictOnMap = new HashMap<Integer, Map<Integer, Map<Integer, String>>>();
		
		// boolean conflictFound = false;
		// budget values conflict resolution
		if (budgetOverwriteMap == null) {
			// first rendering of budget conflict
			budgetOverwriteMap = new HashMap<Integer, Boolean>();
		}
		Collection<Integer> existingWorkItems = taskUIDToWorkItemIDMap.values();
		Map<Integer, Map<Integer, String>> budgetConflictValuesMap = new HashMap<Integer, Map<Integer, String>>();
		Map<Integer, String> projectSpecificItemIDsMap = MsProjectImporterBL.getProjectSpecificItemIDs(
				GeneralUtils.createIntArrFromIntegerCollection(existingWorkItems),
				entityID, entityType);
		MsProjectImporterBL.budgetConflictResolution(
				msProjectExchangeDataStoreBean, hoursPerWorkday,
				taskUIDToWorkItemIDMap, budgetOverwriteMap,
				budgetConflictValuesMap, projectSpecificItemIDsMap);
		
		if (budgetConflictValuesMap.size() > 0) {
		  conflictOnMap.put(MsProjectImporterBL.CONFLICT_ON.BUDGET, budgetConflictValuesMap);
		}
		
		if (startDateOverwriteMap == null) {
			startDateOverwriteMap = new HashMap<Integer, Boolean>();
		}
		if (endDateOverwriteMap == null) {
			endDateOverwriteMap = new HashMap<Integer, Boolean>();
		}
		Map<Integer, Map<Integer, String>> startDateConflictValuesMap = new HashMap<Integer, Map<Integer, String>>();
		Map<Integer, Map<Integer, String>> endDateConflictValuesMap = new HashMap<Integer, Map<Integer, String>>();
		MsProjectImporterBL.dateConflictResolution(
				msProjectExchangeDataStoreBean, existingTrackPlusWorkItemsMap,
				taskUIDToWorkItemIDMap, startDateOverwriteMap,
				endDateOverwriteMap, startDateConflictValuesMap,
				endDateConflictValuesMap, workItemContextsMap, projectSpecificItemIDsMap);
		
		if (startDateConflictValuesMap.size() > 0) {
			/*
			 * conflictFound = true; TFieldConfigBean startDateFieldConfigBean =
			 * LocalizeUtil.localizeFieldConfig(
			 * FieldRuntimeBL.getValidConfig(SystemFields.INTEGER_STARTDATE,
			 * msProjectExchangeDataStoreBean.getIssueType(),
			 * msProjectExchangeDataStoreBean.getProjectID()), locale);
			 * addFieldError("startDateConflict",
			 * getText("admin.actions.importMSProject.conflict.err.conflictMessage"
			 * , new String[] {startDateFieldConfigBean.getLabel()}));
			 */
			conflictOnMap.put(MsProjectImporterBL.CONFLICT_ON.STARTDATE,
					startDateConflictValuesMap);
		}
		if (endDateConflictValuesMap.size() > 0) {
			/*
			 * conflictFound = true; TFieldConfigBean endDateFieldConfigBean =
			 * LocalizeUtil.localizeFieldConfig(
			 * FieldRuntimeBL.getValidConfig(SystemFields.INTEGER_ENDDATE,
			 * msProjectExchangeDataStoreBean.getIssueType(),
			 * msProjectExchangeDataStoreBean.getProjectID()), locale);
			 * addFieldError("endDateConflict",
			 * getText("admin.actions.importMSProject.conflict.err.conflictMessage"
			 * , new String[] {endDateFieldConfigBean.getLabel()}));
			 */
			conflictOnMap.put(MsProjectImporterBL.CONFLICT_ON.ENDDATE,
					endDateConflictValuesMap);
		}
		
		if (deleteTaskMap == null) {
			deleteTaskMap = new HashMap<Integer, Boolean>();
		}
		if (undeleteTaskMap == null) {
			undeleteTaskMap = new HashMap<Integer, Boolean>();
		}
		
		Map<Integer, Map<Integer, String>> deleteTaskValuesMap = new HashMap<Integer, Map<Integer, String>>();
		Map<Integer, Map<Integer, String>> undeleteTaskValuesMap = new HashMap<Integer, Map<Integer, String>>();
		MsProjectImporterBL.getDeleteUndeleteTrackWorkItems(
				msProjectExchangeDataStoreBean, existingTrackPlusWorkItemsMap,
				taskUIDToWorkItemIDMap, deleteTaskValuesMap, deleteTaskMap,
				undeleteTaskValuesMap, undeleteTaskMap, projectSpecificItemIDsMap);
		
		if (deleteTaskValuesMap.size() > 0) {
			/*
			 * conflictFound = true; addFieldError("taskDelete",
			 * getText("admin.actions.importMSProject.conflict.err.taskDelete"
			 * ));
			 */
			conflictOnMap.put(MsProjectImporterBL.CONFLICT_ON.DELETED,
					deleteTaskValuesMap);
		}
		if (undeleteTaskValuesMap.size() > 0) {
			/*
			 * conflictFound = true; addFieldError("taskUndelete",
			 * getText("admin.actions.importMSProject.conflict.err.taskUndelete"
			 * ));
			 */
			conflictOnMap.put(MsProjectImporterBL.CONFLICT_ON.UNDELETED,
					undeleteTaskValuesMap);
		}
		if (conflictOnMap.size() > 0) {
			JSONUtility.encodeJSON(servletResponse, MsProjectImportJSON
					.getMsProjectConflictsJSON(conflictOnMap,
							msProjectExchangeDataStoreBean.getProjectID(),
							msProjectExchangeDataStoreBean.getIssueType(),
							locale, false));
			
			if(msProjectExchangeDataStoreBean.getFile() != null) {
				msProjectExchangeDataStoreBean.getFile().delete();
			}
			return null;
		}
		
		Set<Integer> existingWorkItemSet = existingTrackPlusWorkItemsMap.keySet();
		int[]existingWorkItemKeysArray = new int[existingWorkItemSet.size()];
		int i = 0;
		for(Integer element : existingWorkItemSet) {
			existingWorkItemKeysArray[i] = element;
			i++;
		}
		List<TCostBean> exisitngWorkItemsCosts = ExpenseBL.loadByWorkItemKeys(existingWorkItemKeysArray, null, null, null, null, false);
		Map<Integer, TCostBean> workItemIdToCostBean = new HashMap<Integer, TCostBean>();
		for(TCostBean bean : exisitngWorkItemsCosts) {
			workItemIdToCostBean.put(bean.getWorkItemID(), bean);
		}

		SortedMap<Integer, List<ErrorData>> saveErrorsMap = new TreeMap<Integer, List<ErrorData>>();
		ImportCounts importCounts = MsProjectImporterBL.importTasks(
				msProjectExchangeDataStoreBean, existingTrackPlusWorkItemsMap,
				existingTrackPlusTasks, hoursPerWorkday,
				taskUIDToWorkItemIDMap, saveErrorsMap, workItemContextsMap,
				budgetOverwriteMap, startDateOverwriteMap, endDateOverwriteMap,
				deleteTaskMap, undeleteTaskMap, workItemIdToCostBean);
		if (saveErrorsMap.size() > 0) {
			List<ErrorData> errors = MsProjectImporterBL.handleTaskValidationErrorMap(saveErrorsMap,
					workItemContextsMap, locale);
			
			JSONUtility.encodeJSON(servletResponse, ImportJSON
					.importErrorMessageListJSON(ErrorHandlerJSONAdapter
							.handleErrorList(errors, locale),
							ImportJSON.ERROR_CODES.ERROR_MESSAGES, false));
			return null;
		}
		// saves the file content in the database
		MsProjectExchangeBL
				.saveMSProjectImportFile(msProjectExchangeDataStoreBean);
		JSONUtility.encodeJSON(servletResponse, ImportJSON.importMessageJSON(
				true,
				getText("admin.actions.importMSProject.lbl.result",
						new String[] {
								Integer.valueOf(
										importCounts.getNoOfCreatedIssues())
										.toString(),
								Integer.valueOf(
										importCounts.getNoOfUpdatedIssues())
										.toString(),
								Integer.valueOf(
										importCounts.getNoOfDeletedIssues())
										.toString(),
								Integer.valueOf(
										importCounts.getNoOfPlannedWorks())
										.toString(),
								Integer.valueOf(
										importCounts.getNoOfCreatedLinks())
										.toString(),
								Integer.valueOf(
										importCounts.getNoOfUpdatedLinks())
										.toString(),
								Integer.valueOf(
										importCounts.getNoOfDeletedLinks())
										.toString() }), true, locale));
		return null;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setProjectOrReleaseID(Integer projectOrReleaseID) {
		this.projectOrReleaseID = projectOrReleaseID;
	}

	public Map<Integer, Integer> getResourceUIDToPersonIDMap() {
		return resourceUIDToPersonIDMap;
	}

	public void setResourceUIDToPersonIDMap(
			Map<Integer, Integer> resourceUIDToPersonIDMap) {
		this.resourceUIDToPersonIDMap = resourceUIDToPersonIDMap;
	}

	public Integer getFromProject() {
		return fromProject;
	}

	public void setFromProject(Integer fromProject) {
		this.fromProject = fromProject;
	}

	public Map<Integer, String> getResourceUIDToUsernameMap() {
		return resourceUIDToUsernameMap;
	}

	public void setResourceUIDToUsernameMap(
			Map<Integer, String> resourceUIDToUsernameMap) {
		this.resourceUIDToUsernameMap = resourceUIDToUsernameMap;
	}

	public Map<Integer, String> getResourceUIDToEmailMap() {
		return resourceUIDToEmailMap;
	}

	public void setResourceUIDToEmailMap(
			Map<Integer, String> resourceUIDToEmailMap) {
		this.resourceUIDToEmailMap = resourceUIDToEmailMap;
	}

	public Map<Integer, Boolean> getBudgetOverwriteMap() {
		return budgetOverwriteMap;
	}

	public void setBudgetOverwriteMap(Map<Integer, Boolean> budgetOverwriteMap) {
		this.budgetOverwriteMap = budgetOverwriteMap;
	}

	public Map<Integer, Boolean> getStartDateOverwriteMap() {
		return startDateOverwriteMap;
	}

	public void setStartDateOverwriteMap(
			Map<Integer, Boolean> startDateOverwriteMap) {
		this.startDateOverwriteMap = startDateOverwriteMap;
	}

	public Map<Integer, Boolean> getEndDateOverwriteMap() {
		return endDateOverwriteMap;
	}

	public void setEndDateOverwriteMap(Map<Integer, Boolean> endDateOverwriteMap) {
		this.endDateOverwriteMap = endDateOverwriteMap;
	}

	public Map<Integer, Boolean> getDeleteTaskMap() {
		return deleteTaskMap;
	}

	public void setDeleteTaskMap(Map<Integer, Boolean> deleteTaskMap) {
		this.deleteTaskMap = deleteTaskMap;
	}

	public Map<Integer, Boolean> getUndeleteTaskMap() {
		return undeleteTaskMap;
	}

	public void setUndeleteTaskMap(Map<Integer, Boolean> undeleteTaskMap) {
		this.undeleteTaskMap = undeleteTaskMap;
	}

	public String getIssueNoLabel() {
		return FieldRuntimeBL.getLocalizedDefaultFieldLabel(
				SystemFields.INTEGER_ISSUENO, locale);
	}

	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
}
