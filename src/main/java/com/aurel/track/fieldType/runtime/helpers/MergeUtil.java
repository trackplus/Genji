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


package com.aurel.track.fieldType.runtime.helpers;

import java.util.Collection;
import java.util.Iterator;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TWorkItemBean;

public class MergeUtil {

	private static final Logger LOGGER = LogManager.getLogger(MergeUtil.class);
	
	/**
	 * Merge together two Integer values to a String using a linker string 
	 * @param objectID typically is a configID or a fieldID
	 * @param parameterCode can be null
	 * @return
	 */
	public static String mergeKey(Integer objectID, Integer parameterCode) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(objectID);
		stringBuffer.append(TWorkItemBean.LINKCHAR);
		stringBuffer.append(parameterCode);
		return stringBuffer.toString();
	}
	
	/**
	 * Merge together two Integer values to a String using a linker string 
	 * @param objectID typically is a configID or a fieldID
	 * @param parameterCode can be null
	 * @return
	 */
	public static String mergeKey(String objectID, String parameterCode) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(objectID);
		stringBuffer.append(TWorkItemBean.LINKCHAR);
		stringBuffer.append(parameterCode);
		return stringBuffer.toString();		
	}
	
	/**
	 * Splits a String to their parts 
	 * @param mergedString
	 * @return
	 */
	public static String[] splitKey(String mergedString) {
		return mergedString.split(TWorkItemBean.LINKCHAR);
	}
	
	/**
	 * Gets the first (fieldID or configID) part
	 * @param mergedString
	 * @return
	 */
	public static Integer getFieldID(String mergedString) {
		String[] keys = splitKey(mergedString);
		if (keys!=null && keys[0]!=null)
		{
			return Integer.valueOf(keys[0]);
		}
		return null;
	}
	
	/**
	 * Gets the parameterCode part
	 * @param mergedString
	 * @return
	 */
	public static Integer getParameterCode(String mergedString) {
		String[] keys = splitKey(mergedString);
		if (keys!=null && keys[1]!=null && !"null".equals(keys[1])) {
			return Integer.valueOf(keys[1]);
		}
		return null;
	}
	
	/**
	 * Split a String two their Integer parts 
	 * @param mergedString
	 * @return
	 */
	public static Integer[] getParts(String mergedString) {
		Integer[] intKeys = new Integer[2];
		String[] stringKeys = mergedString.split(TWorkItemBean.LINKCHAR);
		if (stringKeys!=null && stringKeys[0]!=null) {
			try {
				intKeys[0] = Integer.valueOf(stringKeys[0]);
			} catch (Exception e) {
				LOGGER.warn("Getting the first part '" + stringKeys[0] + "' of the merged string '" + mergedString + "' failed with " + e.getMessage());
			}
		}
		try {
			if (stringKeys!=null && stringKeys.length>1 && stringKeys[1]!=null && !"null".equals(stringKeys[1])) {
					intKeys[1] = Integer.valueOf(stringKeys[1]);
			}
		} catch (Exception e) {
			LOGGER.warn("Getting the second part '" + stringKeys[1] + "' of the merged string '" + mergedString + "' failed with " + e.getMessage());
		}
		return intKeys;
	}
	
	/**
	 * Get the merged string of the set delimited by a string
	 * @param values
	 * @param delimiter
	 * @return
	 */
	public static String getMergedString(Collection values, String delimiter) {
		Iterator<Integer> iterator = values.iterator();
		StringBuffer stringBuffer = new StringBuffer();
		while (iterator.hasNext()) {
			Object value = iterator.next();
			stringBuffer.append(value);
			if (iterator.hasNext()) {
				stringBuffer.append(delimiter);
			}
		}
		return stringBuffer.toString();
	}		
}
