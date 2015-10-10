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

import java.util.Locale;

import com.aurel.track.beans.TPersonBean;

/**
 * Base class for filter in menu implementations
 * @author Tamas
 *
 */
public abstract class FilterInMenuBase implements IFilterInMenu {
	protected Integer queryID;

	public FilterInMenuBase(Integer queryID) {
		super();
		this.queryID = queryID;
	}

	public Integer getQueryID() {
		return queryID;
	}

	public void setQueryID(Integer queryID) {
		this.queryID = queryID;
	}
	
	/**
	 * Gets the tooltip for the filter
	 * Returing null means the label will be taken as tooltip
	 * @param entityBean
	 * @param filterExpression
	 * @param locale
	 * @return
	 */
	public String getTooltip(Object entityBean, String filterExpression, Locale locale) {
		return null;
	}
	
	/**
	 * Gets the icon for the filter
	 */
	public String getIcon() {
		return null;
	}
	
	/**
	 * Whether the layout for a filter with view can be saved permanently by person
	 * It applies only to saved filters with view
	 * @param entityBean
	 * @param personBean
	 * @return
	 */
	public boolean maySaveFilterWithViewLayout(Object entityBean, TPersonBean personBean) {
		return false;
	}
}
