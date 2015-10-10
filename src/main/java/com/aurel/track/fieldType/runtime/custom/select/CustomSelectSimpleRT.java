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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.lists.customOption.OptionBL;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.customize.projectType.ProjectTypesBL;
import com.aurel.track.admin.customize.treeConfig.field.CustomListsConfigBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldConfigItemFacade;
import com.aurel.track.admin.customize.treeConfig.field.OptionSettingsBL;
import com.aurel.track.beans.IBeanID;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.ILocalizedLabelBean;
import com.aurel.track.beans.ISerializableLabelBean;
import com.aurel.track.beans.ISortedBean;
import com.aurel.track.beans.TAttributeValueBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TOptionBean;
import com.aurel.track.beans.TOptionSettingsBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.OptionDAO;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.fieldChange.FieldChangeValue;
import com.aurel.track.fieldType.runtime.base.LocalLookupContainer;
import com.aurel.track.fieldType.runtime.base.SelectContext;
import com.aurel.track.fieldType.runtime.base.SerializableBeanAllowedContext;
import com.aurel.track.fieldType.runtime.bl.CustomSelectUtil;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherValue;
import com.aurel.track.fieldType.runtime.matchers.design.MatcherDatasourceContext;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.item.massOperation.MassOperationContext;
import com.aurel.track.item.massOperation.MassOperationValue;
import com.aurel.track.item.workflow.execute.WorkflowContext;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;

/**
 * Class for simple selects but it can be used as base class for 
 * select parts of composite fields
 * @author Tamas Ruff
 *
 */
public class CustomSelectSimpleRT extends CustomSelectBaseRT {
	private static final Logger LOGGER = LogManager.getLogger(CustomSelectSimpleRT.class);
	protected static OptionDAO optionDAO = DAOFactory.getFactory().getOptionDAO();

	/**
	 * Sets the database field specific to the field type (CustomOptionID or SystemOptionID)
	 * @param tAttributeValueBean
	 * @param attribute
	 */
	public void setSpecificAttribute(TAttributeValueBean attributeValueBean, Integer attribute) {
		attributeValueBean.setCustomOptionID(attribute);
	}
	
	/**
	 * Whether the values are based on custom lists or system lists
	 * @return
	 */
	public boolean isCustomPicker() {
		return false;
	}
	
	/**
	 * Whether the list entries have dynamic icons
	 * @return
	 */
	public boolean hasDynamicIcons() {
		return true;
	}
	
	/**
	 * Gets the iconCls class for label bean if dynamicIcons is false
	 * @param labelBean
	 */
	public String getIconCls(ILabelBean labelBean) {
		return null;
	}
	
	/**
	 * Whether the datasource for the picker is tree or list
	 * @return
	 */
	public boolean isTree() {
		return false;
	}
	
	/**
	 * Which field from the record contains the valid value 
	 * @return
	 */
	public int getValueType() {
		return ValueType.CUSTOMOPTION;
	}

	/**
	 * The field key for getting the corresponding map from dropdown. 
	 * The CustomSelects are stored under their own fieldIDs   
	 * @return
	 */
	public Integer getDropDownMapFieldKey(Integer fieldID) {
		return fieldID;
	}

	/**
	 * Sets the custom default values for a field on the workItem from the config settings
	 * @param configID
	 * @param workItemBean
	 * @return
	 */
	@Override
	public void setDefaultAttribute(Integer fieldID, Integer parameterCode, 
			Integer validConfig, Map<String, Object> fieldSettings, TWorkItemBean workItemBean) {
		//do not take into account the parameterCode by creating the mergedKey because 
		//it in mergedKey it would not mean the index of the part inside the composite but the parameterCode
		//of the optionSettingsBean element which is probably null because we do not have fieldTypes 
		//which need more than one optionSettingsBean to be configured 
		String mergedKey = MergeUtil.mergeKey(fieldID, null);
		Object settingsObject = fieldSettings.get(mergedKey);
		if (settingsObject!=null) {
			TOptionSettingsBean optionSettingsBean = null;
			try {
				optionSettingsBean = (TOptionSettingsBean)settingsObject;
			} catch(Exception ex) {
				LOGGER.error("Wrong settings type for simple select " + ex.getMessage());
			}
			if (optionSettingsBean!=null) {
				Integer[] defaultValue = optionDAO.loadDefaultIDsForList(optionSettingsBean.getList());
					workItemBean.setAttribute(fieldID, parameterCode, defaultValue);
			}
		}
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
		Integer[] currentOptions = CustomSelectUtil.getSelectedOptions(
				workItemBean.getAttribute(selectContext.getFieldID(), selectContext.getParameterCode()));
		if (currentOptions==null || currentOptions.length==0) {
			LOGGER.debug("No current option seleced by loading the edit data source");
		}		 
		List<ILabelBean> dataSource = loadDataSource(selectContext);
		//We should be sure that the resulting list contains the currentOptions also
		//It could be that the list is changed by field configuration, but we have still the values from the old list
		//These options should be added manually in order to avoid changing these values involuntarily 
		//TODO add the current options in the correct sort order when it is the case

		if (currentOptions!=null && currentOptions.length>0) {
			if (dataSource==null) {
				dataSource = new LinkedList<ILabelBean>();
			}
			for (int i = 0; i < currentOptions.length; i++) {
				boolean found=false;
				Iterator<ILabelBean> iterator = dataSource.iterator();
				while (iterator.hasNext()) {
					TOptionBean optionBean = (TOptionBean)iterator.next();
					if (optionBean.getObjectID().equals(currentOptions[i])) {
						found = true;
						break;
					}
				}
				if (found==false && currentOptions[i]!=null) {
					TOptionBean optionBean = OptionBL.loadByPrimaryKey(currentOptions[i]);
					if (optionBean!=null) {
						dataSource.add(optionBean);
					}
				}
			}
		}		
		return LocalizeUtil.localizeDropDownList(dataSource, selectContext.getLocale());
	}
	
	/**
	 * Loads the settings for a specific field like default value(s),
	 * datasources (for lists) etc. 
	 * @param selectContext 
	 * @return
	 */
	public List loadCreateDataSource(SelectContext selectContext){
		List<ILabelBean> dataSource = loadDataSource(selectContext);
		return LocalizeUtil.localizeDropDownList(dataSource, selectContext.getLocale());
	}
	
	/**
	 * Load the datasource by list
	 * @param selectContext
	 * @return
	 */
	private List<ILabelBean> loadDataSource(SelectContext selectContext) {
		//Integer person = selectContext.getPerson();
		//Integer project = selectContext.getWorkItemBean().getProjectID();
		TFieldConfigBean fieldConfigBean = selectContext.getFieldConfigBean();
		Integer configID = fieldConfigBean.getObjectID();
		//get the restricted options
		/*List<ILabelBean> dataSource = null; //TODO call this once the configuration is implemented configOptionRoleDAO.loadOptionsByConfigForRoles(configID, person, project);
		//if not restricted load all options for the list
		if (dataSource == null) {
			//no role restrictions, load all options
			TOptionSettingsBean optionSettingsBean = OptionSettingsBL.loadByConfigAndParameter(configID, null);
			if (optionSettingsBean!=null) {
				Integer listID = optionSettingsBean.getList();
				dataSource = LocalizeUtil.localizeDropDownList(OptionBL.loadDataSourceByList(listID), selectContext.getLocale());
			}
		}*/
		return loadList(configID, selectContext.getLocale());
	}
	
	/**
	 * Loads the localized list entries
	 * @param configID
	 * @param locale
	 * @return
	 */
	private List<ILabelBean> loadList(Integer configID, Locale locale) {
		//get the restricted options
		List<ILabelBean> dataSource = null; //TODO call this once the configuration is implemented configOptionRoleDAO.loadOptionsByConfigForRoles(configID, person, project);
		//if not restricted load all options for the list
		if (dataSource == null) {
			//no role restrictions, load all options
			TOptionSettingsBean optionSettingsBean = OptionSettingsBL.loadByConfigAndParameter(configID, null);
			if (optionSettingsBean!=null) {
				Integer listID = optionSettingsBean.getList();
				dataSource = LocalizeUtil.localizeDropDownList(OptionBL.loadDataSourceByList(listID), locale);
			}
		}
		return dataSource;
	}
	
	/**
	 * Get the sort order related to the value
	 * By default the value is directly used as sortOrder
	 * the select fields has extra sortOrder columns 
	 * @param fieldID
	 * @param parameterCode
	 * @param value the value the sortorder is looked for
	 * @param workItemID
	 * @param localLookupContainer 
	 * @return
	 */
	public Comparable getSortOrderValue(Integer fieldID, Integer parameterCode, Object value, 
			Integer workItemID, LocalLookupContainer localLookupContainer) {
		Integer[] optionIDs = CustomSelectUtil.getSelectedOptions(value);
		if (optionIDs==null || optionIDs.length==0) {
			LOGGER.debug("No option seleced by getting the sort order value");
			return null;
		} else {
			ISortedBean sortedBean = null;
			Comparable minSortOrderValue = null; 
			//Comparable listID = null;
			//Map beansMap = dropDownContainer.getDataSourceMap(MergeUtil.mergeKey(getDropDownMapFieldKey(fieldID), parameterCode));
			Map<Integer, ILabelBean> beansMap = localLookupContainer.getCustomOptionsMap();
			if (beansMap!=null) {
				try {
					//select the option with the smallest sortorder
					for (int i = 0; i < optionIDs.length; i++) {
						sortedBean = (ISortedBean)beansMap.get(optionIDs[i]);
						if (sortedBean!=null) {
							if (sortedBean.getSortOrderValue()!=null) {
								Comparable currentSortOrder = sortedBean.getSortOrderValue();
								if (minSortOrderValue==null || currentSortOrder.compareTo(minSortOrderValue)<0) {
									minSortOrderValue = currentSortOrder;
									//add the list also because the sortOrder is local for the list
									//but we might have entries from more lists even for the same field (depending on contexts) 
									//listID = ((TOptionBean)sortedBean).getList();
								}
							} else {
								//no sortOrder set. If null sortorder is returned the workItem.getAttribute() is taken as sororder
								//but in this case it would be an Object[] which is not comparable (throws exception).
								//As best effort return the value itself as Integer
								minSortOrderValue = sortedBean.getObjectID();
							}
						}
					}
				} catch (Exception e) {
					LOGGER.warn("The field number " + fieldID + " and parametercode "+ parameterCode + 
							" doesn't implement the ISortedBean interface " + e.getMessage(), e);
				}
			}
			//SortedMap<Integer, Comparable> comparableMap = new TreeMap<Integer, Comparable>();
			//comparableMap.put(CustomSelectComparable.COMPARABLE_LIST, listID);
			//comparableMap.put(CustomSelectComparable.COMPARABLE_SORTORDER, minSortOrderValue);
			return minSortOrderValue;//new CustomSelectComparable(comparableMap);
		}
	}
	
	/**
	 * Get the value to be shown from the database
	 * @param value
	 * @param locale
	 * @return
	 */
	@Override
	public String getShowValue(Object value, Locale locale) {
		StringBuffer showValue = new StringBuffer();
		Integer[] optionIDs = CustomSelectUtil.getSelectedOptions(value);
		if (optionIDs==null || optionIDs.length==0) {
			LOGGER.debug("No option seleced by getting the show value from database");
			return showValue.toString();
		}
		List<TOptionBean> localizedLabelBeans = OptionBL.loadByKeys(optionIDs);
		if (localizedLabelBeans!=null) {
			Iterator<TOptionBean> iterator = localizedLabelBeans.iterator();
			while (iterator.hasNext()) {
				ILocalizedLabelBean localizedLabelBean = iterator.next();
				showValue.append(LocalizeUtil.localizeDropDownEntry(localizedLabelBean, locale));
				if (iterator.hasNext()) {
					showValue.append(OPTION_SPLITTER_STRING); 
				}
			}
		}
		return showValue.toString().trim();
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
		List<ILabelBean> optionList = new LinkedList<ILabelBean>();
		Integer fieldID = matcherValue.getField();
		Integer[] projectIDs = matcherDatasourceContext.getProjectIDs();
		Locale locale = matcherDatasourceContext.getLocale();
		if (projectIDs!=null && projectIDs.length>0 && matcherValue!=null) {
			List<Integer> fieldIDs = new LinkedList<Integer>();
			fieldIDs.add(fieldID);
			Map<Integer, Set<Integer>> projectIssueTypeContexts = new HashMap<Integer, Set<Integer>>();
			Set<Integer> issueTypeIDsSet;
			Integer[] selectedItemTypeIDs = matcherDatasourceContext.getItemTypeIDs();
			if (selectedItemTypeIDs==null || selectedItemTypeIDs.length==0) {
				Integer[] projectTypeIDs = ProjectTypesBL.getProjectTypeIDsForProjectIDs(projectIDs);
				List<TListTypeBean> issueTypeBeans;
				if (projectTypeIDs==null || projectTypeIDs.length==0) {
					issueTypeBeans = (List)IssueTypeBL.loadAllSelectable();
				} else {
					issueTypeBeans = (List)IssueTypeBL.loadAllowedByProjectTypes(projectTypeIDs);
				}
				issueTypeIDsSet = GeneralUtils.createIntegerSetFromBeanList(issueTypeBeans);
			} else {
				issueTypeIDsSet = GeneralUtils.createSetFromIntegerArr(selectedItemTypeIDs);
			}
			for (Integer projectID : projectIDs) {
				projectIssueTypeContexts.put(projectID, issueTypeIDsSet);
			}
			Map<Integer, Map<Integer, List<TFieldConfigBean>>> fieldIDToListIDToFieldConfigs = CustomListsConfigBL.getMostSpecificLists(fieldIDs, projectIssueTypeContexts);
			Map<Integer, Map<Integer, String>> fieldIDToListIDLabels = CustomListsConfigBL.getFieldIDsToListIDToLabelsMap(fieldIDToListIDToFieldConfigs, locale, false);
			Map<Integer, String> listIDToLabels = fieldIDToListIDLabels.get(fieldID);
			
			//add all lists
			//for (Integer listID : listIDs) {
			for (Map.Entry<Integer, String>  entry : listIDToLabels.entrySet()) {
				Integer listID = entry.getKey();
				String conextsLabel = entry.getValue();
				List<TOptionBean> listOptions = optionDAO.loadActiveByListOrderedBySortorder(listID);
				if (listIDToLabels.size()>1) {
					for (TOptionBean optionBean : listOptions) {
						if (conextsLabel!=null && conextsLabel.length()>0) {
							optionBean.setLabel(optionBean.getLabel() + "[" +  conextsLabel + "]");
						}
					}
				}
				optionList.addAll(listOptions);
			}
		}
		if (matcherDatasourceContext.isWithParameter()) {
			optionList.add(getLabelBean(MatcherContext.PARAMETER, locale));
		}
		if (matcherDatasourceContext.isInitValueIfNull() && matcherValue!=null) {
			if (parameterCode==null) {
				//simple select	
				Object value = matcherValue.getValue();
				if (value==null && optionList!=null && !optionList.isEmpty()) {
					matcherValue.setValue(new Integer[] {optionList.get(0).getObjectID()});
				}
			} else {
				//parent in a composite select
				Map<Integer, Object> actualValuesMap = null;
				try {
					actualValuesMap = (Map<Integer, Object>)matcherValue.getValue();
				} catch (Exception e) {
					LOGGER.warn("ValueModified: converting the matcherValue values to map failed with " + e.getMessage(), e);
				}
				if (actualValuesMap != null) {
					if (actualValuesMap.get(parameterCode)==null && optionList!=null && !optionList.isEmpty()) {
						actualValuesMap.put(parameterCode, new Integer[] {optionList.get(0).getObjectID()});
					}
				}
			}
		}
		return optionList;
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
		
		//TODO filter by configOptionRoleDAO combine the entries for each project 
		//dataSource = configOptionRoleDAO.loadOptionsByConfigForRoles(configID, person, project);
		//if not restricted load all options for the list
		//if (dataSource == null) {	
			//no role restrictions, load all options
		Integer fieldID = massOperationValue.getFieldID();
		Map<Integer, Map<Integer, String>> fieldIDToListIDToLabels = massOperationContext.getFieldIDToListIDToLabels();
		if (fieldIDToListIDToLabels!=null) {
			Map<Integer, String> listIDToLabelMap = fieldIDToListIDToLabels.get(fieldID);
			if (listIDToLabelMap!=null) {
				//initialize the value, datasoure and label maps 
				Map<Integer, Object> dataSourceMap = new HashMap<Integer, Object>();
				massOperationValue.setPossibleValues(dataSourceMap);
				Map<Integer, Object> valueMap = (Map<Integer, Object>)massOperationValue.getValue();
				if (valueMap==null) {
					//no submitted value (by first rendering null anyway) 
					valueMap = new HashMap<Integer, Object>();
					massOperationValue.setValue(valueMap);
				}
				Map<Integer, String> valueLabelMap = new HashMap<Integer, String>();
				massOperationValue.setValueLabelMap(valueLabelMap);
				for (Integer listID : listIDToLabelMap.keySet()) {
					valueLabelMap.put(listID, listIDToLabelMap.get(listID));
					List<IBeanID> optionList = LocalizeUtil.localizeDropDownList(OptionBL.loadDataSourceByList(listID), locale);
					Object actualValue = valueMap.get(listID);
					if (parameterCode==null) {
						dataSourceMap.put(listID, optionList);
						//not a composite select
						//for multiple select there is no need to preselect some value
						Integer[] actualIntArrValue = (Integer[])actualValue;
						valueMap.put(listID, getBulkSelectValue(massOperationContext,
								fieldID, parameterCode, actualIntArrValue, optionList));
					} else {
						//first part of a composite part
						Map<Integer, List<IBeanID>> listDatasourceMap = new TreeMap<Integer, List<IBeanID>>();
						dataSourceMap.put(listID, listDatasourceMap);
						listDatasourceMap.put(parameterCode, optionList);
						SortedMap<Integer, Integer[]> actualValueMap = (SortedMap<Integer, Integer[]>)valueMap.get(listID);
						Integer[] actualIntArrValue = null;
						if (actualValueMap==null) {
							//no submitted value (by first rendering null anyway) 
							actualValueMap = new TreeMap<Integer, Integer[]>();
							valueMap.put(listID, actualValueMap);
						} else {
							//submitted value: get to see whether it is still valid
							actualIntArrValue = actualValueMap.get(parameterCode);
						}
						actualValueMap.put(parameterCode, getBulkSelectValue(massOperationContext, 
								fieldID, parameterCode, actualIntArrValue, optionList));
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
		
		TFieldConfigBean configItem = (TFieldConfigBean)FieldConfigItemFacade.getInstance().getValidConfigDirect(
				workflowContext.getItemTypeID(), workflowContext.getProjectTypeID(), workflowContext.getProjectID(), fieldChangeValue.getFieldID());
		if (configItem==null) {
			configItem = (TFieldConfigBean)FieldConfigItemFacade.getInstance().getValidConfigFallback(
					workflowContext.getItemTypeID(), workflowContext.getProjectTypeID(), workflowContext.getProjectID(), fieldChangeValue.getFieldID());
		}
		List<ILabelBean> dataSource = null;
		if (configItem!=null) {
			dataSource = loadList(configItem.getObjectID(), locale);
		}
		Object actualValue = fieldChangeValue.getValue();
		if (parameterCode==null) {
			//dataSourceMap.put(listID, optionList);
			fieldChangeValue.setPossibleValues(dataSource);
			//not a composite select
			//for multiple select there is no need to preselect some value
			Integer[] actualIntArrValue = (Integer[])actualValue;
			actualIntArrValue = super.getBulkSelectValue(null,
					fieldChangeValue.getFieldID(), parameterCode, actualIntArrValue, (List)dataSource);
			fieldChangeValue.setValue(actualIntArrValue);
		} else {
			//first part of a composite part
			SortedMap<Integer, List<IBeanID>> listDatasourceMap = (SortedMap<Integer, List<IBeanID>>)fieldChangeValue.getPossibleValues();
			
			//Map<Integer, List<IBeanID>> listDatasourceMap = new TreeMap<Integer, List<IBeanID>>();
			//dataSourceMap.put(listID, listDatasourceMap);
			listDatasourceMap.put(parameterCode, (List)dataSource);
			SortedMap<Integer, Integer[]> actualValueMap = (SortedMap<Integer, Integer[]>)fieldChangeValue.getValue();
			Integer[] actualIntArrValue = null;
			if (actualValueMap==null) {
				//no submitted value (by first rendering null anyway) 
				actualValueMap = new TreeMap<Integer, Integer[]>();
				fieldChangeValue.setValue(actualValueMap);
				//valueMap.put(listID, actualValueMap);
			} else {
				//submitted value: get to see whether it is still valid
				actualIntArrValue = actualValueMap.get(parameterCode);
			}
			actualValueMap.put(parameterCode, getBulkSelectValue(null, 
					fieldChangeValue.getFieldID(), parameterCode, actualIntArrValue, (List)dataSource));
		}			
	}
	
	/**
	 * Gets the value for a select field by best effort:
	 * If not yet set sets the select value to the actual value 
	 * of the first selected workItemBean's corresponding field value	 
	 * (Avoid setting the value automatically to the first one)
	 * If already set verify whether the previous value is still valid
	 * according to the new possible values list 
	 * @param massOperationContext
	 * @param fieldID
	 * @param paramaterCode
	 * @param actualValue
	 * @param possibleValues
	 * @return
	 */
	@Override
	protected Integer[] getBulkSelectValue(MassOperationContext massOperationContext, 
			Integer fieldID, Integer paramaterCode, Integer[] actualValue, List<IBeanID> possibleValues) {
		if ((actualValue==null || actualValue.length==0) && possibleValues!=null && !possibleValues.isEmpty()) {
			if (isReallyMultiple) {
				//gets the default values
				List<Integer> defaultValues = new LinkedList<Integer>();
				for (IBeanID labelBean : possibleValues) {
					if (((TOptionBean)labelBean).isDefault()) {
						defaultValues.add(labelBean.getObjectID());
					}
				}
				if (!defaultValues.isEmpty()) {
					actualValue = GeneralUtils.createIntegerArrFromCollection(defaultValues);
				}
			} else {
				//gets the first default value
				for (IBeanID labelBean : possibleValues) {
					if (((TOptionBean)labelBean).isDefault()) {
						actualValue = new Integer[] {labelBean.getObjectID()};
						break;
					}
				}
			}
			if (actualValue==null && massOperationContext!=null) {
				TWorkItemBean firstWorkItemBean = massOperationContext.getFirstSelectedWorkItemBean();
				if (firstWorkItemBean!=null) {
					try {
						actualValue = (Integer[])firstWorkItemBean.getAttribute(fieldID, paramaterCode);
					} catch (Exception e) {
					}
				}
			}
		}
		return getValidValues(actualValue, possibleValues, !isReallyMultiple);
	}
	
	
	
	/**
	 * Get the ILabelBean by primary key 
	 * @return
	 */
	@Override
	public ILabelBean getLabelBean(Integer optionID, Locale locale) {
		if (optionID!=null && 
				optionID.equals(MatcherContext.PARAMETER)) {
			TOptionBean optionBean = new TOptionBean();
			optionBean.setLabel(MatcherContext.getLocalizedParameter(locale));
			optionBean.setObjectID(optionID);
			return optionBean;
		}
		return OptionBL.loadByPrimaryKey(optionID);
	}
	
	/**
	 * Returns the lookup entity type related to the fieldType
	 * @return
	 */
	@Override
	public int getLookupEntityType() {
		return LuceneUtil.LOOKUPENTITYTYPES.CUSTOMOPTION;
	}
	
	/**
	 * Creates a new empty serializableLabelBean
	 * @return
	 */
	public ISerializableLabelBean getNewSerializableLabelBean() {
		return new TOptionBean();
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
	public Integer getLookupIDByLabel(Integer fieldID,
			Integer projectID, Integer issueTypeID, 
			Locale locale, String label, 
			Map<String, ILabelBean> lookupBeansMap, Map<Integer, Integer> componentPartsMap) {
		TFieldConfigBean fieldConfigBean = FieldRuntimeBL.getValidConfig(fieldID, issueTypeID, projectID);
		if (fieldConfigBean == null) {
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
		Integer listID = optionSettingsBean.getList();
		if (listID == null) {
			LOGGER.warn("No list specified in option settings for field " + fieldID + 
					" and projectID " + projectID + " issueTypeID " + issueTypeID);
			return null;
		}
		List<TOptionBean> optionBeans = OptionBL.loadByLabel(listID, null, label);
		if (optionBeans!=null && !optionBeans.isEmpty()) {
			TOptionBean optionBean = optionBeans.get(0);
			if (optionBean!=null) {
				return optionBean.getObjectID();
			}
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
		return (List)OptionBL.loadAll();
	}
	
	/**
	 * Whether the field should appear in the groupable fields list
	 * Typically fields which are typically unique should not be groupable
	 * @return
	 */
	public boolean isGroupable() {
		return !isReallyMultiple;
	}
}
