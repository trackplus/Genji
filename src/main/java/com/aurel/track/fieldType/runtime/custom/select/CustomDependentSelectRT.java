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

package com.aurel.track.fieldType.runtime.custom.select;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.category.filter.tree.design.FieldExpressionSimpleTO;
import com.aurel.track.admin.customize.lists.ListBL;
import com.aurel.track.admin.customize.lists.customOption.OptionBL;
import com.aurel.track.admin.customize.treeConfig.field.OptionSettingsBL;
import com.aurel.track.beans.IBeanID;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TListBean;
import com.aurel.track.beans.TOptionBean;
import com.aurel.track.beans.TOptionSettingsBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.fieldChange.FieldChangeValue;
import com.aurel.track.fieldType.runtime.base.SelectContext;
import com.aurel.track.fieldType.runtime.bl.CustomSelectUtil;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.runtime.matchers.MatchRelations;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherValue;
import com.aurel.track.fieldType.runtime.matchers.design.MatcherDatasourceContext;
import com.aurel.track.fieldType.runtime.matchers.run.CompositeSelectMatcherRT;
import com.aurel.track.item.massOperation.MassOperationContext;
import com.aurel.track.item.massOperation.MassOperationValue;
import com.aurel.track.item.workflow.execute.WorkflowContext;
import com.aurel.track.resources.LocalizeUtil;

/**
 * A select component which depends on other select component.
 * Used as child select in cascading select fields
 * @author Tamas Ruff
 *
 */
class CustomDependentSelectRT extends CustomSelectSimpleRT /*implements IInternalDependent*/ {
	private static final Logger LOGGER = LogManager.getLogger(CustomDependentSelectRT.class);
	/*
	 * the parent of this select is the parentID'th component of the custom composite field
	 */
	private Integer parentID;

	/*
	 * this select has as datasource the childNumber'th child list of the parent list related to parentID  
	 */
	private Integer childNumber;
	
	/**
	 * the entire hierarchy till this component
	 */
	protected List<Integer> childNumbersHierarchy = new ArrayList<Integer>();
	
	/**
	 * Creates a new dependent select
	 * @param parentID
	 * @param childID
	 * @param childNumber
	 */
	public CustomDependentSelectRT(Integer parentID, Integer childNumber, List<Integer> childNumbersHierarchy) {
		super();
		this.parentID = parentID;
		this.childNumber = childNumber;
		this.childNumbersHierarchy = childNumbersHierarchy;
	}

	/**
	 * Returns the internal dependences of this field
	 */
	/*@Override
	public Map getInternalDependences(Integer fieldID) {
		Map internalDependencesMap = new HashMap();
		List list=new ArrayList();
		list.add(parentID);
		//the parameterCode part of the key is null because now it isn't known at this time 
		//it will be set to the corresponding parameterCode by creating the internalDependencesMap 
		//for the entire composite see CustomCompositeFieldTypeRT.getInternalDependences()
		internalDependencesMap.put(MergeUtil.mergeKey(fieldID, null), list);
		return internalDependencesMap;
	}*/
	
	/**
	 * Loads the settings for a specific field like default value(s),
	 * datasources (for lists) etc. by editing an existing issue
	 * It might differ from loadCreateSettings because it could be need 
	 * to add existing entries in the list (also when right already revoked)
	 * @param selectContext
	 * @return
	 */
	@Override
	public List loadEditDataSource(SelectContext selectContext) {
		TWorkItemBean workItemBean = selectContext.getWorkItemBean();
		//get the parent options 		
		Integer[] parentOptionIDs = CustomSelectUtil.getSelectedOptions(
				workItemBean.getAttribute(selectContext.getFieldID(), parentID));
		if (parentOptionIDs==null || parentOptionIDs.length==0) {
			LOGGER.debug("No parent option seleced by loading the edit data source for child");
			return new LinkedList();
		}	
		//get the current child options
		Integer[] currentOptions = CustomSelectUtil.getSelectedOptions(
				workItemBean.getAttribute(selectContext.getFieldID(), selectContext.getParameterCode()));
		if (currentOptions==null || currentOptions.length==0) {
			LOGGER.debug("No child option seleced by loading the edit data source");
		}
		TOptionBean optionBean = OptionBL.loadByPrimaryKey(parentOptionIDs[0]);
		if (optionBean!=null) {
			Integer parentListID = optionBean.getList();
			//get the childNumber'th child list of the parentListID
			TListBean childListBean = ListBL.getChildList(parentListID, childNumber);
			//TODO later filter first by configOptionRoleDAO.loadEditOptionsByConfigForRoles(configID, person, project, currentOptions);
			if (childListBean!=null) {
				return LocalizeUtil.localizeDropDownList(
						optionDAO.loadEditDataSourceByListAndParents(
								childListBean.getObjectID(), parentOptionIDs, currentOptions), 
						selectContext.getLocale());
			}
		}
		return new LinkedList();
	}
	
	/**
	 * Loads the settings for a specific field like default value(s),
	 * datasources (for lists) etc.
	 * @see loadEditSettings
	 * @param selectContext
	 * @return
	 */
	@Override
	public List loadCreateDataSource(SelectContext selectContext) {
		TWorkItemBean workItemBean = selectContext.getWorkItemBean();
		//get the parent options 
		Integer[] parentOptionIDs = CustomSelectUtil.getSelectedOptions(
				workItemBean.getAttribute(selectContext.getFieldID(), parentID));
		if (parentOptionIDs==null || parentOptionIDs.length==0) {
			LOGGER.debug("No parent option seleced by loading the create data source for child");
			return new LinkedList();
		}
		TOptionBean optionBean = OptionBL.loadByPrimaryKey(parentOptionIDs[0]);
		Integer parentListID = optionBean.getList();
		//get the childNumber'th child list of the parentListID
		TListBean childListBean = ListBL.getChildList(parentListID, childNumber);
		if (childListBean!=null) {
			Integer[] childOptionIDs = CustomSelectUtil.getSelectedOptions(
					workItemBean.getAttribute(selectContext.getFieldID(), selectContext.getParameterCode()));
			Integer childOption = null; 
			if (childOptionIDs!=null && childOptionIDs.length>0) {
				childOption = childOptionIDs[0];
			}
			//TODO later filter first by configOptionRoleDAO.loadCreateOptionsByConfigForRoles(configID, person, project);
			List<ILabelBean> options = LocalizeUtil.localizeDropDownList(
					optionDAO.loadCreateDataSourceByListAndParents(
							childListBean.getObjectID(), parentOptionIDs),
					selectContext.getLocale());
			if (options!=null && !options.isEmpty()) {
				boolean found = false;
				for (ILabelBean labelBean : options) {
					if (childOption!=null && childOption.equals(labelBean.getObjectID())) {
						found = true;
						break;
					}
				} 
				if (!found&&selectContext.getFieldConfigBean().isRequiredString()) {
					workItemBean.setAttribute(selectContext.getFieldID(), selectContext.getParameterCode(), new Integer[] {options.get(0).getObjectID()});
				}
			}
			return options;
		}
		return new LinkedList();
	}

	/**
	 *  Sets the custom default values for a field on the workItem from the config settings
	 * @param configID
	 * @param workItemBean
	 * @return
	 */
	@Override
	public void setDefaultAttribute(Integer fieldID, Integer parameterCode, 
			Integer validConfig, Map<String, Object> fieldSettings, TWorkItemBean workItemBean) {
		//do not take into account the parameterCode by creating the mergedKey becuase 
		//it in mergedKey it would not mean the index of the part inside the composite but the parameterCode
		//of the optionSettingsBean element which is probably null because we do not have fieldTypes 
		//which need more than one optionSettingsBean to be configured		 
		//get the parent options 
		Integer[] parentOptionIDs = CustomSelectUtil.getSelectedOptions(
				workItemBean.getAttribute(fieldID, parentID));
		if (parentOptionIDs==null || parentOptionIDs.length==0) {
			LOGGER.debug("No parent option seleced by loading the default value for child");
			return;
		}
		TOptionBean optionBean = OptionBL.loadByPrimaryKey(parentOptionIDs[0]);
		if(optionBean!=null){
			Integer parentListID = optionBean.getList();
			TListBean childListBean = ListBL.getChildList(parentListID, childNumber);
			if (childListBean!=null) {
				//TODO later filter first by configOptionRoleDAO.loadCreateOptionsByConfigForRoles(configID, person, project);
				Integer[] defaultValue = optionDAO.loadDefaultIDsForListAndParents(childListBean.getObjectID(), parentOptionIDs);
				/*if (!BooleanFields.TRUE_VALUE.equals(optionSettingsBean.getMultiple())) {
					if (defaultValue!=null && defaultValue.length>0) {
						//simple select
						workItemBean.setAttribute(fieldID, parameterCode, defaultValue[0]);
					}
				}
				else
				{*/	//multiple select
					workItemBean.setAttribute(fieldID, parameterCode, defaultValue);
				//}
			}
		}
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
		Map<Integer, Integer[]> actualValuesMap = null;
		Locale locale = matcherDatasourceContext.getLocale();
		try {
			actualValuesMap = (Map<Integer, Integer[]>)matcherValue.getValue();
		} catch (Exception e) {
			LOGGER.warn("ValueModified: converting the qnode values to map failed with " + e.getMessage(), e);
		}
		if (actualValuesMap == null || actualValuesMap.isEmpty()) {
			return new ArrayList<IBeanID>();
		}
		Integer parentIntValue = null;
		try {
			Integer[] parentIntArr = actualValuesMap.get(parentID);
			if (parentIntArr!=null && parentIntArr.length>0) {
				parentIntValue = parentIntArr[0];
			}
		} catch(Exception e) {
			LOGGER.warn("Converting the parent value to Integer failed with " + e.getMessage(), e);
		}
		Integer childIntValue = null;
		try {
			Integer[] childIntArr = actualValuesMap.get(parameterCode);
			if (childIntArr!=null && childIntArr.length>0) {
				childIntValue = childIntArr[0];
			}
		} catch (Exception e) {
			LOGGER.warn("Converting the child value to Integer failed with " + e.getMessage(), e);
		}
		List<ILabelBean> possibleValues = null;
		if (parentIntValue!=null) {
			boolean isPartialMatch = false;
			if (parentIntValue.equals(CompositeSelectMatcherRT.ANY_FROM_LEVEL) || 
				parentIntValue.equals(CompositeSelectMatcherRT.NONE_FROM_LEVEL)) {
				isPartialMatch = true;
			}
			if (isPartialMatch) {
				actualValuesMap.put(parameterCode, new Integer[] {parentIntValue});
				possibleValues = new LinkedList<ILabelBean>();
				ILabelBean partialMatch = getPartialMatchEntry(matcherValue, locale);
				if (partialMatch!=null) {
					possibleValues.add(0, partialMatch);
				}
				//then previous level is partial match, the child list should contain only a partial match
			} else {
				TOptionBean parentOptionBean = OptionBL.loadByPrimaryKey(parentIntValue);
				if (parentOptionBean!=null) {
					Integer parentListID = parentOptionBean.getList();
					TListBean childListBean = ListBL.getChildList(parentListID, childNumber);
					if (childListBean!=null) { 
						possibleValues = LocalizeUtil.localizeDropDownList(
								optionDAO.loadActiveByListAndParentOrderedBySortorder(childListBean.getObjectID(), parentIntValue), locale);
						ILabelBean partialMatch = getPartialMatchEntry(matcherValue, locale);
						if (partialMatch!=null) {
							if (possibleValues==null) {
								possibleValues = new LinkedList<ILabelBean>();
							}
							possibleValues.add(0, partialMatch);
						}
						if (possibleValues!=null && !possibleValues.isEmpty()) {
							if (childIntValue==null) {
								//preselect to the first if not yet selected
								actualValuesMap.put(parameterCode, new Integer[] {possibleValues.get(0).getObjectID()});
							} else {
								boolean found = false;
								Iterator<ILabelBean> iterator = possibleValues.iterator();
								while (iterator.hasNext()) {
									ILabelBean beanID = iterator.next();
									if (childIntValue.equals(beanID.getObjectID())) {
										found = true;
										break;
									}
								}
								if (!found) {
									actualValuesMap.put(parameterCode, new Integer[] {possibleValues.get(0).getObjectID()});
								}
							}
						} else {
							actualValuesMap.put(parameterCode, null);
						}	
					}
				}
			}
		}
		if (possibleValues==null) {
			return new ArrayList<IBeanID>();
		} else {
			return possibleValues;
		}
	}
	
	private static ILabelBean getPartialMatchEntry(IMatcherValue matcherValue, Locale locale) {
		try {
			FieldExpressionSimpleTO fieldExpressionSimpleTO = (FieldExpressionSimpleTO)matcherValue;
			Integer selectedMatcher = fieldExpressionSimpleTO.getSelectedMatcher();
			if (selectedMatcher!=null) {
				if (selectedMatcher.intValue()==MatchRelations.PARTIAL_MATCH) {
					TOptionBean optionBean = new TOptionBean();
					optionBean.setObjectID(CompositeSelectMatcherRT.ANY_FROM_LEVEL);
					optionBean.setLabel(LocalizeUtil.getLocalizedTextFromApplicationResources(
							"admin.customize.queryFilter.opt.partialMatch", locale));						
					return optionBean;
				} else {
					if (selectedMatcher.intValue()==MatchRelations.PARTIAL_NOTMATCH) {
						TOptionBean optionBean = new TOptionBean();
						optionBean.setObjectID(CompositeSelectMatcherRT.NONE_FROM_LEVEL);
						optionBean.setLabel(LocalizeUtil.getLocalizedTextFromApplicationResources(
							"admin.customize.queryFilter.opt.partialNotmatch", locale));
						return optionBean;
					}
				}
			}
		} catch(Exception e){
			LOGGER.warn("Getting the partial match entry failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		return null;
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
		Map<Integer, SortedMap<Integer, Integer[]>> actualValuesMap = null;
		try {
			actualValuesMap = (Map<Integer, SortedMap<Integer, Integer[]>>)massOperationValue.getValue();
		} catch (Exception e) {
			LOGGER.warn("ValueModified: converting the massOperationExpression values to map failed with " + e.getMessage(), e);
		}
		if (actualValuesMap == null || actualValuesMap.isEmpty()) {
			return;
		}
		Integer fieldID = massOperationValue.getFieldID();
		Map<Integer, Map<Integer, String>> fieldIDToListIDToLabels = massOperationContext.getFieldIDToListIDToLabels();
		if (fieldIDToListIDToLabels!=null) {
			Map<Integer, String> listIDToLabelMap = fieldIDToListIDToLabels.get(fieldID);
			if (listIDToLabelMap!=null) {
				for (Integer listID : listIDToLabelMap.keySet()) {
					SortedMap<Integer, Integer[]> actualValueMap = actualValuesMap.get(listID);
					Map<Integer, SortedMap<Integer, List<IBeanID>>> dataSourceMap = (Map<Integer, SortedMap<Integer, List<IBeanID>>>)
							massOperationValue.getPossibleValues();
					SortedMap<Integer, List<IBeanID>> compositeDataSource = dataSourceMap.get(listID);
					Object actualParentValue=actualValueMap.get(parentID);
					Integer[] parentIntArr = null;
					Integer parentIntValue = null;
					try {
						parentIntArr = (Integer[])actualParentValue;
						if (parentIntArr!=null && parentIntArr.length>0) {
							parentIntValue = parentIntArr[0];
						}
					} catch(Exception e) {
						LOGGER.warn("Converting the parent value to Integer[] failed with " + e.getMessage(), e);
					}
					List<IBeanID> possiblePartValues = null;
					if (parentIntValue!=null) {			
						TOptionBean optionBean = OptionBL.loadByPrimaryKey(parentIntValue);
						Integer parentListID = optionBean.getList();
						//get the childNumber'th child list of the parentListID
						TListBean childListBean = ListBL.getChildList(parentListID, childNumber);
						if (childListBean!=null) {
							//TODO later filter first by configOptionRoleDAO.loadCreateOptionsByConfigForRoles(configID, person, project);
							possiblePartValues = LocalizeUtil.localizeDropDownList(
									optionDAO.loadCreateDataSourceByListAndParents(
											childListBean.getObjectID(), parentIntArr),
									locale);
						}
						actualValueMap.put(parameterCode, getBulkSelectValue(massOperationContext, 
								massOperationValue.getFieldID(), parameterCode, actualValueMap.get(parameterCode), possiblePartValues));					
					}
					if (possiblePartValues!=null) {
						compositeDataSource.put(parameterCode, possiblePartValues);
					}
				}
			}
		}
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
		SortedMap<Integer, Integer[]> actualValuesMap = null;
		try {
			actualValuesMap = (SortedMap<Integer, Integer[]>)fieldChangeValue.getValue();
		} catch (Exception e) {
			LOGGER.warn("ValueModified: converting the massOperationExpression values to map failed with " + e.getMessage(), e);
		}
		if (actualValuesMap == null || actualValuesMap.isEmpty()) {
			return;
		}
		Integer fieldID = fieldChangeValue.getFieldID();
		SortedMap<Integer, List<IBeanID>> compositeDataSource = (SortedMap<Integer, List<IBeanID>>)
				fieldChangeValue.getPossibleValues();
		Object actualParentValue=actualValuesMap.get(parentID);
		Integer[] parentIntArr = null;
		Integer parentIntValue = null;
		try {
			parentIntArr = (Integer[])actualParentValue;
			if (parentIntArr!=null && parentIntArr.length>0) {
				parentIntValue = parentIntArr[0];
			}
		} catch(Exception e) {
			LOGGER.warn("Converting the parent value to Integer[] failed with " + e.getMessage(), e);
		}
		List<IBeanID> possiblePartValues = null;
		if (parentIntValue!=null) {			
			TOptionBean optionBean = OptionBL.loadByPrimaryKey(parentIntValue);
			Integer parentListID = optionBean.getList();
			//get the childNumber'th child list of the parentListID
			TListBean childListBean = ListBL.getChildList(parentListID, childNumber);
			if (childListBean!=null) {
				//TODO later filter first by configOptionRoleDAO.loadCreateOptionsByConfigForRoles(configID, person, project);
				possiblePartValues = LocalizeUtil.localizeDropDownList(
						optionDAO.loadCreateDataSourceByListAndParents(
								childListBean.getObjectID(), parentIntArr),
						locale);
			}
			actualValuesMap.put(parameterCode, getBulkSelectValue(null, 
					fieldID, parameterCode, actualValuesMap.get(parameterCode), possiblePartValues));					
		}
		if (possiblePartValues!=null) {
			compositeDataSource.put(parameterCode, possiblePartValues);
		}		
	}
	
	/**
	 * Gets the custom option ID by the label
	 * @param fieldID	 
	 * @param projectID
	 * @param issueTypeID
	 * @param locale
	 * @param label
	 * @param lookupBeansMap
	 * @param componentPartsMap
	 * @return
	 */
	@Override
	public Integer getLookupIDByLabel(Integer fieldID,
			Integer projectID, Integer issueTypeID, 
			Locale locale, String label, 
			Map<String, ILabelBean> lookupBeansMap, Map<Integer, Integer> componentPartsMap) {
		TFieldConfigBean fieldConfigBean = FieldRuntimeBL.getValidConfig(fieldID, issueTypeID, projectID);
		if (fieldConfigBean==null) {
			LOGGER.warn("No field config found for field " + fieldID + 
					" and projectID " + projectID + " issueTypeID " + issueTypeID);
			return null;
		}
		TOptionSettingsBean optionSettingsBean = OptionSettingsBL.loadByConfigAndParameter(fieldConfigBean.getObjectID(), null);
		if (optionSettingsBean == null) {
			LOGGER.warn("No option settings found for field " + fieldID + 
					" and projectID " + projectID + " issueTypeID " + issueTypeID);
			return null;
		}
		Integer mainListID = optionSettingsBean.getList();
		TOptionBean optionBean = null;
		if (childNumbersHierarchy==null || childNumbersHierarchy.isEmpty()) {
			List<TOptionBean> optionBeans = OptionBL.loadByLabel(mainListID, null, label);
			if (optionBeans!=null && !optionBeans.isEmpty()) {
				optionBean = optionBeans.get(0);
			}
		} else {
			Integer listID = mainListID;
			Iterator<Integer> iterator = childNumbersHierarchy.iterator();
			TListBean childListBean = null;
			while (iterator.hasNext()) {
				Integer childNumber = iterator.next();
				childListBean = ListBL.getChildList(listID, childNumber);
				if (childListBean!=null) {
					listID = childListBean.getObjectID();
				}
			}
			if (childListBean!=null && componentPartsMap!=null) {
				List<TOptionBean> optionBeans = optionDAO.loadByLabel(childListBean.getObjectID(), componentPartsMap.get(parentID), label);
				if (optionBeans!=null && !optionBeans.isEmpty()) {
					optionBean = optionBeans.get(0);
				}
			}
		}
		if (optionBean!=null) {
			return optionBean.getObjectID();
		}
		return null;
	}
}
