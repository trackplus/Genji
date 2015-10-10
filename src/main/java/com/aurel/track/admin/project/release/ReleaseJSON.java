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

package com.aurel.track.admin.project.release;

import java.util.Iterator;
import java.util.List;

import com.aurel.track.json.JSONUtility;

public class ReleaseJSON {

	static interface JSON_FIELDS {
		String RELEASE_PREFIX = "releaseDetailTO.";
		String STATUS_ID = "statusID";
		String STATUS_LIST = "statusList";
		String STATUS_LABEL = "statusLabel";
		String STATUS_FLAG = "statusFlag";
		String DUE_DATE = "dueDate";
		String DEFAULT_NOTICED = "defaultNoticed";
		String DEFAULT_SCHEDULED = "defaultScheduled";
		
		String RELOAD_TREE = "reloadTree";
		String NODE_ID_TO_RELOAD = "nodeIDToReload";
		
		String LOCALIZED_MAIN = "localizedMain";
		String LOCALIZED_CHILD = "localizedChild";
		
		String IS_CHILD = "isChild";
		
		String SHOW_CLOSED_RELEASES = "showClosedReleases";
		
		String REPLACEMENT_TREE = "replacementTree";
		
	}
	
	/**
	 * Creates the JSON string with localized list names
	 * @param localizedMain
     * @param localizedChild
     * @param showClosedReleases
	 * @return
	 */
	static String createLocalizedLabelsJSON(String localizedMain, String localizedChild, boolean showClosedReleases) {							
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendStringValue(sb, JSON_FIELDS.LOCALIZED_MAIN, localizedMain);
		JSONUtility.appendStringValue(sb, JSON_FIELDS.LOCALIZED_CHILD, localizedChild);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.SHOW_CLOSED_RELEASES, showClosedReleases, true);
		
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Gets the JSON string for configuration children 
	 * @param children
	 * @return
	 */
	public static String getChildrenJSON(List<ReleaseTreeNodeTO> children){
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("[");
		for (Iterator<ReleaseTreeNodeTO> iterator = children.iterator(); iterator.hasNext();) {
			ReleaseTreeNodeTO releaseTreeNodeTO = iterator.next();
			stringBuilder.append("{");
			JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.IS_CHILD, releaseTreeNodeTO.isChild());
			JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ID, releaseTreeNodeTO.getId());
			JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.TEXT, releaseTreeNodeTO.getText());
			JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ICONCLS, releaseTreeNodeTO.getIconCls());
			JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.LEAF, releaseTreeNodeTO.isLeaf(), true);
			stringBuilder.append("}");
			if (iterator.hasNext()) {
				stringBuilder.append(",");
			}
		}
		stringBuilder.append("]");//end data
		return stringBuilder.toString();
	}
	
	/**
	 * Failure JSON by save
	 * @param errorCode
	 * @param title
	 * @param errorMessage
	 * @return
	 */
	public static String saveFailureJSON(Integer errorCode, String title, String errorMessage){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.ERROR_CODE, errorCode);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.TITLE, title);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ERROR_MESSAGE, errorMessage);
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, false, true);
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Failure JSON by save
	 * @param errorCode
	 * @param title
	 * @param warningMessage
	 * @return
	 */
	public static String saveReplacemenetJSON(Integer errorCode,  String warningMessage, String replacementReleasesTree){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.ERROR_CODE, errorCode);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ERROR_MESSAGE, warningMessage);
		JSONUtility.appendJSONValue(sb, JSON_FIELDS.REPLACEMENT_TREE, replacementReleasesTree);
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, false, true);
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Save result JSON
	 * @param nodeIDToReload
	 * @param nodeToSelect
	 * @return
	 */
	static String saveReleaseDetailJSON(String nodeIDToReload, String nodeToSelect, boolean reload) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendStringValue(sb, JSON_FIELDS.NODE_ID_TO_RELOAD, nodeIDToReload);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NODE, nodeToSelect);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.RELOAD_TREE, reload);
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true, true);
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Release details by loading a release
	 * @param releaseDetailTO	 
	 * @return
	 */
	static String loadReleaseDetailJSON(ReleaseDetailTO releaseDetailTO) {
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("{");
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.SUCCESS, true);
		stringBuilder.append(JSONUtility.JSON_FIELDS.DATA).append(":{");
		//projectBaseTO
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.RELEASE_PREFIX+JSONUtility.JSON_FIELDS.LABEL, releaseDetailTO.getLabel());
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.RELEASE_PREFIX+JSONUtility.JSON_FIELDS.DESCRIPTION, releaseDetailTO.getDescription());				
		JSONUtility.appendDateValue(stringBuilder, JSON_FIELDS.RELEASE_PREFIX+JSON_FIELDS.DUE_DATE, releaseDetailTO.getDueDate());
		JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.RELEASE_PREFIX+JSON_FIELDS.DEFAULT_NOTICED, releaseDetailTO.isDefaultNoticed());
		JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.RELEASE_PREFIX+JSON_FIELDS.DEFAULT_SCHEDULED, releaseDetailTO.isDefaultScheduled());
		JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.RELEASE_PREFIX+JSON_FIELDS.STATUS_ID, releaseDetailTO.getReleaseStatusID());
		JSONUtility.appendILabelBeanList(stringBuilder, JSON_FIELDS.STATUS_LIST, (List)releaseDetailTO.getReleaseStatusList(), true);
		stringBuilder.append("}");
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * Gets the JSON string for grid children 
	 * @param children
	 * @return
	 */
	static String getGridRowJSON(List<ReleaseRowTO> children) {
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		for (Iterator<ReleaseRowTO> iterator = children.iterator(); iterator.hasNext();) {
			ReleaseRowTO releaseRowTO = iterator.next();
			sb.append("{");
			JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.LABEL, releaseRowTO.getLabel());
			JSONUtility.appendStringValue(sb, JSON_FIELDS.STATUS_LABEL, releaseRowTO.getReleaseStatusLabel());
			JSONUtility.appendIntegerValue(sb, JSON_FIELDS.STATUS_FLAG, releaseRowTO.getStatusFlag());
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.DEFAULT_NOTICED, releaseRowTO.isDefaultNoticed());
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.DEFAULT_SCHEDULED, releaseRowTO.isDefaultScheduled());
			JSONUtility.appendDateValue(sb, JSON_FIELDS.DUE_DATE, releaseRowTO.getDueDate());
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.IS_CHILD, releaseRowTO.isChild());
			JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NODE, releaseRowTO.getNode(), true);
			sb.append("}");
			if (iterator.hasNext()) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	
	/**
	 * Creates the save JSON string for system list option detail 
	 * @param success
	 * @param node identify the node to be selected in the tree
	 * @param objectID identify the node to be selected in the grid 
	 * @return
	 */
	static String createSortOrderResultJSON(boolean success, String node, Integer objectID){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, success);
		JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.ID, objectID);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NODE, node, true);
		sb.append("}");
		return sb.toString();
	}
}
