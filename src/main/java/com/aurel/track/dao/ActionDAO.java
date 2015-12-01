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


package com.aurel.track.dao;

import java.util.List;

import com.aurel.track.beans.TActionBean;

/**
 * This is the DAO for actions. Actions describe what a user can do, like
 * edit, copy, create new issue, add child, etc.
 * @author Adrian Bojani
 *
 */
public interface ActionDAO {
	/**
	 * Gets a {@link TActionBean} from the TAction table.
	 * @param actionOID the actions object identifier
	 * @return
	 */
	TActionBean loadByPrimaryKey(Integer actionOID);
    
    /**
	 * Loads all fields from TAction table. 
	 * @return list with all {@link TActionBean} objects.
	 */
	List<TActionBean> loadAll();
	
	/**
	 * Load {@link TActionBean} list by actionIDs
	 * @param actionIDs
	 * @return
	 */
	List<TActionBean> loadByActionIDs(List<Integer> actionIDs);
	
	/**
	 * Saves an action to the TAction table.
	 * @param action
	 * @return
	 */
	Integer save(TActionBean action);
	
	
	/**
	 * Deletes an action from the TAction table.
	 * Is deletable should return true before calling this method.
	 * @param actionOID the actions object identifier
	 */
	void delete(Integer actionOID);
		
		
	/**
	 * Verifies whether an action is deletable.
	 * @param actionOID the actions object identifier
	 */
	boolean isDeletable(Integer actionOID);
}
