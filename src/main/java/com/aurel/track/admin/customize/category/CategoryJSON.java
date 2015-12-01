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

package com.aurel.track.admin.customize.category;

import java.util.Iterator;
import java.util.List;

import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.TreeNode;

/**
 * JSON utility for filter and report categories 
 * @author Tamas Ruff
 *
 */
public class CategoryJSON {
	
	private CategoryJSON() {
	}
	
	static interface JSON_FIELDS {
		String CATEGORY_TYPE = "categoryType";
		String READONLY = "readOnly";
		String MODIFIABLE = "modifiable";
		String DELETABLE = "deletable";
		String CAN_ADD_CHILD = "canAddChild";
		String CAN_COPY = "canCopy";
		String TREE_FILTER = "treeFilter";
		String REPORT_CONFIG_NEEDED = "reportConfigNeeded";
		String REPORT_ICON = "icon";
		String TYPE_LABEL = "typeLabel";
		String STYLE_FIELD_LABEL = "styleFieldLabel";
		String INCUDE_IN_MENU = "includeInMenu";
		String VIEW_NAME = "viewName";
	}
	
	public static String getCategoryDetailJSON(String categoryLabel, boolean modifiable) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.LABEL, categoryLabel);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.DELETABLE, modifiable);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.MODIFIABLE, modifiable, true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Gets the JSON string for configuration children 
	 * @param children
	 * @return
	 */
	public static String getChildrenJSON(List<CategoryNodeTO> children){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		for (Iterator<CategoryNodeTO> iterator = children.iterator(); iterator.hasNext();) {
			CategoryNodeTO categoryNodeTO = iterator.next();
			sb.append("{");
			sb.append(getChildJSON(categoryNodeTO));
			sb.append("}");
			if (iterator.hasNext()) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();		
	}	

	/**
	 * Gets the JSON string for configuration children 
	 * @param categoryNodeTO
	 * @return
	 */
	public static String getChildJSON(CategoryNodeTO categoryNodeTO){
		StringBuilder sb=new StringBuilder();
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ID, categoryNodeTO.getNodeID());
		JSONUtility.appendStringValue(sb, JSON_FIELDS.CATEGORY_TYPE, categoryNodeTO.getCategoryType());
		JSONUtility.appendStringValue(sb, JSON_FIELDS.CATEGORY_TYPE, categoryNodeTO.getCategoryType());
		JSONUtility.appendStringValue(sb,"cls", "treeItem-level-"+categoryNodeTO.getLevel());
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.TEXT, categoryNodeTO.getLabel());
		if (categoryNodeTO.getIconCls()!=null) {
			JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ICONCLS, categoryNodeTO.getIconCls());
		}
		if (categoryNodeTO.isReadOnly()) {
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.READONLY, categoryNodeTO.isReadOnly());
		}
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.MODIFIABLE, categoryNodeTO.isModifiable());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.DELETABLE, categoryNodeTO.isDeletable());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.CAN_ADD_CHILD, categoryNodeTO.isCanAddChild());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.CAN_COPY, categoryNodeTO.isCanCopy());
		if (categoryNodeTO.isTreeFilter()!=null) {
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.TREE_FILTER, categoryNodeTO.isTreeFilter().booleanValue());
		}
		if (categoryNodeTO.getReportConfigNeeded()!=null) {
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.REPORT_CONFIG_NEEDED, categoryNodeTO.getReportConfigNeeded().booleanValue());
		}
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.LEAF, categoryNodeTO.isLeaf(), true);
		return sb.toString();
	}	
	
	/**
	 * Gets the JSON string for configuration children 
	 * @param gridRows
	 * @return
	 */
	static String getGridRowJSON(List<CategoryGridRowTO> gridRows){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		for (Iterator<CategoryGridRowTO> iterator = gridRows.iterator(); iterator.hasNext();) {
			CategoryGridRowTO categoryGridRowTO = iterator.next();
			sb.append("{");			
			JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NODE, categoryGridRowTO.getNodeID());
			JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.TEXT, categoryGridRowTO.getLabel());
			JSONUtility.appendStringValue(sb, JSON_FIELDS.CATEGORY_TYPE, categoryGridRowTO.getCategoryType());			
			JSONUtility.appendStringValue(sb, JSON_FIELDS.TYPE_LABEL, categoryGridRowTO.getTypeLabel());
			if (categoryGridRowTO.getViewName()!=null) {
				//specific for filter
				JSONUtility.appendStringValue(sb, JSON_FIELDS.VIEW_NAME, categoryGridRowTO.getViewName());
			}
			if (categoryGridRowTO.getStyleFieldLabel()!=null) {
				//specific for filter
				JSONUtility.appendStringValue(sb, JSON_FIELDS.STYLE_FIELD_LABEL, categoryGridRowTO.getStyleFieldLabel());
			}
			if (categoryGridRowTO.isIncludeInMenu()) {
				//specific for filter
				JSONUtility.appendBooleanValue(sb, JSON_FIELDS.INCUDE_IN_MENU, categoryGridRowTO.isIncludeInMenu());
			}
			if (categoryGridRowTO.isTreeFilter()!=null) {
				JSONUtility.appendBooleanValue(sb, JSON_FIELDS.TREE_FILTER, categoryGridRowTO.isTreeFilter().booleanValue());
			}
			if (categoryGridRowTO.getReportConfigNeeded()!=null) {
				JSONUtility.appendBooleanValue(sb, JSON_FIELDS.REPORT_CONFIG_NEEDED, categoryGridRowTO.getReportConfigNeeded().booleanValue());
			}
			if (categoryGridRowTO.getReportIcon()!=null) {
				JSONUtility.appendStringValue(sb, JSON_FIELDS.REPORT_ICON, categoryGridRowTO.getReportIcon());
			}
			if (categoryGridRowTO.isReadOnly()) {
				JSONUtility.appendBooleanValue(sb, JSON_FIELDS.READONLY, categoryGridRowTO.isReadOnly());
			}
			if (categoryGridRowTO.getIconCls() != null) {
				JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ICONCLS, categoryGridRowTO.getIconCls());
			}else {
				JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ICONCLS, "folder");
			}			
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.MODIFIABLE, categoryGridRowTO.isModifiable());
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.DELETABLE, categoryGridRowTO.isDeletable());
			JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.LEAF, categoryGridRowTO.isLeaf(), true);
			sb.append("}");
			if (iterator.hasNext()) {
				sb.append(",");
			}
		}
		sb.append("]");//end data
		return sb.toString();
	}
	
	/**
	 * Creates the JSON string for failure by save
	 * @param success
	 * @param node
	 * @param errorMessage
	 * @return
	 */
	public static String createNodeResultJSON(boolean success, String node, String errorMessage){	
		StringBuilder sb=new StringBuilder();
		sb.append("{");						
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ERROR_MESSAGE, errorMessage);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NODE, node);
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, success, true);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Creates the save JSON string for a category
	 * @param success
	 * @param node identify the node to be selected in the tree
	 * @param objectID identify the node to be selected in the grid 
	 * @return
	 */
	public static String createSaveResultJSON(boolean success, String node, Integer objectID){	
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, success);
		JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.ID, objectID);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NODE, node, true);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Encode the category path picker
	 * @param nodes
	 * @return
	 */
	public static String encodeCategoryPathPickerJSON(List<TreeNode> nodes) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");						
		JSONUtility.appendJSONValue(sb, JSONUtility.JSON_FIELDS.DATA, JSONUtility.getTreeHierarchyJSON(nodes, false, true));
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true, true);
		sb.append("}");
		return sb.toString();
	}
}
