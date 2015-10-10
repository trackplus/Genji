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


package com.trackplus.dao;

import java.util.List;

import com.trackplus.model.Tworkitemlink;

/**
 * DAO for WorkItemLink
 * 
 * @author adib
 * 
 */
public interface WorkItemLinkDAO {
	/**
	 * Loads a workItemLinkBean by primary key
	 * 
	 * @param objectID
	 * @return
	 */
	Tworkitemlink loadByPrimaryKey(Integer objectID);

	/**
	 * Loads all workItemLinkBeans
	 * 
	 * @return
	 */
	List<Tworkitemlink> loadAllIndexable();

	/**
	 * Load all successors for an workItem
	 * 
	 * @param itemID
	 * @return
	 */
	List<Tworkitemlink> loadByWorkItemPred(Integer itemID);

	/**
	 * Load all predecessors for an workItem
	 * 
	 * @param itemID
	 * @return
	 */
	List<Tworkitemlink> loadByWorkItemSucc(Integer itemID);

	/**
	 * Loads all unidirectional links between two workItems.
	 * 
	 * @param worItemLinkID
	 * @param linkPred
	 * @param linkSucc
	 * @param linkTypeIDs
	 * @return
	 */
	List<Tworkitemlink> loadLinksOfWorkItems(Integer worItemLinkID,
			Integer linkPred, Integer linkSucc, List<Integer> linkTypeIDs);

	/**
	 * Load the directly linked workItems
	 * 
	 * @param workItemIDChunk
	 *            base set of workItemIDs
	 * @param linkTypes
	 *            the link type to look for
	 * @param workItemsArePred
	 * @param direction
	 * @param archived
	 * @param deleted
	 * @return
	 */
	List<Tworkitemlink> getWorkItemsOfDirection(int[] workItemIDChunk,
			List<Integer> linkTypes, boolean workItemsArePred,
			Integer direction, Integer archived, Integer deleted);

	/**
	 * Load all linked workItems for a list of workItemIDs
	 * 
	 * @param workItemIDs
	 *            base set of workItemIDs
	 * @return
	 */
	List<Tworkitemlink> loadByWorkItems(int[] workItemIDs);

	/**
	 * Gets the workItem links of a workItem for linkTypes, column and direction
	 * 
	 * @param workItemID
	 * @param linkType
	 * @param column
	 * @param direction
	 * @return
	 */
	List<Tworkitemlink> getWorkItemLinks(Integer workItemID,
			Integer[] linkType, Integer direction);

	/**
	 * Saves a new/existing workItemLinkBean
	 * 
	 * @param workItemLinkBean
	 * @return the created optionID
	 */
	Integer save(Tworkitemlink workItemLinkBean);

	/**
	 * Deletes a workItemLinkBean
	 * 
	 * @param objectID
	 */
	void delete(Integer objectID);
}
