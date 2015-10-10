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

import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Matcher converter for boolean values
 * No value is used (only matcher relation is important) 
 * @author Tamas Ruff
 *
 */
public class BooleanMatcherConverter implements MatcherConverter{
	private static  BooleanMatcherConverter instance;
	private static final Logger LOGGER = LogManager.getLogger(BooleanMatcherConverter.class);
	public static BooleanMatcherConverter getInstance(){
		if(instance==null){
			instance=new BooleanMatcherConverter();
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
		if(value==null){
			return null;
		}
		return value.toString();
	}

	/**
	 * Convert the xml string to object value after load
	 * @param value
	 * @param matcherRelation
	 * @return
	 */
	public Object fromXMLString(String value, Integer matcherRelation) {
		if(value==null || value.trim().length()==0){
			return null;
		} else {
			try {
				return Boolean.valueOf(value);
			} catch (Exception e) {
				LOGGER.warn("Converting the " + value +  " to Boolean failed with " + e.getMessage(), e);
				return Boolean.FALSE;
			}
		}
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
		return fromXMLString(displayStringMap.get(fieldID.toString()), matcherRelation);
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
		return fromXMLString(valueString, matcherRelation);
	}
}
