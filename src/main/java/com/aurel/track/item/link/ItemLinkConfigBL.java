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

package com.aurel.track.item.link;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.linkType.ILinkType;
import com.aurel.track.linkType.ILinkType.LINK_DIRECTION;
import com.aurel.track.linkType.LinkTypeBL;
import com.aurel.track.plugin.PluginManager;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.LabelValueBean;

/**
 * Add/edit/save/delete item link form item link tab
 * @author Tamas
 *
 */
public class ItemLinkConfigBL {
	
	/**
	 * Render the item links in link tab 
	 * @param workItemID
	 * @param workItemContext
	 * @param personID
	 * @param locale
	 * @return
	 */
	static List<ItemLinkListEntry> getItemLinksInTab(Integer workItemID, WorkItemContext workItemContext, Integer personID, Locale locale) {
		SortedMap<Integer, TWorkItemLinkBean> successorsForMeAsPredecessorMap = null;
		SortedMap<Integer, TWorkItemLinkBean> predecessorsForMeAsSuccessorMap = null;
		boolean editable = false;
		if (workItemID==null) {
			//new item
			SortedMap<Integer, TWorkItemLinkBean> workItemLinksMap = workItemContext.getWorkItemsLinksMap();
			successorsForMeAsPredecessorMap = new TreeMap<Integer, TWorkItemLinkBean>();
			predecessorsForMeAsSuccessorMap = new TreeMap<Integer, TWorkItemLinkBean>();
			ItemLinkBL.getSuccessorsForMeAsPredecessor(workItemLinksMap, successorsForMeAsPredecessorMap, predecessorsForMeAsSuccessorMap);
			editable = true;
		} else {
			//existing item
			successorsForMeAsPredecessorMap =  ItemLinkBL.getSuccessorsForMeAsPredecessorMap(workItemID);
			predecessorsForMeAsSuccessorMap = ItemLinkBL.getPredecessorsForMeAsSuccessorMap(workItemID);
			try {
				editable = AccessBeans.isAllowedToChange(ItemBL.loadWorkItem(workItemID), personID);
			} catch(Exception e) {
				
			}
		}
		return ItemLinkBL.getLinks(successorsForMeAsPredecessorMap, predecessorsForMeAsSuccessorMap, editable, locale, workItemID==null);
	}
	
	/**
	 * Render the add/edit link dialog
	 * @param workItemID
	 * @param workItemContext
	 * @param linkID
	 * @param fromGantt
	 * @param personBean
	 * @param locale
	 * @return
	 */
	static String editItemLink(Integer workItemID, WorkItemContext workItemContext, Integer linkID, boolean fromGantt, TPersonBean personBean, Locale locale) {
		TWorkItemLinkBean workItemLinkBean = ItemLinkConfigBL.getItemLinkBean(linkID, workItemID, workItemContext);
		ItemLinkTO itemLinkTO = ItemLinkBL.getLinkData(workItemID, workItemLinkBean);
		List<LabelValueBean> linkTypes = LinkTypeBL.getLinkTypeNamesList(locale, fromGantt, true);	
		String linkTypePluginClass = null;
		if (workItemLinkBean!=null && workItemLinkBean.getLinkType()!=null) {
			//existing link
			linkTypePluginClass = LinkTypeBL.getLinkTypePluginString(workItemLinkBean.getLinkType());
		} else {
			//new link
			if (!linkTypes.isEmpty()) {
				LabelValueBean labelValueBean = linkTypes.get(0);
				String linkTypeWithDirection = labelValueBean.getValue();
				Integer linkType=MergeUtil.getFieldID(linkTypeWithDirection);
				linkTypePluginClass = LinkTypeBL.getLinkTypePluginString(linkType);
				itemLinkTO.setLinkType(linkType);
				itemLinkTO.setLinkDirection(MergeUtil.getParameterCode(linkTypeWithDirection));
			}
		}
		ILinkType linkType = (ILinkType)PluginManager.getInstance().getPluginClass(PluginManager.LINKTYPE_ELEMENT, linkTypePluginClass);
		String linkTypeJSClass = null;
		String specificData = null;
		if (linkType!=null) {
			linkTypeJSClass = linkType.getLinkTypeJSClass();
			if (linkTypeJSClass!=null) {
				specificData = linkType.getSpecificJSON(workItemLinkBean, personBean, locale);
			}
		}
        Integer linkedWorkItemObjectID = itemLinkTO.getLinkedWorkItemID();
        String linkTypeWithDirection = MergeUtil.mergeKey(itemLinkTO.getLinkType(), itemLinkTO.getLinkDirection());
        return ItemLinkJSON.encodeLink(linkTypeWithDirection, linkTypes, ItemBL.getItemNo(linkedWorkItemObjectID),
                    linkedWorkItemObjectID, itemLinkTO.getLinkedWorkItemTitle(), itemLinkTO.getDescription(), linkTypeJSClass, specificData);
	}
	
	/**
	 * Encode the specific part after changing the link type
	 * @param linkTypeWithDirection
	 * @param workItemID
	 * @param workItemContext
	 * @param linkID
	 * @param personBean
	 * @param locale
	 * @return
	 */
	static String getSpecificPart(String linkTypeWithDirection, Integer workItemID, WorkItemContext workItemContext, Integer linkID, TPersonBean personBean, Locale locale) {
		Integer linkTypeID=MergeUtil.getFieldID(linkTypeWithDirection);
		String linkTypePluginClass = LinkTypeBL.getLinkTypePluginString(linkTypeID);
		ILinkType linkType = (ILinkType)PluginManager.getInstance().getPluginClass(PluginManager.LINKTYPE_ELEMENT, linkTypePluginClass);
		String specificData = null;
		String linkTypeJSClass = null;
		if (linkType!=null) {
			linkTypeJSClass = linkType.getLinkTypeJSClass(); 
			if (linkTypeJSClass!=null) {
				TWorkItemLinkBean workItemLinkBean = ItemLinkConfigBL.getItemLinkBean(linkID, workItemID, workItemContext);;
				specificData = linkType.getSpecificJSON(workItemLinkBean, personBean, locale);
			}
		}
		return ItemLinkJSON.encodeSpecificPart(linkTypeJSClass, specificData);
	}
	
	
	/**
	 * Saves an item link
	 * @param workItemID
	 * @param linkedWorkItemID
	 * @param linkTypeID
	 * @param linkDirection
	 * @param linkID
	 * @param description
	 * @param personBean
	 * @param locale
	 * @param parametersMap
	 * @param workItemContext
	 * @return
	 */
	public static List<ErrorData> saveItemLink(Integer workItemID, Integer linkedWorkItemID,
			Integer linkTypeID, Integer linkDirection,
			Integer linkID, String description, TPersonBean personBean, Locale locale,
			Map<Integer, String> parametersMap, WorkItemContext workItemContext) {
		List<ErrorData> errors = validate(linkTypeID, linkedWorkItemID, locale);
		if (errors!=null && !errors.isEmpty()) {
			return errors;
		}
		//Integer linkType = MergeUtil.getFieldID(linkTypeWithDirection);
		String linkTypePluginString = LinkTypeBL.getLinkTypePluginString(linkTypeID);
		ILinkType linkTypePlugin = (ILinkType)PluginManager.getInstance().getPluginClass(PluginManager.LINKTYPE_ELEMENT, linkTypePluginString);
		//Integer linkDirection = MergeUtil.getParameterCode(linkTypeWithDirection);
		TWorkItemLinkBean workItemLinkBean = ItemLinkConfigBL.getItemLinkBean(linkID, workItemID, workItemContext);
		TWorkItemLinkBean workItemLinkBeanOriginal = null;
		if (workItemID!=null) {
			workItemLinkBeanOriginal = ItemLinkConfigBL.getItemLinkBean(linkID, workItemID, workItemContext);
		}
		if (workItemLinkBean==null) {
			//newly added link
			workItemLinkBean = new TWorkItemLinkBean();
		}
		workItemLinkBean.setLinkType(linkTypeID);
		workItemLinkBean.setDescription(description);
		workItemLinkBean.setLinkDirection(linkDirection);
		if (linkTypePlugin!=null) {
			TWorkItemBean linkedWorkItemBean = null;
			try {
				linkedWorkItemBean = ItemBL.loadWorkItem(linkedWorkItemID);
			} catch (ItemLoaderException e1) {
				
			}
			TWorkItemBean actualWorkItemBean = null;
			SortedMap<Integer, TWorkItemLinkBean> workItemsLinksMap = null;
			if (workItemID==null) {
				if (workItemContext!=null) {
					actualWorkItemBean = workItemContext.getWorkItemBean();
					workItemsLinksMap = workItemContext.getWorkItemsLinksMap();
				}
			} else {
				try {
					actualWorkItemBean = ItemBL.loadWorkItem(workItemID);
				} catch (ItemLoaderException e) {
				}
			}
			TWorkItemBean predecessorItem = null;
			TWorkItemBean successorItem = null;
			int linkTypeDirection = linkTypePlugin.getPossibleDirection();
			if (linkTypeDirection==LINK_DIRECTION.RIGHT_TO_LEFT) {
				//unidirectional inward
				workItemLinkBean.setLinkPred(linkedWorkItemID);
				workItemLinkBean.setLinkSucc(workItemID);
				predecessorItem = linkedWorkItemBean;
				successorItem = actualWorkItemBean;
			} else {
				//unidirectional outward or bidirectional:
				workItemLinkBean.setLinkPred(workItemID);
				workItemLinkBean.setLinkSucc(linkedWorkItemID);
				predecessorItem = actualWorkItemBean;
				successorItem = linkedWorkItemBean;
			}
			errors = new LinkedList<ErrorData>();
			List<ErrorData> unwrapList = linkTypePlugin.unwrapParametersBeforeSave(parametersMap, workItemLinkBean, personBean, locale);
			if (unwrapList!=null && !unwrapList.isEmpty()) {
				//unwrap errors
				errors.addAll(unwrapList);
			}
			List<ErrorData> validationList = linkTypePlugin.validateBeforeSave(workItemLinkBean, workItemLinkBeanOriginal, workItemsLinksMap, predecessorItem, successorItem, personBean, locale);
			
			if (validationList!=null && !validationList.isEmpty()) {
				//link type specific validation
				errors.addAll(validationList);
			}
			if (!errors.isEmpty()) {
				List<ErrorData> doubleValidationList = new LinkedList<ErrorData>();
				for (ErrorData errorData : validationList) {
					//the are two textfields: duplicate the error to mark both as red, but the error text is shown only once 
					ErrorData errorDataNumber = new ErrorData(null);
					errorDataNumber.setFieldName("linkedNumber");
					doubleValidationList.add(errorDataNumber);
					ErrorData errorDataTitle = new ErrorData(errorData.getResourceKey());
					errorDataTitle.setFieldName("linkedWorkItemTitle");
					doubleValidationList.add(errorDataTitle);
				}
				return doubleValidationList;
			}
			if (workItemID==null) {
				//save new item's link in workItemContext 
				ItemLinkBL.saveLinkInContext(workItemContext, workItemLinkBean, linkID);
			} else {
				//save existing item's link in db
				ItemLinkBL.saveLink(workItemLinkBean);
			}
		}
		return null;
	}
	
	/**
	 * Gets the existing (saved in DB or in session) workItemLinkBean by linkID
	 * @param workItemID
	 * @param linkID
	 * @param workItemContext
	 * @return
	 */
	static TWorkItemLinkBean getItemLinkBean(Integer linkID, Integer workItemID, WorkItemContext workItemContext) {
		if (linkID!=null) {
			if (workItemID==null) {
				if (workItemContext!=null) {
					SortedMap<Integer, TWorkItemLinkBean> workItemsLinksMap = workItemContext.getWorkItemsLinksMap();
					if (workItemsLinksMap!=null) {
						return workItemsLinksMap.get(linkID);
					}
				}
			} else {
				return ItemLinkBL.loadByPrimaryKey(linkID);
			}
		}
		return null;
	}
	
	/**
	 * Validate the entry
	 */
	static List<ErrorData> validate(Integer linkTypeID, Integer linkedWorkItemID, Locale locale) {
		List<ErrorData> errors = new ArrayList<ErrorData>();
		if (linkTypeID==null) {
			ErrorData errorData = new ErrorData("common.err.required", "item.tabs.itemLink.lbl.linkType", true);
			errorData.setFieldName("linkTypeWithDirection");
			errors.add(errorData);
		}
		if (linkedWorkItemID==null) {
			//the are two textfields: duplicate the error to mark both as red, but the error text is shown only once for linkedWorkItemTitle
			ErrorData errorData = new ErrorData(null);
			errorData.setFieldName("linkedNumber");
			errors.add(errorData);
			errorData = new ErrorData("common.err.required", "item.tabs.itemLink.lbl.issue", true);
			errorData.setFieldName("linkedWorkItemTitle");
			errors.add(errorData);
		}
		return errors;
	}
	
	/**
	 * Delete the selected links
	 * @param workItemID
	 * @param workItemContext
	 * @param deletedItems
	 */
	static void deleteLinks(Integer workItemID, WorkItemContext workItemContext, String deletedItems) {
		List<Integer> linkIDsToDelete = GeneralUtils.createIntegerListFromStringArr(deletedItems.split(";"));
		if (linkIDsToDelete!=null) {
			for (Integer linkID : linkIDsToDelete) {
				if (workItemID==null) {
					if (workItemContext!=null) {
						SortedMap<Integer, TWorkItemLinkBean> workItemsLinksMap = workItemContext.getWorkItemsLinksMap();
						if (workItemsLinksMap!=null) {
							workItemsLinksMap.remove(linkID);
						}
					}
				} else {
					ItemLinkBL.deleteLink(linkID);
				}
			}
			if (workItemID!=null) {
				ItemLinkSortOrderBL.normalizeSortOrder(workItemID);
			}
		}
	}
}
