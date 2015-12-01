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

import com.trackplus.model.Tbaseline;

/**
 * This defines the Data Access Object (DAO) interface for start and due dates
 * of issues.
 * 
 * @author Tamas Ruff
 * 
 */
public interface BaseLineDAO {
	/**
	 * Loads a BaseLineBean by primary key
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
	List<Tbaseline> getnextchunk(Integer actualvalue, Integer chunkinterval);

	/**
	 * Load all BaseLineBeans
	 * 
	 * @return
	 */
	List loadAll();

	/**
	 * Loads a BaseLineBean list by workItemKeys
	 * 
	 * @param workItemKeys
	 * @return
	 */

	/**
	 * Saves a BaseLineBean in the Tbaseline table
	 * 
	 * @param baseLineBean
	 * @return
	 */
	Integer save(Tbaseline baseline);

	/**
	 * Get the base line history for an item
	 * 
	 * @param workItemKey
	 * @param personID
	 *            if not null filter also by personID
	 * @return
	 */
}
