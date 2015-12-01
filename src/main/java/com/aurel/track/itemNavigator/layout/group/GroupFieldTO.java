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

package com.aurel.track.itemNavigator.layout.group;


/**
 * Transfer object for a grouping field
 * @author Tamas
 *
 */
public class GroupFieldTO extends SortFieldTO {
	private Integer sortPosition;
	private boolean collapsed;
	
	public GroupFieldTO() {
		super();
	}
	
	public GroupFieldTO(Integer fieldID) {
		super(fieldID);
	}
	
	public GroupFieldTO(Integer fieldID, String label) {
		super(fieldID, label);
	}
	
	public GroupFieldTO(Integer fieldID, String label, String name) {
		super(fieldID, label, name);
	}
	
	
	public GroupFieldTO(Integer fieldID, boolean descending, boolean collapsed) {
		super(fieldID);
		this.descending = descending;
		this.collapsed = collapsed;
	}

	public Integer getSortPosition() {
		return sortPosition;
	}
	public void setSortPosition(Integer sortPosition) {
		this.sortPosition = sortPosition;
	}

	public boolean isCollapsed() {
		return collapsed;
	}
	
	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
	}
	
	@Override
	public String toString() {
		return "Level " + getSortPosition() + " grouping field " + getFieldID() + 
				" descending " + isDescending() + " collapsed " + isCollapsed();
	}
	
}
