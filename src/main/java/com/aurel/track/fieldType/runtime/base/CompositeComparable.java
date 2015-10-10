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
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Class for sorting the composite values
 * The parts of the composite are sorted one by one until a
 * different sort order is found at a certain level
 * @author Tamas
 *
 */
public class CompositeComparable implements Comparable, Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(CompositeComparable.class);
	private Map<Integer, Comparable> comparableMap = new HashMap<Integer, Comparable>();
	private int numberOfParts;

	public CompositeComparable(Map<Integer, Comparable> comparableMap,
			int numberOfParts) {
		super();
		this.comparableMap = comparableMap;
		this.numberOfParts = numberOfParts;
	}

	public Map<Integer, Comparable> getComparableMap() {
		return comparableMap;
	}

	public int getNumberOfParts() {
		return numberOfParts;
	}
	@Override
	public int compareTo(Object o) {
		CompositeComparable compositeComparable = (CompositeComparable)o;
		Map<Integer, Comparable> paramComparableMap = compositeComparable.getComparableMap();
		if ((comparableMap==null) && (paramComparableMap==null)) {
			return 0;
		}
		if (comparableMap==null) {
			return -1;
		}
		if (paramComparableMap==null) {
			return 1;
		}
		for (int i=0; i<numberOfParts;i++) {
			Integer parameterCode = new Integer(i+1);
			Comparable value0 = comparableMap.get(parameterCode);
			Comparable value1 = paramComparableMap.get(parameterCode);
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if(comparableMap != null) {
			for (int i=0; i<numberOfParts;i++) {
				Integer parameterCode = new Integer(i+1);
				Comparable value0 = comparableMap.get(parameterCode);
				result = prime * result
						+ ((value0 == null) ? 0 : value0.hashCode());
			}
		}else {
			result = prime * result;
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(!(obj instanceof CompositeComparable)) {
			return false;
		}
	     if(compareTo(obj) == 0) {
	    	 return true;
	     }
	     return false;
	}

	/**
	 * Used by serializing the XML datasource for jasper reports
	 */
	public String toString() {
		long sortValue =0;
			try {
			if (numberOfParts!=0) {
				for (int i = 1; i <= numberOfParts; i++) {
					Comparable value = comparableMap.get(i);
					if (value!=null) {
						sortValue+= (Integer)value*Math.pow(1000, numberOfParts-i);
					}
				}
			}
		} catch(Exception e) {

		}
		return String.valueOf(sortValue);
	}

}
