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


package com.aurel.track.fieldType.constants;

/**
 * Defines the possible value types for a custom field
 * @author Tamas Ruff
 *
 */
public interface ValueType {
	public static final int NOSTORE = 0;
	public static final int TEXT = 1;
	public static final int INTEGER = 2;
	public static final int DOUBLE = 3;
	public static final int DATE = 4;
	//the difference between date and date time refers mainly to showValue
	public static final int DATETIME = 9;
	public static final int LONGTEXT = 5;
	public static final int BOOLEAN = 6;
	public static final int SYSTEMOPTION = 7;
	public static final int CUSTOMOPTION = 8;
	
	public static final int EXTERNALID = 10;
}
