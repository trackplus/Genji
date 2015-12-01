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

import com.trackplus.model.Tlastexecutedquery;

/**
 */
public interface LastExecutedQueryDAO {

	Tlastexecutedquery loadByPrimaryKey(Integer objectID);

	/**
	 * Loads all
	 * 
	 * @return
	 */
	List<Tlastexecutedquery> loadAll();

	/**
	 * Load last executed filters by person
	 * 
	 * @param personID
	 * @return
	 */
	List<Tlastexecutedquery> loadByPerson(Integer personID);

	/**
	 * @param personID
	 * @return
	 */
	Tlastexecutedquery loadLastByPerson(Integer personID);

	/**
	 * Save screenField in the TScreenField table
	 * 
	 * @param lastExecutedQuery
	 * @return
	 */
	Integer save(Tlastexecutedquery lastExecutedQuery);

	/**
	 * Deletes a screenField from the TScreenField table Is deletable should
	 * return true before calling this method
	 * 
	 * @param objectID
	 */
	void delete(Integer objectID);

}
