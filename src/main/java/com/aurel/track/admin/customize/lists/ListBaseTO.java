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

package com.aurel.track.admin.customize.lists;


/**
 * Transfer object for a grid row
 * @author Tamas
 *
 */
public class ListBaseTO {
	private Integer objectID;
	private String label = null;
	private boolean canEdit=false;
	private boolean canDelete=false;
	//CRUD label list for the grid row (edit/delete the row) 
	private Integer rowListForLabel;
	//the full path to icon: used for system lists and 
	//custom and system list options (not used for custom lists)
	private String icon;
	private String node;
	
	public Integer getObjectID() {
		return objectID;
	}
	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}			
	public boolean isCanEdit() {
		return canEdit;
	}
	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}
	public boolean isCanDelete() {
		return canDelete;
	}
	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public Integer getRowListForLabel() {
		return rowListForLabel;
	}
	public void setRowListForLabel(Integer rowListForLabel) {
		this.rowListForLabel = rowListForLabel;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
}
