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

package com.aurel.track.item.possibleOptions;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aurel.track.json.JSONUtility;

/**
 * prepare the JSON for possible filed values 
 * @author Tamas
 *
 */
public class PossibleFieldOptionsJSON {

	/**
	 * Gets the JSON string with the possible options for fields
	 * @param possibleOptionsMap
	 * @return
	 */
	public static String getPossibleOptionsJSON(Map<Integer, List<Integer>> possibleOptionsMap, Map<Integer, Set<Integer>> partialValidOptionsMap) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{");
		if (possibleOptionsMap!=null) {
			for (Iterator<Map.Entry<Integer, List<Integer>>> iterator = possibleOptionsMap.entrySet().iterator(); iterator.hasNext();) {
				Map.Entry<Integer, List<Integer>> entry = iterator.next();
				Integer fieldID = entry.getKey();
				List<Integer> optionIDs = entry.getValue();
				JSONUtility.appendIntegerListAsArray(stringBuilder, "field" + fieldID, optionIDs, !iterator.hasNext() );
			}
		} 
		if (partialValidOptionsMap!=null && partialValidOptionsMap.size()>0) {
			stringBuilder.append(",");
			for (Iterator<Map.Entry<Integer, Set<Integer>>> iterator = partialValidOptionsMap.entrySet().iterator(); iterator.hasNext();) {
				Map.Entry<Integer, Set<Integer>> entry = iterator.next();
				Integer fieldID = entry.getKey();
				Set<Integer> partialValidOptionIDs = entry.getValue();
				JSONUtility.appendIntegerSetAsArray(stringBuilder, "partialField" + fieldID, partialValidOptionIDs, !iterator.hasNext());
			}
		}
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
}
