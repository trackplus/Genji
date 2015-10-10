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

package com.aurel.track.mobile;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.aurel.track.beans.TDashboardFieldBean;
import com.aurel.track.beans.TDashboardTabBean;
import com.aurel.track.beans.screen.IField;
import com.aurel.track.beans.screen.IPanel;
import com.aurel.track.beans.screen.ITab;
import com.aurel.track.screen.dashboard.adapterDAO.DashboardScreenFactory;

public class MobileBL {

	private static Integer CLIENT_IS_COMPATIBLE = new Integer(1);
	private static Integer CLIENT_NOT_COMPATIBLE = new Integer(0);
	private static Integer CLIENT_COMPATIBILE_DONT_KNOW = new Integer(2);

	/**
	 *
	 * @param tabIDs
	 *            Tabs from screen This method set all tabs field types
	 *            FieldType: a set which contains all cockpit ID's from a tab;
	 */
	public static void setFieldTypes(List<ITab> tabIDs) {
		for (ITab aTab : tabIDs) {
			TDashboardTabBean fullTab = (TDashboardTabBean) DashboardScreenFactory.getInstance().getTabDAO().loadFullByPrimaryKey(aTab.getObjectID());
			List<IPanel> panels = fullTab.getPanels();
			HashSet<String> fieldTypes = new HashSet<String>();
			aTab.setFieldTypes(fieldTypes);
			if (panels.size() > 0) {
				for (IPanel aPanel : panels) {
					List<IField> fields = aPanel.getFields();
					for (IField aField : fields) {
						TDashboardFieldBean tmp = (TDashboardFieldBean) aField;
						fieldTypes.add(tmp.getDashboardID());
					}
				}

			}
		}
	}

	public static boolean isMobileApp(Map<String, Object> session) {
		Object tmp = session.get("isMobileApplication");
		boolean isMobile;
		if (tmp == null) {
			isMobile = false;
		} else {
			isMobile = (Boolean) tmp;
		}
		return isMobile;
	}

	public static boolean isMobileApp(HttpSession session) {
		Object tmp = session.getAttribute("isMobileApplication");
		boolean isMobile;
		if (tmp == null) {
			isMobile = false;
		} else {
			isMobile = (Boolean) tmp;
		}
		return isMobile;
	}

	public static Integer checkClientCompatibility(Integer mobileApplicationVersionNo, boolean isMobileApplication) {
		if (mobileApplicationVersionNo != null) {
			if (isMobileApplication) {
				if (mobileApplicationVersionNo <= 100) {
					return CLIENT_IS_COMPATIBLE;
				} else {
					return CLIENT_COMPATIBILE_DONT_KNOW;
				}
			}
		}
		return CLIENT_COMPATIBILE_DONT_KNOW;
	}
}
