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

package com.aurel.track.util;

import java.io.Serializable;
import java.util.List;

/**
 * /**
 * Simple JavaBean to represent treeNode model
 * @author Adrian Bojani
 */
public class TreeNode implements Serializable, Comparable<TreeNode> {
	private static final long serialVersionUID = 1L;
	protected String id = null;
	protected String label=null;
	protected String icon=null;
	protected Boolean leaf=null;
	protected Boolean checked = null;
	//whether a node is selectable
	protected boolean selectable = true;
	protected List<TreeNode> children;

	public TreeNode(){
	}
	public TreeNode(String id, String label){
		this.id=id;
		this.label=label;
	}
	public TreeNode(String id, String label, String icon){
		this(id,label);
		this.icon=icon;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
		
	public Boolean getLeaf() {
		Boolean result=leaf;
		if(this.leaf==null){
			result=Boolean.valueOf(this.children==null||this.children.isEmpty());
		}
		return result;
	}

	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}
	
	public boolean isSelectable() {
		return selectable;
	}
	
	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}

	@Override
	public boolean equals(Object obj) {
		TreeNode treeNode = null;
		try {
			treeNode = (TreeNode)obj;
		} catch(Exception e) {
			return false;
		}
		if (treeNode==null) {
			return false;
		}
		if (this.id==null || treeNode.id==null) {
			return false;
		}
		return this.id.equals(treeNode.getId());
	}

	@Override
	public int hashCode() {
		if (this.id == null) {
			return 0;
		}
		else {
			return this.id.hashCode();
		}
	}

	/**
	 * Comparation is made by the label
	 */
	public int compareTo(TreeNode o) {
		TreeNode treeNode = null;
		try {
			treeNode = o;
		} catch (Exception e) {
			return 1;
		}
		if (treeNode==null) {
			return 1;
		}
		if (this.label==null && treeNode.label==null) {
			return 0;
		}
		if (this.label==null) {
			return -1;
		}
		if (treeNode.label==null) {
			return 1;
		}
		return this.label.compareTo(treeNode.label);
	}
	
	

	
}
