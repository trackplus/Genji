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

import com.trackplus.model.Tworkflowconnect;

/**
 * DAO for WorkflowConnect object
 * 
 * @author Tamas Ruff
 * 
 */
public interface WorkflowConnectDAO {
	/**
	 * Gets a Tworkflowconnect by promary key
	 * 
	 * @param objectID
	 * @return
	 */
	Tworkflowconnect loadByPrimaryKey(Integer objectID);

	/**
	 * Save a workflowConfig
	 * 
	 * @param workflowConfig
	 * @return
	 */
	Integer save(Tworkflowconnect workflowConfig);

	/**
	 * Deletes a screenConfig from the TScreenConfig table Is deletable should
	 * return true before calling this method
	 * 
	 * @param objectID
	 */
	void delete(Integer objectID);

	/**
	 * 
	 * Load the cofiguration for parameteres: projectTypeID projectID issueID
	 * from cfg
	 * 
	 * @param cfg
	 * @return
	 */
	List<Tworkflowconnect> load(Tworkflowconnect cfg);

	/**
	 * Gets the default Tworkflowconnect * @return
	 */
	Tworkflowconnect loadDefault();

	/**
	 * Gets a Tworkflowconnect by issueType
	 * 
	 * @param issueType
	 * @return
	 */
	Tworkflowconnect loadByIssueType(Integer issueType);

	/**
	 * Gets a Tworkflowconnect by projectType
	 * 
	 * @param projectType
	 * @return
	 */
	Tworkflowconnect loadByProjectType(Integer projectType);

	/**
	 * Gets all the Tworkflowconnect for issueType, projectType and project
	 * 
	 * @param issueType
	 *            can be null, in this case the result it is not filtered by
	 *            issueType
	 * @param projectType
	 *            include into filtering even if null
	 * @param project
	 *            include into filtering even if null
	 * @return
	 */
	List<Tworkflowconnect> loadAllByIssueType(Integer issueType,
			Integer projectType, Integer project);

	/**
	 * Gets all the Tworkflowconnect by projectType
	 * 
	 * @param projectType
	 *            if specified filter by projectType otherwise only be sure to
	 *            not to be null (all projectType specific configurations)
	 * @param excludeWithIssueType
	 * @return
	 */
	List<Tworkflowconnect> loadAllByProjectType(Integer projectType,
			boolean excludeWithIssueType);

	/**
	 * Gets Tworkflowconnect by project
	 * 
	 * @param project
	 *            if specified filter by project otherwise only be sure to not
	 *            to be null (all project specific configurations)
	 * @param excludeWithIssueType
	 * @return
	 */
	List<Tworkflowconnect> loadAllByProject(Integer project,
			boolean excludeWithIssueType);

	/**
	 * Gets a Tworkflowconnect by project
	 * 
	 * @param project
	 * @return
	 */
	Tworkflowconnect loadByProject(Integer project);

	/**
	 * Gets a Tworkflowconnect by issueType and projectType
	 * 
	 * @param issueType
	 * @param projectType
	 * @return
	 */
	Tworkflowconnect loadByIssueTypeAndProjectType(Integer issueType,
			Integer projectType);

	/**
	 * Gets a Tworkflowconnect from the TScreenConfig table by issueType and
	 * project
	 * 
	 * @param issueType
	 * @param project
	 * @return
	 */
	Tworkflowconnect loadByIssueTypeAndProject(Integer issueType,
			Integer project);

	/**
	 * Delete the configurations for given node
	 * 
	 * @param issueTypeID
	 * @param projectTypeID
	 * @param projectID
	 */
	void deleteByIssueType(Integer issueTypeID, Integer projectTypeID,
			Integer projectID);

	/**
	 * Delete all configs withe given project pk if the project pk is null -
	 * delete all all configs which have project set
	 * 
	 * @param pk
	 */
	void deleteByProject(Integer pk);

	/**
	 * Delete all configs withe given projectType pk if the project pk is null -
	 * delete all all configs which have projectType set
	 * 
	 * @param pk
	 */
	void deleteByProjectType(Integer pk);
}
