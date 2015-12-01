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

package com.aurel.track.screen.dashboard.assign;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.aurel.track.beans.TDashboardScreenBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizationKeyPrefixes;
import com.aurel.track.resources.LocalizeUtil;

/**
 *
 */
public class DashboardAssignJSON {
	public static String encodeScreenList(List<TDashboardScreenBean> list,
			Locale locale) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		if (list != null) {

			for (Iterator<TDashboardScreenBean> iterator = list.iterator(); iterator.hasNext();) {
				TDashboardScreenBean screenBean = iterator.next();
				sb.append("{");
				JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.ID, screenBean.getObjectID());
				String name = LocalizeUtil.getLocalizedEntity(
						LocalizationKeyPrefixes.COCKPIT_TEMPLATE_NAME_PREFIX,
						screenBean.getObjectID(), locale);
				String description = LocalizeUtil.getLocalizedEntity(
						LocalizationKeyPrefixes.COCKPIT_TEMPLATE_DESCRIPTION_PREFIX,
								screenBean.getObjectID(), locale);
				if (name == null || name.equals("")) {
					name = screenBean.getName();
				}
				if (description == null || description.equals("")) {
					description = screenBean.getDescription();
				}
				Integer ownerID = screenBean.getOwner();
				if (ownerID!=null) {
					TPersonBean personBean = LookupContainer.getPersonBean(ownerID);
					if (personBean!=null) {
						JSONUtility.appendStringValue(sb, "owner", personBean.getFullName());
					}
				}
				JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NAME, name);
				JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.DESCRIPTION, description, true);
				sb.append("}");
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}

	public static String encodeJSONScreenTO(
			TDashboardScreenBean dashboardScreenBean) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		JSONUtility
				.appendStringValue(sb, "name", dashboardScreenBean.getName());
		JSONUtility.appendStringValue(sb, "description",
				dashboardScreenBean.getDescription());
		JSONUtility.appendIntegerValue(sb, "id",
				dashboardScreenBean.getObjectID(), true);
		sb.append("}");
		return sb.toString();
	}
}
