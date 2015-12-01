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
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TDashboardScreenBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.screen.IScreen;
import com.aurel.track.beans.screen.ITab;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.mobile.MobileBL;
import com.aurel.track.screen.dashboard.bl.design.DashboardScreenDesignBL;
import com.aurel.track.screen.dashboard.bl.runtime.DashboardPanelRuntimeBL;
import com.aurel.track.screen.dashboard.bl.runtime.DashboardScreenRuntimeJSON;
import com.aurel.track.screen.dashboard.bl.runtime.DashboardTabRuntimeBL;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 *
 */
public class DashboardAction extends ActionSupport implements Preparable, SessionAware {

	private static final long serialVersionUID = 1L;
	public static final String LAST_SELECTED_DASHBOARD_TAB="lastSelectedDashboardTab";

	private Map<String,Object> session;

	private Integer projectID;
	private Integer releaseID;
	private Integer entityType;
	private Integer tabID;

	private static final Logger LOGGER = LogManager.getLogger(DashboardAction.class);

	private boolean hasInitData=true;
	private String initData;
	private String layoutCls="com.trackplus.layout.HomeLayout";
	private String pageTitle="menu.cockpit";

	@Override
	public void prepare() throws Exception {
	}
	@Override
	public String execute(){
		initData=executeInternal();
		return SUCCESS;
	}

	public String executeJSON(){
		String jsonData=executeInternal();
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			StringBuilder sb=new StringBuilder();
			sb.append("{\"success\":true,\"data\":").append(jsonData).append("}");
			out.println(sb);
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	private String executeInternal(){
		TPersonBean user = (TPersonBean) session.get(Constants.USER_KEY);
		TDashboardScreenBean screen =null;
		Integer lastSelectedTab=null;
		if(projectID!=null){
			screen=DashboardScreenDesignBL.getInstance().loadByProject(user.getObjectID(), projectID, entityType);
		}else{
			if(user.getUserLevel().intValue() == TPersonBean.USERLEVEL.CLIENT.intValue()) {
				LOGGER.debug("Client user trying to access cockpits. We are returning the default template!");
				DashboardScreenDesignBL.getInstance().checkAndCreateClientDefaultCockpit();
				screen=(TDashboardScreenBean)DashboardScreenDesignBL.getInstance().loadScreen(DashboardScreenDesignBL.CLIENT_COCKPIT_TEMPLATE_ID);
				if(screen == null) {
					LOGGER.debug("Client user default template not exists! We must create it!");
					DashboardScreenDesignBL.getInstance().checkAndCreateClientDefaultCockpit();
					screen=(TDashboardScreenBean)DashboardScreenDesignBL.getInstance().loadScreen(DashboardScreenDesignBL.CLIENT_COCKPIT_TEMPLATE_ID);
				}
			}else {
				screen=DashboardScreenDesignBL.getInstance().loadByPerson(user);
			}
		}
		lastSelectedTab=(Integer) session.get(getKeyLastSelectedTab(projectID, entityType));
		List<ITab> tabs=screen.getTabs();
		if(tabs.size()==1){
			lastSelectedTab=null;
			Integer firstTabID=tabs.get(0).getObjectID();
			ITab firstTab= DashboardTabRuntimeBL.getInstance().loadFullTab(firstTabID);
			DashboardPanelRuntimeBL dashboardPanelRuntimeBL=new DashboardPanelRuntimeBL();
			dashboardPanelRuntimeBL.setUser(user);
			dashboardPanelRuntimeBL.setSession(session);
			dashboardPanelRuntimeBL.setProjectID(projectID);
			dashboardPanelRuntimeBL.setReleaseID(releaseID);
			dashboardPanelRuntimeBL.calculateFieldWrappers(firstTab);
			List<ITab> tabsFull=new ArrayList<ITab>();
			tabsFull.add(firstTab);
			screen.setTabs(tabsFull);
		}else{
			if(!verifyTab(lastSelectedTab,tabs) && !tabs.isEmpty()){
				lastSelectedTab=tabs.get(0).getObjectID();
			}

			//Iterate over tabs and getall fields and put on tab get all fields from tab
		}

		if(MobileBL.isMobileApp(session)) {
			MobileBL.setFieldTypes(tabs);
		}
		return prepareJSON(screen,lastSelectedTab,projectID,releaseID);
	}
	public String storeLastSelectedTab(){
		session.put(getKeyLastSelectedTab(projectID, entityType), tabID);
		JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		return null;
	}
	private String getKeyLastSelectedTab(Integer projectID,Integer entityType){
		String key=LAST_SELECTED_DASHBOARD_TAB;
		if(projectID!=null){
			key+="_"+projectID+"_"+entityType!=null?entityType:SystemFields.PROJECT;
		}
		return key;
	}
	private String prepareJSON(IScreen screen,Integer lastSelectedTab,Integer projectID,Integer releaseID){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendIntegerValue(sb, "lastSelectedTab", lastSelectedTab);
		JSONUtility.appendIntegerValue(sb, "projectID", projectID);
		JSONUtility.appendIntegerValue(sb, "releaseID", releaseID);
		sb.append("\"screen\":");
		sb.append(new DashboardScreenRuntimeJSON().encodeScreen(screen));
		sb.append("}");
		return sb.toString();

	}
	public String edit(){
		session.put(DashboardScreenEditAction.DASHBOARD_EDIT_FROM, new Boolean(false));
		return "edit";
	}

	//initialize data for layout
	public boolean isHasInitData() {
		return hasInitData;
	}
	public String getInitData() {
		return initData;
	}

	private boolean verifyTab(Integer tab,List tabs){
		if(tab==null){
			return false;
		}
		for (int i = 0; i < tabs.size(); i++) {
			if(((ITab)tabs.get(i)).getObjectID().equals(tab)){
				return true;
			}
		}
		return false;
	}

	public Map<String, Object> getSession() {
		return session;
	}
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public void setEntityType(Integer entityType) {
		this.entityType = entityType;
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

	public Integer getTabID() {
		return tabID;
	}

	public void setTabID(Integer tabID) {
		this.tabID = tabID;
	}

	public String getLayoutCls() {
		return layoutCls;
	}

	public String getPageTitle() {
		return pageTitle;
	}
}
