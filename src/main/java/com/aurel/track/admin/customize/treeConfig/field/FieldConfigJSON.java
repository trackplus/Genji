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

package com.aurel.track.admin.customize.treeConfig.field;

import java.util.Iterator;
import java.util.List;

import com.aurel.track.json.JSONUtility;
import com.aurel.track.plugin.FieldTypeDescriptor;

public class FieldConfigJSON {

	public static interface JSON_FIELDS {
		String FIELD_ATTRIBUTES_DISABLED = "fieldAttributesDisabled";

		String FIELD_TYPE = "fieldType";
		String FIELD_TYPES = "fieldTypesList";
		String CUSTOM_FIELD = "customField";
		String FIELD_TYPE_DISABLED = "fieldTypeDisabled";
		
		String FILTER_FIELD = "filterField";
		String RENDER_FILTER_FIELD = "renderFilterField";
		String DEPRECATED = "deprecated";
		String RENDER_DEPRECATED = "renderDeprecated";
		String TOOLTIP = "tooltip";
		String REQUIRED = "required";
		String RENDER_REQUIRED = "renderRequired";
		String HISTORY = "history";
		String RENDER_HISTORY = "renderHistory";
		
		String INHERITED_CONFIG = "inheritedConfig";
		
		String CAN_DELETE = "canDelete";
		String SPECIFIC_FIELD_CONFIG_CLASS = "specificFieldConfigClass";
		
		String DEFAULT = "default";
		String VALUE = "Value";
		String MIN = "min";
		String MAX = "max";
		String INTEGER = "Integer";
		String DOUBLE = "Double";
		String TEXT = "Text";
		String TEXT_LENGTH = "TextLength";
		String DATE = "Date";
		String CHAR = "Char";
		String OPTION = "Option";
		String DECIMALDIGIT = "Decimaldigit";
		String LABEL = "Label";
		String MIN_VALUE = "minValue";
		String MAX_VALUE = "maxValue";
		
		String OPTION_SETTINGS_LIST_PREFIX = "optionSettingsList";
		String OPTION_SETTINGS_LIST = "list";
		String OPTION_SETTINGS_LISTS = "lists";
		String OPTION_SETTINGS_LIST_LABEL = LABEL;
		
		String GENERAL_SETTINGS = "generalSettingsList";
		String GENERAL_SETTINGS_INTEGER = "integer" + VALUE;
		String GENERAL_SETTINGS_DOUBLE = "double" + VALUE;
		String GENERAL_SETTINGS_TEXT = "text" + VALUE;
		String GENERAL_SETTINGS_DATE = "date" + VALUE;
		//+ "String" to work with boolean instead of char
		String GENERAL_SETTINGS_CHAR = "character" + VALUE + "String";
		
		String TEXTBOX_SETTINGS = "textBoxSettingsList";
		//char (boolean) fields
		//+ "String" to work with boolean instead of char
		String TEXTBOX_SETTINGS_DEFAULT_CHAR = DEFAULT + CHAR + "String";
		String TEXTBOX_SETTINGS_DEFAULT_CHAR_LABEL = DEFAULT + CHAR + LABEL;
						
		//text box text
		String TEXTBOX_SETTINGS_DEFAULT_TEXT = DEFAULT + TEXT;
		String TEXTBOX_SETTINGS_DEFAULT_TEXT_LABEL = DEFAULT + TEXT + LABEL;
		String TEXTBOX_SETTINGS_MIN_TEXT_LENGTH = MIN + TEXT_LENGTH;
		String TEXTBOX_SETTINGS_MIN_TEXT_LENGTH_LABEL = MIN + TEXT_LENGTH + LABEL;
		String TEXTBOX_SETTINGS_MAX_TEXT_LENGTH = MAX + TEXT_LENGTH;
		String TEXTBOX_SETTINGS_MAX_TEXT_LENGTH_LABEL = MAX + TEXT_LENGTH + LABEL;
		
		//text box integer
		String TEXTBOX_SETTINGS_DEFAULT_INTEGER = DEFAULT + INTEGER;
		String TEXTBOX_SETTINGS_DEFAULT_INTEGER_LABEL = DEFAULT + INTEGER + LABEL;
		String TEXTBOX_SETTINGS_MIN_INTEGER = MIN + INTEGER;
		String TEXTBOX_SETTINGS_MIN_INTEGER_LABEL = MIN + INTEGER + LABEL;
		String TEXTBOX_SETTINGS_MAX_INTEGER = MAX + INTEGER;		
		String TEXTBOX_SETTINGS_MAX_INTEGER_LABEL = MAX + INTEGER + LABEL;

		//text box double
		String TEXTBOX_SETTINGS_DEFAULT_DOUBLE = DEFAULT + DOUBLE;
		String TEXTBOX_SETTINGS_DEFAULT_DOUBLE_LABEL = DEFAULT + DOUBLE +LABEL;
		String TEXTBOX_SETTINGS_MIN_DOUBLE = MIN + DOUBLE;
		String TEXTBOX_SETTINGS_MIN_DOUBLE_LABEL = MIN + DOUBLE + LABEL;
		String TEXTBOX_SETTINGS_MAX_DOUBLE = MAX + DOUBLE;
		String TEXTBOX_SETTINGS_MAX_DOUBLE_LABEL = MAX + DOUBLE + LABEL;
		String TEXTBOX_SETTINGS_MAX_DECIMAL_DIGIT = MAX + DECIMALDIGIT;
		String TEXTBOX_SETTINGS_MAX_DECIMAL_DIGIT_LABEL = MAX + DECIMALDIGIT + LABEL;
		
		//text box date
		String TEXTBOX_SETTINGS_DATE_OPTIONS = "dateOptions";
		String TEXTBOX_SETTINGS_DEFAULT_DATE_OPTIONS = "defaultDateOptions";
		String TEXTBOX_SETTINGS_DEFAULT_OPTION = DEFAULT + OPTION;
		String TEXTBOX_SETTINGS_DEFAULT_OPTION_LABEL = DEFAULT + OPTION + LABEL;
		String TEXTBOX_SETTINGS_DEFAULT_DATE = DEFAULT + DATE;
		String TEXTBOX_SETTINGS_MIN_OPTION = MIN + OPTION;
		String TEXTBOX_SETTINGS_MIN_OPTION_LABEL = MIN + OPTION + LABEL;
		String TEXTBOX_SETTINGS_MIN_DATE = MIN + DATE;
		String TEXTBOX_SETTINGS_MAX_OPTION = MAX + OPTION;
		String TEXTBOX_SETTINGS_MAX_OPTION_LABEL = MAX + OPTION + LABEL;
		String TEXTBOX_SETTINGS_MAX_DATE = MAX + DATE;
		
		String TEXTBOX_SETTINGS_HIERARCHICAL_BEHAVIOR_OPTIONS = "hierarchicalBehaviorOptions";
		String TEXTBOX_SETTINGS_HIERARCHICAL_BEHAVIOR_OPTION = MIN + INTEGER;
		String TEXTBOX_SETTINGS_HIERARCHICAL_BEHAVIOR_LABEL = "hierarchicalBehaviorLabel";
		
		
		
		//after save
		String REFRESH_TREE = "refreshTree";
		
		String PATH_TO_EXPAND = "pathToExpand";
	}
	
	private static void appendFieldTypeList(StringBuilder sb, String name, List<FieldTypeDescriptor> list) {
		JSONUtility.appendFieldName(sb, name);
		sb.append(":[");
		if(list!=null){
			for (Iterator<FieldTypeDescriptor> iterator = list.iterator(); iterator.hasNext();) {
				FieldTypeDescriptor fieldTypeDescriptor = iterator.next();
				sb.append("{");
				JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ID, fieldTypeDescriptor.getTheClassName());
				JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.LABEL, fieldTypeDescriptor.getLocalizedLabel(), true);
				sb.append("}");
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		sb.append(",");
	}
	
	/**
	 * Creates the JSON string for field configuration
	 * @param node
     * @param name
     * @param fieldAttributesDisabled
     * @param fieldType
     * @param fieldTypes
     * @param customField
     * @param fieldTypeDisabled
     * @param filterField
     * @param renderFilterField
     * @param deprecated
     * @param renderDeprecated
     * @param description
     * @param canDelete
     * @param label
     * @param tooltip
     * @param required
     * @param renderRequired
     * @param history
     * @param renderHistory
     * @param inheritedConfig
     * @param specificFieldConfigClass
     * @param specificConfigJSON
     * @return
	 */
	public static String createFieldDetailJSON(String node, String name, boolean fieldAttributesDisabled,
			String fieldType, List<FieldTypeDescriptor> fieldTypes, boolean customField, boolean fieldTypeDisabled,
			boolean filterField, boolean renderFilterField, 
			boolean deprecated, boolean renderDeprecated, String description, boolean canDelete,
			String label, String tooltip, boolean required, boolean renderRequired, boolean history, boolean renderHistory,
			boolean inheritedConfig, String specificFieldConfigClass, String specificConfigJSON){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA);
		sb.append(":{");
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NODE, node);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NAME, name);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.FIELD_ATTRIBUTES_DISABLED, fieldAttributesDisabled);
		
		JSONUtility.appendStringValue(sb, JSON_FIELDS.FIELD_TYPE, fieldType);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.CUSTOM_FIELD, customField);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.FIELD_TYPE_DISABLED, fieldTypeDisabled);
		appendFieldTypeList(sb, JSON_FIELDS.FIELD_TYPES, fieldTypes);
		
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.FILTER_FIELD, filterField);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.RENDER_FILTER_FIELD, renderFilterField);
		
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.DEPRECATED, deprecated);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.RENDER_DEPRECATED, renderDeprecated);
		
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.DESCRIPTION, description);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.CAN_DELETE, canDelete);
		
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.LABEL, label);
		JSONUtility.appendStringValue(sb, JSON_FIELDS.TOOLTIP, tooltip);
		
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.REQUIRED, required);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.RENDER_REQUIRED, renderRequired);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.HISTORY, history);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.RENDER_HISTORY, renderHistory);
		
		JSONUtility.appendStringValue(sb, JSON_FIELDS.SPECIFIC_FIELD_CONFIG_CLASS, specificFieldConfigClass);
		sb.append(specificConfigJSON);
		
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.INHERITED_CONFIG, inheritedConfig, true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Creates the specific JSON string for field configuration
	 * @param specificFieldConfigClass
     * @param specificConfigJSON
	 * @return
	 */
	public static String createSpecificConfigJSON(String specificFieldConfigClass, String specificConfigJSON){	
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendStringValue(sb, JSON_FIELDS.SPECIFIC_FIELD_CONFIG_CLASS, specificFieldConfigClass);
		sb.append(specificConfigJSON);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	
	
	/**
	 * Gets the JSON string after overwrite/reset of a configuration
	 * @param node 
	 * @return
	 */
	public static String getSaveJSON(String node, List<String> pathToExpand, boolean refreshTree){	
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);	
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.REFRESH_TREE, refreshTree);
		if (pathToExpand!=null && !pathToExpand.isEmpty()) {
			JSONUtility.appendStringList(sb, JSON_FIELDS.PATH_TO_EXPAND, pathToExpand);
		}
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NODE, node, true);
		sb.append("}");
		return sb.toString();
	}
}
