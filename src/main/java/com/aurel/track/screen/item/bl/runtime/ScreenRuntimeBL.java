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

package com.aurel.track.screen.item.bl.runtime;

import com.aurel.track.admin.customize.treeConfig.screen.ScreenConfigItemFacade;
import com.aurel.track.beans.TScreenConfigBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.ScreenConfigDAO;
import com.aurel.track.screen.ScreenFactory;
import com.aurel.track.screen.bl.runtime.AbstractScreenRuntimeBL;
import com.aurel.track.screen.item.adapterDAO.ItemScreenFactory;

/**
 * Business Logic for obtain the proper screen in a given context:
 *  (Action,Project and IssueType)
 *  
 * @author Adrian Bojani
 *
 */
public class ScreenRuntimeBL extends AbstractScreenRuntimeBL {
	private ScreenConfigDAO screenConfigDAO;
	//singleton isntance
	private static ScreenRuntimeBL instance;

	/**
	 * get a singleton instance
	 * @return
	 */
	public static ScreenRuntimeBL getInstance() {
		if (instance == null) {
			instance = new ScreenRuntimeBL();
		}
		return instance;
	}

    @Override
	protected ScreenFactory getScreenFactory() {
        return ItemScreenFactory.getInstance();
    }

    /**
	 * constructor
	 */
	public ScreenRuntimeBL() {
		super();
        screenConfigDAO=DAOFactory.getFactory().getScreenConfigDAO();
    }
	/**
	 * Obtain the screen for edit action in given context
	 * @param project
	 * @param issuTypeID
	 * @return
	 */
	/*public Integer findScreenForEdit(TProjectBean project,Integer issuTypeID){
		return findScreenID(SystemActions.EDIT, project, issuTypeID);
	}*/
	/**
	 * Obtain the screen for new action in given context
	 * @param project
	 * @param issuTypeID
	 * @return
	 */
	/*public Integer findScreenForNew(TProjectBean project,Integer issuTypeID){
		return findScreenID(SystemActions.NEW, project, issuTypeID);
	}*/
	
	/**
	 * Obtain the screen in given context
	 * @param actionID
	 * @param projectBean
	 * @param issuTypeID
	 * @return
	 */
	public Integer findScreenID(Integer actionID, Integer projectID, Integer itemTypeID) {
		TScreenConfigBean screenConfigBean = (TScreenConfigBean)ScreenConfigItemFacade.getInstance().getValidConfigFallback(itemTypeID, null, projectID, actionID);
		if (screenConfigBean!=null) {
			return screenConfigBean.getScreen();
		}
		return null;
	}
}
