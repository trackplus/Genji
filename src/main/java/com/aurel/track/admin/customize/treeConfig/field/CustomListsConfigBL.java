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

package com.aurel.track.admin.customize.treeConfig.field;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.admin.customize.lists.ListBL;
import com.aurel.track.admin.customize.projectType.ProjectTypesBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TListBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TOptionSettingsBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TProjectTypeBean;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.GeneralUtils;

/**
 * Helper class for custom list configurations in different contexts
 * @author Tamas
 *
 */
public class CustomListsConfigBL {
	
	/**
	 * Gets the most specific list configurations by field and list
	 * @param customListFieldIDs
	 * @param projectIssueTypeContexts
	 * @return
	 */
	public static Map<Integer, Map<Integer, List<TFieldConfigBean>>> getMostSpecificLists(
			List<Integer> customListFieldIDs, Map<Integer, Set<Integer>> projectIssueTypeContexts) {
		Set<Integer> involvedProjectsSet = new HashSet<Integer>();
		Set<Integer> involvedIssueTypesSet = new HashSet<Integer>();
		Set<Integer> involvedProjectTypesSet = new HashSet<Integer>();
		for (Map.Entry<Integer, Set<Integer>> entry : projectIssueTypeContexts.entrySet()) {
			Integer projectID = entry.getKey();
			involvedProjectsSet.add(projectID);
			TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
			if (projectBean!=null) {
				Integer projectTypeID = projectBean.getProjectType();
				if (projectTypeID!=null) {
					involvedProjectTypesSet.add(projectTypeID);
				}
			}
			Set<Integer> projectIssueTypesSet = entry.getValue();
			involvedIssueTypesSet.addAll(projectIssueTypesSet);
		}
		return CustomListsConfigBL.getMostSpecificLists(projectIssueTypeContexts, GeneralUtils.createIntegerListFromCollection(involvedProjectTypesSet),
			GeneralUtils.createIntegerListFromCollection(involvedProjectsSet), GeneralUtils.createIntegerListFromCollection(involvedIssueTypesSet), customListFieldIDs);
	}
	
	/**
	 * Gets the most specific configurations for lists
	 * @param projectIssueTypeContexts
	 * @param projectTypeIDs
	 * @param projectIDs
	 * @param issueTypeIDs
	 * @param customListFieldIDs
	 * @return map with key: fieldID
	 * 					value: map with key: listID
	 * 									value: list of fieldConfigurationBeans having the listID configured different contexts
	 */
	private static Map<Integer, Map<Integer, List<TFieldConfigBean>>> getMostSpecificLists(
			Map<Integer, Set<Integer>> projectIssueTypeContexts,
			List<Integer> projectTypeIDs, List<Integer> projectIDs, List<Integer> issueTypeIDs, List<Integer> customListFieldIDs) {
		Map<Integer, Map<Integer, List<TFieldConfigBean>>> fieldIDToListIDToFieldConfigs =
				new HashMap<Integer, Map<Integer, List<TFieldConfigBean>>>();
		//get all configurations for involved projects projectTypes and issueTypes
		//the same fieldConfigBean appear more than one times for combined configurations: issueType - project or projectType 
		Map<Integer, Integer> ancestorProjectsHierarchyMap = ProjectBL.getAncestorProjectHierarchy(GeneralUtils.createIntegerArrFromCollection(projectIDs));
		List<Integer> ancestorProjectsIDs = GeneralUtils.createIntegerListFromCollection(ancestorProjectsHierarchyMap.keySet());
		List<TFieldConfigBean> fieldConfigs = getPossibleFieldConfigs(customListFieldIDs, issueTypeIDs, projectTypeIDs, ancestorProjectsIDs);
		/**
		 * Gather data from field configurations 
		 */
		Set<Integer> fieldConfigSet = new HashSet<Integer>();
		Map<Integer, List<Integer>> fieldToFieldConfigMap = new HashMap<Integer, List<Integer>>();
		Map<Integer, TFieldConfigBean> fieldConfigIDToFieldConfigBeanMap = new HashMap<Integer, TFieldConfigBean>();
		for (TFieldConfigBean fieldConfigBean : fieldConfigs) {
			Integer fieldConfigID = fieldConfigBean.getObjectID();
			Integer fieldID = fieldConfigBean.getField();
			if (!fieldConfigSet.contains(fieldConfigID)) {
				//do not add the same fieldConfiguration two times
				fieldConfigSet.add(fieldConfigID);	
				fieldConfigIDToFieldConfigBeanMap.put(fieldConfigID, fieldConfigBean);
				List<Integer> configsForField = fieldToFieldConfigMap.get(fieldID);
				if (configsForField==null) {
					configsForField = new LinkedList<Integer>();
					fieldToFieldConfigMap.put(fieldID, configsForField);
				}
				configsForField.add(fieldConfigID);
			}
		}
		/**
		 * Get the optionSettings for fieldConfigs
		 */
		Map<Integer, Integer> fieldConfigToList = getFieldConfigToListMap(fieldConfigSet);
		for (Integer fieldID : customListFieldIDs) {
			List<Integer> fieldConfigIDsForField = fieldToFieldConfigMap.get(fieldID);
			Map<Integer, List<TFieldConfigBean>> fieldMap = getMostSpecificListsForField(
					projectIssueTypeContexts, fieldConfigIDsForField, fieldConfigIDToFieldConfigBeanMap, fieldConfigToList, ancestorProjectsHierarchyMap);
			fieldIDToListIDToFieldConfigs.put(fieldID, fieldMap);
		}
		return fieldIDToListIDToFieldConfigs;
	}
	
	/**
	 * Gets the most specific lists in each context  
	 * @param fieldID
	 * @param projectIssueTypeContexts
	 * @param fieldToFieldConfigMap
	 * @param fieldConfigIDToFieldConfigBeanMap
	 * @param fieldConfigToList
	 * @param ancestorProjectsHierarchyMap
	 * @return map with key: listID
	 * 					value: list of fieldConfigurationBeans having the listID configured different contexts
	 */
	private static Map<Integer, List<TFieldConfigBean>> getMostSpecificListsForField(
			Map<Integer, Set<Integer>> projectIssueTypeContexts, List<Integer> fieldConfigIDsForField,
			Map<Integer, TFieldConfigBean> fieldConfigIDToFieldConfigBeanMap, Map<Integer, Integer> fieldConfigToList, Map<Integer, Integer> ancestorProjectsHierarchyMap) {
		Map<Integer, List<TFieldConfigBean>> fieldMap = new HashMap<Integer, List<TFieldConfigBean>>();
		for (Map.Entry<Integer, Set<Integer>> projectToItemTypesEntry : projectIssueTypeContexts.entrySet()) {
			Integer projectID = projectToItemTypesEntry.getKey();
			Set<Integer> itemTypesSet = projectToItemTypesEntry.getValue();
			for (Integer itemTypeID : itemTypesSet) {
				TFieldConfigBean projectIssueTypeSpecificConfig = null;
				TFieldConfigBean ancestorProjectIssueTypeSpecificConfig = null;
				int ancestorLevelProjectIssueType = 0; 
				TFieldConfigBean projectSpecificConfig = null;
				TFieldConfigBean ancestorProjectConfig = null;
				int ancestorLevelProject = 0; 
				TFieldConfigBean projectTypeIssueTypeSpecificConfig = null;
				TFieldConfigBean projectTypeSpecificConfig = null;
				TFieldConfigBean issueTypeSpecificConfig = null;
				TFieldConfigBean defaultConfig = null;
				for (Integer fieldConfigID : fieldConfigIDsForField) {
					TFieldConfigBean fieldConfigBean = fieldConfigIDToFieldConfigBeanMap.get(fieldConfigID);
					Integer configProject = fieldConfigBean.getProject();
					Integer configIssueType = fieldConfigBean.getIssueType();
					Integer configProjectType = fieldConfigBean.getProjectType();
					if (EqualUtils.equal(configProject, projectID) && EqualUtils.equal(configIssueType, itemTypeID)) {
						projectIssueTypeSpecificConfig = fieldConfigBean;
						//break because more specific can't be found among fieldConfigIDsForField
						break;
					} else {
						Integer parentProjectID = ancestorProjectsHierarchyMap.get(projectID);
						int crtLevelAncestorProjectIssueType = 0;
						while (parentProjectID!=null) {
							crtLevelAncestorProjectIssueType++;
							if (EqualUtils.equal(configProject, parentProjectID) && EqualUtils.equal(configIssueType, itemTypeID)) {
								//ancestor project and issue type specific configuration found
								if (ancestorProjectIssueTypeSpecificConfig==null || crtLevelAncestorProjectIssueType<ancestorLevelProjectIssueType) {
									//first ancestor or a nearest ancestor found 
									ancestorProjectIssueTypeSpecificConfig = fieldConfigBean;
									ancestorLevelProjectIssueType = crtLevelAncestorProjectIssueType;
								}
								break;
							}
							parentProjectID = ancestorProjectsHierarchyMap.get(parentProjectID);
						}
						if (EqualUtils.equal(configProject, projectID) && configIssueType==null) {
							projectSpecificConfig = fieldConfigBean;
						} else {
							parentProjectID = ancestorProjectsHierarchyMap.get(projectID);
							int crtAncestorProject = 0;
							while (parentProjectID!=null) {
								crtAncestorProject++;
								if (EqualUtils.equal(configProject, parentProjectID)) {
									//ancestor project specific configuration found
									if (ancestorProjectConfig==null || crtAncestorProject<ancestorLevelProject) {
										//first ancestor or a nearest ancestor found 
										ancestorProjectConfig = fieldConfigBean;
										ancestorLevelProject = crtAncestorProject;
									}
									break;
								}
								parentProjectID = ancestorProjectsHierarchyMap.get(parentProjectID);
							}
							TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
							Integer projectTypeID = null;
							if (projectBean!=null) {
								projectTypeID = projectBean.getProjectType();
							}
							if (EqualUtils.equal(configProjectType, projectTypeID) && EqualUtils.equal(configIssueType, itemTypeID)) {
								projectTypeIssueTypeSpecificConfig = fieldConfigBean;
							} else {
								if (EqualUtils.equal(configProjectType, projectTypeID)&& configIssueType==null) {
									projectTypeSpecificConfig = fieldConfigBean;
								} else {
									if (EqualUtils.equal(configIssueType, itemTypeID)) {
										issueTypeSpecificConfig = fieldConfigBean;
									} else {
										if (fieldConfigBean.isDefault()) {
											defaultConfig = fieldConfigBean;
										}
									}
								}
							}
						}
					}
				}
				TFieldConfigBean fieldConfigBean = null;
				if (projectIssueTypeSpecificConfig!=null) {
					fieldConfigBean = projectIssueTypeSpecificConfig;
				} else {
					if (ancestorProjectIssueTypeSpecificConfig!=null) {
						fieldConfigBean = ancestorProjectIssueTypeSpecificConfig;
					} else {
						if (projectSpecificConfig!=null) {
							fieldConfigBean = projectSpecificConfig;
						} else {
							if (ancestorProjectConfig!=null) {
								fieldConfigBean = ancestorProjectConfig;
							} else {
								if (projectTypeIssueTypeSpecificConfig!=null) {
									fieldConfigBean = projectTypeIssueTypeSpecificConfig;
								} else {
									if (projectTypeSpecificConfig!=null) {
										fieldConfigBean = projectTypeSpecificConfig;
									} else {
										if (issueTypeSpecificConfig!=null) {
											fieldConfigBean = issueTypeSpecificConfig;
										} else {
											if (defaultConfig!=null) {
												fieldConfigBean = defaultConfig;
											}
										}
									}
								}
							}
						}
					}
				}
				if (fieldConfigBean!=null) {
					Integer listID = fieldConfigToList.get(fieldConfigBean.getObjectID());
					if (listID!=null) {
						addFieldConfigBean(fieldMap, fieldConfigBean, listID);
					}
				}
			}
		}		
		return fieldMap;
	}
	
	/**
	 * Add a field configuration if found
	 * @param listToFieldConfigsMap
	 * @param fieldConfigBean
	 * @param listID
	 */
	private static void addFieldConfigBean(Map<Integer, List<TFieldConfigBean>> listToFieldConfigsMap,
			TFieldConfigBean fieldConfigBean, Integer listID) {		
		List<TFieldConfigBean> fieldConfigsBeans =  listToFieldConfigsMap.get(listID);
		if (fieldConfigsBeans==null) {
			fieldConfigsBeans = new LinkedList<TFieldConfigBean>();
			listToFieldConfigsMap.put(listID, fieldConfigsBeans);
		}
		boolean configFound = false;
		for (TFieldConfigBean includedFieldConfigBean : fieldConfigsBeans) {
			if (includedFieldConfigBean.getObjectID().equals(fieldConfigBean.getObjectID())) {
				configFound = true;
				break;
			}
		}
		if (!configFound) {
			//do not add the same configuration two times (for ex. projectType specific configurations
			//found for two projects of same project type, or issueType specific configurations for two projects)
			fieldConfigsBeans.add(fieldConfigBean);
		}
	}
	
	/**
	 * Get all configurations for involved projects, projectTypes and issueTypes
	 * the same fieldConfigBean appear more than one times (for combined configurations: issueType - project or projectType) 
	 * @param customListFieldIDs
	 * @param issueTypeIDs
	 * @param projectTypeIDs
	 * @param projectIDs
	 * @return
	 */
	private static List<TFieldConfigBean> getPossibleFieldConfigs(List<Integer> customListFieldIDs,
			List<Integer> issueTypeIDs, List<Integer> projectTypeIDs, List<Integer> projectIDs) {
		List<TFieldConfigBean> fieldConfigBeans = new LinkedList<TFieldConfigBean>();
		fieldConfigBeans.addAll(FieldConfigBL.loadDefaultForFields(customListFieldIDs));
		List<TFieldConfigBean> issueTypeFieldConfigs = FieldConfigBL.loadByIssueTypesAndFields(issueTypeIDs, customListFieldIDs);
		if (issueTypeFieldConfigs!=null) {
			fieldConfigBeans.addAll(issueTypeFieldConfigs);
		}
		List<TFieldConfigBean> projectTypeFieldConfigs = FieldConfigBL.loadByProjectTypesAndFields(projectTypeIDs, customListFieldIDs);
		if (projectTypeFieldConfigs!=null) {
			fieldConfigBeans.addAll(projectTypeFieldConfigs);
		}
		List<TFieldConfigBean> projectFieldConfigs = FieldConfigBL.loadByProjectsAndFields(projectIDs, customListFieldIDs);
		if (projectFieldConfigs!=null) {
			fieldConfigBeans.addAll(projectFieldConfigs);
		}
		return fieldConfigBeans;
	}
	
	/**
	 * Gather all involved optionSettings
	 */
	private static Map<Integer, Integer> getFieldConfigToListMap(Set<Integer> fieldConfigSet) { 
		List<TOptionSettingsBean> optionSettingsList = OptionSettingsBL.loadByConfigList(
				GeneralUtils.createIntegerListFromCollection(fieldConfigSet));
		Map<Integer, Integer> fieldConfigToList = new HashMap<Integer, Integer>();
		for (TOptionSettingsBean optionSettingsBean : optionSettingsList) {
			Integer fieldConfigID = optionSettingsBean.getConfig();
			Integer listID = optionSettingsBean.getList();
			fieldConfigToList.put(fieldConfigID, listID);
		}
		return fieldConfigToList;
	}
	/**
	 * Get the listID to contextual list label for each list type fieldID: customFieldID -> customListID -> custom list name + (context) 
	 * @param fieldIDToListIDToFieldConfigs
	 * @param projectTypeMap
	 * @param locale
	 * @return
	 */
	public static Map<Integer, Map<Integer, String>> getFieldIDsToListIDToLabelsMap(
			Map<Integer, Map<Integer, List<TFieldConfigBean>>> fieldIDToListIDToFieldConfigs, Locale locale, boolean labelIsWithContext) {
		Map<Integer, TProjectTypeBean> projectTypeMap = new HashMap<Integer, TProjectTypeBean>();
		Map<Integer, Map<Integer, String>> fieldIDToListIDLabels = new HashMap<Integer, Map<Integer,String>>();
		for (Integer fieldID : fieldIDToListIDToFieldConfigs.keySet()) {
			Map<Integer, List<TFieldConfigBean>> listToFieldConfigs = fieldIDToListIDToFieldConfigs.get(fieldID);
			Map<Integer, String> listIDToLabels = new HashMap<Integer, String>();
			fieldIDToListIDLabels.put(fieldID, listIDToLabels);
			for (Integer listID : listToFieldConfigs.keySet()) {
				TListBean listBean = ListBL.loadByPrimaryKey(listID);
				List<TFieldConfigBean> fieldConfigs = listToFieldConfigs.get(listID);
				StringBuilder stringBuilder = new StringBuilder(listBean.getLabel());
				if (labelIsWithContext) {
					boolean firstContextLabel = true;
					for (TFieldConfigBean fieldConfigBean : fieldConfigs) {
						Integer issueTypeID = fieldConfigBean.getIssueType();
						Integer projectTypeID = fieldConfigBean.getProjectType();
						Integer projectID = fieldConfigBean.getProject();
						String issueTypeLabel = null;
						if (issueTypeID!=null) {
							TListTypeBean listTypeBean = LookupContainer.getItemTypeBean(issueTypeID, locale);// issueTypeMap.get(issueTypeID);
							if (listTypeBean!=null) {
								issueTypeLabel = listTypeBean.getLabel();
							}
						}
						String projectTypeLabel = null;
						if (projectTypeID!=null) {
							TProjectTypeBean projectTypeBean = projectTypeMap.get(projectTypeID);
							if (projectTypeBean!=null) {
								projectTypeLabel = projectTypeBean.getLabel();
							} else {
								projectTypeBean = ProjectTypesBL.loadByPrimaryKey(projectTypeID);
								if (projectTypeBean!=null) {
									projectTypeMap.put(projectTypeID, projectTypeBean);
									projectTypeLabel = projectTypeBean.getLabel();
								}
							}
						}
						String projectLabel = null;
						if (projectID!=null) {
							TProjectBean projectBean = LookupContainer.getProjectBean(projectID);// projectsMap.get(projectID);
							if (projectBean!=null) {
								projectLabel = projectBean.getLabel();
							}
						}
						if (issueTypeLabel!=null || projectLabel!=null || projectTypeLabel!=null) {
							if (!firstContextLabel) {
								stringBuilder.append(", ");
							} else {
								stringBuilder.append(" (");
							}
							firstContextLabel = false;
						}
						if (issueTypeLabel!=null) {
							if (projectLabel!=null) {
								stringBuilder.append(projectLabel).append("-").append(issueTypeLabel);
							} else {
								if (projectTypeLabel!=null) {
									stringBuilder.append(projectTypeLabel).append("-").append(issueTypeLabel);
								}
								else {
									stringBuilder.append(issueTypeLabel);
								}
							}
						} else {
							if (projectLabel!=null) {
								stringBuilder.append(projectLabel);
							} else {
								if (projectTypeLabel!=null) {
									stringBuilder.append(projectTypeLabel);
								}
							}
						}
					}
					if (!firstContextLabel) {
						stringBuilder.append(")");
					}
				}
				listIDToLabels.put(listID, stringBuilder.toString());
			}
		}
		return fieldIDToListIDLabels;
	}
	
	/**
	 * Gets the most specific configurations for lists
	 * @param projectIssueTypeContexts
	 * @param projectTypeIDs
	 * @param projectIDs
	 * @param issueTypeIDs
	 * @param customListFieldIDs
	 * @return map with key: fieldID
	 * 					value map with key: project
	 * 									value: map with key: itemType
	 * 													value: listID
	 */
	public static Map<Integer, Map<Integer, Map<Integer, Integer>>> getListsInContext(
			Map<Integer, Set<Integer>> projectIssueTypeContexts, List<Integer> customListFieldIDs) {
		Set<Integer> involvedProjectsSet = new HashSet<Integer>();
		Set<Integer> involvedIssueTypesSet = new HashSet<Integer>();
		Set<Integer> involvedProjectTypesSet = new HashSet<Integer>();
		for (Map.Entry<Integer, Set<Integer>> entry : projectIssueTypeContexts.entrySet()) {
			Integer projectID = entry.getKey();
			involvedProjectsSet.add(projectID);
			TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
			if (projectBean!=null) {
				Integer projectTypeID = projectBean.getProjectType();
				if (projectTypeID!=null) {
					involvedProjectTypesSet.add(projectTypeID);
				}
			}
			Set<Integer> projectIssueTypesSet = entry.getValue();
			involvedIssueTypesSet.addAll(projectIssueTypesSet);
		}
		
		//include also the ancestor projects for possible fall back
		Map<Integer, Integer> ancestorProjectsHierarchyMap = ProjectBL.getAncestorProjectHierarchy(GeneralUtils.createIntegerArrFromCollection(involvedProjectsSet));
		List<Integer> ancestorProjectsList = GeneralUtils.createIntegerListFromCollection(ancestorProjectsHierarchyMap.keySet());
		
		//get all configurations for involved projects projectTypes and issueTypes
		//the same fieldConfigBean appear more than one times for combined configurations: issueType - project or projectType 
		List<TFieldConfigBean> fieldConfigBeans = getPossibleFieldConfigs(customListFieldIDs,
				GeneralUtils.createIntegerListFromCollection(involvedIssueTypesSet), 
				GeneralUtils.createIntegerListFromCollection(involvedProjectTypesSet), ancestorProjectsList);
		/**
		 * Gather data from field configurations 
		 */
		Set<Integer> fieldConfigSet = new HashSet<Integer>();
		Map<Integer, List<Integer>> fieldToFieldConfigMap = new HashMap<Integer, List<Integer>>();
		Map<Integer, TFieldConfigBean> fieldConfigIDToFieldConfigBeanMap = new HashMap<Integer, TFieldConfigBean>();
		for (TFieldConfigBean fieldConfigBean : fieldConfigBeans) {
			Integer fieldConfigID = fieldConfigBean.getObjectID();
			Integer fieldID = fieldConfigBean.getField();
			if (!fieldConfigSet.contains(fieldConfigID)) {
				//do not add the same fieldConfiguration two times
				fieldConfigSet.add(fieldConfigID);	
				fieldConfigIDToFieldConfigBeanMap.put(fieldConfigID, fieldConfigBean);
				List<Integer> configsForField = fieldToFieldConfigMap.get(fieldID);
				if (configsForField==null) {
					configsForField = new LinkedList<Integer>();
					fieldToFieldConfigMap.put(fieldID, configsForField);
				}
				configsForField.add(fieldConfigID);
			}
		}
		/**
		 * Get the optionSettings for fieldConfigs
		 */
		Map<Integer, Integer> fieldConfigToListMap = getFieldConfigToListMap(fieldConfigSet);
		Map<Integer, Map<Integer, Map<Integer, Integer>>> listsInContext = new HashMap<Integer, Map<Integer,Map<Integer,Integer>>>();
		for (Integer fieldID : customListFieldIDs) {
			List<Integer> fieldConfigIDsForField = fieldToFieldConfigMap.get(fieldID);
			Map<Integer, Map<Integer, Integer>> listForFieldInContext = getListForFieldInContext(
					projectIssueTypeContexts, fieldConfigIDsForField,
					fieldConfigIDToFieldConfigBeanMap, fieldConfigToListMap, ancestorProjectsHierarchyMap);
			listsInContext.put(fieldID, listForFieldInContext);
		}		
		return listsInContext;
	}
	
	/**
	 * Gets the most specific lists in each context  
	 * @param projectIssueTypeContexts
	 * @param fieldToFieldConfigMap
	 * @param fieldConfigIDToFieldConfigBeanMap
	 * @param fieldConfigToList
	 * @param ancestorProjectsHierarchyMap
	 * @return map with key: project
	 * 					value: map with key: itemType
	 * 									value: listID
	 */
	private static Map<Integer, Map<Integer, Integer>> getListForFieldInContext(
			Map<Integer, Set<Integer>> projectIssueTypeContexts, List<Integer> fieldConfigIDsForField,
			Map<Integer, TFieldConfigBean> fieldConfigIDToFieldConfigBeanMap, Map<Integer, Integer> fieldConfigToList, Map<Integer, Integer> ancestorProjectsHierarchyMap) {
		Map<Integer, Map<Integer, Integer>> projectToIssueTypeToList = new HashMap<Integer, Map<Integer,Integer>>();
		for (Map.Entry<Integer,  Set<Integer>> projectEntry : projectIssueTypeContexts.entrySet()) {
			Integer projectID = projectEntry.getKey();
			Set<Integer> issueTypesSet = projectEntry.getValue();
			Map<Integer, Integer> issueTypeToListForProject = new HashMap<Integer, Integer>();
			projectToIssueTypeToList.put(projectID, issueTypeToListForProject);
			for (Integer issueTypeID : issueTypesSet) {
				TFieldConfigBean projectIssueTypeSpecificConfig = null;
				TFieldConfigBean ancestorProjectIssueTypeSpecificConfig = null;
				int ancestorLevelProjectIssueType = 0; 
				TFieldConfigBean projectSpecificConfig = null;
				TFieldConfigBean ancestorProjectConfig = null;
				int ancestorLevelProject = 0; 
				TFieldConfigBean projectTypeIssueTypeSpecificConfig = null;
				TFieldConfigBean projectTypeSpecificConfig = null;
				TFieldConfigBean issueTypeSpecificConfig = null;
				TFieldConfigBean defaultConfig = null;
				for (Integer fieldConfigID : fieldConfigIDsForField) {
					TFieldConfigBean fieldConfigBean = fieldConfigIDToFieldConfigBeanMap.get(fieldConfigID);
					Integer configProject = fieldConfigBean.getProject();
					Integer configIssueType = fieldConfigBean.getIssueType();
					Integer configProjectType = fieldConfigBean.getProjectType();
					if (EqualUtils.equal(configProject, projectID) && EqualUtils.equal(configIssueType, issueTypeID)) {
						//project and issue type specific configuration found
						projectIssueTypeSpecificConfig = fieldConfigBean;
						//break because more specific can't be found among fieldConfigIDsForField
						break;
					} else {
						Integer parentProjectID = ancestorProjectsHierarchyMap.get(projectID);
						int crtLevelAncestorProjectIssueType = 0;
						while (parentProjectID!=null) {
							crtLevelAncestorProjectIssueType++;
							if (EqualUtils.equal(configProject, parentProjectID) && EqualUtils.equal(configIssueType, issueTypeID)) {
								//ancestor project and issue type specific configuration found
								if (ancestorProjectIssueTypeSpecificConfig==null || crtLevelAncestorProjectIssueType<ancestorLevelProjectIssueType) {
									//first ancestor or a nearest ancestor found 
									ancestorProjectIssueTypeSpecificConfig = fieldConfigBean;
									ancestorLevelProjectIssueType = crtLevelAncestorProjectIssueType;
								}
								break;
							}
							parentProjectID = ancestorProjectsHierarchyMap.get(parentProjectID);
						}
						if (EqualUtils.equal(configProject, projectID) && configIssueType==null) {
							//project specific configuration found
							projectSpecificConfig = fieldConfigBean;
						} else {
							parentProjectID = ancestorProjectsHierarchyMap.get(projectID);
							int crtAncestorProject = 0;
							while (parentProjectID!=null) {
								crtAncestorProject++;
								if (EqualUtils.equal(configProject, parentProjectID)) {
									//ancestor project specific configuration found
									if (ancestorProjectConfig==null || crtAncestorProject<ancestorLevelProject) {
										//first ancestor or a nearest ancestor found 
										ancestorProjectConfig = fieldConfigBean;
										ancestorLevelProject = crtAncestorProject;
									}
									break;
								}
								parentProjectID = ancestorProjectsHierarchyMap.get(parentProjectID);
							}
							TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
							Integer projectTypeID = null;
							if (projectBean!=null) {
								projectTypeID = projectBean.getProjectType();
							}
							if (EqualUtils.equal(configProjectType, projectTypeID) && EqualUtils.equal(configIssueType, issueTypeID)) {
								//project type issue type specific configuration found
								projectTypeIssueTypeSpecificConfig = fieldConfigBean;
							} else {
								if (EqualUtils.equal(configProjectType, projectTypeID) && configIssueType==null) {
									//project type specific configuration found
									projectTypeSpecificConfig = fieldConfigBean;
								} else {
									if (EqualUtils.equal(configIssueType, issueTypeID)) {
										//issue type specific configuration found
										issueTypeSpecificConfig = fieldConfigBean;
									} else {
										if (fieldConfigBean.isDefault()) {
											//default config found
											defaultConfig = fieldConfigBean;
										}
									} 
								}
							}
						}
					}
				}
				TFieldConfigBean fieldConfigBean = null;
				if (projectIssueTypeSpecificConfig!=null) {
					fieldConfigBean = projectIssueTypeSpecificConfig;
				} else {
					if (ancestorProjectIssueTypeSpecificConfig!=null) {
						fieldConfigBean = ancestorProjectIssueTypeSpecificConfig;
					} else {
						if (projectSpecificConfig!=null) {
							fieldConfigBean = projectSpecificConfig;
						} else {
							if (ancestorProjectConfig!=null) {
								fieldConfigBean = ancestorProjectConfig;
							} else {
								if (projectTypeIssueTypeSpecificConfig!=null) {
									fieldConfigBean = projectTypeIssueTypeSpecificConfig;
								} else {
									if (projectTypeSpecificConfig!=null) {
										fieldConfigBean = projectTypeSpecificConfig;
									} else {
										if (issueTypeSpecificConfig!=null) {
											fieldConfigBean = issueTypeSpecificConfig;
										} else {
											if (defaultConfig!=null) {
												fieldConfigBean = defaultConfig;
											}
										}
									}
								}
							}
						}
					}
				}
				if (fieldConfigBean!=null) {
					Integer listID = fieldConfigToList.get(fieldConfigBean.getObjectID());
					issueTypeToListForProject.put(issueTypeID, listID);
				}
			}
		}
		return projectToIssueTypeToList;
	}
}
