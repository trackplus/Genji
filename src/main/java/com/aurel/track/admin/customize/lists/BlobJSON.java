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

package com.aurel.track.admin.customize.lists;

import com.aurel.track.json.JSONUtility;

/**
 * JSON utility for blob values
 * @author Tamas
 *
 */
public class BlobJSON {
	static interface JSON_FIELDS {
		static final String ICON_NAME = "iconName";
		static final String ICON_KEY = "iconKey";
	}
	
	/**
	 * Creates the file upload JSON string for an icon 
	 * @param success
	 * @param iconName
	 * @param iconPath 
	 * @return
	 */
	public static String createFileUploadJSON(boolean success, 
			String iconName, String iconPath){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, success);
		JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendStringValue(sb, JSON_FIELDS.ICON_NAME, iconName);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ICON, iconPath, true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Creates the file upload JSON string for an icon 
	 * @param success
	 * @param iconName
	 * @param iconPath 
	 * @return
	 */
	public static String createFileUploadJSON(boolean success, 
			String iconName, String iconPath, Integer iconKey){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, success);
		JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendStringValue(sb, JSON_FIELDS.ICON_NAME, iconName);
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.ICON_KEY, iconKey);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ICON, iconPath, true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
}
