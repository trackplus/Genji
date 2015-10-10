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


package com.aurel.track.dao;

import java.util.List;

import com.aurel.track.beans.TUserLevelSettingBean;

/**
 * DAO for Department
 * @author Tamas Ruff
 *
 */
public interface UserLevelSettingDAO {
		
	/**
	 * Loads all settings for all user levels
	 * @return 
	 */
	List<TUserLevelSettingBean> loadAll();
	
	/**
	 * Loads the settings for a user level
	 * @param userLevelID
	 * @return 
	 */
	List<TUserLevelSettingBean> loadByUserLevel(Integer userLevelID);
	
	/**
	 * Loads the settings for a user level
	 * @param userLevelID
	 * @param actionID
	 * @return 
	 */
	List<TUserLevelSettingBean> loadByUserLevelAndAction(Integer userLevelID, Integer actionID);
	
	/**
	 * Saves a userLevelSetting to the TUuserLevelSetting table.
	 * @param userLevelSettingBean
	 * @return
	 */
	Integer save(TUserLevelSettingBean userLevelSettingBean);
	
	/**
	 * Deletes a userLevelSetting by userLevel and actionID
	 * @param userLevelID
	 * @param actionID
	 * @return
	 */
	void delete(Integer userLevelID, Integer actionID);
	
	
}
