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

package com.aurel.track.fieldType.fieldChange.converter;

import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.fieldType.fieldChange.FieldChangeSetters;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.util.DateTimeUtils;

/**
 * Used to convert a value from String (XML or displayValue) to Date value and back
 */
public class DateSetterConverter extends AbstractValueConverter {
	private static final Logger LOGGER = LogManager.getLogger(DateSetterConverter.class);
	
	public DateSetterConverter(Integer activityType) {
		super(activityType);
	}

	/**
	 * Convert the string to object value after load
	 * @param value
	 * @param setter
	 * @return
	 */
	@Override
	public Object getActualValueFromStoredString(String value, Integer setter) {
		if (value==null || value.trim().length()==0 || value.trim().equals("null") || setter==null) {
			return null;
		}
		switch (setter.intValue()) {
		case FieldChangeSetters.MOVE_BY_DAYS:
		case FieldChangeSetters.SET_TO_DATE_FIELD_VALUE:
			Integer intValue = null;
			try {
				intValue = Integer.valueOf(value);
			} catch (Exception e) {
				LOGGER.info("Converting the " + value +  " to Integer from string failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			return intValue;
		case FieldChangeSetters.SET_TO:
			return DateTimeUtils.getInstance().parseISODate(value);
		}
		return null;
	}

	/**
	 * Gets the show value transformed form the stored configuration in database 
	 * @param value
	 * @param fieldID
	 * @param setter
	 * @param locale
	 * @return
	 */
	@Override
	public String getDisplayValueFromStoredString(String value, Integer fieldID, Integer setter, Locale locale) {
		if (setter==null) {
			return "";
		}
		switch (setter.intValue()) {
		case FieldChangeSetters.MOVE_BY_DAYS:
			Integer intValue = (Integer)getActualValueFromStoredString(value, setter);
			if (intValue!=null) {
				return intValue.toString();
			}
		case FieldChangeSetters.SET_TO_DATE_FIELD_VALUE:
			Integer dateFieldID = (Integer)getActualValueFromStoredString(value, setter);
			if (dateFieldID!=null) {
				return FieldRuntimeBL.getLocalizedDefaultFieldLabel(dateFieldID, locale);
			}
		case FieldChangeSetters.SET_TO:
			Date dateValue = (Date)getActualValueFromStoredString(value, setter);
			if (dateValue!=null) {
				return DateTimeUtils.getInstance().formatGUIDate(dateValue, locale);
			}
		}
		return "";
	}
	
}
