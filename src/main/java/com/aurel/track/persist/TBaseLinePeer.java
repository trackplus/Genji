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

import com.aurel.track.beans.TBaseLineBean;
import com.aurel.track.dao.BaseLineDAO;
import com.aurel.track.dao.torque.SimpleCriteria;
import com.workingdogs.village.Record;


// Local classes

/** 
 * The skeleton for this class was autogenerated by Torque on:
 *
 * [Fri Jun 14 10:06:48 GMT+02:00 2002]
 *
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TBaseLinePeer 
    extends com.aurel.track.persist.BaseTBaseLinePeer
    implements BaseLineDAO
{

    private static final long serialVersionUID = 6235239076079596981L;    
    private static final Logger LOGGER = LogManager.getLogger(TBaseLinePeer.class);
    
    /**
	 * Loads a BaseLineBean by primary key 
	 * @param objectID
	 * @return
	 */
	/*public TBaseLineBean loadByPrimaryKey(Integer objectID)
	{
		TBaseLine tBaseLine = null;
    	try
    	{
    		tBaseLine = retrieveByPK(objectID);    		
    	}
    	catch(Exception e)    	
    	{
    		LOGGER.error("Loading of a baseline by primary key " + objectID + " failed with " + e.getMessage());
    	} 
    	if (tBaseLine!=null)
		{
			return tBaseLine.getBean();
		}
		return null; 
	}*/
	
    /**
	 * Gets the maximal objectID
	 */
	public Integer getMaxObjectID() {
		String max = "max(" + BLKEY + ")";
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
	public List<TBaseLineBean> getNextChunk(Integer actualValue, Integer chunkInterval) {
		List torqueList = new ArrayList();
		SimpleCriteria crit = new SimpleCriteria();
		int toValue = actualValue.intValue() + chunkInterval.intValue();
		/*crit.add(BLKEY, actualValue, Criteria.GREATER_THAN);
		crit.add(BLKEY, toValue, Criteria.LESS_EQUAL);*/
		crit.addIsBetween(BLKEY, actualValue.intValue(), toValue);
		crit.addAscendingOrderByColumn(LASTEDIT);
		try {
			torqueList = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Getting the base line changes from " + actualValue + " to " + toValue + " failed with " + e.getMessage());
		}
		return convertTorqueListToBeanList(torqueList);
	}
	
	/**
	 * Load all BaseLineBeans 
	 * @return
	 */
	public List loadAll() {
		List torqueList = new ArrayList();
		Criteria crit = new Criteria();
		crit.addAscendingOrderByColumn(LASTEDIT);
		try {
			torqueList = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Getting the base line changes for all workItems failed with " + e.getMessage());
		}
		return convertTorqueListToBeanList(torqueList);
	}
		
	/**
	 * Loads a BaseLineBean list by workItemKeys
	 * @param workItemKeys
	 * @return
	 */
	/*public List loadByWorkItemKeys(int [] workItemKeys) {
		List torqueList = new ArrayList();
		Criteria crit = new Criteria();
        crit.addIn(WORKITEMKEY, workItemKeys);
        crit.addDescendingOrderByColumn(WORKITEMKEY);
        crit.addDescendingOrderByColumn(LASTEDIT);
		try {
			torqueList = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Getting the base line changes for workItems failed with " + e.getMessage());
		}
		return convertTorqueListToBeanList(torqueList);
	}*/
	
	/**
	 * Saves a BaseLineBean in the TBaseLine table
	 * @param baseLineBean
	 * @return
	 */
	public Integer save(TBaseLineBean baseLineBean) {
		TBaseLine tBaseLine;		
		try {
			tBaseLine = BaseTBaseLine.createTBaseLine(baseLineBean);
			tBaseLine.save();
			return tBaseLine.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a baseline failed with " + e.getMessage());
			return null;
		}	
	}
	
	/**
     * Get the base line history for an item
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
			LOGGER.error("Getting the base lines for workItem " + workItemID + " failed with " + e.getMessage());
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
				beanList.add(((TBaseLine)itrTorqueList.next()).getBean());
			}
		}
		return beanList;
	}
    
    /*private static List convertTorqueListToBeanList(List torqueList, Map personsMap) {
		List beanList = new ArrayList();
		if (torqueList!=null){
			Iterator itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				TBaseLineBean bean=((TBaseLine)itrTorqueList.next()).getBean();               
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
