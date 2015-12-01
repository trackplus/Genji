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

package com.aurel.track.screen.dashboard.action;

import com.aurel.track.beans.screen.IPanel;
import com.aurel.track.screen.ScreenFactory;
import com.aurel.track.screen.action.AbstractPanelAction;
import com.aurel.track.screen.bl.design.AbstractFieldDesignBL;
import com.aurel.track.screen.bl.design.AbstractPanelDesignBL;
import com.aurel.track.screen.dashboard.adapterDAO.DashboardScreenFactory;
import com.aurel.track.screen.dashboard.bl.design.DashboardFieldDesignBL;
import com.aurel.track.screen.dashboard.bl.design.DashboardPanelDesignBL;
import com.aurel.track.screen.dashboard.bl.design.DashboardScreenDesignJSON;

/**
 * Used to treat the operations from screen designer
 * operations on pan:reload,getproperties, setproperty,delete field, add field
 * @author Adrian Bojani
 *
 */
public class ScreenPanelAction extends AbstractPanelAction {

	private static final long serialVersionUID = 340L;
	
	private Integer projectID;
	private Integer entityType;
	
	@Override
	protected AbstractPanelDesignBL getAbstractPanelDesignBL() {
        return DashboardPanelDesignBL.getInstance();
    }

    @Override
	protected AbstractFieldDesignBL getAbstractFieldDesignBL() {
        return DashboardFieldDesignBL.getInstance();
    }

    @Override
	public ScreenFactory getScreenFactory() {
        return DashboardScreenFactory.getInstance();
    }

	@Override
	protected String encodePanel(IPanel panel) {
		return new DashboardScreenDesignJSON().encodePanel(panel);
	}

	@Override
	public String reload() {
        String sup=super.reload();
        return sup;
    }

	@Override
	public String updateProperty() {
		return super.updateProperty();	//To change body of overridden methods use File | Settings | File Templates.
	}

	public Integer getProjectID() {
		return projectID;
	}

	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	public Integer getEntityType() {
		return entityType;
	}

	public void setEntityType(Integer entityType) {
		this.entityType = entityType;
	}
}
