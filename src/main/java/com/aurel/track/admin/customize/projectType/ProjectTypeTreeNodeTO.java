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

package com.aurel.track.admin.customize.projectType;

import com.aurel.track.tree.TreeNodeBaseTO;


/**
 * Transfer object for a project type tree node
 * @author Tamas
 *
 */
public class ProjectTypeTreeNodeTO extends TreeNodeBaseTO {	
	/**
	 * The configuration type
	 */
	private Integer configType;
	/**
	 * The rootID of the configuration specific tree (project specific branch): it should 
	 * be the same format as in the "entire" tree 
	 */
	private String branchRoot;
	
	/**
	 * for workflows (branchRoot is not enough because the projectType node
	 * with this projectTypeID should be also visible)
	 */
	private Integer projectTypeID;
	
	
	public ProjectTypeTreeNodeTO(String id, String text, String iconCls, boolean leaf) {
		super(id, text, iconCls, leaf);		
	}
	
	public ProjectTypeTreeNodeTO(String id, String text, String iconCls, Integer configType) {
		super(id, text, iconCls, true);
		this.configType = configType;
	}
	
	public ProjectTypeTreeNodeTO(String id, String text, String iconCls,
			Integer configType, String branchRoot) {
		super(id, text, iconCls, true);
		this.configType = configType;
		this.branchRoot = branchRoot;
	}
	
	public String getBranchRoot() {
		return branchRoot;
	}
	public void setBranchRoot(String branchRoot) {
		this.branchRoot = branchRoot;
	}

	public Integer getConfigType() {
		return configType;
	}

	public void setConfigType(Integer configType) {
		this.configType = configType;
	}

	public Integer getProjectTypeID() {
		return projectTypeID;
	}

	public void setProjectTypeID(Integer projectTypeID) {
		this.projectTypeID = projectTypeID;
	}

	
	
	
}
