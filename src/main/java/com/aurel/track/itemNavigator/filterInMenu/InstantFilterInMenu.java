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

package com.aurel.track.itemNavigator.filterInMenu;

import java.util.Locale;

import com.aurel.track.admin.customize.category.filter.FilterBL;
import com.aurel.track.admin.customize.category.filter.TreeFilterFacade;
import com.aurel.track.resources.LocalizeUtil;

/**
 * Rendering the instant filter in item navigator menu
 * @author Tamas
 *
 */
public class InstantFilterInMenu extends FilterInMenuBase {

	
	public InstantFilterInMenu(Integer queryID) {
		super(queryID);
	}

	/**
	 * Gets the label for the filter
	 * @param entityBean
	 * @param filterExpression
	 * @param locale
	 * @return
	 */
	@Override
	public String getLabel(Object entityBean, String filterExpression, Locale locale) {
		return LocalizeUtil.getLocalizedTextFromApplicationResources("menu.findItems.instantFilter", locale);
	}
	
	/**
	 * Gets the tooltip for the filter 
	 * @param entityBean
	 * @param filterExpression
	 * @param locale
	 * @return
	 */
	@Override
	public String getTooltip(Object entityBean, String filterExpression, Locale locale) {
		return TreeFilterFacade.getInstance().getFilterExpressionString(filterExpression, locale);
	}
	
	/**
	 * Gets the iconCls for the filter
	 * @param entityBean
	 * @return
	 */
	@Override
	public String getIconCls(Object entityBean) {
		return FilterBL.ICONS.TREE_FILTER;
	}
}
