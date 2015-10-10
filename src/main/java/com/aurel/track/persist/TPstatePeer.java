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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TPstateBean;
import com.aurel.track.dao.PStatusDAO;
import com.workingdogs.village.DataSetException;
import com.workingdogs.village.Record;

/**
 * The skeleton for this class was autogenerated by Torque on:
 *
 * [Tue Jun 15 21:31:34 CEST 2004]
 *
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TPstatePeer
	extends com.aurel.track.persist.BaseTPstatePeer
	implements PStatusDAO
{
	private static final long serialVersionUID = 2796147395165000658L;

	private static final Logger LOGGER = LogManager.getLogger(TPstatePeer.class);
	
	/**
	 * Loads all TPstateBeans 
	 * @return
	 */
	public List<TPstateBean> loadAll() {
		Criteria crit = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all pstates failed with " + e.getMessage(), e);
			return new LinkedList<TPstateBean>();
		}
	}
	
	/**
	 * Gets the number of valid statuses for issue types in a projectType
	 * @param projectTypeID
	 * @return
	 */
	public Map<Integer, Integer> loadNumberOfStatusesForIssueTypesInProjectType(Integer projectTypeID) {
		Map<Integer, Integer> numberOfStatusesForIssueTypes = new HashMap<Integer, Integer>();
		Criteria crit = new Criteria();
		crit.add(PROJECTTYPE, projectTypeID);
		String countStatuses = "COUNT(" + STATE + ")";
		crit.addSelectColumn(countStatuses);
		crit.addSelectColumn(LISTTYPE);
		crit.addGroupByColumn(LISTTYPE);
		List<Record> records = new LinkedList<Record>();
		try {
			records = doSelectVillageRecords(crit);
		} catch(Exception e) {
			LOGGER.error("Groupping the statuses by issueTypes in projectType " + projectTypeID + " failed with " + e.getMessage(), e);
		}
		try {
			if (records!=null && !records.isEmpty()) {
				for (Record record : records) {
					Integer numberOfPersons = record.getValue(1).asIntegerObj();
					Integer roleID = record.getValue(2).asIntegerObj();
					numberOfStatusesForIssueTypes.put(roleID, numberOfPersons); 
				}
			}
		} catch (Exception e) {
			LOGGER.error("Getting the number of statuses by issueTypes in projectType " + projectTypeID + " failed with " + e.getMessage(), e);
		}
		return numberOfStatusesForIssueTypes;
	}
	
	/**
	 * Gets the number of valid statuses for an issue type in a projectType
	 * @param projectTypeID
	 * @param issueTypeID
	 * @return
	 */
	public Integer loadNumberOfStatusesForIssueTypeInProjectType(Integer projectTypeID, Integer issueTypeID) {
		Criteria crit = new Criteria();
		crit.add(PROJECTTYPE, projectTypeID);
		crit.add(LISTTYPE, issueTypeID);
		String countStatuses = "COUNT(" + STATE + ")";
		crit.addSelectColumn(countStatuses);
		try {
			return ((Record) doSelectVillageRecords(crit).get(0)).getValue(1).asIntegerObj();
		} catch (TorqueException e) {
			LOGGER.error("Counting the statuses in projectTypeID " + projectTypeID + " and issueTypeID " +
					issueTypeID + " failed with TorqueException " + e.getMessage(), e);
		} catch (DataSetException e) {
			LOGGER.error("Counting the statuses in projectTypeID " + projectTypeID + " and issueTypeID " +
					issueTypeID + " failed with DataSetException " + e.getMessage(), e);
		}
		return Integer.valueOf(0);
	}
	
	/**
	 * Load TPstateBeans by project types and issueType
	 * @param projectType
	 * @param issueType
	 * issueTypes
	 */
	public List<TPstateBean> loadByProjectTypeAndIssueType(Integer projectType, Integer issueType) {
		if (issueType!=null && projectType!=null) {
			Criteria crit = new Criteria();
			crit.add(PROJECTTYPE, projectType);
			crit.add(LISTTYPE, issueType);
			crit.add(STATE, 0, Criteria.NOT_EQUAL);
			try {
				return convertTorqueListToBeanList(doSelect(crit));
			} catch (TorqueException e) {
				LOGGER.error("Getting the statuses allowed by project type "+ projectType + 
						" and issue type " + issueType + " failed with " + e.getMessage(), e);
			}
		}
		return new LinkedList<TPstateBean>();
	}
	public List<TPstateBean> loadByProjectType(Integer projectType) {
		if (projectType!=null) {
			Criteria crit = new Criteria();
			crit.add(PROJECTTYPE, projectType);
			crit.add(STATE, 0, Criteria.NOT_EQUAL);
			try {
				return convertTorqueListToBeanList(doSelect(crit));
			} catch (TorqueException e) {
				LOGGER.error("Getting the statuses allowed by project type "+ projectType +
						" failed with " + e.getMessage(), e);
			}
		}
		return new LinkedList<TPstateBean>();
	}
	
	/**
	 * Load TPstateBeans by project types and issueTypes
	 * @param projectType
	 * @param issueTypes
	 * issueTypes
	 */
	public List<TPstateBean> loadByProjectTypeAndIssueTypes(Integer projectType, List<Integer> issueTypes) {
		if (issueTypes==null || issueTypes.isEmpty() || projectType==null) {
			return new ArrayList<TPstateBean>();
		}		
		Criteria crit = new Criteria();
		crit.add(PROJECTTYPE, projectType);
		crit.addIn(LISTTYPE, issueTypes);
		crit.add(STATE, 0, Criteria.NOT_EQUAL);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Getting the statuses allowed by project type "+ projectType + 
					" and issue types " + issueTypes.size() + " failed with " + e.getMessage(), e);
			 return new ArrayList<TPstateBean>();
		}
	}
	
	/**
	 * Save  TPstateBean in the TPState table
	 * @param pstateBean
	 * @return
	 */
	public Integer save(TPstateBean pstateBean) {
		try {
			TPstate tPstate = BaseTPstate.createTPstate(pstateBean);
			tPstate.save();
			return tPstate.getObjectID();
			
		} catch (Exception e) {
			LOGGER.error("Saving pstateBean " + pstateBean.getLabel()  + ":" + pstateBean.getObjectID() + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Deletes a TPstateBean from the TPstate table 
	 * @param objectID
	 * @return
	 */
	public void delete(Integer objectID) {
		Criteria crit = new Criteria();
		crit.add(OBJECTID, objectID);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the TPstateBean " + objectID + " failed with: " + e);
		}
	}
	
	/**
	 * Deletes TPstateBean(s) from the TPstate table 
	 * @param projectTypeID
	 * @param itemTypeID
	 * @param statusIDs
	 */
	public void delete(Integer projectTypeID, Integer itemTypeID, List<Integer> statusIDs) {
		if (projectTypeID==null || itemTypeID==null || statusIDs==null || statusIDs.isEmpty()) {
			return;
		}
		Criteria crit = new Criteria();
		crit.add(PROJECTTYPE, projectTypeID);
		crit.add(LISTTYPE, itemTypeID);
		crit.addIn(STATE, statusIDs);		
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the TPstateBean by projectType " + projectTypeID +
					" and issueType " + itemTypeID + " and statuses " + statusIDs.size() + " failed with: " + e);
		}
	}
	
	private List<TPstateBean> convertTorqueListToBeanList(List<TPstate> torqueList) {
		List<TPstateBean> beanList = new LinkedList<TPstateBean>();
		if (torqueList!=null){
			Iterator<TPstate> itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				beanList.add(itrTorqueList.next().getBean());
			}
		}
		return beanList;
	}
}
