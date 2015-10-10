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

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.localize.LocalizeBL;
import com.aurel.track.beans.TLocalizedResourcesBean;

/**
 * Implementation of database resource bundle
 * @author Tamas Ruff
 *
 */
public class DatabaseResourceBundle extends ResourceBundle {
	private static final Logger LOGGER = LogManager.getLogger(DatabaseResourceBundle.class);
	private Properties properties;
	
	public DatabaseResourceBundle(Properties properties) {
		this.properties = properties;
	}

	@Override
	public Enumeration<String> getKeys() {
		return properties!=null ? ((Enumeration<String>) properties.propertyNames()) : null;
	}
	
	@Override
	protected Object handleGetObject(String key) {
		return properties.getProperty(key);
	}

	public static ResourceBundle.Control getDatabaseControl() {
		return new ResourceBundle.Control() {
			public List<String> getFormats(String baseName) {
				if (baseName == null) {
					throw new NullPointerException();
				}
				return Arrays.asList("db", "java.properties");
			}

			public ResourceBundle newBundle(String baseName, Locale locale, String format,
				ClassLoader loader, boolean reload) throws IllegalAccessException,
				InstantiationException, IOException {
				if (baseName==null || locale==null || format==null || loader==null) {
					throw new NullPointerException();
				}
				ResourceBundle bundle = null;
				if (format.equals("db")) {
					String localeStr = locale.toString();
					if ("".equals(localeStr)) {
						localeStr = "default";
					}
					Properties p = getResourcesForLocale(locale);
					LOGGER.debug("Number of resources in properties for locale " + localeStr + ": " + p.size());
					bundle = new DatabaseResourceBundle(p);
					LOGGER.debug("Number of keys in resource bundle for locale " + localeStr + ": " + bundle.keySet().size());
				}
				return bundle;
			}

			@Override
			public long getTimeToLive(String baseName, Locale locale) {
				return 1000 * 60 * 30;
			}

			@Override
			public boolean needsReload(String baseName, Locale locale, String format, ClassLoader loader, ResourceBundle bundle, long loadTime) {
				return true;
			}
			
			/**
			 * Get all resources for a locale
			 * @param locale
			 * @return
			 */
			public Properties getResourcesForLocale(Locale locale) {
				Properties properties = new Properties();
				String localeString = null;
				if (locale!=null) {
					localeString = locale.toString();
					if ("".equals(localeString)) {
						localeString = null;
					}
				}
				List<TLocalizedResourcesBean> localizedResourcesBeanList = LocalizeBL.getLocalizedResourcesForLocale(localeString);
				if (localizedResourcesBeanList!=null) {
					if (LOGGER.isDebugEnabled()) {
						String localeStr = null;
						if (localeString==null) {
							localeStr = "default";
						} else {
							localeStr = localeString;
						}
						LOGGER.debug("Number of resources found in database for locale " + localeStr + ": " + localizedResourcesBeanList.size());
					}
					for (TLocalizedResourcesBean localizedResourcesBean : localizedResourcesBeanList) {
						Integer primaryKey = localizedResourcesBean.getPrimaryKeyValue();
						String resourceKey = localizedResourcesBean.getFieldName();
						if (primaryKey!=null) {
							resourceKey = resourceKey + primaryKey.toString();
						}
						String localizedText = localizedResourcesBean.getLocalizedText();
						properties.setProperty(resourceKey, localizedText);
					}
				}
				return properties;
			}
		};
	}
}

