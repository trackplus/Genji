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


package com.trackplus.dao;

import java.util.List;
import java.util.Map;

import com.trackplus.model.Tsite;

public interface SiteDAO {
	/**
	 * Load the one and only TSite object
	 * 
	 * @return
	 */
	Tsite load();
	
	/**
	 * Just for consistency, load all TSite objects
	 */
	List<Tsite> loadAll();
	
	/**
	 * load this TSite object by its primary key
	 * 
	 * @param primaryKey - the object id or primary key
	 */
	Tsite loadByPrimaryKey(Integer primaryKey);
	
	/**
	 * load this TSite object by its primary key
	 * 
	 * @param primaryKey - the object id or primary key
	 */
	Tsite load(Integer primaryKey);

	/**
	 * loads, updates and saves the Tsite in a synchronized method
	 * 
	 * @param fieldValues
	 */
	void loadAndSaveSynchronized(Map<Integer, Object> fieldValues);

	/**
	 * Sets the SMTPUser and SMTPPassword fields to null
	 * 
	 */
	void clearSMTPPassword();

	/**
	 * save the TSite
	 * 
	 * @param site
	 */
	void save(Tsite site);
	
	/**
	 * delete the TSite
	 * 
	 * @param site
	 */
	void delete(Tsite site);
	
	/**
	 * delete this TSite object
	 * 
	 * @param primaryKey - the object id or primary key
	 */
	void delete(Integer primaryKey);

}
