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

import com.aurel.track.fieldType.fieldChange.FieldChangeSetters;
import com.aurel.track.util.GeneralUtils;

/**
 * Used to convert a value from String (XML or displayValue) to Integer[] value and back
 */
public class MultipleSelectSetterConverter extends AbstractValueConverter {
	
	public MultipleSelectSetterConverter(Integer activityType) {
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
		if (value==null || value.trim().length()==0 || setter==null) {
			return null;
		}
		switch (setter) {
		case FieldChangeSetters.SET_TO:
		case FieldChangeSetters.ADD_ITEMS:
		case FieldChangeSetters.REMOVE_ITEMS:
			return GeneralUtils.createIntegerArrFromCommaSeparatedString(value);
		}
		return null;
	}
	
}
