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

package com.aurel.track.admin.customize.category.filter.tree.design;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;

import com.aurel.track.admin.customize.category.filter.FieldExpressionBL;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.matchers.MatchRelations;
import com.aurel.track.fieldType.runtime.matchers.converter.MatcherConverter;
import com.aurel.track.fieldType.types.FieldTypeManager;

/**
 * Prepares a filter for save by applying the submitted request parameters
 * @author Tamas
 *
 */
public class FilterSubmitHandler {
	
	/**
	 * Configures the FilterUpperTO after submit: the selects are automatically set by to the correct Integer[] values
	 * Only the upper filter expressions need to be converted according to the field type
	 * @param simpleMatcherRelationMap
	 * @param simpleDisplayValueMap
	 * @param modifiable
	 * @param personID
	 * @param locale
	 * @return
	 */
	public static List<FieldExpressionSimpleTO> createFieldExpressionSimpleListAfterSubmit(
			Map<Integer, Integer> simpleMatcherRelationMap,
			Map<String, String> simpleDisplayValueMap, Locale locale) {
		List<FieldExpressionSimpleTO> fieldExpressionSimpleTOList =
			new LinkedList<FieldExpressionSimpleTO>();
		if (simpleMatcherRelationMap!=null) {
			for (Integer fieldID : simpleMatcherRelationMap.keySet()) {
				Integer matcherID = simpleMatcherRelationMap.get(fieldID);
				if (matcherID!=null && matcherID.intValue()!=MatchRelations.NO_MATCHER) {
					//do not save if no matcher is specified. 
					//After removing a field from the filters in the field configuration,
					//to get rid of that field from the already saved filters containing this field 
					//set the matcherID to NO_MATCHER 
					FieldExpressionSimpleTO filterExpressionSimpleTO =
						createFilterExpressionSimpleAfterSubmit(fieldID, fieldID, matcherID, simpleDisplayValueMap, locale);
					fieldExpressionSimpleTOList.add(filterExpressionSimpleTO);
				}
			}
		}
		return fieldExpressionSimpleTOList;
	}
	
	/**
	 * Configures the FilterUpperTO after submit: the selects are automatically set by to the correct Integer[] values
	 * Only the upper filter expressions need to be converted according to the field type
	 * @param simpleMatcherRelationMap
	 * @param simpleDisplayValueMap
	 * @param modifiable
	 * @param personID
	 * @param locale
	 * @return
	 */
	public static List<FieldExpressionSimpleTO> createFieldExpressionSimpleListAfterSubmit(Map<Integer, Integer> fieldMap,
			Map<Integer, Integer> simpleMatcherRelationMap,
			Map<String, String> simpleDisplayValueMap, Locale locale) {
		List<FieldExpressionSimpleTO> fieldExpressionSimpleTOList =
			new LinkedList<FieldExpressionSimpleTO>();
		if (simpleMatcherRelationMap!=null && fieldMap!=null) {
			for (Integer index : simpleMatcherRelationMap.keySet()) {
				Integer matcherID = simpleMatcherRelationMap.get(index);
				Integer fieldID  = fieldMap.get(index);
				if (matcherID!=null && matcherID.intValue()!=MatchRelations.NO_MATCHER) {
					//do not save if no matcher is specified. 
					//After removing a field from the filters in the field configuration,
					//to get rid of that field from the already saved filters containing this field 
					//set the matcherID to NO_MATCHER 
					FieldExpressionSimpleTO filterExpressionSimpleTO =
						createFilterExpressionSimpleAfterSubmit(fieldID, index, matcherID, simpleDisplayValueMap, locale);
					fieldExpressionSimpleTOList.add(filterExpressionSimpleTO);
				}
			}
		}
		return fieldExpressionSimpleTOList;
	}
	
	/**
	 * Prepares a FieldExpressionSimpleTO after submit
	 * @param fieldID
	 * @param matcherID
	 * @param displayStringMap
	 * @param locale
	 * @return
	 */
	private static FieldExpressionSimpleTO createFilterExpressionSimpleAfterSubmit(
			Integer fieldID, Integer index, Integer matcherID, Map<String, String> displayStringMap, Locale locale) {
		FieldExpressionSimpleTO fieldExpressionSimpleTO = new FieldExpressionSimpleTO(); 
		fieldExpressionSimpleTO.setField(fieldID);
		fieldExpressionSimpleTO.setSelectedMatcher(matcherID);
		IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
		if (fieldTypeRT!=null) {
			MatcherConverter matcherConverter = fieldTypeRT.getMatcherConverter();
			Object value = matcherConverter.fromDisplayString(displayStringMap, index, locale, matcherID);
			fieldExpressionSimpleTO.setValue(value);
		}
		return fieldExpressionSimpleTO;
	}
	
	/**
	 * Configures the FilterUpperTO after submit: the selects are automatically set by to the correct Integer[] values
	 * Only the upper filter expressions need to be converted according to the field type
	 * @param matcherMap
	 * @param displayValueMap
	 * @param modifiable
	 * @param personID
	 * @param locale
	 * @return
	 */
	public static List<FieldExpressionInTreeTO> createFieldExpressionInTreeListAfterSubmit(
		Map<String, String> inTreeDisplayValueMap,
		Map<Integer, Integer> inTreeMatcherRelationMap,
		Map<Integer, Integer> fieldMap,
		Map<Integer, Integer> fieldMomentMap,
		Map<Integer, Integer> operationMap,
		Map<Integer, Integer> parenthesisOpenedMap,
		Map<Integer, Integer> parenthesisClosedMap,
		SortedMap<Integer, Integer> fieldExpressionOrderMap,
		Locale locale) {
		List<FieldExpressionInTreeTO> fieldExpressionInTreeList = new LinkedList<FieldExpressionInTreeTO>();
		if (fieldExpressionOrderMap!=null) {
			for (Integer realOrderIndex : fieldExpressionOrderMap.keySet()) {
				Integer index = fieldExpressionOrderMap.get(realOrderIndex);
				Integer fieldID  = fieldMap.get(index);
				if (fieldID!=null) {
					FieldExpressionInTreeTO fieldExpressionInTreeTO = new FieldExpressionInTreeTO();
					fieldExpressionInTreeTO.setParenthesisOpen(parenthesisOpenedMap.get(index));
					fieldExpressionInTreeTO.setSelectedOperation(operationMap.get(index));
					if (fieldMomentMap!=null) {
						fieldExpressionInTreeTO.setFieldMoment(fieldMomentMap.get(index));
					}
					fieldExpressionInTreeTO.setField(fieldID);
					Integer matcherID = inTreeMatcherRelationMap.get(index);
					fieldExpressionInTreeTO.setSelectedMatcher(matcherID);
					MatcherConverter matcherConverter = null;
					if (fieldID>0) {
						IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
						if (fieldTypeRT!=null) {
							matcherConverter = fieldTypeRT.getMatcherConverter();
						}
					} else {
						matcherConverter = FieldExpressionBL.getPseudoFieldMatcherConverter(fieldID);
					}
					if (matcherConverter!=null) {
						fieldExpressionInTreeTO.setValue(matcherConverter.fromDisplayString(inTreeDisplayValueMap, index, locale, matcherID));
					}
					fieldExpressionInTreeTO.setParenthesisClosed(parenthesisClosedMap.get(index));
					fieldExpressionInTreeList.add(fieldExpressionInTreeTO);
				}
			}
		}
		return fieldExpressionInTreeList;
	}
}
