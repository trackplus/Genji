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

import com.trackplus.model.Trolelisttype;

/**
 * DAO for RoleListType
 * 
 * @author Tamas Ruff
 * 
 */
public interface RoleListTypeDAO {

	/**
	 * Loads all RoleListTypeBeans
	 * 
	 * @return
	 */
	List<Trolelisttype> loadAll();

	/**
	 * Loads the listTypes assigned to a role
	 * 
	 * @param roleID
	 * @return
	 */
	List<Trolelisttype> loadByRole(Integer roleID);

	/**
	 * Load the role to list type assignments for roles
	 * 
	 * @param roleIDs
	 * @return
	 */
	List<Trolelisttype> loadByRoles(List<Integer> roleIDs);

	/**
	 * Loads the role list types by roles and listType
	 * 
	 * @param roleIDs
	 * @param listType
	 *            if listType is null get all by roles
	 * @return
	 */
	List<Trolelisttype> loadByRolesAndListType(Object[] roleIDs,
			Integer listType);

	/**
	 * Saves a roleListTypebean
	 * 
	 * @param roleBean
	 * @return the created optionID
	 */
	Integer save(Trolelisttype roleListTypebean);

	/**
	 * Deletes a roleListTypebean
	 * 
	 * @param roleID
	 * @param issueTypeID
	 */
	void delete(Integer roleID, Integer issueTypeID);
}
