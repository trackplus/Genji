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

package com.aurel.track.screen.dashboard.template;

import com.aurel.track.Constants;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TDashboardScreenBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.screen.dashboard.assign.DashboardAssignBL;
import com.aurel.track.screen.dashboard.bl.design.DashboardScreenDesignBL;
import com.aurel.track.screen.item.bl.design.ScreenDesignBL;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBooleanBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DashboardTemplateAction extends ActionSupport implements Preparable, SessionAware {
	private static final long serialVersionUID = 1L;

	private Map<String,Object> session;

	private Integer projectID;
	private Integer entityType;

	private Integer resetID;
	private Integer dashboardID;
	private String name,description;

	private static final Logger LOGGER = LogManager.getLogger(DashboardTemplateAction.class);

	public void prepare() throws Exception {
	}

	public String resetDashboard(){
		TPersonBean user = (TPersonBean) session.get(Constants.USER_KEY);
		DashboardAssignBL.resetDashboard(user, resetID);
		JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		return null;
	}
	public String copyAsTemplateDashboard(){
		TDashboardScreenBean dashboardScreenBean=null;
		TPersonBean user = (TPersonBean) session.get(Constants.USER_KEY);
		if(projectID!=null){
			dashboardScreenBean= DashboardScreenDesignBL.getInstance().loadByProject(user.getObjectID(), projectID, entityType);
		}else{
			dashboardScreenBean=DashboardScreenDesignBL.getInstance().loadByPerson(user);
		}

		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendJSONValue(sb,JSONUtility.JSON_FIELDS.DATA,
				ScreenDesignBL.getInstance().encodeJSONScreenTO(dashboardScreenBean),true);
		sb.append("}");
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.println(sb);
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	public String saveAsTemplateDashboard(){
		TPersonBean personBean = (TPersonBean) session.get(Constants.USER_KEY);
		DashboardScreenDesignBL.getInstance().saveAsTemplateDashboard(dashboardID,name,description,null,personBean.getObjectID());
		JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		return null;
	}
	public String loadNotAssigned(){
		TPersonBean personBean = (TPersonBean) session.get(Constants.USER_KEY);
		List<TPersonBean> systemAdmins = PersonBL.loadSystemAdmins();
		List<Integer> personIDs = GeneralUtils.createIntegerListFromBeanList(systemAdmins);
		if (personBean!=null) {
			Integer personID = personBean.getObjectID();
			if (!personIDs.contains(personID)) {
				personIDs.add(personID);
			}
		}
		List<TDashboardScreenBean> screens= DashboardAssignBL.loadMyAndPublicTemplates(personIDs);
		if(screens==null||screens.isEmpty()){
			screens=new ArrayList<TDashboardScreenBean>();
			TDashboardScreenBean screenBean=DashboardScreenDesignBL.getInstance().createNewDashboardScreen("default","default",null,null,null);
			screens.add(screenBean);
		}
		List<IntegerStringBooleanBean> list=new ArrayList<IntegerStringBooleanBean>();
		IntegerStringBooleanBean isbb;
		for(int i=0;i<screens.size();i++){
			TDashboardScreenBean screenBean=screens.get(i);
			isbb=new IntegerStringBooleanBean();
			isbb.setValue(screenBean.getObjectID());
			isbb.setLabel(screenBean.getName());
			isbb.setSelected(personBean.getObjectID().equals(screenBean.getOwner()));
			list.add(isbb);
		}
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendIntegerStringBooleanBeanList(sb,"data",list,true);
		sb.append("}");
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.println(sb);
		} catch (IOException e) {
			// nothing much can be done here
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	public Map<String, Object> getSession() {
		return session;
	}
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public void setEntityType(Integer entityType) {
		this.entityType = entityType;
	}
	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	public void setResetID(Integer resetID) {
		this.resetID = resetID;
	}

	public void setDashboardID(Integer dashboardID) {
		this.dashboardID = dashboardID;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
