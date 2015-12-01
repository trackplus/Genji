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


package com.aurel.track.screen.dashboard.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TDashboardFieldBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.report.dashboard.BasePluginDashboardView;
import com.aurel.track.report.dashboardConfig.IPluginDashboard;
import com.aurel.track.screen.dashboard.bl.DashboardUtil;
import com.aurel.track.screen.dashboard.bl.runtime.DashboardFieldRuntimeBL;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class DashboardAJAXAction extends ActionSupport implements Preparable, SessionAware {

	private static final Logger LOGGER = LogManager.getLogger(DashboardAJAXAction.class);

	private static final long serialVersionUID = -397907234125032102L;
	private String templateAjax;
	private String templateAjaxError;
	private Integer dashboardID;
	private Map<String,String> params;
	private Map session;
	private Integer projectID;
	private Integer releaseID;
	@Override
	public void prepare() throws Exception {

	}

	@Override
	public String execute() throws Exception {
		try{
			Locale locale = (Locale) session.get(Constants.LOCALE_KEY);
			TDashboardFieldBean dashboardItem=(TDashboardFieldBean)DashboardFieldRuntimeBL.getInstance().loadField(dashboardID);
			IPluginDashboard plugin= DashboardUtil.getPlugin(dashboardItem);
			Map<String,String> configParamsMap=dashboardItem.getParametres();
			if (locale!=null) {
				configParamsMap.put(BasePluginDashboardView.CONFIGURATION_PARAMS.LOCALE_STRING, locale.toString());
			}
			String jsonData=plugin.createJsonData(dashboardID,session,configParamsMap,projectID,releaseID,params);
			try {
				JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
				PrintWriter out = ServletActionContext.getResponse().getWriter();
				StringBuilder sb=new StringBuilder();
				sb.append("{");
				JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
				JSONUtility.appendJSONValue(sb,"data",jsonData,true);
				sb.append("}");
				out.println(sb);
			} catch (IOException e) {
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}
		}catch (Exception e){
			LOGGER.error(ExceptionUtils.getStackTrace(e));
			JSONUtility.encodeJSONFailure(ServletActionContext.getResponse(),e.getMessage(),1);
		}

		return null;
	}
	public Map getSession() {
		return session;
	}
	@Override
	public void setSession(Map session) {
		this.session = session;
	}
	public String getTemplateAjax() {
		return templateAjax;
	}
	public void setTemplateAjax(String templateAjax) {
		this.templateAjax = templateAjax;
	}
	public String getTemplateAjaxError() {
		return templateAjaxError;
	}
	public void setTemplateAjaxError(String templateAjaxError) {
		this.templateAjaxError = templateAjaxError;
	}
	public Integer getDashboardID() {
		return dashboardID;
	}
	public void setDashboardID(Integer dashboardID) {
		this.dashboardID = dashboardID;
	}
	public Map getParams() {
		return params;
	}
	public void setParams(Map params) {
		this.params = params;
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
