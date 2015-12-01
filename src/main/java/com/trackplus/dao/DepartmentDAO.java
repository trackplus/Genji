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

import com.trackplus.model.Tdepartment;

/**
 * DAO for Department
 * 
 * @author Tamas Ruff
 * 
 */
public interface DepartmentDAO {

	/**
	 * Loads all department beans
	 * 
	 * @return
	 */
	List<Tdepartment> loadAll();

	/**
	 * Loads a Tdepartment by primary key
	 * 
	 * @param departmentID
	 * @return
	 */
	Tdepartment loadByPrimaryKey(Integer departmentID);

	/**
	 * Loads a Tdepartment by primary keys
	 * 
	 * @param departmentIDs
	 * @return
	 */
	List<Tdepartment> loadByKeys(List<Integer> departmentIDs);

	/**
	 * Loads a Tdepartment by name
	 * 
	 * @param name
	 * @param parentDepartment
	 * @return
	 */
	List<Tdepartment> loadByName(String name, Integer parentDepartment);

	/**
	 * Loads all main departments
	 * 
	 * @param
	 * @return
	 */
	List<Tdepartment> loadMainDepartments();

	/**
	 * Loads all child departments for a department
	 * 
	 * @param parentDepartmentID
	 * @return
	 */
	List<Tdepartment> loadSubdepartments(Integer parentDepartmentID);

	/**
	 * Loads all child departments for a list of parent department
	 * 
	 * @param parentDepartmentIDs
	 * @return
	 */
	List<Tdepartment> loadSubdepartments(List<Integer> parentDepartmentIDs);

	/**
	 * Gets the default department
	 * 
	 * @return
	 */
	Integer getDefaultDepartment();

	/**
	 * Saves a department to the TDepartment table.
	 * 
	 * @param departmentBean
	 * @return
	 */
	Integer save(Tdepartment departmentBean);

	/**
	 * Whether the department has dependent data
	 * 
	 * @param departmentID
	 * @return
	 */
	boolean hasDependentData(Integer departmentID);

	/**
	 * Replaces the oldDepartmentID with newDepartmentID departmentID
	 * 
	 * @param oldDepartmentID
	 * @param newDepartmentID
	 * @return
	 */
	void replace(Integer oldDepartmentID, Integer newDepartmentID);

	/**
	 * Deletes a departmentID
	 * 
	 * @param departmentID
	 * @return
	 */
	void delete(Integer departmentID);

}
