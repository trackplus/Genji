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



package com.aurel.track.fieldType.design.custom.picker;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.category.CategoryBL;
import com.aurel.track.admin.customize.category.CategoryPickerBL;
import com.aurel.track.admin.customize.treeConfig.TreeConfigIDTokens;
import com.aurel.track.admin.customize.treeConfig.field.FieldConfigJSON;
import com.aurel.track.admin.customize.treeConfig.field.GeneralSettingsBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TGeneralSettingsBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.types.custom.CustomItemPicker;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.TreeNode;

/**
 * User picker field config
 * @author Tamas Ruff
 *
 */
public class ItemPickerDT extends GeneralSettingsBaseDT {

	public ItemPickerDT(String pluginID) {
		super(pluginID);
	}

	/**
	 * Get the set of those parameter codes which have a single value associated
	 * @return
	 */
	@Override
	protected List<Integer> getGeneralSettingsParameterCodes() {
		List<Integer> singleValueParameterCodes = new ArrayList<Integer>();
		singleValueParameterCodes.add(
				Integer.valueOf(CustomItemPicker.PARAMETERCODES.DATASOURCE_OPTION));
		singleValueParameterCodes.add(
				Integer.valueOf(CustomItemPicker.PARAMETERCODES.PROJECT_RELEASE));
		singleValueParameterCodes.add(
				Integer.valueOf(CustomItemPicker.PARAMETERCODES.FILTER));
		singleValueParameterCodes.add(
				Integer.valueOf(CustomItemPicker.PARAMETERCODES.INCLUDE_CLOSED));
		return singleValueParameterCodes;
	}
	
	/**
	 * Get the set of those parameter codes which might have multiple values associated
	 * (typically multiple select)
	 * @return
	 */	
	@Override
	protected List<Integer> getMultipleIntegerParameterCodes() {
		return null;
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
				FieldConfigJSON.JSON_FIELDS.GENERAL_SETTINGS + "[" +
				CustomItemPicker.PARAMETERCODES.DATASOURCE_OPTION +"]." +
				FieldConfigJSON.JSON_FIELDS.GENERAL_SETTINGS_INTEGER,
				Integer.valueOf(CustomItemPicker.DATASOURCE_OPTIONS.PROJECT_RELEASE));
		stringBuilder.append(getDatasource(personBean, locale));
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
		List<TGeneralSettingsBean> generalSettingsBeans = GeneralSettingsBL.loadByConfig(configID);
		StringBuilder stringBuilder = new StringBuilder();
		Integer projectReleaseID = null;
		Integer filterID = null;
		boolean includeClosed = false;
		for (TGeneralSettingsBean generalSettingsBean : generalSettingsBeans) {
			if (generalSettingsBean.getParameterCode()!=null) {
				switch (generalSettingsBean.getParameterCode().intValue()) {
				case CustomItemPicker.PARAMETERCODES.DATASOURCE_OPTION:
					if (generalSettingsBean.getIntegerValue()==null) {
						generalSettingsBean.setIntegerValue(Integer.valueOf(CustomItemPicker.DATASOURCE_OPTIONS.PROJECT_RELEASE));
					}
					JSONUtility.appendIntegerValue(stringBuilder,
							FieldConfigJSON.JSON_FIELDS.GENERAL_SETTINGS + "[" + 
									CustomItemPicker.PARAMETERCODES.DATASOURCE_OPTION + "]." +
								FieldConfigJSON.JSON_FIELDS.GENERAL_SETTINGS_INTEGER,
								generalSettingsBean.getIntegerValue());
					break;
				case CustomItemPicker.PARAMETERCODES.PROJECT_RELEASE:
					projectReleaseID = generalSettingsBean.getIntegerValue();
					break;
				case CustomItemPicker.PARAMETERCODES.FILTER:
					filterID = generalSettingsBean.getIntegerValue();
					break;
				case CustomItemPicker.PARAMETERCODES.INCLUDE_CLOSED:
					includeClosed = BooleanFields.fromStringToBoolean(generalSettingsBean.getCharacterValue());
					
					break;	
				}
			}
		}
		JSONUtility.appendIntegerValue(stringBuilder,
			FieldConfigJSON.JSON_FIELDS.GENERAL_SETTINGS + "[" + 
					CustomItemPicker.PARAMETERCODES.PROJECT_RELEASE + "]." +
				FieldConfigJSON.JSON_FIELDS.GENERAL_SETTINGS_INTEGER, projectReleaseID);
		
		JSONUtility.appendBooleanValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.GENERAL_SETTINGS + "[" + 
					CustomItemPicker.PARAMETERCODES.INCLUDE_CLOSED + "]." +
					FieldConfigJSON.JSON_FIELDS.GENERAL_SETTINGS_CHAR,
					includeClosed);
		
		JSONUtility.appendIntegerValue(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.GENERAL_SETTINGS + "[" +
						CustomItemPicker.PARAMETERCODES.FILTER + "]." +
						FieldConfigJSON.JSON_FIELDS.GENERAL_SETTINGS_INTEGER, filterID);
		stringBuilder.append(getDatasource(personBean, locale));
		return stringBuilder.append(getLocalizationJSON(locale, bundleName)).toString(); 
	}
	
	/**
	 * Adds the datasource for project/release and filter
	 * @param personBean
	 * @param locale
	 * @return
	 */
	protected String getDatasource(TPersonBean personBean, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder();
		List<TreeNode> projectReleaseTree = ProjectBL.getProjectNodesByRightEager(false, personBean, true, new int[] {AccessBeans.AccessFlagIndexes.PROJECTADMIN}, false, true);
		JSONUtility.appendJSONValue(stringBuilder, "projectReleaseTree", JSONUtility.getTreeHierarchyJSON(projectReleaseTree, false, true));
		List<TreeNode> filterTree = CategoryPickerBL.getPickerNodesEager(
                personBean, true, false, null,
                false, null, null, locale, CategoryBL.CATEGORY_TYPE.ISSUE_FILTER_CATEGORY);
		JSONUtility.appendJSONValue(stringBuilder, "filterTree", JSONUtility.getTreeHierarchyJSON(filterTree, false, true));
		return stringBuilder.toString();
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
		JSONUtility.appendStringValue(stringBuilder, "dataSourceLabel",
				LocalizeUtil.getLocalizedText("customItemPicker.prompt.datasource",
						locale, bundleName));
		JSONUtility.appendIntegerStringBeanList(stringBuilder, "dataSourceList",
				getDataSourceOptions(locale, bundleName));
		JSONUtility.appendStringValue(stringBuilder, "projectReleaseLabel",
				LocalizeUtil.getLocalizedText("customItemPicker.prompt.projectRelease",
						locale, bundleName));
		JSONUtility.appendStringValue(stringBuilder, "includeClosedLabel",
				LocalizeUtil.getLocalizedText("customItemPicker.prompt.includeClosed",
						locale, bundleName));
		JSONUtility.appendStringValue(stringBuilder, "filterLabel",
				LocalizeUtil.getLocalizedText("customItemPicker.prompt.filter",
						locale, bundleName));
		JSONUtility.appendStringValue(stringBuilder, "includeClosedLabel",
				LocalizeUtil.getLocalizedText("customItemPicker.prompt.includeClosed",
						locale, bundleName));
		return stringBuilder.toString();
	}
	
	/**
	 * Gets the localized datasource options
	 * @param locale
	 * @param bundleName
	 * @return
	 */
	private List<IntegerStringBean> getDataSourceOptions(Locale locale, String bundleName) {
		List<IntegerStringBean> options = new LinkedList<IntegerStringBean>();		
		options.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedText("customItemPicker.datasourceOption.projectRelease", locale, bundleName), Integer.valueOf(CustomItemPicker.DATASOURCE_OPTIONS.PROJECT_RELEASE)));
		options.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedText("customItemPicker.datasourceOption.filter", locale, bundleName), Integer.valueOf(CustomItemPicker.DATASOURCE_OPTIONS.FILTER)));		
		return options;
	}

}
