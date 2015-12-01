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
import java.util.Set;

import com.trackplus.model.Tworkitemlock;

public interface WorkItemLockDAO {

	/**
	 * Gets a Tworkitemlock by primary key
	 * 
	 * @param objectID
	 * @return
	 */
	Tworkitemlock loadByPrimaryKey(Integer objectID);

	/**
	 * Gets a Tworkitemlock by the sessionID
	 * 
	 * @param sessionID
	 * @return
	 */
	Tworkitemlock loadBySessionID(String sessionID);

	/**
	 * Get the workItemIDs from the list which are locked at the moment
	 * 
	 * @param workItemIDsList
	 * @return
	 */
	Set<Integer> getLockedIssues(List<Integer> workItemIDsList);

	/**
	 * Saves a Tworkitemlock * @param workItemLockBean
	 * 
	 * @return
	 */
	Integer save(Tworkitemlock workItemLockBean);

	/**
	 * Deletes an Tworkitemlock by primary key
	 * 
	 * @param objectID
	 */
	void delete(Integer objectID);

	/**
	 * Deletes an Tworkitemlock by sessionID
	 * 
	 * @param sessionID
	 */
	void deleteBySession(String sessionID);

	/**
	 * Deletes an Tworkitemlock by personID
	 * 
	 * @param personID
	 */
	void deleteByPerson(Integer personID);

	/**
	 * Deletes all TWorkItemLockBeans from the database by starting the server
	 */
	void deleteAll();
}
