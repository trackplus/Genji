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

import com.trackplus.model.Tgeneralsetting;

public interface GeneralSettingsDAO {

	/**
	 * Gets a generalSettingsBean from the Tgeneralsetting table
	 * 
	 * @param key
	 * @return
	 */
	Tgeneralsetting loadByPrimaryKey(Integer key);

	/**
	 * Saves a generalSettingsBean in the Tgeneralsetting table
	 * 
	 * @param generalSettingsBean
	 * @return
	 */
	Integer save(Tgeneralsetting generalSettingsBean);

	/**
	 * Deletes the records from the Tgeneralsetting table by a config
	 * 
	 * @param configID
	 */
	void deleteByConfig(Integer configID);

	/**
	 * Gets a generalSettingsBean from the Tgeneralsetting table
	 * 
	 * @param configID
	 * @param parameterCode
	 * @return
	 */
	Tgeneralsetting loadSingleByConfigAndParameter(Integer configID,
			Integer parameterCode);

	/**
	 * Gets a generalSettingsBean list from the Tgeneralsetting table for the
	 * same parameterCode Used by multiple select lists Should be used if it is
	 * part of a composit field
	 * 
	 * @param configID
	 * @param parameterCode
	 * @return
	 */
	public List<Tgeneralsetting> loadListByConfigAndParameter(Integer configID,
			Integer parameterCode);

	/**
	 * Gets the generalSettingsBeans from the Tgeneralsetting table for a
	 * configID
	 * 
	 * @param configID
	 * @return
	 */
	List<Tgeneralsetting> loadByConfig(Integer configID);

	/**
	 * Loads all genaral settings for a list of configIDs - Key: a combination
	 * of configID and parameterCode - Value Tgeneralsetting * @param configIDs
	 * 
	 * @return
	 */
	List<Tgeneralsetting> loadByConfigList(List<Integer> configIDs);

	/**
	 * Loads all genaral settings for a list of configIDs - Key: a combination
	 * of configID and parameterCode - Value Tgeneralsetting * @param configIDs
	 * 
	 * @return
	 */
	List<Tgeneralsetting> loadByConfigListAndParametercode(
			List<Integer> configIDs, Integer parameterCode);

	/**
	 * A Loads all generalsettings from table
	 * 
	 * @return
	 */
	public List<Tgeneralsetting> loadAll();
}
