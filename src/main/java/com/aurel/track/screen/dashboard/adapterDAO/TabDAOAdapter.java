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

package com.aurel.track.screen.dashboard.adapterDAO;


import java.util.List;

import com.aurel.track.beans.TDashboardTabBean;
import com.aurel.track.beans.screen.ITab;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.DashboardTabDAO;
import com.aurel.track.screen.adapterDAO.ITabDAO;

class TabDAOAdapter implements ITabDAO {
	private DashboardTabDAO screenTabDAO;
	private static TabDAOAdapter instance = new TabDAOAdapter();
	public static TabDAOAdapter getInstance() {
		if(instance ==null){
			instance =new TabDAOAdapter();
		}
		return instance;
	}

	private TabDAOAdapter() {
		screenTabDAO= DAOFactory.getFactory().getDashboardTabDAO();
	}

	@Override
	public ITab loadByPrimaryKey(Integer objectID) {
		return screenTabDAO.loadByPrimaryKey(objectID);
	}
	@Override
	public ITab loadFullByPrimaryKey(Integer objectID) {
		return screenTabDAO.loadFullByPrimaryKey(objectID);
	}

	@Override
	public List loadAll() {
		return screenTabDAO.loadAll();
	}

	@Override
	public Integer save(ITab tab) {
		return screenTabDAO.save((TDashboardTabBean)tab);
	}

	@Override
	public void delete(Integer objectID) {
		screenTabDAO.delete(objectID);
	}

	@Override
	public boolean isDeletable(Integer objectID) {
		return screenTabDAO.isDeletable(objectID);
	}

	@Override
	public List loadByParent(Integer parentID) {
		return screenTabDAO.loadByParent(parentID);
	}
}
