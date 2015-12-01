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


package com.trackplus.dao;

import java.util.List;
import java.util.Map;

import com.trackplus.model.Toption;

public interface OptionDAO {

	/**
	 * Gets a optionBean from the TOption table
	 * 
	 * @param objectID
	 * @return
	 */
	Toption loadByPrimaryKey(Integer objectID);

	/**
	 * Gets an optionBean by label
	 * 
	 * @param list
	 * @param parentID
	 * @param label
	 * @return
	 */
	List<Toption> loadByLabel(Integer list, Integer parentID, String label);

	/**
	 * Gets the optionBeans by uuid list
	 * 
	 * @param uuids
	 * @return
	 */
	List<Toption> loadByUUIDs(List<String> uuids);

	/**
	 * Load all options
	 * 
	 * @return
	 */
	List<Toption> loadAll();

	/**
	 * Load by keys
	 * 
	 * @param objectIDs
	 * @return
	 */
	List<Toption> loadByKeys(Integer[] objectIDs);

	/**
	 * Saves a new/existing optionBean in the TOption table
	 * 
	 * @param listBean
	 * @return the created optionID
	 */
	Integer save(Toption optionBean);

	/**
	 * Deletes an optionBean from the TOption table
	 * 
	 * @param objectID
	 */
	void delete(Integer objectID);

	/**
	 * Deletes all the optionBeans belonging to a list from TOption table
	 * 
	 * @param listID
	 */
	void deleteByList(Integer listID);

	/**
	 * Gets the active optionBean objects from the TOption table by listID,
	 * ordered by sortorder field
	 * 
	 * @param listID
	 * @return
	 */
	List<Toption> loadActiveByListOrderedBySortorder(Integer listID);

	/**
	 * Gets the active optionBean objects from the TOption table by listID and
	 * parentID, ordered by sortorder field Used when two or more lists might
	 * have the same parent
	 * 
	 * @param listID
	 * @param parentID
	 * @return
	 */
	List<Toption> loadActiveByListAndParentOrderedBySortorder(Integer listID,
			Integer parentID);

	/**
	 * Gets the active optionBean objects from the TOption table by listID,
	 * ordered by sortorder field
	 * 
	 * @param listID
	 * @param ascending
	 * @return
	 */
	List<Toption> loadActiveByListOrderedByLabel(Integer listID,
			boolean ascending);

	/**
	 * Gets the active optionBean objects from the TOption table by listID and
	 * parentID, ordered by sortorder field Used when two or more lists might
	 * have the same parent
	 * 
	 * @param listID
	 * @param parentID
	 * @param ascending
	 * @return
	 */
	List<Toption> loadActiveByListAndParentOrderedByLabel(Integer listID,
			Integer parentID, boolean ascending);

	/**
	 * Gets the last used sort order for a listID
	 * 
	 * @param listID
	 * @return
	 */
	Integer getNextSortOrder(Integer listID);

	/**
	 * Gets the last used sort order for a listID and parentID
	 * 
	 * @param listID
	 * @param parentID
	 * @return
	 */
	Integer getNextSortOrder(Integer listID, Integer parentID);

	/**
	 * Gets a list of optionBeans from the TOption table by the parentID
	 * 
	 * @param parentID
	 * @return
	 */
	List<Toption> getOptionsByParent(Integer parentID);

	/**
	 * Gets the list of default optionIDs from the TOption by listID
	 * 
	 * @param listID
	 * @return
	 */
	Integer[] loadDefaultIDsForList(Integer listID);

	/**
	 * Gets the list of default optionIDs from the TOption by listID and
	 * parentID
	 * 
	 * @param listID
	 * @param parentIDs
	 * @return
	 */
	Integer[] loadDefaultIDsForListAndParents(Integer listID,
			Integer[] parentIDs);

	/**
	 * Loads the create datasource for a listID
	 * 
	 * @param listID
	 * @return
	 */
	List<Toption> loadDataSourceByList(Integer listID);

	/**
	 * Loads the create datasource for a listID and a parents
	 * 
	 * @param listID
	 * @param parentIDs
	 * @return
	 */
	List loadCreateDataSourceByListAndParents(Integer listID,
			Integer[] parentIDs);

	/**
	 * Loads the edit datasource for a listID, parents and current selections
	 * 
	 * @param listID
	 * @param parentIDs
	 * @param objectIDs
	 * @return
	 */
	List loadEditDataSourceByListAndParents(Integer listID,
			Integer[] parentIDs, Integer[] objectIDs);

	/**
	 * Whether this option value is assigned to workItem(s)
	 * 
	 * @param optionID
	 * @return
	 */
	boolean hasDependentData(Integer optionID);

	/**
	 * Loads a list of TOptionBeans which are assigned to at least one workItem
	 * for a field and are children of the parentID
	 * 
	 * @param fieldID
	 * @param parameterCode
	 * @param parentID
	 * @return
	 */
	// List loadAssignedOptionsForFieldAndParent(Integer fieldID, Integer

	/**
	 * Get the optionBeans associated with workItems from a project
	 * 
	 * @param projectID
	 * @return
	 */
	/**
	 * Get the optionBeans associated with workItems from a release
	 * 
	 * @return
	 */
	/**
	 * Get the optionBeans associated with workItems the person is manager for
	 * 
	 * @param personID
	 * @return
	 */
	/**
	 * Get the optionBeans associated with workItems the person is responsible
	 * for
	 * 
	 * @param personID
	 * @return
	 */
	/**
	 * Get the optionBeans associated with workItems the person is originator
	 * for
	 * 
	 * @param personID
	 * @return
	 */
	/**
	 * Get the optionBeans associated with workItems the person is manager or
	 * responsible or owner for
	 * 
	 * @param personID
	 * @return
	 */
	/**
	 * Get the optionBeans filtered by the FilterSelectsTO
	 * 
	 * @param filterSelectsTO
	 * @return
	 */
	/**
	 * Get the optionBeans for an array of workItemIDs
	 * 
	 * @param workItemIDs
	 * @return
	 */
	List<Toption> loadLuceneOptions(int[] workItemIDs);

	/**
	 * Get the optionBeans for the history of workItemIDs
	 * 
	 * @param workItemIDs
	 * @param personID
	 * @return
	 */
	Map<Integer, Toption> loadHistoryOptions(int[] workItemIDs/*
															 * , Integer
															 * personID
															 */);

	/**
	 * Returns the sort order column name
	 * 
	 * @return
	 */
	String getSortOrderColumn();

	/**
	 * Returns the table name
	 * 
	 * @return
	 */
	String getTableName();

	/**
	 * returns all options related to the list with the object id
	 * 
	 * @param listObjectId
	 * @return
	 */
	List<Toption> loadByListID(Integer listObjectId);

	/**
	 * A Gets the children of the Option by the parents ID If the parentID is
	 * null, then returns all the Options who does not have any parent
	 * 
	 * @param parentID
	 * @return
	 */
	List<Toption> getChildren(Integer parentID);

	/**
	 * A Load all options for the selected list IDs
	 * 
	 * @param listIds
	 * @return
	 */
	List<Toption> loadForListIDs(List<Integer> listIds);
}
