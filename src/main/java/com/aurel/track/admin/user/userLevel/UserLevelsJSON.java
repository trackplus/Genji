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

package com.aurel.track.admin.user.userLevel;

import java.util.Iterator;
import java.util.List;

import com.aurel.track.admin.user.userLevel.UserLevelConfigBL.UserLevelActionTO;
import com.aurel.track.json.JSONUtility;

/**
 * Encode the user level config jsons
 * @author Tamas
 *
 */
public class UserLevelsJSON {
	
	static interface JSON_FIELDS {
		String PATH = "path";
		String SELECTED = "selected";
		String RELOAD_TREE = "reloadTree";
	}
	
	/**
	 * Person to role assignments
	 * @param fieldName
	 * @param userLevelActions
	 * @param departmentMap
	 * @param locale
	 * @return
	 */
	static String encodeUserLevelSettings(List<UserLevelActionTO> userLevelActions) {
		StringBuilder stringBuilder = new StringBuilder();
		if (userLevelActions!=null) {
			stringBuilder.append("[");
			for (Iterator<UserLevelActionTO> iterator = userLevelActions.iterator(); iterator.hasNext();) {
				UserLevelActionTO userLevelTO = iterator.next();
				stringBuilder.append("{");
				JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.PATH, userLevelTO.getPath());
				JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.NAME, userLevelTO.getLabel());
				JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.SELECTED, userLevelTO.isSelected());
				JSONUtility.appendIntegerValue(stringBuilder, JSONUtility.JSON_FIELDS.ID, userLevelTO.getId(), true);
				stringBuilder.append("}");
				if (iterator.hasNext()) {
					stringBuilder.append(",");
				}
			}
			stringBuilder.append("]");
		}
		return stringBuilder.toString();
	}
	
	/**
	 * JSON details for a user level
	 * @param name
	 * @return
	 */
	public static String getUserLevelEditDetailJSON(String name, String description) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		sb.append(JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.DESCRIPTION, description);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NAME, name, true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Gets a JSON success with node after save
	 * @param node 
	 * @return
	 */
	public static String getUserLevelSaveDetailJSON(String node, boolean reloadTree){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.RELOAD_TREE, reloadTree);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NODE, node, true);
		sb.append("}");
		return sb.toString();
	}
	
}
