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

package com.aurel.track.report.execute;

import java.io.Serializable;
import java.util.List;

import com.aurel.track.util.SortOrderUtil;

/**
 *
 *  JavaBean to represent simple tree
 */
public class SimpleTreeNode implements Serializable, Comparable<SimpleTreeNode> {
	private static final long serialVersionUID = 1L;
	protected Integer objectID = null;
	protected Comparable<Object> sortOrder = null;
	protected String label;
	protected List<SimpleTreeNode> children;

	public SimpleTreeNode(){
	}
	
	public SimpleTreeNode(Integer objectID, String label, Comparable<Object> sortOrder){
		this.objectID = objectID;
		this.label = label;
		this.sortOrder = sortOrder;
	}

	public Integer getObjectID() {
		return objectID;
	}
	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
	}
	public List<SimpleTreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<SimpleTreeNode> children) {
		this.children = children;
	}

	public Comparable<Object> getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(Comparable<Object> sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public boolean equals(Object obj) {
		SimpleTreeNode treeNode = null;
		try {
			treeNode = (SimpleTreeNode)obj;
		} catch(Exception e) {
			return false;
		}
		if (treeNode==null) {
			return false;
		}
		if (this.objectID==null || treeNode.objectID==null) {
			return false;
		}
		return this.getObjectID().equals(treeNode.getObjectID());
	}

	@Override
	public int hashCode() {
		if (this.getObjectID() == null) {
			return 0;
		}
		else {
			return this.getObjectID().hashCode();
		}
	}

	/**
	 * Comparation is made by the label
	 */
	public int compareTo(SimpleTreeNode simpleTreeNode) {
		return SortOrderUtil.compareValue(this.sortOrder, simpleTreeNode.getSortOrder(), 1);
	}

}
