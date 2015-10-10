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

package com.aurel.track.screen.dashboard.adapterDAO;

import com.aurel.track.beans.TDashboardFieldBean;
import com.aurel.track.beans.TDashboardPanelBean;
import com.aurel.track.beans.TDashboardScreenBean;
import com.aurel.track.beans.TDashboardTabBean;
import com.aurel.track.beans.screen.IField;
import com.aurel.track.beans.screen.IPanel;
import com.aurel.track.beans.screen.IScreen;
import com.aurel.track.beans.screen.ITab;
import com.aurel.track.screen.ScreenFactory;
import com.aurel.track.screen.adapterDAO.IFieldDAO;
import com.aurel.track.screen.adapterDAO.IPanelDAO;
import com.aurel.track.screen.adapterDAO.IScreenDAO;
import com.aurel.track.screen.adapterDAO.ITabDAO;

/**
 * 
 */
public class DashboardScreenFactory implements ScreenFactory {
    private static DashboardScreenFactory instance;
    public static DashboardScreenFactory getInstance(){
        if(instance==null){
            instance=new DashboardScreenFactory();
        }
        return instance;
    }
    protected DashboardScreenFactory(){
    }
    public IScreenDAO getScreenDAO() {
        return ScreenDAOAdapter.getInstance();
    }

    public ITabDAO getTabDAO() {
        return TabDAOAdapter.getInstance();
    }

    public IPanelDAO getPanelDAO() {
        return PanelDAOAdapter.getInstance();
    }

    public IFieldDAO getFieldDAO() {
        return FieldDAOAdapter.getInstance();
    }

    public IScreen createIScreeenInstance() {
        return new TDashboardScreenBean();
    }

    public IPanel createIPanelInstance() {
        IPanel pan=new TDashboardPanelBean();
        pan.setRowsNo(new Integer(1));
		pan.setColsNo(new Integer(1));
        return pan;
    }

    public ITab createITabInstance() {
        return new TDashboardTabBean();

    }

    public IField createIFieldInstance() {
        return new TDashboardFieldBean();
    }
}
