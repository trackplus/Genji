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

package com.aurel.track.itemNavigator.layout.sort;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TNavigatorGroupingSortingBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.itemNavigator.layout.NavigatorLayoutBL;
import com.aurel.track.itemNavigator.layout.group.LayoutGroupsBL;

/**
 * Saving the sorting for person and or filter
 * @author Tamas
 *
 */
public class LayoutSortBL {
	static private Logger LOGGER = LogManager.getLogger(LayoutSortBL.class);
	
	/**
	 * Saves the sorting
	 * @param personID
	 * @param filterType
	 * @param filterID
	 * @param sortField
	 * @param descending
	 */
	static void saveSorting(TPersonBean personBean, Integer filterType, Integer filterID, Integer sortField, boolean descending) {
		Integer layoutID = NavigatorLayoutBL.getLayoutID(personBean, filterType, filterID, true);
		LOGGER.debug("Save grouping for person " + personBean.getLabel() + " filterType " + filterType + " filterID " + filterID);
		List<TNavigatorGroupingSortingBean> sortingFieldBeans = LayoutGroupsBL.loadSortingByLayout(layoutID);
		if (sortingFieldBeans==null || sortingFieldBeans.isEmpty()) {
			TNavigatorGroupingSortingBean navigatorGroupingSortingBean = new TNavigatorGroupingSortingBean();
			navigatorGroupingSortingBean.setNavigatorLayout(layoutID);
			navigatorGroupingSortingBean.setField(sortField);
			navigatorGroupingSortingBean.setDescending(descending);
			navigatorGroupingSortingBean.setGrouping(false);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Add sort field " + sortField + " descending " + descending);
			}
			LayoutGroupsBL.save(navigatorGroupingSortingBean);
		} else {
			if (!sortingFieldBeans.isEmpty()) {
				if (sortingFieldBeans.size()>1) {
					//should never happen
					boolean first = true;
					for (TNavigatorGroupingSortingBean navigatorGroupingSortingBean : sortingFieldBeans) {
						if (!first) {
							LayoutGroupsBL.delete(navigatorGroupingSortingBean.getObjectID());
						}
						first = false;
					}
				}
				TNavigatorGroupingSortingBean navigatorGroupingSortingBean = sortingFieldBeans.get(0);
				navigatorGroupingSortingBean.setField(sortField);
				navigatorGroupingSortingBean.setDescending(descending);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Change sort field " + sortField + " descending " + descending);
				}
				LayoutGroupsBL.save(navigatorGroupingSortingBean);
			}
		}
	}
}
