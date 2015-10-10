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

package com.aurel.track.user;

import com.aurel.track.Constants;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

public class AnonymousToLogonAction extends ActionSupport implements Preparable,SessionAware {

	private static final Logger LOGGER = LogManager.getLogger(AnonymousToLogonAction.class);
	private Map<String, Object> session;
	private String forwardURL;

	/**
	 * This method is automatically called by the framework before any other method
	 */
	public void prepare() throws Exception {
	}

	@Override
	public String execute() throws Exception {
		session.put(Constants.POSTLOGINFORWARD,forwardURL);
		return "logon";
	}
	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getForwardURL() {
		return forwardURL;
	}

	public void setForwardURL(String forwardURL) {
		this.forwardURL = forwardURL;
	}
}


