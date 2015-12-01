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

package com.aurel.track.admin.customize.role;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import com.aurel.track.admin.customize.treeConfig.field.FieldConfigBL;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TReportLayoutBean;
import com.aurel.track.beans.TRoleFieldBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.RoleFieldDAO;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.IntegerStringBean;


/**
 */
public class FieldsRestrictionsToRoleBL {
	
	private static RoleFieldDAO roleFieldDAO = DAOFactory.getFactory().getRoleFieldDAO();
	
	public interface PSEUDO_COLUMNS {
		public static final int ATTACHMENTS = TReportLayoutBean.PSEUDO_COLUMNS.ATTACHMENT_SYMBOL;
		//restriction field for watchers (both consulted and informed) 
		public static final int WATCHERS = TReportLayoutBean.PSEUDO_COLUMNS.CONSULTANT_LIST;
		//restriction field for my expenses (both work and cost) 
		public static final int MY_EXPENSES = TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_TIME;
		//restriction field for all expenses (both work and cost)
		public static final int ALL_EXPENSES = TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_TIME;
		//restriction field for total budget (both work and cost)
		public static final int PLAN = TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_TIME;
		
		public static final int BUDGET = TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_TIME;
	}
	
	/**
	 * Get the restricted fields by role
	 * @param roleID
	 * @return
	 */
	static List<TRoleFieldBean> getByRole(Integer roleID) {
		return roleFieldDAO.getByRole(roleID);
	}
	
	static void saveBean(TRoleFieldBean roleFieldBean) { 
		roleFieldDAO.save(roleFieldBean);
	}
	
	public static void save(TRoleFieldBean roleFieldBean) {
		roleFieldDAO.save(roleFieldBean);
	}
	
	/**
	 * Gets the pseudo fields which might be restricted
	 * @param locale
	 * @return
	 */
	private static List<FieldForRoleBean> getPseudoFields(Locale locale, boolean all) {
		List<FieldForRoleBean> pseudoFields = new LinkedList<FieldForRoleBean>();
		pseudoFields.add(new FieldForRoleBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
				"admin.customize.role.fieldsRestrictions.field.attachment", locale),
				PSEUDO_COLUMNS.ATTACHMENTS));
		pseudoFields.add(new FieldForRoleBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
				"admin.customize.role.fieldsRestrictions.field.watchers", locale),
				PSEUDO_COLUMNS.WATCHERS));
		FieldForRoleBean fieldForRoleBean = new FieldForRoleBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
				"admin.customize.role.fieldsRestrictions.field.allExpenses", locale),
				PSEUDO_COLUMNS.ALL_EXPENSES);
		fieldForRoleBean.setForcedReadOnly(true);
		pseudoFields.add(fieldForRoleBean);
		pseudoFields.add(new FieldForRoleBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
				"admin.customize.role.fieldsRestrictions.field.myExpenses", locale),
				PSEUDO_COLUMNS.MY_EXPENSES));
		pseudoFields.add(new FieldForRoleBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
				"admin.customize.role.fieldsRestrictions.field.plan", locale),
				PSEUDO_COLUMNS.PLAN));
		if (all || ApplicationBean.getInstance().getBudgetActive()) {
			pseudoFields.add(new FieldForRoleBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
				"admin.customize.role.fieldsRestrictions.field.budget", locale),
				PSEUDO_COLUMNS.BUDGET));
		}
		return pseudoFields;
	}
	
	/**
	 * Gets the restricted fields for a role
	 * @param roleID
	 * @param locale
	 * @return
	 */
	static List<FieldForRoleBean> getFieldsForRole(Integer roleID, Locale locale) {
		List<FieldForRoleBean> fieldForRoleBeanList = new LinkedList<FieldForRoleBean>();
		//get all existing field restrictions for role
		List<TRoleFieldBean> fieldsForRole = getByRole(roleID);
		Map<Integer, TRoleFieldBean> fieldsToAccessFlags = new HashMap<Integer, TRoleFieldBean>();
		for (TRoleFieldBean roleFieldBean : fieldsForRole) {
			fieldsToAccessFlags.put(roleFieldBean.getFieldKey(), roleFieldBean);
		}
		//system/custom fields
		List<TFieldConfigBean> fieldConfigsList = FieldRuntimeBL.getLocalizedDefaultFieldConfigs(locale);
		for (TFieldConfigBean fieldConfigBean : fieldConfigsList) {
			Integer fieldID = fieldConfigBean.getField();
			if (!SystemFields.INTEGER_PROJECT_SPECIFIC_ISSUENO.equals(fieldID)) {
				IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
				if (fieldTypeRT!=null) {
					FieldForRoleBean fieldForRoleBean = new FieldForRoleBean(fieldConfigBean.getLabel(), fieldID);
					fieldForRoleBean.setForcedReadOnly(fieldTypeRT.isReadOnly() ||
							fieldID.equals(SystemFields.INTEGER_PROJECT) || fieldID.equals(SystemFields.INTEGER_ISSUETYPE));
					TRoleFieldBean roleFieldBean = fieldsToAccessFlags.get(fieldID);
					if (roleFieldBean!=null) {
						setAccessRight(fieldForRoleBean, roleFieldBean.getAccessRight(), roleFieldBean.getObjectID());
					}
					fieldForRoleBeanList.add(fieldForRoleBean);
				}
			}
		}
		//pseudo fields
		List<FieldForRoleBean> pseudoFields = getPseudoFields(locale, false);
		for (FieldForRoleBean fieldForRoleBean : pseudoFields) {
			TRoleFieldBean roleFieldBean = fieldsToAccessFlags.get(fieldForRoleBean.getFieldID());
			if (roleFieldBean!=null) {
				setAccessRight(fieldForRoleBean, roleFieldBean.getAccessRight(), roleFieldBean.getObjectID());
			}
			fieldForRoleBeanList.add(fieldForRoleBean);
		}
		Collections.sort(fieldForRoleBeanList);
		return fieldForRoleBeanList;
	}
	
	private static void setAccessRight(FieldForRoleBean fieldForRoleBean, Integer accessRight, Integer objectID) {
		fieldForRoleBean.setObjectID(objectID);
		if (accessRight!=null) {
			if (accessRight==TRoleFieldBean.ACCESSFLAG.NOACCESS) {
				fieldForRoleBean.setHidden(true);
			} else {
				if (accessRight.intValue()==TRoleFieldBean.ACCESSFLAG.READ_ONLY) {
					fieldForRoleBean.setHidden(false);
					fieldForRoleBean.setConfiguredReadOnly(true);
				}
			}
		}
	}
	
	private static Integer getAccessRight(boolean hidden, boolean configuredReadOnly) {
		if (hidden) {
			return TRoleFieldBean.ACCESSFLAG.NOACCESS;
		} else {
			if (configuredReadOnly) {
				return TRoleFieldBean.ACCESSFLAG.READ_ONLY;
			} else {
				return TRoleFieldBean.ACCESSFLAG.READ_WRITE;
			}
		}
	}
	
	/**
	 * Saves the notify trigger
	 * @param roleID
	 * @param label
	 * @param copy
	 * @param personBean
	 * @param records
	 */
	static void saveFieldRestrictions(Integer roleID, String records) {
		List<JSONObject> modifiedRows = JSONUtility.decodeToJsonRecordsList(records);
		List<Integer> changedRowsIDs = new ArrayList<Integer>();
		for (JSONObject jsonObject : modifiedRows) {
			if (jsonObject.has(JSONUtility.JSON_FIELDS.OBJECT_ID)) {
				Integer objectID = jsonObject.getInt(JSONUtility.JSON_FIELDS.OBJECT_ID);
				if (objectID!=null && objectID.intValue()!=0) {
					changedRowsIDs.add(objectID);
				}
			}
		}
		Map<Integer, TRoleFieldBean> oldValuesMap = new HashMap<Integer, TRoleFieldBean>();
		if (!changedRowsIDs.isEmpty()) {
			List<TRoleFieldBean> oldValuesInChangedRowIDs = roleFieldDAO.getRoleFieldsByKeys(changedRowsIDs);
			for (TRoleFieldBean roleFieldBean : oldValuesInChangedRowIDs) {
				oldValuesMap.put(roleFieldBean.getObjectID(), roleFieldBean);
			}
		}
		for (JSONObject jsonObject : modifiedRows) {
			Integer objectID = jsonObject.getInt(JSONUtility.JSON_FIELDS.OBJECT_ID);
			Integer fieldID = jsonObject.getInt(JSONUtility.JSON_FIELDS.ID);
			boolean hidden = jsonObject.getBoolean(FieldRestrictionsToRoleJSON.JSON_FIELDS.HIDDEN);
			boolean configuredReadOnly = jsonObject.getBoolean(FieldRestrictionsToRoleJSON.JSON_FIELDS.CONFIGURED_READ_ONLY);
			if (objectID!=null) {
				TRoleFieldBean oldRoleFieldBean = oldValuesMap.get(objectID);
				if (oldRoleFieldBean!=null) {
					//existing field modified
					if (hidden || configuredReadOnly) {
						oldRoleFieldBean.setAccessRight(getAccessRight(hidden, configuredReadOnly));
						save(oldRoleFieldBean);
						if (hidden) {
							//if a field is set to hidden, also set the explicit history flag in field configuration,
							//(otherwise it would be available through the common history) 
							TFieldConfigBean fieldConfigBean = FieldRuntimeBL.getDefaultFieldConfig(fieldID);
							if (fieldConfigBean!=null && !fieldConfigBean.isHistoryString()) {
								fieldConfigBean.setHistoryString(true);
								FieldConfigBL.save(fieldConfigBean);
							}
						}
					} else {
						//each flag reset: delete the existing trigger field
						roleFieldDAO.delete(objectID);
					}
				} else {
					String id = jsonObject.getString(JSONUtility.JSON_FIELDS.ID);
					if (id!=null) {
						//new restricted field
						TRoleFieldBean newNotifyFieldBean = new TRoleFieldBean();
						newNotifyFieldBean.setFieldKey(fieldID);
						newNotifyFieldBean.setRoleKey(roleID);
						newNotifyFieldBean.setAccessRight(getAccessRight(hidden, configuredReadOnly));
						save(newNotifyFieldBean);
						if (hidden) {
							//if a field is set to hidden, also set the explicit history flag in field configuration,
							//(otherwise it would be available through the common history)
							TFieldConfigBean fieldConfigBean = FieldRuntimeBL.getDefaultFieldConfig(fieldID);
							if (fieldConfigBean!=null && !fieldConfigBean.isHistoryString()) {
								fieldConfigBean.setHistoryString(true);
								FieldConfigBL.save(fieldConfigBean);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Gets all restricted fields
	 * @param locale
	 * @param hiddenFields out parameter
	 * @param readOnlyFields out parameter
	 */
	static void getRestrictedFields(Locale locale,
			Map<Integer, List<IntegerStringBean>> hiddenFields, Map<Integer, List<IntegerStringBean>> readOnlyFields){
		List<TRoleFieldBean> roleFields = roleFieldDAO.loadAll();
		if (roleFields!=null && !roleFields.isEmpty()) {
			Map<Integer, String> fieldConfigLabelsMap = LocalizeUtil.getLocalizedFieldConfigLables(
					FieldConfigBL.loadDefault(), locale);
			List<FieldForRoleBean> pseudoFields = getPseudoFields(locale, true);
			for (FieldForRoleBean fieldForRoleBean : pseudoFields) {
				fieldConfigLabelsMap.put(fieldForRoleBean.getFieldID(), fieldForRoleBean.getFieldLabel());
			}
			for (TRoleFieldBean roleFieldBean : roleFields) {
				Integer roleID = roleFieldBean.getRoleKey();
				Integer fieldID = roleFieldBean.getFieldKey();
				Integer accessRight = roleFieldBean.getAccessRight();
				if (accessRight!=null) {
					if (accessRight.equals(TRoleFieldBean.ACCESSFLAG.NOACCESS) || accessRight.equals(TRoleFieldBean.ACCESSFLAG.READ_ONLY)) {
						Map<Integer, List<IntegerStringBean>> accessMap = null;
						if (accessRight.equals(TRoleFieldBean.ACCESSFLAG.NOACCESS)) {
							accessMap = hiddenFields;
						} else {
							accessMap = readOnlyFields;
						}
						List<IntegerStringBean> fieldsForRole = accessMap.get(roleID);
						if (fieldsForRole==null) {
							fieldsForRole = new LinkedList<IntegerStringBean>();
							accessMap.put(roleID, fieldsForRole);
						}
						fieldsForRole.add(new IntegerStringBean(fieldConfigLabelsMap.get(fieldID), fieldID));
					}
				}
			}
		}
	}
	
	/**
	 * Gets the hidden and read only maps with restricted fields from all roles
	 * @param fieldIDs
	 * @param hiddenFieldsToRoles
	 * @param readOnlyFieldsToRoles
	 */
	public static void getRestrictedFieldsToRoles(List<Integer> fieldIDs,
			Map<Integer, Set<Integer>> hiddenFieldsToRoles, Map<Integer, Set<Integer>> readOnlyFieldsToRoles){
		List<TRoleFieldBean> roleFields = null;
		if (fieldIDs==null) {
			roleFields = roleFieldDAO.loadAll();
		} else {
			roleFields = roleFieldDAO.getByFields(fieldIDs);
		}
		if (roleFields!=null && !roleFields.isEmpty()) {
			for (TRoleFieldBean roleFieldBean : roleFields) {
				Integer roleID = roleFieldBean.getRoleKey();
				Integer fieldID = roleFieldBean.getFieldKey();
				Integer accessRight = roleFieldBean.getAccessRight();
				if (accessRight!=null) {
					if (accessRight.equals(TRoleFieldBean.ACCESSFLAG.NOACCESS) || accessRight.equals(TRoleFieldBean.ACCESSFLAG.READ_ONLY)) {
						Map<Integer, Set<Integer>> accessMap = null;
						if (accessRight.equals(TRoleFieldBean.ACCESSFLAG.NOACCESS)) {
							accessMap = hiddenFieldsToRoles;
						} else {
							accessMap = readOnlyFieldsToRoles;
						}
						Set<Integer> rolesForField = accessMap.get(fieldID);
						if (rolesForField==null) {
							rolesForField = new HashSet<Integer>();
							accessMap.put(fieldID, rolesForField);
						}
						rolesForField.add(roleID);
					}
				}
			}
		}
	}
}
