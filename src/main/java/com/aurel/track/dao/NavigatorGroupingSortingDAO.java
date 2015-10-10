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

import java.util.List;

import com.aurel.track.beans.TNavigatorGroupingSortingBean;

/**
 * DAO for navigator fields
 * @author Tamas
 *
 */
public interface NavigatorGroupingSortingDAO {
	
	/**
	 * Loads the fields for a layout
	 * @param navigatorLayoutID
	 * @return
	 */
	public List<TNavigatorGroupingSortingBean> loadByLayout(Integer navigatorLayoutID);
	
	/**
	 * Loads the sorting bean for a layout
	 * @param navigatorLayoutID
	 * @return
	 */
	public List<TNavigatorGroupingSortingBean> loadSortingByLayout(Integer navigatorLayoutID);

	/**
	 * Saves a grouping/sorting bean in layout
	 * @param navigatorGroupingSortingBean
	 * @return
	 */
	public Integer save(TNavigatorGroupingSortingBean navigatorGroupingSortingBean);
	
	/**
	 * Removes a grouping/sorting from layout
	 * @param  objectID
	 * @return
	 */
	public void delete(Integer objectID);
	
	/**
	 * Deletes all grouping/sorting from layout
	 * @param layoutID
	 * @return
	 */
	public void deleteByLayout(Integer layoutID);
}
