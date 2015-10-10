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

import com.aurel.track.beans.TWorkflowActivityBean;
import com.aurel.track.fieldType.fieldChange.FieldChangeSetters;
import com.aurel.track.persist.TWorkflowActivity;
import com.aurel.track.persist.TWorkflowStationPeer;
import com.aurel.track.persist.TWorkflowTransitionPeer;

import java.util.List;

import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

/**
 *
 */
public interface WorkflowActivityDAO {
	/**
	 * Gets a workflowActivityBean by primary key
	 * @param objectID
	 * @return
	 */
	TWorkflowActivityBean loadByPrimaryKey(Integer objectID);

	/**
	 * Loads all workflow activities
	 * @return
	 */
	List<TWorkflowActivityBean> loadAll();

	List<TWorkflowActivityBean> loadByStation(Integer stationID);

	/**
	 * Loads the station entry activities
	 * @param stationID
	 * @return
	 */
	List<TWorkflowActivityBean> loadByStationEntry(Integer stationID);
	
	/**
	 * Loads the station exit activities
	 * @param stationID
	 * @return
	 */
	List<TWorkflowActivityBean> loadByStationExit(Integer stationID);
	
	/**
	 * Loads the station do activities
	 * @param stationID
	 * @return
	 */
	List<TWorkflowActivityBean> loadByStationDo(Integer stationID);
	
	List<TWorkflowActivityBean> loadByTransition(Integer transitionID);

	
	/**
	 * Whether the worklflow has parameterized transition activity
	 * @param workflowID
	 */
	boolean hasTransitionActivityWithParam(Integer workflowID);
	
	/**
	 * Whether the worklflow has parameterized station entry activity
	 * @param workflowID
	 */
	boolean hasStationEntryActivityWithParam(Integer workflowID);
	
	/**
	 * Whether the worklflow has parameterized station exit activity
	 * @param workflowID
	 */
	boolean hasStationExitActivityWithParam(Integer workflowID);
	
	/**
	 * Gets the parameterized transition activities for workflow
	 * @param workflowID
	 */
	List<TWorkflowActivityBean> getTransitionActivityWithParam(Integer workflowID);
	
	/**
	 * Gets the parameterized station entry activities for workflow
	 * @param workflowID
	 */
	List<TWorkflowActivityBean> getStationEntryActivityWithParam(Integer workflowID);
	
	/**
	 * Gets the parameterized station exit activities for workflow
	 * @param workflowID
	 */
	List<TWorkflowActivityBean> getStationExitActivityWithParam(Integer workflowID);
	
	/**
	 * Saves a workflow activity
	 * @param workflowActivityBean
	 * @return
	 */
	Integer save(TWorkflowActivityBean workflowActivityBean);


	/**
	 * Deletes a workflow
	 * Is deletable should return true before calling this method
	 * @param objectID
	 */
	void delete(Integer objectID);
}
