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

package com.aurel.track.admin.project.assignments;


import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.LabelValueBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;


/**
 * Assign and unassign system lists to projectTypes
 * @author Tamas Ruff
 *
 */
public final class RoleAssignmentsAction extends ActionSupport 
	implements Preparable, SessionAware, ServletResponseAware {
	
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private Locale locale;
	private String node;
	private String assign;
	private String unassign;
	
	private HttpServletResponse servletResponse;
	
	@Override
	public void prepare() {
		locale = (Locale)session.get(Constants.LOCALE_KEY);	
	}
	
	public String expand() {
		JSONUtility.encodeJSON(servletResponse, 
				ProjectAssignmentJSON.getChildrenJSON(
						RoleAssignmentsBL.getAssignedNodes(node, locale)));
		return null;
	}
	
	/**
	 * Refresh the role label regarding counting the children
	 * @return
	 */
	public String refreshParent() {
		List<LabelValueBean> parentLabels = RoleAssignmentsBL.getRole(node, locale);
		JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeJSONLabelValueBeanList(parentLabels));
		return null;
	}
	
	@Override
	public String execute() {
		JSONUtility.encodeJSON(servletResponse,
				RoleAssignmentsBL.getAssignmentJSON(node, locale));
		return null;
	}
	
	public String assign() {
		RoleAssignmentsBL.assign(node, assign);
		JSONUtility.encodeJSON(servletResponse, RoleAssignmentsBL.getNodesToReload(node));
		return null;
	}
	
	public String unassign() {
		RoleAssignmentsBL.unassign(node, unassign);
		JSONUtility.encodeJSON(servletResponse, RoleAssignmentsBL.getNodesToReload(node));
		return null;
	}

	/**
	 * @param session the session to set
	 */
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}


	public void setAssign(String assign) {
		this.assign = assign;
	}

	public void setUnassign(String unassign) {
		this.unassign = unassign;
	}

	public void setNode(String node) {
		this.node = node;
	}
	
	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
}
