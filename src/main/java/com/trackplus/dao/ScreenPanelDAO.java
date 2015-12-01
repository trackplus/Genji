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

import com.trackplus.model.Tscreenpanel;

/**
 * DAO for ScreenPanel object
 * 
 * @author Adrian Bojani
 * 
 */
public interface ScreenPanelDAO {
	/**
	 * Gets a screenPanelBean from the TScreenPanel table
	 * 
	 * @param objectID
	 * @return
	 */
	Tscreenpanel loadByPrimaryKey(Integer objectID);

	Tscreenpanel loadFullByPrimaryKey(Integer objectID);

	/**
	 * Loads all fields from TScreenPanel table
	 * 
	 * @return
	 */
	List<Tscreenpanel> loadAll();

	/**
	 * Save screenPanel in the TScreenPanel table
	 * 
	 * @param screenPanel
	 * @return
	 */
	Integer save(Tscreenpanel screenPanel);

	/**
	 * Deletes a screenPanel from the TScreenPanel table Is deletable should
	 * return true before calling this method
	 * 
	 * @param objectID
	 */
	void delete(Integer objectID);

	/**
	 * Verifies whether a screenPanel is deletable
	 * 
	 * @param objectID
	 */
	boolean isDeletable(Integer objectID);

	/**
	 * Loads all panels from parent order by index
	 * 
	 * @return
	 */
	List<Tscreenpanel> loadByParent(Integer parentID);
}
