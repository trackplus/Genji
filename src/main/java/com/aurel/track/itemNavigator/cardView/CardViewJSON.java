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

package com.aurel.track.itemNavigator.cardView;

import com.aurel.track.beans.TCardPanelBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.screen.card.bl.runtime.CardScreenRuntimeJSON;

import java.util.Iterator;
import java.util.List;


/**
 */
public class CardViewJSON {

	public static final String OPTIONS = "options";
	public static final String LABEL = "label";
	public static final String ID = "id";
	public static final String MAX_SELECTION_COUNT = "maxSelectionCount";
	public static final String SORT_FIELD = "sortField";
	public static final String SORT_ORDER = "sortOrder";
	public static final String WIDTH = "width";
	public static final String PANEL = "panel";
	public static final String GROUP_BY = "groupBy";
	public static final String SORT_BY = "sortBy";
	public static final String GROUPS = "groups";
	public static final String CARD_GROUPING_FIELD_ID = "cardGroupingFieldID";
	public static final String SELECTED_GROUPS = "selectedGroups";

	private CardViewJSON(){
	}

	public static String encodeCardView(CardViewTO cardViewTO){
		StringBuilder sb = new StringBuilder();
		sb.append("{");

		TCardPanelBean cardPanelBean=cardViewTO.getCardPanelBean();
		CardScreenRuntimeJSON cardScreenRuntimeJSON=new CardScreenRuntimeJSON();
		String panelJSON=cardScreenRuntimeJSON.encodePanelAsMatrixField(cardPanelBean);

		JSONUtility.appendJSONValue(sb, PANEL, panelJSON);
		JSONUtility.appendJSONValue(sb, GROUP_BY, encodeGroupBy(cardViewTO.getGroupByTO()));
		JSONUtility.appendIntegerValue(sb, MAX_SELECTION_COUNT, cardViewTO.getMaxSelectionCount());
		JSONUtility.appendJSONValue(sb, SORT_BY, encodeSortBy(cardViewTO.getSortByTO()));
		JSONUtility.appendJSONValue(sb, GROUPS, encodeGroups(cardViewTO.getGroups()));
		JSONUtility.appendIntegerArrayAsArray(sb, SELECTED_GROUPS, cardViewTO.getSelectedGroups());
		JSONUtility.appendIntegerValue(sb, CARD_GROUPING_FIELD_ID, cardViewTO.getCardGroupingFieldID(), true);

		sb.append("}");
		return sb.toString();
	}

	public static String encodeGroupBy(GroupByTO groupByTO){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendIntegerStringBeanList(sb, OPTIONS, groupByTO.getOptions());
		JSONUtility.appendStringValue(sb, LABEL, groupByTO.getLabel());
		JSONUtility.appendIntegerValue(sb, ID, groupByTO.getId(), true);
		sb.append("}");
		return sb.toString();
	}

	public static String encodeSortBy(SortByTO sortByTO){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendIntegerStringBooleanBeanList(sb, OPTIONS,sortByTO.getOptions());
		JSONUtility.appendIntegerValue(sb, SORT_FIELD, sortByTO.getSortField());
		JSONUtility.appendBooleanValue(sb, SORT_ORDER, sortByTO.isSortOrder(),true);
		sb.append("}");
		return sb.toString();
	}
	public static String encodeGroups(List<GroupTO> groups){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(groups!=null){
			for (Iterator<GroupTO> iterator = groups.iterator(); iterator.hasNext();) {
				GroupTO groupTO = iterator.next();
				sb.append("{");
				JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.LABEL, groupTO.getLabel());
				JSONUtility.appendIntegerValue(sb, WIDTH, groupTO.getWidth());
				JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.ID, groupTO.getObjectID(),true);
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
