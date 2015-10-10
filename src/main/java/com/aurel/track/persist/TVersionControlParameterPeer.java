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
import org.apache.torque.util.Transaction;

import com.aurel.track.beans.TVersionControlParameterBean;
import com.aurel.track.dao.VersionControlParameterDAO;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TVersionControlParameterPeer
    extends com.aurel.track.persist.BaseTVersionControlParameterPeer implements VersionControlParameterDAO {
    
	private static final Logger LOGGER = LogManager.getLogger(TVersionControlParameterPeer.class);
    public static final long serialVersionUID = 400L;
    	
    public static List getByProject(Integer projectID, Connection con){
        List torqueList = new ArrayList();
        Criteria crit = new Criteria();
        crit.add(PROJECT,projectID);
        try {
            torqueList = doSelect(crit,con);
        } catch (TorqueException e) {
            LOGGER.debug(ExceptionUtils.getStackTrace(e));
        }
        return torqueList;
    }
    public static void deleteByProject(Integer projectID, Connection con){
        Criteria crit = new Criteria();
        crit.add(PROJECT,projectID);
        try {
            doDelete(crit,con);
        } catch (TorqueException e) {
            LOGGER.debug(ExceptionUtils.getStackTrace(e));
        }
    }
    public static void saveParameters(Integer projectID,Map params,Connection con){
        List oldParams=getByProject(projectID,con);
        if(oldParams==null){//in order to avoid nullPointerException
            oldParams=new ArrayList();
        }
        if(params==null){
            for (int i = 0; i < oldParams.size(); i++) {
                TVersionControlParameter o =  (TVersionControlParameter)oldParams.get(i);
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
               TVersionControlParameter o =  (TVersionControlParameter)oldParams.get(i);
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
               TVersionControlParameter o = new TVersionControlParameter();
               o.setName(param);
               o.setParamValue((String)params.get(param));
               try {
                   o.setProject(projectID);
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
            TVersionControlParameter o =  (TVersionControlParameter)oldParams.get(i);
            try {
                doDelete(SimpleKey.keyFor(o.getObjectID()),con);
            } catch (TorqueException e) {
                LOGGER.debug(ExceptionUtils.getStackTrace(e));
            }
       }
    }

    public List<TVersionControlParameterBean> getByProject(Integer projectID) {
        Connection con = null;
        List<TVersionControlParameterBean> beanList=new ArrayList<TVersionControlParameterBean>();
        try {
            con = Transaction.begin(DATABASE_NAME);
            List torqueList=getByProject(projectID,con);
            Transaction.commit(con);
            beanList=convertTorqueListToBeanList(torqueList);
        } catch (TorqueException e) {
			Transaction.safeRollback(con);
            LOGGER.error("Loading version control parameters  failed with " + e.getMessage(), e);
		}
        return beanList;
    }

    public void save(Integer projectID, Map vcmap) {
        Connection con = null;
        try {
            con = Transaction.begin(DATABASE_NAME);
            saveParameters(projectID,vcmap,con);
            Transaction.commit(con);
        } catch (TorqueException e) {
			Transaction.safeRollback(con);
            LOGGER.error("Save version control parameters   failed with " + e.getMessage(), e);
		}
    }
    private List<TVersionControlParameterBean> convertTorqueListToBeanList(List torqueList) {
		List<TVersionControlParameterBean> beanList = new ArrayList<TVersionControlParameterBean>();
		if (torqueList!=null){
			Iterator itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				beanList.add(((TVersionControlParameter)itrTorqueList.next()).getBean());
			}
		}
		return beanList;
	}
}
