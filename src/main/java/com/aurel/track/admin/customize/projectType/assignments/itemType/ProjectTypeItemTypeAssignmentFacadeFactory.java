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

import com.aurel.track.admin.customize.projectType.ProjectTypesBL.PROJECT_TYPE_ASSIGNMENTS;


/**
 * Factory for  project type simple assignment implementations
 * @author Tamas
 *
 */
public class ProjectTypeItemTypeAssignmentFacadeFactory {
	
	private static ProjectTypeItemTypeAssignmentFacadeFactory instance;
	public static ProjectTypeItemTypeAssignmentFacadeFactory getInstance(){
		if(instance==null){
			instance=new ProjectTypeItemTypeAssignmentFacadeFactory();
		}
		return instance;
	}

			
	/**
	 * Get the proper ProjectTypeSimpleAssignmentFacade for configType
	 * @param configType
	 * @return
	 */
	public ProjectTypeItemTypeAssignmentBaseFacade getProjectTypeSimpleAssignmentFacade(Integer configType) {
		if (configType!=null) {
			switch (configType.intValue()) {
			case PROJECT_TYPE_ASSIGNMENTS.PRIORITY_ASSIGNMENT:
				return ProjectTypeItemTypePriorityAssignmentFacade.getInstance();
			case PROJECT_TYPE_ASSIGNMENTS.SEVERITY_ASSIGNMENT:
				return ProjectTypeItemTypeSeverityAssignmentFacade.getInstance();
			case PROJECT_TYPE_ASSIGNMENTS.STATUS_ASSIGNMENT:
				return ProjectTypeItemTypeStatusAssignmentFacade.getInstance();
			}
		}
		return null;
	}
}
