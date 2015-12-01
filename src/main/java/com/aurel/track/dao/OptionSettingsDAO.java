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


package com.aurel.track.dao;

import java.util.List;

import com.aurel.track.beans.TOptionSettingsBean;

public interface OptionSettingsDAO {
	
	/**
	 * Gets a optionSettingsBean from the TOptionSettings table
	 * @param objectID
	 * @return
	 */
	TOptionSettingsBean loadByPrimaryKey(Integer objectID);
	
	/**
	 * Gets a optionSettingsBean from the TOptionSettings table
	 * Should be used if it is part of a composit field
	 * @param configID
	 * @param parameterCode
	 * @return
	 */
	TOptionSettingsBean loadByConfigAndParameter(Integer configID, Integer parameterCode);
	
	/**
	 * Loads all option settings for a list of configIDs
	 * - Key: a combination of configID and parameterCode
	 * - Value TOptionSettingsBean
	 * @param configIDs
	 * @return
	 */
	List<TOptionSettingsBean> loadByConfigList(List<Integer> configIDs);
	
	/**
	 * Loads all option settings for the List's ObjectID
	 * @param listObjectId
	 * @return
	 */
	List<TOptionSettingsBean> loadByListID(Integer listObjectId);
	

	/**
	 * Loads all OptionSettings from table
	 * @return
	 */
	List<TOptionSettingsBean> loadAll();
	
	/**
	 * Saves an optionSettingsBean in the TOptionSettings table
	 * @param optionSettingsBean
	 * @return
	 */
	Integer save(TOptionSettingsBean optionSettingsBean);		
	
	/**
	 * Deletes by configID
	 * @param configID
	 */
	void delete(Integer configID);
}
