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


package com.aurel.track.fieldType.bulkSetters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.aurel.track.beans.TPersonBean;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.runtime.base.SelectContext;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.GeneralUtils;

/**
 * Bulk setter for custom select fields
 * Differs from the system select by the fact that the value is Integer[] instead of Integer 
 * @author Tamas
 *
 */
public class CustomMultipleSelectBulkSetter extends CustomSingleSelectBulkSetter {
	private static final Logger LOGGER = LogManager.getLogger(CustomMultipleSelectBulkSetter.class);
	private static String LINK_CHAR = "_";
	
	public CustomMultipleSelectBulkSetter(Integer fieldID) {
		super(fieldID);
	}

	@Override
	public List <Integer> getPossibleRelations(boolean required) {
		List <Integer> relations = new ArrayList<Integer>();
		relations.add(Integer.valueOf(BulkRelations.SET_TO));
		relations.add(Integer.valueOf(BulkRelations.ADD_ITEMS));
		relations.add(Integer.valueOf(BulkRelations.REMOVE_ITEMS));
		if (!required) {
			relations.add(Integer.valueOf(BulkRelations.SET_NULL));
		}
		return relations;
	}
	
	/**
	 * Gets the name of the jsp fragment which contains 
	 * the control for rendering the bulk value
	 * @return
	 */
	@Override
	public String getSetterValueControlClass() {
		switch (relation) {
		case BulkRelations.SET_TO:
		case BulkRelations.ADD_ITEMS:
		case BulkRelations.REMOVE_ITEMS:
			return BulkValueTemplates.MULTIPLE_SELECT_BULK_VALUE_TEMPLATE;
		}
		return BulkValueTemplates.NONE_BULK_VALUE_TEMPLATE;
	}
	
	/**
	 * The JSON configuration object for configuring the js control(s) containing the value
	 * @param baseName: the name of the control: important by submit
	 * @param value: the value to be set by rendering (first time or after a submit)
	 * @param dataSource: defined only for lists (list for global lists, map for context dependent lists)
	 * @param labelMap: defined only for context (project/issuType) dependent lists
	 * @param disabled: whether the control is disabled
     * @param personBean
	 * @param locale
	 * @return
	 */
	@Override
	public String getSetterValueJsonConfig(String baseName, Object value,
		Object dataSource, Map<Integer, String> labelMap, boolean disabled, TPersonBean personBean, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder("{");
		//name of the main panel (to enable/disable)
		JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.NAME, getName(baseName));
		Map<Integer, List<ILabelBean>> dataSourceMap = (Map<Integer, List<ILabelBean>>)dataSource;
		if (dataSourceMap!=null) {
			boolean allIssuesFromTheSameProject = dataSourceMap.keySet().size()==1;
			Map<Integer, Integer[]> valueMap = (Map<Integer, Integer[]>)value;
			JSONUtility.appendFieldName(stringBuilder, "projectJson").append(":[");
			for (Iterator<Integer> itrList = dataSourceMap.keySet().iterator(); itrList.hasNext();) {
				Integer listID = itrList.next();
				List<ILabelBean> listDataSource = dataSourceMap.get(listID);
				stringBuilder.append("{");
				if (labelMap!=null && !allIssuesFromTheSameProject) {
					JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.LABEL, labelMap.get(listID));
				}
				JSONUtility.appendILabelBeanList(stringBuilder, JSONUtility.JSON_FIELDS.DATA_SOURCE, listDataSource);
				Integer[] listValues = null;
				if (valueMap!=null) {
					listValues = valueMap.get(listID);
					if (listValues!=null) {
						JSONUtility.appendIntegerArrayAsArray(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, listValues);
					}
				}
				JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.NAME, getNameWithMergedKey(baseName, listID), true);
				stringBuilder.append("}");
				if (itrList.hasNext()) {
					stringBuilder.append(",");
				}
			}
			stringBuilder.append("],");
		}
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.DISABLED, disabled, true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	@Override
	public Object fromDisplayString(Map<String, String> displayStringMap, Locale locale) {
		SortedMap<Integer, Integer[]> actualValuesMap = new TreeMap<Integer, Integer[]>();
		if (displayStringMap == null) {
			return actualValuesMap;
		}
		switch (getRelation()) {
		case BulkRelations.SET_TO:
		case BulkRelations.ADD_ITEMS:
		case BulkRelations.REMOVE_ITEMS:
			for (String key : displayStringMap.keySet()) {
				if (key.startsWith(getKeyPrefix())) { 
					String displayStringMapValue = displayStringMap.get(key);
					if (displayStringMapValue!=null) {
						Integer listID = null;
						String[] tokens = key.split(LINK_CHAR);
						if (tokens.length>1) {
							if (tokens[1]!=null && !"".equals(tokens[1])) {
								listID = Integer.valueOf(tokens[1]);
							}
							String[] multipleValues = displayStringMapValue.split(","); 
							if(multipleValues!=null&&multipleValues.length>0){
								actualValuesMap.put(listID, GeneralUtils.createIntegerArrFromCollection(
										GeneralUtils.createIntegerListFromStringArr(multipleValues)));
							}
						}
					}
				}
			}
		}
		return actualValuesMap;
	}
	
	/**
	 * Sets the workItemBean's attribute depending on the value and bulkRelation
	 * @param workItemBean
	 * @param fieldID
	 * @param parameterCode
	 * @param bulkTranformContext
	 * @param selectContext
	 * @param value	
	 * @return ErrorData if an error is found
	 */
	@Override
	public ErrorData setWorkItemAttribute(TWorkItemBean workItemBean,
			Integer fieldID, Integer parameterCode, 
			BulkTranformContext bulkTranformContext, 
			SelectContext selectContext, Object value) {
		if (getRelation()==BulkRelations.SET_NULL || getRelation()==BulkRelations.SET_TO) {
			return super.setWorkItemAttribute(workItemBean, fieldID, parameterCode, bulkTranformContext, selectContext, value);
		}
		Object originalValue = workItemBean.getAttribute(fieldID, parameterCode);
		Object[] originalSelections = null;
		if (originalValue!=null) {
			try {
				//multiple values are loaded in the workItem as Object[], not as Integer[] !!! 
				originalSelections = (Object[])originalValue;
			} catch (Exception e) {
				LOGGER.info("Getting the original object array value for " + value +  " failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		Set<Integer> originalSet = new HashSet<Integer>();
		if (originalSelections!=null && originalSelections.length>0) {
			for (int i = 0; i < originalSelections.length; i++) {
				try {
					originalSet.add((Integer)originalSelections[i]);
				} catch (Exception e) {
					LOGGER.info("Transforming the original object value " + originalSelections[i] +  " to Integer failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
		}
		Integer[] newValue = null;
		SortedMap<Integer, Integer[]> valueMap = (SortedMap<Integer, Integer[]>)value;
		Map<Integer, Map<Integer, Map<Integer, Integer>>> fieldToProjectToIssueTypeToListMap =
				bulkTranformContext.getFieldToProjectToIssueTypeToListMap();
		if (fieldToProjectToIssueTypeToListMap!=null) {
			Map<Integer, Map<Integer, Integer>> projectToIssueTypeToListMap =  fieldToProjectToIssueTypeToListMap.get(fieldID);
			if (projectToIssueTypeToListMap!=null) {
				Integer projectID = workItemBean.getProjectID();
				Map<Integer, Integer> issueTypeToListMap = projectToIssueTypeToListMap.get(projectID);
				if (issueTypeToListMap!=null) {
					Integer issueTypeID = workItemBean.getListTypeID();
					Integer listID =  issueTypeToListMap.get(issueTypeID);
					if (listID!=null) {
						newValue = valueMap.get(listID);
					}
				}
			}
		}
		Set<Integer> bulkSelectionsSet = GeneralUtils.createSetFromIntegerArr(newValue);
		switch (getRelation()) {
			case BulkRelations.ADD_ITEMS:
				originalSet.addAll(bulkSelectionsSet);
				workItemBean.setAttribute(fieldID, parameterCode, GeneralUtils.createIntegerArrFromCollection(originalSet));
				break;
			case BulkRelations.REMOVE_ITEMS:
				originalSet.removeAll(bulkSelectionsSet);
				workItemBean.setAttribute(fieldID, parameterCode, GeneralUtils.createIntegerArrFromCollection(originalSet));
				break;
			default:
				break;
		}
		return null;
	}
}
