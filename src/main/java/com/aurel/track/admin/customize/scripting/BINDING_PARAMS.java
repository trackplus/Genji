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

package com.aurel.track.admin.customize.scripting;

public interface BINDING_PARAMS {
	public static final String USER = "user";
	public static final String USER_ID = "userID";
	public static final String LOCALE = "locale";
	//the issue edited
	public static final String ISSUE = "issue";
	//the original issue to compare with
	public static final String ISSUE_ORIGINAL  = "originalIssue";
	//the workItem context (contains also the previous two beans but they will be also directly available in the map)
	public static final String WORKITEM_CONTEXT = "workItemContext";
	//the submitted e-mail belongs to an issue from this project
	public static final String EMAIL_PROJECT = "project";
	//error list loaded during executing the script
	public static final String ERRORLIST = "errorList";
	
	public static final String EMAIL_ATTACHMENTS  = "emailAttachments";
	public static final String EMAIL_SUBJECT = "subject";
	public static final String EMAIL_BODY = "body";
	public static final String EMAIL_FROM_ADDRESS = "fromAddress";
	
	//ldap bindings
	public static final String SITEBEAN = "siteBean";
	public static final String LDAP_FILTER = "filter";
	public static final String LDAPMAP = "ldapmap";
	
	//whether the guard script passed
	public static final String GUARD_PASSED = "guardPassed";
	//analog to guard script passed but for other system releted scripts
	public static final String CONTINUE = "cont";
	
	//the notified event
	public static final String EVENT = "event";
	
	
}
