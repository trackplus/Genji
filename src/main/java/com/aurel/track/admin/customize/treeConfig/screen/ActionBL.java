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

package com.aurel.track.admin.customize.treeConfig.screen;

import java.util.List;

import com.aurel.track.beans.TActionBean;
import com.aurel.track.dao.ActionDAO;
import com.aurel.track.dao.DAOFactory;

/**
 * Logic for actions
 * @author Tamas
 *
 */
public class ActionBL {
	private static ActionDAO actionDAO=DAOFactory.getFactory().getActionDAO();

	/**
	 * Get the action having pk
	 * @param pk
	 * @return
	 */
	public static TActionBean getAction(Integer pk){
		return actionDAO.loadByPrimaryKey(pk);
	}
	
	/**
	 * Loads all fields from TAction table. 
	 * @return list with all {@link TActionBean} objects.
	 */
	public static List<TActionBean> loadAll() {
		return actionDAO.loadAll();
	}
		
	/**
	 * Load {@link TActionBean} list by actionIDs
	 * @param actionIDs
	 * @return
	 */
	public static List<TActionBean> loadByActionIDs(List<Integer> actionIDs) {
		return actionDAO.loadByActionIDs(actionIDs);
	}
	
	/**
	 * Deletes an action from the TAction table.
	 * Is deletable should return true before calling this method.
	 * @param actionOID the actions object identifier
	 */
	public static void delete(Integer actionOID) {
		actionDAO.delete(actionOID);
	}
}
