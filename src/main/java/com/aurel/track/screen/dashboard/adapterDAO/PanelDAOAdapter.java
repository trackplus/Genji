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

import java.util.List;

import com.aurel.track.beans.TDashboardPanelBean;
import com.aurel.track.beans.screen.IPanel;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.DashboardPanelDAO;
import com.aurel.track.screen.adapterDAO.IPanelDAO;

/**
 *
 */
class PanelDAOAdapter implements IPanelDAO {
	private DashboardPanelDAO dashboardPanelDAO;
	private static PanelDAOAdapter instance;
	public static PanelDAOAdapter getInstance() {
		if(instance ==null){
			instance =new PanelDAOAdapter();
		}
		return instance;
	}

	private PanelDAOAdapter() {
		dashboardPanelDAO = DAOFactory.getFactory().getDashboardPanelDAO();
	}

	public IPanel loadByPrimaryKey(Integer objectID) {
		return dashboardPanelDAO.loadByPrimaryKey(objectID);
	}
	public IPanel loadFullByPrimaryKey(Integer objectID) {
		return dashboardPanelDAO.loadFullByPrimaryKey(objectID);
	}
	

	public List loadAll() {
		return dashboardPanelDAO.loadAll();
	}

	public Integer save(IPanel panel) {
		return dashboardPanelDAO.save((TDashboardPanelBean)panel);
	}

	public void delete(Integer objectID) {
		dashboardPanelDAO.delete(objectID);
	}

	public boolean isDeletable(Integer objectID) {
		return dashboardPanelDAO.isDeletable(objectID);
	}

	public List loadByParent(Integer parentID) {
		return dashboardPanelDAO.loadByParent(parentID);
	}
}
