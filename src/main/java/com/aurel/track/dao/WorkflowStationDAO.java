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

import com.aurel.track.beans.TWorkflowStationBean;

/**
 */
public interface WorkflowStationDAO {
	/**
	 * Gets a workflowStationBean by primary key
	 * @param objectID
	 * @return
	 */
	TWorkflowStationBean loadByPrimaryKey(Integer objectID);

	/**
	 * Gets all workflowStationBeans
	 * @return
	 */
	List<TWorkflowStationBean> loadAll();
	
	/**
	 * Loads the workflowStationBeans for a workflow
	 * @param workflowID
	 * @return
	 */
	List<TWorkflowStationBean> loadByWorkflow(Integer workflowID);

	
	/**
	 * Loads all possible transitions from fromState in workflow
	 * @param workflowID 
	 * @param fromStatus
	 * @return
	 */
	TWorkflowStationBean loadFromStatusStation(Integer workflowID, Integer fromStatus);
	
	/**
	 * Loads all possible transitions from fromState in workflow
	 * @param workflowID 
	 * @param fromStationType
	 * @return
	 */
	TWorkflowStationBean loadFromStationTypeStation(Integer workflowID, Integer fromStationType);
	
	/**
	 * Loads all possible toStations from fromStation for a triggerEvent
	 * @param fromStation
	 * @param triggerEvent
	 * @param includeHooks whether to include the transitions to the same station
	 * @return
	 */
	List<TWorkflowStationBean> loadToStationsFromStation(Integer fromStation, Integer triggerEvent, boolean includeHooks);
	
	/**
	 * Saves a workflow
	 * @param workflowStationBean
	 * @return
	 */
	Integer save(TWorkflowStationBean workflowStationBean);


	/**
	 * Deletes a workflow
	 * Is deletable should return true before calling this method
	 * @param objectID
	 */
	void delete(Integer objectID);
}
