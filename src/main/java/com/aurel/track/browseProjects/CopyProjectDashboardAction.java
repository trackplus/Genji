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

package com.aurel.track.browseProjects;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TDashboardScreenBean;
import com.aurel.track.beans.TPersonBean;

import com.aurel.track.screen.dashboard.bl.design.DashboardScreenDesignBL;
import com.aurel.track.util.IntegerStringBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class CopyProjectDashboardAction extends ActionSupport implements Preparable, SessionAware {
	
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	
	private Integer projectID;
	private Integer entityType;
	
	private Integer screenIDFrom;
	
	private List<IntegerStringBean> screens;
	
	private TPersonBean user;
	private Locale locale;
	
	@Override
	public void prepare() throws Exception {
		user = (TPersonBean) session.get(Constants.USER_KEY);
		locale = (Locale) session.get(Constants.LOCALE_KEY);
	}
	@Override
	public String execute(){
		screens=CopyProjectDashboardBL.getCopyFrom(user.getObjectID(), projectID, entityType);
		return SUCCESS;
	}
	public String copy(){
		TDashboardScreenBean screen=DashboardScreenDesignBL.getInstance().loadByProject(user.getObjectID(), projectID, entityType);
		if(screen!=null){
			DashboardScreenDesignBL.getInstance().deleteScreen(screen.getObjectID());
		}
		DashboardScreenDesignBL.getInstance().copyScreen(screenIDFrom, user.getObjectID(), projectID, entityType);
		return "ok";
	}
	@Override
	public void setSession(Map<String, Object> session) {
		this.session=session;
	}
	public Map<String, Object> getSession() {
		return session;
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
	public Integer getScreenIDFrom() {
		return screenIDFrom;
	}
	public void setScreenIDFrom(Integer screenIDFrom) {
		this.screenIDFrom = screenIDFrom;
	}
	public List<IntegerStringBean> getScreens() {
		return screens;
	}
	

}
