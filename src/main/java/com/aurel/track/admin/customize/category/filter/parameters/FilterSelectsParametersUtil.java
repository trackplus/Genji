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

package com.aurel.track.admin.customize.category.filter.parameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;

import com.aurel.track.admin.customize.category.filter.FilterBL;
import com.aurel.track.admin.customize.category.filter.QNode;
import com.aurel.track.admin.customize.category.filter.TreeFilterFacade;
import com.aurel.track.admin.customize.category.filter.tree.design.FieldExpressionSimpleTO;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterSelectsListsLoader;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperFromQNodeTransformer;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO.PSEUDO_FIELDS;
import com.aurel.track.admin.customize.category.filter.tree.design.MatcherExpressionBase;
import com.aurel.track.admin.customize.category.filter.tree.design.TreeFilterLoaderBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.project.ProjectPickerBL;
import com.aurel.track.admin.project.release.ReleaseBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.beans.TQueryRepositoryBean.QUERY_PURPOSE;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.custom.picker.CustomTreePickerRT;
import com.aurel.track.fieldType.runtime.matchers.design.MatcherDatasourceContext;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.TreeNode;

public class FilterSelectsParametersUtil {
	
	/**
	 * Whether the saved filter contains parameter or not 
	 * @return
	 */
	public static boolean savedFilterContainsParameter(Integer filterID, TPersonBean personBean, Locale locale) {
		TQueryRepositoryBean queryRepositoryBean = 
			(TQueryRepositoryBean)TreeFilterFacade.getInstance().getByKey(filterID);
		boolean containsParameter = false; 
		if (queryRepositoryBean!=null) {
			Integer queryType = queryRepositoryBean.getQueryType();
			if (queryType!=null && queryType.intValue()==QUERY_PURPOSE.TREE_FILTER) {
				QNode extendedRootNode = FilterBL.loadNode(queryRepositoryBean);
				FilterUpperTO filterUpperTO = FilterUpperFromQNodeTransformer.getFilterSelectsFromTree(
						extendedRootNode, true, true, personBean, locale, true);
				QNode rootNode = TreeFilterLoaderBL.getOriginalTree(extendedRootNode);
				if (containsParameter(filterUpperTO) ||
						QNodeParametersUtil.containsParameter(rootNode)) {
						containsParameter = true;
				}
			}
		}
		return containsParameter;
	}
	
	/**
	 * Whether the filterSelectsTO contains parameter(s)
	 * @param filterSelectsTO
	 * @return
	 */
	public static boolean containsParameter(FilterUpperTO filterSelectsTO) {
		if (filterSelectsTO==null) {
			return false;
		}
		List<Integer> upperSelectFields = getUpperSelectFields(filterSelectsTO);
		for (Integer fieldID : upperSelectFields) {
			if (containsParameter(filterSelectsTO.getSelectedValuesForField(fieldID))) {
				return true;
			}
		}
		List<FieldExpressionSimpleTO> fieldExpressionSimpleList = filterSelectsTO.getFieldExpressionSimpleList();
		if (fieldExpressionSimpleList!=null) {
			for (FieldExpressionSimpleTO fieldExpressionSimpleTO : fieldExpressionSimpleList) {
				Integer matcherID = fieldExpressionSimpleTO.getSelectedMatcher();
				if (MatcherContext.PARAMETER.equals(matcherID)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gets the upper select fields from filterUpperTO
	 * @param filterUpperTO
	 * @return
	 */
	private static List<Integer> getUpperSelectFields(FilterUpperTO filterUpperTO) {
		List<Integer> selectFieldIDList = new LinkedList<Integer>();
		selectFieldIDList.add(SystemFields.INTEGER_PROJECT);
		selectFieldIDList.add(SystemFields.INTEGER_RELEASESCHEDULED);
		selectFieldIDList.add(SystemFields.INTEGER_ORIGINATOR);
		selectFieldIDList.add(SystemFields.INTEGER_CHANGEDBY);
		selectFieldIDList.add(PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID);
		selectFieldIDList.add(SystemFields.INTEGER_MANAGER);
		selectFieldIDList.add(SystemFields.INTEGER_RESPONSIBLE);
		selectFieldIDList.add(SystemFields.INTEGER_ISSUETYPE);
		selectFieldIDList.add(SystemFields.INTEGER_STATE);
		selectFieldIDList.add(SystemFields.INTEGER_PRIORITY);
		selectFieldIDList.add(SystemFields.INTEGER_SEVERITY);
		if (filterUpperTO.getCustomSelectSimpleFields()!=null) {
			selectFieldIDList.addAll(filterUpperTO.getCustomSelectSimpleFields().keySet());
		}
		return selectFieldIDList;
	}
	
	/**
	 * Whether a list has also the $Parameter entry selected 
	 * @param selectedValues
	 * @return
	 */
	static boolean containsParameter(Integer[] selectedValues) {
		if (selectedValues!=null && selectedValues.length>0) {
			for (Integer selectedValue : selectedValues) {
				try {
					if (selectedValue!=null && 
							selectedValue.equals(MatcherContext.PARAMETER)) {
						return true;
					}
				} catch (Exception e) {
				}
			}
		}
		return false;
	}
	
	/**************************************************************************************/
	/**************Helpers by executing the parameterized filter directly******************/
	/*************(parameter replacement based on objectID selected on the UI)*************/
	/**************************************************************************************/
	
	/**
	 * Prepares the filterUpperTO for setting the parameters
	 * 1. by first rendering the parameterized fields of filterSelectsTO
	 * 2. after project change in the already rendered parameterized fields of filterSelectsTO
	 * to refresh the other, project dependent parameterized fields
	 * @param originalFilterUpperTO
	 * @param parameterFilterUpperTO
	 * @param personBean
	 * @param locale
	 * @return
	 */
	public static Integer[] prepareParameterFilterSelects(FilterUpperTO originalFilterUpperTO,
			FilterUpperTO parameterFilterUpperTO, TPersonBean personBean, Locale locale) {
		Integer[] originalSelectedProjects = originalFilterUpperTO.getSelectedProjects();
		List<Integer> parameterizedUpperFields = new LinkedList<Integer>();
		parameterFilterUpperTO.setUpperFields(parameterizedUpperFields);
		/*
		 * get the selected projects
		 */
		//the data in some other lists depends on the current project selection 
		Integer[] selectedProjects;
		if (containsParameter(originalSelectedProjects)) {
			//remove the projects which are selected in the originalReportConfigBean  
			//allProjects = removeSelected(originalSelectedProjects, allProjects);
			//render the projects list only if contains parameter
			//parameterFilterUpperTO.setProjects(allProjects);
			//gather the two project selections together
			Integer[] originalSelection = originalFilterUpperTO.getSelectedProjects();
			selectedProjects = getSelectedWithoutParam(originalSelection);
			parameterizedUpperFields.add(SystemFields.INTEGER_PROJECT);
			parameterFilterUpperTO.setProjectTree(ProjectPickerBL.getTreeNodesForRead(
					GeneralUtils.createSetFromIntegerArr(selectedProjects), true, false, personBean, locale));
		} else {
			//do not render the project list
			selectedProjects = originalFilterUpperTO.getSelectedProjects();
		}
		Integer[] ancestorProjects = null;
		if (selectedProjects==null || selectedProjects.length==0) {
			//none of the projects is selected -> get the other lists datasource as all available projects would be selected
			List<TProjectBean> projectBeans = ProjectBL.loadUsedProjectsFlat(personBean.getObjectID());
			if (projectBeans!=null && !projectBeans.isEmpty()) {
				selectedProjects = GeneralUtils.createIntegerArrFromCollection(GeneralUtils.createIntegerListFromBeanList(projectBeans));
				ancestorProjects = selectedProjects;
			}
		} else {
			ancestorProjects = ProjectBL.getAncestorProjects(selectedProjects);
		}
		List<Integer> upperSelectFields = getUpperSelectFields(originalFilterUpperTO);
		//project is already prepared explicitly 
		upperSelectFields.remove(SystemFields.INTEGER_PROJECT);
		/*
		 * get the watchers (no fieldType based datasource)
		 */
		Integer[] selectedConsultantsInformants = originalFilterUpperTO.getSelectedConsultantsInformants();
		if (containsParameter(selectedConsultantsInformants)) {
			Integer[] selectedWithoutParam = getSelectedWithoutParam(selectedConsultantsInformants);
			parameterFilterUpperTO.setSelectedConsultantsInformants(selectedWithoutParam);
			parameterFilterUpperTO.setConsultantsInformants(
					FilterSelectsListsLoader.getConsultantsInformants(selectedProjects, ancestorProjects, personBean.getObjectID(), false, null, locale));
			parameterizedUpperFields.add(Integer.valueOf(PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID));
		}
		upperSelectFields.remove(Integer.valueOf(PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID));
		/*
		 * get all other selects 
		 */
		for (Integer fieldID : upperSelectFields) {
			Integer[] selectedValues = originalFilterUpperTO.getSelectedValuesForField(fieldID);
			if (containsParameter(selectedValues)) {
				IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
				Integer[] selectedWithoutParam = getSelectedWithoutParam(selectedValues);
				parameterFilterUpperTO.setSelectedValuesForField(fieldID, selectedWithoutParam);
				MatcherDatasourceContext matcherDatasourceContext = new MatcherDatasourceContext(
						selectedProjects, ancestorProjects, originalFilterUpperTO.getSelectedIssueTypes(), personBean, locale, false, true, originalFilterUpperTO.isShowClosedReleases(), false);
				Object datasource = fieldTypeRT.getMatcherDataSource(
						new MatcherExpressionBase(fieldID, selectedWithoutParam), matcherDatasourceContext, null);
				if (FieldBL.isTree(fieldID)) {
					parameterFilterUpperTO.setTreeDatasourceForField(fieldID, (List<TreeNode>)datasource);
				} else {
					parameterFilterUpperTO.setListDatasourceForField(fieldID, (List<ILabelBean>)datasource);
				}
				parameterizedUpperFields.add(fieldID);
			}
		}
		return selectedProjects;
	}

	private static Integer[] getSelectedWithoutParam(Integer[] originalSelection) {
		List<Integer> selectionsWithoutParam = new LinkedList<Integer>();
		if (originalSelection!=null) {
			for (Integer selection : originalSelection) {
				if (!MatcherContext.PARAMETER.equals(selection)) {
					selectionsWithoutParam.add(selection);
				}
			}
		}
		return GeneralUtils.createIntegerArrFromCollection(selectionsWithoutParam);
	}
	
	/**
	 * Replace the parameter value(s) with actual values in the original selections
	 * @param originalValues
	 * @param parameterValues
	 * @return
	 */
	static Integer[] replaceParameter(Integer[] originalValues, Integer[] parameterValues) {
		if (containsParameter(originalValues)) {
			List<Integer> selectedValues = new ArrayList<Integer>();
			if (originalValues!=null && originalValues.length>0) {
				for (int i = 0; i < originalValues.length; i++) {
					Integer selectedValue = originalValues[i];
					if (selectedValue!=null) {
						try {
							if (selectedValue.equals(MatcherContext.PARAMETER)) {
								//if "clear" do not add anything instead of parameter, only get rid of parameter
								if (parameterValues!=null) {
									for (int j = 0; j < parameterValues.length; j++) {
										if (parameterValues[j]!=null) {
											selectedValues.add(parameterValues[j]);
										}
									}
								}
							} else {
								selectedValues.add(selectedValue);
							}
						} catch (Exception e) {
						}
					}
				}
			}
			return GeneralUtils.createIntegerArrFromCollection(selectedValues);
		} else {
			return originalValues;
		}
	}

	/**
	 * Replace the $Parameter placeholders with values after the parameters are set during the execution of a filter
	 * @param originalFilterSelectsTO
	 * @param parametersFilterSelectsTO
	 * @param locale 
	 * @return
	 */
	public static FilterUpperTO replaceParameters(FilterUpperTO originalFilterSelectsTO, 
			FilterUpperTO parametersFilterSelectsTO, Locale locale) {
		//FilterUpperTO replacedFilterSelectsTO = new FilterUpperTO();
		//make a copy of the originalFilterSelectsTO instead of modifying the original
		//to keep also the parameterized originalReportConfigBean in the session which is helpful
		//by using the browser's back button after executing a parameterized filter. 
		//Otherwise for example by project change no upper part would be rendered because the parameters 
		//are already replaced in originalReportConfigBean
		/*try {
			PropertyUtils.copyProperties(replacedFilterSelectsTO, originalFilterSelectsTO);
		} catch (Exception e) {	
			replacedFilterSelectsTO = originalFilterSelectsTO;
			LOGGER.debug(org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
		}*/
		List<Integer> upperSelectFields = getUpperSelectFields(originalFilterSelectsTO);
		for (Integer fieldID : upperSelectFields) {
			if (containsParameter(originalFilterSelectsTO.getSelectedValuesForField(fieldID))) {
				originalFilterSelectsTO.setSelectedValuesForField(fieldID, parametersFilterSelectsTO.getSelectedValuesForField(fieldID));
			}
			/*originalFilterSelectsTO.setSelectedValuesForField(fieldID, replaceParameter(
					originalFilterSelectsTO.getSelectedValuesForField(fieldID), 
					parametersFilterSelectsTO.getSelectedValuesForField(fieldID)));*/
		}
		return originalFilterSelectsTO; 
	}
	
	/**************************************************************************************/
	/**************Helpers by executing the parameterized filter by URL *******************/
	/************(parameter replacement based on labels set in request parameters)*********/
	/**************************************************************************************/
	
	
	/**
	 * Replaces the parameters from the filterUpperTO selects with corresponding parameters from request 
	 * @param filterUpperTO
	 * @param request
	 * @param personBean
	 * @param locale
	 * @param clear
	 * @return
	 */
	public static FilterUpperTO replaceFilterSelectsParameters(FilterUpperTO filterUpperTO,
			ServletRequest request, TPersonBean personBean, Locale locale, boolean clear) throws NotExistingBeanException {
		if (request!=null) {
			//only the upper selects are parameterizable for encoded query urls!
			List<IntegerStringBean> parametrizedFields = FilterSelectsParametersUtil.getParameterizedFields(filterUpperTO);
			if(parametrizedFields!=null && !parametrizedFields.isEmpty()) {
				//prepares the paramsRequest: from label based values in request it creates fieldID based values 
				Map<Integer, String> paramsRequest = new HashMap<Integer, String>();
				for (int i = 0; i < parametrizedFields.size(); i++) {
					IntegerStringBean integerStringBean = parametrizedFields.get(i);
					String value = request.getParameter(integerStringBean.getLabel());
					if(value!=null && value.length()>0) {
						Integer fieldID = integerStringBean.getValue();
						paramsRequest.put(fieldID, value);
					}
				}
				filterUpperTO = FilterSelectsListsLoader.loadFilterSelects(filterUpperTO, personBean, locale, false, false);
				filterUpperTO = replaceFilterSelectsParameters(filterUpperTO, personBean.getObjectID(), paramsRequest, clear);
			}
		}
		return filterUpperTO;
	}
	
	/**
	 * Replaces the parameters from filterSelectsTO with values from the requestParams map
	 * @param filterSelectsTO
	 * @param requestParams
	 * @param clear
	 * @throws NotExistingBeanException when a bean can't be identified by a label
	 * @return
	 */
	private static FilterUpperTO replaceFilterSelectsParameters(FilterUpperTO filterSelectsTO, Integer personID,
			Map<Integer, String> requestParams, boolean clear) throws NotExistingBeanException {
		List<Integer> upperSelectFields = getUpperSelectFields(filterSelectsTO);
		for (Integer fieldID : upperSelectFields) {
			List<ILabelBean> datasourceForField = null;
			if (SystemFields.INTEGER_PROJECT.equals(fieldID)) {
				datasourceForField = FilterSelectsListsLoader.getProjects(personID);
			} else {
				if (SystemFields.INTEGER_RELEASE.equals(fieldID)) {
					datasourceForField = (List)ReleaseBL.loadAllByProjects(GeneralUtils.createListFromIntArr(filterSelectsTO.getSelectedProjects()));
				} else {
					if (FieldBL.isTree(fieldID)) {
						CustomTreePickerRT customTreePickerRT = (CustomTreePickerRT)FieldTypeManager.getFieldTypeRT(fieldID);
						//project picker: no tree but the flat list is needed now
						datasourceForField = customTreePickerRT.getFlatDataSource(personID);
					} else {
						datasourceForField = filterSelectsTO.getListDatasourceForField(fieldID);
					}
				}
			}
			filterSelectsTO.setSelectedValuesForField(fieldID, obtainSelectedBeans(fieldID, requestParams.get(fieldID),
					filterSelectsTO.getSelectedValuesForField(fieldID),
					datasourceForField, clear));
		}
		return filterSelectsTO;
	}

	/**
	 * Obtain the new selected bean from label
	 * @param fieldID
	 * @param originalSelections
	 * @param beanList
	 * @param clear force clear
	 * @throws NotExistingBeanException when a bean can't be identified by a label
	 * @return
	 */
	private static Integer[] obtainSelectedBeans(Integer fieldID, String selectOptionLabel,
			Integer[] originalSelections, List<ILabelBean> beanList, boolean clear) throws NotExistingBeanException {
		//set result to original selected beans 
		Integer[] parameterValue = originalSelections;
		Integer[] paramsReplace=null;
		Integer optionID=null;
		if (selectOptionLabel!=null){
			//obtain the id corresponding to label bean
			optionID = findILabelBeanValue(beanList, selectOptionLabel);
			if(optionID!=null){
				paramsReplace=new Integer[]{optionID};
			} else {
				if (!clear) {
					throw new NotExistingBeanException("The label " + selectOptionLabel + " is not found for fieldID " + fieldID);
				}
			}
		}
		//replace selected beans only if there exist a bean for label or is 
		//clear is true -- force to clear
		if(optionID!=null || clear){
			parameterValue = replaceParameter(originalSelections, paramsReplace);
		}
		return parameterValue;
	}

	/**
	 * Finds a label bean by label 
	 * @param labelBeans
	 * @param label
	 * @return
	 */
	private static Integer findILabelBeanValue(List<ILabelBean> labelBeans, String label){
		if(label==null){
			return null;
		}
		if (labelBeans!=null){
			for (ILabelBean labelBean : labelBeans) {
				if(labelBean.getLabel().equals(label)){
					return labelBean.getObjectID();
				}
			}
		}
		return null;
	}

	/**
	 * Get a list of IntegerStringBean for those fields which are parameterized 
	 * @param filterSelectsTO
	 * @return
	 */
	public static List<IntegerStringBean> getParameterizedFields(FilterUpperTO filterSelectsTO) {
		List<IntegerStringBean> result = new LinkedList<IntegerStringBean>();
		List<Integer> upperSelectFields = getUpperSelectFields(filterSelectsTO);
		Set<Integer> fieldIDs=new HashSet<Integer>();
		for (Integer fieldID : upperSelectFields) {
			if (containsParameter(filterSelectsTO.getSelectedValuesForField(fieldID))) {
				fieldIDs.add(fieldID);
			}
		}
		if(!fieldIDs.isEmpty()){
			List<TFieldBean> allFields=FieldBL.loadByFieldIDs(fieldIDs.toArray());
			for (TFieldBean fieldBean : allFields) {
				result.add(new IntegerStringBean(fieldBean.getName(), fieldBean.getObjectID()));
			}
		}
		//PSEUDO FIELDS
		if(containsParameter(filterSelectsTO.getSelectedConsultantsInformants())){
			result.add(new IntegerStringBean("watcher", PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID));
		}
		return result;
	}
}
