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
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.customize.treeConfig.TreeConfigIDTokens;
import com.aurel.track.beans.TPersonBean;

public abstract class CustomCompositeFieldTypeDT extends BaseFieldTypeDT {
		
	public CustomCompositeFieldTypeDT(String pluginID) {
		super(pluginID);		
	}


	/**
	 * Number of parts the composite field has
	 * @return
	 */
	public abstract int getNumberOfParts();
		
	
	/**
	 * The custom field type of each part 
	 * @param index
	 * @return
	 */
	public abstract BaseFieldTypeDT getCustomFieldType(int index);
	
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
		IFieldTypeDT fieldTypeDT;	
		for (int i = 0; i < getNumberOfParts(); i++) {			
			fieldTypeDT = getCustomFieldType(i+1);
			fieldTypeDT.copySettings(srcSettings, destSettings, destConfigID);					
		}	
	}

	/**
	 * Loads a map with default settings for each part
	 */
	/*public Map loadDefaultSettings() {
		Map fieldSettings = new HashMap();
		IFieldTypeDT fieldTypeDT;	
		for (int i = 0; i < getNumberOfParts(); i++) {
			fieldTypeDT = getCustomFieldType(i+1);
			Map partSettings = fieldTypeDT.loadDefaultSettings();
			fieldSettings.putAll(partSettings);			
		}
		return fieldSettings;
	}*/

	/**
	 * Gets the JSON string for new a field
	 * @param personBean
	 * @param locale
	 * @param bundleName
	 * @return
	 */
	@Override
	public String getDefaultSettingsJSON(TPersonBean personBean, Locale locale, String bundleName) {
		StringBuilder stringBuilder = new StringBuilder();
		IFieldTypeDT fieldTypeDT;
		for (int i = 0; i < getNumberOfParts(); i++) {
			fieldTypeDT = getCustomFieldType(i+1);
			String partSettings = fieldTypeDT.getDefaultSettingsJSON(personBean, locale, bundleName);
			stringBuilder.append(partSettings);			
		}
		return stringBuilder.toString();
	}
	
	/**
	 * Loads a map with settings for each part
	 */
	@Override
	public Map<Integer, Object> loadSettings(Integer configID) {
		Map<Integer, Object> fieldSettings = new HashMap<Integer, Object>();
		IFieldTypeDT fieldTypeDT;		
		for (int i = 0; i < getNumberOfParts(); i++) {
			fieldTypeDT = getCustomFieldType(i+1);
			Map partSettings = fieldTypeDT.loadSettings(configID);
			fieldSettings.putAll(partSettings);			
		}
		return fieldSettings;
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
	@Override
	public String getSettingsJSON(Integer configID, 
			TreeConfigIDTokens treeConfigIDTokens, TPersonBean personBean, Locale locale, String bundleName) {
		StringBuilder stringBuilder = new StringBuilder();
		IFieldTypeDT fieldTypeDT;
		for (int i = 0; i < getNumberOfParts(); i++) {
			fieldTypeDT = getCustomFieldType(i+1);
			String partSettings = fieldTypeDT.getSettingsJSON(configID, treeConfigIDTokens, personBean, locale, bundleName);
			stringBuilder.append(partSettings);			
		}
		return stringBuilder.toString();
	}
	
	/**
	 * Saves the configuration settings for a field
	 * @param settings
	 * @param configID
	 */
	@Override
	public void saveSettings(Map<Integer, Object> settings, Integer configID) {		
		IFieldTypeDT fieldTypeDT;		
		for (int i = 0; i < getNumberOfParts(); i++) {			
			fieldTypeDT = getCustomFieldType(i+1);
			fieldTypeDT.saveSettings(settings, configID);					
		}		
	}
}
