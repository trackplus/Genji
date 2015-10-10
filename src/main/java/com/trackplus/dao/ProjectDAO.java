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


package com.trackplus.dao;

import java.util.List;
import java.util.Set;

import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.trackplus.model.Tproject;

/**
 * DAO for Project object
 * 
 */
public interface ProjectDAO {
	/**
	 * Gets a projectBean from the TProject table
	 * 
	 * @param objectID
	 * @return
	 */
	Tproject loadByPrimaryKey(Integer objectID);

	/**
	 * Gets a projectBean by label
	 * 
	 * @param label
	 * @return
	 */
	Tproject loadByLabel(String label);

	/**
	 * Loads the private project for the person
	 * 
	 * @param personID
	 * @return
	 */
	boolean hasPrivateProject(Integer personID);

	/**
	 * Gets the projectBeans by uuid list
	 * 
	 * @param uuids
	 * @return
	 */
	List<Tproject> loadByUUIDs(List<String> uuids);

	/**
	 * Gets the projectBeans by label list
	 * 
	 * @param labels
	 * @return
	 */
	List<Tproject> loadByLabels(List<String> labels);

	/**
	 * Count the number of projects
	 * 
	 * @param all
	 *            : if true count all projects (mainProjects parameter has no
	 *            importance)
	 * @param mainProjects
	 *            : if all is false whether to count the main- or the
	 *            sub-projects
	 * @return
	 */
	int count(boolean all, boolean mainProjects);

	/**
	 * Loads a ProjectBean list by projectIDs
	 * 
	 * @param projectIDs
	 * @return
	 */
	List<Tproject> loadByProjectIDs(List<Integer> projectIDs);

	/**
	 * Loads all main projects (those without parent project)
	 * 
	 * @return
	 */
	List<Tproject> loadAllMainProjects();

	/**
	 * Loads the main (no child) projects with specific states
	 * 
	 * @param states
	 * @return
	 */
	List<Tproject> loadMainProjectsByStates(int[] states);

	/**
	 * Loads the not closed main projects
	 * 
	 * @return
	 */
	// List<Tproject> loadActiveInactiveMainProjects();
	/**
	 * Loads the active main projects
	 * 
	 * @return
	 */
	// List<Tproject> loadActiveMainProjects(); >
	/**
	 * Loads all main projects (those without parent project) for which the
	 * personID is project admin
	 * 
	 * @param personID
	 * @return
	 */
	// List<Tproject> loadMainProjectsByProjectAdmin(Integer personID); >
	/**
	 * Loads all subprojects for a project
	 * 
	 * @param projectID
	 * @return
	 */
	List<Tproject> loadSubrojects(Integer projectID);

	/**
	 * Loads the subprojects with specific states for a parent project
	 * 
	 * @param projectID
	 * @param states
	 * @return
	 */
	List<Tproject> loadSubprojectsByParentAndStates(Integer projectID,
			int[] states);

	/**
	 * Loads the subprojects with specific states for parent projects
	 * 
	 * @param projectID
	 * @param states
	 * @return
	 */
	List<Tproject> loadSubprojectsByParentsAndStates(List<Integer> projectID,
			int[] states);

	/**
	 * Loads all projects from TProject table
	 * 
	 * @return
	 */
	List<Tproject> loadAll();

	/**
	 * Load all ProjectBeans except those closed Should be called only by system
	 * admin
	 * 
	 * @return
	 */
	List<Tproject> loadProjectsByStateIDs(int[] stateIDs);

	/**
	 * Get the IDs of the closed projects form projectIDs
	 * 
	 * @param projectIDs
	 * @return
	 */
	Set<Integer> getClosedProjectIDs(List<Integer> projectIDs);

	/**
	 * Whether this project name already exists on the same level
	 * 
	 * @param name
	 * @param parentProject
	 * @param exceptProjectID
	 * @param nonPrivate
	 *            verify only the non private projects
	 * @return
	 */
	boolean projectNameExists(String name, Integer parentProjectID,
			Integer exceptProjectID, boolean nonPrivate);

	/**
	 * Whether this project prefix already exists
	 * 
	 * @param prefix
	 * @param parentProject
	 * @param exceptProjectID
	 * @param nonPrivate
	 *            verify only the non private projects
	 * @return
	 */
	boolean projectPrefixExists(String prefix, Integer parentProjectID,
			Integer exceptProjectID, boolean nonPrivate);

	/**
	 * Helper for deep and shallow copy
	 * 
	 * @param projectOriginal
	 * @param deep
	 * @return
	 */
	Tproject copy(Tproject projectOriginal, boolean deep);

	/**
	 * Saves a copy of a project specified by projectID It copies also the
	 * linked entities to the selected entities map
	 * 
	 * @param projectID
	 * @param selectedEntitiesMap
	 * @param projectName
	 * @return id of the new project
	 */
	// Integer copy(Integer projectID, Map selectedEntitiesMap, String
	// projectName);

	/**
	 * Loads a ProjectBean list by workItemKeys
	 * 
	 * @param workItemIDs
	 * @return
	 */
	List<Tproject> loadByWorkItemKeys(int[] workItemIDs);

	/**
	 * Loads a ProjectBean by workItemID
	 * 
	 * @param workItemIDs
	 * @return
	 */
	Tproject loadByWorkItemKey(Integer workItemID);

	/**
	 * Load by user and right and state independently of the level (main or
	 * subproject)
	 * 
	 * @param personID
	 * @param right
	 * @param states
	 * @return
	 */
	List<Tproject> loadByUserAndRightAndState(Integer personID, int[] right,
			int[] states);

	/**
	 * Load main projects by user and right and state
	 * 
	 * @param personID
	 * @param right
	 * @param states
	 * @return
	 */
	/*
	 * List<Tproject> loadMainProjectsByUserAndRightAndState(> Integer personID,
	 * int[] right, int[] states);
	 */

	/**
	 * Load subprojects by user and right and state
	 * 
	 * @param personID
	 * @param projectIDs
	 * @param right
	 * @param states
	 * @return
	 */
	/*
	 * List<Tproject> loadSubprojectsByUserAndRightAndState( > Integer personID,
	 * List<Integer> projectIDs, int[] right, int[] states);
	 */

	/**
	 * Load subprojects by parent project, user, right and state
	 * 
	 * @param personID
	 * @param parentProjectID
	 * @param right
	 * @param states
	 * @return
	 */
	List<Tproject> loadSubprojectsByUserAndRightAndState(Integer personID,
			Integer parentProjectID, int[] right, int[] states);

	/**
	 * Loads all active and inactive projects for which the personID is project
	 * admin
	 * 
	 * @param personID
	 * @return
	 */
	// List<Tproject> loadActiveInactiveProjectsByProjectAdmin(Integer
	// personID); >
	/**
	 * Loads the not closed projects where the person has read right
	 * 
	 * @param person
	 * @return
	 */
	// List<Tproject> loadProjectsWithReadIssueRight(Integer person);

	/**
	 * Loads all projects right for the personID
	 * 
	 * @param personID
	 * @param arrRights
	 * @return
	 */
	// List<Tproject> loadProjectsByRights(Integer personID, int[] arrRights);
	/**
	 * Loads the not closed projects where the person has create right
	 * 
	 * @param person
	 * @return
	 */
	// List<Tproject> loadProjectsWithCreateIssueRight(Integer person);
	/**
	 * Loads the not closed projects where the person has modify right
	 * 
	 * @param person
	 * @return
	 */
	// List<Tproject> loadProjectsWithModifyIssueRight(Integer person);
	/**
	 * Loads the projects where the person has view all expenses right
	 * 
	 * @param person
	 * @return
	 */
	// List<Tproject> loadProjectsWithViewAllExpensesRight(Integer person);
	/**
	 * Loads the not closed projects where the person has any role in project or
	 * raci role in at least one workItem form the project
	 * 
	 * @param person
	 * @return
	 */
	// List<Tproject> loadUsedProjects(Integer person);
	/**
	 * Load the distinct projects containing issues having the field set
	 * 
	 * @param fieldID
	 * @return
	 */
	List<Tproject> getProjectsWithField(Integer fieldID);

	/**
	 * Load the not closed projects where the user has
	 * originator/manager/responsible role for at least one workItem except the
	 * exceptProjects
	 * 
	 * @param personID
	 * @param projectStates
	 * @param exceptProjects
	 * @return
	 */
	List<Tproject> loadOrigManRespProjects(Integer personID,
			int[] projectStates, List<Integer> exceptProjects);

	/**
	 * Load the not closed projects where the user has consultant/informant role
	 * for at least one workItem except the exceptProjects
	 * 
	 * @param personID
	 * @param projectStates
	 * @param exceptProjects
	 * @return
	 */
	List<Tproject> loadConsultantInformantProjects(Integer personID,
			int[] projectStates, List<Integer> exceptProjects);

	/**
	 * Loads a list of projectBeans which has notificationSettings (set either
	 * explicitely by a person or inherited the default)
	 * 
	 * @param personID
	 * @return
	 */
	List<Tproject> loadByOwnOrDefaultNotifySettings(Integer personID);

	/**
	 * Loads a list of projectBeans which has default notificationSettings set
	 * 
	 * @return
	 */
	List<Tproject> loadByDefaultNotifySettings();

	/**
	 * Saves a projectBean in the TProject table
	 * 
	 * @param projectBean
	 * @return
	 */
	Integer save(Tproject projectBean);

	/**
	 * Deletes the project dependencies
	 * 
	 * @param projectID
	 * @return
	 */
	void deleteDependencies(Integer projectID);

	/**
	 * Deletes a projectBean from the TProject table By executing this method no
	 * project dependencies should exist
	 * 
	 * @param projectID
	 * @return
	 */
	void delete(Integer projectID);

	/*********************************************************
	 * Manager-, Responsible-, My- and Custom Reports methods *
	 *********************************************************/

	/**
	 * Get the projectBeans associated with workItems the person is manager for
	 * 
	 * @param personID
	 * @return
	 */
	List<Tproject> loadManagerProjects(Integer personID);

	/**
	 * Get the projectBeans associated through a project picker with workItems
	 * the person is manager for
	 * 
	 * @param personID
	 * @return
	 */
	// List<Tproject> loadManagerPickerProjects(Integer personID);
	/**
	 * Get the projectBeans associated with workItems the person is responsible
	 * for
	 * 
	 * @param personID
	 * @return
	 */
	List<Tproject> loadResponsibleProjects(Integer personID);

	/**
	 * Get the projectBeans associated through a project picker with workItems
	 * the person is responsible for
	 * 
	 * @param personID
	 * @return
	 */
	// List<Tproject> loadResponsiblePickerProjects(Integer personID);
	/**
	 * Get the projectBeans associated with workItems the person is originator
	 * for
	 * 
	 * @param personID
	 * @return
	 */
	List<Tproject> loadReporterProjects(Integer personID);

	/**
	 * Get the projectBeans associated with workItems the person is manager or
	 * responsible or owner for
	 * 
	 * @param personID
	 * @return
	 */
	List<Tproject> loadMyProjects(Integer personID);

	/**
	 * Get the projectBeans associated with through a project picker workItems
	 * the person is manager or responsible or owner for
	 * 
	 * @param personID
	 * @return
	 */
	// List<Tproject> loadMyPickerProjects(Integer personID);
	/**
	 * Get the projectBeans filtered by the FilterSelectsTO
	 * 
	 * @param filterSelectsTO
	 * @return
	 */
	List<Tproject> loadCustomReportProjects(FilterUpperTO filterSelectsTO);

	/**
	 * Get the projectBeans associated through a project picker filtered by the
	 * FilterSelectsTO
	 * 
	 * @param filterSelectsTO
	 * @return
	 */
	// List<Tproject> loadCustomReportPickerProjects(FilterUpperTO
	// filterSelectsTO);
	/**
	 * Get the projectBeans associated through a project picker for an array of
	 * workItemIDs
	 * 
	 * @param workItemIDs
	 * @return
	 */
	// List<Tproject> loadLucenePickerProjects(int[] workItemIDs);
	/**
	 * Get the projectBeans from the history of the workItemIDs, a project might
	 * appear more times in this list!
	 * 
	 * @param workItemIDs
	 * @return
	 */
	List<Tproject> loadHistoryProjects(int[] workItemIDs);
}
