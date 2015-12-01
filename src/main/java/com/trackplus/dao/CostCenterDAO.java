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

import com.trackplus.model.Tcostcenter;

/**
 * DAO for Costcenter
 * 
 * @author Tamas Ruff
 * 
 */
public interface CostCenterDAO {

	/**
	 * Loads a cost center by primary key
	 * 
	 * @param objectID
	 * @return
	 */
	Tcostcenter loadByPrimaryKey(Integer objectID);

	/**
	 * Loads cost centers by label
	 * 
	 * @param number
	 * @return
	 */
	List<Tcostcenter> loadByNumber(String number);

	/**
	 * Gets all costcenters
	 * 
	 * @return
	 */
	List<Tcostcenter> loadAll();

	/**
	 * Saves an costCenter to the TCostCenter table.
	 * 
	 * @param costCenter
	 * @return
	 */
	Integer save(Tcostcenter costCenter);

	/**
	 * Deletes a costcenter from the database
	 * 
	 * @param objectID
	 * @return
	 */
	public void delete(Integer objectID);

	/**
	 * Returns whether the costcenter has dependent data
	 * 
	 * @param objectID
	 * @return
	 */
	boolean hasDependentData(Integer objectID);

	/**
	 * Replaces all occurrences of the oldID with the newID and then deletes the
	 * oldID costcenter
	 * 
	 * @param oldID
	 * @param newID
	 * @return
	 */
	void replaceAndDelete(Integer oldID, Integer newID);
}
