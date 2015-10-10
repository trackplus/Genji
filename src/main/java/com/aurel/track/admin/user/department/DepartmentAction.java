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

package com.aurel.track.admin.user.department;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.LabelValueBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class DepartmentAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware {
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private Locale locale;
	private String node;
	//add or edit
	private boolean add;
	//when add, whether to add a new main department or a subdepartment to the selected department	
	private boolean addAsSubdepartment;
	private String name;
	private String assign;
	private String unassign;
	private Integer replacementID;
	//the old parent departments to refresh the label for: contain the comma separated value 
	//of the newly added person's (eventually more persons dropped at once)
	//old departments (a person can be in a single department at the same time)
	//the new department's label should be refreshed also but this is contained in node 
	private String oldParentIDs;
	//for copy/cut paste
	private String nodeFrom;
	private String nodeTo;
	
	/**
	 * The already selected departmentID(s): important only for trees (to set the checked field) not for pickers
	 */	
	private String departmentIDs;
	/**
	 * Whether or not the checked checkbox should appear near the node
	 * Should be used in project tree (when multiple select possible) not project picker
	 * if useChecked is true then useSelectable does not have any importance because then the selectable option
	 * is controlled by whether the checkbox is rendered or not i.e. checked flag is null or not null
	 */
	private boolean useChecked;
	
	public static final long serialVersionUID = 400L;
	
	/**
	 * the department to exclude by expanding (to avoid replacing a department with itself or any of its subdepartment)
	 */
	private Integer excludeID;
	/**
	 * Whether to include the persons by expanding parent department
	 * true for organizing the persons into departments but false for department picker
	 */
	private boolean includePersons;
	
	
	public void prepare() {
		locale=(Locale) session.get(Constants.LOCALE_KEY);
	}
	
	/**
	 * Expands the root or a department node lazily
	 * @return
	 */
	public String expand() {
		JSONUtility.encodeJSON(servletResponse, 
				JSONUtility.getChildrenJSON(DepartmentBL.getChildren(node, includePersons, locale)));
		return null;
	}

	/**
	 * Refresh the department labels regarding counting the children
	 * @return
	 */
	public String refreshParent() {
		List<LabelValueBean> parentLabels = DepartmentBL.getParents(node, oldParentIDs);
		JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeJSONLabelValueBeanList(parentLabels));
		return null;
	}
	
	/**
	 * Get all the departments eagerly
	 * @return
	 */
	public String picker() {
		Set<Integer> selectedDepartmentIDsSet = null;
		if (departmentIDs!=null) {
			selectedDepartmentIDsSet = GeneralUtils.createIntegerSetFromStringSplit(departmentIDs);
		}
		JSONUtility.encodeJSON(servletResponse,
				JSONUtility.getTreeHierarchyJSON(DepartmentBL.getDepartmentNodesEager(excludeID, selectedDepartmentIDsSet, useChecked),
				useChecked, false));
		return null;
	}
	
	@Override
	public String execute() {
		JSONUtility.encodeJSON(servletResponse,
				DepartmentBL.getAssignmentJSON(node, null, locale));
		return null;
	}
	
	/**
	 * Loads the group detail for add or edit 
	 * @return
	 */
	public String edit() {
		JSONUtility.encodeJSON(servletResponse, 
				DepartmentBL.getEditDetailJSON(node, add));
		return null;
	}
	
	/**
	 * Saves a group (new or edited)
	 * @return
	 */
	public String save() {
		JSONUtility.encodeJSON(servletResponse,
				DepartmentBL.getSaveDetailJSON(node, add, addAsSubdepartment, name, locale));
		return null;
	}
	
	/**
	 * Not a real copy but move after drag and drop
	 * @return
	 */
	public String copy() {
		JSONUtility.encodeJSON(servletResponse, DepartmentBL.copy(nodeFrom, nodeTo));
		return null;
	}
	
	/**
	 * Clears the parent department
	 * @return
	 */
	public String clearParent() {
		JSONUtility.encodeJSON(servletResponse, DepartmentBL.clearParent(node));
		return null;
	}
	
	/**
	 * Deletes a department
	 * @return
	 */
	public String delete() {
		JSONUtility.encodeJSON(servletResponse,
				DepartmentBL.delete(node));
		return null;
	}
	
	/**
	 * Renders the replacement department
	 * @return
	 */
	public String renderReplace() {
		JSONUtility.encodeJSON(servletResponse,
				DepartmentBL.prepareReplacement(node, null, locale));
		return null;
	}
	
	/**
	 * Replaces and deletes the department
	 * @return
	 */
	public String replaceAndDelete() {
		String jsonResponse = null;
		if (replacementID == null) {
			String errorMessage = getText("common.err.replacementRequired",
					new String[] {getText("admin.user.department.lbl.department")});
			jsonResponse = DepartmentBL.prepareReplacement(node, errorMessage, locale);
		} else {
			DepartmentBL.replaceAndDeleteDepartment(node, replacementID);
			jsonResponse = JSONUtility.encodeJSONSuccess();
		}
		JSONUtility.encodeJSON(servletResponse, jsonResponse);
		return null;
	}
	
	/**
	 * Assign person(s) to department
	 * @return
	 */
	public String assign() {
		List<String> parentsToReload = DepartmentBL.assign(node, assign);
		JSONUtility.encodeJSON(servletResponse,
				DepartmentBL.getAssignmentJSON(node, parentsToReload, locale));
		return null;
	}
	
	/**
	 * Remove person(s) from department
	 * @return
	 */
	public String unassign() {
		DepartmentBL.unassign(node, unassign);
		return execute();
	}
	
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
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

	public void setAssign(String assign) {
		this.assign = assign;
	}

	public void setUnassign(String unassign) {
		this.unassign = unassign;
	}

	public void setReplacementID(Integer replacementID) {
		this.replacementID = replacementID;
	}

	public void setAddAsSubdepartment(boolean addAsSubdepartment) {
		this.addAsSubdepartment = addAsSubdepartment;
	}

	public void setIncludePersons(boolean includePersons) {
		this.includePersons = includePersons;
	}

	public void setExcludeID(Integer excludeID) {
		this.excludeID = excludeID;
	}

	public void setDepartmentIDs(String departmentIDs) {
		this.departmentIDs = departmentIDs;
	}

	public void setUseChecked(boolean useChecked) {
		this.useChecked = useChecked;
	}

	public void setOldParentIDs(String oldParentIDs) {
		this.oldParentIDs = oldParentIDs;
	}

	public void setNodeFrom(String nodeFrom) {
		this.nodeFrom = nodeFrom;
	}

	public void setNodeTo(String nodeTo) {
		this.nodeTo = nodeTo;
	}
	
	
}
