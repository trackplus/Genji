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

package com.aurel.track.admin.project.assignments;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.user.profile.ProfileJSON;
import com.aurel.track.beans.TAccountBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;

public class ProjectAssignmentJSON {

	static interface JSON_FIELDS {
		String ASSIGNED = "assigned";
		String UNASSIGNED = "unassigned";
		String ASSIGNMENT_INFO = "assignmentInfo";
		String NODE_TO_RELOAD = "nodeToReload";
		String NODE_TO_SELECT = "nodeToSelect";
		String GROUP = "group";
		String GROUP_LABEL = "groupLabel";
		String USER_NAME = "userName";
	}
	
	/**
	 * Gets the JSON string for tree folder children 
	 * @param children
	 * @return
	 */
	static String getChildrenJSON(List<RoleAssignmentTreeNodeTO> children){
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("[");
		for (Iterator<RoleAssignmentTreeNodeTO> iterator = children.iterator(); iterator.hasNext();) {
			RoleAssignmentTreeNodeTO roleAssignmentTreeNodeTO = iterator.next();
			stringBuilder.append("{");
			JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ID, roleAssignmentTreeNodeTO.getId());
			JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.TEXT, roleAssignmentTreeNodeTO.getText());
			JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ICONCLS, roleAssignmentTreeNodeTO.getIconCls());
			JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.GROUP, roleAssignmentTreeNodeTO.isGroup());
			JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.LEAF, roleAssignmentTreeNodeTO.isLeaf(), true);
			stringBuilder.append("}");
			if (iterator.hasNext()) {
				stringBuilder.append(",");
			}
		}
		stringBuilder.append("]");//end data
		return stringBuilder.toString();
	}
	
	/**
	 * Encode the assignments
	 * @param assigned
	 * @param unassigned
	 * @return
	 */
	static String encodePersonToRoleAssignment(List<TPersonBean> assigned, List<TPersonBean> unassigned,
			String assignmentInfo, Map<Integer, String> departmentMap, Locale locale,
			String reloadFromNode, String nodeToSelect) {
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("{");
		stringBuilder.append(addPersonToRoleAssignment(JSON_FIELDS.ASSIGNED, assigned, departmentMap, locale));
		stringBuilder.append(addPersonToRoleAssignment(JSON_FIELDS.UNASSIGNED, unassigned, departmentMap, locale));
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.ASSIGNMENT_INFO, assignmentInfo);
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.NODE_TO_RELOAD, reloadFromNode);
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.NODE_TO_SELECT, nodeToSelect);
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.SUCCESS, true, true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * Encode the assignments
	 * @param assigned
	 * @param unassigned
	 * @return
	 */
	public static String encodeNodes(String node) {
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("{");
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.NODE_TO_RELOAD, node);
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.NODE_TO_SELECT, node);
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.SUCCESS, true, true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * Person to role assignments
	 * @param fieldName
	 * @param assigned
	 * @param departmentMap
	 * @param locale
	 * @return
	 */
	private static String addPersonToRoleAssignment(String fieldName,
			List<TPersonBean> assigned, Map<Integer, String> departmentMap, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder();
		if (assigned!=null) {
			JSONUtility.appendFieldName(stringBuilder, fieldName);
			stringBuilder.append(":[");
			for (Iterator<TPersonBean> iterator = assigned.iterator(); iterator.hasNext();) {
				TPersonBean personBean = iterator.next();
				stringBuilder.append("{");
				JSONUtility.appendIntegerValue(stringBuilder, JSONUtility.JSON_FIELDS.ID, personBean.getObjectID());
				JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.NAME, personBean.getLabel());
				boolean active = !personBean.isDisabled();
				JSONUtility.appendBooleanValue(stringBuilder, ProfileJSON.JSON_FIELDS.active, active);
				JSONUtility.appendStringValue(stringBuilder, ProfileJSON.JSON_FIELDS.activeLabel, ProfileJSON.getBooleanLabel(active, locale));
				boolean isGroup = personBean.isGroup();
				JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.GROUP, active);
				JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.GROUP_LABEL, ProfileJSON.getBooleanLabel(isGroup, locale));
				if (!isGroup) {
					JSONUtility.appendStringValue(stringBuilder, ProfileJSON.JSON_FIELDS.department, departmentMap.get(personBean.getDepartmentID()));
					JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.USER_NAME, personBean.getLoginName());
				}
				JSONUtility.appendIntegerValue(stringBuilder, JSONUtility.JSON_FIELDS.ID, personBean.getObjectID(), true);
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
	static String encodeAccountAssignment(List<TAccountBean> assigned, List<TAccountBean> unassigned,
			String assignmentInfo, String iconCls) {
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("{");
		stringBuilder.append(addAccountAssignment(JSON_FIELDS.ASSIGNED, assigned, iconCls));
		stringBuilder.append(addAccountAssignment(JSON_FIELDS.UNASSIGNED, unassigned, iconCls));
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.ASSIGNMENT_INFO, assignmentInfo);
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.SUCCESS, true, true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * Add account assignment
	 * @param fieldName
	 * @param assigned
	 * @param iconCls
	 * @return
	 */
	private static String addAccountAssignment(String fieldName, List<TAccountBean> assigned, String iconCls) {
		StringBuilder stringBuilder = new StringBuilder();
		if (assigned!=null) {
			JSONUtility.appendFieldName(stringBuilder, fieldName).append(":[");
			for (Iterator<TAccountBean> iterator = assigned.iterator(); iterator.hasNext();) {
				TAccountBean labelBean = iterator.next();
				stringBuilder.append("{");
				JSONUtility.appendIntegerValue(stringBuilder, JSONUtility.JSON_FIELDS.ID, labelBean.getObjectID());
				JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ICONCLS, iconCls);
				JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.TEXT, labelBean.getLabel(), true);
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
