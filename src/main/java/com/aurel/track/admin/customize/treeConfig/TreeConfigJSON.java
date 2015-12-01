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

package com.aurel.track.admin.customize.treeConfig;

import java.util.Iterator;
import java.util.List;

import com.aurel.track.json.JSONUtility;

/**
 * JSON utility for field and screen configurations
 * @author Tamas Ruff
 *
 */
public class TreeConfigJSON {
	
	static interface JSON_FIELDS {
		//node specific fields
		String INHERITED_CONFIG = "inheritedConfig";
		String DEFAULT_CONFIG = "defaultConfig";
		String CONFIG_TYPE = "configType";		
		
		String CONFIG_TYPE_LABEL = "configTypeLabel";
		String DOMAIN_LABEL = "domainLabel";
		String INFO = "info";
		String REFRESH_TREE = "refreshTree";
		//helper fields to restrict the number of branches to reload
		String CHILDREN_ARE_LEAF = "childrenAreLeaf";
		String BRANCH_RESET = "branchReset";
		String ISSUE_TYPE = "issueType";
		String PROJECT_TYPE = "projectType";
		String PROJECT = "project";
		String ASSIGNED_ID = "assignedID";
	}
	
	static String getFolderJSON(/*String configTypeLabel, String domainLabel,*/ String info) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendStringValue(sb, JSON_FIELDS.INFO, info, true);
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Gets the JSON string for configuration children 
	 * @param children
	 * @return
	 */
	public static String getChildrenJSON(List<TreeConfigNodeTO> children){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		for (Iterator<TreeConfigNodeTO> iterator = children.iterator(); iterator.hasNext();) {
			TreeConfigNodeTO treeConfigNodeTO = iterator.next();
			sb.append("{");
			sb.append(getChildJSON(treeConfigNodeTO));
			sb.append("}");
			if (iterator.hasNext()) {
				sb.append(",");
			}
		}
		sb.append("]");//end data
		return sb.toString();
	}

	/**
	 * Gets the JSON string for configuration children 
	 * @param treeConfigNodeTO
	 * @return
	 */
	public static String getChildJSON(TreeConfigNodeTO treeConfigNodeTO){
		StringBuilder sb=new StringBuilder();
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.TEXT, treeConfigNodeTO.getText());
		JSONUtility.appendStringValue(sb, JSON_FIELDS.CONFIG_TYPE, treeConfigNodeTO.getConfigType());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.INHERITED_CONFIG, treeConfigNodeTO.isInherited());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.DEFAULT_CONFIG, treeConfigNodeTO.isDefaultConfig());
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.LEAF, treeConfigNodeTO.isLeaf());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.CHILDREN_ARE_LEAF, treeConfigNodeTO.isChildrenAreLeaf());
		if (treeConfigNodeTO.isChildrenAreLeaf()) {
			JSONUtility.appendIntegerValue(sb, JSON_FIELDS.ISSUE_TYPE, treeConfigNodeTO.getIssueType());
			JSONUtility.appendIntegerValue(sb, JSON_FIELDS.PROJECT_TYPE, treeConfigNodeTO.getProjectType());
			JSONUtility.appendIntegerValue(sb, JSON_FIELDS.PROJECT, treeConfigNodeTO.getProject());
		}
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.ASSIGNED_ID, treeConfigNodeTO.getAssignedID());
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ICON, treeConfigNodeTO.getIcon());
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ICONCLS, treeConfigNodeTO.getIconCls());
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ID, treeConfigNodeTO.getId(), true);
		return sb.toString();
	}
	
	/**
	 * Gets the JSON string after an overwrite: no tree refresh is needed
	 * @param node
	 * @param text
	 * @param inherited
	 * @return
	 */
	static String getOverwriteJSON(String node, String text, boolean inherited){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.TEXT, text);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.INHERITED_CONFIG, inherited);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NODE, node, true);
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Gets the JSON string after overwrite/reset of a configuration
	 * @param node 
	 * @return
	 */
	static String getResetJSON(String node, String text, boolean branchReset, boolean refreshTree,
			Integer issueType, Integer projectType, Integer project){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.TEXT, text);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.REFRESH_TREE, refreshTree);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.BRANCH_RESET, branchReset);
		if (branchReset || refreshTree) {
			JSONUtility.appendIntegerValue(sb, JSON_FIELDS.ISSUE_TYPE, issueType);
			JSONUtility.appendIntegerValue(sb, JSON_FIELDS.PROJECT_TYPE, projectType);
			JSONUtility.appendIntegerValue(sb, JSON_FIELDS.PROJECT, project);
		}
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NODE, node, true);
		sb.append("}");
		return sb.toString();
	}

}
