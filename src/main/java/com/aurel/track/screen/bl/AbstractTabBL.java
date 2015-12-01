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

package com.aurel.track.screen.bl;

import com.aurel.track.beans.screen.ITab;
import com.aurel.track.screen.ScreenFactory;
import com.aurel.track.screen.adapterDAO.IPanelDAO;
import com.aurel.track.screen.adapterDAO.ITabDAO;

/**
 * 
 */
public abstract class AbstractTabBL {
	protected IPanelDAO panelDAO;
	protected ITabDAO tabDAO;
	protected abstract ScreenFactory getScreenFactory();
	protected AbstractTabBL(){
		tabDAO = getScreenFactory().getTabDAO();
		panelDAO = getScreenFactory().getPanelDAO();
	}

	/**
	 * Load a ScreenTab having given pk
	 * @param pk
	 * @return
	 */
	public ITab loadTab(Integer pk){
		ITab tab= tabDAO.loadByPrimaryKey(pk);
		return tab;
	}
	public ITab loadFullTab(Integer pk){
		ITab tab= tabDAO.loadFullByPrimaryKey(pk);
		return tab;
	}
}
