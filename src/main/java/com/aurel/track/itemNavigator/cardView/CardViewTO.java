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

package com.aurel.track.itemNavigator.cardView;

import com.aurel.track.beans.TCardPanelBean;

import java.util.List;

/**
 */
public class CardViewTO {
	private GroupByTO groupByTO;
	private SortByTO sortByTO;
	private List<GroupTO> groups;
	private Integer[] selectedGroups;
	private TCardPanelBean cardPanelBean;
	private Integer maxSelectionCount;
	private Integer cardGroupingFieldID;

	public CardViewTO(){
	}

	public GroupByTO getGroupByTO() {
		return groupByTO;
	}

	public void setGroupByTO(GroupByTO groupByTO) {
		this.groupByTO = groupByTO;
	}

	public SortByTO getSortByTO() {
		return sortByTO;
	}

	public void setSortByTO(SortByTO sortByTO) {
		this.sortByTO = sortByTO;
	}

	public List<GroupTO> getGroups() {
		return groups;
	}

	public void setGroups(List<GroupTO> groups) {
		this.groups = groups;
	}

	public Integer[] getSelectedGroups() {
		return selectedGroups;
	}

	public void setSelectedGroups(Integer[] selectedGroups) {
		this.selectedGroups = selectedGroups;
	}

	public TCardPanelBean getCardPanelBean() {
		return cardPanelBean;
	}

	public void setCardPanelBean(TCardPanelBean cardPanelBean) {
		this.cardPanelBean = cardPanelBean;
	}

	public Integer getCardGroupingFieldID() {
		return cardGroupingFieldID;
	}

	public void setCardGroupingFieldID(Integer cardGroupingFieldID) {
		this.cardGroupingFieldID = cardGroupingFieldID;
	}
	public Integer getMaxSelectionCount() {
		return maxSelectionCount;
	}

	public void setMaxSelectionCount(Integer maxSelectionCount) {
		this.maxSelectionCount = maxSelectionCount;
	}

}
