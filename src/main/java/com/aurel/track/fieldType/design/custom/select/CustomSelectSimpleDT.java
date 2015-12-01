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


package com.aurel.track.fieldType.design.custom.select;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.customize.lists.ListBL;
import com.aurel.track.admin.customize.treeConfig.TreeConfigIDTokens;
import com.aurel.track.admin.customize.treeConfig.field.FieldConfigJSON;
import com.aurel.track.admin.customize.treeConfig.field.OptionSettingsBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TListBean;
import com.aurel.track.beans.TOptionSettingsBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.design.BaseFieldTypeDT;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;

/**
 * Custom simple select for design time
 * @author Tamas Ruff
 *
 */
public class CustomSelectSimpleDT extends BaseFieldTypeDT {

	public CustomSelectSimpleDT(Integer parameterCode, String pluginID) {
		super(parameterCode, pluginID);
	}

	public CustomSelectSimpleDT(String pluginID) {
		super(pluginID);
	}

	@Override
	public List<ErrorData> isValidSettings(Map<Integer, Object> settings) {
		List<ErrorData> errorDataList = new ArrayList<ErrorData>();
		TOptionSettingsBean optionSettingsBean = (TOptionSettingsBean)settings.get(mapParameterCode);
		if (optionSettingsBean==null || optionSettingsBean.getList()==null) {
			errorDataList.add(new ErrorData("customSelectSimple.error.noList"));
		}
		return errorDataList;
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
		TOptionSettingsBean srcOptionSettingsBean = (TOptionSettingsBean)srcSettings.get(mapParameterCode);
		TOptionSettingsBean destOptionSettingsBean = (TOptionSettingsBean)destSettings.get(mapParameterCode);
		if (destOptionSettingsBean == null){
			destOptionSettingsBean = new TOptionSettingsBean();
			destOptionSettingsBean.setConfig(destConfigID);
			destSettings.put(mapParameterCode, destOptionSettingsBean);
		}
		//only if there are specific source settings
		if (srcOptionSettingsBean!=null) {
			destOptionSettingsBean.setList(srcOptionSettingsBean.getList());
			destOptionSettingsBean.setParameterCode(srcOptionSettingsBean.getParameterCode());
		}
	}
	
	@Override
	public Map<Integer, Object> loadSettings(Integer configID) {
		Map<Integer, Object> settings = new HashMap<Integer, Object>();
		TOptionSettingsBean optionSettingsBean;
		if (configID==null) {
			optionSettingsBean = new TOptionSettingsBean();
		} else {
			optionSettingsBean = OptionSettingsBL.loadByConfigAndParameter(configID, parameterCode);
		}
		settings.put(mapParameterCode, optionSettingsBean);
		return settings;
	}

	/**
	 * Saves the configuration settings for a field
	 * @param settings
	 * @param configID
	 */
	@Override
	public void saveSettings(Map<Integer, Object> settings, Integer configID) {
		TOptionSettingsBean optionSettingsBean = (TOptionSettingsBean)settings.get(mapParameterCode);
		optionSettingsBean.setConfig(configID);
		OptionSettingsBL.save(optionSettingsBean);
	}
	
	@Override
	public void deleteSettings(Integer configID) {
		OptionSettingsBL.delete(configID);
	}
	
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
		JSONUtility.appendILabelBeanList(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.OPTION_SETTINGS_LISTS,
				getLists(null, locale, null));
		return stringBuilder.append(getLocalizationJSON(locale, bundleName)).toString(); 
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
		TOptionSettingsBean optionSettingsBean = OptionSettingsBL.loadByConfigAndParameter(configID, parameterCode);
		StringBuilder stringBuilder = new StringBuilder();
		if (optionSettingsBean!=null) {
			JSONUtility.appendIntegerValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.OPTION_SETTINGS_LIST_PREFIX + "[0]." +
				FieldConfigJSON.JSON_FIELDS.OPTION_SETTINGS_LIST,
				optionSettingsBean.getList());
		}
		JSONUtility.appendILabelBeanList(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.OPTION_SETTINGS_LISTS,
				getLists(treeConfigIDTokens.getProjectID(), locale, configID));
		return stringBuilder.append(getLocalizationJSON(locale, bundleName)).toString(); 
	}
	
	/**
	 * Gets the localized labels used in field specific configuration
	 * (are common for getSettingsJSON() and getDefaultSettingsJSON())  
	 * @param locale
	 * @param bundleName 
	 * @return
	 */
	@Override
	public String getLocalizationJSON(Locale locale, String bundleName) {
		StringBuilder stringBuilder = new StringBuilder();		
		JSONUtility.appendStringValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.OPTION_SETTINGS_LIST_LABEL,
				getListLabel(locale, bundleName));
		
		return stringBuilder.toString(); 
	}
		
	protected String getListLabel(Locale locale, String bundleName) {
		return LocalizeUtil.getLocalizedText("customSelectSimple.prompt.list",
				locale, bundleName);
	}
	
	/**
	 * Gets the data specific to configuring the field
	 * Specifically useful when select lists should 
	 * be present at configuration time  
     * @param projectID
     * @param locale
	 * @param fieldConfigID
	 * @return
	 */
	protected List<ILabelBean> getLists(Integer projectID, Locale locale, Integer fieldConfigID) {
		Integer currentValue = null;
		if (fieldConfigID!=null) {
			TOptionSettingsBean optionSettingsBean = OptionSettingsBL.loadByConfigAndParameter(fieldConfigID, null);
			if (optionSettingsBean!=null) {
				currentValue = optionSettingsBean.getList();
			}
		}
		return (List)ListBL.getSelectsOfType(projectID, TListBean.LIST_TYPE.SIMPLE, null, currentValue);
	}
	
}
