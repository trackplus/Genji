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


package com.aurel.track.admin.customize.role;


import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.json.JSONUtility;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;


/**
 * Assign and unassign issueTypes to roles
 * @author Tamas Ruff
 *
 */
public final class IssueTypeToRoleAction extends ActionSupport 
	implements Preparable, SessionAware {
	
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private Locale locale;
	private Integer roleID;
	private Integer[] assignedSelected;
	private Integer[] noAccessSelected;
	
	@Override
	public void prepare() {
		locale = (Locale)session.get(Constants.LOCALE_KEY);
	}
	

	@Override
	public String execute() {
		List<TListTypeBean> issueTypesList = (List)IssueTypeBL.loadAll(locale);
		List<TListTypeBean> assiged = RoleBL.getIssueTypesForRole(roleID, true, issueTypesList);
		List<TListTypeBean> noAccess = RoleBL.getIssueTypesForRole(roleID, false, issueTypesList);
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA);
		sb.append(":{");
		JSONUtility.appendIssueTypeBeanList(sb,"assiged",assiged);
		JSONUtility.appendIssueTypeBeanList(sb,"noAccess",noAccess,true);
		sb.append("}}");
		JSONUtility.encodeJSON(ServletActionContext.getResponse(), sb.toString());
		return null;
	}
	
	
	
	public String unassign() {
		RoleBL.unassignRoleListTypes(roleID, assignedSelected);
		return execute();
	}
	
	public String assign() {
		RoleBL.assignRoleListTypes(roleID, noAccessSelected);
		return execute();
	}

	/**
	 * @param session the session to set
	 */
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	/**
	 * @return the assignedSelected
	 */
	public Integer[] getAssignedSelected() {
		return assignedSelected;
	}

	/**
	 * @param assignedSelected the assignedSelected to set
	 */
	public void setAssignedSelected(Integer[] assignedSelected) {
		this.assignedSelected = assignedSelected;
	}

	/**
	 * @return the noAccessSelected
	 */
	public Integer[] getNoAccessSelected() {
		return noAccessSelected;
	}

	/**
	 * @param noAccessSelected the noAccessSelected to set
	 */
	public void setNoAccessSelected(Integer[] noAccessSelected) {
		this.noAccessSelected = noAccessSelected;
	}

	
	/**
	 * @return the roleID
	 */
	public Integer getRoleID() {
		return roleID;
	}

	/**
	 * @param roleID the roleID to set
	 */
	public void setRoleID(Integer roleID) {
		this.roleID = roleID;
	}
	
	
}
