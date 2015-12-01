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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PercentNumberFormatUtil /*extends NumberFormatUtil*/ {
			
	protected Map<Locale, NumberFormat> guiNumberFormatMap = new HashMap<Locale, NumberFormat>();
	private static PercentNumberFormatUtil instance;
	
		
	private PercentNumberFormatUtil() {
		super();
	}

	/**
	 * get a singleton instance
	 * @return
	 */
	public static PercentNumberFormatUtil getInstance() {
		if (instance == null) {
			instance = new PercentNumberFormatUtil();
		}
		return instance;
	}
			
	protected NumberFormat getNumberFormatInstance(Locale locale) {		
		NumberFormat guiNumberFormat = NumberFormat.getPercentInstance(locale);
		guiNumberFormat.setMaximumFractionDigits(2);
		guiNumberFormat.setMinimumFractionDigits(2);
		//grouping characters are not used because in some languages the 
		//grouping character is the space which causes problems after submit, by type converters 
		guiNumberFormat.setGroupingUsed(false);
		return guiNumberFormat;
	}

	/**
	 * Formats a percent value according to the given locale
	 * @param percentValue
	 * @param locale
	 * @return
	 */
	public String formatGUI(Number percentValue, Locale locale) {
		if (locale==null) {
			locale = Locale.ENGLISH;
		}
		if (percentValue!=null) {		    
			NumberFormat numberFormat = (NumberFormat)guiNumberFormatMap.get(locale);
			if (numberFormat==null) {
				numberFormat = getNumberFormatInstance(locale);
				guiNumberFormatMap.put(locale, numberFormat);
			}
			return numberFormat.format(percentValue);			
		}
		return "";
	}		
}
