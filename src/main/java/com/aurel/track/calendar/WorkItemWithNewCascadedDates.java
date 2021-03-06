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

package com.aurel.track.calendar;

import java.util.Date;

import com.aurel.track.beans.TWorkItemBean;

public class WorkItemWithNewCascadedDates {
	private TWorkItemBean workItemBean;
	private Date starDate;
	private Date endDate;
	private boolean needsCascade = true;
	private boolean save = false;

	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getStarDate() {
		return starDate;
	}
	public void setStarDate(Date starDate) {
		this.starDate = starDate;
	}
	
	public boolean isNeedsCascade() {
		return needsCascade;
	}
	public void setNeedsCascade(boolean needsCascade) {
		this.needsCascade = needsCascade;
	}
	public TWorkItemBean getWorkItemBean() {
		return workItemBean;
	}
	public void setWorkItemBean(TWorkItemBean workItemBeam) {
		this.workItemBean = workItemBeam;
	}
	public boolean isSave() {
		return save;
	}
	public void setSave(boolean save) {
		this.save = save;
	}
}
