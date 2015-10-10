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

/**
 * The ftl names for rendering the bulk values
 * @author Tamas
 *
 */
public interface FieldChangeTemplates {
	static final String SETTER_CLASS_PATH = "js.ext.com.track.setter.";
	public static final String TEXT_TEMPLATE = SETTER_CLASS_PATH + "textSetter";
	public static final String PARENT_TEMPLATE = SETTER_CLASS_PATH + "itemPickerSetter";
	public static final String TEXTAREA_TEMPLATE = SETTER_CLASS_PATH + "textAreaSetter";
	public static final String RICH_TEXT_TEMPLATE = SETTER_CLASS_PATH + "htmlEditorSetter";
	public static final String NUMBER_TEMPLATE = SETTER_CLASS_PATH + "numberSetter";
	public static final String DATE_TEMPLATE = SETTER_CLASS_PATH + "dateSetter";
	public static final String SELECT_TEMPLATE = SETTER_CLASS_PATH + "selectSetter";
	public static final String COMPOSITE_SELECT_TEMPLATE = SETTER_CLASS_PATH + "cascadingSelectSetter";
	public static final String NONE_TEMPLATE = "";
	public static final String MULTIPLE_LIST_TEMPLATE = SETTER_CLASS_PATH + "multipleListSetter";
	public static final String SINGLE_LIST_TEMPLATE = SETTER_CLASS_PATH + "simpleListSetter";
	public static final String SINGLE_TREE_TEMPLATE = SETTER_CLASS_PATH + "singleTreeSetter";
	public static final String MULTIPLE_TREE_TEMPLATE = SETTER_CLASS_PATH + "multipleTreeSetter";
	public static final String EMAIL_TEMPLATE = SETTER_CLASS_PATH + "emailSender";
}
