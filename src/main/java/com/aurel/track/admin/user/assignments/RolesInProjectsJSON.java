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

package com.aurel.track.admin.user.assignments;

import java.util.Iterator;
import java.util.List;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TRoleBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.TreeNode;

public class RolesInProjectsJSON {
	
	interface JSON_FIELDS {		
		String PROJECTID = "projectID";
		String PROJECTLABEL = "projectLabel";
        String PROJECTS = "projects";
		String ROLEID = "roleID";
		String ROLELABEL = "roleLabel";
		String DIRECT = "direct";		
		String FIRST = "first";
		String ROLES = "roles"; 
	}
	
	
	
	public static String encodeRolesInProjectsJSON(List<ProjectRoleTO> assignments) {
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		for (Iterator<ProjectRoleTO> iterator = assignments.iterator(); iterator.hasNext();) {
			ProjectRoleTO projectRoleTO = iterator.next();
			sb.append("{");
			JSONUtility.appendIntegerValue(sb, JSON_FIELDS.PROJECTID , projectRoleTO.getProjectID());
			JSONUtility.appendStringValue(sb, JSON_FIELDS.PROJECTLABEL, projectRoleTO.getProjectLabel());
			JSONUtility.appendIntegerValue(sb, JSON_FIELDS.ROLEID, projectRoleTO.getRoleID());
			JSONUtility.appendStringValue(sb, JSON_FIELDS.ROLELABEL, projectRoleTO.getRoleLabel());
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.DIRECT, projectRoleTO.isDirect());
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.FIRST, projectRoleTO.isFirst(), true);
			sb.append("}");
			if(iterator.hasNext()){
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * JSON details for a group
	 * @param projectID
     * @param projectTree
     * @param roleID
     * @param roles
     * @return
	 */
	public static String encodeRoleProjectDetailJSON(Integer projectID, List<TreeNode> projectTree, Integer roleID, List<ILabelBean> roles) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		sb.append(JSONUtility.JSON_FIELDS.DATA).append(":{");
        if (projectID!=null) {
		    JSONUtility.appendStringValue(sb, JSON_FIELDS.PROJECTID, projectID.toString());
        }
        JSONUtility.appendJSONValue(sb, JSON_FIELDS.PROJECTS, JSONUtility.getTreeHierarchyJSON(projectTree, false, false));
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.ROLEID, roleID);
		JSONUtility.appendILabelBeanList(sb, JSON_FIELDS.ROLES, roles, true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Creates the JSON string for actualizing the role list after a project change
	 * @param roleBeans
	 * @param roleID
	 * @return
	 */
	static String encodeRoleListJSON(List<ILabelBean> roleBeans, Integer roleID) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
        JSONUtility.appendIntegerValue(sb, JSON_FIELDS.ROLEID, roleID);
        JSONUtility.appendILabelBeanList(sb, JSON_FIELDS.ROLES, roleBeans, true);
		sb.append("}");
		return sb.toString();		
	}
	
}
