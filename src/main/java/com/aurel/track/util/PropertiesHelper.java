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


package com.aurel.track.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class PropertiesHelper {
	
	private static final Logger LOGGER = LogManager.getLogger(PropertiesHelper.class);
	
	/**
	 * Method to get a property. This is to easily expand the
	 * set of preferences without having to provide a database attribute
	 * for each. The preferences are stored in the database in regular
	 * Java property format.
	 * @param propertiesString the entire property string
	 * @param thePropertyName name of property to get
	 * @return
	 */
	public static Properties getProperties(String propertiesString) {
		Properties properties = new Properties();
		if (propertiesString!=null) {
			try {
				properties.load(new ByteArrayInputStream(
						propertiesString.getBytes()));
			}
			catch (Exception e) {
				LOGGER.warn("Loading the propertiesString " + propertiesString + " failed with " + e.getMessage());
			}
		}
		return properties;
	}
	
	/**
	 * Method to get a property. This is to easily expand the
	 * set of preferences without having to provide a database attribute
	 * for each. The preferences are stored in the database in regular
	 * Java property format.
	 * @param propertiesString the entire property string
	 * @param thePropertyName name of property to get
	 * @return
	 */
	public static String getProperty(Properties properties, String thePropertyName) {
		if(properties==null){
			return null;
		}
		return properties.getProperty(thePropertyName);
	}
	
	/**
	 * Method to get a property. This is to easily expand the
	 * set of preferences without having to provide a database attribute
	 * for each. The preferences are stored in the database in regular
	 * Java property format.
	 * @param propertiesString the entire property string
	 * @param thePropertyName name of property to get
	 * @return
	 */
	public static String getProperty(String propertiesString, String thePropertyName) {
		Properties properties = getProperties(propertiesString);
		return properties.getProperty(thePropertyName);
	}
	
	/**
	 * Method to get a property. This is to easily expand the
	 * set of preferences without having to provide a database attribute
	 * for each. The preferences are stored in the database in regular
	 * Java property format.
	 * @param propertiesString the entire property string
	 * @param thePropertyName name of property to get
	 * @return
	 */
	public static Integer getIntegerProperty(String propertiesString, String thePropertyName) {
		String strProperty = getProperty(propertiesString, thePropertyName);
		return getIntegerFromString(strProperty);
	}
	
	/**
	 * Gets Integer from a String 
	 * @param strProperty
	 * @return
	 */
	public static Integer getIntegerProperty(Properties properties, String strProperty) {
		String stringValue = getProperty(properties, strProperty);
		return getIntegerFromString(stringValue);
	}
	
	/**
	 * Gets Integer from a String 
	 * @param strProperty
	 * @return
	 */
	private static Integer getIntegerFromString(String strProperty) {
		if (strProperty==null || "".equals(strProperty)) {
			return null;
		}
		try {
			return Integer.valueOf(strProperty);
		} catch(Exception e) {
			LOGGER.warn("Converting the property " + strProperty + " to integer failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Gets a boolean property
	 * @param propertiesString the entire property string
	 * @param thePropertyName name of property to get
	 * @return the value of the property
	 */
	public static boolean getBooleanProperty(String propertiesString, String thePropertyName) {
		String value = getProperty(propertiesString, thePropertyName);
		return getBooleanFromString(value, false);
	}
	
	
	public static String getStringProperty(String propertiesString, String thePropertyName) {
		String value = getProperty(propertiesString, thePropertyName);
		return value;
	}
	
	/**
	 * Gets a boolean property
	 * @param propertiesString the entire property string
	 * @param thePropertyName name of property to get
	 * @param deafultValue value if not found
	 * @return the value of the property
	 */
	public static boolean getBooleanProperty(String propertiesString, String thePropertyName, boolean deafultValue) {
		String value = getProperty(propertiesString, thePropertyName);
		return getBooleanFromString(value, deafultValue);
	}
	
	/**
	 * Gets Integer from a String
	 * @param properties 
	 * @param strProperty
	 * @return
	 */
	public static Boolean getBooleanProperty(Properties properties, String strProperty) {
		String stringValue = getProperty(properties, strProperty);
		return getBooleanFromString(stringValue, false);
	}
	
	/**
	 * Gets Integer from a String 
	 * @param properties
	 * @param strProperty
	 * @return
	 */
	public static Boolean getBooleanProperty(Properties properties, String strProperty, boolean deafultValue) {
		String stringValue = getProperty(properties, strProperty);
		return getBooleanFromString(stringValue, deafultValue);
	}
	
	/**
	 * Gets Integer from a String 
	 * @param strProperty
	 * @return
	 */
	private static boolean getBooleanFromString(String strProperty, boolean defaultValue) {
		if (strProperty==null || "".equals(strProperty)) {
			return defaultValue;
		} else {
			if (strProperty.equalsIgnoreCase("true")) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	/**
	 * Method to set a property. 
	 * @param properties Properties object
	 * @param thePropertyName name of property
	 * @param thePropertyValue value of property
	 * @return the modified properties string
	 */
	public static void setProperty(Properties properties, String thePropertyName, String thePropertyValue) {
		if(thePropertyValue==null){
			properties.remove(thePropertyName);
		}else{
			properties.setProperty(thePropertyName, thePropertyValue);
		}
	}
	
	/**
	 * Method to set a boolean property. This is to easily expand the
	 * set of preferences without having to provide a database attribute
	 * for each. The preferences are stored in the database in regular
	 * Java property format.
	 * @param propertiesString the entire property string
	 * @param thePropertyName name of property
	 * @param thePropertyValue value of property
	 * @return the modified propertiesString
	 */
	public static void setBooleanProperty(Properties properties, String thePropertyName, Boolean thePropertyValue) {
		if (thePropertyValue!=null && thePropertyValue.equals(true)) {
			setProperty(properties, thePropertyName, "true");
		} else {
			setProperty(properties, thePropertyName, "false");
		}
	}
	
	
	/**
	 * Method to set a boolean property. This is to easily expand the
	 * set of preferences without having to provide a database attribute
	 * for each. The preferences are stored in the database in regular
	 * Java property format.
	 * @param propertiesString the entire property string
	 * @param thePropertyName name of property
	 * @param thePropertyValue value of property
	 * @return the modified propertiesString
	 */
	public static void setIntegerProperty(Properties properties, String thePropertyName, Integer thePropertyValue) {
		if (thePropertyValue!=null) {
			setProperty(properties, thePropertyName, thePropertyValue.toString());
		} else {
			properties.remove(thePropertyName);
		}
	}
	
	/**
	 * Method to set a property. This is to easily expand the
	 * set of preferences without having to provide a database attribute
	 * for each. The preferences are stored in the database in regular
	 * Java property format.
	 * @param propertiesString the entire property string
	 * @param thePropertyName name of property
	 * @param thePropertyValue value of property
	 * @return the modified properties string
	 */
	public static String setProperty(String propertiesString, String thePropertyName, String thePropertyValue) {
		Properties properties = getProperties(propertiesString);
		setProperty(properties, thePropertyName, thePropertyValue);
		return serializeProperties(properties);
	}
	
	/**
	 * Serialize the properties into a string
	 * @param properties
	 * @return
	 */
	public static String serializeProperties(Properties properties) {
		String propertiesString = null; 
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			properties.store(bos,"Genji Preferences");
			propertiesString = bos.toString();
		}
		catch (Exception e) {
			LOGGER.warn("problem with saving preferences " + e.getMessage());
		}
		return propertiesString;
	}
	
	/**
	 * Method to set a boolean property. This is to easily expand the
	 * set of preferences without having to provide a database attribute
	 * for each. The preferences are stored in the database in regular
	 * Java property format.
	 * @param propertiesString the entire property string
	 * @param thePropertyName name of property
	 * @param thePropertyValue value of property
	 * @return the modified propertiesString
	 */
	public static String setBooleanProperty(String propertiesString, String thePropertyName, Boolean thePropertyValue) {
		if (thePropertyValue!=null && thePropertyValue.equals(true)) {
			return setProperty(propertiesString, thePropertyName, "true");
		} else {
			return setProperty(propertiesString, thePropertyName, "false");
		}
	}
	
	
	public static String setStringProperty(String propertiesString, String thePropertyName, String thePropertyValue) {
		if (thePropertyValue!=null) {
			return setProperty(propertiesString, thePropertyName, thePropertyValue);
		} else {
			return setProperty(propertiesString, thePropertyName, "");
		}
	}
	
	/**
	 * Method to set a boolean property. This is to easily expand the
	 * set of preferences without having to provide a database attribute
	 * for each. The preferences are stored in the database in regular
	 * Java property format.
	 * @param propertiesString the entire property string
	 * @param thePropertyName name of property
	 * @param thePropertyValue value of property
	 * @return the modified propertiesString
	 */
	public static String setIntegerProperty(String propertiesString, String thePropertyName, Integer thePropertyValue) {
		if (thePropertyValue!=null) {
			return setProperty(propertiesString, thePropertyName, thePropertyValue.toString());
		} else {
			return removeProperty(propertiesString, thePropertyName);
		}
	}
	
	/**
	 * Method to get a property. This is to easily expand the
	 * set of preferences without having to provide a database attribute
	 * for each. The preferences are stored in the database in regular
	 * Java property format.
	 * @param propertiesString the entire property string
	 * @param thePropertyName name of property to get
	 * @return
	 */
	public static Integer[] getIntegerArrProperty(String propertiesString, String thePropertyName) {
		String strProperty = getProperty(propertiesString, thePropertyName);
		if (strProperty==null || strProperty.trim().length()==0) {
			return null;
		}
		return GeneralUtils.createIntegerArrFromCollection(GeneralUtils.createIntegerListFromStringArr(strProperty.split(",")));
	}
	
	/**
	 * Method to set a boolean property. This is to easily expand the
	 * set of preferences without having to provide a database attribute
	 * for each. The preferences are stored in the database in regular
	 * Java property format.
	 * @param propertiesString the entire property string
	 * @param thePropertyName name of property
	 * @param thePropertyValue value of property
	 * @return the modified propertiesString
	 */
	public static String setIntegerArrProperty(String propertiesString, String thePropertyName, Integer[] thePropertyValue) {
		if (thePropertyValue!=null && thePropertyValue.length>0) {
			StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < thePropertyValue.length; i++) {
				Integer intValue = thePropertyValue[i];
				if (intValue!=null) {
					stringBuilder.append(intValue.toString());
					if (i<thePropertyValue.length) {
						stringBuilder.append(",");
					}
				}
			}
			return setProperty(propertiesString, thePropertyName, stringBuilder.toString());
		} else {
			return removeProperty(propertiesString, thePropertyName);
		}
	}
	
	/**
	 * Method to set a property. This is to easily expand the
	 * set of preferences without having to provide a database attribute
	 * for each. The preferences are stored in the database in regular
	 * Java property format.
	 * @param propertiesString the entire property string
	 * @param thePropertyName name of property
	 * @param thePropertyValue value of property
	 * @return the modified properties string
	 */
	public static String removeProperty(String propertiesString, String thePropertyName) {
		Properties properties = getProperties(propertiesString);
		properties.remove(thePropertyName);
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			properties.store(bos,"Genji Preferences");
			propertiesString = bos.toString();
		}
		catch (Exception e) {
			LOGGER.warn("problem with saving preferences " + e.getMessage());
		}
		return propertiesString;
	}
	
	
}
