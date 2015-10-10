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


package com.aurel.track.screen.dashboard.action;

import com.aurel.track.beans.TDashboardFieldBean;
import com.aurel.track.beans.screen.IField;
import com.aurel.track.plugin.DashboardDescriptor;
import com.aurel.track.screen.ScreenFactory;
import com.aurel.track.screen.action.AbstractFieldAction;
import com.aurel.track.screen.bl.design.AbstractFieldDesignBL;
import com.aurel.track.screen.bl.design.AbstractPanelDesignBL;
import com.aurel.track.screen.dashboard.adapterDAO.DashboardScreenFactory;
import com.aurel.track.screen.dashboard.bl.DashboardUtil;
import com.aurel.track.screen.dashboard.bl.design.DashboardFieldDesignBL;
import com.aurel.track.screen.dashboard.bl.design.DashboardPanelDesignBL;

/**
 * Used to treat the operations from screen designer
 * operations on field:reload,getproperty setproperty
 * @author Adrian Bojani
 *
 */
public class ScreenFieldAction extends AbstractFieldAction {

	private static final long serialVersionUID = 340L;

	@Override
	protected AbstractFieldDesignBL getAbstractFieldDesignBL() {
        return DashboardFieldDesignBL.getInstance();
    }

    @Override
	protected AbstractPanelDesignBL getAbstractPanelDesignBL() {
        return DashboardPanelDesignBL.getInstance();
    }

    @Override
	public ScreenFactory getScreenFactory() {
        return DashboardScreenFactory.getInstance();
    }

    @Override
	protected String getType(IField field) {
    	DashboardDescriptor dashboardDescriptor=DashboardUtil.getDescriptor((TDashboardFieldBean)field);
        return  dashboardDescriptor==null?null:dashboardDescriptor.getName();
    }
}
