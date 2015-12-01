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

package com.aurel.track.screen.item.action;

import com.aurel.track.beans.screen.IField;
import com.aurel.track.item.ItemScreenCache;
import com.aurel.track.screen.ScreenFactory;
import com.aurel.track.screen.action.AbstractFieldAction;
import com.aurel.track.screen.bl.design.AbstractFieldDesignBL;
import com.aurel.track.screen.bl.design.AbstractPanelDesignBL;
import com.aurel.track.screen.item.adapterDAO.ItemScreenFactory;
import com.aurel.track.screen.item.bl.design.ScreenFieldDesignBL;
import com.aurel.track.screen.item.bl.design.ScreenPanelDesignBL;

/**
 * Used to treat the operations from screen designer
 * operations on field:reload,getproperty setproperty
 * @author Adrian Bojani
 *
 */
public class ScreenFieldAction extends AbstractFieldAction{

	private static final long serialVersionUID = 340L;

	@Override
	protected AbstractFieldDesignBL getAbstractFieldDesignBL() {
        return ScreenFieldDesignBL.getInstance();
    }

    @Override
	protected AbstractPanelDesignBL getAbstractPanelDesignBL() {
        return ScreenPanelDesignBL.getInstance();
    }

    @Override
	public ScreenFactory getScreenFactory() {
        return ItemScreenFactory.getInstance();
    }

    @Override
	protected String getType(IField field) {
    	return "";
    }
	@Override
	protected void clearCache(){
		ItemScreenCache.getInstance().removeScreen(screenID);
	}
}
