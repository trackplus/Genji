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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.aurel.track.beans.TPersonBean;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.runtime.base.CustomCompositeBaseRT;
import com.aurel.track.fieldType.runtime.base.SelectContext;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.massOperation.MassOperationBL;
import com.aurel.track.json.JSONUtility;

/**
 * Bulk setter for composite select fields 
 * @author Tamas
 *
 */
public class CompositeSelectBulkSetter extends AbstractBulkSetter {
	private static final Logger LOGGER = LogManager.getLogger(CompositeSelectBulkSetter.class);
	
	private static String LINK_CHAR = "_";
	
	public CompositeSelectBulkSetter(Integer fieldID) {
		super(fieldID);
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
			return BulkValueTemplates.COMPOSITE_SELECT_BULK_VALUE_TEMPLATE;
		}
		return BulkValueTemplates.NONE_BULK_VALUE_TEMPLATE;
	}
	
	/**
	 * Builds the name of the client side controls for submit
	 * @param baseName
	 * @param index
	 * @return
	 */
	protected String getNameWithMergedKey(String baseName, Integer fieldID, Integer listID, Integer parameterCode) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(baseName).append(".").append(encodeListSpecificKey(listID, parameterCode));
		return stringBuilder.toString();
	}
	
	/**
	 * Builds the name of the client side controls for submit
	 * @param baseName
	 * @param index
	 * @return
	 */
	protected String getItemIdWithMergedKey(String baseName, Integer fieldID, Integer listID, Integer parameterCode) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(baseName).append(encodeListSpecificKey(listID, parameterCode));
		return stringBuilder.toString();
	}
	
	protected String encodeListSpecificKey(Integer listID, Integer parameterCode) {
		//the key should start with string otherwise it is not set by struts!!! (bug?)
		StringBuilder stringBuilder = new StringBuilder(getKeyPrefix());
		stringBuilder.append(LINK_CHAR);
		stringBuilder.append(listID);
		stringBuilder.append(LINK_CHAR);
		stringBuilder.append(parameterCode);
		return stringBuilder.toString();
	}
	
	/**
	 * The JSON configuration object for configuring the js control(s) containing the value
	 * @param baseName: the name of the control: important by submit
	 * @param value: the value to be set by rendering (first time or after a submit)
	 * @param dataSource: defined only for lists (list for global lists, map for context dependent lists)
	 * @param labelMap: defined only for context (project/issuType) dependent lists
	 * @param disabled: whether the control is disabled
	 * @param locale
	 * @return
	 */
	public String getJsonValuesForList(String baseName, String baseItemID, Object value,
		Object dataSource, Integer listID) {
		Integer fieldID = getFieldID();
		StringBuilder stringBuilder = new StringBuilder();
		CustomCompositeBaseRT compositeBaseRT = (CustomCompositeBaseRT)FieldTypeManager.getFieldTypeRT(fieldID);
		if (compositeBaseRT!=null) {
			Map<Integer, SortedMap<Integer, List<ILabelBean>>> dataSourceMap = (Map<Integer, SortedMap<Integer, List<ILabelBean>>>)dataSource;
			if (dataSourceMap!=null) {
				Map<Integer, SortedMap<Integer, Integer[]>> valueMap = (Map<Integer, SortedMap<Integer, Integer[]>>)value;
				stringBuilder.append("[");	
					SortedMap<Integer, List<ILabelBean>> compositeListDataSource = dataSourceMap.get(listID);
					if (compositeListDataSource!=null) {
						SortedMap<Integer, Integer[]> compositeListValue = null;
						if (valueMap!=null) {
							compositeListValue = valueMap.get(listID);
						}
						for (Iterator<Integer> itrPart = compositeListDataSource.keySet().iterator(); itrPart.hasNext();) {
							Integer parameterCode = itrPart.next();
							List<ILabelBean> partDataSource = compositeListDataSource.get(parameterCode);
							stringBuilder.append("{");
							JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.NAME,
									getNameWithMergedKey(baseName, fieldID, listID, parameterCode));
							JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ITEMID,
									getItemIdWithMergedKey(baseItemID, fieldID, listID, parameterCode));
							JSONUtility.appendILabelBeanList(stringBuilder, JSONUtility.JSON_FIELDS.DATA_SOURCE, partDataSource);
							Integer[] listValues = null;
							Integer listValue = null;
							if (compositeListValue!=null) {
								listValues = compositeListValue.get(parameterCode);
								if (listValues!=null && listValues.length>0) {
									listValue = listValues[0];
								}
							}
							if (listValue==null) {
								if (partDataSource!=null && !partDataSource.isEmpty()) {
									listValue = partDataSource.get(0).getObjectID();
								}
							}
							if (listValue!=null) {
								JSONUtility.appendIntegerValue(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, listValue, true);
							}
							stringBuilder.append("}");
							if (itrPart.hasNext()) {
								stringBuilder.append(",");
							}
						}
					}
				stringBuilder.append("]");
			}
		}
		return stringBuilder.toString();
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
		Integer fieldID = getFieldID();
		StringBuilder stringBuilder = new StringBuilder("{");
		//it will be the itemId of the panel, needed for removing the panel after setter change 
		JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.NAME, getName(baseName));
		CustomCompositeBaseRT compositeBaseRT = (CustomCompositeBaseRT)FieldTypeManager.getFieldTypeRT(fieldID);
		if (compositeBaseRT!=null) {
			Map<Integer, SortedMap<Integer, List<ILabelBean>>> dataSourceMap = (Map<Integer, SortedMap<Integer, List<ILabelBean>>>)dataSource;
			if (dataSourceMap!=null) {
				boolean allIssuesFromTheSameProject = dataSourceMap.keySet().size()==1;
				Map<Integer, SortedMap<Integer, Integer[]>> valueMap = (Map<Integer, SortedMap<Integer, Integer[]>>)value;
				JSONUtility.appendFieldName(stringBuilder, "listsJson").append(":[");
				for (Iterator<Integer> itrList = dataSourceMap.keySet().iterator(); itrList.hasNext();) {
					stringBuilder.append("{");
					Integer listID = itrList.next();
					SortedMap<Integer, List<ILabelBean>> compositeListDataSource = dataSourceMap.get(listID);
					if (compositeListDataSource!=null) {
						SortedMap<Integer, Integer[]> compositeListValue = null;
						if (valueMap!=null) {
							compositeListValue = valueMap.get(listID);
						}
						if (labelMap!=null && !allIssuesFromTheSameProject) {
							JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.LABEL, labelMap.get(listID));
						}
						JSONUtility.appendIntegerValue(stringBuilder, "listID", listID);
						JSONUtility.appendFieldName(stringBuilder, "parts").append(":[");
						for (Iterator<Integer> itrPart = compositeListDataSource.keySet().iterator(); itrPart.hasNext();) {
							Integer parameterCode = itrPart.next();
							List<ILabelBean> partDataSource = compositeListDataSource.get(parameterCode);
							stringBuilder.append("{");
							JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.NAME,
									getNameWithMergedKey(baseName, fieldID, listID, parameterCode));
							JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ITEMID,
									getItemIdWithMergedKey(MassOperationBL.VALUE_BASE_ITEMID, fieldID, listID, parameterCode));
							JSONUtility.appendILabelBeanList(stringBuilder, JSONUtility.JSON_FIELDS.DATA_SOURCE, partDataSource);
							Integer[] listValues = null;
							Integer listValue = null;
							if (compositeListValue!=null) {
								listValues = compositeListValue.get(parameterCode);
								if (listValues!=null && listValues.length>0) {
									listValue = listValues[0];
								}
							}
							if (listValue==null) {
								if (partDataSource!=null && !partDataSource.isEmpty()) {
									listValue = partDataSource.get(0).getObjectID();
								}
							}
							if (listValue!=null) {
								JSONUtility.appendIntegerValue(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, listValue, true);
							}
							stringBuilder.append("}");
							if (itrPart.hasNext()) {
								stringBuilder.append(",");
							}
						}
						stringBuilder.append("]");
						stringBuilder.append("}");
						if (itrList.hasNext()) {
							stringBuilder.append(",");
						}
					}
				}
				stringBuilder.append("],");
			}
			JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.DISABLED, disabled, true);
		}
		stringBuilder.append("}");
		return stringBuilder.toString();
	}

	@Override
	public Object fromDisplayString(Map<String, String> displayStringMap, Locale locale) {
		Map<Integer, SortedMap<Integer, Integer[]>> actualValuesMap = new HashMap<Integer, SortedMap<Integer,Integer[]>>();
		if (displayStringMap == null) {
			return actualValuesMap;
		}
		switch (getRelation()) {
		case BulkRelations.SET_TO:
			for (String key : displayStringMap.keySet()) {
				if (key.startsWith(getKeyPrefix())) { 
				String displayStringMapValue = displayStringMap.get(key);
					if (displayStringMapValue!=null && !"".equals(displayStringMapValue)) {
						Integer listID = null;
						String[] tokens = key.split(LINK_CHAR);
						if (tokens.length>1) {
							if (tokens[1]!=null && !"".equals(tokens[1])) {
								listID = Integer.valueOf(tokens[1]);
								if (listID!=null) {
									SortedMap<Integer, Integer[]> valuesForList = actualValuesMap.get(listID);
									if (valuesForList==null) {
										valuesForList  = new TreeMap<Integer, Integer[]>();
										actualValuesMap.put(listID, valuesForList);
									}
									Integer parameterCode = null;
									if (tokens.length>2) {
										if (tokens[2]!=null && !"".equals(tokens[2])) {
											parameterCode = Integer.valueOf(tokens[2]);
										}
										Integer intValue = null;
										try {
											intValue = Integer.valueOf(displayStringMapValue);
											valuesForList.put(parameterCode, new Integer[] { intValue });
										} catch (Exception e) {
											LOGGER.warn("Converting the " + displayStringMapValue + " for fieldID " + getFieldID() +
												" and list " + listID +  " to Integer from display string failed with " + e.getMessage());
											LOGGER.debug(ExceptionUtils.getStackTrace(e));
										}
									}
								}
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
			if (value==null) {
				return null;
			}
			Map<Integer, SortedMap<Integer, Integer[]>> valueMap = (Map<Integer, SortedMap<Integer, Integer[]>>)value;
			CustomCompositeBaseRT customCompositeBaseRT = null;
			try {
				customCompositeBaseRT = (CustomCompositeBaseRT)FieldTypeManager.getFieldTypeRT(fieldID);
			} catch (Exception e) {
			}
			
			if (customCompositeBaseRT!=null) {
				int noOfParts = customCompositeBaseRT.getNumberOfParts();
				if (getRelation()==BulkRelations.SET_NULL) {
					for (int i = 0; i < noOfParts; i++) {
						workItemBean.setAttribute(fieldID, Integer.valueOf(i+1), null);
					}
					return null;
				} else {
					if (getRelation()==BulkRelations.SET_TO && value!=null) {
						Map<Integer, Map<Integer, Map<Integer, Integer>>> fieldToProjectToIssueTypeToListMap =
								bulkTranformContext.getFieldToProjectToIssueTypeToListMap();
						if (fieldToProjectToIssueTypeToListMap!=null) {
						Map<Integer, Map<Integer, Integer>> projectToIssueTypeToListMap = fieldToProjectToIssueTypeToListMap.get(fieldID);
							if (projectToIssueTypeToListMap!=null) {
								Integer projectID = workItemBean.getProjectID();
								Map<Integer, Integer> issueTypeToListMap = projectToIssueTypeToListMap.get(projectID);
								if (issueTypeToListMap!=null) {
									Integer issueTypeID = workItemBean.getListTypeID();
									Integer listID =  issueTypeToListMap.get(issueTypeID);
									if (listID!=null) {
										SortedMap<Integer, Integer[]> compositeListMap = valueMap.get(listID);
										if (compositeListMap!=null) {
											for (int i = 0; i < noOfParts; i++) {
												Integer localParameterCode = Integer.valueOf(i+1);
												workItemBean.setAttribute(fieldID, localParameterCode, compositeListMap.get(localParameterCode));
											}
										}
									}
								}
							}
						}
					}
				}
			}
			return null;
	}
}
