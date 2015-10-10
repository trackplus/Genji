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

package com.aurel.track.report.dashboard;

import java.util.List;
import java.util.Objects;

import com.aurel.track.Constants;

/**
 *
 */
public class ProjectWrapper implements Comparable{
	private String label;
	private List list;

	//used for query
	private Integer groupByFieldType;
	private Integer groupByFieldType2;
	private Integer projectID;
	private Integer releaseID;
	private boolean openOnly;


	private List secondList;
	private String symbol;

	private Integer numberResolved = 0;
	private Integer numberOpen = 0;
	private Integer number = 0;
	private Integer widthResolved = 0;
	private Integer widthOpen = 0;
	private boolean areResolved = true;

	public boolean getAreResolved() {
		return areResolved;
	}

	public void setAreResolved(boolean areResolved) {
		this.areResolved = areResolved;
	}

	public ProjectWrapper(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public int compareTo(Object o) {
		return this.label.compareTo(((ProjectWrapper)o).getLabel());
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(!(obj instanceof ProjectWrapper)) {
			return false;
		}
		return Objects.equals(((ProjectWrapper)obj).getLabel(), this.getLabel());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((label == null) ? 0 : label.hashCode());
		return result;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public List getSecondList() {
		return secondList;
	}

	public void setSecondList(List secondList) {
		this.secondList = secondList;
	}

	public Integer getNumberResolved() {
		return numberResolved;
	}

	public void setNumberResolved(Integer numberResolved) {
		this.numberResolved = numberResolved;
		this.number = this.numberResolved + this.numberOpen;
		if (this.number != 0) {
			this.widthOpen = this.numberOpen * Constants.DASHBOARDBARLENGTH / (this.number);
			this.widthResolved = this.numberResolved * Constants.DASHBOARDBARLENGTH / (this.number);
		}
		else {
			this.widthOpen = Constants.DASHBOARDBARLENGTH;
			this.widthResolved = 0;
		}
	}

	public Integer getNumberOpen() {
		return numberOpen;
	}

	public void setNumberOpen(Integer numberOpen) {
		this.numberOpen = numberOpen;
		this.number = this.numberResolved + this.numberOpen;
		if (this.number != 0) {
			this.widthOpen = this.numberOpen * Constants.DASHBOARDBARLENGTH / (this.number);
			this.widthResolved = this.numberResolved * Constants.DASHBOARDBARLENGTH / (this.number);
		}
		else {
			this.widthOpen = Constants.DASHBOARDBARLENGTH;
			this.widthResolved = 0;
		}
	}

	public Integer getWidthResolved() {
		return widthResolved;
	}

	public Integer getWidthOpen() {
		return widthOpen;
	}

	public Integer getNumber() {
		return number;
	}

	public Integer getGroupByFieldType() {
		return groupByFieldType;
	}

	public void setGroupByFieldType(Integer groupByFieldType) {
		this.groupByFieldType = groupByFieldType;
	}

	public Integer getGroupByFieldType2() {
		return groupByFieldType2;
	}

	public void setGroupByFieldType2(Integer groupByFieldType2) {
		this.groupByFieldType2 = groupByFieldType2;
	}

	public Integer getProjectID() {
		return projectID;
	}

	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	public Integer getReleaseID() {
		return releaseID;
	}

	public void setReleaseID(Integer releaseID) {
		this.releaseID = releaseID;
	}

	public boolean isOpenOnly() {
		return openOnly;
	}

	public void setOpenOnly(boolean openOnly) {
		this.openOnly = openOnly;
	}

}
