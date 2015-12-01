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

import com.trackplus.model.Tplisttype;

public interface PIssueTypeDAO {

	/**
	 * Loads all TPlistTypeBeans
	 * 
	 * @return
	 */
	List<Tplisttype> loadAll();

	/**
	 * Load the Tplisttype for a projectTypeID
	 * 
	 * @param projectTypeID
	 * @return
	 */
	List<Tplisttype> loadByProjectType(Integer projectTypeID);

	/**
	 * Load the Tplisttype for projectTypeIDs
	 * 
	 * @param projectTypeIDs
	 * @return
	 */
	List<Tplisttype> loadByProjectTypes(Object[] projectTypeIDs);

	/**
	 * Save Tplisttype in the TPlistType table
	 * 
	 * @param pissueTypeBean
	 * @return
	 */
	Integer save(Tplisttype pissueTypeBean);

	/**
	 * Deletes a Tplisttype from the TPlistType table
	 * 
	 * @param objectID
	 * @return
	 */
	void delete(Integer objectID);

	/**
	 * Deletes a Tplisttype from the TPlistType table
	 * 
	 * @param projectTypeID
	 * @param issueTypeID
	 */
	void delete(Integer projectTypeID, Integer issueTypeID);
}
