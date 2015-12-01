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

package com.aurel.track.admin.customize.projectType;


/**
 * The structure of a project type node's id field
 * @author Tamas
 *
 */
public class ProjectTypeIDTokens {	
	private Integer projectTypeID;	
	//If null this is a main projectType node
	private Integer configType;
	
		
	public ProjectTypeIDTokens() {
		super();
	}
	
	/**
	 * Create a projectType node ID
	 * @param projectTypeID
	 */
	public ProjectTypeIDTokens(Integer projectTypeID) {
		super();
		this.projectTypeID = projectTypeID;		
	}

	/**
	 * Creates a hadcoded assignment node ID
	 * @param projectTypeID
	 * @param configType
	 */
	public ProjectTypeIDTokens(Integer projectTypeID, Integer configType) {
		super();
		this.projectTypeID = projectTypeID;
		this.configType = configType;	
	}	
	
	public Integer getProjectTypeID() {
		return projectTypeID;
	}

	public void setProjectTypeID(Integer projectTypeID) {
		this.projectTypeID = projectTypeID;
	}

	public Integer getConfigType() {
		return configType;
	}

	public void setConfigType(Integer configType) {
		this.configType = configType;
	}
	
	
}
