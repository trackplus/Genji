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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TStateBean;
import com.aurel.track.dao.StateDAO;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.itemNavigator.ItemNavigatorBL.QUERY_TYPE;
import com.aurel.track.itemNavigator.lastExecuted.LastExecutedBL;
import com.aurel.track.util.GeneralUtils;
import com.workingdogs.village.Record;


/** 
 * The skeleton for this class was autogenerated by Torque on:
 *
 * [Fri Jun 14 10:06:48 GMT+02:00 2002]
 *
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TStatePeer 
	extends com.aurel.track.persist.BaseTStatePeer
	implements StateDAO
{
	private static final Logger LOGGER = LogManager.getLogger(TStatePeer.class);
	private static final long serialVersionUID = 7904156802942655791L;

	private static Class[] replacePeerClasses = {
		TWorkItemPeer.class, 
		TProjectPeer.class,
		TStateChangePeer.class,
		TInitStatePeer.class
	};
	
	private static String[] replaceFields = {
		TWorkItemPeer.STATE, 
		TProjectPeer.DEFINITSTATE,
		TStateChangePeer.CHANGEDTO, 
		TInitStatePeer.STATEKEY
	};
	
	private static Class[] deletePeerClasses = {
		TNotifyPeer.class,
		TWorkFlowPeer.class,
		TWorkFlowPeer.class,
		TPstatePeer.class,
		TWorkflowStationPeer.class,
		TEscalationStatePeer.class,
		TStatePeer.class
	};
	
	private static String[] deleteFields = {
		TNotifyPeer.STATEKEY,
		TWorkFlowPeer.STATETO,
		TWorkFlowPeer.STATEFROM,
		TPstatePeer.STATE,
		TWorkflowStationPeer.STATUS,
		TEscalationStatePeer.STATUS,
		TStatePeer.PKEY
	};
	
	/**
	 * Loads a state by primary key
	 * @param objectID
	 * @return
	 */
	public TStateBean loadByPrimaryKey(Integer objectID) {
		TState tState = null;
		try {
			tState = retrieveByPK(objectID);
		} catch(Exception e) {
			LOGGER.info("Loading of a state by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} 
		if (tState!=null) {
			return tState.getBean();
		}
		return null;
	}
	
	/**
	 * Gets an stateBean by label
	 * @param label
	 * @return
	 */
	public List<TStateBean> loadByLabel(String label) {
		Criteria crit = new Criteria();
		crit.add(LABEL, label);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading the state by label " + label +  " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Loads stateBeans by stateIDs
	 * @param stateIDs
	 * @return
	 */
	public List<TStateBean> loadByKeys(Object[] stateIDs) {
		if (stateIDs==null || stateIDs.length==0) {
			return new LinkedList<TStateBean>();
		}
		Criteria crit = new Criteria();
		crit.addIn(PKEY, stateIDs);
		crit.addAscendingOrderByColumn(SORTORDER);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading the states by keys " + stateIDs + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Loads all stateBeans  
	 * @return
	 */
	public List<TStateBean> loadAll() {
		Criteria crit = new Criteria();
		crit.add(PKEY, Integer.valueOf(0), Criteria.GREATER_THAN);
		crit.addAscendingOrderByColumn(SORTORDER);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all states failed with " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Return the stateBeans with a specific stateFlag   
	 * @param stateFlag
	 * @return
	 */
	public List<TStateBean> loadByStateFlag(int stateFlag) {
		Criteria crit = new Criteria();
		crit.add(PKEY, new Integer(0), Criteria.GREATER_THAN);
		crit.add(STATEFLAG, stateFlag);
		crit.addAscendingOrderByColumn(SORTORDER);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading states by stateFlag " + stateFlag + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Return the stateBeans by stateFlags   
	 * @param stateFlags
	 * @return
	 */
	public List<TStateBean> loadByStateFlags(int[] stateFlags) {
		if (stateFlags==null || stateFlags.length==0) {
			return new LinkedList<TStateBean>();
		}
		Criteria crit = new Criteria();
		crit.add(PKEY, new Integer(0), Criteria.GREATER_THAN);
		crit.addIn(STATEFLAG, stateFlags);
		crit.addAscendingOrderByColumn(SORTORDER);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading states by stateFlags " + stateFlags + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Gets the next available sortorder
	 * @return
	 */
	public Integer getNextSortOrder() {
		Integer sortOrder = null;
		String max = "max(" + SORTORDER + ")";
		Criteria crit = new Criteria();
		crit.addSelectColumn(max);
		try {
			sortOrder = ((Record) doSelectVillageRecords(crit).get(0)).getValue(1).asIntegerObj();
		} catch (Exception e) {
			LOGGER.error("Getting the next sortorder for state failed with: " + e);
		}
		if (sortOrder==null) {
			return Integer.valueOf(1);
		} else {
			return Integer.valueOf(sortOrder.intValue()+1);
		}
	}
	
	/**
	 * Save  state in the TState table
	 * @param stateBean
	 * @return
	 */
	public Integer save(TStateBean stateBean) {
		TState tState;
		try {
			tState = BaseTState.createTState(stateBean);
			tState.save();
			return tState.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a state bean failed with " + e.getMessage(), e);
			return null;
		}	
	}
	
	public boolean hasDependentData(Integer pkey) {
		return ReflectionHelper.hasDependentData(replacePeerClasses, replaceFields, pkey);
	}
	
	/** 
	 * This method replaces all occurrences of state value oldOID with
	 * state value newOID.
	 * @param oldOID
	 * @param newOID
	 */
	public void replace(Integer oldOID, Integer newOID) {
		ReflectionHelper.replace(replacePeerClasses, replaceFields, oldOID, newOID);
	}
	
	/**
	 * Deletes a state from the TState table 
	 * @param objectID
	 */
	public void delete(Integer objectID) {
		new TCardFieldOptionPeer().deleteOptionForField(SystemFields.INTEGER_STATE, objectID);
		LastExecutedBL.deleteByFilterIDAndFilterType(objectID, QUERY_TYPE.STATUS);
		ReflectionHelper.delete(deletePeerClasses, deleteFields, objectID);
	}
	
	/**
	 * Loads a list with statusBeans with statuses assigned 
	 * to at least one workItem in any of the projects	
	 * @param projects
	 * @return
	 */
	/*public List<TStateBean> loadUsedByProject(Integer[] projects) {
		List<TStateBean> statusList = new ArrayList<TStateBean>();
		if (projects==null || projects.length==0) {
			return new ArrayList<TStateBean>();
		}
		List<int[]> projectChunkList = GeneralUtils.getListOfChunks(projects);
		Iterator<int[]> iterator = projectChunkList.iterator();
		while (iterator.hasNext()) {
			int[] projectIDChunk = iterator.next(); 
			Criteria criteria = new Criteria();
			criteria.addJoin(PKEY, TWorkItemPeer.STATE);
			criteria.addIn(TWorkItemPeer.PROJECTKEY, projectIDChunk);
			criteria.addAscendingOrderByColumn(TStatePeer.SORTORDER);
			criteria.setDistinct();
			try {
				statusList.addAll(convertTorqueListToBeanList(doSelect(criteria)));
			} catch (Exception e) {
				LOGGER.error("Getting the used states for projects " + projectIDChunk + " failed with " + e.getMessage(), e);
			}
		}
		return statusList;
	}*/
	
	/**
	 * Returns the sort order column name
	 * @return
	 */
	public String getSortOrderColumn() {
		return "SORTORDER";
	}
	
	/**
	 * Returns the table name
	 * @return
	 */
	public String getTableName() {
		return TABLE_NAME;
	}
	
	private List<TStateBean> convertTorqueListToBeanList(List<TState> torqueList) {
		List<TStateBean> beanList = new LinkedList<TStateBean>();
		if (torqueList!=null){
			Iterator<TState> itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				beanList.add(itrTorqueList.next().getBean());
			}
		}
		return beanList;
	}
}
