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

package com.aurel.track.admin.project.assignments;


/**
 * The structure of a project assignment node's id field
 * @author Tamas
 *
 */
public class RoleAssignmentTokens {
	private Integer projectID;
	private Integer roleID;
	private Integer personID;
		
	public RoleAssignmentTokens() {
		super();
	}
		
	/**
	 * Creates a hadcoded assignment node ID
	 * @param projectID
	 */
	public RoleAssignmentTokens(Integer projectID) {
		super();
		this.projectID = projectID;
			
	}	
		
	public RoleAssignmentTokens(Integer projectID, Integer roleID) {
		super();
		this.projectID = projectID;
		this.roleID = roleID;
	}

	/**
	 * Create an issueType specific assignment nodeID  
	 * @param projectID
	 * @param roleID
	 * @param personID
	 */
	public RoleAssignmentTokens(Integer projectID, Integer roleID, Integer personID) {
		super();
		this.projectID = projectID;
		this.roleID = roleID;
		this.personID = personID;
	}		

	public Integer getProjectID() {
		return projectID;
	}

	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}
	
	public Integer getRoleID() {
		return roleID;
	}

	public void setRoleID(Integer roleID) {
		this.roleID = roleID;
	}

	public Integer getPersonID() {
		return personID;
	}

	public void setPersonID(Integer personID) {
		this.personID = personID;
	}
	
}
