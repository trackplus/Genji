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
import java.util.Set;

import com.aurel.track.beans.TFieldConfigBean;

public interface FieldConfigDAO {
	
	/**
	 * Gets a fieldConfigBean from the TFieldConfig table
	 * @param key
	 * @return
	 */
	TFieldConfigBean loadByPrimaryKey(Integer key);
	
	/**
	 * Gets all the TFieldConfigBean's  from the TFieldConfig table	
	 * @return
	 */
	List<TFieldConfigBean> loadAll();
	
	/**
	 * Gets all the TFieldConfigBean's with explicit history from the TFieldConfig table	
	 * @return
	 */
	List<TFieldConfigBean> loadAllWithExplicitHistory();
	
	/**
	 * Gets the list of default TFieldConfigBeans from the TFieldConfig table for a list of fieldConfigIDs
	 * @param fieldIDList
	 * @return
	 */
	List<TFieldConfigBean> loadByFieldConfigIDs(List<Integer> fieldConfigIDsList);
	
	/**
	 * Saves a field config in the TFieldConfig table
	 * @param fieldConfigBean
	 */
	Integer save(TFieldConfigBean fieldConfigBean);
	
	/**
	 * Copies a field configuration
	 * @param fieldConfigBean
	 * @param deep
	 */
	TFieldConfigBean copy(TFieldConfigBean fieldConfigBean, boolean deep);
	
	/**
	 * Deletes a field config from the TFieldConfig table
	 * @param fieldConfigId
	 */
	void delete(Integer fieldConfigId);
	
	/**
	 * Gets all the TFieldConfigBean's  from the TFieldConfig table 
	 * for a given issueType, projectType, project and isCustom
	 * @param issueType include into filtering even if null
	 * @param projectType include into filtering even if null
	 * @param project include into filtering even if null
	 * @param isCustom: is null: do not include into filtering
	 * 					is false: filter by system fields
	 * 					is true: filter by custom fields 
	 * @return
	 */
	List<TFieldConfigBean> loadAllByFieldType(Integer issueType, Integer projectType, Integer project, Boolean isCustom);
	
	/**
	 * Gets all the TFieldConfigBean's  from the TFieldConfig table for issueType, projectType and project
	 * The issueType  
	 * @param issueType can be null, in this case the result it is not filtered by issueType
	 * @param projectType include into filtering even if null
	 * @param project include into filtering even if null
	 * @return
	 */
	List<TFieldConfigBean> loadAllByIssueType(Integer issueType, Integer projectType, Integer project);
	
	/**
	 * Gets all the TFieldConfigBean's  from the TFieldConfig table which have a projectType specified	
	 * @param projectType if specified filter by projectType otherwise only be sure to not to be null (all projectType specific configurations)
	 * @return
	 */
	List<TFieldConfigBean> loadAllByProjectType(Integer projectType);
	
	/**
	 * Gets all the TFieldConfigBean's  from the TFieldConfig table which have a project specified
	 * @param project if specified filter by project otherwise only be sure that project is not null (all project specific configurations)
	 * @return
	 */
	List<TFieldConfigBean> loadAllByProject(Integer project);
	
	/**
	 * Gets all the TFieldConfigBean's  from the TFieldConfig table which have a project specified
	 * @param project if specified filter by project otherwise only be sure that project is not null (all project specific configurations)
	 * @return
	 */
	List<TFieldConfigBean> loadAllByProjects(List<Integer> projects);
	
	/**
	 * Gets the default TFieldConfigBean from the TFieldConfig table
	 * @param key
	 * @return
	 */
	TFieldConfigBean loadDefault(Integer field);
	
	/**
	 * Gets the list of default TFieldConfigBeans from the TFieldConfig table for a list of fieldIDs
	 * @param fieldIDList
	 * @return
	 */
	List<TFieldConfigBean> loadDefaultForFields(List<Integer> fieldIDList);

	/**
	 * Gets the list of all TFieldConfigBeans from the TFieldConfig table for a list of fieldIDs
	 * @param fieldIDList
	 * @return
	 */
	List<TFieldConfigBean> loadAllForFields(Set<Integer> fieldIDSet);
	
	/**
	 * Gets the list of all TFieldConfigBeans from the TFieldConfig table for a fieldID
	 * @param fieldID
	 * @return
	 */
	List<TFieldConfigBean> loadAllForField(Integer fieldID);
	
	/**
	 * Gets a TFieldConfigBean from the TFieldConfig table by issueType
	 * @param key
	 * @return
	 */
	TFieldConfigBean loadByIssueType(Integer field, Integer issueType);

	/**
	 * Gets a TFieldConfigBean from the TFieldConfig table by projectType
	 * @param field
	 * @param projectType
	 * @return
	 */
	TFieldConfigBean loadByProjectType(Integer field, Integer projectType);
	
	/**
	 * Gets a TFieldConfigBean from the TFieldConfig table by project
	 * @param field
	 * @param project
	 * @return
	 */
	TFieldConfigBean loadByProject(Integer field, Integer project);
	
	/**
	 * Gets a TFieldConfigBean from the TFieldConfig table by issueType and projectType
	 * @param key
	 * @return
	 */
	TFieldConfigBean loadByIssueTypeAndProjectType(Integer field, Integer issueType, Integer projectType);

	/**
	 * Gets a TFieldConfigBean from the TFieldConfig table by issueType and project
	 * @param key
	 * @return
	 */
	TFieldConfigBean loadByIssueTypeAndProject(Integer field, Integer issueType, Integer project);

	/**
	 * Gets a list of TFieldConfigBean from the parameters found in the fieldConfigBean
	 * @param fieldConfigBean
	 * @return
	 */
	List<TFieldConfigBean> loadByFieldConfigParameters(TFieldConfigBean fieldConfigBean);
	List<TFieldConfigBean> loadByFieldConfigParameters(TFieldConfigBean fieldConfigBean,boolean ignoreCustom);
	
	/* Runtime methods */
	
	/**
	 * Gets the list of default TFieldConfigBeans 
	 * @return
	 */
	List<TFieldConfigBean> loadDefault();

	/**
	 * Gets the list of TFieldConfigBeans for an issueType
	 * @param issueType
	 * @return
	 */
	List<TFieldConfigBean> loadByIssueType(Integer issueType);

	List<TFieldConfigBean> loadByIssueTypes(Object[] issueTypeIDs);

	/**
	 * Gets the list of TFieldConfigBeans for an projectType
	 * @param projectType
	 * @return
	 */
	List<TFieldConfigBean> loadByProjectType(Integer projectType);
	
	/**
	 * Gets the list of TFieldConfigBeans for an project
	 * @param project
	 * @return
	 */
	List<TFieldConfigBean> loadByProject(Integer project);
			
	/**
	 * Gets the list of TFieldConfigBeans for an issueType and a projectType
	 * @param issueType
	 * @param projectType
	 * @return
	 */
	List<TFieldConfigBean> loadByIssueTypeAndProjectType(Integer issueType, Integer projectType);

	/**
	 * Gets the list of TFieldConfigBeans for an issueType and project 
	 * @param issueType
	 * @param project
	 * @return
	 */
	List<TFieldConfigBean> loadByIssueTypeAndProject(Integer issueType, Integer project);
	
	/**
	 * Gets the list of TFieldConfigBeans for issueTypes and fieldIDs
	 * Project and ProjectType does not matter 
	 * @param issueTypes
	 * @param fieldIDs
	 * @return
	 */
	List<TFieldConfigBean> loadByIssueTypesAndFields(List<Integer> issueTypes, List<Integer> fieldIDs);
	
	/**
	 * Gets the list of TFieldConfigBeans for projectTypes and fieldIDs
	 * Project and IssueType does not matter 
	 * @param projectTypes
	 * @param fieldIDs
	 * @return
	 */
	List<TFieldConfigBean> loadByProjectTypesAndFields(List<Integer> projectTypes, List<Integer> fieldIDs);

	/**
	 * Gets the list of TFieldConfigBeans for an projects and fieldIDs
	 * @param projects
	 * @param fieldIDs
	 * @return
	 */
	List<TFieldConfigBean> loadByProjectsAndFields(List<Integer> projects, List<Integer> fieldIDs);
	
}
