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


package com.aurel.track.admin.customize.role;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TRoleBean;
import com.aurel.track.json.JSONUtility;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public final class RoleAction extends ActionSupport 
		implements Preparable, SessionAware, ServletResponseAware {
	
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private Locale locale;
	//the ID of the selected role by edit or copy
	private Integer roleID;
	private boolean copy;
	private String extendedAccessKey;
	private String label;

	public void prepare() {
		locale = (Locale)session.get(Constants.LOCALE_KEY);
	}

	/**
	 * Loads the list of roles
	 * @return
	 */
	public String loadRoles(){
		List<TRoleBean> rolesList = RoleBL.loadVisible(locale);
		JSONUtility.encodeJSON(servletResponse, RoleJSON.encodeJSONRoles(rolesList,locale));
		return null;
	}

	/**
	 * Edits a new/existing/copied role
	 * @return
	 */
	public String edit() {
		JSONUtility.encodeJSON(servletResponse, RoleJSON.encodeRoleTO(RoleBL.loadRoleTO(roleID, copy, locale)));
		return null;
	}

	/**
	 * Saves a role
	 * @return
	 */
	public String save () {
		JSONUtility.encodeJSON(servletResponse, 
				RoleBL.saveRole(label, extendedAccessKey, roleID, copy, locale));
		//TODO copy also the issueType assignments or not?
		return null;
	}
	
	/**
	 * Deletes a role
	 * @return
	 */
	public String delete () {
		RoleBL.deleteRole(roleID);
		JSONUtility.encodeJSONSuccess(servletResponse);
		return null;
	}


	/**
	 * @param roleID the roleID to set
	 */
	public void setRoleID(Integer roleID) {
		this.roleID = roleID;
	}
	/**
	 * @param session the session to set
	 */
	public void setSession(Map<String,Object> session) {
		this.session = session;
	}

	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}

	
	public void setExtendedAccessKey(String extendedAccessKey) {
		this.extendedAccessKey = extendedAccessKey;
	}
	public void setCopy(boolean copy) {
		this.copy = copy;
	}

}
