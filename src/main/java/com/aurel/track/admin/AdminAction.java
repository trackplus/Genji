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

package com.aurel.track.admin;

import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.json.JSONUtility;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class AdminAction extends ActionSupport implements Preparable, SessionAware{
	private static final long serialVersionUID = 1543744965855309661L;
	public static final String LAST_SELECTED_SECTION="admin.lastSelectedSection";
	public static final String LAST_SELECTED_NODE="admin.lastSelectedNode";

	private Map<String,Object> session;
	private String selectedNodeID;
	private String sectionSelected;

	private boolean hasInitData=true;
	private String initData;
	private String layoutCls="com.trackplus.layout.AdminLayout";
	private String pageTitle="menu.admin";

	public boolean isHasInitData() {
		return hasInitData;
	}

	public void setHasInitData(boolean hasInitData) {
		this.hasInitData = hasInitData;
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session=session;
	}

	@Override
	public void prepare() throws Exception {
	}
	
	@Override
	public String execute(){
		StringBuilder sb=new StringBuilder();
		if(sectionSelected==null){
			sectionSelected=(String)session.get(LAST_SELECTED_SECTION);
		}else{
			storeLastSelectedNode();
		}
		if(selectedNodeID==null){
			selectedNodeID=(String)session.get(LAST_SELECTED_NODE);
		}
		sb.append("{");
		JSONUtility.appendStringValue(sb, "selectedNodeID",selectedNodeID);
		JSONUtility.appendStringValue(sb,"sectionSelected",sectionSelected,true);
		sb.append("}");
		initData=sb.toString();
		return SUCCESS;
	}
	public String storeLastSelectedNode(){
		session.put(LAST_SELECTED_SECTION,sectionSelected);
		session.put(LAST_SELECTED_NODE,selectedNodeID);
		JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		return null;
	}


	public String getSelectedNodeID() {
		return selectedNodeID;
	}

	public void setSelectedNodeID(String selectedNodeID) {
		this.selectedNodeID = selectedNodeID;
	}

	public String getSectionSelected() {
		return sectionSelected;
	}

	public void setSectionSelected(String sectionSelected) {
		this.sectionSelected = sectionSelected;
	}

	public String getInitData() {
		return initData;
	}

	public void setInitData(String initData) {
		this.initData = initData;
	}

	public String getLayoutCls() {
		return layoutCls;
	}

	public String getPageTitle() {
		return pageTitle;
	}
}
