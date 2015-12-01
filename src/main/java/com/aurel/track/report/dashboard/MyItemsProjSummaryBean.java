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


package com.aurel.track.report.dashboard;

import java.io.Serializable;

import com.aurel.track.Constants;

public class MyItemsProjSummaryBean implements Serializable {

	private static final long serialVersionUID = -2199115108893348271L;

	private Integer filterID=null;
	private String projName = null;
	private Integer projId = null;
	private int numItems = 0;
	private int numItemsOverdue = 0;
	private double dlate = 0;
	private int greenWidth = 0;
	private int redWidth = 0;
	
	/**
	 * @return Returns the projName.
	 */
	public String getProjName() {
		return projName;
	}

	/**
	 * @param projName The projName to set.
	 */
	public void setProjName(String projName) {
		this.projName = projName;
	}

	public Integer getProjId() {
		return projId;
	}

	public void setProjId(Integer projId) {
		this.projId = projId;
	}

	public int getNumItems() {
		return numItems;
	}

	public void setNumItems(int numItems) {
		this.numItems = numItems;
	}

	public int getNumItemsOverdue() {
		return numItemsOverdue;
	}

	public void setNumItemsOverdue(int numItemsOverdue) {
		this.numItemsOverdue = numItemsOverdue;
	}

	public double getDlate() {
		dlate = (numItems==0?0:(double) getNumItemsOverdue() / getNumItems()*Constants.DASHBOARDBARLENGTH.doubleValue());
		return dlate;
	}

	public int getGreenWidth() {
		greenWidth = (int) Math.ceil(Constants.DASHBOARDBARLENGTH.intValue()- getDlate());
		return greenWidth;
	}

	public int getRedWidth() {
		redWidth = (int) Math.ceil(getDlate());
		return redWidth;
	}

	public Integer getFilterID() {
		return filterID;
	}

	public void setFilterID(Integer filterID) {
		this.filterID = filterID;
	}
}
