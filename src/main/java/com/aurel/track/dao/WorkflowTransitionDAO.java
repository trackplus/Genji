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

import com.aurel.track.beans.TWorkflowTransitionBean;

/**
 */
public interface WorkflowTransitionDAO {
	/**
	 * Gets a workflowTransitionBean by primary key
	 * @param objectID
	 * @return
	 */
	TWorkflowTransitionBean loadByPrimaryKey(Integer objectID);

	/**
	 * Loads all workflow Transitions
	 * @return
	 */
	List<TWorkflowTransitionBean> loadAll();

	/**
	 * Loads all transitions for a workflow
	 * @param workflowID
	 * @return
	 */
	List<TWorkflowTransitionBean> loadByWorkflow(Integer workflowID);

	/**
	 * Loads all transitions with transition event
	 * @param transitionEvents
	 * @param workflowIDs
	 * @return
	 */
	List<TWorkflowTransitionBean> loadByTransitionEvent(Integer[] transitionEvents, Object[] workflowIDs);
	
	/**
	 * Loads all transitions for a workflow and states
	 * @param workflowID
	 * @param stateFrom
	 * @param stateTo
	 * @param actionID if it is a direct status change then actionID is null 
	 * @return
	 */
	List<TWorkflowTransitionBean> loadByWorkflowAndStates(Integer workflowID, Integer stateFrom, Integer stateTo, Integer actionID);
	
	/**
	 * Loads all transitions for a workflow from the pseudo create
	 * @param workflowID
	 * @param stationType
	 * @param stateTo
	 * @param actionID if it is a direct status change then actionID is null 
	 * @return
	 */
	List<TWorkflowTransitionBean> loadFromCreate(Integer workflowID, Integer stationType, Integer stateTo, Integer actionID);
	
	/**
	 * Load all possible transitions from fromStation in workflow with an escalationEvent
	 * @param fromStation
	 * @param triggerEvent
	 * @param includeHooks whether to include the transitions to the same station
	 * @return
	 */
	List<TWorkflowTransitionBean> loadFromStation(Integer fromStation, Integer triggerEvent, boolean includeHooks);
	
	/**
	 * Gets the workflow transitions starting from the pseudo create station
	 * @param workflowID
	 * @param stationType
	 * @return
	 */
	//List<TWorkflowTransitionBean> loadFromPseudoCreateStation(Integer workflowID, Integer stationType);
	
	/**
	 * Whether workflow contains stationID (either as from or to)
	 * @param stationID
	 * @return
	 */
	boolean stationInWorkflow(Integer workflowID, Integer stationID);

	/**
	 * Saves a workflow
	 * @param workflowTransitionBean
	 * @return
	 */
	Integer save(TWorkflowTransitionBean workflowTransitionBean);

	/**
	 * Deletes a workflow
	 * Is deletable should return true before calling this method
	 * @param objectID
	 */
	void delete(Integer objectID);
}
