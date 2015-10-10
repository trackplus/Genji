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

package com.aurel.track.admin.project;

import com.aurel.track.json.JSONUtility;

/**
 */
public class ProjectEmailJSON {
	private ProjectEmailJSON(){
	}
	static interface JSON_FIELDS {
		String PROJECT_EMAIL_BEAN_PREFIX = "projectEmailTO.";

		String PROTOCOL =PROJECT_EMAIL_BEAN_PREFIX+"protocol";
		String PROJECT_EMAIL_ENABLED = PROJECT_EMAIL_BEAN_PREFIX + "enabled";
		String SERVER_NAME = PROJECT_EMAIL_BEAN_PREFIX+"serverName";
		String SECURITY_CONNECTION = PROJECT_EMAIL_BEAN_PREFIX+"securityConnection";
		String PORT = PROJECT_EMAIL_BEAN_PREFIX+"port";
		String USER = PROJECT_EMAIL_BEAN_PREFIX+"user";
		String PASSWORD = PROJECT_EMAIL_BEAN_PREFIX+"password";
		String KEEP_MESSAGES_ON_SERVER = PROJECT_EMAIL_BEAN_PREFIX+"keepMessagesOnServer";
		String PROJECT_FROM_EMAIL = PROJECT_EMAIL_BEAN_PREFIX+"projectFromEmail";
		String PROJECT_FROM_EMAIL_NAME = PROJECT_EMAIL_BEAN_PREFIX+"projectFromEmailName";
		String SENT_FROM_PROJECT_EMAIL = PROJECT_EMAIL_BEAN_PREFIX+"sendFromProjectEmail";
		String SENT_FROM_PROJECT_AS_REPLAY_TO = PROJECT_EMAIL_BEAN_PREFIX+"sendFromProjectAsReplayTo";
	}
	public static void encodeProjectEmail(ProjectEmailTO projectEmailTO, StringBuilder stringBuilder) {
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.PROTOCOL, projectEmailTO.getProtocol());
		JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.PROJECT_EMAIL_ENABLED , projectEmailTO.isEnabled());
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.PROTOCOL, projectEmailTO.getProtocol());
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.SERVER_NAME, projectEmailTO.getServerName());
		JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.SECURITY_CONNECTION, projectEmailTO.getSecurityConnection());
		JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.PORT, projectEmailTO.getPort());
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.USER, projectEmailTO.getUser());
		JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.KEEP_MESSAGES_ON_SERVER, projectEmailTO.isKeepMessagesOnServer());
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.PROJECT_FROM_EMAIL, projectEmailTO.getProjectFromEmail());
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.PROJECT_FROM_EMAIL_NAME, projectEmailTO.getProjectFromEmailName());
		JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.SENT_FROM_PROJECT_EMAIL, projectEmailTO.isSendFromProjectEmail());
		JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.SENT_FROM_PROJECT_AS_REPLAY_TO, projectEmailTO.isSendFromProjectAsReplayTo(), true);
	}
}
