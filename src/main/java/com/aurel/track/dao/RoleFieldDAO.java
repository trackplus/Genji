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

import com.aurel.track.beans.TRoleFieldBean;

public interface RoleFieldDAO {
	
	/**
	 * Gets all field restrictions for all roles
	 * @return
	 */
	List<TRoleFieldBean> loadAll();
	
	/**
	 * Gets the TRoleFieldBean for a role
	 * @param roleID
	 * @return
	 */
	List<TRoleFieldBean> getByRole(Integer roleID);
	
	/**
	 * Gets the list of TRoleFieldBean by IDs 
	 * @param roleFieldBeanIDs 
	 * @return
	 */
	List<TRoleFieldBean> getRoleFieldsByKeys(List<Integer> roleFieldBeanIDs);
	
	/**
	 * Gets the restricted fields in all roles
	 * @param roleIDs
	 * @param presentFields
	 * @return
	 */
	
	/**
	 * Gets the restricted fields in any role
	 * @param fieldIDs
	 * @return
	 */
	List<TRoleFieldBean> getByFields(List<Integer> fieldIDs);
	
	/**
	 * Saves a TRoleFieldBean
	 * @param roleFieldBean 
	 * @return
	 */
	void save(TRoleFieldBean roleFieldBean);
	
	/**
	 * Deletes a TRoleFieldBean field by objectID
	 * @param objectID
	 */
	void delete(Integer objectID);
}
