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

import com.trackplus.model.Tmailtemplateconfig;

/**
 *
 */
public interface MailTemplateConfigDAO {
	/**
	 * Gets a mailTemplateConfigBean from the TMailTemplateConfig table
	 * 
	 * @param objectID
	 * @return
	 */
	Tmailtemplateconfig loadByPrimaryKey(Integer objectID);

	/**
	 * Gets the mailTemplateConfigBeans by event from the TMailTemplateConfig
	 * table
	 * 
	 * @param eventID
	 * @return
	 */
	List<Tmailtemplateconfig> loadByEventKey(Integer eventID);

	/**
	 * Load the mailTemplate configurations for projects
	 * 
	 * @param projectIDs
	 * @return
	 */
	List<Tmailtemplateconfig> loadByProjects(Object[] projectIDs);

	/**
	 * Load the mailTemplate configurations for project types
	 * 
	 * @param projectTypeIDs
	 * @return
	 */
	List<Tmailtemplateconfig> loadByProjectTypes(Object[] projectTypeIDs);

	/**
	 * Load the mailTemplate configurations for issue types
	 * 
	 * @param issueTypeIDs
	 * @return
	 */
	List<Tmailtemplateconfig> loadByIssueTypes(Object[] issueTypeIDs);

	/**
	 * Load the global mailTemplates
	 * 
	 * @return
	 */
	List<Tmailtemplateconfig> loadDefaults();

	/**
	 * Save mailTemplateConfig in the TMailTemplateConfig table
	 * 
	 * @param mailTemplateConfig
	 * @return
	 */
	Integer save(Tmailtemplateconfig mailTemplateConfig);

	/**
	 * Copies a mailTemplate configuration
	 * 
	 * @param mailTemplateConfigBean
	 * @param deep
	 */
	Tmailtemplateconfig copy(Tmailtemplateconfig mailTemplateConfigBean,
			boolean deep);

	/**
	 * Deletes a mailTemplateConfig from the TMailTemplateConfig table Is
	 * deletable should return true before calling this method
	 * 
	 * @param objectID
	 */
	void delete(Integer objectID);

	/**
	 * Verifies whether a mailTemplateConfig is deletable
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
	List<Tmailtemplateconfig> load(Tmailtemplateconfig cfg);

	/**
	 * Gets the default Tmailtemplateconfig from the TMailTemplateConfig table
	 * 
	 * @param event
	 * @return
	 */
	Tmailtemplateconfig loadDefault(Integer event);

	/**
	 * Gets a Tmailtemplateconfig from the TMailTemplateConfig table by
	 * issueType
	 * 
	 * @param event
	 * @param issueType
	 * @return
	 */
	Tmailtemplateconfig loadByIssueType(Integer event, Integer issueType);

	/**
	 * Gets a Tmailtemplateconfig from the TMailTemplateConfig table by
	 * projectType
	 * 
	 * @param event
	 * @param projectType
	 * @return
	 */
	Tmailtemplateconfig loadByProjectType(Integer event, Integer projectType);

	/**
	 * Gets all the TMailTemplateConfigBeans for issueType, projectType and
	 * project
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
	List<Tmailtemplateconfig> loadAllByIssueType(Integer issueType,
			Integer projectType, Integer project);

	/**
	 * Gets all the TMailTemplateConfigBeans which have a projectType specified
	 * 
	 * @param projectType
	 *            if specified filter by projectType otherwise only be sure to
	 *            not to be null (all projectType specific configurations)
	 * @return
	 */
	List<Tmailtemplateconfig> loadAllByProjectType(Integer projectType);

	/**
	 * Gets TMailTemplateConfigBeans by project
	 * 
	 * @param project
	 * @return
	 */
	List<Tmailtemplateconfig> loadAllByProject(Integer project);

	/**
	 * Gets a Tmailtemplateconfig from the TMailTemplateConfig table by project
	 * 
	 * @param event
	 * @param project
	 * @return
	 */
	Tmailtemplateconfig loadByProject(Integer event, Integer project);

	/**
	 * Gets a Tmailtemplateconfig from the TMailTemplateConfig table by
	 * issueType and projectType
	 * 
	 * @param event
	 * @param issueType
	 * @param projectType
	 * @return
	 */
	Tmailtemplateconfig loadByIssueTypeAndProjectType(Integer event,
			Integer issueType, Integer projectType);

	/**
	 * Gets a Tmailtemplateconfig from the TMailTemplateConfig table by
	 * issueType and project
	 * 
	 * @param event
	 * @param issueType
	 * @param project
	 * @return
	 */
	Tmailtemplateconfig loadByIssueTypeAndProject(Integer event,
			Integer issueType, Integer project);

	Tmailtemplateconfig loadByEventAndTemplate(Integer eventType,
			Integer mailTemplateID);

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
