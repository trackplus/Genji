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


package com.aurel.track.fieldType.runtime.bl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.admin.customize.treeConfig.field.FieldConfigBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldConfigItemFacade;
import com.aurel.track.admin.customize.treeConfig.field.GeneralSettingsBL;
import com.aurel.track.admin.customize.treeConfig.field.OptionSettingsBL;
import com.aurel.track.admin.customize.treeConfig.field.TextBoxSettingsBL;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TGeneralSettingsBean;
import com.aurel.track.beans.TOptionSettingsBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TTextBoxSettingsBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.FieldConfigDAO;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.resources.LocalizationKeyPrefixes;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.EqualUtils;


/**
 * Business logic methods for field configurations and field settings
 * @author Tamas Ruff
 *
 */
public class FieldRuntimeBL {
	private static FieldConfigDAO fieldConfigDAO = DAOFactory.getFactory().getFieldConfigDAO();
	
	/**
	 * Get default configuration for all fields
	 * @param locale
	 * @return
	 */
	public static List<TFieldConfigBean> getLocalizedDefaultFieldConfigs(Locale locale) {
		return LocalizeUtil.localizeFieldConfigs(FieldConfigBL.loadDefault(), locale);
	}
	
	/**
	 * Get default configuration for all fields
	 * @param locale
	 * @return
	 */
	public static Map<Integer, TFieldConfigBean> getLocalizedDefaultFieldConfigsMap(Locale locale) {
		List<TFieldConfigBean> fieldConfigList = getLocalizedDefaultFieldConfigs(locale);
		Map<Integer, TFieldConfigBean> fieldConfigLocalizedMap = new HashMap<Integer, TFieldConfigBean>();
		for (TFieldConfigBean fieldConfigBean : fieldConfigList) {
			fieldConfigLocalizedMap.put(fieldConfigBean.getField(), fieldConfigBean);
		}
		return fieldConfigLocalizedMap;
	}
	
	/**
	 * Get a map with the best matching field configs, including the present and required fields
	 * key: fieldID 
	 * value: TFieldConfigBean set with the localized labels  
	 * @param presentFields 
	 * @param workItem
	 * @param locale
	 * @return
	 */
	public static Map<Integer, TFieldConfigBean> getLocalizedFieldConfigs(Set<Integer> presentFields, Integer projectID, Integer issueTypeID, Locale locale) {
		Map<Integer, TFieldConfigBean> fieldConfigMap = getFieldConfigsMap(projectID, issueTypeID, locale);
		Map<Integer, TFieldConfigBean> fieldConfigFilteredMap = new HashMap<Integer, TFieldConfigBean>();
		for (Integer fieldID : fieldConfigMap.keySet()) {
			TFieldConfigBean fieldConfigBean = fieldConfigMap.get(fieldID);
			if (presentFields.contains(fieldID)) {
				//add the configs for the fields present on the screen
				fieldConfigFilteredMap.put(fieldID, fieldConfigBean);
			}
		}
		return fieldConfigFilteredMap;
	}
	
	/**
	 * Get the map with best matching FieldConfigBeans
	 * key: fieldID 
	 * value: TFieldConfigBean
	 * @param workItem
	 * @return
	 */
	public static Map<Integer, TFieldConfigBean> getFieldConfigsMap(Integer projectID, Integer issueTypeID, Locale locale) {
		Map<Integer, TFieldConfigBean> fieldConfigMap = new HashMap<Integer, TFieldConfigBean>();
		//project - itemType configuration
		List<TFieldConfigBean> issueTypeProjectFieldConfigs = 
				fieldConfigDAO.loadByIssueTypeAndProject(issueTypeID, projectID);
		addNotPresentToMap(issueTypeProjectFieldConfigs, fieldConfigMap, locale);
		TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
		if (projectBean!=null) {
			Integer parentProjectID = projectBean.getParent();
			//ancestor project - issueType configuration
			while (parentProjectID!=null) {
				TProjectBean parentProjectBean = LookupContainer.getProjectBean(parentProjectID);
				if (parentProjectBean!=null) {
					if (EqualUtils.equal(projectBean.getProjectType(), parentProjectBean.getProjectType())) {
						List<TFieldConfigBean> issueTypeaAncestorProjectFieldConfigs =  fieldConfigDAO.loadByIssueTypeAndProject(issueTypeID, parentProjectID);
						addNotPresentToMap(issueTypeaAncestorProjectFieldConfigs, fieldConfigMap, locale);
					}
					parentProjectID = parentProjectBean.getParent();
				} else {
					break;
				}
			}
			//project type - item type configuration
			Integer projectTypeID = projectBean.getProjectType();
			if (projectTypeID!=null) {
				List<TFieldConfigBean> issueTypeProjectTypeFieldConfigs = fieldConfigDAO.loadByIssueTypeAndProjectType(
					issueTypeID, projectTypeID);
				addNotPresentToMap(issueTypeProjectTypeFieldConfigs, fieldConfigMap, locale);
			}
		}
		//project configuration
		List<TFieldConfigBean> projectFieldConfigs = fieldConfigDAO.loadByProject(projectID);
		addNotPresentToMap(projectFieldConfigs, fieldConfigMap, locale);
		if (projectBean!=null) {
			Integer parentProjectID = projectBean.getParent();
			//ancestor project configuration
			while (parentProjectID!=null) {
				TProjectBean parentProjectBean = LookupContainer.getProjectBean(parentProjectID);
				if (parentProjectBean!=null) {
					if (EqualUtils.equal(projectBean.getProjectType(), parentProjectBean.getProjectType())) {
						List<TFieldConfigBean> ancestorProjectFieldConfigs =  fieldConfigDAO.loadByProject(parentProjectID);
						addNotPresentToMap(ancestorProjectFieldConfigs, fieldConfigMap, locale);
					}
					parentProjectID = parentProjectBean.getParent();
				} else {
					break;
				}
			}
			//project type configuration
			List<TFieldConfigBean> projectTypeFieldConfigs = fieldConfigDAO.loadByProjectType(projectBean.getProjectType());
			addNotPresentToMap(projectTypeFieldConfigs, fieldConfigMap, locale);
		}
		//item type configuration
		List<TFieldConfigBean> issueTypeFieldConfigs = fieldConfigDAO.loadByIssueType(issueTypeID);
		addNotPresentToMap(issueTypeFieldConfigs, fieldConfigMap, locale);
		//default configuration
		List<TFieldConfigBean> defaultFieldConfigs = FieldConfigBL.loadDefault();
		addNotPresentToMap(defaultFieldConfigs, fieldConfigMap, locale);
		return fieldConfigMap;
	}
	
	/**
	 * Helper method to put the TFieldConfigBeans in the map
	 * 
	 * @param fieldConfigMap
	 * @param presentFields
	 * @param fieldConfigs
	 */
	private static void addNotPresentToMap(List<TFieldConfigBean> fieldConfigs, 
			Map<Integer, TFieldConfigBean> fieldConfigMap, Locale locale) {
		if (fieldConfigs!=null) {
			for (TFieldConfigBean fieldConfigBean : fieldConfigs) {
				Integer fieldID = fieldConfigBean.getField();
				if (fieldConfigMap.get(fieldID)==null) {
					if (locale!=null) {
						fieldConfigBean = LocalizeUtil.localizeFieldConfig(fieldConfigBean, locale);
					}
					fieldConfigMap.put(fieldID, fieldConfigBean);
				}
			}
		}
	}
	
	/**
	 * Get the map with best matching FieldConfigBeans
	 * key: fieldID 
	 * value: TFieldConfigBean
	 * @param workItem
	 * @return
	 */
	public static void getFieldConfigMaps(List<TFieldConfigBean> fieldConfigBeans,
			Map<Integer, Map<Integer, Map<Integer, TFieldConfigBean>>> issueTypeProjectMap,
			Map<Integer, Map<Integer, Map<Integer, TFieldConfigBean>>> issueTypeProjectTypeMap,
			Map<Integer, Map<Integer, TFieldConfigBean>> projectMap,
			Map<Integer, Map<Integer, TFieldConfigBean>> projectTypeMap,
			Map<Integer, Map<Integer, TFieldConfigBean>> issueTypeMap,
			Map <Integer, TFieldConfigBean> defaultMap) {
		if (fieldConfigBeans!=null) {
			Iterator<TFieldConfigBean> iterator = fieldConfigBeans.iterator();
			while (iterator.hasNext()) {
				TFieldConfigBean fieldConfigBean = iterator.next();
				Integer issueType = fieldConfigBean.getIssueType();
				Integer project = fieldConfigBean.getProject();
				Integer projectType = fieldConfigBean.getProject();
				Integer fieldID = fieldConfigBean.getField();
				if (issueType!=null) {
					if (project!=null) {
						//issueType + project
						Map<Integer, Map<Integer, TFieldConfigBean>> fieldForIssueTypeForProject = issueTypeProjectMap.get(issueType);
						if (fieldForIssueTypeForProject==null) {
							fieldForIssueTypeForProject = new HashMap<Integer, Map<Integer, TFieldConfigBean>>();
							issueTypeProjectMap.put(issueType, fieldForIssueTypeForProject);
						}
						Map<Integer, TFieldConfigBean> fieldForProject = fieldForIssueTypeForProject.get(project);
						if (fieldForProject==null) {
							fieldForProject = new HashMap<Integer, TFieldConfigBean>();
							fieldForIssueTypeForProject.put(project, fieldForProject);
						}
						fieldForProject.put(fieldID, fieldConfigBean);
					} else {
						if (projectType!=null) {
							//issueType + projectType
							Map<Integer, Map<Integer, TFieldConfigBean>> fieldForIssueTypeForProjectType = issueTypeProjectMap.get(issueType);
							if (fieldForIssueTypeForProjectType==null) {
								fieldForIssueTypeForProjectType = new HashMap<Integer, Map<Integer, TFieldConfigBean>>();
								issueTypeProjectMap.put(issueType, fieldForIssueTypeForProjectType);
							}
							Map<Integer, TFieldConfigBean> fieldForProjectType = fieldForIssueTypeForProjectType.get(projectType);
							if (fieldForProjectType==null) {
								fieldForProjectType = new HashMap<Integer, TFieldConfigBean>();
								fieldForIssueTypeForProjectType.put(projectType, fieldForProjectType);
							}
							fieldForProjectType.put(fieldID, fieldConfigBean);
						} else {
							//issueType	
							Map<Integer, TFieldConfigBean> fieldForIssueType = issueTypeMap.get(issueType);
							if (fieldForIssueType==null) {
								fieldForIssueType = new HashMap<Integer, TFieldConfigBean>();
								issueTypeMap.put(issueType, fieldForIssueType);
							}
							fieldForIssueType.put(fieldID, fieldConfigBean);
						}
					}
				} else {
					if (project!=null) {
						//project	
						Map<Integer, TFieldConfigBean> fieldForProject = projectMap.get(project);
						if (fieldForProject==null) {
							fieldForProject = new HashMap<Integer, TFieldConfigBean>();
							projectMap.put(project, fieldForProject);
						}
						fieldForProject.put(fieldID, fieldConfigBean);
					} else {
						if (projectType!=null) {
							//project	
							Map<Integer, TFieldConfigBean> fieldForProjectType = projectTypeMap.get(projectType);
							if (fieldForProjectType==null) {
								fieldForProjectType = new HashMap<Integer, TFieldConfigBean>();
								projectTypeMap.put(projectType, fieldForProjectType);
							}
							fieldForProjectType.put(fieldID, fieldConfigBean);
						} else {
							//default
							defaultMap.put(fieldID, fieldConfigBean);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Get the localized default field configs for 
	 * all fields which have a matcher defined 
	 * @param locale
	 * @return
	 */
	public static List<TFieldConfigBean> getDefaultFieldConfigsWithMatcher(Locale locale) {
		List<TFieldConfigBean> fieldConfigList = new LinkedList<TFieldConfigBean>();
		List<TFieldConfigBean> defaultFieldConfigsList = LocalizeUtil.localizeFieldConfigs(FieldConfigBL.loadDefault(), locale);
		for (TFieldConfigBean fieldConfigBean : defaultFieldConfigsList) {
			Integer fieldID = fieldConfigBean.getField();
				//unfortunately comment and description have the 
				//same fieldTypeRT SystemLongTextRT but the matcher works only for description
				//(the comments are not loaded in the ReportBean) so we get it out explicitly
				//TODO split the Description and Comment in different fieldTypeRT
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
			if (fieldTypeRT!=null) {
				if (fieldTypeRT.processLoadMatcherDT(fieldID)!=null) {
					fieldConfigList.add(fieldConfigBean);
				}
			}
		}
		//the TFieldConfigBean is comparable
		return fieldConfigList;
	}
	
	/**
	 * Get a localized default field config for 
	 * a fieldID if it has a matcher defined.
	 * Otherwise return null
	 * @param fieldID 
	 * @param locale
	 * @return
	 */
	public static TFieldConfigBean getDefaultFieldConfigIfMatcher(Integer fieldID, Locale locale) {
		IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
		if (fieldTypeRT!=null) {
			if (fieldTypeRT.processLoadMatcherDT(fieldID)!=null) {
				TFieldConfigBean fieldConfigBean = FieldConfigBL.loadDefault(fieldID);
				if (fieldConfigBean!=null) {
					return LocalizeUtil.localizeFieldConfig(fieldConfigBean, locale);
				}
			}
		}
		return null;
	}
	
	/**
	 * Get a localized default field config for 
	 * a fieldID if it has a matcher defined.
	 * Otherwise return null
	 * @param fieldID 
	 * @return
	 */
	public static TFieldConfigBean getDefaultFieldConfig(Integer fieldID) {
		IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
		if (fieldTypeRT!=null) {
			return FieldConfigBL.loadDefault(fieldID);
		}
		return null;
	}
	
	/**
	 * Get a localized default field config for 
	 * a fieldID if it has a matcher defined.
	 * Otherwise return null
	 * @param fieldID 
	 * @param locale
	 * @return
	 */
	public static TFieldConfigBean getDefaultFieldConfig(Integer fieldID, Locale locale) {
		IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
		if (fieldTypeRT!=null) {
			TFieldConfigBean fieldConfigBean = FieldConfigBL.loadDefault(fieldID);
			if (fieldConfigBean!=null) {
				return LocalizeUtil.localizeFieldConfig(fieldConfigBean, locale);
			}
		}
		return null;
	}
	
	/**
	 * Get a localized default label for a fieldID 
	 * @param fieldID 
	 * @param locale
	 * @return
	 */
	public static String getLocalizedDefaultFieldLabel(Integer fieldID, Locale locale) {
		TFieldConfigBean fieldConfigBean = getDefaultFieldConfig(fieldID, locale);
		if (fieldConfigBean!=null) {
			return fieldConfigBean.getLabel();
		}		
		return "";
	}
	
	/**
	 * Localize a single TFieldConfigBean
	 * i.e. set the localized fields localizedLabel and localizedTooltip
	 * Fall back to BoxResources if not found in the database 
	 * @param fieldConfigBean
	 * @param locale
	 * @return
	 */
	public static String localizeFieldConfig(Integer fieldConfigID, Locale locale) {
		if (fieldConfigID!=null) {
			return LocalizeUtil.getLocalizedEntity(
					LocalizationKeyPrefixes.FIELD_LABEL_KEY_PREFIX, fieldConfigID, locale);
			
		}
		return "";
	}
	
	/**
	 * Get a localized default labels map for fieldIDs
	 * @param fieldIDs
	 * @param locale
	 */
	public static Map<Integer, String> getLocalizedDefaultFieldLabels(List<Integer> fieldIDs, Locale locale) {
		List<TFieldConfigBean> fieldConfigList = FieldConfigBL.loadDefaultForFields(fieldIDs);
		return LocalizeUtil.getLocalizedFieldConfigLables(fieldConfigList, locale);
	}
	
	/**
	 * Returns the most specific valid configID for a field
	 * @param fieldID
	 * @param issueTypeID
	 * @param projectID
	 */
	public static TFieldConfigBean getValidConfig(Integer fieldID, Integer issueTypeID, Integer projectID) {
		return (TFieldConfigBean)FieldConfigItemFacade.getInstance().getValidConfigFallback(issueTypeID, null, projectID, fieldID);
	}
	
	/**
	 * Get the fieldConfigs map pro project and issueType
	 * @param workItemBeanList
	 * @param presentFieldIDs
	 * @param locale
	 * @return
	 */
	public static Map<Integer, Map<Integer, Map<Integer, TFieldConfigBean>>> getFieldConfigsForWorkItemBeans(
			Collection<TWorkItemBean> workItemBeanList, Set<Integer> presentFieldIDs, Locale locale) {
		Map<Integer, Map<Integer, Map<Integer, TFieldConfigBean>>> projectsIssueTypesFieldConfigsMap = 
			new HashMap<Integer, Map<Integer, Map<Integer, TFieldConfigBean>>>();
		Iterator<TWorkItemBean> iterator = workItemBeanList.iterator();
		while (iterator.hasNext()) {
			TWorkItemBean workItemBean = iterator.next();
			Integer projectID = workItemBean.getProjectID();
			Integer issueTypeID = workItemBean.getListTypeID();
			Map<Integer, Map<Integer, TFieldConfigBean>> issueTypesFieldConfigsMap = projectsIssueTypesFieldConfigsMap.get(projectID);
			if (issueTypesFieldConfigsMap == null) {
				issueTypesFieldConfigsMap = new HashMap<Integer, Map<Integer,TFieldConfigBean>>();
				projectsIssueTypesFieldConfigsMap.put(projectID, issueTypesFieldConfigsMap);
			}
			Map<Integer, TFieldConfigBean> fieldConfigsMap = issueTypesFieldConfigsMap.get(issueTypeID);
			if (fieldConfigsMap == null) {
				fieldConfigsMap = getLocalizedFieldConfigs(presentFieldIDs,
						projectID, issueTypeID, locale);
				issueTypesFieldConfigsMap.put(issueTypeID, fieldConfigsMap);
			}
		}
		return projectsIssueTypesFieldConfigsMap;
	}
	
	/**
	 * Get the fieldConfigs for fields by project and issueType
	 * @param projectsToIssueTypesForFieldsConfigMap
	 * @param project
	 * @param issueType
	 * @return
	 */
	public static Map<Integer, TFieldConfigBean> getFieldConfigsForProjectIssueType(
		Map<Integer, Map<Integer, Map<Integer, TFieldConfigBean>>> projectsToIssueTypesForFieldsConfigMap,
		Integer project, Integer issueType) {
		if (projectsToIssueTypesForFieldsConfigMap!=null) {
			Map<Integer, Map<Integer, TFieldConfigBean>> issueTypesFieldConfigsMap = 
				projectsToIssueTypesForFieldsConfigMap.get(project);
			if (issueTypesFieldConfigsMap!=null) {
				return issueTypesFieldConfigsMap.get(issueType);
			}
		}
		return null;
	}
	
	/**
	 * Get a fieldConfig by project issueType and field
	 * @param projectsToIssueTypesForFieldsConfigMap
	 * @param projectID
	 * @param issueTypeID
	 * @param fieldID
	 * @return
	 */
	public static TFieldConfigBean getFieldConfigForProjectIssueTypeField(
		Map<Integer, Map<Integer, Map<Integer, TFieldConfigBean>>> projectsToIssueTypesForFieldsConfigMap,
		Integer projectID, Integer issueTypeID, Integer fieldID) {
		if (projectsToIssueTypesForFieldsConfigMap!=null) {
			Map<Integer, Map<Integer, TFieldConfigBean>> issueTypesFieldConfigsMap = 
				projectsToIssueTypesForFieldsConfigMap.get(projectID);
			if (issueTypesFieldConfigsMap!=null) {
				Map<Integer, TFieldConfigBean> fieldConfigsMap = 
					issueTypesFieldConfigsMap.get(issueTypeID);
				if (fieldConfigsMap!=null) {
					return fieldConfigsMap.get(fieldID);
				}
			}
		}
		return null;
	}
	
	/**
	 * Gets the fieldConfigBeans for each selected field in each context 
	 * @param projectsToIssueTypes
	 * @param selectedFieldsSet
	 * @param locale
	 * @param targetProject
	 * @param targetIssueType
	 * @return
	 */
	public static Map<Integer, Map<Integer, Map<Integer, TFieldConfigBean>>> loadFieldConfigsInContextsAndTargetProjectAndIssueType(
			Map<Integer, Set<Integer>> projectsToIssueTypes, Set<Integer> selectedFieldsSet, Locale locale,
			Integer targetProject, Integer targetIssueType) {
		Map<Integer, Map<Integer, Map<Integer, TFieldConfigBean>>> projectsToIssueTypesForFieldsConfigMap = 
			new HashMap<Integer, Map<Integer, Map<Integer, TFieldConfigBean>>>();
		if (targetProject==null && targetIssueType==null) {
			//none of target project or issueType is specified
			for (Integer projectID : projectsToIssueTypes.keySet()) {
				Set<Integer> issueTypeSet = projectsToIssueTypes.get(projectID);
				if (issueTypeSet!=null && !issueTypeSet.isEmpty()) {
					Map<Integer, Map<Integer, TFieldConfigBean>> issueTypesRight = 
						new HashMap<Integer, Map<Integer, TFieldConfigBean>>();
					projectsToIssueTypesForFieldsConfigMap.put(projectID, issueTypesRight);
					for (Integer issueTypeID : issueTypeSet) {
						Map<Integer, TFieldConfigBean> fieldConfigMap = 
							getLocalizedFieldConfigs(selectedFieldsSet, projectID, issueTypeID, locale);
						issueTypesRight.put(issueTypeID, fieldConfigMap);
					}
				}
			}
		} else {
			if (targetProject!=null) {
				//target project is specified
				if (targetIssueType==null) {
					//target issue type is not specified
					//gather each issueType (from each "old" project) to verify with the targetProject
					Map<Integer, Map<Integer, TFieldConfigBean>> issueTypesRight = new HashMap<Integer, Map<Integer, TFieldConfigBean>>();
					projectsToIssueTypesForFieldsConfigMap.put(targetProject, issueTypesRight);
					for (Integer projectID : projectsToIssueTypes.keySet()) {
						Set<Integer> issueTypeSet = projectsToIssueTypes.get(projectID);
						if (issueTypeSet!=null && !issueTypeSet.isEmpty()) {
							for (Integer issueTypeID : issueTypeSet) {
								if (!issueTypesRight.containsKey(issueTypeID)) {
									Map<Integer, TFieldConfigBean> fieldConfigMap = 
										getLocalizedFieldConfigs(selectedFieldsSet, targetProject, issueTypeID, locale);
									issueTypesRight.put(issueTypeID, fieldConfigMap);
								}
							}
						}
					}
				}
				else {
					//both targetProject and targetIssueType is specified: only this context should be verified
					Map<Integer, Map<Integer, TFieldConfigBean>> issueTypesRight = new HashMap<Integer, Map<Integer, TFieldConfigBean>>();
					projectsToIssueTypesForFieldsConfigMap.put(targetProject, issueTypesRight);
					Map<Integer, TFieldConfigBean> fieldConfigMap = 
						getLocalizedFieldConfigs(selectedFieldsSet, targetProject, targetIssueType, locale);
					issueTypesRight.put(targetIssueType, fieldConfigMap);
				}
			} else {
				//no targetProject but targetIssueType is specified
				//for each project verify the target issueType
				for (Integer projectID : projectsToIssueTypes.keySet()) {
					Map<Integer, Map<Integer, TFieldConfigBean>> issueTypesRight = new HashMap<Integer, Map<Integer, TFieldConfigBean>>();
					projectsToIssueTypesForFieldsConfigMap.put(projectID, issueTypesRight);
					Map<Integer, TFieldConfigBean> fieldConfigMap = 
						getLocalizedFieldConfigs(selectedFieldsSet, projectID, targetIssueType, locale);
					issueTypesRight.put(targetIssueType, fieldConfigMap);
				}
			}
		}	
		return projectsToIssueTypesForFieldsConfigMap;
	}
	
	/**
	 * Get the fieldConfigs for fields by project and issueType
	 * @param projectsToIssueTypesForFieldsConfigMap
	 * @param projectID
	 * @param issueTypeID
	 * @return
	 */
	public static Map<String, Object> getFieldSettingsForProjectIssueType(
			Map<Integer, Map<Integer, Map<String, Object>>> projectsToIssueTypesFieldsSettingsMap,
		Integer projectID, Integer issueTypeID) {
		if (projectsToIssueTypesFieldsSettingsMap!=null) {
			Map<Integer, Map<String, Object>> issueTypesFieldSettingsMap = 
				projectsToIssueTypesFieldsSettingsMap.get(projectID);
			if (issueTypesFieldSettingsMap!=null) {
				return issueTypesFieldSettingsMap.get(issueTypeID);
			}
		}
		return null;
	}
	
	/**
	 * Get the fieldConfigs for fields by project and issueType
	 * @param projectsToIssueTypesForFieldsConfigMap
	 * @param projectID
	 * @param issueTypeID
	 * @return
	 */
	public static Object getFieldSettingsForProjectIssueTypeField(
			Map<Integer, Map<Integer, Map<String, Object>>> projectsToIssueTypesFieldsSettingsMap,
		Integer projectID, Integer issueTypeID, Integer fieldID) {
		if (projectsToIssueTypesFieldsSettingsMap!=null) {
			Map<Integer, Map<String, Object>> issueTypesFieldSettingsMap = 
				projectsToIssueTypesFieldsSettingsMap.get(projectID);
			if (issueTypesFieldSettingsMap!=null) {
				Map<String, Object> fieldSettingsMap = issueTypesFieldSettingsMap.get(issueTypeID);
				if (fieldSettingsMap!=null) {
					return fieldSettingsMap.get(MergeUtil.mergeKey(fieldID, null));
				}
			}
		}
		return null;
	}
	
	/**
	 *  Get the fieldSettings map pro project and issueType
	 * @param projectsIssueTypesFieldConfigsMap
	 * @return
	 */
	public static Map<Integer, Map<Integer, Map<String, Object>>> getFieldSettingsForFieldConfigs(
			Map<Integer, Map<Integer, Map<Integer, TFieldConfigBean>>> projectsIssueTypesFieldConfigsMap) {
		Map<Integer, Map<Integer, Map<String, Object>>> projectsIssueTypesFieldSettingsMap = 
			new HashMap<Integer, Map<Integer,Map<String, Object>>>();
		for (Integer projectID : projectsIssueTypesFieldConfigsMap.keySet()) {
			Map<Integer, Map<String, Object>> issueTypesFieldSettingsMap = new HashMap<Integer, Map<String, Object>>();
			projectsIssueTypesFieldSettingsMap.put(projectID, issueTypesFieldSettingsMap);
			Map<Integer, Map<Integer, TFieldConfigBean>> issueTypesFieldConfigsMap = projectsIssueTypesFieldConfigsMap.get(projectID);
			for (Integer issueTypeID : issueTypesFieldConfigsMap.keySet()) {
				Map<Integer, TFieldConfigBean> fieldConfigsMap = issueTypesFieldConfigsMap.get(issueTypeID);
				issueTypesFieldSettingsMap.put(issueTypeID, getFieldSettings(fieldConfigsMap));
			}
		}
		return projectsIssueTypesFieldSettingsMap;
	}
	
	/**
	 * Load the map of field settings
	 * @param configIDs
	 * @return
	 */
	public static Map<String, Object> getFieldSettings(Map<Integer, TFieldConfigBean> fieldConfigsMap) {
		List<Integer> configIDs = new LinkedList<Integer>();
		Map<Integer, Integer> configIDToFieldID = new HashMap<Integer, Integer>();
		Map<String, Object> fieldSettings = new HashMap<String, Object>();
		if (fieldConfigsMap!=null && !fieldConfigsMap.isEmpty()) {
			Collection<TFieldConfigBean> fieldConfigsList = fieldConfigsMap.values();
			for (TFieldConfigBean fieldConfigBean : fieldConfigsList) {
				Integer configID = fieldConfigBean.getObjectID();
				configIDs.add(configID);
				configIDToFieldID.put(configID, fieldConfigBean.getField());
			}
		}
		if (configIDs!=null && !configIDs.isEmpty()) {
			List<TOptionSettingsBean> optionSettingsList = OptionSettingsBL.loadByConfigList(configIDs);
			if (optionSettingsList!=null) {
				for (TOptionSettingsBean optionSettingsBean : optionSettingsList) {
					Integer fieldID = configIDToFieldID.get(optionSettingsBean.getConfig());
					fieldSettings.put(fieldID + TWorkItemBean.LINKCHAR + optionSettingsBean.getParameterCode(), optionSettingsBean);
				}
			}
			List<TTextBoxSettingsBean> textBoxSettingsList = TextBoxSettingsBL.loadByConfigList(configIDs);
			if (textBoxSettingsList!=null) {
				for (TTextBoxSettingsBean textBoxSettingsBean : textBoxSettingsList) {
					Integer fieldID = configIDToFieldID.get(textBoxSettingsBean.getConfig());
					fieldSettings.put(fieldID + TWorkItemBean.LINKCHAR + textBoxSettingsBean.getParameterCode(), textBoxSettingsBean);
				}
			}
			List<TGeneralSettingsBean> generalSettingsList = GeneralSettingsBL.loadByConfigList(configIDs);
			if (generalSettingsList!=null) {
				for (TGeneralSettingsBean generalSettingsBean : generalSettingsList) {
					Integer fieldID = configIDToFieldID.get(generalSettingsBean.getConfig());
					fieldSettings.put(fieldID + TWorkItemBean.LINKCHAR + generalSettingsBean.getParameterCode(), generalSettingsBean);
				}
			}
		}
		return fieldSettings;
	}
	
	/**
	 * Gets the possible bottom up fields
	 * @return
	 */
	public static Set<Integer> getPossibleBottomUpFields() {
		Set<Integer> bottomUpFields = new HashSet<Integer>();
		bottomUpFields.add(SystemFields.INTEGER_STARTDATE);
		bottomUpFields.add(SystemFields.INTEGER_ENDDATE);
		bottomUpFields.add(SystemFields.INTEGER_TOP_DOWN_START_DATE);
		bottomUpFields.add(SystemFields.INTEGER_TOP_DOWN_END_DATE);
		return bottomUpFields;
	}
}
