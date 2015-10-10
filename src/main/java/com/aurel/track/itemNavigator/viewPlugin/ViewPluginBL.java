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

package com.aurel.track.itemNavigator.viewPlugin;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.itemNavigator.IssueListViewPlugin;

/**
 * Logic for view plugins
 * @author Tamas
 *
 */
public class ViewPluginBL {
	
	private static final Logger LOGGER = LogManager.getLogger(ViewPluginBL.class);
	//cache the plugin classes
	public static Map<String, Class> cachePlugins;
	
	public synchronized static IssueListViewPlugin getPlugin(String className) {
		if (cachePlugins==null) {
			cachePlugins = new HashMap<String, Class>();
		}
		Class pluginClass = (Class) cachePlugins.get(className);
		if (pluginClass==null) {
			try{
				pluginClass = Class.forName(className);
			} catch (ClassNotFoundException e) {
				LOGGER.error(ExceptionUtils.getStackTrace(e));
				return null;
			}
			cachePlugins.put(className, pluginClass);
		}
		try {
			return (IssueListViewPlugin) pluginClass.newInstance();
		} catch (InstantiationException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		} catch (IllegalAccessException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;//plugin class problem
	}

}
