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

package com.aurel.track.admin.user.group;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.user.person.PersonConfigBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.LabelValueBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * Action for configuring the groups and assigning users to groups
 * @author Tamas
 *
 */
public class GroupAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware {
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private Locale locale;
	private String node;
	//add or edit
	private boolean add;
	private String name;
	private boolean originator;
	private boolean manager;
	private boolean responsible;
	private boolean joinNewUserToThisGroup;
	
	private String assign;
	private String unassign;
	//the parent group to refresh the label for
	private Integer replacementID;
	private String selectedGroupIDs;
	private List<Integer> selectedGroupList;
	
	public static final long serialVersionUID = 400L;
	private TPersonBean personBean;
	
	@Override
	public void prepare() {
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		locale=(Locale) session.get(Constants.LOCALE_KEY);
		selectedGroupList = PersonConfigBL.getPersonIDList(selectedGroupIDs);
	}
	
	/**
	 * Expands the root or a group node
	 * @return
	 */
	public String expand() {		
		JSONUtility.encodeJSON(servletResponse,
			JSONUtility.getChildrenJSON(GroupConfigBL.getChildren(node)));
		return null;
	}

	/**
	 * Refresh the group label regarding counting the children
	 * @return
	 */
	public String refreshParent() {
		List<LabelValueBean> parentLabels = GroupConfigBL.getGroups(node);
		JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeJSONLabelValueBeanList(parentLabels));
		return null;
	}
	
	/**
	 * Gets the assignment data 
	 */
	@Override
	public String execute() {
		JSONUtility.encodeJSON(servletResponse,
				GroupConfigBL.getAssignmentJSON(node, locale));
		return null;
	}
	
	/**
	 * Loads the group detail for add or edit 
	 * @return
	 */
	public String edit() {
		JSONUtility.encodeJSON(servletResponse, 
				GroupConfigBL.getEditDetailJSON(node, add));
		return null;
	}
	
	/**
	 * Saves a group (new or edited)
	 * @return
	 */
	public String save() {
		JSONUtility.encodeJSON(servletResponse, 
				GroupConfigBL.getSaveDetailJSON(node, add, name, originator, manager, responsible, joinNewUserToThisGroup, locale));
		return null;
	}
	
	/**
	 * Deletes a group
	 * @return
	 */
	public String delete() {
		JSONUtility.encodeJSON(servletResponse,
				GroupConfigBL.delete(selectedGroupList));
		return null;
	}
	
	/**
	 * Renders the replacement groups
	 * @return
	 */
	public String renderReplace() {
		JSONUtility.encodeJSON(servletResponse,
			GroupConfigBL.prepareReplacement(selectedGroupList, null, locale));
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
					new String[] {getText("admin.user.group.lbl.group")});
			jsonResponse = GroupConfigBL.prepareReplacement(selectedGroupList, errorMessage, locale);
		} else {
			GroupConfigBL.replaceAndDeleteGroup(selectedGroupList, replacementID);
			jsonResponse = JSONUtility.encodeJSONSuccess();
		}
		JSONUtility.encodeJSON(servletResponse, jsonResponse);
		return null;
	}
	
	/**
	 * Assign person(s) to group
	 * @return
	 */
	public String assign() {
		GroupConfigBL.assign(node, assign);
		JSONUtility.encodeJSON(servletResponse, GroupConfigBL.getNodesToReload(node));
		return null;
	}
	
	/**
	 * Remove person(s) from group
	 * @return
	 */
	public String unassign() {
		GroupConfigBL.unassign(node, unassign);
		JSONUtility.encodeJSON(servletResponse, GroupConfigBL.getNodesToReload(node));
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

	public void setOriginator(boolean originator) {
		this.originator = originator;
	}

	public void setManager(boolean manager) {
		this.manager = manager;
	}

	public void setResponsible(boolean responsible) {
		this.responsible = responsible;
	}

	public void setAssign(String assign) {
		this.assign = assign;
	}

	public void setUnassign(String unassign) {
		this.unassign = unassign;
	}

	public void setReplacementID(Integer replacementID) {
		this.replacementID = replacementID;
	}

	public void setSelectedGroupIDs(String selectedGroupIDs) {
		this.selectedGroupIDs = selectedGroupIDs;
	}
	
	public boolean isJoinNewUserToThisGroup() {
		return joinNewUserToThisGroup;
	}

	public void setJoinNewUserToThisGroup(boolean joinNewUserToThisGroup) {
		this.joinNewUserToThisGroup = joinNewUserToThisGroup;
	}
	
	
}
