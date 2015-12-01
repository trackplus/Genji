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

import java.util.Date;

import com.aurel.track.calendar.WorkDaysConfigImplementation;
import com.aurel.track.errors.ErrorData;


public class CalendarValidator implements Validator {
	//whether it is a working day for this person
	//now the personID is not set
	protected Integer personID;
	

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
		Date dateValue=(Date)value;
		if (WorkDaysConfigImplementation.isNonWorkingDay(dateValue, personID)) {
			return new ErrorData("common.err.interval.date.nonWorkingDay");
		}
		return null;
	}

	public Integer getPersonID() {
		return personID;
	}

	public void setPersonID(Integer personID) {
		this.personID = personID;
	}
	
}
