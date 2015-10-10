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

package com.aurel.track.item.workflow.execute;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.lists.systemOption.StatusBL;
import com.aurel.track.admin.customize.projectType.assignments.itemType.ProjectTypeItemTypeStatusAssignmentFacade;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TWorkflowActivityBean;
import com.aurel.track.beans.TWorkflowConnectBean;
import com.aurel.track.beans.TWorkflowGuardBean;
import com.aurel.track.beans.TWorkflowStationBean;
import com.aurel.track.beans.TWorkflowTransitionBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.util.GeneralUtils;

public class StatusWorkflow {
	private static final Logger LOGGER = LogManager.getLogger(StatusWorkflow.class);
	
	
	/**
	 * Executes the workflow activity before save. If the error data list is not empty
	 * the issue will not be saved and an error message will be shown.
	 * Otherwise the actual workItemBean can me modified right before saving it
	 * If there is also a status change then the guards for status change were verified already in loadStatesTo()
	 * that means only those statuses were allowed to choose from which passed the guards, consequently the status change
	 * related activities are executed without testing the guards again
	 * If there were no status change (hook to the same status exists in the workflow) then the guards related to the hook are executed
	 * and the activities related are executed only if guard is passed
	 * @param workItemContext
	 * @return
	 */
	public static List<ErrorData> executeWorkflowActivity(WorkItemContext workItemContext) {
		List<ErrorData> errorsList = new LinkedList<ErrorData>();
		return errorsList;
	}
	
	/**
	 * Load the next states possible to choose 
	 * @param projectID not necessarily equals with workItemBean.getProjectID()
	 * @param issueTypeID not necessarily equals with workItemBean.getListTypeID()
	 * @param statusIDFrom may be null (if creating a new issue)
	 * @param personID
	 * @param workItemBean the original workItemBean
	 * @return
	 */
	public static List<TStateBean> loadStatesTo(Integer projectID, Integer issueTypeID,
			Integer statusIDFrom, Integer personID, TWorkItemBean workItemBean, Map<String, Object> inputBinding) {
		TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
		Integer projectTypeID = null;
		if (projectBean!=null) {
			projectTypeID = projectBean.getProjectType();
		}
		return StatusBL.filterClosedStateBeans(statusIDFrom, StatusBL.getByProjectTypeIssueTypeAssignments(projectTypeID, issueTypeID, statusIDFrom), workItemBean, personID);
	}
	
	/**
	 * Whether after moving the item the actual status is valid or not 
	 * @param projectID
	 * @param issueTypeID
	 * @param stateIDFrom
	 */
	public static boolean statusIsValid(Integer projectID, Integer issueTypeID, Integer stateIDFrom) {
		if (projectID == null || issueTypeID == null || stateIDFrom ==null) {
			LOGGER.error("All the following fields should have valid values: " + 
					"project=" + projectID + " issueType="+issueTypeID + " stateFrom=" + stateIDFrom);
			return false;
		}
		TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
		Integer projectTypeID = null;
		if (projectBean!=null) {
			projectTypeID = projectBean.getProjectType();
		}
			Set<Integer> statusIDs = ProjectTypeItemTypeStatusAssignmentFacade.getInstance().getAssignedIDsByProjectTypeIDAndItemType(projectTypeID, issueTypeID);
			if (statusIDs.isEmpty()) {
				//all statuses are allowed for project type and issueType
				return true;
			} else {
				return statusIDs.contains(stateIDFrom);
			}
	}
	
	/**
	 * Loads the possible statuses in an excel import
	 * @param projectID
	 * @param issueTypeID
	 * @return
	 */
	public static List<TStateBean> loadExcelImportStatuses(Integer projectID, Integer issueTypeID) {
			TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
			Integer projectTypeID = null;
			if (projectBean!=null) {
				projectTypeID = projectBean.getProjectType();
			}
			//no workflow: get filtered by projectType and issueType
			return StatusBL.getByProjectTypeIssueTypeAssignments(projectTypeID, issueTypeID, null);
	}
	
	/**
	 * Gets the possible initial statuses and sets the workItemBean with the best matching initial status 
	 * @param projectID
	 * @param issueTypeID
	 * @param workItemBean
	 * @param personID
	 * @return
	 */
	public static List<TStateBean> loadInitialStates(Integer projectID, Integer issueTypeID,
			TWorkItemBean workItemBean, Integer personID, Map<String, Object> inputBinding) {
		List<TStateBean> statesByCreate = loadStatesTo(projectID, issueTypeID, null, personID, workItemBean, inputBinding);
		if (workItemBean!=null) {
			setWorkItemStatus(GeneralUtils.createIntegerListFromBeanList(statesByCreate), workItemBean, projectID, issueTypeID);
		}
		return statesByCreate;
	}
	
	
	
	/**
	 * Set the initial state for the workItemBean
	 * @param stateIDs
	 * @param workItemBean
     * @param issueTypeID
	 */
	private static void setWorkItemStatus(List<Integer> stateIDs,
			TWorkItemBean workItemBean, Integer projectID, Integer issueTypeID) {
		if (!stateIDs.isEmpty() && workItemBean!=null) {
			Integer initialState = StatusBL.getInitialState(projectID, issueTypeID);
			if (initialState!=null) {
				boolean initialStateValid = false;
				for (Integer stateID : stateIDs) {
					if (initialState.equals(stateID)) {
						workItemBean.setStateID(initialState);
						initialStateValid = true;
						break;
					}
				}
				if (!initialStateValid) {
					//set explicitly to the first entry from the list 
					//(if this value was not already set in setDefaultValues() with the value from the defaultWorkItem)
					//Reason: if the tab where this field is present is not selected before saving the issue
					//this field will not be submitted by tab change or save
					workItemBean.setStateID(stateIDs.get(0));
				}
			}
		}
	}
	
	/**
	 * Gets the best matching workflowID
	 * @param projectID
	 * @param issuTypeID
	 * @return
	 */
	private static Integer findWorklowID(Integer projectID, Integer issuTypeID) {
		return null;
	}
}
