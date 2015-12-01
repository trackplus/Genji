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

import com.trackplus.model.Tchildissuetype;

public interface ChildIssueTypeDAO {

	/**
	 * Loads all Tchildissuetype
	 * 
	 * @return
	 */
	List<Tchildissuetype> loadAll();

	/**
	 * Load the Tchildissuetype for childissuetypes (changed rows)
	 * 
	 * @param childIssueTypes
	 * @return
	 */
	List<Tchildissuetype> loadByChildAssignments(List<Integer> childissuetypes);

	/**
	 * Load the Tchildissuetype for a parentissuetype
	 * 
	 * @param parentIssueType
	 * @return
	 */
	List<Tchildissuetype> loadByChildAssignmentsByParent(Integer parentissuetype);

	/**
	 * Save Tchildissuetype in the tchildissuetype table
	 * 
	 * @param childIssueType
	 * @return
	 */
	Integer save(Tchildissuetype childissuetype);

	/**
	 * Deletes a Tchildissuetype from the tchildissuetype table
	 * 
	 * @param objectID
	 * @return
	 */
	void delete(Integer objectID);

	/**
	 * Deletes Tchildissuetype by parent and children
	 * 
	 * @param parentIssueTypeID
	 * @param childIssueTypeIDs
	 * @return
	 */
	void delete(Integer parentIssueTypeID, List<Integer> childIssueTypeIDs);
}
