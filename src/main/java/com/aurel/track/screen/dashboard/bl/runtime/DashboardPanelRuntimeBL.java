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

package com.aurel.track.screen.dashboard.bl.runtime;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.beans.TDashboardFieldBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.screen.IField;
import com.aurel.track.plugin.DashboardDescriptor;
import com.aurel.track.report.dashboard.BasePluginDashboardView;
import com.aurel.track.report.dashboardConfig.IPluginDashboard;
import com.aurel.track.screen.FieldWrapper;
import com.aurel.track.screen.ScreenFactory;
import com.aurel.track.screen.bl.AbstractPanelBL;
import com.aurel.track.screen.dashboard.DashboardFieldWrapper;
import com.aurel.track.screen.dashboard.adapterDAO.DashboardScreenFactory;
import com.aurel.track.screen.dashboard.bl.DashboardUtil;

/**
 * A Business Logic class for ScreenPanel for runtime
 * @author Adrian Bojani
 *
 */
public class DashboardPanelRuntimeBL extends AbstractPanelBL {
	private static final String errorJsClass ="js.ext.com.track.dashboard.Error";
	private Locale locale;
	private TPersonBean user;
	private String prefixMapping;
	private Map session;

	private Integer projectID;
	private Integer releaseID;
	// singleton instance
	//private static DashboardPanelRuntimeBL instance;

	/**
	 * get a singleton instance
	 * @return
	 */
	/*public static DashboardPanelRuntimeBL getInstance() {
		if (instance == null) {
			instance = new DashboardPanelRuntimeBL();
		}
		return instance;
	}*/

	@Override
	protected ScreenFactory getScreenFactory() {
		return DashboardScreenFactory.getInstance();
	}

	/**
	 * constructor
	 */
	public DashboardPanelRuntimeBL() {
		super();
	}

	@Override
	protected FieldWrapper createFieldWrapper() {
		return new DashboardFieldWrapper();
	}

	@Override
	protected void updateFieldWrapper(FieldWrapper fw, IField fieldScreen) {
		DashboardFieldWrapper  fieldWrapper=(DashboardFieldWrapper)fw;
		TDashboardFieldBean dashboardField=(TDashboardFieldBean)fieldScreen;
		DashboardDescriptor dashboardDescriptor=DashboardUtil.getDescriptor(dashboardField);
		if(dashboardDescriptor==null){
			Map map=new HashMap();
			map.put("dashboardId",dashboardField.getDashboardID());
			fieldWrapper.setJsonData("{}");
			fieldWrapper.setJsClass(errorJsClass);
		}else{
			IPluginDashboard plugin= DashboardUtil.getPlugin(dashboardDescriptor.getTheClassName());
			String jsClass=dashboardDescriptor.getJsClass();
			if(jsClass==null||jsClass.trim().length()==0){
				jsClass=errorJsClass;
			}
			fieldWrapper.setJsClass(jsClass);
			Integer dashboardID=fw.getField().getObjectID();
			Map<String,String> configParamsMap=dashboardField.getParametres();
			if (locale!=null) {
				configParamsMap.put(BasePluginDashboardView.CONFIGURATION_PARAMS.LOCALE_STRING, locale.toString());
			}
			fieldWrapper.setJsonData(plugin.createJsonData(dashboardID,session,configParamsMap,projectID,releaseID,null));
		}
	}
	
	public TPersonBean getUser() {
		return user;
	}
 

	public void setUser(TPersonBean user) {
		this.user = user;
	}

	/**
	 * @return the prefixMapping
	 */
	public String getPrefixMapping() {
		return prefixMapping;
	}
	/**
	 * @param prefixMapping the prefixMapping to set
	 */
	public void setPrefixMapping(String prefixMapping) {
		this.prefixMapping = prefixMapping;
	}
	public Map getSession() {
		return session;
	}

	public void setSession(Map session) {
		this.session = session;
	}

	public Integer getProjectID() {
		return projectID;
	}

	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	public Integer getReleaseID() {
		return releaseID;
	}

	public void setReleaseID(Integer releaseID) {
		this.releaseID = releaseID;
	}
}
