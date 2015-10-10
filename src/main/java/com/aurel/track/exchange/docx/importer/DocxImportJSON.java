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


package com.aurel.track.exchange.docx.importer;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import com.aurel.track.exchange.ImportJSON;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.TreeNode;


/**
 * @author Tamas Ruff
 *
 */
public class DocxImportJSON {
	static interface JSON_FIELDS {
		//upload fields
		//upload fields
		static final String ACCEPT = "accept";
		
		static final String FILENAME = "fileName";
		//add/update to item fields
		static final String SELECTED_ID = "selectedProjectReleaseID";
        static final String PROJECT_RELEASE_TREE = "projectReleaseTree";
        
        static final String WORKITEM_ID = "workItemID";
        static final String ITEM_NO = "itemNo";
        static final String ITEM_TITLE = "itemTitle";
        
        static final String ITEM_TREE = "itemTree";

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
	 * Creates the JSON string for upload
	 * @param accept
	 * @param projectID
	 * @param projectReleaseTree
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
	 * Show the found item
	 * @param workItemID
	 * @param itemNo
	 * @param itemTitle
	 * @return
	 */
	static String getUpdateItemJSON(/*String fileName, */Integer workItemID, String itemNo, String itemTitle) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		sb.append(JSONUtility.JSON_FIELDS.DATA).append(":{");
		//JSONUtility.appendJSONValue(sb, JSON_FIELDS.FILE_NAME, fileName);
		JSONUtility.appendStringValue(sb, JSON_FIELDS.ITEM_NO, itemNo);
		JSONUtility.appendStringValue(sb, JSON_FIELDS.ITEM_TITLE, itemTitle);
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.WORKITEM_ID, workItemID, true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Creates the JSON string for upload
	 * @param accept
	 * @param projectID
	 * @param projectReleaseTree
	 * @return
	 */
	static String getAddToItemJSON(String fileName, String projectReleaseTree/*, Integer projectReleaseID, Integer parentWorkItemID, String parentItemID, String parentItemTitle*/) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		//sb.append(JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendStringValue(sb, JSON_FIELDS.FILE_NAME, fileName);
		JSONUtility.appendJSONValue(sb, JSON_FIELDS.PROJECT_RELEASE_TREE, projectReleaseTree, true);
		//sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Creates the JSON string for editing/adding/deriving a notification setting
     * @param itemTree
	 * @return
	 */
	static String createPreviewJSON(List<TreeNode> itemTree) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		sb.append(JSONUtility.JSON_FIELDS.DATA).append(":{");
        JSONUtility.appendJSONValue(sb, JSON_FIELDS.ITEM_TREE, JSONUtility.getTreeHierarchyJSON(itemTree));
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
	 * @param wrongGridValues
	 * @param wrongRowValues
	 * @param locale
     * @param disableFinal
	 * @return
	 */
	static String getExcelWrongGridValuesJSON(Map<String, List<String>> wrongGridValues,
			Map<String, String> wrongRowValues, Locale locale, boolean disableFinal) {
		StringBuilder stringBuilder = new StringBuilder();
		boolean hasWrongRowValues = wrongRowValues!=null && !wrongRowValues.isEmpty();
		stringBuilder.append("{");
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.SUCCESS, false);
		JSONUtility.appendBooleanValue(stringBuilder, ImportJSON.JSON_FIELDS.DISABLE_FINAL, disableFinal);
		JSONUtility.appendIntegerValue(stringBuilder, JSONUtility.JSON_FIELDS.ERROR_CODE, ImportJSON.ERROR_CODES.GRID_AND_ROW_ERRORS);
		if (wrongGridValues!=null && !wrongGridValues.isEmpty()) {
			stringBuilder.append(JSON_FIELDS.GRID_ERRORS).append(":[");
			for (Iterator<String> iterator = wrongGridValues.keySet().iterator(); iterator.hasNext();) {
				String errorMessage = iterator.next();
				List<String> locationList = wrongGridValues.get(errorMessage);
				stringBuilder.append("{");
				JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ERROR_MESSAGE, errorMessage);
				JSONUtility.appendStringList(stringBuilder, JSON_FIELDS.LOCATION_LIST, locationList, true);
				stringBuilder.append("}");
				if (iterator.hasNext()) {
					stringBuilder.append(",");
				}
			}
			stringBuilder.append("]");
			if (hasWrongRowValues) {
				stringBuilder.append(",");
			}
		}
		if (hasWrongRowValues) {
			stringBuilder.append(JSON_FIELDS.ROW_ERRORS).append(":[");
			for (Iterator<String> iterator = wrongRowValues.keySet().iterator(); iterator.hasNext();) {
				String errorMessage = iterator.next();
				String locationList = wrongRowValues.get(errorMessage);
				stringBuilder.append("{");
				JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ERROR_MESSAGE,
						LocalizeUtil.getParametrizedString(errorMessage, new Object[] { locationList}, locale), true);
				stringBuilder.append("}");
				if (iterator.hasNext()) {
					stringBuilder.append(",");
				}
			}
			stringBuilder.append("]");
		}
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
}
