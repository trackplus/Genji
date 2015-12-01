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

package com.aurel.track.macro;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLoaderException;

public class MacroIssueDecorate extends MacroIssue{
	private static final Logger LOGGER = LogManager.getLogger(MacroIssueDecorate.class);
	@Override
	public String format(Integer workItemID, MacroContext macroContext){
		boolean useProjectSpecificID=macroContext.isUseProjectSpecificID();
		
		Map<Integer,TWorkItemBean> itemsMap=macroContext.getItemsMap();
		if(itemsMap==null){
			itemsMap=new HashMap<Integer,TWorkItemBean>();
			macroContext.setItemsMap(itemsMap);
		}
		TWorkItemBean inlineItem=itemsMap.get(workItemID);
		if(inlineItem==null){
			LOGGER.debug("No item found in chache. Try dynamic load");
			try {
				inlineItem= ItemBL.loadWorkItem(workItemID);
				itemsMap.put(workItemID,inlineItem);
			} catch (ItemLoaderException e) {
				LOGGER.debug(e.getMessage(), e);
			}
		}
		if(inlineItem==null){
			LOGGER.error("No workitem found!");
			return "[issue "+workItemID+"/]";
		}
		String title="";
		if(useProjectSpecificID){
			title=ItemBL.getSpecificProjectID(inlineItem)+":";
		}
		title=title+inlineItem.getSynopsis();
		return "[issue "+workItemID+" (<span class=\"disabled\">"+title+"</span>) /]";
	}
}
