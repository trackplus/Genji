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


package com.aurel.track.fieldType.bulkSetters;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.TreeNode;
/**
 * Bulk setter for system select fields
 * Differs from the custom select by the fact that the value is Integer instead of Integer[]
 * @author Tamas
 *
 */
public class ReleaseBulkSetter extends ProjectDependentSystemSelectBulkSetter {
	
	public ReleaseBulkSetter(Integer fieldID) {
		super(fieldID);
	}

	/**
	 * Gets the name of the jsp fragment which contains 
	 * the control for rendering the bulk value
	 * @return
	 */
	@Override
	public String getSetterValueControlClass() {
		switch (relation) {
		case BulkRelations.SET_TO:
			return BulkValueTemplates.RELEASE_BULK_VALUE_TEMPLATE;
		}
		return BulkValueTemplates.NONE_BULK_VALUE_TEMPLATE;
	}
	
	/**
	 * The JSON configuration object for configuring the js control(s) containing the value
	 * @param baseName: the name of the control: important by submit
	 * @param value: the value to be set by rendering (first time or after a submit)
	 * @param dataSource: defined only for lists (list for global lists, map for context dependent lists)
	 * @param labelMap: defined only for context (project/issuType) dependent lists
	 * @param disabled: whether the control is disabled
     * @param personBean
	 * @param locale
	 * @return
	 */
	@Override
	public String getSetterValueJsonConfig(String baseName, Object value,
		Object dataSource, Map<Integer, String> labelMap, boolean disabled, TPersonBean personBean, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder("{");
		JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.NAME, getName(baseName));
		Map<Integer, Object> dataSourceMap = (Map<Integer, Object>) dataSource;
		Map<Integer, Integer> projectSpecificValueMap = (Map<Integer, Integer>)value;
		boolean allIssuesFromTheSameProject = dataSourceMap.keySet().size()==1;
		JSONUtility.appendFieldName(stringBuilder, "projectJson").append(":[");
		for (Iterator<Integer> iterator = dataSourceMap.keySet().iterator(); iterator.hasNext();) {
			Integer projectID = iterator.next();
			List<TreeNode> datasourceForProject = (List<TreeNode>)dataSourceMap.get(projectID);
			stringBuilder.append("{");
			if (projectSpecificValueMap!=null) {
				Integer projectSpecificValue = projectSpecificValueMap.get(projectID);
				if (projectSpecificValue!=null) {
					JSONUtility.appendStringValue(stringBuilder, "releaseID", projectSpecificValue.toString());	
				}
			}
			if (labelMap!=null && !allIssuesFromTheSameProject) {
				JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.LABEL, labelMap.get(projectID));
			}
            JSONUtility.appendJSONValue(stringBuilder, "releaseTree", JSONUtility.getTreeHierarchyJSON(datasourceForProject));
            JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ITEMID, getProjectSpecificItemId(baseName, fieldID, projectID));
            JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.NAME, getProjectSpecificName(baseName, fieldID, projectID), true);
			stringBuilder.append("}");
			if (iterator.hasNext()) {
				stringBuilder.append(",");
			}
		}
		stringBuilder.append("],");
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.DISABLED, disabled, true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
}
