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
import java.util.Map;

import com.trackplus.model.Tperson;

/**
 * <p>
 * This is the Data Access Object (DAO) interface for persons and groups.
 * Persons and groups are treated the same in many places throughout the
 * application; the difference between a group and a person is just marked by a
 * single attribute value.
 * <p>
 * <p>
 * A session is always related to a person. An anonymous session means that it
 * is run under a special user, the "guest" user. This user cannot be deleted.
 * </p>
 * Persons and groups (not the group members, just the groups themselves) are
 * persisted in table <code>TPERSON</code>.
 * 
 */
public interface PersonDAO {
	/**
	 * Gets a personBean from the TPerson table
	 * 
	 * @param objectID
	 * @return
	 */
	Tperson loadByPrimaryKey(Integer objectID);

	/**
	 * Loads the number of persons in departments
	 * 
	 * @return
	 */
	Map<Integer, Integer> loadNumberOfPersonsInDepartments(
			List<Integer> departmentIDs);

	/**
	 * Gets the persons who need reminder e-mails
	 * 
	 * @return
	 */
	List<Tperson> loadReminderPersons();

	/**
	 * @return List of Persons with the specific keys
	 * @throws Exception
	 */
	List<Tperson> loadByKeys(List<Integer> personIDs);

	/**
	 * Gets the personBeans by uuid list
	 * 
	 * @param uuids
	 * @return
	 */
	List<Tperson> loadByUUIDs(List<String> uuids);

	/**
	 * Gets the personBeans by emails list
	 * 
	 * @param emails
	 * @return
	 */
	List<Tperson> loadByEmails(List<String> emails);

	List<Tperson> loadByEmail(String emails);

	/**
	 * Gets a personBean from the TPerson table by userName
	 * 
	 * @param userName
	 * @return
	 */
	Tperson loadByLoginName(String userName);

	/**
	 * Gets a personBean from the TPerson table by groupname
	 * 
	 * @param groupName
	 * @return
	 */
	Tperson loadGroupByName(String groupName);

	/**
	 * Whether a combination of lastname, firstname and email already exists
	 * 
	 * @param lastname
	 * @param firstname
	 * @param email
	 * @param personKey
	 * @return
	 */
	boolean nameAndEmailExist(String lastname, String firstname, String email,
			Integer personKey);

	/**
	 * Load a person by firstName and lastName
	 * 
	 * @param firstName
	 * @param lastName
	 * @return
	 */
	Tperson loadByFirstNameLastName(String firstName, String lastName);

	/**
	 * Load all system administrators
	 */
	List<Tperson> loadSystemAdmins();

	/**
	 * Activate or deactivate the persons
	 * 
	 * @param persons
	 * @param deactivate
	 */
	void activateDeactivatePersons(List<Integer> persons, boolean deactivate);

	/**
	 * Set the user level for persons
	 * 
	 * @param persons
	 * @param deactivate
	 */
	void setUserLevelPersons(List<Integer> persons, Integer userLevel);

	/**
	 * Load all persons
	 * 
	 * @return
	 */
	List<Tperson> loadPersons();

	/**
	 * Loads the active persons
	 * 
	 * @param actualValue
	 * @return
	 */
	List<Tperson> loadActivePersons();

	/**
	 * Load the active persons and groups
	 * 
	 * @return
	 */
	List<Tperson> loadActivePersonsAndGroups();

	/**
	 * Load all groups
	 * 
	 * @return
	 */
	List<Tperson> loadGroups();

	/**
	 * Loads all persons and groups
	 * 
	 * @return
	 */
	List<Tperson> loadPersonsAndGroups();

	/**
	 * Loads the persons with any of the specified roles in a project
	 * 
	 * @param projectIDs
	 * @param roleIDs
	 * @return
	 */
	List<Tperson> loadByProjectAndRoles(List<Integer> projectIDs,
			List<Integer> roleIDs);

	Integer countUsers(boolean disabled, boolean limited);

	/**
	 * Saves a personBean in the TPerson table
	 * 
	 * @param personBean
	 * @return
	 */
	Integer save(Tperson personBean);

	/**
	 * Whether the person has dependent data
	 * 
	 * @param personID
	 * @return
	 */
	boolean hasDependentPersonData(Integer personID);

	/**
	 * The reflection does not work because an additional condition should be
	 * satisfied (no direct foreign key relationship exists)
	 * 
	 * @param oldPersonID
	 * @param newPersonID
	 */
	boolean hasDependencyInUserPicker(Integer oldPersonID);

	/**
	 * The reflection does not work because an additional condition should be
	 * satisfied (no direct foreign key relationship exists)
	 * 
	 * @param oldPersonID
	 * @param newPersonID
	 */
	boolean hasDependencyInHistory(Integer oldPersonID, boolean newValues);

	/**
	 * Whether the group has dependent data
	 * 
	 * @param groupID
	 * @return
	 */
	boolean hasDependentGroupData(Integer groupID);

	/**
	 * Deletes a person without dependences
	 * 
	 * @param personID
	 */
	void deletePerson(Integer personID);

	/**
	 * Deletes a group without dependences
	 * 
	 * @param oldGroup
	 */
	void deleteGroup(Integer oldGroup);

	/**
	 * The reflection does not work because an additional condition should be
	 * satisfied (no direct foreign key relationship exists)
	 * 
	 * @param oldPersonID
	 * @param newPersonID
	 */
	void replaceUserPicker(Integer oldPersonID, Integer newPersonID);

	/**
	 * The reflection does not work because an additional condition should be
	 * satisfied (no direct foreign key relationship exists)
	 * 
	 * @param oldPersonID
	 * @param newPersonID
	 * @param newValues
	 */
	void replaceHistoryPerson(Integer oldPersonID, Integer newPersonID,
			boolean newValues);

	/**
	 * Replaces the dependences with a new personID and deletes the old personID
	 * from the TPerson table
	 * 
	 * @param oldPersonID
	 * @param newPersonID
	 */
	void replacePerson(Integer oldPersonID, Integer newPersonID);

	/**
	 * Replaces the dependences with a new personID and deletes the old personID
	 * from the TPerson table
	 * 
	 * @param oldGroupID
	 * @param newGroupID
	 */
	void replaceGroup(Integer oldGroupID, Integer newGroupID);

	/**
	 * Gets a list of personBeans ordered by isGroup, lastname, firstname,
	 * loginname
	 * 
	 * @param objectIDs
	 * @return
	 */
	List<Tperson> loadSortedPersonsOrGroups(List<Integer> objectIDs);

	/**
	 * Returns the real persons which are directly present in the list The list
	 * may contain both personID-s and groupID-s
	 * 
	 * @param personIDs
	 * @param excludeInactive
	 * @param currentOption
	 * @return
	 */
	List<Tperson> getDirectPersons(List<Integer> personIDs,
			boolean excludeInactive, Integer currentOption);

	/**
	 * Returns the real persons which are indirectly (through group) present in
	 * the list The list may contain both personID-s and groupID-s
	 * 
	 * @param personIDs
	 * @param excludeInactive
	 * @param currentOption
	 * @return
	 */
	List<Tperson> getIndirectPersons(List<Integer> personIDs,
			boolean excludeInactive, Integer currentOption);

	/**
	 * Returns the real groups which are directly present in the list The list
	 * may contain both personID-s and groupID-s
	 * 
	 * @param personIDs
	 * @param excludeInactive
	 * @return
	 */
	List<Tperson> getDirectGroups(List<Integer> personIDs,
			boolean excludeInactive);

	/**
	 * Gets the list of TPersonBeans which have the priorityWarningLevel or
	 * severityWarningLevel less or equal as the ones given as parameter
	 * 
	 * @param keys
	 * @param priorityWarningLevel
	 * @param severityWarningLevel
	 * @return
	 */
	List<Tperson> loadByKeysAndWarningLevels(List<Integer> personIDs,
			Integer priorityWarningLevel, Integer severityWarningLevel);

	/**
	 * Get the persons with any RACI role (informant or consultant) for a
	 * workItemKey
	 * 
	 * @param workItemKey
	 * @return
	 */
	List<Tperson> loadNotifyThroughRaci(Integer workItemKey);

	/**
	 * Get the persons who have at least one work in a project or release and
	 * listType
	 * 
	 * @param entityID
	 * @param entityType
	 * @param listType
	 * @return
	 */
	List<Tperson> getPersonsWithWorkInProject(Integer entityID, int entityType,
			Integer listType);

	/**
	 * Load the persons belonging to a group
	 * 
	 * @param groupKey
	 * @return
	 */
	List<Tperson> loadPersonsForGroup(Integer groupKey);

	/**
	 * Load the groups a person belongings to
	 * 
	 * @param personID
	 * @return
	 */
	List<Tperson> loadGroupsForPerson(Integer personID);

	/**
	 * Load the persons from a department
	 * 
	 * @param departmentID
	 * @return
	 */
	List<Tperson> loadPersonsForDepartment(Integer departmentID);

	/**
	 * Load the persons belonging to any group from an array of groups
	 * 
	 * @param groupKeys
	 * @return
	 */
	List<Tperson> loadPersonsForGroups(List<Integer> groupKeys);

	/**
	 * Get the direct RACI persons or groups
	 * 
	 * @param workItemKey
	 * @param group
	 * @param raciRole
	 * @return
	 */
	List<Tperson> getDirectRaciPersons(Integer workItemKey, boolean group,
			String raciRole);

	/**
	 * Returns the direct informants (groups or persons)
	 * 
	 * @param workItemKey
	 * @return
	 */
	List<Tperson> getDirectInformants(Integer workItemKey);

	/**
	 * Returns the direct consultant (groups or persons)
	 * 
	 * @param workItemKey
	 * @return
	 */
	List<Tperson> getDirectConsultants(Integer workItemKey);

	/**
	 * Load the distinct persons who have added cost/effort to a workItem
	 * 
	 * @param workItemKey
	 * @return
	 */
	List<Tperson> loadPersonsWithEffort(Integer workItemKey);

	/**
	 * Load the persons which are managers for at least one workItem in any of
	 * the projects
	 * 
	 * @param projects
	 * @return
	 */
	List<Tperson> loadUsedManagersByProjects(Integer person, Integer[] projects);

	/**
	 * Load the persons/groups which are responsibles for at least one workItem
	 * in any of the projects
	 * 
	 * @param projects
	 * @return
	 */
	List<Tperson> loadUsedResonsiblesByProjects(Integer person,
			Integer[] projects);

	/**
	 * Load the persons which are originators for at least one workItem in any
	 * of the projects
	 * 
	 * @param projects
	 * @return
	 */
	List<Tperson> loadUsedOriginatorsByProjects(Integer person,
			Integer[] projects);

	/**
	 * Load the persons which are last edited persons for at least one workItem
	 * in any of the projects
	 * 
	 * @param projects
	 * @return
	 */
	List<Tperson> loadUsedLastEditedByProjects(Integer[] projects);

	/**
	 * Load the persons/groups which are consultants/informants for at least one
	 * workItem in any of the projects
	 * 
	 * @param projects
	 * @return
	 */
	List<Tperson> loadUsedConsultantsInformantsByProjects(Integer[] projects);

	/**
	 * Load the persons which are picked persons for at least one workItem in
	 * any of the projects
	 * 
	 * @param projects
	 * @return
	 */
	List<Tperson> loadUsedUserPickersByProjects(Integer fieldID,
			Integer[] projects);

	/**
	 * Load the persons/groups which are consultants/informants for at least one
	 * workItem from the array
	 * 
	 * @param workItemIDs
	 * @param raciRole
	 * @return
	 */
	public List<Tperson> loadUsedConsultantsInformantsByWorkItemIDs(
			List<Integer> workItemIDs, String raciRole);

	/**
	 * @return List of personbeans from departments
	 * @throws Exception
	 */
	public List<Tperson> loadByDepartments(Integer[] departmentKeys);

	/**
	 * Loads the persons from the departments
	 * 
	 * @param departmentIDs
	 * @param currentOptions
	 * @return
	 */
	public List<Tperson> loadByDepartments(List<Integer> departmentIDs,
			Integer[] currentOptions);

	/**
	 * Gets the persons who changed the budget for any of the workItems
	 * 
	 * @param workItemIDs
	 * @return
	 */
	List<Tperson> getBudgetChangePersons(int[] workItemIDs);

	/**
	 * Gets the persons who added expense for any of the workItems
	 * 
	 * @param workItemIDs
	 * @return
	 */
	List<Tperson> getExpensePersons(int[] workItemIDs);

	/**
	 * Gets the persons who added attachment for any of the workItems
	 * 
	 * @param workItemIDs
	 * @return
	 */
	List<Tperson> getAttachmentPersons(int[] workItemIDs);

	/**
	 * Returns the persons having explicit automail settings in any of the
	 * projects having a trigger set with at least one observer field
	 * 
	 * @param personIDs
	 * @param actionType
	 * @param isCreated
	 * @return
	 */
	List<Tperson> getObserverPersonsInProjects(List<Integer> personIDs,
			List<Integer> projects, Integer actionType);
}
