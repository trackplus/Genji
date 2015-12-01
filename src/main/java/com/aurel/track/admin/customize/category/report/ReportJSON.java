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

package com.aurel.track.admin.customize.category.report;

import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.TreeNode;

import java.util.List;

/**
 * JSON utility for filters
 * @author Tamas Ruff
 *
 */
public class ReportJSON {
	
	static interface JSON_FIELDS {
		String MODIFIABLE = "modifiable";
	}

    /**
     * Gets the report edit detail JSON
     * @param label
     * @param modifiable
     * @return
     */
	static String getReportDetailJSON(String label, boolean modifiable) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.LABEL, label);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.MODIFIABLE, modifiable, true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
    /**
     * Gets the report tree and selected report JSON
     * @param categoryTree
     * @param reportID
     * @return
     */
    public static String chooseReportJSON(List<TreeNode> categoryTree, Integer reportID){
        StringBuilder sb=new StringBuilder();
        sb.append("{");
        JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
        JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA).append(":{");
        if (reportID!=null) {
            JSONUtility.appendStringValue(sb, "templateID", reportID.toString());
        }
        JSONUtility.appendJSONValue(sb, "categoryTree", JSONUtility.getTreeHierarchyJSON(categoryTree, false, true), true);
        sb.append("}");
        sb.append("}");
        return sb.toString();
    }
}
