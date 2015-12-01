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


package com.aurel.track.admin.customize.notify.trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import com.aurel.track.admin.customize.treeConfig.field.FieldConfigBL;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TNotifyFieldBean;
import com.aurel.track.beans.TNotifyTriggerBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.NotifyFieldDAO;
import com.aurel.track.dao.NotifyTriggerDAO;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.item.budgetCost.AccountingBL.FIELDS;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.IntegerStringBean;

/**
 * Business logic for notification trigger management 
 * @author Tamas Ruff
 *
 */
public class NotifyTriggerBL {
	
	private static NotifyTriggerDAO notifyTriggerDAO = DAOFactory.getFactory().getNotifyTriggerDAO();
	private static NotifyFieldDAO notifyFieldDAO = DAOFactory.getFactory().getNotifyFieldDAO();
	private static String KEY_SPLITTER = "_";
	/**
	 * Defines the possible action types on an issue
	 * Several actions can result in the same action type 
	 * (especially the modify actions)
	 * @author Tamas Ruff
	 *
	 */
	public static interface ACTIONTYPE {
		public static final int CREATE_ISSUE = 1;
		public static final int EDIT_ISSUE = 2;
		public static final int DELETE_ISSUE = 3; //not yet used
	}

	/**
	 * Defines the meaning of the "Field" field,
	 * relevant only for ACTIONTYPE = EDIT_ISSUE to avoid collision 
	 * between the issue fieldIDs and budget/cost fieldIDs 
	 * @author Tamas Ruff
	 *
	 */
	public static interface FIELDTYPE {
		public static final int ISSUE_FIELD = 1;
		public static final int PLANNED_VALUE_FIELD = 2;
		public static final int REMAINING_PLAN_FIELD = 3;
		public static final int EXPENSE_FIELD = 4;
		public static final int BUDGET_FIELD = 5;
		public static final int ATTACHMENT_FIELD = 6;
		public static final int WATCHER_FIELD = 7;
	}

	/**
	 * The map keys for different maps (edit) and TNotifyFieldBean (create) 
	 * @author Tamas Ruff
	 *
	 */
	/*static interface FIELD_MAP {
		public static final int EDIT_ISSUE_FIELD_MAP = 1;
		public static final int EDIT_BUDGET_FIELD_MAP = 2;
		public static final int EDIT_REMAINING_BUDGET_FIELD_MAP = 3;
		public static final int EDIT_EXPENSE_FIELD_MAP = 4;
		public static final int CREATE_ISSUE_FIELD_BEAN = 5;
		public static final int ADD_COMMENT_BEAN = 6;
		public static final int ATTACHMENT_BEAN = 7;
	}*/

	/**
	 * Defines symbolic constants for the raci roles defined in the TNotifyField table 
	 * @author Tamas Ruff
	 *
	 */
	public static interface RACI_ROLES {
		public static final int ORIGINATOR = Integer.valueOf(1);
		public static final int MANAGER = Integer.valueOf(2);
		public static final int RESPONSIBLE = Integer.valueOf(3);
		public static final int CONSULTANT = Integer.valueOf(4);
		public static final int INFORMANT = Integer.valueOf(5);
		public static final int OBSERVER = Integer.valueOf(6);
	}
			
	enum LOCALE_KEYS {
		ERROR_DELETE_NOT_ALLOWED("admin.customize.automail.trigger.err.deleteNotAllowed"),//You have no right to delete this trigger.		
		PROMPT("admin.customize.automail.trigger.lblReplace"),//=notification trigger
		PROMPT_ACTIONTYPE_CREATE_ISSUE("admin.customize.automail.trigger.lbl.actionType.createIssue"),//=Create Issue
		PROMPT_ACTIONTYPE_EDIT_ISSUE("admin.customize.automail.trigger.lbl.actionType.editIssue"),//=Edit Issue
		PROMPT_CONSULTED("admin.customize.automail.trigger.lbl.consultant"),//=Consulted
		PROMPT_FIELD("admin.customize.automail.trigger.lbl.field"),//=Field
		PROMPT_FIELDTYPE("admin.customize.automail.trigger.lbl.fieldType"),//=Field type
		PROMPT_FIELDTYPE_ATTACHMENT("admin.customize.automail.trigger.lbl.fieldType.attachment"),//=Attachment
		PROMPT_FIELDTYPE_WATCHER("admin.customize.automail.trigger.lbl.fieldType.watcher"),//add me as watcher
		PROMPT_FIELDTYPE_BUDGET_FIELDS("admin.customize.automail.trigger.lbl.fieldType.budgetFields"),//=Planned value fields
		PROMPT_FIELDTYPE_PLANNED_VALUE_FIELDS("admin.customize.automail.trigger.lbl.fieldType.plannedValueFields"),//=Planned value fields
		PROMPT_FIELDTYPE_EXPENSE_FIELDS("admin.customize.automail.trigger.lbl.fieldType.expenseFields"),//=Expense fields
		PROMPT_FIELDTYPE_ISSUE_FIELDS("admin.customize.automail.trigger.lbl.fieldType.issueFields"),//=Issue fields
		PROMPT_FIELDTYPE_REMAINING_BUDGET_FIELDS("admin.customize.automail.trigger.lbl.fieldType.remainingBudgetFields"),//=Remaining PV fields
		PROMPT_INFORMED("admin.customize.automail.trigger.lbl.informant"),//=Informed
		PROMPT_OBSERVER("admin.customize.automail.trigger.lbl.observer"),//=Observer
		PROMPT_TYPE("admin.customize.automail.trigger.lbl.type"),//=Type
		PROMPT_OWN("admin.customize.automail.trigger.lbl.type.own"),//=Own
		PROMPT_SYSTEM("admin.customize.automail.trigger.lbl.type.system"),
		PROMPT_REPLACEMENT_REQUIRED("common.err.replacementRequired");
		
		private String key;
		LOCALE_KEYS (String key) {
			this.key = key;
		}
		
		public String getKey() {
			return key;
		}
	}
	
	/**
	 * Gets the localized action type
	 * @param actionType
	 * @param locale
	 * @return
	 */
	private static String getActionTypeLabel(Integer actionType, Locale locale) {
		if (actionType==null) {
			return "";
		}
		switch (actionType.intValue()) {
		case ACTIONTYPE.CREATE_ISSUE:
			return LocalizeUtil.getLocalizedTextFromApplicationResources(LOCALE_KEYS.PROMPT_ACTIONTYPE_CREATE_ISSUE.getKey(), locale);
		case ACTIONTYPE.EDIT_ISSUE:	
			return 	LocalizeUtil.getLocalizedTextFromApplicationResources(LOCALE_KEYS.PROMPT_ACTIONTYPE_EDIT_ISSUE.getKey(), locale);
		}
		return "";
	}
	
	/**
	 * Gets the localized field type
	 * @param fieldType
	 * @param locale
	 * @return
	 */
	private static String getFieldTypeLabel(Integer fieldType, Locale locale) {
		if (fieldType==null) {
			return "";
		}
		switch (fieldType.intValue()) {
		case FIELDTYPE.ISSUE_FIELD:
			return LocalizeUtil.getLocalizedTextFromApplicationResources(LOCALE_KEYS.PROMPT_FIELDTYPE_ISSUE_FIELDS.getKey(), locale);
		case FIELDTYPE.BUDGET_FIELD:
			return LocalizeUtil.getLocalizedTextFromApplicationResources(LOCALE_KEYS.PROMPT_FIELDTYPE_BUDGET_FIELDS.getKey(), locale);
		case FIELDTYPE.PLANNED_VALUE_FIELD:
			return LocalizeUtil.getLocalizedTextFromApplicationResources(LOCALE_KEYS.PROMPT_FIELDTYPE_PLANNED_VALUE_FIELDS.getKey(), locale);
		case FIELDTYPE.EXPENSE_FIELD:
			return LocalizeUtil.getLocalizedTextFromApplicationResources(LOCALE_KEYS.PROMPT_FIELDTYPE_EXPENSE_FIELDS.getKey(), locale);
		case FIELDTYPE.REMAINING_PLAN_FIELD:
			return LocalizeUtil.getLocalizedTextFromApplicationResources(LOCALE_KEYS.PROMPT_FIELDTYPE_REMAINING_BUDGET_FIELDS.getKey(), locale);
		/*case FIELDTYPE.ATTACHMENT_FIELD:
			return LocalizeUtil.getLocalizedTextFromApplicationResources(LOCALE_KEYS.PROMPT_FIELDTYPE_ATTACHMENT.getKey(), locale);*/
		case FIELDTYPE.WATCHER_FIELD:
			return LocalizeUtil.getLocalizedTextFromApplicationResources(LOCALE_KEYS.PROMPT_FIELDTYPE_WATCHER.getKey(), locale);
		}
		return "";
	}
	
	/**
	 * Encode a trigger field
	 * @param notifyTriggerFieldTokens
	 * @return
	 */
	static String encodeTriggerField(NotifyTriggerFieldTokens notifyTriggerFieldTokens){
		StringBuffer stringBuffer = new StringBuffer();
		Integer actionType = notifyTriggerFieldTokens.getActionType();
		stringBuffer.append(actionType);
		Integer fieldType = notifyTriggerFieldTokens.getFieldType();
		if (fieldType!=null) {
			stringBuffer.append(KEY_SPLITTER);
			stringBuffer.append(fieldType);
			Integer fieldID = notifyTriggerFieldTokens.getFieldID();
			if (fieldID!=null) {
				stringBuffer.append(KEY_SPLITTER);
				stringBuffer.append(fieldID);
			}
		}
		return stringBuffer.toString();
	}
	
	/**
	 * Decode a trigger field
	 * @param id
	 * @return
	 */
	static NotifyTriggerFieldTokens decodeTriggerField(String id){
		NotifyTriggerFieldTokens notifyTriggerFieldTokens = new NotifyTriggerFieldTokens();
		if (id!=null) {
			String[] tokens = id.split(KEY_SPLITTER);
			if (tokens!=null && tokens.length>0) {
				Integer actionType = Integer.valueOf(tokens[0]);
				notifyTriggerFieldTokens.setActionType(actionType);
				if (tokens.length>1) {
					if (tokens[1]!=null && !"".equals(tokens[1])) {
						notifyTriggerFieldTokens.setFieldType(Integer.valueOf(tokens[1]));
					}
					if (tokens.length>2) {
						if (tokens[2]!=null && !"".equals(tokens[2])) {
							notifyTriggerFieldTokens.setFieldID(Integer.valueOf(tokens[2]));
						}
					}
				}
			}
		}
		return notifyTriggerFieldTokens;
	}
	
	
	
	/**
	 * Loads all notifyTriggerBeans defined by a person or by the system admin
	 * @param personID
	 * @return
	 */
	static List<TNotifyTriggerBean> loadNotifyTriggerBeans(boolean defaultSettings, TPersonBean personBean, Locale locale) {
		List<TNotifyTriggerBean> triggersList = loadNotifyTriggerBeans(personBean.getObjectID(), !defaultSettings);
		for (TNotifyTriggerBean notifyTriggerBean : triggersList) {
			String key;
			if (BooleanFields.TRUE_VALUE.equals(notifyTriggerBean.getIsSystem())) {
				key = LOCALE_KEYS.PROMPT_SYSTEM.getKey();
			} else {
				key =  LOCALE_KEYS.PROMPT_OWN.getKey();
			}
			notifyTriggerBean.setTypeLabel(LocalizeUtil.getLocalizedTextFromApplicationResources(key, locale));
			if (notifyTriggerBean.getPerson().equals(personBean.getObjectID()) || personBean.isSys()) {
				notifyTriggerBean.setOwn(true);
			} else {
				notifyTriggerBean.setOwn(false);
			}
		}
		return triggersList;
	}
	
	/**
	 * Loads all notifyTriggerBeans defined by a person or by the system admin
	 * @param personID
	 * @return
	 */
	public static List<TNotifyTriggerBean> loadNotifyTriggerBeans(Integer personID, boolean includeOwn) {
		return notifyTriggerDAO.loadSystemAndOwn(personID, includeOwn);
	} 
	
	/**
	 * Loads a notifyTriggerBean by primary key
	 * @param objectID
	 * @return
	 */
	public static TNotifyTriggerBean loadNotifyTriggerBean(Integer objectID) {
		return notifyTriggerDAO.loadByPrimaryKey(objectID);
	}
	
	/**
	 * Gets the list of edit notifyFieldBeans for a trigger 
	 * @param triggerID 
	 * @return
	 */
	public static List<TNotifyFieldBean> getNotifyFieldsForTrigger(Integer triggerID) {
		return notifyFieldDAO.getNotifyFieldsForTrigger(triggerID);
	}
	
	/**
	 * Saves a TNotifyFieldBean
	 * @param notifyFieldBean   
	 * @return
	 */
	public static Integer save(TNotifyFieldBean notifyFieldBean) {
		return notifyFieldDAO.save(notifyFieldBean);
	}
	
	/**
	 * Loads all notify fields for rendering. 
	 * All possible fields are loaded even if there is no database correspondence for them
	 * @param notifyTriggerID
	 * @param locale
	 * @return
	 */
	static List<NotifyTriggerFieldTO> loadAllNotifyFields(Integer notifyTriggerID, Locale locale) {
		List<NotifyTriggerFieldTO> allNotifyFields = new ArrayList<NotifyTriggerFieldTO>();
		Map<String, NotifyTriggerFieldTO> existingNotifyFields = loadExistingNotifyFields(notifyTriggerID);
		NotifyTriggerFieldTO notifyTriggerFieldTO;
		//add field for new issue
		NotifyTriggerFieldTokens notifyTriggerFieldTokens = new NotifyTriggerFieldTokens(ACTIONTYPE.CREATE_ISSUE, null, null);
		String id = encodeTriggerField(notifyTriggerFieldTokens);
		if (existingNotifyFields.containsKey(id)) {
			notifyTriggerFieldTO = existingNotifyFields.get(id);
		} else {
			notifyTriggerFieldTO = new NotifyTriggerFieldTO(id);
		}
		notifyTriggerFieldTO.setActionTypeLabel(getActionTypeLabel(ACTIONTYPE.CREATE_ISSUE, locale));
		notifyTriggerFieldTO.setFieldTypeLabel("");
		notifyTriggerFieldTO.setFieldLabel("");
		notifyTriggerFieldTO.setActionTypeLabel(getActionTypeLabel(ACTIONTYPE.CREATE_ISSUE, locale));
		allNotifyFields.add(notifyTriggerFieldTO);
		//add fields for edit issue
		List<TFieldConfigBean> issueFields = getIssueFields(locale);
		//shown only by the first 
		boolean first = true;
		String actionTypeLabel = getActionTypeLabel(ACTIONTYPE.EDIT_ISSUE, locale);
		String fieldTypeLabel = getFieldTypeLabel(FIELDTYPE.ISSUE_FIELD, locale); 
		for (TFieldConfigBean fieldConfigBean: issueFields) {
			notifyTriggerFieldTokens = new NotifyTriggerFieldTokens(
					ACTIONTYPE.EDIT_ISSUE, FIELDTYPE.ISSUE_FIELD, fieldConfigBean.getField());
			id = encodeTriggerField(notifyTriggerFieldTokens);
			if (existingNotifyFields.containsKey(id)) {
				notifyTriggerFieldTO = existingNotifyFields.get(id);
			} else {
				notifyTriggerFieldTO = new NotifyTriggerFieldTO(id);
			}
			notifyTriggerFieldTO.setActionTypeLabel(actionTypeLabel);
			notifyTriggerFieldTO.setFieldTypeLabel(fieldTypeLabel);
			if (first) {
				first = false;
				actionTypeLabel = "";
				fieldTypeLabel = "";
			}
			notifyTriggerFieldTO.setFieldLabel(fieldConfigBean.getLabel());
			allNotifyFields.add(notifyTriggerFieldTO);
		}
		//add field for attachments
		notifyTriggerFieldTokens = new NotifyTriggerFieldTokens(
				ACTIONTYPE.EDIT_ISSUE, FIELDTYPE.ISSUE_FIELD, SystemFields.ATTACHMENT_ADD_HISTORY_FIELD);
		id = encodeTriggerField(notifyTriggerFieldTokens);
		if (existingNotifyFields.containsKey(id)) {
			notifyTriggerFieldTO = existingNotifyFields.get(id);
		} else {
			notifyTriggerFieldTO = new NotifyTriggerFieldTO(id);
		}
		notifyTriggerFieldTO.setFieldLabel(LocalizeUtil.getLocalizedTextFromApplicationResources(LOCALE_KEYS.PROMPT_FIELDTYPE_ATTACHMENT.getKey(), locale));
		allNotifyFields.add(notifyTriggerFieldTO);
		//add watcher fields
		//consulted
		notifyTriggerFieldTokens = new NotifyTriggerFieldTokens(
				ACTIONTYPE.EDIT_ISSUE, FIELDTYPE.ISSUE_FIELD, SystemFields.ADD_CONSULTED);
		id = encodeTriggerField(notifyTriggerFieldTokens);
		if (existingNotifyFields.containsKey(id)) {
			notifyTriggerFieldTO = existingNotifyFields.get(id);
		} else {
			notifyTriggerFieldTO = new NotifyTriggerFieldTO(id);
		}
		notifyTriggerFieldTO.setFieldTypeLabel(getFieldTypeLabel(FIELDTYPE.WATCHER_FIELD, locale));
		notifyTriggerFieldTO.setFieldLabel(LocalizeUtil.getLocalizedTextFromApplicationResources(LOCALE_KEYS.PROMPT_CONSULTED.getKey(), locale));
		allNotifyFields.add(notifyTriggerFieldTO);
		//informed
		notifyTriggerFieldTokens = new NotifyTriggerFieldTokens(
				ACTIONTYPE.EDIT_ISSUE, FIELDTYPE.ISSUE_FIELD, SystemFields.ADD_INFORMED);
		id = encodeTriggerField(notifyTriggerFieldTokens);
		if (existingNotifyFields.containsKey(id)) {
			notifyTriggerFieldTO = existingNotifyFields.get(id);
		} else {
			notifyTriggerFieldTO = new NotifyTriggerFieldTO(id);
		}
		notifyTriggerFieldTO.setFieldLabel(LocalizeUtil.getLocalizedTextFromApplicationResources(LOCALE_KEYS.PROMPT_INFORMED.getKey(), locale));
		allNotifyFields.add(notifyTriggerFieldTO);
		//add fields for efforts
		List<IntegerStringBean> effortFields = getLocalizedEffortFields(locale);
		//edit budget 
		boolean budgetActive = ApplicationBean.getInstance().getBudgetActive();
		if (budgetActive) {
			allNotifyFields.addAll(getEffortFields(effortFields, existingNotifyFields, FIELDTYPE.BUDGET_FIELD, locale));
		}
		//edit planned value 
		allNotifyFields.addAll(getEffortFields(effortFields, existingNotifyFields, FIELDTYPE.PLANNED_VALUE_FIELD, locale));
		//edit remaining budget
		allNotifyFields.addAll(getEffortFields(effortFields, existingNotifyFields, FIELDTYPE.REMAINING_PLAN_FIELD, locale));
		//edit expense
		allNotifyFields.addAll(getEffortFields(effortFields, existingNotifyFields, FIELDTYPE.EXPENSE_FIELD, locale));
		return allNotifyFields;
	}
	
	/**
	 * Loads the existing notify trigger fields 
	 * - key: encoded trigger field
	 * - value: corresponding TNotifyFieldBean  
	 * @param notifyTriggerID
	 * @return
	 */
	private static Map<String, NotifyTriggerFieldTO> loadExistingNotifyFields(Integer notifyTriggerID) {
		Map<String, NotifyTriggerFieldTO> notifyTriggerFieldsMap = new HashMap<String, NotifyTriggerFieldTO>();
		List<TNotifyFieldBean> notifyFieldBeansList = getNotifyFieldsForTrigger(notifyTriggerID);
		if (notifyFieldBeansList!=null) {
			for (TNotifyFieldBean notifyFieldBean : notifyFieldBeansList) {
				Integer objectID = notifyFieldBean.getObjectID();
				Integer actionType = notifyFieldBean.getActionType();
				Integer fieldType = null;
				if (actionType!=null && actionType.equals(ACTIONTYPE.EDIT_ISSUE)) {
					fieldType = notifyFieldBean.getFieldType();
				}
				Integer fieldID = notifyFieldBean.getField();
				boolean originator = notifyFieldBean.isOriginator();
				boolean manager = notifyFieldBean.isManager();
				boolean responsible = notifyFieldBean.isResponsible();
				boolean consultant = notifyFieldBean.isConsultant();
				boolean informant = notifyFieldBean.isInformant();
				boolean observer = notifyFieldBean.isObserver();
				NotifyTriggerFieldTokens notifyTriggerFieldTokens = new NotifyTriggerFieldTokens(actionType, fieldType, fieldID);
				String encodeID = encodeTriggerField(notifyTriggerFieldTokens);
				notifyTriggerFieldsMap.put(encodeID, new NotifyTriggerFieldTO(encodeID,
						objectID, originator, manager, responsible, consultant, informant, observer));
			}
		}
		return notifyTriggerFieldsMap;
	}
	
	/**
	 * Get a list of IntegerStringBean with localized effort types
	 * Now it is hardcoded effort=1, cost=2
	 * TODO 1. get all defined effort types from the database
	 * 2. change APPLICATION_RESOURCES_STRUTS2 to DATABASE_RESOURCES
	 * @return
	 */
	public static List<IntegerStringBean> getLocalizedEffortFields(Locale locale) {
		List<IntegerStringBean> effortFields = new ArrayList<IntegerStringBean>();
		IntegerStringBean integerStringBean;
		integerStringBean = new IntegerStringBean();
		integerStringBean.setValue(FIELDS.EFFORT);
		integerStringBean.setLabel(LocalizeUtil.getLocalizedTextFromApplicationResources("common.lbl.effort", locale));
		effortFields.add(integerStringBean);
		integerStringBean = new IntegerStringBean();
		integerStringBean.setValue(FIELDS.COST);
		integerStringBean.setLabel(LocalizeUtil.getLocalizedTextFromApplicationResources("common.lbl.cost", locale));
		effortFields.add(integerStringBean);
		return effortFields;
	}
	
	/**
	 * Gets the effort fields for an effortFieldType
	 * @param effortFields
	 * @param existingNotifyFields
	 * @param effortFieldType
	 * @param locale
	 * @return
	 */
	private static List<NotifyTriggerFieldTO> getEffortFields(List<IntegerStringBean> effortFields,
			Map<String, NotifyTriggerFieldTO> existingNotifyFields, Integer effortFieldType, Locale locale) {
		List<NotifyTriggerFieldTO> notifyFields = new ArrayList<NotifyTriggerFieldTO>();
		NotifyTriggerFieldTO notifyTriggerFieldTO;
		boolean first = true;
		String fieldTypeLabel = getFieldTypeLabel(effortFieldType, locale);
		for (IntegerStringBean integerStringBean : effortFields) {
			NotifyTriggerFieldTokens notifyTriggerFieldTokens = new NotifyTriggerFieldTokens(
					ACTIONTYPE.EDIT_ISSUE, effortFieldType, integerStringBean.getValue());
			String id = encodeTriggerField(notifyTriggerFieldTokens);
			if (existingNotifyFields.containsKey(id)) {
				notifyTriggerFieldTO = existingNotifyFields.get(id);
			} else {
				notifyTriggerFieldTO = new NotifyTriggerFieldTO(id);
			}
			notifyTriggerFieldTO.setActionTypeLabel("");
			notifyTriggerFieldTO.setFieldTypeLabel(fieldTypeLabel);
			if (first) {
				first = false;
				fieldTypeLabel = "";
			}
			notifyTriggerFieldTO.setFieldLabel(integerStringBean.getLabel());
			notifyFields.add(notifyTriggerFieldTO);
		}
		return notifyFields;
	}
	
	/**
	 * Saves the notify trigger
	 * @param notifyTriggerID
	 * @param label
	 * @param copy
	 * @param personBean
	 * @param records
	 */
	static Integer saveNotifyTrigger(Integer notifyTriggerID, String label, boolean copy,
			TPersonBean personBean, boolean defaultSettings, String records) {
		TNotifyTriggerBean notifyTriggerBean = null;
		if (notifyTriggerID!=null && !copy) {
			notifyTriggerBean = loadNotifyTriggerBean(notifyTriggerID);
		}
		if (notifyTriggerBean==null) {
			notifyTriggerBean = new TNotifyTriggerBean();
			notifyTriggerBean.setPerson(personBean.getObjectID());
			notifyTriggerBean.setIsSystem(BooleanFields.fromBooleanToString(defaultSettings));
		}
		notifyTriggerBean.setLabel(label);
		Integer triggerID = notifyTriggerDAO.save(notifyTriggerBean);
		if (copy) {
			List<TNotifyFieldBean> oldValuesInChangedRowIDs = getNotifyFieldsForTrigger(notifyTriggerID);
			for (TNotifyFieldBean copiedFromNotifyFieldBean : oldValuesInChangedRowIDs) {
				TNotifyFieldBean copiedToNotifyFieldBean = new TNotifyFieldBean();
				copiedToNotifyFieldBean.setActionType(copiedFromNotifyFieldBean.getActionType());
				copiedToNotifyFieldBean.setFieldType(copiedFromNotifyFieldBean.getFieldType());
				copiedToNotifyFieldBean.setField(copiedFromNotifyFieldBean.getField());
				copiedToNotifyFieldBean.setNotifyTrigger(triggerID);
				copiedToNotifyFieldBean.setOriginator(copiedFromNotifyFieldBean.getOriginator());
				copiedToNotifyFieldBean.setManager(copiedFromNotifyFieldBean.getManager());
				copiedToNotifyFieldBean.setResponsible(copiedFromNotifyFieldBean.getResponsible());
				copiedToNotifyFieldBean.setConsultant(copiedFromNotifyFieldBean.getConsultant());
				copiedToNotifyFieldBean.setInformant(copiedFromNotifyFieldBean.getInformant());
				copiedToNotifyFieldBean.setObserver(copiedFromNotifyFieldBean.getObserver());
				save(copiedToNotifyFieldBean);
			}
		}
		
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
		Map<Integer, TNotifyFieldBean> oldValuesMap = new HashMap<Integer, TNotifyFieldBean>();
		if (!changedRowsIDs.isEmpty()) {
			List<TNotifyFieldBean> oldValuesInChangedRowIDs = notifyFieldDAO.getNotifyFieldsByKeys(changedRowsIDs);
			for (TNotifyFieldBean notifyFieldBean : oldValuesInChangedRowIDs) {
				oldValuesMap.put(notifyFieldBean.getObjectID(), notifyFieldBean);
			}
		}
		for (JSONObject jsonObject : modifiedRows) {
			Integer objectID = jsonObject.getInt(JSONUtility.JSON_FIELDS.OBJECT_ID);
			boolean originator = jsonObject.getBoolean(NotifyTriggerJSON.JSON_FIELDS.ORIGINATOR);
			boolean manager = jsonObject.getBoolean(NotifyTriggerJSON.JSON_FIELDS.MANAGER);
			boolean responsible = jsonObject.getBoolean(NotifyTriggerJSON.JSON_FIELDS.RESPONSIBLE);
			boolean consultant = jsonObject.getBoolean(NotifyTriggerJSON.JSON_FIELDS.CONSULTED);
			boolean informant = jsonObject.getBoolean(NotifyTriggerJSON.JSON_FIELDS.INFORMED);
			boolean observer = jsonObject.getBoolean(NotifyTriggerJSON.JSON_FIELDS.OBSERVER);
			if (objectID!=null) {
				TNotifyFieldBean oldNotifyFieldBean = oldValuesMap.get(objectID);
				if (oldNotifyFieldBean!=null) {
					//existing field modified
					if (originator || manager || responsible || consultant || informant || observer) {
						oldNotifyFieldBean.setOriginator(originator);
						oldNotifyFieldBean.setManager(manager);
						oldNotifyFieldBean.setResponsible(responsible);
						oldNotifyFieldBean.setConsultant(consultant);
						oldNotifyFieldBean.setInformant(informant);
						oldNotifyFieldBean.setObserver(observer);
						save(oldNotifyFieldBean);
					} else {
						//each flag reset: delete the existing trigger field
						notifyFieldDAO.delete(objectID);
					}
				} else {
					String id = jsonObject.getString(JSONUtility.JSON_FIELDS.ID);
					if (id!=null) {
						//new trigger field
						TNotifyFieldBean newNotifyFieldBean = new TNotifyFieldBean();
						NotifyTriggerFieldTokens notifyTriggerFieldTokens = decodeTriggerField(id);
						newNotifyFieldBean.setField(notifyTriggerFieldTokens.getFieldID());
						newNotifyFieldBean.setActionType(notifyTriggerFieldTokens.getActionType());
						newNotifyFieldBean.setFieldType(notifyTriggerFieldTokens.getFieldType());
						newNotifyFieldBean.setNotifyTrigger(triggerID);
						newNotifyFieldBean.setOriginator(originator);
						newNotifyFieldBean.setManager(manager);
						newNotifyFieldBean.setResponsible(responsible);
						newNotifyFieldBean.setConsultant(consultant);
						newNotifyFieldBean.setInformant(informant);
						newNotifyFieldBean.setObserver(observer);
						save(newNotifyFieldBean);
					}
				}
			}
		}
		return triggerID;
	}
	
	/**
	 * Whether the person has right to delete the trigger
	 * @param objectID
	 * @param personBean
	 * @return
	 */
	static boolean isAllowedToDelete(Integer objectID, TPersonBean personBean) {
		if (personBean.isSys()) {
			return true;
		} else {
			return notifyTriggerDAO.isAllowedToDelete(objectID, personBean.getObjectID());
		}
	}
	
	/**
	 * Whether the trigger is assigned for at least a project.
	 * If yes a replacement trigger should be selected before delete 
	 * @param objectID
	 * @return
	 */
	static boolean isDeletable(Integer objectID) {
		return notifyTriggerDAO.isDeletable(objectID);
	}
	
	/**
	 * Deletes a notify trigger
	 * @param triggerID
	 */
	static void deleteNotifyTrigger(Integer triggerID) {
		notifyFieldDAO.deleteByTrigger(triggerID);
		notifyTriggerDAO.delete(triggerID);
	}
	
	/**
	 * Replaces and deletes the notify trigger
	 * @param oldID
	 * @param newID
	 */
	static void replaceAndDeleteTreeQuery(Integer oldID, Integer newID) {
		notifyTriggerDAO.replaceAndDelete(oldID, newID);
	}
	
	/**
	 * Get the notifyFieldBeans, the person requests 
	 * notification for, for a project, actionType and fieldType 
	 * @param person
	 * @param projectIDs typically one single project but if the project 
	 * 	itself is modified then both the old and the new project
	 * @param actionType
	 * @param fieldType  
	 * @return
	 */	
	public static List<TNotifyFieldBean> getTriggerFieldsForRaciRole(Integer person, List<Integer> projectIDs, Integer actionType, Integer fieldType) {
		return notifyFieldDAO.getTriggerFieldsForRaciRole(person, projectIDs, actionType, fieldType);
	}

	
	/**
	 * Creates the JSON string for replacement triggers 
	 * @param notifyTriggerID
	 * @param personID
	 * @param success
	 * @param errorMessage
	 * @param locale
	 * @return
	 */
	static String prepareReplacement(Integer notifyTriggerID, boolean defaultSettings, Integer personID, String errorMessage, Locale locale) {		
		String triggerLabel = "";
		TNotifyTriggerBean notifyTriggerBean = loadNotifyTriggerBean(notifyTriggerID);
		if (notifyTriggerBean!=null) {
			triggerLabel = notifyTriggerBean.getLabel();
		}
		/*String automailAssignment = LocalizeUtil.getLocalizedTextFromApplicationResources(
				"admin.customize.automail.assignments.lbl.assignment", locale);		
		List<IntegerStringBean> removeOptions = new LinkedList<IntegerStringBean>();
		removeOptions.add(new IntegerStringBean(LocalizeUtil.getParametrizedString(
				NotifyTriggerBL.LOCALE_KEYS.PROMPT_REPLACE_IN_AFFECTED.getKey(), new Object[] {automailAssignment} , locale), NotifySettingsBL.REMOVE_OPTION.REPLACE_IN_AFFECTED));
		removeOptions.add(new IntegerStringBean(LocalizeUtil.getParametrizedString(
				NotifyTriggerBL.LOCALE_KEYS.PROMPT_REMOVE_FROM_AFFECTED.getKey(), new Object[] {automailAssignment} , locale), NotifySettingsBL.REMOVE_OPTION.REMOVE_FROM_AFFECTED));
		removeOptions.add(new IntegerStringBean(LocalizeUtil.getParametrizedString(
				NotifyTriggerBL.LOCALE_KEYS.PROMPT_REMOVE_AFFECTED.getKey(), new Object[] {automailAssignment} , locale), NotifySettingsBL.REMOVE_OPTION.REMOVE_AFFECTED));*/			
		List<TNotifyTriggerBean> replacementTriggerList = prepareReplacementTriggers(personID, notifyTriggerID, !defaultSettings);
		return NotifyTriggerJSON.createReplacementTriggerJSON(triggerLabel, /*removeOptions,*/ replacementTriggerList, errorMessage);
	}
	
	/**
	 * Prepares the replacement triggers
	 * @param personID
	 * @param triggerID
	 * @return
	 */
	static List<TNotifyTriggerBean> prepareReplacementTriggers(Integer personID, Integer triggerID, boolean includeOwn) {
		List<TNotifyTriggerBean> replacementTriggersList = notifyTriggerDAO.loadSystemAndOwn(personID, includeOwn);
		if (replacementTriggersList!=null) {
			Iterator<TNotifyTriggerBean> iterator = replacementTriggersList.iterator();
			while (iterator.hasNext()) {
				TNotifyTriggerBean notifyTriggerBean = iterator.next();
				if (notifyTriggerBean.getObjectID().equals(triggerID)) {
					iterator.remove();
					break;
				}
			}
		}
		return replacementTriggersList;
	}
	
	/**
	 * Get the list of localized default TFieldConfigBean for the issue fields
	 * @param locale
	 * @return
	 */
	private static List<TFieldConfigBean> getIssueFields(Locale locale) {
		Set<Integer> excludedFieldsSet = new HashSet<Integer>();
		excludedFieldsSet.add(SystemFields.INTEGER_ISSUENO);
		excludedFieldsSet.add(SystemFields.INTEGER_PROJECT_SPECIFIC_ISSUENO);
		excludedFieldsSet.add(SystemFields.INTEGER_ORIGINATOR);
		excludedFieldsSet.add(SystemFields.INTEGER_CREATEDATE);
		excludedFieldsSet.add(SystemFields.INTEGER_LASTMODIFIEDDATE);
		excludedFieldsSet.add(SystemFields.INTEGER_CHANGEDBY);
		excludedFieldsSet.add(SystemFields.INTEGER_ACCESSLEVEL);
		excludedFieldsSet.add(SystemFields.INTEGER_ARCHIVELEVEL);
		excludedFieldsSet.add(SystemFields.INTEGER_TASK_IS_MILESTONE);
		excludedFieldsSet.add(SystemFields.INTEGER_WBS);
		//excludedFieldsSet.add(SystemFields.INTEGER_SUBMITTEREMAIL);
		List<TFieldConfigBean> defaultConfigs = FieldConfigBL.loadDefault();
		if (defaultConfigs!=null) {
			Iterator<TFieldConfigBean> iterator = defaultConfigs.iterator();
			while (iterator.hasNext()) {
				TFieldConfigBean tConfigBean = iterator.next();
				if (excludedFieldsSet.contains(tConfigBean.getField())) {
					iterator.remove();
				}
			}
		}
		return LocalizeUtil.localizeFieldConfigs(defaultConfigs, locale);
	}	
	
	/**
	 * Builds a map from a list of notifyFieldBeans
	 * 	- key: raci role
	 * 	- value: list of fields for which the raci role is checked 
	 * @param notifyFieldBeanList
	 * @return
	 */
	public static Map<Integer, List<Integer>> organizeByRaciRole(List<TNotifyFieldBean> notifyFieldBeanList) {
		Map<Integer, List<Integer>> raciMap = new HashMap<Integer, List<Integer>>();
		raciMap.put(RACI_ROLES.ORIGINATOR, new LinkedList<Integer>());
		raciMap.put(RACI_ROLES.MANAGER, new LinkedList<Integer>());
		raciMap.put(RACI_ROLES.RESPONSIBLE, new LinkedList<Integer>());
		raciMap.put(RACI_ROLES.CONSULTANT, new LinkedList<Integer>());
		raciMap.put(RACI_ROLES.INFORMANT, new LinkedList<Integer>());
		raciMap.put(RACI_ROLES.OBSERVER, new LinkedList<Integer>());
		List<Integer> raciList;
		String originator;
		String manager;
		String responsible;
		String consultant;
		String informant;
		String observer;
		if (notifyFieldBeanList!=null) {
			for (TNotifyFieldBean notifyFieldBean : notifyFieldBeanList) {
				originator = notifyFieldBean.getOriginator();
				if (BooleanFields.TRUE_VALUE.equals(originator)) {
					raciList = raciMap.get(Integer.valueOf(RACI_ROLES.ORIGINATOR));
					raciList.add(notifyFieldBean.getField());
				}
				manager =  notifyFieldBean.getManager();
				if (BooleanFields.TRUE_VALUE.equals(manager)) {
					raciList = raciMap.get(Integer.valueOf(RACI_ROLES.MANAGER));
					raciList.add(notifyFieldBean.getField());
				}
				responsible = notifyFieldBean.getResponsible();
				if (BooleanFields.TRUE_VALUE.equals(responsible)) {
					raciList = raciMap.get(Integer.valueOf(RACI_ROLES.RESPONSIBLE));
					raciList.add(notifyFieldBean.getField());
				}
				consultant = notifyFieldBean.getConsultant();
				if (BooleanFields.TRUE_VALUE.equals(consultant)) {
					raciList = raciMap.get(Integer.valueOf(RACI_ROLES.CONSULTANT));
					raciList.add(notifyFieldBean.getField());
				}
				informant = notifyFieldBean.getInformant();
				if (BooleanFields.TRUE_VALUE.equals(informant)) {
					raciList = raciMap.get(Integer.valueOf(RACI_ROLES.INFORMANT));
					raciList.add(notifyFieldBean.getField());
				}
				observer = notifyFieldBean.getObserver();
				if (BooleanFields.TRUE_VALUE.equals(observer)) {
					raciList = raciMap.get(Integer.valueOf(RACI_ROLES.OBSERVER));
					raciList.add(notifyFieldBean.getField());
				}
			}
		}
		return raciMap;
	}
}
