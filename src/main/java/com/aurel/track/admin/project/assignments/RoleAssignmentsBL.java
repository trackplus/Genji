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

package com.aurel.track.admin.project.assignments;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.accessControl.AccessControlBL;
import com.aurel.track.admin.customize.projectType.assignments.simple.ProjectTypeRoleAssignmentFacade;
import com.aurel.track.admin.customize.role.RoleBL;
import com.aurel.track.admin.user.department.DepartmentBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TPersonBean.USERLEVEL;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TRoleBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.RoleDAO;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.LabelValueBean;

public class RoleAssignmentsBL {

	private static RoleDAO roleDAO = DAOFactory.getFactory().getRoleDAO();
	
	private static String LINK_CHAR = "_";
	private static String USER_ICON_CLASS = "user-ticon";
	private static String GROUP_ICON_CLASS = "users-ticon";
	private static String ROLES_ICON_CLASS = "roles-ticon";
	
	
	/**
	 * Encode a node
	 * @param roleAssignmentTokens
	 * @return
	 */
	static String encodeNode(RoleAssignmentTokens roleAssignmentTokens){
		StringBuffer stringBuffer = new StringBuffer();
		Integer projectID = roleAssignmentTokens.getProjectID();
		if (projectID!=null) {
			stringBuffer.append(projectID);
			Integer roleID = roleAssignmentTokens.getRoleID();
			Integer personID = roleAssignmentTokens.getPersonID();
			if (roleID!=null) {
				//roleID is null for account assignments but the LINK_CHAR 
				//should be added anyway to remain consistent over different assignments 
				stringBuffer.append(LINK_CHAR);
				stringBuffer.append(roleID);
				if (personID!=null) {
					stringBuffer.append(LINK_CHAR);
					stringBuffer.append(personID);
				}
			}
		}
		return stringBuffer.toString();
	}
	
	/**
	 * Decode a node
	 * @param id
	 * @return
	 */
	public static RoleAssignmentTokens decodeNode(String id) {
		RoleAssignmentTokens roleAssignmentTokens = new RoleAssignmentTokens();
		if (id!=null && !"".equals(id)) {
			String[] tokens = id.split(LINK_CHAR);
			if (tokens!=null && tokens.length>0) {
				roleAssignmentTokens.setProjectID(Integer.valueOf(tokens[0]));
				if (tokens.length>1) {
					if (tokens[1]!=null && !"".equals(tokens[1])) {
						roleAssignmentTokens.setRoleID(Integer.valueOf(tokens[1]));
					}
					if (tokens.length>2) {
						if (tokens[2]!=null && !"".equals(tokens[2])) {
							roleAssignmentTokens.setPersonID(Integer.valueOf(tokens[2]));
						}
					}
				}
			}
		}
		return roleAssignmentTokens;
	}
	
	public static String getProjectBranchNodeID(Integer projectID) {
		return encodeNode(new RoleAssignmentTokens(projectID));
	}
	
	/**
	 * Gets the assigned nodes for a project
	 * @param node
	 * @param locale
	 * @return
	 */
	public static List<RoleAssignmentTreeNodeTO> getAssignedNodes(
			String node, Locale locale) {
		RoleAssignmentTokens roleAssignmentTokens = decodeNode(node);
		List<RoleAssignmentTreeNodeTO> assignedNodes = new LinkedList<RoleAssignmentTreeNodeTO>();
		Integer projectID = roleAssignmentTokens.getProjectID();
		Integer roleID = roleAssignmentTokens.getRoleID();
		Set<Integer> assignedPersonIDs = null;
		List<ILabelBean> labelBeans = null;
		String iconCls = null;
		boolean leaf = true;
		boolean group = false;
		if (roleID==null) {
			//first level: roles
			Set<Integer> possibleRoleIDs = null;
			TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
			if (projectBean!=null) {
				Integer projectTypeID = projectBean.getProjectType();
				if (projectTypeID!=null) {
					possibleRoleIDs = ProjectTypeRoleAssignmentFacade.getInstance().getAssignedIDsByProjectTypeID(projectTypeID);
				}
			}
			labelBeans = (List)RoleBL.loadVisible(locale);
			Map<Integer, Integer> peronsInRole = AccessControlBL.loadNumberOfPersonsInRolesForProject(projectID);
			if (peronsInRole!=null) {
				for (Iterator<ILabelBean> iterator = labelBeans.iterator(); iterator.hasNext();) {
					TRoleBean roleBean = (TRoleBean)iterator.next();
					Integer roleKey = roleBean.getObjectID();
					Integer numberOfPersonsInRole = peronsInRole.get(roleKey);
					if (numberOfPersonsInRole!=null) {
						roleBean.setLabel(roleBean.getLabel()+ " (" + numberOfPersonsInRole + ")");
					} else {
						if (possibleRoleIDs!=null && !possibleRoleIDs.isEmpty() && !possibleRoleIDs.contains(roleKey)) {
							iterator.remove();
						}
					}
				}	
			}
			iconCls = ROLES_ICON_CLASS;
			leaf = false;
		} else {
			//second level: persons in role
			assignedPersonIDs = AccessControlBL.getPersonSetByProjectRole(projectID, roleID);
			labelBeans = (List)PersonBL.loadPersonsAndGroups();
		}
		for (ILabelBean labelBean : labelBeans) {
			if ((assignedPersonIDs==null && leaf==false) || //all roles or the real assignments
					(assignedPersonIDs != null && assignedPersonIDs.contains(labelBean.getObjectID()))) {
				RoleAssignmentTokens roleAssignmentToken = null;
				if (!leaf) {
					//role entries
					roleAssignmentToken = new RoleAssignmentTokens(projectID, labelBean.getObjectID());
				} else {
					//person entries
					TPersonBean personBean = (TPersonBean)labelBean;
					if (personBean.isGroup()) {
						iconCls = GROUP_ICON_CLASS;
						group = true;
					} else {
						iconCls = USER_ICON_CLASS;
						group = false;
					}
					roleAssignmentToken = new RoleAssignmentTokens(projectID, roleID, labelBean.getObjectID());
				}
				RoleAssignmentTreeNodeTO treeNodeTO = new RoleAssignmentTreeNodeTO(
						encodeNode(roleAssignmentToken), labelBean.getLabel(), iconCls, leaf, group);
				//projectTreeNodeTO.setLeaf(leaf);
				assignedNodes.add(treeNodeTO);
			}
		}
		return assignedNodes;
	}
	
	/**
	 * Gets the role with actualized label
	 * @param node
	 * @param locale
	 * @return
	 */
	static List<LabelValueBean> getRole(String node, Locale locale) {
		RoleAssignmentTokens roleAssignmentTokens = decodeNode(node);
		Integer projectID = roleAssignmentTokens.getProjectID();
		Integer roleID = roleAssignmentTokens.getRoleID();
		List<LabelValueBean> roleLabelBeans = new LinkedList<LabelValueBean>();
		if (roleID!=null) {
			TRoleBean roleBean = RoleBL.loadRoleByKey(roleID, locale);
			String roleLabel = null;
			if (roleBean!=null) {
				roleLabel = roleBean.getLabel();
			}
			if (projectID!=null) {
				Integer numberOfPersons = AccessControlBL.loadNumberOfPersonsInRoleForProject(projectID, roleID);
				if (numberOfPersons!=null && numberOfPersons.intValue()!=0) {
						roleLabel = roleLabel + " ("+numberOfPersons + ")";
				}
			}
			roleLabelBeans.add(new LabelValueBean(roleLabel, node));
		}
		return roleLabelBeans;
	}
	
	/**
	 * Gets the assignment detail json for a node
	 * @param node
	 * @param locale	
	 * @return
	 */
	static String getAssignmentJSON(String node, Locale locale) {
		RoleAssignmentTokens roleAssignmentTokens = decodeNode(node);
		Integer projectID = roleAssignmentTokens.getProjectID();
		Integer roleID = roleAssignmentTokens.getRoleID();
		String projectLabel = "";
		if (projectID!=null) {
			TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
			if (projectBean!=null) {
				projectLabel = projectBean.getLabel();
			}
		}
		String assignmentInfo = null;
		Set<Integer> assignedPersonIDs = AccessControlBL.getPersonSetByProjectRole(projectID, roleID);
		List<TPersonBean> personsAndGroups = PersonBL.loadPersonsAndGroups();
		List<TPersonBean> assigned = getAssigned(personsAndGroups, assignedPersonIDs, true);
		List<TPersonBean> unassigned = getAssigned(personsAndGroups, assignedPersonIDs, false);
		TRoleBean roleBean =  RoleBL.loadRoleByKey(roleID, locale);
		String roleLabel = null;
		if (roleBean!=null) {
			roleLabel = RoleBL.loadRoleByKey(roleID, locale).getLabel();
		}
		assignmentInfo = LocalizeUtil.getParametrizedString("admin.project.roleAssignment.lbl.infoRole",
				new Object[] {roleLabel, projectLabel}, locale);
		String reloadFromNode = encodeNode(new RoleAssignmentTokens(projectID, roleID));
		return ProjectAssignmentJSON.encodePersonToRoleAssignment((List)assigned, (List)unassigned,
				assignmentInfo, DepartmentBL.getDepartmentMap(), locale, reloadFromNode, reloadFromNode);	
	}
	
	/**
	 * The node to reload is always a role even if the selected node is a person in role 
	 * (a leaf in the tree), so the personID should be removed from the node
	 * @param node
	 * @return
	 */
	static String getNodesToReload(String node) {
		RoleAssignmentTokens roleAssignmentTokens = decodeNode(node);
		Integer projectID = roleAssignmentTokens.getProjectID();
		Integer roleID = roleAssignmentTokens.getRoleID();
		String reloadFromNode = encodeNode(new RoleAssignmentTokens(projectID, roleID));
		return ProjectAssignmentJSON.encodeNodes(reloadFromNode);
	}
	
	/**
	 * Assign entries
	 * @param node
	 * @param assign
	 */
	static void assign(String node, String assign) {
		RoleAssignmentTokens roleAssignmentTokens = decodeNode(node);
		Integer projectID = roleAssignmentTokens.getProjectID();
		Integer roleID = roleAssignmentTokens.getRoleID();
		List<Integer> assignIDs = null;
		if (assign!=null && !"".equals(assign)) {
			assignIDs = GeneralUtils.createIntegerListFromStringArr(assign.split(","));
		}
		addPersonsToRoles(projectID, roleID, assignIDs);
	}
	
	/**
	 * Unassign entries
	 * @param node
	 * @param unassign
	 */
	static void unassign(String node, String unassign) {
		RoleAssignmentTokens roleAssignmentTokens = decodeNode(node);
		Integer projectID = roleAssignmentTokens.getProjectID();
		Integer roleID = roleAssignmentTokens.getRoleID();
		List<Integer> unassignIDs = null;
		if (unassign==null) {
			unassignIDs = new LinkedList<Integer>();
			Integer personID = roleAssignmentTokens.getPersonID();
			if (personID!=null) {
				//unassingn from tree
				unassignIDs.add(personID);
			}
		} else {
			unassignIDs = GeneralUtils.createIntegerListFromStringArr(unassign.split(","));
		}
		removePersonsFromRoles(projectID, roleID, unassignIDs);
	}
	
	/**
	 * Gets the issueTypeBeans assigned to the projectType
	 * @param allPersonsList
	 * @param assignedIDs
	 * @param assigned
	 * @return
	 */
	private static List<TPersonBean> getAssigned(List<TPersonBean> allPersonsList,
			Set<Integer> assignedIDs, boolean assigned) {
		List<TPersonBean> filteredBeans = new LinkedList<TPersonBean>();
		for (TPersonBean personBean : allPersonsList) {
			if ((assigned && assignedIDs.contains(personBean.getObjectID())) ||
					(!assigned && !assignedIDs.contains(personBean.getObjectID()) && (personBean.getUserLevel()==null || !personBean.getUserLevel().equals(USERLEVEL.CLIENT)))) {
				filteredBeans.add(personBean);
			}
		}
		return filteredBeans;
	}
	
	/**
	 * Assign persons to role in project
	 * @param projectID
	 * @param roleID
	 * @param personIDs
	 */
	public static void addPersonsToRoles(Integer projectID, Integer roleID, List<Integer> personIDs) {
		if (projectID!=null && roleID!=null && personIDs!=null && !personIDs.isEmpty())
			for (Integer personID : personIDs) {
				AccessControlBL.save(personID, projectID, roleID);
			}
	}
	
	/**
	 * Remove persons from roles in project
	 * @param projectID
	 * @param roleID
	 * @param personIDs
	 */
	public static void removePersonsFromRoles(Integer projectID, Integer roleID, List<Integer> personIDs) {
		if (projectID!=null && roleID!=null && personIDs!=null && !personIDs.isEmpty()) {
			for (Integer personID : personIDs) {
				AccessControlBL.deleteByProjectRolePerson(projectID, roleID, personID);
			}
		}
	}	
	
	/**
	 * Gets the TAccessControlListBean for 
	 * @param personID
	 * @param projectID
	 * @return
	 */
	/*public static List<TAccessControlListBean> getRightsForPersonInProject(Integer personID, Integer projectID) {
		List<Integer> selectedPersons = new LinkedList<Integer>();
		selectedPersons.add(personID);
		List<Integer> selectedProjects = new LinkedList<Integer>();
		selectedProjects.add(projectID);
		return accessControlListDAO.loadByPersonsAndProjects(selectedPersons, selectedProjects);
	}*/
}
