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

import com.aurel.track.screen.AbstractScreenJSON;
import com.aurel.track.screen.ScreenFactory;
import com.aurel.track.screen.action.AbstractTabAction;
import com.aurel.track.screen.bl.design.AbstractPanelDesignBL;
import com.aurel.track.screen.bl.design.AbstractTabDesignBL;
import com.aurel.track.screen.dashboard.adapterDAO.DashboardScreenFactory;
import com.aurel.track.screen.dashboard.bl.design.DashboardPanelDesignBL;
import com.aurel.track.screen.dashboard.bl.design.DashboardScreenDesignJSON;
import com.aurel.track.screen.dashboard.bl.design.DashboardTabDesignBL;

/**
 * Used to treat the operations from screen designer
 * operations on tab:reload,getproperty setproperty, addPanel,removePanel
 * @author Adrian Bojani
 *
 */
public class ScreenTabAction extends AbstractTabAction {

	private Integer projectID;
	private Integer entityType;
	
	private static final long serialVersionUID = 340L;

	@Override
	protected AbstractPanelDesignBL getAbstractPanelDesignBL() {
        return DashboardPanelDesignBL.getInstance();
    }

    @Override
	protected AbstractTabDesignBL getAbstractTabDesignBL() {
        return DashboardTabDesignBL.getInstance();
    }

    @Override
	public ScreenFactory getScreenFactory() {
        return DashboardScreenFactory.getInstance();
    }

	@Override
	public AbstractScreenJSON getScreenJSON() {
		return new DashboardScreenDesignJSON();
	}

	@Override
	public String localizeTab(String label){
    	if(getText("dashboard.tab.label."+label).equals("dashboard.tab.label."+label)){
    		return label;
    	}else{
    		return getText("dashboard.tab.label."+label);
    	}
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
