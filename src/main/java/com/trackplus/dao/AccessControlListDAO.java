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


package com.trackplus.dao;

import java.util.List;
import java.util.Map;

import com.trackplus.model.Tacl;
import com.workingdogs.village.Record;

/**
 * <p>
 * This is the Data Access Object (DAO) interface for access control lists
 * (ACL). ACL entries are persisted in the Tacl database table. all access
 * permissions in projects are controlled via access control lists.
 * </p>
 * <p>
 * An entry in an ACL relates a person or group (from database table Tperson), a
 * role (from database table Trole), and a project (from database table
 * tproject).
 * </p>
 * <p>
 * The permission related to an ACL entry are transported via the role; that is
 * the role defines the permissions.
 * </p>
 * 
 * @author Joerg Friedrich
 * @see com.trackplus.dao.PersonDAO
 * @see com.trackplus.dao.ProjectDAO
 * @see com.trackplus.dao.RoleDAO
 */
public interface AccessControlListDAO {

	/**
	 * Loads the number of persons in different roles roles for a project
	 * 
	 * @param projectID
	 * @return
	 */
	Map<Integer, Integer> loadNumberOfPersonsInRolesForProject(Integer projectID);

	/**
	 * Loads the number of persons in a role in a project
	 * 
	 * @param projectID
	 * @param roleID
	 * @return
	 */
	Integer loadNumberOfPersonsInRoleForProject(Integer projectID,
			Integer roleID);

	/**
	 * Loads a list of AccessControlListBeans by project and role
	 * 
	 * @param projectID
	 * @param roleID
	 * @return
	 */
	List<Tacl> loadByProjectAndRole(Integer projectID, Integer roleID);

	/**
	 * Loads a list of AccessControlListBeans by person
	 * 
	 * @param personID
	 * @return
	 */
	List<Tacl> loadByPerson(Integer personID);

	/**
	 * Loads a list of AccessControlListBeans by person and project status flags
	 * 
	 * @param personID
	 * @return
	 */
	List<Tacl> loadByPersonAndProjectStatusFlag(Integer personID,
			int[] projectStatusFlag);

	/**
	 * Loads a list of AccessControlListBeans by persons
	 * 
	 * @param personIDs
	 * @return
	 */
	List<Tacl> loadByPersons(List<Integer> personIDs);

	/**
	 * Loads a list of AccessControlListBeans by persons and project status
	 * flags
	 * 
	 * @param personIDs
	 * @return
	 */
	List<Tacl> loadByPersonsAndProjectStatusFlag(List<Integer> personIDs,
			int[] projectStatusFlag);

	/**
	 * Loads a list of AccessControlListBeans by project
	 * 
	 * @param projectID
	 * @return
	 */
	List<Tacl> loadByProject(Integer projectID);

	/**
	 * Whether the person has right in any project with certain status flags
	 * 
	 * @param personIDs
	 * @param arrRight
	 * @param projectStatusFlag
	 * @return
	 */
	boolean hasPersonRightInAnyProjectWithStatusFlag(int[] personIDs,
			int[] arrRight, int[] projectStatusFlag);

	/**
	 * Gets the "projectsIDs to issueTypeID set" map the person has rights in
	 * 
	 * @param groupsAndSelf
	 * @param projectIDs
	 * @param arrRights
	 * @return
	 */
	List<Record> getProjectIssueTypeRecords(int[] groupsAndSelf,
			Integer[] projectIDs, int[] arrRights);

	/**
	 * Returns a set of personIDs which have one of the specified rights in any
	 * project from projects
	 * 
	 * @param projectOIDs
	 *            an array with project object identifiers
	 * @param arrRights
	 *            an array of rights, null means any right
	 * @return set with persons object IDs
	 */
	List<Tacl> loadByProjectsAndRights(Integer[] projectOIDs, int[] arrRights);

	/**
	 * Loads a list of AccessControlListBeans with the directly assigned
	 * persons/groups
	 * 
	 * @param selectedPersons
	 *            when null or of length 0 it will be no filtered
	 * @param selectedProjects
	 *            when null or of length 0 no results
	 * @return
	 */
	List<Tacl> loadByPersonsAndProjects(List<Integer> selectedPersons,
			List<Integer> selectedProjects);

	/**
	 * Load a list of AccessControlListBeans by person, projects and right
	 * 
	 * @param personIDs
	 * @param projectID
	 * @param rights
	 * @return
	 */
	List<Tacl> loadByPersonProjectsRight(int[] personIDs,
			List<Integer> projectIDs, int[] rights);

	/**
	 * Load a list of AccessControlListBeans by person, projects and role
	 * 
	 * @param personIDs
	 * @param projectID
	 * @param role
	 * @return
	 */
	List<Tacl> loadByPersonProjectsRole(int[] personIDs,
			List<Integer> projectIDs, Integer role);

	/**
	 * Load a list of AccessControlListBeans by project, roles and listType
	 * 
	 * @param projectIDs
	 * @param roles
	 * @param listType
	 *            if null do not filter by
	 * @return
	 */
	List<Tacl> loadByProjectsRolesListType(List<Integer> projectIDs,
			Object[] roles, Integer listType);

	/**
	 * Saves a accessControlListBean to the Taccesscontrollist table.
	 * 
	 * @param accessControlListBean
	 * @return
	 */
	void save(Tacl accessControlListBean);

	/**
	 * Deletes an AccessControlList
	 * 
	 * @param projKey
	 * @param roleKey
	 * @param personKey
	 */
	void deleteByProjectRolePerson(Integer projectID, Integer roleID,
			Integer personID);
}
