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

package com.aurel.track.admin.user.group;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TGroupMemberBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.GroupMemberDAO;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.prop.ApplicationBean;

/**
 * @author Tamas
 *
 */
public class GroupMemberBL {
	private static final Logger LOGGER = LogManager.getLogger(GroupMemberBL.class);
	private static GroupMemberDAO groupMemberDAO = DAOFactory.getFactory().getGroupMemberDAO();
	
	/**
	 * Whether the person is member in the group 
	 * @param personID
	 * @param groupID
	 * @return
	 */
	public static boolean isPersonMemberInGroup(Integer personID, Integer groupID) {
		return groupMemberDAO.isPersonMemberInGroup(personID, groupID);
	}
	
	/**
	 * Whether the person is member in the group 
	 * @param personID
	 * @param groupID
	 * @return
	 */
	public static boolean isAnyPersonMemberInGroup(List<Integer> personIDs, Integer groupID) {
		return groupMemberDAO.isAnyPersonMemberInGroup(personIDs, groupID);
	}
	
	/**
	 * Whether a person is member in any group from list
	 * @param personID
	 * @param groupIDs
	 * @return
	 */
	public static boolean isPersonMemberInAnyGroup(Integer personID, List<Integer> groupIDs) {
		return groupMemberDAO.isPersonMemberInAnyGroup(personID, groupIDs);
	}
	
	/**
	 * Whether any person from list is member in any group from list
	 * @param personIDs
	 * @param groupIDs
	 * @return
	 */
	public static boolean isAnyPersonMemberInAnyGroup(List<Integer> personIDs, List<Integer> groupIDs) {
		return groupMemberDAO.isAnyPersonMemberInAnyGroup(personIDs, groupIDs);
	}
	
	/**
	* Get the list of group IDs the person is member of  
	* @param personID
	* @return
	*/
	public static List<Integer> getGroupsIDsForPerson(Integer personID) {
		return groupMemberDAO.getGroupsIDsForPerson(personID);
	}
	
	/**
	* Get the set of group IDs any person is member of  
	* @param personIDs
	* @return
	*/
	public static Set<Integer> getGroupsIDsForPersons(Integer[] personIDs) {
		return groupMemberDAO.getGroupsIDsForPersons(personIDs);
	}
	
	/**
	 * Load all assignments
	 * @return
	 */
	public static List<TGroupMemberBean> loadAllAssignments() {
		return groupMemberDAO.loadAll();
	}
	
	/**
	 * Loads the number of persons in groups 
	 * @return
	 */
	public static Map<Integer, Integer> loadNumberOfPersonsInAllGroups() {
		return groupMemberDAO.loadNumberOfPersonsInAllGroups();
	}
	
	/**
	 * Loads the number of persons in group
	 * @param groupID
	 * @return
	 */
	public static Integer loadNumberOfPersonsInGroup(Integer groupID) {
		return groupMemberDAO.loadNumberOfPersonsInGroup(groupID);
	}
	
	/**
	 * Saves a group to person assignment
	 * @param groupID
	 * @param personID
	 * @return
	 */
	public static Integer saveGroupMember(Integer groupID, Integer personID) {
		TGroupMemberBean groupMemberBean = new TGroupMemberBean();
		groupMemberBean.setTheGroup(groupID);
		groupMemberBean.setPerson(personID);
		return groupMemberDAO.save(groupMemberBean);
	}
	
	/**
	 * Deletes a TGroupMemberBean by groupID and personID
	 * @param groupID
	 * @param personID
	 */
	public static void delete(Integer groupID, Integer personID) {
		groupMemberDAO.delete(groupID, personID);
	}

	/**
	 * Synchronize a group to users map by group name and loginname with the database users and groups
	 * @param groupToPersonsMap
	 * @return
	 * @throws Exception
	 */
	public static Map<String, TPersonBean> synchronizeUserListWithGroup(Map<String, List<TPersonBean>> groupToPersonsMap) 
			throws Exception {
		Map<String, TPersonBean> ldapPersonsMap = new HashMap<String, TPersonBean>();
		List<TPersonBean> dbasePersons = PersonBL.loadPersons();
		Map<String, TPersonBean> dbPersonsByLoginNameMap = new HashMap<String, TPersonBean>();
		for (TPersonBean dbasePerson: dbasePersons) {
			dbPersonsByLoginNameMap.put(dbasePerson.getLoginName(), dbasePerson);
		}
		List<TPersonBean> dbaseGroups = PersonBL.loadGroups();
		Map<String, TPersonBean> dbGroupsMapByLoginName = new HashMap<String, TPersonBean>();
		for (TPersonBean dbaseGroup: dbaseGroups) {
			dbGroupsMapByLoginName.put(dbaseGroup.getLoginName(), dbaseGroup);
		}
		Map<Integer, Set<Integer>> groupIDToPersonsIDMap = new HashMap<Integer, Set<Integer>>();
		List<TGroupMemberBean> dbAssignments = loadAllAssignments();
		if (dbAssignments!=null) {
			for (TGroupMemberBean groupMemberBean : dbAssignments) {
				Integer groupID = groupMemberBean.getTheGroup();
				Integer personID = groupMemberBean.getPerson();
				Set<Integer> personIDs = groupIDToPersonsIDMap.get(groupID);
				if (personIDs==null) {
					personIDs = new HashSet<Integer>();
					groupIDToPersonsIDMap.put(groupID, personIDs);
				}
				personIDs.add(personID);
			}
		}
		int groupCount = 0;
		int personCount = 0;
		int personToGroupAdded = 0;
		int personToGroupRemoved = 0;
		for (Map.Entry<String,List<TPersonBean>> entry : groupToPersonsMap.entrySet()) {
			String ldapGroupName = entry.getKey();
			if (ldapGroupName!=null) {
				TPersonBean dbGroupBean = dbGroupsMapByLoginName.get(ldapGroupName);
				Integer groupID = null;
				if (dbGroupBean==null) {
					//add group if not exist
					dbGroupBean = new TPersonBean();
					dbGroupBean.setLoginName(ldapGroupName);
					dbGroupBean.setIsGroupBool(true);
					LOGGER.info("Adding group " + ldapGroupName + " to Genji database.");
					groupID = PersonBL.save(dbGroupBean);
					groupCount++;
					dbGroupsMapByLoginName.put(ldapGroupName, dbGroupBean);
				} else {
					groupID = dbGroupBean.getObjectID();
				}
				Set<Integer> dbPersonsInGroup = groupIDToPersonsIDMap.get(groupID);
				List<TPersonBean> ldapPersonsForGroup = entry.getValue();
				if (ldapPersonsForGroup!=null) {
					//add persons (from group)
					for (TPersonBean ldapPerson: ldapPersonsForGroup) {
						String ldapLoginame = ldapPerson.getLoginName();
						String ldapFirstname = ldapPerson.getFirstName();
						String ldapLastname = ldapPerson.getLastName();
						String ldapEmail = ldapPerson.getEmail();
						String phone = ldapPerson.getPhone();
						ldapPersonsMap.put(ldapLoginame, ldapPerson);
						TPersonBean dbPerson = dbPersonsByLoginNameMap.get(ldapLoginame);
						boolean saveNeeded = false;
						Integer personID = null;
						if (dbPerson!=null) {
							personID = dbPerson.getObjectID();
							//person exist in db
							saveNeeded = !PersonBL.isLdapPersonSame(dbPerson, ldapPerson);
							if (saveNeeded) {
								PersonBL.updateLdapPerson(dbPerson, ldapFirstname, ldapLastname, ldapEmail, phone);
								LOGGER.info("Existing user " + ldapFirstname +" "+ ldapLastname 
										+ " ("+ldapEmail+") to be changed in Genji database.");
							}
						} else {
							//person does not exist in db
							dbPerson = PersonBL.createLdapPerson(ldapLoginame, ldapFirstname, ldapLastname, ldapEmail, phone);
							dbPersonsByLoginNameMap.put(ldapLoginame, dbPerson);
							saveNeeded = true;
							++personCount;
							LOGGER.info("Adding user " + ldapFirstname +" "+ ldapLastname 
									+ " ("+ldapEmail+") to Genji database.");
						}
						if (saveNeeded) {
							personID = PersonBL.saveAndAddMenuFilters(dbPerson);
							dbPersonsByLoginNameMap.put(ldapLoginame, dbPerson);
						}
						if (dbPersonsInGroup==null) {
							LOGGER.debug("Person " + ldapLoginame + " added to group " + ldapGroupName);
							personToGroupAdded++;
							saveGroupMember(groupID, personID);
						} else {
							if (!dbPersonsInGroup.contains(personID)) {
								LOGGER.debug("Person " + ldapLoginame + " added to group " + ldapGroupName);
								personToGroupAdded++;
								saveGroupMember(groupID, personID);
							} 
							dbPersonsInGroup.remove(personID);
						}
					}
					if (dbPersonsInGroup!=null && !dbPersonsInGroup.isEmpty()) {
						//remove those person to group associations which are not present on ldap
						for (Integer personID : dbPersonsInGroup) {
							if (LOGGER.isDebugEnabled()) {
								TPersonBean personBean = LookupContainer.getPersonBean(personID);
								LOGGER.debug("PersonID " + personBean==null?"":personBean.getLoginName() + "(" + personID + ") removed from group " + ldapGroupName);
							}
							personToGroupRemoved++;
							delete(groupID, personID);
						}
					}
				}
			}
		}
		if (personCount>0) {
			ApplicationBean.getInstance().setActualUsers();
		}
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Group and user sync results for " + groupToPersonsMap.size() + " ldap groups and " + ldapPersonsMap.size() + " ldap member persons");
			if (groupCount==0) {
				LOGGER.info("LDAP directory groups are in sync with Genji database.");
			} else {
					LOGGER.info(groupCount + " group(s) from LDAP directory added to Genji database.");
			}
			if (personCount==0) {
				LOGGER.info("LDAP directory users are in sync with Genji database.");
			} else  {
					LOGGER.info(personCount + " user(s) from LDAP directory added to Genji database.");
			}
			if (personToGroupAdded>0) {
				LOGGER.info(personToGroupAdded + " user to group assignment(s) added in Genji database.");
			}
			if (personToGroupRemoved>0) {
				LOGGER.info(personToGroupRemoved + " user to group assignment(s) removed from Genji database.");
			}
		}
		return ldapPersonsMap;
	}
}
