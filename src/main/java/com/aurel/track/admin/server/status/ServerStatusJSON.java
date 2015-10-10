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

package com.aurel.track.admin.server.status;

import java.util.Iterator;
import java.util.List;

import com.aurel.track.json.JSONUtility;
import com.aurel.track.json.JSONUtility.JSON_FIELDS;

/**
 * Encode the JSON for server status page
 * @author Tamas
 *
 */
public class ServerStatusJSON {
	/**
	 * Encode the JSON for server status
	 * @param serverStatusTO
	 * @return
	 */
	static String encodeJSON(ServerStatusTO serverStatusTO) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA).append(":");
		sb.append("{");
		JSONUtility.appendStringValue(sb,"version",serverStatusTO.getVersion());
		JSONUtility.appendStringValue(sb,"serverIPAddress",serverStatusTO.getServerIPAddress());
		JSONUtility.appendFieldName(sb, "licensedUsers").append(":");
		appendUserCountTO(sb, serverStatusTO.getLicensedUsers());
		sb.append(",");
		JSONUtility.appendStringValue(sb,"numberOfProjects",serverStatusTO.getNumberOfProjects());
		JSONUtility.appendIntegerValue(sb,"numberOfIssues",serverStatusTO.getNumberOfIssues());
		JSONUtility.appendFieldName(sb, "userCountList").append(":");
		sb.append("[");
		List<UserCountTO> userCountList = serverStatusTO.getUsersCountList();
		if (userCountList!=null) {
			for (Iterator<UserCountTO> iterator = userCountList.iterator(); iterator.hasNext();) {
				UserCountTO userCountTO = (UserCountTO) iterator.next();
				appendUserCountTO(sb, userCountTO);
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("],");
		JSONUtility.appendLabelValueBeanList(sb,"usersLoggedIn",serverStatusTO.getUsersLoggedIn());
		JSONUtility.appendLabelValueBeanList(sb,"clusterNodes",serverStatusTO.getClusterNodes());
		JSONUtility.appendStringValue(sb,"operationalStatus",serverStatusTO.getOperationalStatus());
		JSONUtility.appendBooleanValue(sb,"isUserInfo",serverStatusTO.getHasUserInfo());
		JSONUtility.appendStringValue(sb,"userInfo",serverStatusTO.getUserInfo());

		JSONUtility.appendLongValue(sb,"javaVMmaxMemory",serverStatusTO.getJavaVMmaxMemory());
		JSONUtility.appendLongValue(sb,"javaVMtotalMemory",serverStatusTO.getJavaVMtotalMemory());
		JSONUtility.appendLongValue(sb,"javaVMusedMemory",serverStatusTO.getJavaVMusedMemory());
		JSONUtility.appendLongValue(sb,"javaVMfreeMemory",serverStatusTO.getJavaVMfreeMemory());

		JSONUtility.appendStringValue(sb,"javaVersion",serverStatusTO.getJavaVersion());
		JSONUtility.appendStringValue(sb,"javaVendor",serverStatusTO.getJavaVendor());
		JSONUtility.appendStringValue(sb,"javaHome",serverStatusTO.getJavaHome());
		JSONUtility.appendStringValue(sb,"javaVMVersion",serverStatusTO.getJavaVMVersion());
		JSONUtility.appendStringValue(sb,"javaVMVendor",serverStatusTO.getJavaVMVendor());
		JSONUtility.appendStringValue(sb,"javaVMName",serverStatusTO.getJavaVMName());
		JSONUtility.appendStringValue(sb,"jasperVersion",serverStatusTO.getJasperVersion());

		JSONUtility.appendStringValue(sb,"operatingSystem",serverStatusTO.getOperatingSystem());
		JSONUtility.appendStringValue(sb,"userName",serverStatusTO.getUserName());
		JSONUtility.appendStringValue(sb,"currentUserDir",serverStatusTO.getCurrentUserDir());
		JSONUtility.appendStringValue(sb,"systemLocale",serverStatusTO.getSystemLocale());
		JSONUtility.appendStringValue(sb,"userTimezone",serverStatusTO.getUserTimezone());
		
		JSONUtility.appendStringList(sb,"logMessages",serverStatusTO.getLogMessages());

		JSONUtility.appendStringValue(sb,"database",serverStatusTO.getDatabase());
		JSONUtility.appendStringValue(sb,"pingTime",serverStatusTO.getPingTime());
		JSONUtility.appendStringValue(sb,"jdbcUrl",serverStatusTO.getJdbcUrl());
		JSONUtility.appendStringValue(sb,"jdbcDriver",serverStatusTO.getJdbcDriver(),true);

		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	
	static String getLogMessageLinesJSON(int start) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA).append(":");
		sb.append("{");
		
		List<String> msgs = ServerStatusTO.getLogMessageLines(start);
		
		JSONUtility.appendIntegerValue(sb,"endLineNo",start+msgs.size());
		
		JSONUtility.appendStringList(sb,"logMessageLines",msgs,true);

		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Append a UserCountTO to stringBuilder
	 * @param sb
	 * @param userCountTO
	 */
	private static void appendUserCountTO(StringBuilder sb, UserCountTO userCountTO) {
		sb.append("{");
		JSONUtility.appendStringValue(sb, JSON_FIELDS.LABEL, userCountTO.getLabel());
		int active = userCountTO.getNumberOfActiveUsers();
		int incative =  userCountTO.getNumberOfInactiveUsers();
		JSONUtility.appendIntegerValue(sb, "active", active);
		JSONUtility.appendIntegerValue(sb, "inactive", incative);
		JSONUtility.appendIntegerValue(sb, "total", active + incative, true);
		sb.append("}");
	}
}
