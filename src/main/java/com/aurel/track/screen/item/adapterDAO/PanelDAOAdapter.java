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

import com.aurel.track.beans.TScreenPanelBean;
import com.aurel.track.beans.screen.IPanel;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.ScreenPanelDAO;
import com.aurel.track.screen.adapterDAO.IPanelDAO;

/**
 *
 */
class PanelDAOAdapter implements IPanelDAO {
	private ScreenPanelDAO screenPanelDAO;
	private static PanelDAOAdapter instance;
	public static PanelDAOAdapter getInstance() {
		if(instance ==null){
			instance =new PanelDAOAdapter();
		}
		return instance;
	}

	private PanelDAOAdapter() {
		screenPanelDAO = DAOFactory.getFactory().getScreenPanelDAO();
	}

	public IPanel loadByPrimaryKey(Integer objectID) {
		return screenPanelDAO.loadByPrimaryKey(objectID);
	}
	public IPanel loadFullByPrimaryKey(Integer objectID){
		return screenPanelDAO.loadFullByPrimaryKey(objectID);
	}

	public List loadAll() {
		return screenPanelDAO.loadAll();
	}

	public Integer save(IPanel panel) {
		return screenPanelDAO.save((TScreenPanelBean)panel);
	}

	public void delete(Integer objectID) {
		screenPanelDAO.delete(objectID);
	}

	public boolean isDeletable(Integer objectID) {
		return screenPanelDAO.isDeletable(objectID);
	}

	public List loadByParent(Integer parentID) {
		return screenPanelDAO.loadByParent(parentID);
	}
}
