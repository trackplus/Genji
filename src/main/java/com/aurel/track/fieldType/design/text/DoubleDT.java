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
import com.aurel.track.errors.ErrorParameter;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.numberFormatter.DoubleNumberFormatUtil;

/**
 * Custom textbox for float values for design time
 * @author Tamas Ruff
 *
 */
public class DoubleDT extends TextBaseDT {
	
	protected static int maximalDecimalDigits = 10;  
	
	
	
	public DoubleDT(Integer parameterCode, String pluginID) {
		super(parameterCode, pluginID);		
	}

	public DoubleDT(String pluginID) {
		super(pluginID);
	}

	@Override
	public List<ErrorData> isValidSettings(Map<Integer, Object> settings) {
		List<ErrorData> errorDataList = new ArrayList<ErrorData>();
		TTextBoxSettingsBean textBoxSettingsBean = (TTextBoxSettingsBean)settings.get(mapParameterCode);
		//textBoxSettingsBean can be null when no settings was set (especially by system fields)
		if (textBoxSettingsBean!=null) {
			Double defaultDouble = textBoxSettingsBean.getDefaultDouble();
			Double minDouble = textBoxSettingsBean.getMinDouble();
			Double maxDouble = textBoxSettingsBean.getMaxDouble();
			Integer maxDecimalDigits = textBoxSettingsBean.getMaxDecimalDigit();
			
			if (minDouble!=null && maxDouble!=null && 
					minDouble.doubleValue()>maxDouble.doubleValue()) {
				errorDataList.add(new ErrorData("customTextBoxDouble.error.minMax"));
			}
			if (minDouble!=null && defaultDouble!=null && 
					minDouble.doubleValue()>defaultDouble.doubleValue()) {
				errorDataList.add(new ErrorData("customTextBoxDouble.error.defaultMin"));
			}
			if (maxDouble!=null && defaultDouble!=null && 
					maxDouble.doubleValue()<defaultDouble.doubleValue()) {
				errorDataList.add(new ErrorData("customTextBoxDouble.error.defaultMax"));
			}	
			if (maxDecimalDigits!=null) 
					if (maxDecimalDigits.intValue()<0 || maxDecimalDigits.intValue()>maximalDecimalDigits) {
						errorDataList.add(new ErrorData("customTextBoxDouble.error.maxDecimalDigits", new Integer(maximalDecimalDigits)));						
					} else {
						if (getNumberOfDecimalDigits(defaultDouble)>maxDecimalDigits.intValue()) {
							errorDataList.add(createDecimalDigitsMoreThanMaxDecimalDigitsErrorData("customTextBoxDouble.prompt.defaultDouble", maxDecimalDigits));						
						}
						if (getNumberOfDecimalDigits(minDouble)>maxDecimalDigits.intValue()) {
							errorDataList.add(createDecimalDigitsMoreThanMaxDecimalDigitsErrorData("customTextBoxDouble.prompt.minValue", maxDecimalDigits));																					
						}
						if (getNumberOfDecimalDigits(maxDouble)>maxDecimalDigits.intValue()) {
							errorDataList.add(createDecimalDigitsMoreThanMaxDecimalDigitsErrorData("customTextBoxDouble.prompt.maxValue", maxDecimalDigits));						
						}
					}
			
		}
		return errorDataList;
	}
	
	/**
	 * Gets the number of decimal digits by preparing a DecimalFormat with a maximum allowed decimal digits (in this case 10).
	 * (Using the java.util.Double toString(double d) works only within a limited range of doubles!)
	 * @param value
	 * @return
	 */
	private int getNumberOfDecimalDigits(Double value) {
		//the English standard format because it has "." as decimal separator.
		//it would be more correct to use the DecimalFormatSymbols
		DoubleNumberFormatUtil doubleNumberFormatUtil = DoubleNumberFormatUtil.getInstance();
		if (value==null) {
			return 0;
		}		
		int lastOccurence = 0;
		String stringValue = doubleNumberFormatUtil.formatGUI(value, null) ;
		lastOccurence = stringValue.lastIndexOf(".");
		if (lastOccurence!=-1) {
			return stringValue.substring(lastOccurence).length()-1;
		}
		return 0;
	}
	
	/**
	 * Create the ErrorData when one of the values has more decimal digits as the one specified in maximal decimal digits field
	 * @param fieldIdentifierKey
	 * @return
	 */
	private ErrorData createDecimalDigitsMoreThanMaxDecimalDigitsErrorData(String fieldIdentifierKey, Integer maxDecimalDigits) {
		List<ErrorParameter> errorParameters = new ArrayList<ErrorParameter>();
		errorParameters.add(new ErrorParameter(true, fieldIdentifierKey));
		errorParameters.add(new ErrorParameter(maxDecimalDigits));
		return new ErrorData("customTextBoxDouble.error.decimalDigitsMoreThanMaxDecimalDigits", errorParameters);
	}

	@Override
	public void copySettingsSpecific(TTextBoxSettingsBean srcTextBoxSettingsBean,
			TTextBoxSettingsBean destTextBoxSettingsBean) {
		destTextBoxSettingsBean.setDefaultDouble(srcTextBoxSettingsBean.getDefaultDouble());
		destTextBoxSettingsBean.setMaxDecimalDigit(srcTextBoxSettingsBean.getMaxDecimalDigit());
		destTextBoxSettingsBean.setMaxDouble(srcTextBoxSettingsBean.getMaxDouble());
		destTextBoxSettingsBean.setMinDouble(srcTextBoxSettingsBean.getMinDouble());
		destTextBoxSettingsBean.setParameterCode(srcTextBoxSettingsBean.getParameterCode());
		destTextBoxSettingsBean.setRequired(srcTextBoxSettingsBean.getRequired());
	}

	/**
	 * @return the maximalDecimalDigits
	 */
	public static int getMaximalDecimalDigits() {
		return maximalDecimalDigits;
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
			JSONUtility.appendDoubleValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS + "[0]." + 
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_DEFAULT_DOUBLE, 
				textBoxSettingsBean.getDefaultDouble());
			JSONUtility.appendDoubleValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS + "[0]." + 
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_MIN_DOUBLE, 
				textBoxSettingsBean.getMinDouble());
			JSONUtility.appendDoubleValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS + "[0]." + 
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_MAX_DOUBLE, 
				textBoxSettingsBean.getMaxDouble());
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
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_DEFAULT_DOUBLE_LABEL,
				LocalizeUtil.getLocalizedText("customTextBoxDouble.prompt.defaultDouble",
						locale, bundleName));
		JSONUtility.appendStringValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_MIN_DOUBLE_LABEL,
				LocalizeUtil.getLocalizedText("customTextBoxDouble.prompt.minValue",
						locale, bundleName));
		JSONUtility.appendStringValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_MAX_DOUBLE_LABEL,
				LocalizeUtil.getLocalizedText("customTextBoxDouble.prompt.maxValue",
						locale, bundleName));
		return stringBuilder.toString(); 
	}
}
