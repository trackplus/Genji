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


package com.aurel.track.fieldType.runtime.validators;

import com.aurel.track.errors.ErrorData;


public class IntegerValidator implements Validator {

	Integer minValue;
	Integer maxValue;
	
	public Integer getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Integer maxValue) {
		this.maxValue = maxValue;
	}

	public Integer getMinValue() {
		return minValue;
	}

	public void setMinValue(Integer minValue) {
		this.minValue = minValue;
	}
	
	/**
	 * Validates an value for a field and returns ErrorData in case of validation error
	 * @param value 
	 * @return
	 */
	public ErrorData validateField(Object value){		
		if (value==null) {
			return null;	
		}
		Integer integerValue = (Integer)value;		
		if (minValue!=null && minValue.intValue()>integerValue.intValue()){
			ErrorData errorData = new ErrorData("common.err.interval.numeric.minValue",minValue);
			return errorData;
		}
		if (maxValue!=null && maxValue.intValue()<integerValue.intValue()){				
			ErrorData errorData = new ErrorData("common.err.interval.numeric.maxValue",maxValue);
			return errorData;
		}
		return null;
	}	
}
