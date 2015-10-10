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



package com.aurel.track.admin.customize.treeConfig;

import com.aurel.track.tree.TreeNodeBaseTO;


/**
 * Used as model for tree config  
 * @author Adrian Bojani
 *
 */
public class TreeConfigNodeTO extends TreeNodeBaseTO {
	private String configType;
	private String type;
	private boolean defaultConfig;
	private boolean inherited;
	//the deepest folders: only these nodes should be  
	//reloaded because after any operation 
	//only the leaf nodes are changed 
	private boolean childrenAreLeaf;
	//limit the number of refreshes
	private Integer issueType;
	private Integer projectType;
	private Integer project;
	//the assigned or inherited screenID, used only for screen and workflow assignments
	private Integer assignedID;
		
	/**
	 * Constructor
	 * @param id
	 * @param configType
	 * @param type
	 * @param text
	 * @param leaf
	 */
	public TreeConfigNodeTO(String id, String configType, 
			String type, String text, boolean leaf){
		this.id=id;
		this.configType=configType;
		this.type=type;
		this.text=text;
		this.leaf=leaf;
	}
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the configType
	 */
	public String getConfigType() {
		return configType;
	}

	/**
	 * @param configType the configType to set
	 */
	public void setConfigType(String configType) {
		this.configType = configType;
	}
	
	public boolean isInherited() {
		return inherited;
	}

	public void setInherited(boolean inherited) {
		this.inherited = inherited;
	}

	public boolean isDefaultConfig() {
		return defaultConfig;
	}

	public void setDefaultConfig(boolean defaultConfig) {
		this.defaultConfig = defaultConfig;
	}

	public boolean isChildrenAreLeaf() {
		return childrenAreLeaf;
	}

	public void setChildrenAreLeaf(boolean childrenAreLeaf) {
		this.childrenAreLeaf = childrenAreLeaf;
	}

	public Integer getIssueType() {
		return issueType;
	}

	public void setIssueType(Integer issueType) {
		this.issueType = issueType;
	}

	public Integer getProjectType() {
		return projectType;
	}

	public void setProjectType(Integer projectType) {
		this.projectType = projectType;
	}

	public Integer getProject() {
		return project;
	}

	public void setProject(Integer project) {
		this.project = project;
	}

	public Integer getAssignedID() {
		return assignedID;
	}

	public void setAssignedID(Integer assignedID) {
		this.assignedID = assignedID;
	}
	
}
