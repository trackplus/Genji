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
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TBasketBean;
import com.aurel.track.dao.BasketDAO;
import com.aurel.track.util.GeneralUtils;

/**
 * WorkItem baskets for persons
 *
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TBasketPeer
    extends com.aurel.track.persist.BaseTBasketPeer implements BasketDAO {
	
	private static final Logger LOGGER = LogManager.getLogger(TBasketPeer.class);
	
	public static final long serialVersionUID = 400L;
	
	private static Class[] replacePeerClasses = {		
    	TPersonBasketPeer.class,     	
    };
    
    private static String[] replaceFields = {
    	TPersonBasketPeer.BASKET,     	
    };	    
    
	/**
	 * Loads a basketBean by primary key 
	 * @param objectID
	 * @return
	 */
	public TBasketBean loadByPrimaryKey(Integer objectID) {
		TBasket tBasket = null;
    	try {
    		tBasket = retrieveByPK(objectID);    		
    	} catch(Exception e) {
    		LOGGER.warn("Loading the basket by primary key " + objectID + " failed with " + e.getMessage());
    		LOGGER.debug(ExceptionUtils.getStackTrace(e));
    	} 
    	if (tBasket!=null) {
			return tBasket.getBean();
		}
		return null;
	}
		
	/**
	 * Loads a basketBeans by keys 
	 * @param objectID
	 * @return
	 */
	public List<TBasketBean> loadByPrimaryKeys(Set<Integer> objectIDs) {
		Criteria crit = new Criteria();     
		crit.addIn(OBJECTID, objectIDs.toArray());
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading all main baskets failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Loads the main baskets   
	 * @return
	 */
	public List<TBasketBean> loadMainBaskets() {
		Criteria crit = new Criteria();        
         crit.add(PARENTBASKET, (Object)null, Criteria.ISNULL);
        try {
        	return convertTorqueListToBeanList(doSelect(crit));
        } catch(Exception e) {
			LOGGER.error("Loading all main baskets failed with " + e.getMessage(), e);
			return null;
		}
	}
		
	/**
	 * Loads child baskets of a parent basket   
	 * @return
	 */
	public List<TBasketBean> loadChildBaskets(Integer basketID) {
		Criteria crit = new Criteria();        
        crit.addAscendingOrderByColumn(LABEL);        
        crit.add(PARENTBASKET, basketID);
    	try {
    		return convertTorqueListToBeanList(doSelect(crit));
        } catch (Exception e) {
            LOGGER.error("Loading child baskets of the parent basket " + basketID +  " failed with " + e.getMessage(), e);
            return null;
        }	
	}

	public TBasketBean loadBasketByItemAndPerson(Integer itemID,Integer personID){
		TBasketBean basketBean = null;
		Criteria crit = new Criteria();
		crit.addJoin(OBJECTID,BaseTPersonBasketPeer.BASKET);
		crit.add(BaseTPersonBasketPeer.WORKITEM,itemID);
		crit.add(BaseTPersonBasketPeer.PERSON,personID);
		try {
			List lst=doSelect(crit);
			if(lst!=null && !lst.isEmpty()){
				basketBean=((TBasket)lst.get(0)).getBean();
			}
		} catch (Exception e) {
			LOGGER.error("Loading  basket by itemID:"+itemID+" and personID:" + personID +  " failed with " + e.getMessage(), e);
			basketBean=null;
		}
		return basketBean;
	}
	public List<TBasketBean> loadBasketsByItemsAndPerson(int[] workItemIDs,Integer personID){
		List basketList =  new ArrayList();
		if (workItemIDs==null || workItemIDs.length==0) {
			return basketList;
		}
		Criteria criteria;
		List workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);
		if (workItemIDChunksList==null) {
			return basketList;
		}
		Iterator iterator = workItemIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] workItemIDChunk = (int[])iterator.next();
			criteria = new Criteria();
			criteria.addJoin(OBJECTID,BaseTPersonBasketPeer.BASKET);
			criteria.addIn(BaseTPersonBasketPeer.WORKITEM, workItemIDChunk);
			criteria.add(BaseTPersonBasketPeer.PERSON,personID);
			try {
				basketList.addAll(doSelect(criteria));
			} catch(Exception e) {
				LOGGER.error("Loading the basket by workItemIDs failed with " + e.getMessage(), e);
			}
		}
		return convertTorqueListToBeanList(basketList);
	}
	
	/**
	 * Saves a basketBean in the TBasket table
	 * @param basketBean
	 * @return
	 */
	public Integer save(TBasketBean basketBean) {			
		try {			
			TBasket tBasket = BaseTBasket.createTBasket(basketBean);			
			tBasket.save();
			return tBasket.getObjectID();			
		} catch (Exception e) {
			LOGGER.error("Saving of a basket failed with " + e.getMessage(), e);
			return null;
		}	
	}
			
	/**
	 * Whether there are issues in this basket
	 * @param objectID
	 * @return
	 */
	public boolean hasDependentData(Integer objectID) {
		return ReflectionHelper.hasDependentData(replacePeerClasses, replaceFields, objectID);
	}
	
	/**
	 * Deletes a basketBean from the TBasket table 
	 * @param objectID
	 */
	public void delete(Integer objectID) {
		Criteria  criteria = new Criteria();
		criteria.add(OBJECTID, objectID);
		try {
			doDelete(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the basket " + objectID + " failed with " + e.getMessage(), e);
		}
	}
	
	/**
	 * Replaces the dependences with a newBasketID and 
	 * deletes the oldBasketID from the TBasket table 
	 * @param oldBasketID
	 * @param newBasketID
	 */
	public void replace(Integer oldBasketID, Integer newBasketID) {
		ReflectionHelper.replace(replacePeerClasses, replaceFields, oldBasketID, newBasketID);    
	}
	
	
	private List<TBasketBean> convertTorqueListToBeanList(List<TBasket> torqueList) {		
		List<TBasketBean> beanList = new LinkedList<TBasketBean>();
		if (torqueList!=null) {
			for (TBasket tBasket : torqueList) {
				beanList.add(tBasket.getBean());
			}			
		}
		return beanList;
	}
}
