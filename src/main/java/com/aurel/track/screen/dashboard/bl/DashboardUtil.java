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

package com.aurel.track.screen.dashboard.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TDashboardFieldBean;
import com.aurel.track.plugin.DashboardDescriptor;
import com.aurel.track.plugin.PluginDescriptor;
import com.aurel.track.plugin.PluginManager;
import com.aurel.track.report.dashboardConfig.IPluginDashboard;
import com.aurel.track.screen.dashboard.action.DashboardAJAXAction;

/**
 * An util class for dashboard
 */
public class DashboardUtil {

	private static final Logger LOGGER = LogManager.getLogger(DashboardAJAXAction.class);

    private static Map cachePlugins;
    //List<IPluginDashboard> a list with all names of plugins
    private static Map descriptors;
    public static void invalidateCache() {
        cachePlugins = null;
        descriptors=null;
    }
    public static String[] getDefautPlugins(){
        return new String[]{"com.aurel.track.report.dashboard.MyItems","com.aurel.track.report.dashboard.ActivityStream"};
    }

    public static String[] getDefaultPluginForClientUsers() {
    	return new String[]{"com.aurel.track.report.dashboard.MyFilterView", "com.aurel.track.report.dashboard.ActivityStream", "com.aurel.track.report.dashboard.MyFilterView"};
    }

    public static DashboardDescriptor getDescriptor(String descriptorId){
        if(descriptors==null){
            getDashboardPlugins();
        }
        return (DashboardDescriptor)descriptors.get(descriptorId);
    }
    public static DashboardDescriptor getDescriptor(TDashboardFieldBean field){
        return getDescriptor(field.getDashboardID());
    }
    public static IPluginDashboard getPlugin(TDashboardFieldBean field) {
       return  getPlugin(getDescriptor(field).getTheClassName());
    }
	public static IPluginDashboard getPluginByID(String dashboardID) {
		IPluginDashboard result=null;
		if(dashboardID!=null){
			DashboardDescriptor descriptor=getDescriptor(dashboardID);
			if(descriptor!=null){
				result=getPlugin(descriptor.getTheClassName());
			}
		}
		return  result;
	}
    public static IPluginDashboard getPlugin(String className) {
        if (cachePlugins == null) {
            cachePlugins = new HashMap();
        }
        Class pluginClass = (Class) cachePlugins.get(className);
        if (pluginClass == null) {
            try{
            	pluginClass=Class.forName(className);
            } catch (ClassNotFoundException e) {
                LOGGER.error(ExceptionUtils.getStackTrace(e));
                return null;
            }
            cachePlugins.put(className, pluginClass);
        }
        try {
        	IPluginDashboard plugin = (IPluginDashboard) pluginClass.newInstance();
            return plugin;
        } catch (InstantiationException e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        } catch (IllegalAccessException e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return null;//plugin class problem
    }

    /**
     * Find all the dashboard plugins
     *
     * @return
     */
    public static List getDashboardPlugins() {
        List lst=PluginManager.getInstance().getDashboardDescriptors();
        descriptors=new HashMap();
        for (int i = 0; i < lst.size(); i++) {
            PluginDescriptor o =  (PluginDescriptor)lst.get(i);
            descriptors.put(o.getId(),o);
        }
        return lst;
    }
}
