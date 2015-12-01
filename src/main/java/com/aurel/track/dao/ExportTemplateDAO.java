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

import com.aurel.track.beans.TExportTemplateBean;

/**
 * A Data Acces Object for export template entity
 */
public interface ExportTemplateDAO {
     /**
      * Gets a exportTemplateBean from the TExportTemplate table
      * @param objectID
      * @return
      */
	TExportTemplateBean loadByPrimaryKey(Integer objectID);

	/**
	 * Load the exportTemplateBeans by label
	 * @param repository
	 * @param categoryID
	 * @param projectID
	 * @param personID
	 * @param label
	 * @return
	 */
	List<TExportTemplateBean> loadByLabel(Integer repository, Integer categoryID, Integer projectID, Integer personID, String label);
	
	/**
	 * Get the root reports  
	 * @param repository
	 * @param projectID
	 * @param personID
	 * @param queryTypes
	 * @return
	 */
	List<TExportTemplateBean> loadRootReports(Integer repository, Integer projectID, Integer personID);		
	
	/**
	 * Get the root reports  
	 * @param projectIDs
	 * @return
	 */
	List<TExportTemplateBean> loadProjectRootCategories(List<Integer> projectIDs);	
	
	/**
	 * Loads the queries for a category
	 * @param categoryID
	 * @return
	 */
	List<TExportTemplateBean> loadByCategory(Integer categoryID);
	
	/**
	 * Loads the queries for parent categories
	 * @param categoryIDs
	 * @return
	 */
	List<TExportTemplateBean> loadByCategories(List<Integer> categoryIDs);
	
	/**
	 * Loads exportTemplateBeans by IDs
	 * @param reportIds
	 * @return
	 */
	List<TExportTemplateBean> loadByIDs(List<Integer> reportIds);
	
	/**
	 * Loads the templates derived from a parent template
	 * @param parentTemplateID
	 * @return
	 */
	List<TExportTemplateBean> loadDerived(Integer parentTemplateID);
	
	/**
	 * Loads all templates from TExportTemplate table
	 * @return
	 */
	List<TExportTemplateBean> loadAll();
	
	/**
	 * Loads all templates between two indexes
	 * @return
	 */
	List<TExportTemplateBean> loadFromTo(Integer from, Integer to);

	/**
	 * Save  exportTemplate in the TExportTemplate table
	 * @param exportTemplate
	 * @return
	 */
	Integer save(TExportTemplateBean exportTemplate);


	/**
	 * Deletes a exportTemplate from the TExportTemplate table
	 * Is deletable should return true before calling this method
	 * @param objectID
	 */
	void delete(Integer objectID);


	/**
	 * Verifies whether a screenField is deletable
	 * @param objectID
	 */
	boolean isDeletable(Integer objectID);

	/**
	 * Loads all private templates 
	 * @param personID
	 * @return
	 */
	List<TExportTemplateBean> loadPrivate(Integer personID);


}
