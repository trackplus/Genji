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

package com.aurel.track.itemNavigator.layout.column;

import java.util.Iterator;
import java.util.List;

import com.aurel.track.json.JSONUtility;

/**
 * JSON methods for layout change operations
 * @author Tamas
 *
 */
public class LayoutColumnsJSON {

	interface JSON_FIELDS {
		static String FIELDID = "fieldID";
		static String USED = "used";
	}
	
	/**
	 * Encodes all columns
	 * @param chooseColumns
	 * @return
	 */
	static String encodeAllColumns(List<ChooseColumnTO> chooseColumns) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA);
		sb.append(":[");
		for (Iterator<ChooseColumnTO> iterator = chooseColumns.iterator(); iterator.hasNext();) {
			ChooseColumnTO chooseColumnTO = iterator.next();
			sb.append("{");
			JSONUtility.appendIntegerValue(sb, JSON_FIELDS.FIELDID, chooseColumnTO.getFieldID());
			JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.LABEL, chooseColumnTO.getLabel());
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.USED, chooseColumnTO.isUsed(), true);
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
