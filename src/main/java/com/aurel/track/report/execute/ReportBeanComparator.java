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
import java.util.List;
import java.util.Map;

import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.itemNavigator.layout.group.GroupFieldTO;
import com.aurel.track.util.SortOrderUtil;

/**
 * This class handles sorting of reportBean columns.
 * The only requirement is that the object of the sort column
 * implements the Comparable interface. Nulls are handled graciously.
 * @author Tamas Ruff
 *
 */
public class ReportBeanComparator implements Comparator<ReportBean> {

	/**
	 * whether to sort descending or ascending
	 */
	private boolean isDesc;

	/**
	 * the field to sort by (if grouping is active than inside each group, otherwise over all items)
	 */
	private Integer fieldID;

	/**
	 * key: the fieldID for a tree field
	 * value: a map with:
	 * 			key: field value in workItem
	 * 			value: sort order for that value
	 */
	private Map<Integer, Map<Integer, Integer>> treeFieldToValueToSortOrderMap;

	/**
	 * The fields to group by with settings
	 */
	private List<GroupFieldTO> groupByList;

	/**
	 * whether the sorting is needed inside the group
	 * For TQL result there should be sorted only for the group but leave the original sort order inside the group
	 */
	private boolean sortNeeded;

	/**
	 *  The default sort order when nothing is specified, or the last when sorting by the specified sortorder brings to equality.
	 *  Anyway we will have a complete ordering, this aspect is important by grouping:
	 *  For example by expanding/collapsing a grouping node a new ReportBeans will be created, a resorting will take place
	 *  but the groupingLimits will not be recalculated and that can bring to wrongly shown grouping because comparing
	 *  the same (for ex. null) values again can bring to a modified sort order.
	 */
	private Integer DEFAULT_OR_LASTFIELD = new Integer(SystemFields.ISSUENO);

	private boolean DEFAULT_SORTORDER = false;


	public ReportBeanComparator() {
		super();
		this.isDesc = DEFAULT_SORTORDER;
		this.fieldID = DEFAULT_OR_LASTFIELD;
	}

	/**
	 * Creates and configures a comparator
	 * @param isUp
	 * @param fieldID
	 */
	public ReportBeanComparator(Boolean isUp, Integer fieldID, List<GroupFieldTO> groupByList, boolean sortNeeded) {
		super();
		if (isUp==null) {
			this.isDesc = DEFAULT_SORTORDER;
		} else {
			this.isDesc = isUp.booleanValue();
		}
		if (fieldID==null) {
			this.fieldID = DEFAULT_OR_LASTFIELD;
		} else {
			this.fieldID = fieldID;
		}
		this.groupByList = groupByList;
		this.sortNeeded = sortNeeded;
	}

	/**
	 * Ascending or descending sort order
	 * @return
	 */
	private int getSortOrder() {
		if (isDesc) {
			return -1;
		} else {
			return 1;
		}
	}

	/**
	 * Sets the sort order map for tree fields
	 * @param treeFieldToSortOrderMap
	 */
	public void setTreeFieldToSortOrderMap(
			Map<Integer, Map<Integer, Integer>> treeFieldToSortOrderMap) {
		this.treeFieldToValueToSortOrderMap = treeFieldToSortOrderMap;
	}

	/**
	 * Compares two ReportBean according to the configured field and ascending order
	 */
	public int compare(ReportBean reportBean0, ReportBean reportBean1) {
		if ((reportBean0==null) && (reportBean1==null)) {
			return 0;
		}
		if (reportBean0==null) {
			return getSortOrder() * -1;
		}
		if (reportBean1==null) {
			return getSortOrder() * 1;
		}
		Comparable<Object> value0;
		Comparable<Object> value1;
		int compareResult;
		//sort by grouping levels
		if (groupByList!=null && !groupByList.isEmpty()) {
			for (GroupFieldTO groupField : groupByList) {
				Integer groupFieldID = groupField.getFieldID();
				if (treeFieldToValueToSortOrderMap!=null) {
					Map<Integer, Integer> treeSortOrderMap = treeFieldToValueToSortOrderMap.get(groupFieldID);
					if (treeSortOrderMap!=null) {
						Integer objectID0 = (Integer)reportBean0.getWorkItemBean().getAttribute(groupFieldID);
						Integer objectID1 = (Integer)reportBean1.getWorkItemBean().getAttribute(groupFieldID);
						if (objectID0!=null && objectID1!=null) {
							//order is 1 because only inside the same level is possible to order, not globally throughout the hierarchy.
							//the treeSortOrderMap is already sorted on levels
							compareResult = SortOrderUtil.compareValue(treeSortOrderMap.get(objectID0), treeSortOrderMap.get(objectID1), 1);
							if (compareResult!=0) {
								//return if in different tree field group
								return compareResult;
							}
						} else {
							//at least one tree field is not set
							compareResult = SortOrderUtil.compareValue(objectID0, objectID1, groupField.isDescending());
							if (compareResult!=0) {
								//return if in different tree field group
								return compareResult;
							}
						}
					}
				}
				value0 = reportBean0.getSortOrder(groupFieldID);
				value1 = reportBean1.getSortOrder(groupFieldID);
				compareResult = SortOrderUtil.compareValue(value0, value1, groupField.isDescending());
				if (compareResult!=0) {
					//return only if in different group (subgroup)
					return compareResult;
				}
			}
		}

		if (sortNeeded) {
			//apply the sort field (inside the last subgroup, if grouping is set or "globally" otherwise)
			value0 = reportBean0.getSortOrder(fieldID);
			value1 = reportBean1.getSortOrder(fieldID);
			compareResult = SortOrderUtil.compareValue(value0, value1, getSortOrder());
			if (compareResult!=0) {
				//return only if different sort order
				return compareResult;
			} else {
				value0 = reportBean0.getSortOrder(DEFAULT_OR_LASTFIELD);
				value1 = reportBean1.getSortOrder(DEFAULT_OR_LASTFIELD);
				return  SortOrderUtil.compareValue(value0, value1, 1);
			}
		} else {
			//leave the original order (for TQL queries)
			return 0;
		}
	}


}
