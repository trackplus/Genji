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

package com.aurel.track.admin.customize.projectType.assignments;


import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.projectType.assignments.simple.ProjectTypeSimpleAssignmentBaseFacade;
import com.aurel.track.admin.customize.projectType.assignments.simple.ProjectTypeSimpleAssignmentFacadeFactory;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.GeneralUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;


/**
 * Assign or unassign simple objects to projectType
 * @author Tamas Ruff
 *
 */
public final class ProjectTypeSimpleAssignmentAction extends ActionSupport 
	implements Preparable, SessionAware, ServletResponseAware {
	
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private Locale locale;
	private Integer projectTypeID;
	private Integer configType;
	private String assign;
	private String unassign;
	private HttpServletResponse servletResponse;
	private ProjectTypeSimpleAssignmentBaseFacade projectTypeSimpleAssignmentFacade;
	
	@Override
	public void prepare() {
		locale = (Locale)session.get(Constants.LOCALE_KEY);
		projectTypeSimpleAssignmentFacade = ProjectTypeSimpleAssignmentFacadeFactory.getInstance().getProjectTypeSimpleAssignmentFacade(configType);
	}
	
	@Override
	public String execute() {
		JSONUtility.encodeJSON(servletResponse,
				projectTypeSimpleAssignmentFacade.encodeJSON(projectTypeID, configType, null, locale));
		return null;
	}
	
	public String assign() {
		if (assign!=null && !"".equals(assign)) {
			projectTypeSimpleAssignmentFacade.assign(projectTypeID, null, GeneralUtils.createIntegerListFromStringArr(assign.split(",")));
		}
		return execute();
	}
	
	public String unassign() {
		if (unassign!=null && !"".equals(unassign)) {
			projectTypeSimpleAssignmentFacade.unassign(projectTypeID, null, GeneralUtils.createIntegerListFromStringArr(unassign.split(",")));
		}
		return execute();
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

	public void setProjectTypeID(Integer projectTypeID) {
		this.projectTypeID = projectTypeID;
	}

	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public void setConfigType(Integer configType) {
		this.configType = configType;
	}
	
	
}
