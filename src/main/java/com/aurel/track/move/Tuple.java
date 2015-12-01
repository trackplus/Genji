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

import java.text.SimpleDateFormat;

import com.google.common.base.Objects;

public class Tuple {
	private Node source;
	private Node target;
	private Edge edge;

	public Tuple(Node source, Node target, Edge edge) {
		this.source = source;
		this.target = target;
		this.edge = edge;
	}

	public Node getSource() {
		return source;
	}

	public void setSource(Node source) {
		this.source = source;
	}

	public Node getTarget() {
		return target;
	}

	public void setTarget(Node target) {
		this.target = target;
	}

	public Edge getEdge() {
		return edge;
	}

	public void setEdge(Edge edge) {
		this.edge = edge;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (other == this) {
			return true;
		}
		if (!(other instanceof Tuple)) {
			return false;
		}
		Tuple aTuple = (Tuple) other;
		if (aTuple.getSource().equals(this.getSource()) && aTuple.getTarget().equals(this.getTarget()) && aTuple.getEdge().equals(this.getEdge())) {

			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.edge, this.source, this.target);
	}

	@Override
	public String toString() {
		SimpleDateFormat smp = new SimpleDateFormat("yyyy-MM-dd");
		String s = "Source: " + getSource().getObjectID().toString() + " Date: " + smp.format(getSource().getNodeDate()) + " Type: " + getSource().getNodeType();
		String t = " Target: " + getTarget().getObjectID().toString() + " Date: " + smp.format(getTarget().getNodeDate()) + " Type: " + getTarget().getNodeType();
		String e = " Edge: " + String.valueOf(getEdge().getEdgeType());
		return s + t + e;
	}

}

