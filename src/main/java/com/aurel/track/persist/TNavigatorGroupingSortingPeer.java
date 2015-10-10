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

package com.aurel.track.persist;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TNavigatorGroupingSortingBean;
import com.aurel.track.dao.NavigatorGroupingSortingDAO;
import com.aurel.track.fieldType.constants.BooleanFields;

/**
 * grouping and sorting in item navigator
 *
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TNavigatorGroupingSortingPeer
    extends com.aurel.track.persist.BaseTNavigatorGroupingSortingPeer implements NavigatorGroupingSortingDAO
{
	private static final long serialVersionUID = 500L;
	private static final Logger LOGGER = LogManager.getLogger(TNavigatorGroupingSortingPeer.class);
	/**
	 * Loads the fields for a layout
	 * @param navigatorLayoutID
	 * @return
	 */
	public List<TNavigatorGroupingSortingBean> loadByLayout(Integer navigatorLayoutID) {
		Criteria crit = new Criteria();
		crit.add(NAVIGATORLAYOUT, navigatorLayoutID);
		crit.addAscendingOrderByColumn(SORTPOSITION);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading grouping/sorting for layout "+ navigatorLayoutID + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	/**
	 * Loads the sorting bean for a layout
	 * @param navigatorLayoutID
	 * @return
	 */
	public List<TNavigatorGroupingSortingBean> loadSortingByLayout(Integer navigatorLayoutID) {
		Criteria crit = new Criteria();
		crit.add(NAVIGATORLAYOUT, navigatorLayoutID);
		crit.add(ISGROUPING, BooleanFields.FALSE_VALUE);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading sorting for layout "+ navigatorLayoutID + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Saves a grouping/sorting bean in layout
	 * @param navigatorGroupingSortingBean
	 * @return
	 */
	public Integer save(TNavigatorGroupingSortingBean navigatorGroupingSortingBean) {
		try {
			TNavigatorGroupingSorting navigatorGroupingSorting = BaseTNavigatorGroupingSorting.createTNavigatorGroupingSorting(navigatorGroupingSortingBean);
			navigatorGroupingSorting.save();
			Integer objectID = navigatorGroupingSorting.getObjectID();
			navigatorGroupingSortingBean.setObjectID(objectID);
			return objectID;
		} catch (Exception e) {
			LOGGER.error("Saving of a sorting/grouping field failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return null;
		}
	}
	
	/**
	 * Removes a grouping/sorting from layout
	 * @param  objectID
	 * @return
	 */
	public void delete(Integer objectID) {
		Criteria crit = new Criteria();
		crit.add(OBJECTID, objectID);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the sorting/grouping field " + objectID + " failed with: " + e);
		}
	}
	
	/**
	 * Deletes all grouping/sorting from layout
	 * @param layoutID
	 * @return
	 */
	public void deleteByLayout(Integer layoutID) {
		Criteria crit = new Criteria();
		crit.add(NAVIGATORLAYOUT, layoutID);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the groups/sorts for layoutID " + layoutID + " failed with: " + e);
		}
	}
	
	private List<TNavigatorGroupingSortingBean> convertTorqueListToBeanList(List<TNavigatorGroupingSorting> torqueList) {
		List<TNavigatorGroupingSortingBean> beanList = new ArrayList<TNavigatorGroupingSortingBean>();
		if (torqueList!=null) {
			for (TNavigatorGroupingSorting navigatorGroupingSorting : torqueList) {
				beanList.add(navigatorGroupingSorting.getBean());
			}
		}
		return beanList;
	}
}
