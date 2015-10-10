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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TPriorityBean;
import com.aurel.track.dao.PriorityDAO;
import com.aurel.track.fieldType.constants.SystemFields;
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
public class TPriorityPeer
	extends com.aurel.track.persist.BaseTPriorityPeer
	implements PriorityDAO
{
	private static final long serialVersionUID = 5194892018523066588L;

	private static final Logger LOGGER = LogManager.getLogger(TPriorityPeer.class);
	
	private static Class[] replacePeerClasses = {
		TEscalationEntryPeer.class,
		TWorkItemPeer.class
	};
	
	private static String[] replaceFields = {
		TEscalationEntryPeer.PRIORITY,
		TWorkItemPeer.PRIORITYKEY
	};
	
	private static Class[] deletePeerClasses = {
		TPpriorityPeer.class,
		TPriorityPeer.class
	};
	
	private static String[] deleteFields = {
		TPpriorityPeer.PRIORITY,
		TPriorityPeer.PKEY
	};
	
	/**
	 * Loads a priorityBean by primary key 
	 * @param objectID
	 * @return
	 */
	public TPriorityBean loadByPrimaryKey(Integer objectID) {
		TPriority tPriority = null;
		try {
			tPriority = retrieveByPK(objectID);
		} catch(Exception e) {
			LOGGER.info("Loading the priority by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} 
		if (tPriority!=null) {
			return tPriority.getBean();
		}
		return null;
	}
		
	/**
	 * Gets an priorityBean by label
	 * @param label
	 * @return
	 */
	public List<TPriorityBean> loadByLabel(String label) {
		Criteria crit = new Criteria();
		crit.add(LABEL, label);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading the priority by label " + label +  " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Loads all priorityBeans  
	 * @return
	 */
	public List<TPriorityBean> loadAll() {
		Criteria crit = new Criteria();
		crit.addAscendingOrderByColumn(SORTORDER);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		}
		catch(Exception e) {
			LOGGER.error("Loading all priorities failed with " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Loads the priorities by IDs
	 * @param priorityIDs
	 */
	public List<TPriorityBean> loadByPriorityIDs(List<Integer> priorityIDs) {
		if (priorityIDs==null || priorityIDs.isEmpty()) {
			LOGGER.warn("No priorityIDs specified " + priorityIDs);
			return new LinkedList<TPriorityBean>();
		}
		Criteria crit = new Criteria();
		crit.addAscendingOrderByColumn(SORTORDER);
		crit.addIn(PKEY, priorityIDs); 
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading of priorities by IDs failed with " + e.getMessage(), e);
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
			LOGGER.error("Getting the next sortorder for priority failed with: " + e);
		}
		if (sortOrder==null) {
			return Integer.valueOf(1);
		} else {
			return Integer.valueOf(sortOrder.intValue()+1);
		}
	}
	
	/**
	 * Saves a priorityBean in the TPriority table
	 * @param priorityBean
	 * @return
	 */
	public Integer save(TPriorityBean priorityBean) {
		TPriority tPriority;
		try {
			tPriority = BaseTPriority.createTPriority(priorityBean);
			tPriority.save();
			return tPriority.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a priority failed with " + e.getMessage(), e);
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
		new TCardFieldOptionPeer().deleteOptionForField(SystemFields.INTEGER_PRIORITY, objectID);
		ReflectionHelper.delete(deletePeerClasses, deleteFields, objectID);
	}
	
	public List<TPriorityBean> loadByProjectAndIssueType(Integer project, Integer listType) {
		Criteria crit = new Criteria();
		crit.addJoin(TPpriorityPeer.PRIORITY, PKEY);
		crit.addJoin(TPpriorityPeer.PROJECTTYPE, TProjectPeer.PROJECTTYPE);
		crit.add(TProjectPeer.PKEY, project);
		crit.add(TPpriorityPeer.LISTTYPE, listType);
		crit.addAscendingOrderByColumn(SORTORDER);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Getting the assigned priorities for project and list type failed with " + e.getMessage(), e);
			return null;
		}
	}
	
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
	
	private List<TPriorityBean> convertTorqueListToBeanList(List<TPriority> torqueList) {
		List<TPriorityBean> beanList = new LinkedList<TPriorityBean>();
		if (torqueList!=null){
			Iterator<TPriority> itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				beanList.add(itrTorqueList.next().getBean());
			}
		}
		return beanList;
	}
	
	
}
