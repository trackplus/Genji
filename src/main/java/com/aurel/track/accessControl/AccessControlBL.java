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

package com.aurel.track.accessControl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.beans.TAccessControlListBean;
import com.aurel.track.dao.AccessControlListDAO;
import com.aurel.track.dao.DAOFactory;
import com.workingdogs.village.Record;

public class AccessControlBL {
	
	private AccessControlBL() {
	}

	private static AccessControlListDAO accessControlListDAO = DAOFactory.getFactory().getAccessControlListDAO();

	/**************************************************************************************************************/
	/************************************* Access control configuration methods *************************************/
	/**************************************************************************************************************/

	/**
	 * Loads the number of persons in different roles roles for a project
	 * 
	 * @param projectID
	 * @return
	 */
	public static Map<Integer, Integer> loadNumberOfPersonsInRolesForProject(Integer projectID) {
		return accessControlListDAO.loadNumberOfPersonsInRolesForProject(projectID);
	}

	/**
	 * Loads the number of persons in a role in a project
	 * 
	 * @param projectID
	 * @param roleID
	 * @return
	 */
	public static Integer loadNumberOfPersonsInRoleForProject(Integer projectID, Integer roleID) {
		return accessControlListDAO.loadNumberOfPersonsInRoleForProject(projectID, roleID);
	}

	/**
	 * Loads a list of AccessControlListBeans by person
	 * 
	 * @param personID
	 * @return
	 */
	public static List<TAccessControlListBean> loadByPerson(Integer personID) {
		return accessControlListDAO.loadByPerson(personID);
	}

	/**
	 * Loads a list of AccessControlListBeans by a list of persons
	 * 
	 * @param personIDs
	 * @return
	 */
	public static List<TAccessControlListBean> loadByPersons(List<Integer> personIDs) {
		return accessControlListDAO.loadByPersons(personIDs);
	}

	/**
	 * Loads a list of AccessControlListBeans by project and role
	 * 
	 * @param projectID
	 * @param roleID
	 * @return
	 */
	public static List<TAccessControlListBean> loadByProjectAndRole(Integer projectID, Integer roleID) {
		return accessControlListDAO.loadByProjectAndRole(projectID, roleID);
	}

	/**
	 * Saves a new role to person assignment for a project
	 * 
	 * @param personID
	 * @param projectID
	 * @param roleID
	 */
	public static void save(Integer personID, Integer projectID, Integer roleID) {
		TAccessControlListBean accessControlListBeanDest = new TAccessControlListBean();
		accessControlListBeanDest.setPersonID(personID);
		accessControlListBeanDest.setProjectID(projectID);
		accessControlListBeanDest.setRoleID(roleID);
		save(accessControlListBeanDest);
	}

	/**
	 * Saves a accessControlListBean to the TAccessControlList table.
	 * 
	 * @param accessControlListBean
	 * @return
	 */
	public static void save(TAccessControlListBean accessControlListBean) {
		accessControlListDAO.save(accessControlListBean);
	}

	/**
	 * Deletes an AccessControlList
	 * 
	 * @param projKey
	 * @param roleKey
	 * @param personKey
	 */
	public static void deleteByProjectRolePerson(Integer projectID,
			Integer roleID, Integer personID) {
		accessControlListDAO.deleteByProjectRolePerson(projectID, roleID,
				personID);
	}

	/**
	 * Gets the status IDs assigned to the projectType for an issueType
	 * 
	 * @param projectID
	 * @param roleID
	 * @return
	 */
	public static Set<Integer> getPersonSetByProjectRole(Integer projectID,
			Integer roleID) {
		List<TAccessControlListBean> accessControlListBeans = loadByProjectAndRole(
				projectID, roleID);
		return AccessBeans.getPersonsFromAcList(accessControlListBeans);
	}

	/**
	 * Loads a list of AccessControlListBeans by project
	 * 
	 * @param projectID
	 * @return
	 */
	public static List<TAccessControlListBean> loadByProject(Integer projectID) {
		return accessControlListDAO.loadByProject(projectID);
	}

	/**************************************************************************************************************/
	/******************************************** Access control use methods ****************************************/
	/**************************************************************************************************************/

	/**
	 * Loads a list of AccessControlListBeans by persons and project status
	 * flags
	 * 
	 * @param personIDs
	 * @return
	 */
	static List<TAccessControlListBean> loadByPersonsAndProjectStatusFlag(
			List<Integer> personIDs, int[] projectStatusFlag) {
		return accessControlListDAO.loadByPersonsAndProjectStatusFlag(personIDs, projectStatusFlag);
	}

	/**
	 * Whether the person has right in any project with certain status flags
	 * 
	 * @param personIDs
	 * @param arrRight
	 * @param projectStatusFlag
	 * @return
	 */
	public static List<TAccessControlListBean> loadByPersonRightInAnyProjectWithStatusFlag(
			List<Integer> personIDs, int[] arrRights, int[] projectStatusFlag) {
		return accessControlListDAO.loadByPersonRightInAnyProjectWithStatusFlag(
				personIDs, arrRights, projectStatusFlag);
	}

	/**
	 * Whether the person has any right in a project with statusflags
	 * 
	 * @param personID
	 * @param arrRights
	 * @param projectStatusFlag
	 * @return
	 */
	public static boolean hasPersonRightInAnyProjectWithStatusFlag(
			Integer personID, int[] arrRights, int[] projectStatusFlag) {
		return accessControlListDAO.hasPersonRightInAnyProjectWithStatusFlag(
				AccessBeans.getMeAndSubstitutedAndGroups(personID), arrRights, projectStatusFlag);
	}

	/**
	 * Returns whether a person has a specific right in any project with the
	 * specified statusflags
	 * 
	 * @param personID
	 * @param arrRights
	 * @return
	 */
	public static boolean hasPersonRightInNonPrivateProject(Integer personID,
			int[] arrRights) {
		return accessControlListDAO.hasPersonRightInNonPrivateProject(
				AccessBeans.getMeAndSubstitutedAndGroups(personID), arrRights);
	}

	/**
	 * Gets the "projectsIDs to issueTypeID set" map the person has rights in
	 * 
	 * @param personIDs
	 * @param projectIDs
	 * @param arrRights
	 * @return
	 */
	public static List<Record> getProjectIssueTypeRecords(
			List<Integer> personIDs, Integer[] projectIDs, int[] arrRights) {
		return accessControlListDAO.getProjectIssueTypeRecords(
				personIDs, projectIDs, arrRights);
	}

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
	public static List<TAccessControlListBean> loadByProjectsAndRights(
			Integer[] projectIDs, int[] arrRights) {
		return accessControlListDAO.loadByProjectsAndRights(projectIDs,	arrRights);
	}

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
	public static List<TAccessControlListBean> loadByPersonsAndProjects(
			List<Integer> selectedPersons, List<Integer> selectedProjects) {
		return accessControlListDAO.loadByPersonsAndProjects(selectedPersons, selectedProjects);
	}

	/**
	 * Load a list of AccessControlListBeans by person, projects and right
	 * 
	 * @param personIDs
	 * @param projectID
	 * @param rights
	 * @return
	 */
	public static List<TAccessControlListBean> loadByPersonProjectsRight(
			List<Integer> personIDs, List<Integer> projectIDs, int[] rights) {
		return accessControlListDAO.loadByPersonProjectsRight(personIDs, projectIDs, rights);
	}

	/**
	 * Load a list of AccessControlListBeans by person, projects and role
	 * 
	 * @param personIDs
	 * @param projectID
	 * @param role
	 * @return
	 */
	public static List<TAccessControlListBean> loadByPersonProjectsRole(
			List<Integer> personIDs, List<Integer> projectIDs, Integer role) {
		return accessControlListDAO.loadByPersonProjectsRole(personIDs, projectIDs, role);
	}

	/**
	 * Load a list of AccessControlListBeans by project, roles and listType
	 * 
	 * @param projectIDs
	 * @param roles
	 * @param listType
	 *            if null do not filter by
	 * @return
	 */
	public static List<TAccessControlListBean> loadByProjectsRolesListType(
			List<Integer> projectIDs, Object[] roles, Integer listType) {
		return accessControlListDAO.loadByProjectsRolesListType(projectIDs, roles, listType);
	}
}
