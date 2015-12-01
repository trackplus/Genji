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

package com.aurel.track.admin.customize.treeConfig.field;

import java.util.List;

import com.aurel.track.beans.TTextBoxSettingsBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.TextBoxSettingsDAO;

/**
 * Textbox configuration logic 
 * @author Tamas
 *
 */
public class TextBoxSettingsBL {
	private static TextBoxSettingsDAO textBoxSettingsDAO = DAOFactory.getFactory().getTextBoxSettingsDAO();
	
	/**
	 * Gets a textBoxSettingsBean from the TTextBoxSettings table
	 * @param objectID
	 * @return
	 */
	public static TTextBoxSettingsBean loadByPrimaryKey(Integer objectID) {
		return textBoxSettingsDAO.loadByPrimaryKey(objectID);
	}
			
	/**
	 * Gets a textBoxSettingsBean from the TTextBoxSettings table
	 * Should be used if it is part of a composite field
	 * @param configID
	 * @param parameterCode
	 * @return
	 */
	public static TTextBoxSettingsBean loadByConfigAndParameter(Integer configID, Integer parameterCode) {
		return textBoxSettingsDAO.loadByConfigAndParameter(configID, parameterCode);
	}
	
	/**
	 * Loads all textbox settings for a list of configIDs
	 * - Key: a combination of configID and parameterCode
	 * - Value TTextBoxSettingsBean 
	 * @param configIDs
	 * @return
	 */
	public static List<TTextBoxSettingsBean> loadByConfigList(List<Integer> configIDs) {
		return textBoxSettingsDAO.loadByConfigList(configIDs);
	}
	
	/**
	 * Loads all textbox settings for a configID
	 * - Key: a combination of configID and parameterCode
	 * - Value TTextBoxSettingsBean 
	 * @param configIDs
	 * @return
	 */
	public static List<TTextBoxSettingsBean> loadByConfig(Integer configID) {
		return textBoxSettingsDAO.loadByConfig(configID);
	}
	
	
    /**
     * Loads all TextBoxSettings from table 
     * @return
     */
	public static List<TTextBoxSettingsBean> loadAll() {
		return textBoxSettingsDAO.loadAll();
	}
	
	/**
	 * Saves a textBoxSettingsBean in the TTextBoxSettings table
	 * @param textBoxSettingsBean
	 * @return
	 */
	public static Integer save(TTextBoxSettingsBean textBoxSettingsBean) {
		return textBoxSettingsDAO.save(textBoxSettingsBean);
	}
	
	/**
	 * Deletes by configID
	 * @param configID
	 */
	public static void delete(Integer configID) {
		textBoxSettingsDAO.delete(configID);
	}
}
