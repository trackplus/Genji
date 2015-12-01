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

import com.trackplus.model.Tscreen;

/**
 * DAO for Screen
 * 
 * @author Adrian Bojani
 * 
 */
public interface ScreenDAO {

	/**
	 * Gets a screenBean from the TScreen table
	 * 
	 * @param objectID
	 * @return
	 */

	Tscreen loadByPrimaryKey(Integer objectID);

	/**
	 * Load the screenBeans by primary keys
	 * 
	 * @param screenIDs
	 * @return
	 */
	List<Tscreen> loadByKeys(List<Integer> screenIDs);

	/**
	 * Gets a screenBean from the TScreen table with all tabs, panels and fields
	 * 
	 * @param objectID
	 * @return
	 **/

	Tscreen loadFullByPrimaryKey(Integer objectID);

	/**
	 * Loads all fields from TScreen table
	 * 
	 * @return
	 */
	List<Tscreen> loadAll();

	/**
	 * Load all screeen oreder by given column ascending or not
	 * 
	 * @param sortKey
	 * @param ascending
	 * @return
	 */
	List<Tscreen> loadAllOrdered(String sortKey, boolean ascending);

	/**
	 * Save screen in the TScreen table
	 * 
	 * @param screen
	 * @return
	 */
	Integer save(Tscreen screen);

	/**
	 * Deletes a screen from the TScreen table Is deletable should return true
	 * before calling this method
	 * 
	 * @param objectID
	 */
	void delete(Integer objectID);

	/**
	 * Verifies whether a screen is deletable
	 * 
	 * @param objectID
	 */
	boolean isDeletable(Integer objectID);

}
