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
import java.util.Set;

import com.trackplus.model.Tfieldconfig;

public interface FieldConfigDAO {

	/**
	 * Gets a fieldConfigBean from the TFieldConfig table
	 * 
	 * @param key
	 * @return
	 */
	Tfieldconfig loadByPrimaryKey(Integer key);

	/**
	 * Gets all the TFieldConfigBean's from the TFieldConfig table
	 * 
	 * @return
	 */
	List<Tfieldconfig> loadAll();

	/**
	 * Gets the list of default TFieldConfigBeans from the TFieldConfig table
	 * for a list of fieldConfigIDs
	 * 
	 * @param fieldIDList
	 * @return
	 */
	List<Tfieldconfig> loadByFieldConfigIDs(List<Integer> fieldConfigIDsList);

	/**
	 * Returns whether the label is unique for the field
	 * 
	 * @param label
	 * @param fieldID
	 * @return
	 */
	boolean isLabelUnique(String label, Integer fieldID);

	/**
	 * Saves a field config in the TFieldConfig table
	 * 
	 * @param fieldConfigBean
	 */
	Integer save(Tfieldconfig fieldConfigBean);

	/**
	 * Copies a field configuration
	 * 
	 * @param fieldConfigBean
	 * @param deep
	 */
	Tfieldconfig copy(Tfieldconfig fieldConfigBean, boolean deep);

	/**
	 * Deletes a field config from the TFieldConfig table
	 * 
	 * @param fieldConfigId
	 */
	void delete(Integer fieldConfigId);

	/**
	 * Gets all the TFieldConfigBean's from the TFieldConfig table for a given
	 * issueType, projectType, project and isCustom
	 * 
	 * @param issueType
	 *            include into filtering even if null
	 * @param projectType
	 *            include into filtering even if null
	 * @param project
	 *            include into filtering even if null
	 * @param isCustom
	 *            : is null: do not include into filtering is false: filter by
	 *            system fields is true: filter by custom fields
	 * @return
	 */
	List<Tfieldconfig> loadAllByFieldType(Integer issueType,
			Integer projectType, Integer project, Boolean isCustom);

	/**
	 * Gets all the TFieldConfigBean's from the TFieldConfig table for
	 * issueType, projectType and project The issueType
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
	List<Tfieldconfig> loadAllByIssueType(Integer issueType,
			Integer projectType, Integer project);

	/**
	 * Gets all the TFieldConfigBean's from the TFieldConfig table which have a
	 * projectType specified
	 * 
	 * @param projectType
	 *            if specified filter by projectType otherwise only be sure to
	 *            not to be null (all projectType specific configurations)
	 * @return
	 */
	List<Tfieldconfig> loadAllByProjectType(Integer projectType);

	/**
	 * Gets all the TFieldConfigBean's from the TFieldConfig table which have a
	 * project specified
	 * 
	 * @param project
	 *            if specified filter by project otherwise only be sure to not
	 *            to be null (all project specific configurations)
	 * @return
	 */
	List<Tfieldconfig> loadAllByProject(Integer project);

	/**
	 * Gets the default Tfieldconfig from the TFieldConfig table
	 * 
	 * @param key
	 * @return
	 */
	Tfieldconfig loadDefault(Integer field);

	/**
	 * Gets the list of default TFieldConfigBeans from the TFieldConfig table
	 * for a list of fieldIDs
	 * 
	 * @param fieldIDList
	 * @return
	 */
	List<Tfieldconfig> loadDefaultForFields(List<Integer> fieldIDList);

	/**
	 * Gets the list of all TFieldConfigBeans from the TFieldConfig table for a
	 * list of fieldIDs
	 * 
	 * @param fieldIDList
	 * @return
	 */
	List<Tfieldconfig> loadAllForFields(Set<Integer> fieldIDSet);

	/**
	 * Gets the list of all TFieldConfigBeans from the TFieldConfig table for a
	 * fieldID
	 * 
	 * @param fieldID
	 * @return
	 */
	List<Tfieldconfig> loadAllForField(Integer fieldID);

	/**
	 * Gets a Tfieldconfig from the TFieldConfig table by issueType
	 * 
	 * @param key
	 * @return
	 */
	Tfieldconfig loadByIssueType(Integer field, Integer issueType);

	/**
	 * Gets a Tfieldconfig from the TFieldConfig table by projectType
	 * 
	 * @param field
	 * @param projectType
	 * @return
	 */
	Tfieldconfig loadByProjectType(Integer field, Integer projectType);

	/**
	 * Gets a Tfieldconfig from the TFieldConfig table by project
	 * 
	 * @param field
	 * @param project
	 * @return
	 */
	Tfieldconfig loadByProject(Integer field, Integer project);

	/**
	 * Gets a Tfieldconfig from the TFieldConfig table by issueType and
	 * projectType
	 * 
	 * @param key
	 * @return
	 */
	Tfieldconfig loadByIssueTypeAndProjectType(Integer field,
			Integer issueType, Integer projectType);

	/**
	 * Gets a Tfieldconfig from the TFieldConfig table by issueType and project
	 * 
	 * @param key
	 * @return
	 */
	Tfieldconfig loadByIssueTypeAndProject(Integer field, Integer issueType,
			Integer project);

	/**
	 * Gets a list of Tfieldconfigbean from the parameters found in the
	 * fieldconfig * @param fieldConfigBean
	 * 
	 * @return
	 */
	List<Tfieldconfig> loadByFieldConfigParameters(Tfieldconfig fieldConfigBean);

	/**
	 * Returns the most specific valid configID for a field
	 * 
	 * @param workItem
	 * @param fieldID
	 * @return
	 */
	// Tfieldconfig getValidConfig(Integer fieldID, Integer issueType, Integer
	// project);

	/* Runtime methods */

	/**
	 * Gets the list of default TFieldConfigBeans
	 * 
	 * @return
	 */
	List<Tfieldconfig> loadDefault();

	/**
	 * Gets the list of TFieldConfigBeans for an issueType
	 * 
	 * @param issueType
	 * @return
	 */
	List<Tfieldconfig> loadByIssueType(Integer issueType);

	/**
	 * Gets the list of TFieldConfigBeans for an projectType
	 * 
	 * @param projectType
	 * @return
	 */
	List<Tfieldconfig> loadByProjectType(Integer projectType);

	/**
	 * Gets the list of TFieldConfigBeans for an project
	 * 
	 * @param project
	 * @return
	 */
	List<Tfieldconfig> loadByProject(Integer project);

	/**
	 * Gets the list of TFieldConfigBeans for an issueType and a projectType
	 * 
	 * @param issueType
	 * @param projectType
	 * @return
	 */
	List<Tfieldconfig> loadByIssueTypeAndProjectType(Integer issueType,
			Integer projectType);

	/**
	 * Gets the list of TFieldConfigBeans for an issueType and project
	 * 
	 * @param issueType
	 * @param project
	 * @return
	 */
	List<Tfieldconfig> loadByIssueTypeAndProject(Integer issueType,
			Integer project);

	/**
	 * Gets the list of TFieldConfigBeans for issueTypes and fieldIDs Project
	 * and ProjectType does not matter
	 * 
	 * @param issueTypes
	 * @param fieldIDs
	 * @return
	 */
	List<Tfieldconfig> loadByIssueTypesAndFields(List<Integer> issueTypes,
			List<Integer> fieldIDs);

	/**
	 * Gets the list of TFieldConfigBeans for projectTypes and fieldIDs Project
	 * and IssueType does not matter
	 * 
	 * @param projectTypes
	 * @param fieldIDs
	 * @return
	 */
	List<Tfieldconfig> loadByProjectTypesAndFields(List<Integer> projectTypes,
			List<Integer> fieldIDs);

	/**
	 * Gets the list of TFieldConfigBeans for an projects and fieldIDs
	 * 
	 * @param projects
	 * @param fieldIDs
	 * @return
	 */
	List<Tfieldconfig> loadByProjectsAndFields(List<Integer> projects,
			List<Integer> fieldIDs);
}
