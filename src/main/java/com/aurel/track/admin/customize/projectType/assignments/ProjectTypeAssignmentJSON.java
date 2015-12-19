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

package com.aurel.track.admin.customize.projectType.assignments;

import java.util.Iterator;
import java.util.List;

import com.aurel.track.admin.customize.projectType.assignments.itemType.ProjectTypeItemTypeAssignmentBaseFacade;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.json.JSONUtility;

public class ProjectTypeAssignmentJSON {

	static interface JSON_FIELDS {
		String ASSIGNED = "assigned";
		String UNASSIGNED = "unassigned";
		String ASSIGNMENT_INFO = "assignmentInfo";
		String NODE_TO_RELOAD = "nodeToReload";
		String NODE_TO_SELECT = "nodeToSelect";
	}
	
	/**
	 * Encode the issue type assignments for a parent issueType
	 * @param parentIssueTypeNodeID
	 * @param locale
	 * @return
	 */
	public static String encodeSimpleAssignmentWithIconCls(List<ILabelBean> assigned,
			List<ILabelBean> unassigned, String assignmentInfo, String iconCls) {
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("{");
		stringBuilder.append(addAssignment(JSON_FIELDS.ASSIGNED, assigned, iconCls));
		stringBuilder.append(addAssignment(JSON_FIELDS.UNASSIGNED, unassigned, iconCls));
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.ASSIGNMENT_INFO, assignmentInfo);
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.SUCCESS, true, true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * Gets an assignment JSON array
	 * @param fieldName
	 * @param assigned
	 * @param projectTypeID
	 * @param assignmentType
	 * @param issueTypeID
	 * @return
	 */
	private static String addAssignment(String fieldName, List<ILabelBean> assigned, String iconCls) {
		StringBuilder stringBuilder = new StringBuilder();
		if (assigned!=null) {
			JSONUtility.appendFieldName(stringBuilder, fieldName).append(":[");
			for (Iterator<ILabelBean> iterator = assigned.iterator(); iterator.hasNext();) {
				ILabelBean labelBean = iterator.next();
				stringBuilder.append("{");
				JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.TEXT, labelBean.getLabel());
				JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ICONCLS, iconCls);
				JSONUtility.appendIntegerValue(stringBuilder, JSONUtility.JSON_FIELDS.ID, labelBean.getObjectID(), true);
				stringBuilder.append("}");
				if (iterator.hasNext()) {
					stringBuilder.append(",");
				}
			}
			stringBuilder.append("],");
		}
		return stringBuilder.toString();
	}
	
	/**
	 * Encode the assignments
	 * @param assigned
	 * @param unassigned
	 * @return
	 */
	public static String encodeSimpleAssignmentWithDynamicIcon(List<ILabelBean> assigned, List<ILabelBean> unassigned,
			String assignmentInfo, Integer projectTypeID, Integer assignmentType) {
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("{");
		stringBuilder.append(addAssignment(JSON_FIELDS.ASSIGNED, assigned, projectTypeID, assignmentType, null));
		stringBuilder.append(addAssignment(JSON_FIELDS.UNASSIGNED, unassigned, projectTypeID, assignmentType, null));
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.ASSIGNMENT_INFO, assignmentInfo);
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.SUCCESS, true, true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * Encode the assignment JSON
	 * @param assigned
	 * @param unassigned
	 * @return
	 */
	public static String encodeAssignment(List<ILabelBean> assigned, List<ILabelBean> unassigned,
			String assignmentInfo, String reloadFromNode, String nodeToSelect,
			Integer projectTypeID, Integer configType, Integer issueTypeID) {
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("{");
		stringBuilder.append(addAssignment(JSON_FIELDS.ASSIGNED, assigned, projectTypeID, configType, issueTypeID));
		stringBuilder.append(addAssignment(JSON_FIELDS.UNASSIGNED, unassigned, projectTypeID, configType, issueTypeID));
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.ASSIGNMENT_INFO, assignmentInfo);
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.NODE_TO_RELOAD, reloadFromNode);
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.NODE_TO_SELECT, nodeToSelect);		
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.SUCCESS, true, true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * Gets an assignment JSON array
	 * @param fieldName
	 * @param assigned
	 * @param projectTypeID
	 * @param assignmentType
	 * @param issueTypeID
	 * @return
	 */
	private static String addAssignment(String fieldName, List<ILabelBean> assigned,
			Integer projectTypeID, Integer assignmentType, Integer issueTypeID) {
		StringBuilder stringBuilder = new StringBuilder();
		if (assigned!=null) {
			JSONUtility.appendFieldName(stringBuilder, fieldName).append(":[");
			for (Iterator<ILabelBean> iterator = assigned.iterator(); iterator.hasNext();) {
				ILabelBean labelBean = iterator.next();
				stringBuilder.append("{");
				JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.TEXT, labelBean.getLabel());
				ProjectTypeAssignmentTokens projectTypeAssignmentTokens;
				if (issueTypeID==null) {
					projectTypeAssignmentTokens = new ProjectTypeAssignmentTokens(
							projectTypeID, assignmentType, labelBean.getObjectID());
				} else {
					projectTypeAssignmentTokens = new ProjectTypeAssignmentTokens(
							projectTypeID, assignmentType, issueTypeID, labelBean.getObjectID());
				}
				String node = ProjectTypeItemTypeAssignmentBaseFacade.encodeNode(projectTypeAssignmentTokens);
				JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ICON, 
						ProjectTypeItemTypeAssignmentBaseFacade.iconLoadURL+node);
				JSONUtility.appendIntegerValue(stringBuilder, JSONUtility.JSON_FIELDS.ID, labelBean.getObjectID(), true);
				stringBuilder.append("}");
				if (iterator.hasNext()) {
					stringBuilder.append(",");
				}
			}
			stringBuilder.append("],");
		}
		return stringBuilder.toString();
	}
	
}
