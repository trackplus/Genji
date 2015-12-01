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


public class DoubleValidator implements Validator {
	private Double minDouble;
	private Double maxDouble;
	private Integer maxDecimalDigits;
	
	/**
	 * Validates an value for a field and returns ErrorData in case of validation error
	 * @param value 
	 * @return
	 */
	@Override
	public ErrorData validateField(Object value){
		if (value==null) {
			return null;
		}
		Double doubleValue = (Double)value;		
		if (minDouble!=null && minDouble.doubleValue()>doubleValue.doubleValue()) {
			ErrorData errorData = new ErrorData("common.err.interval.numeric.minValue", minDouble);
			return errorData;
		}
		if (maxDouble!=null && maxDouble.doubleValue()<doubleValue.doubleValue()) {				
			ErrorData errorData = new ErrorData("common.err.interval.numeric.maxValue", maxDouble);
			return errorData;
		}
		if (maxDecimalDigits!=null) {
			String strDoubleValue = doubleValue.toString();
			int decimalDigitsNo = 0;
			int decimalIndex = strDoubleValue.indexOf(".");
			if (decimalIndex!=-1) {
				decimalDigitsNo = strDoubleValue.length()-decimalIndex-1;
			}			
			if (decimalDigitsNo>maxDecimalDigits.intValue()) {
				ErrorData errorData = new ErrorData("common.err.interval.numeric.maxDecimalDigits", maxDecimalDigits);
				return errorData;
			}			
		}
		return null;
	}
	
	/**
	 * @return the maxDecimalDigits
	 */
	public Integer getMaxDecimalDigits() {
		return maxDecimalDigits;
	}

	/**
	 * @param maxDecimalDigits the maxDecimalDigits to set
	 */
	public void setMaxDecimalDigits(Integer maxDecimalDigits) {
		this.maxDecimalDigits = maxDecimalDigits;
	}

	/**
	 * @return the maxDouble
	 */
	public Double getMaxDouble() {
		return maxDouble;
	}
	/**
	 * @param maxDouble the maxDouble to set
	 */
	public void setMaxDouble(Double maxDouble) {
		this.maxDouble = maxDouble;
	}
	/**
	 * @return the minDouble
	 */
	public Double getMinDouble() {
		return minDouble;
	}
	/**
	 * @param minDouble the minDouble to set
	 */
	public void setMinDouble(Double minDouble) {
		this.minDouble = minDouble;
	}
}
