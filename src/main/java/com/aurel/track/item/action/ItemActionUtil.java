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

package com.aurel.track.item.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.plugin.ItemActionDescription;
import com.aurel.track.plugin.PluginDescriptor;
import com.aurel.track.plugin.PluginManager;

/**
 * An helper class use to obtain the configuration for givenaction
 * @author Adrian Bojani
 *
 */
public class ItemActionUtil {
	
	private static final Logger LOGGER = LogManager.getLogger(ItemActionUtil.class);

	private static Map cachePlugins;
	//List<IPluginDashboard> a list with all names of plugins
	private static Map descriptors;
	public static void invalidateCache() {
		cachePlugins = null;
		descriptors=null;
	}
	public static ItemActionDescription getDescriptor(String descriptorId){
		if(descriptors==null){
			getItemActionPlugins();
		}
		return (ItemActionDescription)descriptors.get(descriptorId);
	}
	public static IPluginItemAction getPlugin(Integer actionID) {
		return  getPlugin(getDescriptor(actionID.toString()).getTheClassName());
	}
	public static IPluginItemAction getPlugin(String className) {
		if (cachePlugins == null) {
			cachePlugins = new HashMap();
		}
		IPluginItemAction plugin = (IPluginItemAction) cachePlugins.get(className);
		if (plugin != null) {
			return plugin;
		}
		try {
			plugin = (IPluginItemAction) Class.forName(className).newInstance();
			cachePlugins.put(className, plugin);
			return plugin;
		} catch (InstantiationException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		} catch (IllegalAccessException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		} catch (ClassNotFoundException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;//plugin class problem
	}

	/**
	 * Find all the dashboard plugins
	 *
	 * @return
	 */
	public static List getItemActionPlugins() {
		List lst=PluginManager.getInstance().getItemActionDescriptors();
		descriptors=new HashMap();
		for (int i = 0; i < lst.size(); i++) {
			PluginDescriptor o =  (PluginDescriptor)lst.get(i);
			descriptors.put(o.getId(),o);
		}
		return lst;
	}
}
