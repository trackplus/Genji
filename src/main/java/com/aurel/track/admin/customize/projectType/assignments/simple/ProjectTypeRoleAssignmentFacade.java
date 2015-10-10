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

package com.aurel.track.admin.customize.projectType.assignments.simple;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.aurel.track.admin.customize.role.RoleBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TPRoleBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.PRoleDAO;
import com.aurel.track.util.IconClass;

/**
 * Facade implementation for role assignments for project type
 * @author Tamas Ruff
 *
 */
public class ProjectTypeRoleAssignmentFacade extends ProjectTypeSimpleAssignmentBaseFacade {
	
	private static PRoleDAO pRoleDAO = DAOFactory.getFactory().getPRoleDAO();
	
	private static ProjectTypeRoleAssignmentFacade instance;
	
	/**
	 * Return a ProjectTypeSimpleAssignmentFacade instance which implements the role assignments 
	 * @return
	 */
	public static ProjectTypeRoleAssignmentFacade getInstance(){
		if(instance==null){
			instance = new ProjectTypeRoleAssignmentFacade();
		}
		return instance;
	}
	
	/**
	 * Gets the already assigned IDs
	 * @param projectTypeID
	 * @return
	 */
	public Set<Integer> getAssignedIDsByProjectTypeID(Integer projectTypeID) {
		Set<Integer> roleIDs = new HashSet<Integer>();
		List<TPRoleBean> pRoleBeans = pRoleDAO.loadByProjectType(projectTypeID);
		for (TPRoleBean childProjectTypeBean : pRoleBeans) {
			Integer roleID = childProjectTypeBean.getRole();
			roleIDs.add(roleID);
		}
		return roleIDs;
	}
	
	/**
	 * Gets the complete list of assignable beans
	 * @param locale
	 * @return
	 */
	public List<ILabelBean> getAllAssignables(Locale locale) {
		return (List)RoleBL.loadVisible(locale);
	}
	
	/**
	 * Gets the key for the assignment info 
	 * @return
	 */
	public String getAssignmentInfoKey() {
		return "admin.customize.projectType.lbl.assignmentInfoRole";
	}
	
	/**
	 * Whether the icon is dynamic or static
	 * If static the getIconCssClass() should return a non null value
	 * @return
	 */
	public boolean isDynamicIcon() {
		return false;
	}
	
	/**
	 * Get icon css class
	 * @return
	 */
	public String getIconCssClass() {
		return IconClass.ROLE;
	}
	
	/**
	 * Creates a new assignment bean
	 * @param projectTypeID
	 * @param itemTypeID
	 * @param assignedID
	 * @return
	 */
	public Object createAssignmentBean(Integer projectTypeID, Integer itemTypeID, Integer assignedID) {
		TPRoleBean pRoleBean = new TPRoleBean();
		pRoleBean.setProjectType(projectTypeID);
		pRoleBean.setRole(assignedID);
		return pRoleBean;
	}
	
	/**
	 * Saves an assignment
	 * @param assignmentBean
	 * @return
	 */
	public Integer save(Object assignmentBean) {
		return pRoleDAO.save((TPRoleBean)assignmentBean);
	}

	/**
	 * Deletes the assignments for a projectType
	 * @param projectTypeID
	 * @param itemTypeID
	 * @param assignedIDs
	 */
	public void delete(Integer projectTypeID, Integer itemTypeID, List<Integer> assignedIDs) {
		pRoleDAO.delete(projectTypeID, assignedIDs);
	}
	
}
