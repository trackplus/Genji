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

package com.aurel.track.screen.dashboard.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TDashboardTabBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.json.JSONUtility.JSON_FIELDS;
import com.aurel.track.screen.dashboard.bl.runtime.DashboardPanelRuntimeBL;
import com.aurel.track.screen.dashboard.bl.runtime.DashboardScreenRuntimeJSON;
import com.aurel.track.screen.dashboard.bl.runtime.DashboardTabRuntimeBL;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 */
public class DashboardTabRuntime extends ActionSupport implements Preparable, SessionAware {

	private static final long serialVersionUID = 340L;
	
	private static final Logger LOGGER = LogManager.getLogger(DashboardTabRuntime.class);
	
	private Map session;
	protected List panels;
	private Integer tabID;
	private Integer projectID;
	private Integer releaseID;
	public void prepare() throws Exception {

	}
	@Override
	public String execute(){
		TPersonBean user = (TPersonBean) session.get(Constants.USER_KEY);
		//TDashboardScreenBean screen = DashboardScreenDesignBL.getInstance().loadByPerson(userID);
		panels=null;
		//ensure user
		DashboardPanelRuntimeBL dashboardPanelRuntimeBL=new DashboardPanelRuntimeBL();
		dashboardPanelRuntimeBL.setUser(user);
		dashboardPanelRuntimeBL.setSession(session);
		dashboardPanelRuntimeBL.setProjectID(projectID);
		dashboardPanelRuntimeBL.setReleaseID(releaseID);
		
		Locale locale=(Locale) session.get(Constants.LOCALE_KEY);
		TDashboardTabBean tab=(TDashboardTabBean) DashboardTabRuntimeBL.getInstance().loadFullTab(tabID);
		dashboardPanelRuntimeBL.calculateFieldWrappers(tab);
		session.put(DashboardAction.LAST_SELECTED_DASHBOARD_TAB, tabID);
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			StringBuilder sb=new StringBuilder();
			sb.append("{");
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.SUCCESS, true);
			if (tab!=null) {
				sb.append("\"data\":");
				sb.append(new DashboardScreenRuntimeJSON().encodeTab(tab));
			}
			sb.append("}");
			out.println(sb);
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
		//return SUCCESS;
	}
	
	public Map getSession() {
		return session;
	}

	public void setSession(Map session) {
		this.session = session;
	}

	public List getPanels() {
		return panels;
	}

	public void setPanels(List panels) {
		this.panels = panels;
	}

	public Integer getTabID() {
		return tabID;
	}

	public void setTabID(Integer tabID) {
		this.tabID = tabID;
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
