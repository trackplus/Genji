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


package com.aurel.track.dao;

import java.util.List;
import java.util.Locale;

import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.RACIBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.errors.ErrorData;

/**
 * DAO for WorkItemLink
 * @author adib
 *
 */
public interface WorkItemLinkDAO {
	/**
	 * Loads a workItemLinkBean by primary key
	 * @param objectID
	 * @return 
	 */
	TWorkItemLinkBean loadByPrimaryKey(Integer objectID);
	
	/**
	 * Loads all workItemLinkBeans
	 * @return 
	 */
	List<TWorkItemLinkBean> loadAllIndexable();
	
	/**
	 * Loads a workItemLinkBean list by ids
	 * @param linkIDs
	 * @return
	 */
	List<TWorkItemLinkBean> loadByIDs(List<Integer> linkIDs);
	
	/**
	 * Loads all workItemLinkBeans by link types and direction
	 * @param linkTypeIDs
	 * @param linkDirection
	 * @return 
	 */
	List<TWorkItemLinkBean> loadByLinkTypeAndDirection(List<Integer> linkTypeIDs, Integer linkDirection);
	
	/**
	 * Load all successors for an workItem
	 * @param itemID
	 * @return
	 */
	List<TWorkItemLinkBean> loadByWorkItemPred(Integer itemID);
	
	/**
	 * Load all predecessors for an workItem
	 * @param itemID
	 * @return
	 */
	List<TWorkItemLinkBean> loadByWorkItemSucc(Integer itemID);

	/**
	 * Load all successors for an workItem for a link type
	 * @param itemID
	 * @param linkType
	 * @return
	 */
	List<TWorkItemLinkBean> loadByPredAndLinkType(Integer itemID, Integer linkType, Integer direction);
	
	/**
	 * Load all predecessors for an workItem  for a link type
	 * @param itemID
	 * @param linkType
	 * @return
	 */
	List<TWorkItemLinkBean> loadBySuccAndLinkType(Integer itemID, Integer linkType, Integer direction);
	
	/**
	 * Count all links of a workItem
	 * @param itemID
	 * @return
	 */
	int countByWorkItem(Integer itemID);
	
	/**
	 * Loads all unidirectional links between two workItems.
	 * @param worItemLinkID  
	 * @param linkPred
	 * @param linkSucc
	 * @param linkTypeIDs
	 * @return
	 */
	List<TWorkItemLinkBean> loadLinksOfWorkItems(Integer worItemLinkID, Integer linkPred, Integer linkSucc, List<Integer> linkTypeIDs);
	
	/**
	 * Load the directly linked workItems
	 * @param predChunk
	 * @param succChunk
	 * @param linkType
	 * @param direction
	 * @return
	 */
	List<TWorkItemLinkBean> getLinkedItems(int[] predChunk, int[] succChunk, Integer linkType, Integer direction);
	
	/**
	 * Load the directly linked workItems
	 * @param workItemIDChunk base set of workItemIDs
	 * @param linkTypes the link type to look for
	 * @param workItemsArePred
	 * @param direction
	 * @param archived
	 * @param deleted
	 * @return
	 */
	List<TWorkItemLinkBean> getWorkItemsOfDirection(int[] workItemIDChunk, 
			List<Integer> linkTypes, boolean workItemsArePred, Integer direction, Integer archived, Integer deleted);	
	
	/**
	 * Load all linked workItems for a list of workItemIDs
	 * @param workItemIDs base set of workItemIDs 
	 * @return
	 */
	List<TWorkItemLinkBean> loadByWorkItems(int[] workItemIDs);
	
	/**
	 * Gets the links filtered by filterSelectsTO and raciBean
	 * @param filterUpperTO
	 * @param raciBean
	 * @param personID
	 * @return
	 */
	List<TWorkItemLinkBean> loadTreeFilterLinks(FilterUpperTO filterUpperTO, RACIBean raciBean, Integer personID);
	
	/**
	 * Get the links for a TQL expression
	 * @param tqlExpression
	 * @param personBean
	 * @param locale
	 * @param errors
	 * @return
	 */
	List<TWorkItemLinkBean> loadTQLFilterLinks(String tqlExpression, TPersonBean personBean, Locale locale, List<ErrorData> errors);
	
	/**
	 * Gets the sort order column name 
	 * @return
	 */
	String getSortOrderColumn();
	
	/**
	 * Returns the table name
	 * @return
	 */
	String getTableName();
	
	/**
	 * Saves a new/existing workItemLinkBean
	 * @param workItemLinkBean
	 * @return the created optionID
	 */
	Integer save(TWorkItemLinkBean workItemLinkBean);
	
	/**
	 * Deletes a workItemLinkBean
	 * @param objectID
	 */
	void delete(Integer objectID);
	
	
}
