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


package com.cynertia.track.iCalendar;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.Support;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class ICalendarAction extends ActionSupport implements Preparable, SessionAware {

	private static final long serialVersionUID = 1L;

	private Map session;

	protected Locale locale = null;
	private TPersonBean personBean;

	private String selectedProjects;
	boolean allProjects=false;

	private static final Logger LOGGER = LogManager.getLogger(ICalendarAction.class);

	public void prepare() throws Exception {
		personBean=(TPersonBean) session.get(Constants.USER_KEY);
		locale=(Locale) session.get(Constants.LOCALE_KEY);
	}

	/*public void validate() {
			//no access to any project -> project dropdown is empty -> no possible calendar
			addFieldError("reportConfigBean.selectedProjects", getText("common.err.required",
					new String[]{FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_PROJECT, locale)}));
	}*/


	@Override
	public String execute(){
		return null;
	}
	/*public String projectPicker() {
		JSONUtility.encodeJSON(ServletActionContext.getResponse(),
				JSONUtility.getTreeHierarchyJSON(
						ProjectBL.getProjectNodesByRightEager(
						false, personBean, true,
						new int[]{AccessBeans.AccessFlagMigrationIndexes.PROJECTADMIN,
								AccessBeans.AccessFlagMigrationIndexes.READANYTASK}), true, false, null));
		return null;
	}*/
	public String generateURL(){
		String url=createURL();
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendStringValue(sb,"data",url,true);
		sb.append("}");
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.println(sb);
		} catch (IOException e) {
			LOGGER.debug(org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	private String createURL(){
		StringBuilder sb=new StringBuilder();
		sb.append(Constants.getBaseURL());
		sb.append("/iCalendarSynchronize.action?");
		sb.append("user=").append(personBean.getLoginName()).append("&pwd=").append(personBean.getPasswd());
		sb.append("&project=");
		if(allProjects){
			sb.append("ALL");
		}else{
			sb.append(selectedProjects);
		}

		return sb.toString();
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(Map session) {
		this.session = session;
	}

	/**
	 * @return the session
	 */
	public Map getSession(){
		return session;
	}

	public void setSelectedProjects(String selectedProjects) {
		this.selectedProjects = selectedProjects;
	}

	public void setAllProjects(boolean allProjects) {
		this.allProjects = allProjects;
	}
}
