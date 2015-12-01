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

package com.aurel.track.browseProjects;


import com.aurel.track.json.JSONUtility;

/**
 * JSON utility for browse projects
 */
public class BrowseProjectJSON {
	public static String encodeJSONProjectDetailTO(BrowseProjectDetailTO project){
		StringBuilder sb=new StringBuilder();
		sb.append("{");

		JSONUtility.appendIntegerValue(sb, "projectID", project.getProjectID());
		JSONUtility.appendBooleanValue(sb, "canEdit", project.isCanEdit());
		JSONUtility.appendBooleanValue(sb, "projectLinking", project.isProjectLinking());
		JSONUtility.appendBooleanValue(sb, "projectWorkCost", project.isProjectWorkCost());

		JSONUtility.appendStringValue(sb, "projectLabel", project.getProjectLabel());
		JSONUtility.appendStringValue(sb, "projectDescription", project.getProjectDescription());
		JSONUtility.appendStringValue(sb, "projectType", project.getProjectType());
		JSONUtility.appendStringValue(sb, "projectState", project.getProjectState());
		JSONUtility.appendStringValue(sb, "defaultIssueType", project.getDefaultIssueType());
		JSONUtility.appendIntegerValue(sb, "defaultIssueTypeID", project.getDefaultIssueTypeID());
		JSONUtility.appendStringValue(sb, "defaultManager", project.getDefaultManager());
		JSONUtility.appendStringValue(sb, "defaultResponsible", project.getDefaultResponsible());
		JSONUtility.appendStringValue(sb, "defaultItemState", project.getDefaultItemState());
		JSONUtility.appendIntegerValue(sb, "defaultItemStateID", project.getDefaultItemStateID());
		JSONUtility.appendStringValue(sb, "defaultPriority", project.getDefaultPriority());
		JSONUtility.appendIntegerValue(sb, "defaultPriorityID", project.getDefaultPriorityID());
		JSONUtility.appendStringValue(sb, "defaultSeverity", project.getDefaultSeverity());
		JSONUtility.appendIntegerValue(sb, "defaultSeverityID", project.getDefaultSeverityID(),true);
		sb.append("}");
		return sb.toString();
	}

	public static String encodeJSONReleaseDetail(ReleaseDetailTO releaseDetailTO){
		StringBuilder sb=new StringBuilder();
		sb.append("{");

		JSONUtility.appendIntegerValue(sb, "objectID", releaseDetailTO.getObjectID());
		JSONUtility.appendBooleanValue(sb, "canEdit", releaseDetailTO.isCanEdit());
		JSONUtility.appendBooleanValue(sb, "noticedDefault", releaseDetailTO.isNoticedDefault());
		JSONUtility.appendBooleanValue(sb, "scheduledDefault", releaseDetailTO.isScheduledDefault());

		JSONUtility.appendStringValue(sb, "description", releaseDetailTO.getDescription());
		JSONUtility.appendStringValue(sb, "state", releaseDetailTO.getState());
		JSONUtility.appendStringValue(sb, "dueDate", releaseDetailTO.getDueDate());
		JSONUtility.appendStringValue(sb, "stateFlag", releaseDetailTO.getStateFlag());
		JSONUtility.appendStringValue(sb, "label", releaseDetailTO.getLabel(),true);

		sb.append("}");
		return sb.toString();
	}
}
