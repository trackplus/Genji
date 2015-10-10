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

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.json.JSONUtility;

/**
 * This class provides a JSON object containing all localized
 * resources for the application. It is being used when
 * the application first loads, and after the locale is changed
 * or resources have been changed for the currently used locale.
 */
public class LocalizeJSON{
	private static final Logger LOGGER = LogManager.getLogger(LocalizeJSON.class);

	/**
	 * This methods returns a JSON object containing all
	 * resources for the application for the given locale
	 * in a key-value format.
	 * 
	 * @param locale - the locale for the resources
	 * @return a JSON string with all resources in
	 */
	public static String encodeLocalization(Locale locale){
		LOGGER.debug("Encode localization for locale: "+locale);
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		ResourceBundleManager resourceBundleManager=ResourceBundleManager.getInstance();
		List<String> bundles=resourceBundleManager.getBundles();
		for (String bundleName : bundles) {
			boolean debugDatabase = LOGGER.isDebugEnabled() && ResourceBundleManager.DATABASE_RESOURCES.equals(bundleName);
			//get the values from the locale specific resource bundle...
			ResourceBundle resourceBundle=resourceBundleManager.getResourceBundle(bundleName, locale);
			if (debugDatabase) {
				if (resourceBundle==null) {
					LOGGER.info("No resource bundle found for: "+bundleName + " and locale " + locale);
				} else {
					LOGGER.debug("ResourceBundle found for: "+bundleName + " and locale " + locale);
				}
			}
			//but get the keys from the defaultResourceBundle because the default is complete 
			//and if a key is not present in the resourceBundle then it should fall back to the default value
			ResourceBundle defaultResourceBundle = resourceBundleManager.getResourceBundle(bundleName,new Locale(""));
			if (debugDatabase) {
				LOGGER.debug("Number of keys in resource bundle " + bundleName +  " for locale " + locale.toString() + ": " + resourceBundle.keySet().size());
				LOGGER.debug("Number of keys in default resource bundle " + bundleName  + ": " + defaultResourceBundle.keySet().size());
			}
			if (defaultResourceBundle != null) {
				if (debugDatabase) {
					LOGGER.debug("DefaultResourceBundle found for: "+bundleName);
				}
				//Enumeration<String> keys= defaultResourceBundle.getKeys();
				Set<String> keys = defaultResourceBundle.keySet();
				if (ResourceBundleManager.DATABASE_RESOURCES.equals(bundleName) && (keys==null || keys.isEmpty())) {
					//the database resources was not loaded: probably the first access to the login page was too early (before initializing the database) and the empty bundle is cached
					ResourceBundle.clearCache();
					LOGGER.debug("Clear cache for " + bundleName);
				}
				int count=0;
				//while (keys.hasMoreElements()) {
				for (String key : keys) {
					//String key=keys.nextElement();
					String resource = null;
					if (resourceBundle != null) {
						try {
							resource = resourceBundle.getString(key);
						} catch(Exception t) {
							LOGGER.debug("Getting the resource for key from resource bundle for locale " + locale.toString() + " and key " + key + " failed with " + t.getMessage());
						}
					} else {
						try {
							resource = defaultResourceBundle.getString(key);
						} catch (Exception t) {
							LOGGER.debug("Getting the resource for key from default resource bundle for key " + key + " failed with " + t.getMessage());
						}
					}
					if (resource!=null) {
						JSONUtility.appendStringValue(sb, key, resource);
					}
					count++;
				}
				if (debugDatabase) {
					LOGGER.debug("Resource entries count:"+count);
				}
			}else{
				if (debugDatabase) {
					LOGGER.info("No default resource bundle found for:"+bundleName);
				}
			}
		}
		if(sb.length()>1){
			//remove last ","
			sb.deleteCharAt(sb.length()-1);
		}
		sb.append("}");
		return sb.toString();
	}
}
