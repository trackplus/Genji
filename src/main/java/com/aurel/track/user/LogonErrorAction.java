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

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.prop.BanProcessor;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Implementation of <strong>Action</strong> that processes errors before login.
 * $Id: LogonErrorAction.java 1229 2015-08-15 10:23:23Z friedric $
 */
public final class LogonErrorAction extends ActionSupport implements
		SessionAware {

	private static final long serialVersionUID = 400L;

	private static final Logger LOGGER = LogManager.getLogger(LogonErrorAction.class);

	private Map session;
	private String nonce;


	/**
	 * This method is automatically called if the associated action is triggered.
	 */
	@Override
	public String execute() throws Exception {

		HttpServletRequest request = org.apache.struts2.ServletActionContext
				.getRequest();
		HttpSession httpSession = request.getSession();
		
		Locale locale = request.getLocale();

		if (locale == null) {
			locale = Locale.getDefault();
		}
		
		BanProcessor bp = BanProcessor.getBanProcessor();
		if (!bp.isBanned(request.getRemoteAddr())) {
			clearFieldErrors();
			return "banRemoved";
		}

		return SUCCESS;
	}

	public Map getSession() {
		return session;
	}

	public void setSession(Map session) {
		this.session = session;
	}
	
	public String getNonce() {
		return nonce;
	}
}
