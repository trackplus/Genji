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

import com.aurel.track.beans.TWorkflowGuardBean;

import java.util.List;

/**
 *
 */
public interface WorkflowGuardDAO {
	/**
	 * Gets a workflowGuardBean by primary key
	 * @param objectID
	 * @return
	 */
	TWorkflowGuardBean loadByPrimaryKey(Integer objectID);

	/**
	 * Loads all workflow activities
	 * @return
	 */
	List<TWorkflowGuardBean> loadAll();

	/**
	 * Loads the guards for a transition
	 * @param transitionID
	 * @return
	 */
	List<TWorkflowGuardBean> loadByTransition(Integer transitionID);

	/**
	 * Load all guards linked with transitions starting from stationFrom
	 * @param stationFrom
	 * @param triggerEvent
	 * @param includeHooks whether to include the transitions to the same station
	 * @return
	 */
	List<TWorkflowGuardBean> loadFromStation(Integer stationFrom, Integer triggerEvent, boolean includeHooks);
	
	/**
	 * Saves a workflow guard
	 * @param workflowGuardBean
	 * @return
	 */
	Integer save(TWorkflowGuardBean workflowGuardBean);


	/**
	 * Deletes a workflow guard
	 * Is deletable should return true before calling this method
	 * @param objectID
	 */
	void delete(Integer objectID);
}
