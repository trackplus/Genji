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

import com.aurel.track.beans.TProjectTypeBean;

/**
 * DAO for ProjectType object
 * 
 *
 */
public interface ProjectTypeDAO {
	/**
	 * Gets a projectTypeBean from the TProjectType table
	 * @param objectID
	 * @return
	 */
	TProjectTypeBean loadByPrimaryKey(Integer objectID);
	
	/**
	 * Loads all projectTypeBeans 
	 * @return 
	 */
	List<TProjectTypeBean> loadAll();
	
	/**
	 * Loads all non private projectTypeBeans 
	 * @return 
	 */
	List<TProjectTypeBean> loadNonPrivate();
	
	/**
	 * Loads all private projectTypeBeans 
	 * @return 
	 */
	List<TProjectTypeBean> loadPrivate();
	
	/**
	 * Loads project type beans by IDs 
	 * @param projectTypeIDs
	 * @return
	 */
	List<TProjectTypeBean> loadByProjectTypeIDs(List<Integer> projectTypeIDs);
	
	/**
	 * Whether all project types have issueType restrictions
	 * @param projectTypeIDs
	 * @return
	 */
	boolean allHaveIssueTypeRestrictions(Object[] projectTypeIDs);
	
	/**
	 * Save  projectType in the TProjectType table
	 * @param projectType
	 * @return
	 */
	Integer save(TProjectTypeBean projectTypeBean);
	
	/**
	 * Deletes a projectType from the TProjectType table
	 * Is deletable should return true before calling this method
	 * @param objectID
	 */
	void delete(Integer objectID);
	
	/**
	 * Replace and delete a projectType 
	 * @param oldOID
	 * @param newOID
	 */
	void replaceAndDelete(Integer oldOID, Integer newOID);
		
	/**
	 * This method verifies whether <code>ProjectType</code> is used in other tables in the database.
	 * @param oldOID object identifier of list type to be replaced
	 */
	boolean hasDependentData(Integer oldOID); 

}
