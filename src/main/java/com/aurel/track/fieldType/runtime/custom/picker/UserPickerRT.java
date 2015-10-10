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



package com.aurel.track.fieldType.runtime.custom.picker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.treeConfig.field.FieldConfigBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldConfigItemFacade;
import com.aurel.track.admin.customize.treeConfig.field.GeneralSettingsBL;
import com.aurel.track.admin.customize.workflow.activity.IActivityConfig;
import com.aurel.track.admin.customize.workflow.activity.IActivityExecute;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.IBeanID;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.ISerializableLabelBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TGeneralSettingsBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TPersonBean.PERSON_CATEGORY;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.exchange.track.NameMappingBL;
import com.aurel.track.fieldType.bulkSetters.IBulkSetter;
import com.aurel.track.fieldType.bulkSetters.UserPickerBulkSetter;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.fieldChange.FieldChangeValue;
import com.aurel.track.fieldType.fieldChange.apply.SelectFieldChangeApply;
import com.aurel.track.fieldType.fieldChange.config.MultipleListFieldChangeConfig;
import com.aurel.track.fieldType.fieldChange.config.SingleListFieldChangeConfig;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.SelectContext;
import com.aurel.track.fieldType.runtime.base.SerializableBeanAllowedContext;
import com.aurel.track.fieldType.runtime.bl.CustomSelectUtil;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherValue;
import com.aurel.track.fieldType.runtime.matchers.design.MatcherDatasourceContext;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.fieldType.types.custom.CustomUserPicker;
import com.aurel.track.fieldType.types.custom.CustomUserPicker.PARAMETERCODES;
import com.aurel.track.item.massOperation.MassOperationContext;
import com.aurel.track.item.massOperation.MassOperationValue;
import com.aurel.track.item.workflow.execute.WorkflowContext;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.util.GeneralUtils;

/**
 * Class for user picker
 * @author Tamas Ruff
 *
 */
public class UserPickerRT extends CustomPickerRT {
	private static final Logger LOGGER = LogManager.getLogger(UserPickerRT.class);
	
	public boolean inheritsOriginatorRole() {
		return false;
	}
	
	/**
	 * Whether the datasource for the picker is tree or list
	 * @return
	 */
	public boolean isTree() {
		return false;
	}
	
	/**
	 * Gets the iconCls class for label bean if dynamicIcons is false
	 * @param labelBean
	 */
	public String getIconCls(ILabelBean labelBean) {
		if (labelBean==null) {
			return null;
		} else {
			TPersonBean personBean = (TPersonBean)labelBean;
			if (personBean.isGroup()) {
				return PersonBL.GROUP_ICON_CLASS;
			}else{
				return PersonBL.USER_ICON_CLASS;
			}
		}
	}
	
	/**
	 * Identifies the fieldID of the fieldType the picker is based of
	 * A user picker is based on users but there are more than one 
	 * user system fields (manager/responsible/originator)
	 * By convention we take the id of the manager 
	 * @return
	 */	
	@Override
	public Integer getSystemOptionType() {
		return SystemFields.INTEGER_PERSON;
	}

	
	/**
	 * Loads the settings for a specific field like default value(s),
	 * datasources (for lists) etc. by editing an existing issue
	 * It might differ from loadCreateSettings because it could be need 
	 * to add existing entries in the list (also when right already revoked)
	 * @param selectContext 
	 * @return
	 */
	public List loadEditDataSource(SelectContext selectContext)	{
		TWorkItemBean workItemBean = selectContext.getWorkItemBean();
		Integer fieldID = selectContext.getFieldID();
		Integer parameterCode = selectContext.getParameterCode();
		//get the current options 
		Integer[] currentOptions = CustomSelectUtil.getSelectedOptions(
				workItemBean.getAttribute(fieldID, parameterCode));
		if (currentOptions==null || currentOptions.length==0) {
			LOGGER.debug("No current option seleced by loading the edit data source");
		}		 
		return loadDataSource(selectContext, currentOptions);
	}
	
	/**
	 * Returns the datasource for a userPicker field by creating a new issue.  
	 * When the list is empty this field should be set explicitly to null in the workItem. 
	 * (An empty HTML list will not send request parameter and it will remain the original value. This should be avoided)  
	 * @param selectContext
	 * @return
	 */
	public List loadCreateDataSource(SelectContext selectContext){
		return loadDataSource(selectContext, null);
	}
	
	/**
	 * Loads the datasource for the userPicker	
	 * @param selectContext 
	 * @return
	 */
	private List<ILabelBean> loadDataSource(SelectContext selectContext, Integer[] currentOptions) {
		Integer projectID = selectContext.getWorkItemBean().getProjectID();
		Integer issueTypeID = selectContext.getWorkItemBean().getListTypeID();
		TFieldConfigBean fieldConfigBean = selectContext.getFieldConfigBean();
		Integer configID = fieldConfigBean.getObjectID();
		return loadList(configID, projectID, issueTypeID, selectContext.getPersonID(), currentOptions);
	}
	/**
	 * Loads the datasource for the userPicker	
	 * @param selectContext 
	 * @return
	 */
	private List<ILabelBean> loadList(Integer configID, Integer projectID, Integer issueTypeID, Integer personID, Integer[] currentOptions) {
		List<ILabelBean> dataSource = new LinkedList<ILabelBean>();
		//get the datasource options
		TGeneralSettingsBean dataSourceSelectionBean = GeneralSettingsBL.loadSingleByConfigAndParameter(configID, 
				Integer.valueOf(CustomUserPicker.PARAMETERCODES.DATASOURCE_OPTION));
		if (dataSourceSelectionBean==null) {
			LOGGER.warn("The datasource selection bean is missing");
			return dataSource;
		}
		Integer selectedDataSource = dataSourceSelectionBean.getIntegerValue();
		if (selectedDataSource==null) {
			LOGGER.warn("The datasource selection value is missing");
			return dataSource;
		}
		
		//get the users by role
		if (selectedDataSource.intValue()==CustomUserPicker.DATASOURCE_OPTIONS.ROLE) {
			List<TGeneralSettingsBean> roleSelectionBeans = GeneralSettingsBL.loadListByConfigAndParameter(configID, 
					Integer.valueOf(CustomUserPicker.PARAMETERCODES.ROLE_OPTION));
			if (roleSelectionBeans==null || roleSelectionBeans.isEmpty()) {
				LOGGER.warn("No role selected");
			} else {
				List<Integer> roleIDs = new LinkedList<Integer>();
				Iterator<TGeneralSettingsBean> iterator = roleSelectionBeans.iterator();
				while (iterator.hasNext()) {
					TGeneralSettingsBean roleGeneralSettingsBean = iterator.next();
					roleIDs.add(roleGeneralSettingsBean.getIntegerValue());
				}
				dataSource = (List)PersonBL.loadAllPersonsAndGroupsByRoles(roleIDs, projectID, issueTypeID, currentOptions);
			}
		}
		//get the users by department
		if (selectedDataSource.intValue()==CustomUserPicker.DATASOURCE_OPTIONS.DEPARTMENT) {
			List<TGeneralSettingsBean> departmentSelectionBeans = GeneralSettingsBL.loadListByConfigAndParameter(configID, 
					Integer.valueOf(CustomUserPicker.PARAMETERCODES.DEPARTMENT_OPTION));
			if (departmentSelectionBeans==null || departmentSelectionBeans.isEmpty()) {
				LOGGER.warn("No department selected");
			} else {
				List<Integer> departmentIDs = new LinkedList<Integer>();
				Iterator<TGeneralSettingsBean> iterator = departmentSelectionBeans.iterator();
				while (iterator.hasNext()) {
					TGeneralSettingsBean departmentGeneralSettingsBean = iterator.next();
					Integer departmentID = departmentGeneralSettingsBean.getIntegerValue();
					if (departmentID!=null) {
						if (departmentID.intValue()==CustomUserPicker.CURRENT_USERS_DEPARTMENT) {
							TPersonBean personBean = LookupContainer.getPersonBean(personID);
							if (personBean!=null) {
								departmentID = personBean.getDepartmentID();
							}
						}
					}
					if (departmentID!=null && !departmentIDs.contains(departmentID)) {
						departmentIDs.add(departmentID);
					}
				}
				dataSource = (List)PersonBL.loadByDepartments(departmentIDs, currentOptions);
			}
		}
		return dataSource;
	}
	
	/**
	 * Gets the selected label beans
	 * @param optionIDs
	 * @param locale
	 * @return
	 */
	/*protected List<ILabelBean> getSelelectedLabelBeans(Integer[] optionIDs, Locale locale) {
		//symbolic value used when getShowValue() is called from QueryTreeBL.updateNodeExpressionTitle()
		//and in this case the array has only one element
		boolean symbolicFound = false;
		for (int i = 0; i < optionIDs.length; i++) {
			if (MatcherContext.LOGGED_USER_SYMBOLIC.equals(optionIDs[i])) {
				symbolicFound=true;
				break;
			}
		}
		//when called from other code the array can have more elements (when the select renderer is multiple)
		List<ILabelBean> labelBeans = (List)PersonBL.loadSortedPersonsOrGroups(
				GeneralUtils.createListFromIntArr(optionIDs));
		if (labelBeans==null) {
			labelBeans = new LinkedList<ILabelBean>();
		}
		if (symbolicFound) {
			labelBeans.add(0, getLabelBean(MatcherContext.LOGGED_USER_SYMBOLIC, locale));
		}
		return labelBeans;
	}*/
	
	/**
	 * Gets the label bean for an objectID
	 * @param objectID
	 * @param locale
	 * @return
	 */
	protected ILabelBean lookupLabelBean(Integer objectID, Locale locale) {
		return LookupContainer.getNotLocalizedLabelBean(getSystemOptionType(), objectID);
	}
	
	
	/**
	 * Replace the symbolic value with the actual value (when it is the case)
	 * @param integerArr
	 * @param matcherContext
	 * @return
	 */
	protected Integer[] getMatchValue(Integer[] integerArr, MatcherContext matcherContext) {
		if (integerArr!=null) {
			for (int i = 0; i < integerArr.length; i++) {
				Integer intMatchValue = integerArr[i];
				if (intMatchValue.intValue()==MatcherContext.LOGGED_USER_SYMBOLIC.intValue()) {
					Map<Integer, Integer> loggedUserReplacement = 
						(Map<Integer, Integer>)(matcherContext.getContextMap().get(
								MatcherContext.LOGGED_USER));
					if (loggedUserReplacement!=null && loggedUserReplacement.get(MatcherContext.LOGGED_USER_SYMBOLIC)!=null) {
						integerArr[i] = (Integer)loggedUserReplacement.get(MatcherContext.LOGGED_USER_SYMBOLIC);							
					}
				}
			}
		}
		return integerArr;
	}
	
	/**
	 * Loads the datasource for the matcher
	 * used by select fields to get the possible values
	 * It will be called from both field expressions and upper selects 
	 * The value can be a list for simple select or a map of lists for composite selects or a tree
	 * @param matcherValue
	 * @param matcherDatasourceContext the data source may be project dependent. 
	 * @param parameterCode for composite selects
	 * @return the datasource (list or tree)
	 */	
	public Object getMatcherDataSource(IMatcherValue matcherValue, MatcherDatasourceContext matcherDatasourceContext, Integer parameterCode) {
		List<TPersonBean> personList = new LinkedList<TPersonBean>();
		Integer[] projects = matcherDatasourceContext.getProjectIDs(); 
		if (projects!=null && projects.length>0) {
			Set<Integer> projectSet = GeneralUtils.createSetFromIntegerArr(projects);
			//get all configurations for this field
			List<TFieldConfigBean> fieldConfigList = FieldConfigBL.loadAllForField(matcherValue.getField());
			List<TGeneralSettingsBean> generalSettingsBeans = GeneralSettingsBL.loadByConfigListAndParametercode(
					GeneralUtils.createIntegerListFromBeanList(fieldConfigList),
					Integer.valueOf(CustomUserPicker.PARAMETERCODES.DATASOURCE_OPTION));
			Set<Integer> configsSet = new HashSet<Integer>();
			for (TGeneralSettingsBean generalSettingsBean : generalSettingsBeans) {
				Integer selectedDataSource = generalSettingsBean.getIntegerValue();
				if (selectedDataSource!=null) {
					configsSet.add(generalSettingsBean.getConfig());
				}
			}
			//issue type independent configs: only the projects are important for getting the persons by roles in projects						
			Map<Integer, List<Integer>> projectsByConfigIDMap = new HashMap<Integer, List<Integer>>();
			//issue type dependent configs
			if (!configsSet.isEmpty()) {							
				List<TFieldConfigBean> projectTypeSpecificFieldConfig = new LinkedList<TFieldConfigBean>();
				List<TFieldConfigBean> globalFieldConfig = new LinkedList<TFieldConfigBean>();
				/**
				 * gather project and project-issueType specific settings, remove the project specific ones from project set 
				 */
				for (TFieldConfigBean fieldConfigBean: fieldConfigList) {
					Integer configID = fieldConfigBean.getObjectID();
					Integer project = fieldConfigBean.getProject();
					Integer issueType = fieldConfigBean.getIssueType();
					Integer projectType = fieldConfigBean.getProjectType();
					if (project!=null) {
						//project specific custom list configuration: add independently whether it is also issue type specific or not
						addToList(projectsByConfigIDMap, configID, project);
						if (issueType==null) {
							//strictly project specific (not also issue type specific): 
							//do not search for further lists in more general contexts for this project, remove it from the set 	
							projectSet.remove(project);
						} /*else {
							//project and issueType specific: search further in more general contexts (for the other issue types) 						
							Map<Integer, Integer> issueTypeToConfig = projectIDToIssueTypeToConfigIDs.get(project);
							if (issueTypeToConfig==null) {
								issueTypeToConfig = new HashMap<Integer, Integer>();
								projectIDToIssueTypeToConfigIDs.put(project, issueTypeToConfig);
							}
							issueTypeToConfig.put(issueType, configID);
						}*/
					} else {
						if (projectType!=null) {
							//project type specific config
							projectTypeSpecificFieldConfig.add(fieldConfigBean);
						} else {
							//global or global issue type specific
							globalFieldConfig.add(fieldConfigBean);
						}
					}
				}
				//not all selected projects have strictly project specific configuration: search further in more general contexts 
				/**
				 * gather projectType and projectType-issueType specific settings, remove the projectType specific
				 * ones from project set (for each project of that project type) 
				 */
				if (!projectSet.isEmpty()) {
					//gather the project types for the remaining projects
					List<TProjectBean> projectsWithoutExplicitUserPickerSettings =
							ProjectBL.loadByProjectIDs(GeneralUtils.createListFromSet(projectSet));
					//load the projectIDs list for projectTypeID map
					Set<Integer> projectTypesSet = new HashSet<Integer>();
					Map<Integer, List<Integer>> projectsForProjectTypes = new HashMap<Integer, List<Integer>>();
					for (TProjectBean projectBean : projectsWithoutExplicitUserPickerSettings) {
						Integer projectID = projectBean.getObjectID();
						Integer projectTypeID = projectBean.getProjectType();
						List<Integer> projetctList =  projectsForProjectTypes.get(projectTypeID);
						if (projetctList==null) {
							projetctList = new ArrayList<Integer>();
							projectTypesSet.add(projectTypeID);
							projectsForProjectTypes.put(projectTypeID, projetctList);
						}
						projetctList.add(projectID);
					}
					//project types (for projects without explicit configurations)
					for (TFieldConfigBean fieldConfigBean : projectTypeSpecificFieldConfig) {
						Integer configID = fieldConfigBean.getObjectID();
						Integer projectType = fieldConfigBean.getProjectType();
						Integer issueType = fieldConfigBean.getIssueType();
						List<Integer> projectsForProjectType = projectsForProjectTypes.get(projectType);
						if (projectsForProjectType!=null) {
							//for each project from the projectType
							for (Integer projectID : projectsForProjectType) {
								if (issueType==null) {
									//strictly project type specific (not also issue type specific)
									addToList(projectsByConfigIDMap, configID, projectID);
									//do not search for further lists in more general contexts for this project type (projects of this projectType)
									projectSet.remove(projectID);
								} /*else {
									//projectType and issueType specific: search further in more general contexts (for the other issue types) 						
									Map<Integer, Integer> issueTypeToConfig = projectIDToIssueTypeToConfigIDs.get(projectID);
									if (issueTypeToConfig==null) {
										issueTypeToConfig = new HashMap<Integer, Integer>();
										projectIDToIssueTypeToConfigIDs.put(projectID, issueTypeToConfig);
									}
									issueTypeToConfig.put(issueType, configID);
								}*/
							
							}
						}
					}
					/**
					 * gather global and issueType specific settings
					 */
					if (!projectSet.isEmpty()) {
						for (TFieldConfigBean fieldConfigBean : globalFieldConfig) {
							Integer configID = fieldConfigBean.getObjectID();
							//Integer issueType = fieldConfigBean.getIssueType();
							//if (issueType==null) {
								for (Integer projectID : projectSet) {
									addToList(projectsByConfigIDMap, configID, projectID);
								}
							/*} else {
								for (Integer projectID : projectSet) {
									Map<Integer, Integer> issueTypeToConfig = projectIDToIssueTypeToConfigIDs.get(projectID);
									if (issueTypeToConfig==null) {
										issueTypeToConfig = new HashMap<Integer, Integer>();
										projectIDToIssueTypeToConfigIDs.put(projectID, issueTypeToConfig);
									}
									issueTypeToConfig.put(issueType, configID);
								}
							}*/
						}	
					}
				}
			}
			//get all persons from issueType independent configs
			Set<Integer> personsSet = new HashSet<Integer>();
			for (Iterator<Integer> iterator = projectsByConfigIDMap.keySet().iterator(); iterator.hasNext();) {
				Integer configID = iterator.next();				
				List<Integer> projectIDs = projectsByConfigIDMap.get(configID);
				personsSet.addAll(GeneralUtils.createIntegerListFromBeanList(loadDataSourceForProject(configID, projectIDs))); 
			}
			//get all persons from issueType dependent configs
			/*for (Iterator<Integer> iterator = projectIDToIssueTypeToConfigIDs.keySet().iterator(); iterator.hasNext();) {
				Integer projectID = iterator.next();
				Map<Integer, Integer> issueTypeToConfig = projectIDToIssueTypeToConfigIDs.get(projectID);
				for (Iterator<Integer> itrIssueType = issueTypeToConfig.keySet().iterator(); iterator.hasNext();) {
					Integer issueType = itrIssueType.next();
					Integer configID = issueTypeToConfig.get(issueType);
					personsSet.addAll(GeneralUtils.createIntegerListFromBeanList(loadDataSourceForProject(configID, projectID)));
				}
			}*/
			if (matcherValue!=null && matcherValue.getField()!=null) {
				//already assigned picked users
				personsSet.addAll(GeneralUtils.createIntegerListFromBeanList(
						PersonBL.loadUsedUserPickersByProjects(matcherValue.getField(), projects)));
			}
			//get the persons and also those with roles through group
			personList = PersonBL.getPersonsByCategory(personsSet, PERSON_CATEGORY.ALL_PERSONS, true, true, null);
		}
		Locale locale = matcherDatasourceContext.getLocale();
		TPersonBean loggedInSymbolicUser = (TPersonBean)getLabelBean(MatcherContext.LOGGED_USER_SYMBOLIC, locale);
		personList.add(0, loggedInSymbolicUser);
		if (matcherDatasourceContext.isWithParameter()) {
			personList.add((TPersonBean)getLabelBean(MatcherContext.PARAMETER, locale));
		}		
		if (matcherDatasourceContext.isInitValueIfNull() && matcherValue!=null) {
			Object value = matcherValue.getValue();
			if (value==null && personList!=null && !personList.isEmpty()) {
				matcherValue.setValue(new Integer[] {personList.get(0).getObjectID()});
			}
		}
		return personList;
	}
	
	private static void addToList(Map<Integer, List<Integer>> map, Integer configID, Integer projectID) {
		List<Integer> list = map.get(configID);
		if (list==null) {
			list = new LinkedList<Integer>();
			map.put(configID, list);
		}
		list.add(projectID);
	}
		
	
	private static List<TPersonBean> loadDataSourceForProject(Integer configID, List<Integer> projectIDs) {
		List<TPersonBean> dataSource = new LinkedList<TPersonBean>();
		//get the datasource options
		TGeneralSettingsBean dataSourceSelectionBean = GeneralSettingsBL.loadSingleByConfigAndParameter(configID, 
				Integer.valueOf(CustomUserPicker.PARAMETERCODES.DATASOURCE_OPTION));
		if (dataSourceSelectionBean==null) {
			LOGGER.warn("The datasource selection bean is missing");
			return dataSource;
		}
		Integer selectedDataSource = dataSourceSelectionBean.getIntegerValue();
		if (selectedDataSource==null) {
			LOGGER.warn("The datasource selection value is missing");
			return dataSource;
		}
		
		//get the users by role
		if (selectedDataSource.intValue()==CustomUserPicker.DATASOURCE_OPTIONS.ROLE) {
			List<TGeneralSettingsBean> roleSelectionBeans = GeneralSettingsBL.loadListByConfigAndParameter(configID, 
					Integer.valueOf(CustomUserPicker.PARAMETERCODES.ROLE_OPTION));
			if (roleSelectionBeans==null || roleSelectionBeans.isEmpty()) {
				LOGGER.warn("No role selected");
			} else {
				List<Integer> roleIDs = new LinkedList<Integer>();
				Iterator<TGeneralSettingsBean> iterator = roleSelectionBeans.iterator();
				while (iterator.hasNext()) {
					TGeneralSettingsBean roleGeneralSettingsBean = iterator.next();
					roleIDs.add(roleGeneralSettingsBean.getIntegerValue());
					dataSource = PersonBL.loadByProjectAndRoles(projectIDs, roleIDs);
				}
			}
		} else {
			//get the users by department
			if (selectedDataSource.intValue()==CustomUserPicker.DATASOURCE_OPTIONS.DEPARTMENT) {
				List<TGeneralSettingsBean> departmentSelectionBeans = GeneralSettingsBL.loadListByConfigAndParameter(configID, 
						Integer.valueOf(CustomUserPicker.PARAMETERCODES.DEPARTMENT_OPTION));
				if (departmentSelectionBeans==null || departmentSelectionBeans.isEmpty()) {
					LOGGER.warn("No department selected");
				} else {
					List<Integer> departmentIDs = new LinkedList<Integer>();
					Iterator<TGeneralSettingsBean> iterator = departmentSelectionBeans.iterator();
					while (iterator.hasNext()) {
						TGeneralSettingsBean departmentGeneralSettingsBean = iterator.next();
						departmentIDs.add(departmentGeneralSettingsBean.getIntegerValue());
						dataSource = PersonBL.loadByDepartments(departmentIDs, null);
					}
				}
			}
		}
		return dataSource;
	}
	
	/**
	 * Loads the IBulkSetter object for configuring the bulk operation
	 * @param fieldID
	 */	
	public IBulkSetter getBulkSetterDT(Integer fieldID) {		
		return new UserPickerBulkSetter(fieldID);
	}
	
	/**
	 * Loads the datasource for the mass operation
	 * used mainly by select fields to get 
	 * the all possible options for a field (system or custom select) 
	 * It also sets a value if not yet selected
	 * The value can be a List for simple select or a Map of lists for composite selects  
	 * @param massOperationContext
	 * @param massOperationValue
	 * @param parameterCode
	 * @param personBean
	 * @param locale
	 * @return
	 */
	@Override
	public void loadBulkOperationDataSource(MassOperationContext massOperationContext,
			MassOperationValue massOperationValue,
			Integer parameterCode, TPersonBean personBean, Locale locale) {
		List<TPersonBean> personsList = PersonBL.loadActivePersons();
		massOperationValue.setPossibleValues(personsList);
		massOperationValue.setValue(getBulkSelectValue(massOperationContext,
				massOperationValue.getFieldID(), null, 
				(Integer[])massOperationValue.getValue(), 
				(List<IBeanID>)massOperationValue.getPossibleValues()));
	}
	
	/**
	 * Based on field settings the field could be single or multiple select.
	 * This method checks whether is multiple select or not.
	 * @return
	 */
	private boolean isMultipleSelectPicker(Integer fieldID) {
		boolean isMultipleSelect = false;		
		if(fieldID != null) {
			TFieldConfigBean fieldConfigBean = FieldRuntimeBL.getDefaultFieldConfig(fieldID);
			if (fieldConfigBean!=null) {
				List<TGeneralSettingsBean> generalSettingsBeans = GeneralSettingsBL.loadByConfig(fieldConfigBean.getObjectID());
				for (TGeneralSettingsBean generalSettingsBean : generalSettingsBeans) {
					if (generalSettingsBean.getParameterCode()!=null) {
						if(generalSettingsBean.getParameterCode().intValue() == PARAMETERCODES.IS_MULTIPLE_SELECT) {
							isMultipleSelect = BooleanFields.fromStringToBoolean(generalSettingsBean.getCharacterValue());
						}
					}
				}
			}
		}
		return isMultipleSelect;
	}
	
	/**
	 * Gets the IFieldChangeConfig object for configuring the field change operation
	 * @param fieldID
	 * @return
	 */
	public IActivityConfig getFieldChangeConfig(Integer fieldID) {
		if(isMultipleSelectPicker(fieldID)) {
			return new MultipleListFieldChangeConfig(fieldID, false, true);			
		}
		return new SingleListFieldChangeConfig(fieldID, false, true);
	}
	
	/**
	 * Gets the IActivityExecute object for applying the field change operation
	 * @param fieldID
	 * @return
	 */
	public IActivityExecute getFieldChangeApply(Integer fieldID) {
		return new SelectFieldChangeApply(fieldID, true);
	}
	
	/**
	 * Loads the datasource and value for configuring the field change
	 * @param workflowContext
	 * @param fieldChangeValue
	 * @param parameterCode
	 * @param personBean
	 * @param locale
	 */
	public void loadFieldChangeDatasourceAndValue(WorkflowContext workflowContext,
			FieldChangeValue fieldChangeValue, 
			Integer parameterCode, TPersonBean personBean, Locale locale) {
		
		TFieldConfigBean configItem = (TFieldConfigBean)FieldConfigItemFacade.getInstance().getValidConfigDirect(
				workflowContext.getItemTypeID(), workflowContext.getProjectTypeID(), workflowContext.getProjectID(), fieldChangeValue.getFieldID());
		if (configItem==null) {
			configItem = (TFieldConfigBean)FieldConfigItemFacade.getInstance().getValidConfigFallback(
					workflowContext.getItemTypeID(), workflowContext.getProjectTypeID(), workflowContext.getProjectID(), fieldChangeValue.getFieldID());
		}
		List<ILabelBean> dataSource = null;
		Object value = fieldChangeValue.getValue();
		Integer[] currentOptions = null;
		if (value!=null) {
			currentOptions = (Integer[])value;
		}
		if (configItem!=null) {
			dataSource = loadList(configItem.getObjectID(), workflowContext.getProjectID(), workflowContext.getItemTypeID(), personBean.getObjectID(), currentOptions);
		}
		fieldChangeValue.setPossibleValues(dataSource);
		fieldChangeValue.setValue(getBulkSelectValue(null,
				fieldChangeValue.getFieldID(), null, 
				(Integer[])fieldChangeValue.getValue(), 
				(List<IBeanID>)fieldChangeValue.getPossibleValues()));
	}
	
	/**
	 * Get the ILabelBean by primary key 
	 * @param personID
	 * @return
	 */
	@Override
	public ILabelBean getLabelBean(Integer optionID, Locale locale) {
		if (optionID!=null) {
			if ((optionID.equals(MatcherContext.LOGGED_USER_SYMBOLIC) || optionID.equals(MatcherContext.PARAMETER))) {
				TPersonBean personBean = new TPersonBean();
				String localizedName;
				if (optionID.equals(MatcherContext.LOGGED_USER_SYMBOLIC)) {
					localizedName = MatcherContext.getLocalizedLoggedInUser(locale);
				} else {
					localizedName = MatcherContext.getLocalizedParameter(locale);
				}
				personBean.setLastName(localizedName);
				personBean.setObjectID(optionID);
				return personBean;
			}
			return PersonBL.loadByPrimaryKey(optionID);
		}
		return null;
	}

	/**
	 * Returns the lookup entity type related to the fieldType
	 * @return
	 */
	@Override
	public int getLookupEntityType() {
		return LuceneUtil.LOOKUPENTITYTYPES.PERSONNAME;
	}
	
	/**
	 * Whether this field is a UserPicker
	 * Special care should be taken by user pickers because of the notification (automail)
	 * A person may receive an automail just because he/she is selected in a userPicker 
	 * @return
	 */
	@Override
	public boolean isUserPicker() {
		return true;
	}
	
	/**
	 * Creates a new empty serializableLabelBean
	 * @return
	 */
	public ISerializableLabelBean getNewSerializableLabelBean() {
		return new TPersonBean();
	}
	
	/**
	 * Gets the ID by the label
	 * @param fieldID
	 * @param projectID
	 * @param issueTypeID
	 * @param locale
	 * @param label
	 * @param lookupBeansMap
	 * @param componentPartsMap
	 * @return
	 */
	public Integer getLookupIDByLabel(Integer fieldID,
			Integer projectID, Integer issueTypeID, 
			Locale locale, String label, 
			Map<String, ILabelBean> lookupBeansMap, Map<Integer, Integer> componentPartsMap) {
		if (label!=null) {
			/*if (label.endsWith(TPersonBean.SYS_ADM)) {
				label = label.replace(TPersonBean.SYS_ADM, "");
			}*/
			if (label.endsWith(TPersonBean.DEACTIVATED)) {
				label = label.replace(TPersonBean.DEACTIVATED, "");
			}
		}
		Integer objectID = NameMappingBL.getExactOrSimilarMatch(label, lookupBeansMap);
		if (objectID!=null) {
			return objectID;
		} else {
			//try to match the login name
			if (lookupBeansMap!=null) {
				Map<String, ILabelBean> loginToLabelBeanMap = new HashMap<String, ILabelBean>();
				for (Iterator<ILabelBean> iterator = lookupBeansMap.values().iterator(); iterator.hasNext();) {
					TPersonBean labelBean = (TPersonBean)iterator.next();
					String loginName = labelBean.getLoginName();
					if (loginName!=null) {
						if (loginName.equals(label)) {
							return labelBean.getObjectID();
						} else {
							loginToLabelBeanMap.put(loginName, labelBean);
						}
					}
				}
				String similarName = NameMappingBL.getSimilarName(label, loginToLabelBeanMap.keySet());
				if (similarName!=null) {
					ILabelBean labelBean  = loginToLabelBeanMap.get(similarName);
					if (labelBean!=null) {
						return labelBean.getObjectID();
					}
				}
			}
		}
		TPersonBean personBean = PersonBL.loadByLabel(label);
		if (personBean!=null) {
			return personBean.getObjectID();
		}
		return null;
	}
	
	/**
	 * Whether the lookupID found by label is allowed in 
	 * the context of serializableBeanAllowedContext
	 * In excel the lookup entries are not limited by the user interface controls
	 * This method should return false if the lookupID
	 * is not allowed (for ex. a person without manager role was set as manager) 
	 * @param objectID
	 * @param serializableBeanAllowedContext
	 * @return
	 */
	public boolean lookupBeanAllowed(Integer objectID, 
			SerializableBeanAllowedContext serializableBeanAllowedContext) {
		return true;
	}
	
	/**
	 * Gets the datasource for all possible list entries 
	 * @param fieldID
	 * @return
	 */
	public List<ILabelBean> getDataSource(Integer fieldID) {
		return (List)PersonBL.loadPersonsAndGroups();
	}
}
