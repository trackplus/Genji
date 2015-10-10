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


package com.aurel.track.fieldType.fieldChange;

import com.aurel.track.fieldType.bulkSetters.BulkRelations;
import com.aurel.track.item.massOperation.MassOperationBL;

/**
 * The possible setters for activities
 * @author Tamas
 *
 */
public interface FieldChangeSetters {		
	//general relations
	public static final int SET_NULL = BulkRelations.SET_NULL;
	public static final int SET_TO = BulkRelations.SET_TO;
	
	//make a field required
	public static final int SET_REQUIRED = BulkRelations.SET_REQUIRED;
		
	//for numeric fields (integer and double)
	public static final int ADD_IF_SET = BulkRelations.ADD_IF_SET;
	public static final int ADD_OR_SET = BulkRelations.ADD_OR_SET;
	
	//for date fields	
	public static final int MOVE_BY_DAYS = BulkRelations.MOVE_BY_DAYS;
	public static final int SET_TO_DATE_FIELD_VALUE = BulkRelations.SET_TO_DATE_FIELD_VALUE;
	
	//for custom boolean fields
	public static final int SET_TRUE = BulkRelations.SET_TRUE;
	public static final int SET_FALSE = BulkRelations.SET_FALSE;
	
	//specific system boolean (integer based): access level (private/public issue)
	public static final int SET_PRIVATE = BulkRelations.SET_PRIVATE;
	public static final int SET_PUBLIC = BulkRelations.SET_PUBLIC;
	
	//archive level: archived/deleted/undeleted issue
	public static final int ARCHIVE = BulkRelations.ARCHIVE;
	public static final int DELETE = BulkRelations.DELETE;
	public static final int UNARCHIVE_UNDELETE = BulkRelations.UNARCHIVE_UNDELETE;
	
	//for reach text fields
	public static final int ADD_COMMENT = BulkRelations.ADD_COMMENT;		
	//for long text fields
	public static final int ADD_TEXT_TO_BEGIN = BulkRelations.ADD_TEXT_TO_BEGIN;
	public static final int ADD_TEXT_TO_END = BulkRelations.ADD_TEXT_TO_END;
	public static final int REMOVE_TEXT = BulkRelations.REMOVE_HTML_TEXT;	

	
	//for multiple select and consulted/informed fields
	public static final int ADD_ITEMS = BulkRelations.ADD_ITEMS;
	public static final int REMOVE_ITEMS = BulkRelations.REMOVE_ITEMS;
	public static final int ADD_ME = BulkRelations.ADD_ME;
	public static final int REMOVE_ME = BulkRelations.REMOVE_ME;
	
	public static final int PARAMETER = -100;

	//reuse the prefix names for relations from mass operation 
	public static String FIELD_CHANGE_RELATION_PREFIX = MassOperationBL.BULK_OPERATION_RELATION_PREFIX;
}
