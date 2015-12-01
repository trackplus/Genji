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
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TDashboardFieldBean;
import com.aurel.track.beans.TDashboardParameterBean;
import com.aurel.track.dao.DashboardParameterDAO;
/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TDashboardParameterPeer
    extends com.aurel.track.persist.BaseTDashboardParameterPeer
    implements DashboardParameterDAO
{
	private static final Logger LOGGER = LogManager.getLogger(TDashboardParameterPeer.class);
	public static final long serialVersionUID = 400L;
	
	/**
	 * Loads all dashboardParameterBean by parent
	 * @param parentID
	 * @return
	 */
	@Override
	public List loadByParent(Integer parentID) {
		List torqueList = new ArrayList();
	    Criteria crit = new Criteria();
	    crit.add(DASHBOARDFIELD, parentID);
	        try {
	            torqueList = doSelect(crit);
	        } catch (TorqueException e) {
	        	LOGGER.error("Getting the parameters by field " + parentID + " failed with " + e.getMessage());
	        }
	        return convertTorqueListToBeanList(torqueList);
	}
	
	/**
	 * Save  parameter in the TDashboardParameter table
	 * @param dashboardParameterBean
	 * @return
	 */
	@Override
	public Integer save(TDashboardParameterBean dashboardParameterBean) {
		try {			
			TDashboardParameter tDashboardParameter = BaseTDashboardParameter.createTDashboardParameter(dashboardParameterBean);
			tDashboardParameter.save();
			Integer objectID = tDashboardParameter.getObjectID();			
			return objectID;
		} catch (Exception e) {
			LOGGER.error("Saving of a dashboardParameter failed with " + e.getMessage());
			return null;
		}
	}


	/**
	 * Deletes a dashboardParameterBean from the TDashboardParameter table 
	 * @param objectID
	 */
	@Override
	public void delete(Integer objectID) {
		Criteria crit = new Criteria();
        crit.add(OBJECTID, objectID);        
        try {
            doDelete(crit);
        } catch (TorqueException e) {
        	LOGGER.error("Deleting the dashboardParameter with ID " + 
        			objectID + " failed with: " + e);
        }  
	}


	
	
	public static List getByDashboardField(Integer fieldID, Connection con){
        List torqueList = new ArrayList();
        Criteria crit = new Criteria();
        crit.add(DASHBOARDFIELD,fieldID);
        try {
            torqueList = doSelect(crit,con);
        } catch (TorqueException e) {
            LOGGER.debug(ExceptionUtils.getStackTrace(e));
        }
        return torqueList;
    }
    public static void deleteByDashboardField(Integer fieldID, Connection con){
        Criteria crit = new Criteria();
        crit.add(DASHBOARDFIELD,fieldID);
        try {
            doDelete(crit,con);
        } catch (TorqueException e) {
            LOGGER.debug(ExceptionUtils.getStackTrace(e));
        }
    }
    public static void saveParameters(TDashboardFieldBean field, Connection con){
        Integer fieldID=field.getObjectID();
        List oldParams=getByDashboardField(fieldID,con);
        if(oldParams==null){//in order to avoid nullPointerException
            oldParams=new ArrayList();
        }
        Map params=field.getParametres();
        if(params==null){
            for (int i = 0; i < oldParams.size(); i++) {
                TDashboardParameter o =  (TDashboardParameter)oldParams.get(i);
                try {
                    doDelete(SimpleKey.keyFor(o.getObjectID()),con);
                } catch (TorqueException e) {
                    LOGGER.debug(ExceptionUtils.getStackTrace(e));
                }
            }
            return;
       }
       Iterator it=params.keySet().iterator();
       while(it.hasNext()){
           String param=(String)it.next();
           boolean found=false;
           for(int i=0;i<oldParams.size();i++){
               TDashboardParameter o =  (TDashboardParameter)oldParams.get(i);
               if(o.getName().equals(param)){
                   o.setParamValue((String)params.get(param));
                   found=true;
                   try {
                       o.save(con);
                   } catch (TorqueException e) {
                       LOGGER.debug(ExceptionUtils.getStackTrace(e));
                   }
                   oldParams.remove(o);
                   break;
               }
           }
           if(found==false){
               TDashboardParameter o = new TDashboardParameter();
               o.setName(param);
               o.setParamValue((String)params.get(param));
               try {
                   o.setDashboardField(field.getObjectID());
               } catch (TorqueException e) {
                   LOGGER.debug(ExceptionUtils.getStackTrace(e));  //To change body of catch statement use File | Settings | File Templates.
               }
               try {
                   o.save(con);
               } catch (TorqueException e) {
                   LOGGER.debug(ExceptionUtils.getStackTrace(e));
               }
           }
       }
       //remove old parameters (if exists)
       for (int i = 0; i < oldParams.size(); i++) {
            TDashboardParameter o =  (TDashboardParameter)oldParams.get(i);
            try {
                doDelete(SimpleKey.keyFor(o.getObjectID()),con);
            } catch (TorqueException e) {
                LOGGER.debug(ExceptionUtils.getStackTrace(e));
            }
       }
    }
    
    private List convertTorqueListToBeanList(List torqueList) {
		List beanList = new ArrayList();
		if (torqueList!=null){
			Iterator itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				beanList.add(((TDashboardParameter)itrTorqueList.next()).getBean());
			}
		}
		return beanList;
	}
}
