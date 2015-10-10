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

import com.aurel.track.beans.TReportCategoryBean;

public interface ReportCategoryDAO {

	/**
	 * Loads a reportCategoryBean by primary key
	 * @param objectID
	 * @return
	 */
	TReportCategoryBean loadByPrimaryKey(Integer objectID);
	
	/**
	 * Load the reportCategoryBean by label
	 * @param repository
	 * @param parentID
	 * @param projectID
	 * @param personID filter by only if set
	 * @param label
	 * @return
	 */
	List<TReportCategoryBean> loadByLabel(Integer repository, Integer parentID, Integer projectID, Integer personID, String label);
	
	/**
	 * Get the root categories  
	 * @param repository
	 * @param projectID
	 * @param personID
	 * @return
	 */
	List<TReportCategoryBean> loadRootCategories(Integer repository, Integer projectID, Integer personID);
		
	/**
	 * Get the root categories for projects
	 * @param projectIDs
	 * @return
	 */
	List<TReportCategoryBean> loadProjectRootCategories(List<Integer> projectIDs);
	
	/**
	 * Loads reportCategoryBeans by repository
	 * @param repository
	 * @param personID
	 * @return
	 */
	List<TReportCategoryBean> loadByRepositoryPersonProjects(
			Integer repository, Integer personID, List<Integer> projects);
	
	/**
	 * Loads filterCategoryBeans by parent
	 * @param parentID
	 * @return
	 */
	List<TReportCategoryBean> loadByParent(Integer parentID);
	
	/**
	 * Loads a reportCategoryBeans by parentIDs
	 * @param parentIDs
	 * @return
	 */
	List<TReportCategoryBean> loadByParents(List<Integer> parentIDs);

	/**
	 * Saves a reportCategoryBean in the database
	 * @param queryRepositoryBean 
	 * @return
	 */
	Integer save(TReportCategoryBean reportCategoryBean);
	
	/**
	 * Whether the category is empty or has content
	 * @param objectID
	 * @return
	 * @throws TrackDAOException
	 */
	boolean hasDependenData(Integer objectID);
	
	/**
	 * Deletes a reportCategoryBean from the database
	 * @param objectID 
	 * @return
	 */
	void delete(Integer objectID);
	
	/**
	 * Delete all private report categories of a person
	 * @param personID
	 */
	void deletePrivateReportCategories(Integer personID); 
}

