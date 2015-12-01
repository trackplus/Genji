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

import com.trackplus.model.Tfiltercategory;

public interface FilterCategoryDAO {

	/**
	 * Loads a filterCategoryBean by primary key
	 * 
	 * @param objectID
	 * @return
	 */
	Tfiltercategory loadByPrimaryKey(Integer objectID);

	/**
	 * Loads the filterCategoryBeans by keys
	 * 
	 * @param categoryIDs
	 * @return
	 */
	List<Tfiltercategory> loadByKeys(List<Integer> categoryIDs);

	/**
	 * Load the filterCategoryBeans by label
	 * 
	 * @param repository
	 * @param filterType
	 * @param parentID
	 * @param projectID
	 * @param personID
	 *            filter by only if set
	 * @param label
	 * @return
	 */
	List<Tfiltercategory> loadByLabel(Integer repository, Integer filterType,
			Integer parentID, Integer projectID, Integer personID, String label);

	/**
	 * Get the root categories
	 * 
	 * @param repository
	 * @param filterType
	 * @param projectID
	 * @param personID
	 * @return
	 */
	List<Tfiltercategory> loadRootCategories(Integer repository,
			Integer filterType, Integer projectID, Integer personID);

	/**
	 * Get the root categories for projects
	 * 
	 * @param projectIDs
	 * @param filterType
	 * @return
	 */
	List<Tfiltercategory> loadProjectRootCategories(List<Integer> projectIDs,
			Integer filterType);

	/**
	 * Loads filterCategoryBeans by repository
	 * 
	 * @param repository
	 * @param personID
	 * @return
	 */
	/*
	 * List<Tfiltercategory> loadByRepositoryPersonProjects(> Integer
	 * repository, Integer personID, List<Integer> projects);
	 */

	/**
	 * Loads filterCategoryBeans by parent
	 * 
	 * @param parentID
	 * @return
	 */
	List<Tfiltercategory> loadByParent(Integer parentID);

	/**
	 * Loads a filterCategoryBeans by parentIDs
	 * 
	 * @param parentIDs
	 * @return
	 */
	List<Tfiltercategory> loadByParents(List<Integer> parentIDs);

	/**
	 * Saves a filterCategoryBean in the database
	 * 
	 * @param queryRepositoryBean
	 * @return
	 */
	Integer save(Tfiltercategory filterCategoryBean);

	/**
	 * Whether the category is empty or has content
	 * 
	 * @param objectID
	 * @return
	 * @throws TrackDAOException
	 */
	boolean hasDependenData(Integer objectID);

	/**
	 * Deletes a filterCategoryBean from the database
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
	void deletePrivateFilterCategories(Integer personID);
}
