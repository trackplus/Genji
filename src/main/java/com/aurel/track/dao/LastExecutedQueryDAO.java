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

import java.sql.Connection;
import java.util.List;

import com.aurel.track.beans.TLastExecutedQueryBean;

/**
 */
public interface LastExecutedQueryDAO {

	/**
	 * Loads the last executed query by primary key
	 * @param objectID
	 * @return
	 */
	TLastExecutedQueryBean loadByPrimaryKey(Integer objectID);

	/**
	 * Load last executed filters by person
	 * @param personID
	 * @return
	 */
	List<TLastExecutedQueryBean> loadByPerson(Integer personID);

	/**
	 * @param personID
	 * @return
	 */
	TLastExecutedQueryBean loadLastByPerson(Integer personID);


	/**
	 * Save  screenField in the TScreenField table
	 * @param lastExecutedQuery
	 * @return
	 */
	Integer save(TLastExecutedQueryBean lastExecutedQuery);


	/**
	 * Deletes a screenField from the TScreenField table
	 * Is deletable should return true before calling this method
	 * @param objectID
	 */
	void delete(Integer objectID);

	/**
	 * Deletes the last executed quieries by filterID and filterType
	 */
	void deleteByFilterIDAndFilterType(Integer filterID, Integer filterType);
	
	/**
	 * Deletes the last executed quieries by filterID and filterType
	 */
	void deleteByFilterIDAndFilterType(Integer filterID, Integer filterType, Connection con);

}
