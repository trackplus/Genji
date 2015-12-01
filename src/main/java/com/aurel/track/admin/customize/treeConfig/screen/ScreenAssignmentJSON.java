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

package com.aurel.track.admin.customize.treeConfig.screen;

import java.util.Iterator;
import java.util.List;

import com.aurel.track.beans.TScreenBean;
import com.aurel.track.json.JSONUtility;

/**
 * JSON utility for screen assignments
 * @author Tamas
 *
 */
public class ScreenAssignmentJSON {

	static interface JSON_FIELDS {
		static final String INHERITED_CONFIG = "inheritedConfig";
		static final String SCREENS_LIST = "screensList";
		static final String SCREEN_NAME = "screenName";
		static final String SCREEN_SELECTED = "screenSelected";
		static final String ACTION_NAME = "actionName";
		//after save
		String REFRESH_TREE = "refreshTree";
	}
	
	private static void appendScreenList(StringBuilder sb, String name, List<TScreenBean> list){
		sb.append(name).append(":[");
		if(list!=null){
			for (Iterator<TScreenBean> iterator = list.iterator(); iterator.hasNext();) {
				TScreenBean screenBean = iterator.next();
				sb.append("{");
				JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.ID, screenBean.getObjectID());
				JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.LABEL, screenBean.getName());
				JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.DESCRIPTION, screenBean.getDescription(), true);
				sb.append("}");
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		sb.append(",");
	}
	
	/**
	 * Creates the JSON string for screen configuration
	 * @param node
     * @param screenBean
     * @param action
     * @param screensList
     * @param inheritedConfig
	 * @return
	 */
	static String getScreenDetailLoadJSON(String node, TScreenBean screenBean, String action,
			List<TScreenBean> screensList, boolean inheritedConfig){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NODE, node);
		JSONUtility.appendStringValue(sb, JSON_FIELDS.ACTION_NAME, action);
		JSONUtility.appendStringValue(sb, JSON_FIELDS.SCREEN_NAME, screenBean.getName());
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.DESCRIPTION, screenBean.getDescription());
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.SCREEN_SELECTED, screenBean.getObjectID());
		appendScreenList(sb, JSON_FIELDS.SCREENS_LIST, screensList);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.INHERITED_CONFIG, inheritedConfig, true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Creates the JSON string for screen configuration
	 * @param gridRows 
	 * @return
	 */
	static String getScreenDetailSaveJSON(String node){	
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		//always refresh the tree because the node text contains the assignment info as text 
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.REFRESH_TREE, true);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NODE, node, true);
		sb.append("}");
		return sb.toString();
	}
}
