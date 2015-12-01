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


package com.aurel.track.fieldType.runtime.bl;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
/**
 * Utility class for custom selects to get the selected values as Integer[]
 * The custom selects are considered multiple selects (@see CustomSelectBaseRT#isMultiple()),
 * so they are loaded as Object[] (@see CustomOnePieceBaseRT#loadAttribute()) even if 
 * at renderer level they are single selects 
 * @author Tamas
 *
 */
public class CustomSelectUtil {
	
	private static final Logger LOGGER = LogManager.getLogger(CustomSelectUtil.class);
	
	public static Integer[] getSelectedOptions(Object attributeValue) {		
		Integer[] selectedOptionIDs = null;
		Object[] attributeValues = null;
		if (attributeValue!=null) {
			try {
				attributeValues = (Object[])attributeValue;
			} catch (Exception e) {
				LOGGER.warn("The type of the attributeValue source is " + 
						attributeValue.getClass().getName() + ". Casting it to Object[] failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			//clean the null values
			selectedOptionIDs = getNotNullValues(attributeValues);
		}
		return selectedOptionIDs;
	}
	
	public static Integer[] getSelectedOptionsFromStringArr(Object attributeValue) {		
		Integer[] integerOptionIDs = null;
		String[] stringOptionIDs = null;
		if (attributeValue!=null) {
			try {
				stringOptionIDs = (String[])attributeValue;
			} catch (Exception e) {
				LOGGER.warn("The type of the attributeValue source is " + 
						attributeValue.getClass().getName() + ". Casting it to String[] failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			if (stringOptionIDs!=null) {
				integerOptionIDs = new Integer[stringOptionIDs.length];
				for (int i = 0; i < stringOptionIDs.length; i++) {
					if ("".equals(stringOptionIDs[i])) {
						//probably list was empty and no real value was submitted just an empty string 
						integerOptionIDs[i]=null;
						break;
					} else {
						integerOptionIDs[i]=new Integer(stringOptionIDs[i]);
					}
				}
				//clean the null values
				integerOptionIDs = getNotNullValues(integerOptionIDs);
			}
		}
		return integerOptionIDs;
	}
	
	/**
	 * Filter out the null values from the argument's array 
	 * because they cause severe torque exception
	 * @param values
	 */
	private static Integer[] getNotNullValues(Object[] values) {
		Integer[] notNullValuesArray = null;
		List<Object> notNullValuesList = new LinkedList<Object>();
		if (values!=null && values.length>0) {
			for (Object value : values) {
				if (value!=null) {
					notNullValuesList.add(value);
				}
			}
		}
		if (!notNullValuesList.isEmpty()) {
			notNullValuesArray = new Integer[notNullValuesList.size()];
			for (int i = 0; i < notNullValuesList.size(); i++) {
				notNullValuesArray[i]=(Integer)notNullValuesList.get(i);
			}
		}
		return notNullValuesArray;
	}
}
