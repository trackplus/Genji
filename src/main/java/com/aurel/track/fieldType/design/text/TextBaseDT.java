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


package com.aurel.track.fieldType.design.text;

import java.util.HashMap;
import java.util.Map;

import com.aurel.track.admin.customize.treeConfig.field.TextBoxSettingsBL;
import com.aurel.track.beans.TTextBoxSettingsBean;
import com.aurel.track.fieldType.design.BaseFieldTypeDT;

/**
 * Base class for defining the design time behavior 
 * for custom single text box field types 
 * @author Tamas Ruff
 *
 */
public abstract class TextBaseDT extends BaseFieldTypeDT {
	
	public TextBaseDT(Integer parameterCode, String pluginID) {
		super(parameterCode, pluginID);		
	}

	public TextBaseDT(String pluginID) {
		super(pluginID);
	}

	protected TTextBoxSettingsBean getTTextBoxSettingsBeanByConfig(Integer configID) {
		return TextBoxSettingsBL.loadByConfigAndParameter(configID, parameterCode);
	}
	
	/**
	 * Copy all the settings regarding a field configuration to an other field configuration
	 * Used only by override (for rendering the settings to the user the getSettingsJSON() is used)
	 * @param srcSettings
	 * @param destSettings
	 * @param destConfigID
	 */
	@Override
	public void copySettings(Map<Integer, Object> srcSettings,
			Map<Integer, Object> destSettings, Integer destConfigID) {		
		TTextBoxSettingsBean srcTextBoxSetting = (TTextBoxSettingsBean)srcSettings.get(mapParameterCode);
		TTextBoxSettingsBean destTextBoxSetting = (TTextBoxSettingsBean)destSettings.get(mapParameterCode);			
		if (destTextBoxSetting == null) {
			destTextBoxSetting = new TTextBoxSettingsBean();
			destTextBoxSetting.setConfig(destConfigID);
			destSettings.put(mapParameterCode, destTextBoxSetting);
		}
		//only if there are specific source settings
		if (srcTextBoxSetting!=null) {
			copySettingsSpecific(srcTextBoxSetting, destTextBoxSetting);
		}
	}
	
	/**
	 * Copies the specific settings between two text box pojo objects depending on the exact text box type 
	 * @param srcTextBoxSetting
	 * @param destTextBoxSetting
	 */
	public abstract void copySettingsSpecific(TTextBoxSettingsBean srcTextBoxSetting, TTextBoxSettingsBean destTextBoxSetting);
	
	/**
	 * Loads a map with settings for a text box from the database
	 */
	@Override
	public Map<Integer, Object> loadSettings(Integer configID) {		
		Map<Integer, Object> settings = new HashMap<Integer, Object>();
		TTextBoxSettingsBean textBoxSettingsBean;
		if (configID==null) {
			textBoxSettingsBean = new TTextBoxSettingsBean();
		} else {
			textBoxSettingsBean = TextBoxSettingsBL.loadByConfigAndParameter(configID, parameterCode);
		}
		settings.put(mapParameterCode, textBoxSettingsBean);		
		return settings;
	}
	
	/**
	 * Saves the configuration settings for a field
	 * @param settings
	 * @param configID
	 */
	@Override
	public void saveSettings(Map<Integer, Object> settings, Integer configID) {		
		TTextBoxSettingsBean textBoxSettingsBean = (TTextBoxSettingsBean)settings.get(mapParameterCode);
		if (textBoxSettingsBean==null) {
			//if check box type and the default value is unchecked then no settings 
			//value is submitted, the textBoxSettingsBean bean is not created by struts    
			textBoxSettingsBean = new TTextBoxSettingsBean();			
		}
		textBoxSettingsBean.setConfig(configID);
		TextBoxSettingsBL.save(textBoxSettingsBean);
	}
	
	/**
	 * Deletes the settings for a configID
	 * @param configID
	 */
	@Override
	public void deleteSettings(Integer configID) {		
		TextBoxSettingsBL.delete(configID);
	}

	
}
