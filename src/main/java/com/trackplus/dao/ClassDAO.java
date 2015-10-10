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

import com.trackplus.model.Tclass;

/**
 * DAO for Class. Class is a project specific attribute. Example for usage is:
 * documentation, hardware, software.
 * 
 * @author Tamas Ruff
 * 
 */
public interface ClassDAO {
	/**
	 * Loads a classBean by primary key
	 * 
	 * @param objectID
	 * @return
	 */
	// Tclass loadbyprimarykey(Integer objectid);

	/**
	 * Gets an class from a project by label
	 * 
	 * @param projectID
	 * @param label
	 * @return
	 */
	// Tclass loadbyprojectandlabel(Integer projectid, string label);

	/**
	 * Gets the classs by uuid list
	 * 
	 * @param uuids
	 * @return
	 */
	// List<Tclass> loadbyuuids(list<string> uuids);
	/**
	 * Gets the classs by labels list
	 * 
	 * @param labels
	 * @return
	 */
	// List<Tclass> loadbylabels(list<string> labels);
	/**
	 * Loads all ClassBeans
	 * 
	 * @return
	 */
	// List loadAll();

	/**
	 * Loads a ClassBean list by workItemKeys
	 * 
	 * @param workItemIDs
	 * @return
	 */
	// List loadByWorkItemKeys(int[] workItemIDs);

	/**
	 * Loads a list with classs with the defined classes for a project
	 * 
	 * @param projectID
	 * @return
	 */
	List<Tclass> loadByProject(Integer projectid);
	/**
	 * Loads a ClassBean list by projectIDs
	 * 
	 * @param projectIDs
	 * @return
	 */
	// List<Tclass> loadbyprojectkeys(list<Integer> projectids);
	/**
	 * Loads a list with classs with the used classes for a project (classes
	 * which are assigned to at least one workItem)
	 * 
	 * @param projectID
	 * @return
	 */
	// List loadUsedByProject(Integer projectID);

	/**
	 * Saves a classBean in the Tclass table
	 * 
	 * @param classBean
	 * @return
	 */
	// Integer save(Tlclass class);

	/**
	 * Whether there are workItems with this class
	 * 
	 * @param objectID
	 * @return
	 */
	// boolean hasDependentData(Integer objectID);

	/**
	 * Replaces the dependences with a new classID and deletes the old classID
	 * from the Tclass table
	 * 
	 * @param oldClassID
	 * @param newClassID
	 */
	// void replaceAndDelete1(Integer oldClassID, Integer newClassID);

	/*********************************************************
	 * Manager-, Responsible-, My- and Custom Reports methods *
	 *********************************************************/

	/**
	 * Get the classs associated with workItems the person is manager for
	 * 
	 * @param personID
	 * @return
	 */
	// List loadManagerClasses(Integer personID);

	/**
	 * Get the classs associated through a class picker with workItems the
	 * person is manager for
	 * 
	 * @param personID
	 * @return
	 */
	// List loadManagerPickerClasses(Integer personID);

	/**
	 * Get the classs associated with workItems the person is responsible for
	 * 
	 * @param personID
	 * @return
	 */
	// List loadResponsibleClasses(Integer personID);

	/**
	 * Get the classs associated through a class picker with workItems the
	 * person is responsible for
	 * 
	 * @param personID
	 * @return
	 */
	// List loadResponsiblePickerClasses(Integer personID);

	/**
	 * Get the classs associated with workItems the person is originator for
	 * 
	 * @param personID
	 * @return
	 */
	// List loadReporterClasses(Integer personID);

	/**
	 * Get the classs associated with workItems the person is manager or
	 * responsible or owner for
	 * 
	 * @param personID
	 * @return
	 */
	// List loadMyClasses(Integer personID);

	/**
	 * Get the classs associated through a class picker with workItems the
	 * person is manager or responsible or owner for
	 * 
	 * @param personID
	 * @return
	 */
	// List loadMyPickerClasses(Integer personID);

	/**
	 * Get the classs filtered by the FilterSelectsTo
	 * 
	 * @param filterSelectsTo
	 * @return
	 */
	// List<Tclass> loadcustomreportclasses(filterupperto filterselectsto);
	/**
	 * Get the classs through a class picker filtered by the FilterSelectsTo
	 * 
	 * @param filterSelectsTo
	 * @return
	 */
	// List<Tclass> loadcustomreportpickerclasses(filterupperto
	// filterselectsto);
	/**
	 * Get the classs associated through a class picker for an array of
	 * workItemIDs
	 * 
	 * @param workItemIDs
	 * @return
	 */
	// List loadLucenePickerClasses(int[] workItemIDs);

	/**
	 * Get the Map of classs from the history of the workItemIDs added by
	 * personID
	 * 
	 * @param workItemIDs
	 * @param personID
	 *            in null do not filter by personID
	 * @return
	 */
	// Map<Integer, Tclass> loadhistoryclasses(int[] workitemids/*, Integer
	// personid*/);
}
