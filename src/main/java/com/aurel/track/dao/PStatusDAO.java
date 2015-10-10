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
import java.util.Map;

import com.aurel.track.beans.TPstateBean;

public interface PStatusDAO {
	
	/**
	 * Loads all TPstateBeans 
	 * @return
	 */
	List<TPstateBean> loadAll();
	
	/**
	 * Gets the number of valid statuses for issue types in a projectType
	 * @param projectTypeID
	 * @return
	 */
	Map<Integer, Integer> loadNumberOfStatusesForIssueTypesInProjectType(Integer projectTypeID);
	
	/**
	 * Gets the number of valid statuses for an issue type in a projectType
	 * @param projectTypeID
	 * @param issueTypeID
	 * @return
	 */
	Integer loadNumberOfStatusesForIssueTypeInProjectType(Integer projectTypeID, Integer issueTypeID);
	
	/**
	 * Load TPstateBeans by project types and issueTypes
	 * @param projectType
	 * @param issueTypes
	 * issueTypes
	 */
	public List<TPstateBean> loadByProjectTypeAndIssueTypes(Integer projectType, List<Integer> issueTypes);
	
	/**
	 * Load TPstateBeans by project types and issueType
	 * @param projectType
	 * @param issueTypes
	 * issueTypes
	 */
	public List<TPstateBean> loadByProjectTypeAndIssueType(Integer projectType, Integer issueType);
	public List<TPstateBean> loadByProjectType(Integer projectType);

	/**
	 * Save  TPstateBean in the TPState table
	 * @param pstateBean
	 * @return
	 */
	Integer save(TPstateBean pstateBean);
	
	/**
	 * Deletes a TPstateBean from the TPstate table 
	 * @param objectID
	 * @return
	 */
	void delete(Integer objectID);
	
	/**
	 * Deletes TPstateBean(s) from the TPstate table 
	 * @param projectTypeID
	 * @param itemTypeID
	 * @param statusIDs
	 */
	void delete(Integer projectTypeID, Integer itemTypeID, List<Integer> statusIDs);
}