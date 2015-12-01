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


package com.aurel.track.report.execute;

import java.util.Comparator;

import com.aurel.track.util.SortOrderUtil;

/**
 * This class handles sorting of SimpleTreeNodes. 
 * @author Tamas Ruff
 *
 */
public class SimpleTreeNodedComparator implements Comparator<SimpleTreeNode> {
	
	/**
	 * whether to sort descending or ascending
	 */
	private boolean isDescending;

	/**
	 * Creates and configures a comparator 
	 * @param isUp
	 * @param fieldID
	 */
	public SimpleTreeNodedComparator(boolean isDescending) {
		super();
		this.isDescending = isDescending;
		
	}

	/**
	 * Ascending or descending sort order
	 * @return
	 */
	private int getOrdering() {
		if (isDescending) {
			return -1;
		} else {
			return 1;
		}
	}	
	
	/**
	 * Compares two ReportBean according to the configured field and ascending order
	 */
	public int compare(SimpleTreeNode simpleTreeNode0, SimpleTreeNode simpleTreeNode1) {
		return SortOrderUtil.compareValue(simpleTreeNode0.getSortOrder(), simpleTreeNode1.getSortOrder(), getOrdering());
	}
	
}
