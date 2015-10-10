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


package com.aurel.track.admin.projectCopy;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.woden.wsdl20.Interface;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.validators.IntegerValidator;
import com.aurel.track.json.JSONUtility;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class ProjectCopyAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware {
	
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private Locale locale;
	private TPersonBean personBean;
	private Integer projectID;
	private String projectName;
	private Map<Integer, Boolean> associatedEntitiyMap;
	private Map<Integer, Boolean> customListsMap;
	private boolean copyAsSibling;
	private boolean copySubprojects;
	private boolean copyOpenItems;
	private boolean copyAttachments;
	private boolean copyReleases;
	
	private Integer actionTarget;
	
	public static interface WP_TPL_COPY_TARGET{
		public final static Integer COPY_ACTION_WP_FROM_TPL = Integer.valueOf(0);
		public final static Integer COPY_ACTION_TPL_FROM_WP = Integer.valueOf(1);
		public final static Integer COPY_ACTION_COPY_TPL = Integer.valueOf(2);
		public final static Integer COPY_ACTION_COPY_WP = Integer.valueOf(3);
	}

	public void prepare() throws Exception {
		//needed for rendering by execute and copy (in case of validation error)
		locale = (Locale)session.get(Constants.LOCALE_KEY);
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
	}
	
	/**
	 * Render the project copy page
	 */
	@Override
	public String execute() {
		JSONUtility.encodeJSON(servletResponse, ProjectCopyBL.loadCopy(projectID, personBean, locale, actionTarget));
		return null;
	}
	
	/**
	 * Executes the copy operation
	 * @return
	 */
	public String copy() {
		JSONUtility.encodeJSON(servletResponse, ProjectCopyBL.copyProject(projectID,
				associatedEntitiyMap, customListsMap, projectName, personBean, copyAsSibling, copySubprojects, null, copyOpenItems, copyAttachments, copyReleases, locale, false, actionTarget));
		//force reloading of all projects
		LookupContainer.resetProjectMap();
		return null;
	}
	
	public Map<Integer, Boolean> getAssociatedEntitiyMap() {
		return associatedEntitiyMap;
	}

	public void setAssociatedEntitiyMap(Map<Integer, Boolean> associatedEntitiyMap) {
		this.associatedEntitiyMap = associatedEntitiyMap;
	}

	public Map<Integer, Boolean> getCustomListsMap() {
		return customListsMap;
	}

	public void setCustomListsMap(Map<Integer, Boolean> customListsMap) {
		this.customListsMap = customListsMap;
	}

	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @param projectID the projectID to set
	 */
	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public void setCopyAsSibling(boolean copyAsSibling) {
		this.copyAsSibling = copyAsSibling;
	}

	public void setCopySubprojects(boolean copySubprojects) {
		this.copySubprojects = copySubprojects;
	}

	public void setCopyOpenItems(boolean copyOpenItems) {
		this.copyOpenItems = copyOpenItems;
	}

	public void setCopyAttachments(boolean copyAttachments) {
		this.copyAttachments = copyAttachments;
	}

	public void setCopyReleases(boolean copyReleases) {
		this.copyReleases = copyReleases;
	}
	
	public Integer getActionTarget() {
		return actionTarget;
	}

	public void setActionTarget(Integer actionTarget) {
		this.actionTarget = actionTarget;
	}
	
}
