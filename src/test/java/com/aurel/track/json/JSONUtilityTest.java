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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TScreenBean;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.TreeNode;
import com.aurel.track.util.numberFormatter.PercentNumberFormatUtil;
import com.google.common.primitives.Ints;


public class JSONUtilityTest {

	private StringBuilder sb;
	private String key;
	private String value;
	private Integer integerValue;
	private Long longValue;
	private List<Integer> integerList;
	private Set<Integer> integerSet;
	private Integer[] integerArray;
	private int[] intArray;
	private Object[] objectArray;
	private Double doubleValue;
	private Locale locale;
	private Date dateVelue;
	private Boolean booleanValue;
	private List<ILabelBean> labelBeanListValue;
	private List<TreeNode> treeNodeValues;


	@Before
	public void init() {
		sb = new StringBuilder();
		key = new String("Key");
		value = new String("Value");
		integerValue = new Integer(10);
		longValue = new Long(1010);

		integerList = new ArrayList<Integer>();
		integerList.add(10);
		integerList.add(20);

		integerSet = new HashSet<Integer>();
		integerSet.add(10);
		integerSet.add(20);

		integerArray = new Integer[2];
		integerArray[0] = 10;
		integerArray[1] = 20;

		intArray = new int[2];
		intArray[0] = 10;
		intArray[1] = 20;

		objectArray = new Object[2];
		objectArray[0] = new MyBean("firstName1", "lastName1");
		objectArray[1] = new MyBean("firstName2", "lastName2");

		doubleValue = Double.valueOf(10.01);

		locale = new Locale.Builder().setLanguage("en").setRegion("US").build();

		dateVelue = new Date();

		booleanValue = Boolean.valueOf(true);

		labelBeanListValue = new ArrayList<ILabelBean>();
		TScreenBean scr = new TScreenBean();
		scr.setObjectID(1000);
		scr.setLabel("Label");
		TScreenBean scr1 = new TScreenBean();
		scr1.setLabel("Label");
		scr1.setObjectID(2000);
		labelBeanListValue.add(scr);
		labelBeanListValue.add(scr1);

		treeNodeValues = new ArrayList<TreeNode>();
		TreeNode firstNode =  new TreeNode();



	}

	@Test
	public void testGetPathInHelpWrapper() {
		List<String>result = JSONUtility.getPathInHelpWrapper("controlName");
		assertNotNull(result);
		assertFalse(result.isEmpty());
	}

	@Test
	public void testAppendFieldName() {
		JSONUtility.appendFieldName(sb, key);
		assertNotNull(sb);
		assertFalse(sb.length() == 0);
		assertFalse(!sb.toString().contains("\"" + key + "\""));
	}

	@Test
	public void testEscapeFieldName() {
		String  result = JSONUtility.escapeFieldName(key);
		assertNotNull(result);
		assertFalse(!result.contains("\"" + key + "\""));
	}



	@Test
	public void testAppendStringValue() {
		JSONUtility.appendStringValue(sb, key, value);
		assertNotNull(sb);
		assertFalse(sb.toString().length() == 0);
		assertFalse(!sb.toString().matches(".*" + "\"" + key + "\"" + "(\\s\\s)*" + ":" +  "(\\s\\s)*" + "\"" + value + "\",.*"));
	}

	@Test
	public void testAppendStringValueLastTrue() {
		JSONUtility.appendStringValue(sb, key, value, true);
		assertNotNull(sb);
		assertFalse(sb.toString().length() == 0);
		assertFalse(!sb.toString().matches(".*" + "\"" + key + "\"" + "(\\s\\s)*" + ":" +  "(\\s\\s)*" + "\"" + value + "\".*"));
	}

	@Test
	public void testAppendJSONValue() {
		value = "{\"jsonKey\":\"jsonValue\"}";
		String valueForRegexp = "\\{\"jsonKey\":\"jsonValue\"\\}";
		JSONUtility.appendJSONValue(sb, key, value);
		assertNotNull(sb);
		assertFalse(sb.toString().length() == 0);
		assertFalse(!sb.toString().matches(".*" + "\"" + key + "\"" + "(\\s\\s)*" + ":" + "(\\s\\s)*" + valueForRegexp +  ",.*"));
	}

	@Test
	public void testAppendJSONValueLastTrue() {
		value = "{\"jsonKey\":\"jsonValue\"}";
		String valueForRegexp = "\\{\"jsonKey\":\"jsonValue\"\\}";
		JSONUtility.appendJSONValue(sb, key, value, true);
		assertNotNull(sb);
		assertFalse(sb.toString().length() == 0);
		assertFalse(!sb.toString().matches(".*" + "\"" + key + "\"" + "(\\s\\s)*" + ":" + "(\\s\\s)*" + valueForRegexp +  ".*"));
	}

	@Test
	public void testAppendIntegerValue() {
		JSONUtility.appendIntegerValue(sb, key, integerValue);
		assertNotNull(sb);
		assertFalse(sb.toString().length() == 0);
		assertFalse(!sb.toString().matches(".*" + "\"" + key + "\"" + "(\\s\\s)*" + ":" + "(\\s\\s)*" + integerValue +  ",.*"));
	}


	@Test
	public void testappendIntegerValueLastTrue() {
		JSONUtility.appendIntegerValue(sb, key, integerValue, true);
		assertNotNull(sb);
		assertFalse(sb.toString().length() == 0);
		assertFalse(!sb.toString().matches(".*" + "\"" + key + "\"" + "(\\s\\s)*" + ":" + "(\\s\\s)*" + integerValue +  ".*"));
	}

	@Test
	public void testAppendLongValue() {
		JSONUtility.appendLongValue(sb, key, longValue, true);
		assertNotNull(sb);
		assertFalse(sb.toString().length() == 0);
		assertFalse(!sb.toString().matches(".*" + "\"" + key + "\"" + "(\\s\\s)*" + ":" + "(\\s\\s)*" + longValue +  ".*"));
	}

	@Test
	public void testAppendLongValueLastTrue() {
		JSONUtility.appendLongValue(sb, key, longValue, true);
		assertNotNull(sb);
		assertFalse(sb.toString().length() == 0);
		assertFalse(!sb.toString().matches(".*" + "\"" + key + "\"" + "(\\s\\s)*" + ":" + "(\\s\\s)*" + longValue +  ".*"));
	}

	@Test
	public void testAppendIntegerListAsArray() {
		StringBuilder resultToTest = new StringBuilder();
		resultToTest.append("\\[");
		for (Iterator<Integer> iterator = integerList.iterator(); iterator.hasNext();) {
			Integer item = iterator.next();
			resultToTest.append(item);
			if(iterator.hasNext()) {
				resultToTest.append(",");
			}
		}
		resultToTest.append("\\]");
		JSONUtility.appendIntegerListAsArray(sb, key, integerList);
		assertNotNull(sb);
		assertFalse(sb.toString().length() == 0);
		assertFalse(!sb.toString().matches(".*" + "\"" + key + "\"" + "(\\s\\s)*" + ":" + "(\\s\\s)*" + resultToTest +  ",.*"));
	}

	@Test
	public void testAppendIntegerSetAsArray() {
		StringBuilder resultToTest = new StringBuilder();
		resultToTest.append("\\[");
		for (Iterator<Integer> iterator = integerSet.iterator(); iterator.hasNext();) {
			Integer item = iterator.next();
			resultToTest.append(item);
			if(iterator.hasNext()) {
				resultToTest.append(",");
			}
		}
		resultToTest.append("\\]");
		JSONUtility.appendIntegerSetAsArray(sb, key, integerSet);
		assertNotNull(sb);
		assertFalse(sb.toString().length() == 0);
		assertFalse(!sb.toString().matches(".*" + "\"" + key + "\"" + "(\\s\\s)*" + ":" + "(\\s\\s)*" + resultToTest +  ".*"));
	}

	@Test
	public void testAppendIntegerArrayAsArray() {
		StringBuilder resultToTest = new StringBuilder();
		resultToTest.append("\\[");
		List<Integer> tmpList =  Arrays.asList(integerArray);
		for (Iterator<Integer> iterator = tmpList.iterator(); iterator.hasNext();) {
			Integer item = iterator.next();
			resultToTest.append(item);
			if(iterator.hasNext()) {
				resultToTest.append(",");
			}
		}
		resultToTest.append("\\]");
		JSONUtility.appendIntegerArrayAsArray(sb, key, integerArray);
		assertNotNull(sb);
		assertFalse(sb.toString().length() == 0);
		assertFalse(!sb.toString().matches(".*" + "\"" + key + "\"" + "(\\s\\s)*" + ":" + "(\\s\\s)*" + resultToTest +  ".*"));
	}

	@Test
	public void testAppendIntArrayAsArray() {
		StringBuilder resultToTest = new StringBuilder();
		resultToTest.append("\\[");
		List<Integer> tmpList =  Ints.asList(intArray);
		for (Iterator<Integer> iterator = tmpList.iterator(); iterator.hasNext();) {
			Integer item = iterator.next();
			resultToTest.append(item);
			if(iterator.hasNext()) {
				resultToTest.append(",");
			}
		}
		resultToTest.append("\\]");
		JSONUtility.appendIntArrayAsArray(sb, key, intArray);
		assertNotNull(sb);
		assertFalse(sb.toString().length() == 0);
		assertFalse(!sb.toString().matches(".*" + "\"" + key + "\"" + "(\\s\\s)*" + ":" + "(\\s\\s)*" + resultToTest +  ".*"));
	}

	@Test
	public void testAppendObjectArrayAsArray() {

		StringBuilder resultToTest = new StringBuilder();
		resultToTest.append("\\[");
		List<Object> tmpList =  Arrays.asList(objectArray);
		for (Iterator<Object> iterator = tmpList.iterator(); iterator.hasNext();) {
			Object item = iterator.next();
			resultToTest.append(item.toString());
			if(iterator.hasNext()) {
				resultToTest.append(",");
			}
		}
		resultToTest.append("\\]");
		JSONUtility.appendObjectArrayAsArray(sb, key, objectArray);
		assertNotNull(sb);
		assertFalse(sb.toString().length() == 0);
		assertFalse(!sb.toString().matches(".*" + "\"" + key + "\"" + "(\\s\\s)*" + ":" + "(\\s\\s)*" + resultToTest +  ".*"));
	}

	@Test
	public void testEncodeIntegerArrayAsArray() {
		String encodedArray = JSONUtility.encodeIntegerArrayAsArray(integerArray);
		StringBuilder resultToTest = new StringBuilder("[");
		List<Integer> tmpList =  Arrays.asList(integerArray);
		for (Iterator<Integer> iterator = tmpList.iterator(); iterator.hasNext();) {
			Object item = iterator.next();
			resultToTest.append(item.toString());
			if(iterator.hasNext()) {
				resultToTest.append(",");
			}
		}
		resultToTest.append("]");
		assertNotNull(encodedArray);
		assertFalse(encodedArray.equals(resultToTest));
	}

	@Test
	public void testEncodeObjectArrayAsArray() {
		String encodedArray = JSONUtility.encodeObjectArrayAsArray(objectArray);
		StringBuilder resultToTest = new StringBuilder("[");
		List<Object> tmpList =  Arrays.asList(objectArray);
		for (Iterator<Object> iterator = tmpList.iterator(); iterator.hasNext();) {
			Object item = iterator.next();
			resultToTest.append(item.toString());
			if(iterator.hasNext()) {
				resultToTest.append(",");
			}
		}
		resultToTest.append("]");
		assertNotNull(encodedArray);
		assertFalse(encodedArray.equals(resultToTest));
	}

	@Test
	public void testAppendIntegerArrayAsCommaSplittedString() {
		JSONUtility.appendIntegerArrayAsCommaSplittedString(sb, key, integerList);
		StringBuilder values = new StringBuilder();

		for (Iterator<Integer> iterator = integerList.iterator(); iterator.hasNext();) {
			Object item = iterator.next();
			values.append(item.toString());
			if(iterator.hasNext()) {
				values.append(",");
			}
		}
		assertNotNull(sb);
		assertFalse(sb.toString().length() == 0);
		assertFalse(!sb.toString().matches(".*" + "\"" + key + "\"" + "(\\s\\s)*" + ":" + "(\\s\\s)*\"" + values +  "\".*"));
	}

	@Test
	public void appendDoubleValue() {
		JSONUtility.appendDoubleValue(sb, key, doubleValue);
		assertNotNull(sb);
		assertFalse(sb.length() == 0);
		assertFalse(!sb.toString().matches(".*" + "\"" + key + "\"" + "(\\s\\s)*" + ":" + "(\\s\\s)*" + doubleValue.toString() +  ",.*"));
	}

	@Test
	public void appendDoubleValueLastTrue() {
		JSONUtility.appendDoubleValue(sb, key, doubleValue, true);
		assertNotNull(sb);
		assertFalse(sb.length() == 0);
		assertFalse(!sb.toString().matches(".*" + "\"" + key + "\"" + "(\\s\\s)*" + ":" + "(\\s\\s)*" + doubleValue.toString() +  ".*"));
	}

	@Test
	public void testAppendLocaleFormattedDoubleValue() {
		JSONUtility.appendLocaleFormattedDoubleValue(sb, key, doubleValue, locale);
		NumberFormat formatter = NumberFormat.getInstance(locale);
		assertNotNull(sb);
		assertFalse(sb.length() == 0);
		assertFalse(!sb.toString().matches(".*" + "\"" + key + "\"" + "(\\s\\s)*" + ":" + "(\\s\\s)*\"" + formatter.format(doubleValue).toString() +  "\",.*"));
	}

	@Test
	public void testAppendLocaleFormattedDoubleValueLastTrue() {
		JSONUtility.appendLocaleFormattedDoubleValue(sb, key, doubleValue, locale);
		NumberFormat formatter = NumberFormat.getInstance(locale);
		assertNotNull(sb);
		assertFalse(sb.length() == 0);
		assertFalse(!sb.toString().matches(".*" + "\"" + key + "\"" + "(\\s\\s)*" + ":" + "(\\s\\s)*\"" + formatter.format(doubleValue).toString() +  "\".*"));
	}

	@Test
	public void testAppendLocaleFormattedPercentValue() {
		JSONUtility.appendLocaleFormattedPercentValue(sb, key, doubleValue, locale);
		assertNotNull(sb);
		assertFalse(sb.length() == 0);
		assertFalse(!sb.toString().matches(".*" + "\"" + key + "\"" + "(\\s\\s)*" + ":" + "(\\s\\s)*\"" + PercentNumberFormatUtil.getInstance().formatGUI(doubleValue, locale) +  "\",.*"));
	}

	@Test
	public void testAppendLocaleFormattedPercentValueLastTrue() {
		JSONUtility.appendLocaleFormattedPercentValue(sb, key, doubleValue, locale);
		assertNotNull(sb);
		assertFalse(sb.length() == 0);
		assertFalse(!sb.toString().matches(".*" + "\"" + key + "\"" + "(\\s\\s)*" + ":" + "(\\s\\s)*\"" + PercentNumberFormatUtil.getInstance().formatGUI(doubleValue, locale) +  "\".*"));
	}

	@Test
	public void testAppendDateValue() {
		JSONUtility.appendDateValue(sb, key, dateVelue);
		assertNotNull(sb);
		assertFalse(sb.length() == 0);
		assertFalse(!sb.toString().matches(".*" + "\"" + key + "\"" + "(\\s\\s)*" + ":" + "(\\s\\s)*\"" + DateTimeUtils.getInstance().formatISODate(dateVelue) +  "\",.*"));
	}

	@Test
	public void testAppendDateValueLastTrue() {
		JSONUtility.appendDateValue(sb, key, dateVelue, true);
		assertNotNull(sb);
		assertFalse(sb.length() == 0);
		assertFalse(!sb.toString().matches(".*" + "\"" + key + "\"" + "(\\s\\s)*" + ":" + "(\\s\\s)*\"" + DateTimeUtils.getInstance().formatISODate(dateVelue) +  "\".*"));
	}

	@Test
	public void testAppendDateTimeValue() {
		JSONUtility.appendDateTimeValue(sb, key, dateVelue);
		assertNotNull(sb);
		assertFalse(sb.length() == 0);
		assertFalse(!sb.toString().matches(".*" + "\"" + key + "\"" + "(\\s\\s)*" + ":" + "(\\s\\s)*\"" + DateTimeUtils.getInstance().formatISODateTime(dateVelue) +  "\",.*"));
	}

	@Test
	public void testAppendDateTimeValueLastTrue() {
		JSONUtility.appendDateTimeValue(sb, key, dateVelue, true);
		assertNotNull(sb);
		assertFalse(sb.length() == 0);
		assertFalse(!sb.toString().matches(".*" + "\"" + key + "\"" + "(\\s\\s)*" + ":" + "(\\s\\s)*\"" + DateTimeUtils.getInstance().formatISODateTime(dateVelue) +  "\".*"));
	}

	@Test
	public void testAppendLocaleFormattedDateValue() {
		JSONUtility.appendLocaleFormattedDateValue(sb, key, dateVelue, locale);
		assertNotNull(sb);
		assertFalse(sb.length() == 0);
		assertFalse(!sb.toString().matches(".*" + "\"" + key + "\"" + "(\\s\\s)*" + ":" + "(\\s\\s)*\"" + DateTimeUtils.getInstance().formatGUIDate(dateVelue, locale) +  "\",.*"));
	}


	@Test
	public void testAppendLocaleFormattedDateValueLastTrue() {
		JSONUtility.appendLocaleFormattedDateValue(sb, key, dateVelue, locale, true);
		assertNotNull(sb);
		assertFalse(sb.length() == 0);
		assertFalse(!sb.toString().matches(".*" + "\"" + key + "\"" + "(\\s\\s)*" + ":" + "(\\s\\s)*\"" + DateTimeUtils.getInstance().formatGUIDate(dateVelue, locale) +  "\".*"));
	}

	@Test
	public void testAppendBooleanValue() {
		JSONUtility.appendBooleanValue(sb, key, booleanValue);
		assertNotNull(sb);
		assertFalse(sb.length() == 0);
		assertFalse(!sb.toString().matches(".*" + "\"" + key + "\"" + "(\\s\\s)*" + ":" + "(\\s\\s)*" + booleanValue +  ",.*"));
	}

	@Test
	public void testAppendBooleanValueLastTrue() {
		JSONUtility.appendBooleanValue(sb, key, booleanValue, true);
		assertNotNull(sb);
		assertFalse(sb.length() == 0);
		assertFalse(!sb.toString().matches(".*" + "\"" + key + "\"" + "(\\s\\s)*" + ":" + "(\\s\\s)*" + booleanValue +  ".*"));
	}

	@Test
	public void testCreateILabelBeanListJSON() {
		String createdResult = JSONUtility.createILabelBeanListJSON(key, labelBeanListValue);
		ILabelBean labelBean = null;
		StringBuilder expectedResult = new StringBuilder();
		expectedResult.append("\\[");
		for (Iterator<ILabelBean> iterator = labelBeanListValue.iterator(); iterator.hasNext();) {
			labelBean = iterator.next();
			expectedResult.append("\\{");
			if(labelBean.getLabel() != null) {
				expectedResult.append("\"label\":\"" + labelBean.getLabel() + "\"");
			}
			expectedResult.append(",\"id\":" + labelBean.getObjectID());
			expectedResult.append("\\}");
			if(iterator.hasNext()) {
				expectedResult.append(",");
			}
		}
		expectedResult.append("\\]");
		assertNotNull(createdResult);
		assertFalse(createdResult.length() == 0);
		assertFalse(!createdResult.matches(".*" + "\"" + key + "\"" +  ":" +  expectedResult +  ",.*"));
	}

	@Test
	public void testAppendILabelBeanList() {
		JSONUtility.appendILabelBeanList(sb, key, labelBeanListValue);
		ILabelBean labelBean = null;
		StringBuilder expectedResult = new StringBuilder();
		expectedResult.append("\\[");
		for (Iterator<ILabelBean> iterator = labelBeanListValue.iterator(); iterator.hasNext();) {
			labelBean = iterator.next();
			expectedResult.append("\\{");
			if(labelBean.getLabel() != null) {
				expectedResult.append("\"label\":\"" + labelBean.getLabel() + "\"");
			}
			expectedResult.append(",\"id\":" + labelBean.getObjectID());
			expectedResult.append("\\}");
			if(iterator.hasNext()) {
				expectedResult.append(",");
			}
		}
		expectedResult.append("\\]");
		assertNotNull(sb);
		assertFalse(sb.length() == 0);
		assertFalse(!sb.toString().matches(".*" + "\"" + key + "\"" +  ":" +  expectedResult +  ",.*"));
	}

	@Test
	public void testAppendILabelBeanListLastTrue() {
		JSONUtility.appendILabelBeanList(sb, key, labelBeanListValue);
		ILabelBean labelBean = null;
		StringBuilder expectedResult = new StringBuilder();
		expectedResult.append("\\[");
		for (Iterator<ILabelBean> iterator = labelBeanListValue.iterator(); iterator.hasNext();) {
			labelBean = iterator.next();
			expectedResult.append("\\{");
			if(labelBean.getLabel() != null) {
				expectedResult.append("\"label\":\"" + labelBean.getLabel() + "\"");
			}
			expectedResult.append(",\"id\":" + labelBean.getObjectID());
			expectedResult.append("\\}");
			if(iterator.hasNext()) {
				expectedResult.append(",");
			}
		}
		expectedResult.append("\\]");
		assertNotNull(sb);
		assertFalse(sb.length() == 0);
		assertFalse(!sb.toString().matches(".*" + "\"" + key + "\"" +  ":" +  expectedResult +  ".*"));
	}

	@Test
	//TODO implement me need db connection!
	public void testAppendILabelBeanListWithIcons() {
	}

	@Test
	//TODO implement me need db connection!
	public void testEncodeListWithIconCls() {
	}

	@Test
	public void testGetTreeHierarchyJSON() {

	}

}
