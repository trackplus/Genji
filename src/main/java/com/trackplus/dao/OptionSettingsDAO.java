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

import com.trackplus.model.Toptionsetting;

public interface OptionSettingsDAO {

	/**
	 * Gets a optionSettingsBean from the Toptionsetting table
	 * 
	 * @param objectID
	 * @return
	 */
	Toptionsetting loadByPrimaryKey(Integer objectID);

	/**
	 * Saves an optionSettingsBean in the Toptionsetting table
	 * 
	 * @param optionSettingsBean
	 * @return
	 */
	Integer save(Toptionsetting optionSettingsBean);

	/**
	 * Deletes by configID
	 * 
	 * @param configID
	 */
	void delete(Integer configID);

	/**
	 * Gets a optionSettingsBean from the Toptionsetting table Should be used by
	 * simple list
	 * 
	 * @param configID
	 * @return
	 */

	/**
	 * Gets a optionSettingsBean from the Toptionsetting table Should be used if
	 * it is part of a composit field
	 * 
	 * @param configID
	 * @param parameterCode
	 * @return
	 */
	Toptionsetting loadByConfigAndParameter(Integer configID,
			Integer parameterCode);

	/**
	 * Loads all option settings for a list of configIDs - Key: a combination of
	 * configID and parameterCode - Value Toptionsetting * @param configIDs
	 * 
	 * @return
	 */
	List<Toptionsetting> loadByConfigList(List<Integer> configIDs);

	/**
	 * A Loads all option settings for the List's ObjectID
	 * 
	 * @param listObjectId
	 * @return
	 */
	List<Toptionsetting> loadByListID(Integer listObjectId);

	/**
	 * A Loads all OptionSettings from table
	 * 
	 * @return
	 */
	public List<Toptionsetting> loadAll();

}
