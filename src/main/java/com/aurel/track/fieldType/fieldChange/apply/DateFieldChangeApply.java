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


package com.aurel.track.fieldType.fieldChange.apply;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.calendar.WorkDaysConfigImplementation;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.fieldChange.FieldChangeSetters;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;

/**
 * Apply field change for date fields
 * @author Tamas
 *
 */
public class DateFieldChangeApply extends GenericFieldChangeApply {
	private static final Logger LOGGER = LogManager.getLogger(DateFieldChangeApply.class);
	
	public DateFieldChangeApply(Integer fieldID) {
		super(fieldID);
	}
	
	

	/**
	 * Sets the workItemBean's attribute
	 * @param workItemContext
	 * @param workItemBean
	 * @param fieldID
	 * @param parameterCode
	 * @param value	
	 * @return ErrorData if an error is found
	 */
	@Override
	public List<ErrorData> setWorkItemAttribute(WorkItemContext workItemContext,
			TWorkItemBean workItemBean, Integer parameterCode, Object value) {
		if (getSetter()==FieldChangeSetters.SET_NULL || getSetter()==FieldChangeSetters.SET_REQUIRED) {
			return super.setWorkItemAttribute(workItemContext, workItemBean, parameterCode, value);
		}
		if (getSetter()==FieldChangeSetters.SET_TO) {
			Date dateValue = null;
			try {
				dateValue = (Date)value;
			} catch (Exception e) {
				LOGGER.warn("Getting the date value for " + value +  " failed with " + e.getMessage(), e);
			}
			workItemBean.setAttribute(activityType, parameterCode, dateValue);
			return null;
		}
		if (getSetter()==FieldChangeSetters.SET_TO_DATE_FIELD_VALUE) {
			Integer dateFieldID = null;
			try {
				dateFieldID = (Integer)value;
			} catch (Exception e) {
				LOGGER.warn("Getting the integer value for " + value +  " failed with " + e.getMessage(), e);
			}
			if (dateFieldID!=null) {
				Object otherDateValue = null;
				if (dateFieldID.intValue()==SystemFields.CREATEDATE && workItemBean.getObjectID()==null) {
					//new item, create date not yet set
					otherDateValue = new Date();
				} else {
					otherDateValue = workItemBean.getAttribute(dateFieldID, parameterCode);
				}
				if (otherDateValue!=null) {
					workItemBean.setAttribute(activityType, parameterCode, otherDateValue);
				}
			}
			return null;
		}
		//the other relations depend on original date
		Object originalValue = workItemBean.getAttribute(activityType, parameterCode);
		if (originalValue==null) {
			return null;
		}
		Date originalDate = null;
		try {
			originalDate = (Date)originalValue;
		} catch (Exception e) {
			LOGGER.warn("Converting the original value " + originalValue +  " to Date failed with " + e.getMessage(), e);
		}
		if (originalDate==null) {
			return null;
		}
		
		switch (getSetter()) {
		case FieldChangeSetters.MOVE_BY_DAYS:
			Integer intValue = null;
			try {
				intValue = (Integer)value;
			} catch (Exception e) {
				LOGGER.warn("Getting the integer value for " + value +  " failed with " + e.getMessage(), e);
			}
			workItemBean.setAttribute(activityType, parameterCode, 
					shiftByDays(originalDate, intValue));
			break;
		}
		return null;
	}
	
	/**
	 * Shift a date by a number of days 
	 * @param originalDate
	 * @param daysOffset
	 * @return
	 */
	protected static Date shiftByDays(Date originalDate, Integer daysOffset) {
		if (daysOffset!=null && daysOffset.intValue()!=0) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(originalDate);
			calendar.add(Calendar.DATE, daysOffset.intValue());
			while (WorkDaysConfigImplementation.isNonWorkingDay(calendar.getTime(), null)) {
				calendar.add(Calendar.DATE, 1);
			}
			return calendar.getTime();
		} 
		return originalDate;
	}
	
	
}
