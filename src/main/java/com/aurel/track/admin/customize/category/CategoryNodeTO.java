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

package com.aurel.track.admin.customize.category;

/**
 * Used as model for categories
 * @author Tamas
 *
 */
public class CategoryNodeTO extends CategoryTO {
	private boolean canAddChild=false;
	private boolean canCopy=false;
	private int level=1;
	
	public CategoryNodeTO(String nodeID, String categoryType,
			String label, boolean canAddChild, boolean canCopy, boolean readOnly) {
		super(nodeID, categoryType, label, false, false);
		this.canAddChild = canAddChild;
		this.canCopy = canCopy;
		this.readOnly = readOnly;
	}
	
	public CategoryNodeTO(String nodeID, String categoryType, 
			String label, boolean modifiable, boolean canAddChild, boolean canCopy, boolean leaf) {
		super(nodeID, categoryType, label, modifiable, leaf);
		this.canAddChild = canAddChild;
		this.canCopy = canCopy;
	}

	public boolean isCanAddChild() {
		return canAddChild;
	}

	public void setCanAddChild(boolean canAddChild) {
		this.canAddChild = canAddChild;
	}

	public boolean isCanCopy() {
		return canCopy;
	}

	public void setCanCopy(boolean canCopy) {
		this.canCopy = canCopy;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
