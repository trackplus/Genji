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

package com.aurel.track.admin.customize.objectStatus;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.IntegerStringBean;

public class ObjectStatusJSON {
	
	static interface JSON_FIELDS {
		//tree nodes and common		
		static final String LISTID = "listID";		
		static final String MODIFIABLE = "modifiable";
		static final String LIST_FOR_LABEL = "listForLabel";
		static final String TYPE_FLAG_LABEL = "typeflagLabel";
		//detail fields
		static final String TYPE_FLAG = "typeflag";
		static final String TYPE_FLAG_LIST = "typeflagsList";
	}
	
	
	/**
	 * Creates the JSON string with localized list names
	 * @param localizedLabels
	 * @return
	 */
	public static String createLocalizedLabelsJSON(Map<Integer, String> localizedLabels) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);	
		JSONUtility.appendIntegerStringMap(sb, JSONUtility.JSON_FIELDS.RECORDS, localizedLabels, true);
		sb.append("}");
		return sb.toString();
	}
		
	/**
	 * Creates the JSON string list for list options 
	 * @param gridRows
	 * @return
	 */
	static String createListOptionGridRowsJSON(List<ObjectStatusGridRowTO> gridRows){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		for (Iterator<ObjectStatusGridRowTO> iterator = gridRows.iterator(); iterator.hasNext();) {
			ObjectStatusGridRowTO objectStatusGridRowTO = iterator.next();
			sb.append("{");
			JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.ID, objectStatusGridRowTO.getId());
			JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.LABEL, objectStatusGridRowTO.getLabel());
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.MODIFIABLE, objectStatusGridRowTO.isModifiable());
			JSONUtility.appendIntegerValue(sb, JSON_FIELDS.LIST_FOR_LABEL, objectStatusGridRowTO.getListForLabel());
			JSONUtility.appendStringValue(sb, JSON_FIELDS.TYPE_FLAG_LABEL, objectStatusGridRowTO.getTypeflagLabel());
			JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NODE, objectStatusGridRowTO.getNode(), true);
			sb.append("}");
			if (iterator.hasNext()) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	
	/**
	 * Creates the JSON string for system list option detail
	 * @param label
	 * @param typeflag
	 * @param typeflagsList
	 * @return
	 */
	static String createObjectStatusDetailJSON(String label, Integer typeflag, 
			List<IntegerStringBean> typeflagsList){	
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.LABEL, label);
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.TYPE_FLAG, typeflag);
		JSONUtility.appendIntegerStringBeanList(sb, JSON_FIELDS.TYPE_FLAG_LIST, typeflagsList, true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	
	
	
	/**
	 * Creates the JSON string after a save/delete operation with error message 
	 * @param success
	 * @param node
	 * @param errorMessage 
	 * @return
	 */
	static String createNodeResultJSON(boolean success, String node, String errorMessage){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, success);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ERROR_MESSAGE, errorMessage);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NODE, node, true);
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Creates the save JSON string for system list option detail 
	 * @param success
	 * @param node identify the node to be selected in the tree
	 * @param objectID identify the node to be selected in the grid 
	 * @return
	 */
	static String createSaveResultJSON(boolean success, String node, Integer objectID){	
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, success);
		JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.ID, objectID);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NODE, node, true);
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Creates the save JSON string for system list option detail 
	 * @param success
	 * @param node identify the node to be selected in the tree
	 * @param objectID identify the node to be selected in the grid 
	 * @param lastCascadingChildDeleted whether the last cascading child list was deleted
	 * @return
	 */
	static String createDeleteResultJSON(boolean success, String node, Integer objectID){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, success);
		JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.ID, objectID);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NODE, node, true);
		sb.append("}");
		return sb.toString();
	}

}
