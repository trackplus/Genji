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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.WorkItemLinkDAO;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.SortOrderUtil;

/**
 * Business logic class for reordering item links
 * @author Tamas
 *
 */
public class ItemLinkSortOrderBL {

	private static WorkItemLinkDAO workItemLinkDAO=DAOFactory.getFactory().getWorkItemLinkDAO();
	
	/***********************************Reorder links for an existing item*******************************************/
	/**
	 * Gets the sort order column name 
	 * @return
	 */
	public static String getSortOrderColumn() {
		return workItemLinkDAO.getSortOrderColumn();
	}
	
	/**
	 * Returns the table name
	 * @return
	 */
	public static String getTableName() {
		return workItemLinkDAO.getTableName();
	}
	
	/**
	 * Normalize the sort order for the list: removes the holes between the sortOrder fields
	 * @param sortedBeans
	 * @return 
	 */
	static List<TWorkItemLinkBean> normalizeSortOrder(Integer workItemID) {
		List<TWorkItemLinkBean> successorsForMeAsPredecessor = ItemLinkBL.getSuccessorsForMeAsPredecessor(workItemID);
		List<TWorkItemLinkBean> predecessorsForMeAsSuccessor = ItemLinkBL.getPredecessorsForMeAsSuccessor(workItemID);
		List<TWorkItemLinkBean> workItemLinkBeans = ItemLinkBL.getLinks(successorsForMeAsPredecessor, predecessorsForMeAsSuccessor);
		if (workItemLinkBeans!=null) {
			for (int i = 0; i < workItemLinkBeans.size(); i++) {
				TWorkItemLinkBean workItemLinkBean = workItemLinkBeans.get(i);
				Integer sortOrder = workItemLinkBean.getSortorder();
				if (sortOrder==null || sortOrder.intValue()!=i+1) {
					workItemLinkBean.setSortorder(Integer.valueOf(i+1));
					ItemLinkBL.saveLink(workItemLinkBean);
				}
			}
		}
		return workItemLinkBeans;
	}

	/**
	 * Sets the sortOrder on the affected links after a drag and drop operation
	 * @param draggedID
	 * @param droppedToID
	 * @param before
	 */
	static synchronized void dragAndDrop(Integer workItemID, String draggedLinkIDs, Integer droppedToID, boolean before) {
		List<Integer> draggedIDs = GeneralUtils.createIntegerListFromStringArr(draggedLinkIDs.split(","));
		if (draggedIDs!=null && !draggedIDs.isEmpty()) {
			List<TWorkItemLinkBean> sortedWorkItemLinks = ItemLinkSortOrderBL.normalizeSortOrder(workItemID);
			draggedIDs = reorderDragged(sortedWorkItemLinks, draggedIDs);
			dropNear(workItemID, draggedIDs, droppedToID, before);
		}
	}
	
	/**
	 * Move up the links
	 * @param workItemID
	 * @param draggedNodeID
	 */
	static void moveUp(Integer workItemID, String draggedLinkIDs) {
		List<Integer> draggedIDs = GeneralUtils.createIntegerListFromStringArr(draggedLinkIDs.split(","));
		if (draggedIDs!=null && !draggedIDs.isEmpty()) {
			List<TWorkItemLinkBean> sortedWorkItemLinks = ItemLinkSortOrderBL.normalizeSortOrder(workItemID);
			draggedIDs = reorderDragged(sortedWorkItemLinks, draggedIDs);
			Integer firstDraggedLinkID = draggedIDs.get(0);
			TWorkItemLinkBean previousItemLinkBean = null;
			boolean found = false;
			for (TWorkItemLinkBean workItemLinkBean : sortedWorkItemLinks) {
				if (workItemLinkBean.getObjectID().equals(firstDraggedLinkID)) {
					found = true;
					break;
				}
				previousItemLinkBean = workItemLinkBean;
			}
			if (found && previousItemLinkBean!=null) {
				dropNear(workItemID, draggedIDs, previousItemLinkBean.getObjectID(), true);
			}
		}
	}
	
	/**
	 * Move down the links
	 * @param workItemID
	 * @param draggedNodeID
	 */
	static void moveDown(Integer workItemID, String draggedLinkIDs) {
		List<Integer> draggedIDs = GeneralUtils.createIntegerListFromStringArr(draggedLinkIDs.split(","));
		if (draggedIDs!=null && !draggedIDs.isEmpty()) {
			List<TWorkItemLinkBean> sortedWorkItemLinks = ItemLinkSortOrderBL.normalizeSortOrder(workItemID);
			draggedIDs = reorderDragged(sortedWorkItemLinks, draggedIDs);
			Integer lastDraggedLinkID = draggedIDs.get(draggedIDs.size()-1);
			TWorkItemLinkBean nextItemLinkBean = null;
			for (Iterator<TWorkItemLinkBean> iterator = sortedWorkItemLinks.iterator(); iterator.hasNext();) {
				TWorkItemLinkBean itemLinkBean = iterator.next();
				if (itemLinkBean.getObjectID().equals(lastDraggedLinkID)) {
					if (iterator.hasNext()) {
						nextItemLinkBean = iterator.next();
					}
					break;
				}
				
			}
			if (nextItemLinkBean!=null) {
				dropNear(workItemID, draggedIDs, nextItemLinkBean.getObjectID(), false);
			}
		}
	}
	
	/**
	 * Reorder the dragged items because they are sent in the order of selection not the sort order 
	 * @param sortedWorkItemLinks
	 * @param draggedIDs
	 */
	private static List<Integer> reorderDragged(List<TWorkItemLinkBean> sortedWorkItemLinks, List<Integer> draggedIDs) {
		List<Integer> reorderedIDs = new ArrayList<Integer>(draggedIDs.size());
		for (TWorkItemLinkBean workItemLinkBean : sortedWorkItemLinks) {
			Integer linkID = workItemLinkBean.getObjectID();
			if (draggedIDs.contains(linkID)) {
				reorderedIDs.add(linkID);
			}
		}
		return reorderedIDs;
	}
	
	/**
	 * Sets the sortOrder on the affected links after a drag and drop operation
	 * @param draggedID
	 * @param droppedToID
	 * @param before
	 */
	private static synchronized void dropNear(Integer workItemID, List<Integer> draggedIDs, Integer droppedToID, boolean before) {
		TWorkItemLinkBean droppedToBean =  ItemLinkBL.loadByPrimaryKey(droppedToID);
		if (draggedIDs!=null && !draggedIDs.isEmpty()) {
			for (Integer draggedID : draggedIDs) {
				//load the dragged links one by one because SortOrderUtil.dropNear() modifies the sort order of this bean also
				TWorkItemLinkBean draggedBean = ItemLinkBL.loadByPrimaryKey(draggedID);
				Integer draggedSortOrder =  draggedBean.getSortorder();
				Integer droppedToSortOrder = droppedToBean.getSortorder();
				String sortOrderColumn = getSortOrderColumn();
				String tabelName = getTableName();
				Integer newSortOrder = SortOrderUtil.dropNear(draggedSortOrder, droppedToSortOrder,
						sortOrderColumn, tabelName, getSpecificCriteria(workItemID), before);
				draggedBean.setSortorder(newSortOrder);
				ItemLinkBL.saveLink(draggedBean);
				before = false;
				droppedToBean = draggedBean;
			}
		}
	}
	
	/**
	 * Gets the specific extra constraints
	 * @param systemStateBean
	 * @return
	 */
	private static String getSpecificCriteria(Integer workItemID) {
		return " AND (LINKSUCC=" + workItemID + " OR LINKPRED="+workItemID + ")";
	}
	
	/***********************************Reorder links for a new item*******************************************/
	/**
	 * Reorder the dragged items because they are sent in the order of selection not the sort order 
	 * @param sortedWorkItemLinks
	 * @param draggedIDs
	 */
	private static List<Integer> reorderDragged(SortedMap<Integer, TWorkItemLinkBean> workItemLinksMap, List<Integer> draggedIDs) {
		List<Integer> reorderedIDs = new ArrayList<Integer>(draggedIDs.size());
		for (Integer sortOrder : workItemLinksMap.keySet()) {
			if (draggedIDs.contains(sortOrder)) {
				reorderedIDs.add(sortOrder);
			}
		}
		return reorderedIDs;
	}

	/**
	 * Sets the sortOrder on the affected links after a drag and drop operation
	 * @param draggedID
	 * @param droppedToID
	 * @param before
	 */
	static synchronized void dragAndDrop(SortedMap<Integer, TWorkItemLinkBean> workItemLinksMap, String draggedLinkIDs, Integer droppedToID, boolean before) {
		List<Integer> draggedSortOrders = GeneralUtils.createIntegerListFromStringArr(draggedLinkIDs.split(","));
		if (draggedSortOrders!=null && !draggedSortOrders.isEmpty()) {
			draggedSortOrders = reorderDragged(workItemLinksMap, draggedSortOrders);
			dropNear(workItemLinksMap, draggedSortOrders, droppedToID, before);
		}
	}
	
	static void moveUp(SortedMap<Integer, TWorkItemLinkBean> workItemLinksMap, String draggedLinkIDs) {
		List<Integer> draggedIDs = GeneralUtils.createIntegerListFromStringArr(draggedLinkIDs.split(","));
		if (draggedIDs!=null && !draggedIDs.isEmpty()) {
			draggedIDs = reorderDragged(workItemLinksMap, draggedIDs);
			Integer firstDraggedLinkID = draggedIDs.get(0);
			Integer previousSortOrder = null;
			boolean found = false;
			for (Map.Entry<Integer, TWorkItemLinkBean> entry : workItemLinksMap.entrySet()) {
				Integer sortOrder = entry.getKey();
				if (sortOrder.equals(firstDraggedLinkID)) {
					found = true;
					break;
				}
				previousSortOrder = sortOrder;
			}
			if (found && previousSortOrder!=null) {
				dropNear(workItemLinksMap, draggedIDs, previousSortOrder, true);
			}
		}
	}
	
	static void moveDown(SortedMap<Integer, TWorkItemLinkBean> workItemLinksMap, String draggedLinkIDs) {
		List<Integer> draggedIDs = GeneralUtils.createIntegerListFromStringArr(draggedLinkIDs.split(","));
		if (draggedIDs!=null && !draggedIDs.isEmpty()) {
			draggedIDs = reorderDragged(workItemLinksMap, draggedIDs);
			Integer lastDraggedLinkID = draggedIDs.get(draggedIDs.size()-1);
			Integer nextSortOrder = null;
			for (Iterator<Map.Entry<Integer, TWorkItemLinkBean>> iterator = workItemLinksMap.entrySet().iterator(); iterator.hasNext();) {
				Map.Entry<Integer, TWorkItemLinkBean> entry = iterator.next();
				Integer sortOrder = entry.getKey();
				if (sortOrder.equals(lastDraggedLinkID)) {
					if (iterator.hasNext()) {
						nextSortOrder = iterator.next().getKey();
					}
					break;
				}
			}
			if (nextSortOrder!=null) {
				dropNear(workItemLinksMap, draggedIDs, nextSortOrder, false);
			}
		}
	}
	
	
	/**
	 * Sets the sortOrder on the affected links after a drag and drop operation
	 * @param draggedID
	 * @param droppedToID
	 * @param before
	 */
	static synchronized void dropNear(SortedMap<Integer, TWorkItemLinkBean> workItemLinksMap, List<Integer> draggedSortOrders, Integer droppedToSortOrder, boolean before) {
		if (workItemLinksMap!=null && draggedSortOrders!=null && droppedToSortOrder!=null) {
			boolean moveDown = draggedSortOrders.get(0).intValue()<droppedToSortOrder.intValue();
			int i = 0;
			for (Integer draggedSortOrder : draggedSortOrders) {
				SortOrderUtil.dropNear(draggedSortOrder - i, droppedToSortOrder,
						before, (SortedMap)workItemLinksMap);
				before = false;
				if (moveDown) {
					//if move down the sortOrder of the other selected links below is recalculated after each SortOrderUtil.dropNear()
					//so it should be actualized to catch the modified sortOrder to move down
					//if move up the sort order of the other selected links is not influenced by SortOrderUtil.dropNear
					i++;
				}
				//droppedToSortOrder = droppedToSortOrder+1;
			}
		}
	}
}
