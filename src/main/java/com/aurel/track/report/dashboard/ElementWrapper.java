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

/**
 *  Element wrapper: State/Priority/User wrapper
 */
public class ElementWrapper {
	private String label;
	private Integer groupByField;
	private String icon;
	private int number;
	private int width;  // width in pixel
	private int percent;
	private int percentLate;
	private int widthLate; // width late in pixel

	public ElementWrapper(String label, int number,
						  int width, int percent, int widthLate, int percentLate) {
		this.widthLate = widthLate;
		this.percentLate = percentLate;
		this.label = label;
		this.number = number;
		this.width = width;
		this.percent = percent;
	}
	
	public ElementWrapper(String label, int number, int width, int percent) {
		this.label = label;
		this.number = number;
		this.width = width;
		this.percent = percent;
	}
   
	public ElementWrapper(String label) {
		super();
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public int getNumber() {
		return number;
	}

	public int getWidth() {
		return width;
	}

	public int getPercent() {
		return percent;
	}
	

	public int getWidthLate() {
		return widthLate;
	}

	public int getPercentLate() {
		return percentLate;
	}

	public Integer getGroupByField() {
		return groupByField;
	}

	public void setGroupByField(Integer groupByField) {
		this.groupByField = groupByField;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}
