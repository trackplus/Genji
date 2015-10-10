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

import com.aurel.track.beans.TChildIssueTypeBean;

public interface ChildIssueTypeDAO {

	/**
	 * Loads all TChildIssueTypeBean 
	 * @return
	 */
	List<TChildIssueTypeBean> loadAll();
	
	/**
	 * Load the TChildIssueTypeBean for childIssueTypes (changed rows)
	 * @param childIssueTypes
	 * @return
	 */
	List<TChildIssueTypeBean> loadByChildAssignments(List<Integer> childIssueTypes);
	
	/**
	 * Load the TChildIssueTypeBean for a parentIssueType 
	 * @param parentIssueType
	 * @return
	 */
	List<TChildIssueTypeBean> loadByChildAssignmentsByParent(Integer parentIssueType);
	
	/**
	 * Save  TChildIssueTypeBean in the TChildIssueType table
	 * @param childIssueTypeBean
	 * @return
	 */
	Integer save(TChildIssueTypeBean childIssueTypeBean);
	
	/**
	 * Deletes a TChildIssueTypeBean from the TChildIssueType table 
	 * @param objectID
	 * @return
	 */
	void delete(Integer objectID);
	
	/**
	 * Deletes TChildIssueTypeBean by parent and children 
	 * @param parentIssueTypeID
	 * @param childIssueTypeIDs
	 * @return
	 */
	void delete(Integer parentIssueTypeID, List<Integer> childIssueTypeIDs);
}
