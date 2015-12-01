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

import com.aurel.track.errors.ErrorData;


public class StringValidator implements Validator {
	private Integer minLength;
	private Integer maxLength;
	/**
	 * @return the maxLength
	 */
	public Integer getMaxLength() {
		return maxLength;
	}


	/**
	 * @param maxLength the maxLength to set
	 */
	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}


	/**
	 * @return the minLength
	 */
	public Integer getMinLength() {
		return minLength;
	}


	/**
	 * @param minLength the minLength to set
	 */
	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}


	/**
	 * Validates an value for a field and returns ErrorData in case of validation error
	 * @param value 
	 * @return
	 */
	@Override
	public ErrorData validateField(Object value){
		String textValue = (String)value;
		if(textValue==null||textValue.length()==0){
			return null;
		}
		if (minLength!=null && minLength.intValue()>textValue.length()){
				ErrorData errorData = new ErrorData("common.err.interval.text.minValue",minLength);
				return errorData;
		}
		if (maxLength!=null && maxLength.intValue()<textValue.length()){				
				ErrorData errorData = new ErrorData("common.err.interval.text.maxValue",maxLength);
				return errorData;
		}
		return null;
	}
}
