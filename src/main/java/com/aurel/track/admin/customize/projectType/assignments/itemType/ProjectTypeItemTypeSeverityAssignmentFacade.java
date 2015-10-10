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

package com.aurel.track.admin.customize.projectType.assignments.itemType;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.admin.customize.lists.systemOption.SeverityBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TPseverityBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.PSeverityDAO;

/**
 * Facade implementing priority assignments
 * @author Tamas
 *
 */
public class ProjectTypeItemTypeSeverityAssignmentFacade extends ProjectTypeItemTypeAssignmentBaseFacade {
	
	private static PSeverityDAO pSeverityDAO = DAOFactory.getFactory().getPSeverityDAO();
	
	private static ProjectTypeItemTypeSeverityAssignmentFacade instance;
	
	/**
	 * Return a ProjectTypeItemTypeAssignmentFacade instance which implements the severity assignments 
	 * @return
	 */
	public static ProjectTypeItemTypeSeverityAssignmentFacade getInstance(){
		if(instance==null){
			instance = new ProjectTypeItemTypeSeverityAssignmentFacade();
		}
		return instance;
	}
	
	/**
	 * Gets the map with number of assignments for issue types in a projectType
	 * @param projectTypeID
	 * @return
	 */
	public Map<Integer, Integer> loadNumberOfAssignmentsForIssueTypesInProjectType(Integer projectTypeID) {
		return pSeverityDAO.loadNumberOfSeveritiesForIssueTypesInProjectType(projectTypeID);
	}
	
	/**
	 * Gets the number of assignments for an issue type in a projectType
	 * @param projectTypeID
	 * @param issueTypeID
	 * @return
	 */
	public Integer loadNumberOfAssignmentsForIssueTypeInProjectType(Integer projectTypeID, Integer issueTypeID) {
		return pSeverityDAO.loadNumberOfSeveritiesForIssueTypeInProjectType(projectTypeID, issueTypeID);
	}
	
	/**
	 * Gets the already assigned IDs
	 * @param projectTypeID
	 * @return
	 */
	public Set<Integer> getAssignedIDsByProjectTypeIDAndItemType(Integer projectTypeID, Integer itemTypeID) {
		Set<Integer> assignedSeverities = new HashSet<Integer>();
		List<TPseverityBean> projectTypeToIssueTypeToSeverity = pSeverityDAO.loadByProjectTypeAndIssueType(projectTypeID, itemTypeID);
		for (TPseverityBean pseverityBean : projectTypeToIssueTypeToSeverity) {
			Integer severity = pseverityBean.getSeverity();
			assignedSeverities.add(severity);
		}
		return assignedSeverities;
	}
	
	/**
	 * Gets the complete list of assignable beans
	 * @param locale
	 * @return
	 */
	public List<ILabelBean> getAllAssignables(Locale locale) {
		return SeverityBL.loadAll(locale);
	}
	
	
	/**
	 * Gets the key for the assignment info general
	 * @return
	 */
	public String getAssignmentInfoGeneralKey() {
		return "admin.customize.projectType.lbl.assignmentInfoSeverityGeneral";
	}
	
	/**
	 * Gets the key for the assignment info 
	 * @return
	 */
	public String getAssignmentInfoKey() {
		return "admin.customize.projectType.lbl.assignmentInfoSeverity";
	}
	
	
	/**
	 * Creates a new assignment bean
	 * @param projectTypeID
	 * @param assignedID
	 * @return
	 */
	public Object createAssignmentBean(Integer projectTypeID, Integer itemTypeID, Integer assignedID) {
		TPseverityBean pseverityBean = new TPseverityBean();
		pseverityBean.setProjectType(projectTypeID);
		pseverityBean.setListType(itemTypeID);
		pseverityBean.setSeverity(assignedID);
		return pseverityBean;
	}
	
	/**
	 * Saves an assignment
	 * @param assignmentBean
	 * @return
	 */
	public Integer save(Object assignmentBean) {
		return pSeverityDAO.save((TPseverityBean)assignmentBean);
	}
	
	/**
	 * Deletes the assignments for a projectType
	 * @param projectTypeID
	 * @param itemTypeID
	 * @param assignedIDs
	 */
	public void delete(Integer projectTypeID, Integer itemTypeID, List<Integer> assignedIDs) {
		pSeverityDAO.delete(projectTypeID, itemTypeID, assignedIDs);
	}
}
