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

import com.trackplus.model.Tscript;

/**
 * DAO for Groovy Scripts.
 * 
 * @author Joerg Friedrich
 * @since 3.7.0
 */
public interface ScriptsDAO {

	/**
	 * Loads the script by primary key
	 * 
	 * @param objectID
	 * @return
	 */
	Tscript loadByPrimaryKey(Integer objectID);

	/**
	 * Get the Tscript by class name
	 * 
	 * @param className
	 * @param objectID
	 * @return
	 */
	boolean classNameExists(String className, Integer objectID);

	/**
	 * Returns a typed List with all scripts.
	 * 
	 * @return List with Tscript beans.
	 */
	List<Tscript> getAllScripts();

	/**
	 * Returns a typed List with all Tscript beans of scriptType
	 * 
	 * @return List with Tscript beans.
	 */
	List<Tscript> geTscriptByType(Integer scriptType);

	/**
	 * Saves the scripts to the database.
	 */
	Integer saveScript(Tscript script);

	/**
	 * Whether the personID has right to delete the objectID script
	 * 
	 * @param objectID
	 * @param personID
	 * @return
	 */
	// boolean isAllowedToDelete(Integer objectID, Integer personID);

	/**
	 * Whether the script has dependent data
	 * 
	 * @param objectID
	 * @return
	 */
	boolean hasDependentData(Integer objectID);

	/**
	 * Deletes the script from the database.
	 * 
	 * @param scriptID
	 */
	void deleteScript(Integer scriptID);

}
