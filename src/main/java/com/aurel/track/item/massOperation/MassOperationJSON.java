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

package com.aurel.track.item.massOperation;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;

/**
 * JSON utility for filters
 * @author Tamas Ruff
 *
 */
public class MassOperationJSON {
	
	
	/**
	 * JSON field names for bulk expression
	 * @author Tamas
	 *
	 */
	private static interface BULK_EXPRESSION_JSON_FIELDS {
		String FIELD = "field";
		String FIELD_LABEL = "fieldLabel";
		String FIELD_NAME = "fieldName"; //the name of the select field checkbox
		String FIELD_ITEM_ID = "fieldItemId"; 
		String SELECTED_RELATION = "selectedRelation"; // "matcher" -> // <-relationID
		String SETTER_RELATIONS = "setterRelations"; //matcherList ->
		String VALUE_REQUIRED = "valueRequired"; // needMatcherValue ->
		String RELATION_NAME = "relationName"; //matcherName
		String RELATION_ITEM_ID = "relationItemId"; //matcher itemId
		String VALUE_NAME = "valueName";
		String VALUE_ITEM_ID = "valueItemId";
		String VALUE_RENDEDER = "valueRenderer";//<- bulkValueTemplate 
		String JSON_CONFIG = "jsonConfig";
		//displayValueMap;
		//value;
		//fieldDisabled = true;		
		//whether the field is a rich text field
		//visible = true;
		//String SHOW_CONFIRM = "showConfirm"; // needMatcherValue ->
		
		String NUMBER_OF_SELECTED_ISSUES = "numberOfSelectedIssues";
		String CONTEXT_REFRESH = "contextRefresh";
		//String ENABLE_COPY_CHILDREN = "enableCopyChildren";
		String EXPRESSIONS = "expressions";
	}
	
	
	/**
	 * Gets the JSON string for a FieldExpressionSimpleTO list  
	 * @param massOperationExpressionList
	 * @param contextRefresh
	 * @param numberOfSelectedIssues
	 * @return
	 */
	public static String getBulkExpressionListJSON(
			List<MassOperationExpressionTO> massOperationExpressionList, boolean contextRefresh, int numberOfSelectedIssues) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{");
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(stringBuilder, JSONUtility.JSON_FIELDS.DATA).append(":{");;
		JSONUtility.appendIntegerValue(stringBuilder, BULK_EXPRESSION_JSON_FIELDS.NUMBER_OF_SELECTED_ISSUES, numberOfSelectedIssues);
		JSONUtility.appendBooleanValue(stringBuilder, BULK_EXPRESSION_JSON_FIELDS.CONTEXT_REFRESH, contextRefresh);
		JSONUtility.appendFieldName(stringBuilder, BULK_EXPRESSION_JSON_FIELDS.EXPRESSIONS).append(":[");
		if (massOperationExpressionList!=null && !massOperationExpressionList.isEmpty()) {
			for (Iterator<MassOperationExpressionTO> iterator = massOperationExpressionList.iterator(); iterator.hasNext();) {
				MassOperationExpressionTO massOperationExpression = iterator.next();
				stringBuilder.append(getBulkExpressionJSON(massOperationExpression));
				if (iterator.hasNext()) {
					stringBuilder.append(",");
				}
			}
		}
		stringBuilder.append("]");
		stringBuilder.append("}");
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * Gets the JSON string for a FieldrExpressionSimpleTO
	 * @return
	 */
	private static String getBulkExpressionJSON(MassOperationExpressionTO massOperationExpression) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{");
		JSONUtility.appendStringValue(stringBuilder, BULK_EXPRESSION_JSON_FIELDS.FIELD_NAME,
				massOperationExpression.getFieldName());
		JSONUtility.appendStringValue(stringBuilder, BULK_EXPRESSION_JSON_FIELDS.FIELD_ITEM_ID,
				massOperationExpression.getFieldItemId());
		JSONUtility.appendStringValue(stringBuilder, BULK_EXPRESSION_JSON_FIELDS.RELATION_NAME,
				massOperationExpression.getRelationName());
		JSONUtility.appendStringValue(stringBuilder, BULK_EXPRESSION_JSON_FIELDS.RELATION_ITEM_ID,
				massOperationExpression.getRelationItemId());
		JSONUtility.appendIntegerValue(stringBuilder, BULK_EXPRESSION_JSON_FIELDS.SELECTED_RELATION,
				massOperationExpression.getRelationID());
		JSONUtility.appendIntegerStringBeanList(stringBuilder,  BULK_EXPRESSION_JSON_FIELDS.SETTER_RELATIONS, 
				massOperationExpression.getSetterRelations());
		stringBuilder.append(getBulkExpressionValueBaseJSON(
				massOperationExpression.getValueName(),
				massOperationExpression.getValueItemId(),
				massOperationExpression.isValueRequired(),
				massOperationExpression.getBulkValueTemplate(),
				massOperationExpression.getJsonConfig(), false));
		JSONUtility.appendStringValue(stringBuilder, BULK_EXPRESSION_JSON_FIELDS.FIELD_LABEL,
			massOperationExpression.getFieldLabel());
		JSONUtility.appendIntegerValue(stringBuilder, BULK_EXPRESSION_JSON_FIELDS.FIELD,
				massOperationExpression.getFieldID(), true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * Gets the JSON for field expression value: used after a matcher relation change 
	 * @param valueName
	 * @param valueItemId
	 * @param valueRequired
	 * @param valueRenderer
	 * @param jsonConfig
	 * @return
	 */
	public static String getBulkExpressionValueJSON(String valueName, String valueItemId, boolean valueRequired,	
			String valueRenderer, String jsonConfig) {
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("{");
		stringBuilder.append(getBulkExpressionValueBaseJSON(valueName, valueItemId, valueRequired, valueRenderer, jsonConfig, true));
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * Gets the base JSON for field expression value
	 * @param valueName
	 * @param valueItemId
	 * @param valueRequired
	 * @param valueRenderer
	 * @param jsonConfig
	 * @param last
	 * @return
	 */
	private static String getBulkExpressionValueBaseJSON(String valueName, String valueItemId, boolean valueRequired,
			String valueRenderer, String jsonConfig, boolean last) {
		StringBuilder stringBuilder = new StringBuilder();
		JSONUtility.appendBooleanValue(stringBuilder,
				BULK_EXPRESSION_JSON_FIELDS.VALUE_REQUIRED, valueRequired);
		JSONUtility.appendJSONValue(stringBuilder,
				BULK_EXPRESSION_JSON_FIELDS.JSON_CONFIG, jsonConfig);
		JSONUtility.appendStringValue(stringBuilder,
				BULK_EXPRESSION_JSON_FIELDS.VALUE_RENDEDER, valueRenderer);
		JSONUtility.appendStringValue(stringBuilder,
				BULK_EXPRESSION_JSON_FIELDS.VALUE_ITEM_ID, valueItemId);
		JSONUtility.appendStringValue(stringBuilder,
				BULK_EXPRESSION_JSON_FIELDS.VALUE_NAME, valueName, last);
		return stringBuilder.toString();
	}
	
	/**
	 * Get the error messages
	 * @param errorList
	 * @param allowedMessage
	 * @param showConfirm
	 * @return
	 */
	public static String getErrorMessagesJSON(MassOperationException massOperationErrorTO, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder("{");
		JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.TITLE,
				LocalizeUtil.getLocalizedTextFromApplicationResources("itemov.massOperation.err.title", locale));
		JSONUtility.appendIntegerValue(stringBuilder, JSONUtility.JSON_FIELDS.ERROR_CODE, massOperationErrorTO.getErrorCode());
		StringBuilder errorMessage = new StringBuilder();
		List<String> errorList = massOperationErrorTO.getErrorList();
		for (String strError : errorList) {
			errorMessage.append(strError).append("<br>");
		}
		String allowedMessage = massOperationErrorTO.getAllowedMessage();
		if (allowedMessage!=null) {
			errorMessage.append(allowedMessage);
		}
		JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ERROR_MESSAGE, errorMessage.toString(), true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
}
