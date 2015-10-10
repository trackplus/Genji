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

import com.trackplus.model.Tworkflowactivity;

/**
 *
 */
public interface WorkflowActivityDAO {
	/**
	 * Gets a workflowActivityBean by primary key
	 * 
	 * @param objectID
	 * @return
	 */

	Tworkflowactivity loadByPrimaryKey(Integer objectID);

	/**
	 * Loads all workflow activities
	 * 
	 * @return
	 */
	List<Tworkflowactivity> loadAll();

	List<Tworkflowactivity> loadByStation(Integer stationID);

	/**
	 * Loads the station entry activities
	 * 
	 * @param stationID
	 * @return
	 */
	List<Tworkflowactivity> loadByStationEntry(Integer stationID);

	/**
	 * Loads the station exit activities
	 * 
	 * @param stationID
	 * @return
	 */
	List<Tworkflowactivity> loadByStationExit(Integer stationID);

	/**
	 * Loads the station do activities
	 * 
	 * @param stationID
	 * @return
	 */
	List<Tworkflowactivity> loadByStationDo(Integer stationID);

	List<Tworkflowactivity> loadByTransition(Integer transitionID);

	/**
	 * Saves a workflow activity
	 * 
	 * @param workflowActivityBean
	 * @return
	 */
	Integer save(Tworkflowactivity workflowActivityBean);

	/**
	 * Deletes a workflow Is deletable should return true before calling this
	 * method
	 * 
	 * @param objectID
	 */
	void delete(Integer objectID);
}
