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

package com.aurel.track.fieldType.runtime.matchers.converter;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.fieldType.runtime.matchers.MatchRelations;
import com.aurel.track.util.DateTimeUtils;

/**
 * Used to convert a value from String (XML or displayValue) to Date value and back
 */
public class DateMatcherConverter implements MatcherConverter{
	private static final Logger LOGGER = LogManager.getLogger(DateMatcherConverter.class);
	private static DateMatcherConverter instance;
	
	private DateMatcherConverter(){
	}
	
	public static DateMatcherConverter getInstance(){
		if(instance==null){
			instance=new DateMatcherConverter();
		}
		return instance;
	}
	
	/**
	 * Convert the object value to xml string for save
	 * @param value
	 * @param matcherRelation
	 * @return
	 */
	@Override
	public String toXMLString(Object value, Integer matcherRelation) {
		if (value==null || matcherRelation==null) {
			return null;
		}
		switch (matcherRelation.intValue()) {
		case MatchRelations.MORE_THAN_DAYS_AGO:
		case MatchRelations.MORE_THAN_EQUAL_DAYS_AGO:
		case MatchRelations.LESS_THAN_DAYS_AGO:
		case MatchRelations.LESS_THAN_EQUAL_DAYS_AGO:
		case MatchRelations.IN_MORE_THAN_DAYS:
		case MatchRelations.IN_MORE_THAN_EQUAL_DAYS:
		case MatchRelations.IN_LESS_THAN_DAYS:
		case MatchRelations.IN_LESS_THAN_EQUAL_DAYS:
				return value.toString();
		case MatchRelations.EQUAL_DATE:
		case MatchRelations.NOT_EQUAL_DATE:
		case MatchRelations.GREATHER_THAN_DATE:
		case MatchRelations.GREATHER_THAN_EQUAL_DATE:
		case MatchRelations.LESS_THAN_DATE:
		case MatchRelations.LESS_THAN_EQUAL_DATE:
			try {
				Date dateValue = (Date)value;
				return DateTimeUtils.getInstance().formatISODateTime(dateValue);
			} catch (Exception e) {
				LOGGER.warn("Converting the " + value +  " to Date for xml string failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
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
	@Override
	public Object fromXMLString(String value, Integer matcherRelation) {
		if (value==null || value.trim().length()==0 || value.trim().equals("null") || matcherRelation==null) {
			return null;
		}
		switch (matcherRelation.intValue()) {
		case MatchRelations.MORE_THAN_DAYS_AGO:
		case MatchRelations.MORE_THAN_EQUAL_DAYS_AGO:
		case MatchRelations.LESS_THAN_DAYS_AGO:
		case MatchRelations.LESS_THAN_EQUAL_DAYS_AGO:
		case MatchRelations.IN_MORE_THAN_DAYS:	
		case MatchRelations.IN_MORE_THAN_EQUAL_DAYS:
		case MatchRelations.IN_LESS_THAN_DAYS:
		case MatchRelations.IN_LESS_THAN_EQUAL_DAYS:
			Integer intValue = null;
			try {
				intValue = Integer.valueOf(value);
			} catch (Exception e) {
				LOGGER.warn("Converting the " + value +  " to Integer from xml string failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			return intValue;
		case MatchRelations.EQUAL_DATE:
		case MatchRelations.NOT_EQUAL_DATE:
		case MatchRelations.GREATHER_THAN_DATE:
		case MatchRelations.GREATHER_THAN_EQUAL_DATE:
		case MatchRelations.LESS_THAN_DATE:
		case MatchRelations.LESS_THAN_EQUAL_DATE:
			return DateTimeUtils.getInstance().parseISODateTime(value);
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
	@Override
	public Object fromDisplayString(Map<String, String> displayStringMap,
			Integer fieldID, Locale locale, Integer matcherRelation) {
		if (displayStringMap == null) {
			return null;
		}
		String value = displayStringMap.get(fieldID.toString());
		//suppose the date value is submitted from the server in locale specific GUI format
				//see DateMatcherDT.getMatchValueJsonConfig()
		return fromValueString(value, locale, matcherRelation);
	}
	
	/**
	 * Convert a string to object value
	 * By changing the matcherID for a field expression locally
	 * (not after submitting a form). The reason for this is to maintain the existing
	 * value if the new matcher is "compatible" with the old matcher 
	 * @param value
	 * @param locale
	 * @param matcherRelation
	 * @return
	 */
	@Override
	public Object fromValueString(String value, Locale locale, Integer matcherRelation) {
		//suppose the date value is submitted from the server in locale specific GUI format
		//see DateMatcherDT.getMatchValueJsonConfig()
		if (value==null || value.trim().length()==0 || value.trim().equals("null") || matcherRelation==null) {
			return null;
		}
		switch (matcherRelation.intValue()) {
		case MatchRelations.MORE_THAN_DAYS_AGO:
		case MatchRelations.MORE_THAN_EQUAL_DAYS_AGO:
		case MatchRelations.LESS_THAN_DAYS_AGO:
		case MatchRelations.LESS_THAN_EQUAL_DAYS_AGO:
		case MatchRelations.IN_MORE_THAN_DAYS:	
		case MatchRelations.IN_MORE_THAN_EQUAL_DAYS:
		case MatchRelations.IN_LESS_THAN_DAYS:
		case MatchRelations.IN_LESS_THAN_EQUAL_DAYS:
			Integer intValue = null;
			try {
				intValue = Integer.valueOf(value);
			} catch (Exception e) {
				LOGGER.warn("Converting the " + value +  " to Integer from xml string failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			return intValue;
		case MatchRelations.EQUAL_DATE:
		case MatchRelations.NOT_EQUAL_DATE:
		case MatchRelations.GREATHER_THAN_DATE:
		case MatchRelations.GREATHER_THAN_EQUAL_DATE:
		case MatchRelations.LESS_THAN_DATE:
		case MatchRelations.LESS_THAN_EQUAL_DATE:
			return DateTimeUtils.getInstance().parseGUIDate(value, locale);
		}
		return null;
	}
}
