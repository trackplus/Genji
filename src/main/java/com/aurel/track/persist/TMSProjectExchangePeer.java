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

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TMSProjectExchangeBean;
import com.aurel.track.dao.MsProjectExchangeDAO;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TMSProjectExchangePeer
    extends com.aurel.track.persist.BaseTMSProjectExchangePeer
    implements MsProjectExchangeDAO
{
	private static final Logger LOGGER = LogManager.getLogger(TMSProjectExchangePeer.class);
	
	public static final long serialVersionUID = 400L;
	
	/**
	 * Loads an TMSProjectExchangeBean by primary key
	 * @param objectID
	 * @return 
	 */
	@Override
	public TMSProjectExchangeBean loadByPrimaryKey(Integer objectID) {
		TMSProjectExchange msProjectExchange = null;
    	try {
    		msProjectExchange = retrieveByPK(objectID);    		
    	} catch(Exception e) {
    		LOGGER.warn("Loading of an msProjectExchange by primary key " + objectID + " failed with " + e.getMessage());
    		LOGGER.debug(ExceptionUtils.getStackTrace(e));
    	} 
    	if (msProjectExchange!=null) {
			return msProjectExchange.getBean();
		}
		return null;
	}
		
	/**
	 * Loads an TMSProjectExchangeBean by  project or release
	 * @param entityID
	 * @param entityType
	 * @param exchangeDirection
	 * @return
	 */
	@Override
	public List<TMSProjectExchangeBean> loadByProjectOrRelease(Integer entityID, int entityType, int exchangeDirection) {
		List msProjectExchangeBeans = new ArrayList();
        Criteria crit = new Criteria();
        crit.addDescendingOrderByColumn(LASTEDIT);
        crit.add(ENTITYID, entityID);
        crit.add(ENTITYTYPE, entityType); 
        crit.add(EXCHANGEDIRECTION, exchangeDirection);
        try {
        	msProjectExchangeBeans = doSelect(crit);
    	} catch(Exception e) {
    		LOGGER.error("Loading MsProjectTaskBean by entityID " + entityID + " entityType " + entityType + " failed with " + e.getMessage());
    	}    	
    	return convertTorqueListToBeanList(msProjectExchangeBeans);
	}			
	
	/**
	 * Saves an TMSProjectExchangeBean
	 * @param mSProjectTaskBean
	 * @return
	 */
	@Override
	public Integer save(TMSProjectExchangeBean mSProjectExchangeBean) {
		try {			
			TMSProjectExchange msProjectExchange = BaseTMSProjectExchange.createTMSProjectExchange(mSProjectExchangeBean);
			msProjectExchange.save();
			return msProjectExchange.getObjectID();						
		} catch (Exception e) {
			LOGGER.error("Saving of a msProjectExchange failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Deletes an TMSProjectExchangeBean
	 * @param objectID
	 */
	@Override
	public void delete(Integer objectID) {
		Criteria crit = new Criteria();
        crit.add(OBJECTID, objectID);
        try {
            doDelete(crit);
        } catch (TorqueException e) {
        	LOGGER.error("Deleting the msProjectExchange " + objectID + " failed with: " + e);
        }
	}
	
	private static List convertTorqueListToBeanList(List torqueList) {		
		List beanList = new ArrayList();
		if (torqueList!=null){
			Iterator itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				beanList.add(((TMSProjectExchange)itrTorqueList.next()).getBean());
			}
		}
		return beanList;
	}
}
