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

package com.aurel.track.report.group;

import java.util.Comparator;

import com.aurel.track.util.SortOrderUtil;

/**
 * This class handles sorting of reportBean columns. 
 * The only requirement is that the object of the sort column
 * implements the Comparable interface. Nulls are handled graciously.
 * @author Tamas Ruff
 *
 */
public class GroupLimitComparator implements Comparator<GroupLimitBean> {
	//private int order;
	boolean descending;
	
	public GroupLimitComparator() {
		super();
	}

	/**
	 * Creates and configures a comparator 
	 * @param isUp
	 * @param fieldID
	 */
	public GroupLimitComparator(boolean descending) {
		super();
		this.descending = descending;
	}
	
	
	/**
	 * Compares two ReportBean according to the configured field and ascending order
	 */
	public int compare(GroupLimitBean groupLimitBean0, GroupLimitBean groupLimitBean1) {
		if ((groupLimitBean0==null) && (groupLimitBean1==null)) {
			return 0;
		}
		if (groupLimitBean0==null) {
			if (descending) {
				return 1;
			} else {
				return -1;
			}
		}
		if (groupLimitBean1==null) {
			//return order * 1;
			if (descending) {
				return -1;
			} else {
				return 1;
			}
		}
		int sortOrder = SortOrderUtil.compareValue(groupLimitBean0.getGroupSortOrder(), groupLimitBean1.getGroupSortOrder(), descending);
		if (sortOrder==0) {
			return SortOrderUtil.compareValue(groupLimitBean0.getGroupLabel(), groupLimitBean1.getGroupLabel(), descending);
		} else {
			return sortOrder;
		}
		
	}
	
	
}
