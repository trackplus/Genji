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

package com.aurel.track.screen.bl;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.screen.IScreen;
import com.aurel.track.screen.ScreenFactory;
import com.aurel.track.screen.adapterDAO.IScreenDAO;
import com.aurel.track.screen.adapterDAO.ITabDAO;

/**
 */
public abstract class AbstractScreenBL implements IScreenBL {
    protected static Logger LOGGER = LogManager.getLogger(AbstractScreenBL.class);

	protected IScreenDAO screenDAO;
	protected ITabDAO tabDAO;
    protected abstract ScreenFactory getScreenFactory();
	protected AbstractScreenBL(){
		screenDAO= getScreenFactory().getScreenDAO();
		tabDAO= getScreenFactory().getTabDAO();
	}

    /**
	 * Get the screens
	 * @return
	 */
	public List getScreens(){
		return screenDAO.loadAll();
	}

	/**
	 * Load a screen with given pk
	 * @param pk
	 * @return
	 */
	public IScreen loadScreen(Integer pk){
		IScreen screen=screenDAO.loadByPrimaryKey(pk);
		List tabs=tabDAO.loadByParent(pk);
		screen.setTabs(tabs);
		return screen;
	}
	
	/**
	 * Load a screen with given pk
	 * @param pk
	 * @return
	 */
	public IScreen tryToLoadScreen(Integer pk){
		IScreen screen=screenDAO.tryToLoadByPrimaryKey(pk);
		List tabs=tabDAO.loadByParent(pk);
		screen.setTabs(tabs);
		return screen;
	}
}
