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

public class MeetingTO implements Serializable{
	
	private static final long serialVersionUID = 8143250957470965062L;
	
	private String date;
	private Integer objectID;
	private String synopsis;
	private Integer number,numberResolved,numberOpen;
	private Integer widthResolved,widthOpen;
	
	public Integer getObjectID() {
		return objectID;
	}
	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
	}
	public String getSynopsis() {
		return synopsis;
	}
	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public Integer getNumberResolved() {
		return numberResolved;
	}
	public void setNumberResolved(Integer numberResolved) {
		this.numberResolved = numberResolved;
	}
	public Integer getNumberOpen() {
		return numberOpen;
	}
	public void setNumberOpen(Integer numberOpen) {
		this.numberOpen = numberOpen;
	}
	public Integer getWidthResolved() {
		return widthResolved;
	}
	public void setWidthResolved(Integer widthResolved) {
		this.widthResolved = widthResolved;
	}
	public Integer getWidthOpen() {
		return widthOpen;
	}
	public void setWidthOpen(Integer widthOpen) {
		this.widthOpen = widthOpen;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
