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


package com.aurel.track.fieldType.runtime.base;

/**
 * Base interface for all custom fields 
 * defining their runtime behavior
 * @author Tamas Ruff
 *
 */
public interface ICustomFieldTypeRT extends IFieldTypeRT {
	
	/**
	 * Whether it is a selection field
	 * @return
	 */
	public boolean isCustomSelect();
	
	/**
	 * Convert a value to string
	 * @param value
	 * @return
	 */
	public String convertToString(Object value);

	/**
	 * Convert a string  to object value
	 * @param value
	 * @return
	 */
	public Object convertFromString(String value);
	
}
