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

import com.aurel.track.beans.TNavigatorColumnBean;

/**
 * DAO for navigator fields
 * @author Tamas
 *
 */
public interface NavigatorColumnDAO {
	
	
	/**
	 * Loads the fields for a layout
	 * @param navigatorLayoutID
	 * @return
	 */
	public TNavigatorColumnBean loadByPrimaryKey(Integer navigatorColumnID);
	
	/**
	 * Loads the fields for a layout
	 * @param navigatorLayoutID
	 * @return
	 */
	public List<TNavigatorColumnBean> loadByLayout(Integer navigatorLayoutID);
	
	/**
	 * Loads the column for a field in a layout
	 * @param navigatorLayoutID
	 * @param fieldID
	 * @return
	 */
	public List<TNavigatorColumnBean> loadByLayoutAndField(Integer navigatorLayoutID, Integer fieldID) ;
	
	/**
	 * Saves a navigator field in layout
	 * @param navigatorLayoutBean
	 * @return
	 */
	public Integer save(TNavigatorColumnBean navigatorFieldsBean);
	
	/**
	 * Deletes a field from layout
	 * @param  objectID
	 * @return
	 */
	public void delete(Integer objectID);
	
	/**
	 * Deletes all fields from a layout
	 * @param layoutID
	 * @return
	 */
	public void deleteByLayout(Integer layoutID);
}
