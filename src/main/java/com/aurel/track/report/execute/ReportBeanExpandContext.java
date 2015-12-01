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

package com.aurel.track.report.execute;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.itemNavigator.QueryContext;
import com.aurel.track.itemNavigator.layout.LayoutTO;
import com.aurel.track.itemNavigator.layout.group.GroupFieldTO;
import com.aurel.track.itemNavigator.layout.group.LayoutGroupsBL;
import com.aurel.track.itemNavigator.layout.group.SortFieldTO;

/**
 * Context for the actual expanding status in the ReportBeans
 * @author Tamas Ruff
 *
 */
public class ReportBeanExpandContext {
	public static String OTHER_ITEMS_SET = "otherItemsSet";
	public static String ALL_ITEMS_EXPANDED="allItemsExpanded";
	public static String GROUP_BY ="groupBy";
	public static String OTHER_GROUPS_SET = "otherGroupsSet";
	
	private Boolean allItemsExpanded; 
	private Set<Integer> otherItemsSet;
	private List<GroupFieldTO> groupBy;
	private Set<String> otherGroupsSet;
	private SortFieldTO sortFieldTO;
	
	public Boolean getAllItemsExpanded() {
		return allItemsExpanded;
	}
	public void setAllItemsExpanded(Boolean allItemsExpanded) {
		this.allItemsExpanded = allItemsExpanded;
	}
	public Set<Integer> getOtherItemsSet() {
		return otherItemsSet;
	}
	public void setOtherItemsSet(Set<Integer> otherItemsSet) {
		this.otherItemsSet = otherItemsSet;
	}
	public List<GroupFieldTO> getGroupBy() {
		return groupBy;
	}
	public void setGroupBy(List<GroupFieldTO> groupBy) {
		this.groupBy = groupBy;
	}
	public Set<String> getOtherGroupsSet() {
		return otherGroupsSet;
	}
	public void setOtherGroupsSet(Set<String> otherGroupsSet) {
		this.otherGroupsSet = otherGroupsSet;
	}

	public SortFieldTO getSortFieldTO() {
		return sortFieldTO;
	}
	public void setSortFieldTO(SortFieldTO sortFieldTO) {
		this.sortFieldTO = sortFieldTO;
	}
	
	public static ReportBeanExpandContext getExpandContext(Map session/*, TPersonBean personBean, QueryContext queryContext*/) {
		ReportBeanExpandContext reportBeanExpandContext = new ReportBeanExpandContext();
		reportBeanExpandContext.setAllItemsExpanded((Boolean)session.get(ReportExpandAction.ALL_ITEMS_EXPANDED));
		reportBeanExpandContext.setOtherItemsSet((Set)session.get(ReportExpandAction.OTHER_ITEMS_SET));
		reportBeanExpandContext.setOtherGroupsSet((Set)session.get(ReportExpandAction.OTHER_GROUPS_SET));
		return reportBeanExpandContext;
	}
	
	public ReportBeanExpandContext setGroupingSorting(ReportBeanExpandContext reportBeanExpandContext, LayoutTO layoutTO) {
		if (reportBeanExpandContext==null) {
			reportBeanExpandContext = new ReportBeanExpandContext();
		}
		if (layoutTO!=null) {
			reportBeanExpandContext.setSortFieldTO(layoutTO.getSortField());
			reportBeanExpandContext.setGroupBy(layoutTO.getGroupFields());
		}
		return reportBeanExpandContext;
	}
}
