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


package com.aurel.track.admin.project;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * This class is used for both project picker (single select) and project tree (multiple select)
 * @author Tamas Ruff
 *
 */
public class ProjectPickerAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware {

	private static final long serialVersionUID = 1L;
	private transient Map<String, Object> session;
	private transient HttpServletResponse servletResponse;
	private TPersonBean personBean;
	private Locale locale;
		
	/**
	 * Whether or not the checked checkbox should appear near the node
	 * Should be used in project tree (when multiple select possible) not project picker
	 * if useChecked is true then useSelectable does not have any importance because then the selectable option
	 * is controlled by whether the checkbox is rendered or not i.e. checked flag is null or not null
	 */
	private boolean useChecked;
	
	/**
	 * Should be used only if use checked is false (by project picker not project tree) when only a single
	 * selection is possible but still might exist some nodes which are not selectable only part of a hierarchy
	 */
	
	/**
	 * Whether to include only the active projects or also the inactive ones
	 */
	
	/**
	 * The already selected projectID(s): important only for trees (to set the checked field) not for pickers (they are selected by form.load())
	 */
	
	//projects for notify settings 
	//private Integer exclusiveProjectID;
	
	/**
	 * Projects with this right
	 */
	//private boolean createTask = false;	
	
	@Override
	public void prepare() throws Exception {
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		locale = (Locale)session.get(Constants.LOCALE_KEY);
	}

	/**
	 * Gets the projects with read/modify/create right for a project picker and ICalendar
	 * @return
	 */
	@Override
	public String execute() {
		JSONUtility.encodeJSON(servletResponse, JSONUtility.getTreeHierarchyJSON(
				ProjectPickerBL.getTreeNodesForRead(null, useChecked, false,
				personBean, locale), useChecked, false));
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

	public void setUseChecked(boolean useChecked) {
		this.useChecked = useChecked;
	}
	
}
