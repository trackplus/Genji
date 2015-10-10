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


package com.trackplus.dao;

import java.util.List;

import com.trackplus.model.Tworkflowtransition;

/**
 */
public interface WorkflowTransitionDAO {
	/**
	 * Gets a workflowTransitionBean by primary key
	 * 
	 * @param objectID
	 * @return
	 */

	Tworkflowtransition loadByPrimaryKey(Integer objectID);

	/**
	 * Loads all workflow Transitions
	 * 
	 * @return
	 */
	List<Tworkflowtransition> loadAll();

	/**
	 * Loads all transitions for a workflow
	 * 
	 * @param workflowID
	 * @return
	 */
	List<Tworkflowtransition> loadByWorkflow(Integer workflowID);

	/**
	 * Loads all transitions for a workflow and states
	 * 
	 * @param workflowID
	 * @param stateFrom
	 * @param stateTo
	 * @param actionID
	 *            if it is a direct status change then actionID is null
	 * @return
	 */
	List<Tworkflowtransition> loadByWorkflowAndStates(Integer workflowID,
			Integer stateFrom, Integer stateTo, Integer actionID);

	/**
	 * Loads all transitions for a workflow from the pseudo create
	 * 
	 * @param workflowID
	 * @param stationType
	 * @param stateTo
	 * @param actionID
	 *            if it is a direct status change then actionID is null
	 * @return
	 */
	List<Tworkflowtransition> loadFromCreate(Integer workflowID,
			Integer stationType, Integer stateTo, Integer actionID);

	/**
	 * Load all possible transitions from fromStation in workflow
	 * 
	 * @param fromStation
	 * @param actions
	 *            whether to get the transactions with actions or the direct
	 *            status change transitions
	 * @return
	 */
	List<Tworkflowtransition> loadFromStation(Integer fromStation,
			boolean actions);

	/**
	 * Loads all possible transitions from fromState in workflow
	 * 
	 * @param workflowID
	 * @param fromState
	 * @return
	 */
	List<Tworkflowtransition> loadFromState(Integer workflowID,
			Integer fromState);

	/**
	 * Loads all possible transitions from stations of stationType in workflow
	 * 
	 * @param workflowID
	 * @param stationType
	 * @return
	 */
	List<Tworkflowtransition> loadFromStationType(Integer workflowID,
			Integer stationType);

	/**
	 * Gets the workflow transitions starting from the pseudo create station
	 * 
	 * @param workflowID
	 * @param stationType
	 * @return
	 */
	List<Tworkflowtransition> loadFromPseudoCreateStation(Integer workflowID,
			Integer stationType);

	/**
	 * Whether workflow contains stationID (either as from or to)
	 * 
	 * @param stationID
	 * @return
	 */
	boolean stationInWorkflow(Integer workflowID, Integer stationID);

	/**
	 * Saves a workflow
	 * 
	 * @param workflowTransitionBean
	 * @return
	 */
	Integer save(Tworkflowtransition workflowTransitionBean);

	/**
	 * Deletes a workflow Is deletable should return true before calling this
	 * method
	 * 
	 * @param objectID
	 */
	void delete(Integer objectID);
}
