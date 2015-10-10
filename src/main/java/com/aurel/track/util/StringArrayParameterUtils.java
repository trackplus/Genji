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


package com.aurel.track.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class StringArrayParameterUtils {
	private final static Logger LOGGER = LogManager.getLogger(StringArrayParameterUtils.class);

	public static int parseIntegerValue(Map<String, String[]> configParameters, String fieldName, int defaultValue) {
		int result = defaultValue;
		String[] strArr = null;
		if (configParameters!=null) {
			try {
				strArr = (String[])configParameters.get(fieldName);
			} catch (Exception e) {
				LOGGER.info("parseIntValue: converting the " +  fieldName  + " parameter to String[] failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		if (strArr!=null && strArr.length>0) {
			String str = strArr[0];
			if (str!=null && !"".equals(str)) {
				try {
					result = Integer.parseInt(str);
				} catch (Exception e) {
					LOGGER.info("Converting the value " + str + " for field " + fieldName + " to int failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
		}
		return result;
	}


	public static Boolean parseBooleanValue(Map<String, String[]> configParameters, String fieldName, Boolean defaultValue) {
		Boolean result = defaultValue;
		String[] strArr = null;
		if (configParameters!=null) {
			try {
				strArr = (String[])configParameters.get(fieldName);
			} catch (Exception e) {
				LOGGER.info("parseIntValue: converting the " +  fieldName  + " parameter to String[] failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		if (strArr!=null && strArr.length>0) {
			String str = strArr[0];
			if (str!=null && !"".equals(str)) {
				try {
					result = Boolean.valueOf(str);
				} catch (Exception e) {
					LOGGER.info("Converting the value " + str + " for field " + fieldName + " to int failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
		}
		return result;
	}

	public static String parseStringValue(Map<String, String[]> configParameters, String fieldName, String defaultValue) {
		String result = defaultValue;
		String[] strArr = null;
		if (configParameters!=null) {
			try {
				strArr = (String[])configParameters.get(fieldName);
			} catch (Exception e) {
				LOGGER.info("parseIntValue: converting the " +  fieldName  + " parameter to String[] failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		if (strArr!=null && strArr.length>0) {
			String str = strArr[0];
			if (str!=null && !"".equals(str)) {
				try {
					result = str;
				} catch (Exception e) {
					LOGGER.info("Converting the value " + str + " for field " + fieldName + " to int failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
		}
		return result;
	}

	public static Integer parseIntegerValue(Map/*<String, String[]>*/ configParameters, String fieldName) {
		Integer result = null;
		String[] strArr = null;
		if (configParameters!=null) {
			try {
				strArr = (String[])configParameters.get(fieldName);
			} catch (Exception e) {
				LOGGER.info("parseIntegerValue: converting the " +  fieldName  + " parameter to String[] failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		if (strArr!=null && strArr.length>0) {
			String str = strArr[0];
			if (str!=null && !"".equals(str)) {
				try {
					result = new Integer(str);
				} catch (Exception e) {
					LOGGER.info("Converting the value " + str + " for field " + fieldName + " to Integer failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
		}
		return result;
	}

	public static Integer[] parseIntegerArrValue(Map<String, String[]> configParameters, String fieldName) {
		String[] strArr = null;
		if (configParameters!=null) {
			try {
				strArr = (String[])configParameters.get(fieldName);
			} catch (Exception e) {
				LOGGER.info("parseIntegerArrValue: converting the " +  fieldName  + "  parameter to String[] failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		Integer[] integerValues = null;
		if (strArr!=null && strArr.length>0 && !"".equals(strArr[0])) {
			integerValues = new Integer[strArr.length];
			for (int i = 0; i < strArr.length; i++) {
				try {
					integerValues[i]=new Integer(strArr[i]);
				} catch (Exception e) {
					LOGGER.info("Converting the " + strArr[i] + " as the " + i + "th parameter to Integer failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
		}
		return integerValues;
	}

	public static String getStringValue(Map<String, String[]> configParameters, String fieldName) {
		String[] strArr = null;
		if (configParameters!=null) {
			try {
				strArr = (String[])configParameters.get(fieldName);
			} catch (Exception e) {
				LOGGER.info("getStringValue: converting the " +  fieldName  + " parameter to String[] failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		if (strArr!=null && strArr.length>0) {
			return strArr[0];
		}
		return null;
	}

	public static Boolean getBooleanValue(Map/*<String, String[]>*/ configParameters, String fieldName) {
		String[] strArr = null;
		if (configParameters!=null) {
			try {
				strArr = (String[])configParameters.get(fieldName);
			} catch (Exception e) {
				LOGGER.info("getBooleanValue: converting the " +  fieldName  + " parameter to String[] failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		if (strArr!=null && strArr.length>0) {
			try {
				return new Boolean(strArr[0]);
			} catch (Exception e) {
				LOGGER.info("Converting the parameter " + strArr[0] + " to boolean failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return new Boolean(false);
	}

	/**
	 * Transform the string with comma separated values into a list
	 * @param selection
	 * @return
	 */
	public static List<String> splitSelection(String selection) {
		List<String> theList = new ArrayList<String>();
		if (selection == null || "".equals(selection)) {
			return theList;
		}
		// The following is required since Struts2 returns the selected
		// elements in a single string, with values separated by ", ".
		String[] elements = selection.split(",");
		if (elements == null || elements.length == 0) {
			return theList;
		}
		for (int i=0; i < elements.length; ++i) {
			theList.add(elements[i].trim());
		}
		return theList;
	}
	public static List<Integer> splitSelectionAsInteger(String selection) {
		return splitSelectionAsInteger(selection,",");
	}
	/**
	 * Transform the string with comma separated values into a list
	 * @param selection
	 * @return
	 */
	public static List<Integer> splitSelectionAsInteger(String selection,String separator) {
		List<Integer> theList = new ArrayList<Integer>();
		if (selection == null || "".equals(selection)) {
			return theList;
		}
		// The following is required since Struts2 returns the selected
		// elements in a single string, with values separated by ", ".
		String[] elements = selection.split(separator);
		if (elements == null || elements.length == 0) {
			return theList;
		}
		for (int i=0; i < elements.length; ++i) {
			Integer intValue=null;
			if(elements[i]!=null){
				try{
					intValue=Integer.parseInt(elements[i].trim());
					theList.add(intValue);
				}catch (Exception ex){}
			}

		}
		return theList;
	}

	public static int[] splitSelectionAsIntArray(String selection) {
		List<Integer> intList=splitSelectionAsInteger(selection);
		int[]  result=new int[intList.size()];
		for(int i=0;i<intList.size();i++){
			result[i]=intList.get(i).intValue();
		}
		return result;
	}
	public static Integer[] splitSelectionAsIntegerArray(String selection,String separator) {
		List<Integer> intList=splitSelectionAsInteger(selection,separator);
		Integer[]  result=new Integer[intList.size()];
		for(int i=0;i<intList.size();i++){
			result[i]=intList.get(i);
		}
		return result;
	}

	/**
	 * Creates a comma separated string of integer values from a list of Integer
	 * @param lst with Integer values
	 * @return a comma separated String with integers
	 */
	public static String createStringFromIntegerList (List<Integer> lst) {
		StringBuffer buf = new StringBuffer();
		if (lst != null && !lst.isEmpty()) {
			Iterator<Integer> it = lst.iterator();
			while (it.hasNext()) {
				Integer day = it.next();
				buf.append(day.toString());
				if (it.hasNext()) {
					buf.append(",");
				}
			}
		}
		return buf.toString();
	}

	/**
	 * If a String URL not ends with slash this method will append it/
	 * @param urlTOCheck
	 * @return
	 */
	public static String appendSlashToURLString(String urlTOCheck) {
		String retValue = urlTOCheck;
		if(urlTOCheck != null && urlTOCheck.length() > 0 ) {
			if(!urlTOCheck.substring(urlTOCheck.length() - 1).equals("/")) {
				retValue += "/";
			}
		}
		return retValue;
	}

	public static String getStringFromInputStream(InputStream is) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					LOGGER.error(ExceptionUtils.getStackTrace(e));
				}
			}
		}
		return sb.toString();
	}
}
