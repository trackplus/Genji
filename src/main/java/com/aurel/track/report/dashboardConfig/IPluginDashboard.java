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


package com.aurel.track.report.dashboardConfig;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.util.LabelValueBean;

/**
 * This interface define some operation needed for dashboard plugin.
 * 
 * If you want to develop a new dashboard plugin you should extend BasePluginDashboardView
 * 
 * @author Adrian Bojani
 */
public interface IPluginDashboard {
	/**
	 * Get an unique id for the plugin
	 * @return the id of plugin
	 */
	public String getPluginID();

	/**
	 * Convert/obtain the parameters obtained from the configuration page in to the persistent parameters.
	 * @param properties the parameters from configuration page 
	 * @param user TPersonBean of the current user
	 * @return the persistent map of parameters associated to the field: Map<String,String>
	 */
	Map<String,String> convertMapFromPageConfig(Map<String,String> properties, TPersonBean user,Locale locale,Integer entityId, Integer entityType, List<LabelValueBean> errors);


	String createJsonData(Integer dashboardID, Map<String,Object> session,Map<String,String> parameters,Integer projectID,Integer releaseID,Map<String,String> ajaxParams);
	String createJsonDataConfig(Map<String,String> parameters, TPersonBean user, Integer entityId, Integer entityType);
	
	List<ReportBean> getIssues(Map<String, String> configParams, Map<String, String> filterParams, TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException;
}
