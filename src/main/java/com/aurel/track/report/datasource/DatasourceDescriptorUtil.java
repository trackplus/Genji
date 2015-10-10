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

package com.aurel.track.report.datasource;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aurel.track.plugin.DatasourceDescriptor;
import com.aurel.track.plugin.PluginManager;

public class DatasourceDescriptorUtil {    
    
    private static List datasourceDescriptorsList;                    
    private static Map datasourceDescriptorsMap;        
    public synchronized static DatasourceDescriptor getDatasourceDescriptor(String pluginID) {
    	if (datasourceDescriptorsMap==null) {
    		datasourceDescriptorsMap = new HashMap();    	
    		datasourceDescriptorsList = getDatasourceDescriptors();    		
    		addDescriptorListToMap(datasourceDescriptorsMap, datasourceDescriptorsList);
    	}
    	
        return (DatasourceDescriptor)datasourceDescriptorsMap.get(pluginID);
    }

    private static void addDescriptorListToMap(Map datasourceDescriptorsMap, List datasourceDescriptorsList) {
    	if (datasourceDescriptorsList!=null) {
    		Iterator iterator = datasourceDescriptorsList.iterator();
    		while (iterator.hasNext()) {
				DatasourceDescriptor datasourceDescriptor =  (DatasourceDescriptor) iterator.next();
				datasourceDescriptorsMap.put(datasourceDescriptor.getId(), datasourceDescriptor);
			}
        }
    }
   
    
    /**
     * Cache the fieldType plugins
     *
     * @return
     */
    public synchronized static List getDatasourceDescriptors() {
    	if (datasourceDescriptorsList==null) {
    		datasourceDescriptorsList = PluginManager.getInstance().getDatasourceDescriptors();	              
    	}
    	return datasourceDescriptorsList;
    }
    
    
}
