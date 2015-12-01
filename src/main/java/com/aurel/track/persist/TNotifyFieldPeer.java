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

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TNotifyFieldBean;
import com.aurel.track.dao.NotifyFieldDAO;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TNotifyFieldPeer
	extends com.aurel.track.persist.BaseTNotifyFieldPeer
	implements NotifyFieldDAO {
	private static final Logger LOGGER = LogManager.getLogger(TNotifyTriggerPeer.class);
	
	public static final long serialVersionUID = 400L;
	
	/**
	 * Get the notifyFieldBeans, the person requests 
	 * notification for, for a project, actionType and fieldType 
	 * @param person
	 * @param projectIDs typically one single project but if the project 
	 * 	itself is modified then both the old and the new project
	 * @param actionType
	 * @param fieldType  
	 * @return
	 */	
	@Override
	public List<TNotifyFieldBean> getTriggerFieldsForRaciRole(Integer person, List<Integer> projectIDs, Integer actionType, Integer fieldType) {
		if (projectIDs==null || projectIDs.isEmpty() || actionType==null) {
			LOGGER.error("Getting the notification fields for person " + person + 
					" projects " + projectIDs + " actionType " + actionType + " fieldType " + fieldType + " failed with null arguments" );
			return new LinkedList<TNotifyFieldBean>();
		}
		Criteria criteria = new Criteria();
		criteria.addJoin(NOTIFYTRIGGER, TNotifySettingsPeer.NOTIFYTRIGGER);
		criteria.add(ACTIONTYPE, actionType);
		if (fieldType!=null) {
			//fieldType could be null for example for create issue
			criteria.add(FIELDTYPE, fieldType);
		}
		if (person==null) {
			criteria.add(TNotifySettingsPeer.PERSON, (Object)null, Criteria.ISNULL);
		} else {
			criteria.add(TNotifySettingsPeer.PERSON, person);
		}
		criteria.addIn(TNotifySettingsPeer.PROJECT, projectIDs);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Getting the notification fields for person " + person + 
					" project " + projectIDs.get(0) + " actionType " + actionType + " fieldType " + fieldType + "failed with " + e.getMessage(), e);
			return null;
		}
	}
		
	/**
	 * Saves a TNotifyFieldBean
	 * @param notifyFieldBean   
	 * @return
	 */
	@Override
	public Integer save(TNotifyFieldBean notifyFieldBean) {
		try {
			TNotifyField tNotifyField = BaseTNotifyField.createTNotifyField(notifyFieldBean);
			tNotifyField.save();
			return tNotifyField.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving the triggering fields for trigger " + notifyFieldBean.getNotifyTrigger() + 
					" and field " + notifyFieldBean.getField() + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	
	/**
	 * Gets the list of notifyFieldBeans for a trigger 
	 * @param triggerID 
	 * @return
	 */
	@Override
	public List<TNotifyFieldBean> getNotifyFieldsForTrigger(Integer triggerID) {
		Criteria criteria = new Criteria();
		criteria.add(NOTIFYTRIGGER, triggerID);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Getting the fields defined in trigger " + triggerID + " failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Gets the list of edit notifyFieldBeans by IDs 
	 * @param notifyTriggerFieldIDs 
	 * @return
	 */
	@Override
	public List<TNotifyFieldBean> getNotifyFieldsByKeys(List<Integer> notifyTriggerFieldIDs) {
		if (notifyTriggerFieldIDs==null || notifyTriggerFieldIDs.isEmpty()) {
			return new LinkedList<TNotifyFieldBean>();
		}
		Criteria criteria = new Criteria();
		criteria.addIn(OBJECTID, notifyTriggerFieldIDs);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Getting the notify fields by ids failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Deletes all entries assigned to a triggerID
	 * @param triggerID 
	 * @return
	 */
	@Override
	public void deleteByTrigger(Integer triggerID) {
		if (triggerID==null) {
			return;
		}		
		Criteria criteria = new Criteria();
		criteria.add(NOTIFYTRIGGER, triggerID);
		try {
			doDelete(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the previous triggering fields for a trigger failed with " + e.getMessage());
		}
	}
	
	/**
	 * Deletes the field assigned to triggers
	 * @param fieldID
	 * @param action probably ACTIONTYPE.EDIT_ISSUE 
	 * @param fieldType probably FIELDTYPE.ISSUE_FIELD
	 */
	@Override
	public void deleteByField(Integer fieldID, int action, int fieldType) {
		if (fieldID==null) {
			return;
		}		
		Criteria criteria = new Criteria();
		criteria.add(FIELD, fieldID);
		criteria.add(ACTIONTYPE, action);
		criteria.add(FIELDTYPE, fieldType);
		try {
			doDelete(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the field for triggers failed with " + e.getMessage());
		}
	}
	
	/**
	 * Deletes the field assigned to triggers
	 * @param fieldID
	 * @param action probably ACTIONTYPE.EDIT_ISSUE 
	 * @param fieldType probably FIELDTYPE.ISSUE_FIELD
	 */
	public void updateByField(Integer oldFieldID, Integer newFieldID, int action, int fieldType) {
		if (oldFieldID==null) {
			return;
		}		
		Criteria selectCriteria = new Criteria();
		Criteria updateCriteria = new Criteria();
		selectCriteria.add(FIELD, oldFieldID);
		selectCriteria.add(ACTIONTYPE, action);
		selectCriteria.add(FIELDTYPE, fieldType);
		updateCriteria.add(FIELD, newFieldID);
		try {
			doUpdate(selectCriteria, updateCriteria);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the field for triggers failed with " + e.getMessage());
		}
	}
	
	/**
	 * Deletes a trigger field by objectID
	 * @param objectID
	 */
	@Override
	public void delete(Integer objectID) {
		Criteria crit = new Criteria();
		crit.add(OBJECTID, objectID);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the trigger field " + objectID + " failed with: " + e);
		}
	}
	
	/**
	 * Converts a list of TOption torque objects to a list of TOptionBean objects 
	 * @param torqueList
	 * @return
	 */
	private static List<TNotifyFieldBean> convertTorqueListToBeanList(List<TNotifyField> torqueList) {
		List<TNotifyFieldBean> beanList = new LinkedList<TNotifyFieldBean>();
		if (torqueList!=null) {
			for (TNotifyField notifyField : torqueList) {
				beanList.add(notifyField.getBean());
			}
		}
		return beanList;
	}
}
