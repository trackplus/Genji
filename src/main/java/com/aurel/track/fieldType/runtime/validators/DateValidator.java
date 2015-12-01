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


package com.aurel.track.fieldType.runtime.validators;

import java.util.Calendar;
import java.util.Date;

import com.aurel.track.errors.ErrorData;


public class DateValidator implements Validator {
	protected Date minDate;
	protected Date maxDate;
	protected boolean minNow=false;
	protected boolean maxNow=false;
	protected static Calendar maxCalendar = Calendar.getInstance();
	static {
		maxCalendar.set(Calendar.YEAR, 9999);
		maxCalendar.set(Calendar.MONTH, 11);
		maxCalendar.set(Calendar.DATE, 31);
	}

	/**
	 * Implements the date validator
	 * Validates an attribute for a field 
	 * and returns a map in case of validation error
	 * - key: fieldID
	 * - value: ValidatorData  
	 * @return
	 */
	@Override
	public ErrorData validateField(Object value){
		if(value==null){
			return null;
		}
		if(minNow){
			minDate=new Date();
		}
		if(maxNow){
			maxDate=new Date();
		}
		Date dateValue=(Date)value;
		if (minDate!=null) {
			//minCalendar.setTime(minDate);
			//minCalendar = CalendarUtil.clearTime(minCalendar);
			//Date minDayBegin = minCalendar.getTime();
			if (minDate.after(dateValue)) {
				ErrorData errorData = new ErrorData("common.err.interval.date.minValue",minDate);
				return errorData;
			}
		}
		if (maxDate==null) {
			maxDate = maxCalendar.getTime();
		}
		if (maxDate!=null) {
			//maxCalendar.setTime(maxDate);
			//maxCalendar = CalendarUtil.clearTime(maxCalendar);
			//maxCalendar.add(Calendar.DATE, 1);
			//Date maxDayEnd = maxCalendar.getTime();
			if (maxDate.before(dateValue) || maxDate.equals(dateValue)) {
				ErrorData errorData =new ErrorData("common.err.interval.date.maxValue",maxDate);
				return errorData;
			}
		}
		return null;
	}
	/**
	 * @return the maxDate
	 */
	public Date getMaxDate() {
		return maxDate;
	}
	/**
	 * @param maxDate the maxDate to set
	 */
	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}
	/**
	 * @return the maxNow
	 */
	public boolean isMaxNow() {
		return maxNow;
	}
	/**
	 * @param maxNow the maxNow to set
	 */
	public void setMaxNow(boolean maxNow) {
		this.maxNow = maxNow;
	}
	/**
	 * @return the minDate
	 */
	public Date getMinDate() {
		return minDate;
	}
	/**
	 * @param minDate the minDate to set
	 */
	public void setMinDate(Date minDate) {
		this.minDate = minDate;
	}
	/**
	 * @return the minNow
	 */
	public boolean isMinNow() {
		return minNow;
	}
	/**
	 * @param minNow the minNow to set
	 */
	public void setMinNow(boolean minNow) {
		this.minNow = minNow;
	}
}
