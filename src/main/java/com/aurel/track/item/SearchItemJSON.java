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

package com.aurel.track.item;

import java.util.Iterator;
import java.util.Map;

import com.aurel.track.json.JSONUtility;

/**
 * Serializes the full text search results in Json
 * @author Tamas
 *
 */
public class SearchItemJSON {
	
	/**
	 * Encode the Json string 
	 * @param highlightedFragmentsMap
	 * @param highlightedTitlesMap
	 * @return
	 */
	static String encodeJson(Map<Integer, String> highlightedFragmentsMap, Map<Integer, String> highlightedTitlesMap) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		sb.append("data:");
		sb.append("[");
		for (Iterator<Map.Entry<Integer, String>> iterator = highlightedFragmentsMap.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<Integer, String> entry = iterator.next();
			Integer itemID = entry.getKey();
			String fragment = entry.getValue();
			String title = highlightedTitlesMap.get(itemID);
			sb.append("{");
			JSONUtility.appendStringValue(sb, "fragment", fragment);
			JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.TITLE, title);
			JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.ID, itemID, true);
			sb.append("}");
			if (iterator.hasNext()) {
				sb.append(",");
			}
		}
		sb.append("]");
		sb.append("}");
		return sb.toString();
	}
}
