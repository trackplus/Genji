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
import com.aurel.track.fieldType.runtime.custom.select.CustomExtensibleSelectRT;


public class ExtensibleSelectRequiredValidator implements Validator {

	/**
	 * Validates an value for a field and returns ErrorData in case of validation error
	 * @param value 
	 * @return
	 */
	public ErrorData validateField(Object value){
		//ADD_NEW
		Integer[] valueArr = null;
		try {
			valueArr = (Integer[])value;
		} catch (Exception e) {
			// TODO: handle exception
		}
		 if(valueArr==null || valueArr.length==0 ||  
				 (valueArr.length>0 && CustomExtensibleSelectRT.ADD_NEW.equals(valueArr[0]))){
			 ErrorData errorData =new ErrorData("common.err.required");
			 return errorData;
		 }
		return null;
	}
}
