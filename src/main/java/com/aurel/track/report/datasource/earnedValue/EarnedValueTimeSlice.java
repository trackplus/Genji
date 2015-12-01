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

package com.aurel.track.report.datasource.earnedValue;

import java.util.Date;

public class EarnedValueTimeSlice {
	private Double plannedValue;
	private Double actualCost;
	private Double earnedvalue;
	private Date date;
		
	public Double getPlannedValue() {
		return plannedValue;
	}
	public void setPlannedValue(Double plannedValue) {
		this.plannedValue = plannedValue;
	}
	public Double getActualCost() {
		return actualCost;
	}
	public void setActualCost(Double actualCost) {
		this.actualCost = actualCost;
	}
	public Double getEarnedvalue() {
		return earnedvalue;
	}
	public void setEarnedvalue(Double earnedvalue) {
		this.earnedvalue = earnedvalue;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}	
	
}
