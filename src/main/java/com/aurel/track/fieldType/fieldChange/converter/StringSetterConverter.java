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

package com.aurel.track.fieldType.fieldChange.converter;

import org.apache.commons.lang.StringEscapeUtils;

import com.aurel.track.fieldType.fieldChange.FieldChangeSetters;

/**
 * Used to convert a value from String (XML or displayValue) to String value and back
 */
public class StringSetterConverter  extends AbstractValueConverter {
	
	public StringSetterConverter(Integer activityType) {
		super(activityType);
	}

	/**
	 * Convert the string to object value after load
	 * @param value
	 * @param setter
	 * @return
	 */
	public Object getActualValueFromStoredString(String value, Integer setter) {
		if (setter!=null) {
			switch (setter.intValue()) {
			case FieldChangeSetters.SET_TO:
			case FieldChangeSetters.ADD_TEXT_TO_BEGIN:
			case FieldChangeSetters.ADD_TEXT_TO_END:
			case FieldChangeSetters.ADD_COMMENT:	
			case FieldChangeSetters.REMOVE_TEXT:
				if (value!=null) {
					return StringEscapeUtils.unescapeXml(value);
				}
			}
		}
		return null;
	}

}
