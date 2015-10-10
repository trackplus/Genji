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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.dao.DAOFactory;

public class SortOrderUtil {

	private static final Logger LOGGER = LogManager.getLogger(SortOrderUtil.class);
	
	/**
	 * Compare two "atomic" values
	 * @param value0
	 * @param value1
	 * @return
	 */
	public static int compareValue(Comparable value0, Comparable value1, int order) {
		if ((value0 == null) && (value1 == null)) {
			return 0;
		}
		if (value0 == null) {
			return order * -1;
		}
		if (value1 == null) {
			return order * 1;
		}
		try {
			return order * value0.compareTo(value1);
		} catch (Exception e) {
			LOGGER.warn("Sorting the values " + value0 + " of class " + value0.getClass().getName() + 
					" and " + value1 + " of class " + value1.getClass().getName() + " failed with " + e.getMessage(), e);
			return 0; // can't sort
		}
	}
	
	/**
	 * Compare two "atomic" values
	 * @param value0
	 * @param value1
	 * @return
	 */
	public static int compareValue(Comparable value0, Comparable value1, boolean descending) {
		if ((value0 == null) && (value1 == null)) {
			return 0;
		}
		if (value0 == null) {
			//return order * -1;
			if (descending) {
				return 1;
			} else {
				return -1;
			}
		}
		if (value1 == null) {
			//return order * 1;
			if (descending) {
				return -1;
			} else {
				return 1;
			}
		}
		try {
			//return order * value0.compareTo(value1);
			if (descending) {
				return value0.compareTo(value1) * -1;
			} else {
				return value0.compareTo(value1);
			}
		} catch (Exception e) {
			LOGGER.warn("Sorting the values " + value0 + " of class " + value0.getClass().getName() + 
					" and " + value1 + " of class " + value1.getClass().getName() + " failed with " + e.getMessage(), e);
			return 0; // can't sort
		}
	}
	
	/**
	 * Compare two "atomic" values
	 * @param value0
	 * @param value1
	 * @return
	 */
	public static int compareValue(Comparable value0, Comparable value1) {
		if ((value0 == null) && (value1 == null)) {
			return 0;
		}
		if (value0 == null) {
			return 1;
		}
		if (value1 == null) {
			return -1;
		}
		try {
			return value0.compareTo(value1);
		} catch (Exception e) {			
			return 0; // can't sort
		}
	}
	
	/**
	 * Sets the sort order on the affected entries after a drag and drop operation
	 * Do not sets the sortOrder for the draggedNode. This should be set explicitly after returning from this method
	 * @param draggedSortOrder
	 * @param droppedToSortOrder
	 * @param sortOrderColumn
	 * @param tableName
	 * @param specificCriteria
	 * @param before
	 * @param locale
	 * @return
	 */
	public static Integer dropNear(Integer draggedSortOrder, Integer droppedToSortOrder,
			String sortOrderColumn, String tableName, String specificCriteria, boolean before) {
		if (draggedSortOrder==null || droppedToSortOrder==null) {
			LOGGER.warn("The draggedSortOrder " + draggedSortOrder + " droppedToSortOrder " + droppedToSortOrder);
			//after normalization this can't happen
			return draggedSortOrder; 
		}
		String sqlStmt = null;
		String droppedCriteria = "";
		String draggedCriteria = "";
		Integer newSortOrder;
		if (draggedSortOrder.equals(droppedToSortOrder)) {
			//same sortOrder: do nothing 
			LOGGER.debug("The draggedSortOrder " + draggedSortOrder + " equals droppedToSortOrder " + droppedToSortOrder);
			return draggedSortOrder; 
		}
		int inc = 0;
		if (draggedSortOrder>droppedToSortOrder) {
			inc = 1;
			if (before) {
				droppedCriteria = " AND " + sortOrderColumn + " >= " + droppedToSortOrder;
				draggedCriteria = sortOrderColumn + " < " + draggedSortOrder;
				newSortOrder = droppedToSortOrder;
			} else {
				droppedCriteria = " AND " + sortOrderColumn + " > " + droppedToSortOrder;
				draggedCriteria = sortOrderColumn + " < " + draggedSortOrder;
				newSortOrder = droppedToSortOrder+1;
			}
		} else {
			inc = -1;
			if (before) {
				droppedCriteria = " AND " + sortOrderColumn + " < " + droppedToSortOrder;
				draggedCriteria = sortOrderColumn + " > " + draggedSortOrder;
				newSortOrder = droppedToSortOrder-1;
			} else {
				droppedCriteria = " AND " + sortOrderColumn + " <= " + droppedToSortOrder;
				draggedCriteria = sortOrderColumn + " > " + draggedSortOrder;
				newSortOrder = droppedToSortOrder;
			}
		}
		sqlStmt = "UPDATE " + tableName + " SET " + sortOrderColumn + " = " + 
		sortOrderColumn + " + " + inc + " WHERE " + 
		draggedCriteria + droppedCriteria + specificCriteria;
		DAOFactory.getFactory().executeUpdateStatement(sqlStmt);
		return newSortOrder;
	}
	
	/**
	 * Sets the sort order on the affected entries after a drag and drop operation
	 * Do not sets the sortOrder for the draggedNode. This should be set explicitly after returning from this method
	 * @param draggedSortOrder
	 * @param droppedToSortOrder
	 * @param sortOrderColumn
	 * @param tableName
	 * @param specificCriteria
	 * @param before
	 * @param locale
	 * @return
	 */
	public static void dropNear(Integer draggedSortOrder, Integer droppedToSortOrder, boolean before, SortedMap<Integer, Object> sortedMap) {
		if (draggedSortOrder==null || droppedToSortOrder==null) {
			LOGGER.warn("The draggedSortOrder " + draggedSortOrder + " droppedToSortOrder " + droppedToSortOrder);
			//after normalization this can't happen
			return; 
		}
		if (draggedSortOrder.equals(droppedToSortOrder)) {
			//same sortOrder: do nothing 
			LOGGER.debug("The draggedSortOrder " + draggedSortOrder + " equals droppedToSortOrder " + droppedToSortOrder);
			return; 
		}
		Object draggedValue = sortedMap.get(draggedSortOrder);
		if (draggedSortOrder>droppedToSortOrder) {
			//move up
			List<Integer> reverseSortorders = new ArrayList<Integer>();
			for (Map.Entry<Integer, Object> entry : sortedMap.entrySet()) {
				Integer sortOrder = entry.getKey();
				//gather the old values in a local map from the affected section of the map
				boolean greatherThanDropped = false;
				if (before) {
					greatherThanDropped = sortOrder>=droppedToSortOrder;
				} else {
					greatherThanDropped = sortOrder>droppedToSortOrder;
				}			
				if (greatherThanDropped && sortOrder<draggedSortOrder) {
					reverseSortorders.add(0, sortOrder);
				}
			}
			Integer targetSortOrder = draggedSortOrder;
			for (Integer oldSortorder : reverseSortorders) {
				sortedMap.put(targetSortOrder, sortedMap.get(oldSortorder));
				targetSortOrder = oldSortorder;
			}
			if (before) {
				sortedMap.put(droppedToSortOrder, draggedValue);
			} else {
				sortedMap.put(droppedToSortOrder+1, draggedValue);
			}
		} else {
			//move down
			Integer targetSortOrder = draggedSortOrder;
			for (Map.Entry<Integer, Object> entry : sortedMap.entrySet()) {
				Integer sortOrder = entry.getKey();
				//gather the old values in a local map from the affected section of the map
				boolean lessThanDropped = false;
				if (before) {
					lessThanDropped = sortOrder<droppedToSortOrder;
				} else {
					lessThanDropped = sortOrder<=droppedToSortOrder;
				}
				if (lessThanDropped && sortOrder>draggedSortOrder) {
					sortedMap.put(targetSortOrder, sortedMap.get(sortOrder));
					targetSortOrder = sortOrder;
				}
			}
			if (before) {
				sortedMap.put(droppedToSortOrder-1, draggedValue);
			} else {
				sortedMap.put(droppedToSortOrder, draggedValue);
			}
		}
	}
}
