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

package com.aurel.track.macro;

import com.aurel.track.Constants;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.item.ItemBL;
import com.aurel.track.report.execute.ReportBeanLink;

import java.util.Locale;
import java.util.SortedSet;

public class MacroIssueBL {
	private static String A_OPEN = "<a class=\"body-text\" target=\"_blank\" href=\"";
	private static String A_CLOSE = "</a>";
	private static String PARAM = "/printItem.action?key=";
	private static String HTML_CLOSE = "\">";

	private MacroIssueBL(){
	}
	public static StringBuilder createInlineItemHTML(boolean editMode, boolean includeWrapper, boolean useProjectSpecificID, Locale locale, TWorkItemBean inlineItem, SortedSet<ReportBeanLink> links) {
		StringBuilder sb = new StringBuilder();
		if(includeWrapper){
			sb.append("<div class=\"inlineItem\"");
			if(editMode){
				sb.append(" workitemid=").append(inlineItem.getObjectID());
				sb.append(" contenteditable=\"false\"");
			}
			sb.append(">");
		}
		sb.append("<div class=\"inlineItemTitle\">");
		sb.append(formatIssueTitle(inlineItem, useProjectSpecificID, locale));
		sb.append("</div>");
		sb.append("<div class=\"inlineItemBody\">");
		String description = inlineItem.getDescription();
		if (description == null || description.length() == 0) {
			sb.append(inlineItem.getSynopsis());
		} else {
			sb.append(description);// formatDescription(description,
			// true,false,false));
		}
		sb.append("</div>");
		if (links != null && !links.isEmpty()) {
			sb.append("<div class=\"inlineItemLinks\">");
			for (ReportBeanLink itemLinkListEntry : links) {
				sb.append("<div class=\"inlineLink\">");
				Integer linkedWorkItemID = itemLinkListEntry.getWorkItemID();
				String itemIDStr = useProjectSpecificID ? itemLinkListEntry.getProjectSpecificIssueNo() : linkedWorkItemID + "";
				sb.append(itemLinkListEntry.getLinkTypeName() + "&nbsp;&nbsp;");
				sb.append(A_OPEN);
				sb.append(Constants.BaseURL + PARAM + linkedWorkItemID);
				sb.append(HTML_CLOSE).append(itemIDStr).append(":").append(itemLinkListEntry.getLinkedItemTitle()).append(A_CLOSE);
				sb.append("</div>");
			}
			sb.append("</div>");
		}
		if(includeWrapper){
			sb.append("</div>");
		}
		return sb;
	}

	private static String formatIssueTitle(TWorkItemBean workItemBean, boolean useProjectSpecificID, Locale locale) {
		StringBuilder sb = new StringBuilder();
		if (workItemBean.getObjectID() != null) {
			sb.append("<img style=\"margin-bottom: -2px;margin-right: 2px;\" src=\"optionIconStream.action?fieldID=-2&optionID=");
			sb.append(workItemBean.getListTypeID() + "\" title=\"");
			sb.append(ItemBL.getIssueTypeDisplay(workItemBean, locale));
			sb.append("\"><strong>" + ItemBL.getProjectDisplay(workItemBean, locale));
			sb.append("</strong>");

			sb.append("<strong><span class=\"emphasize\">&nbsp;");
			String id = "";
			if (useProjectSpecificID) {
				id = ItemBL.getSpecificProjectID(workItemBean);
			} else {
				id = workItemBean.getObjectID().toString();
			}
			sb.append(id).append("</span>&nbsp;:&nbsp;");
			String statusDisplay = ItemBL.getStatusDisplay(workItemBean, locale);
			sb.append("<span class=\"dataEmphasize\">").append(statusDisplay).append("</span>");
			sb.append("&nbsp;:&nbsp;");
			sb.append("<a class=\"body-text\" target=\"_blank\" href=\"").append(Constants.BaseURL).append("/printItem.action?key=")
					.append(workItemBean.getObjectID()).append("\">");
			sb.append(workItemBean.getSynopsis());
			sb.append("</a>");
			sb.append("</strong>");
		}
		return sb.toString();
	}
}
