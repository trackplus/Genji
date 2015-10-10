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


package com.aurel.track.util.emailHandling;

import java.io.Serializable;

/**
 *
 */
public class IncomingMailSettings implements Serializable {

	protected String protocol;
	protected String serverName;
	protected int securityConnection;
	protected int port;
	protected String user;
	protected String password;
	protected boolean keepMessagesOnServer;

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getSecurityConnection() {
		return securityConnection;
	}

	public void setSecurityConnection(int securityConnection) {
		this.securityConnection = securityConnection;
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

	public boolean isKeepMessagesOnServer() {
		return keepMessagesOnServer;
	}

	public void setKeepMessagesOnServer(boolean keepMessagesOnServer) {
		this.keepMessagesOnServer = keepMessagesOnServer;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String toString(){
		StringBuilder sb=new StringBuilder();
		sb.append("[").append(protocol).append("://").append(serverName).append(":").append(port);
		sb.append(" ").append(user).append("]");
		return sb.toString();
	}
	public String debug(){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		debugFields(sb);
		sb.append("]");
		return sb.toString();
	}
	protected void debugFields(StringBuilder sb){
		sb.append("protocol").append(protocol).append(",");
		sb.append("serverName").append(serverName).append(",");
		sb.append("securityConnection").append(securityConnection).append(",");
		sb.append("port").append(port).append(",");
		sb.append("user").append(user).append(",");
		sb.append("password").append(password==null?null:password.replaceAll(".","*")).append(",");
		sb.append("keepMessagesOnServer").append(keepMessagesOnServer);
	}
}
