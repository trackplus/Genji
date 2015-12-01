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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DoubleNumberFormatUtil {

	private static DoubleNumberFormatUtil instance;
	private static final NumberFormat isoDoubleFormat =NumberFormat.getInstance(Locale.ENGLISH);
	private static final NumberFormat isoDoubleFormatWithoutGroup =NumberFormat.getInstance(Locale.ENGLISH);
	static {
		isoDoubleFormatWithoutGroup.setGroupingUsed(false);
	}
	private static final String ISO_DOUBLE_FORMAT_STRING="#,##0.00;-#,##0.00";
	
	
	
	protected Map<Locale, NumberFormat> guiNumberFormatMap = new HashMap<Locale, NumberFormat>();	
	
	private DoubleNumberFormatUtil() {
		super();
	}

	/**
	 * Gets the singleton instance
	 * @return
	 */
	public static DoubleNumberFormatUtil getInstance() {
		if (instance == null) {
			instance = new DoubleNumberFormatUtil();
		}
		return instance;
	}
	
	protected NumberFormat getNumberFormatInstance(Locale locale) {
		NumberFormat guiNumberFormat = NumberFormat.getInstance(locale);
		//we want no limit, show all fraction digits 
		//(guiNumberFormat.getMaximumFractionDigits() is 3 
		//but we might need more): set hardcoded to 10 which should be enough
		guiNumberFormat.setMaximumFractionDigits(10);
		//grouping characters are not used because in some languages the 
		//grouping character is the space which causes problems after submit, by type converters 
		guiNumberFormat.setGroupingUsed(false);
		return guiNumberFormat;				
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
			NumberFormat numberFormat = (NumberFormat)guiNumberFormatMap.get(locale);
			if (numberFormat==null) {
				numberFormat = getNumberFormatInstance(locale);
				guiNumberFormatMap.put(locale, numberFormat);
			}
			return numberFormat.format(doubleValue);			
		}
		return "";	
	}
	
	public String getGUINumberattern(Locale locale) {
		NumberFormat numberFormat = (NumberFormat)guiNumberFormatMap.get(locale);;
		if (numberFormat==null) {
			numberFormat = getNumberFormatInstance(locale);			
		} 
		return ((DecimalFormat)numberFormat).toPattern();
	}
	
	/**
	 * Used for struts1 pages (because no automatic and locale dependent type conversion is made)
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
			NumberFormat numberFormat = (NumberFormat)guiNumberFormatMap.get(locale);
			if (numberFormat==null) {
				numberFormat = getNumberFormatInstance(locale);
				guiNumberFormatMap.put(locale, numberFormat);
			}
		    try {
				number = numberFormat.parse(stringValue);
			} catch (ParseException e) {
			}
			if (number!=null) {
				//it is not sure that it will be a Double instance. It could be a Long instance
				//therefore convert it to a primitive double 
				return new Double(number.doubleValue());
			}
		}
		return null;		
	}
	
	/**
	 * Formats a double value according to the ISO format for double
	 * @param doubleValue
	 * @return
	 */
	public static String formatISO(Double doubleValue) {
		if (doubleValue != null) {
		    return isoDoubleFormat.format(doubleValue);
		} else {
		    return "";
		}
	}

	/**
	 * Formats a double value according to the ISO format for double
	 * @param doubleValue
	 * @return
	 */
	public static String formatISOWithoutGroup(Double doubleValue) {
		if (doubleValue != null) {
		    return isoDoubleFormatWithoutGroup.format(doubleValue);
		} else {
		    return "";
		}
	}

	
	/**
	 * Formats a double value according to the ISO format for double
	 * @param doubleValue
	 * @return
	 */
	public static Double parseISO(String strValue) {
		if (strValue != null) {
		    try {
				Number number = isoDoubleFormat.parse(strValue);
				if (number!=null) {
					return new Double(number.doubleValue());
				}
			} catch (ParseException e) {
				
			}
		}
		return null;		
	}
	
	public static String getIsoFormatString() {
		return ISO_DOUBLE_FORMAT_STRING;
	}	
		
}
