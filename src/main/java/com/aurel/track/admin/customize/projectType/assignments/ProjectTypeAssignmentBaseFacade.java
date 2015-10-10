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

package com.aurel.track.admin.customize.projectType.assignments;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.aurel.track.beans.ILabelBean;

/**
 * Facade for project type specific assignments
 * @author Tamas
 *
 */
public abstract class ProjectTypeAssignmentBaseFacade {

	/**
	 * Gets the assigned or not assigned beans
	 * @param allLabelBeansList
	 * @param assignedIDs
	 * @param assigned
	 * @param locale
	 * @return
	 */
	public static List<ILabelBean> getAssignedBeans(List<ILabelBean> assignableBeans,
			Set<Integer> assignedIDs, boolean assigned) {		
		List<ILabelBean> filteredBeans = new LinkedList<ILabelBean>();	
		for (ILabelBean assignableBean : assignableBeans) {
			if ((assignedIDs.contains(assignableBean.getObjectID()) && assigned) ||
					(!assignedIDs.contains(assignableBean.getObjectID()) && !assigned)) {
				filteredBeans.add(assignableBean);	
			}
		}
		return filteredBeans;
	}	
	
	/**
	 * Assign IDs to project type
	 * @param projectTypeID
	 * @param issueTypeID
	 * @param severityIDs
	 */
	public void assign(Integer projectTypeID, Integer itemTypeID, List<Integer> assignedIDs) {
		if (projectTypeID!=null && (!isItemTypeBased() || (isItemTypeBased() && itemTypeID!=null)) && assignedIDs!=null && !assignedIDs.isEmpty())
			for (Integer assignedID : assignedIDs) {
				Object assignmentBean = createAssignmentBean(projectTypeID, itemTypeID, assignedID);
				save(assignmentBean);
			}
	}
	
	/**
	 * Unassign IDs from project type
	 * @param projectTypeID
	 * @param itemTypeID
	 * @param assignedIDs
	 */
	public void unassign(Integer projectTypeID, Integer itemTypeID, List<Integer> assignedIDs) {
		if (projectTypeID!=null && (!isItemTypeBased() || (isItemTypeBased() && itemTypeID!=null)) && assignedIDs!=null && !assignedIDs.isEmpty()) {
			delete(projectTypeID, itemTypeID, assignedIDs);
		}
	}
	
	
	
	/**
	 * Whether the assignmnent is also item type based
	 * @return
	 */
	public abstract boolean isItemTypeBased();
	
	/**
	 * Encode the assignments 
	 * @param projectTypeID
	 * @param assignmentType
	 * @param issueTypeID
	 * @param locale
	 * @return
	 */
	public abstract String encodeJSON(Integer projectTypeID, Integer assignmentType, Integer issueTypeID, Locale locale);
	
	/**
	 * Gets the complete list of assignable beans
	 * @param locale
	 * @return
	 */
	public abstract List<ILabelBean> getAllAssignables(Locale locale);
	
	
	/**
	 * Gets the key for the assignment info 
	 * @return
	 */
	public abstract String getAssignmentInfoKey();
	
	
	/**
	 * Creates a new assignment bean
	 * @param projectTypeID
	 * @param itemTypeID
	 * @param assignedID
	 * @return
	 */
	public abstract Object createAssignmentBean(Integer projectTypeID, Integer itemTypeID, Integer assignedID);
	
	/**
	 * Saves an assignment
	 * @param assignmentBean
	 * @return
	 */
	public abstract Integer save(Object assignmentBean);
	
	/**
	 * Deletes the assignments for a projectType
	 * @param projectTypeID
	 * @param itemTypeID
	 * @param assignedIDs
	 */
	public abstract void delete(Integer projectTypeID, Integer itemTypeID, List<Integer> assignedIDs);
	
}
