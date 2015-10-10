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

package com.aurel.track.admin.customize.treeConfig.field;

import java.util.List;

import com.aurel.track.beans.TGeneralSettingsBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.GeneralSettingsDAO;

/**
 * General configuration logic 
 * @author Tamas
 *
 */
public class GeneralSettingsBL {
	private static GeneralSettingsDAO generalSettingsDAO = DAOFactory.getFactory().getGeneralSettingsDAO();
	
	/**
	 * Gets a generalSettingsBean from the TGeneralSettings table
	 * @param key
	 * @return
	 */
	public static TGeneralSettingsBean loadByPrimaryKey(Integer key) {
		return generalSettingsDAO.loadByPrimaryKey(key);
	}
			
	/**
	 * Gets a generalSettingsBean from the TGeneralSettings table  
	 * @param configID
	 * @param parameterCode
	 * @return
	 */
	public static TGeneralSettingsBean loadSingleByConfigAndParameter(Integer configID, Integer parameterCode) {
		return generalSettingsDAO.loadSingleByConfigAndParameter(configID, parameterCode);
	}

	 /**
	 * Gets a generalSettingsBean list from the TGeneralSettings table for the same parameterCode
	 * Used by multiple select lists 
	 * Should be used if it is part of a composit field
	 * @param configID
	 * @param parameterCode
	 * @return
	 */
	public static  List<TGeneralSettingsBean> loadListByConfigAndParameter(Integer configID, Integer parameterCode) {
		return generalSettingsDAO.loadListByConfigAndParameter(configID, parameterCode);
	}
   
	/**
	 * Gets the generalSettingsBeans from the 
	 * TGeneralSettings table for a configID 
	 * @param configID
	 * @return
	 */
	public static List<TGeneralSettingsBean> loadByConfig(Integer configID) {
		return generalSettingsDAO.loadByConfig(configID);
	}

	/**
	 * Loads all genaral settings for a list of configIDs
	 * - Key: a combination of configID and parameterCode
	 * - Value TGeneralSettingsBean
	 * @param configIDs
	 * @return
	 */
	public static List<TGeneralSettingsBean> loadByConfigList(List<Integer> configIDs) {
		return generalSettingsDAO.loadByConfigList(configIDs);
	}
	
	/**
	 * Loads all genaral settings for a list of configIDs
	 * - Key: a combination of configID and parameterCode
	 * - Value TGeneralSettingsBean
	 * @param configIDs
	 * @return
	 */
	public static List<TGeneralSettingsBean> loadByConfigListAndParametercode(List<Integer> configIDs, Integer parameterCode) {
		return generalSettingsDAO.loadByConfigListAndParametercode(configIDs, parameterCode);
	}
	
   /**
	 * Loads all generalsettings from table	
	 * @return
	 */
	public static List<TGeneralSettingsBean> loadAll() {
		return generalSettingsDAO.loadAll();
	}
	
	/**
	 * Saves a generalSettingsBean in the TGeneralSettings table
	 * @param generalSettingsBean
	 * @return
	 */
	public static Integer save(TGeneralSettingsBean generalSettingsBean) {
		return generalSettingsDAO.save(generalSettingsBean);
	}
	
	/**
	 * Deletes the records from the TGeneralSettings table by a config
	 * @param configID
	 */
	public static void deleteByConfig(Integer configID) {
		generalSettingsDAO.deleteByConfig(configID);
	}
}
