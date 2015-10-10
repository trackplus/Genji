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

package com.aurel.track.itemNavigator.cardView;

import com.aurel.track.util.IntegerStringBean;

import java.util.List;

/**
 * A transfer object containing the data needed for grouping by ("columns" in UI )
 */
public class GroupByTO {
	private List<IntegerStringBean> options;
	private Integer id;
	private String label;

	public GroupByTO(){
	}

	public GroupByTO(Integer id, String label) {
		this.id = id;
		this.label = label;
	}

	public List<IntegerStringBean> getOptions() {
		return options;
	}

	public void setOptions(List<IntegerStringBean> options) {
		this.options = options;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
