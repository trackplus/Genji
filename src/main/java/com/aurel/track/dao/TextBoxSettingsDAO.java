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

import com.aurel.track.beans.TTextBoxSettingsBean;

public interface TextBoxSettingsDAO {
	
	/**
	 * Gets a textBoxSettingsBean from the TTextBoxSettings table
	 * @param objectID
	 * @return
	 */
	TTextBoxSettingsBean loadByPrimaryKey(Integer objectID);
	
	/**
	 * Saves a textBoxSettingsBean in the TTextBoxSettings table
	 * @param textBoxSettingsBean
	 * @return
	 */
	Integer save(TTextBoxSettingsBean textBoxSettingsBean);
	
	/**
	 * Deletes by configID
	 * @param configID
	 */
	void delete(Integer configID);
			
	/**
	 * Gets a textBoxSettingsBean from the TTextBoxSettings table
	 * Should be used if it is part of a composite field
	 * @param configID
	 * @param parameterCode
	 * @return
	 */
	TTextBoxSettingsBean loadByConfigAndParameter(Integer configID, Integer parameterCode);
	
	/**
	 * Loads all textbox settings for a list of configIDs
	 * - Key: a combination of configID and parameterCode
	 * - Value TTextBoxSettingsBean 
	 * @param configIDs
	 * @return
	 */
	List<TTextBoxSettingsBean> loadByConfigList(List<Integer> configIDs);
	
	/**
	 * Loads all textbox settings for a configID
	 * - Key: a combination of configID and parameterCode
	 * - Value TTextBoxSettingsBean 
	 * @param configIDs
	 * @return
	 */
	List<TTextBoxSettingsBean> loadByConfig(Integer configID);
	
	
    /**
     * Loads all TextBoxSettings from table 
     * @return
     */
	List<TTextBoxSettingsBean> loadAll();
	
}
