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


package com.trackplus.dao;

import java.util.List;

import com.trackplus.model.Tsystemstate;

/**
 * DAO for SystemState
 * 
 * @author Tamas Ruff
 * 
 */
public interface SystemStateDAO {

	/**
	 * Loads a SystemStateBeans by primary key
	 * 
	 * @param objectID
	 * @return
	 */
	Tsystemstate loadByPrimaryKey(Integer objectID);

	/**
	 * Gets a Tsystemstate by label
	 * 
	 * @param entityFlag
	 * @param label
	 * @return
	 */
	List<Tsystemstate> loadByLabel(Integer entityFlag, String label);

	/**
	 * Gets the label beans with a certain symbol
	 * 
	 * @param listID
	 * @param symbol
	 * @param excludeObjectID
	 * @return
	 */
	List<Tsystemstate> loadBySymbol(Integer entityFlag, String symbol,
			Integer excludeObjectID);

	/**
	 * Loads all SystemStateBeans
	 * 
	 * @return
	 */
	List<Tsystemstate> loadAll();

	/**
	 * List with all available TSystemStatePeer objects according to the
	 * entityFlag
	 * 
	 * @param entityFlag
	 *            defines the entity, the state refers to
	 * @throws Exception
	 */
	List<Tsystemstate> loadAllForEntityFlag(Integer entityFlag);

	/**
	 * List of TSystemStatePeer objects by entityFlag and stateflags
	 * 
	 * @param entityFlag
	 *            defines the entity, the state refers to
	 * @param stateFlags
	 * @throws Exception
	 */
	List<Tsystemstate> loadWithStateFlagForEntity(int entityFlag,
			int[] stateFlags);

	/**
	 * Load by roleIDs
	 * 
	 * @return
	 */
	List<Tsystemstate> loadByStateIDs(List<Integer> primaryKeys);

	/**
	 * Get the status flag of the project
	 * 
	 * @param entityID
	 * @return
	 */
	int getStatusFlag(Integer entityID, int entityFlag);

	/**
	 * Gets the next available sortorder
	 * 
	 * @param entityID
	 * @return
	 */
	Integer getNextSortOrder(Integer entityID);

	/**
	 * Saves a systemState to the Tsystemstate table.
	 * 
	 * @param systemStateBean
	 * @return
	 */
	Integer save(Tsystemstate systemStateBean);

	/**
	 * Whether the system state is used as foreign key
	 * 
	 * @param objectID
	 * @return
	 */
	boolean hasDependentData(Integer objectID);

	/**
	 * Deletes a systemStateBean from the TSystemState table
	 * 
	 * @param objectID
	 */
	void delete(Integer objectID);

	/**
	 * Replaces the dependences with a newSystemStateID and deletes the
	 * oldSystemStateID from the TSystemState table
	 * 
	 * @param oldSystemStateID
	 * @param newSystemStateID
	 */
	void replace(Integer oldSystemStateID, Integer newSystemStateID);

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
