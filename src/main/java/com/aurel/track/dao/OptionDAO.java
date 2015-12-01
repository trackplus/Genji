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


package com.aurel.track.dao;

import java.util.List;
import java.util.Map;

import com.aurel.track.beans.TOptionBean;


public interface OptionDAO {
	
	/**
	 * Gets a optionBean from the TOption table
	 * @param objectID
	 * @return
	 */
	TOptionBean loadByPrimaryKey(Integer objectID);
	
	/**
	 * Gets an optionBean by label
	 * @param list
	 * @param parentID
	 * @param label
	 * @return
	 */
	List<TOptionBean> loadByLabel(Integer list, Integer parentID, String label);
	
	
	/**
	 * Gets the optionBeans by uuid list
	 * @param uuids
	 * @return
	 */
	List<TOptionBean> loadByUUIDs(List<String> uuids);
	
	/**
	 * Load all options
	 * @return
	 */
	List<TOptionBean> loadAll();
	
	/**
	 * Load by keys
	 * @param objectIDs
	 * @return
	 */
	List<TOptionBean> loadByKeys(Integer[] objectIDs);
	
	/**
	 * Saves a new/existing optionBean in the TOption table
	 * @param optionBean
	 * @return the created optionID
	 */
	Integer save(TOptionBean optionBean);
	
	/**
	 * Deletes an optionBean from the TOption table
	 * @param objectID
	 */
	void delete(Integer objectID);
	
	/**
	 * Deletes all the optionBeans belonging to a list from TOption table
	 * @param listID
	 */
	void deleteByList(Integer listID);
	
	/**
	 * Gets the active optionBean objects from the TOption table 
	 * by listID, ordered by sortorder field  
	 * @param listID
	 * @return
	 */
	List<TOptionBean> loadActiveByListOrderedBySortorder(Integer listID);
		
	/**
 	 * Gets the active optionBean objects from the TOption table 
	 * by listID and parentID, ordered by sortorder field  
	 * Used when two or more lists might have the same parent
	 * @param listID
	 * @param parentID
	 * @return
	 */
	List<TOptionBean> loadActiveByListAndParentOrderedBySortorder(Integer listID, Integer parentID);

	/**
	 * Gets the active optionBean objects from the TOption table 
	 * by listID, ordered by sortorder field  
	 * @param listID
	 * @param ascending
	 * @return
	 */
	List<TOptionBean> loadActiveByListOrderedByLabel(Integer listID, boolean ascending);
		
	/**
 	 * Gets the active optionBean objects from the TOption table 
	 * by listID and parentID, ordered by sortorder field  
	 * Used when two or more lists might have the same parent
	 * @param listID
	 * @param parentID
	 * @param ascending
	 * @return
	 */
	List<TOptionBean> loadActiveByListAndParentOrderedByLabel(Integer listID, Integer parentID, boolean ascending);
	
	/**
	 * Gets the last used sort order for a listID
	 * @param listID
	 * @return
	 */
	Integer getNextSortOrder(Integer listID);
	
	/**
	 * Gets the last used sort order for a listID and parentID 
	 * @param listID
	 * @param parentID
	 * @return
	 */
	Integer getNextSortOrder(Integer listID, Integer parentID);
	
	
	/**
	 * Gets a list of optionBeans from the TOption table by the parentID
	 * @param parentID
	 * @return
	 */
	List<TOptionBean> getOptionsByParent(Integer parentID);
	

	/**
	 * Gets the list of default optionIDs from the TOption by listID 
	 * @param listID
	 * @return
	 */
	Integer[] loadDefaultIDsForList(Integer listID);

	/**
	 * Gets the list of default optionIDs from the TOption by listID and parentID
	 * @param listID
	 * @param parentIDs
	 * @return
	 */
	Integer[] loadDefaultIDsForListAndParents(Integer listID, Integer[] parentIDs);
	
	/**
	 * Loads the create datasource for a listID
	 * @param listID
	 * @return
	 */
	List<TOptionBean> loadDataSourceByList(Integer listID);
	
	/**
	 * Loads the create datasource for a listID and a parents
	 * @param listID
	 * @param parentIDs
	 * @return
	 */
	List loadCreateDataSourceByListAndParents(Integer listID, Integer[] parentIDs);

	/**
	 * Loads the edit datasource for a listID, parents and current selections
	 * @param listID
	 * @param parentIDs
	 * @param objectIDs
	 * @return
	 */
	List loadEditDataSourceByListAndParents(Integer listID, Integer[] parentIDs, Integer[] objectIDs);
	
	/**
	 * Whether this option value is assigned to workItem(s)
	 * @param optionID
	 * @return
	 */
	boolean hasDependentData(Integer optionID);
	
	/**
	 * Get the optionBeans for an array of workItemIDs 
	 * @param workItemIDs
	 * @return
	 */
	List<TOptionBean> loadLuceneOptions(int[] workItemIDs);
	
	/**
	 * Get the optionBeans for the history of workItemIDs
	 * @param workItemIDs
	 * @param personID
	 * @return
	 */
	Map<Integer, TOptionBean> loadHistoryOptions(int[] workItemIDs);
	
	/**
	 * Returns the sort order column name
	 * @return
	 */
	String getSortOrderColumn();
	
	/**
	 * Returns the table name
	 * @return
	 */
	String getTableName();
	
	/**
	 * returns all options related to the list with the object id
	 * @param listObjectId
	 * @return
	 */
	List<TOptionBean> loadByListID(Integer listObjectId);

	/**A
	 * Gets the children of the Option by the parents ID
	 * If the parentID is null, then returns all the Options who does not have any parent
	 * @param parentID
	 * @return 
	 */
	List<TOptionBean> getChildren(Integer parentID);
	
	/**
	 * Load all options for the selected list IDs
	 * @param listIds
	 * @return
	 */
	List<TOptionBean> loadForListIDs(List<Integer> listIds);
	
	/**
	 * Whether any option from a list is assigned to history entry
	 * @param listID
	 * @param newValue
	 * @return
	 */
	boolean isListAssignedToHistoryEntry(Integer listID, boolean newValue);
}
