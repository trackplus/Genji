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

import com.trackplus.model.Tworkflowstation;

/**
 */
public interface WorkflowStationDAO {
	/**
	 * Gets a workflowStationBean by primary key
	 * 
	 * @param objectID
	 * @return
	 */
	Tworkflowstation loadByPrimaryKey(Integer objectID);

	/**
	 * Loads the workflowStationBeans by keys
	 * 
	 * @param keys
	 * @return
	 */
	// List<Tworkflowstation> loadByKeys(Object[] stationIDs);
	/**
	 * Loads all workflow stations
	 * 
	 * @return
	 */
	// List<Tworkflowstation> loadAll();
	/**
	 * Loads the workflowStationBeans for a workflow
	 * 
	 * @param workflowID
	 * @return
	 */
	List<Tworkflowstation> loadByWorkflow(Integer workflowID);

	/**
	 * Loads all possible transitions from fromState in workflow
	 * 
	 * @param workflowID
	 * @param fromStatus
	 * @return
	 */
	Tworkflowstation loadFromStatusStation(Integer workflowID,
			Integer fromStatus);

	/**
	 * Loads all possible transitions from fromState in workflow
	 * 
	 * @param workflowID
	 * @param fromStationType
	 * @return
	 */
	Tworkflowstation loadFromStationTypeStation(Integer workflowID,
			Integer fromStationType);

	/**
	 * Loads all possible transitions from fromStation in workflow
	 * 
	 * @param fromStation
	 * @return
	 */
	List<Tworkflowstation> loadFromStation(Integer fromStation);

	/**
	 * Loads the workflowStationBeans for a workflow and statusIDs
	 * 
	 * @param workflowID
	 * @return
	 */
	// List<Tworkflowstation> loadByWorkflowAndState(Integer workflowID,
	// List<Integer> statusIDs);
	/**
	 * Saves a workflow
	 * 
	 * @param workflowStationBean
	 * @return
	 */
	Integer save(Tworkflowstation workflowStationBean);

	/**
	 * Deletes a workflow Is deletable should return true before calling this
	 * method
	 * 
	 * @param objectID
	 */
	void delete(Integer objectID);
}
