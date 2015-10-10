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

package com.aurel.track.admin.server.siteConfig;

import java.io.Serializable;
import java.util.List;

import com.aurel.track.util.IntegerStringBean;

/**
 * Transfer object bean used for outgoing email
 * @author Adrian Bojani
 */
public class OutgoingEmailTO implements Serializable{
	private static final long serialVersionUID = 400L;
	private String trackEmail;
	private String emailPersonalName;
	private String mailEncoding;
	private String serverName;
	private Integer securityConnection;
	private List<IntegerStringBean> securityConnectionsModes;
	private Integer port;
	private boolean reqAuth;
	private Integer authMode;
	private List<IntegerStringBean> authenticationModes;
	private String user;
	private String password;
	private Integer sendFromMode;
	private List<IntegerStringBean> sendFromModes;

	
	/** 
	 * The fields of this transfer object, that is their names in the JSON world.
	 * For example, in the user interface world a field would be named
	 * "outgoingEmail.trackEmail". The base name builds the first part, the second
	 * part is the specific field.
	 * 
	 */
	public interface JSONFIELDS {
		//for localizing the fields by errors
		String tabOutgoingEmail = "tab.outgoingEmail";		
		String fsTrackEmail = "fsTrackEmail";				
		String fsSmtpServer = "fsSmtpServer";
		String fsSmtpAuth = "fsSmtpAuth";
		String fsSmtpMore = "fsSmtpMore";
		
		// the base name
		String outgoingEmail = "outgoingEmail."; 
		
		//fields
	    String trackEmail = outgoingEmail + "trackEmail";  
		String emailPersonalName = outgoingEmail + "emailPersonalName";
		String sendFromMode = outgoingEmail + "sendFromMode";
		String mailEncoding = outgoingEmail + "mailEncoding";
		String serverName = outgoingEmail + "serverName";
		String securityConnection = outgoingEmail + "securityConnection";
		String port = outgoingEmail + "port";
		String reqAuth = outgoingEmail + "reqAuth";
		String authMode = outgoingEmail + "authMode";
		String user = outgoingEmail + "user";
		String securityConnectionsModes = outgoingEmail + "securityConnectionsModes";
		String authenticationModes = outgoingEmail + "authenticationModes";
		String sendFromModes = outgoingEmail + "sendFromModes";
	}

	public List<IntegerStringBean> getSendFromModes() {
		return sendFromModes;
	}

	public void setSendFromModes(List<IntegerStringBean> sendFromModes) {
		this.sendFromModes = sendFromModes;
	}

	public String getTrackEmail() {
		return trackEmail;
	}

	public void setTrackEmail(String trackEmail) {
		this.trackEmail = trackEmail;
	}

	public String getEmailPersonalName() {
		return emailPersonalName;
	}

	public void setEmailPersonalName(String emailPersonalName) {
		this.emailPersonalName = emailPersonalName;
	}

	public Integer getSendFromMode() {
		return sendFromMode;
	}

	public void setSendFromMode(Integer useTrackFromAddress) {
		this.sendFromMode = useTrackFromAddress;
	}

	public String getMailEncoding() {
		return mailEncoding;
	}

	public void setMailEncoding(String mailEncoding) {
		this.mailEncoding = mailEncoding;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public Integer getSecurityConnection() {
		return securityConnection;
	}

	public void setSecurityConnection(Integer securityConnection) {
		this.securityConnection = securityConnection;
	}

	public List<IntegerStringBean> getSecurityConnectionsModes() {
		return securityConnectionsModes;
	}

	public void setSecurityConnectionsModes(List<IntegerStringBean> securityConnectionsModes) {
		this.securityConnectionsModes = securityConnectionsModes;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public boolean isReqAuth() {
		return reqAuth;
	}

	public void setReqAuth(boolean reqAuth) {
		this.reqAuth = reqAuth;
	}

	public Integer getAuthMode() {
		return authMode;
	}

	public void setAuthMode(Integer authMode) {
		this.authMode = authMode;
	}

	public List<IntegerStringBean> getAuthenticationModes() {
		return authenticationModes;
	}

	public void setAuthenticationModes(List<IntegerStringBean> authenticationModes) {
		this.authenticationModes = authenticationModes;
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
}
