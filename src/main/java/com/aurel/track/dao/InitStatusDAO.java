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

import com.aurel.track.beans.TInitStateBean;

/**
 * This is the DAO for initial statuses
 * @author Tamas Ruff
 *
 */
public interface InitStatusDAO {
	
	/**
	 * Loads the initial states by project
	 * @param projectID
	 * @return
	 */
	List<TInitStateBean> loadByProject(Integer projectID);
	
	
	/**
	 * Loads the initial states by project and issueType
	 * @param projectID
	 * @param issueTypeID
	 * @return
	 */
	List<TInitStateBean> loadByProjectAndIssueType(Integer projectID, Integer issueTypeID);
	
	/**
	 * Loads the initial states by project and issueTypes
	 * @param projectID
	 * @param issueTypeIDs
	 * @return
	 */
	List<TInitStateBean> loadByProjectAndIssueTypes(Integer projectID, List<Integer> issueTypeIDs);
	
	
	/**
	 * Saves an initial state .
	 * @param initStateBean
	 * @return
	 */
	Integer save(TInitStateBean initStateBean);
	
	
	/**
	 * Deletes a init state by objectID
	 * @param objectID
	 */
	void delete(Integer objectID);
	
}
