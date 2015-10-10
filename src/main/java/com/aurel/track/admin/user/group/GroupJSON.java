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

package com.aurel.track.admin.user.group;


import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.user.group.GroupConfigBL.GROUP_FLAGS;
import com.aurel.track.admin.user.profile.ProfileJSON;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;

public class GroupJSON {
	

	static interface JSON_FIELDS {
		String ASSIGNED = "assigned";
		String UNASSIGNED = "unassigned";
		String ASSIGNMENT_INFO = "assignmentInfo";
		String NODE_TO_RELOAD = "nodeToReload";
		String NODE_TO_SELECT = "nodeToSelect";
		String RELOAD_TREE = "reloadTree";
		String PARENTS_TO_RELOAD = "parentsToReload";
	}

	
	/**
	 * JSON details for a group
	 * @param name
	 * @return
	 */
	public static String getGroupEditDetailJSON(String name,
			boolean originator,boolean manager, boolean responsible, 
			boolean joinNewUserToThisGroup) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		sb.append(JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NAME, name);
		JSONUtility.appendBooleanValue(sb, GROUP_FLAGS.ORIGINATOR, originator);
		JSONUtility.appendBooleanValue(sb, GROUP_FLAGS.MANAGER, manager);
		JSONUtility.appendBooleanValue(sb, GROUP_FLAGS.JOIN_NEW_USER_TO_THIS_GROUP, joinNewUserToThisGroup);
		JSONUtility.appendBooleanValue(sb, GROUP_FLAGS.RESPONSIBLE, responsible,true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * JSON details for a group
	 * @param name
	 * @return
	 */
	public static String getDepartmentEditDetailJSON(String name) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		sb.append(JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NAME, name, true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Gets a JSON success with node
	 * @param node 
	 * @return
	 */
	public static String getGroupSaveDetailJSON(String node, boolean reloadTree){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.RELOAD_TREE, reloadTree);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NODE, node, true);
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Encode the assignments
	 * @param assigned
	 * @param unassigned
	 * @return
	 */
	public static String encodePersonInGroupDetail(List<TPersonBean> assigned, List<TPersonBean> unassigned,
			String assignmentInfo, List<String> parentsToReload,
			String reloadFromNode, String nodeToSelect,
			Map<Integer, String> departmentMap, Locale locale) {
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("{");
		stringBuilder.append(addPersonInGroupForGrid(JSON_FIELDS.ASSIGNED, assigned, departmentMap, locale));
		stringBuilder.append(addPersonInGroupForGrid(JSON_FIELDS.UNASSIGNED, unassigned, departmentMap, locale));
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.ASSIGNMENT_INFO, assignmentInfo);
		JSONUtility.appendStringList(stringBuilder, JSON_FIELDS.PARENTS_TO_RELOAD, parentsToReload);
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
	private static String addPersonInGroupForGrid(String fieldName, List<TPersonBean> assigned,
			Map<Integer, String> departmentMap, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder();
		if (assigned!=null) {
			JSONUtility.appendFieldName(stringBuilder, fieldName).append(":[");
			for (Iterator<TPersonBean> iterator = assigned.iterator(); iterator.hasNext();) {
				TPersonBean personBean = iterator.next();
				stringBuilder.append("{");
				JSONUtility.appendStringValue(stringBuilder, ProfileJSON.JSON_FIELDS.userNameGrid, personBean.getLoginName());
				JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.NAME, personBean.getFullName());
				boolean active = !personBean.isDisabled();
				JSONUtility.appendBooleanValue(stringBuilder, ProfileJSON.JSON_FIELDS.active, active);
				JSONUtility.appendStringValue(stringBuilder, ProfileJSON.JSON_FIELDS.activeLabel, ProfileJSON.getBooleanLabel(active, locale));
				JSONUtility.appendStringValue(stringBuilder, ProfileJSON.JSON_FIELDS.department, departmentMap.get(personBean.getDepartmentID()));			
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
}
