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

import com.aurel.track.beans.TDepartmentBean;

/**
 * DAO for Department
 * @author Tamas Ruff
 *
 */
public interface DepartmentDAO {
		
	/**
	 * Loads all department beans
	 * @return 
	 */
	List<TDepartmentBean> loadAll();
	
	/**
	 * Loads a TDepartmentBean by primary key
	 * @param departmentID
	 * @return
	 */
	TDepartmentBean loadByPrimaryKey(Integer departmentID);
	
	/**
	 * Loads a TDepartmentBean by primary keys
	 * @param departmentIDs
	 * @return
	 */
	List<TDepartmentBean> loadByKeys(List<Integer> departmentIDs);
	
	/**
	 * Loads a TDepartmentBean by name
	 * @param name
	 * @param parentDepartment
	 * @return
	 */
	List<TDepartmentBean> loadByName(String name, Integer parentDepartment);

	/**
	 * Loads all main departments
	 * @param
	 * @return
	 */
	List<TDepartmentBean> loadMainDepartments();
	
	/**
	 * Loads all child departments for a department
	 * @param parentDepartmentID
	 * @return
	 */
	List<TDepartmentBean> loadSubdepartments(Integer parentDepartmentID);
	
	/**
	 * Loads all child departments for a list of parent department
	 * @param parentDepartmentIDs
	 * @return
	 */
	List<TDepartmentBean> loadSubdepartments(List<Integer> parentDepartmentIDs);
	
	/**
	 * Gets the default department
	 * @return
	 */
	Integer getDefaultDepartment();
	
	/**
	 * Saves a department to the TDepartment table.
	 * @param departmentBean
	 * @return
	 */
	Integer save(TDepartmentBean departmentBean);
	
	/**
	 * Whether the department has dependent data
	 * @param departmentID
	 * @return
	 */
	boolean hasDependentData(Integer departmentID);
	
	/**
	 * Replaces the oldDepartmentID with newDepartmentID departmentID
	 * @param oldDepartmentID
	 * @param newDepartmentID
	 * @return
	 */
	void replace(Integer oldDepartmentID, Integer newDepartmentID);
	
	/**
	 * Deletes a departmentID
	 * @param departmentID
	 * @return
	 */
	void delete(Integer departmentID);
	
	
}
