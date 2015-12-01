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


package com.aurel.track.util.numberFormatter;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DoubleWithDecimalDigitsNumberFormatUtil implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Map<Integer, DoubleWithDecimalDigitsNumberFormatUtil> instances =
			new HashMap<Integer, DoubleWithDecimalDigitsNumberFormatUtil>();
	
	private int roundFactor;
	private int maximumFractionDigits;
	private Map<Locale, NumberFormat> guiNumberFormatMap = new HashMap<Locale, NumberFormat>();
	
	
	private DoubleWithDecimalDigitsNumberFormatUtil(int numberOfDecimalDigits) {
		this.maximumFractionDigits = numberOfDecimalDigits;
		this.roundFactor = new Double(Math.pow(10, numberOfDecimalDigits)).intValue();
	}
		
	/**
	 * get a singleton instance
	 * @return
	 */
	public static DoubleWithDecimalDigitsNumberFormatUtil getInstance(int numberOfDecimalDigits) {
		if (instances.get(Integer.valueOf(numberOfDecimalDigits)) == null) {
			instances.put(Integer.valueOf(numberOfDecimalDigits), new DoubleWithDecimalDigitsNumberFormatUtil(numberOfDecimalDigits));
		}
		return (DoubleWithDecimalDigitsNumberFormatUtil)instances.get(Integer.valueOf(numberOfDecimalDigits));
	}
	
	
	public NumberFormat getNumberFormatInstance(Locale locale) {
		NumberFormat numberFormat = (NumberFormat)guiNumberFormatMap.get(locale);
		if (numberFormat==null) {
			numberFormat = NumberFormat.getInstance(locale);
			numberFormat.setMaximumFractionDigits(maximumFractionDigits);
			numberFormat.setMinimumFractionDigits(maximumFractionDigits);
			//grouping characters are not used because in some languages the 
			//grouping character is the space which causes problems after submit, by type converters 
			numberFormat.setGroupingUsed(false);
		}
		guiNumberFormatMap.put(locale, numberFormat);
		return numberFormat;
	}
	
	/**
	 * Formats a double value according to the given locale
	 * @param doubleValue
	 * @param locale
	 * @return
	 */
	public String formatGUI(Double doubleValue, Locale locale) {
		if (locale==null) {
			locale = Locale.ENGLISH;
		}
		if (doubleValue!=null) {
			NumberFormat numberFormat = getNumberFormatInstance(locale);
			return numberFormat.format(doubleValue);
		}
		return "";
	}
	
	/**
	 * Parses the string value taking into account the maximum number of decimal digits 
	 * @param stringValue
	 * @param locale
	 * @return
	 */
	public Double parseGUI(String stringValue, Locale locale) {
		if (locale==null) {
			locale = Locale.ENGLISH;
		}
		if (stringValue != null) {
			Number number = null;
			NumberFormat numberFormat = getNumberFormatInstance(locale);
			try {
				number = numberFormat.parse(stringValue);
			} catch (ParseException e) {
			}
			if (number!=null) {
				//it is not sure that it will be a Double instance. It could be a Long instance
				//therefore convert it to a primitive double 
				return new Double(new Double(Math.round(number.doubleValue()*roundFactor)).doubleValue()/roundFactor);
			}
		}
		return null;
	}

	
}
