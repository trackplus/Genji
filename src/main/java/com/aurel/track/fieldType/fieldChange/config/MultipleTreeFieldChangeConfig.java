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


package com.aurel.track.fieldType.fieldChange.config;

import java.util.List;
import java.util.Locale;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.fieldChange.FieldChangeSetters;
import com.aurel.track.fieldType.fieldChange.FieldChangeTemplates;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.TreeNode;

/**
 * Configure field change for project picker
 * @author Tamas
 *
 */
public class MultipleTreeFieldChangeConfig extends MultipleListFieldChangeConfig {		
	public MultipleTreeFieldChangeConfig(Integer fieldID) {
		super(fieldID, false, true);	
	}

	
	/**
	 * Gets the name of the jsp fragment which contains 
	 * the control for rendering the bulk value
	 * @return
	 */
	@Override
	public String getValueRendererJsClass() {
		switch (setter) {
		case FieldChangeSetters.SET_TO:
		case FieldChangeSetters.ADD_ITEMS:
		case FieldChangeSetters.REMOVE_ITEMS:
			return FieldChangeTemplates.MULTIPLE_TREE_TEMPLATE;
		}
		return FieldChangeTemplates.NONE_TEMPLATE;
	}
	
	/**
	 * Adds the field type specific json content to stringBuilder (without curly brackets)
	 * @param stringBuilder
	 * @param value
	 * @param dataSource
	 * @param personBean
	 * @param locale
	 */
	public void addSpecificJsonContent(StringBuilder stringBuilder, Object value,
			Object dataSource, TPersonBean personBean, Locale locale) {
		Integer[] treeValues = (Integer[])value;
		if (treeValues!=null) {
			JSONUtility.appendIntegerArrayAsArray(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, treeValues);
		}
        JSONUtility.appendJSONValue(stringBuilder, JSONUtility.JSON_FIELDS.DATA, JSONUtility.getTreeHierarchyJSON((List<TreeNode>)dataSource, true, false));
        
		
	}
	
}
