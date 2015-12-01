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

import com.aurel.track.beans.TUserLevelBean;

/**
 * DAO for Department
 * @author Tamas Ruff
 *
 */
public interface UserLevelDAO {
		
	/**
	 * Loads all user level beans
	 * @return 
	 */
	List<TUserLevelBean> loadAll();
	
	/**
	 * Loads a TUserLevelBean by primary key
	 * @param objectID
	 * @return
	 */
	TUserLevelBean loadByPrimaryKey(Integer objectID);
	
	/**
	 * Saves a userLevelBean to the TUserLevel table.
	 * @param userLevelBean
	 * @return
	 */
	Integer save(TUserLevelBean userLevelBean);
	
	/**
	 * Whether the user level has dependent data (is assigned to any user)
	 * @param userLevelID
	 * @return
	 */
	boolean hasDependentData(Integer userLevelID);
	
	/**
	 * Replaces the oldUserLevelID with newUserLevelID 
	 * @param oldUserLevelID
	 * @param newUserLevelID
	 * @return
	 */
	void replace(Integer oldUserLevelID, Integer newUserLevelID);
	
	/**
	 * Deletes a userLevelID
	 * @param userLevelID
	 * @return
	 */
	void delete(Integer userLevelID);
	
	
}
