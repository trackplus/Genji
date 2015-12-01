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

package com.aurel.track.admin.server.status;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.prop.ApplicationBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class ServerStatusAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware{
	private static final Logger LOGGER = LogManager.getLogger(ServerStatusAction.class);
	private static final long serialVersionUID = 341L;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private Locale locale; 
	private ApplicationBean appBean = null;

	// -- opState --
	protected String opState = "";
	// -- hasUserMsg --
	protected boolean hasUserMsg = false;
	// -- userMsg --
	protected String userMsg = "";
	//  -- the line in the log from where on to fetch messages
	private int start = 0;


	@Override
	public void prepare() throws Exception {
		appBean = ApplicationBean.getInstance();
		locale = (Locale) session.get(Constants.LOCALE_KEY);
	}
	
	@Override
	public String execute() throws Exception {
		  return load();
	}
	
	public String load() throws Exception {
		// Get the currently logged on user (is he administrator?)
		TPersonBean user = (TPersonBean) session.get(Constants.USER_KEY);
		if(user == null || !user.getIsSysAdmin()) {
			// just throw her to the logon page
			return "logon";
		}
		ServerStatusTO serverStatusTO=ServerStatusBL.load(locale,appBean);
		JSONUtility.encodeJSON(servletResponse, ServerStatusJSON.encodeJSON(serverStatusTO));
		return null;
	}
	
	public String loadLines() throws Exception {
		// Get the currently logged on user (is he administrator?)
		TPersonBean user = (TPersonBean) session.get(Constants.USER_KEY);
		if(user == null || !user.getIsSysAdmin()) {
			// just throw her to the logon page
			return "logon";
		}
		JSONUtility.encodeJSON(servletResponse, ServerStatusJSON.getLogMessageLinesJSON(start));
		return null;		
	}
	
	public String save() throws Exception {
		// Get the currently logged on user (is he administrator?)
		TPersonBean user = (TPersonBean) session.get(Constants.USER_KEY);
		if(user == null || !user.getIsSysAdmin()) {
			// just throw her to the logon page
			return "logon";
		}		
		// update the opState
		ApplicationBean appBean = ApplicationBean.getInstance();
		if(appBean == null) {
			LOGGER.error("No applicationBean found");
		}
		else {
			ServerStatusBL.saveOpMode(opState, userMsg, hasUserMsg);
		}
		JSONUtility.encodeJSONSuccess(servletResponse);
		return null;
	}
	

	@Override
	public void setSession(Map<String, Object> _sessionMap) {
		session = _sessionMap;
	}
	
	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public String getOpState() {
		return this.opState;
	}
	public void setOpState(String opState) {
		this.opState = opState;
	}

	public boolean getHasUserMsg() {
		return this.hasUserMsg;
	}
	public void setHasUserMsg(boolean hasUserMsg) {
		this.hasUserMsg = hasUserMsg;
	}

	public String getUserMsg() {
		return this.userMsg;
	}
	public void setUserMsg(String userMsg) {
		this.userMsg = userMsg;
	}
	
	public void setStart(int _start) {
		this.start = _start;
	}
	
	public int getStart() {
		return this.start;
	}
}
