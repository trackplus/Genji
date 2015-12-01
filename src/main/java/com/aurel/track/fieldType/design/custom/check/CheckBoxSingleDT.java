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


package com.aurel.track.fieldType.design.custom.check;

import java.util.Locale;

import com.aurel.track.admin.customize.treeConfig.TreeConfigIDTokens;
import com.aurel.track.admin.customize.treeConfig.field.FieldConfigJSON;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TTextBoxSettingsBean;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.design.text.TextBaseDT;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;

/**
 * Single check box field for design time
 * @author Tamas Ruff
 *
 */
public class CheckBoxSingleDT extends TextBaseDT {
		
	public CheckBoxSingleDT(Integer parameterCode, String pluginID) {
		super(parameterCode, pluginID);		
	}

	public CheckBoxSingleDT(String pluginID) {
		super(pluginID);
	}

	/**
	 * Whether the "Deprecated" check box should be rendered 
	 * Typically it should be rendered when the required flag should be also rendered 
	 * @return
	 */
	@Override
	public boolean renderDeprecatedFlag() {
		return true;
	}
	
	/**
	 * Whether by configuring a system or custom field 
	 * the required checkbox should appear or not.
	 * It should not appear for the required system fields 
	 * and for the types where this doesn't makes sense: ex. lables, checkboxes
	 * Overwrite it to return false for such fields 
	 * @return
	 */
	@Override
	public boolean renderRequiredFlag() {
		return false;
	}
	
	/**
	 *  Copy textbox settings specific to check box
	 */
	@Override
	public void copySettingsSpecific(TTextBoxSettingsBean srcTextBoxSetting, TTextBoxSettingsBean destTextBoxSetting) {
		//only if there are specific source settings
		if (srcTextBoxSetting!=null) {
			destTextBoxSetting.setDefaultChar(srcTextBoxSetting.getDefaultChar());
			destTextBoxSetting.setParameterCode(srcTextBoxSetting.getParameterCode());
			destTextBoxSetting.setRequired(srcTextBoxSetting.getRequired());
		}
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
			JSONUtility.appendBooleanValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS + "[0]." + 
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_DEFAULT_CHAR, 
				BooleanFields.fromStringToBoolean(textBoxSettingsBean.getDefaultChar()));
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
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_DEFAULT_CHAR_LABEL,
				LocalizeUtil.getLocalizedText("customCheckBoxSingle.prompt.default",
						locale, bundleName));
		return stringBuilder.toString(); 
	}
}
