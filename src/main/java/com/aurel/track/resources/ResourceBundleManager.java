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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.util.PluginUtils;

/**
 * Used to have access to resource bundles outside of struts
 * @author Tamas Ruff 
 *
 */
public class ResourceBundleManager {
	private static final Logger LOGGER = LogManager.getLogger(ResourceBundleManager.class); 
	public static String APPLICATION_RESOURCES_STRUTS2 = "resources.UserInterface.ApplicationResources";
	public static String DATABASE_RESOURCES ="database";
	public static String ONLINEHELP_RESOURCES ="resources.OnlineHelpResources";
	public static String FIELDTYPE_RESOURCES ="resources.FieldTypeResources.FieldTypeResources";
	public static String DASHBOARD_RESOURCES ="resources.DashboardResources.DashboardResources";
	public static String DATASOURCE_RESOURCES ="resources.DatasourceResources.DatasourceConfigResources";
	//loading the bulk by startup before login
	public static String LOADER_RESOURCES = "resources/UserInterface/LoaderResources";
	
	//singleton instance
	private static ResourceBundleManager instance;
	/**
	 * get a singleton instance
	 * @return
	 */
	public static ResourceBundleManager getInstance() {
		if (instance == null) {
			instance = new ResourceBundleManager();
			//localesList=new ArrayList();
		}
		return instance;
	}

	/**
	 * constructor
	 */
	private ResourceBundleManager() {
	}
	
	/**
	 * Gets the loader resources for bulk by startup before login
	 * @param locales
	 * @return
	 */
	public static ResourceBundle getLoaderResourceBundle(Enumeration<Locale> locales) {
		ResourceBundle rb = null;
		//first try based on user's browser settings
		for (Enumeration<Locale> e = locales; e.hasMoreElements(); ) {
			Locale locale = e.nextElement();
			try {
				rb = ResourceBundle.getBundle(LOADER_RESOURCES, locale);
				if (rb!=null) {
					break;
				}
			} catch (MissingResourceException ex) {
				LOGGER.debug("LoaderResources for browser locale " + locale.toString() + " failed with " + ex.getMessage());
			}
		}
		if (rb==null) {
			//based on server default
			Locale locale =  Locale.getDefault();
			try {
				rb = ResourceBundle.getBundle(LOADER_RESOURCES, Locale.getDefault());
			} catch (MissingResourceException ex) {
				LOGGER.debug("LoaderResources for defualt locale " + locale.toString() + " failed with " + ex.getMessage());
			}
		}
		if (rb==null) {
			//fall back to english
			try {
				rb = ResourceBundle.getBundle(LOADER_RESOURCES, Locale.ENGLISH);
			} catch (MissingResourceException ex) {
				LOGGER.debug("LoaderResources for locale en failed with " + ex.getMessage());
			}
		}
		return rb;
	}
	
	/**
	 * Load the resource bundle with the specific locale in the resourceBundleCache
	 * @param resourceName
	 * @param locale
	 * @return
	 */
	public ResourceBundle getResourceBundle(String resourceName, Locale locale) {
		ResourceBundle resourceBundle = null;
		try {
			if (resourceName.equals(DATABASE_RESOURCES)) {
				resourceBundle = ResourceBundle.getBundle("", locale, DatabaseResourceBundle.getDatabaseControl());
			} else {
				resourceBundle = ResourceBundle.getBundle(resourceName, locale);
			}
		} catch (Exception e) {
			LOGGER.debug("Getting the resourceName " + resourceName + " for locale " + locale + " failed with " + e.getMessage() );
		}
		return resourceBundle;
	}

	/**
	 * Get the bundles available with symbolic names
	 * 
	 */
	public List<String> getBundles() {
		List<String> list = new ArrayList<String>();
		list.add(DATABASE_RESOURCES);
		list.add(ONLINEHELP_RESOURCES);
		list.add(DASHBOARD_RESOURCES);
		list.add(FIELDTYPE_RESOURCES);
		list.add(DATASOURCE_RESOURCES);
		if (PluginUtils.getBundles() != null && PluginUtils.getBundles().size() > 0) {
			list.addAll(PluginUtils.getBundles());
		}
		return list;
	}
	
}
