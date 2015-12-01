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


package com.aurel.track.dao;

import java.util.List;

import com.aurel.track.beans.TWorkFlowBean;

public interface WorkflowDAO {
	
	/**
	 * Load all workflows
	 * @return
	 */
	List<TWorkFlowBean> loadAll();
	
	/**
	 * Gets the workflow transition from stateFrom to stateTo for projectType and issueType
	 * @param projectType
	 * @param issueType
	 * @param stateFrom
	 * @param stateTo 
	 */
	TWorkFlowBean getWorkflow(Integer projectType, 
			Integer  issueType,	Integer stateFrom, Integer stateTo);
	
	/**
	 * Gets the workflow transition from stateFrom for projectType and issueType
	 * @param projectType
	 * @param issueType
	 * @param stateFrom
	 * @return
	 */
	/*List<TWorkFlowBean> getWorkflows(Integer projectType, 
			Integer  issueType,	Integer stateFrom);*/
}
