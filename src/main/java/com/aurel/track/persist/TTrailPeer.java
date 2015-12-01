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
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TTrailBean;
import com.aurel.track.dao.TrailDAO;
import com.aurel.track.dao.torque.SimpleCriteria;
import com.workingdogs.village.Record;

// Local classes

/** 
 */
public class TTrailPeer 
    extends com.aurel.track.persist.BaseTTrailPeer 
    implements TrailDAO {

    private static final long serialVersionUID = -3235426681347257436L;
    private static final Logger LOGGER = LogManager.getLogger(TTrailPeer.class);

    /**
     * Fills the itemInfo bean with the audit trail and returns a list
	 * of TrailBeans. The first item in the list returned is the most 
     * up to date one.
     */
    /*public static ArrayList load(Integer key) {
        // Get the audit trail info from the database
		List list = null;
		ArrayList results = new ArrayList(10);
		LOGGER.debug("Loading trail beans");
		try {
			Criteria crit = new Criteria();
			crit.add(BaseTTrailPeer.WORKITEMKEY, key, Criteria.EQUAL);
			crit.addDescendingOrderByColumn(BaseTTrailPeer.LASTEDIT);
			list = doSelect(crit);
		}
		catch (Exception e) {
			LOGGER.error("Exception loading trail beans " + e);
		}
		results = addBeans(list);
		return results;
    }*/


	/*private static ArrayList addBeans(List list) {
		Iterator i = list.iterator();
		ArrayList alist = new ArrayList(5);
		TTrail object = null;
		LOGGER.debug("Retrieving trailbeans from result list");
		while (i.hasNext()) {
			object = (TTrail) i.next();
			alist.add(object);
		}
		return alist;
	}*/

	/**
	 * Loads a TrailBean by primary key 
	 * @param objectID
	 * @return
	 */
	/*public TTrailBean loadByPrimaryKey(Integer objectID)
	{
		TTrail tTrail = null;
    	try
    	{
    		tTrail = retrieveByPK(objectID);    		
    	}
    	catch(Exception e)    	
    	{
    		LOGGER.error("Loading of a trail by primary key " + objectID + " failed with " + e.getMessage());
    	} 
    	if (tTrail!=null)
		{
			return tTrail.getBean();
		}
		return null; 
	}*/
	
    /**
	 * Gets the maximal objectID
	 */
	@Override
	public Integer getMaxObjectID() {
		String max = "max(" + TRAILKEY + ")";
		Criteria crit = new Criteria();		
		crit.addSelectColumn(max);
		try {
			return ((Record) doSelectVillageRecords(crit).get(0)).getValue(1).asIntegerObj();
		} catch (Exception e) {
			LOGGER.error("Getting the maximal objectID failed with " + e.getMessage());
		}
		return null;
	}
    
	/**
	 * Gets the next chunk
	 * @param actualValue
	 * @param chunkInterval
	 * @return
	 */
	@Override
	public List<TTrailBean> getNextChunk(Integer actualValue, Integer chunkInterval) {
		List torqueList = new ArrayList();
		SimpleCriteria crit = new SimpleCriteria();
		int toValue = actualValue.intValue() + chunkInterval.intValue();
		/*crit.add(TRAILKEY, actualValue, Criteria.GREATER_THAN);
		crit.add(TRAILKEY, toValue, Criteria.LESS_EQUAL);*/
		crit.addIsBetween(TRAILKEY, actualValue.intValue(), toValue);
		crit.addAscendingOrderByColumn(LASTEDIT);
		try {
			torqueList = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Getting the trail changes from " + actualValue + " to " + toValue + " failed with " + e.getMessage());
		}
		return convertTorqueListToBeanList(torqueList);
	}
	
	/**
	 * Load all TrailBeans 
	 * @return
	 */
	@Override
	public List loadAll() {
		List torqueList = new ArrayList();
		Criteria crit = new Criteria();
		crit.addAscendingOrderByColumn(LASTEDIT);
		try {
			torqueList = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Getting the trail changes for all workItems failed with " + e.getMessage());
		}
		return convertTorqueListToBeanList(torqueList);
	}
	
	/**
	 * Load a TrailBean list by workItemKeys
	 * @param workItemKeys
	 * @return
	 */
	/*public List loadByWorkItemKeys(int[] workItemKeys) {		 
		List torqueList = new ArrayList();
		Criteria crit = new Criteria();
        crit.addIn(WORKITEMKEY, workItemKeys);
        crit.addDescendingOrderByColumn(WORKITEMKEY);
        crit.addDescendingOrderByColumn(LASTEDIT);
		try {
			torqueList = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Getting the trail changes for workItems failed with " + e.getMessage());
		}
		return convertTorqueListToBeanList(torqueList);
	}*/


	/**
	 * Saves a TrailBean in the TBaseLine table
	 * @param trailBean
	 * @return
	 */
	@Override
	public Integer save(TTrailBean trailBean) {
		TTrail tTrail;		
		try {
			tTrail = BaseTTrail.createTTrail(trailBean);
			tTrail.save();
			return tTrail.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a trail failed with " + e.getMessage());
			return null;
		}	
	}    
    
	/**
    * Get the trail history for an item
    * @param workItemKey
    * @param personID if not null filter also by personID
    * @return
    */
    /*public List getByWorkItemAndPerson(Integer workItemID, Integer personID) {
        List torqueList = new ArrayList();
		Criteria crit = new Criteria();
        crit.add(WORKITEMKEY, workItemID);
        if (personID!=null) {
        	crit.add(CHANGEDBY, personID);
        }
        crit.addDescendingOrderByColumn(LASTEDIT);
		try {
			torqueList = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Getting the trail changes for workItem " + workItemID + " failed with " + e.getMessage());
		}
		//get the persons map 		
		crit.clear();
		crit.addJoin(TPersonPeer.PKEY, CHANGEDBY);
		crit.add(WORKITEMKEY, workItemID);		
		Map personsMap = TPersonPeer.getBeansMap(crit);
				
        return convertTorqueListToBeanList(torqueList, personsMap);
    }*/
    
    private static List convertTorqueListToBeanList(List torqueList) {
		List beanList = new ArrayList();
		if (torqueList!=null){
			Iterator itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				beanList.add(((TTrail)itrTorqueList.next()).getBean());
			}
		}
		return beanList;
	}
    
    /*private static List convertTorqueListToBeanList(List torqueList, Map personsMap) {
		List beanList = new ArrayList();
		if (torqueList!=null){
			Iterator itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				TTrailBean bean=((TTrail)itrTorqueList.next()).getBean();               
                TPersonBean personBean = (TPersonBean)personsMap.get(bean.getChangedByID());
                if (personBean!=null) {
                	bean.setChangedByName(personBean.getFullName());
                }
                beanList.add(bean);
			}
		}
		return beanList;
	}*/

}
