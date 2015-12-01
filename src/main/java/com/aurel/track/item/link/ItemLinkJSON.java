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

package com.aurel.track.item.link;

import com.aurel.track.item.ItemBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.report.execute.ReportBeanLink;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.LabelValueBean;

import java.util.*;

public class ItemLinkJSON {

	public static interface JSON_FIELDS {
		//link edit fields
		static final String LINK_TYPE_WITH_DIRECTION = "linkTypeWithDirection";
		static final String LINK_TYPE_LIST = "linkTypesList";
		static final String LINKED_WORKITEM_ID = "linkedWorkItemID";
        static final String LINKED_WORKITEM_OBJECTID = "linkedWorkItemObjectID";
		static final String LINKED_WORKITEM_TITLE = "linkedWorkItemTitle";
		static final String SPECIFIC_DATA = "specificData";
		static final String LINK_TYPE_JS_CLASS = "linkTypeJSClass";
		//link list fields
		static final String ID = "id";
		static final String LAST_EDIT = "date";
		static final String LINK_TYPE_ID = "linkTypeID";
		static final String LINK_TYPE = "linkType";
		static final String ITEM_TITLE = "itemTitle";
		static final String WORKITEMID = "workItemID";
		static final String ITEMID = "itemID";
		static final String ITEMSTATUS = "itemStatus";
		static final String ITEMRESPONSIBLE = "itemResponsible";
		static final String PARAMETERS = "parameters";
		static final String COMMENT = "comment";
		static final String EDITABLE = "editable";
	}

	/**
	 * Encode the workItem links list for grid
	 * @param list
	 * @return
	 */
	public static String encodeLinkList(List<ItemLinkListEntry> list) {
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if (list!=null) {
			for (Iterator<ItemLinkListEntry> iterator = list.iterator(); iterator.hasNext();) {
				ItemLinkListEntry itemLinkListEntry = iterator.next();
				sb.append(encodeLinkListEntry(itemLinkListEntry));
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}

	public static String encodeLinkSet(Set<ReportBeanLink> list) {
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if (list!=null) {
			for (Iterator<ReportBeanLink> iterator = list.iterator(); iterator.hasNext();) {
				ReportBeanLink reportBeanLink = iterator.next();
				sb.append(encodeReportBeanLink(reportBeanLink));
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}

	private static String encodeReportBeanLink(ReportBeanLink reportBeanLink) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.LINK_TYPE_ID, reportBeanLink.getLinkTypeID());
		JSONUtility.appendStringValue(sb, JSON_FIELDS.LINK_TYPE, reportBeanLink.getLinkTypeName());
		JSONUtility.appendStringValue(sb, JSON_FIELDS.ITEM_TITLE, reportBeanLink.getLinkedItemTitle());
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.WORKITEMID, reportBeanLink.getWorkItemID());
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.ID, reportBeanLink.getObjectID(),true);
		sb.append("}");
		return sb.toString();
	}



	/**
	 * Encode a list entry for grid
	 * @param itemLinkListEntry

	 * @return
	 */
	private static String encodeLinkListEntry(ItemLinkListEntry itemLinkListEntry) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.ID, itemLinkListEntry.getLinkID());
		JSONUtility.appendDateTimeValue(sb, JSON_FIELDS.LAST_EDIT, itemLinkListEntry.getLastEdit());
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.LINK_TYPE_ID, itemLinkListEntry.getLinkType());
		JSONUtility.appendStringValue(sb, JSON_FIELDS.LINK_TYPE, itemLinkListEntry.getLinkTypeName());
		JSONUtility.appendStringValue(sb, JSON_FIELDS.ITEM_TITLE, itemLinkListEntry.getLinkedWorkItemTitle());
		Integer linkedWorkItemID = itemLinkListEntry.getLinkedWorkItemID();
		String itemIDStr = ItemBL.getItemNo(linkedWorkItemID);
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.WORKITEMID, linkedWorkItemID);
		JSONUtility.appendStringValue(sb, JSON_FIELDS.ITEMID, itemIDStr);
		JSONUtility.appendStringValue(sb, JSON_FIELDS.ITEMSTATUS, itemLinkListEntry.getStateLabel());
		JSONUtility.appendStringValue(sb, JSON_FIELDS.ITEMRESPONSIBLE, itemLinkListEntry.getResponsibleLabel());
		JSONUtility.appendStringValue(sb, JSON_FIELDS.PARAMETERS, itemLinkListEntry.getParameters());
		JSONUtility.appendStringValue(sb, JSON_FIELDS.COMMENT, itemLinkListEntry.getDescription());
		sb.append("\"").append(JSON_FIELDS.EDITABLE).append("\":").append(itemLinkListEntry.isEditable());
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Encode a workItem link for edit
	 * @param linkTypeWithDirection
	 * @param linkTypes
	 * @param linkedWorkItemID
	 * @param linkedWorkItemTitle
	 * @param linkDescription
	 * @param linkTypeJSClass
	 * @param specificJSON
	 * @return
	 */
	public static String encodeLink(String linkTypeWithDirection, List<LabelValueBean> linkTypes, String linkedWorkItemID,
            Integer linkedWorkItemObjectID, String linkedWorkItemTitle, String linkDescription, String linkTypeJSClass, String specificJSON){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendStringValue(sb, JSON_FIELDS.LINK_TYPE_WITH_DIRECTION, linkTypeWithDirection);
		JSONUtility.appendStringValue(sb, JSON_FIELDS.LINKED_WORKITEM_ID,linkedWorkItemID);
        JSONUtility.appendIntegerValue(sb, JSON_FIELDS.LINKED_WORKITEM_OBJECTID,linkedWorkItemObjectID);
		JSONUtility.appendStringValue(sb, JSON_FIELDS.LINKED_WORKITEM_TITLE,linkedWorkItemTitle);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.DESCRIPTION,linkDescription);
		JSONUtility.appendStringValue(sb, JSON_FIELDS.LINK_TYPE_JS_CLASS, linkTypeJSClass);
		JSONUtility.appendJSONValue(sb, JSON_FIELDS.SPECIFIC_DATA, specificJSON);
		JSONUtility.appendLabelValueBeanList(sb, JSON_FIELDS.LINK_TYPE_LIST, linkTypes, true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Encode the specific part of a workItem link
	 * @param linkTypeJSClass
	 * @param specificJSON
	 * @return
	 */
	public static String encodeSpecificPart(String linkTypeJSClass, String specificJSON){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendJSONValue(sb, JSON_FIELDS.SPECIFIC_DATA, specificJSON);
		JSONUtility.appendStringValue(sb, JSON_FIELDS.LINK_TYPE_JS_CLASS, linkTypeJSClass, true);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Encode the add link errors from issue navigator
	 * @param errorMap
	 * @param locale
	 * @return
	 */
	static String encodeAddLinkFromItemNavigatorErrors(Map<String, List<LabelValueBean>> errorMap, int numberOfLinks, Locale locale) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, false);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.TITLE,
				LocalizeUtil.getLocalizedTextFromApplicationResources("itemov.link.err.title", locale));
		if (numberOfLinks>0) {
			//not all links have problems. If some links might be added then the user should be asked whether to add only those links
			JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.ERROR_CODE, JSONUtility.EDIT_ERROR_CODES.NEED_CONFIRMATION);
		}
		sb.append(JSONUtility.JSON_FIELDS.ERROR_MESSAGE).append(":\"");
		for (Iterator<Map.Entry<String, List<LabelValueBean>>> iterator = errorMap.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String, List<LabelValueBean>> pairsForErrorKey = iterator.next();
			String errorKey = pairsForErrorKey.getKey();
			List<LabelValueBean> issuePairs = pairsForErrorKey.getValue();
			if (issuePairs!=null && !issuePairs.isEmpty()) {
				Object[] warningArguments = new Object[2];
				StringBuilder itemPairs = new StringBuilder();
				for (Iterator<LabelValueBean> itrPairs = issuePairs.iterator(); itrPairs.hasNext();) {
					LabelValueBean labelValueBean = (LabelValueBean) itrPairs.next();
					itemPairs.append(labelValueBean.getLabel());
					itemPairs.append("-");
					itemPairs.append(labelValueBean.getValue());
					if (itrPairs.hasNext()) {
						itemPairs.append(", ");
					}
				}
				warningArguments[0] = itemPairs.toString();
				warningArguments[1] = LocalizeUtil.getLocalizedTextFromApplicationResources(errorKey, locale);
				sb.append(LocalizeUtil.getParametrizedString("itemov.link.err.notAddWarning", warningArguments, locale));
				if (numberOfLinks>0) {
					Object[] confirmArguments = new Object[] {String.valueOf(numberOfLinks)};
					sb.append(" ");
					sb.append(LocalizeUtil.getParametrizedString("itemov.link.err.confirm", confirmArguments, locale));
				}
			}
			if (iterator.hasNext()) {
				sb.append("<br>");
			}
		}
		sb.append("\"");
		sb.append("}");
		return sb.toString();
	}
}
