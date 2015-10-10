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

import com.aurel.track.beans.TMailTemplateConfigBean;

import java.util.List;

/**
 *
 */
public interface MailTemplateConfigDAO {
	/**
	 * Gets a mailTemplateConfigBean from the TMailTemplateConfig table
	 * @param objectID
	 * @return
	 */
	TMailTemplateConfigBean loadByPrimaryKey(Integer objectID);

	/**
	 * Gets the mailTemplateConfigBeans by event from the TMailTemplateConfig table
	 * @param eventID
	 * @return
	 */
	List<TMailTemplateConfigBean> loadByEventKey(Integer eventID);

	/**
	 * Load the mailTemplate configurations for projects
	 * @param projectIDs
	 * @return
	 */
	//List<TMailTemplateConfigBean> loadByProjects(Object[] projectIDs);

	/**
	 * Load the mailTemplate configurations for project types
	 * @param projectTypeIDs
	 * @return
	 */
	//List<TMailTemplateConfigBean> loadByProjectTypes(Object[] projectTypeIDs);

	/**
	 * Load the mailTemplate configurations for issue types
	 * @param issueTypeIDs
	 * @return
	 */
	//List<TMailTemplateConfigBean> loadByIssueTypes(Object[] issueTypeIDs);

	/**
	 * Load the global mailTemplates
	 * @return
	 */
	List<TMailTemplateConfigBean> loadDefaults();

	/**
	 * Save  mailTemplateConfig in the TMailTemplateConfig table
	 * @param mailTemplateConfig
	 * @return
	 */
	Integer save(TMailTemplateConfigBean mailTemplateConfig);

	/**
	 * Copies a mailTemplate configuration
	 * @param mailTemplateConfigBean
	 * @param deep
	 */
	TMailTemplateConfigBean copy(TMailTemplateConfigBean mailTemplateConfigBean, boolean deep);

	/**
	 * Deletes a mailTemplateConfig from the TMailTemplateConfig table
	 * Is deletable should return true before calling this method
	 * @param objectID
	 */
	void delete(Integer objectID);


	/**
	 * Verifies whether a mailTemplateConfig is deletable
	 * @param objectID
	 */
	boolean isDeletable(Integer objectID);
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
	List<TMailTemplateConfigBean> load(TMailTemplateConfigBean cfg);

	/**
	 * Gets the default TMailTemplateConfigBean from the TMailTemplateConfig table
	 * @param event
	 * @return
	 */
	TMailTemplateConfigBean loadDefault(Integer event);

	/**
	 * Gets a TMailTemplateConfigBean from the TMailTemplateConfig table by issueType
	 * @param event
	 * @param issueType
	 * @return
	 */
	TMailTemplateConfigBean loadByIssueType(Integer event, Integer issueType);

	/**
	 * Gets a TMailTemplateConfigBean from the TMailTemplateConfig table by projectType
	 * @param event
	 * @param projectType
	 * @return
	 */
	TMailTemplateConfigBean loadByProjectType(Integer event, Integer projectType);

	/**
	 * Gets all the TMailTemplateConfigBeans for issueType, projectType and project
	 * @param issueType can be null, in this case the result it is not filtered by issueType
	 * @param projectType include into filtering even if null
	 * @param project include into filtering even if null
	 * @return
	 */
	List<TMailTemplateConfigBean> loadAllByIssueType(Integer issueType, Integer projectType, Integer project);

	/**
	 * Gets all the TMailTemplateConfigBeans which have a projectType specified
	 * @param projectType if specified filter by projectType otherwise only be sure to not to be null (all projectType specific configurations)
	 * @return
	 */
	List<TMailTemplateConfigBean> loadAllByProjectType(Integer projectType);

	/**
	 * Gets TMailTemplateConfigBeans by project
	 * @param project
	 * @return
	 */
	List<TMailTemplateConfigBean> loadAllByProject(Integer project);
	
	/**
	 * Gets TMailTemplateConfigBeans by projects
	 * @param projects
	 * @return
	 */
	List<TMailTemplateConfigBean> loadAllByProjects(List<Integer> projects);

	/**
	 * Gets a TMailTemplateConfigBean from the TMailTemplateConfig table by project
	 * @param event
	 * @param project
	 * @return
	 */
	TMailTemplateConfigBean loadByProject(Integer event, Integer project);

	/**
	 * Gets a TMailTemplateConfigBean from the TMailTemplateConfig table by issueType and projectType
	 * @param event
	 * @param issueType
	 * @param projectType
	 * @return
	 */
	TMailTemplateConfigBean loadByIssueTypeAndProjectType(Integer event, Integer issueType, Integer projectType);

	/**
	 * Gets a TMailTemplateConfigBean from the TMailTemplateConfig table by issueType and project
	 * @param event
	 * @param issueType
	 * @param project
	 * @return
	 */
	TMailTemplateConfigBean loadByIssueTypeAndProject(Integer event, Integer issueType, Integer project);

	TMailTemplateConfigBean loadByEventAndTemplate(Integer eventType,Integer mailTemplateID);
	TMailTemplateConfigBean loadByEvent(Integer eventType);


	/**
	 * Delete the configurations for given node
	 * @param issueTypeID
	 * @param projectTypeID
	 * @param projectID
	 */
	void deleteByIssueType(Integer issueTypeID, Integer projectTypeID, Integer projectID);

	/**
	 * Delete all configs with a given projectIDs
	 * if the projects is null - delete all all configs which have project set
	 * @param projects
	 */
	void deleteByProjects(List<Integer> projects);

	/**
	 * Delete all configs withe given projectType pk
	 * if the project pk is null - delete all all configs which have projectType set
	 * @param pk
	 */
	void deleteByProjectType(Integer pk);
}
