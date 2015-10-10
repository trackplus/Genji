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


package com.aurel.track.fieldType.bulkSetters;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.runtime.base.SelectContext;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.TreeNode;

/**
 * Bulk setter for project picker
 * Differs from the project bulk setter by the fact that the value is Integer[] instead of Integer
 * @author Tamas
 *
 */
public class ProjectPickerBulkSetter extends CustomMultipleSelectBulkSetter {		
	private static final Logger LOGGER = LogManager.getLogger(ProjectPickerBulkSetter.class);
	public ProjectPickerBulkSetter(Integer fieldID) {
		super(fieldID);	
	}

	/**
	 * Gets the name of the jsp fragment which contains 
	 * the control for rendering the bulk value
	 * @return
	 */
	/*public String getSetterValueControlClass() {
		switch (relation) {
		case BulkRelations.SET_TO:
			return BulkValueTemplates.PROJECT_PICKER_BULK_VALUE_TEMPLATE;
		}
		return BulkValueTemplates.NONE_BULK_VALUE_TEMPLATE;		
	}*/
	
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
			return BulkValueTemplates.PROJECT_PICKER_BULK_VALUE_TEMPLATE;
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
		JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.NAME, getName(baseName));		
		if (value!=null) {			
			try {
				Integer[] intValue = (Integer[])value;
				if (intValue!=null) {
					JSONUtility.appendIntegerArrayAsArray(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, intValue);
				}
			} catch (Exception e) {				
			}
		}
        JSONUtility.appendJSONValue(stringBuilder, JSONUtility.JSON_FIELDS.DATA, JSONUtility.getTreeHierarchyJSON((List<TreeNode>)dataSource, true, false));
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.DISABLED, disabled, true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	@Override
	public Object fromDisplayString(Map<String, String> displayStringMap, Locale locale) {
		Integer[] actualValuesArr = null;
		if (displayStringMap == null) {
			return actualValuesArr;
		}
		switch (getRelation()) {
		case BulkRelations.SET_TO:
		case BulkRelations.ADD_ITEMS:
		case BulkRelations.REMOVE_ITEMS:
			for (String key : displayStringMap.keySet()) {
				if (key.startsWith(getKeyPrefix())) { 
					String displayStringMapValue = displayStringMap.get(key);
					if (displayStringMapValue!=null) {
						String[] multipleValues = displayStringMapValue.split(","); 
						if(multipleValues!=null && multipleValues.length>0){
							actualValuesArr = GeneralUtils.createIntegerArrFromCollection(
									GeneralUtils.createIntegerListFromStringArr(multipleValues));
						}
					}
				}
			}
		}
		return actualValuesArr;
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
		if (getRelation()==BulkRelations.SET_NULL) {
			workItemBean.setAttribute(fieldID, parameterCode, null);
			return null;
		}
		Integer[] selectedValues = (Integer[])value;
		if (getRelation()==BulkRelations.SET_TO) {
			workItemBean.setAttribute(fieldID, selectedValues);
			return null;
		}
		Object originalValue = workItemBean.getAttribute(fieldID, parameterCode);
		Object[] originalSelections = null;
		if (originalValue!=null) {
			try {
				//multiple values are loaded in the workItem as Object[], not as Integer[] !!! 
				originalSelections = (Object[])originalValue;
			} catch (Exception e) {
				LOGGER.debug("Getting the original object array value for " + value +  " failed with " + e.getMessage(), e);
			}
		}
		Set<Integer> originalSet = new HashSet<Integer>();
		if (originalSelections!=null && originalSelections.length>0) {
			for (int i = 0; i < originalSelections.length; i++) {
				try {
					originalSet.add((Integer)originalSelections[i]);
				} catch (Exception e) {
					LOGGER.debug("Transforming the original object value " + originalSelections[i] +  " to Integer failed with " + e.getMessage(), e);
				}
			}
		}
		Set<Integer> bulkSelectionsSet = GeneralUtils.createSetFromIntegerArr(selectedValues);
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
