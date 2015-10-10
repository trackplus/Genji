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

package com.aurel.track.itemNavigator.filterInMenu;

import java.util.Locale;

import com.aurel.track.beans.TDashboardFieldBean;
import com.aurel.track.plugin.DashboardDescriptor;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.resources.ResourceBundleManager;
import com.aurel.track.screen.dashboard.bl.DashboardUtil;

/**
 * Rendering the dashboard filter in item navigator menu
 * @author Tamas
 *
 */
public class DashboardFilterInMenu extends FilterInMenuBase {

	public static String DAHBOARD_ICON = "dashboard-ticon";
	
	
	public DashboardFilterInMenu(Integer queryID) {
		super(queryID);
	}

	/**
	 * Gets the label for the filter
	 * @param entityBean
	 * @param filterExpression
	 * @param locale
	 * @return
	 */
	public String getLabel(Object entityBean, String filterExpression, Locale locale) {
		String queryTitle = null;
		TDashboardFieldBean dashboardItemBean=(TDashboardFieldBean)entityBean;
		if (dashboardItemBean!=null) {
			String title=dashboardItemBean.getParametres().get("title");
			if(title==null||title.trim().length()==0){
				DashboardDescriptor desc=DashboardUtil.getDescriptor(dashboardItemBean);
				title=desc.getLabel();
			}
			queryTitle=title;
			
		}
		if (queryTitle!=null) {
			 return LocalizeUtil.getLocalizedText(queryTitle, locale, ResourceBundleManager.DASHBOARD_RESOURCES);
		} else {
			return LocalizeUtil.getLocalizedTextFromApplicationResources("menu.cockpit",locale);
		
		}
	}
	
	/**
	 * Gets the iconCls for the filter
	 * @param entityBean
	 * @return
	 */
	public String getIconCls(Object entityBean) {
		return DAHBOARD_ICON;
	}
	
}
