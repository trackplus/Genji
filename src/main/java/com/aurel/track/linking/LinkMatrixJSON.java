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

package com.aurel.track.linking;

import java.util.List;
import java.util.Map;

import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.item.IssuePickerJSON;
import com.aurel.track.item.link.ItemLinkJSON;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.TreeNode;

/**
 * Generate the JSON for linking
 * @author Tamas
 *
 */
public class LinkMatrixJSON {

	/**
	 * Get the configuration JSON for the link matrix
	 * @param columnFilterID
	 * @param columnLinkedFlag
	 * @param rowFilterID
	 * @param rowLinkedFlag
	 * @param linkFlags
	 * @param linkTypeWithDirection
	 * @param linkTypes
	 * @return
	 */
	static String getConfigJSON(List<TreeNode> filterTree, Integer columnFilterID, Integer columnLinkedFlag,
			Integer rowFilterID, Integer rowLinkedFlag,
			List<IntegerStringBean> linkFlags, String linkTypeWithDirection, List<LabelValueBean> linkTypes) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendJSONValue(sb, "columnFilterTree", JSONUtility.getTreeHierarchyJSON(filterTree));
		JSONUtility.appendIntegerValue(sb, "columnFilterID", columnFilterID);
		JSONUtility.appendIntegerValue(sb, "columnLinkedFlag", columnLinkedFlag);
		JSONUtility.appendIntegerValue(sb, "rowFilterID", rowFilterID);
		JSONUtility.appendJSONValue(sb, "rowFilterTree", JSONUtility.getTreeHierarchyJSON(filterTree));
		JSONUtility.appendIntegerValue(sb, "rowLinkedFlag", rowLinkedFlag);
		JSONUtility.appendStringValue(sb, "linkTypeWithDirection", linkTypeWithDirection);
		JSONUtility.appendIntegerStringBeanList(sb, "linkFlags", linkFlags);
		JSONUtility.appendLabelValueBeanList(sb, ItemLinkJSON.JSON_FIELDS.LINK_TYPE_LIST, linkTypes, true);
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Gets the link matrix JSON 
	 * @param itemsColumns
	 * @param itemsRows
	 * @param linkList
	 * @param reverseLinkList
	 * @param workItemIDsRows
	 * @param linkDirection
	 * @return
	 */
	static String getLoadMatrixJSON(List<TWorkItemBean> itemsColumns, List<TWorkItemBean> itemsRows,
			Map<String,String> linksMap,  Map<String,String> linksDescription, boolean readOnly) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA);
		sb.append(":{");
		JSONUtility.appendJSONValue(sb, "itemsColumns", IssuePickerJSON.encodeIssues(itemsColumns, false, null));
		JSONUtility.appendJSONValue(sb, "itemsRows", IssuePickerJSON.encodeIssues(itemsRows, false, null));
		JSONUtility.appendStringParametersMap(sb, "links", linksMap);
		JSONUtility.appendStringParametersMap(sb, "linksDescription", linksDescription);
		JSONUtility.appendBooleanValue(sb, "readOnly", readOnly, true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
}
