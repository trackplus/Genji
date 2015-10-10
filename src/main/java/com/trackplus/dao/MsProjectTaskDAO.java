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

import com.trackplus.model.Tmsprojecttask;

/**
 * DAO for MsProjectTask
 * 
 * @author Tamas Ruff
 * 
 */
public interface MsProjectTaskDAO {

	/**
	 * Loads an MsProjectTaskBean by primary key
	 * 
	 * @param objectID
	 * @return
	 */
	Tmsprojecttask loadByPrimaryKey(Integer objectID);

	/**
	 * Loads an MsProjectTaskBean by workItemID
	 * 
	 * @param workItemID
	 * @return
	 */
	Tmsprojecttask loadByPrimaryWorkItemID(Integer workItemID);

	/**
	 * Loads an MsProjectTaskBean by UID and release or project
	 * 
	 * @param UID
	 * @return
	 */
	Tmsprojecttask loadByUIDAndRelease(Integer UID, Integer entityID,
			int entityType);

	/**
	 * Loads list of TMSProjectTaskBeans for a release/project for import
	 * 
	 * @param entityID
	 * @param entityType
	 * @return
	 */
	List<Tmsprojecttask> loadMsProjectTasksForImport(Integer entityID,
			int entityType);

	/**
	 * Loads list of TMSProjectTaskBeans for a release/project for export
	 * 
	 * @param entityID
	 * @param entityType
	 * @param notClosed
	 * @return
	 */
	List<Tmsprojecttask> loadMsProjectTasksForExport(Integer entityID,
			int entityType, boolean notClosed);

	/**
	 * Saves an MsProjectTaskBean
	 * 
	 * @param mSProjectTaskBean
	 * @return
	 */
	Integer save(Tmsprojecttask mSProjectTaskBean);

	/**
	 * Deletes an MsProjectTaskBean
	 * 
	 * @param objectID
	 */
	void delete(Integer objectID);
}
