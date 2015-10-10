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


package com.aurel.track.dao;

import java.util.List;
import java.util.Set;

import com.aurel.track.beans.TProjectBean;

/**
 * DAO for Project object
 * 
 */
public interface ProjectDAO {
	/**
	 * Gets a projectBean from the TProject table
	 * @param objectID
	 * @return
	 */
	TProjectBean loadByPrimaryKey(Integer objectID);
	
	/**
	 * Gets a projectBean by label
	 * @param label
	 * @return
	 */
	TProjectBean loadByLabel(String label);
	
	/**
	 * Gets a projectBean by prefix
	 * @param prefix
	 * @return
	 */
	List<TProjectBean> loadByPrefix(String prefix);
	
	/**
	 * Loads the private project for the person
	 * @param personID
	 * @return
	 */
	boolean hasPrivateProject(Integer personID);
	
	/**
	 * Loads the private project for the person
	 * @param personID
	 * @return
	 */
	List<TProjectBean> getPrivateProject(Integer personID);
	
	/**
	 * Gets the projectBeans by uuid list
	 * @param uuids
	 * @return
	 */
	List<TProjectBean> loadByUUIDs(List<String> uuids); 
	
	/**
	 * Gets the projectBeans by label list
	 * @param labels
	 * @return
	 */
	List<TProjectBean> loadByLabels(List<String> labels); 
	
	/**
	 * Count the number of projects
	 * @param all: if true count all projects (mainProjects parameter has no importance) 
	 * @param mainProjects: if all is false whether to count the main- or the sub-projects
	 * @return
	 */
	int count(boolean all, boolean mainProjects); 
	
	/**
	 * Loads a projectBean list by a project type
	 * @param projectTypeID
	 * @return
	 */
	List<TProjectBean> loadByProjectType(Integer projectTypeID);
	
	/**
	 * Loads a projectBean list by projectIDs
	 * @param projectIDs
	 * @return
	 */
	List<TProjectBean> loadByProjectIDs(List<Integer> projectIDs);
	
	/**
	 * Loads all main projects (those without parent project)
	 * @return
	 */
	List<TProjectBean> loadAllMainProjects();
	
	/**
	 * Loads the main (no child) projects with specific states 
	 * @param states
	 * @return
	 */
	List<TProjectBean> loadMainProjectsByStates(int[] states);
	
	/**
	 * Loads all subprojects for a project
	 * @param projectID
	 * @return
	 */
	List<TProjectBean> loadSubrojects(Integer projectID);	
	
	/**
	 * Loads the subprojects with specific states for a parent project
	 * @param projectID
	 * @param states
	 * @return
	 */
	List<TProjectBean> loadSubprojectsByParentAndStates(Integer projectID, int[] states);
	
	/**
	 * Loads the subprojects with specific states for parent projects
	 * @param projectID
	 * @param states
	 * @return
	 */
	List<TProjectBean> loadSubprojectsByParentsAndStates(List<Integer> projectID, int[] states);
	
	/**
	 * Loads all projects from TProject table 
	 * @return 
	 */
	List<TProjectBean> loadAll();
	
	/**
	 * Load all ProjectBeans except those closed
	 * Should be called only by system admin
	 * @return
	 */
	List<TProjectBean> loadProjectsByStateIDs(int[] stateIDs);
	
	/**
	 * Get the IDs of the closed projects form projectIDs
	 * @param projectIDs
	 * @return
	 */
	Set<Integer> getClosedProjectIDs(List<Integer> projectIDs);
	
	/**
	 * Whether this project name already exists on the same level 
	 * @param name
	 * @param parentProject
	 * @param exceptProjectID
	 * @param nonPrivate verify only the non private projects
	 * @return
	 */
	boolean projectNameExists(String name, Integer parentProjectID, Integer exceptProjectID, boolean nonPrivate);
	
	/**
	 * Whether this project prefix already exists 
	 * @param prefix
	 * @param parentProject
	 * @param exceptProjectID
	 * @param nonPrivate verify only the non private projects
	 * @return
	 */
	boolean projectPrefixExists(String prefix, Integer parentProjectID, Integer exceptProjectID, boolean nonPrivate);
	
	/**
	 * Helper for deep and shallow copy 
	 * @param projectOriginal
	 * @param deep
	 * @return
	 */
	TProjectBean copy(TProjectBean projectOriginal, boolean deep); 
	
	/**
	 * Loads a ProjectBean list by workItemKeys
	 * @param workItemIDs
	 * @return
	 */
	List<TProjectBean> loadByWorkItemKeys(int[] workItemIDs);
	
	/**
	 * Loads a ProjectBean by workItemID
	 * @param workItemIDs
	 * @return
	 */
	TProjectBean loadByWorkItemKey(Integer workItemID);
	
	/**
	 * Load by users and right and state independently of the project level (main or subproject)
	 * @param personIDs
	 * @param right
	 * @param states
	 * @return
	 */
	List<TProjectBean> loadByUserAndRightAndState(List<Integer> personIDs, int[] right, int[] states);
	
	/**
	 * Load subprojects by persons, parent project, explicit right and states
	 * @param personIDs
	 * @param parentProjectID
	 * @param right
	 * @param states
	 * @return
	 */
	List<TProjectBean> loadSubprojectsByUserAndRightAndState( 
			List<Integer> personIDs, Integer parentProjectID, int[] right, int[] states);
	
	/**
	 * Load the distinct projects containing issues having the field set 
	 * @param fieldID
	 * @return
	 */
	List<TProjectBean> getProjectsWithField(Integer fieldID);
	
	/**
	 * Filter the projects from the projectIDs having specific projectStates
	 * @param projectIDs
	 * @param projectStates
	 * @return
	 */
	Set<Integer> filterProjectsIDsByStates(Set<Integer> projectIDs, int[] projectStates);
	
	/**
	 * Load the not closed projects where the user has
	 * originator/manager/responsible role for at least one workItem except the exceptProjects
	 * @param meAndMySubstituted
	 * @param meAndMySubstitutedAndGroups
	 * @param projectStates
	 * @param exceptProjects
	 * @return
	 */
	/*List<TProjectBean> loadOrigManRespProjects(List<Integer> meAndMySubstituted,
			List<Integer> meAndMySubstitutedAndGroups, int[] projectStates, List<Integer> exceptProjects);*/
	
	/**
	 * Load the not closed projects where the user has consultant/informant
	 * role for at least one workItem except the exceptProjects
	 * @param meAndMySubstitutedAndGroups
	 * @param projectStates
	 * @param exceptProjects
	 * @return
	 */
	/*List<TProjectBean> loadConsultantInformantProjects(List<Integer> meAndMySubstitutedAndGroups,
			int[] projectStates, List<Integer> exceptProjects);*/
	
	/**
	 * Load the not closed projects where the user has on behalf of
	 * role for at least one workItem except the exceptProjects
	 * @param meAndMySubstitutedAndGroups
	 * @param projectStates
	 * @param exceptProjects
	 * @return
	 */
	/*List<TProjectBean> loadOnBehalfOfProjects(List<Integer> meAndMySubstitutedAndGroups,
			int[] projectStates, List<Integer> exceptProjects, List<Integer> onBehalfPickerFieldIDs);*/
	
	/**
	 * Loads a list of projectBeans which has notificationSettings 
	 * (set either explicitely by a person or inherited the default) 
	 * @param personID
	 * @return
	 */
	List<TProjectBean> loadByOwnOrDefaultNotifySettings(Integer personID);
	
	/**
	 * Loads a list of projectBeans which has default notificationSettings set
	 * @return
	 */
	List<TProjectBean> loadByDefaultNotifySettings();
	
	/**
	 * Saves a projectBean in the TProject table
	 * @param projectBean
	 * @return
	 */
	Integer save(TProjectBean projectBean);
	
	/**
	 * Deletes the project dependencies
	 * @param projectID
	 * @return
	 */
	void deleteDependencies(Integer projectID);
	
	/**
	 * Deletes a projectBean from the TProject table
	 * By executing this method no project dependencies should exist
	 * @param projectID
	 * @return
	 */
	void delete(Integer projectID);
	
	/*********************************************************
	* Manager-, Responsible-, My- and Custom Reports methods * 
	*********************************************************/
	
	/**
	 * Get the projectBeans associated with workItems the person is manager for
	 * @param personID
	 * @return
	 */
	//List<TProjectBean> loadManagerProjects(Integer personID);

	/**
	 * Get the projectBeans associated through a project picker
	 * with workItems the person is manager for
	 * @param personID
	 * @return
	 */
	//List<TProjectBean> loadManagerPickerProjects(Integer personID);
	
	/**
	 * Get the projectBeans associated with workItems the person is responsible for
	 * @param personID
	 * @return
	 */
	//List<TProjectBean> loadResponsibleProjects(Integer personID);
	
	/**
	 * Get the projectBeans associated through a project picker 
	 * with workItems the person is responsible for
	 * @param personID
	 * @return
	 */
	//List<TProjectBean> loadResponsiblePickerProjects(Integer personID);
	
	/**
	 * Get the projectBeans associated with workItems the person is originator for
	 * @param personID
	 * @return
	 */
	//List<TProjectBean> loadReporterProjects(Integer personID);
	
	/**
	 * Get the projectBeans associated with workItems the person is manager or responsible or owner for
	 * @param personID 
	 * @return
	 */
	//List<TProjectBean> loadMyProjects(Integer personID);
	
	/**
	 * Get the projectBeans associated with through a project picker  
	 * workItems the person is manager or responsible or owner for
	 * @param personID 
	 * @return
	 */
	//List<TProjectBean> loadMyPickerProjects(Integer personID);
	
	/**
	 * Get the projectBeans filtered by the FilterSelectsTO
	 * @param filterSelectsTO
	 * @return
	 */
	//List<TProjectBean> loadCustomReportProjects(FilterUpperTO filterSelectsTO);
		
	/**
	 * Get the projectBeans associated through a project picker 
	 * filtered by the FilterSelectsTO
	 * @param filterSelectsTO
	 * @return
	 */
	//List<TProjectBean> loadCustomReportPickerProjects(FilterUpperTO filterSelectsTO);
	
	/**
	 * Get the projectBeans associated through a project picker 
	 * for an array of workItemIDs
	 * @param workItemIDs
	 * @return
	 */
	//List<TProjectBean> loadLucenePickerProjects(int[] workItemIDs);
	
	/**
	 * Get the projectBeans from the history of the workItemIDs, a project might appear more times in this list!
	 * @param workItemIDs
	 * @return
	 */
	List<TProjectBean> loadHistoryProjects(int[] workItemIDs);
}
