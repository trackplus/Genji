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

import com.aurel.track.beans.TQueryRepositoryBean;

public interface QueryRepositoryDAO {

	/**
	 * Loads a queryRepositoryBean by primary key
	 * @param objectID
	 * @return
	 */
	TQueryRepositoryBean loadByPrimaryKey(Integer objectID);
	
	/**
	 * Loads a queryRepositoryBean by primary keys
	 * @param objectIDs
	 * @return
	 */
	List<TQueryRepositoryBean> loadByPrimaryKeys(List<Integer> objectIDs);
	
	/**
	 * Loads the hardcoded filters
	 * @return
	 */
	List<TQueryRepositoryBean> loadHardcodedFilters();
	
	/**
	 * Load the filterCategoryBeans by label
	 * @param repository
	 * @param categoryID
	 * @param projectID
	 * @param personID filter by only if set
	 * @param queryType
	 * @param label
	 * @return
	 */
	List<TQueryRepositoryBean> loadByLabel(Integer repository, Integer categoryID,
			Integer projectID, Integer personID, Integer queryType, String label);	
			
	/**
	 * Get the root filters  
	 * @param repository
	 * @param projectID
	 * @param personID
	 * @param queryTypes
	 * @return
	 */
	List<TQueryRepositoryBean> loadRootFilters(Integer repository, Integer projectID, Integer personID, List<Integer> queryTypes);		
	
	/**
	 * Get the root filters for the projects
	 * @param projectIDs
	 * @param queryTypes
	 * @return
	 */
	List<TQueryRepositoryBean> loadProjectRootFilters(List<Integer> projectIDs, List<Integer> queryTypes);		
	
	/**
	 * Loads the queries for a category
	 * @param categoryID
	 * @return
	 */
	List<TQueryRepositoryBean> loadByCategory(Integer categoryID);				
	
	/**
	 * Loads the queries by parent categories
	 * @param categoryIDs
	 * @return
	 */
	List<TQueryRepositoryBean> loadByCategories(List<Integer> categoryIDs);
			 
	/**
	 * Loads a list of queryRepositoryBean
	 * @param queryType report (1) or notification (2)
	 * @param repositoryType private (1), public (2) or project (3)
	 * @param entityID a personID or a projectID depending on the repository type
	 * @return
	 */
	List<TQueryRepositoryBean> loadQueries(Integer queryType, Integer repositoryType, Integer entityID);
	
	/**
	 * Loads a list of private queryRepositoryBean report tree queries which are included in menus 
	 * @return
	 */
	List<TQueryRepositoryBean> loadPrivateReportQueriesInMenu();
		
	/**
	 * Load the queries included in the personal menu for a user
	 * @param personID
	 * @param queryTpes
	 * @param sorted
	 * @return
	 */
	List<TQueryRepositoryBean> loadMenuitemQueries(Integer personID,int[] queryTpes, boolean sorted);
	
	/**
	 * Loads a list of queryRepositoryBeans set for any project: 
	 * for the personID (own) or those not linked to any person (default)   
	 * @param personID
	 * @return
	 */
	List<TQueryRepositoryBean> loadByOwnOrDefaultNotifySettings(Integer personID);
	
	/**
	 * Loads a list of queryRepositoryBeans set for any project 
	 * which is not linked to any person (default)
	 * @return
	 */
	List<TQueryRepositoryBean> loadByDefaultNotifySettings();
	
	/**
	 * Saves a queryRepositoryBean in the database
	 * @param queryRepositoryBean 
	 * @return
	 */
	Integer save(TQueryRepositoryBean queryRepositoryBean);
	
	/**
	 * Deletes a queryRepositoryBean from the database
	 * @param objectID 
	 * @return
	 */
	void delete(Integer objectID);

	/**
	 * Delete all private tree queries of a person
	 * @param personID
	 */
	void deletePrivateTreeQueries(Integer personID);
	
	/**
	 * Returns wheteher a queryRepositoryBean has dependent data
	 * @param objectID 
	 * @return
	 */
	boolean hasDependentData(Integer objectID); 
		
	/**
	 * Replaces all occurences of the oldID with the newID and then deletes the oldID queryRepositoryBean
	 * @param oldID
	 * @param newID
	 * @return
	 */
	void replaceAndDelete(Integer oldID, Integer  newID);
		
}

