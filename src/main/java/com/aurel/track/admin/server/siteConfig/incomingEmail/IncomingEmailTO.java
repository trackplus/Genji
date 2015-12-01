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

package com.aurel.track.admin.server.siteConfig.incomingEmail;

import java.io.Serializable;
import java.util.List;

import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.emailHandling.IncomingMailSettings;

/**
 * Transfer object used for incoming email (POP3, IMAP) configuration.
 * @author Adrian Bojani
 */
public class IncomingEmailTO extends IncomingMailSettings implements Serializable{

	private static final long serialVersionUID = 400L;
	
	/**
	 * 
	 * The fields of this transfer object, that is their names in the JSON world.
	 * For example, in the user interface world a field would be named
	 * "incomingEmail.systemVersion". The base name builds the first part, the second
	 * part is the specific field.
	 *
	 */
	public interface JSONFIELDS {
		//for localizing the fields by errors
		String TAB_INCOMING_EMAIL = "tab.incomingEmail";
		String FS_INCOMING_SERVER = "fsIncomingServer";
		String FS_MAILBOX_AUTH = "fsMailboxAuth";
		String FS_MAILBOX_MORE = "fsMailboxMore";
		
		// base name
		String INCOMING_EMAIL = "incomingEmail.";
		
		//fields
		String PROTOCOL = INCOMING_EMAIL + "protocol";
		String SERVER_NAME = INCOMING_EMAIL + "serverName";
		String SECURITY_CONNECTION = INCOMING_EMAIL + "securityConnection";
		String PORT = INCOMING_EMAIL + "port";
		String user = INCOMING_EMAIL + "user";
		String PASSWORD = INCOMING_EMAIL + "password";
		String EMAIL_SUBMISSION_ENABLED = INCOMING_EMAIL + "emailSubmissionEnabled";
		String UNKNOWN_SENDER_ENABLED = INCOMING_EMAIL + "unknownSenderEnabled";
		String UNKNOWN_SENDER_REGISTRATION_ENABLED = INCOMING_EMAIL + "unknownSenderRegistrationEnabled";
		String DEFAULT_PROJECT = INCOMING_EMAIL + "defaultProject";
		String ALLOWED_EMAIL_PATTERN = INCOMING_EMAIL + "allowedEmailPattern";
		String KEEP_MESSAGES_ON_SERVER = INCOMING_EMAIL + "keepMessagesOnServer";

		String SECURITY_CONNECTIONS_MODES = INCOMING_EMAIL + "securityConnectionsModes";
		String PROJECT_TREE = "projectTree";
	}
	
	private transient List<IntegerStringBean> securityConnectionsModes;
	private boolean emailSubmissionEnabled;
	private boolean unknownSenderEnabled;
	private boolean unknownSenderRegistrationEnabled;
	
	private Integer defaultProject;
	private String allowedEmailPattern;

	public List<IntegerStringBean> getSecurityConnectionsModes() {
		return securityConnectionsModes;
	}

	public void setSecurityConnectionsModes(List<IntegerStringBean> securityConnectionsModes) {
		this.securityConnectionsModes = securityConnectionsModes;
	}

	public Integer getDefaultProject() {
		return defaultProject;
	}

	public void setDefaultProject(Integer defaultProject) {
		this.defaultProject = defaultProject;
	}

	public String getAllowedEmailPattern() {
		return allowedEmailPattern;
	}

	public void setAllowedEmailPattern(String allowedEmailPattern) {
		this.allowedEmailPattern = allowedEmailPattern;
	}

	public boolean isUnknownSenderEnabled() {
		return unknownSenderEnabled;
	}

	public void setUnknownSenderEnabled(boolean unknownSenderEnabled) {
		this.unknownSenderEnabled = unknownSenderEnabled;
	}
	
	public boolean isUnknownSenderRegistrationEnabled() {
		return unknownSenderRegistrationEnabled;
	}

	public void setUnknownSenderRegistrationEnabled(boolean unknownSenderRegistrationEnabled) {
		this.unknownSenderRegistrationEnabled = unknownSenderRegistrationEnabled;
	}
	

	public boolean isEmailSubmissionEnabled() {
		return emailSubmissionEnabled;
	}

	public void setEmailSubmissionEnabled(boolean emailSubmissionEnabled) {
		this.emailSubmissionEnabled = emailSubmissionEnabled;
    }

    @Override
    protected void debugFields(StringBuilder sb){
		super.debugFields(sb);
		sb.append(", emailSubmissionEnabled:").append(emailSubmissionEnabled).append(", ");
		sb.append("unknownSenderEnabled:").append(unknownSenderEnabled).append(", ");
		sb.append("unknownSenderRegistrationEnabled:").append(unknownSenderRegistrationEnabled).append(", ");
		sb.append("defaultProject:").append(defaultProject);
	}
}
