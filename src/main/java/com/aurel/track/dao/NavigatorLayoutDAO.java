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

package com.aurel.track.dao;

import com.aurel.track.beans.TNavigatorLayoutBean;

import java.util.List;

/**
 */
public interface NavigatorLayoutDAO {
	
	/**
	 * Load the default navigator layout
	 * @return
	 */
	public List<TNavigatorLayoutBean> loadDefault();
	
	/**
	 * Load navigator layout by user
	 * @param personID
	 * @return
	 */
	public List<TNavigatorLayoutBean> loadByPerson(Integer personID);
	
	/**
	 *  Load navigator layout by user and filter
	 * @param personID
	 * @param filterID
	 * @param filterType
	 * @return
	 */
	public List<TNavigatorLayoutBean> loadByPersonAndFilter(Integer personID,Integer filterID,Integer filterType);

	/**
	 * Load navigator layout filter and view
	 * @param personID
	 * @param filterID
	 * @param filterType
	 * @return
	 */
	public List<TNavigatorLayoutBean> loadByFilterWithView(Integer filterID, Integer filterType);
	
	/**
	 * Loads the layout by context
	 * @param personID
	 * @param filterID
	 * @param filterType
	 * @return
	 */
	public List<TNavigatorLayoutBean> loadByContext(Integer personID,Integer filterID,Integer filterType);

	/**
	 * Saves a navigator layout bean
	 * @param navigatorLayoutBean
	 * @return
	 */
	public Integer save(TNavigatorLayoutBean navigatorLayoutBean);
	
	/**
	 * Deletes a navigator layout with dependencies
	 * @param layoutID
	 * @return
	 */
	public void delete(Integer layoutID);
}
