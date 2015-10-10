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

package com.aurel.track.screen.adapterDAO;

import java.util.List;

import com.aurel.track.beans.screen.IScreen;

/**
 * 
 */
public interface IScreenDAO {
    
	/**
	 * Gets a screenBean from the TScreen table by primary key
	 * @param objectID
	 * @return
	 */
	IScreen loadByPrimaryKey(Integer objectID);
	
	/**
	 * Gets a screenBean from the TScreen table by primary key
	 * @param objectID
	 * @return
	 */
	IScreen tryToLoadByPrimaryKey(Integer objectID);

	/**
	 * Gets a screenBean from the TScreen table by person key
	 * @param objectID
	 * @return
	 */
    IScreen loadByPerson(Integer pk);

    /**
     * Gets the default screenBean from the TScreen table 
     * (that with no person)
     * @return
     */
    IScreen loadDefault();
    
    /**
	 * Loads all fields from TScreen table
	 * @return
	 */
	List loadAll();
	/**
	 * Load all screen order by given column ascending or not
	 * @param sortKey
	 * @param ascending
	 * @return
	 */
	List loadAllOrdered(String sortKey,boolean ascending);

	/**
	 * Save  screen in the TScreen table
	 * @param screen
	 * @return
	 */
	Integer save(IScreen screen);


	/**
	 * Deletes a screen from the TScreen table
	 * Is deletable should return true before calling this method
	 * @param objectID
	 */
	void delete(Integer objectID);


	/**
	 * Verifies whether a screen is deletable
	 * @param objectID
	 */
	boolean isDeletable(Integer objectID);
}
