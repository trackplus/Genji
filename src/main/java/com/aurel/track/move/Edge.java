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

import com.aurel.track.beans.TWorkItemLinkBean;
import com.google.common.base.Objects;

public class Edge {

	private int duration;
	private TWorkItemLinkBean link;
	private int edgeType;

	public interface EDGE_TYPES {
		public static int VIRTUAL_EDGE = 1;
		public static int DURATION = 2;
		public static int LINK = 3;
	}

	public Edge(int duration, TWorkItemLinkBean link, int edgeType) {
		this.duration = duration;
		this.link = link;
		this.edgeType = edgeType;
	}

	public Edge(int edgeType) {
		this.duration = -1;
		this.link = null;
		this.edgeType = edgeType;
	}

	public Edge(TWorkItemLinkBean link, int edgeType) {
		this.duration = -1;
		this.link = link;
		this.edgeType = edgeType;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getEdgeType() {
		return edgeType;
	}

	public void setEdgeType(int edgeType) {
		this.edgeType = edgeType;
	}

	public TWorkItemLinkBean getLink() {
		return link;
	}

	public void setLink(TWorkItemLinkBean link) {
		this.link = link;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.duration, this.edgeType, this.link);
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (other == this) {
			return true;
		}
		if (!(other instanceof Edge)) {
			return false;
		}
		Edge anEdge = (Edge) other;
		if (anEdge.getDuration() == this.getDuration() && anEdge.getLink().equals(this.getLink()) && anEdge.getEdgeType() == this.getEdgeType()) {
			return true;
		}
		return false;
	}
}
