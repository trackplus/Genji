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
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.customize.treeConfig.TreeConfigIDTokens;
import com.aurel.track.admin.customize.treeConfig.field.FieldConfigJSON;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TTextBoxSettingsBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.DateOptions;
import com.aurel.track.fieldType.types.system.text.SystemTextBoxDate.HIERARCHICAL_BEHAVIOR_OPTIONS;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.IntegerStringBean;

/**
 * Custom textbox with datepicker for design time
 * @author Tamas Ruff
 *
 */
public abstract class DateDT extends TextBaseDT {

	public DateDT(Integer parameterCode, String pluginID) {
		super(parameterCode, pluginID);
	}

	public DateDT(String pluginID) {
		super(pluginID);
	}
	
	@Override
	public List <ErrorData> isValidSettings(Map<Integer, Object> settings) {
		List <ErrorData> errorDataList = new ArrayList<ErrorData>();
		Date now = new Date();
		TTextBoxSettingsBean textBoxSettingsBean = (TTextBoxSettingsBean)settings.get(mapParameterCode);
		//textBoxSettingsBean can be null when no settings was set (especially by system fields)
		if (textBoxSettingsBean!=null) {
			Integer defaultOption = textBoxSettingsBean.getDefaultOption();
			Integer minOption = textBoxSettingsBean.getMinOption();
			Integer maxOption = textBoxSettingsBean.getMaxOption();
			Date defaultDate = null;
			Date minDate = null;
			Date maxDate = null;
			if (defaultOption != null) {
				switch (defaultOption.intValue()) {
				case DateOptions.NOW:
					defaultDate = now;
					break;
				case DateOptions.DATE:
					defaultDate = textBoxSettingsBean.getDefaultDate();
					break;
				}
			}
			
			if (minOption != null) {
				switch (minOption.intValue()) {
				case DateOptions.NOW:
					minDate = now;
					break;
				case DateOptions.DATE:
					minDate = textBoxSettingsBean.getMinDate();
					break;
				}
			}
			
			if (maxOption != null) {
				switch (maxOption.intValue()) {
				case DateOptions.NOW:
					maxDate = now;
					break;
				case DateOptions.DATE:
					maxDate = textBoxSettingsBean.getMaxDate();
					break;
				}
			}
			
			if (minDate!=null && maxDate!=null && 
					minDate.after(maxDate)) {
				errorDataList.add(new ErrorData("customTextBoxDate.error.minMaxDate"));
			}
			if (minDate!=null && defaultDate!=null && 
					minDate.after(defaultDate)) {
				errorDataList.add(new ErrorData("customTextBoxDate.error.defaultMinDate"));
			}
			if (maxDate!=null && defaultDate!=null && 
					maxDate.before(defaultDate)) {
				errorDataList.add(new ErrorData("customTextBoxDate.error.defaultMaxDate"));
			}	
		}
		return errorDataList;
	}
	
	protected abstract String getMinMaxDateErrorKey();
	
	protected abstract String getDefaultMinDateErrorKey();
	
	protected abstract String getDefaultMaxDateErrorKey();
	
	
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
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS + "[0]." +
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_DEFAULT_OPTION,
				Integer.valueOf(DateOptions.EMPTY));
		JSONUtility.appendIntegerValue(stringBuilder, 
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS + "[0]." +
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_MIN_OPTION,
				Integer.valueOf(DateOptions.EMPTY));
		JSONUtility.appendIntegerValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS + "[0]." +
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_MAX_OPTION,
				Integer.valueOf(DateOptions.EMPTY));
		/*JSONUtility.appendIntegerValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS + "[0]." +
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_HIERARCHICAL_BEHAVIOR_OPTION,
				Integer.valueOf(HIERARCHICAL_BEHAVIOR_OPTIONS.COMPUTE_BOTTOM_UP));*/
		stringBuilder.append(getLocalizationJSON(locale, bundleName));
		return stringBuilder.toString();
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
			if (textBoxSettingsBean.getDefaultOption()==null) {
				textBoxSettingsBean.setDefaultOption(DateOptions.EMPTY);
			}
			JSONUtility.appendIntegerValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS + "[0]." + 
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_DEFAULT_OPTION, 
				textBoxSettingsBean.getDefaultOption());
			JSONUtility.appendLocaleFormattedDateValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS + "[0]." + 
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_DEFAULT_DATE, 
				textBoxSettingsBean.getDefaultDate(), locale);
			JSONUtility.appendIntegerValue(stringBuilder,
					FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS + "[0]." + 
					FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_DEFAULT_INTEGER, 
					textBoxSettingsBean.getDefaultInteger());
			if (textBoxSettingsBean.getMinOption()==null) {
				textBoxSettingsBean.setMinOption(DateOptions.EMPTY);
			}
			JSONUtility.appendIntegerValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS + "[0]." + 
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_MIN_OPTION, 
				textBoxSettingsBean.getMinOption());
			JSONUtility.appendLocaleFormattedDateValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS + "[0]." + 
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_MIN_DATE, 
				textBoxSettingsBean.getMinDate(), locale);
			if (textBoxSettingsBean.getMaxOption()==null) {
				textBoxSettingsBean.setMaxOption(DateOptions.EMPTY);
			}
			JSONUtility.appendIntegerValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS + "[0]." + 
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_MAX_OPTION, 
				textBoxSettingsBean.getMaxOption());
			JSONUtility.appendLocaleFormattedDateValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS + "[0]." + 
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_MAX_DATE, 
				textBoxSettingsBean.getMaxDate(), locale);
			/*String submitFormat = DateTimeUtils.getInstance().getExtJsTwoDigitsYearDateFormat(locale);
			JSONUtility.appendStringValue(stringBuilder,
					FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_DATE_SUBMIT_FORMAT, submitFormat);*/
			JSONUtility.appendIntegerValue(stringBuilder,
					FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS + "[0]." +
					FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_HIERARCHICAL_BEHAVIOR_OPTION,
					textBoxSettingsBean.getMinInteger());
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
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_DEFAULT_OPTION_LABEL,
				LocalizeUtil.getLocalizedText("customTextBoxDate.prompt.defaultDate",
						locale, bundleName));
		JSONUtility.appendStringValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_MIN_OPTION_LABEL,
				LocalizeUtil.getLocalizedText("customTextBoxDate.prompt.minDate",
						locale, bundleName));
		JSONUtility.appendStringValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_MAX_OPTION_LABEL,
				LocalizeUtil.getLocalizedText("customTextBoxDate.prompt.maxDate",
						locale, bundleName));
		JSONUtility.appendIntegerStringBeanList(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_DATE_OPTIONS,
				getOptions(locale, bundleName));
		JSONUtility.appendIntegerStringBeanList(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_DEFAULT_DATE_OPTIONS,
				getDefaultOptions(locale, bundleName));
		JSONUtility.appendStringValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_HIERARCHICAL_BEHAVIOR_LABEL,
				LocalizeUtil.getLocalizedText("customTextBoxDate.prompt.hierarchicalBehavior",
						locale, bundleName));
		JSONUtility.appendIntegerStringBeanList(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.TEXTBOX_SETTINGS_HIERARCHICAL_BEHAVIOR_OPTIONS,
				getHierarchicalOptions(locale, bundleName));
		return stringBuilder.toString(); 
	}
	
	/**
	 * Gets the hierarchical behavior options
	 * @param locale
	 * @param bundleName
	 * @return
	 */
	protected List <IntegerStringBean> getHierarchicalOptions(Locale locale, String bundleName) {
		List <IntegerStringBean> options = new ArrayList <IntegerStringBean>();
		options.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedText(
						"customTextBoxDate.hierarchy.option.computeBottomUp", locale, bundleName), Integer.valueOf(HIERARCHICAL_BEHAVIOR_OPTIONS.COMPUTE_BOTTOM_UP)));
		options.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedText(
						"customTextBoxDate.hierarchy.option.validate", locale, bundleName), Integer.valueOf(HIERARCHICAL_BEHAVIOR_OPTIONS.VALIDATE)));
		options.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedText(
						"customTextBoxDate.hierarchy.option.noRestriction", locale, bundleName), Integer.valueOf(HIERARCHICAL_BEHAVIOR_OPTIONS.NO_RESTRICTION)));
		return options;
	}
	
	@Override
	public void copySettingsSpecific(TTextBoxSettingsBean srcTextBoxSettingsBean,
			TTextBoxSettingsBean destTextBoxSettingsBean) {
		destTextBoxSettingsBean.setDefaultOption(srcTextBoxSettingsBean.getDefaultOption());
		destTextBoxSettingsBean.setDefaultDate(srcTextBoxSettingsBean.getDefaultDate());
		destTextBoxSettingsBean.setDefaultInteger(srcTextBoxSettingsBean.getDefaultInteger());
		destTextBoxSettingsBean.setMaxOption(srcTextBoxSettingsBean.getMaxOption());
		destTextBoxSettingsBean.setMaxDate(srcTextBoxSettingsBean.getMaxDate());
		destTextBoxSettingsBean.setMinOption(srcTextBoxSettingsBean.getMinOption());
		destTextBoxSettingsBean.setMinDate(srcTextBoxSettingsBean.getMinDate());
		destTextBoxSettingsBean.setMinInteger(srcTextBoxSettingsBean.getMinInteger());
		destTextBoxSettingsBean.setParameterCode(srcTextBoxSettingsBean.getParameterCode());
		destTextBoxSettingsBean.setRequired(srcTextBoxSettingsBean.getRequired());
	}
	
	/**
	 * Although the options are similar for custom or system dates 
	 * the labels are take from different bundles
	 * @param locale
	 * @param bundleName
	 * @return
	 */
	protected abstract List<IntegerStringBean> getOptions(Locale locale, String bundleName);

	/**
	 * Get the options for the default date
	 * @param locale
	 * @param bundleName
	 * @return
	 */
	protected abstract List<IntegerStringBean> getDefaultOptions(Locale locale, String bundleName);

}
