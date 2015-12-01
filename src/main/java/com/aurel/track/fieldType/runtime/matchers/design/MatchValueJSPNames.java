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


package com.aurel.track.fieldType.runtime.matchers.design;

public interface MatchValueJSPNames {
	/*public static final String STRING_MATCHER_JSP = "stringMatcher.jsp";
	public static final String INTEGER_MATCHER_JSP = "integerMatcher.jsp";
	public static final String DOUBLE_MATCHER_JSP = "doubleMatcher.jsp";
	public static final String DATE_MATCHER_JSP = "dateMatcher.jsp";
	public static final String SELECT_MATCHER_JSP = "selectMatcher.jsp";
	public static final String COMPOSITE_SELECT_MATCHER_JSP = "cascadingSelectMatcher.jsp";
	public static final String NONE_MATCHER_JSP = "noneMatcher.jsp";*/
	
	static final String PICKER_CLASS_PATH =  "com.trackplus.util.";
	//public static final String PROJECT_PICKER = PICKER_CLASS_PATH + "ProjectPicker";
	//public static final String RELEASE_PICKER = PICKER_CLASS_PATH + "ReleasePicker";
	//public static final String PROJECT_PICKER = PICKER_CLASS_PATH + "SingleTreePicker";
	//public static final String RELEASE_PICKER = PICKER_CLASS_PATH + "SingleTreePicker";
	
	static final String MATCHER_CLASS_PATH =  "js.ext.com.track.matcher.";
	public static final String MULTIPLE_TREE_PICKER = MATCHER_CLASS_PATH + "multipleTreePickerMatcher";
	public static final String SELECT_MATCHER_CLASS = MATCHER_CLASS_PATH + "selectMatcher";
	public static final String STRING_MATCHER_CLASS = MATCHER_CLASS_PATH + "stringMatcher";
	public static final String NUMBER_MATCHER_CLASS = MATCHER_CLASS_PATH +  "numberMatcher";
	public static final String TIME_MATCHER_CLASS = MATCHER_CLASS_PATH +  "timeMatcher";
	//public static final String DOUBLE_MATCHER_CLASS = MATCHER_CLASS_PATH + "doubleMatcher";
	public static final String DATE_MATCHER_CLASS = MATCHER_CLASS_PATH + "dateMatcher";	
	public static final String COMPOSITE_SELECT_MATCHER_CLASS = MATCHER_CLASS_PATH + "cascadingSelectMatcher";
	public static final String ITEM_PICKER_MATCHER_CLASS = MATCHER_CLASS_PATH + "itemMatcher";
	public static final String NONE_MATCHER_CLASS = "";//MATCHER_CLASS_PATH + "noneMatcher";
}
