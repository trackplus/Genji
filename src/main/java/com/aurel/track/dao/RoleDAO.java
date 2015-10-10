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

import com.aurel.track.beans.TRoleBean;

/**
 * <p>
 * This is the Data Access Object (DAO) interface for roles.
 * Roles are assigned to persons or groups  in projects 
 * via access control lists (ACL).
 * </p>
 * <p>
 * Persons and groups are stored in database table <code>TPERSON</code>.</br>
 * Projects are stored in database table <code>TPROJECT</code>.</br>
 * Access control lists are stored in database table <code>TACL</code>.</br>
 * Roles are stored in database table <code>TROLE</code>.</br>
 * </p> 
 * 
 * @author Tamas Ruff
 * @see com.aurel.track.dao.PersonDAO
 * @see com.aurel.track.dao.ProjectDAO
 * @see com.aurel.track.dao.AccessControlListDAO
 *
 */
public interface RoleDAO {
		
	/**
	 * Loads a roleBean by primary key
	 * @param objectID
	 * @return
	 */
	TRoleBean loadByPrimaryKey(Integer objectID);
	
	/**
	 * Loads a roleBean by label
	 * Might be used in Groowy scripts
	 * @param label
	 * @return
	 */
	TRoleBean loadByName(String label);
	
	/**
	 * Loads all roleBeans
	 * @return 
	 */
	List<TRoleBean> loadAll();
	
	/**
	 * Loads all visible RoleBeans
	 * @return 
	 */
	List<TRoleBean> loadVisible();
	
	/**
	 * Loads all not visible RoleBeans
	 * @return 
	 */
	List<TRoleBean> loadNotVisible();
	
	/**
	 * Load by roleIDs
	 * @return
	 */
	List<TRoleBean> loadByRoleIDs(List<Integer> primaryKeys);
	
	/**
	 * Load the roles which have explicit issueType(s) assignments
	 * @param roleIDs
	 * @return
	 */
	List<TRoleBean> loadWithExplicitIssueType(Object[] roleIDs);
	
	/**
	 * Load the roles which have explicit field restrictions
	 * @param roleIDs
	 * @return
	 */
	List<TRoleBean> loadWithExplicitFieldRestrictions(Object[] roleIDs);

	
	/**
	 * Gets the roles needed for any state transition from stateFrom for projectType and issueType
	 * @param projectType
	 * @param issueType
	 * @param stateFrom 
	 */
	List<TRoleBean> getRolesForTransition(Integer projectType, 
			Integer  issueType,	Integer stateFrom); 
	
	/**
	 * Saves a new/existing roleBean
	 * @param roleBean
	 * @return the created roleID
	 */
	Integer save(TRoleBean roleBean);
	
	/**
	 * Deletes a roleBean
	 * @param objectID
	 */
	void delete(Integer objectID);
}
