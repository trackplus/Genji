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


package com.aurel.track.json;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.errors.ErrorHandlerJSONAdapter;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.callbackInterfaces.ILookup;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.toolbar.ToolbarItem;
import com.aurel.track.tree.TreeNodeBaseTO;
import com.aurel.track.util.BooleanStringBean;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.DownloadUtil;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.IntegerStringBooleanBean;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.Support;
import com.aurel.track.util.TreeNode;
import com.aurel.track.util.numberFormatter.DoubleWithDecimalDigitsNumberFormatUtil;
import com.aurel.track.util.numberFormatter.PercentNumberFormatUtil;

/**
 * This class provides various helper routines to encode Java Objects into JSON objects.
 *
 */
public class JSONUtility {

	private static final Logger LOGGER = LogManager.getLogger(JSONUtility.class);
	
	//send formated version to client
	public static interface DELETE_ERROR_CODES {
		public static int NEED_REPLACE = 1;
		public static int NO_RIGHT_TO_DELETE = 2;
		public static int NOT_EMPTY_WARNING = 3;
	}
	
	public static interface EDIT_ERROR_CODES {
		public static int NEED_CONFIRMATION = 4;
		public static int RECOMMENDED_REPLACE = 5;
	}
	public static int ERROR_CODE_NO_USER_LOGIN= -1000;
	
	/**
	 * The most common used JSON fields
	 */
	public static interface JSON_FIELDS {
		static final String SUCCESS = "success";
		static final String MESSAGE = "message";
		static final String ERRORS = "errors";
		static final String ERROR_MESSAGE = "errorMessage";
		static final String WARNING_MESSAGE = "warningMessage";
		static final String ERROR_CODE = "errorCode";
		static final String RECORDS = "records";
		static final String DATA = "data";
		static final String TITLE = "title";
		
		
		static final String ID = "id";
		static final String OBJECT_ID = "objectID";
		static final String LABEL = "label";
		static final String SYMBOL = "symbol";
		static final String SELECTED = "selected";
		static final String NAME = "name";
		static final String VALUE = "value";
		static final String DATA_SOURCE = "dataSource";
		static final String USE_ICON_CLS = "useIconCls";
		static final String ICON_URL_PREFIX = "iconUrlPrefix";
		static final String DESCRIPTION = "description";
		
		//the tree uses text for label
		static final String TEXT = "text";
		static final String NODE = "node";
		static final String SHOW_GRID = "showGrid";
		
		
		static final String DISABLED = "disabled";
		
		static final String LEAF = "leaf";
		static final String CHECKED = "checked";
		static final String SELECTABLE = "selectable";
		static final String EXPANDED = "expanded";
		static final String ICON = "icon";
		static final String ICONCLS = "iconCls";
		static final String QTIP = "qtip";
		
		static final String CHILDREN = "children";
		
		//for tooltip
		static final String TT = "TT";
		
		static final String REPLACEMENT_WARNING = "replacementWarning";
		static final String REPLACEMENT_LABEL = "replacementListLabel";
		static final String REPLACEMENT_LIST_LABEL = "replacementListLabel";
		static final String REPLACEMENT_LIST = "replacementList";
        static final String REPLACEMENT_TREE = "replacementTree";
		
		static final String REMOVE_OPTIONS = "removeOptions";
		
		/**
		 * The suffix for the help wrapper container 
		 */
		static final String WRP = ".wrp";
		
		/**
		 * The itemId of the wrapper container
		 */
		static final String PAN_WARPPER = "panWrapper";
		
		//wizard parameters
		//static final String ACTUAL_STEP = "actualStep";
		//static final String TOTAL_STEP = "totalStep";
	}
	
	public static List<String> getPathInHelpWrapper(String controlName) {
		List<String> path = new LinkedList<String>();
		path.add(controlName + JSON_FIELDS.WRP);
		path.add(JSON_FIELDS.PAN_WARPPER);
		path.add(controlName);
		return path;
	}
	
	/*
	 * This helper method encodes a field name. It is quoted with double quotes if it contains
	 * special characters ".", "'", "-", and " " (space). Otherwise the field name is taken without 
	 * quotes.
	 */
	public static StringBuilder appendFieldName(StringBuilder stringBuilder, String fieldName) {
		/*if (fieldName.contains(" ") || fieldName.contains("'") 
			|| fieldName.contains("-") || fieldName.contains(".")) {
			return stringBuilder.append("\"").append(fieldName).append("\"");
		}
		else {*/
			return stringBuilder.append("\"").append(fieldName).append("\"");
		//}
	}
	public static String escapeFieldName(String fieldName) {
		return new StringBuilder().append("\"").append(fieldName).append("\"").toString();		
	}
	
	/**
	 * Appends a string type field. Example: <code>fieldName:"The value of the field",</code>.</br>
	 * Special characters are properly escaped, a comma is added as the last character.
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param value - the field value, properly escaped for JSON notation
	 */
	public static void appendStringValue(StringBuilder sb, String name, String value){
		appendStringValue(sb, name, value, false);
	}
	
	/**
	 * Appends a string type field. Example: <code>fieldName:"The value of the field"</code>.</br>
	 * Special characters are properly escaped.
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param value - the field value, properly escaped for JSON notation
	 * @param last - if true, no comma is added as the last character, otherwise there will be a comma
	 */
	public static void appendStringValue(StringBuilder sb, String name, String value, boolean last){
		if(value!=null){
			appendFieldName(sb, name).append(":\"").append(escape(value)).append("\"");
			if(!last) {
				sb.append(",");
			}
		}
	}
	
	
	/**
	 * Appends a JSON field. Example: <code>fieldName:"The value of the field",</code>.</br>
	 * Special characters are properly escaped, a comma is added as the last character.
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param value - the field value, properly escaped for JSON notation
	 */
	public static void appendJSONValue(StringBuilder sb, String name, String value){
		appendJSONValue(sb, name, value, false);
	}
	
	
	/**
	 * Appends a JSON field. Example: <code>fieldName:"The value of the field"</code>.</br>
	 * Special characters are properly escaped.
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param value - the field value, properly escaped for JSON notation
	 * @param last - if true, no comma is added as the last character, otherwise there will be a comma
	 */
	public static void appendJSONValue(StringBuilder sb, String name, String value, boolean last){
		if(value!=null){
			appendFieldName(sb, name).append(":").append(value);
			if(!last){
				sb.append(",");
			}
		}
	}
	
	/**
	 * Appends an integer type field. Example: <code>fieldName:2345,</code>.</br>
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param value - the field value
	 */
	public static void appendIntegerValue(StringBuilder sb, String name, Integer value){
		appendIntegerValue(sb, name, value, false);
	}
	
	/**
	 * Appends an integer type field. Example: <code>fieldName:1234,</code>.</br>
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param value - the field value
	 * @param last - if true, no comma is added as the last character, otherwise there will be a comma
	 */
	public static void appendIntegerValue(StringBuilder sb, String name, Integer value, boolean last){
		if(value!=null){
			appendFieldName(sb, name).append(":").append(value);
			if(!last) {
				sb.append(",");
			}
		}
	}

	/**
	 * Appends a long type field. Example: <code>fieldName:1234,</code>.
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param value - the field value
	 */
	public static void appendLongValue(StringBuilder sb, String name, Long value){
		appendLongValue(sb, name, value, false);
	}

	/**
	 * Appends a long type field. Example: <code>fieldName:1234,</code>.</br>
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param value - the field value
	 * @param last - if true, no comma is added as the last character, otherwise there will be a comma
	 */
	public static void appendLongValue(StringBuilder sb, String name, Long value, boolean last){
		if(value!=null){
			appendFieldName(sb, name).append(":").append(value);
			if(!last) {
				sb.append(",");
			}
		}
	}

	/**
	 * Appends a list of integers. Example: <code>fieldName:[1234, 5678, 9123],</code>.</br>
	 * Note: there is always a comma added in the end, so this element cannot be the
	 * last in a JSON response.
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param list - the field value(s)
	 */
	public static void appendIntegerListAsArray(StringBuilder sb, String name, List<Integer> list){
		appendIntegerListAsArray(sb, name, list, false);	
	}	
	
	/**
	 * Appends a list of integers. Example: <code>fieldName:[1234, 5678, 9123],</code>.</br>
	 * Note: there is always a comma added in the end, so this element cannot be the
	 * last in a JSON response.
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param list - the field value(s)
	 */
	public static void appendIntegerListAsArray(StringBuilder sb, String name, List<Integer> list, boolean last){
		if (list!=null && !list.isEmpty()){
			appendFieldName(sb, name).append(":[");
			for (Iterator<Integer> iterator = list.iterator(); iterator.hasNext();) {
				Integer integerValue = iterator.next();
				if (integerValue!=null) {
					sb.append(integerValue);
				}
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
			sb.append("]");
			if (!last) {
				sb.append(",");
			}
		}		
	}	
	
	/**
	 * Appends a list of integers from a set. Example: <code>fieldName:[1234, 5678, 9123],</code>.</br>
	 * Note: there is always a comma added in the end, so this element cannot be the
	 * last in a JSON response.
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param set - the field value(s)
	 */
	public static void appendIntegerSetAsArray(StringBuilder sb, String name, Set<Integer> set){
		appendIntegerSetAsArray(sb, name, set, false);	
	}

	/**
	 * Appends a list of integers from a set. Example: <code>fieldName:[1234, 5678, 9123],</code>.</br>
	 * Note: there is always a comma added in the end, so this element cannot be the
	 * last in a JSON response.
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param set - the field value(s)
	 */
	public static void appendIntegerSetAsArray(StringBuilder sb, String name, Set<Integer> set, boolean last){
		if (set!=null && !set.isEmpty()){
			appendFieldName(sb, name).append(":[");
			for (Iterator<Integer> iterator = set.iterator(); iterator.hasNext();) {
				Integer integerValue = iterator.next();
				if (integerValue!=null) {
					sb.append(integerValue);
				}
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
			sb.append("]");
			if (!last) {
				sb.append(",");
			}
		}		
	}	
	
	/**
	 * Appends a list of integers from an Integer array. Example: <code>fieldName:[1234, 5678, 9123],</code>.</br>
	 * Note: There is always a comma added in the end, so this element cannot be the
	 * last in a JSON response.
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param intArr - the field value(s)
	 */
	public static void appendIntegerArrayAsArray(StringBuilder sb, String name, Integer[] intArr, boolean last){
		if (intArr!=null && intArr.length>0){
			appendFieldName(sb, name).append(":[");
			for (int i = 0; i < intArr.length; i++) {
				Integer integerValue = intArr[i];
				if (integerValue!=null) {
					sb.append(integerValue);
				}
				if (i<intArr.length-1) {
					sb.append(",");
				}
			}
			sb.append("]");
			if (!last) {
				sb.append(",");
			}
		}
	}
	
	/**
	 * Appends a list of integers from an Integer array. Example: <code>fieldName:[1234, 5678, 9123],</code>.</br>
	 * Note: There is always a comma added in the end, so this element cannot be the
	 * last in a JSON response.
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param intArr - the field value(s)
	 */
	public static void appendIntegerArrayAsArray(StringBuilder sb, String name, Integer[] intArr){
		appendIntegerArrayAsArray(sb, name, intArr, false);
	}
	
	/**
	 * Appends a list of integers from an int array. Example: <code>fieldName:[1234, 5678, 9123],</code>.</br>
	 * Note: There is always a comma added in the end, so this element cannot be the
	 * last in a JSON response.
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param intArr - the field value(s)
	 */
	public static void appendIntArrayAsArray(StringBuilder sb, String name, int[] intArr){
		if (intArr!=null && intArr.length>0){
			appendFieldName(sb, name).append(":[");
			for (int i = 0; i < intArr.length; i++) {
				int intValue = intArr[i];
				sb.append(intValue);
				if (i<intArr.length-1) {
					sb.append(",");
				}
			}			
			sb.append("]");
			sb.append(",");
		}
	}
	
	/**
	 * Appends a list of integers from an object array. Example: <code>fieldName:[1234, 5678, 9123],</code>.</br>
	 * Note: There is always a comma added in the end, so this element cannot be the
	 * last in a JSON response.
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param intArr - the field value(s)
	 */
	public static void appendObjectArrayAsArray(StringBuilder sb, String name, Object[] intArr){
		if (intArr!=null && intArr.length>0){
			appendFieldName(sb, name).append(":[");
			for (int i = 0; i < intArr.length; i++) {
				Object value = intArr[i];
				if (value!=null) {
					sb.append(value.toString());
					if (i<intArr.length-1) {
						sb.append(",");
					}
				}
			}
			sb.append("]");
			sb.append(",");
		}
	}
	public static String encodeIntegerArrayAsArray(Integer[] intArr){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if (intArr!=null && intArr.length>0){
			for (int i = 0; i < intArr.length; i++) {
				Integer integerValue = intArr[i];
				if (integerValue!=null) {
					sb.append(integerValue);
				}
				if (i<intArr.length-1) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return  sb.toString();
	}
	public static String encodeObjectArrayAsArray(Object[] intArr){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if (intArr!=null && intArr.length>0){
			for (int i = 0; i < intArr.length; i++) {
				Object value = intArr[i];
				if (value!=null) {
					sb.append(value);
				}
				if (i<intArr.length-1) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return  sb.toString();
	}
	
	/**
	 * Appends a list of integers from an integer list. Example: <code>fieldName:1234, 5678, 9123,</code>.</br>
	 * Note: There is always a comma added in the end, so this element cannot be the
	 * last in a JSON response.
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 */
	public static void appendIntegerArrayAsCommaSplittedString(StringBuilder sb, String name, List<Integer> integerList){
		if (integerList!=null && !integerList.isEmpty()){
			appendFieldName(sb, name).append(":\"");
			for (Iterator<Integer> iterator = integerList.iterator(); iterator.hasNext();) {
				Integer integerValue = iterator.next();
				if (integerValue!=null) {
					sb.append(integerValue);
				}
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
			sb.append("\"");
			sb.append(",");
		}
	}	
	
	
	/**
	 * Appends a double type field. Example: <code>fieldName:12.08,</code>.</br>
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param value - the field value
	 */
	public static void appendDoubleValue(StringBuilder sb, String name, Double value){
		appendDoubleValue(sb, name, value,false);
	}
	
	
	/**
	 * Appends a double type field. Example: <code>fieldName:12.08,</code>.</br>
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param value - the field value
	 * @param last - if true, no comma is added as the last character, otherwise there will be a comma
	 */
	public static void appendDoubleValue(StringBuilder sb, String name, Double value, boolean last){
		if(value!=null){
			//appendFieldName(sb, name).append(":'").append(DoubleNumberFormatUtil.formatISO(value)).append("'");
			appendFieldName(sb, name).append(":").append(value.toString()).append("");
			if(!last) {
				sb.append(",");
			}
		}
	}
	
	/**
	 * Appends a localized date type field. Example: <code>fieldName:"11.8.2012",</code>.</br>
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param date - the field value
	 * @param locale - the locale used to format the date
	 */
	public static void appendLocaleFormattedDoubleValue(StringBuilder sb, String name, Double doubleVal, Locale locale) {
		appendLocaleFormattedDoubleValue(sb, name, doubleVal, locale, false);		
	}

	/**
	 * Appends a localized date type field. Example: <code>fieldName:"11.8.2012",</code>.</br>
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param date - the field value
	 * @param locale - the locale used to format the date
	 * @param last - if true, no comma is added as the last character, otherwise there will be a comma
	 */
	public static void appendLocaleFormattedDoubleValue(StringBuilder sb, String name, Double doubleVal, Locale locale, boolean last){
		if(doubleVal!=null) {
			appendFieldName(sb, name).append(":").append("\"").append(DoubleWithDecimalDigitsNumberFormatUtil.getInstance(2).formatGUI(doubleVal, locale)).append("\"");
			if(!last) {
				sb.append(",");
			}
		}
	}
	
	/**
	 * Appends a localized date type field. Example: <code>fieldName:"11.8.2012",</code>.</br>
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param date - the field value
	 * @param locale - the locale used to format the date
	 */
	public static void appendLocaleFormattedPercentValue(StringBuilder sb, String name, Double doubleVal, Locale locale) {
		appendLocaleFormattedPercentValue(sb, name, doubleVal, locale, false);		
	}

	/**
	 * Appends a localized date type field. Example: <code>fieldName:"11.8.2012",</code>.</br>
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param date - the field value
	 * @param locale - the locale used to format the date
	 * @param last - if true, no comma is added as the last character, otherwise there will be a comma
	 */
	public static void appendLocaleFormattedPercentValue(StringBuilder sb, String name, Double doubleVal, Locale locale, boolean last){
		if(doubleVal!=null) {
			appendFieldName(sb, name).append(":").append("\"").append(PercentNumberFormatUtil.getInstance().formatGUI(doubleVal, locale)).append("\"");
			if(!last) {
				sb.append(",");
			}
		}
	}
	
	/**
	 * Appends a date type field. Example: <code>fieldName:"2012-08-11",</code>.</br>
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param value - the field value
	 */
	public static void appendDateValue(StringBuilder sb, String name, Date value){
		appendDateValue(sb, name, value, false);
	}
	
	/**
	 * Appends a date type field. Example: <code>fieldName:"2012-08-11",</code>.</br>
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param value - the field value
	 * @param last - if true, no comma is added as the last character, otherwise there will be a comma
	 */
	public static void appendDateValue(StringBuilder sb, String name, Date value, boolean last){
		if(value!=null){
			appendFieldName(sb, name).append(":").append("\"").append(DateTimeUtils.getInstance().formatISODate(value)).append("\"");
			if(!last) {
				sb.append(",");
			}
		}
	}
	
	/**
	 * Appends a date time type field. Example: <code>fieldName:"2012-08-11 12:27",</code>.</br>
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param value - the field value
	 */
	public static void appendDateTimeValue(StringBuilder sb, String name, Date value){
		appendDateTimeValue(sb, name, value, false);
	}
	
	/**
	 * Appends a date time type field. Example: <code>fieldName:"2012-08-11 12:27",</code>.</br>
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param value - the field value
	 * @param last - if true, no comma is added as the last character, otherwise there will be a comma
	 */
	public static void appendDateTimeValue(StringBuilder sb, String name, Date value, boolean last){
		if(value!=null){
			appendFieldName(sb, name).append(":").append("\"").append(DateTimeUtils.getInstance().formatISODateTime(value)).append("\"");
			if(!last) {
				sb.append(",");
			}
		}
	}
	
	/**
	 * Appends a localized date type field. Example: <code>fieldName:"11.8.2012",</code>.</br>
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param date - the field value
	 * @param locale - the locale used to format the date
	 */
	public static void appendLocaleFormattedDateValue(StringBuilder sb, String name, Date date, Locale locale) {
		appendLocaleFormattedDateValue(sb, name, date, locale, false);		
	}

	/**
	 * Appends a localized date type field. Example: <code>fieldName:"11.8.2012",</code>.</br>
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param date - the field value
	 * @param locale - the locale used to format the date
	 * @param last - if true, no comma is added as the last character, otherwise there will be a comma
	 */
	public static void appendLocaleFormattedDateValue(StringBuilder sb, String name, Date date, Locale locale, boolean last){
		if(date!=null){
			appendFieldName(sb, name).append(":").append("\"").append(DateTimeUtils.getInstance().formatGUIDate(date, locale)).append("\"");
			if(!last) {
				sb.append(",");
			}
		}
	}
	
	
	/**
	 * Appends a boolean type field. Example: <code>fieldName:true,</code>.</br>
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param value - the field value
	 */
	public static void appendBooleanValue(StringBuilder sb, String name, boolean value){
		appendBooleanValue(sb, name, value, false);
	}
	
	/**
	 * Appends a boolean type field. Example: <code>fieldName:true,</code>.</br>
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param value - the field value
	 * @param last - if true, no comma is added as the last character, otherwise there will be a comma
	 */
	public static void appendBooleanValue(StringBuilder sb, String name, boolean value, boolean last){
		appendFieldName(sb, name).append(":").append(value);
		if(!last){
			sb.append(",");
		}
	}
	
	/**
	 * Returns a JSON string constructed from a list of {@link com.aurel.track.beans.ILabelBean ILabelBean}. 
	 * Example: <code>fieldName:[id:12345, label:"first label", id:3456, label: "second label],</code>.</br>
	 * @param name - the field name
	 * @param labelBeanList - the field value(s)
	 */
	public static String createILabelBeanListJSON(String name, List<ILabelBean> labelBeanList){
		StringBuilder stringBuilder = new StringBuilder();
		JSONUtility.appendILabelBeanList(stringBuilder, name, labelBeanList);
		return stringBuilder.toString();
	}
	
	/**
	 * Returns a JSON string constructed from a list of {@link com.aurel.track.beans.ILabelBean ILabelBean}. 
	 * Example: <code>fieldName:[id:12345, label:"first label", id:3456, label: "second label],</code>.</br>
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param list - the field value(s)
	 */
	public static void appendILabelBeanList(StringBuilder sb, String name, List<ILabelBean> list){
		appendILabelBeanList(sb, name, list, false);
	}
	
	/**
	 * Appends a JSON string constructed from a list of {@link com.aurel.track.beans.ILabelBean ILabelBean} to
	 * a <code>StringBuilder</code>.</br> 
	 * Example: <code>fieldName:[id:12345, label:"first label", id:3456, label: "second label],</code>.</br>
	 * @param sb - the field name and value are appended to this <code>StringBuilder</code>
	 * @param name - the field name
	 * @param list - the field value(s)
	 * @param last - if true, no comma is added as the last character, otherwise there will be a comma
	 */
	public static void appendILabelBeanList(StringBuilder sb, String name, List<ILabelBean> list, boolean last){
		appendFieldName(sb, name).append(":");
		sb.append(encodeILabelBeanList(list));
		if(!last){
			sb.append(",");
		}
	}
	public static String encodeILabelBeanList(List<ILabelBean> list){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(list!=null && !list.isEmpty()){
			ILabelBean labelBean;
			for (Iterator<ILabelBean> iterator = list.iterator(); iterator.hasNext();) {
				labelBean = iterator.next();
				sb.append("{");
				if(labelBean.getLabel()!=null){
					sb.append("\""  + JSON_FIELDS.LABEL + "\"").append(":\"").append(escape(labelBean.getLabel())).append("\",");
				}
				sb.append("\"" + JSON_FIELDS.ID + "\"").append(":").append(labelBean.getObjectID());
				sb.append("}");
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Append the JSON for a list with icon
	 * @param stringBuilder
	 * @param datasourceName
	 * @param fieldID
	 * @param labelBeanList
	 * @param last
	 * @return
	 */
	public static String appendILabelBeanListWithIcons(StringBuilder stringBuilder, Integer fieldID, ILookup lookup, String datasourceName, List<ILabelBean> labelBeanList, boolean last) {
		//ILookup lookup = null;
		boolean dynamicIcons = false;
		boolean isCustom = false;
		IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
		if (fieldTypeRT!=null && fieldTypeRT.isLookup()) {
			lookup = (ILookup)fieldTypeRT;
			dynamicIcons = lookup.hasDynamicIcons();
			isCustom = fieldTypeRT.isCustom();
		}
		//appendFieldName(stringBuilder, fieldName).append(":{");
		if (lookup!=null) {
			if (dynamicIcons) {
				Integer iconFieldID = fieldID;
				if (!isCustom) {
					//the system fields for icons are negated
					iconFieldID = -fieldID;
				}
				JSONUtility.appendILabelBeanList(stringBuilder, datasourceName, labelBeanList);
				String iconUrlPrefix = "optionIconStream.action?fieldID=" + iconFieldID +"&optionID=";
				JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.ICON_URL_PREFIX, iconUrlPrefix, last);
			} else {
				JSONUtility.appendJSONValue(stringBuilder, datasourceName, encodeListWithIconCls(lookup, labelBeanList));
				JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.USE_ICON_CLS, true, last);
			}
		}
		//stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * Encode 
	 * @param fieldID
	 * @param labelBeanList
	 * @return
	 */
	public static String encodeListWithIconCls(ILookup lookup, List<ILabelBean> labelBeanList){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(labelBeanList!=null){
			if (lookup!=null) {
				for (Iterator<ILabelBean> iterator = labelBeanList.iterator(); iterator.hasNext();) {
					ILabelBean labelBean = iterator.next();
					sb.append("{");
					JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.LABEL, labelBean.getLabel());
					JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ICONCLS, lookup.getIconCls(labelBean));
					JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.ID, labelBean.getObjectID(), true);
					sb.append("}");
					if (iterator.hasNext()) {
						sb.append(",");
					}
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * Gets the JSON string for tree folder children 
	 * @param children
	 * @return
	 */
	public static String getChildrenJSON(List<TreeNodeBaseTO> children){
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("[");
		for (Iterator<TreeNodeBaseTO> iterator = children.iterator(); iterator.hasNext();) {
			TreeNodeBaseTO projectTypeTreeNodeTO = iterator.next();
			stringBuilder.append("{");
			JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ID, projectTypeTreeNodeTO.getId());
			JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.TEXT, projectTypeTreeNodeTO.getText());
			JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ICONCLS, projectTypeTreeNodeTO.getIconCls());
			JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ICON, projectTypeTreeNodeTO.getIcon());
			JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.LEAF, projectTypeTreeNodeTO.isLeaf(), true);
			stringBuilder.append("}");
			if (iterator.hasNext()) {
				stringBuilder.append(",");
			}
		}
		stringBuilder.append("]");//end data
		return stringBuilder.toString();
	}
	
	
	/**
	 * Get JSON with the tree hierarchy: supposes the nodes are already sorted
	 * @param nodes
	 * @return
	 */
	public static String getTreeHierarchyJSON(List<TreeNode> nodes) {
		return getTreeHierarchyJSON(nodes, false, false, null);
	}	
	
	/**
	 * Get JSON with the tree hierarchy: supposes the nodes are already sorted
	 * @param nodes
	 * @return
	 */
	public static String getTreeHierarchyJSON(List<TreeNode> nodes, boolean useCheck, boolean useSelectable) {
		return getTreeHierarchyJSON(nodes, useCheck, useSelectable, null);
	}
	
	public static String getTreeHierarchyJSON(List<TreeNode> nodes, boolean useCheck, boolean useSelectable, Integer level) {
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("[");
		if(nodes!=null){
			for (Iterator<TreeNode> iterator = nodes.iterator(); iterator.hasNext();) {
				TreeNode treeNode = iterator.next();
				stringBuilder.append("{");
				JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ID, treeNode.getId());
				JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.TEXT, treeNode.getLabel());
				JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ICONCLS, treeNode.getIcon());				
				if(level!=null){
					JSONUtility.appendStringValue(stringBuilder,"cls", "treeItem-level-"+level);
				}
				if (useCheck && treeNode.getChecked()!=null) {
					JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.CHECKED, treeNode.getChecked());
				}
				if (useSelectable) {
					JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.SELECTABLE, treeNode.isSelectable());
				}
				if (treeNode.getLeaf()) {
					JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.LEAF, treeNode.getLeaf(), true);
				} else {
					JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.LEAF, treeNode.getLeaf());
					List<TreeNode> children = treeNode.getChildren();
					if (children!=null && !children.isEmpty()) {
						JSONUtility.appendJSONValue(stringBuilder, JSONUtility.JSON_FIELDS.CHILDREN,
								getTreeHierarchyJSON(children, useCheck, useSelectable, level==null?null:1), true);
					} else {
						//initialize the children with empty array (null would trigger a request to the server to get the children)
						JSONUtility.appendJSONValue(stringBuilder, JSONUtility.JSON_FIELDS.CHILDREN, "[]",true);
					}
				}
				stringBuilder.append("}");
				if (iterator.hasNext()) {
					stringBuilder.append(",");
				}
			}
		}
		stringBuilder.append("]");//end data
		return stringBuilder.toString();
	}
	
	public static void appendIssueTypeBeanList(StringBuilder sb, String name, List<TListTypeBean> list){
		appendIssueTypeBeanList(sb, name, list, false);
	}

	/**
	 * Appends a list of ILabelBeans
	 * @param sb
	 * @param name
	 * @param list
	 * @param last
	 */
	public static void appendIssueTypeBeanList(StringBuilder sb, String name, List<TListTypeBean> list, boolean last){
		appendFieldName(sb, name).append(":[");
		if(list!=null){
			for (Iterator<TListTypeBean> iterator = list.iterator(); iterator.hasNext();) {
				TListTypeBean labelBean = iterator.next();
				sb.append("{");
				appendIntegerValue(sb, JSON_FIELDS.ID, labelBean.getObjectID());
				appendStringValue(sb, JSON_FIELDS.SYMBOL, labelBean.getSymbol());
				appendStringValue(sb, JSON_FIELDS.LABEL, labelBean.getLabel(), true);
				sb.append("}");
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		if(!last){
			sb.append(",");
		}
	}
	
	
	
	/**
	 * Appends a list of IntegerStringBean
	 * @param sb
	 * @param name
	 * @param list
	 */
	public static void appendIntegerStringBeanList(StringBuilder sb, String name, List<IntegerStringBean> list) {
		appendIntegerStringBeanList(sb, name, list, false);
	}
	
	/**
	 * Appends a list of IntegerStringBean
	 * @param sb
	 * @param name
	 * @param list
	 * @param last
	 */
	public static void appendIntegerStringBeanList(StringBuilder sb, String name, List<IntegerStringBean> list, boolean last) {
		appendJSONValue(sb,name,encodeJSONIntegerStringBeanList(list),last);
	}
	
	/**
	 * Appends a list of IntegerStringBean
	 * @param sb
	 * @param name
	 * @param list
	 */
	public static void appendBooleanStringBeanList(StringBuilder sb, String name, List<BooleanStringBean> list) {
		appendBooleanStringBeanList(sb, name, list, false);
	}
	
	/**
	 * Appends a list of IntegerStringBean
	 * @param sb
	 * @param name
	 * @param list
	 * @param last
	 */
	public static void appendBooleanStringBeanList(StringBuilder sb, String name, List<BooleanStringBean> list, boolean last){
		appendJSONValue(sb,name,encodeJSONBooleanStringBeanList(list),last);
	}
	
	/**
	 * Appends a list of LabelValueBean
	 * @param sb
	 * @param name
	 * @param list
	 */
	public static void appendLabelValueBeanList(StringBuilder sb, String name, List<LabelValueBean> list){
		appendLabelValueBeanList(sb, name, list, false);
	}
	
	/**
	 * Appends a list of LabelValueBean
	 * @param sb
	 * @param name
	 * @param list
	 * @param last
	 */
	public static void appendLabelValueBeanList(StringBuilder sb, String name, List<LabelValueBean> list, boolean last){
		appendJSONValue(sb,name,encodeJSONLabelValueBeanList(list),last);
	}
	
	/**
	 * Appends
	 * @param sb
	 * @param name
	 * @param map
	 */
	public static void appendIntegerStringMap(StringBuilder sb, String name, Map<Integer, String> map){
		appendIntegerStringMap(sb, name, map, false);
	}
	
	/**
	 * Appends
	 * @param sb
	 * @param name
	 * @param map
	 * @param last
	 */
	public static void appendIntegerStringMap(StringBuilder sb, String name, Map<Integer, String> map, boolean last){		
		appendFieldName(sb, name).append(":[");
		if(map!=null){
			for (Iterator<Integer> iterator = map.keySet().iterator(); iterator.hasNext();) {
				Integer key = iterator.next();
				sb.append("{");
				appendIntegerValue(sb, JSON_FIELDS.ID, key);
				appendStringValue(sb, JSON_FIELDS.LABEL, map.get(key), true);
				sb.append("}");
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		if(!last){
			sb.append(",");
		}
	}

	/**
	 * Appends
	 * @param sb
	 * @param name
	 * @param map
	 */
	public static void appendStringParametersMap(StringBuilder sb, String name, Map<String, String> map){
		appendStringParametersMap(sb, name, map, false);
	}

	/**
	 * Appends
	 * @param sb
	 * @param name
	 * @param map
	 * @param last
	 */
	public static void appendStringParametersMap(StringBuilder sb, String name, Map<String, String> map, boolean last){
		appendFieldName(sb, name).append(":{");
		if(map!=null){
			for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next();
				appendStringValue(sb, key, map.get(key), true);
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("}");
		if(!last){
			sb.append(",");
		}
	}

	/**
	 * Appends
	 * @param sb
	 * @param name
	 * @param map
	 */
	public static void appendIntegerBooleanMap(StringBuilder sb, String name, Map<Integer, Boolean> map){		
		appendIntegerBooleanMap(sb, name, map, false);			
	}
	
	/**
	 * Appends
	 * @param sb
	 * @param name
	 * @param map
	 * @param last
	 */
	public static void appendIntegerBooleanMap(StringBuilder sb, String name, Map<Integer, Boolean> map, boolean last){		
		appendFieldName(sb, name).append(":{");
		if(map!=null){
			for (Iterator<Integer> iterator = map.keySet().iterator(); iterator.hasNext();) {
				Integer key = iterator.next();				
				sb.append(key);
				sb.append(":");
				sb.append(map.get(key));				
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("}");
		if(!last){
			sb.append(",");
		}
	}
	
	
	public static void appendStringList(StringBuilder sb, String name, List<String> list){		
		appendStringList(sb, name, list, false);
	}
	
	public static void appendStringList(StringBuilder sb, String name, List<String> list, boolean last){
		appendFieldName(sb, name);
		sb.append(":[");
		if(list!=null){
            try {
                for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
                    String string = iterator.next();
                    sb.append("\"").append(escape(string)).append("\"");
                    if (iterator.hasNext()) {
                        sb.append(",");
                    }
                }
            } catch(ConcurrentModificationException exception) {
                //if logs are added in the memory appender during serializing the existing logs in serverStatus
                LOGGER.debug("Creating the string list JSON failed with " + exception.getMessage());
            }
		}
		sb.append("]");
		if(!last){
			sb.append(",");
		}
	}
	
	/**
	 * Creates the JSON with replacement list
	 * @param success
	 * @param label the label of the entity to be deleted
	 * @param replacementWarningLabel the entity type to be deleted 
	 * @param replacementFieldLabel the label for the replacement combo 
	 * @param replacementList the replacement list
	 * @param errorMessage when no replacement entry was selected 
	 * @param locale
	 * @return
	 */
	public static String createReplacementListJSON(boolean success, String label, String replacementWarningLabel, 
			String replacementFieldLabel, List<ILabelBean> replacementList, String errorMessage, Locale locale){	
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, success);		
		sb.append(JSONUtility.JSON_FIELDS.DATA).append(":{");
		if (errorMessage!=null) {
			JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ERROR_MESSAGE, errorMessage);
		}
		String warning = LocalizeUtil.getParametrizedString("common.lbl.replacementWarning", 
				new Object[] {replacementWarningLabel, label}, locale);
		warning = warning + " " +  LocalizeUtil.getLocalizedTextFromApplicationResources("common.lbl.cancelDeleteAlert", locale);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.REPLACEMENT_WARNING, warning);
		String listLabel = LocalizeUtil.getParametrizedString("common.lbl.replacement", 
				new Object[] {replacementFieldLabel}, locale);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.REPLACEMENT_LIST_LABEL, listLabel);
		JSONUtility.appendILabelBeanList(sb, JSONUtility.JSON_FIELDS.REPLACEMENT_LIST, replacementList, true);		
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Creates the JSON with replacement list by deleting more entities at once
	 * @param success
	 * @param label the label of the entity to be deleted
	 * @param replacementWarningLabel the entity type to be deleted 
	 * @param replacementFieldLabel the label for the replacement combo 
	 * @param replacementList the replacement list
	 * @param errorMessage when no replacement entry was selected 
	 * @param locale
	 * @return
	 */
	public static String createReplacementListJSON(boolean success, int totalNumber, String entities, String replacementEntity, 
			List<ILabelBean> replacementList, String errorMessage, Locale locale){	
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, success);		
		sb.append(JSONUtility.JSON_FIELDS.DATA).append(":{");
		if (errorMessage!=null) {
			JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ERROR_MESSAGE, errorMessage);
		}
		String warning = LocalizeUtil.getParametrizedString("common.lbl.replacementWarningMore", 
				new Object[] {String.valueOf(totalNumber), entities, replacementEntity}, locale);
		warning = warning + " " + LocalizeUtil.getLocalizedTextFromApplicationResources("common.lbl.cancelDeleteAlert", locale);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.REPLACEMENT_WARNING, warning);
		String listLabel = LocalizeUtil.getParametrizedString("common.lbl.replacement", 
				new Object[] {replacementEntity}, locale);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.REPLACEMENT_LIST_LABEL, listLabel);
		JSONUtility.appendILabelBeanList(sb, JSONUtility.JSON_FIELDS.REPLACEMENT_LIST, replacementList, true);		
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	
    /**
     * Creates the JSON with replacement list
     * @param success
     * @param label the label of the entity to be deleted
     * @param replacementWarningLabel the entity type to be deleted
     * @param replacementFieldLabel the label for the replacement combo
     * @param replacementTree the replacement list
     * @param errorMessage when no replacement entry was selected
     * @param locale
     * @return
     */
    public static String createReplacementTreeJSON(boolean success, String label, String replacementWarningLabel,
                                                   String replacementFieldLabel, String replacementTree, String errorMessage, Locale locale){
        StringBuilder sb=new StringBuilder();
        sb.append("{");
        JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, success);
        sb.append(JSONUtility.JSON_FIELDS.DATA).append(":{");
        if (errorMessage!=null) {
            JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ERROR_MESSAGE, errorMessage);
        }
        String warning = LocalizeUtil.getParametrizedString("common.lbl.replacementWarning",
                new Object[] {replacementWarningLabel, label}, locale);
        warning = warning + " " + LocalizeUtil.getLocalizedTextFromApplicationResources("common.lbl.cancelDeleteAlert", locale);
        JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.REPLACEMENT_WARNING, warning);
        String listLabel = LocalizeUtil.getParametrizedString("common.lbl.replacement",
                new Object[] {replacementFieldLabel}, locale);
        JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.REPLACEMENT_LIST_LABEL, listLabel);
        JSONUtility.appendJSONValue(sb, JSONUtility.JSON_FIELDS.REPLACEMENT_TREE, replacementTree, true);
        sb.append("}");
        sb.append("}");
        return sb.toString();
    }

	public static void encodeJSON(HttpServletResponse httpServletResponse, String text, boolean setContentType) {
		try {
			JSONUtility.prepareServletResponseJSON(httpServletResponse, setContentType);
			PrintWriter out = httpServletResponse.getWriter();
			out.println(text);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
	}
	
	public static void encodeJSON(HttpServletResponse httpServletResponse, String text) {
		try {
			JSONUtility.prepareServletResponseJSON(httpServletResponse, true);
			PrintWriter out = httpServletResponse.getWriter();
			out.println(text);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
	}
	
	public static void prepareServletResponseJSON(HttpServletResponse httpServletResponse){
		prepareServletResponseJSON(httpServletResponse, true);
	}
	
	public static void prepareServletResponseJSON(HttpServletResponse httpServletResponse, boolean jsonContentType){
		try {
			httpServletResponse.reset();
			httpServletResponse.setCharacterEncoding("UTF-8");
		} catch (Exception e) {
			//when the <s:action/> is present on the jsp 
			//(mainly getting root nodes the same response object is used, and second time it can't be reseted)
		}
		if (jsonContentType) {
			httpServletResponse.setContentType("application/json");
		} else {
			httpServletResponse.setContentType("text/html");
		}
		//httpServletResponse.setContentType("text/javascript");
		DownloadUtil.prepareCacheControlHeader(ServletActionContext.getRequest(), httpServletResponse);
		/*DetectBrowser dtb = new DetectBrowser();
		dtb.setRequest(ServletActionContext.getRequest());
		// Workaround for a IE bug
		if (dtb.getIsIE()) {
			httpServletResponse.setHeader("Cache-Control", "private");
			httpServletResponse.addHeader("Expires", "0");
		} else {
			httpServletResponse.setHeader("Cache-Control", "no-cache");
		}*/
	}
	
	/**
	 * Gets a JSON success with node
	 * @param node 
	 * @return
	 */
	public static String encodeJSONSuccessAndNode(String node){	
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NODE, node, true);
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Gets the JSON success with id
	 * @param id
	 * @return
	 */
	public static String encodeJSONSuccessAndID(Integer id){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.ID, id, true);
		sb.append("}");
		return sb.toString();
	}
	
	public static String encodeJSONSuccess(boolean success){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		appendBooleanValue(sb, JSON_FIELDS.SUCCESS, success, true);
		sb.append("}");
		return sb.toString();
	}
	
	public static String encodeJSONSuccess(){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		appendBooleanValue(sb, JSON_FIELDS.SUCCESS, true);
		sb.append("\"data\":{}");
		sb.append("}");
		return sb.toString();
	}
	
	public static String encodeJSONSuccess(String confirmationMessage){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		appendBooleanValue(sb, JSON_FIELDS.SUCCESS, true);	
		appendStringValue(sb, JSON_FIELDS.ERROR_MESSAGE, confirmationMessage, true);
		sb.append("}");
		return sb.toString();
	}

	public static void encodeJSONSuccess(HttpServletResponse httpServletResponse) {
		encodeJSONSuccess(httpServletResponse,true);
	}
	
	public static void encodeJSONSuccess(HttpServletResponse httpServletResponse, boolean jsonContentType) {
		try {
			JSONUtility.prepareServletResponseJSON(httpServletResponse, jsonContentType);
			PrintWriter out = httpServletResponse.getWriter();
			out.println(JSONUtility.encodeJSONSuccess());
		} catch (IOException e) {
			// nothing much can be done here
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
	}
	public static String encodeJSONFailure(String errorMessage){
		return encodeJSONFailure(errorMessage,null);
	}

	
	
	
	public static String encodeJSONFailure(Integer errorCode){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		if(errorCode!=null){
			appendIntegerValue(sb, JSON_FIELDS.ERROR_CODE, errorCode);
		}
		appendBooleanValue(sb, JSON_FIELDS.SUCCESS, false, true);
		sb.append("}");
		return sb.toString();
	}
	
	public static String encodeJSONFailure(String errorMessage, Integer errorCode){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		if(errorCode!=null){
			appendIntegerValue(sb, JSON_FIELDS.ERROR_CODE, errorCode);
		}
		appendStringValue(sb, JSON_FIELDS.ERROR_MESSAGE, errorMessage);
		appendBooleanValue(sb, JSON_FIELDS.SUCCESS, false, true);
		sb.append("}");
		return sb.toString();
	}
	public static void encodeJSONFailure(HttpServletResponse httpServletResponse,String errorMessage) {
		encodeJSONFailure(httpServletResponse, errorMessage,null);
	}
	public static void encodeJSONFailure(HttpServletResponse httpServletResponse,String errorMessage,Integer errorCode) {
		encodeJSONFailure(httpServletResponse,errorMessage,errorCode,true);
	}
	public static void encodeJSONFailure(HttpServletResponse httpServletResponse,String errorMessage,Integer errorCode,boolean jsonContentType) {
		try {
			JSONUtility.prepareServletResponseJSON(httpServletResponse, jsonContentType);
			PrintWriter out = httpServletResponse.getWriter();
			out.println(JSONUtility.encodeJSONFailure(errorMessage,errorCode));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
	}
	
	public static String encodeJSONBoolean(boolean booleanValue){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		appendBooleanValue(sb, JSON_FIELDS.SUCCESS, true);
		appendBooleanValue(sb, JSON_FIELDS.VALUE, booleanValue,true);
		sb.append("}");
		return sb.toString();
	}
	
	public static String encodeJsonErrors(List<LabelValueBean> fieldNameToErrorMessageList) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		appendBooleanValue(sb, JSON_FIELDS.SUCCESS, false);
		appendLabelValueBeanList(sb, JSON_FIELDS.ERRORS, fieldNameToErrorMessageList, true);
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Renders the error messages to controls by name
	 * errorData.getFieldName() should be specified
	 * @param errorDataList
	 * @param locale
	 * @return
	 */
	public static String encodeErrorDataList(List<ErrorData> errorDataList, Locale locale) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		appendBooleanValue(sb, JSON_FIELDS.SUCCESS, false);
		appendFieldName(sb, JSON_FIELDS.ERRORS).append(":{");
		if (errorDataList!=null){
			for (Iterator<ErrorData> iterator = errorDataList.iterator(); iterator.hasNext();) {
				ErrorData errorData = iterator.next();
				if (errorData.getFieldName()!=null) {
					appendStringValue(sb, errorData.getFieldName(), ErrorHandlerJSONAdapter.createMessage(errorData, locale), true);
				}
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	
	
	public static String encodeJsonError(String fieldName, String errorMessage) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		appendBooleanValue(sb, JSON_FIELDS.SUCCESS, false);
		sb.append(JSON_FIELDS.ERRORS +":{");
		JSONUtility.appendStringValue(sb, fieldName, errorMessage, true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Method to create a JSON response object in case that there have been problems
	 * found in validation process.
	 * 
	 * @param httpServletResponse the response the JSON object has to be embedded in
	 * @param errors a list of label value beans. Label is error message, value is field name.
	 */
	public static void encodeJSONErrors(HttpServletResponse httpServletResponse,List<LabelValueBean> errors){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		appendBooleanValue(sb, JSON_FIELDS.SUCCESS, false);
		appendLabelValueBeanList(sb, JSON_FIELDS.ERRORS, errors,true);
		sb.append("}");
		try {
			JSONUtility.prepareServletResponseJSON(httpServletResponse, true);
			PrintWriter out = httpServletResponse.getWriter();
			out.println(sb);
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
	}
	public static void encodeJSONErrorsExtJS(HttpServletResponse httpServletResponse,List<LabelValueBean> errors,boolean jsonContentType){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		appendBooleanValue(sb, JSON_FIELDS.SUCCESS, false);

		appendErrorsExtJS(sb,errors,true);
		sb.append("}");
		try {
			JSONUtility.prepareServletResponseJSON(httpServletResponse, jsonContentType);
			PrintWriter out = httpServletResponse.getWriter();
			out.println(sb);
		} catch (IOException e) {
			LOGGER.error(Support.readStackTrace(e));
		}
	}
	public static void appendErrorsExtJS(StringBuilder sb,List<LabelValueBean> errors){
		appendErrorsExtJS(sb,errors,false);
	}
	public static void appendErrorsExtJS(StringBuilder sb,List<LabelValueBean> errors,boolean last){
		StringBuilder sbErrors = new StringBuilder();
		sbErrors.append("{");
		if(errors!=null){
			for (Iterator<LabelValueBean> iterator = errors.iterator(); iterator.hasNext();) {
				LabelValueBean labelBean = iterator.next();
				JSONUtility.appendStringValue(sbErrors,labelBean.getValue(),labelBean.getLabel(),!iterator.hasNext());
			}
		}
		sbErrors.append("}");
		appendJSONValue(sb,"errors",sbErrors.toString(),true);
	}
	/**
	 * Method to create a JSON response object in case that there have been problems
	 * found in validation process.
	 * 
	 * @param httpServletResponse the response the JSON object has to be embedded in
	 * @param errors a list of label value beans. Label is error message, value is field name.
	 */
	public static void encodeJSONErrorsExtJS(HttpServletResponse httpServletResponse,List<LabelValueBean> errors){
		encodeJSONErrorsExtJS(httpServletResponse,errors,true);
	}

	
	public static String encodeJSONErrors(String clientCode, String message){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		sb.append("success: false,");
		sb.append("errors:[{");
		sb.append("clientCode: \"").append(escape(clientCode)).append("\",");
		sb.append("message: \"").append(escape( message)).append("\"");
		sb.append("}]");
		sb.append("}");
		return sb.toString();
	}
	
	public static String encodeJSONLabelValueBeanList(List<LabelValueBean> list){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(list!=null){
			for (Iterator<LabelValueBean> iterator = list.iterator(); iterator.hasNext();) {
				LabelValueBean labelBean = iterator.next();
				sb.append("{");
				boolean last = labelBean.getLabel() == null ? true : false;
				appendStringValue(sb, JSON_FIELDS.ID, labelBean.getValue(), last);				
				appendStringValue(sb, JSON_FIELDS.LABEL, labelBean.getLabel(), true);
				sb.append("}");
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	public static String encodeJSONLabelValueBeanArray(LabelValueBean[] list){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(list!=null){
			for (int i=0;i<list.length;i++) {
				LabelValueBean labelBean = list[i];
				sb.append("{");
				appendStringValue(sb, JSON_FIELDS.ID, labelBean.getValue());
				appendStringValue(sb, JSON_FIELDS.LABEL, labelBean.getLabel(), true);
				sb.append("}");
				if (i<list.length-1) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}
	public static void appendJSONLabelValueBeanMatrix(StringBuilder sb, String name, LabelValueBean[][] value){
		appendJSONLabelValueBeanMatrix(sb, name, value, false);
	}
	public static void appendJSONLabelValueBeanMatrix(StringBuilder sb, String name, LabelValueBean[][] value, boolean last){
		appendJSONValue(sb,name,encodeJSONLabelValueBeanMatrix(value),last);
	}
	public static String encodeJSONLabelValueBeanMatrix(LabelValueBean[][] matrix){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(matrix!=null){
			for (int i=0;i<matrix.length;i++) {
				LabelValueBean[] array=matrix[i];
				sb.append(encodeJSONLabelValueBeanArray(array));
				if (i<matrix.length-1) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}

	public static String encodeJSONIntegerStringBeanList(List<IntegerStringBean> list){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(list!=null){
			for (Iterator<IntegerStringBean> iterator = list.iterator(); iterator.hasNext();) {
				IntegerStringBean labelBean = iterator.next();
				sb.append("{");
				appendIntegerValue(sb, JSON_FIELDS.ID, labelBean.getValue());
				appendStringValue(sb, JSON_FIELDS.LABEL, labelBean.getLabel(), true);
				sb.append("}");
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	public static String encodeJSONBooleanStringBeanList(List<BooleanStringBean> list){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(list!=null){
			for (Iterator<BooleanStringBean> iterator = list.iterator(); iterator.hasNext();) {
				BooleanStringBean booleanStringBean = iterator.next();
				sb.append("{");
				appendBooleanValue(sb, JSON_FIELDS.ID, booleanStringBean.getValue());
				appendStringValue(sb, JSON_FIELDS.LABEL, booleanStringBean.getLabel(), true);
				sb.append("}");
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	public static String encodeJSONIntegerStringBooleanBeanList(List<IntegerStringBooleanBean> list){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(list!=null){
			for (Iterator<IntegerStringBooleanBean> iterator = list.iterator(); iterator.hasNext();) {
				IntegerStringBooleanBean labelBean = iterator.next();
				sb.append("{");
				appendIntegerValue(sb, JSON_FIELDS.ID, labelBean.getValue());
				appendStringValue(sb, JSON_FIELDS.LABEL, labelBean.getLabel());
				appendBooleanValue(sb, JSON_FIELDS.SELECTED, labelBean.isSelected(),true);
				sb.append("}");
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	
	
	
	public static String  encodeJSONToolbarItemsList(List<ToolbarItem> list){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(list!=null){
			ToolbarItem bean;
			for (int i = 0; i < list.size(); i++) {
				bean=list.get(i);
				if(i>0){
					sb.append(",");
				}
				sb.append("{");
				JSONUtility.appendIntegerValue(sb,"id",bean.getId());
				JSONUtility.appendJSONValue(sb,"jsonData",bean.getJsonData());
				appendBooleanValue(sb,"condition",bean.isCondition());
				//appendBooleanValue(sb,"submit",bean.isSubmit());
				appendStringValue(sb,"cssClass",bean.getCssClass());
				appendStringValue(sb,"labelKey",bean.getLabelKey());
				appendStringValue(sb,"tooltipKey",bean.getTooltipKey());
				appendStringValue(sb,"imageInactive",bean.getImageInactive());
				//appendBooleanValue(sb,"showItem",bean.isShowItem());
				//appendBooleanValue(sb,"placeholder",bean.isPlaceholder(),true);
				appendBooleanValue(sb,"isMore",bean.isMore(),true);
				sb.append("}");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	public static String  encodeJSONFileList(List<File> list){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(list!=null){
			File file;
			for (int i = 0; i < list.size(); i++) {
				file=list.get(i);
				if(i>0){
					sb.append(",");
				}
				sb.append("{");
				JSONUtility.appendStringValue(sb,"name",file.getName());
				JSONUtility.appendLongValue(sb,"length",file.length());
				JSONUtility.appendDateTimeValue(sb,"lastModified",new Date(file.lastModified()),true);
				sb.append("}");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * Decodes the records string into and id based map of JSON objects
	 * @param records
	 * @param idField
	 * @return
	 */
	public static Map<String, JSONObject> decodeToJsonRecordsMap(String records, String idField) {		
		JSON json = null;
		try {
			json = JSONSerializer.toJSON(records);
		} catch (JSONException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}		
		Map<String, JSONObject> jsonObjectsMap = new HashMap<String, JSONObject>();
		if (json == null) {
			return jsonObjectsMap;
		}
		if (json.isArray()) {
			JSONArray jsonArray = (JSONArray)json;
			Iterator<JSONObject> iterator = jsonArray.iterator();		
			while (iterator.hasNext()) {
				JSONObject jsonObject = iterator.next();		
				jsonObjectsMap.put(jsonObject.getString(idField), jsonObject);
			}						
		} else {
			if (!json.isEmpty()) {
				JSONObject jsonObject = (JSONObject)json;
				jsonObjectsMap.put(jsonObject.getString(idField), jsonObject);
			}
		}		
		return jsonObjectsMap;
	}
	
	/**
	 * Decodes the records string into and id based map of JSON objects
	 * @param records
	 * @return
	 */
	public static List<JSONObject> decodeToJsonRecordsList(String records) {
		JSON json = null;
		try {
			json = JSONSerializer.toJSON(records);
		} catch (JSONException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}		
		List<JSONObject> jsonObjectsList = new ArrayList<JSONObject>();
		if (json != null  && !json.equals(JSONNull.getInstance()) && !json.isEmpty()) {
			if (json.isArray()) {
				JSONArray jsonArray = (JSONArray)json;
				Iterator<JSONObject> iterator = jsonArray.iterator();
				while (iterator.hasNext()) {
					JSONObject jsonObject = iterator.next();	
					jsonObjectsList.add(jsonObject);
				}
			} else {
				JSONObject jsonObject = (JSONObject)json;
				jsonObjectsList.add(jsonObject);
			}
		}
		return jsonObjectsList;
	}
	
	/**
	 * Decodes the records string into and id based map of JSON objects
	 * @param jsonString
	 * @return
	 */
	public static JSONObject decodeToJsonObject(String jsonString) {		
		JSON json = null;		
		try {
			json = JSONSerializer.toJSON(jsonString);
		} catch (JSONException e) {
			LOGGER.error(e.getMessage(), e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}		
		
		if (json != null  && !json.equals(JSONNull.getInstance()) && !json.isEmpty()) {					
			return (JSONObject)json;					
		}
		return null; 
	}
	
	
	/**
	 * Appends a list of IntegerStringBean
	 * @param sb
	 * @param name
	 * @param list
	 */
	public static void appendIntegerStringBooleanBeanList(StringBuilder sb, String name, List<IntegerStringBooleanBean> list){		
		appendIntegerStringBooleanBeanList(sb, name, list, false);			
	}
	
	/**
	 * Appends a list of IntegerStringBean
	 * @param sb
	 * @param name
	 * @param list
	 * @param last
	 */
	public static void appendIntegerStringBooleanBeanList(StringBuilder sb, String name, List<IntegerStringBooleanBean> list, boolean last){		
		appendJSONValue(sb,name,encodeJSONIntegerStringBooleanBeanList(list),last);
	}
	
	
	public static String encodeIssueTypes(List<TListTypeBean> issueTypes){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(issueTypes!=null){
			for (Iterator<TListTypeBean> iterator = issueTypes.iterator(); iterator.hasNext();) {
				TListTypeBean listType = iterator.next();
				sb.append(encodeIssueType(listType));
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}
	public static String encodeIssueType(TListTypeBean issueType){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		if(issueType!=null){
			JSONUtility.appendIntegerValue(sb,JSONUtility.JSON_FIELDS.ID,issueType.getObjectID());
			JSONUtility.appendStringValue(sb,JSONUtility.JSON_FIELDS.LABEL,issueType.getLabel());
			JSONUtility.appendIntegerValue(sb,"sortOrder",issueType.getSortorder(),true);
		}
		sb.append("}");
		return sb.toString();
	}

	
	/**
	 * Appends a JSON encoded HTML list of error messages from LabelValueBean(s). The values are ignored,
	 * only the labels are being used. 
	 * @param sb StringBuilder where the text is appended
	 * @param name the name of the property (like <code>msg: 'the label'</code>)
	 * @param errors the list of errors as LabelValueBean objects
	 * @param last this is the last parameter in the JSON object
	 */
	public static void appendErrorsToHTMLList(StringBuilder sb, String name, List<LabelValueBean> errors, boolean last) {
		if (errors != null && errors.size() > 0) {
			StringBuilder msg = new StringBuilder();
			Iterator<LabelValueBean> ilb = errors.iterator();
			LabelValueBean lvb = null;
			msg.append("<ul>");
			while (ilb.hasNext()) {
				lvb = ilb.next();
				msg.append("<li>"+lvb.getLabel() + "</li>");
			}
			msg.append("</ul>");
			JSONUtility.appendStringValue(sb,name,msg.toString(),last);
		}	
	}
	
	/**
	 * This method properly escapes an special characters so that the returned String conforms
	 * to JSON notation.
     * @param s - Must not be null.
     */
    public static String escape(String s) {
    	StringBuilder sb = new StringBuilder();
                for(int i=0;i<s.length();i++){
                        char ch=s.charAt(i);
                        switch(ch){
                        /*http://json.org/ only control characters should be escaped
                        \"
                        \\
                        \/
                        \b
                        \f
                        \n
                        \r
                        \t*/
                        /*case '\'':
                            sb.append("\\\'");
                            break;*/
                        case '"':
                                sb.append("\\\"");
                                break;
                        case '\\':
                                sb.append("\\\\");
                                break;
                        case '\b':
                                sb.append("\\b");
                                break;
                        case '\f':
                                sb.append("\\f");
                                break;
                        case '\n':
                                sb.append("\\n");
                                break;
                        case '\r':
                                sb.append("\\r");
                                break;
                        case '\t':
                                sb.append("\\t");
                                break;
//                        case '/':
//                                sb.append("\\/");
//                                break;
                        default:
                //Reference: http://www.unicode.org/versions/Unicode5.1.0/
                                if((ch>='\u0000' && ch<='\u001F') || (ch>='\u007F' && ch<='\u009F') || (ch>='\u2000' && ch<='\u20FF')){
                                        String ss=Integer.toHexString(ch);
                                        sb.append("\\u");
                                        for(int k=0;k<4-ss.length();k++){
                                                sb.append('0');
                                        }
                                        sb.append(ss.toUpperCase());
                                }
                                else{
                                        sb.append(ch);
                                }
                        }
                }//for
                return sb.toString();
        }

}
