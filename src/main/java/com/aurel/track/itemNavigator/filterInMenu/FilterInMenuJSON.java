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

package com.aurel.track.itemNavigator.filterInMenu;

import java.util.Iterator;
import java.util.List;

import com.aurel.track.json.JSONUtility;

/**
 * Encode the filters in menu into JSON
 * @author Tamas
 *
 */
public class FilterInMenuJSON {

	/**
	 * Encode the list of filters which should appear in the item navigator menu
	 * @param filterInMenuTOList
	 * @return
	 */
	public static String encodeFiltersInMenu(List<FilterInMenuTO> filterInMenuTOList){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(filterInMenuTOList!=null){
			for (Iterator<FilterInMenuTO> iterator = filterInMenuTOList.iterator(); iterator.hasNext();) {
				FilterInMenuTO filterInMenuTO = iterator.next();
				sb.append(encodeFilterInMenu(filterInMenuTO));
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Encode a filter menu entry
	 * @param filterInMenuTO
	 * @return
	 */
	private static String encodeFilterInMenu(FilterInMenuTO filterInMenuTO){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		if(filterInMenuTO!=null){
			JSONUtility.appendIntegerValue(sb,"type", filterInMenuTO.getType());
			JSONUtility.appendStringValue(sb,"label",filterInMenuTO.getLabel());
			JSONUtility.appendStringValue(sb,"tooltip",filterInMenuTO.getTooltip());
			JSONUtility.appendStringValue(sb, "icon", filterInMenuTO.getIcon());
			JSONUtility.appendStringValue(sb, "iconCls", filterInMenuTO.getIconCls());
			JSONUtility.appendStringValue(sb, "viewID", filterInMenuTO.getViewID());
			JSONUtility.appendIntegerValue(sb,"objectID",filterInMenuTO.getObjectID());
			JSONUtility.appendIntegerValue(sb,"queryContextID",filterInMenuTO.getLastExecutedQueryID());
			JSONUtility.appendBooleanValue(sb, "maySaveFilterLayout", filterInMenuTO.isMaySaveFilterLayout(), true);		
			
		}
		sb.append("}");
		return sb.toString();
	}

}
