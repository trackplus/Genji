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
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Criteria.Criterion;

import com.aurel.track.beans.TWorkflowStationBean;
import com.aurel.track.dao.WorkflowStationDAO;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TWorkflowStationPeer
    extends com.aurel.track.persist.BaseTWorkflowStationPeer implements WorkflowStationDAO{
	public static final long serialVersionUID = 400L;
	
	private static final Logger LOGGER = LogManager.getLogger(TWorkflowStationPeer.class); 
	
	private static Class[] deletePeerClasses = {
		TWorkflowActivityPeer.class,
		TWorkflowActivityPeer.class,
		TWorkflowActivityPeer.class,
		TWorkflowTransitionPeer.class,
		TWorkflowTransitionPeer.class,
		BaseTWorkflowStationPeer.class
	};

	private static String[] deleteFields = {
		TWorkflowActivityPeer.STATIONENTRYACTIVITY,
		TWorkflowActivityPeer.STATIONEXITACTIVITY,
		TWorkflowActivityPeer.STATIONDOACTIVITY,
		TWorkflowTransitionPeer.STATIONFROM,
		TWorkflowTransitionPeer.STATIONTO,
		BaseTWorkflowStationPeer.OBJECTID
	};
	
	/**
	 * Deletes the TReleases satisfying a certain criteria 
	 * together with the dependent database entries 
	 * @param crit
	 */
	public static void doDelete(Criteria crit) {
		try {
			List<TWorkflowStation> workflowStationList = doSelect(crit);
			if (workflowStationList == null || workflowStationList.isEmpty()) {
				return;
			}
			for (TWorkflowStation tWorkflowStation : workflowStationList) {
				ReflectionHelper.delete(deletePeerClasses, deleteFields, tWorkflowStation.getObjectID());
			}
		} catch (TorqueException e) {
			LOGGER.error("Deleting the workflow stations by criteria " + crit.toString() + " failed with " + e.getMessage());
		}
		
	}

	/**
	 * Loads the station by primary key
	 * @param objectID
	 * @return
	 */
	@Override
	public TWorkflowStationBean loadByPrimaryKey(Integer objectID) {
		TWorkflowStation tobject = null;
		try{
			tobject = retrieveByPK(objectID);
		}
		catch(Exception e){
			LOGGER.warn("Loading of a workflow station by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (tobject!=null){
			return tobject.getBean();
		}
		return null;
	}

	/**
	 * Gets all workflowStationBeans
	 * @return
	 */
	@Override
	public List<TWorkflowStationBean> loadAll() {
		Criteria crit = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all workflow stations failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Loads the workflowStationBeans by keys 
	 * @param keys
	 * @return
	 */
	
	/**
	 * Loads all workflow stations
	 * @return
	 */
	
	@Override
	public List<TWorkflowStationBean> loadByWorkflow(Integer workflowID){
		Criteria crit = new Criteria();
		crit.add(WORKFLOW,workflowID);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading workflow stations by workflow:"+workflowID+" failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Loads all possible transitions from fromState in workflow
	 * @param workflowID 
	 * @param fromStatus
	 * @return
	 */
	@Override
	public TWorkflowStationBean loadFromStatusStation(Integer workflowID, Integer fromStatus) {
		Criteria crit = new Criteria();
		crit.add(WORKFLOW, workflowID);
		crit.add(STATUS, fromStatus);
		//possibly more transitions (more toState-s or even more actions for the same toStates) 
		try {
			List<TWorkflowStation> workflowStationList = doSelect(crit);
			if (workflowStationList!=null && !workflowStationList.isEmpty()) {
				return workflowStationList.get(0).getBean();
			}
		} catch (TorqueException e) {
			LOGGER.error("Loading workflow transitions for workflow " + workflowID +
					" from status " + fromStatus + " failed with " + e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * Loads all possible transitions from fromState in workflow
	 * @param workflowID 
	 * @param fromStationType
	 * @return
	 */
	@Override
	public TWorkflowStationBean loadFromStationTypeStation(Integer workflowID, Integer fromStationType) {
		Criteria crit = new Criteria();
		crit.add(WORKFLOW, workflowID);
		crit.addJoin(TWorkflowTransitionPeer.STATIONFROM, OBJECTID);
		crit.add(TWorkflowTransitionPeer.WORKFLOW, workflowID);
		crit.add(STATIONTYPE, fromStationType);
		//possibly more transitions (more toState-s or even more actions for the same toStates) 
		crit.setDistinct();
		try {
			List<TWorkflowStation> workflowStationList = doSelect(crit);
			if (workflowStationList!=null && !workflowStationList.isEmpty()) {
				return workflowStationList.get(0).getBean();
			}
		} catch (TorqueException e) {
			LOGGER.error("Loading workflow transitions for workflow " + workflowID +
					" from stationType " + fromStationType + " failed with " + e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * Loads all possible toStations from fromStation for a triggerEvent
	 * @param fromStation
	 * @param triggerEvent
	 * @param includeHooks whether to include the transitions to the same station
	 * @return
	 */
	@Override
	public List<TWorkflowStationBean> loadToStationsFromStation(Integer fromStation, Integer triggerEvent, boolean includeHooks) {
		Criteria crit = new Criteria();
		crit.addJoin(TWorkflowTransitionPeer.STATIONTO, OBJECTID);
		crit.add(TWorkflowTransitionPeer.STATIONFROM, fromStation);
		//remove the hooks to the same station because the possible other states are searched (StatusWorkflow.loadStatesTo()),
		//the actual state is included anyway in list independently of existence of the hook,
		//but we may avoid to unnecessarily process the guards to the same status
		if (!includeHooks) {
			crit.add(OBJECTID, fromStation, Criteria.NOT_EQUAL);
		}
		Criterion actionKeyIs = crit.getNewCriterion(TWorkflowTransitionPeer.ACTIONKEY, triggerEvent, Criteria.EQUAL);
		Criterion actionKeyIsNull = crit.getNewCriterion(TWorkflowTransitionPeer.ACTIONKEY, null, Criteria.ISNULL);
		crit.add(actionKeyIs.or(actionKeyIsNull));
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading workflow stations from station " + fromStation + " failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Saves a workflow
	 * @param workflowStationBean
	 * @return
	 */
	@Override
	public Integer save(TWorkflowStationBean workflowStationBean) {
		try {
			TWorkflowStation tobject = TWorkflowStation.createTWorkflowStation(workflowStationBean);
			tobject.save();
			return tobject.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a workflow station failed with " + e.getMessage());
			return null;
		}
	}

	/**
	 * Deletes a screen by primary key
	 * Is deletable should return true before calling this method
	 * @param objectID
	 */
	@Override
	public void delete(Integer objectID) {
		ReflectionHelper.delete(deletePeerClasses, deleteFields, objectID);
	}
	private List<TWorkflowStationBean> convertTorqueListToBeanList(List<TWorkflowStation> torqueList) {
		List<TWorkflowStationBean> beanList = new LinkedList<TWorkflowStationBean>();
		if (torqueList!=null){
			for (TWorkflowStation workflowStation : torqueList) {
				beanList.add(workflowStation.getBean());
			}
		}
		return beanList;
	}
}
