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

package com.aurel.track.itemNavigator.navigator;

import java.io.Serializable;
import java.util.List;

public class Navigator  implements Serializable{
	private static final long serialVersionUID = -4111459440732807184L;
	private Integer objectID;
	private String name;
	private View queryView;
	private View subFilterView;
	public Integer getObjectID() {
		return objectID;
	}
	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public View getQueryView() {
		return queryView;
	}

	public void setQueryView(View queryView) {
		this.queryView = queryView;
	}

	public View getSubFilterView() {
		return subFilterView;
	}

	public void setSubFilterView(View subFilterView) {
		this.subFilterView = subFilterView;
	}
}
