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

package com.aurel.track.admin.customize.category.filter;

import java.util.List;

import com.aurel.track.beans.TCLOBBean;
import com.aurel.track.dao.ClobDAO;
import com.aurel.track.dao.DAOFactory;
/**
 * Clob utility methods
 * @author Tamas
 *
 */
public class ClobBL {

	private static ClobDAO clobDAO = DAOFactory.getFactory().getClobDAO();
	
	/**
	 * Gets a CLOBBean by primary key 
	 * @param objectID
	 * @return
	 */
	public static TCLOBBean loadByPrimaryKey(Integer objectID) {
		return clobDAO.loadByPrimaryKey(objectID);
	}
	
	/**
	 * Loads CLOBBeans by primary keys
	 * @param objectIDs
	 * @return
	 */
	public static List<TCLOBBean> loadByPrimaryKeys(List<Integer> objectIDs) {
		return clobDAO.loadByPrimaryKeys(objectIDs);
	}
	
	/**
	 * Load all clobs
	 * @return
	 */
	public static List<TCLOBBean> loadAll() {
		return clobDAO.loadAll();
	}
	
	/**
	 * Saves a CLOBBean
	 * @param clobBean
	 * @return
	 */
	public static Integer save(TCLOBBean clobBean) {
		return clobDAO.save(clobBean);
	}
	
	/**
	 * Deletes an CLOBBean by primary key
	 * @param objectID
	 */
	public static void delete(Integer objectID) {
		clobDAO.delete(objectID);
	}
}
