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

package com.aurel.track.admin.customize.projectType.assignments.simple;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.aurel.track.admin.customize.projectType.ProjectTypesBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TChildProjectTypeBean;
import com.aurel.track.dao.ChildProjectTypeDAO;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.util.IconClass;

/**
 * Facade implementation for child project type assignments
 * @author Tamas Ruff
 *
 */
public class ProjectTypeChildrenAssignmentFacade extends ProjectTypeSimpleAssignmentBaseFacade {
	
	private static ChildProjectTypeDAO childProjectTypeDAO = DAOFactory.getFactory().getChildProjectTypeDAO();
	
	private static ProjectTypeChildrenAssignmentFacade instance;
	
	/**
	 * Return a ProjectTypeSimpleAssignmentFacade instance which implements the child project type assignments
	 * @return
	 */
	public static ProjectTypeChildrenAssignmentFacade getInstance(){
		if(instance==null){
			instance = new ProjectTypeChildrenAssignmentFacade();
		}
		return instance;
	}
	
	/**
	 * Gets the already assigned IDs
	 * @param projectTypeID
	 * @return
	 */
	@Override
	public Set<Integer> getAssignedIDsByProjectTypeID(Integer projectTypeID) {
		Set<Integer> childProjectTypeIDs = new HashSet<Integer>();
		List<TChildProjectTypeBean> childProjectTypeBeans = childProjectTypeDAO.loadAssignmentsByParent(projectTypeID);
		for (TChildProjectTypeBean childProjectTypeBean : childProjectTypeBeans) {
			Integer childProjectType = childProjectTypeBean.getProjectTypeChild();
			childProjectTypeIDs.add(childProjectType);
		}
		return childProjectTypeIDs;
	}
	
	/**
	 * Gets the complete list of assignable beans
	 * @param locale
	 * @return
	 */
	@Override
	public List<ILabelBean> getAllAssignables(Locale locale) {
		return (List)ProjectTypesBL.loadNonPrivate();
	}
	
	/**
	 * Gets the key for the assignment info 
	 * @return
	 */
	@Override
	public String getAssignmentInfoKey() {
		return "admin.customize.projectType.lbl.assignmentInfoChildProjectType";
	}
	
	/**
	 * Whether the icon is dynamic or static
	 * If static the getIconCssClass() should return a non null value
	 * @return
	 */
	@Override
	public boolean isDynamicIcon() {
		return false;
	}
	
	/**
	 * Get icon css class
	 * @return
	 */
	@Override
	public String getIconCssClass() {
		return IconClass.PROJECTTYPE_ICON_CLASS;
	}
	
	
	/**
	 * Creates a new assignment bean
	 * @param projectTypeID
	 * @param itemTypeID
	 * @param assignedID
	 * @return
	 */
	@Override
	public Object createAssignmentBean(Integer projectTypeID, Integer itemTypeID, Integer assignedID) {
		TChildProjectTypeBean childProjectTypeBean = new TChildProjectTypeBean();
		childProjectTypeBean.setProjectTypeParent(projectTypeID);
		childProjectTypeBean.setProjectTypeChild(assignedID);
		return childProjectTypeBean;
	}
	
	/**
	 * Saves an assignment
	 * @param assignmentBean
	 * @return
	 */
	@Override
	public Integer save(Object assignmentBean) {
		return childProjectTypeDAO.save((TChildProjectTypeBean)assignmentBean);
	}
	
	/**
	 * Deletes the assignments for a projectType
	 * @param projectTypeID
	 * @param itemTypeID
	 * @param assignedIDs
	 */
	@Override
	public void delete(Integer projectTypeID, Integer itemTypeID, List<Integer> assignedIDs) {
		childProjectTypeDAO.delete(projectTypeID, assignedIDs);
	}
}
