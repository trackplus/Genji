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

import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.report.execute.ReportBeanLink;

/**
 */
public class MacroIssue implements IMacro {

	private static final Logger LOGGER = LogManager.getLogger(MacroIssue.class);

	public String format(MacroDef macroDef, MacroContext macroContext) {
		Integer workItemID = getItemID(macroDef);
		if (workItemID == null) {
			LOGGER.error("no workItemID found");
			return "";
		}
		return format(workItemID, macroContext);
	}

	public static Integer getItemID(MacroDef macroDef) {
		Integer workItemID = null;
		String[] params = macroDef.getSimpleParameters();
		if (params != null && params.length > 0) {
			try {
				workItemID = Integer.parseInt(params[0]);
			} catch (Exception ex) {
			}
		}
		return workItemID;
	}

	public String format(Integer workItemID, MacroContext macroContext) {
		return  format(workItemID, macroContext,false);
	}
	protected String format(Integer workItemID, MacroContext macroContext,boolean editMode) {
		boolean useProjectSpecificID = macroContext.isUseProjectSpecificID();
		Locale locale = macroContext.getLocale();
		Map<Integer, TWorkItemBean> itemsMap = macroContext.getItemsMap();
		Map<Integer, SortedSet<ReportBeanLink>> linksMap = macroContext.getLinksMap();

		TWorkItemBean inlineItem = itemsMap == null ? null : itemsMap.get(workItemID);
		if (inlineItem == null) {
			LOGGER.debug("No item found in chache. Try dynamic load");
			try {
				inlineItem = ItemBL.loadWorkItem(workItemID);
				itemsMap.put(workItemID,inlineItem);
			} catch (ItemLoaderException e) {
				LOGGER.debug(e.getMessage(), e);
			}
		}
		if (inlineItem == null) {
			LOGGER.error("No workitem found!");
			return "";
		}
		SortedSet<ReportBeanLink> links=null;
		if(linksMap!=null){
			links = linksMap.get(workItemID);
		}

		StringBuilder sb = MacroIssueBL.createInlineItemHTML(editMode,true, useProjectSpecificID, locale, inlineItem, links);

		return sb.toString();
	}




}
