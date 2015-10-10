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

package com.aurel.track.screen.item.adapterDAO;

import java.util.List;

import com.aurel.track.beans.TScreenTabBean;
import com.aurel.track.beans.screen.ITab;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.ScreenTabDAO;
import com.aurel.track.screen.adapterDAO.ITabDAO;

class TabDAOAdapter implements ITabDAO {
	
	private ScreenTabDAO screenTabDAO;
	private static TabDAOAdapter instance = new TabDAOAdapter();
	public static TabDAOAdapter getInstance() {
		if(instance ==null){
			instance =new TabDAOAdapter();
		}
		return instance;
	}

	private TabDAOAdapter() {
		screenTabDAO= DAOFactory.getFactory().getScreenTabDAO();
	}

	public ITab loadByPrimaryKey(Integer objectID) {
		return screenTabDAO.loadByPrimaryKey(objectID);
	}
	public ITab loadFullByPrimaryKey(Integer objectID) {
		return screenTabDAO.loadFullByPrimaryKey(objectID);
	}

	public List loadAll() {
		return screenTabDAO.loadAll();
	}

	public Integer save(ITab tab) {
		return screenTabDAO.save((TScreenTabBean)tab);
	}

	public void delete(Integer objectID) {
		screenTabDAO.delete(objectID);
	}

	public boolean isDeletable(Integer objectID) {
		return screenTabDAO.isDeletable(objectID);
	}

	public List loadByParent(Integer parentID) {
		return screenTabDAO.loadByParent(parentID);
	}
}
