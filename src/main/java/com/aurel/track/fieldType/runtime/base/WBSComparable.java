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
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.util.GeneralUtils;

/**
 * Class for sorting the composite values
 * The parts of the composite are sorted one by one until a
 * different sort order is found at a certain level
 * @author Tamas
 *
 */
public class WBSComparable implements Comparable, Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(WBSComparable.class);

	private Integer workItemID = null;
	private List<Integer> wbsOnLevelsList = null;

	public WBSComparable(String wbsNumber, String wbsSeparator, Integer workItemID) {
		super();
		this.wbsOnLevelsList = GeneralUtils.createIntegerListFromStringArr(wbsNumber.split(wbsSeparator));
		this.workItemID = workItemID;
	}

	public List<Integer> getWbsOnLevelsList() {
		return wbsOnLevelsList;
	}


	public Integer getWorkItemID() {
		return workItemID;
	}

	public int compareTo(Object o) {
		WBSComparable wbsComparable = (WBSComparable)o;
		List<Integer> paramWbsOnLevelsList = wbsComparable.getWbsOnLevelsList();
		if ((wbsOnLevelsList==null || wbsOnLevelsList.isEmpty()) &&
				(paramWbsOnLevelsList==null || paramWbsOnLevelsList.isEmpty())) {
			return 0;
		}
		if (wbsOnLevelsList==null || wbsOnLevelsList.isEmpty()) {
			return -1;
		}
		if (paramWbsOnLevelsList==null || paramWbsOnLevelsList.isEmpty()) {
			return 1;
		}
		int length = wbsOnLevelsList.size();
		int paramLength = paramWbsOnLevelsList.size();
		int minLength = length;
		if (minLength>paramLength) {
			minLength=paramLength;
		}
		for (int i=0; i<minLength;i++) {
			Integer wbsOnLevel = wbsOnLevelsList.get(i);
			Integer paramWbsOnLevel = paramWbsOnLevelsList.get(i);
			if (wbsOnLevel==null && paramWbsOnLevel==null) {
				return 0;
			}
			if (wbsOnLevel==null) {
				return -1;
			}
			if (paramWbsOnLevel==null) {
				return 1;
			}
			try {
				int compareResult = wbsOnLevel.compareTo(paramWbsOnLevel);
				if (compareResult!=0) {
					//return only if the part if different
					return compareResult;
				}
			} catch (Exception e) {
				LOGGER.warn("Sorting the values " + wbsOnLevel + " of class " + wbsOnLevel.getClass().getName() +
						" and " + paramWbsOnLevel + " of class " + paramWbsOnLevel.getClass().getName() + " failed with " + e.getMessage(), e);
			}
		}
		//ancestor-descendant relation: the longer the path the later in wbs
		return Integer.valueOf(length).compareTo(Integer.valueOf(paramLength));
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(!(obj instanceof WBSComparable)) {
			return false;
		}
		if(compareTo(obj) == 0) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if(wbsOnLevelsList != null) {
			int length = wbsOnLevelsList.size();
			for (int i=0; i<length; i++) {
				Integer wbsOnLevel = wbsOnLevelsList.get(i);
				result = prime * result
						+ ((wbsOnLevel == null) ? 0 : wbsOnLevel.hashCode());
			}
		}else {
			result = prime * result;
		}
		return result;
	}

}
