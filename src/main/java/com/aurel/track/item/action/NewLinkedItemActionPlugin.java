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


package com.aurel.track.item.action;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TLinkTypeBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemDetailBL;
import com.aurel.track.item.ItemLocationForm;
import com.aurel.track.linkType.ILinkType;
import com.aurel.track.linkType.LinkTypeBL;
import com.aurel.track.plugin.PluginManager;

public class NewLinkedItemActionPlugin extends NewItemChildActionPlugin {
	private static final Logger LOGGER = LogManager.getLogger(NewLinkedItemActionPlugin.class);
	
	
	@Override
	protected WorkItemContext createCtx(ItemLocationForm form,Integer personID,Locale locale){
		Boolean accessLevelFlag=form.isAccessLevelFlag();
		Integer issueTypeID=form.getIssueTypeID();
		Integer projectID=form.getProjectID();
		Integer parentID=form.getParentID();
		WorkItemContext workItemContext=ItemBL.createNewItem(projectID, issueTypeID, personID, accessLevelFlag, locale);
		HashMap<String, Object>newlyCreatedLinkSettings = (HashMap<String, Object>) form.getNewlyCreatedLinkSettings();
		Integer newParentID = null;
		String linkTypeWithDirection = null;
		String description = null;
		Map<Integer, String> parametersMap = null;
		//In case of Add linked item  we need to check if parametrs are valids for 
		//creating the link. Otherwise will be created a dummy link.
		if(newlyCreatedLinkSettings!=null) {
			newParentID = (Integer)newlyCreatedLinkSettings.get("linkedWorkItemID");
			linkTypeWithDirection = (String)newlyCreatedLinkSettings.get("linkTypeWithDirection");
			parametersMap = (HashMap<Integer, String>)newlyCreatedLinkSettings.get("parametersMap");
			description = (String)newlyCreatedLinkSettings.get("description");
		}
		if (parentID!=null) {
			List<TLinkTypeBean> linkTypeBeans = LinkTypeBL.loadAll();
			if (linkTypeBeans!=null && !linkTypeBeans.isEmpty()) {
				TLinkTypeBean linkTypeBean = linkTypeBeans.get(0);
				SortedMap<Integer, TWorkItemLinkBean> workItemsLinksMap = new TreeMap<Integer, TWorkItemLinkBean>();
				workItemContext.setWorkItemsLinksMap(workItemsLinksMap);
				TWorkItemLinkBean workItemLinkBean = new TWorkItemLinkBean();
				workItemsLinksMap.put(Integer.valueOf(1), workItemLinkBean);
				if(newlyCreatedLinkSettings!=null) {
					//In case of Add linked item  the newly link will be created based on user settings
					createLinkBasedOnClientSettings(newParentID, linkTypeWithDirection, workItemLinkBean, description, parametersMap, personID, locale);
				}else {
					//Otherwise will be created a dummy link. First from TLinkTypeBean list.
					LOGGER.debug("The linked item is " + parentID);
					workItemLinkBean.setLinkType(linkTypeBean.getObjectID());
					Integer linkDirection = linkTypeBean.getLinkDirection();
					Integer itemLinkDirction = null;
					if (linkDirection.intValue()==ILinkType.LINK_DIRECTION.BIDIRECTIONAL) {
						itemLinkDirction = ILinkType.LINK_DIRECTION.LEFT_TO_RIGHT;
						workItemLinkBean.setLinkSucc(parentID);
					} else {
						itemLinkDirction = ILinkType.LINK_DIRECTION.RIGHT_TO_LEFT;
						workItemLinkBean.setLinkPred(parentID);
					}
					workItemLinkBean.setLinkDirection(itemLinkDirction);
				}
			}
		}
		return  workItemContext;
	}
	
	/**
	 * 	In case of Add linked item  the newly link will be created based on user settings
	 * @param newParentID
	 * @param linkTypeWithDirection
	 * @param workItemLinkBean
	 * @param description
	 * @param parametersMap
	 * @param personID
	 * @param locale
	 */
	private void createLinkBasedOnClientSettings(Integer parentID, String linkTypeWithDirection, TWorkItemLinkBean workItemLinkBean, 
			String description, Map<Integer, String> parametersMap, Integer personID, Locale locale) {
		LOGGER.debug("The linked item is " + parentID);
		Integer linkType=MergeUtil.getFieldID(linkTypeWithDirection);
		String linkTypePluginString = LinkTypeBL.getLinkTypePluginString(linkType);		
		ILinkType linkTypePlugin = (ILinkType)PluginManager.getInstance().getPluginClass(PluginManager.LINKTYPE_ELEMENT, linkTypePluginString);
		
		workItemLinkBean.setLinkType(linkType);
		Integer linkDirection = MergeUtil.getParameterCode(linkTypeWithDirection);
		Integer itemLinkDirction = ILinkType.LINK_DIRECTION.RIGHT_TO_LEFT;;
		if (linkTypePlugin.getPossibleDirection()==ILinkType.LINK_DIRECTION.BIDIRECTIONAL) {
			workItemLinkBean.setLinkSucc(parentID);
		}else {
			workItemLinkBean.setLinkPred(parentID);
		}
		if(linkDirection.intValue() == ILinkType.LINK_DIRECTION.LEFT_TO_RIGHT) {
			itemLinkDirction = ILinkType.LINK_DIRECTION.LEFT_TO_RIGHT;
		}
		linkTypePlugin.unwrapParametersBeforeSave(parametersMap, workItemLinkBean, PersonBL.loadByPrimaryKey(personID), locale);
		if(description!=null) {
			workItemLinkBean.setDescription(description);
		}
		workItemLinkBean.setLinkDirection(itemLinkDirction);
	}
	
	@Override
	public boolean isEnabled(Integer personID, TWorkItemBean workItemBean,
			boolean allowedToChange, boolean allowedToCreate, int appEdition) {								
		if (workItemBean==null || workItemBean.getObjectID()==null) {			
			return false;
		}
		return appEdition>=2 && allowedToCreate && ItemDetailBL.isEnableLinks(workItemBean);
	}
}
