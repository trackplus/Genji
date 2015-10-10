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

import com.aurel.track.beans.TListTypeBean;

/**
 * DAO for IssueType object
 * 
 *
 */
public interface IssueTypeDAO {
	
	/**
	 * Gets an issueTypeBean by primary key
	 * @param objectID
	 * @return
	 */
	TListTypeBean loadByPrimaryKey(Integer objectID);
	
	/**
	 * Gets an issueTypeBean by label
	 * @param label
	 * @return
	 */
	List<TListTypeBean> loadByLabel(String label);
	
	
	/**
	 * Loads all issue types
	 * @return 
	 */
	List<TListTypeBean> loadAll();
	
	/**
	 * Load all selectable item types
	 * @return
	 */
	List<TListTypeBean> loadAllSelectable();
	
	/**
	 * Loads all document type item types
	 * @return
	 */
	List<TListTypeBean> loadAllDocumentTypes();
	
	/**
	 * Loads strikt document type item types
	 * @return
	 */
	List<TListTypeBean> loadStrictDocumentTypes();

	/**
	 * Loads the issue types by IDs
	 * @param issueTypeIDs
	 */
	List<TListTypeBean> loadByIssueTypeIDs(List<Integer> issueTypeIDs);
	
	/**
	 * Return the issueTypeBean with a specific typeFlag   
	 * @param typeFlag
	 * @return
	 */
	List<TListTypeBean> loadByTypeFlag(int typeFlag);
	
	/**
	 * Load all issue types allowed for the project type
	 * @param projectType
	 * @return
	 */
	List<TListTypeBean> loadAllowedByProjectType(Integer projectType);
	
	/**
	 * Load all non document issue types allowed for the project type
	 * @param projectType
	 * @return
	 */
	List<TListTypeBean> loadAllowedNonDocumentTypesByProjectType(Integer projectType);
	
	/**
	 * Load all document issue types allowed for the project type
	 * @param projectType
	 * @return
	 */
	List<TListTypeBean> loadAllowedDocumentTypesByProjectType(Integer projectType);
	
	/**
	 * Load all issueTypes which are allowed in the projectTypes
	 * @param projectTypeIDs
	 * @return
	 */
	List<TListTypeBean> loadAllowedByProjectTypes(Object[] projectTypeIDs);
	List<TListTypeBean> loadAllowedDocumentsByProjectTypes(Object[] projectTypeIDs);

	/**
	 * Load all issueTypes assigned to roles
	 * @param roleIDs
	 * @return
	 */
	List<TListTypeBean> loadForRoles(Object[] roleIDs);
		
	
	
	
	/**
	 * Gets the next available sortorder
	 * @return
	 */
	Integer getNextSortOrder();
	
	/**
	 * Save  issueType in the TCategory table
	 * @param issueType
	 * @return
	 */
	Integer save(TListTypeBean issueType);
	
	/**
	 * Whether the issuetype is used as foreign key
	 * @param objectID
	 * @return
	 */
	boolean hasDependentData(Integer objectID);
	
	/**
	 * Deletes a issueType from the TCategory table
	 * @param objectID
	 */
	void delete(Integer objectID);
	
	/**
	 * Replaces the dependences with a newIssustypeID and 
	 * deletes the oldIssuetypeID from the TCategory table 
	 * @param oldIssuetypeID
	 * @param newIssustypeID
	 */
	void replace(Integer oldIssuetypeID, Integer newIssustypeID);
		
	/**
	 * Returns the sort order column name
	 * @return
	 */
	String getSortOrderColumn();
	
	/**
	 * Returns the table name
	 * @return
	 */
	String getTableName();
}
