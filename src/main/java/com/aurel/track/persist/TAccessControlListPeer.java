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


package com.aurel.track.persist;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Criteria.Criterion;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.beans.TAccessControlListBean;
import com.aurel.track.dao.AccessControlListDAO;
import com.aurel.track.util.GeneralUtils;
import com.workingdogs.village.DataSetException;
import com.workingdogs.village.Record;

// Local classes

/** 
 *
 */
public class TAccessControlListPeer 
    extends com.aurel.track.persist.BaseTAccessControlListPeer
    implements AccessControlListDAO {

    private static final long serialVersionUID = 5826510476827383089L;

    private static final Logger LOGGER = LogManager.getLogger(TAccessControlListPeer.class); 
    
    /**
	 *  Loads a list of AccessControlListBeans by project and role
	 * @param projectID
	 * @param roleID
	 * @return
	 */
	@Override
	public List<TAccessControlListBean> loadByProjectAndRole(Integer projectID, Integer roleID) {
		Criteria crit = new Criteria();
		crit.add(PROJKEY, projectID);
		crit.add(ROLEKEY, roleID);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Getting the role assignments for project " + projectID + " and role " + roleID + " failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Loads a list of AccessControlListBeans by person
	 * @param personID
	 * @return
	 */
	@Override
	public List<TAccessControlListBean> loadByPerson(Integer personID) {
		Criteria crit = new Criteria();
		crit.add(PERSONKEY, personID);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Getting the role assignments for person " + personID + " failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Loads a list of AccessControlListBeans by person and project status flags
	 * @param personID
	 * @return
	 */
	
	/**
	 * Loads a list of AccessControlListBeans by persons
	 * @param personIDs
	 * @return
	 */
	@Override
	public List<TAccessControlListBean> loadByPersons(List<Integer> personIDs) {
		Criteria crit = new Criteria();
		if (personIDs!=null && !personIDs.isEmpty()) {
			crit.addIn(PERSONKEY, personIDs);
		} else {
			return new LinkedList<TAccessControlListBean>();
		}
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Getting the role assignments for persons " + personIDs.size() +  " failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Loads a list of AccessControlListBeans by persons and project status flags
	 * @param personIDs
	 * @return
	 */
	@Override
	public List<TAccessControlListBean> loadByPersonsAndProjectStatusFlag(List<Integer> personIDs, int[] projectStatusFlag) {
		Criteria crit = new Criteria();
		if (personIDs!=null && !personIDs.isEmpty()) {
			crit.addIn(PERSONKEY, personIDs);
		} else {
			return new LinkedList<TAccessControlListBean>();
		}
		if (projectStatusFlag!=null && projectStatusFlag.length!=0) {
			crit.addJoin(PROJKEY, TProjectPeer.PKEY);
			crit.addJoin(TProjectPeer.STATUS, TSystemStatePeer.OBJECTID);
			crit.addIn(TSystemStatePeer.STATEFLAG, projectStatusFlag);
		}
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Getting the role assignments for persons " + personIDs.size() + " and projectStatusFlags failed with " + e.getMessage());
			return null;
		}
	}

	/**
	 * Loads a list of AccessControlListBeans by project
	 * @param projectID
	 * @return
	 */
	@Override
	public List<TAccessControlListBean> loadByProject(Integer projectID) {
		Criteria criteria = new Criteria();
		criteria.add(PROJKEY, projectID);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Getting the rights by project " + projectID + " failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Whether the person has right in any project with certain status flags 
	 * @param personIDs
	 * @param arrRight
	 * @param projectStatusFlag
	 * @return
	 */
	@Override
	public List<TAccessControlListBean> loadByPersonRightInAnyProjectWithStatusFlag(List<Integer> personIDs, int[] arrRights, int[] projectStatusFlag) {
		if (personIDs==null || personIDs.isEmpty()) {
			return new LinkedList<TAccessControlListBean>();
		}		
		//get the TAccessControlList objects
		Criteria crit = new Criteria();
		addExtendedAccessKeyCriteria(crit, arrRights);
		crit.addIn(PERSONKEY, personIDs);
		if (projectStatusFlag!=null && projectStatusFlag.length!=0) {
			crit.addJoin(PROJKEY, TProjectPeer.PKEY);
			crit.addJoin(TProjectPeer.STATUS, TSystemStatePeer.OBJECTID);
			crit.addIn(TSystemStatePeer.STATEFLAG, projectStatusFlag);
		}
		try { 
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Getting the right for a person in project with statusflag failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Returns whether a person has a specific right in any project with the specified statusflags 
	 * @param personIDs
	 * @param arrRights
	 * @param projectStatusFlag possible status flags. If null it does not matter 
	 * @return
	 */
	@Override
	public boolean hasPersonRightInAnyProjectWithStatusFlag(
			List<Integer> personIDs, int[] arrRights, int[] projectStatusFlag) {
		List<TAccessControlList> tACLList = null;
		if (personIDs==null || personIDs.isEmpty()) {
			return false;
		}		
		//get the TAccessControlList objects
		Criteria crit = new Criteria();
		addExtendedAccessKeyCriteria(crit, arrRights);
		crit.addIn(PERSONKEY, personIDs);
		if (projectStatusFlag!=null && projectStatusFlag.length!=0) {
			crit.addJoin(PROJKEY, TProjectPeer.PKEY);
			crit.addJoin(TProjectPeer.STATUS, TSystemStatePeer.OBJECTID);
			crit.addIn(TSystemStatePeer.STATEFLAG, projectStatusFlag);
		}
		try { 
			tACLList = doSelect(crit);
		} catch(Exception e) {
			LOGGER.error("Getting the right for a person in project with statusflag failed with " + e.getMessage());
		}
		if (tACLList!=null && !tACLList.isEmpty()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns whether a person has a specific right in any project with the specified statusflags 
	 * @param personIDs
	 * @param arrRights 
	 * @return
	 */
	@Override
	public boolean hasPersonRightInNonPrivateProject(
			List<Integer> personIDs, int[] arrRights) {
		List<TAccessControlList> tACLList = null;
		if (personIDs==null || personIDs.isEmpty()) {
			return false;
		}		
		//get the TAccessControlList objects
		Criteria crit = new Criteria();
		addExtendedAccessKeyCriteria(crit, arrRights);
		crit.addIn(PERSONKEY, personIDs);
		crit.addJoin(PROJKEY, TProjectPeer.PKEY);
		crit.add(TProjectPeer.PROJECTTYPE, 0, Criteria.GREATER_EQUAL);
		try { 
			tACLList = doSelect(crit);
		} catch(Exception e) {
			LOGGER.error("Getting the right for a person in non private project failed with " + e.getMessage());
		}
		if (tACLList!=null && !tACLList.isEmpty()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the "projectsIDs to issueTypeID set" map  the person has rights in
	 * @param personIDs
	 * @param projectIDs
	 * @param arrRights
	 * @return
	 */
	@Override
	public List<Record> getProjectIssueTypeRecords(List<Integer> personIDs, Integer[] projectIDs, int[] arrRights) {
		List<Record> projectIssueTypeRecords = new LinkedList<Record>();
		if (projectIDs==null || projectIDs.length==0) {
			return projectIssueTypeRecords;
		}
		List<int[]> projectIDChunksList = GeneralUtils.getListOfChunks(projectIDs);
		for (int[] projectIDChunk : projectIDChunksList) {
			Criteria crit = new Criteria();
			crit.addIn(PERSONKEY, personIDs);
			crit.addIn(PROJKEY, projectIDChunk);
			// add the rights criteria
			addExtendedAccessKeyCriteria(crit, arrRights);
			//join roles with rolesListType. Left join to include also those projects which do not have issue type restrictions
			crit.addJoin(TRolePeer.PKEY, TRoleListTypePeer.PROLE, Criteria.LEFT_JOIN);
			crit.addSelectColumn(PROJKEY);
			crit.addSelectColumn(TRoleListTypePeer.LISTTYPE);
			crit.setDistinct();
			try { 
				projectIssueTypeRecords.addAll(doSelectVillageRecords(crit));
			} catch(Exception e) {
				LOGGER.error("Getting all involved roles failed with " + e.getMessage());
			}
		}
		return projectIssueTypeRecords;
	}
	
	/**
	 * Returns a set of personIDs which have one of the specified rights in any project from projects
	 * @param projects  
	 * @param arrRights an array of rights, null means any right
	 * @return
	 */
	@Override
	public List<TAccessControlListBean> loadByProjectsAndRights(Integer[] projects, int[] arrRights) {
		List<TAccessControlListBean> tAclList = new LinkedList<TAccessControlListBean>();
		if (projects==null || projects.length==0) {
			return tAclList;
		}		
		List<int[]> projectIDChunksList = GeneralUtils.getListOfChunks(projects);
		if (projectIDChunksList==null) {
			return tAclList;
		}
		Iterator<int[]> iterator = projectIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] projectIDChunk = iterator.next();
			Criteria crit = new Criteria();
			crit.addIn(PROJKEY, projectIDChunk);
			if (arrRights!=null && arrRights.length>0) {
				//some rights are specified
				addExtendedAccessKeyCriteria(crit, arrRights);
			} else {
				crit.addJoin(ROLEKEY, TRolePeer.PKEY);
				//no explicit right specified but any right with any flag set would suffice
				crit.add(TRolePeer.EXTENDEDACCESSKEY, (Object)AccessBeans.anyRightFilterString(), Criteria.LIKE);
			}
			//crit.setDistinct();
			try { 
				tAclList.addAll(convertTorqueListToBeanList(doSelect(crit)));
			} catch(Exception e) {
				LOGGER.error("Getting all involved roles failed with " + e.getMessage());
			}
		}
		return tAclList;
	}
	
	/**
	 * Add criteria statements regarding the TRolePeer.EXTENDEDACCESSKEY  
	 * @param crit
	 * @param arrFlags
	 * @return
	 */
	public static Criteria addExtendedAccessKeyCriteria(Criteria crit, int[] arrFlags) {
		if (crit==null || arrFlags==null || arrFlags.length==0) {
			return crit;
		}
		Criterion criterion;
		crit.addJoin(ROLEKEY, TRolePeer.PKEY);
		String[] arrLikeString = AccessBeans.likeFilterString(arrFlags);
		crit.add(TRolePeer.EXTENDEDACCESSKEY, (Object) arrLikeString[0], Criteria.LIKE);
		for (int i=1;i<arrLikeString.length;i++) {
			criterion = crit.getCriterion(BaseTRolePeer.EXTENDEDACCESSKEY);
			criterion.or(crit.getNewCriterion(
					criterion.getTable(),
					criterion.getColumn(),
					arrLikeString[i],
					Criteria.LIKE));
		}
		return crit;
	}
	
	/**************** Role assignment: only direct roles are important (not those derived from ascendent projects) ******************/
		
	
	/**
	 * Loads the number of persons in roles for a project
	 * @param projectID 
	 * @return
	 */
	@Override
	public Map<Integer, Integer> loadNumberOfPersonsInRolesForProject(Integer projectID) {
		Map<Integer, Integer> numberOfPersonsInRoles = new HashMap<Integer, Integer>();
		Criteria crit = new Criteria();
		crit.add(PROJKEY, projectID);
		String countPersons = "COUNT(" + PERSONKEY + ")";
		crit.addSelectColumn(countPersons);
		crit.addSelectColumn(ROLEKEY);
		crit.addGroupByColumn(ROLEKEY);
		List<Record> records = new LinkedList<Record>();
		try {
			records = doSelectVillageRecords(crit);
		} catch(Exception e) {
			LOGGER.error("Groupping the persons by roles in project " + projectID + " failed with " + e.getMessage());
		}
		try {
			if (records!=null && !records.isEmpty()) {
				for (Record record : records) {
					Integer numberOfPersons = record.getValue(1).asIntegerObj();
					Integer roleID = record.getValue(2).asIntegerObj();
					numberOfPersonsInRoles.put(roleID, numberOfPersons); 
				}
			}
		} catch (Exception e) {
			LOGGER.error("Getting the number of persons by roles in project " + projectID + " failed with " + e.getMessage());
		}
		return numberOfPersonsInRoles;
	}
	
	/**
	 * Loads the number of persons in a role in a project 
	 * @param projectID
	 * @param roleID
	 * @return
	 */
	@Override
	public Integer loadNumberOfPersonsInRoleForProject(Integer projectID, Integer roleID) {
		Criteria crit = new Criteria();
		crit.add(PROJKEY, projectID);
		crit.add(ROLEKEY, roleID);
		String countPersons = "COUNT(" + PERSONKEY + ")";
		crit.addSelectColumn(countPersons);
		try {
			return ((Record) doSelectVillageRecords(crit).get(0)).getValue(1).asIntegerObj();
		} catch (TorqueException e) {
			LOGGER.error("Counting the persons in projectID " + projectID + " and roleID " +
					roleID + " failed with TorqueException " + e.getMessage(), e);
		} catch (DataSetException e) {
			LOGGER.error("Counting the persons in projectID " + projectID + " and roleID " +
					roleID + " failed with DataSetException " + e.getMessage(), e);
		}
		return Integer.valueOf(0);
	}
	
	/**
	 * Loads a list of AccessControlListBeans with the directly assigned persons/groups
	 * @param selectedPersons when null or of length 0 it will be no filtered
	 * @param selectedProjects when null or of length 0 no results
	 * @param selectedRoles when null or of length 0 it will be no filtered
	 * @return
	 */
	@Override
	public List<TAccessControlListBean> loadByPersonsAndProjects(List<Integer> selectedPersons, List<Integer> selectedProjects) {
		List<TAccessControlListBean> accessControlListBeans = new LinkedList<TAccessControlListBean>();
		if (selectedProjects==null || selectedProjects.isEmpty()) {
			return accessControlListBeans;
		}
		List<int[]> projectIDChunksList = GeneralUtils.getListOfChunks(selectedProjects);
		if (projectIDChunksList==null) {
			return accessControlListBeans;
		}
		for (int[] projectIDChunk : projectIDChunksList) {
			Criteria crit = new Criteria();
			crit.addIn(PROJKEY, projectIDChunk);
			if (selectedPersons!=null && !selectedPersons.isEmpty()) {
				/* not TAccessControlListPeer.PERSONKEY!!!
				* (for direct persons doesn't matter (because of the direct join) but for persons through groups should be 
				* TPersonPeer.PKEY because the join is made through TGroupMemberPeer)*/
				crit.addIn(PERSONKEY, selectedPersons);
			}
			try {
				accessControlListBeans.addAll(convertTorqueListToBeanList(doSelect(crit)));
			} catch (TorqueException e) {
				LOGGER.error("Getting the directly assigned roles failed with " + e.getMessage());
			}
		}
		return accessControlListBeans;
	}

	/**
	 * Load a list of AccessControlListBeans by person, projects and right
	 * @param personID
	 * @param projectIDs
	 * @param rights
	 * @return
	 */
	@Override
	public List<TAccessControlListBean> loadByPersonProjectsRight(List<Integer> personIDs, List<Integer> projectIDs, int[] rights) {
		List<TAccessControlListBean> accessControlListBeans = new LinkedList<TAccessControlListBean>();	
		if (projectIDs==null || projectIDs.isEmpty() || personIDs==null || personIDs.isEmpty()) {
			return accessControlListBeans;
		}
		if (projectIDs==null || projectIDs.isEmpty()) {
			return accessControlListBeans;
		}
		List<int[]> projectIDChunksList = GeneralUtils.getListOfChunks(projectIDs);
		if (projectIDChunksList==null) {
			return accessControlListBeans;
		}
		for (int[] projectIDChunk : projectIDChunksList) {
			Criteria crit = new Criteria();
			crit.addIn(PROJKEY, projectIDChunk);
			addExtendedAccessKeyCriteria(crit, rights);
			crit.addIn(PERSONKEY, personIDs);
			try {
				accessControlListBeans.addAll(convertTorqueListToBeanList(doSelect(crit)));
			} catch(Exception e) {
				LOGGER.error("Getting the right for a user in a project failed with " + e.getMessage());
			}
		}
		return accessControlListBeans;
	}
	
	/**
	 * Load a list of AccessControlListBeans by person, projects and role
	 * @param personIDs
	 * @param projectID
	 * @param role
	 * @return
	 */
	@Override
	public List<TAccessControlListBean> loadByPersonProjectsRole(List<Integer> personIDs, List<Integer> projectIDs, Integer role) {
		List<TAccessControlListBean> accessControlListBeans = new LinkedList<TAccessControlListBean>();	
		if (projectIDs==null || projectIDs.isEmpty() || personIDs==null || personIDs.isEmpty()) {
			return accessControlListBeans;
		}
		if (projectIDs==null || projectIDs.isEmpty()) {
			return accessControlListBeans;
		}
		List<int[]> projectIDChunksList = GeneralUtils.getListOfChunks(projectIDs);
		if (projectIDChunksList==null) {
			return accessControlListBeans;
		}
		for (int[] projectIDChunk : projectIDChunksList) {
			Criteria crit = new Criteria();
			crit.addIn(PROJKEY, projectIDChunk);
			crit.add(ROLEKEY, role);
			crit.addIn(PERSONKEY, personIDs);
			try {
				accessControlListBeans.addAll(convertTorqueListToBeanList(doSelect(crit)));
			} catch(Exception e) {
				LOGGER.error("Getting the role for a user in a project failed with " + e.getMessage());
			}
		}
		return accessControlListBeans;
	}
	
	
	/**
	 * Load a list of AccessControlListBeans by projects, roles and listType
	 * @param projectID
	 * @param roles
	 * @param listType if null do not filter by	 
	 * @return
	 */
	@Override
	public List<TAccessControlListBean> loadByProjectsRolesListType(List<Integer> projectIDs,
			Object[] roles, Integer listType) {
		List<TAccessControlListBean> accessControlListBeans = new LinkedList<TAccessControlListBean>();
		if (roles==null || roles.length==0 || projectIDs==null || projectIDs.isEmpty()) {
			return accessControlListBeans;
		}		
		List<int[]> projectIDChunksList = GeneralUtils.getListOfChunks(projectIDs);
		if (projectIDChunksList==null) {
			return accessControlListBeans;
		}
		for (int[] projectIDChunk : projectIDChunksList) {
			Criteria crit = new Criteria();
			crit.addIn(PROJKEY, projectIDChunk);
			crit.addIn(ROLEKEY, roles);
			if (listType!=null) {
				crit.addJoin(ROLEKEY, TRoleListTypePeer.PROLE);
				crit.add(TRoleListTypePeer.LISTTYPE, listType);
			}		
			try { 
				accessControlListBeans.addAll(convertTorqueListToBeanList(doSelect(crit)));
			} catch(Exception e) {
				LOGGER.error("Getting tacls by projectIDs " + projectIDChunk.length +
						" roles " + roles.length + " and listType " + listType + " failed with " + e.getMessage(), e);
			}
		}
		return accessControlListBeans;
	}
	
	/**
	 * Saves a accessControlListBean in the TAccessControlList table
	 * @param fieldBean
	 * @return
	 */
	@Override
	public void save(TAccessControlListBean accessControlListBean) {
		TAccessControlList tAccessControlList;
		try {
			tAccessControlList = BaseTAccessControlList.createTAccessControlList(accessControlListBean);
			tAccessControlList.save();
		} catch (Exception e) {
			LOGGER.error("Saving of a tacl failed with " + e.getMessage());
		}
	}
	
	/**
	 * Deletes an AccessControlList 
	 * @param projKey
	 * @param roleKey
	 * @param personKey
	 */
	@Override
	public void deleteByProjectRolePerson(Integer projectID, Integer roleID, Integer personID) {
		Criteria crit = new Criteria();
		crit.add(PROJKEY, projectID);
		crit.add(ROLEKEY, roleID);
		crit.add(PERSONKEY, personID);
		try {
			doDelete(crit);
		} catch (Exception e) {
			LOGGER.error("Deleting the accessControlList by project  " + projectID + " , role " +
					roleID + " and person " + personID + " failed with " + e.getMessage(), e);
		}	
	}
	
	/**
	 * Converts the torque objects to beans
	 * @param torqueList
	 * @return
	 */
	private static List<TAccessControlListBean> convertTorqueListToBeanList(List<TAccessControlList> torqueList) {
		List<TAccessControlListBean> beanList = new LinkedList<TAccessControlListBean>();
		if (torqueList!=null){
			Iterator<TAccessControlList> itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				beanList.add(itrTorqueList.next().getBean());
			}
		}
		return beanList;
	}
}
