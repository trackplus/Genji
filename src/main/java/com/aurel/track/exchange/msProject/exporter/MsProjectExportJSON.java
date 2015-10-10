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

package com.aurel.track.exchange.msProject.exporter;

import java.util.Iterator;
import java.util.List;

import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.TreeNode;

public class MsProjectExportJSON {

	static interface JSON_FIELDS {
		//upload fields
        static final String PROJECT_RELEASE_TREE = "projectReleaseTree";
		static final String SELECTED_ID = "selectedProjectReleaseID";
		//static final String PROJECT_OR_RELEASEID = "projectOrReleaseID";
		static final String IMPORT_FILE_INFO = "importFileInfo";
	}
	/**
	 * Creates the JSON string for editing/adding/deriving a notification setting
	 * @param projectID
	 * @param releaseTree
	 * @param importFileInfo
	 * @return
	 */
	static String getLoadJSON(Integer projectID, String releaseTree, String importFileInfo) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		sb.append(JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendStringValue(sb, JSON_FIELDS.SELECTED_ID, projectID.toString()); //the intern value
		JSONUtility.appendJSONValue(sb, JSON_FIELDS.PROJECT_RELEASE_TREE, releaseTree);	//for picker the visible value is the textbox content
		JSONUtility.appendStringValue(sb, JSON_FIELDS.IMPORT_FILE_INFO, importFileInfo, true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}

    /**
     * Creates the JSON string for editing/adding/deriving a notification setting
     * @param importFileInfo
     * @return
     */
    static String getImportFileInfoJSON(String importFileInfo) {
        StringBuilder sb=new StringBuilder();
        sb.append("{");
        JSONUtility.appendStringValue(sb, JSON_FIELDS.IMPORT_FILE_INFO, importFileInfo, true);
        sb.append("}");
        return sb.toString();
    }


    public static String encodeProjectPickerData(List<TreeNode> projectTree) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("[");
    	if(projectTree != null){
			for (Iterator<TreeNode> iterator = projectTree.iterator(); iterator.hasNext();) {
				sb.append("{");
				TreeNode treeNode = iterator.next();
				Integer id = Integer.parseInt(treeNode.getId());
				id = id * (-1);
				JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ID, id.toString());
				JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.TEXT, treeNode.getLabel());
				JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ICONCLS, treeNode.getIcon());
				if (treeNode.getLeaf()) {
					JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.LEAF, treeNode.getLeaf(), true);
				} else {
					JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.LEAF, treeNode.getLeaf());
					List<TreeNode> children = treeNode.getChildren();
					if (children!=null && !children.isEmpty()) {
						JSONUtility.appendJSONValue(sb, JSONUtility.JSON_FIELDS.CHILDREN, encodeProjectPickerData(children), true);
					} else {
						JSONUtility.appendJSONValue(sb, JSONUtility.JSON_FIELDS.CHILDREN, "[]",true);
					}
				}
				sb.append("}");
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
    	}
    	sb.append("]");
		return sb.toString();
    }

}
