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

import com.aurel.track.beans.TListBean;

public interface ListDAO {

	/**
	 * Loads a listBean from the TList table
	 * @param objectID
	 * @return
	 */
	TListBean loadByPrimaryKey(Integer objectID);

	/**
	 * Loads all lists
	 * @return
	 */
	List<TListBean> loadAll();

	/**
	 * Loads a list by name
	 * @return
	 */
	List<TListBean> loadByNameInContext(String name, Integer repositoryType, Integer project);

	/**
	 * Saves a new/existing listBean in the TList table
	 * @param listBean
	 * @return the created optionID
	 */
	Integer save(TListBean listBean);

	/**
	 * Deletes a record from the TList table
	 * @param objectID
	 */
	void delete(Integer objectID);

	/**
	 * Sets the deleted flag for an option
	 * @param flag
	 */
	void setDeleted(Integer listID, boolean flag);

	/**
	 * Gets the child lists of a list
	 * Only one level
	 * @return
	 */
	List<TListBean> getChildLists(Integer listID);

	/**
	 * Gets a listBean with a certain parent and childnumber
	 * @param parentListID
	 * @param childNumber
	 * @return
	 */
	TListBean getChildList(Integer parentListID, Integer childNumber);

	/**
	 * Gets the public lists of specific type: single select or tree select
	 * @param type
	 * @return
	 */
	List<TListBean> getPublicListsOfType(Integer type);

	/**
	 * Gets the project specific lists of specific type: single select or tree select
	 * @param projectIDs
	 * @param type
	 * @return
	 */
	List<TListBean> getProjectListsOfType(List<Integer> projectIDs, Integer type);

	/**
	 * Whether any option from a list is assigned to workItem(s)
	 * @param optionID
	 * @return
	 */
	boolean isListAssignedToWorkitem(Integer listID);



	/**
	 * Whether a list is assigned to config(s)
	 * @param optionID
	 * @return
	 */
	boolean isListAssignedToConfig(Integer listID);

	/**
	 * Get the lists for a project
	 * @param projectID
	 * @param whether to include only the not deleted list
	 * @return
	 */
	List<TListBean> getListsByProject(Integer projectID, boolean onlyNotDeleted);

	/**
	 * Load the public lists
	 * @return
	 */
	List<TListBean> loadPublicLists();


	/**A
	 * Load all lists using fieldIDs list
	 * @param screenID
	 * @return
	 */
	public List<TListBean> loadAllForField(List<Integer> fieldIDs);

	/**
	 * Load by parentID
	 * @param parentID
	 * @return
	 */
	public List<TListBean> loadByParent(Integer parentID);

	/**
	 * Load a Bean by attribute
	 * @param name
	 * @param ListType
	 * @param repositoryType
	 * @param ChildNumber
	 * @return
	 */
	public TListBean loadByAttribute(String name, Integer ListType, Integer repositoryType,
						Integer ChildNumber);
}
