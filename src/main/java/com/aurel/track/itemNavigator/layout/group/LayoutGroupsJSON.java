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

package com.aurel.track.itemNavigator.layout.group;

import java.util.Iterator;
import java.util.List;

import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.BooleanStringBean;
import com.aurel.track.util.IntegerStringBean;

/**
 * JSON for layout groups
 * @author Tamas
 *
 */
public class LayoutGroupsJSON {
	
	interface JSON_FIELDS {
		static String SELECTED_GROUPING_FIELDS = "selectedGroupFields";
		static String GROUPING_FIELD_LIST = "groupFieldList";
		static String ASCENDING_DESCENDING_LIST = "ascendingDescendingList";
		static String EXPAND_COLLAPSE_LIST = "expandCollapseList";
		static String FIELDID = "fieldID";
		static String DESCENDING = "descending";
		static String COLLAPSED = "collapsed";
	}
	
	/**
	 * Encode the current groupings in JSON
	 * @param groupFields
	 * @param groupFieldList
	 * @param ascendingDescendingList
	 * @param expandCollapseList
	 * @return
	 */
	static String encodeGrouping(List<GroupFieldTO> groupFields,
            List<IntegerStringBean> groupFieldList, List<BooleanStringBean> ascendingDescendingList,
            List<BooleanStringBean> expandCollapseList) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA);
		sb.append(":{");
		JSONUtility.appendJSONValue(sb, JSON_FIELDS.SELECTED_GROUPING_FIELDS, encodeGroupFieldList(groupFields));
		JSONUtility.appendIntegerStringBeanList(sb, JSON_FIELDS.GROUPING_FIELD_LIST, groupFieldList);
		JSONUtility.appendBooleanStringBeanList(sb, JSON_FIELDS.ASCENDING_DESCENDING_LIST, ascendingDescendingList);
		JSONUtility.appendBooleanStringBeanList(sb, JSON_FIELDS.EXPAND_COLLAPSE_LIST,expandCollapseList,true);
		sb.append("}");
		sb.append("}");
		return  sb.toString();
	}
	
	/**
	 * Encode the selected groupings
	 * @param groupFields
	 * @return
	 */
	private static String encodeGroupFieldList(List<GroupFieldTO> groupFields) {
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		for (Iterator<GroupFieldTO> iterator = groupFields.iterator(); iterator.hasNext();) {
			GroupFieldTO groupField = iterator.next();
			sb.append("{");
			//JSONUtility.appendBooleanValue(sb, "activated", groupField.isActivated());
			JSONUtility.appendIntegerValue(sb, JSON_FIELDS.FIELDID, groupField.getFieldID());
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.DESCENDING, groupField.isDescending());
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.COLLAPSED, groupField.isCollapsed(), true);
			sb.append("}");
			if (iterator.hasNext()) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	
}
