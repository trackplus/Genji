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

import com.aurel.track.beans.TWorkflowConnectBean;

/**
 *  DAO for WorkflowConnect object
 * @author Tamas Ruff
 *
 */
public interface WorkflowConnectDAO {
	/**
	 * Gets a TWorkflowConnectBean by primary key
	 * @param objectID
	 * @return
	 */
	TWorkflowConnectBean loadByPrimaryKey(Integer objectID);
	
	/**
	 * Gets all TWorkflowConnectBeans
	 * @return
	 */
	List<TWorkflowConnectBean> loadAll();

	/**
	 * Save a workflowConfig
	 * @param workflowConfig
	 * @return
	 */
	Integer save(TWorkflowConnectBean workflowConfig);
	
	
	/**
	 * Deletes a screenConfig from the TScreenConfig table
	 * Is deletable should return true before calling this method
	 * @param objectID
	 */
	void delete(Integer objectID);
	
	/**
	 * 
	 * Load the cofiguration for parameteres:
	 * projectTypeID
	 * projectID
	 * issueID
	 * from cfg
	 * 
	 * @param cfg
	 * @return
	 */
	List<TWorkflowConnectBean> load(TWorkflowConnectBean cfg);
	
	/**
	 * Gets the default TWorkflowConnectBean
	 * @return
	 */
	TWorkflowConnectBean loadDefault();
	
	/**
	 * Gets a TWorkflowConnectBean by issueType
	 * @param issueType
	 * @return
	 */
	TWorkflowConnectBean loadByIssueType(Integer issueType);
	List<TWorkflowConnectBean> loadByIssueTypes(Object[] issueTypeIDs);

	/**
	 * Gets a TWorkflowConnectBean by projectType
	 * @param projectType
	 * @return
	 */
	TWorkflowConnectBean loadByProjectType(Integer projectType);
	
	/**
	 * Gets all the TWorkflowConnectBean for issueType, projectType and project  
	 * @param issueType can be null, in this case the result it is not filtered by issueType
	 * @param projectType include into filtering even if null
	 * @param project include into filtering even if null
	 * @return
	 */
	List<TWorkflowConnectBean> loadAllByIssueType(Integer issueType, Integer projectType, Integer project);
	
	/**
	 * Gets all the TWorkflowConnectBean by projectType
	 * @param projectType if specified filter by projectType otherwise only be sure to not to be null (all projectType specific configurations)
	 * @param excludeWithIssueType
	 * @return
	 */
	List<TWorkflowConnectBean> loadAllByProjectType(Integer projectType, boolean excludeWithIssueType);
	
	/**
	 * Gets TWorkflowConnectBean by project
	 * @param project if specified filter by project otherwise only be sure to not to be null (all project specific configurations)
	 * @param excludeWithIssueType
	 * @return
	 */
	List<TWorkflowConnectBean> loadAllByProject(Integer project, boolean excludeWithIssueType);
	
	/**
	 * Gets TWorkflowConnectBean by project
	 * @param projects if specified filter by projects otherwise only be sure to not to be null (all project specific configurations)
	 * @return
	 */
	List<TWorkflowConnectBean> loadAllByProjects(List<Integer> projects);
	
	/**
	 * Gets a TWorkflowConnectBean by project
	 * @param project
	 * @return
	 */
	TWorkflowConnectBean loadByProject(Integer project);
	
	/**
	 * Gets a TWorkflowConnectBean by issueType and projectType
	 * @param issueType
	 * @param projectType
	 * @return
	 */
	TWorkflowConnectBean loadByIssueTypeAndProjectType(Integer issueType, Integer projectType);

	/**
	 * Gets a TWorkflowConnectBean from the TScreenConfig table by issueType and project
	 * @param issueType
	 * @param project
	 * @return
	 */
	TWorkflowConnectBean loadByIssueTypeAndProject(Integer issueType, Integer project);

	
	/**
	 * Delete the configurations for given node
	 * @param issueTypeID
	 */
	void deleteByIssueType(Integer issueTypeID);
	
	/**
	 * Delete all configs with projects
	 * if the projects is null - delete all all configs which have project set
	 * @param projectIDs
	 */
	void deleteByProjects(List<Integer> projectIDs);
	
	/**
	 * Delete all configs withe given projectType pk
	 * if the project pk is null - delete all all configs which have projectType set
	 * @param projectType
	 */
	void deleteByProjectType(Integer projectType);
}
