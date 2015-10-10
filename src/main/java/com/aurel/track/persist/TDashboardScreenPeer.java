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

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Criteria.Criterion;
import org.apache.torque.util.Transaction;

import com.aurel.track.beans.TDashboardScreenBean;
import com.aurel.track.dao.DashboardScreenDAO;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TDashboardScreenPeer
    extends com.aurel.track.persist.BaseTDashboardScreenPeer implements DashboardScreenDAO {
    private static final Logger LOGGER = LogManager.getLogger(TDashboardScreenPeer.class);
	public static final long serialVersionUID = 400L;

	
    public TDashboardScreenBean tryToLoadByPrimaryKey(Integer objectID) {
        TDashboardScreen tobject = null;
 		try{
 			tobject = retrieveByPK(objectID);
 		}
 		catch(Exception e){
 			LOGGER.debug("Could not retrieve dashboard screen by primary key " + objectID + ": " + e.getMessage(), e);
 		}
 		if (tobject!=null){
 			return tobject.getBean();
 		}
 		return null;
    }
    
    public TDashboardScreenBean loadByPrimaryKey(Integer objectID) {
       TDashboardScreen tobject = null;
		try{
			tobject = retrieveByPK(objectID);
		}
		catch(Exception e){
			LOGGER.info("Loading of a dashboard screen by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (tobject!=null){
			return tobject.getBean();
		}
		return null;
    }

    public TDashboardScreenBean loadByPersonKey(Integer objectID) {
        List torqueList = null;
		Criteria crit = new Criteria();
        crit.add(PERSONPKEY,objectID);
        crit.add(PROJECT,(Object)null,Criteria.ISNULL);
        crit.add(ENTITYTYPE,(Object)null,Criteria.ISNULL);
        try {
			torqueList = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Loading of a dashboard screen by person key " + objectID + " failed with " + e.getMessage(), e);
		}
        if(torqueList==null || torqueList.isEmpty()){
        	return null;
        } else {
        	TDashboardScreenBean dashboardScreenBean = ((TDashboardScreen)torqueList.get(0)).getBean();
        	/*if (dashboardScreenBean.getSystemScreenRef()!=null) {
        		return loadByPrimaryKey(dashboardScreenBean.getSystemScreenRef());
        	} else {*/
        		return dashboardScreenBean;
        	//}
        }
    }
    public TDashboardScreenBean loadByProject(Integer projectID,Integer entityType){
    	List torqueList = null;
		Criteria crit = new Criteria();
        crit.add(PROJECT,projectID);
        crit.add(ENTITYTYPE,entityType);
        try {
			torqueList = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Loading of a dashboard screen by project  " + projectID + " failed with " + e.getMessage(), e);
		}
        if(torqueList==null || torqueList.isEmpty()){
        	return null;
        } else {
        	TDashboardScreenBean dashboardScreenBean = ((TDashboardScreen)torqueList.get(0)).getBean();
        	/*if (dashboardScreenBean.getSystemScreenRef()!=null) {
        		return loadByPrimaryKey(dashboardScreenBean.getSystemScreenRef());
        	} else {*/
        		return dashboardScreenBean;
        	//}
        }
    }
    
    /**
	 * Gets the default screenBean from the TDashboardScreen table
	 * (that with personID null) 
	 * @return
	 */
	public TDashboardScreenBean loadDefault() {
		List torqueList = null;
		Criteria crit = new Criteria();
        crit.add(PERSONPKEY, (Object)null, Criteria.ISNULL);
		crit.add(PROJECT,(Object)null,Criteria.ISNULL);
		crit.add(ENTITYTYPE,(Object)null,Criteria.ISNULL);
        try {
			torqueList = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Loading the default dashboard screen failed with " + e.getMessage(), e);
		}
        if(torqueList==null || torqueList.isEmpty()){
            return  null;
        } else {
        	if (torqueList.size()>1) {
        		LOGGER.warn("More than one default dashboardScreen found");
        	}
        }
		return ((TDashboardScreen)torqueList.get(0)).getBean();
	}
	
    public Integer save(TDashboardScreenBean bean) {
        try {
			TDashboardScreen tobject = BaseTDashboardScreen.createTDashboardScreen(bean);
			if(tobject.getLabel()==null){
				tobject.setLabel(tobject.getName());
			}
			tobject.save();
			return tobject.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a dashboard screen failed with " + e.getMessage(), e);
			return null;
		}		
    }
    
    /**
	 * Deletes the TDashboardScreen satisfying a certain criteria 
	 * together with the dependent database entries 
	 * @param crit
	 */
	public static void doDelete(Criteria crit) {
		List<TDashboardScreen> list = null;
		try {
			list = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Getting the list of TNotifyTriggers to be deleted failed with " + e.getMessage(), e);
		}			
        if (list == null || list.size() < 1) {
            return;
        }
        for (TDashboardScreen dashboardScreen : list) {
			Integer objectID = dashboardScreen.getObjectID();
			Connection con = null;
			try{
				con = Transaction.begin(DATABASE_NAME);
				deleteChildren(objectID, con);
				doDelete(SimpleKey.keyFor(objectID),con);
				Transaction.commit(con);
			} catch (TorqueException e) {
				Transaction.safeRollback(con);
				LOGGER.error("Deleting a screen for key from doDelete " + objectID + " failed with: " + e);
			} 			
		}
	}
    
    /**
	 * Deletes a screen by primary key
	 * Is deletable should return true before calling this method
	 * @param objectID
	 */
	public void delete(Integer objectID) {
		Connection con = null;
		try{
			con = Transaction.begin(DATABASE_NAME);
			deleteChildren(objectID, con);
			doDelete(SimpleKey.keyFor(objectID),con);
			Transaction.commit(con);
		} catch (TorqueException e) {
			Transaction.safeRollback(con);
			LOGGER.error("Deleting a screen for key " + objectID + " failed with: " + e);
		}  
	}
	/**
	 * delete all children and then delete the object
	 * @param objectID
	 * @param con
	 * @throws TorqueException
	 */
	public static void deleteChildren(Integer objectID,Connection con) throws TorqueException {
		//delete also all children
		LOGGER.debug("Deleting children for screen:"+objectID+"...");
		Criteria critTabs = new Criteria();
		critTabs.add(BaseTDashboardTabPeer.PARENT,objectID);
		List tabs=BaseTDashboardTabPeer.doSelect(critTabs,con);
		for (Iterator iterTabs = tabs.iterator(); iterTabs.hasNext();) {
			TDashboardTab tab=(TDashboardTab) iterTabs.next();
			//delete the children for the tab
			TDashboardTabPeer.deleteChildren(tab.getObjectID(), con);
			//delete the tab
			BaseTDashboardTabPeer.doDelete(SimpleKey.keyFor(tab.getObjectID()),con);
		}
	}
	
   

    public List<TDashboardScreenBean> loadByProjects(int[] projects) {
    	List result = new ArrayList();
    	if (projects==null || projects.length==0) {
    		return result;
    	}
    	Criteria criteria=new Criteria();
    	criteria.addIn(PROJECT, projects);			
    	try {
    		result.addAll(doSelect(criteria));
    	} catch(Exception e) {
    		LOGGER.error("Loading the screens by projects failed with " + e.getMessage());
    		LOGGER.debug(ExceptionUtils.getStackTrace(e));
    	}			
    	return convertTorqueListToBeanList(result);
    }
    
    public List<TDashboardScreenBean> loadMyAndPublicTemplates(List<Integer> personIDs) {
		Criteria criteria=new Criteria();
		criteria.add(PERSONPKEY, (Object)null, Criteria.ISNULL);
		criteria.add(PROJECT, (Object)null, Criteria.ISNULL);
		criteria.add(ENTITYTYPE, (Object)null, Criteria.ISNULL);
		Criterion criterionOwnerIn = criteria.getNewCriterion(OWNER, personIDs, Criteria.IN);
		Criterion criterionNoOwner = criteria.getNewCriterion(OWNER, null, Criteria.ISNULL);
		criteria.add(criterionOwnerIn.or(criterionNoOwner));
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch(Exception e) {
			LOGGER.error("Loading not assigned dashboard screens failed with " + e.getMessage());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			return null;
		}
	}
    
	public List<TDashboardScreenBean> loadAllTemplates() {
		Criteria criteria=new Criteria();
		criteria.add(PERSONPKEY,(Object)null,Criteria.ISNULL);
		criteria.add(PROJECT,(Object)null,Criteria.ISNULL);
		criteria.add(ENTITYTYPE,(Object)null,Criteria.ISNULL);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch(Exception e) {
			LOGGER.error("Loading not assigned dashboard screens failed with " + e.getMessage());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			return null;
		}
	}
    
	 private static List<TDashboardScreenBean> convertTorqueListToBeanList(List<TDashboardScreen> torqueList) {		
			List<TDashboardScreenBean> beanList = new ArrayList<TDashboardScreenBean>();
			if (torqueList!=null) {
				for (TDashboardScreen tDashboardScreen : torqueList) {
					beanList.add(tDashboardScreen.getBean());
				}
			}
			return beanList;
		}
}
