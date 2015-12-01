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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TNavigatorColumnBean;
import com.aurel.track.dao.NavigatorColumnDAO;

/**
 * column in item navigator
 *
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TNavigatorColumnPeer
    extends com.aurel.track.persist.BaseTNavigatorColumnPeer implements NavigatorColumnDAO
{
	private static final long serialVersionUID = 500L;
	static private Logger LOGGER = LogManager.getLogger(TNavigatorColumnPeer.class);
	
	/**
	 * Loads the fields for a layout
	 * @param navigatorLayoutID
	 * @return
	 */
	@Override
	public TNavigatorColumnBean loadByPrimaryKey(Integer navigatorColumnID) {
		TNavigatorColumn tNavigatorColumn = null;
		try {
			tNavigatorColumn = retrieveByPK(navigatorColumnID);
		} catch(Exception e) {
			LOGGER.info("Loading of a navigator colum by primary key " + navigatorColumnID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} 
		if (tNavigatorColumn!=null) {
			return tNavigatorColumn.getBean();
		}
		return null;
	}
	
	/**
	 * Loads the fields for a layout
	 * @param navigatorLayoutID
	 * @return
	 */
	@Override
	public List<TNavigatorColumnBean> loadByLayout(Integer navigatorLayoutID) {
		Criteria crit = new Criteria();
		crit.add(NAVIGATORLAYOUT, navigatorLayoutID);
		crit.addAscendingOrderByColumn(FIELDPOSITION);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading navigator fields for layout "+ navigatorLayoutID + " failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Loads the column for a field in a layout
	 * @param navigatorLayoutID
	 * @param fieldID
	 * @return
	 */
	@Override
	public List<TNavigatorColumnBean> loadByLayoutAndField(Integer navigatorLayoutID, Integer fieldID) {
		Criteria crit = new Criteria();
		crit.add(NAVIGATORLAYOUT, navigatorLayoutID);
		crit.add(FIELD, fieldID);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading navigator fields for layout "+ navigatorLayoutID + " failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Saves a navigator field in layout
	 * @param navigatorLayoutBean
	 * @return
	 */
	@Override
	public Integer save(TNavigatorColumnBean navigatorFieldsBean) {
		try {
			TNavigatorColumn navigatorColumn = TNavigatorColumn.createTNavigatorColumn(navigatorFieldsBean);
			navigatorColumn.save();
			Integer objectID = navigatorColumn.getObjectID();
			navigatorFieldsBean.setObjectID(objectID);
			return objectID;
		} catch (Exception e) {
			LOGGER.error("Saving of a navigator field failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return null;
		}
	}
	
	/**
	 * Deletes a field from layout
	 * @param  objectID
	 * @return
	 */
	@Override
	public void delete(Integer objectID) {
		Criteria crit = new Criteria();
		crit.add(OBJECTID, objectID);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the navigator column " + objectID + " failed with: " + e);
		}
	}
	
	/**
	 * Deletes all fields from a layout
	 * @param layoutID
	 * @return
	 */
	@Override
	public void deleteByLayout(Integer layoutID) {
		Criteria crit = new Criteria();
		crit.add(NAVIGATORLAYOUT, layoutID);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the columns for layoutID " + layoutID + " failed with: " + e);
		}
	}
	
	private List<TNavigatorColumnBean> convertTorqueListToBeanList(List<TNavigatorColumn> torqueList) {
		List<TNavigatorColumnBean> beanList = new ArrayList<TNavigatorColumnBean>();
		if (torqueList!=null){
			Iterator<TNavigatorColumn> itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				beanList.add(itrTorqueList.next().getBean());
			}
		}
		return beanList;
	}
}
