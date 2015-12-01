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

package com.aurel.track.move;

import java.util.Date;

import com.aurel.track.util.DateTimeUtils;
import com.google.common.base.Objects;

public class Node {

	private Date nodeDate;
	private Integer objectID;
	private int nodeType;
	private boolean nodeDateChanged;

	public interface NODE_TYPES {
		public static int START_DATE = 19;
		public static int END_DATE = 20;
	}

	public Node(Date nodeDate, Integer objectID, int nodeType) {
		this.nodeDate = nodeDate;
		this.objectID = objectID;
		this.nodeType = nodeType;
		this.nodeDateChanged = false;
	}

	public Date getNodeDate() {
		return nodeDate;
	}

	public void setNodeDate(Date nodeDate) {
		this.nodeDate = nodeDate;
	}

	public Integer getObjectID() {
		return objectID;
	}

	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
	}

	public int getNodeType() {
		return nodeType;
	}

	public void setNodeType(int nodeType) {
		this.nodeType = nodeType;
	}

	public boolean isNodeDateChanged() {
		return nodeDateChanged;
	}

	public void setNodeDateChanged(boolean nodeDateChanged) {
		this.nodeDateChanged = nodeDateChanged;
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(this.nodeDate, this.nodeType, this.objectID);
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (other == this) {
			return true;
		}
		if (!(other instanceof Node)) {
			return false;
		}
		Node aNode = (Node) other;
		if (DateTimeUtils.compareTwoDatesWithoutTimeValue(aNode.getNodeDate(), this.getNodeDate()) == 0 && aNode.getObjectID().equals(this.getObjectID()) && aNode.getNodeType() == this.getNodeType()) {
			return true;
		}
		return false;
	}
}
