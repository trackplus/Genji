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


package com.aurel.track.util.numberFormatter;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class NumberFormatUtil implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	//protected NumberFormat guiNumberFormat;
	//Locale to NumberFormat Map 
	protected Map guiNumberFormatMap = new HashMap();
	//protected Locale locale;
		
	/*public NumberFormatUtil() {
		setLocale(Locale.getDefault());
	}*/

	/*public NumberFormatUtil(Locale locale) {
		setLocale(locale);		
	}*/

	/**
	 * @return Returns the locale.
	 */
	/*public Locale getLocale() {
		return locale;
	}*/


	/**
	 * @param locale The locale to set.
	 */
	/*public void setLocale(Locale locale) {		
		if (locale==null) {
			locale = Locale.ENGLISH;
		}	
		if (guiNumberFormatMap.get(locale)==null) { 
			//if (this.locale!=null && this.locale.equals(locale)) {
				//avoid fetching the Instances for efficiency reasons
				//return;
			//}
			//this.locale = locale;
			NumberFormat guiNumberFormat = getNumberFormatInstance(locale);				
			//grouping characters are not used because in some languages the 
			//grouping character is the space which causes problems after submit, by type converters 
			guiNumberFormat.setGroupingUsed(false);
			guiNumberFormatMap.put(locale, getNumberFormatInstance(locale));
		}
	}*/

	
	
	protected abstract NumberFormat getNumberFormatInstance(Locale locale);
		
}
