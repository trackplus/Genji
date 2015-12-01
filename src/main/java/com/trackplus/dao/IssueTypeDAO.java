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

import com.trackplus.model.Tcategory;

/**
 * DAO for IssueType object
 * 
 * 
 */
public interface IssueTypeDAO {

	/**
	 * Gets an issueTypeBean by primary key
	 * 
	 * @param objectID
	 * @return
	 */
	Tcategory loadByPrimaryKey(Integer objectID);

	/**
	 * Gets an issueTypeBean by label
	 * 
	 * @param label
	 * @return
	 */
	List<Tcategory> loadByLabel(String label);

	/**
	 * Gets the label beans with a certain symbol
	 * 
	 * @param listID
	 * @param symbol
	 * @param excludeObjectID
	 * @return
	 */
	// List<Tcategory> loadBySymbol(Integer listID, String symbol, Integer
	/**
	 * Loads all issue types
	 * 
	 * @return
	 */
	List<Tcategory> loadAll();

	/**
	 * Loads the issue types by IDs
	 * 
	 * @param issueTypeIDs
	 */
	List<Tcategory> loadByIssueTypeIDs(List<Integer> issueTypeIDs);

	/**
	 * Return the issueTypeBean with a specific typeFlag
	 * 
	 * @param typeFlag
	 * @return
	 */
	List<Tcategory> loadByTypeFlag(int typeFlag);

	/**
	 * Loads the issue types which have any of the rightFlags rights in the
	 * project for the person
	 * 
	 * @returnTtlistty
	 */
	// List<Tcategory> loadByPersonAndProjectAndRight(Integer person, Integer
	/**
	 * Load all issueTypes which are allowed in the projectTypes
	 * 
	 * @param projectTypeIDs
	 * @return
	 */
	List<Tcategory> loadAllowedByProjectTypes(Object[] projectTypeIDs);

	/**
	 * Load all issueTypes assigned to roles
	 * 
	 * @param roleIDs
	 * @return
	 */
	List<Tcategory> loadForRoles(Object[] roleIDs);

	/**
	 * Loads a list with issueTypeBeans with issue types assigned to at least
	 * one workItem in any of the projects
	 * 
	 * @param projects
	 * @return
	 */
	/**
	 * Load all issue types allowed for the project type the project belongs to
	 * 
	 * @param projectType
	 * @return
	 */
	List<Tcategory> loadAllowedByProjectType(Integer projectType);

	/**
	 * Gets the next available sortorder
	 * 
	 * @return
	 */
	Integer getNextSortOrder();

	/**
	 * Save issueType in the TCategory table
	 * 
	 * @param issueType
	 * @return
	 */
	Integer save(Tcategory issueType);

	/**
	 * Whether the issuetype is used as foreign key
	 * 
	 * @param objectID
	 * @return
	 */
	boolean hasDependentData(Integer objectID);

	/**
	 * Deletes a issueType from the TCategory table
	 * 
	 * @param objectID
	 */
	void delete(Integer objectID);

	/**
	 * Replaces the dependences with a newIssustypeID and deletes the
	 * oldIssuetypeID from the TCategory table
	 * 
	 * @param oldIssuetypeID
	 * @param newIssustypeID
	 */
	void replace(Integer oldIssuetypeID, Integer newIssustypeID);

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
}
