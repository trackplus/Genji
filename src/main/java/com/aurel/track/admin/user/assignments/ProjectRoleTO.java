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

package com.aurel.track.admin.user.assignments;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class ProjectRoleTO implements Comparable<ProjectRoleTO> {

	private static final Logger LOGGER = LogManager.getLogger(ProjectRoleTO.class);

	/**
	 * projectID and roleID are null only for inherited roles (person through
	 * group)
	 */
	private Integer projectID;
	private Integer roleID;

	private String projectLabel;
	private String roleLabel;
	/**
	 * direct assignment or derived from a group
	 */
	private boolean direct;
	/**
	 * Whether it is the first role in a project (otherwise the project should
	 * be not shown)
	 */
	private boolean isFirst;

	public Integer getProjectID() {
		return projectID;
	}

	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	public Integer getRoleID() {
		return roleID;
	}

	public void setRoleID(Integer roleID) {
		this.roleID = roleID;
	}

	public String getProjectLabel() {
		return projectLabel;
	}

	public void setProjectLabel(String projectLabel) {
		this.projectLabel = projectLabel;
	}

	public String getRoleLabel() {
		return roleLabel;
	}

	public void setRoleLabel(String roleLabel) {
		this.roleLabel = roleLabel;
	}

	public boolean isDirect() {
		return direct;
	}

	public void setDirect(boolean direct) {
		this.direct = direct;
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	@Override
	public int compareTo(ProjectRoleTO projectRoleTO) {
		if (projectRoleTO == null) {
			return 1;
		}
		int compareResult;
		compareResult = compareValue(this.getProjectLabel(), projectRoleTO.getProjectLabel());
		if (compareResult != 0) {
			return compareResult;
		}
		return compareValue(this.getRoleLabel(), projectRoleTO.getRoleLabel());
	}

	@Override
	public boolean equals(Object projectRoleTO) {
		if (projectRoleTO == null) {
			return false;
		}
		if(!(projectRoleTO instanceof ProjectRoleTO)) {
			return false;
		}
		if(compareTo((ProjectRoleTO)projectRoleTO) == 0) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((roleLabel == null) ? 0 : roleLabel.hashCode());
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

}
