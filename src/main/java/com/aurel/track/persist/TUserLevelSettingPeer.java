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

package com.aurel.track.persist;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TUserLevelSettingBean;
import com.aurel.track.dao.UserLevelSettingDAO;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TUserLevelSettingPeer
    extends com.aurel.track.persist.BaseTUserLevelSettingPeer implements UserLevelSettingDAO
{
	private static final Logger LOGGER = LogManager.getLogger(TUserLevelSettingPeer.class);
	
	/**
	 * Loads all settings for all user levels
	 * @return 
	 */
	@Override
	public List<TUserLevelSettingBean> loadAll() {
		Criteria crit = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading all user level settings failed with " + e);
			return null;
		}
	}
	
	/**
	 * Loads the settings for a user level
	 * @param userLevelID
	 * @return 
	 */
	@Override
	public List<TUserLevelSettingBean> loadByUserLevel(Integer userLevelID) {
		Criteria crit = new Criteria();
		crit.add(USERLEVEL, userLevelID);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading user level settings by user level " + userLevelID + " failed with " + e);
			return null;
		}
	}
	
	/**
	 * Loads the settings for a user level
	 * @param userLevelID
	 * @param actionID
	 * @return 
	 */
	@Override
	public List<TUserLevelSettingBean> loadByUserLevelAndAction(Integer userLevelID, Integer actionID) {
		Criteria crit = new Criteria();
		crit.add(USERLEVEL, userLevelID);
		crit.add(ACTIONKEY, actionID);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading user level settings by user level " + userLevelID + " and " + actionID + " failed with " + e);
			return null;
		}
	}
	
	/**
	 * Saves a userLevelSetting to the TUuserLevelSetting table.
	 * @param userLevelSettingBean
	 * @return
	 */
	@Override
	public Integer save(TUserLevelSettingBean userLevelSettingBean) {
		try {
			TUserLevelSetting tUserLevelSetting = TUserLevelSetting.createTUserLevelSetting(userLevelSettingBean);
			tUserLevelSetting.save();
			return tUserLevelSetting.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a user level setting failed with " + e.getMessage());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			return null;
		}
	}
	
	/**
	 * Deletes a userLevelSetting by userLevel and actionID
	 * @param userLevelID
	 * @param actionID
	 * @return
	 */
	@Override
	public void delete(Integer userLevelID, Integer actionID) {
		Criteria crit = new Criteria();
		crit.add(USERLEVEL, userLevelID);
		crit.add(ACTIONKEY, actionID);
		try {			
			doDelete(crit);
		} catch (Exception e) {
			log.error("Deleting a TUSERLEVELSETTING by USERLEVEL " +userLevelID + " and " + ACTIONKEY + actionID + " failed with " + e);
		}
	}
	
	/**
	 * Converts the torque object list to bean list
	 * @param torqueList
	 * @return
	 */
	private static List<TUserLevelSettingBean> convertTorqueListToBeanList(List<TUserLevelSetting> torqueList) {
		List<TUserLevelSettingBean> beanList = new LinkedList<TUserLevelSettingBean>();
		if (torqueList!=null) {
			for (TUserLevelSetting tUserLevelSetting : torqueList) {
				beanList.add(tUserLevelSetting.getBean());
			}
		}
		return beanList;
	}
}
