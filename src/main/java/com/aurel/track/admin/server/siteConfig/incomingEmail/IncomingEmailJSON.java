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

import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.TreeNode;

import java.util.List;

public class IncomingEmailJSON {

	private IncomingEmailJSON(){
	}

	public static String encodeIncomingEmailTO(IncomingEmailTO incomingEmailTO, List<TreeNode> projectTree) {
		StringBuilder sb=new StringBuilder();
		JSONUtility.appendStringValue(sb, IncomingEmailTO.JSONFIELDS.PROTOCOL, incomingEmailTO.getProtocol());
		JSONUtility.appendStringValue(sb, IncomingEmailTO.JSONFIELDS.SERVER_NAME, incomingEmailTO.getServerName());
		JSONUtility.appendIntegerValue(sb, IncomingEmailTO.JSONFIELDS.SECURITY_CONNECTION, incomingEmailTO.getSecurityConnection());
		JSONUtility.appendIntegerValue(sb, IncomingEmailTO.JSONFIELDS.PORT, incomingEmailTO.getPort());
		JSONUtility.appendStringValue(sb, IncomingEmailTO.JSONFIELDS.user, incomingEmailTO.getUser());
		JSONUtility.appendBooleanValue(sb, IncomingEmailTO.JSONFIELDS.EMAIL_SUBMISSION_ENABLED, incomingEmailTO.isEmailSubmissionEnabled());
		JSONUtility.appendBooleanValue(sb, IncomingEmailTO.JSONFIELDS.UNKNOWN_SENDER_ENABLED, incomingEmailTO.isUnknownSenderEnabled());
		JSONUtility.appendBooleanValue(sb, IncomingEmailTO.JSONFIELDS.UNKNOWN_SENDER_REGISTRATION_ENABLED, incomingEmailTO.isUnknownSenderRegistrationEnabled());
		JSONUtility.appendStringValue(sb, IncomingEmailTO.JSONFIELDS.ALLOWED_EMAIL_PATTERN, incomingEmailTO.getAllowedEmailPattern());
		JSONUtility.appendBooleanValue(sb, IncomingEmailTO.JSONFIELDS.KEEP_MESSAGES_ON_SERVER, incomingEmailTO.isKeepMessagesOnServer());
		JSONUtility.appendIntegerStringBeanList(sb, IncomingEmailTO.JSONFIELDS.SECURITY_CONNECTIONS_MODES, incomingEmailTO.getSecurityConnectionsModes());
		Integer defaultProject = incomingEmailTO.getDefaultProject();
		JSONUtility.appendJSONValue(sb, IncomingEmailTO.JSONFIELDS.PROJECT_TREE, JSONUtility.getTreeHierarchyJSON(projectTree, false, false));
		if (defaultProject==null && projectTree!=null && !projectTree.isEmpty()) {
			defaultProject = Integer.valueOf(projectTree.get(0).getId());
		}
		if (defaultProject!=null) {
			JSONUtility.appendStringValue(sb, IncomingEmailTO.JSONFIELDS.DEFAULT_PROJECT, defaultProject.toString());
		}
		return sb.toString();
	}
}
