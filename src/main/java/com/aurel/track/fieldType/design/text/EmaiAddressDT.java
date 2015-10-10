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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Perl5Compiler;

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
public class EmaiAddressDT extends TextDT {
	protected static Logger LOGGER = LogManager.getLogger(EmaiAddressDT.class);
		
	public EmaiAddressDT(Integer parameterCode, String pluginID) {
		super(parameterCode, pluginID);
	}

	public EmaiAddressDT(String pluginID) {
		super(pluginID);
	}

	@Override
	public List<ErrorData> isValidSettings(Map<Integer, Object> settings) {
		List<ErrorData> errorDataList = new ArrayList<ErrorData>();
		TTextBoxSettingsBean textBoxSettingsBean = (TTextBoxSettingsBean)settings.get(mapParameterCode);
		//textBoxSettingsBean can be null when no settings was set (especially by system fields)
		if (textBoxSettingsBean!=null) {
			String patternString = textBoxSettingsBean.getDefaultText();
			Perl5Compiler pc = new Perl5Compiler();
			try {
				/*Perl5Pattern pattern=(Perl5Pattern)*/pc.compile(patternString,Perl5Compiler.CASE_INSENSITIVE_MASK |Perl5Compiler.SINGLELINE_MASK);
			} catch (MalformedPatternException e) {
				LOGGER.error("Malformed Email Domain Pattern " + patternString);
				errorDataList.add(new ErrorData("admin.user.profile.err.emailAddress.format"));
			}
		}
		return errorDataList;
	}
		

	@Override
	public void copySettingsSpecific(TTextBoxSettingsBean srcTextBoxSettingsBean,
			TTextBoxSettingsBean destTextBoxSettingsBean) {
		destTextBoxSettingsBean.setDefaultText(srcTextBoxSettingsBean.getDefaultText());
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
		return getLocalizationJSON(locale, bundleName); 
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
		}
		return stringBuilder.append(getDefaultSettingsJSON(personBean, locale, bundleName)).toString(); 
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
				LocalizeUtil.getLocalizedText("customTextBoxDate.prompt.emailPerlPattern",
						locale, bundleName));
		return stringBuilder.toString(); 
	}
	
}
