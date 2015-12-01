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

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class IntegerNumberFormatUtil /*extends NumberFormatUtil*/ {
			
	private static final long serialVersionUID = 1L;
	private static IntegerNumberFormatUtil instance;
	private static final NumberFormat isoIntegerFormat = NumberFormat.getIntegerInstance(Locale.ENGLISH);
	
	protected Map guiNumberFormatMap = new HashMap();		
	
	private IntegerNumberFormatUtil() {
		super();
	}

	
	/**
	 * Gets a singleton instance
	 * @return
	 */
	public static IntegerNumberFormatUtil getInstance() {
		if (instance == null) {
			instance = new IntegerNumberFormatUtil();
		}
		return instance;
	}
	
	protected NumberFormat getNumberFormatInstance(Locale locale) {
		NumberFormat guiNumberFormat = NumberFormat.getIntegerInstance(locale);
		//grouping characters are not used because in some languages the 
		//grouping character is the space which causes problems after submit, by type converters 
		guiNumberFormat.setGroupingUsed(false);
		return guiNumberFormat;
	}

	/**
	 * Formats an Integer value according to the given locale
	 * @param integerValue
	 * @param locale
	 * @return
	 */
	public String formatGUI(Integer integerValue, Locale locale) {
		if (locale==null) {
			locale = Locale.ENGLISH;
		}
		if (integerValue!=null) {		    
			NumberFormat numberFormat = (NumberFormat)guiNumberFormatMap.get(locale);
			if (numberFormat==null) {
				numberFormat = getNumberFormatInstance(locale);
				guiNumberFormatMap.put(locale, numberFormat);
			}
			return numberFormat.format(integerValue);			
		}
		return "";		
	}	
	
	
	/**
	 * Formats a double value according to the ISO format for double
	 * @param integerValue
	 * @return
	 */
	public static String formatISO(Integer integerValue) {
		if (integerValue != null) {
		    return isoIntegerFormat.format(integerValue);
		} else {
		    return "";
		}
	}
	
	/**
	 * Formats a double value according to the ISO format for double
	 * @param doubleValue
	 * @return
	 */
	public static Integer parseISO(String strValue) {
		if (strValue != null) {
		    try {
		    	Number number = isoIntegerFormat.parse(strValue);
		    	if (number!=null) {
		    		return new Integer(number.intValue());
		    	}
			} catch (ParseException e) {			
			}
		}
		return null;		
	}
}
