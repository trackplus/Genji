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

import com.aurel.track.beans.TUserLevelBean;
import com.aurel.track.dao.UserLevelDAO;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TUserLevelPeer
    extends com.aurel.track.persist.BaseTUserLevelPeer implements UserLevelDAO
{
	private static final Logger LOGGER = LogManager.getLogger(TUserLevelPeer.class);
	
	private static Class[] replaceUserLevelPeerClasses = {
		TPersonPeer.class
	};

	private static String[] replaceUserLevelFields = {
		TPersonPeer.USERLEVEL
	};


	private static Class[] deleteUserLevelPeerClasses = {
			TUserLevelSettingPeer.class,
			//delete itself
			TUserLevelPeer.class
		};

	private static String[] deleteUserLevelFields = {
			TUserLevelSettingPeer.USERLEVEL,
			TUserLevelPeer.OBJECTID
		};
	
	/**
	 * Loads all user level beans
	 * @return 
	 */
	@Override
	public List<TUserLevelBean> loadAll() {
		Criteria crit = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading all user levels failed with " + e);
			return null;
		}
	}
	
	/**
	 * Loads a TUserLevelBean by primary key
	 * @param objectID
	 * @return
	 */
	@Override
	public TUserLevelBean loadByPrimaryKey(Integer objectID) {
		TUserLevel tUserLevel = null;
		try {
			tUserLevel = retrieveByPK(objectID);
		} catch(Exception e) {
			LOGGER.info("Loading the user level by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (tUserLevel!=null) {
			return tUserLevel.getBean();
		}
		return null;
	}
	
	/**
	 * Saves a userLevelBean to the TUserLevel table.
	 * @param userLevelBean
	 * @return
	 */
	@Override
	public Integer save(TUserLevelBean userLevelBean) {
		try {
			TUserLevel tUserLevel = TUserLevel.createTUserLevel(userLevelBean);
			tUserLevel.save();
			return tUserLevel.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a user level failed with " + e.getMessage());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			return null;
		}
	}
	
	/**
	 * Whether the user level has dependent data (is assigned to any user)
	 * @param userLevelID
	 * @return
	 */
	@Override
	public boolean hasDependentData(Integer userLevelID) {
		return ReflectionHelper.hasDependentData(replaceUserLevelPeerClasses, replaceUserLevelFields, userLevelID);
	}
	
	/**
	 * Replaces the oldUserLevelID with newUserLevelID 
	 * @param oldUserLevelID
	 * @param newUserLevelID
	 * @return
	 */
	@Override
	public void replace(Integer oldUserLevelID, Integer newUserLevelID) {
		ReflectionHelper.replace(replaceUserLevelPeerClasses, replaceUserLevelFields, oldUserLevelID, newUserLevelID);
	}
	
	/**
	 * Deletes a userLevelID
	 * @param userLevelID
	 * @return
	 */
	@Override
	public void delete(Integer userLevelID) {
		ReflectionHelper.delete(deleteUserLevelPeerClasses, deleteUserLevelFields, userLevelID);
	}
	
	/**
	 * Converts the torque object list to bean list
	 * @param torqueList
	 * @return
	 */
	private static List<TUserLevelBean> convertTorqueListToBeanList(List<TUserLevel> torqueList) {
		List<TUserLevelBean> beanList = new LinkedList<TUserLevelBean>();
		if (torqueList!=null) {
			for (TUserLevel tUserLevel : torqueList) {
				beanList.add(tUserLevel.getBean());
			}
		}
		return beanList;
	}
}
