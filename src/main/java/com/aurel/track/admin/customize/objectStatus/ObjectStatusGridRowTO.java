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

package com.aurel.track.admin.customize.objectStatus;

/**
 * Transfer object for a grid row
 * @author Tamas
 *
 */
public class ObjectStatusGridRowTO {
	private String label = null;
	private String typeflagLabel;
	//whether this node can be edited/deleted by the current user
	private boolean modifiable=false;		
	//label list for the node (edit/delete the node)  
	private Integer listForLabel;		
	private String node;
	
				
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
	public Integer getListForLabel() {
		return listForLabel;
	}
	public void setListForLabel(Integer listForLabel) {
		this.listForLabel = listForLabel;
	}	
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public String getTypeflagLabel() {
		return typeflagLabel;
	}
	public void setTypeflagLabel(String typeflagLabel) {
		this.typeflagLabel = typeflagLabel;
	}
		
}
