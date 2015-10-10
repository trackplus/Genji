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

package com.aurel.track.screen.dashboard.bl.runtime;

import com.aurel.track.beans.TDashboardFieldBean;
import com.aurel.track.beans.screen.IField;
import com.aurel.track.screen.ScreenFactory;
import com.aurel.track.screen.bl.AbstractFieldBL;
import com.aurel.track.screen.dashboard.adapterDAO.DashboardScreenFactory;

/**
 */
public class DashboardFieldRuntimeBL extends AbstractFieldBL {
    private static DashboardFieldRuntimeBL instance;

	/**
	 * get a singleton instance
	 * @return
	 */
	public static DashboardFieldRuntimeBL getInstance() {
		if (instance == null) {
			instance = new DashboardFieldRuntimeBL();
		}
		return instance;
	}
    public IField createField(Integer panelPk,Integer fieldDefPK) {
        return null;
    }
    @Override
	protected ScreenFactory getScreenFactory() {
        return DashboardScreenFactory.getInstance();
    }
    public void saveDashBoardField(TDashboardFieldBean dashboardField){
        screenFieldDAO.save(dashboardField);
    }
}
