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

import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.linkType.InlineLinkType;
import com.aurel.track.linkType.LinkTypeBL;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.List;

public class InlineItemLinkBL {
	private static final Logger LOGGER = LogManager.getLogger(InlineItemLinkBL.class);

	public static void saveInlineItemLinks(List<Integer> inlineItems,Integer workItemID,Integer rootItemID){
		Integer linkTypeID = getInlineItemLinkType();
		if (linkTypeID==null) {
			return;
		}
		Integer linkDirection=InlineLinkType.getInstance().getPossibleDirection();
		LOGGER.debug("Save inlineItems for: workItemID="+workItemID+" rootItemID="+rootItemID);
		LOGGER.debug("linkTypeIDD="+linkTypeID+" linkDirection="+linkDirection);
		String description=null;
		String params=null;
		//documentID
		Integer predItemID=rootItemID==null?workItemID:rootItemID;
		List<TWorkItemLinkBean> linkList =ItemLinkBL.loadByPredAndLinkType(predItemID, linkTypeID, linkDirection);
		if(inlineItems!=null){
			for(Integer itemID:inlineItems){
				boolean needToSave=true;
				TWorkItemLinkBean workItemLinkBean=finLinkByItemID(linkList,itemID);
				if(workItemLinkBean!=null){
					//link already added
					LOGGER.debug("Inline item:"+itemID+" already added!");
					params=workItemLinkBean.getStringValue1();
					if(params!=null&&params.indexOf(workItemID+";")>-1){
						needToSave=false;
						LOGGER.debug("Link not need to save!");
					}else{
						needToSave=true;
						if(params==null){
							params=workItemID+";";
						}else{
							params=params+workItemID+";";
						}
						workItemLinkBean.setStringValue1(params);
					}
				}else{
					workItemLinkBean=new TWorkItemLinkBean();
					workItemLinkBean.setStringValue1(workItemID+";");
					workItemLinkBean.setLinkType(linkTypeID);
					workItemLinkBean.setDescription(description);
					workItemLinkBean.setLinkDirection(linkDirection);
					workItemLinkBean.setLinkSucc(itemID);
					workItemLinkBean.setLinkPred(predItemID);
					needToSave=true;
				}
				if(needToSave){
					ItemLinkBL.saveLink(workItemLinkBean);
				}
			}
		}
		//delete/update links
		for(TWorkItemLinkBean linkBean:linkList){
			params=linkBean.getStringValue1();
			int index=params.indexOf(workItemID+";");
			if(index>-1){
				Integer itemID=linkBean.getLinkSucc();
				if(inlineItems==null||!inlineItems.contains(itemID)){
					LOGGER.debug("Inline item is not needed anymore: workItemID="+workItemID+" itemID="+itemID);
					if(params.equals(workItemID+";")){
						//delete
						LOGGER.debug("Delete link for inline Item:"+itemID);
						ItemLinkBL.deleteLink(linkBean.getObjectID());
					}else{
						//update
						params=params.substring(0,index)+params.substring(index+(workItemID+";").length(),params.length());
						linkBean.setStringValue1(params);
						ItemLinkBL.saveLink(linkBean);
					}
				}
			}
		}
	}

	public static Integer getInlineItemLinkType() {
		List<Integer> inlineLinkTypes= LinkTypeBL.getLinkTypesByPluginClass(InlineLinkType.getInstance());
		if (inlineLinkTypes!=null && !inlineLinkTypes.isEmpty()) {
			return inlineLinkTypes.get(0);
		}
		return null;
	}

	private static TWorkItemLinkBean finLinkByItemID(List<TWorkItemLinkBean> linkList,Integer itemID){
		if(linkList==null||linkList.size()==0){
			return null;
		}
		for(TWorkItemLinkBean linkBean:linkList){
			if(itemID.equals(linkBean.getLinkSucc())){
				return linkBean;
			}
		}
		return null;
	}
}
