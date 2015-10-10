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

import com.aurel.track.beans.TConfigOptionsRoleBean;

public interface ConfigOptionRoleDAO {
	
	/**
	 * Loads all restricted options which are valid 
	 * for the person and project given a configID
	 * If no restriction exists return null which means 
	 * all the options from the list should be loaded
	 * If it returns an empty list, it means that all 
	 * options are restricted  
	 * @param configID
	 * @param person
	 * @param project 
	 */
	List loadOptionsByConfigForRoles(Integer configID, Integer person,  Integer project);
	
	/**
	 * Loads all edit options which are valid 
	 * for the person and project given a configID
	 * If no restriction exists return null which means 
	 * all the options from the list should be loaded  
	 * @param configID
	 * @param person
	 * @param project 
	 */
	//List loadEditOptionsByConfigForRoles(Integer configID, Integer person,  Integer project, Integer[] currentOption);
	
	/**
	 * Whether the config has at least one option restriction
	 * @param configID
	 * @return
	 */
	boolean hasOptionRestriction(Integer configID);
	
	/**
	 * Saves a new/existing configOptionsRoleBeanBean in the TConfigOptionsRole table
	 * @param configOptionsRoleBean
	 * @return the created optionID
	 */
	Integer save(TConfigOptionsRoleBean configOptionsRoleBean);
	
	/**
	 * Deletes an optionBean from the TOption table
	 * @param objectID
	 */
	void delete(Integer objectID);
	
						
}
