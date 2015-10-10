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


package com.aurel.track.admin.customize.projectType.assignments;


import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.projectType.assignments.itemType.ProjectTypeItemTypeAssignmentBaseFacade;
import com.aurel.track.admin.customize.projectType.assignments.itemType.ProjectTypeItemTypeAssignmentFacadeFactory;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.LabelValueBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;


/**
 * Assign and unassign issue type dependent system lists (status/priority/severity) to projectTypes
 * @author Tamas Ruff
 *
 */
public final class ProjectTypeAssignmentsAction extends ActionSupport 
	implements Preparable, SessionAware, ServletResponseAware, ServletRequestAware {
	
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private Locale locale;
	private String node;
	private String assign;
	private String unassign;
	
	private HttpServletResponse servletResponse;
	private HttpServletRequest servletRequest;
	ProjectTypeAssignmentTokens projectTypeAssignmentTokens;
	private ProjectTypeItemTypeAssignmentBaseFacade projectTypeItemTypeAssignmentFacade;
	
	public void prepare() {
		locale = (Locale)session.get(Constants.LOCALE_KEY);
		projectTypeAssignmentTokens = ProjectTypeItemTypeAssignmentBaseFacade.decodeNode(node);
		projectTypeItemTypeAssignmentFacade = ProjectTypeItemTypeAssignmentFacadeFactory.getInstance().getProjectTypeSimpleAssignmentFacade(projectTypeAssignmentTokens.getAssignmentType());
	}
	
	public String expand() {
		JSONUtility.encodeJSON(servletResponse, 
				JSONUtility.getChildrenJSON(projectTypeItemTypeAssignmentFacade.getAssignedNodes(projectTypeAssignmentTokens, locale)));
		return null;
	}
	
	/**
	 * Refresh the issueType label regarding counting the children
	 * @return
	 */
	public String refreshParent() {
		List<LabelValueBean> parentLabels = projectTypeItemTypeAssignmentFacade.getIssueType(node, projectTypeAssignmentTokens, locale);
		JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeJSONLabelValueBeanList(parentLabels));
		return null;
	}
	
	public String execute() {
		JSONUtility.encodeJSON(servletResponse, projectTypeItemTypeAssignmentFacade.encodeJSON(projectTypeAssignmentTokens.getProjectTypeID(),
				projectTypeAssignmentTokens.getAssignmentType(), projectTypeAssignmentTokens.getIssueTypeID() , locale));
		return null;
	}
	
	public String assign() {
		if (assign!=null && !"".equals(assign)) {
			projectTypeItemTypeAssignmentFacade.assign(projectTypeAssignmentTokens.getProjectTypeID(),
					projectTypeAssignmentTokens.getIssueTypeID(),
					GeneralUtils.createIntegerListFromStringArr(assign.split(",")));
		}
		return execute();
	}
	
	public String unassign() {
		if (unassign!=null && !"".equals(unassign)) {
			projectTypeItemTypeAssignmentFacade.unassign(projectTypeAssignmentTokens.getProjectTypeID(),
					projectTypeAssignmentTokens.getIssueTypeID(),
					GeneralUtils.createIntegerListFromStringArr(unassign.split(",")));
		}
		return execute();
	}

	/**
	 * Downloads the file
	 * @return
	 */
	public String downloadIcon() {
		ProjectTypeItemTypeAssignmentBaseFacade.downloadTreeIcon(node, servletRequest, servletResponse);
		return null;
	}
	
	/**
	 * @param session the session to set
	 */
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
	
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
	
	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
	}
}
