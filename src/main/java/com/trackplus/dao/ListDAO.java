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

import com.trackplus.model.Tlist;

public interface ListDAO {

	/**
	 * Loads a listBean from the TList table
	 * 
	 * @param objectID
	 * @return
	 */
	Tlist loadByPrimaryKey(Integer objectID);

	/**
	 * Loads all lists
	 * 
	 * @return
	 */
	List<Tlist> loadAll();

	/**
	 * Loads a list by name
	 * 
	 * @return
	 */
	List<Tlist> loadByNameInContext(String name, Integer repositoryType,
			Integer project);

	/**
	 * Saves a new/existing listBean in the TList table
	 * 
	 * @param listBean
	 * @return the created optionID
	 */
	Integer save(Tlist listBean);

	/**
	 * Deletes a record from the TList table
	 * 
	 * @param objectID
	 */
	void delete(Integer objectID);

	/**
	 * Sets the deleted flag for an option
	 * 
	 * @param flag
	 */
	void setDeleted(Integer listID, boolean flag);

	/**
	 * Gets the child lists of a list Only one level
	 * 
	 * @return
	 */
	List<Tlist> getChildLists(Integer listID);

	/**
	 * Gets a listBean with a certain parent and childnumber
	 * 
	 * @param parentListID
	 * @param childNumber
	 * @return
	 */
	Tlist getChildList(Integer parentListID, Integer childNumber);

	/**
	 * Gets the public lists of specific type: single select or tree select
	 * 
	 * @param type
	 * @return
	 */
	List<Tlist> getPublicListsOfType(Integer type);

	/**
	 * Gets the project specific lists of specific type: single select or tree
	 * select
	 * 
	 * @param projectID
	 * @param type
	 * @return
	 */
	List<Tlist> getProjectListsOfType(Integer projectID, Integer type);

	/**
	 * Whether any option from a list is assigned to workItem(s)
	 * 
	 * @param optionID
	 * @return
	 */
	boolean isListAssignedToWorkitem(Integer listID);

	/**
	 * Whether any option from a list is assigned to history entry
	 * 
	 * @param listID
	 * @param newValue
	 * @return
	 */
	boolean isListAssignedToHistoryEntry(Integer listID, boolean newValue);

	/**
	 * Whether a list is assigned to config(s)
	 * 
	 * @param optionID
	 * @return
	 */
	boolean isListAssignedToConfig(Integer listID);

	/**
	 * Get the lists for a project
	 * 
	 * @param projectID
	 * @return
	 */
	List<Tlist> getListsByProject(Integer projectID);

	/**
	 * Load the lists for projects
	 * 
	 * @param projectIDs
	 * @return
	 */
	/**
	 * Load the public lists
	 * 
	 * @return
	 */
	List<Tlist> loadPublicLists();

	/**
	 * A Load all lists using fieldIDs list
	 * 
	 * @param screenID
	 * @return
	 */
	public List<Tlist> loadAllForField(List<Integer> fieldIDs);

	/**
	 * A Load by parentID
	 * 
	 * @param parentID
	 * @return
	 */
	public List<Tlist> loadByParent(Integer parentID);

	/**
	 * Load a Bean by attribute
	 * 
	 * @param name
	 * @param ListType
	 * @param repositoryType
	 * @param ChildNumber
	 * @return
	 */
	public Tlist loadByAttribute(String name, Integer ListType,
			Integer repositoryType, Integer ChildNumber);
}
