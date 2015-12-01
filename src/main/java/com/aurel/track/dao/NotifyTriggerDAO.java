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


package com.aurel.track.dao;

import java.util.List;

import com.aurel.track.beans.TNotifyTriggerBean;

public interface NotifyTriggerDAO {

	/**
	 * Loads a notifyTriggerBean by primary key
	 * @param objectID
	 * @return
	 */
	TNotifyTriggerBean loadByPrimaryKey(Integer objectID);

	/**
	 * Loads a list of notifyTriggerBean created by a person 
	 * @param personID
	 * @param includeOwn whether do we include also the own defined triggers
	 * @return
	 */
	List<TNotifyTriggerBean> loadSystemAndOwn(Integer personID, boolean includeOwn);
	
	/**
	 * Loads a list of notifyTriggerBeans set for any project: 
	 * for the personID (own) or those not linked to any person (default)  
	 * @param personID
	 * @return
	 */
	List<TNotifyTriggerBean> loadByOwnOrDefaultNotifySettings(Integer personID);
	
	/**
	 * Loads a list of notifyTriggerBeans set for any project 
	 * which is not linked to any person (default)
	 * @return
	 */
	List<TNotifyTriggerBean> loadByDefaultNotifySettings();
	
	/**
	 * Saves a notifyTriggerBean in the database
	 * @param notifyTriggerBean 
	 * @return
	 */
	Integer save(TNotifyTriggerBean notifyTriggerBean);
		
	
	/**
	 * Deletes a notifyTriggerBean from the database
	 * @param objectID 
	 * @return
	 */
	void delete(Integer objectID);

	/**
	 * Delete all own triggers of a person
	 * @param personID
	 */
	void deleteOwnTriggers(Integer personID);
	
	/**
	 * Whether the personID has right to delete the objectID trigger
	 * @param objectID
	 * @param personID
	 * @return
	 */
	boolean isAllowedToDelete(Integer objectID, Integer personID);
	
	/**
	 * Returns wheteher a notificationTrigger can be deleted
	 * @param objectID 
	 * @return
	 */
	boolean isDeletable(Integer objectID);
	
	/**
	 * Replaces all occurences of the oldID with the newID and then deletes the oldID notifyTriggerBean
	 * @param oldID
	 * @param newID
	 * @return
	 */
	void replaceAndDelete(Integer oldID, Integer  newID); 
}

