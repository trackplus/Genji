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
 * The ftl names for rendering the bulk values
 * @author Tamas
 *
 */
public interface BulkValueTemplates {
	static final String BULK_SETTER_CLASS_PATH = "js.ext.com.track.bulkSetter.";
	public static final String TEXT_BULK_VALUE_TEMPLATE = BULK_SETTER_CLASS_PATH + "textBulkSetter";
	public static final String PARENT_BULK_VALUE_TEMPLATE = BULK_SETTER_CLASS_PATH + "parentBulkSetter";
	public static final String TEXTAREA_BULK_VALUE_TEMPLATE = BULK_SETTER_CLASS_PATH + "textAreaBulkSetter";
	public static final String RICH_TEXT_BULK_VALUE_TEMPLATE = BULK_SETTER_CLASS_PATH + "htmlEditorBulkSetter";
	public static final String NUMBER_BULK_VALUE_TEMPLATE = BULK_SETTER_CLASS_PATH + "numberBulkSetter";
	public static final String DATE_BULK_VALUE_TEMPLATE = BULK_SETTER_CLASS_PATH + "dateBulkSetter";
	public static final String SYSTEM_DATE_EARLIEST_LATEST_BULK_VALUE_TEMPLATE = BULK_SETTER_CLASS_PATH + "systemDateEarliestLatestBulkSetter";	
	public static final String SYSTEM_DATE_MOVE_BY_DAYS_BULK_VALUE_TEMPLATE = BULK_SETTER_CLASS_PATH + "systemDateMoveByDaysBulkSetter";
	
	public static final String SELECT_BULK_VALUE_TEMPLATE = BULK_SETTER_CLASS_PATH + "selectBulkSetter";
	public static final String COMPOSITE_SELECT_BULK_VALUE_TEMPLATE = BULK_SETTER_CLASS_PATH + "cascadingSelectBulkSetter";
	public static final String NONE_BULK_VALUE_TEMPLATE = "";
	public static final String MULTIPLE_SELECT_BULK_VALUE_TEMPLATE = BULK_SETTER_CLASS_PATH + "multipleSelectBulkSetter";
	public static final String PROJECT_DEPENDENT_SELECT_BULK_VALUE_TEMPLATE = BULK_SETTER_CLASS_PATH + "projectDependentSelectBulkSetter";
	public static final String WATCHER_BULK_VALUE_TEMPLATE = BULK_SETTER_CLASS_PATH + "watcherSelectBulkSetter";
	public static final String USER_PICKER_MULTIPLE_SELECT = BULK_SETTER_CLASS_PATH + "userPickerMultipleSelect";
	
	public static final String ISSUE_TYPE_BULK_VALUE_TEMPLATE = BULK_SETTER_CLASS_PATH + "issueTypeBulkSetter";
	public static final String RELEASE_BULK_VALUE_TEMPLATE = BULK_SETTER_CLASS_PATH + "releaseBulkSetter";

	public static final String PROJECT_BULK_VALUE_TEMPLATE = BULK_SETTER_CLASS_PATH + "projectBulkSetter";
	public static final String PROJECT_PICKER_BULK_VALUE_TEMPLATE = BULK_SETTER_CLASS_PATH + "projectPickerBulkSetter";
}
