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

package com.aurel.track.linking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.item.IssuePickerJSON;
import com.aurel.track.item.link.ItemLinkJSON;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.linkType.ILinkType;
import com.aurel.track.util.LabelValueBean;

/**
 * Generate the JSON for linking
 * @author Tamas
 *
 */
public class LinkMatrixJSON {

	/**
	 * Get the configuration JSON for the link matrix
	 * @param columnFilterID
	 * @param rowFilterID
	 * @param linkTypeWithDirection
	 * @param linkTypes
	 * @return
	 */
	static String getConfigJSON(Integer columnFilterID, Integer rowFilterID, String linkTypeWithDirection, List<LabelValueBean> linkTypes) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendIntegerValue(sb, "columnsQueryID",columnFilterID);
		JSONUtility.appendIntegerValue(sb, "rowsQueryID", rowFilterID);
		JSONUtility.appendStringValue(sb, "linkTypeWithDirection", linkTypeWithDirection);
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
			List<TWorkItemLinkBean> linkList, List<TWorkItemLinkBean> reverseLinkList, List<Integer> workItemIDsRows, Integer linkDirection, boolean readOnly) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA);
		sb.append(":{");
		JSONUtility.appendBooleanValue(sb,"readOnly",readOnly);
		JSONUtility.appendJSONValue(sb,"itemsColumns",IssuePickerJSON.encodeIssues(itemsColumns, false, null));
		JSONUtility.appendJSONValue(sb,"itemsRows",IssuePickerJSON.encodeIssues(itemsRows, false, null));
		Map<String,String> linksMap=new HashMap<String, String>();
		Map<String,String> linksDescription=new HashMap<String, String>();
		if (linkList!=null && !linkList.isEmpty()){
			for(TWorkItemLinkBean workItemLinkBean:linkList) {
				Integer linkedItem = null;
				if (linkDirection.intValue()==ILinkType.LINK_DIRECTION.UNIDIRECTIONAL_OUTWARD) {
					linkedItem = workItemLinkBean.getLinkPred();
				} else {
					linkedItem = workItemLinkBean.getLinkSucc();
				}
				if (workItemIDsRows.contains(linkedItem)){
					if (workItemLinkBean.getDescription()!=null) {
						linksDescription.put(workItemLinkBean.getObjectID().toString(), workItemLinkBean.getDescription());
					}
					String key = null;
					if (linkDirection.intValue()==ILinkType.LINK_DIRECTION.UNIDIRECTIONAL_OUTWARD) {
						key = workItemLinkBean.getLinkPred()+"_"+workItemLinkBean.getLinkSucc();
					} else {
						key = workItemLinkBean.getLinkSucc()+"_"+workItemLinkBean.getLinkPred();
					}
					linksMap.put(key,workItemLinkBean.getObjectID().toString());
				}
			}
		}
		if (reverseLinkList!=null && !reverseLinkList.isEmpty()) {
			for(TWorkItemLinkBean workItemLinkBean:reverseLinkList) {
				Integer linkedItem = null;
				if (linkDirection.intValue()==ILinkType.LINK_DIRECTION.UNIDIRECTIONAL_OUTWARD) {
					linkedItem = workItemLinkBean.getLinkSucc();
				} else {
					linkedItem = workItemLinkBean.getLinkPred();
				}
				if (workItemIDsRows.contains(linkedItem)) {
					if (workItemLinkBean.getDescription()!=null) {
						linksDescription.put(workItemLinkBean.getObjectID().toString(),workItemLinkBean.getDescription());
					}
					String key = null;
					if (linkDirection.intValue()==ILinkType.LINK_DIRECTION.UNIDIRECTIONAL_OUTWARD) {
						key = workItemLinkBean.getLinkSucc()+"_"+workItemLinkBean.getLinkPred();
					} else {
						key = workItemLinkBean.getLinkPred()+"_"+workItemLinkBean.getLinkSucc();
					}
					linksMap.put(key,workItemLinkBean.getObjectID().toString());
				}
			}
		}
		JSONUtility.appendStringParametersMap(sb,"links",linksMap);
		JSONUtility.appendStringParametersMap(sb,"linksDescription",linksDescription);
		JSONUtility.appendIntegerValue(sb,"some",1,true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
}
