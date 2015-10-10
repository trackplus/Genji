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


package com.aurel.track.fieldType.runtime.matchers.converter;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.fieldType.runtime.matchers.MatchRelations;

/**
 */
public class CompositSelectMatcherConverter implements MatcherConverter{
	private static final Logger LOGGER = LogManager.getLogger(CompositSelectMatcherConverter.class);
	private static String PART_SPLITTER_STRING = "#";
	private static CompositSelectMatcherConverter instance;
	public static CompositSelectMatcherConverter getInstance(){
		if(instance==null){
			instance=new CompositSelectMatcherConverter();
		}
		return instance;
	}
	
	/**
	 * Convert the object value to xml string for save
	 * @param value
	 * @param matcherRelation
	 * @return
	 */
	public String toXMLString(Object value, Integer matcherRelation) {
		if (value==null || matcherRelation==null) {
			return null;
		}
		switch (matcherRelation.intValue()) {
		case MatchRelations.EQUAL:
		case MatchRelations.NOT_EQUAL:
		case MatchRelations.PARTIAL_MATCH:
		case MatchRelations.PARTIAL_NOTMATCH:
			SortedMap<Integer, Integer[]> actualValuesMap = null;
			try {
				actualValuesMap = (SortedMap<Integer, Integer[]>)value;
			} catch (Exception e) {
				LOGGER.warn("Converting the " + value +
						" to SortedMap<Integer, Integer[]> for display string failed with " + e.getMessage(), e);
			}
			if (actualValuesMap!=null) {
				StringBuffer stringBuffer = new StringBuffer();
				Iterator<Integer> iterator = actualValuesMap.keySet().iterator();
				while (iterator.hasNext()) {
					Integer partNo = iterator.next();
					Integer[] partValueArr = null;
					try {
						partValueArr = actualValuesMap.get(partNo);
					} catch (Exception e) {
						LOGGER.warn("Converting the part " + partNo +  " to Integer[] for XML string string failed with " + e.getMessage(), e);
					}
					String partValue = "";
					if (partValueArr!=null && partValueArr.length>0) {
						//partValue is probably an integer array
						//if there is a possibility that the composite contains also other
						//datatypes for example date which should be formatted then 
						//we would need to extend the API with further method parameters
						partValue = partValueArr[0].toString();
					}
					stringBuffer.append(partValue);
					if (iterator.hasNext()) {
						stringBuffer.append(PART_SPLITTER_STRING);
					}
				}
				return stringBuffer.toString().trim();
			}
		}
		return null;
	}

	/**
	 * Convert the xml string to object value after load
	 * @param value
	 * @param matcherRelation
	 * @return
	 */
	public Object fromXMLString(String value, Integer matcherRelation) {
		if (value==null || matcherRelation==null) {
			return null;
		}
		switch (matcherRelation.intValue()) {
		case MatchRelations.EQUAL:
		case MatchRelations.NOT_EQUAL:
		case MatchRelations.PARTIAL_MATCH:
		case MatchRelations.PARTIAL_NOTMATCH:
			SortedMap<Integer, Integer[]> actualValuesMap = new TreeMap<Integer, Integer[]>();
			String[] partStringArr = value.split(PART_SPLITTER_STRING);
			if (partStringArr!=null && partStringArr.length>0) {
				for (int i = 0; i < partStringArr.length; i++) {
					String partString = partStringArr[i];
					if (partString!=null && !"".equals(partString)) {
						try {
							Integer partValue = Integer.valueOf(partString);
							actualValuesMap.put(Integer.valueOf(i+1), new Integer[] {partValue});
						} catch (Exception e) {
							LOGGER.warn("Converting the " + i+1 + "th part " + partString +  " to Integer failed with " + e.getMessage());
							LOGGER.debug(ExceptionUtils.getStackTrace(e));
						}
					}
				}
			}
			return actualValuesMap;
		}
		return null;
	}
	
	/**
	 * Convert a string from displayStringMap to object value
	 * Called after submitting the filter's form
	 * @param displayStringMap
	 * @param fieldID
	 * @param locale
	 * @param matcherRelation
	 * @return
	 */
	public Object fromDisplayString(Map<String, String> displayStringMap,
			Integer fieldID, Locale locale, Integer matcherRelation) {
		if (displayStringMap == null) {
			return null;
		}
		if (matcherRelation!=null) {
			switch (matcherRelation.intValue()) {
			case MatchRelations.EQUAL:
			case MatchRelations.NOT_EQUAL: 
			case MatchRelations.PARTIAL_MATCH:
			case MatchRelations.PARTIAL_NOTMATCH:
				//it should be sorted map because of toXMLString(), 
				//in xml the values should be serialized in the right order
				SortedMap<Integer, Integer[]> actualValuesMap = new TreeMap<Integer, Integer[]>();
				Iterator<String> iterator = displayStringMap.keySet().iterator();
				while (iterator.hasNext()) {
					String key = iterator.next();
					Integer[] parts = MergeUtil.getParts(key);
					if (parts!=null && parts.length>1) {
						Integer fieldOrIndex = parts[0];
						Integer parametertCode = parts[1];
						if (fieldOrIndex!=null && fieldOrIndex.equals(fieldID) && parametertCode!=null) {
							String partStringValue = displayStringMap.get(key);
							if (partStringValue!=null) {
								try {
									actualValuesMap.put(parametertCode, new Integer[] {Integer.valueOf(partStringValue)});
								} catch (Exception e) {
									LOGGER.warn("Converting the " + partStringValue +  " to Integer from display string failed with " + e.getMessage(), e);
								}
							}
						}
					}
				}
				return actualValuesMap;
			}
		}
		return null;
	}
	
	/**
	 * Convert a string to object value
	 * By changing the matcherID for a field expression locally
	 * (not after submitting a form). The reason for this is to maintain the existing
	 * value if the new matcher is "compatible" with the old matcher 
	 * @param valueString
	 * @param locale
	 * @param matcherRelation
	 * @return
	 */
	public Object fromValueString(String valueString, Locale locale, Integer matcherRelation) {
		SortedMap<Integer, Object> actualValuesMap = null;
		if (matcherRelation!=null) {
			switch (matcherRelation.intValue()) {
			case MatchRelations.EQUAL:
			case MatchRelations.NOT_EQUAL:
			case MatchRelations.PARTIAL_MATCH:
			case MatchRelations.PARTIAL_NOTMATCH:
				if (valueString!=null  && !"".equals(valueString)) {
					actualValuesMap = new TreeMap<Integer, Object>();
					String[] parts = valueString.split(",");
					for (int i = 0; i < parts.length; i++) {
						try {
							actualValuesMap.put(Integer.valueOf(i+1), new Integer[] { Integer.valueOf(parts[i])});
						} catch (Exception e) {
							LOGGER.warn("Converting the " + i+1 +  "th part to Integer from value string failed with " + e.getMessage(), e);
						}
					}
				}
			}
		}
		return actualValuesMap;
	}
}
