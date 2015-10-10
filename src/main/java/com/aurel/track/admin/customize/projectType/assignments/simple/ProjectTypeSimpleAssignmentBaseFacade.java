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

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.aurel.track.admin.customize.projectType.ProjectTypesBL;
import com.aurel.track.admin.customize.projectType.assignments.ProjectTypeAssignmentBaseFacade;
import com.aurel.track.admin.customize.projectType.assignments.ProjectTypeAssignmentJSON;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TProjectTypeBean;
import com.aurel.track.resources.LocalizeUtil;

/**
 * Facade for simple assignments
 * @author Tamas
 *
 */
public abstract class ProjectTypeSimpleAssignmentBaseFacade extends ProjectTypeAssignmentBaseFacade {
	
	/**
	 * Gets the possible labelBeans to select, depending on the restrictions for projectType
	 * The labelBean of an existing assignment should always be included even if it is not any more valid for projectType  
	 * @param projectTypeID
	 * @param actualAssignedID
	 * @param locale
	 * @return
	 */
	public List<ILabelBean> getPossibleBeans(Integer projectTypeID, Integer actualAssignedID, Locale locale) {
		List<ILabelBean> labelBeans = getAllAssignables(locale);
		Set<Integer> assignedIDs = null;
		if (projectTypeID!=null) {
			assignedIDs = getAssignedIDsByProjectTypeID(projectTypeID);
		}	
		if (assignedIDs!=null && !assignedIDs.isEmpty()) {
			for (Iterator<ILabelBean> iterator = labelBeans.iterator(); iterator.hasNext();) {
				ILabelBean labelBean = iterator.next();
				Integer objectID = labelBean.getObjectID();
				if (!assignedIDs.contains(objectID) && (actualAssignedID==null || !objectID.equals(actualAssignedID))) {
						iterator.remove();
				}
			}
		}
		return labelBeans;
	}
	
	/**
	 * Gets then json of a simple assignment
	 * @param projectTypeID
	 * @param locale
	 * @return
	 */
	public String encodeJSON(Integer projectTypeID, Integer assignmentType, Integer itemType, Locale locale) {
		String projectTypeLabel = "";
		if (projectTypeID!=null) {
			TProjectTypeBean projectTypeBean = ProjectTypesBL.loadByPrimaryKey(projectTypeID);
			if (projectTypeBean!=null) {
				projectTypeLabel = projectTypeBean.getLabel();
			}
		}
		Set<Integer> assigneIDs = getAssignedIDsByProjectTypeID(projectTypeID);
		List<ILabelBean> assignableBeans = getAllAssignables(locale);
		List<ILabelBean> assigned = getAssignedBeans(assignableBeans, assigneIDs, true);
		List<ILabelBean> unassigned = getAssignedBeans(assignableBeans, assigneIDs, false);
		String assignmentInfo = LocalizeUtil.getParametrizedString(getAssignmentInfoKey(), new Object[] {projectTypeLabel}, locale);
		if (isDynamicIcon()) {
			return ProjectTypeAssignmentJSON.encodeSimpleAssignmentWithDynamicIcon(assigned, unassigned, assignmentInfo, projectTypeID, assignmentType);
		} else {
			return ProjectTypeAssignmentJSON.encodeSimpleAssignmentWithIconCls(assigned, unassigned, assignmentInfo, getIconCssClass());
		}
	}
	
	
	
	/**
	 * Whether the assignmnent is also item type based
	 * @return
	 */
	public boolean isItemTypeBased() {
		return false;
	}
	
	/**
	 * Gets the already assigned IDs
	 * @param projectTypeID
	 * @return
	 */
	public abstract Set<Integer> getAssignedIDsByProjectTypeID(Integer projectTypeID);
	
	
	/**
	 * Whether the icon is dynamic or static
	 * If static the getIconCssClass() should return a non null value
	 * @return
	 */
	public abstract boolean isDynamicIcon();
	
	/**
	 * Get icon css class: either this or getIcon() should return null
	 * @return
	 */
	public String getIconCssClass() {
		return null;
	}
	
	
	
}
