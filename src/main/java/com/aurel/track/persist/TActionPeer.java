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
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TActionBean;
import com.aurel.track.dao.ActionDAO;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TActionPeer
extends com.aurel.track.persist.BaseTActionPeer implements ActionDAO{

	private static final Logger LOGGER = LogManager.getLogger(TActionPeer.class);
	public static final long serialVersionUID = 400L;
		
	private static Class[] deletePeerClasses = {
		TWorkflowTransitionPeer.class,
		TScreenConfigPeer.class,
		TActionPeer.class
	};

	private static String[] deleteFields = {
		TWorkflowTransitionPeer.ACTIONKEY,
		TScreenConfigPeer.ACTIONKEY,
		TActionPeer.OBJECTID
	};

	/**
	 * Loads the action by primary key
	 * @param objectID
	 * @return
	 */
	@Override
	public TActionBean loadByPrimaryKey(Integer objectID)  {
		TAction tobject = null;
		try{
			tobject = retrieveByPK(objectID);
		}
		catch(Exception e){
			LOGGER.warn("Loading of a action by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} 
		if (tobject!=null){
			return tobject.getBean();
		}
		return null;
	}
	/**
	 * Loads all actions from TAction table 
	 * @return
	 */
	@Override
	public List<TActionBean> loadAll() {
		Criteria crit = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all actions failed with " + e.getMessage());
			return null;
		}
	}

	/**
	 * Load {@link TActionBean} list by actionIDs
	 * @param actionIDs
	 * @return
	 */
	@Override
	public List<TActionBean> loadByActionIDs(List<Integer> actionIDs) {
		if (actionIDs==null || actionIDs.isEmpty()) {
			return new LinkedList<TActionBean>();
		}
		Criteria crit = new Criteria();
		crit.addIn(OBJECTID, actionIDs);
		crit.addAscendingOrderByColumn(LABEL);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading the actions by keys " + actionIDs + " failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Saves a action in the TAction table
	 * @param bean
	 * @return
	 */
	@Override
	public Integer save(TActionBean bean){
		try {
			TAction tobject = BaseTAction.createTAction(bean);
			tobject.save();
			return tobject.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a action failed with " + e.getMessage());
			return null;
		}
	}

	/**
	 * Deletes a action by primary key
	 * isDeletable should return true before calling this method
	 * @param objectID
	 */
	@Override
	public void delete(Integer objectID) {
		ReflectionHelper.delete(deletePeerClasses, deleteFields, objectID);
	}

	/**
	 * Verify is a action can be delete 
	 * @param objectID
	 */
	@Override
	public boolean isDeletable(Integer objectID){
		return false;
	}

	/**
	 * Converts the torque object list to bean list
	 * @param torqueList
	 * @return
	 */
	private List<TActionBean> convertTorqueListToBeanList(List<TAction> torqueList) {
		List<TActionBean> beanList = new ArrayList<TActionBean>();
		if (torqueList!=null) {
			for (TAction tAction : torqueList) {
				beanList.add(tAction.getBean());
			}
		}
		return beanList;
	}
}
