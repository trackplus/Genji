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

package com.aurel.track.admin.customize.lists;


/**
 * The structure of a list option node's id field
 * @author Tamas
 *
 */
public class ListOptionIDTokens {
	//whether the list is global or project specific
	private Integer repository;
	//set only for hardcoded project nodes and for project specific lists in tree and in grid
	private Integer projectID;
	//System or custom
	private Integer type;
	//specified for all list and list entry nodes
	//for lists: it represents the id of the list
	//for folder options it represents the listID of the children options' (only for cascading custom lists)
	//for leaf options it represents the option's list (we need this for leafs to know where the system list the entry is from)
	private Integer childListID;
	//null for lists, set only for list entries
	private Integer optionID;
	//makes the difference between adding a new custom list or custom list child (non parent) entry by: 
	//for both optionID (new) and listID is null 
	private boolean leaf;
	
	
	public ListOptionIDTokens() {
		super();
	}

	public ListOptionIDTokens(Integer repository, Integer projectID, Integer type, Integer listID, boolean leaf) {
		super();
		this.repository = repository;
		this.projectID = projectID;
		this.type = type;
		this.childListID = listID;
		this.leaf = leaf;
	}
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}	

	public Integer getChildListID() {
		return childListID;
	}

	public void setChildListID(Integer childListID) {
		this.childListID = childListID;
	}

	public Integer getOptionID() {
		return optionID;
	}

	public void setOptionID(Integer optionID) {
		this.optionID = optionID;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public Integer getProjectID() {
		return projectID;
	}

	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	public Integer getRepository() {
		return repository;
	}

	public void setRepository(Integer repository) {
		this.repository = repository;
	}

	

	
	
}
