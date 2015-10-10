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

package com.aurel.track.admin.user.userLevel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TUserLevelBean;
import com.aurel.track.beans.TUserLevelSettingBean;

/**
 * User level map cache
 * @author Tamas Ruff
 *
 */
public class UserLevelsProxy {

    private static UserLevelsProxy INSTANCE = null;

    public static UserLevelsProxy getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new UserLevelsProxy();
        }
        return INSTANCE;
    }

    private Map<Integer, Map<Integer, Boolean>> userLevelsMap;


    private UserLevelsProxy() {
        this.userLevelsMap = loadUserLevels();
    }

    public Map<Integer, Boolean> getMapByUserLevel(Integer userLevel) {
        return this.userLevelsMap.get(userLevel);
    }

    /**
     * Loads the user levels
     * This is a map of maps. The outer map is for each user level, and the inner
     * map contains the settings for each user level
     * @return
     */
    public synchronized Map<Integer, Map<Integer, Boolean>> loadUserLevels() {
        Map<Integer, Map<Integer, Boolean>> userLevelsMap = new HashMap<Integer, Map<Integer, Boolean>>();
        userLevelsMap.put(TPersonBean.USERLEVEL.CLIENT, UserLevelBL.getEasyUserActionsMap());
        Map<Integer, Boolean> fullUsersMap = UserLevelBL.getFullUserActionsMap();
        userLevelsMap.put(TPersonBean.USERLEVEL.FULL, fullUsersMap);
        userLevelsMap.put(TPersonBean.USERLEVEL.SYSMAN, fullUsersMap);
        userLevelsMap.put(TPersonBean.USERLEVEL.SYSADMIN, fullUsersMap);
        List<TUserLevelSettingBean> allSettings = UserLevelBL.loadAllSettings();
        if (allSettings!=null) {
	        for (TUserLevelSettingBean userLevelSettingBean : allSettings) {
				Integer userLevelID = userLevelSettingBean.getUserLevel();
				Integer actionID = userLevelSettingBean.getACTIONKEY();
				boolean active = userLevelSettingBean.isActive();
				Map<Integer, Boolean> userLevelActionsMap = userLevelsMap.get(userLevelID);
				if (userLevelActionsMap==null) {
					userLevelActionsMap = new HashMap<Integer, Boolean>();
					userLevelsMap.put(userLevelID, userLevelActionsMap);
				}
				userLevelActionsMap.put(actionID, active);
			}
        }
        //ensure that the boolean flag for each actionID is set
        List<TUserLevelBean> customUserLevels = UserLevelBL.loadCustomUserLevels();
        List<Integer> actionIDs = UserLevelBL.getUserLevelActions();
        for (TUserLevelBean userLevelBean : customUserLevels) {
        	Integer userLevelID = userLevelBean.getObjectID();
        	Map<Integer, Boolean> userLevelActionsMap = userLevelsMap.get(userLevelID);
        	if (userLevelActionsMap==null) {
        		//the map was not created if none or the check boxes was set
        		userLevelActionsMap = new HashMap<Integer, Boolean>();
        		userLevelsMap.put(userLevelID, userLevelActionsMap);
        	}
        	for (Integer actionID : actionIDs) {
				if (userLevelActionsMap.get(actionID)==null) {
					//missing actionID in the database is equivalent with restricted action
					//it should be set to avoid error in BorderLayout.jsp
					userLevelActionsMap.put(actionID, Boolean.FALSE);
				}
			}
		}
        this.userLevelsMap = userLevelsMap;
        return userLevelsMap;
    }

}
