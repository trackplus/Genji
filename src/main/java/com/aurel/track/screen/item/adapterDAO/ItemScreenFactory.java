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

package com.aurel.track.screen.item.adapterDAO;

import com.aurel.track.beans.TScreenBean;
import com.aurel.track.beans.TScreenFieldBean;
import com.aurel.track.beans.TScreenPanelBean;
import com.aurel.track.beans.TScreenTabBean;
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
public class ItemScreenFactory implements ScreenFactory {
	private static ItemScreenFactory instance;
	public static ItemScreenFactory getInstance(){
		if(instance==null){
			instance=new ItemScreenFactory();
		}
		return instance;
	}
	protected ItemScreenFactory(){
	}
	@Override
	public IScreenDAO getScreenDAO() {
		return ScreenDAOAdapter.getInstance();
	}

	@Override
	public ITabDAO getTabDAO() {
		return TabDAOAdapter.getInstance();
	}

	@Override
	public IPanelDAO getPanelDAO() {
		return PanelDAOAdapter.getInstance();
	}

	@Override
	public IFieldDAO getFieldDAO() {
		return FieldDAOAdapter.getInstance();
	}

	@Override
	public IScreen createIScreeenInstance() {
		return new TScreenBean();
	}

	@Override
	public IPanel createIPanelInstance() {
		IPanel pan=new TScreenPanelBean();
		pan.setRowsNo(new Integer(3));
		pan.setColsNo(new Integer(3));
		return pan;
	}

	@Override
	public ITab createITabInstance() {
		return new TScreenTabBean();
	}

	@Override
	public IField createIFieldInstance() {
		return new TScreenFieldBean();
	}
}
