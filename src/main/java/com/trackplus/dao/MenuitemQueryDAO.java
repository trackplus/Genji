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


package com.trackplus.dao;

import java.util.List;

import com.trackplus.model.Tmenuitemquery;

public interface MenuitemQueryDAO {

	/**
	 * Loads the menuitemQueryBeans by person
	 * 
	 * @param personID
	 * @param queryIDs
	 * @return
	 */
	/**
	 * Loads the menuitemQueryBeans by person and queryIDs
	 * 
	 * @param personID
	 * @param queryIDs
	 * @return
	 */
	List<Tmenuitemquery> loadByPersonAndQueries(Integer personID,
			List<Integer> queryIDs);

	/**
	 * Loads the menuitemQueryBeans by person and query
	 * 
	 * @param personID
	 * @param queryID
	 * @return
	 */
	Tmenuitemquery loadByPersonAndQuery(Integer personID, Integer queryID);

	/**
	 * Saves a menuitemQueryBean in the database
	 * 
	 * @param menuitemQueryBean
	 * @return
	 */
	Integer save(Tmenuitemquery menuitemQueryBean);

	/**
	 * Deletes a menuitemQueryBean from the database
	 * 
	 * @param personID
	 * @param queryID
	 * @return
	 */
	void delete(Integer personID, Integer queryID);

}
