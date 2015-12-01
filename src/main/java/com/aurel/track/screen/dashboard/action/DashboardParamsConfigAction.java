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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TDashboardFieldBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.plugin.DashboardDescriptor;
import com.aurel.track.plugin.PluginDescriptor;
import com.aurel.track.report.dashboardConfig.IPluginDashboard;
import com.aurel.track.screen.dashboard.assign.DashboardAssignBL;
import com.aurel.track.screen.dashboard.bl.DashboardUtil;
import com.aurel.track.screen.dashboard.bl.runtime.DashboardFieldRuntimeBL;
import com.aurel.track.util.LabelValueBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 *  The action for configuration the parameters for a dashboard item
 *
 */
public class DashboardParamsConfigAction extends ActionSupport implements SessionAware, Preparable {

	private static final Logger LOGGER = LogManager.getLogger(DashboardParamsConfigAction.class);

	private static final long serialVersionUID = 340L;
	private Map<String, Object> session;
	private Integer dashboardID;
	private Map<String,String> params;
	private PluginDescriptor descriptor;
	private String descriptorKey;
	private TPersonBean personBean;
	private String backAction;
	private Locale locale;
	private Integer projectID;
	private Integer releaseID;
	private Integer entityType;

	@Override
	public void prepare() {
		locale = (Locale) session.get(Constants.LOCALE_KEY);
		Boolean fromAdmin = (Boolean)session.get(DashboardScreenEditAction.DASHBOARD_EDIT_FROM);
		if (fromAdmin==null) {
			fromAdmin = new Boolean(false);
		}
		if (fromAdmin.booleanValue()) {
			//set in dashboardAssign
			personBean = DashboardAssignBL.loadPersonByKey(
					(Integer)session.get(DashboardScreenEditAction.DASHBOARD_EDIT_PERSON));
			backAction = "dashboardAssign.action";
		} else {
			personBean =(TPersonBean) session.get(Constants.USER_KEY);
			backAction = "cockpit.action";
		}
	}

	@Override
	public String execute(){
        TDashboardFieldBean dashboardItem=(TDashboardFieldBean)DashboardFieldRuntimeBL.getInstance().loadField(dashboardID);
		Map<String,String> itemParams=dashboardItem.getParametres();
		IPluginDashboard plugin= DashboardUtil.getPlugin(dashboardItem);
		DashboardDescriptor descriptor=DashboardUtil.getDescriptor(dashboardItem);
		TPersonBean user = (TPersonBean) session.get(Constants.USER_KEY);
		String jsonData=plugin.createJsonDataConfig(itemParams,user,projectID,entityType);
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			StringBuilder sb=new StringBuilder();
			sb.append("{");
			JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
			sb.append("data:{");
			JSONUtility.appendStringValue(sb,"cfgClass",descriptor.getJsConfigClass());
			JSONUtility.appendIntegerValue(sb,"projectID",projectID);
			JSONUtility.appendIntegerValue(sb,"entityType",entityType);
			JSONUtility.appendJSONValue(sb,"jsonData",jsonData,true);
			sb.append("}");
			sb.append("}");
			out.println(sb);
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	public String save(){
		TDashboardFieldBean dashboardItem=(TDashboardFieldBean)DashboardFieldRuntimeBL.getInstance().loadField(dashboardID);
		IPluginDashboard plugin= DashboardUtil.getPlugin(dashboardItem);
		List<LabelValueBean> errors=new ArrayList<LabelValueBean>();
		Map<String,String> itemParams=plugin.convertMapFromPageConfig(params,personBean,locale,projectID,entityType,errors);
		if(errors.isEmpty()){
			dashboardItem.setParametres(itemParams);
			DashboardFieldRuntimeBL.getInstance().saveDashBoardField(dashboardItem);
			JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		}else{
			JSONUtility.encodeJSONErrorsExtJS(ServletActionContext.getResponse(),errors);
		}
		return null;
	}

	public String cancel(){
		return "dashboardEdit";
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	public Map getParams() {
		return params;
	}

	public void setParams(Map params) {
		this.params = params;
	}

	public PluginDescriptor getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(PluginDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	public String getDescriptorKey() {
		return descriptorKey;
	}

	public void setDescriptorKey(String descriptorKey) {
		this.descriptorKey = descriptorKey;
	}

	/**
	 * @return the backAction
	 */
	public String getBackAction() {
		return backAction;
	}

	/**
	 * If date picker is needed on the configuration page
	 * @return
	 */

	/**
	 * If date picker is needed on the configuration page
	 * (importing the locale specific .js)
	 */
	@Override
	public Locale getLocale() {
		return locale;
	}

	public Integer getProjectID() {
		return projectID;
	}

	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	public Integer getEntityType() {
		return entityType;
	}

	public void setEntityType(Integer entityType) {
		this.entityType = entityType;
	}

	public Integer getReleaseID() {
		return releaseID;
	}

	public void setReleaseID(Integer releaseID) {
		this.releaseID = releaseID;
	}

	public Integer getDashboardID() {
		return dashboardID;
	}

	public void setDashboardID(Integer dashboardID) {
		this.dashboardID = dashboardID;
	}

}
