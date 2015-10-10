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
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Criteria.Criterion;

import com.aurel.track.beans.TNotifyTriggerBean;
import com.aurel.track.dao.NotifyTriggerDAO;
import com.aurel.track.fieldType.constants.BooleanFields;



/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TNotifyTriggerPeer
    extends com.aurel.track.persist.BaseTNotifyTriggerPeer
    implements NotifyTriggerDAO    
{
	public static final long serialVersionUID = 400L;
	
	private static final Logger LOGGER = LogManager.getLogger(TNotifyTriggerPeer.class);	
	
	private static Class[] replacePeerClasses = {		
    	TNotifySettingsPeer.class,     	    	
    };
    
    private static String[] replaceFields = {
    	TNotifySettingsPeer.NOTIFYTRIGGER,     	    	    	    	    
    };
	
    
    private static Class[] deletePeerClassesWithoutNotifySettings = {
    	TNotifyFieldPeer.class,
    	//use the superclass doDelete() method!!!
    	BaseTNotifyTriggerPeer.class
    };
    
    private static String[] deleteFieldsWithoutNotifySettings = {  
    	TNotifyFieldPeer.NOTIFYTRIGGER,
    	BaseTNotifyTriggerPeer.OBJECTID,    	
    };
    
    private static Class[] deletePeerClassesWithNotifySettings = {
    	TNotifySettingsPeer.class,
    	TNotifyFieldPeer.class,
    	//use the superclass doDelete() method!!!
    	BaseTNotifyTriggerPeer.class
    };
    
    private static String[] deleteFieldsWithNotifySettings = {
    	TNotifySettingsPeer.NOTIFYTRIGGER, 
    	TNotifyFieldPeer.NOTIFYTRIGGER,
    	BaseTNotifyTriggerPeer.OBJECTID,    	
    };
    
	/**
	 * Loads a notifyTriggerBean by primary key
	 * @param objectID
	 * @return
	 */
	public TNotifyTriggerBean loadByPrimaryKey(Integer objectID) {
		TNotifyTrigger tNotifyTrigger = null;
    	try {
    		tNotifyTrigger = retrieveByPK(objectID);    		
    	} catch(Exception e) {
    		LOGGER.warn("Loading of a notificationTrigger by primary key " + objectID + " failed with " + e.getMessage());
    		LOGGER.debug(ExceptionUtils.getStackTrace(e));
    	}
    	if (tNotifyTrigger!=null) {
			return tNotifyTrigger.getBean();
		}
		return null;
	}

	/**
	 * Loads a list of notifyTriggerBean created by a person 
	 * @param personID
	 * @param includeOwn whether do we include also the own triggers
	 * @return
	 */
	public List<TNotifyTriggerBean> loadSystemAndOwn(Integer personID, boolean includeOwn) {		
		Criteria criteria = new Criteria();
		if (includeOwn) {
			Criterion personTriggerCrit = criteria.getNewCriterion(PERSON, personID, Criteria.EQUAL);
			Criterion systemTriggerCrit = criteria.getNewCriterion(ISSYSTEM, BooleanFields.TRUE_VALUE, Criteria.EQUAL);
			criteria.add(personTriggerCrit.or(systemTriggerCrit));
		} else {
			criteria.add(ISSYSTEM, BooleanFields.TRUE_VALUE);
		}
		criteria.addAscendingOrderByColumn(PERSON);
		criteria.addAscendingOrderByColumn(LABEL);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading of the notificationTriggers by person " + personID + " and includeSystem " + includeOwn + " failed with " + e.getMessage(), e);
			return null;
		}		
	}
	
	/**
	 * Loads a list of notifyTriggerBeans set for any project: 
	 * for the personID (own) or those not linked to any person (default)  
	 * @param personID
	 * @return
	 */
	public List<TNotifyTriggerBean> loadByOwnOrDefaultNotifySettings(Integer personID) {
		List notifyTriggers = new ArrayList();
		Criteria criteria = new Criteria();
		criteria.addJoin(OBJECTID, BaseTNotifySettingsPeer.NOTIFYTRIGGER);
		Criterion personCrit = criteria.getNewCriterion(BaseTNotifySettingsPeer.PERSON, personID, Criteria.EQUAL);
		Criterion defaultCrit = criteria.getNewCriterion(BaseTNotifySettingsPeer.PERSON, (Object)null, Criteria.ISNULL);
		criteria.add(personCrit.or(defaultCrit));	
		criteria.setDistinct();		
		try {
			notifyTriggers = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading of the notificationTriggers with notify assignments " +
					"either as default or by person " + personID + " failed with " + e.getMessage(), e);
		}
		return convertTorqueListToBeanList(notifyTriggers);		
	}
	
	/**
	 * Loads a list of notifyTriggerBeans set for any project 
	 * which is not linked to any person (default)
	 * @return
	 */
	public List<TNotifyTriggerBean> loadByDefaultNotifySettings() {
		List notifyTriggers = new ArrayList();
		Criteria criteria = new Criteria();
		criteria.addJoin(OBJECTID, BaseTNotifySettingsPeer.NOTIFYTRIGGER);
		criteria.add(BaseTNotifySettingsPeer.PERSON, (Object)null, Criteria.ISNULL);
		criteria.setDistinct();		
		try {
			notifyTriggers = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading of the notificationTriggers " +
					"having default notify assignments failed with " + e.getMessage(), e);
		}
		return convertTorqueListToBeanList(notifyTriggers);		
	}
	
	/**
	 * Saves a notifyTriggerBean in the database
	 * @param notifyTriggerBean 
	 * @return
	 */
	public Integer save(TNotifyTriggerBean notifyTriggerBean) {
		TNotifyTrigger tNotifyTrigger;		
		try {
			tNotifyTrigger = BaseTNotifyTrigger.createTNotifyTrigger(notifyTriggerBean);
			tNotifyTrigger.save();
			return tNotifyTrigger.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a notificationTrigger failed with " + e.getMessage(), e);
			return null;
		}		
	}

	/**
	 * Deletes a notifyTriggerBean from the database
	 * @param objectID 
	 * @return
	 */
	public void delete(Integer objectID) {
		ReflectionHelper.delete(deletePeerClassesWithoutNotifySettings, deleteFieldsWithoutNotifySettings, objectID);
	}
	
	/**
	 * Delete all own triggers of a person
	 * @param personID
	 */
	public void deleteOwnTriggers(Integer personID) {
		Criteria criteria = new Criteria();        
        criteria.add(BaseTNotifyTriggerPeer.PERSON, personID);
        criteria.add(BaseTNotifyTriggerPeer.ISSYSTEM, (Object)BooleanFields.TRUE_VALUE, Criteria.NOT_EQUAL);
        doDelete(criteria);        
	}
	
	/**
	 * Deletes the TQueryRepositoryBean satisfying a certain criteria 
	 * together with the dependent database entries 
	 * @param crit
	 */
	public static void doDelete(Criteria crit) {
		List list = null;
		try {
			list = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Getting the list of TNotifyTriggers to be deleted failed with " + e.getMessage(), e);
		}			
        if (list == null || list.size() < 1) {
            return;
        }
		Iterator<TNotifyTrigger> iter = list.iterator();
		TNotifyTrigger notifyTrigger = null;
		while(iter.hasNext()) {
			notifyTrigger = iter.next();			
			ReflectionHelper.delete(deletePeerClassesWithNotifySettings, deleteFieldsWithNotifySettings, notifyTrigger.getObjectID());			
		}
	}
	
	/**
	 * Returns whether a queryRepositoryBean can be deleted
	 * @param objectID 
	 * @return
	 */
	public boolean isDeletable(Integer objectID) {
		return !ReflectionHelper.hasDependentData(replacePeerClasses, replaceFields, objectID); 
	}

	/**
	 * Whether the personID has right to delete the objectID trigger
	 * @param objectID
	 * @param personID
	 * @return
	 */
	public boolean isAllowedToDelete(Integer objectID, Integer personID) {
		List triggers = null; 
		Criteria crit = new Criteria();		
        crit.add(OBJECTID, objectID);
        crit.add(PERSON, personID);
        try {
        	triggers = doSelect(crit);
        } catch (TorqueException e) {
        	LOGGER.error("Testing whether it is allowed to delete the notificationTrigger " + 
        			objectID + " for person " + personID + " failed with: " + e.getMessage(), e);        	
        }  
        return triggers!=null && !triggers.isEmpty();
	}
	
	/**
	 * Replaces all occurences of the oldID with the newID and then deletes the oldID queryRepositoryBean
	 * @param oldID
	 * @param newID
	 * @return
	 */
	public void replaceAndDelete(Integer oldID, Integer  newID) {    	
    	if (newID!=null) {
    		ReflectionHelper.replace(replacePeerClasses, replaceFields, oldID, newID);
    	}
    	ReflectionHelper.delete(deletePeerClassesWithoutNotifySettings, deleteFieldsWithoutNotifySettings, oldID);
    }
	
	/**
	 * Converts a list of TNotificationTrigger torque objects to a list of TNotificationTriggerBean objects 
	 * @param torqueList
	 * @return
	 */
	private static List<TNotifyTriggerBean> convertTorqueListToBeanList(List<TNotifyTrigger> torqueList) {		
		List<TNotifyTriggerBean> beanList = new ArrayList<TNotifyTriggerBean>();		
		if (torqueList!=null) {
			Iterator<TNotifyTrigger> itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()) {
				TNotifyTrigger notifyTrigger = itrTorqueList.next();
				beanList.add(notifyTrigger.getBean());
			}
		}
		return beanList;
	}
}
