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

package com.aurel.track.admin.customize.workflow.activity;

import java.util.Locale;


/**
 * Convert a field specific value from String (stored or submitted) and back
 */
public interface IValueConverter {

	/**
	 * Convert the string to object value after load
	 * @param value
	 * @param setter
	 * @return
	 */
	public Object getActualValueFromStoredString(String value, Integer setter);
	
	/**
	 * Gets the show value transformed form the stored configuration in database 
	 * @param value
	 * @param fieldID
	 * @param setter
	 * @param locale
	 * @return
	 */
	public String getDisplayValueFromStoredString(String value, Integer fieldID, Integer setter, Locale locale);
	
	/**
	 * Merge the configured and assigned value. Typically assigned takes precedence if exists (parameterized activity) otherwise the configured (no parameters)
	 * A real merge makes sense if some parts are configured (not parameterized) and other parts are parameterized (like e-mail activity parts)
	 * @param configValue the value set by workflow configuration
	 * @param assignedValue the value set by workflow assignment
	 * @param setter
	 * @return
	 */
	public Object getMergedValueFromStoredStrings(String configValue, String assignedValue, Integer setter);
}
