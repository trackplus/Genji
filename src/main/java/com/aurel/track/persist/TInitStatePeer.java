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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TInitStateBean;
import com.aurel.track.dao.InitStatusDAO;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TInitStatePeer
	extends com.aurel.track.persist.BaseTInitStatePeer implements InitStatusDAO
{
	private static final Logger LOGGER = LogManager.getLogger(TInitStatePeer.class); 
	
	public static final long serialVersionUID = 400L;
	
	/**
	 * Loads the initial states by project
	 * @param projectID
	 * @return
	 */
	public List<TInitStateBean> loadByProject(Integer projectID) {
		Criteria crit = new Criteria();
		crit.add(PROJECT, projectID);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Getting the initial states by project "+ projectID + 
					" failed with " + e.getMessage(), e);
			return new LinkedList<TInitStateBean>();
		}
	}
	
	/**
	 * Loads the initial states by project and issueType
	 * @param projectID
	 * @param issueTypeID
	 * @return
	 */
	public List<TInitStateBean> loadByProjectAndIssueType(Integer projectID, Integer issueTypeID) {
		Criteria crit = new Criteria();
		crit.add(PROJECT, projectID);
		crit.add(LISTTYPE, issueTypeID);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Getting the initial states by project "+ projectID + 
					" and issue type " + issueTypeID + " failed with " + e.getMessage(), e);
			return new LinkedList<TInitStateBean>();
		}
	}
	
	/**
	 * Loads the initial states by project and issueTypes
	 * @param projectID
	 * @param issueTypeIDs
	 * @return
	 */
	public  List<TInitStateBean> loadByProjectAndIssueTypes(Integer projectID, List<Integer> issueTypeIDs) {
		if (issueTypeIDs==null || issueTypeIDs.isEmpty() || projectID==null) {
			return new LinkedList<TInitStateBean>();
		}
		Criteria crit = new Criteria();
		crit.add(PROJECT, projectID);
		crit.addIn(LISTTYPE, issueTypeIDs);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Getting the initial states by project "+ projectID + 
					" and issue types " + issueTypeIDs.size() + " failed with " + e.getMessage(), e);
			 return new LinkedList<TInitStateBean>();
		}
	}
	
	
	/**
	 * Saves an initial state .
	 * @param initStateBean
	 * @return
	 */
	public Integer save(TInitStateBean initStateBean) {
		TInitState tInitState;
		try {
			tInitState = BaseTInitState.createTInitState(initStateBean);
			tInitState.save();
			return tInitState.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of an initStateBean failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	
	/**
	 * Deletes a initial state by objectID
	 * @param objectID
	 */
	public void delete(Integer objectID) {
		Criteria crit = new Criteria();
		crit.add(OBJECTID, objectID);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the InitState by " + objectID + " failed with: " + e);
		}
	}
	
	private List<TInitStateBean> convertTorqueListToBeanList(List<TInitState> torqueList) {
		List<TInitStateBean> beanList = new LinkedList<TInitStateBean>();
		if (torqueList!=null){
			Iterator<TInitState> itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				beanList.add(itrTorqueList.next().getBean());
			}
		}
		return beanList;
	}
}
