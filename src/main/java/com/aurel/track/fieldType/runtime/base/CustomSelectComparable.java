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

package com.aurel.track.fieldType.runtime.base;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Class for sorting the simple custom list entries
 * The sortOrder is not enough because we can't be sure that only one list is involved for this field in different contexts
 * The sortOrder is local in a list, so it can happen that two entries from different lists have the same sortOrder, consequently
 * by sorting/grouping the different values might be mixed just because they have the same sortOrder
 * TODO we do not take into account multiple selections. For multiple selections always the minimal value is taken see CustomSelectBaseRT
 * @author Tamas
 *
 */
public class CustomSelectComparable implements Comparable, Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(CustomSelectComparable.class);
	public static Integer COMPARABLE_LIST = new Integer(1);
	public static Integer COMPARABLE_SORTORDER = new Integer(2);
	//sorted because first compare the list and then the sortOrderValue
	private SortedMap<Integer, Comparable> comparableMap = new TreeMap<Integer, Comparable>();

	public CustomSelectComparable(SortedMap<Integer, Comparable> comparableMap) {
		super();
		this.comparableMap = comparableMap;

	}

	public Map<Integer, Comparable> getComparableMap() {
		return comparableMap;
	}

	@Override
	public int compareTo(Object o) {
		CustomSelectComparable compositeComparable = (CustomSelectComparable)o;
		Map<Integer, Comparable> paramComparableMap = compositeComparable.getComparableMap();
		if ((comparableMap == null) && (paramComparableMap == null)) {
			return 0;
		}
		if (comparableMap == null) {
			return -1;
		}
		if (paramComparableMap == null) {
			return 1;
		}
		Iterator<Integer> itrComparableMap = comparableMap.keySet().iterator();
		while (itrComparableMap.hasNext()) {
			Integer key = itrComparableMap.next();
			Comparable value0 = comparableMap.get(key);
			Comparable value1 = paramComparableMap.get(key);
			if ((value0 == null) && (value1 == null)) {
				return 0;
			}
			if (value0 == null) {
				return -1;
			}
			if (value1 == null) {
				return 1;
			}
			try {
				int compareResult = value0.compareTo(value1);
				if (compareResult!=0) {
					//return now only if the part if different
					return compareResult;
				}
			} catch (Exception e) {
				LOGGER.warn("Sorting the values " + value0 + " of class " + value0.getClass().getName() +
						" and " + value1 + " of class " + value1.getClass().getName() + " failed with " + e.getMessage(), e);
			}
		}
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(!(obj instanceof CustomSelectComparable)) {
			return false;
		}
	    if(compareTo(obj) == 0) {
	    	return  true;
	    }
	    return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if(comparableMap != null) {
			Iterator<Integer> itrComparableMap = comparableMap.keySet().iterator();
			while (itrComparableMap.hasNext()) {
				Integer key = itrComparableMap.next();
				Comparable value0 = comparableMap.get(key);
				result = prime * result
						+ ((value0 == null) ? 0 : value0.hashCode());
			}
		}else {
			result = prime * result;
		}
		return result;
	}
}
