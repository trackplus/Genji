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


package com.aurel.track.admin.user.assignments;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.accessControl.AccessControlBL;
import com.aurel.track.admin.customize.projectType.assignments.simple.ProjectTypeRoleAssignmentFacade;
import com.aurel.track.admin.customize.role.RoleBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TAccessControlListBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TRoleBean;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.TreeNode;

public class RolesInProjectsBL {
	
	/**
	 * Loads the roles in projects for a person
	 * @param personID
	 * @param isGroup
	 * @param locale
	 * @return
	 */
	static List<ProjectRoleTO> getRolesInProjects(Integer personID, boolean isGroup, Locale locale) {
		Map<Integer, Map<Integer, Boolean>> assignmentsMap = null;
		if (isGroup) {
			assignmentsMap = loadGroupAssignments(personID);
		} else {
			assignmentsMap = loadPersonAssignments(personID);
		}
		return loadAssignmnetData(assignmentsMap, locale);
	}

	
	private static List<ProjectRoleTO> loadAssignmnetData(Map<Integer, Map<Integer, Boolean>> assignmentsMap, Locale locale) {
		List<ProjectRoleTO>  rolesInProjects = new LinkedList<ProjectRoleTO>();
		//get roles
		List<TRoleBean> roleBeans = LocalizeUtil.localizeDropDownList(RoleBL.loadAll(), locale);
		Map<Integer, TRoleBean> roleMap = GeneralUtils.createMapFromList(roleBeans);
		List<TProjectBean> projectBeans = ProjectBL.loadByProjectIDs(GeneralUtils.createListFromSet(assignmentsMap.keySet()));
		for (TProjectBean projectBean : projectBeans) {
			Integer projectID = projectBean.getObjectID();
			Map<Integer, Boolean> roleAssignmentMap = assignmentsMap.get(projectID);
			if (roleMap!=null) {
				for (Integer roleID : roleAssignmentMap.keySet()) {
					TRoleBean roleBean = roleMap.get(roleID);
					if (roleBean!=null) {
						ProjectRoleTO projectRoleTO = new ProjectRoleTO();
						projectRoleTO.setProjectID(projectID);
						projectRoleTO.setProjectLabel(projectBean.getLabel());
						projectRoleTO.setRoleID(roleID);
						projectRoleTO.setRoleLabel(roleBean.getLabel());
						projectRoleTO.setDirect(roleAssignmentMap.get(roleID).booleanValue());
						rolesInProjects.add(projectRoleTO);
					}
				}
			}
		}
		Collections.sort(rolesInProjects);
		String projectLabelTmp = null;
		for (ProjectRoleTO projectRoleTO : rolesInProjects) {
			String projectLabel = projectRoleTO.getProjectLabel();
			if (!projectLabel.equals(projectLabelTmp)) {
				projectLabelTmp = projectLabel;
				projectRoleTO.setFirst(true);
			}
		}
		return rolesInProjects;
	}
	
	/**
	 * Load the assignment map with direct roles and roles got through groups for a person 
	 * @param personID
	 * @return
	 */
	private static Map<Integer, Map<Integer, Boolean>> loadPersonAssignments(Integer personID) { 
		Map<Integer, Map<Integer, Boolean>> hierarchicalMap = new HashMap<Integer, Map<Integer,Boolean>>();
		//first the directly assigned roles (because the direct flag true has priority over false)
		List<TAccessControlListBean> directRoles = AccessControlBL.loadByPerson(personID);
		addRoles(hierarchicalMap, directRoles, true);
		List<TPersonBean> groupsForPerson = PersonBL.loadGroupsForPerson(personID);
		if (groupsForPerson!=null && !groupsForPerson.isEmpty()) {
			List<Integer> groupIDs = GeneralUtils.createIntegerListFromBeanList(groupsForPerson);
			List<TAccessControlListBean> indirectRoles = AccessControlBL.loadByPersons(groupIDs);
			//then the roles assigned through groups
			addRoles(hierarchicalMap, indirectRoles, false);
		}
		return hierarchicalMap;
	}
	 
	/**
	 * Load the assignment map with direct roles for a group 
	 * @param groupID
	 * @return
	 */
	private static Map<Integer, Map<Integer, Boolean>> loadGroupAssignments(Integer groupID) { 
		Map<Integer, Map<Integer, Boolean>> hierarchicalMap = new HashMap<Integer, Map<Integer,Boolean>>();
		//only the directly assigned roles
		List<TAccessControlListBean> directRoles = AccessControlBL.loadByPerson(groupID);
		addRoles(hierarchicalMap, directRoles, true);
		return hierarchicalMap;
	}
	
	/**
	 * Adds an accessControlListBean to the hierarchicalMap
	 * @param hierarchicalMap
	 * @param accessControlListBeansList
	 * @param direct
	 */
	private static void addRoles(Map<Integer, Map<Integer, Boolean>> hierarchicalMap,
			List<TAccessControlListBean> accessControlListBeansList, boolean direct) {
		if (accessControlListBeansList!=null) {
			for (TAccessControlListBean accessControlListBean : accessControlListBeansList) {
				Integer projectID = accessControlListBean.getProjectID();
				Integer roleID = accessControlListBean.getRoleID();
				if (roleID.intValue()>0) {
					//do not show those with negative roleID (the private workspace should not appear)
					Map<Integer, Boolean> projectMap = hierarchicalMap.get(projectID);
					if (projectMap==null) {
						projectMap = new HashMap<Integer, Boolean>();
						hierarchicalMap.put(projectID, projectMap);
					}
					Boolean directRole = projectMap.get(roleID);
					if (directRole==null) {
						//if it exists do not overwrite the value
						//it should be assured that the mathod will be first called 
						//with direct=true and then with direct=false
						projectMap.put(roleID, new Boolean(direct));
					}
				}
			}
		}
	}
	
	/**
	 * Edits a role to project assignment
	 * @param projectID
	 * @param roleID
	 * @param personBean
	 * @param locale
	 * @return
	 */
	static String editAssignment(Integer projectID, Integer roleID, TPersonBean personBean, Locale locale) {
		List<ILabelBean> roleBeans = getRoleBeans(projectID, roleID, locale);
        List<Integer> accessRights = new LinkedList<Integer>();
        accessRights.add(Integer.valueOf(AccessBeans.AccessFlagIndexes.PROJECTADMIN));
        int[] rights = GeneralUtils.createIntArrFromIntegerList(accessRights);
        List<TreeNode> projectTree = ProjectBL.getProjectNodesByRightEager(
                false, personBean, true, rights, false, true);
		return RolesInProjectsJSON.encodeRoleProjectDetailJSON(projectID, projectTree, roleID,  roleBeans);
	}
	
	/**
	 * Gets the possible roleBeans to select from depending on the allowed roles for projectType
	 * The role bean of an existing role assignment to project should always be included even if it is not any more valid for projectType  
	 * @param projectID
	 * @param roleID
	 * @param locale
	 * @return
	 */
	static List<ILabelBean> getRoleBeans(Integer projectID, Integer roleID, Locale locale) {
		Integer projectTypeID = null;
		if (projectID!=null) {
			TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
			if (projectBean!=null) {
				projectTypeID = projectBean.getProjectType();
				
			}
		}
		return ProjectTypeRoleAssignmentFacade.getInstance().getPossibleBeans(projectTypeID, roleID, locale);
	}
	
	/**
	 * Assign persons to role in project
     * @param personID
	 * @param projectID
	 * @param roleID
	 * @param projectIDOld
     * @param roleIDOld
     * @param add
	 */
	static void addTacl(Integer personID, Integer projectID, Integer roleID, Integer projectIDOld, Integer roleIDOld, boolean add) {
		if (add) {
			//add new
			AccessControlBL.save(personID, projectID, roleID);
		} else {
			//edit			
			if (EqualUtils.notEqual(projectIDOld, projectID) || EqualUtils.notEqual(roleIDOld, roleID)) {
				AccessControlBL.deleteByProjectRolePerson(projectIDOld, roleIDOld, personID);
				AccessControlBL.save(personID, projectID, roleID);
			}
		}
	}
	
	/**
	 * Unassign entries
	 * @param personID
	 * @param unassign
	 */
	static void unassign(Integer personID, String unassign) {
		if (personID!=null) {
			String[] unassignIDs = unassign.split(",");
			for (String unassignStr : unassignIDs) {
				String[] unassignParts = unassignStr.split("\\|");
				Integer projectID = null;
				try {
					projectID = Integer.valueOf(unassignParts[0]);
				} catch (Exception e) {
				}
				Integer roleID = null;
				try {
					roleID = Integer.valueOf(unassignParts[1]);
				} catch (Exception e) {
				}
				if (projectID!=null && roleID!=null) {
					AccessControlBL.deleteByProjectRolePerson(projectID, roleID, personID);
				}
			}
		}
	}
}
