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

package com.aurel.track.admin.user.filtersInMenu;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class FilterForPersonTO implements Comparable<FilterForPersonTO> {

	private Integer filterID;
	private Integer personID;
	private String filterLabel;
	private String personName;

	private static final Logger LOGGER = LogManager.getLogger(FilterForPersonTO.class);

	/**
	 * Whether it is the first role in a project (otherwise the project should
	 * be not shown)
	 */
	private boolean isFirst;

	public boolean isFirst() {
		return isFirst;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	@Override
	public int compareTo(FilterForPersonTO projectRoleTO) {
		if (projectRoleTO == null) {
			return 1;
		}
		int compareResult;
		compareResult = compareValue(this.getFilterLabel(), projectRoleTO.getFilterLabel());
		if (compareResult != 0) {
			return compareResult;
		}
		return compareValue(this.getPersonName(), projectRoleTO.getPersonName());
	}

	@Override
	public boolean equals(Object  projectRoleTO) {
		if (projectRoleTO == null) {
			return false;
		}
		if(!(projectRoleTO instanceof FilterForPersonTO)) {
			return false;
		}

		if(compareTo((FilterForPersonTO)projectRoleTO) == 0) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if(filterLabel != null) {
			result = prime * result
				+ ((filterLabel == null) ? 0 : filterLabel.hashCode());
		}else {
			result = prime * result
					+ ((personName == null) ? 0 : personName.hashCode());
		}
		return result;
	}



	/**
	 * Compare two "atomic" values
	 *
	 * @param value0
	 * @param value1
	 * @return
	 */
	private int compareValue(String value0, String value1) {
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
			return value0.compareTo(value1);
		} catch (Exception e) {
			LOGGER.debug("Cannot sort this bean.");
			return 0; // can't sort
		}
	}

	public Integer getFilterID() {
		return filterID;
	}

	public void setFilterID(Integer filterID) {
		this.filterID = filterID;
	}

	public Integer getPersonID() {
		return personID;
	}

	public void setPersonID(Integer personID) {
		this.personID = personID;
	}

	public String getFilterLabel() {
		return filterLabel;
	}

	public void setFilterLabel(String filterLabel) {
		this.filterLabel = filterLabel;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}
}
