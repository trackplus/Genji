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

import com.aurel.track.beans.TWfActivityContextParamsBean;
import com.aurel.track.item.workflow.execute.WorkflowContext;

/**
 *
 */
public interface WfActivityContextParamsDAO {
	/**
	 * Gets a wfActivityContextParamsBean by workflowContext
	 * @param workflowContext
	 * @return
	 */
	List<TWfActivityContextParamsBean> loadByContext(WorkflowContext workflowContext);

	/**
	 * Saves a wfActivityContextParamsBean
	 * @param wfActivityContextParamsBean
	 * @return
	 */
	Integer save(TWfActivityContextParamsBean wfActivityContextParamsBean);


	/**
	 * Deletes all parameters for a workflow context
	 * Is deletable should return true before calling this method
	 * @param workflowContext
	 */
	void deleteByContext(WorkflowContext workflowContext);
	
	/**
	 * Delete the workflow parameters for issue types
	 * @param issueTypeID
	 * @param projectTypeID
	 * @param projectID
	 */
	void deleteByIssueType(Integer issueTypeID);
	
	/**
	 * Delete the workflow parameters for projects
	 * if the projects is null - delete all all configs which have project set
	 * @param projectIDs
	 */
	void deleteByProjects(List<Integer> projectIDs);
	
	/**
	 * Delete the workflow parameters for projectType
	 * if the project pk is null - delete all all configs which have projectType set
	 * @param projectTypeID
	 */
	void deleteByProjectType(Integer projectTypeID);
	
	/**
	 * Delete the workflow parameter by primary key
	 * @param objectID
	 */
	void delete(Integer objectID);
}
