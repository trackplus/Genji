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

import com.aurel.track.beans.TPseverityBean;

public interface PSeverityDAO {
	/**
	 * Loads all TPseverityBeans
	 * @return
	 */
	List<TPseverityBean> loadAll();
	
	/**
	 * Gets the number of valid severities for issue types in a projectType
	 * @param projectTypeID
	 * @return
	 */
	Map<Integer, Integer> loadNumberOfSeveritiesForIssueTypesInProjectType(Integer projectTypeID);
	
	/**
	 * Gets the number of valid severities for an issue type in a projectType
	 * @param projectTypeID
	 * @param issueTypeID
	 * @return
	 */
	Integer loadNumberOfSeveritiesForIssueTypeInProjectType(Integer projectTypeID, Integer issueTypeID);
	
	/**
	 * Load TPseverityBeans by project types
	 * @param projectType
	 * @return
	 */
	List<TPseverityBean> loadByProjectType(Integer projectType);
	
	/**
	 * Load TPseverityBeans by project types and severities
	 * @param projectType
	 * @param severities
	 * @return
	 */
	List<TPseverityBean> loadByProjectTypeAndSeverities(Integer projectType, List<Integer> severities);
	
	/**
	 * Load TPseverityBeans by project types and issueType
	 * @param projectType
	 * @param issueType
	 * @return
	 */
	List<TPseverityBean> loadByProjectTypeAndIssueType(Integer projectType, Integer issueType);
	
	/**
	 * Load TPseverityBeans by project types and issueTypes
	 * @param projectType
	 * @param issueTypes
	 * @return
	 */
	List<TPseverityBean> loadByProjectTypeAndIssueTypes(Integer projectType, List<Integer> issueTypes);
	
	/**
	 * Save  TPseverityBean in the TPseverity table
	 * @param pseverityBean
	 * @return
	 */
	Integer save(TPseverityBean pseverityBean);
	
	/**
	 * Deletes a TPseverityBean from the TPseverity table 
	 * @param objectID
	 * @return
	 */
	void delete(Integer objectID);
		
	/**
	 * Deletes TPseverityBean(s) from the TPseverity table 
	 * @param projectTypeID
	 * @param itemTypeID
	 * @param severityIDs
	 */
	void delete(Integer projectTypeID, Integer itemTypeID, List<Integer> severityIDs);
	
}
