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


package com.trackplus.dao;

import java.util.List;

import com.trackplus.model.Tstatechange;

/**
 * DAO for StateChange
 * 
 * @author Tamas Ruff
 * 
 */
public interface StateChangeDAO {

	/**
	 * Loads a StateChangeBean by primary key
	 * 
	 * @param objectID
	 * @return
	 */

	/**
	 * Gets the maximal objectID
	 */
	Integer getMaxObjectID();

	/**
	 * Gets the next chunk
	 * 
	 * @param actualValue
	 * @param chunkInterval
	 * @return
	 */
	List<Tstatechange> getNextChunk(Integer actualValue, Integer chunkInterval);

	/**
	 * Load all StateChangeBean
	 * 
	 * @return
	 */
	List<Tstatechange> loadAll();

	/**
	 * Loads a StateChangeBean list by workItemKeys
	 * 
	 * @param workItemKeys
	 *            if null or empty return empty list
	 * @return
	 */

	/**
	 * Saves a StateChangeBean in the TStateChange table
	 * 
	 * @param stateChangeBean
	 * @return
	 */
	Integer save(Tstatechange stateChangeBean);

	/**
	 * Get the state change history for an item
	 * 
	 * @param workItemKey
	 * @param personID
	 *            if not null filter also by personID
	 * @return
	 */

	/**
	 * Load stateChanges to statuses occurred in a time interval for issues in
	 * certain projects
	 * 
	 * @param dateFrom
	 * @param dateTo
	 * @param projectIDs
	 * @param statusIDs
	 * @return
	 */
	// List loadForProjectsInTimeInterval(Date dateFrom, Date dateTo, List

	/**
	 * Load stateChanges to statuses occurred in a time interval for issues in
	 * certain releases
	 * 
	 * @param dateFrom
	 * @param dateTo
	 * @param projects
	 * @param statusIDs
	 * @return
	 */
	// List loadForReleasesInTimeInterval(Date dateFrom, Date dateTo, List

	/**
	 * Load stateChanges to statuses occurred in a time interval for workItemIDs
	 * 
	 * @param dateFrom
	 * @param dateTo
	 * @param workItemIDs
	 * @param statusIDs
	 * @return
	 */
	// List loadForWorkItemsInTimeInterval(Date dateFrom, Date dateTo, List
}
