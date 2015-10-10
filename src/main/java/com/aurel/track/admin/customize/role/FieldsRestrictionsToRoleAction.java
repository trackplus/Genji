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

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.json.JSONUtility;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 *
 */
public class FieldsRestrictionsToRoleAction extends ActionSupport
		implements Preparable, SessionAware {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private Locale locale;
	private Integer roleID;
	private String records;
	

	public void prepare() {
		locale = (Locale)session.get(Constants.LOCALE_KEY);
	}

	/**
	 * Edit/add/copy of a trigger
	 * @return
	 */
	public String edit() {
		List<FieldForRoleBean> fieldGridRows = FieldsRestrictionsToRoleBL.getFieldsForRole(roleID, locale);
		JSONUtility.encodeJSON(ServletActionContext.getResponse(), 
				FieldRestrictionsToRoleJSON.createEditFieldRestrictionsJSON(fieldGridRows));
		return null;
	}
	
	/**
	 * Saves the edited/added/copied trigger
	 * @return
	 */
	public String save() {
		FieldsRestrictionsToRoleBL.saveFieldRestrictions(roleID, records);
		JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		return null;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(Map<String, Object> session) {
		this.session = session;
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

	public void setRecords(String records) {
		this.records = records;
	}
	
	
}

