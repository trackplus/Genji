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


package com.aurel.track.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aurel.track.beans.TReleaseBean;

/**
 * DAO for Release
 * @author Tamas Ruff
 *
 */
public interface ReleaseDAO {
	/**
	 * Loads a releaseBean by primary key 
	 * @param objectID
	 * @return
	 */
	TReleaseBean loadByPrimaryKey(Integer objectID);
	
	/** 
	 * Loads all releaseBeans for a project (also the child releases)  
	 * @param projectID 
	 * @return
	 */
	List<TReleaseBean> loadAllByProject(Integer projectID);
	
	/** 
	 * Loads all releaseBeans for a project with specific states 
	 * Gets all (main and child) independently of hierarchy
	 * @param projectID
	 * @param states
	 * @return
	 */
	List<TReleaseBean> loadAllByProjectAndStates(Integer projectID, int[] states);
	
	/** 
	 * Loads the main releaseBeans for a project (no child releases) 
	 * @param projectID 
	 * @return
	 */
	List<TReleaseBean> loadMainByProject(Integer projectID);
			
	/**
	 * Loads the main (no child) releaseBeans with specific states 
	 * @param projectID
	 * @param states
	 * @return
	 */
	List<TReleaseBean> loadMainByProjectAndStates(Integer projectID, int[] states);
	
	/**
	 * Loads the main (no child) releaseBeans with specific states 
	 * @param projectIDs
	 * @param states
	 * @param selectedReleaseIDsSet
	 * @return
	 */
	List<TReleaseBean> loadMainByProjectsAndStates(List<Integer> projectIDs, int[] states, Set<Integer> selectedReleaseIDsSet);
	
	/**
	 * Loads all children for a release
	 * @param releaseID
	 * @return
	 */
	List<TReleaseBean> loadChildren(Integer releaseID);	
		
	/**
	 * Loads the child releaseBeans with specific states for a parent release
	 * @param releaseID
	 * @param states
	 * @return
	 */
	List<TReleaseBean> loadChildrenByParentAndStates(Integer releaseID, int[] states);
	
	/**
	 * Loads the child releaseBeans with specific states for parent releases
	 * @param releaseIDs
	 * @param states
	 * @param selectedReleaseIDsSet
	 * @return
	 */
	List<TReleaseBean> loadChildrenByParentsAndStates(List<Integer> releaseIDs, int[] states, Set<Integer> selectedReleaseIDsSet);
	
	/**
	 * Gets the sort order column name 
	 * @return
	 */
	String getSortOrderColumn();
	
	/**
	 * Returns the table name
	 * @return
	 */
	String getTableName();
	
	/**
	 * Gets a releaseBean by project by label
	 * @param projectID
	 * @param label
	 * @return
	 */
	TReleaseBean loadByProjectAndLabel(Integer projectID, String label);
	
	/**
	 * Gets the releaseBeans by uuid list
	 * @param uuids
	 * @return
	 */
	List<TReleaseBean> loadByUUIDs(List<String> uuids); 
	
	/**
	 * Gets the releaseBeans by labels list
	 * @param labels
	 * @return
	 */
	List<TReleaseBean> loadByLabels(List<String> labels); 
	
	/**
	 * Loads a ReleaseBean list by releaseIDs
	 * @param releaseIDs
	 * @return
	 */
	List<TReleaseBean> loadByReleaseIDs(List<Integer> releaseIDs);
	
	
	/**
	 * Loads all ReleaseBeans
	 * @return 
	 */
	List<TReleaseBean> loadAll();
	
	/**
	 * Loads the noticed ReleaseBeans by workItemKeys
	 * @param workItemIDs
	 * @return
	 */
	List<TReleaseBean> loadNoticedByWorkItemKeys(int[] workItemIDs);
	
	/**
	 * Loads the scheduled ReleaseBeans by workItemKeys
	 * @param workItemIDs
	 * @return
	 */
	List<TReleaseBean> loadScheduledByWorkItemKeys(int[] workItemIDs);
	
	/**
	 * Load all active and inactive releases for a list of projects
	 * @return
	 */
	List<TReleaseBean> loadNotClosedByProjectIDs(List<Integer> projectKeys);
	
	/**
	 * Loads a ClassBean list by projectIDs
	 * @param projectIDs
	 * @return
	 */
	List<TReleaseBean> loadByProjectKeys(List<Integer> projectIDs);
	
	
	/**
	 * Saves a releaseBean in the TRelease table
	 * @param releaseBean
	 * @return
	 */
	Integer save(TReleaseBean releaseBean);
	
	/**
	 * Copies a relaseBean
	 * @param releaseBean
	 * @param deep
	 */
	TReleaseBean copy(TReleaseBean releaseBean, boolean deep);
	
	/**
	 * Whether there are workItems with this release
	 * @param objectID
	 * @return
	 */
	boolean hasDependentIssues(Integer objectID);
	
	/**
	 * Deletes a release by primary key
	 * @param objectID
	 */
	void delete(Integer objectID);
	
	/**
	 * Replaces the dependences with a new releaseID and 
	 * deletes the old releaseID from the TRelease table 
	 * @param oldReleaseID
	 * @param newReleaseID
	 */
	void replace(Integer oldReleaseID, Integer newReleaseID);
	
	/*********************************************************
	* Manager-, Responsible-, My- and Custom Reports methods * 
	*********************************************************/
	
	/**
	 * Get the scheduled releaseBeans associated 
	 * with workItems the person is manager for
	 * @param personID
	 * @return
	 */
	
	/**
	 * Get the scheduled releaseBeans associated 
	 * with workItems the person is responsible for
	 * @param personID
	 * @return
	 */

	/**
	 * Get the scheduled releaseBeans associated 
	 * with workItems the person is originator for
	 * @param personID
	 * @return
	 */
	
	/**
	 * Get the scheduled releaseBeans associated 
	 * with workItems the person is manager or responsible or owner for
	 * @param personID
	 * @return
	 */
		
	/**
	 * Get the scheduled releaseBeans filtered by the FilterSelectsTO
	 * @param filterSelectsTO
	 * @return
	 */

	/**
	 * Get the noticed releaseBeans associated with workItems the person is manager for
	 * @param personID
	 * @return
	 */

	/**
	 * Get the noticed releaseBeans associated with workItems the person is responsible for
	 * @param personID
	 * @return
	 */
	
	/**
	 * Get the noticed releaseBeans associated with workItems the person is originator for
	 * @param personID
	 * @return
	 */
	
	/**
	 * Get the noticed releaseBeans associated with workItems the person is manager or responsible or owner for
	 * @param personID
	 * @return
	 */
		
	/**
	 * Get the noticed releaseBeans filtered by the FilterSelectsTO
	 * @param filterSelectsTO
	 * @return
	 */
	
	/**
	 * Get the picker releaseBeans associated 
	 * with workItems the person is manager for
	 * @param personID
	 * @return
	 */

	/**
	 * Get the releaseBeans associated 
	 * with workItems the person is responsible for
	 * @param personID
	 * @return
	 */
	
	/**
	 * Get the picker releaseBeans associated 
	 * with workItems the person is manager or responsible or owner for
	 * @param personID
	 * @return
	 */
	
	/**
	 * Get the picker releaseBeans filtered by the FilterSelectsTO
	 * @param filterSelectsTO
	 * @return
	 */
	
	/**
	 * Get the releaseBeans associated through a release picker 
	 * for an array of workItemIDs
	 * @param workItemIDs
	 * @return
	 */

	/**
	 * Get the Map of releaseBeans from the history of the workItemIDs added by personID
	 * @param workItemIDs
	 * @param personID in null do not filter by personID
	 * @return
	 */
	Map<Integer, TReleaseBean> loadHistoryReleases(int[] workItemIDs);
}
