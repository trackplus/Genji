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

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TWorkItemLockBean;
import com.aurel.track.dao.WorkItemLockDAO;
import com.aurel.track.util.GeneralUtils;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TWorkItemLockPeer
    extends com.aurel.track.persist.BaseTWorkItemLockPeer
    implements WorkItemLockDAO
{
	private static final long serialVersionUID = -1183449522367635746L;
	private static final Logger LOGGER = LogManager.getLogger(TWorkItemLockPeer.class);
	
	/**
	 * Loads a workItemLockBean by primary key
	 * @param objectID
	 * @return
	 */
	public TWorkItemLockBean loadByPrimaryKey(Integer objectID) {		
		TWorkItemLock tWorkItemLock = null;
    	try {		
    		tWorkItemLock = retrieveByPK(objectID);    		
    	} catch(Exception e) {
    		//no logging because this can happen quite oft
    		LOGGER.debug("Loading of a workItemLockBean by primary key " + objectID + " failed with " + e.getMessage());
    		LOGGER.debug(ExceptionUtils.getStackTrace(e));
    	} 
    	if (tWorkItemLock!=null)
		{
			return tWorkItemLock.getBean();
		}
		return null;
	}
	
	/**
	 * Gets a TWorkItemLockBean by the sessionID
	 * @param objectID
	 * @return
	 */
	public TWorkItemLockBean loadBySessionID(String sessionID) {
		List workItemLocks = null;
		Criteria crit = new Criteria();
        crit.add(HTTPSESSION, sessionID);
        try {
        	workItemLocks = doSelect(crit);
        } catch (TorqueException e) {
        	LOGGER.error("Loading of a workItemLockBean by sessionID " + sessionID + " failed with " + e.getMessage(), e);
        }
        if (workItemLocks!=null && workItemLocks.size()>1) {
        	LOGGER.error("Loading of a workItemLockBean by sessionID " + sessionID + " resulted in " + workItemLocks.size() + " number of records");
        }
        if (workItemLocks!=null && !workItemLocks.isEmpty()) {
        	return ((TWorkItemLock)workItemLocks.get(0)).getBean();
        }
        return null;
	}
	
	/**
	 * Get the workItemIDs from the list 
	 * which are locked at the moment 
	 * @param workItemIDsList
	 * @return
	 */
	public List<TWorkItemLockBean> getLockedIssues(List<Integer> workItemIDsList) {
		List<TWorkItemLockBean> lockedList=new LinkedList<TWorkItemLockBean>();
		if (workItemIDsList==null || workItemIDsList.isEmpty()) {
			return lockedList;
		}				
		List<int[]> chunksList = GeneralUtils.getListOfChunks(workItemIDsList);
		if (chunksList==null) {
			return lockedList;
		}
		Criteria criteria = new Criteria();
		Iterator<int[]> iterator = chunksList.iterator();
		while (iterator.hasNext()) {
			int[] workItemIDChunk = iterator.next();
			criteria = new Criteria();			
			criteria.addIn(WORKITEM, workItemIDChunk);
			try {
				lockedList.addAll(convertTorqueListToBeanList(doSelect(criteria)));
			} catch(Exception e) {
	        	LOGGER.error("Getting the locked workItemLockBeans failed with " + e.getMessage(), e);
	        }			
		}
        return lockedList;
	}

	/**
	 * Saves a TWorkItemLockBean
	 * @param workItemLockBean
	 * @return
	 */
	public Integer save(TWorkItemLockBean workItemLockBean) {
		TWorkItemLock tWorkItemLock;		
		try {
			tWorkItemLock = BaseTWorkItemLock.createTWorkItemLock(workItemLockBean);
			tWorkItemLock.save();
			return tWorkItemLock.getWorkItem();
		} catch (Exception e) {
			LOGGER.error("Saving of a workItemLock failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Deletes an TWorkItemLockBean by primary key
	 * @param objectID
	 */
	public void delete(Integer objectID) {
		Criteria crit = new Criteria();
        crit.add(WORKITEM, objectID);
        try {
            doDelete(crit);
        } catch (TorqueException e) {
        	LOGGER.error("Deleting the workItemLock by primary key " + objectID + " failed with: " + e);
        }		
	}
	
	
	/**
	 * Deletes an TWorkItemLockBean by sessionID
	 * @param sessionID
	 */
	public void deleteBySession(String sessionID) {
		Criteria crit = new Criteria();
        crit.add(HTTPSESSION, sessionID);
        try {
            doDelete(crit);
        } catch (TorqueException e) {
        	LOGGER.error("Deleting the workItemLock by sessionID " + sessionID + " failed with: " + e);
        }			
	}

	/**
	 * Deletes an TWorkItemLockBean by personID
	 * @param personID
	 */
	public void deleteByPerson(Integer personID) {
		Criteria crit = new Criteria();
        crit.add(PERSON, personID);
        try {
            doDelete(crit);
        } catch (TorqueException e) {
        	LOGGER.error("Deleting the workItemLock by personID " + personID + " failed with: " + e);
        }
	}
	
	
	
	/**
	 * Deletes all TWorkItemLockBeans from the database by starting the server
	 */
	public void deleteAll() {
		Criteria crit = new Criteria();        
        try {
            doDelete(crit);
        } catch (TorqueException e) {
        	LOGGER.error("Deleting all the workItemLocks failed with: " + e);
        }		
	}

	/**
	 * Converts a list of TWorkItemLock torque objects to a list of TWorkItemLockBean objects 
	 * @param torqueList
	 * @return
	 */
	private static List<TWorkItemLockBean> convertTorqueListToBeanList(List<TWorkItemLock> torqueList) {		
		List<TWorkItemLockBean> beanList = new LinkedList<TWorkItemLockBean>();
		if (torqueList!=null) {
			for (TWorkItemLock workItemLock : torqueList) {
				beanList.add(workItemLock.getBean());
			}
		}
		return beanList;
	}
}
