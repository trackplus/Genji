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



package com.aurel.track.fieldType.design.custom.picker;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.aurel.track.admin.customize.role.RoleBL;
import com.aurel.track.admin.customize.treeConfig.TreeConfigIDTokens;
import com.aurel.track.admin.customize.treeConfig.field.FieldConfigJSON;
import com.aurel.track.admin.customize.treeConfig.field.GeneralSettingsBL;
import com.aurel.track.admin.user.department.DepartmentBL;
import com.aurel.track.beans.TGeneralSettingsBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.types.custom.CustomUserPicker;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.TreeNode;

/**
 * User picker field config
 * @author Tamas Ruff
 *
 */
public class UserPickerDT extends GeneralSettingsBaseDT {
	
	boolean curentUserDepartmentChecked = false;
	
	public UserPickerDT(String pluginID) {
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
				Integer.valueOf(CustomUserPicker.PARAMETERCODES.DATASOURCE_OPTION));
		singleValueParameterCodes.add(
				Integer.valueOf(CustomUserPicker.PARAMETERCODES.AUTOMAIL_OPTION));
		singleValueParameterCodes.add(
				Integer.valueOf(CustomUserPicker.PARAMETERCODES.IS_MULTIPLE_SELECT));
		return singleValueParameterCodes;
	}

	/**
	 * Get the set of those parameter codes which might have multiple values associated
	 * (typically multiple select)
	 * @return
	 */
	@Override
	protected List<Integer> getMultipleIntegerParameterCodes() {
		List<Integer> multipleValueParameterCodes = new ArrayList<Integer>();
		multipleValueParameterCodes.add(
				Integer.valueOf(CustomUserPicker.PARAMETERCODES.ROLE_OPTION));
		multipleValueParameterCodes.add(
				Integer.valueOf(CustomUserPicker.PARAMETERCODES.DEPARTMENT_OPTION));
		return multipleValueParameterCodes;
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
				CustomUserPicker.PARAMETERCODES.DATASOURCE_OPTION +"]." +
				FieldConfigJSON.JSON_FIELDS.GENERAL_SETTINGS_INTEGER,
				Integer.valueOf(CustomUserPicker.DATASOURCE_OPTIONS.ROLE));
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
		List<Integer> roles = new ArrayList<Integer>();
		List<Integer> departments = new ArrayList<Integer>();
		for (TGeneralSettingsBean generalSettingsBean : generalSettingsBeans) {
			if (generalSettingsBean.getParameterCode()!=null) {
				switch (generalSettingsBean.getParameterCode().intValue()) {
				case CustomUserPicker.PARAMETERCODES.DATASOURCE_OPTION:
					if (generalSettingsBean.getIntegerValue()==null) {
						generalSettingsBean.setIntegerValue(Integer.valueOf(CustomUserPicker.DATASOURCE_OPTIONS.ROLE));
					}
					JSONUtility.appendIntegerValue(stringBuilder,
							FieldConfigJSON.JSON_FIELDS.GENERAL_SETTINGS + "[" +
								CustomUserPicker.PARAMETERCODES.DATASOURCE_OPTION + "]." +
								FieldConfigJSON.JSON_FIELDS.GENERAL_SETTINGS_INTEGER,
								generalSettingsBean.getIntegerValue());
					break;
				case CustomUserPicker.PARAMETERCODES.ROLE_OPTION:
					roles.add(generalSettingsBean.getIntegerValue());
					break;
				case CustomUserPicker.PARAMETERCODES.DEPARTMENT_OPTION:
					departments.add(generalSettingsBean.getIntegerValue());
					break;
				case CustomUserPicker.PARAMETERCODES.AUTOMAIL_OPTION:
					JSONUtility.appendIntegerValue(stringBuilder,
							FieldConfigJSON.JSON_FIELDS.GENERAL_SETTINGS + "[" +
								CustomUserPicker.PARAMETERCODES.AUTOMAIL_OPTION + "]." +
								FieldConfigJSON.JSON_FIELDS.GENERAL_SETTINGS_INTEGER,
								generalSettingsBean.getIntegerValue());
					break;
				case CustomUserPicker.PARAMETERCODES.IS_MULTIPLE_SELECT:
					JSONUtility.appendBooleanValue(stringBuilder,
							FieldConfigJSON.JSON_FIELDS.GENERAL_SETTINGS + "[" +
								CustomUserPicker.PARAMETERCODES.IS_MULTIPLE_SELECT + "]." +
								FieldConfigJSON.JSON_FIELDS.GENERAL_SETTINGS_CHAR,
								BooleanFields.fromStringToBoolean(generalSettingsBean.getCharacterValue()));
					break;
				default:
					break;
				}
			}
		}
		curentUserDepartmentChecked = departments.contains(CustomUserPicker.CURRENT_USERS_DEPARTMENT);
		JSONUtility.appendIntegerListAsArray(stringBuilder,
			FieldConfigJSON.JSON_FIELDS.GENERAL_SETTINGS + "[" +
				CustomUserPicker.PARAMETERCODES.ROLE_OPTION + "]." +
				FieldConfigJSON.JSON_FIELDS.GENERAL_SETTINGS_INTEGER, roles);
		JSONUtility.appendIntegerListAsArray(stringBuilder,
				FieldConfigJSON.JSON_FIELDS.GENERAL_SETTINGS + "[" +
						CustomUserPicker.PARAMETERCODES.DEPARTMENT_OPTION + "]." +
						FieldConfigJSON.JSON_FIELDS.GENERAL_SETTINGS_INTEGER, departments);
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
		JSONUtility.appendStringValue(stringBuilder, "dataSourceLabel",
				LocalizeUtil.getLocalizedText("customUserPicker.prompt.option",
						locale, bundleName));
		JSONUtility.appendIntegerStringBeanList(stringBuilder, "dataSourceList",
				getDataSourceOptions(locale, bundleName));
		JSONUtility.appendStringValue(stringBuilder, "roleLabel",
				LocalizeUtil.getLocalizedText("customUserPicker.prompt.roles",
						locale, bundleName));
		JSONUtility.appendILabelBeanList(stringBuilder, "roleList", (List)RoleBL.loadVisible(locale));
		JSONUtility.appendStringValue(stringBuilder, "departmentLabel",
				LocalizeUtil.getLocalizedText("customUserPicker.prompt.department",
						locale, bundleName));
        List<TreeNode> departmentTree = DepartmentBL.getDepartmentNodesEager(null, null, true);
        //add the pseudo department for the current user
        TreeNode currentUserDepartment = DepartmentBL.createDepartmentTreeNode(CustomUserPicker.CURRENT_USERS_DEPARTMENT,
        		LocalizeUtil.getLocalizedText("customUserPicker.departmentOption.currentUser", locale, bundleName), true, curentUserDepartmentChecked);
        currentUserDepartment.setLeaf(Boolean.TRUE);
        departmentTree.add(0, currentUserDepartment);
        JSONUtility.appendJSONValue(stringBuilder, "departmentTree", JSONUtility.getTreeHierarchyJSON(departmentTree, true, false));
		JSONUtility.appendStringValue(stringBuilder, "automailLabel",
				LocalizeUtil.getLocalizedText("customUserPicker.prompt.automail",
						locale, bundleName));
		JSONUtility.appendStringValue(stringBuilder, "isMultipleSelectLabel",
				LocalizeUtil.getLocalizedText("customSelectMultiple.label",
						locale, bundleName));
		JSONUtility.appendIntegerStringBeanList(stringBuilder, "automailList",
				getAutomailOptions(locale, bundleName));
		return stringBuilder.toString();
	}


	private List<IntegerStringBean> getDataSourceOptions(Locale locale, String bundleName) {
		List<IntegerStringBean> options = new LinkedList<IntegerStringBean>();
		options.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedText("customUserPicker.datasourceOption.role", locale, bundleName), Integer.valueOf(CustomUserPicker.DATASOURCE_OPTIONS.ROLE)));
		options.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedText("customUserPicker.datasourceOption.department", locale, bundleName), Integer.valueOf(CustomUserPicker.DATASOURCE_OPTIONS.DEPARTMENT)));
		return options;
	}

	private List<IntegerStringBean> getAutomailOptions(Locale locale, String bundleName) {
		List<IntegerStringBean> options = new LinkedList<IntegerStringBean>();
		options.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedText("customUserPicker.automailOption.noMail",
						locale, bundleName), Integer.valueOf(CustomUserPicker.AUTOMAIL_OPTIONS.NOMAIL)));
		options.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.automail.trigger.lbl.originator",
						locale), Integer.valueOf(CustomUserPicker.AUTOMAIL_OPTIONS.ORIGINATOR)));
		options.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.automail.trigger.lbl.manager",
						locale), Integer.valueOf(CustomUserPicker.AUTOMAIL_OPTIONS.MANAGER)));
		options.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.automail.trigger.lbl.responsible",
						locale), Integer.valueOf(CustomUserPicker.AUTOMAIL_OPTIONS.RESPONSIBLE)));
		options.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.automail.trigger.lbl.consultant",
						locale), Integer.valueOf(CustomUserPicker.AUTOMAIL_OPTIONS.CONSULTANT)));
		options.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.automail.trigger.lbl.informant", locale),
				Integer.valueOf(CustomUserPicker.AUTOMAIL_OPTIONS.INFORMANT)));
		options.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.automail.trigger.lbl.observer", locale), Integer.valueOf(CustomUserPicker.AUTOMAIL_OPTIONS.OBSERVER)));
		return options;
	}
}
