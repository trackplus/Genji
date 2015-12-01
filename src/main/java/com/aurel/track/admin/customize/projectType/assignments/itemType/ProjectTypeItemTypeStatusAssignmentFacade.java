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

package com.aurel.track.admin.customize.projectType.assignments.itemType;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.admin.customize.lists.systemOption.StatusBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TPstateBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.PStatusDAO;

/**
 * Facade implementing priority assignments
 * @author Tamas
 *
 */
public class ProjectTypeItemTypeStatusAssignmentFacade extends ProjectTypeItemTypeAssignmentBaseFacade {
	
	private static PStatusDAO pStatusDAO = DAOFactory.getFactory().getPStatusDAO();
	
	private static ProjectTypeItemTypeStatusAssignmentFacade instance;
	
	/**
	 * Return a ProjectTypeItemTypeAssignmentFacade instance which implements the severity assignments 
	 * @return
	 */
	public static ProjectTypeItemTypeStatusAssignmentFacade getInstance(){
		if(instance==null){
			instance = new ProjectTypeItemTypeStatusAssignmentFacade();
		}
		return instance;
	}
	
	/**
	 * Gets the map with number of assignments for issue types in a projectType
	 * @param projectTypeID
	 * @return
	 */
	@Override
	public Map<Integer, Integer> loadNumberOfAssignmentsForIssueTypesInProjectType(Integer projectTypeID) {
		return pStatusDAO.loadNumberOfStatusesForIssueTypesInProjectType(projectTypeID);
	}
	
	/**
	 * Gets the number of assignments for an issue type in a projectType
	 * @param projectTypeID
	 * @param issueTypeID
	 * @return
	 */
	@Override
	public Integer loadNumberOfAssignmentsForIssueTypeInProjectType(Integer projectTypeID, Integer issueTypeID) {
		return pStatusDAO.loadNumberOfStatusesForIssueTypeInProjectType(projectTypeID, issueTypeID);
	}
	
	/**
	 * Gets the already assigned IDs
	 * @param projectTypeID
	 * @return
	 */
	@Override
	public Set<Integer> getAssignedIDsByProjectTypeIDAndItemType(Integer projectTypeID, Integer itemTypeID) {
		Set<Integer> assignedStatuses = new HashSet<Integer>();
		List<TPstateBean> projectTypeToIssueTypeToStatus = pStatusDAO.loadByProjectTypeAndIssueType(projectTypeID, itemTypeID);
		for (TPstateBean pstateBean : projectTypeToIssueTypeToStatus) {
			Integer status = pstateBean.getState();
			assignedStatuses.add(status);
		}
		return assignedStatuses;
	}
	
	/**
	 * Gets the complete list of assignable beans
	 * @param locale
	 * @return
	 */
	@Override
	public List<ILabelBean> getAllAssignables(Locale locale) {
		return StatusBL.loadAll(locale);
	}
	
	
	/**
	 * Gets the key for the assignment info general
	 * @return
	 */
	@Override
	public String getAssignmentInfoGeneralKey() {
		return "admin.customize.projectType.lbl.assignmentInfoStatusGeneral";
	}
	
	/**
	 * Gets the key for the assignment info 
	 * @return
	 */
	@Override
	public String getAssignmentInfoKey() {
		return "admin.customize.projectType.lbl.assignmentInfoStatus";
	}
	
	
	/**
	 * Creates a new assignment bean
	 * @param projectTypeID
	 * @param assignedID
	 * @return
	 */
	@Override
	public Object createAssignmentBean(Integer projectTypeID, Integer itemTypeID, Integer assignedID) {
		TPstateBean pstateBean = new TPstateBean();
		pstateBean.setProjectType(projectTypeID);
		pstateBean.setListType(itemTypeID);
		pstateBean.setState(assignedID);
		return pstateBean;
	}
	
	/**
	 * Saves an assignment
	 * @param assignmentBean
	 * @return
	 */
	@Override
	public Integer save(Object assignmentBean) {
		return pStatusDAO.save((TPstateBean)assignmentBean);
	}
	
	/**
	 * Deletes the assignments for a projectType
	 * @param projectTypeID
	 * @param itemTypeID
	 * @param assignedIDs
	 */
	@Override
	public void delete(Integer projectTypeID, Integer itemTypeID, List<Integer> assignedIDs) {
		pStatusDAO.delete(projectTypeID, itemTypeID, assignedIDs);
	}
}
