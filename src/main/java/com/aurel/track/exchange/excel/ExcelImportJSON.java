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


package com.aurel.track.exchange.excel;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.exchange.ImportJSON;
import com.aurel.track.exchange.excel.ExcelImportBL.IConflictMapEntry;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.item.ItemBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.BooleanStringBean;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.TreeNode;


/**
 * @author Tamas Ruff
 *
 */
public class ExcelImportJSON {
	static interface JSON_FIELDS {
		//upload fields
		static final String ACCEPT = "accept";

		//matching fields
		static final String FILE_NAME = "fileName";
		static final String SELECTED_SHEET = "selectedSheet";
		static final String SHEET_NAMES = "sheetNames";
		static final String COLUMN_INDEX = "columnIndex";
		static final String COLUMN_LETTER = "columnLetter";
		static final String COLUMN_NAME = "columnName";
		static final String FIELDID = "fieldID";
		static final String IS_IDENTIFIER = "isIdentifier";
		static final String IDENTIFIER_DISABLED = "identifierDisabled";
		static final String MAPPING_EXPRESSIONS = "mappingExpressions";
		static final String FIELD_LIST = "fieldList";
		static final String POSSIBLE_IDENTIFIERS = "possibleIdentifiers";
		static final String MANDATORY_IDENTIFIERS = "mandatoryIdentifiers";
		
		static final String POSSIBLE_VALUES = "possibleValues";
		static final String POSSIBLE_FIELD_VALUES = "possibleFieldValues";
		static final String DEFAULT_FIELD_VALUE = "defaultFieldValue";
		static final String INVALID_VALUE_HANDLING_LIST = "invalidValueHandlingList";
		static final String INVALID_VALUE_HANDLING_VALUE = "invalidValueHandlingValue";
		

		//static final String ERROR_MESSAGES = "errorMessages";
		static final String GRID_ERRORS = "gridErrors";
		static final String ROW_ERRORS = "rowErrors";
		static final String SOLUTION_MESSAGE = "solutionMessage";
		static final String LOCATION_LIST = "locationList";
		static final String CONFLICTS = "conflicts";
					
		//conflict fields
		static final String CONFLICT_RESOLUTION_LIST = "conflictResolutionList";
		static final String ISSUENO_LABEL = "issueNoLabel";
		static final String ROW = "row";
		static final String FIELD = "field";
		static final String FIELDS = "fields";
		static final String WORKITEMID = "workItemID";
		static final String FELED_NAME = "fieldName";
		static final String EXCEL_VALUE = "excelValue";
		static final String TRACKPLUS_VALUE = "trackplusValue";
		static final String WORKITEMID$FIELDID = "workItemIDFieldID";
	}
	
	/**
	 * Creates the JSON string for accepting mime type
	 * @param accept
	 * @return
	 */
	static String getUploadJSON(String accept) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		sb.append(JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendStringValue(sb, JSON_FIELDS.ACCEPT, accept, true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Gets the json string for field mapping
	 * @param fileName
	 * @param selectedSheet
	 * @param sheetNames
	 * @param fieldBeans
	 * @param columnIndexToColumNameMap
	 * @param columnIndexNumericToLetter
	 * @param columnIndexToFieldIDMap
	 * @param columnIndexIsIdentifierMap
	 * @param possibleIdentifiersSet
	 * @param mandatoryIdentifiersSet
	 * @return
	 */
	static String getExcelFieldMatcherJSON(String fileName, Integer selectedSheet,
			List<IntegerStringBean> sheetNames,
			List<IntegerStringBean> fieldBeans,
			SortedMap<Integer, String> columnIndexToColumNameMap,
			Map<Integer, String> columnIndexNumericToLetter,
			Map<Integer, Integer> columnIndexToFieldIDMap,
			Map<Integer, Boolean> columnIndexIsIdentifierMap,
			Set<Integer> possibleIdentifiersSet,
			Set<Integer> mandatoryIdentifiersSet) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{");
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.SUCCESS, true);
		stringBuilder.append(JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.FILE_NAME, fileName);
		JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.SELECTED_SHEET, selectedSheet);
		JSONUtility.appendIntegerStringBeanList(stringBuilder, JSON_FIELDS.SHEET_NAMES, sheetNames);
		JSONUtility.appendIntegerStringBeanList(stringBuilder, JSON_FIELDS.FIELD_LIST, fieldBeans);
		JSONUtility.appendIntegerSetAsArray(stringBuilder, JSON_FIELDS.POSSIBLE_IDENTIFIERS, possibleIdentifiersSet);
		JSONUtility.appendIntegerSetAsArray(stringBuilder, JSON_FIELDS.MANDATORY_IDENTIFIERS, mandatoryIdentifiersSet);
		//JSONUtility.appendIntegerValue(stringBuilder, JSONUtility.JSON_FIELDS.ACTUAL_STEP, Integer.valueOf(2));
		//JSONUtility.appendIntegerValue(stringBuilder, JSONUtility.JSON_FIELDS.TOTAL_STEP, Integer.valueOf(3));
		stringBuilder.append(getExcelFieldMatcherRows(columnIndexToColumNameMap,
				columnIndexNumericToLetter,
				columnIndexToFieldIDMap,
				columnIndexIsIdentifierMap,
				possibleIdentifiersSet,
				mandatoryIdentifiersSet));
		stringBuilder.append("}");
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * Gets the JSON string for mapping expressions
	 * @param columnIndexToColumNameMap
	 * @param columnIndexNumericToLetter
	 * @param columnIndexToFieldIDMap
	 * @param columnIndexIsIdentifierMap
	 * @param possibleIdentifiersSet
	 * @param mandatoryIdentifiersSet
	 * @return
	 */
	private static String getExcelFieldMatcherRows(
			SortedMap<Integer, String> columnIndexToColumNameMap,
			Map<Integer, String> columnIndexNumericToLetter,
			Map<Integer, Integer> columnIndexToFieldIDMap,
			Map<Integer, Boolean> columnIndexIsIdentifierMap,
			Set<Integer> possibleIdentifiersSet,
			Set<Integer> mandatoryIdentifiersSet) {
		StringBuilder stringBuilder = new StringBuilder();
		if (columnIndexToColumNameMap!=null && !columnIndexToColumNameMap.isEmpty()) {
			stringBuilder.append(JSON_FIELDS.MAPPING_EXPRESSIONS).append(":[");
			for (Iterator<Map.Entry<Integer, String>> iterator =
					columnIndexToColumNameMap.entrySet().iterator(); iterator.hasNext();) {
				Map.Entry<Integer, String>  indexToNameEntry = iterator.next();
				Integer columnIndex = indexToNameEntry.getKey();
				String columnName = indexToNameEntry.getValue();
				String columnLetter = columnIndexNumericToLetter.get(columnIndex);
				Integer fieldID = columnIndexToFieldIDMap.get(columnIndex);
				Boolean isIdentifier = columnIndexIsIdentifierMap.get(columnIndex);
				boolean identifierDisabled = !possibleIdentifiersSet.contains(fieldID) ||
						mandatoryIdentifiersSet.contains(fieldID);
				stringBuilder.append(getExcelFieldMatcherRow(columnIndex, columnLetter, 
						columnName, fieldID, isIdentifier!=null && isIdentifier.booleanValue(), identifierDisabled));
				if (iterator.hasNext()) {
					stringBuilder.append(",");
				}
			}
			stringBuilder.append("]");
		}
		return stringBuilder.toString();
	}
	
	/**
	 * Gets the JSON string for a field mapping
	 * @return
	 */	
	private static String getExcelFieldMatcherRow(Integer columnIndex, String columnLetter, String columnName,
			Integer fieldID, boolean isIdentifier, boolean identifierDisabled) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{");
		JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.COLUMN_INDEX, columnIndex);
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.COLUMN_LETTER, columnLetter);
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.COLUMN_NAME, columnName);
		JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.FIELDID, fieldID);
		JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.IS_IDENTIFIER, isIdentifier);
		JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.IDENTIFIER_DISABLED, identifierDisabled, true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * Gets the json string for invalid value handling
	 * @param fieldBeans
	 * @param invalidValueHandlingList
	 * @param possibleValuesMap
	 * @return
	 */
	static String getExcelInvalidHandlingFirstTimeJSON(
			List<IntegerStringBean> fieldBeans,
			List<IntegerStringBean> invalidValueHandlingList,
			Map<Integer, Integer> invalidValueHandlingValueMap,
			Map<Integer, List<ILabelBean>> possibleValuesMap,
            List<TreeNode> projectTree,
			Map<Integer, Integer> defaultFieldValuesMap) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{");
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.SUCCESS, true);
		stringBuilder.append(JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendIntegerStringBeanList(stringBuilder, JSON_FIELDS.FIELD_LIST, fieldBeans);
		JSONUtility.appendIntegerStringBeanList(stringBuilder, JSON_FIELDS.INVALID_VALUE_HANDLING_LIST, invalidValueHandlingList);
		stringBuilder.append(JSON_FIELDS.POSSIBLE_VALUES).append(":{");
		for (Iterator<IntegerStringBean> iterator = fieldBeans.iterator(); iterator.hasNext();) {
			IntegerStringBean fieldBean = iterator.next();
			Integer fieldID = fieldBean.getValue();
            Integer defaultFieldValue = defaultFieldValuesMap.get(fieldID);
            if (SystemFields.INTEGER_PROJECT.equals(fieldID)) {
                stringBuilder.append(JSON_FIELDS.FIELDID + fieldID).append(":{");
                if (projectTree!=null && !projectTree.isEmpty()) {
                    JSONUtility.appendJSONValue(stringBuilder, JSON_FIELDS.POSSIBLE_FIELD_VALUES, JSONUtility.getTreeHierarchyJSON(projectTree, false, false));
                    if (defaultFieldValue==null) {
                        defaultFieldValue = Integer.valueOf(projectTree.get(0).getId());
                    }
                }
            } else {
                List<ILabelBean> possibleValuesForField = possibleValuesMap.get(fieldID);
                stringBuilder.append(JSON_FIELDS.FIELDID + fieldID).append(":{");
                if (possibleValuesForField!=null && !possibleValuesForField.isEmpty()) {
                    JSONUtility.appendILabelBeanList(stringBuilder, JSON_FIELDS.POSSIBLE_FIELD_VALUES, possibleValuesForField);
                    if (defaultFieldValue==null) {
                        defaultFieldValue = possibleValuesForField.get(0).getObjectID();
                    } else {
                        boolean found = false;
                        for (ILabelBean labelBean : possibleValuesForField) {
                            if (labelBean.getObjectID().equals(defaultFieldValue)) {
                                found = true;
                            }
                        }
                        if (!found) {
                            defaultFieldValue = possibleValuesForField.get(0).getObjectID();
                        }
                    }
                }
            }
            JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.DEFAULT_FIELD_VALUE, defaultFieldValue);
            Integer invalidValueHandlingValue = invalidValueHandlingValueMap.get(fieldID);
            if (invalidValueHandlingValue==null) {
                invalidValueHandlingValue = ExcelImportBL.DEFAULT_IF_NOT_EXIST_OR_EMPTY;
            }
			JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.INVALID_VALUE_HANDLING_VALUE, invalidValueHandlingValue, true);
			stringBuilder.append("}");
			if (iterator.hasNext()) {
				stringBuilder.append(",");
			}
		}
		stringBuilder.append("}");
		stringBuilder.append("}");
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * Gets the json string for invalid value handling
	 * @param fieldIDs
	 * @param possibleValuesMap
	 * @param defaultFieldValuesMap
	 * @return
	 */
	static String getExcelInvalidHandlingUpdateJSON(
			List<Integer> fieldIDs,
			Map<Integer, List<ILabelBean>> possibleValuesMap,
			Map<Integer, Integer> defaultFieldValuesMap) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{");
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.SUCCESS, true);
		stringBuilder.append(JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendIntegerListAsArray(stringBuilder, JSON_FIELDS.FIELD_LIST, fieldIDs);
		stringBuilder.append(JSON_FIELDS.POSSIBLE_VALUES).append(":{");
		for (Iterator<Integer> iterator = fieldIDs.iterator(); iterator.hasNext();) {
			Integer fieldID = iterator.next();
			List<ILabelBean> possibleValuesForField = possibleValuesMap.get(fieldID);
			if (possibleValuesForField!=null && !possibleValuesForField.isEmpty()) {
				Integer defaultFieldValue = defaultFieldValuesMap.get(fieldID);
				stringBuilder.append(JSON_FIELDS.FIELDID + fieldID).append(":{");
				JSONUtility.appendILabelBeanList(stringBuilder, JSON_FIELDS.POSSIBLE_FIELD_VALUES, possibleValuesForField);
				if (defaultFieldValue==null) {
					defaultFieldValue = possibleValuesForField.get(0).getObjectID();
				} else {
					boolean found = false;
					for (ILabelBean labelBean : possibleValuesForField) {
						if (labelBean.getObjectID().equals(defaultFieldValue)) {
							found = true;
						}
					}
					if (!found) {
						defaultFieldValue = possibleValuesForField.get(0).getObjectID();
					}
				}
				JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.DEFAULT_FIELD_VALUE, defaultFieldValue, true);
				stringBuilder.append("}");
				if (iterator.hasNext()) {
					stringBuilder.append(",");
				}
			}
		}
		stringBuilder.append("}");
		stringBuilder.append("}");
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * Gets the json string for invalid value handling
	 * @param wrongGridValues
	 * @param wrongRowValues
	 * @param locale
     * @param disableFinal
	 * @return
	 */
	static String getExcelWrongGridValuesJSON(Map<Integer, List<String>> wrongGridValues,
			Map<String, String> wrongRowValues, List<String> requiredFieldErrorsList, Locale locale, boolean disableFinal) {
		StringBuilder stringBuilder = new StringBuilder();
		boolean hasWrongRowValues = wrongRowValues!=null && !wrongRowValues.isEmpty();
		boolean hasRequiredFieldRows = requiredFieldErrorsList!=null && !requiredFieldErrorsList.isEmpty();
		stringBuilder.append("{");
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.SUCCESS, false);
		JSONUtility.appendBooleanValue(stringBuilder, ImportJSON.JSON_FIELDS.DISABLE_FINAL, disableFinal);
		JSONUtility.appendIntegerValue(stringBuilder, JSONUtility.JSON_FIELDS.ERROR_CODE, ImportJSON.ERROR_CODES.GRID_AND_ROW_ERRORS);
		if (wrongGridValues!=null && !wrongGridValues.isEmpty()) {
			stringBuilder.append(JSON_FIELDS.GRID_ERRORS).append(":[");
			for (Iterator<Integer> iterator = wrongGridValues.keySet().iterator(); iterator.hasNext();) {
				Integer errorID = iterator.next();
				String errorMessage = ExcelImportBL.getGridErrorMessage(errorID, locale);
				String solutionMessage = ExcelImportBL.getGridSolutionMessage(errorID, locale);
				List<String> locationList = wrongGridValues.get(errorID);
				stringBuilder.append("{");
				JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ERROR_MESSAGE, errorMessage);
				if (solutionMessage!=null) {
					JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.SOLUTION_MESSAGE, solutionMessage);
				}
				JSONUtility.appendStringList(stringBuilder, JSON_FIELDS.LOCATION_LIST, locationList, true);
				stringBuilder.append("}");
				if (iterator.hasNext()) {
					stringBuilder.append(",");
				}
			}
			stringBuilder.append("]");
			if (hasWrongRowValues || hasRequiredFieldRows) {
				stringBuilder.append(",");
			}
		}
		if (hasWrongRowValues || hasRequiredFieldRows) {
			stringBuilder.append(JSON_FIELDS.ROW_ERRORS).append(":[");
			if (hasWrongRowValues) {
				for (Iterator<String> iterator = wrongRowValues.keySet().iterator(); iterator.hasNext();) {
					String errorMessage = iterator.next();
					String locationList = wrongRowValues.get(errorMessage);
					stringBuilder.append("{");
					JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ERROR_MESSAGE,
							LocalizeUtil.getParametrizedString(errorMessage, new Object[] { locationList}, locale), true);
					stringBuilder.append("}");
					if (iterator.hasNext() || hasRequiredFieldRows) {
						stringBuilder.append(",");
					}
				}
			}
			if (hasRequiredFieldRows) {
				for (Iterator<String> iterator = requiredFieldErrorsList.iterator(); iterator.hasNext();) {
					String errorMessage = iterator.next();
					stringBuilder.append("{");
					JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ERROR_MESSAGE,
							errorMessage, true);
					stringBuilder.append("}");
					if (iterator.hasNext()) {
						stringBuilder.append(",");
					}
				}
			}
			stringBuilder.append("]");
		}
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * Gets the json string for invalid value handling
	 * @param conflictsMap
	 * @param locale
	 * @param disableFinal
	 * @return
	 */
	static String getExcelConflictsJSON(
			SortedMap<Integer, SortedMap<Integer, Map<Integer, Object>>> conflictsMap,
			Locale locale, boolean disableFinal) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{");
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.SUCCESS, false);
		JSONUtility.appendIntegerValue(stringBuilder, JSONUtility.JSON_FIELDS.ERROR_CODE, ImportJSON.ERROR_CODES.CONFLICTS);
		JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ERROR_MESSAGE,
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importExcel.err.conflictResolution", locale));
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.ISSUENO_LABEL,
				FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_ISSUENO, locale));
		JSONUtility.appendBooleanStringBeanList(stringBuilder, JSON_FIELDS.CONFLICT_RESOLUTION_LIST, getLeaveOverwriteList(locale));
		JSONUtility.appendBooleanValue(stringBuilder, ImportJSON.JSON_FIELDS.DISABLE_FINAL, disableFinal);
		if (conflictsMap!=null && !conflictsMap.isEmpty()) {
			stringBuilder.append(JSON_FIELDS.CONFLICTS).append(":[");
			for (Iterator<Integer> itrRow = conflictsMap.keySet().iterator(); itrRow.hasNext();) {
				Integer row = itrRow.next();
				stringBuilder.append("{");
				JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.ROW, row);
				//stringBuilder.append("{");
				SortedMap<Integer, Map<Integer, Object>> rowConflicts = conflictsMap.get(row);
				stringBuilder.append(JSON_FIELDS.FIELDS).append(":[");
				for (Iterator<Integer> itrField = rowConflicts.keySet().iterator(); itrField.hasNext();) {
					Integer field = itrField.next();
					stringBuilder.append("{");
					Map<Integer, Object> fieldConlictMap = rowConflicts.get(field);
					JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.FIELD, field);
                    Integer workItemID = (Integer)fieldConlictMap.get(IConflictMapEntry.WORKITEMID);
                    if (workItemID!=null) {
                        String itemNo = ItemBL.getItemNo(workItemID);
					    JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.WORKITEMID, itemNo);
                    }
					JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.COLUMN_LETTER,
                            (String) fieldConlictMap.get(IConflictMapEntry.COLUMN_LETTER));
					JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.FELED_NAME,
                            (String) fieldConlictMap.get(IConflictMapEntry.FELED_NAME));
					JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.EXCEL_VALUE,
							(String)fieldConlictMap.get(IConflictMapEntry.EXCEL_VALUE));
					JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.TRACKPLUS_VALUE,
							(String)fieldConlictMap.get(IConflictMapEntry.TRACKPLUS_VALUE));
					JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.WORKITEMID$FIELDID,
							(String)fieldConlictMap.get(IConflictMapEntry.WORKITEMID_FIELDID), true);
					stringBuilder.append("}");
					if (itrField.hasNext()) {
						stringBuilder.append(",");
					}
				}
				stringBuilder.append("]");
				stringBuilder.append("}");
				if (itrRow.hasNext()) {
					stringBuilder.append(",");
				}
			}
			stringBuilder.append("]");
		}
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	private static List<BooleanStringBean> getLeaveOverwriteList(Locale locale) {
		List<BooleanStringBean> dateList = new LinkedList<BooleanStringBean>();
		dateList.add(new BooleanStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importExcel.conflict.opt.overwrite", locale),
				ExcelImportBL.IConflictResolution.OVERWRITE));
		dateList.add(new BooleanStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importExcel.conflict.opt.leave", locale),
				ExcelImportBL.IConflictResolution.LEAVE));
		return dateList;
	}
}
