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


package com.aurel.track.resources;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.localize.LocalizeBL;
import com.aurel.track.beans.ILocalizedLabelBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.util.IntegerStringBean;


/**
 * Localization helpers 
 * @author Tamas Ruff
 *
 */
public class LocalizeUtil {
	private static final Logger LOGGER = LogManager.getLogger(LocalizeUtil.class);
	
	
	/**
	 * Methods for getting the localization from the database
	 */
	
	/**
	 * Get the localized text by field name and primary key from the database bundle
	 * @param fieldName
	 * @param primaryKey
	 * @param locale
	 * @return
	 */
	public static String getLocalizedEntity(String fieldName, Integer key, Locale locale) {
		if (key!=null) {
			fieldName = fieldName + key;
		}
		ResourceBundle resourceBundle = ResourceBundleManager.getInstance().getResourceBundle(
				ResourceBundleManager.DATABASE_RESOURCES, locale);
		try {
			return resourceBundle.getString(fieldName);
		} catch (Exception e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Getting the localized text for key " + fieldName + 
						" form database and locale " + locale + " failed with " + e.getMessage(), e);
			}
			return null;
		}
	}
	
	/**
	 * Gets the parametrized string for a specific locale
	 * @param key
	 * @param locale
	 * @return
	 */
	public static String getLocalizedTextFromApplicationResources(String key, Locale locale) {
		ResourceBundle resourceBundle = ResourceBundleManager.getInstance().getResourceBundle(ResourceBundleManager.DATABASE_RESOURCES, locale);
		try {
			return resourceBundle.getString(key);
		} catch (Exception e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Getting the localized text for key " + key + 
						" form database and locale " + locale + " failed with " + e.getMessage(), e);
			}
			return key;
		}
	}
	
	/**
	 * Gets the parametrized string for a specific locale
	 * @param key
	 * @param arguments
	 * @param locale
	 * @return
	 */
	public static String getParametrizedString(String key, Object[] arguments, Locale locale) {
		MessageFormat formatter = new MessageFormat("");
		formatter.setLocale(locale);
		String pattern = getLocalizedTextFromApplicationResources(key, locale);
		if (pattern!=null) {			
			formatter.applyPattern(pattern);
			try {
				return formatter.format(arguments);
			}catch(Exception ex) {
				System.out.println(arguments);
			}
		}
		return "";
	}
	
	/**
	 * Localize a single TFieldConfigBean
	 * i.e. set the localized fields localizedLabel and localizedTooltip
	 * Fall back to BoxResources if not found in the database 
	 * @param fieldConfigBean
	 * @param locale
	 * @return
	 */
	public static TFieldConfigBean localizeFieldConfig(TFieldConfigBean fieldConfigBean, Locale locale) {
		if (fieldConfigBean!=null) {
			Integer fieldConfigID = fieldConfigBean.getObjectID();
			String localizedLabel = getLocalizedEntity(
					LocalizationKeyPrefixes.FIELD_LABEL_KEY_PREFIX, fieldConfigID, locale);
			String localizedTooltip = getLocalizedEntity(
					LocalizationKeyPrefixes.FIELD_TOOLTIP_KEY_PREFIX, fieldConfigID, locale);
			if (localizedLabel!=null && localizedLabel.length()>0) {
				fieldConfigBean.setLabel(localizedLabel);
			}
			if (localizedTooltip!=null && localizedTooltip.length()>0) {
				fieldConfigBean.setTooltip(localizedTooltip);
			}
		}
		return fieldConfigBean;
	}
	
	/**
	 * Localize a list of TFieldConfigBeans 
	 * @param fieldConfigBeans
	 * @param locale
	 * @return
	 */
	public static List<TFieldConfigBean> localizeFieldConfigs(List<TFieldConfigBean> fieldConfigBeans, Locale locale) {
		if (fieldConfigBeans!=null) {
			for (TFieldConfigBean fieldConfigBean : fieldConfigBeans) {
				localizeFieldConfig(fieldConfigBean, locale);
			}
		}
		return fieldConfigBeans;
	}
	
	/**
	 * Gets a map of localized config labels
	 * 	-	key: 	fieldID
	 * 	-	value:	localized field label
	 * @param groupFieldIDs list of fieldIDs
	 * @param locale
	 * @return
	 */
	public static Map<Integer, String> getLocalizedFieldConfigLables(List<TFieldConfigBean> fieldConfigBeans, Locale locale) {
		Map<Integer, String> fieldIDToFieldName = new HashMap<Integer, String>();
		List<TFieldConfigBean> localizedFieldConfigs = localizeFieldConfigs(fieldConfigBeans, locale);
		if (localizedFieldConfigs!=null) {
			for (TFieldConfigBean fieldConfigBean : localizedFieldConfigs) {
				fieldIDToFieldName.put(fieldConfigBean.getField(), fieldConfigBean.getLabel());
			}
		}
		return fieldIDToFieldName;
	}
	
	/**
	 * Returns the  ILocalizedLabelBean
	 * @param localizedLabelBean
	 * @param locale
	 * @return
	 */
	public static Integer getDropDownPrimaryKeyFromLocalizedText(String prefix, String localizedText, Locale locale) {
		Integer primaryKey = LocalizeBL.getPrimaryKey(prefix, localizedText, locale);
		if (primaryKey!=null) {
			return primaryKey;
		}
		return null;
	}
	
	/**
	 * Returns the the label String of the ILocalizedLabelBean
	 * @param localizedLabelBean
	 * @param locale
	 * @return
	 */
	public static String localizeDropDownEntry(ILocalizedLabelBean localizedLabelBean, Locale locale) {
		if (localizedLabelBean!=null) {
			String localizedLabel = getLocalizedEntity(localizedLabelBean.getKeyPrefix(), localizedLabelBean.getObjectID(), locale);
			if (localizedLabel!=null && localizedLabel.length()>0) {
				return localizedLabel;
			} else {
				return localizedLabelBean.getLabel();
			}
		}
		return "";
	}
	
	/**
	 * Returns the  ILocalizedLabelBean
	 * @param localizedLabelBean
	 * @param locale
	 * @return
	 */
	public static String localizeSystemStateEntry(ILocalizedLabelBean localizedLabelBean, int entityFlag,  Locale locale) {
		if (localizedLabelBean!=null) {
			String localizedLabel = getLocalizedEntity(localizedLabelBean.getKeyPrefix() + entityFlag + ".", localizedLabelBean.getObjectID(), locale);
			if (localizedLabel!=null && localizedLabel.length()>0) {
				return localizedLabel;
			} else {
				return localizedLabelBean.getLabel();
			}
		}
		return "";
	}
	
	/**
	 * Localize a list of ILocalizedLabelBeans
	 * @param dataSource
	 * @param locale
	 * @return
	 */
	public static List localizeDropDownList(List dataSource, Locale locale) {
		if (locale==null) {
			return dataSource;
		}		
		if (dataSource!=null) {
			for (Object bean : dataSource) {
				ILocalizedLabelBean localizedLabelBean = (ILocalizedLabelBean)bean;
				String localizedText = localizeDropDownEntry(localizedLabelBean, locale);
				if (localizedText!=null && localizedText.length()>0 && !localizedText.equals(localizedLabelBean.getLabel())) {
					localizedLabelBean.setLabel(localizedText);
				}
			}
		}
		return dataSource;
	}
	
	/**
	 * Gets a list of IntegerStringBean with ids and localised names from a bundle 
	 * according to a prefix key and an list of Integers
	 * @param keyPrefix
	 * @param idArray	 
	 * @param locale
	 * @return list of IntegerStringBeans
	 */
	public static List<IntegerStringBean> getLocalizedList(String keyPrefix, List<Integer> integerList, Locale locale) {
		List<IntegerStringBean> localizedList = new LinkedList<IntegerStringBean>();
		if (integerList!=null) {
			for (Integer integerValue : integerList) {
				String label=getLocalizedTextFromApplicationResources(keyPrefix + integerValue, locale);
				if (label==null || "".equals(label.trim())) {
					label = integerValue.toString();
				}
				localizedList.add(new IntegerStringBean(label, integerValue));
			}
		}
		return localizedList;
	}
	
	/**
	 * Gets a list of IntegerStringBean with ids and localised names from resources 
	 * according to a prefix string and an array of ints
	 * @param keyPrefix
	 * @param idArray	 
	 * @param locale
	 * @return list of IntegerStringBeans
	 */
	public static List<IntegerStringBean> getLocalizedList(String keyPrefix, int[] idArray, Locale locale) {
		List<IntegerStringBean> localizedList = new LinkedList<IntegerStringBean>();
		for (int i=0; i<idArray.length; i++) {
			int value = idArray[i];	
			String label=getLocalizedTextFromApplicationResources(keyPrefix + new Integer(value), locale);
			if (label==null || "".equals(label.trim())) {
				label = new Integer(value).toString();
			}
			localizedList.add(new IntegerStringBean(label, new Integer(value)));
		}
		return localizedList;
	}
	
	/**********************************************************************
	 * Methods for getting the localization from external resource bundles
	 *********************************************************************/
	
	/**
	 * Get the localized text by key from a bundle
	 * @param key
	 * @param locale
	 * @param bundleName
	 * @return
	 */
	public static String getLocalizedText(String key, Locale locale, String bundleName) { 
		ResourceBundle resourceBundle = ResourceBundleManager.getInstance().getResourceBundle(bundleName, locale);
		try {
			return resourceBundle.getString(key);
		} catch (Exception e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Getting the localized text for key " + key + 
						" bundleName " + bundleName + " and locale " + locale + " failed with " + e.getMessage(), e);
			}
			return key;
		}
	}
	
	/**
	 * Get the localized text by key and parameters from a bundle
	 * @param key
	 * @param locale
	 * @param bundleName
	 * @param arguments
	 * @return
	 */
	public static String getLocalizedTextWithParams(String key, Locale locale, String bundleName, Object[] arguments){
		MessageFormat formatter = new MessageFormat("");
		formatter.setLocale(locale);
		String pattern = getLocalizedText(key, locale,bundleName);
		String result=pattern;
		if (pattern!=null) {
			try{
				formatter.applyPattern(pattern);
				result= formatter.format(arguments);
			}catch (Exception e) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Getting the localized text for key " + key + 
							" bundleName " + bundleName + " and locale " + locale + " and argumnets " + arguments + " failed with " + e.getMessage(), e);
				}
			}
		}
		return result;
	}
	
}
