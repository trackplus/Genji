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
import java.util.Map;
import java.util.Set;

import com.aurel.track.beans.TGroupMemberBean;

/**
 * DAO for GroupMember 
 * @author Tamas Ruff
 *
 */
public interface GroupMemberDAO {
	
	/**
	 * Load all assignments
	 * @return
	 */
	List<TGroupMemberBean> loadAll();
	
	/**
	 * Loads the number of persons in groups 
	 * @return
	 */
	Map<Integer, Integer> loadNumberOfPersonsInAllGroups();
	
	/**
	 * Loads the number of persons in group
	 * @param groupID
	 * @return
	 */
	Integer loadNumberOfPersonsInGroup(Integer groupID);
	
	/**
	 * Loads the GroupMemberBeans for a person 
	 * @param personID
	 * @return
	 */
	List<TGroupMemberBean> loadGroupsForPerson(Integer personID);
	
	/**
	 * Loads the GroupMemberBeans for a group 
	 * @param personID
	 * @return
	 */
	List<TGroupMemberBean> loadPersonsForGroups(List<Integer> groupID);
	
	
	/**
	* Whether a person is member in a group
	* @param person
	* @param group
	* @return
	*/
	boolean isPersonMemberInGroup(Integer person, Integer group);

	/**
	 * Whether any person from list person is member in the group 
	 * @param personIDs
	 * @param groupID
	 * @return
	 */
	boolean isAnyPersonMemberInGroup(List<Integer> personIDs, Integer groupID);
		
	/**
	 * Whether a person is member in any group from list
	 * @param personID
	 * @param groupIDs
	 * @return
	 */
	boolean isPersonMemberInAnyGroup(Integer personID, List<Integer> groupIDs);
	
	/**
	 * Whether any person from list is member in any group from list
	 * @param personIDs
	 * @param groupIDs
	 * @return
	 */
	boolean isAnyPersonMemberInAnyGroup(List<Integer> personIDs, List<Integer> groupIDs);
	
	/**
	* Get the list of group IDs the person is member of  
	* @param personID
	* @return
	*/
	List<Integer> getGroupsIDsForPerson(Integer personID);

	/**
	* Get the set of group IDs any person is member of  
	* @param personIDs
	* @return
	*/
	Set<Integer> getGroupsIDsForPersons(Integer[] personIDs);
	
	/**
	 * Save TGroupMemberBean
	 * @param groupMemberBean
	 * @return
	 */
	Integer save(TGroupMemberBean groupMemberBean);
	
	/**
	 * Deletes a TGroupMemberBean by groupID and personID
	 * @param groupID
	 * @param personID
	 */
	void delete(Integer groupID, Integer personID);
}

