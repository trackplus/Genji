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

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Criteria.Criterion;

import com.aurel.track.beans.TWorkflowTransitionBean;
import com.aurel.track.dao.WorkflowTransitionDAO;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TWorkflowTransitionPeer
    extends com.aurel.track.persist.BaseTWorkflowTransitionPeer implements WorkflowTransitionDAO {
	public static final long serialVersionUID = 400L;
	private static final Logger LOGGER = LogManager.getLogger(TWorkflowTransitionPeer.class); 
	
	private static Class[] deletePeerClasses = {
		TWorkflowActivityPeer.class,
		TWorkflowGuardPeer.class,
		TItemTransitionPeer.class,
		BaseTWorkflowTransitionPeer.class
	};

	private static String[] deleteFields = {
		TWorkflowActivityPeer.TRANSITIONACTIVITY,
		TWorkflowGuardPeer.WORKFLOWTRANSITION,
		TItemTransitionPeer.TRANSITION,
		BaseTWorkflowTransitionPeer.OBJECTID
	};
	
	/**
	 * Deletes the TReleases satisfying a certain criteria 
	 * together with the dependent database entries 
	 * @param crit
	 */
	public static void doDelete(Criteria crit) {
		try {
			List<TWorkflowTransition> workflowTransitionList = doSelect(crit);
			if (workflowTransitionList == null || workflowTransitionList.isEmpty()) {
				return;
			}
			for (TWorkflowTransition tWorkflowTransition : workflowTransitionList) {
				ReflectionHelper.delete(deletePeerClasses, deleteFields, tWorkflowTransition.getObjectID());
			}
		} catch (TorqueException e) {
			LOGGER.error("Deleting the workflow transitions by criteria " + crit.toString() + " failed with " + e.getMessage(), e);
		}
	}

	/**
	 * Loads the station by primary key
	 * @param objectID
	 * @return
	 */
	public TWorkflowTransitionBean loadByPrimaryKey(Integer objectID) {
		TWorkflowTransition tobject = null;
		try{
			tobject = retrieveByPK(objectID);
		}
		catch(Exception e){
			LOGGER.warn("Loading of a workflow transition by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (tobject!=null){
			return tobject.getBean();
		}
		return null;
	}

	/**
	 * Loads all workflow transitions
	 * @return
	 */
	public List<TWorkflowTransitionBean> loadAll() {
		Criteria crit = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all workflow transitions failed with " + e.getMessage(), e);
			return null;
		}
	}
	public List<TWorkflowTransitionBean> loadByWorkflow(Integer workflowID){
		Criteria crit = new Criteria();
		crit.add(WORKFLOW,workflowID);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading workflow transitions by workflow:"+workflowID+" failed with " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Loads all transitions with transition event
	 * @param transitionEvents
	 * @param workflowIDs
	 * @return
	 */
	public List<TWorkflowTransitionBean> loadByTransitionEvent(Integer[] transitionEvents, Object[] workflowIDs) {
		Criteria crit = new Criteria();
		crit.addIn(ACTIONKEY, transitionEvents);
		if (workflowIDs==null || workflowIDs.length==0) {
			return null;
		}
		crit.addIn(WORKFLOW, workflowIDs);
		//respect the chronological order of transitions if there are more 
		crit.addAscendingOrderByColumn(TIMEUNIT);
		crit.addAscendingOrderByColumn(ELAPSEDTIME);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading workflow transitions by transitionEvent " + transitionEvents + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Loads all transitions for a workflow and states
	 * @param workflowID
	 * @param stateFrom
	 * @param stateTo
	 * @param actionID if it is a direct status change then actionID is null 
	 * @return
	 */
	public List<TWorkflowTransitionBean> loadByWorkflowAndStates(Integer workflowID, Integer stateFrom, Integer stateTo, Integer actionID) {
		Criteria crit = new Criteria();
		String stationFrom = "STATIONFROMTABLE";
		String stationTo = "STATIONTOTABLE";
		crit.addAlias(stationFrom, TWorkflowStationPeer.TABLE_NAME);
		crit.addAlias(stationTo, TWorkflowStationPeer.TABLE_NAME);
		crit.add(WORKFLOW,workflowID);
		crit.addJoin(stationFrom + ".OBJECTID", STATIONFROM);
		crit.addJoin(stationTo + ".OBJECTID", STATIONTO);
		crit.add(stationFrom + ".STATUS", stateFrom);
		crit.add(stationTo + ".STATUS", stateTo);
		/*if (actionID==null) {
			crit.add(ACTIONKEY, (Object)null, Criteria.ISNULL);
		} else {
			crit.add(ACTIONKEY, actionID);
		}*/
		Criterion actionKeyIs = crit.getNewCriterion(ACTIONKEY, actionID, Criteria.EQUAL);
		Criterion actionKeyIsNull = crit.getNewCriterion(ACTIONKEY, null, Criteria.ISNULL);
		crit.add(actionKeyIs.or(actionKeyIsNull));
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading workflow transitions by workflow " + workflowID + " stateFrom " + stateFrom +
					" stateTo " + stateTo + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Loads all transitions for a workflow from the pseudo create
	 * @param workflowID
	 * @param stationType
	 * @param stateTo
	 * @param actionID if it is a direct status change then actionID is null 
	 * @return
	 */
	public List<TWorkflowTransitionBean> loadFromCreate(Integer workflowID, Integer stationType, Integer stateTo, Integer actionID) {
		Criteria crit = new Criteria();
		String stationFrom = "STATIONFROMTABLE";
		String stationTo = "STATIONTOTABLE";
		crit.addAlias(stationFrom, TWorkflowStationPeer.TABLE_NAME);
		crit.addAlias(stationTo, TWorkflowStationPeer.TABLE_NAME);
		crit.add(WORKFLOW,workflowID);
		crit.addJoin(stationFrom + ".OBJECTID", STATIONFROM);
		crit.addJoin(stationTo + ".OBJECTID", STATIONTO);
		crit.add(stationFrom + ".STATIONTYPE", stationType);
		if (stateTo!=null) {
			crit.add(stationTo + ".STATUS", stateTo);
		}
		Criterion actionKeyIs = crit.getNewCriterion(ACTIONKEY, actionID, Criteria.EQUAL);
		Criterion actionKeyIsNull = crit.getNewCriterion(ACTIONKEY, null, Criteria.ISNULL);
		crit.add(actionKeyIs.or(actionKeyIsNull));
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading workflow transitions by workflow " + workflowID + " stationType " + stationType +
					" stateTo " + stateTo + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Load all possible transitions from fromStation in workflow with an escalationEvent
	 * @param fromStation
	 * @param triggerEvent
	 * @param includeHooks whether to include the transitions to the same station
	 * @return
	 */
	public List<TWorkflowTransitionBean> loadFromStation(Integer fromStation, Integer triggerEvent, boolean includeHooks) {
		Criteria crit = new Criteria();
		crit.add(STATIONFROM, fromStation);
		if (!includeHooks) {
			crit.add(STATIONTO, fromStation, Criteria.NOT_EQUAL);
		}
		Criterion actionKeyIs = crit.getNewCriterion(ACTIONKEY, triggerEvent, Criteria.EQUAL);
		Criterion actionKeyIsNull = crit.getNewCriterion(ACTIONKEY, null, Criteria.ISNULL);
		crit.add(actionKeyIs.or(actionKeyIsNull));
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading workflow transitions from station " + fromStation + " for escaltionEvent " + triggerEvent + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Loads all possible transitions from fromState in workflow
	 * @param workflowID 
	 * @param fromState
	 * @return
	 */
	/*public List<TWorkflowTransitionBean> loadFromState(Integer workflowID, Integer fromState) {
		Criteria crit = new Criteria();
		crit.add(WORKFLOW, workflowID);
		crit.addJoin(STATIONFROM, TWorkflowStationPeer.OBJECTID);
		crit.add(TWorkflowStationPeer.STATUS, fromState);
		crit.add(ACTIONKEY, (Object)null, Criteria.ISNULL);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading workflow transitions for workflow " + workflowID +
					" from state " + fromState + " failed with " + e.getMessage(), e);
			return null;
		}
	}*/
	
	/**
	 * Loads all possible transitions from stations of stationType in workflow
	 * @param workflowID 
	 * @param stationType
	 * @return
	 */
	/*public List<TWorkflowTransitionBean> loadFromStationType(Integer workflowID, Integer stationType) {
		Criteria crit = new Criteria();
		crit.add(WORKFLOW, workflowID);
		crit.addJoin(STATIONFROM, TWorkflowStationPeer.OBJECTID);
		crit.add(TWorkflowStationPeer.STATIONTYPE, stationType);
		crit.add(ACTIONKEY, (Object)null, Criteria.ISNULL);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading workflow transitions for workflow " + workflowID +
					" from stationType " + stationType + " failed with " + e.getMessage(), e);
			return null;
		}
	}*/
	
	/**
	 * Gets the workflow transitions starting from the pseudo create station
	 * @param workflowID
	 * @param stationType
	 * @return
	 */
	/*public List<TWorkflowTransitionBean> loadFromPseudoCreateStation(Integer workflowID, Integer stationType) {
		Criteria crit = new Criteria();
		crit.add(WORKFLOW, workflowID);
		crit.addJoin(STATIONFROM, TWorkflowStationPeer.OBJECTID);
		crit.add(TWorkflowStationPeer.STATIONTYPE, stationType);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading workflow transitions from pseudio create station for workflow "+workflowID+" failed with " + e.getMessage(), e);
			return null;
		}
	}*/
	
	/**
	 * Whether workflow contains stationID (either as from or to)
	 * @param stationID
	 * @return
	 */
	public boolean stationInWorkflow(Integer workflowID, Integer stationID) {
		Criteria crit = new Criteria();
		crit.add(WORKFLOW, workflowID);
		Criterion crtStateFrom = crit.getNewCriterion(STATIONFROM, stationID, Criteria.EQUAL);
		Criterion crtStateTo = crit.getNewCriterion(STATIONTO, stationID, Criteria.EQUAL);
		crit.add(crtStateFrom.or(crtStateTo));
		try {
			List<TWorkflowTransition> transitions = doSelect(crit);
			return transitions!=null && !transitions.isEmpty();
		} catch (TorqueException e) {
			LOGGER.error("Does workflow transitions in workflow " + workflowID + " contains station "+stationID+" failed with " + e.getMessage(), e);
			return false;
		}
	}

	
	/**
	 * Saves a workflow
	 * @param workflowTransitionBean
	 * @return
	 */
	public Integer save(TWorkflowTransitionBean workflowTransitionBean) {
		try {
			TWorkflowTransition tobject = TWorkflowTransition.createTWorkflowTransition(workflowTransitionBean);
			tobject.save();
			return tobject.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a workflow transition failed with " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Deletes a screen by primary key
	 * Is deletable should return true before calling this method
	 * @param objectID
	 */
	public void delete(Integer objectID) {
		ReflectionHelper.delete(deletePeerClasses, deleteFields, objectID);
	}

	private List<TWorkflowTransitionBean> convertTorqueListToBeanList(List<TWorkflowTransition> torqueList) {
		List<TWorkflowTransitionBean> beanList = new LinkedList<TWorkflowTransitionBean>();
		if (torqueList!=null){
			for (TWorkflowTransition workflowTransition : torqueList) {
				beanList.add(workflowTransition.getBean());
			}
		}
		return beanList;
	}
}
