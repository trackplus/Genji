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


package com.aurel.track.fieldType.runtime.matchers.design;


/**
 * Implement project picker matcher
 * @author Tamas
 *
 */
public class ProjectPickerMatcherDT extends TreePickerMatcherDT {	
	
	public ProjectPickerMatcherDT(Integer fieldID) {
		super(fieldID);	
	}
	
	/**
	 * Whether the selectable tree node field is used by rendering
	 * Should be used if there could be some nodes in tree which are not selectable 
	 * (for ex. projects in release tree)
	 * @return
	 */
	boolean getUseSelectable() {
		return false;
	}
	
	/**
	 * The name of the jsp fragment which contains 
	 * the control for rendering the matcher value
	 * @return
	 */
	/*@Override
	public String getMatchValueControlClass() {
		switch (relation) {
		case MatchRelations.EQUAL:
		case MatchRelations.NOT_EQUAL:
			return MatchValueJSPNames.MULTIPLE_TREE_PICKER;
		}
		return MatchValueJSPNames.NONE_MATCHER_CLASS;		
	}*/
	
	/**
	 * The json configuration object for configuring the js control containing the value
	 * @param fieldID
	 * @param baseName: the name of the control: important by submit
	 * @param index
	 * @param value: the value to be set by render
	 * @param disabled: whether the control is disabled
	 * @param dataSource: for combos, lists etc. the datasource is loaded in getMatcherDataSource()
	 * @param projectIDs: the selected projects for getting the datasource for project dependent pickers/trees (releases)
	 * @param matcherRelation: the value might depend on matcherRelation
	 * @param locale
	 * @param personID
	 * @return
	 */
	/*@Override
	public String getMatchValueJsonConfig(Integer fieldID, String baseName, Integer index, Object value,
			boolean disabled, Object dataSource, Integer[] projectIDs, Integer matcherRelation, Locale locale, Integer personID) {
		StringBuilder stringBuilder = new StringBuilder("{");
		JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.NAME, getName(baseName, index));
		if (value!=null) {
			try {
				Integer[] intArr = (Integer[])value;
				JSONUtility.appendIntegerArrayAsArray(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, intArr);
			} catch (Exception e) {				
			}
		}
		JSONUtility.appendJSONValue(stringBuilder, JSONUtility.JSON_FIELDS.DATA_SOURCE,
				JSONUtility.getTreeHierarchyJSON((List<TreeNode>)dataSource, true, false));
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.DISABLED, disabled, true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}*/
			
}
