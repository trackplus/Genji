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


package com.aurel.track.linkType;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.item.link.ItemLinkBL;
import com.aurel.track.util.EqualUtils;

public abstract class AbstractLinkType implements ILinkType {

	public String getPluginID() {
		return this.getClass().getName();
	}
	
	@Override
	public void afterCreateLink() {
	}

	@Override
	public boolean beforeCreateLink() {
		return false;
	}
	
	/**
	 * Whether this link type appears in gantt view
	 * @return
	 */
	@Override
	public boolean isGanttSpecific() {
		return false;
	}
	
	/**
	 * Whether this link type is inline 
	 * (never created explicitly, only implicitly in the background by including an item inline in a document)
	 * @return
	 */
	@Override
	public boolean isInline() {
		return false;
	}
	
	/**
	 * Gets the item type specific data for a reportBeanLink
	 * @param workItemLinkBean
	 * @return
	 */
	@Override
	public ItemLinkSpecificData getItemLinkSpecificData(TWorkItemLinkBean workItemLinkBean) {
		return null;
	}
	
	/**
	 * Gets the itemLinkSpecificData as a string map for serializing 
	 * @param itemLinkSpecificData
	 * @param locale
	 * @return
	 */
	@Override
	public Map<String, String> serializeItemLinkSpecificData(ItemLinkSpecificData itemLinkSpecificData, Locale locale) {
		return null;
	}
	
	/**
	 * Validation called before saving the issue
	 * @param workItemBean
	 * @param workItemBeanOriginal
	 * @param person
	 * @param directDirectionLinksOfThisType
	 * @param reverseDirectionLinksOfThisType
	 * @param confirm
	 * @param locale
	 * @return
	 */
	@Override
	public List<ErrorData> validateBeforeIssueSave(TWorkItemBean workItemBean, 
			TWorkItemBean workItemBeanOriginal, Integer person, 
			List<TWorkItemLinkBean> directDirectionLinksOfThisType,
			List<TWorkItemLinkBean> reverseDirectionLinksOfThisType, boolean confirm, Locale locale) {
		return null;
	}
	
	/**
	 *  Called after the issue is saved
	 * @param workItemBean
	 * @param workItemBeanOriginal
	 * @param personID
	 * @param predToSuccLinksOfType
	 * @param succToPredLinksOfType
	 * @param locale
	 */
	@Override
	public void afterIssueSave(TWorkItemBean workItemBean, TWorkItemBean workItemBeanOriginal, Integer personID, List<TWorkItemLinkBean> predToSuccLinksOfType,
			List<TWorkItemLinkBean> succToPredLinksOfType, Locale locale) {
	}
	
	/**
	 * Prepares the parameters in the Link tab
	 * @param workItemLinkBean
	 * @param linkDirection
	 * @param locale
	 */
	@Override
	public String prepareParametersOnLinkTab(TWorkItemLinkBean workItemLinkBean, Integer linkDirection, Locale locale) {
		//no specific parameters
		return "";
	}
	
	/**
	 * Gets the JavaScript class for configuring the link type specific part
	 * @return
	 */
	@Override
	public String getLinkTypeJSClass() {
		return null;
	}
	
	/**
	 * Gets the link type specific configuration as JSON
	 * @param workItemLinkBean
	 * @param personBean
	 * @param locale
	 * @return
	 *
	 */
	@Override
	public String getSpecificJSON(TWorkItemLinkBean workItemLinkBean, TPersonBean personBean, Locale locale) {
		return "";
	}
	
	/**
	 * Sets the workItemLinkBean according to the values submitted in the parameters map
	 * @param parametersMap the parameters from link configuration
	 * @param workItemLinkBean
	 * @param personBean the current user
	 * @param locale
	 * @return
	 */
	@Override
	public List<ErrorData> unwrapParametersBeforeSave(Map<Integer, String> parametersMap, 
			TWorkItemLinkBean workItemLinkBean, TPersonBean personBean, Locale locale) {
		//no specific parameters nothing to unwrap
		return null;
	}
	
	/**
	 * Whether the linked item is stored as predecessor or successor
	 * @param workItemLinkBean
	 * @param linkedIsSuccessor
	 * @return
	 */
	private Integer getLinkedItem(TWorkItemLinkBean workItemLinkBean, boolean linkedIsSuccessor) {
		if (linkedIsSuccessor) {
			return workItemLinkBean.getLinkSucc();
		} else {
			return workItemLinkBean.getLinkPred();
		}
	}
	
	/**
	 * Validates the workItemLink before save
	 * @param workItemLinkBean
	 * @param workItemLinkOriginal
	 * @param workItemsLinksMap
	 * @param predWorkItem
	 * @param succWorkItem
	 * @param personBean
	 * @param locale
	 * @return
	 */
	@Override
	public List<ErrorData> validateBeforeSave(TWorkItemLinkBean workItemLinkBean, TWorkItemLinkBean workItemLinkOriginal,
			SortedMap<Integer, TWorkItemLinkBean> workItemsLinksMap,
			TWorkItemBean predWorkItem, TWorkItemBean succWorkItem, TPersonBean personBean, Locale locale) {
		List<ErrorData> errorDataList = new LinkedList<ErrorData>();
		Integer linkPred = workItemLinkBean.getLinkPred();
		Integer linkSucc = workItemLinkBean.getLinkSucc();
		if (linkPred==null || linkSucc==null) {
			//add link to a new item
			boolean linkedIsSuccessor = false;
			if (linkSucc!=null) {
				linkedIsSuccessor = true;
			} else {
				if (linkPred!=null) {
					linkedIsSuccessor = false;
				}
			}
			Integer newLinkedItemID = getLinkedItem(workItemLinkBean, linkedIsSuccessor);
			Integer newlinkType = workItemLinkBean.getLinkType();
			Integer newLinkDirection = workItemLinkBean.getLinkDirection(); 
			if (newLinkedItemID!=null) {
				if (workItemsLinksMap!=null) {
					for (TWorkItemLinkBean addedWorkItemLinkBean : workItemsLinksMap.values()) {
						Integer addedLinkedItemID = getLinkedItem(addedWorkItemLinkBean, linkedIsSuccessor);
						if (EqualUtils.equal(newLinkedItemID, addedLinkedItemID) &&
							EqualUtils.equal(newlinkType, addedWorkItemLinkBean.getLinkType())) {
							//duplicate link for new item
							if (EqualUtils.equal(newLinkDirection, addedWorkItemLinkBean.getLinkDirection())) {
								errorDataList.add(new ErrorData("item.tabs.itemLink.err.linkExists"));
							} else {
								errorDataList.add(new ErrorData("item.tabs.itemLink.err.circularDependency"));
							}
							return errorDataList;
						}
					}
				}
			}
			//no duplicate link for new item
			return errorDataList;
		}
		Integer linkTypeID = workItemLinkBean.getLinkType();
		Integer newLinkDirection = workItemLinkBean.getLinkDirection(); 
		List<Integer> linkTypeIDs = new LinkedList<Integer>();
		linkTypeIDs.add(linkTypeID);
		//link with the same linkTypeID can't be duplicated
		List<TWorkItemLinkBean> existingLinks = ItemLinkBL.loadLinksOfWorkItems(
				workItemLinkBean.getObjectID(), linkPred, linkSucc, linkTypeIDs);
		boolean linkExists = existingLinks!=null && !existingLinks.isEmpty();
		if (linkExists) {
			TWorkItemLinkBean existingLinkBean = existingLinks.get(0);
			if (EqualUtils.equal(newLinkDirection, existingLinkBean.getLinkDirection())) {
				errorDataList.add(new ErrorData("item.tabs.itemLink.err.linkExists"));
			} else {
				errorDataList.add(new ErrorData("item.tabs.itemLink.err.circularDependency"));
			}
		}
		List<TWorkItemLinkBean> reverseLinks = ItemLinkBL.loadLinksOfWorkItems(
				workItemLinkBean.getObjectID(), linkSucc, linkPred, linkTypeIDs);
		boolean reverseLinkExists = reverseLinks!=null && !reverseLinks.isEmpty();
		if (reverseLinkExists) {
			TWorkItemLinkBean existingLinkBean = reverseLinks.get(0);
			if (EqualUtils.equal(newLinkDirection, existingLinkBean.getLinkDirection())) {
				errorDataList.add(new ErrorData("item.tabs.itemLink.err.circularDependency"));
			} else {
				errorDataList.add(new ErrorData("item.tabs.itemLink.err.linkExists"));
			}
		}
		return errorDataList;
	}
	
	
}
