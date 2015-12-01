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

import com.trackplus.model.Tscreenconfig;

/**
 * DAO for ScreenConfig object
 * 
 * @author Adrian Bojani
 * 
 */
public interface ScreenConfigDAO {
	/**
	 * Gets a screenConfigBean from the TScreenConfig table
	 * 
	 * @param objectID
	 * @return
	 */
	Tscreenconfig loadByPrimaryKey(Integer objectID);

	/**
	 * Gets the screenConfigBeans by action from the TScreenConfig table
	 * 
	 * @param actionID
	 * @return
	 */
	List<Tscreenconfig> loadByActionKey(Integer actionID);

	/**
	 * Load the screen configurations for projects
	 * 
	 * @param projectIDs
	 * @return
	 */
	List<Tscreenconfig> loadByProjects(Object[] projectIDs);

	/**
	 * Load the screen configurations for project types
	 * 
	 * @param projectTypeIDs
	 * @return
	 */
	List<Tscreenconfig> loadByProjectTypes(Object[] projectTypeIDs);

	/**
	 * Load the screen configurations for issue types
	 * 
	 * @param issueTypeIDs
	 * @return
	 */
	List<Tscreenconfig> loadByIssueTypes(Object[] issueTypeIDs);

	/**
	 * Load the global screens
	 * 
	 * @return
	 */
	List<Tscreenconfig> loadDefaults();

	/**
	 * Save screenConfig in the TScreenConfig table
	 * 
	 * @param screenConfig
	 * @return
	 */
	Integer save(Tscreenconfig screenConfig);

	/**
	 * Copies a screen configuration
	 * 
	 * @param screenConfigBean
	 * @param deep
	 */
	Tscreenconfig copy(Tscreenconfig screenConfigBean, boolean deep);

	/**
	 * Deletes a screenConfig from the TScreenConfig table Is deletable should
	 * return true before calling this method
	 * 
	 * @param objectID
	 */
	void delete(Integer objectID);

	/**
	 * Verifies whether a screenConfig is deletable
	 * 
	 * @param objectID
	 */
	boolean isDeletable(Integer objectID);

	/**
	 * 
	 * Load the cofiguration for parameteres: projectTypeID projectID issueID
	 * from cfg
	 * 
	 * @param cfg
	 * @return
	 */
	List<Tscreenconfig> load(Tscreenconfig cfg);

	/**
	 * Gets the default Tscreenconfig from the TScreenConfig table
	 * 
	 * @param action
	 * @return
	 */
	Tscreenconfig loadDefault(Integer action);

	/**
	 * Gets a Tscreenconfig from the TScreenConfig table by issueType
	 * 
	 * @param action
	 * @param issueType
	 * @return
	 */
	Tscreenconfig loadByIssueType(Integer action, Integer issueType);

	/**
	 * Gets a Tscreenconfig from the TScreenConfig table by projectType
	 * 
	 * @param action
	 * @param projectType
	 * @return
	 */
	Tscreenconfig loadByProjectType(Integer action, Integer projectType);

	/**
	 * Gets all the TScreenConfigBeans for issueType, projectType and project
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
	List<Tscreenconfig> loadAllByIssueType(Integer issueType,
			Integer projectType, Integer project);

	/**
	 * Gets all the TScreenConfigBeans which have a projectType specified
	 * 
	 * @param projectType
	 *            if specified filter by projectType otherwise only be sure to
	 *            not to be null (all projectType specific configurations)
	 * @return
	 */
	List<Tscreenconfig> loadAllByProjectType(Integer projectType);

	/**
	 * Gets TScreenConfigBeans by project
	 * 
	 * @param project
	 * @return
	 */
	List<Tscreenconfig> loadAllByProject(Integer project);

	/**
	 * Gets a Tscreenconfig from the TScreenConfig table by project
	 * 
	 * @param action
	 * @param project
	 * @return
	 */
	Tscreenconfig loadByProject(Integer action, Integer project);

	/**
	 * Gets a Tscreenconfig from the TScreenConfig table by issueType and
	 * projectType
	 * 
	 * @param action
	 * @param issueType
	 * @param projectType
	 * @return
	 */
	Tscreenconfig loadByIssueTypeAndProjectType(Integer action,
			Integer issueType, Integer projectType);

	/**
	 * Gets a Tscreenconfig from the TScreenConfig table by issueType and
	 * project
	 * 
	 * @param action
	 * @param issueType
	 * @param project
	 * @return
	 */
	Tscreenconfig loadByIssueTypeAndProject(Integer action, Integer issueType,
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
