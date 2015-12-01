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

package com.aurel.track.util.emailHandling;

import com.aurel.track.beans.TSiteBean;

/**
 *
 */
public class SMTPMailSettings {
	public static int DEFAULT_PORT 	= 25;


	private boolean reqAuth = true;
	private int authMode= TSiteBean.SMTP_AUTHENTICATION_MODES.CONNECT_USING_SMTP_SETTINGS;

	protected String  host		= null;
	protected int security    = TSiteBean.SECURITY_CONNECTIONS_MODES.NEVER;
	protected int port		= DEFAULT_PORT;
	protected String user		= null;
	protected String password	= null;
	private IncomingMailSettings incomingMailSettings;
	private String mailEncoding;

	public boolean isReqAuth() {
		return reqAuth;
	}

	public void setReqAuth(boolean reqAuth) {
		this.reqAuth = reqAuth;
	}

	public int getAuthMode() {
		return authMode;
	}

	public void setAuthMode(int authMode) {
		this.authMode = authMode;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getSecurity() {
		return security;
	}

	public void setSecurity(int security) {
		this.security = security;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public IncomingMailSettings getIncomingMailSettings() {
		return incomingMailSettings;
	}

	public void setIncomingMailSettings(IncomingMailSettings incomingMailSettings) {
		this.incomingMailSettings = incomingMailSettings;
	}

	public String getMailEncoding() {
		return mailEncoding;
	}

	public void setMailEncoding(String mailEncoding) {
		this.mailEncoding = mailEncoding;
	}
}
