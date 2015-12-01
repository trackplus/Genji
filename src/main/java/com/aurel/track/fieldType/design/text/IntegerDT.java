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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.customize.treeConfig.TreeConfigIDTokens;
import com.aurel.track.admin.customize.treeConfig.field.FieldConfigJSON;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TTextBoxSettingsBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;

/**
 * Custom textbox for integer values for design time
 * @author Tamas Ruff
 *
 */
public class IntegerDT extends TextBaseDT {
	
	
	
	public IntegerDT(Integer parameterCode, String pluginID) {
		super(parameterCode, pluginID);		
	}

	public IntegerDT(String pluginID) {
		super(pluginID);
	}

	@Override
	public List<ErrorData> isValidSettings(Map<Integer, Object> settings) {
		List<ErrorData> errorDataList = new ArrayList<ErrorData>();
		TTextBoxSettingsBean textBoxSettingsBean = (TTextBoxSettingsBean)settings.get(mapParameterCode);
		//textBoxSettingsBean can be null when no settings was set (especially by system fields)
		if (textBoxSettingsBean!=null) {
			Integer defaultInteger = textBoxSettingsBean.getDefaultInteger();
			Integer minInteger = textBoxSettingsBean.getMinInteger();
			Integer maxInteger = textBoxSettingsBean.getMaxInteger();		
			if (minInteger!=null && maxInteger!=null && 
					minInteger.intValue()>maxInteger.intValue()) {
				errorDataList.add(new ErrorData("customTextBoxInteger.error.minMax"));
			}
			if (minInteger!=null && defaultInteger!=null && 
					minInteger.intValue()>defaultInteger.intValue()) {
				errorDataList.add(new ErrorData("customTextBoxInteger.error.defaultMin"));
			}
			if (maxInteger!=null && defaultInteger!=null && 
					maxInteger.intValue()<defaultInteger.intValue()) {
				errorDataList.add(new ErrorData("customTextBoxInteger.error.defaultMax"));
			}		
		}
		return errorDataList;
	}
		
	@Override
	public void copySettingsSpecific(TTextBoxSettingsBean srcTextBoxSettingsBean,
			TTextBoxSettingsBean destTextBoxSettingsBean) {
		destTextBoxSettingsBean.setDefaultInteger(srcTextBoxSettingsBean.getDefaultInteger());
		destTextBoxSettingsBean.setMaxInteger(srcTextBoxSettingsBean.getMaxInteger());
		destTextBoxSettingsBean.setMinInteger(srcTextBoxSettingsBean.getMinInteger());
		destTextBoxSettingsBean.setParameterCode(srcTextBoxSettingsBean.getParameterCode());
		destTextBoxSettingsBean.setRequired(srcTextBoxSettingsBean.getRequired());
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
			TreeConfigIDTokens treeConfigIDTokens, TPersonBean personBean, Locale locale, String bundleName)  {		
		TTextBoxSettingsBean textBoxSettingsBean = getTTextBoxSettingsBeanByConfig(configID);					
		StringBuilder stringBuilder = new StringBuilder();
		if (textBoxSettingsBean!=null) {
			JSONUtility.appendIntegerValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS + "[0]." + 
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_DEFAULT_INTEGER, 
				textBoxSettingsBean.getDefaultInteger());
			JSONUtility.appendIntegerValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.MIN_VALUE, Integer.MIN_VALUE);		
			JSONUtility.appendIntegerValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS + "[0]." + 
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_MIN_INTEGER, 
				textBoxSettingsBean.getMinInteger());
			JSONUtility.appendIntegerValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.MAX_VALUE, Integer.MAX_VALUE);
			JSONUtility.appendIntegerValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS + "[0]." + 
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_MAX_INTEGER, 
				textBoxSettingsBean.getMaxInteger());
		}
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
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_DEFAULT_INTEGER_LABEL,
				LocalizeUtil.getLocalizedText("customTextBoxInteger.prompt.defaultInteger",
						locale, bundleName));
		JSONUtility.appendStringValue(stringBuilder,	
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_MIN_INTEGER_LABEL,
				LocalizeUtil.getLocalizedText("customTextBoxInteger.prompt.minValue",
						locale, bundleName));
		JSONUtility.appendStringValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_MAX_INTEGER_LABEL,
				LocalizeUtil.getLocalizedText("customTextBoxInteger.prompt.maxValue",
						locale, bundleName));
		return stringBuilder.toString(); 
	}
}
