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

package com.aurel.track.admin.user.filtersInMenu;

import java.util.Iterator;
import java.util.List;

import com.aurel.track.beans.TRoleBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.TreeNode;

public class FiltersInMenuJSON {
	
	interface JSON_FIELDS {		
		String FILTERID = "filterID";
		String FILTERLABEL = "filterLabel";
        String FILTER_TREE = "filterTree";
		String PERSONID = "personID";
		String PERSONNAME = "personName";
		String FIRST = "first";
	}
	
	
	
	public static String encodeFiltersForPersonsJSON(List<FilterForPersonTO> filterAssignments) {
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		for (Iterator<FilterForPersonTO> iterator = filterAssignments.iterator(); iterator.hasNext();) {
			FilterForPersonTO filterForPersonTO = iterator.next();
			sb.append("{");
			JSONUtility.appendIntegerValue(sb, JSON_FIELDS.FILTERID , filterForPersonTO.getFilterID());
			JSONUtility.appendStringValue(sb, JSON_FIELDS.FILTERLABEL, filterForPersonTO.getFilterLabel());
			JSONUtility.appendIntegerValue(sb, JSON_FIELDS.PERSONID, filterForPersonTO.getPersonID());
			JSONUtility.appendStringValue(sb, JSON_FIELDS.PERSONNAME, filterForPersonTO.getPersonName());
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.FIRST, filterForPersonTO.isFirst(), true);
			sb.append("}");
			if(iterator.hasNext()){
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * JSON details for a group
	 * @param filterTree
     * @return
	 */
	public static String encodeFilterTreeJSON(List<TreeNode> filterTree) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		sb.append(JSONUtility.JSON_FIELDS.DATA).append(":{");
        JSONUtility.appendJSONValue(sb, JSON_FIELDS.FILTER_TREE, JSONUtility.getTreeHierarchyJSON(filterTree, true, false));
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	
}
