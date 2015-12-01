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

package com.aurel.track.admin.customize.category.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.customize.category.filter.tree.design.FieldExpressionInTreeTO;
import com.aurel.track.admin.customize.category.filter.tree.design.FieldExpressionSimpleTO;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterSelectsListsLoader;
import com.aurel.track.admin.customize.role.FieldsRestrictionsToRoleBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.beans.TReportLayoutBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.runtime.custom.text.CustomDoubleRT;
import com.aurel.track.fieldType.runtime.matchers.MatchRelations;
import com.aurel.track.fieldType.runtime.matchers.converter.AccountingTimeMatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.converter.DoubleMatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.converter.MatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.converter.SelectMatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.design.AccountingTimeMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.design.DoubleMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherValue;
import com.aurel.track.fieldType.runtime.matchers.design.MatcherDatasourceContext;
import com.aurel.track.fieldType.runtime.matchers.design.SelectMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.fieldType.runtime.system.select.SystemManagerRT;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.consInf.RaciRole;
import com.aurel.track.itemNavigator.layout.column.ColumnFieldsBL;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;

public class FieldExpressionBL {
	//the following names should comply with the setter field names 
	//in the filter actions which will be set as a result of a form submit
	public static String MATCHER_RELATION_BASE_NAME = "MatcherRelationMap";
	public static String VALUE_BASE_NAME = "DisplayValueMap";
	public static String VALUE_BASE_ITEM_ID = "DisplayValue";
	public static String SIMPLE = "simple";
	public static String IN_TREE = "inTree";
	public static String CASCADING_PART = "CascadingPart";
	
	public static String FIELD_NAME = "fieldMap";
	public static String FIELD_MOMENT_NAME = "fieldMomentMap";
	public static String OPERATION_NAME = "operationMap";
	public static String PARENTHESIS_OPEN_NAME = "parenthesisOpenedMap";
	public static String PARENTHESIS_CLOSED_NAME = "parenthesisClosedMap";
	public static String NEGATION_NAME = "negationMap";
		
	public static String MATCH_RELATION_PREFIX = "admin.customize.queryFilter.opt.relation.";
	
	/**
	 * Prepares a JSON string after matcher relation change  
	 * @param baseName
	 * @param stringValue
	 * @param fieldID
	 * @param matcherID
	 * @param modifiable
	 * @param personID
	 * @param locale
	 * @return
	 */
	static String selectMatcher(Integer[] projectIDs, Integer[] itemTypeIDs, String baseName, String baseItemId, Integer index, String stringValue,
			Integer fieldID, Integer matcherID, boolean modifiable, TPersonBean personBean, Locale locale) {
		FieldExpressionInTreeTO fieldExpressionInTreeTO =
			configureValueDetails(projectIDs, itemTypeIDs, baseName, baseItemId, index, stringValue, fieldID, matcherID, modifiable, personBean, locale);
		return FilterJSON.getFieldExpressionValueJSON(fieldExpressionInTreeTO.getValueItemId(), fieldExpressionInTreeTO.isNeedMatcherValue(),
				fieldExpressionInTreeTO.getValueRenderer(), fieldExpressionInTreeTO.getJsonConfig());
	}

	/**
	 * Reload part of the filter expression after a field change: possible matchers and the value part
	 * @param baseName
	 * @param stringValue
	 * @param index
	 * @param fieldID
	 * @param matcherID
	 * @param personID
	 * @param locale
	 * @param withParameter
	 * @return
	 */
	static String selectField(Integer[] projectIDs, Integer[] itemTypeIDs, String baseName, String baseItemId, Integer index, String stringValue, Integer fieldID, Integer matcherID,
			boolean modifiable, TPersonBean personBean, Locale locale, boolean withParameter) {
		List<IntegerStringBean> matcherRelations = getMatchers(fieldID, withParameter, true, locale);
		if (matcherID == null) {
			//set the matcher relation to the first when not yet selected
			matcherID = matcherRelations.get(0).getValue();
		} else {
			Iterator<IntegerStringBean> iterator = matcherRelations.iterator();
			boolean found = false;
			while (iterator.hasNext()) {
				IntegerStringBean integerStringBean	= iterator.next();
				if (matcherID.equals(integerStringBean.getValue())) {
					found = true;
					break;
				}
			}
			if (!found) {
				//change the matcher relation to the first
				//only if the old one is not found among the new matcherRelations list 
				matcherID = matcherRelations.get(0).getValue();
			}
		}
		FieldExpressionInTreeTO fieldExpressionInTreeTO =
			configureValueDetails(projectIDs, itemTypeIDs, baseName, baseItemId, index, stringValue, fieldID, matcherID, modifiable, personBean, locale);
		String valueJSON = FilterJSON.getFieldExpressionValueBaseJSON(fieldExpressionInTreeTO.getValueItemId(), fieldExpressionInTreeTO.isNeedMatcherValue(),
				fieldExpressionInTreeTO.getValueRenderer(), fieldExpressionInTreeTO.getJsonConfig(), true);
		return FilterJSON.getFieldExpressionMatcherAndValueValueJSON(matcherRelations, matcherID, valueJSON);
	}	
	
	/**
	 * Prepares a JSON string after matcher relation or field change  
	 * @param name
	 * @param stringValue
	 * @param fieldID
	 * @param matcherID
	 * @param modifiable
	 * @param personID
	 * @param locale
	 * @return
	 */
	private static FieldExpressionInTreeTO configureValueDetails(Integer[] projectIDs, Integer[] itemTypeIDs, String baseName, String baseItemId, Integer index, String stringValue,
			Integer fieldID, Integer matcherID, boolean modifiable, TPersonBean personBean, Locale locale) {
		FieldExpressionInTreeTO fieldExpressionInTreeTO = new FieldExpressionInTreeTO();
		fieldExpressionInTreeTO.setField(fieldID);
		fieldExpressionInTreeTO.setSelectedMatcher(matcherID);
		MatcherConverter matcherConverter = null;
		if (fieldID>0) {
			//system or custom field
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
			if (fieldTypeRT!=null) {
				matcherConverter = fieldTypeRT.getMatcherConverter();
			}
		} else {
			//pseudo field
			matcherConverter = getPseudoFieldMatcherConverter(fieldID);
		}
		Object value = null;
		if (matcherConverter!=null) {
			value = matcherConverter.fromValueString(stringValue, locale, matcherID);
		}
		fieldExpressionInTreeTO.setValue(value);
		Integer[] ancestorProjectIDs = null;
		if (projectIDs==null || projectIDs.length==0) {
			//none of the projects is selected -> get the other lists datasource as all available projects would be selected
			List<TProjectBean> projectBeans = ProjectBL.loadUsedProjectsFlat(personBean.getObjectID());
			if (projectBeans!=null && !projectBeans.isEmpty()) {
				projectIDs = GeneralUtils.createIntegerArrFromCollection(GeneralUtils.createIntegerListFromBeanList(projectBeans));
				ancestorProjectIDs = projectIDs;
			}
		} else {
			ancestorProjectIDs = ProjectBL.getAncestorProjects(projectIDs);
		}
		setExpressionValue(fieldExpressionInTreeTO, projectIDs, ancestorProjectIDs, itemTypeIDs, baseName, baseItemId, index, modifiable, personBean, locale);
		return fieldExpressionInTreeTO;
	}
	
	/**
	 * Prepares a FieldExpressionSimpleTO for rendering:
	 * Only the matcher and the value controls are "active",
	 * field is fixed (only label is rendered for field)
	 * @param fieldID
	 * @param matcherID
	 * @param modifiable
	 * @param withParameter
	 * @param personID
	 * @param locale
	 * @param value
	 * @return
	 */
	public static FieldExpressionSimpleTO loadFilterExpressionSimple(Integer fieldID, Integer[] projectIDs, Integer[] itemTypeIDs,
			Integer matcherID, boolean modifiable, boolean withParameter, TPersonBean personBean,
			Locale locale, Map<Integer, String> fieldLabelsMap, Object value) {
		FieldExpressionSimpleTO fieldExpressionSimpleTO = new FieldExpressionSimpleTO();
		fieldExpressionSimpleTO.setField(fieldID);
		fieldExpressionSimpleTO.setSelectedMatcher(matcherID);
		fieldExpressionSimpleTO.setValue(value);
		fieldExpressionSimpleTO.setFieldLabel(fieldLabelsMap.get(fieldID));
		fieldExpressionSimpleTO.setMatcherName(getName(SIMPLE + MATCHER_RELATION_BASE_NAME, fieldID));
		fieldExpressionSimpleTO.setMatcherItemId(getItemId(SIMPLE + MATCHER_RELATION_BASE_NAME, fieldID));
		List<IntegerStringBean> matchers = getMatchers(fieldID,  withParameter, false, locale);
		//add empty matcher for FieldExpressionSimpleTO to give the possibility to not filter by this field
		matchers.add(0, new IntegerStringBean("-", MatchRelations.NO_MATCHER));
		fieldExpressionSimpleTO.setMatchersList(matchers);
		Integer[] ancestorProjectIDs = null;
		if (projectIDs==null || projectIDs.length==0) {
			//none of the projects is selected -> get the other lists datasource as all available projects would be selected
			List<TProjectBean> projectBeans = ProjectBL.loadUsedProjectsFlat(personBean.getObjectID());
			if (projectBeans!=null && !projectBeans.isEmpty()) {
				projectIDs = GeneralUtils.createIntegerArrFromCollection(GeneralUtils.createIntegerListFromBeanList(projectBeans));
				ancestorProjectIDs = projectIDs;
			}
		} else {
			ancestorProjectIDs = ProjectBL.getAncestorProjects(projectIDs);
		}
		setExpressionValue(fieldExpressionSimpleTO, projectIDs, ancestorProjectIDs, itemTypeIDs,
				SIMPLE + VALUE_BASE_NAME, SIMPLE + VALUE_BASE_ITEM_ID, fieldID, modifiable, personBean, locale);
		return fieldExpressionSimpleTO;
	}
	
	/**
	 * Prepares a FieldExpressionSimpleTO created from a parameterized FieldExpressionInTreeTO for rendering:
	 * @param fieldExpressionSimpleTO
	 * @param projectIDs
	 * @param showClosed
	 * @param personID
	 * @param locale
	 * @param index
	 * @return
	 */
	public static FieldExpressionSimpleTO loadFieldExpressionSimpleForInTreeParameter(
			FieldExpressionSimpleTO fieldExpressionSimpleTO, Integer[] projectIDs, Integer[] itemTypeIDs,
			TPersonBean personBean, Locale locale, Map<Integer, String> fieldLabelsMap, int index) {
		Integer fieldID = fieldExpressionSimpleTO.getField();
		fieldExpressionSimpleTO.setIndex(index);
		fieldExpressionSimpleTO.setFieldLabel(fieldLabelsMap.get(fieldID));
		setMatchers(fieldExpressionSimpleTO, true, locale);
		fieldExpressionSimpleTO.setMatcherName(getName(IN_TREE + MATCHER_RELATION_BASE_NAME, index));
		fieldExpressionSimpleTO.setMatcherItemId(getItemId(IN_TREE + MATCHER_RELATION_BASE_NAME, index));
		Integer[] ancestorProjectIDs = null;
		if (projectIDs==null || projectIDs.length==0) {
			//none of the projects is selected -> get the other lists datasource as all available projects would be selected
			List<TProjectBean> projectBeans = ProjectBL.loadUsedProjectsFlat(personBean.getObjectID());
			if (projectBeans!=null && !projectBeans.isEmpty()) {
				projectIDs = GeneralUtils.createIntegerArrFromCollection(GeneralUtils.createIntegerListFromBeanList(projectBeans));
				ancestorProjectIDs = projectIDs;
			}
		} else {
			ancestorProjectIDs = ProjectBL.getAncestorProjects(projectIDs);
		}
		setExpressionValue(fieldExpressionSimpleTO, projectIDs, ancestorProjectIDs, itemTypeIDs,
				IN_TREE + VALUE_BASE_NAME, IN_TREE + VALUE_BASE_ITEM_ID, index, true, personBean, locale);
		return fieldExpressionSimpleTO;
	}
	
	/**
	 * Set the matchers for parameter fields
	 * @param fieldExpressionSimpleTO
	 * @param locale
	 * @return
	 */
	public static FieldExpressionSimpleTO setMatchers(FieldExpressionSimpleTO fieldExpressionSimpleTO, boolean isTree, Locale locale) {
		List<IntegerStringBean> matcherRelations = getMatchers(fieldExpressionSimpleTO.getField(), false, isTree, locale);
		//add empty matcher for FieldExpressionSimpleTO to give the possibility to
		//not to set parameter for this field consequently do not filter by this field
		//TODO do we need this empty matcher for parameters or not?
		matcherRelations.add(0, new IntegerStringBean("", MatchRelations.NO_MATCHER));
		fieldExpressionSimpleTO.setMatchersList(matcherRelations);
		Integer matcherID = fieldExpressionSimpleTO.getSelectedMatcher();
		if (matcherID==null || matcherID.equals(MatcherContext.PARAMETER)) {
			//matcher was parameter, now set it to the first
			matcherID = matcherRelations.get(0).getValue();
			fieldExpressionSimpleTO.setSelectedMatcher(matcherID);
		}
		return fieldExpressionSimpleTO;
	}
	
	/**
	 * Prepares the "in tree" filter expressions for rendering 
	 * @param fieldExpressionInTreeList
	 * @param modifiable
	 * @param personID
	 * @param locale
	 * @param instant
	 * @param withFieldMoment
	 */
	public static void loadFilterExpressionInTreeList(List<FieldExpressionInTreeTO> fieldExpressionInTreeList, Integer[] projectIDs,
			Integer[] itemTypeIDs, boolean modifiable, TPersonBean personBean, Locale locale, boolean withParameter, boolean withFieldMoment) {
		if (fieldExpressionInTreeList!=null && !fieldExpressionInTreeList.isEmpty()) {
			List<IntegerStringBean> fieldsWithMatcher = getAllMatcherColumns(projectIDs, personBean.getObjectID(), locale);
			List<IntegerStringBean> localizedOperationList = getLocalizedOperationList(locale);
			List<IntegerStringBean> parenthesisOpenList = createParenthesisOpenList(); 
			List<IntegerStringBean> parenthesisClosedList = createParenthesisClosedList();
			List<IntegerStringBean> fieldMoments = null;
			if (withFieldMoment) {
				fieldMoments = prepareFieldMomentList(locale);
			}
			Integer[] ancestorProjects = null;
			if (projectIDs==null || projectIDs.length==0) {
				//none of the projects is selected -> get the other lists datasource as all available projects would be selected
				List<TProjectBean> projectBeans = ProjectBL.loadUsedProjectsFlat(personBean.getObjectID());
				if (projectBeans!=null && !projectBeans.isEmpty()) {
					projectIDs = GeneralUtils.createIntegerArrFromCollection(GeneralUtils.createIntegerListFromBeanList(projectBeans));
					ancestorProjects = projectIDs;
				}
			} else {
				ancestorProjects = ProjectBL.getAncestorProjects(projectIDs);
			}
			int i=0;
			for (FieldExpressionInTreeTO fieldExpressionInTreeTO : fieldExpressionInTreeList) {
				fieldExpressionInTreeTO.setOperationsList(localizedOperationList);
				fieldExpressionInTreeTO.setParenthesisOpenList(parenthesisOpenList);
				fieldExpressionInTreeTO.setParenthesisClosedList(parenthesisClosedList);
				if (withFieldMoment) {
					fieldExpressionInTreeTO.setFieldMomentsList(fieldMoments);
				}
				loadFieldExpressionInTree(fieldExpressionInTreeTO, projectIDs, ancestorProjects, itemTypeIDs, fieldsWithMatcher,
						modifiable, personBean, locale, withParameter, withFieldMoment, i++);
			}
		}
	}
	
	/**
	 * Prepares the "in tree" filter expressions for rendering
	 * @param index
	 * @param operation the default operation
	 * @param modifiable
	 * @param personID
	 * @param locale
	 * @param instant
	 * @param withFieldMoment
	 * @return
	 */
	static FieldExpressionInTreeTO loadFilterExpressionInTree(Integer[] projectIDs, Integer[] itemTypeIDs, Integer fieldID, Integer index,
			boolean modifiable, TPersonBean personBean, Locale locale, boolean withParameter, boolean withFieldMoment) {
			List<IntegerStringBean> fieldsWithMatcher = getAllMatcherColumns(projectIDs, personBean.getObjectID(), locale);
			FieldExpressionInTreeTO fieldExpressionInTreeTO = new FieldExpressionInTreeTO();
			List<IntegerStringBean> operationList = getLocalizedOperationList(locale);
			fieldExpressionInTreeTO.setOperationsList(operationList);
			//if added from the first add (For adds from a filter expression
			//it takes the operation from the actual filter expression as default on the client side) 
			fieldExpressionInTreeTO.setSelectedOperation(QNode.OR);
			fieldExpressionInTreeTO.setField(fieldID);
			fieldExpressionInTreeTO.setParenthesisOpenList(createParenthesisOpenList());
			fieldExpressionInTreeTO.setParenthesisClosedList(createParenthesisClosedList());
			if (withFieldMoment) {
				List<IntegerStringBean> fieldMoments = prepareFieldMomentList(locale);
				fieldExpressionInTreeTO.setFieldMomentsList(fieldMoments);
				fieldExpressionInTreeTO.setFieldMoment(fieldMoments.get(0).getValue());
			}
			Integer[] ancestorProjects = null;
			if (projectIDs==null || projectIDs.length==0) {
				//none of the projects is selected -> get the other lists datasource as all available projects would be selected
				List<TProjectBean> projectBeans = ProjectBL.loadUsedProjectsFlat(personBean.getObjectID());
				if (projectBeans!=null && !projectBeans.isEmpty()) {
					projectIDs = GeneralUtils.createIntegerArrFromCollection(GeneralUtils.createIntegerListFromBeanList(projectBeans));
					ancestorProjects = projectIDs;
				}
			} else {
				ancestorProjects = ProjectBL.getAncestorProjects(projectIDs);
			}
			loadFieldExpressionInTree(fieldExpressionInTreeTO, projectIDs, ancestorProjects, itemTypeIDs, fieldsWithMatcher,
					modifiable, personBean, locale, withParameter, withFieldMoment, index);
			return fieldExpressionInTreeTO;
	}
	
	/**
	 * Get all matcher columns
	 * @param projectIDs
	 * @param personID
	 * @param locale
	 * @return
	 */
	private static List<IntegerStringBean> getAllMatcherColumns(Integer[] projectIDs, Integer personID, Locale locale) {
		List<TFieldConfigBean> fieldConfigsWithMatcher = 
				FieldRuntimeBL.getDefaultFieldConfigsWithMatcher(locale);
		List<IntegerStringBean> fieldsWithMatcher = new LinkedList<IntegerStringBean>();
		for (TFieldConfigBean fieldConfigBean : fieldConfigsWithMatcher) {
			fieldsWithMatcher.add(new IntegerStringBean(fieldConfigBean.getLabel(), fieldConfigBean.getField()));
		}
		fieldsWithMatcher.addAll(getPseudoColumns(personID, locale));
		Collections.sort(fieldsWithMatcher);
		return fieldsWithMatcher;
	}
	
	/**
	 * Gets the pseudo columns allowed to see
	 * @param projectIDs
	 * @param locale
	 * @return
	 */
	private static List<IntegerStringBean> getPseudoColumns(Integer personID, Locale locale) {
		boolean hasAccounting = ColumnFieldsBL.hasAccounting(personID);
		Map<Integer, Boolean> pseudoFieldsVisible = ColumnFieldsBL.getVisisblePseudoFields(personID, hasAccounting);
		List<IntegerStringBean> fieldsWithMatcher = new LinkedList<IntegerStringBean>();
		Boolean watcherFieldsAllowed = pseudoFieldsVisible.get(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.WATCHERS);
		if (watcherFieldsAllowed!=null && watcherFieldsAllowed.booleanValue()) {
			fieldsWithMatcher.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.CONSULTANT_LIST, locale), TReportLayoutBean.PSEUDO_COLUMNS.CONSULTANT_LIST));
			fieldsWithMatcher.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.INFORMANT_LIST, locale), TReportLayoutBean.PSEUDO_COLUMNS.INFORMANT_LIST));
		}
		Boolean ownExpense = pseudoFieldsVisible.get(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.MY_EXPENSES);
		if (ownExpense!=null && ownExpense.booleanValue()) {
			fieldsWithMatcher.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.MY_EXPENSE_TIME, locale), TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_TIME));
			fieldsWithMatcher.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.MY_EXPENSE_COST, locale), TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_COST));
		}
		Boolean viewAllExpenses = pseudoFieldsVisible.get(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.ALL_EXPENSES);
		if (viewAllExpenses!=null && viewAllExpenses.booleanValue()) {
			fieldsWithMatcher.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.TOTAL_EXPENSE_TIME, locale), TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_TIME));
			fieldsWithMatcher.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.TOTAL_EXPENSE_COST, locale), TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_COST));
		}
		Boolean planVisible = pseudoFieldsVisible.get(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.PLAN);
		if (planVisible!=null && planVisible.booleanValue()) {
			fieldsWithMatcher.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.TOTAL_PLANNED_TIME, locale), TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_TIME));
			fieldsWithMatcher.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.TOTAL_PLANNED_COST, locale), TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_COST));
			fieldsWithMatcher.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.REMAINING_PLANNED_TIME, locale), TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_TIME));
			fieldsWithMatcher.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.REMAINING_PLANNED_COST, locale), TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_COST));
		}
		Boolean budgetVisible = pseudoFieldsVisible.get(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.BUDGET);
		if (budgetVisible!=null && budgetVisible) {
			fieldsWithMatcher.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.BUDGET_TIME, locale), TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_TIME));
			fieldsWithMatcher.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.BUDGET_COST, locale), TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_COST));
		}
		return fieldsWithMatcher;
	}
	/**
	 * Populates an "in tree" filter expression
	 * @param fieldExpressionInTreeTO
	 * @param fieldsWithMatcher
	 * @param modifiable
	 * @param personID
	 * @param locale
	 * @param instant
	 * @param withFieldMoment
	 * @param index
	 */
	private static void loadFieldExpressionInTree(FieldExpressionInTreeTO fieldExpressionInTreeTO, Integer[] projectIDs,
			Integer[] ancestorProjectIDs, Integer[] itemTypeIDs, List<IntegerStringBean> fieldsWithMatcher, boolean modifiable, TPersonBean personBean,
			Locale locale, boolean withParameter, boolean withFieldMoment, int index) {
		fieldExpressionInTreeTO.setIndex(index);
		//field moment
		if (withFieldMoment) {
			Integer selectedFieldMoment = fieldExpressionInTreeTO.getFieldMoment();
			if (selectedFieldMoment==null) {
				fieldExpressionInTreeTO.setFieldMoment(Integer.valueOf(TQueryRepositoryBean.FIELD_MOMENT.NEW));
			}
			fieldExpressionInTreeTO.setFieldMomentName(getName(FIELD_MOMENT_NAME, index));
			fieldExpressionInTreeTO.setWithFieldMoment(withFieldMoment);
		}
		//field
		Integer fieldID = fieldExpressionInTreeTO.getField();
		if (fieldID==null && 
				fieldsWithMatcher!=null && !fieldsWithMatcher.isEmpty()) {
			//new field	expression: preselect the first available field
			fieldID = fieldsWithMatcher.get(0).getValue();
			fieldExpressionInTreeTO.setField(fieldID);
		}
		fieldExpressionInTreeTO.setFieldsList(fieldsWithMatcher);
		fieldExpressionInTreeTO.setFieldName(getName(FIELD_NAME, index));
		fieldExpressionInTreeTO.setFieldItemId(getItemId(FIELD_NAME, index));
		//matcher
		List<IntegerStringBean> matcherList = getMatchers(fieldID, withParameter, true, locale);
		fieldExpressionInTreeTO.setMatchersList(matcherList);
		Integer matcherID = fieldExpressionInTreeTO.getSelectedMatcher();
		if (matcherID==null) {
			//new field	expression: preselect the first available matcher:
			//in FieldExpressionInTreeTO no empty matcher is available (like in FieldExpressionSimpleTO)
			if (matcherList!=null && !matcherList.isEmpty()) {
				matcherID = matcherList.get(0).getValue();
				fieldExpressionInTreeTO.setSelectedMatcher(matcherID);
			}
		}
		fieldExpressionInTreeTO.setMatcherName(getName(IN_TREE + MATCHER_RELATION_BASE_NAME, index));
		fieldExpressionInTreeTO.setMatcherItemId(getItemId(IN_TREE + MATCHER_RELATION_BASE_NAME, index));
		//value 		
		setExpressionValue(fieldExpressionInTreeTO, projectIDs, ancestorProjectIDs, itemTypeIDs, IN_TREE + VALUE_BASE_NAME, IN_TREE + VALUE_BASE_ITEM_ID, index, modifiable, personBean, locale);						
		fieldExpressionInTreeTO.setOperationName(getName(OPERATION_NAME, index));
		fieldExpressionInTreeTO.setOperationItemId(getItemId(OPERATION_NAME, index));
		fieldExpressionInTreeTO.setParenthesisOpenName(getName(PARENTHESIS_OPEN_NAME, index));
		fieldExpressionInTreeTO.setParenthesisClosedName(getName(PARENTHESIS_CLOSED_NAME, index));
	}
	
	/**
	 * Set expression value attributes:  needMatcherValue, matcherID, valueRenderer and JsonConfig string
	 * The field, the matcher (if it is the case) and the value object should be already set for FieldExpressionSimpleTO
	 * @param fieldExpressionSimpleTreeTO
	 * @param projectIDs
	 * @param ancestorProjectIDs
	 * @param itemTypeIDs
	 * @param baseName
	 * @param baseItemId
	 * @param index
	 * @param modifiable
	 * @param personID
	 * @param locale
	 */
	private static void setExpressionValue(FieldExpressionSimpleTO fieldExpressionSimpleTreeTO,
			Integer[] projectIDs, Integer[] ancestorProjectIDs, Integer[] itemTypeIDs,  String baseName, String baseItemId, Integer index,
			boolean modifiable, TPersonBean personBean, Locale locale) {
		Integer personID = personBean.getObjectID();
		Integer matcherID = fieldExpressionSimpleTreeTO.getSelectedMatcher();
		fieldExpressionSimpleTreeTO.setValueItemId(getItemId(baseItemId, index));
		if (matcherID!=null) {
			//do not force the matcher to a value if not specified because no matcher means no filtering by that field
			//even if the field is always present (it is set as upper filter field)
			//(it was set already to a value if if was the case)
			boolean needMatcherValue = getNeedMatcherValue(matcherID);
			fieldExpressionSimpleTreeTO.setNeedMatcherValue(needMatcherValue);
			if (needMatcherValue) {
				Integer fieldID = fieldExpressionSimpleTreeTO.getField();
				IMatcherDT matcherDT = null;
				Object possibleValues = null;
				//for field expressions the "withParameter" is false for getting the datasoucre,
				//because only the matcher is parameterized not the value
				//but "initValueIfNull" is true to preselect the first entry if nothing selected
				//do not show the closed entities in field expressions (typically used for release but it can be
				//interpreted also for person (deactivated), deleted custom entries etc.)
				MatcherDatasourceContext matcherDatasourceContext = new MatcherDatasourceContext(projectIDs, ancestorProjectIDs, itemTypeIDs, personBean, locale, false, true, false, false);
				if (fieldID.intValue()>0) {
					IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
					if (fieldTypeRT!=null) {
						matcherDT = fieldTypeRT.processLoadMatcherDT(fieldID);
						possibleValues = fieldTypeRT.getMatcherDataSource(fieldExpressionSimpleTreeTO, matcherDatasourceContext, null);
					}
				} else {
					matcherDT = getPseudoMatcherDT(fieldID);
					possibleValues = getPseudoFieldMatcherDataSource(fieldExpressionSimpleTreeTO, matcherDatasourceContext);
				}
				
				if (matcherDT!=null) {
					matcherDT.setRelation(matcherID);
					fieldExpressionSimpleTreeTO.setValueRenderer(matcherDT.getMatchValueControlClass()); 
					fieldExpressionSimpleTreeTO.setJsonConfig(matcherDT.getMatchValueJsonConfig(fieldID,
							baseName, index, fieldExpressionSimpleTreeTO.getValue(), !modifiable, possibleValues, projectIDs, matcherID, locale, personID));
				}
			}
			
		}
	}
	
	/**
	 * Gets the datasource for pseudo fields (only for lists)
	 * @param matcherValue
	 * @param matcherDatasourceContext
	 * @param parameterCode
	 * @return
	 */
	private static Object getPseudoFieldMatcherDataSource(IMatcherValue matcherValue, MatcherDatasourceContext matcherDatasourceContext) {
		Integer fieldID = matcherValue.getField();
		if (fieldID!=null) {
			String raciRole;
			switch (fieldID.intValue()) {
			case TReportLayoutBean.PSEUDO_COLUMNS.CONSULTANT_LIST:
				raciRole = RaciRole.CONSULTANT;
				break;
			case TReportLayoutBean.PSEUDO_COLUMNS.INFORMANT_LIST:
				raciRole = RaciRole.INFORMANT;
				break;
			default:
				//expense/budget/plan
				return null;
			}
			Integer[] projects = matcherDatasourceContext.getProjectIDs();
			Integer[] ancestorProjects = matcherDatasourceContext.getAncestorProjectIDs();
			if (projects!=null && projects.length>0) {
				List<ILabelBean> watcherList = FilterSelectsListsLoader.getConsultantsInformants(projects, ancestorProjects,
						matcherDatasourceContext.getPersonBean().getObjectID(), false, raciRole, matcherDatasourceContext.getLocale());
				if (matcherDatasourceContext.isInitValueIfNull() && matcherValue!=null && watcherList!=null && !watcherList.isEmpty()) {
					Object value = matcherValue.getValue();
					if (value==null) {
						matcherValue.setValue(new Integer[] {watcherList.get(0).getObjectID()});
					}
				}
				return watcherList;
			}	
		}
		return null;
	}
	
	/**
	 * Whether do we need a matcherValue field: for matcherRelations 
	 * IS_NULL NOT_IS_NULL and LATER_AS_LASTLOGIN we do not need it
	 * @param matcherRelation
	 * @return
	 */
	public static boolean getNeedMatcherValue(Integer matcherRelation) {
		if (matcherRelation==null) {
			return false;
		}
		if (matcherRelation.equals(MatcherContext.PARAMETER)) {
			return false;
		}
		switch(matcherRelation.intValue()) {
		case MatchRelations.IS_NULL:
		case MatchRelations.NOT_IS_NULL:
		case MatchRelations.LATER_AS_LASTLOGIN:
		case MatchRelations.SET:
		case MatchRelations.RESET:
			return false;
		default:
			return true;
		}
	}
	
	/**
	 * Builds the name of the client side controls for submit
	 * @param name
	 * @param suffix
	 * @return
	 */
	private static String getName(String name, Integer suffix) {
		StringBuilder stringBuilder = new StringBuilder();
		return stringBuilder.append(name).append("[").append(suffix).append("]").toString();
	}
	
	/**
	 * Builds the name of the client side controls for submit
	 * @param name
	 * @param suffix
	 * @return
	 */
	private static String getItemId(String name, Integer suffix) {
		StringBuilder stringBuilder = new StringBuilder();
		return stringBuilder.append(name).append(suffix).toString();
	}
	
	/**
	 * Gets the localized operations
	 * @param locale
	 * @return
	 */
	private static List<IntegerStringBean> getLocalizedOperationList(Locale locale) {
		List<IntegerStringBean> operationList = new ArrayList<IntegerStringBean>();
		operationList.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(
						"admin.customize.queryFilter.opt.operation.and", locale), QNode.AND));
		operationList.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(
						"admin.customize.queryFilter.opt.operation.or", locale), QNode.OR));
		operationList.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(
						"admin.customize.queryFilter.opt.operation.notAnd", locale), QNode.NOT_AND));
		operationList.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(
						"admin.customize.queryFilter.opt.operation.notOr", locale), QNode.NOT_OR));
		return operationList;
	} 	
	
	//max number of parenthesis
	private static final int MAX_PARENTHESIS_DEEP = 6;
	
	/**
	 * Generate the parenthesisOpenList
	 * @return
	 */
	private static List<IntegerStringBean> createParenthesisOpenList() {
		return getParenthesisList('(', MAX_PARENTHESIS_DEEP);
	}
	
	/**
	 * Generate the parenthesisClosedList
	 * @return
	 */
	private static List<IntegerStringBean> createParenthesisClosedList() {
		return getParenthesisList(')', MAX_PARENTHESIS_DEEP);
	}
	
	/**
	 * Generate the parenthesis list
	 * @param parenthesis
	 * @param maxDeep
	 * @return
	 */
	private static List<IntegerStringBean> getParenthesisList(char parenthesis, int maxDeep) {
		List<IntegerStringBean> parenthesisList = new ArrayList<IntegerStringBean>();
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < maxDeep; i++) {
			parenthesisList.add(new IntegerStringBean(stringBuffer.toString(), Integer.valueOf(i)));
			stringBuffer.append(parenthesis);
		}
		return parenthesisList;
	}
	
	/**
	 * Prepares the field moment list for notification filters 
	 * @param locale
	 * @return
	 */
	private static List<IntegerStringBean> prepareFieldMomentList(Locale locale) {
		List<IntegerStringBean> fieldMomentList = new ArrayList<IntegerStringBean>();
		fieldMomentList.add
			(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
					"common.opt.fieldMoment.new", locale),
				Integer.valueOf(TQueryRepositoryBean.FIELD_MOMENT.NEW)));
		fieldMomentList.add
			(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
					"common.opt.fieldMoment.old", locale),
					Integer.valueOf(TQueryRepositoryBean.FIELD_MOMENT.OLD)));
		return fieldMomentList;
	}

	/**
	 * Get the matchers corresponding to the field 
	 * @param fieldID
	 * @param withParameter
	 * @param inTree
	 * @param locale
	 * @return
	 */
	public static List<IntegerStringBean> getMatchers(Integer fieldID, boolean withParameter, boolean inTree, Locale locale) {
		List<IntegerStringBean> localizedRelations = new ArrayList<IntegerStringBean>();
		if (fieldID==null) {
			return localizedRelations;
		} else {
			IMatcherDT matcherDT = null;
			if (fieldID>0) {
				IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
				if (fieldTypeRT==null) {
					//for example the Keyword pseudo field
					return localizedRelations;
				}
				matcherDT = fieldTypeRT.processLoadMatcherDT(fieldID);
			} else {
				matcherDT = getPseudoMatcherDT(fieldID);
			}
			if (matcherDT!=null) {
				List<Integer> possibleRelations = matcherDT.getPossibleRelations(withParameter, inTree);
				if (possibleRelations!=null) {
					localizedRelations = LocalizeUtil.getLocalizedList(
							MATCH_RELATION_PREFIX, possibleRelations, locale);
				}
			}
		}
		return localizedRelations;
	}
	
	/**
	 * Gets the pseudo field matcherDTs
	 * @param fieldID
	 * @return
	 */
	private static IMatcherDT getPseudoMatcherDT(Integer fieldID) {
		if (fieldID!=null) {
			switch (fieldID.intValue()) {
			case TReportLayoutBean.PSEUDO_COLUMNS.CONSULTANT_LIST:
			case TReportLayoutBean.PSEUDO_COLUMNS.INFORMANT_LIST:
				 return new SelectMatcherDT(fieldID);
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_TIME:
			case TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_TIME:
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_TIME:
			case TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_TIME:
			case TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_TIME:
				return new AccountingTimeMatcherDT(fieldID);
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_COST:
			case TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_COST:
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_COST:
			case TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_COST:
			case TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_COST:
				return new DoubleMatcherDT(fieldID);
			}
		}
		return null;
	}
	
	/**
	 * Gets the pseudo fields matcher converter
	 * @param fieldID
	 * @return
	 */
	public static MatcherConverter getPseudoFieldMatcherConverter(Integer fieldID) {
		if (fieldID!=null) {
			switch (fieldID.intValue()) {
			case TReportLayoutBean.PSEUDO_COLUMNS.CONSULTANT_LIST:
			case TReportLayoutBean.PSEUDO_COLUMNS.INFORMANT_LIST:
				 return new SelectMatcherConverter();
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_TIME:
			case TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_TIME:
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_TIME:	
			case TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_TIME:
			case TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_TIME:
				return new AccountingTimeMatcherConverter();
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_COST:
			case TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_COST:
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_COST:
			case TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_COST:
			case TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_COST:
				return new DoubleMatcherConverter();
			}
		}
		return null;
	}
	
	/**
	 * Gets the pseudo field matcherDTs
	 * @param fieldID
	 * @return
	 */
	public static IFieldTypeRT getPseudoFieldFieldTypeRT(Integer fieldID) {
		if (fieldID!=null) {
			switch (fieldID.intValue()) {
			case TReportLayoutBean.PSEUDO_COLUMNS.CONSULTANT_LIST:
			case TReportLayoutBean.PSEUDO_COLUMNS.INFORMANT_LIST:
				 return new SystemManagerRT();
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_TIME:
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_COST:
			case TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_TIME:
			case TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_COST:
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_TIME:
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_COST:
			case TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_TIME:
			case TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_COST:
			case TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_TIME:
			case TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_COST:
				return new CustomDoubleRT();
			}
		}
		return null;
	}
	
	/**
	 * Gets the pseudo field matcherDTs
	 * @param fieldID
	 * @return
	 */
	public static Integer getPseudoFieldSystemOption(Integer fieldID) {
		if (fieldID!=null) {
			switch (fieldID.intValue()) {
			case TReportLayoutBean.PSEUDO_COLUMNS.CONSULTANT_LIST:
			case TReportLayoutBean.PSEUDO_COLUMNS.INFORMANT_LIST:
				 return SystemFields.INTEGER_PERSON;
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_TIME:
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_COST:
			case TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_TIME:
			case TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_COST:
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_TIME:
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_COST:
			case TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_TIME:
			case TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_COST:
			case TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_TIME:
			case TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_COST:
				return fieldID;
			}
		}
		return null;
	}
	
	
}
