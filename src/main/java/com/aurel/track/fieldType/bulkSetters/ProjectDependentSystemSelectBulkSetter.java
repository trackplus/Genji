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

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.aurel.track.beans.TPersonBean;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.SelectContext;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.massOperation.MassOperationBL;
import com.aurel.track.json.JSONUtility;

/**
 * Bulk setter for project dependent system select fields: release noticed, release scheduled
 * Differs from the custom select by the fact that the value is Integer instead of Integer[]
 * @author Tamas
 *
 */
public class ProjectDependentSystemSelectBulkSetter extends SystemSelectBulkSetter {
	private static final Logger LOGGER = LogManager.getLogger(ProjectDependentSystemSelectBulkSetter.class);
	private static String LINK_CHAR = "_";
	public ProjectDependentSystemSelectBulkSetter(Integer fieldID) {
		super(fieldID, false);	
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
			return BulkValueTemplates.PROJECT_DEPENDENT_SELECT_BULK_VALUE_TEMPLATE;
		}
		return BulkValueTemplates.NONE_BULK_VALUE_TEMPLATE;
	}
	
	/**
	 * Builds the name of the client side controls for submit
	 * @param baseName
	 * @param fieldID
	 * @return
	 */
	protected static String getProjectSpecificName(String baseName, Integer fieldID, Integer projectID) {
		StringBuilder stringBuilder = new StringBuilder();
		return stringBuilder.append(baseName).append(".").append(encodeProjectSpecificKey(fieldID, projectID)).toString();
	}
	
	private static String encodeProjectSpecificKey(Integer fieldID, Integer projectID) {
		StringBuilder stringBuilder = new StringBuilder();
		return stringBuilder.append(MassOperationBL.getKeyPrefix(fieldID)).append(LINK_CHAR).append(projectID).toString();	
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
		Map<Integer, Object> dataSourceMap = (Map<Integer, Object>) dataSource;
		Map<Integer, Integer> projectSpecificValueMap = (Map<Integer, Integer>)value;
		Map<Integer, List<ILabelBean>> projectSpecificDataSourceMap = (Map<Integer, List<ILabelBean>>)dataSource;
		JSONUtility.appendFieldName(stringBuilder, "projectJson").append(":[");
		//stringBuilder.append("projectJson:[");
		for (Iterator<Integer> iterator = dataSourceMap.keySet().iterator(); iterator.hasNext();) {
			Integer projectID = iterator.next();
			stringBuilder.append("{");
			Integer projectSpecificValue = projectSpecificValueMap.get(projectID);
			if (projectSpecificValue!=null) {
				JSONUtility.appendIntegerValue(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, projectSpecificValue);
			}
			if (labelMap!=null) {
				JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.LABEL, labelMap.get(projectID));
			}
			JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.NAME, getProjectSpecificName(baseName, fieldID, projectID));
			JSONUtility.appendILabelBeanList(stringBuilder, JSONUtility.JSON_FIELDS.DATA_SOURCE, projectSpecificDataSourceMap.get(projectID), true);
			stringBuilder.append("}");
			if (iterator.hasNext()) {
				stringBuilder.append(",");
			}
		}
		stringBuilder.append("],");
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.DISABLED, disabled, true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}	

	@Override
	public Object fromDisplayString(Map<String, String> displayStringMap, Locale locale) {
		SortedMap<Integer, Integer> actualValuesMap = new TreeMap<Integer, Integer>();
		if (displayStringMap == null) {
			return actualValuesMap;
		}   
		switch (getRelation()) {
		case BulkRelations.SET_TO:
			for (String key : displayStringMap.keySet()) {
				if (key.startsWith(getKeyPrefix())) {
					String displayStringMapValue = displayStringMap.get(key);
					if (displayStringMapValue!=null) {
						Integer projectID = null;
						String[] tokens = key.split(LINK_CHAR);
						if (tokens.length>1) {
							if (tokens[1]!=null && !"".equals(tokens[1])) {
								projectID = Integer.valueOf(tokens[1]);
							}
							Integer intValue = null;
							try {
								intValue = Integer.valueOf(displayStringMapValue);
								actualValuesMap.put(projectID, intValue);
							} catch (Exception e) {
								LOGGER.warn("Converting the " + displayStringMapValue + " for fieldID " + getFieldID() +
									" and project " + projectID +  " to Integer from display string failed with " + e.getMessage(), e);
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
		if (getRelation()==BulkRelations.SET_NULL) {
			workItemBean.setAttribute(fieldID, parameterCode, null);
			return null;
		}
		Map<Integer, Integer> newProjectDependentMap = null;
		try {
			newProjectDependentMap = (Map<Integer, Integer>)value;
		} catch (Exception e) {
			LOGGER.debug("Getting the map value for " + value +  " failed with " + e.getMessage(), e);
		} 	
		switch (getRelation()) {
		case BulkRelations.SET_TO: 
			//change only if the target value not equals the actual value
			Object oldValue = workItemBean.getAttribute(fieldID);
			Integer newProjectValue = null;
			try {
				newProjectValue = (Integer)workItemBean.getAttribute(SystemFields.INTEGER_PROJECT);
			} catch (Exception e) {
				LOGGER.debug("Getting the new project value for failed with " + e.getMessage(), e);
			}
			Integer newValue = null; 
			if (newProjectValue!=null && newProjectDependentMap!=null) {
				newValue = newProjectDependentMap.get(newProjectValue);
			}
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
			if (oldValue==null || fieldTypeRT.valueModified(newValue, oldValue)) {
				workItemBean.setAttribute(fieldID, parameterCode, newValue);
				return null;
			}
			break;
		}
		return null;
	}
	
}
