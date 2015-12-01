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

import java.util.Date;
import java.util.Locale;

import com.aurel.track.admin.customize.lists.ListBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.fieldType.runtime.base.LookupContainer;

/**
 * Rendering the status filter in item navigator menu
 * @author Tamas
 *
 */
public class StatusFilterInMenu extends FilterInMenuBase {

	public StatusFilterInMenu(Integer queryID) {
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
		ILabelBean statusBean = LookupContainer.getStatusBean(queryID, locale);
		if (statusBean!=null){
			return statusBean.getLabel();
		}
		return null;
	}
	
	/**
	 * Gets the iconCls for the filter
	 * @param entityBean
	 * @return
	 */
	@Override
	public String getIconCls(Object entityBean) {
		return ListBL.ICONS_CLS.STATUS_ICON;
	}
	
	/**
	 * Gets the icon for the filter
	 */
	@Override
	public String getIcon() {
		TStateBean statusBean = LookupContainer.getStatusBean(queryID);
		if (statusBean!=null && statusBean.getIconKey()!=null) {
			return "optionIconStream.action?fieldID=-4&optionID="+queryID + "&time="+new Date().getTime();
		}
		return null;
	}
}
