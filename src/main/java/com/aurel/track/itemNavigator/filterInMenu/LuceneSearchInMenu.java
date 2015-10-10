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

import com.aurel.track.admin.customize.category.filter.FilterBL;

/**
 * Rendering the full text search filter in item navigator menu
 * @author Tamas
 *
 */
public class LuceneSearchInMenu extends FilterInMenuBase {
	
	
	public LuceneSearchInMenu(Integer queryID) {
		super(queryID);
	}

	/**
	 * Gets the label for the filter
	 * @param entityBean
	 * @param filterExpression
	 * @param locale
	 * @return
	 */
	public String getLabel(Object entityBean, String filterExpression, Locale locale) {
		return filterExpression;
	}
	
	/**
	 * Gets the iconCls for the filter
	 * @param entityBean
	 * @return
	 */
	public String getIconCls(Object entityBean) {
		return FilterBL.ICONS.TQL_PLUS_FILTER;
	}
}
