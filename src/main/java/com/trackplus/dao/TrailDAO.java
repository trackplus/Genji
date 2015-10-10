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

import com.trackplus.model.Ttrail;

/**
 * DAO for Trail
 * 
 * @author Tamas Ruff
 * 
 */
public interface TrailDAO {
	/**
	 * Loads a TrailBean by primary key
	 * 
	 * @param objectID
	 * @return
	 */
	// Ttrail loadByPrimaryKey(Integer objectID);

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
	List<Ttrail> getNextChunk(Integer actualValue, Integer chunkInterval);

	/**
	 * Load all TrailBeans
	 * 
	 * @return
	 */
	List<Ttrail> loadAll();

	/**
	 * Load a TrailBean list by workItemKeys
	 * 
	 * @param workItemKeys
	 * @return
	 */
	// List loadByWorkItemKeys(int [] workItemKeys);

	/**
	 * Saves a TrailBean in the TTrail table
	 * 
	 * @param trailBean
	 * @return
	 */
	Integer save(Ttrail trailBean);

	/**
	 * Get the trail history for an item
	 * 
	 * @param workItemKey
	 * @param personID
	 *            if not null filter also by personID
	 * @return
	 */
	// List getByWorkItemAndPerson(Integer workItemID, Integer personID);

}
