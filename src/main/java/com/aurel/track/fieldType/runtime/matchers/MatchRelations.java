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


package com.aurel.track.fieldType.runtime.matchers;

public interface MatchRelations {

	public static final int NO_MATCHER = -1;
	
	public static final int EQUAL = 0;
	public static final int NOT_EQUAL = 1;
	public static final int EQUAL_IGNORE_CASE = 2;	
	public static final int IS_NULL = 3;
	public static final int NOT_IS_NULL = 4;
	//for composite selects
	public static final int PARTIAL_MATCH = 5;
	public static final int PARTIAL_NOTMATCH = 6;
	
	//for selects
	public static final int IN = 10;
	public static final int NOT_IN = 11;
	
	//for strings
	public static final int PERL_PATTERN = 20;
	public static final int LIKE = 21;
	public static final int NOT_LIKE = 22;
	
	//for numeric fields
	public static final int GREATHER_THAN = 30;
	public static final int GREATHER_THAN_EQUAL = 31;
	public static final int LESS_THAN = 32;
	public static final int LESS_THAN_EQUAL = 33;
	
	//for date fields
	public static final int EQUAL_DATE = 40;
	public static final int NOT_EQUAL_DATE = 41;
	public static final int GREATHER_THAN_DATE = 42;
	public static final int GREATHER_THAN_EQUAL_DATE = 43;
	public static final int LESS_THAN_DATE = 44;
	public static final int LESS_THAN_EQUAL_DATE = 45;
	
	public static final int MORE_THAN_DAYS_AGO = 50;
	public static final int MORE_THAN_EQUAL_DAYS_AGO = 51;
	public static final int LESS_THAN_DAYS_AGO = 52;
	public static final int LESS_THAN_EQUAL_DAYS_AGO = 53;	
	public static final int IN_MORE_THAN_DAYS = 54;
	public static final int IN_MORE_THAN_EQUAL_DAYS = 55;
	public static final int IN_LESS_THAN_DAYS = 56;
	public static final int IN_LESS_THAN_EQUAL_DAYS = 57;
	
	public static final int LATER_AS_LASTLOGIN = 60;
	//TODO implement this values
	public static final int BEGINNING_OF_THIS_WEEK = 61;
	public static final int BEGINNING_OF_THIS_MONTH = 62;
	public static final int BEGINNING_OF_THIS_YEAR = 63;
	public static final int BEGINNING_OF_THIS_QUARTER = 64;
	
	public static final int THIS_WEEK = 65;
	public static final int LAST_WEEK = 66;
	public static final int THIS_MONTH = 67;
	public static final int LAST_MONTH = 68;
	
	//for boolean fields
	public static final int SET = 70;
	public static final int RESET = 71;
}
