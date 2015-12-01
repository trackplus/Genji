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

import com.aurel.track.beans.TWorkflowGuardBean;
import com.aurel.track.dao.WorkflowGuardDAO;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Criteria.Criterion;

import java.util.LinkedList;
import java.util.List;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TWorkflowGuardPeer
    extends com.aurel.track.persist.BaseTWorkflowGuardPeer implements WorkflowGuardDAO {
	public static final long serialVersionUID = 400L;
	private static final Logger LOGGER = LogManager.getLogger(TWorkflowGuardPeer.class);

	@Override
	public TWorkflowGuardBean loadByPrimaryKey(Integer objectID) {
		TWorkflowGuard tobject = null;
		try{
			tobject = retrieveByPK(objectID);
		}
		catch(Exception e){
			LOGGER.warn("Loading of a workflow guard by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (tobject!=null){
			return tobject.getBean();
		}
		return null;
	}

	@Override
	public List<TWorkflowGuardBean> loadAll() {
		Criteria crit = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all workflow guards failed with " + e.getMessage());
			return null;
		}
	}

	@Override
	public List<TWorkflowGuardBean> loadByTransition(Integer transitionID) {
		Criteria crit = new Criteria();
		crit.add(WORKFLOWTRANSITION,transitionID);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading workflow guards by transition:"+transitionID+" failed with " + e.getMessage());
			return null;
		}
	}

	/**
	 * Load all guards linked with transitions starting from stationFrom
	 * @param stationFrom
	 * @param triggerEvent
	 * @param includeHooks whether to include the transitions to the same station
	 * @return
	 */
	@Override
	public List<TWorkflowGuardBean> loadFromStation(Integer stationFrom, Integer triggerEvent, boolean includeHooks) {
		Criteria crit = new Criteria();
		crit.addJoin(WORKFLOWTRANSITION, TWorkflowTransitionPeer.OBJECTID);
		crit.add(TWorkflowTransitionPeer.STATIONFROM, stationFrom);
		if (!includeHooks) {
			crit.add(TWorkflowTransitionPeer.STATIONTO, stationFrom, Criteria.NOT_EQUAL);
		}
		Criterion actionKeyIs = crit.getNewCriterion(TWorkflowTransitionPeer.ACTIONKEY, triggerEvent, Criteria.EQUAL);
		Criterion actionKeyIsNull = crit.getNewCriterion(TWorkflowTransitionPeer.ACTIONKEY, null, Criteria.ISNULL);
		crit.add(actionKeyIs.or(actionKeyIsNull));
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading workflow guards for transitions starting from:"+stationFrom+" failed with " + e.getMessage());
			return null;
		}
	}
	
	@Override
	public Integer save(TWorkflowGuardBean workflowGuardBean) {
		try {
			TWorkflowGuard tobject = TWorkflowGuard.createTWorkflowGuard(workflowGuardBean);
			tobject.save();
			return tobject.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a workflow guard failed with " + e.getMessage());
			return null;
		}
	}

	@Override
	public void delete(Integer objectID) {
		try {
			doDelete(SimpleKey.keyFor(objectID));
		} catch (TorqueException e) {
			LOGGER.error("Deleting a workflow guard for key " + objectID + " failed with: " + e);
		}
	}

	private List<TWorkflowGuardBean> convertTorqueListToBeanList(List<TWorkflowGuard> torqueList) {
		List<TWorkflowGuardBean> beanList = new LinkedList<TWorkflowGuardBean>();
		if (torqueList!=null){
			for (TWorkflowGuard workflowGuard : torqueList) {
				beanList.add(workflowGuard.getBean());
			}
		}
		return beanList;
	}
}
