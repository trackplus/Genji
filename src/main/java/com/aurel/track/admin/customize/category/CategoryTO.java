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

package com.aurel.track.admin.customize.category;

/**
 * Base object for category grid and tree node
 * @author Tamas
 *
 */
public class CategoryTO {
	protected String categoryType;
	protected String label = null;
	//no right to edit
	protected boolean modifiable = false;
	//not linked with rights it is read only anyway 
	protected boolean readOnly = false;
	protected boolean deletable = false;
	protected String nodeID;
	protected boolean leaf;
	//meaningful only for filter leaf nodes, null otherwise
	protected Boolean treeFilter;
	//meaningful only for report leaf nodes, null otherwise
	protected Boolean reportConfigNeeded;
	protected String iconCls;
	


	public CategoryTO(String nodeID, String categoryType, String label, 
			boolean modifiable, boolean leaf) {
		super();
		this.nodeID = nodeID;
		this.categoryType = categoryType;
		this.label = label;
		this.modifiable = modifiable;
		this.deletable = modifiable;
		this.leaf = leaf;
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public boolean isModifiable() {
		return modifiable;
	}
	public void setModifiable(boolean modifiable) {
		this.modifiable = modifiable;
	}	

	public String getNodeID() {
		return nodeID;
	}

	public void setNodeID(String nodeID) {
		this.nodeID = nodeID;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public Boolean isTreeFilter() {
		return treeFilter;
	}

	public void setTreeFilter(Boolean treeFilter) {
		this.treeFilter = treeFilter;
	}
	
	public Boolean getReportConfigNeeded() {
		return reportConfigNeeded;
	}

	public void setReportConfigNeeded(Boolean reportConfigNeeded) {
		this.reportConfigNeeded = reportConfigNeeded;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public boolean isDeletable() {
		return deletable;
	}

	public void setDeletable(boolean deletable) {
		this.deletable = deletable;
	}
	
	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	
	
}
