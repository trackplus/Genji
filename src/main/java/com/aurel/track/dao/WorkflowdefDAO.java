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

import java.util.List;

import com.aurel.track.beans.TWorkflowDefBean;

/**
 * DAO for Screen
 * @author Adrian Bojani
 *
 */
public interface WorkflowdefDAO {
	
	/**
	 * Gets a workflowDefBean by primary key
	 * @param objectID
	 * @return
	 */
	
	TWorkflowDefBean loadByPrimaryKey(Integer objectID);
	
	/**
	 * Loads all workflows 
	 * @return 
	 */
	List<TWorkflowDefBean> loadAll();
	
	/**
	 * Loads workflows by IDs
	 * @param ids
	 * @return
	 */
	List<TWorkflowDefBean> loadByIDs(List<Integer> ids);


	/**
	 * Saves a workflow
	 * @param workflowDefBean
	 * @return
	 */
	Integer save(TWorkflowDefBean workflowDefBean);
	
	
	/**
	 * Deletes a workflow
	 * Is deletable should return true before calling this method
	 * @param objectID
	 */
	void delete(Integer objectID);

}
