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


package com.trackplus.dao;

import java.util.List;
import java.util.Map;

import com.trackplus.model.Tppriority;

public interface PPriorityDAO {

	/**
	 * Loads all TPpriorityBeans
	 * 
	 * @return
	 */
	List<Tppriority> loadAll();

	/**
	 * Gets the number of valid priorities for issue types in a projectType
	 * 
	 * @param projectTypeID
	 * @return
	 */
	Map<Integer, Integer> loadNumberOfPrioritiesForIssueTypesInProjectType(
			Integer projectTypeID);

	/**
	 * Gets the number of valid priorities for an issue type in a projectType
	 * 
	 * @param projectTypeID
	 * @param issueTypeID
	 * @return
	 */
	Integer loadNumberOfPrioritiesForIssueTypeInProjectType(
			Integer projectTypeID, Integer issueTypeID);

	/**
	 * Load TPpriorityBeans by project types
	 * 
	 * @param projectType
	 * @return
	 */
	List<Tppriority> loadByProjectType(Integer projectType);

	/**
	 * Load TPpriorityBeans by project types and priorities
	 * 
	 * @param projectType
	 * @param priorities
	 * @return
	 */
	List<Tppriority> loadByProjectTypeAndPriorities(Integer projectType,
			List<Integer> priorities);

	/**
	 * Load TPpriorityBeans by project types and issueType
	 * 
	 * @param projectType
	 * @param issueType
	 * @return
	 */
	List<Tppriority> loadByProjectTypeAndIssueType(Integer projectType,
			Integer issueType);

	/**
	 * Load TPpriorityBeans by project types and issueTypes
	 * 
	 * @param projectType
	 * @param issueTypes
	 * @return
	 */
	List<Tppriority> loadByProjectTypeAndIssueTypes(Integer projectType,
			List<Integer> issueTypes);

	/**
	 * Save Tppriority in the TPpriority table
	 * 
	 * @param ppriorityBean
	 * @return
	 */
	Integer save(Tppriority ppriorityBean);

	/**
	 * Deletes a Tppriority from the TPpriority table
	 * 
	 * @param objectID
	 * @return
	 */
	void delete(Integer objectID);

	/**
	 * Deletes a Tppriority from the TPpriority table
	 * 
	 * @param projectTypeID
	 * @param issueTypeID
	 * @param priorityID
	 */
	void delete(Integer projectTypeID, Integer issueTypeID, Integer priorityID);

}
