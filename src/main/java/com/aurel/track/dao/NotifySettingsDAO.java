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


package com.aurel.track.dao;

import java.util.List;

import com.aurel.track.beans.TNotifySettingsBean;

public interface NotifySettingsDAO {

	/**
	 * Loads a notifySettingsBean by primary key
	 * @param objectID
	 * @return
	 */
	TNotifySettingsBean loadByPrimaryKey(Integer objectID);

	/**
	 * Loads a list of notifySettingsBean created explicitely 
	 * by a person or inherited from default 
	 * @param personID 
	 * @return
	 */
	List<TNotifySettingsBean> loadOwnSettings(Integer personID);

	/**
	 * Get the notifySettingsBean defined by a person for a project 
	 * @param personID
	 * @param projectID
	 * @return
	 */
	TNotifySettingsBean loadOwnByPersonAndProject(Integer personID, Integer projectID);
	
	/**
	 * Loads all default projectAssignments
	 * @return
	 */
	List<TNotifySettingsBean> loadAllDefaultAssignments();
	
	/**
	 * Loads a list of notifySettingsBean created for all projectAdmin projects  
	 * @param projectIDs
	 * @return
	 */
	List<TNotifySettingsBean> loadDefaultsByProjects(List<Integer> projectIDs);
	
	/**
	 * Get all notifySettingsBean for a project
	 * @param projectID
	 * @return
	 */
	List<TNotifySettingsBean> loadAllByProject(Integer projectID); 
	
	/**
	 * Get the default notifySettingsBean for a project (person==null)
	 * @param projectID
	 * @return
	 */
	TNotifySettingsBean loadDefaultByProject(Integer projectID);
	
	/**
	 * Saves a notifyTriggerBean in the database
	 * @param notifyTriggerBean 
	 * @return
	 */
	Integer save(TNotifySettingsBean notifySettingsBean);
	
	
	/**
	 * Deletes a notifySettingsBean from the database
	 * @param objectID 
	 * @return
	 */
	void delete(Integer objectID);
	
}

