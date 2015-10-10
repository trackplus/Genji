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
 * Textbox for text values for design time
 * @author Tamas Ruff
 *
 */
public class TextDT extends TextBaseDT {
	
		
	public TextDT(Integer parameterCode, String pluginID) {
		super(parameterCode, pluginID);
	}

	public TextDT(String pluginID) {
		super(pluginID);
	}

	@Override
	public List<ErrorData> isValidSettings(Map<Integer, Object> settings) {
		List<ErrorData> errorDataList = new ArrayList<ErrorData>();
		TTextBoxSettingsBean textBoxSettingsBean = (TTextBoxSettingsBean)settings.get(mapParameterCode);
		//textBoxSettingsBean can be null when no settings was set (especially by system fields)
		if (textBoxSettingsBean!=null) {
			String defaultText = textBoxSettingsBean.getDefaultText();
			Integer minTextLength = textBoxSettingsBean.getMinTextLength();
			Integer maxTextLength = textBoxSettingsBean.getMaxTextLength();
			if (minTextLength!=null && maxTextLength!=null && 
					minTextLength.intValue()>maxTextLength.intValue()) {
				errorDataList.add(new ErrorData("customTextBoxText.error.minMaxLength"));
			}
			if (minTextLength!=null && defaultText!=null && !"".equals(defaultText.trim()) && 
					minTextLength.intValue()>defaultText.length()) {
				errorDataList.add(new ErrorData("customTextBoxText.error.defaultMinLength"));
			}
			if (maxTextLength!=null && defaultText!=null && !"".equals(defaultText.trim()) &&
					maxTextLength.intValue()<defaultText.length()) {
				errorDataList.add(new ErrorData("customTextBoxText.error.defaultMaxLength"));
			}
		}
		return errorDataList;
	}
		

	@Override
	public void copySettingsSpecific(TTextBoxSettingsBean srcTextBoxSettingsBean,
			TTextBoxSettingsBean destTextBoxSettingsBean) {
		//FieldBeansHelper.copyTextBoxTextProperties(srcTextBoxSetting, destTextBoxSetting);
		destTextBoxSettingsBean.setDefaultText(srcTextBoxSettingsBean.getDefaultText());
		destTextBoxSettingsBean.setMaxTextLength(srcTextBoxSettingsBean.getMaxTextLength());
		destTextBoxSettingsBean.setMinTextLength(srcTextBoxSettingsBean.getMinTextLength());
		destTextBoxSettingsBean.setParameterCode(srcTextBoxSettingsBean.getParameterCode());
		destTextBoxSettingsBean.setRequired(srcTextBoxSettingsBean.getRequired());
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
		JSONUtility.appendIntegerValue(stringBuilder,
			FieldConfigJSON.JSON_FIELDS.MIN_VALUE, 0);
		JSONUtility.appendIntegerValue(stringBuilder,
			FieldConfigJSON.JSON_FIELDS.MAX_VALUE, getMaxValue());
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
		TTextBoxSettingsBean textBoxSettingsBean = getTTextBoxSettingsBeanByConfig(configID);
		StringBuilder stringBuilder = new StringBuilder();
		if (textBoxSettingsBean!=null) {
			JSONUtility.appendStringValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS + "[0]." + 
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_DEFAULT_TEXT, 
				textBoxSettingsBean.getDefaultText());
			JSONUtility.appendIntegerValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS + "[0]." + 
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_MIN_TEXT_LENGTH, 
				textBoxSettingsBean.getMinTextLength());
			JSONUtility.appendIntegerValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS + "[0]." + 
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_MAX_TEXT_LENGTH, 
				textBoxSettingsBean.getMaxTextLength());
		}
		return stringBuilder.append(getDefaultSettingsJSON(personBean, locale, bundleName)).toString(); 
	}
	
	protected int getMaxValue() {
		return 255;
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
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_DEFAULT_TEXT_LABEL,
				LocalizeUtil.getLocalizedText("customTextBoxText.prompt.defaultText",
						locale, bundleName));
		JSONUtility.appendStringValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_MIN_TEXT_LENGTH_LABEL,
				LocalizeUtil.getLocalizedText("customTextBoxText.prompt.minLength",
						locale, bundleName));
		JSONUtility.appendStringValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_MAX_TEXT_LENGTH_LABEL,
				LocalizeUtil.getLocalizedText("customTextBoxText.prompt.maxLength",
						locale, bundleName));
		return stringBuilder.toString(); 
	}
	
}
