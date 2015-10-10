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

import com.aurel.track.beans.TWorkflowActivityBean;
import com.aurel.track.dao.WorkflowActivityDAO;
import com.aurel.track.fieldType.fieldChange.FieldChangeSetters;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TWorkflowActivityPeer
    extends com.aurel.track.persist.BaseTWorkflowActivityPeer implements WorkflowActivityDAO{

	public static final long serialVersionUID = 400L;
	private static final Logger LOGGER = LogManager.getLogger(TWorkflowActivityPeer.class);

	private static Class[] deletePeerClasses = {
		TWfActivityContextParamsPeer.class,
		BaseTWorkflowActivityPeer.class
	};

	private static String[] deleteFields = {
		TWfActivityContextParamsPeer.WORKFLOWACTIVITY,
		BaseTWorkflowActivityPeer.OBJECTID
	};

	/**
	 * Deletes the TReleases satisfying a certain criteria 
	 * together with the dependent database entries 
	 * @param crit
	 */
	public static void doDelete(Criteria crit) {
		try {
			List<TWorkflowActivity> workflowActivityList = doSelect(crit);
			if (workflowActivityList == null || workflowActivityList.isEmpty()) {
				return;
			}
			for (TWorkflowActivity tWorkflowActivity : workflowActivityList) {
				ReflectionHelper.delete(deletePeerClasses, deleteFields, tWorkflowActivity.getObjectID());
			}
		} catch (TorqueException e) {
			LOGGER.error("Deleting the workflow activities by criteria " + crit.toString() + " failed with " + e.getMessage(), e);
		}
	}
	
	public TWorkflowActivityBean loadByPrimaryKey(Integer objectID) {
		TWorkflowActivity tobject = null;
		try{
			tobject = retrieveByPK(objectID);
		}
		catch(Exception e){
			LOGGER.warn("Loading of a workflow activity by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (tobject!=null){
			return tobject.getBean();
		}
		return null;
	}

	public List<TWorkflowActivityBean> loadAll() {
		Criteria crit = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all workflow activities failed with " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Loads the station entry activities
	 * @param stationID
	 * @return
	 */
	public List<TWorkflowActivityBean> loadByStationEntry(Integer stationID) {
		Criteria crit = new Criteria();
		crit.add(TWorkflowActivityPeer.STATIONENTRYACTIVITY, stationID);
		crit.addAscendingOrderByColumn(SORTORDER);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading station entry activities by station:"+stationID+" failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Loads the station exit activities
	 * @param stationID
	 * @return
	 */
	public List<TWorkflowActivityBean> loadByStationExit(Integer stationID) {
		Criteria crit = new Criteria();
		crit.add(TWorkflowActivityPeer.STATIONEXITACTIVITY, stationID);
		crit.addAscendingOrderByColumn(SORTORDER);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading station exit activities by station:"+stationID+" failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Loads the station do activities
	 * @param stationID
	 * @return
	 */
	public List<TWorkflowActivityBean> loadByStationDo(Integer stationID) {
		Criteria crit = new Criteria();
		crit.add(TWorkflowActivityPeer.STATIONDOACTIVITY, stationID);
		crit.addAscendingOrderByColumn(SORTORDER);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading station do activities by station:"+stationID+" failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	public List<TWorkflowActivityBean> loadByStation(Integer stationID) {
		Criteria crit = new Criteria();
		Criteria.Criterion criterionEntry = crit.getNewCriterion(TWorkflowActivityPeer.STATIONENTRYACTIVITY, stationID, Criteria.EQUAL);
		Criteria.Criterion criterionExit = crit.getNewCriterion(TWorkflowActivityPeer.STATIONEXITACTIVITY, stationID, Criteria.EQUAL);
		Criteria.Criterion criterionDo = crit.getNewCriterion(TWorkflowActivityPeer.STATIONDOACTIVITY, stationID, Criteria.EQUAL);
		crit.add(criterionEntry.or(criterionExit).or(criterionDo));
		crit.addAscendingOrderByColumn(SORTORDER);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading workflow activities by station:"+stationID+" failed with " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Load transition activities
	 */
	public List<TWorkflowActivityBean> loadByTransition(Integer transitionID) {
		Criteria crit = new Criteria();
		crit.add(TRANSITIONACTIVITY,transitionID);
		crit.addAscendingOrderByColumn(SORTORDER);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading workflow activities by transition:"+transitionID+" failed with " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Whether the worklflow has parameterized transition activity
	 * @param workflowID
	 */
	public boolean hasTransitionActivityWithParam(Integer workflowID) {
		Criteria crit = new Criteria();
		crit.addJoin(TRANSITIONACTIVITY, TWorkflowTransitionPeer.OBJECTID);
		crit.add(TWorkflowTransitionPeer.WORKFLOW, workflowID);
		crit.add(FIELDSETTERRELATION, FieldChangeSetters.PARAMETER);
		try {
			List<TWorkflowActivity> list = doSelect(crit);
			return list!=null && !list.isEmpty();
		} catch (TorqueException e) {
			LOGGER.error("Loading parameterized transaction activities by workflow:" + workflowID + " failed with " + e.getMessage(), e);
			return false;
		}
	}
	
	/**
	 * Gets the parameterized transition activities for workflow
	 * @param workflowID
	 */
	public List<TWorkflowActivityBean> getTransitionActivityWithParam(Integer workflowID) {
		Criteria crit = new Criteria();
		crit.addJoin(TRANSITIONACTIVITY, TWorkflowTransitionPeer.OBJECTID);
		crit.add(TWorkflowTransitionPeer.WORKFLOW, workflowID);
		crit.add(FIELDSETTERRELATION, FieldChangeSetters.PARAMETER);
		crit.addAscendingOrderByColumn(TRANSITIONACTIVITY);
		crit.addAscendingOrderByColumn(SORTORDER);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading parameterized transaction activities by workflow:" + workflowID + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Whether the worklflow has parameterized station entry activity
	 * @param workflowID
	 */
	public boolean hasStationEntryActivityWithParam(Integer workflowID) {
		Criteria crit = new Criteria();
		crit.addJoin(STATIONENTRYACTIVITY, TWorkflowStationPeer.OBJECTID);
		crit.add(TWorkflowStationPeer.WORKFLOW, workflowID);
		crit.add(FIELDSETTERRELATION, FieldChangeSetters.PARAMETER);
		try {
			List<TWorkflowActivity> list = doSelect(crit);
			return list!=null && !list.isEmpty();
		} catch (TorqueException e) {
			LOGGER.error("Loading parameterized station entry activities by workflow:" + workflowID + " failed with " + e.getMessage(), e);
			return false;
		}
	}
	
	
	
	/**
	 * Gets the parameterized station entry activities for workflow
	 * @param workflowID
	 */
	public List<TWorkflowActivityBean> getStationEntryActivityWithParam(Integer workflowID) {
		Criteria crit = new Criteria();
		crit.addJoin(STATIONENTRYACTIVITY, TWorkflowStationPeer.OBJECTID);
		crit.add(TWorkflowStationPeer.WORKFLOW, workflowID);
		crit.add(FIELDSETTERRELATION, FieldChangeSetters.PARAMETER);
		crit.addAscendingOrderByColumn(STATIONENTRYACTIVITY);
		crit.addAscendingOrderByColumn(SORTORDER);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading parameterized station entry activities by workflow:" + workflowID + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	
	/**
	 * Whether the worklflow has parameterized station exit activity
	 * @param workflowID
	 */
	public boolean hasStationExitActivityWithParam(Integer workflowID) {
		Criteria crit = new Criteria();
		crit.addJoin(STATIONEXITACTIVITY, TWorkflowStationPeer.OBJECTID);
		crit.add(TWorkflowStationPeer.WORKFLOW, workflowID);
		crit.add(FIELDSETTERRELATION, FieldChangeSetters.PARAMETER);
		try {
			List<TWorkflowActivity> list = doSelect(crit);
			return list!=null && !list.isEmpty();
		} catch (TorqueException e) {
			LOGGER.error("Loading parameterized station exit activities by workflow:" + workflowID + " failed with " + e.getMessage(), e);
			return false;
		}
	}
	
	/**
	 * Gets the parameterized station exit activities for workflow
	 * @param workflowID
	 */
	public List<TWorkflowActivityBean> getStationExitActivityWithParam(Integer workflowID) {
		Criteria crit = new Criteria();
		crit.addJoin(STATIONEXITACTIVITY, TWorkflowStationPeer.OBJECTID);
		crit.add(TWorkflowStationPeer.WORKFLOW, workflowID);
		crit.add(FIELDSETTERRELATION, FieldChangeSetters.PARAMETER);
		crit.addAscendingOrderByColumn(STATIONEXITACTIVITY);
		crit.addAscendingOrderByColumn(SORTORDER);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading parameterized station exit activities by workflow:" + workflowID + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	public Integer save(TWorkflowActivityBean workflowActivityBean) {
		try {
			TWorkflowActivity tobject = TWorkflowActivity.createTWorkflowActivity(workflowActivityBean);
			tobject.save();
			return tobject.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a workflow activity failed with " + e.getMessage(), e);
			return null;
		}
	}

	public void delete(Integer objectID) {
		ReflectionHelper.delete(deletePeerClasses, deleteFields, objectID);
	}

	private List<TWorkflowActivityBean> convertTorqueListToBeanList(List<TWorkflowActivity> torqueList) {
		List<TWorkflowActivityBean> beanList = new LinkedList<TWorkflowActivityBean>();
		if (torqueList!=null){
			for (TWorkflowActivity workflowActivity : torqueList) {
				beanList.add(workflowActivity.getBean());
			}
		}
		return beanList;
	}
}
