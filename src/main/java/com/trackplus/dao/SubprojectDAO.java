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


package com.trackplus.dao;

import java.util.List;

import com.aurel.track.persist.TProject;

/**
 * DAO for Subproject
 * 
 * @author Tamas Ruff
 * 
 */
public interface SubprojectDAO {
	/**
	 * Loads a subprojectBean by primary key
	 * 
	 * @param objectID
	 * @return
	 */
	// Tprojectcategory loadByPrimaryKey(Integer objectID);

	/**
	 * Gets an subprojectBean from a project by label
	 * 
	 * @param projectID
	 * @param label
	 * @return
	 */
	// Tprojectcategory loadByProjectAndLabel(Integer projectID, String label);

	/**
	 * Gets the subprojectsBeans by uuid list
	 * 
	 * @param uuids
	 * @return
	 */
	// List<Tprojectcategory> loadByUUIDs(List<String> uuids); >
	/**
	 * Gets the subprojectsBeans by labels list
	 * 
	 * @param labels
	 * @return
	 */
	// List<Tprojectcategory> loadByLabels(List<String> labels);
	/**
	 * Loads all ProjectCategoryBeans
	 * 
	 * @return
	 */
	// List loadAll();

	/**
	 * Loads a ProjectCategoryBean list by workItemKeys
	 * 
	 * @param workItemKeys
	 * @return
	 */
	// List loadByWorkItemKeys(int[] workItemKeys);

	/**
	 * Loads all subprojects of the active and inactive projects with the labels
	 * prefixed with the projectlabel
	 * 
	 * @return
	 */
	// List loadAllWithProjectLabel();

	/**
	 * Loads a list with subprojectBeans with the defined subprojects for a
	 * project
	 * 
	 * @param projectID
	 * @return
	 */
	List<TProject> loadByProject(Integer projectID);
	/**
	 * Loads a ProjectCategoryBean list by projectKeys
	 * 
	 * @param projectIDs
	 * @return
	 */
	// List<Tprojectcategory> loadByProjectKeys(List<Integer> projectIDs);
	/**
	 * Loads a list with subprojectBeans with the used subprojects for a project
	 * (subprojects which have workItems)
	 * 
	 * @param projectID
	 * @return
	 */
	// List loadUsedByProject(Integer projectID);

	/**
	 * saves a projectCategoryBean in the TProjCat table
	 * 
	 * @param projectCategoryBean
	 * @return
	 */
	// Integer save(Tprojectcategory projectCategoryBean);

	/**
	 * Whether there are workItems with this subproject
	 * 
	 * @param objectID
	 * @return
	 */
	// boolean hasDependentData(Integer objectID);

	/**
	 * Replaces the dependences with a new subprojectID and deletes the old
	 * subprojectID from the TProjCat table
	 * 
	 * @param oldSubprojectID
	 * @param newSubprojectID
	 */
	// void replaceAndDelete1(Integer oldSubprojectID, Integer newSubprojectID);

	/*********************************************************
	 * Manager-, Responsible-, My- and Custom Reports methods *
	 *********************************************************/

	/**
	 * Get the subprojectBeans associated with workItems the person is manager
	 * for
	 * 
	 * @param personID
	 * @return
	 */
	// List loadManagerSubprojects(Integer personID);

	/**
	 * Get the subprojectBeans associated through a subproject picker with
	 * workItems the person is manager for
	 * 
	 * @param personID
	 * @return
	 */
	// List loadManagerPickerSubprojects(Integer personID);

	/**
	 * Get the subprojectBeans associated with workItems the person is
	 * responsible for
	 * 
	 * @param personID
	 * @return
	 */
	// List loadResponsibleSubprojects(Integer personID);

	/**
	 * Get the subprojectBeans associated through a subproject picker with
	 * workItems the person is responsible for
	 * 
	 * @param personID
	 * @return
	 */
	// List loadResponsiblePickerSubprojects(Integer personID);

	/**
	 * Get the subprojectBeans associated with workItems the person is
	 * originator for
	 * 
	 * @param personID
	 * @return
	 */
	// List loadReporterSubprojects(Integer personID);

	/**
	 * Get the subprojectBeans associated with workItems the person is manager
	 * or responsible or owner for
	 * 
	 * @param personID
	 * @param locale
	 * @return
	 */
	// List<Tprojectcategory> loadMySubprojects(Integer personID);
	/**
	 * Get the subprojectBeans associated through a subproject picker with
	 * workItems the person is manager or responsible or owner for
	 * 
	 * @param personID
	 * @return
	 */
	// List loadMyPickerSubprojects(Integer personID);

	/**
	 * Get the subprojectBeans filtered by the FilterSelectsTO
	 * 
	 * @param filterSelectsTO
	 * @return
	 */
	// List<Tprojectcategory> loadCustomReportSubprojects(FilterUpperTO
	// filterSelectsTO);
	/**
	 * Get the subprojectBeans associated through a subproject picker filtered
	 * by the FilterSelectsTO
	 * 
	 * @param filterSelectsTO
	 * @return
	 */
	// List<Tprojectcategory> loadCustomReportPickerSubprojects(FilterUpperTO
	// filterSelectsTO);
	/**
	 * Get the subprojectBeans associated through a subproject picker for an
	 * array of workItemIDs
	 * 
	 * @param workItemIDs
	 * @return
	 */
	// List loadLucenePickerSubprojects(int[] workItemIDs);

	/**
	 * Get the Map of projectCategoryBeans from the history of the workItemIDs
	 * added by personID
	 * 
	 * @param workItemIDs
	 * @param personID
	 *            in null do not filter by personID
	 * @return
	 */
	// Map<Integer, Tprojectcategory loadHistorySubprojects(int[] workItemIDs/*,
	// Integer personID*/);
}
