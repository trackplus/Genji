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

import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.fieldType.fieldChange.FieldChangeSetters;

/**
 */
public class CompositeSelectSetterConverter extends AbstractValueConverter {
	private static final Logger LOGGER = LogManager.getLogger(CompositeSelectSetterConverter.class);
	private static String PART_SPLITTER_STRING = ",";
	
	public CompositeSelectSetterConverter(Integer activityType) {
		super(activityType);
	}

	/**
	 * Convert the string to object value after load
	 * @param value
	 * @param setter
	 * @return
	 */
	public Object getActualValueFromStoredString(String value, Integer setter) {
		if (value==null || setter==null) {
			return null;
		}
		switch (setter.intValue()) {
		case FieldChangeSetters.SET_TO:
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
	
}
