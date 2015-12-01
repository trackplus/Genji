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

import com.trackplus.model.Tstate;

/**
 * DAO for State
 * 
 * @author Tamas Ruff
 * 
 */
public interface StateDAO {
	/**
	 * Loads a stateBean by primary key
	 * 
	 * @param objectID
	 * @return
	 */
	Tstate loadByPrimaryKey(Integer objectID);

	/**
	 * Gets an stateBean by label
	 * 
	 * @param label
	 * @return
	 */
	List<Tstate> loadByLabel(String label);

	/**
	 * Loads stateBeans by stateIDs
	 * 
	 * @param stateIDs
	 * @return
	 */
	List<Tstate> loadByKeys(Object[] stateIDs);

	/**
	 * Loads all stateBeans
	 * 
	 * @return
	 */
	List<Tstate> loadAll();

	/**
	 * Return the stateBeans with a specific stateFlag
	 * 
	 * @param stateFlag
	 * @return
	 */
	List<Tstate> loadByStateFlag(int stateFlag);

	/**
	 * Loads a list with statusBeans with statuses assigned to at least one
	 * workItem in any of the projects
	 * 
	 * @param projects
	 * @return
	 */
	List<Tstate> loadUsedByProject(Integer[] projects);

	/**
	 * Gets the next available sortorder
	 * 
	 * @return
	 */
	Integer getNextSortOrder();

	/**
	 * Save state in the TState table
	 * 
	 * @param stateBean
	 * @return
	 */
	Integer save(Tstate stateBean);

	/**
	 * Whether the state is used as foreign key
	 * 
	 * @param objectID
	 * @return
	 */
	boolean hasDependentData(Integer objectID);

	/**
	 * Deletes a state from the TState table
	 * 
	 * @param objectID
	 */
	void delete(Integer objectID);

	/**
	 * Replaces the dependences with a newStatusID and deletes the oldStatusID
	 * from the TState table
	 * 
	 * @param oldStatusID
	 * @param newStatusID
	 */
	void replace(Integer oldStatusID, Integer newStatusID);

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
