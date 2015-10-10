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

package com.aurel.track.admin.customize.category.filter.tree.design;

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

import com.aurel.track.admin.customize.category.filter.FieldExpressionBL;
import com.aurel.track.admin.customize.category.filter.FilterBL;
import com.aurel.track.admin.customize.category.filter.QNode;
import com.aurel.track.admin.customize.category.filter.QNodeExpression;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.util.GeneralUtils;

public class FilterUpperFromQNodeTransformer {

	static final Logger LOGGER = LogManager.getLogger(FilterUpperFromQNodeTransformer.class);
		
	/**
	 * Gets the value form the qNodeExpression
	 * @param qNodeExpression
	 * @param directProcessFields
	 * @param presentUpperFields out parameter to gather the present upper fields
	 * @param directValuesMap map for direct values (those with hard coded matcher)
	 * @param presentUpperCustomSelectFieldsMap gather the custom simple selects on the upper part  
	 * @param matcherExpressionMap gather the simple field expressions (matcher is not hard coded) 
	 */
	private static void getValueFromQNodeExpression(QNodeExpression qNodeExpression, Set<Integer> directProcessFields,
			Set<Integer> presentUpperFields, Map<Integer, List<Object>> directValuesMap, 
			Map<Integer, Integer> presentUpperCustomSelectFieldsMap, Map<Integer, QNodeExpression> matcherExpressionMap) {
		Integer fieldID = qNodeExpression.getField();
		boolean isCustomSelect = FieldBL.isCustomSelect(fieldID);
		presentUpperFields.add(fieldID);
		if (directProcessFields.contains(fieldID) || isCustomSelect) {
			//hard coded matchers: system/custom simple selects, miscellaneous filtering attributes
			//List of Object because simple selects are Integer[] and miscellaneous values are Strings  
			//(typically valid Integer strings except the keyword) because the haven't fieldType based matchers 
			List<Object> fieldValuesList = directValuesMap.get(fieldID);
			if (fieldValuesList==null) {
				fieldValuesList=new LinkedList<Object>();
				directValuesMap.put(fieldID, fieldValuesList);
			}
			fieldValuesList.add(qNodeExpression.getValue());
			if (isCustomSelect) {
				if (!presentUpperCustomSelectFieldsMap.keySet().contains(fieldID)) {
					presentUpperCustomSelectFieldsMap.put(fieldID, FieldBL.getSystemOptionType(fieldID));
				}
			}
		} else {
			//simple field expressions: the matcher is not hard coded, it should be dealt with fieldType specific
			matcherExpressionMap.put(fieldID, qNodeExpression);
		}
	}
	
	/**
	 * Loads the FilterUpperTO from the stored query  
	 * By convention the first child of the root AND node will be the tree for FilterUpperTO
	 * @param qNode
	 * @param modifiable
	 * @param personBean
	 * @param locale
	 * @param excute whether the filter is edited or executed
	 * @return
	 */
	public static FilterUpperTO getFilterSelectsFromTree(QNode qNode, boolean modifiable, boolean withParameter, TPersonBean personBean, Locale locale, boolean excute) {
		FilterUpperTO filterUpperTO = new FilterUpperTO();
		//all fields with filter field set
		List<Integer> definedFilterFields = FilterBL.getUpperFilterFields();
		filterUpperTO.setUpperFields(definedFilterFields);
		Set<Integer> directProcesFields = FilterBL.getDirectProcessFields();
		//the fields with values in the upper part should be forced to be 
		//present even if they are not set any more as filter fields
		Set<Integer> presentUpperFields = new HashSet<Integer>();
		Map<Integer, Integer> presentUpperCustomSelectFieldsMap = new HashMap<Integer, Integer>();
		if (qNode==null || qNode.getChildren()==null) {
			return filterUpperTO;
		}
		//direct values map for hardcoded matchers
		Map<Integer, List<Object>> directValuesMap = new HashMap<Integer, List<Object>>();
		//the first child of the AND root  
		QNode qNodeFilterSelectsAND = qNode.getChildren().get(0);
		//each defined dropdown has is an AND branch
		List<QNode> andBranches = qNodeFilterSelectsAND.getChildren();
		if (andBranches==null) {
			return filterUpperTO;
		}
		/**
		 * gather the present fields and the values
		 */
		Map<Integer, QNodeExpression> matcherExpressionMap = new HashMap<Integer, QNodeExpression>();
		Iterator<QNode> itrAndBranches = andBranches.iterator();
		while (itrAndBranches.hasNext()) {
			QNode qNodeOR = itrAndBranches.next();
			if (qNodeOR.getType()==QNode.EXP) {
				QNodeExpression qNodeChild = (QNodeExpression) qNodeOR;
				getValueFromQNodeExpression(qNodeChild, directProcesFields,
						presentUpperFields, directValuesMap, presentUpperCustomSelectFieldsMap, matcherExpressionMap);
			} else {
				//each selection in a drop down is an OR branch
				List<QNode> orBranches = qNodeOR.getChildren();
				if (orBranches!=null) {
					Iterator<QNode> itrOrBranches = orBranches.iterator();
					while (itrOrBranches.hasNext()) {
						QNodeExpression qNodeChild = (QNodeExpression) itrOrBranches.next();
						getValueFromQNodeExpression(qNodeChild, directProcesFields, presentUpperFields, directValuesMap, presentUpperCustomSelectFieldsMap, matcherExpressionMap);						
					}
				}
			}
		} 
		/*
		 * set the selected system selects
		 */
		List<Integer> systemFields = new LinkedList<Integer>();
		systemFields.add(SystemFields.INTEGER_PROJECT);
		systemFields.add(SystemFields.INTEGER_RELEASESCHEDULED);
		systemFields.add(SystemFields.INTEGER_MANAGER);
		systemFields.add(SystemFields.INTEGER_RESPONSIBLE);
		systemFields.add(SystemFields.INTEGER_ORIGINATOR);
		systemFields.add(SystemFields.INTEGER_CHANGEDBY);
		systemFields.add(SystemFields.INTEGER_STATE);
		systemFields.add(SystemFields.INTEGER_ISSUETYPE);
		systemFields.add(SystemFields.INTEGER_PRIORITY);
		systemFields.add(SystemFields.INTEGER_SEVERITY);
		systemFields.add(FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID);
		for (Integer fieldID : systemFields) {
			filterUpperTO.setSelectedValuesForField(fieldID, createIntegerArrFromCollection(
					(List)directValuesMap.get(fieldID)));
		}
		//release type selector
		List<Object> releaseTypeSelectorList = directValuesMap.get(
				Integer.valueOf(FilterUpperTO.PSEUDO_FIELDS.RELEASE_TYPE_SELECTOR));
		//the default value: which was active before implementing the release type selector
		Integer releaseTypeSelector = Integer.valueOf(FilterUpperTO.RELEASE_SELECTOR.SCHEDULED);
		if (releaseTypeSelectorList!=null && !releaseTypeSelectorList.isEmpty()) {
			try {
				releaseTypeSelector = (Integer)releaseTypeSelectorList.get(0);
			} catch(Exception e) {
				LOGGER.warn("Parsing the releaseSelector value failed with " + e.getMessage(), e);
			}
		}
		filterUpperTO.setReleaseTypeSelector(releaseTypeSelector);
		
		//show closed releases flag
		List<Object> showClosedReleaseList = directValuesMap.get(
				Integer.valueOf(FilterUpperTO.PSEUDO_FIELDS.SHOW_CLOSED_RELEASES));
		//the default value: which was active before implementing the release type selector
		boolean showClosedReleases = false;
		if (showClosedReleaseList!=null && !showClosedReleaseList.isEmpty()) {
			Object showClosedRelObj = showClosedReleaseList.get(0);
			if (showClosedRelObj!=null) {
				try {
					showClosedReleases = 
							((Boolean)showClosedRelObj).booleanValue();
				} catch(Exception e) {
					LOGGER.warn("Parsing the showClosedReleases value failed with " + e.getMessage(), e);
				}
			}
		}
		filterUpperTO.setShowClosedReleases(showClosedReleases);
		//get the consultantInformantSelector
		List<Object> consultantInformantSelectorList = directValuesMap.get(
				Integer.valueOf(FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_OR_INFORMANT_SELECTOR));
		Integer selector = Integer.valueOf(FilterUpperTO.CONSINF_SELECTOR.CONSULTANT_OR_INFORMANT);
		if (consultantInformantSelectorList!=null && !consultantInformantSelectorList.isEmpty()) {
			try {
				selector = (Integer)consultantInformantSelectorList.get(0);
			} catch (Exception e) {
				LOGGER.warn("Parsing the consultantInformantSelector value failed with " + e.getMessage(), e);
			}
		}
		filterUpperTO.setWatcherSelector(selector);
		
		//get the keyword if set
		List<Object> keywordList = directValuesMap.get(
				Integer.valueOf(FilterUpperTO.PSEUDO_FIELDS.KEYWORD_FIELD_ID));
		if (keywordList!=null && !keywordList.isEmpty()) {
			filterUpperTO.setKeyword((String)keywordList.get(0));
		}
		//get the archived if set
		try {
			List<Object> archivedList = directValuesMap.get(
					Integer.valueOf(FilterUpperTO.PSEUDO_FIELDS.ARCHIVED_FIELD_ID));
			if (archivedList!=null && !archivedList.isEmpty()) {
				filterUpperTO.setArchived((Integer)archivedList.get(0));
			}
		} catch (Exception e) {
			LOGGER.warn("Setting the archived flag failed with " + e.getMessage(), e);
		}
		//get the deleted if set
		try {
			List<Object> deletedList = directValuesMap.get(
					Integer.valueOf(FilterUpperTO.PSEUDO_FIELDS.DELETED_FIELD_ID));
			if (deletedList!=null && !deletedList.isEmpty()) {
				filterUpperTO.setDeleted((Integer)deletedList.get(0));
			}
		} catch (Exception e) {
			LOGGER.warn("Setting the deleted flag failed with " + e.getMessage(), e);
		}
		//get the linkTypeFilterSuperset if set
		try {
			List<Object> linkTypeFilterSuperset = directValuesMap.get(
					Integer.valueOf(FilterUpperTO.PSEUDO_FIELDS.LINKTYPE_FILTER_SUPERSET));
			if (linkTypeFilterSuperset!=null && !linkTypeFilterSuperset.isEmpty()) {
				filterUpperTO.setLinkTypeFilterSuperset((String)linkTypeFilterSuperset.get(0));
			}
		} catch (Exception e) {
			LOGGER.warn("Setting the link type flag failed with " + e.getMessage(), e);
		}
		
		//present (not null valued) upper custom selects
		if (!presentUpperCustomSelectFieldsMap.isEmpty()) {
			Map<Integer, Integer[]> selectedCustomSelects = new HashMap<Integer, Integer[]>();
			filterUpperTO.setSelectedCustomSelects(selectedCustomSelects);
			for (Integer fieldID : presentUpperCustomSelectFieldsMap.keySet()) {
				List<Integer[]> selectedEntries = (List)directValuesMap.get(fieldID);
				selectedCustomSelects.put(fieldID, createIntegerArrFromCollection(selectedEntries));
			}
		}
		//remain only the not any more filter fields which has a value in the filter
		presentUpperFields.removeAll(definedFilterFields);
		//add this "used to be" filter fields with values as they would be filter fields
		definedFilterFields.addAll(presentUpperFields);
		List<FieldExpressionSimpleTO> fieldExpressionSimpleTOList =
			new LinkedList<FieldExpressionSimpleTO>();	
		filterUpperTO.setCustomSelectSimpleFields(presentUpperCustomSelectFieldsMap);
		List<QNodeExpression> nonSelectQNodeExpressions = new LinkedList<QNodeExpression>();
		Set<Integer> nonSelectFieldIDs = new HashSet<Integer>();
		for (Integer fieldID : definedFilterFields) {
			if (!directProcesFields.contains(fieldID) && !presentUpperCustomSelectFieldsMap.keySet().contains(fieldID)) {
				boolean isCustomSelect = FieldBL.isCustomSelect(fieldID);
				IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
				if (fieldTypeRT!=null) {
					if (isCustomSelect) {
						//defined but not present (no value) custom select simple: add only now to customSelectSimpleFields
						filterUpperTO.getCustomSelectSimpleFields().put(fieldID, FieldBL.getSystemOptionType(fieldID));
					} else {
						//gather non simple select upper field expressions
						QNodeExpression qNodeExpression = matcherExpressionMap.get(fieldID);
						if (qNodeExpression==null && !excute) {
							//although appear in filter but no value was set
							qNodeExpression = new QNodeExpression();
							qNodeExpression.setField(fieldID);
						}
						if (qNodeExpression!=null) {
							nonSelectQNodeExpressions.add(qNodeExpression);
							nonSelectFieldIDs.add(fieldID);
						}
					}
				}
			}
		}
		if (!nonSelectQNodeExpressions.isEmpty()) {
			Map<Integer, String> fieldLabelsMap = FieldRuntimeBL.getLocalizedDefaultFieldLabels(
					GeneralUtils.createListFromSet(nonSelectFieldIDs), locale);
			for (QNodeExpression qNodeExpression : nonSelectQNodeExpressions) {
				Integer fieldID = qNodeExpression.getField();
				Integer matcherID = qNodeExpression.getMatcherID();
				Object value = qNodeExpression.getValue();
				FieldExpressionSimpleTO fieldExpressionSimpleTO =
					FieldExpressionBL.loadFilterExpressionSimple(fieldID, filterUpperTO.getSelectedProjects(), filterUpperTO.getSelectedIssueTypes(), 
							matcherID, modifiable, withParameter, personBean, locale, fieldLabelsMap, value);
				fieldExpressionSimpleTOList.add(fieldExpressionSimpleTO);
			}
		}
		filterUpperTO.setFieldExpressionSimpleList(fieldExpressionSimpleTOList);
		filterUpperTO.setUpperFields(definedFilterFields);
		return filterUpperTO;
	}
	
	/**
	 * Creates an array of Integers from an Set of Integers
	 * @param arrIDs
	 * @return
	 */
	public static Integer[] createIntegerArrFromCollection(List<Integer[]> integerArrList) {
		if (integerArrList == null) {
			return null;
		}
		List<Integer> integerList = new LinkedList<Integer>();
		Iterator<Integer[]> iterator = integerArrList.iterator();
		while (iterator.hasNext()) {
			Integer[] integerArr = iterator.next();
			if (integerArr!=null && integerArr.length>0) {
				for (Integer integerValue : integerArr) {
					if (integerValue!=null) {
						integerList.add(integerValue);
					}
				}
			}
		}
		return GeneralUtils.createIntegerArrFromCollection(integerList);
	}

	/**
	 * Transform an entitySet to a string array
	 * @param stringList
	 * @return
	 */
	private static Integer[] getIntegerArrFromStringList(List<String> stringList) {
		if (stringList==null || stringList.isEmpty()) {
			return null;
		} else {
			Integer[] entities = new Integer[stringList.size()];
			Iterator<String> iterator = stringList.iterator();
			int i=0;
			while (iterator.hasNext()) {
				String entityID = null;
				try {
					entityID = iterator.next();
					entities[i++]=Integer.valueOf(entityID);
				} catch (Exception e) {
					LOGGER.warn("Converting the " + entityID + " to Integer failed with" + e.getMessage(), e);
				}
			}
			return entities;
		}
	}

}

