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


package com.aurel.track.fieldType.design;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.customize.treeConfig.TreeConfigIDTokens;
import com.aurel.track.beans.ConfigItem;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.errors.ErrorData;


/**
 * Base class for all fields (system and custom) 
 * defining their design time behavior 
 * @author Tamas Ruff
 *
 */
public class BaseFieldTypeDT implements IFieldTypeDT {

	protected Integer parameterCode;
	//the index (settings map key) of the 
	//settings object submitted from the UI
	protected Integer mapParameterCode;
	
	public static Integer SETTINGS_MAP_DEFAULT_KEY = Integer.valueOf(0);
	
	//the ID of the plugin
	protected String pluginID;
	
	public BaseFieldTypeDT(String pluginID) {
		super();
		mapParameterCode = SETTINGS_MAP_DEFAULT_KEY;
		this.pluginID = pluginID;
	}

	public BaseFieldTypeDT(Integer parameterCode, String pluginID) {
		super();
		this.parameterCode = parameterCode;
		if (parameterCode==null) {
			mapParameterCode = SETTINGS_MAP_DEFAULT_KEY;
		} else {
			mapParameterCode = parameterCode;
		}
		this.pluginID = pluginID;
	}
	
	public String getPluginID() {
		return pluginID;
	}

	public void setPluginID(String pluginID) {
		this.pluginID = pluginID;
	}

	/**   
	 * Whether the "Deprecated" check box should be rendered 
	 * Typically it should be rendered when the required flag should be also rendered 
	 * @return
	 */
	public boolean renderDeprecatedFlag() {
		return renderRequiredFlag();
	}
	
	/**
	 * Whether by configuring a system or custom field 
	 * the "Required" check box should be rendered or not.
	 * It should not render for the required system fields 
	 * and for the types where this doesn't makes sense: 
	 * ex. labels, check boxes, superiorWorkitem  
	 * @return
	 */
	public boolean renderRequiredFlag() {
		return true;
	}
	
	/**   
	 * Whether the "History" check box should be rendered  
	 * It should not be rendered for: 
	 * 1. the status, start/end date 
	 * (the explicit fields from the old history), 
	 * and comment. They are forced by code to true. 
	 * 2. some read only fields issueNo, originator, createDate, 
	 * lastModifiedDate, accessLevel, lastEditedBy, archiveLevel
	 * They are forced by code to false
	 * @return
	 */
	public boolean renderHistoryFlag() {
		return true;
	}
	
	/**
	 * Copy all the settings regarding a field configuration to an other field configuration
	 * Used only by override (for rendering the settings to the user the getSettingsJSON() is used)
	 * @param srcSettings
	 * @param destSettings
	 * @param destConfigID
	 */
	public void copySettings(Map<Integer, Object> srcSettings, 
			Map<Integer, Object> destSettings, Integer destConfigID) {				
	}

	public void deleteSettings(Integer configID) {		
	}

	public List<ErrorData> isValidSettings(Map<Integer, Object> settings) {
		return null;
	}
	
	/**
	 * Gets the specific JSON string for a field
	 * @param configID ID of the direct or nearest fallback configuration 
	 * @param treeConfigIDTokens a decoded node
	 * @param personBean
	 * @param locale
	 * @param bundleName
	 * @return
	 */
	public String getSettingsJSON(Integer configID, 
			TreeConfigIDTokens treeConfigIDTokens, TPersonBean personBean, Locale locale, String bundleName) {
		return getLocalizationJSON(locale, bundleName);
	}
	
	/**
	 * Gets the JSON string for new a field
	 * @param personBean
	 * @param locale
	 * @param bundleName
	 * @return
	 */
	public String getDefaultSettingsJSON(TPersonBean personBean, Locale locale, String bundleName) {
		return getLocalizationJSON(locale, bundleName);
	}
	
	/**
	 * Gets the localized labels used in field specific configuration
	 * (are common for getSettingsJSON() and getDefaultSettingsJSON())  
	 * @param locale
	 * @param bundleName 
	 * @return
	 */
	public String getLocalizationJSON(Locale locale, String bundleName) {
		return "";
	}

	public Map<Integer, Object> loadSettings(Integer configID) {		
		return new HashMap<Integer, Object>();
	}

	/**
	 * Saves the configuration settings for a field
	 * @param settings
	 * @param configID
	 */
	public void saveSettings(Map<Integer, Object> settings, Integer configID) {		
	}
	
	/**
	 * Executes a command
	 * @param configItem
	 */
	public void executeCommand(ConfigItem configItem) {
		
	}
	
		
}
