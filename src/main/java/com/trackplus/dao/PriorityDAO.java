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

import com.trackplus.model.Tpriority;

/**
 * DAO for Priority
 * 
 * @author Tamas Ruff
 * 
 */
public interface PriorityDAO {
	/**
	 * Loads a priorityBean by primary key
	 * 
	 * @param objectID
	 * @return
	 */
	Tpriority loadByPrimaryKey(Integer objectID);

	/**
	 * Gets priorityBeans by label
	 * 
	 * @param label
	 * @return
	 */
	List<Tpriority> loadByLabel(String label);

	/**
	 * Loads all priorityBeans
	 * 
	 * @return
	 */
	List<Tpriority> loadAll();

	/**
	 * Loads the priorities by IDs
	 * 
	 * @param priorityIDs
	 */
	List<Tpriority> loadByPriorityIDs(List<Integer> priorityIDs);

	/**
	 * Loads the priorityBeans for a project and issueType
	 * 
	 * @param projectID
	 * @param issueType
	 * @return
	 */
	List<Tpriority> loadByProjectAndIssueType(Integer projectID,
			Integer issueType);

	/**
	 * Gets the next available sortorder
	 * 
	 * @return
	 */
	Integer getNextSortOrder();

	/**
	 * Saves a priorityBean in the TPriority table
	 * 
	 * @param priorityBean
	 * @return
	 */
	Integer save(Tpriority priorityBean);

	/**
	 * Whether the priority is used as foreign key
	 * 
	 * @param objectID
	 * @return
	 */
	boolean hasDependentData(Integer objectID);

	/**
	 * Deletes a priorityBean from the TPriority table
	 * 
	 * @param objectID
	 */
	void delete(Integer objectID);

	/**
	 * Replaces the dependences with a newPriorityID and deletes the
	 * oldPriorityID from the TPriority table
	 * 
	 * @param oldPriorityID
	 * @param newPriorityID
	 */
	void replace(Integer oldPriorityID, Integer newPriorityID);

	/**
	 * Returns the sort order column name
	 * 
	 * @return
	 */
	String getSortOrderColumn();

	/**
	 * Returns the table name
	 * 
	 * @return
	 */
	String getTableName();

}
