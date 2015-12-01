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

package com.aurel.track.admin.user.userLevel;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.json.JSONUtility;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * Action for configuring the user levels
 * @author Tamas
 *
 */
public class UserLevelAction  extends ActionSupport implements Preparable, SessionAware, ServletResponseAware {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private Locale locale;
	private String node;
	//add or edit
	private boolean add;
	private String name;
	private String description;

	private Integer replacementID;
	
	//checking actions
	private Integer actionID;
	private boolean checked;
	
	@Override
	public void prepare() {
		locale=(Locale) session.get(Constants.LOCALE_KEY);
	}
	
	/**
	 * Expands the root or a group node
	 * @return
	 */
	public String expand() {		
		JSONUtility.encodeJSON(servletResponse,
			JSONUtility.getChildrenJSON(UserLevelConfigBL.getCustomUserLevels(locale)));
		return null;
	}

	/**
	 * Gets the grid data 
	 */
	public String loadList() {
		JSONUtility.encodeJSON(servletResponse,
				UserLevelConfigBL.getUserLevelSettingsJSON(node, locale));
		return null;
	}
	
	/**
	 * Loads the group detail for add or edit 
	 * @return
	 */
	public String edit() {
		JSONUtility.encodeJSON(servletResponse, 
				UserLevelConfigBL.getEditDetailJSON(node, add, locale));
		return null;
	}
	
	/**
	 * Saves a group (new or edited)
	 * @return
	 */
	public String save() {
		JSONUtility.encodeJSON(servletResponse, 
				UserLevelConfigBL.saveUserLevel(node, add, name, description, locale));
		return null;
	}
	
	/**
	 * Add/remove a licensed feature
	 * @return
	 */
	public String changeAction() {
		UserLevelConfigBL.changeUserLevelAction(node, actionID, checked);
		JSONUtility.encodeJSONSuccess(servletResponse);
		return null;
	}
	/**
	 * Deletes a group
	 * @return
	 */
	public String delete() {
		JSONUtility.encodeJSON(servletResponse,
				UserLevelConfigBL.delete(node));
		return null;
	}
	
	/**
	 * Renders the replacement groups
	 * @return
	 */
	public String renderReplace() {
		JSONUtility.encodeJSON(servletResponse,
				UserLevelConfigBL.prepareReplacement(node, null, locale));
		return null;
	}
	
	/**
	 * Replaces and deletes the group
	 * @return
	 */
	public String replaceAndDelete() {
		String jsonResponse = null;
		if (replacementID == null) {
			String errorMessage = getText("common.err.replacementRequired", 
					new String[] {getText("admin.customize.userLevel.lbl.userLevel")});
			jsonResponse = UserLevelConfigBL.prepareReplacement(node, errorMessage, locale);
		} else {
			UserLevelConfigBL.replaceAndDeleteUserLevel(node, replacementID);
			jsonResponse = JSONUtility.encodeJSONSuccess();
		}
		JSONUtility.encodeJSON(servletResponse, jsonResponse);
		return null;
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setReplacementID(Integer replacementID) {
		this.replacementID = replacementID;
	}

	public void setActionID(Integer actionID) {
		this.actionID = actionID;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	
	
	
	
}
