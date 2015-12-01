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

import com.trackplus.model.Tqueryrepository;

public interface QueryRepositoryDAO {

	/**
	 * Loads a queryRepositoryBean by primary key
	 * 
	 * @param objectID
	 * @return
	 */
	Tqueryrepository loadByPrimaryKey(Integer objectID);

	/**
	 * Load the filterCategoryBeans by label
	 * 
	 * @param repository
	 * @param categoryID
	 * @param projectID
	 * @param personID
	 *            filter by only if set
	 * @param queryType
	 * @param label
	 * @return
	 */
	List<Tqueryrepository> loadByLabel(Integer repository, Integer categoryID,
			Integer projectID, Integer personID, Integer queryType, String label);

	/**
	 * Get the root filters
	 * 
	 * @param repository
	 * @param projectID
	 * @param personID
	 * @param queryTypes
	 * @return
	 */
	List<Tqueryrepository> loadRootFilters(Integer repository,
			Integer projectID, Integer personID, List<Integer> queryTypes);

	/**
	 * Get the root filters for the projects
	 * 
	 * @param projectIDs
	 * @param queryTypes
	 * @return
	 */
	List<Tqueryrepository> loadProjectRootFilters(List<Integer> projectIDs,
			List<Integer> queryTypes);

	/**
	 * Loads the queries for a category
	 * 
	 * @param categoryID
	 * @return
	 */
	List<Tqueryrepository> loadByCategory(Integer categoryID);

	/**
	 * Loads the queries by parent categories
	 * 
	 * @param categoryIDs
	 * @return
	 */
	List<Tqueryrepository> loadByCategories(List<Integer> categoryIDs);

	/**
	 * Loads a list of queryRepositoryBean
	 * 
	 * @param queryType
	 *            report (1) or notification (2)
	 * @param repositoryType
	 *            private (1), public (2) or project (3)
	 * @param entityID
	 *            a personID or a projectID depending on the repository type
	 * @return
	 */
	List<Tqueryrepository> loadQueries(Integer queryType,
			Integer repositoryType, Integer entityID);

	/**
	 * Loads a list of private queryRepositoryBean report tree queries which are
	 * included in menus
	 * 
	 * @return
	 */
	List<Tqueryrepository> loadPrivateReportQueriesInMenu();

	/**
	 * Load the queries included in the personal menu for a user
	 * 
	 * @param personID
	 * @return
	 */
	List<Tqueryrepository> loadMenuitemQueries(Integer personID);

	public List<Tqueryrepository> loadMenuitemQueries(Integer personID,
			int[] queryTYpes, boolean sorted);

	/**
	 * Loads a list of queryRepositoryBeans set for any project: for the
	 * personID (own) or those not linked to any person (default)
	 * 
	 * @param personID
	 * @return
	 */
	List<Tqueryrepository> loadByOwnOrDefaultNotifySettings(Integer personID);

	/**
	 * Loads a list of queryRepositoryBeans set for any project which is not
	 * linked to any person (default)
	 * 
	 * @return
	 */
	List<Tqueryrepository> loadByDefaultNotifySettings();

	/**
	 * Saves a queryRepositoryBean in the database
	 * 
	 * @param queryRepositoryBean
	 * @return
	 */
	Integer save(Tqueryrepository queryRepositoryBean);

	/**
	 * Deletes a queryRepositoryBean from the database
	 * 
	 * @param objectID
	 * @return
	 */
	void delete(Integer objectID);

	/**
	 * Delete all private tree queries of a person
	 * 
	 * @param personID
	 */
	void deletePrivateTreeQueries(Integer personID);

	/**
	 * Returns wheteher a queryRepositoryBean has dependent data
	 * 
	 * @param objectID
	 * @return
	 */
	boolean hasDependentData(Integer objectID);

	/**
	 * Replaces all occurences of the oldID with the newID and then deletes the
	 * oldID queryRepositoryBean
	 * 
	 * @param oldID
	 * @param newID
	 * @return
	 */
	void replaceAndDelete(Integer oldID, Integer newID);

}
