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

package com.aurel.track.mobile;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class MobileAction extends ActionSupport implements Preparable, SessionAware, ServletRequestAware, ServletResponseAware, ApplicationAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static interface NODE_TYPE_PREFIX {
		public static final int ITEM_OPERATION = 1;
		public static final int QUERY = 2;
	}

	public static final String NODE_TYPE_SEPARATOR = "_";

	private Map application;
	private HttpServletRequest servletRequest;
	private HttpServletResponse servletResponse;
	private Map<String, Object> session;
	private TPersonBean personBean;
	private Locale locale;

	public String getAllProjects() {

		return null;
	}

	@Override
	public void setApplication(Map<String, Object> application) {
		this.application = application;

	}

	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;

	}

	@Override
	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;

	}

	@Override
	public void prepare() throws Exception {
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		locale = (Locale) session.get(Constants.LOCALE_KEY);
	}

	public void getLastQueries() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"data\":");
		sb.append(session.get("lastExecutedFiltersJSON").toString());
		sb.append("}");
		JSONUtility.encodeJSON(ServletActionContext.getResponse(), sb.toString());
	}

	/**
	 * This method is called from Teamgeist to check if CBA is switched ON.
	 * 
	 * The value is on the response header
	 */
	public void testCba() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"success\":true}");
		JSONUtility.encodeJSON(ServletActionContext.getResponse(), sb.toString());
	}

}
