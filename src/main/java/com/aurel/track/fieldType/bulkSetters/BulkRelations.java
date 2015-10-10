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


package com.aurel.track.fieldType.bulkSetters;
/**
 * The possible relations for bulk value
 * @author Tamas
 *
 */
public interface BulkRelations {		
	//general relations
	public static final int SET_NULL = 0;
	public static final int SET_TO = 1;
	
	//make a field required
	public static final int SET_REQUIRED = 5;
		
	//for numeric fields (integer and double)
	public static final int ADD_IF_SET = 10;
	public static final int ADD_OR_SET = 11;
	
	//for date fields	
	public static final int SET_EARLIEST_STARTING_FROM = 20;
	public static final int SET_LATEST_ENDING_AT = 21;	
	public static final int MOVE_BY_DAYS = 22;
	public static final int SET_TO_DATE_FIELD_VALUE = 23;
		
	//for custom boolean fields
	public static final int SET_TRUE = 30;
	public static final int SET_FALSE = 31;
	
	//specific system boolean (integer based): access level (private/public issue)
	public static final int SET_PRIVATE = 40;
	public static final int SET_PUBLIC = 41;
	
	//archive level: archived/deleted/undeleted issue
	public static final int ARCHIVE = 50;
	public static final int DELETE = 51;
	public static final int UNARCHIVE_UNDELETE = 52;
	
	//for reach text fields
	public static final int ADD_COMMENT = 60;		
	//for long text fields
	public static final int ADD_TEXT_TO_BEGIN = 61;
	public static final int ADD_TEXT_TO_END = 62;
	public static final int REMOVE_HTML_TEXT = 63;	

	
	//for multiple select and consulted/informed fields
	public static final int ADD_ITEMS = 70;
	public static final int REMOVE_ITEMS = 71;
	public static final int ADD_ME = 72;
	public static final int REMOVE_ME = 73;
	
	
	

}
