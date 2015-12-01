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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.project.release.ReleaseBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.item.ItemAction;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.json.JSONUtility.JSON_FIELDS;
import com.aurel.track.util.TreeNode;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class BrowseProjectsAction extends ActionSupport implements Preparable, SessionAware {

	private static final Logger LOGGER = LogManager.getLogger(BrowseProjectsAction.class);

	public static final String LAST_SELECTED_NODE="browseProjects.lastSelectedNode";
	public static final String PREFIX_LAST_SELCETED_TAB="browseProjects.lastSelectedTab.";
	public static final String SPLITER_COLLAPSED_KEY="browseProjects.spliterCollapsed.";
	
	private static final long serialVersionUID = 8112488072837466572L;
	private Map<String, Object> session;
	private TPersonBean user;
	private Locale locale;

	private Integer entityID;
	private Integer entityType;

	private boolean spliterCollapsed=false;
	private boolean editSpliterCollapses=false;
	
	private boolean hasInitData=true;
	private String initData;

	private String layoutCls="com.trackplus.layout.BrowseProjectsLayout";
	private String pageTitle="menu.browseProjects";
	
	@Override
	public void prepare() throws Exception {
		user = (TPersonBean) session.get(Constants.USER_KEY);
		locale = (Locale) session.get(Constants.LOCALE_KEY);
		Boolean b=(Boolean) session.get(SPLITER_COLLAPSED_KEY);
		if(b!=null&&b.booleanValue()){
			spliterCollapsed=true;
		}else{
			spliterCollapsed=false;
		}
	}
	@Override
	public String execute(){
		Integer selectedNode=(Integer) session.get(LAST_SELECTED_NODE);
		List<TreeNode> releaseNodes = ReleaseBL.getReleaseNodesEager(user,
				null, false, true, true, true, null, false, true, false, null, locale);
		String treeHierarchyJSON =null;
		if(releaseNodes!=null && !releaseNodes.isEmpty()){
			treeHierarchyJSON=JSONUtility.getTreeHierarchyJSON(releaseNodes, false, false,1);
		}

		if(selectedNode==null){
			//no previous selection; select first project
			if(!releaseNodes.isEmpty()){
				selectedNode=Integer.valueOf(releaseNodes.get(0).getId());
			}
		}
		LOGGER.debug("selectedNode="+selectedNode);
		prepareInitData(treeHierarchyJSON,selectedNode);
		return SUCCESS;
	}

	private void prepareInitData(String treeHierarchyJSON,Integer selectedNode){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendIntegerValue(sb, "selectedNode", selectedNode);
		JSONUtility.appendJSONValue(sb,"treeDataProjects",treeHierarchyJSON);
		JSONUtility.appendBooleanValue(sb, "spliterCollapsed", isSpliterCollapsed(),true);
		sb.append("}");
		initData=sb.toString();
	}
	
	public String storeSpliterCollapsed(){
		if(editSpliterCollapses){
			session.put(SPLITER_COLLAPSED_KEY,Boolean.TRUE);
		}else{
			session.put(SPLITER_COLLAPSED_KEY,Boolean.FALSE);
		}
		JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		return null;
	}
	public String entityDetail(){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		if(entityType==null||entityType.intValue()==SystemFields.PROJECT){//project
			if(entityID!=null){
				session.put(LAST_SELECTED_NODE,entityID);
				Integer projectID=Integer.valueOf(-1*entityID.intValue());
				BrowseProjectDetailTO projectDetail=BrowseProjectsBL.loadProjectDetail(user.getObjectID(), projectID,locale);
				JSONUtility.appendBooleanValue(sb,JSON_FIELDS.SUCCESS, true);
				sb.append("data:").append(BrowseProjectJSON.encodeJSONProjectDetailTO(projectDetail));
			}else{
				JSONUtility.appendBooleanValue(sb,JSON_FIELDS.SUCCESS, false);
			}
		}else{//release
			if(entityID!=null){
				ReleaseDetailTO release=BrowseProjectsBL.loadReleaseDetail(user.getObjectID(),entityID,locale);
				session.put(LAST_SELECTED_NODE,entityID);
				JSONUtility.appendBooleanValue(sb,JSON_FIELDS.SUCCESS, true);
				sb.append("data:").append(BrowseProjectJSON.encodeJSONReleaseDetail(release));
			}else{
				JSONUtility.appendBooleanValue(sb,JSON_FIELDS.SUCCESS, false);
			}
		}
		sb.append("}");
		JSONUtility.encodeJSON(ServletActionContext.getResponse(), sb.toString());
		return null;
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session=session;
	}

	public void setEntityType(Integer entityType) {
		this.entityType = entityType;
	}
	
	public boolean isSpliterCollapsed() {
		return spliterCollapsed;
	}
	public void setSpliterCollapsed(boolean spliterCollapsed) {
		this.spliterCollapsed = spliterCollapsed;
	}
	public void setEditSpliterCollapses(boolean editSpliterCollapses) {
		this.editSpliterCollapses = editSpliterCollapses;
	}
	public String getForwardAction(){
		return ItemAction.FORWARD_TO_BROWS_PROJECTS;
	}
	public boolean isHasInitData() {
		return hasInitData;
	}
	public String getInitData() {
		return initData;
	}
	public void setEntityID(Integer entityID) {
		this.entityID = entityID;
	}

	public String getLayoutCls() {
		return layoutCls;
	}

	public String getPageTitle() {
		return pageTitle;
	}
}
