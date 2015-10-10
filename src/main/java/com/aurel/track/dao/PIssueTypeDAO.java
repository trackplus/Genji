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

import com.aurel.track.beans.TPlistTypeBean;

public interface PIssueTypeDAO {

	/**
	 * Loads all TPlistTypeBeans 
	 * @return
	 */
	List<TPlistTypeBean> loadAll();
	
	/**
	 * Load the TPlistTypeBean for a projectTypeID
	 * @param projectTypeID
	 * @return
	 */
	List<TPlistTypeBean> loadByProjectType(Integer projectTypeID);
	
	/**
	 * Load the TPlistTypeBean for a itemTypeID
	 * @param itemTypeID
	 * @return
	 */
	List<TPlistTypeBean> loadByItemType(Integer itemTypeID);
	
	/**
	 * Load the TPlistTypeBean for a itemTypeFlags
	 * @param itemTypeFlags
	 * @return
	 */
	List<TPlistTypeBean> loadByItemTypeFlags(int[] itemTypeFlags);
	
	/**
	 * Load the TPlistTypeBean for projectTypeIDs
	 * @param projectTypeIDs
	 * @return
	 */
	List<TPlistTypeBean> loadByProjectTypes(Object[] projectTypeIDs);	
	
	/**
	 * Save  TPlistTypeBean in the TPlistType table
	 * @param pissueTypeBean
	 * @return
	 */
	Integer save(TPlistTypeBean pissueTypeBean);
	
	/**
	 * Deletes a TPlistTypeBean from the TPlistType table 
	 * @param objectID
	 * @return
	 */
	void delete(Integer objectID);
	
	/**
	 * Deletes TPlistTypeBean(s) from the TPlistType table 
	 * @param projectTypeID
	 * @param issueTypeIDs
	 */
	void delete(Integer projectTypeID, List<Integer> issueTypeIDs);
}
