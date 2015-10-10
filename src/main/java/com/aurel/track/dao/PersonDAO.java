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

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.aurel.track.beans.TPersonBean;

/**
 * <p>
 * This is the Data Access Object (DAO) interface for persons and groups.
 * Persons and groups are treated the same in many places throughout the
 * application; the difference between a group and a person is just marked
 * by a single attribute value.
 * <p>
 * <p>
 * A session is always related to a person. An anonymous session means
 * that it is run under a special user, the "guest" user. This user cannot be deleted.
 * </p>
 * Persons and groups (not the group members, just the groups themselves)
 * are persisted in table <code>TPERSON</code>.
 * 
 */
public interface PersonDAO {
	/**
	 * Gets a personBean from the TPerson table
	 * @param objectID
	 * @return
	 */
	TPersonBean loadByPrimaryKey(Integer objectID);
	
	/**
	 * Load all persons substituted by a person 
	 * @param substituteID
	 * @return
	 */
	List<Integer> loadSubstitutedPersons(Integer substituteID);
	
	/**
	 * Loads the number of persons in departments 
	 * @return
	 */
	Map<Integer, Integer> loadNumberOfPersonsInDepartments(List<Integer> departmentIDs);
	
	/**
	 * Gets the persons who might need reminder e-mails
	 * @param date
	 * @return
	 */
	List<TPersonBean> loadDailyReminderPersons(Date date);
	
	/**
	 * Gets the persons who need basket reminder e-mails
	 *  @param fromDate
	 * @param toDate
	 * @return
	 */
	List<TPersonBean> loadBasketReminderPersons(Date fromDate, Date toDate);
	
	/**
	 * @return List of Persons with the specific keys 
	 * @throws Exception
	 */
	List<TPersonBean> loadByKeys(List<Integer> personIDs);
	
	/**
	 * Gets the personBeans by uuid list
	 * @param uuids
	 * @return
	 */
	List<TPersonBean> loadByUUIDs(List<String> uuids); 
	
	/**
	 * Gets the personBeans by emails list
	 * @param emails
	 * @return
	 */
	//List<TPersonBean> loadByEmails(List<String> emails);
	
	/**
	 * Gets the person(s) with an email
	 * @param email
	 * @return
	 */
	List<TPersonBean> loadByEmail(String email);
	
	/**
	 * Gets a personBean from the TPerson table by userName
	 * @param userName	 
	 * @return
	 */
	TPersonBean loadByLoginName(String userName);
	
	/**
	 * Gets a personBean from the TPerson table by groupname
	 * @param groupName
	 * @return
	 */
	TPersonBean loadGroupByName(String groupName);
	
	/**
	 * Whether a combination of lastname, firstname and email already exists
	 * @param lastname
	 * @param firstname
	 * @param email
	 * @param personKey
	 * @return
	 */
	boolean nameAndEmailExist(String lastname, String firstname, String email, Integer personKey);
	
	/**
	 * Load a person by firstName and lastName
	 * @param firstName
	 * @param lastName
	 * @return
	 */
	TPersonBean loadByFirstNameLastName(String firstName, String lastName);
	
	/**
	 * Load all system administrators
	 */
	List<TPersonBean> loadSystemAdmins();
	
	/**
	 * Activate or deactivate the persons 
	 * @param persons
	 * @param deactivate
	 */
	void activateDeactivatePersons(List<Integer> persons, boolean deactivate);
	
	/**
	 * Set the user level for persons 
	 * @param persons
	 * @param deactivate
	 */
	void setUserLevelPersons(List<Integer> persons, Integer userLevel); 
	
	/**
	 * Load all persons
	 * @return
	 */
	List<TPersonBean> loadPersons();
	
	/**
	 * Load all clients
	 * @return
	 */
	List<TPersonBean> loadAllClients();
	
	/**
	 * Load all users except client users
	 * @return
	 */
	List<TPersonBean> loadAllUsers();
	
	
		
	/**
	 * Loads the active persons
	 * @param actualValue
	 * @return
	 */
	List<TPersonBean> loadActivePersons();
	
	/**
	 * Load the active persons and groups
	 * @return
	 */
	List<TPersonBean> loadActivePersonsAndGroups();
	
	/**
	 * Load all groups
	 * @return
	 */
	List<TPersonBean> loadGroups();
	
	/**
	 * Loads all persons and groups
	 * @return
	 */
	List<TPersonBean> loadPersonsAndGroups();
	
	
	/**
	 * Loads all persons and groups except client
	 * @return
	 */
	//List<TPersonBean> loadPersonsAndGroupsExceptClient();
	
	/**
	 * Loads the persons with any of the specified roles in a project
	 * @param projectIDs
	 * @param roleIDs
	 * @return
	 */
	List<TPersonBean> loadByProjectAndRoles(List<Integer> projectIDs, List<Integer> roleIDs);
	
	/**
	 * Gets the logged in users
	 * @return
	 */
	List<TPersonBean> getLoggedInUsers();
	
	/**
	 * Count the users 
	 * @param disabled
	 * @param limited
	 * @return
	 */
	Integer countUsers(boolean disabled, boolean limited);
	
	/**
	 * Count the user by user levels
	 * @param userLevels
	 * @return
	 */
	int countByUserLevels(List<Integer> userLevels, boolean disabled);
	
	/**
	 * Saves a personBean in the TPerson table
	 * @param personBean
	 * @return
	 */
	Integer save(TPersonBean personBean);
	
	/**
	 * Whether any person from the list has dependent data
	 * @param personID
	 * @return
	 */
	boolean hasDependentPersonData(List<Integer> personIDs);
	
	/**
	 * The reflection does not work because an additional condition 
	 * should be satisfied (no direct foreign key relationship exists)
	 * @param personIDs
	 */
	//boolean hasDependencyInUserPicker(List<Integer> personIDs);
	
	/**
	 * The reflection does not work because an additional condition 
	 * should be satisfied (no direct foreign key relationship exists)
	 * @param personIDs
	 */
	//boolean hasDependencyInHistory(List<Integer> personIDs, boolean newValues); 
	
	/**
	 * Whether the group has dependent data
	 * @param groupID
	 * @return
	 */
	boolean hasDependentGroupData(List<Integer> groupIDs);	
	
	/**
	 * Deletes a person without dependences
	 * @param personID
	 */
	void deletePerson(Integer personID);
	
	/**
	 * Deletes a group without dependences
	 * @param oldGroup
	 */
	void deleteGroup(Integer oldGroup);
	
	/**
	 * The reflection does not work because an additional condition 
	 * should be satisfied (no direct foreign key relationship exists)
	 * @param oldPersonID
	 * @param newPersonID
	 */
	void replaceUserPicker(Integer oldPersonID, Integer newPersonID);
	
	/**
	 * The reflection does not work because an additional condition 
	 * should be satisfied (no direct foreign key relationship exists)
	 * @param oldPersonID
	 * @param newPersonID
	 * @param newValues
	 */
	void replaceHistoryPerson(Integer oldPersonID, Integer newPersonID, boolean newValues);
	
	/**
	 * Replaces the dependences with a new personID and 
	 * deletes the old personID from the TPerson table 
	 * @param oldPersonID
	 * @param newPersonID
	 */
	void replacePerson(Integer oldPersonID, Integer newPersonID);
	
	/**
	 * Replaces the dependences with a new personID and 
	 * deletes the old personID from the TPerson table 
	 * @param oldGroupID
	 * @param newGroupID
	 */
	void replaceGroup(Integer oldGroupID, Integer newGroupID);
	
	/**
	 * Gets a list of personBeans ordered by isGroup, lastname, firstname, loginname 
	 * @param objectIDs
	 * @return
	 */
	List<TPersonBean> loadSortedPersonsOrGroups(List<Integer> objectIDs);
	
	/**
	 * Returns the real persons which are directly present in the list
	 * The list may contain both personID-s and groupID-s
	 * @param personIDs
	 * @param excludeInactive
	 * @param currentOption
	 * @return
	 */
	List<TPersonBean> getDirectPersons(List<Integer> personIDs, boolean excludeInactive, Integer currentOption);
	
	
	/**
	 * Returns the real persons which are indirectly (through group) present in the list
	 * The list may contain both personID-s and groupID-s
	 * @param personIDs
	 * @param excludeInactive
	 * @param currentOption
	 *@return
	 */
	List<TPersonBean> getIndirectPersons(List<Integer> personIDs, 
			boolean excludeInactive, Integer currentOption);
	
	/**
	 * Returns the real groups which are directly present in the list
	 * The list may contain both personID-s and groupID-s
	 * @param personIDs
	 * @param excludeInactive
	 * @return
	 */
	List<TPersonBean> getDirectGroups(List<Integer> personIDs, boolean excludeInactive);
	
	/**
	 * Get the persons with any RACI role (informant or consultant)
	 * for a workItemKey  
	 * @param workItemKey
	 * @return
	 */
	List<TPersonBean> loadNotifyThroughRaci(Integer workItemKey); 
	
	/**
	 * Get the persons who have at least one work in a project or release and listType
	 * @param entityID
	 * @param entityType
	 * @param listType
	 * @return
	 */
	List<TPersonBean> getPersonsWithWorkInProject(Integer entityID, int entityType, Integer listType);
	
	/**
	 * Load the persons belonging to a group
	 * @param groupKey
	 * @return
	 */
	List<TPersonBean> loadPersonsForGroup(Integer groupKey);
	
	/**
	 * Load the groups a person belongings to
	 * @param personID
	 * @return
	 */
	List<TPersonBean> loadGroupsForPerson(Integer personID);
	
	/**
	 * Load the groups any person from personIDs belongs to
	 * @param personIDs
	 * @return
	 */
	List<TPersonBean> loadGroupsForPersons(List<Integer> personIDs);
	
	/**
	 * Load the persons from a department
	 * @param departmentID
	 * @return
	 */
	List<TPersonBean> loadPersonsForDepartment(Integer departmentID);
	
	/**
	 * Load the persons belonging to any group from an array of groups
	 * @param groupKeys
	 * @return
	 */
	List<TPersonBean> loadPersonsForGroups(List<Integer> groupKeys);
	
	/**
	 * Get the direct RACI persons or groups
	 * @param workItemKey
	 * @param group
	 * @param raciRole
	 * @return
	 */
	List<TPersonBean> getDirectRaciPersons(Integer workItemKey, boolean group, String raciRole);

	/**
	 * Returns the direct informants (groups or persons)
	 * @param workItemKey
	 * @return
	 */
	List<TPersonBean> getDirectInformants(Integer workItemKey);
	
	/**
	 * Returns the direct consultant (groups or persons) 
	 * @param workItemKey
	 * @return
	 */
	List<TPersonBean> getDirectConsultants(Integer workItemKey);

	/**
	 * Load the distinct persons who 
	 * have added cost/effort to a workItem
	 * @param workItemKey
	 * @return
	 */
	List<TPersonBean> loadPersonsWithEffort(Integer workItemKey);
	
	/**
	 * Load the persons which are managers 
	 * for at least one workItem in any of the projects   
	 * @param projects  
	 * @return
	 */
	List<TPersonBean> loadUsedManagersByProjects(Integer person, Integer[] projects);

	/**
	 * Load the persons/groups which are responsibles 
	 * for at least one workItem in any of the projects   
	 * @param projects  
	 * @return
	 */
	List<TPersonBean> loadUsedResonsiblesByProjects(Integer person, Integer[] projects);
	
	/**
	 * Load the persons which are originators 
	 * for at least one workItem in any of the projects   
	 * @param projects  
	 * @return
	 */
	List<TPersonBean> loadUsedOriginatorsByProjects(Integer person, Integer[] projects);
	
	/**
	 * Load the persons which are last edited persons 
	 * for at least one workItem in any of the projects  
	 * @param projects
	 * @return
	 */
	List<TPersonBean> loadUsedLastEditedByProjects(Integer[] projects);
	
	/**
	 * Load the persons/groups which are consultants/informants 
	 * for at least one workItem in any of the projects   
	 * @param projects 
	 * @param raciRole
	 * @return
	 */
	List<TPersonBean> loadUsedConsultantsInformantsByProjects(Integer[] projects, String raciRole);
	
	/**
	 * Load the persons which are picked persons 
	 * for at least one workItem in any of the projects  
	 * @param projects
	 * @return
	 */
	List<TPersonBean> loadUsedUserPickersByProjects(Integer fieldID, Integer[] projects);
	
	/**
	 * Load the persons/groups which are consultants/informants 
	 * for at least one workItem from the array
	 * @param workItemIDs  
	 * @param raciRole
	 * @return
	 */
	List<TPersonBean> loadUsedConsultantsInformantsByWorkItemIDs(List<Integer> workItemIDs, String raciRole);
	
	/**
	 * @return List of personbeans from departments
	 * @throws Exception
	 */
	List<TPersonBean> loadByDepartments(Integer[] departmentKeys);
	
	/**
	 * Loads the persons from the departments
	 * @param departmentIDs
	 * @param currentOptions
	 * @return
	 */
	List<TPersonBean> loadByDepartments(List<Integer> departmentIDs, Integer[] currentOptions);
	
	/**
	 * Gets the persons who added attachment for any of the workItems
	 * @param workItemIDs
	 * @return
	 */
	List<TPersonBean> getAttachmentPersons(int[] workItemIDs);
	
	/**
	 * Returns the persons having explicit automail settings in any of the projects having a trigger set with at least one observer field 
	 * @param personIDs
	 * @param actionType
	 * @param isCreated
	 * @return
	 */
	List<TPersonBean> getObserverPersonsInProjects(List<Integer> personIDs, List<Integer> projects, Integer actionType);
	
	/**
	 * Returns the newly registered person with this tokenPasswd
	 * @param tokenPasswd
	 * @return
	 */	
	TPersonBean loadByTokenPasswd(String tokenPasswd);

	/**
	 * Returns the person for which a new password wsa requested.
	 * @param tokenPasswd
	 * @return
	 */	
	TPersonBean loadByForgotPasswordToken(String tokenPasswd);

	/**
	 * Remove the tokens for the forgot password links in case somebody tried to reset the password
	 * for another person
	 * 
	 */
	void cancelForgotPasswordTokens();
	
	/**
	 * Remove users that have registered themselves and that have not confirmed their registration
	 * within the grace period.
	 * 
	 */
	void removeUnconfirmedUsers();
}
