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

import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TPlistTypeBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.PIssueTypeDAO;

/**
 * Facade implementation for role assignments for project type
 * @author Tamas Ruff
 *
 */
public class ProjectTypeItemTypeAssignmentFacade extends ProjectTypeSimpleAssignmentBaseFacade {
	
	private static PIssueTypeDAO pIssueTypeDAO = DAOFactory.getFactory().getPIssueTypeDAO();
	
	private static ProjectTypeItemTypeAssignmentFacade instance;
	
	/**
	 * Return a ProjectTypeSimpleAssignmentFacade instance which implements the item type assignments 
	 * @return
	 */
	public static ProjectTypeItemTypeAssignmentFacade getInstance(){
		if(instance==null){
			instance = new ProjectTypeItemTypeAssignmentFacade();
		}
		return instance;
	}
	
	/**
	 * Gets the already assigned IDs
	 * @param projectTypeID
	 * @return
	 */
	public Set<Integer> getAssignedIDsByProjectTypeID(Integer projectTypeID) {
		Set<Integer> itemTypeIDs = new HashSet<Integer>();
		List<TPlistTypeBean> pItemTypeBeans = pIssueTypeDAO.loadByProjectType(projectTypeID);
		for (TPlistTypeBean pItemTypeBean : pItemTypeBeans) {
			Integer itemTypeID = pItemTypeBean.getCategory();
			itemTypeIDs.add(itemTypeID);
		}
		return itemTypeIDs;
	}
	
	/**
	 * Gets the complete list of assignable beans
	 * @param locale
	 * @return
	 */
	public List<ILabelBean> getAllAssignables(Locale locale) {
		return IssueTypeBL.loadAll(locale);
	}
	
	/**
	 * Gets the key for the assignment info 
	 * @return
	 */
	public String getAssignmentInfoKey() {
		return "admin.customize.projectType.lbl.assignmentInfoIssueType";
	}
	
	/**
	 * Whether the icon is dynamic or static
	 * If static the getIconCssClass() should return a non null value
	 * @return
	 */
	public boolean isDynamicIcon() {
		return true;
	}
	
	/**
	 * Creates a new assignment bean
	 * @param projectTypeID
	 * @param itemTypeID
	 * @param assignedID
	 * @return
	 */
	public Object createAssignmentBean(Integer projectTypeID, Integer itemTypeID, Integer assignedID) {
		TPlistTypeBean plistTypeBean = new TPlistTypeBean();
		plistTypeBean.setProjectType(projectTypeID);
		plistTypeBean.setCategory(assignedID);
		return plistTypeBean;
	}
	
	/**
	 * Saves an assignment
	 * @param assignmentBean
	 * @return
	 */
	public Integer save(Object assignmentBean) {
		return pIssueTypeDAO.save((TPlistTypeBean)assignmentBean);
	}

	/**
	 * Deletes the assignments for a projectType
	 * @param projectTypeID
	 * @param itemTypeID
	 * @param assignedIDs
	 */
	public void delete(Integer projectTypeID, Integer itemTypeID, List<Integer> assignedIDs) {
		pIssueTypeDAO.delete(projectTypeID, assignedIDs);
	}
	
}
