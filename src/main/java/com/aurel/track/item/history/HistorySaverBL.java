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


package com.aurel.track.item.history;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.GeneralSettings;
import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.beans.TFieldChangeBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.THistoryTransactionBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.FieldChange;
import com.aurel.track.fieldType.runtime.base.FieldsManagerRT;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.runtime.system.text.SystemProjectSpecificIssueNoRT;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.ItemPersisterException;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.event.parameters.AfterItemSaveEventParam;

public class HistorySaverBL {
	
	private static String fieldLabelSeparator=": ";
	private static String commonFieldsSeparator = ";";
	private static String lineBreak = "<br>"; //"\r\n";
	private static final Logger LOGGER = LogManager.getLogger(HistorySaverBL.class);
	
	public static boolean saveHistory(AfterItemSaveEventParam afterItemSaveEventParam, Locale locale, 
			Integer personID, List<Integer> longFields, boolean isCreate, boolean isCopy, Integer fieldChangeID) {
		SortedMap<Integer, FieldChange> fieldChangesMap = getLocalizedFieldChanges(afterItemSaveEventParam, locale, true);
		return persistHistory(fieldChangesMap, afterItemSaveEventParam, personID, longFields, locale, isCreate, isCopy, fieldChangeID);
	}
	
	/**
	 * Gets the localized field changes
	 * @param afterItemSaveEventParam
	 * @param locale
	 * @param history true for history, false for e-mail
	 * @return
	 */
	public static SortedMap<Integer, FieldChange> getLocalizedFieldChanges(AfterItemSaveEventParam afterItemSaveEventParam, Locale locale, boolean history) {
		SortedMap<Integer, FieldChange> fieldChangeMap = new TreeMap<Integer, FieldChange>();
		TWorkItemBean workItemBean = afterItemSaveEventParam.getWorkItemNew();
		TWorkItemBean workItemBeanOriginal = afterItemSaveEventParam.getWorkItemOld();
		Map<Integer, TFieldConfigBean> fieldConfigsMap = afterItemSaveEventParam.getFieldConfigs();
		Set<Integer> explicitHistoryFields = getHardCodedExplicitHistoryFields();
		//the attachment changes are also explicitly historized
		Set<Integer> attachmentHistoryFields = HistoryLoaderBL.getAttachmentHistoryFields();
		for (Integer fieldID : afterItemSaveEventParam.getInterestingFields()) {
			FieldChange fieldChange = new FieldChange();
			fieldChange.setFieldID(fieldID);
			//pseudo fields:attachment history
			if (attachmentHistoryFields.contains(fieldID)) {
				String fieldLabel = null;
				if (history) {
					fieldLabel = LocalizeUtil.getLocalizedTextFromApplicationResources(HistoryLoaderBL.getHistoryFieldKey(fieldID), locale);
				} else {
					fieldLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.automail.trigger.lbl.fieldType.attachment", locale);
				}
				fieldChange.setLocalizedFieldLabel(fieldLabel);
				fieldChange.setExplicitHistory(true);
				String newStringValue = workItemBean.getAttachment();
				fieldChange.setNewShowValue(newStringValue);
				if (workItemBeanOriginal!=null) {
					String oldStringValue = workItemBeanOriginal.getAttachment();
					fieldChange.setOldShowValue(oldStringValue);
					fieldChange.setChanged(EqualUtils.notEqual(newStringValue, oldStringValue));
				} else {
					fieldChange.setChanged(true);
				}
			} else {
				IFieldTypeRT fieldType = FieldTypeManager.getFieldTypeRT(fieldID);
				Object newValue = workItemBean.getAttribute(fieldID);
				Object oldValue = null;
				if (workItemBeanOriginal!=null) {
					oldValue = workItemBeanOriginal.getAttribute(fieldID);
				}
				TFieldConfigBean fieldConfigBean = fieldConfigsMap.get(fieldID);
				String fieldLabel = fieldConfigBean.getLabel();
				if (locale!=null && !locale.equals(afterItemSaveEventParam.getLocale())) {
					fieldLabel = FieldRuntimeBL.localizeFieldConfig(fieldConfigBean.getObjectID(), locale);
					if (fieldLabel==null) {
						LOGGER.debug("Field label is null for locale " + locale + " fall back to " + fieldConfigBean.getLabel());
						fieldLabel = fieldConfigBean.getLabel();
					}
				}
				fieldChange.setLocalizedFieldLabel(fieldLabel);
				fieldChange.setExplicitHistory(explicitHistoryFields.contains(fieldID) || fieldConfigBean.isHistoryString());
				//get the new value to show
				fieldChange.setNewShowValue(fieldType.getShowValue(newValue, locale));
				if ((workItemBeanOriginal!=null && fieldType.valueModified(newValue, oldValue)) || 
						//the rare case when the comment field is present on the create screen 
						(workItemBeanOriginal==null && fieldID.intValue()==SystemFields.COMMENT && newValue!=null && !"".equals(newValue))) {
					//get the old value to show and set the changed flag.
					//the old value and change flag is set only by edit mode when the value is really changed
					//or by create mode when Comment is specified 				
					fieldChange.setOldShowValue(fieldType.getShowValue(oldValue, locale));
					fieldChange.setChanged(true);
					LOGGER.debug("FieldID " + fieldID + " oldValue " + oldValue + " newValue " + newValue);
				}
			}
			fieldChangeMap.put(fieldID, fieldChange);
		}
		return fieldChangeMap;
	}
	
	/**
	 * Builds the trail text for history
	 * @param fieldChanges Map with FieldChange values
	 * @param longFields the fields with longer texts (description, comment). This will be added at the end of the trail text
	 * @param locale
	 * @param isNew whether creating a new issue (isCreate || isCopy) or editing an existing one 
	 * @param newLineString
	 * 
	 * @return
	 */ 
	private static boolean persistHistory(SortedMap<Integer, FieldChange> fieldChanges, AfterItemSaveEventParam afterItemSaveEventParam,
			Integer personID, List<Integer> longFields, Locale locale, boolean isCreate, boolean isCopy, Integer fieldChangeID) {
		SortedMap<Integer, FieldChange> historyLongTextMap = new TreeMap<Integer, FieldChange>(); //maintain order
		TWorkItemBean workItemBeanNew = afterItemSaveEventParam.getWorkItemNew();
		TWorkItemBean workItemBeanOld = afterItemSaveEventParam.getWorkItemOld();
		boolean needHistoryTransaction = false;
		if (isCreate || isCopy) {
			//need first status in history
			needHistoryTransaction = true;
		}
		Map<Integer, TFieldChangeBean> lastHistoryFieldChangesMap = null;
		if (!needHistoryTransaction && fieldChanges!=null) {
			//gather the fields with explicit history
			List<Integer> explicitHistoryFields = new LinkedList<Integer>();
			int minutes = GeneralSettings.getHistoryAndEmailDelay();
			for (FieldChange fieldChange : fieldChanges.values()) {
				if (fieldChange.isChanged()) {
					needHistoryTransaction = true;
					if (minutes==0 || minutes<0) {
						//no need to handle recent history changes
						break;
					}
					Integer fieldID = fieldChange.getFieldID();
					if (fieldChange.isExplicitHistory() &&
							!SystemFields.INTEGER_STATE.equals(fieldID) &&
							!SystemFields.INTEGER_COMMENT.equals(fieldID)) {
						//the status field although is hardcoded to have explicit history but me make it exception from rule.
						//(the status change will be added to the history even the last status change happened within x minutes)
						//the comment should be added in the history anyway
						explicitHistoryFields.add(fieldChange.getFieldID());
					}
				}
			}
			if (!explicitHistoryFields.isEmpty()) {
				Date targetTime = new Date(); //now
				targetTime = DateUtils.addMinutes(targetTime, -minutes); 
				Map<Integer, THistoryTransactionBean> lastHistoryTransactionsMap = GeneralUtils.createMapFromList(HistoryTransactionBL.loadByItemAndFieldsSince(workItemBeanNew.getObjectID(), explicitHistoryFields, targetTime));
				List<TFieldChangeBean> lastFieldChanges = FieldChangeBL.loadByItemAndFieldsSince(workItemBeanNew.getObjectID(), explicitHistoryFields, targetTime);
				lastHistoryFieldChangesMap = new HashMap<Integer, TFieldChangeBean>();
				for (TFieldChangeBean fieldChangeBean : lastFieldChanges) {
					Integer transactionID = fieldChangeBean.getHistoryTransaction();
					Integer fieldID = fieldChangeBean.getFieldKey();
					THistoryTransactionBean historyTransactionBean = lastHistoryTransactionsMap.get(transactionID);
					if (historyTransactionBean!=null) {
						//only the first found
						Integer changedByPersonID = historyTransactionBean.getChangedByID();
						if (personID.equals(changedByPersonID) && lastHistoryFieldChangesMap.get(fieldID)==null) {
							lastHistoryFieldChangesMap.put(fieldID, fieldChangeBean);
						}
						explicitHistoryFields.remove(fieldID);
						if (explicitHistoryFields.isEmpty()) {
							break;
						}
					}
				}
			}
		}
		boolean mightTriggerEmail = false;
		if (!needHistoryTransaction) {
			return false;
		}
		//Integer historyTransactionID = HistoryTransactionBL.saveHistoryTransaction(workItemBeanNew.getObjectID(), personID, new Date(), null);
		if (isCreate || isCopy) {
			//add the first status change history entry if not deep copy 
			if (!workItemBeanNew.isDeepCopy()) {
				//with deep copy the status changes will be copied also no need for first status change in history
				//set null for workItemBeanOld parameter (by create is null anyway) because otherwise the 
				//values are the same and will not be saved  
				Integer statusTransactionID = HistoryTransactionBL.saveHistoryTransaction(workItemBeanNew.getObjectID(), personID, new Date(), null);
				saveExplicitField(SystemFields.INTEGER_STATE, 
						statusTransactionID, workItemBeanNew, null, null);
			}
			mightTriggerEmail = true;
		}
		StringBuilder compoundTextNewBuffer = new StringBuilder();
		StringBuilder compoundTextOldBuffer = new StringBuilder();
		if (isCopy) {
			Object[] msgArguments = null;
			String messageKey = null;
			if (ApplicationBean.getInstance().getSiteBean().getProjectSpecificIDsOn()) {
				String projectSpecificID = SystemProjectSpecificIssueNoRT.getShowValue(workItemBeanOld.getIDNumber(), workItemBeanOld);
				msgArguments = new Object[] {projectSpecificID};
				messageKey = "item.history.copyMessageProjectSpecificID";
			} else {
				msgArguments = new Object[] {workItemBeanOld.getObjectID()};
				messageKey = "item.history.copyMessage";
			}
			compoundTextNewBuffer.append(LocalizeUtil.getParametrizedString(messageKey, msgArguments, locale));	
		}
		Set<Integer> attachmentHistoryFields = HistoryLoaderBL.getAttachmentHistoryFields();
		Integer historyTransactionID = null;
		for (Map.Entry<Integer, FieldChange> entry : fieldChanges.entrySet()) {
			FieldChange fieldChange = (FieldChange)entry.getValue();
			Integer fieldID = fieldChange.getFieldID();
			String fieldLabel = fieldChange.getLocalizedFieldLabel();
			String newValue = fieldChange.getNewShowValue();
			String oldValue = fieldChange.getOldShowValue();
			//For history text we are interested in: 
			//1. all field changes for existing issues 
			//2. "Comment" for new issues
			//For that the fieldChange.isChanged() should be set accordingly already  
			if (!fieldChange.isChanged()) {
				continue;
			}
			if (attachmentHistoryFields.contains(fieldID)) {
				Integer attachmentChangeTransactionID = HistoryTransactionBL.saveHistoryTransaction(workItemBeanNew.getObjectID(), personID, new Date(), null);
				insertFieldChange(attachmentChangeTransactionID, fieldID, newValue, oldValue);
				mightTriggerEmail = true;
				continue;
			}
			if (fieldChange.isExplicitHistory() || SystemFields.INTEGER_COMMENT.equals(fieldID)) {
				TFieldChangeBean fieldChangeBean = null;
				boolean isCommentChange = false;
				if (fieldChangeID==null) {
					if (lastHistoryFieldChangesMap!=null) {
						fieldChangeBean = lastHistoryFieldChangesMap.get(fieldID);
					}
					if (fieldChangeBean==null) {
						//no previous entry within x minutes 
						mightTriggerEmail = true;
					}
				} else {
					isCommentChange = true;
					fieldChangeBean = FieldChangeBL.loadByPrimaryKey(fieldChangeID);
					mightTriggerEmail = true;
				}
				if (historyTransactionID==null && !isCommentChange) {
					historyTransactionID = HistoryTransactionBL.saveHistoryTransaction(workItemBeanNew.getObjectID(), personID, new Date(), null);
				}
				saveExplicitField(fieldID, historyTransactionID, workItemBeanNew, workItemBeanOld, fieldChangeBean);
				//the comment is saved anyway explicitly in the history as Comment field
				//even if explicit history is not configured.
				//Explicit history for comment means whether to historize the comment changes (edit and delete).
				//The field set into the workitemContext is COMMENT also for edit and delete comment 
				//(instead of COMMENT_DELETE_HISTORY_FIELD or COMMENT_MODIFY_HISTORY_FIELD) comment because
				//we need the explicit history flag which is set only for COMMENT field (the other two are only pseudo fields)
				if (fieldChange.isExplicitHistory() && SystemFields.INTEGER_COMMENT.equals(fieldID)) {
					if (oldValue!=null && !"".equals(oldValue)) {
						//history only if the comment is edited or deleted
						Integer commentChangeTransactionID = HistoryTransactionBL.saveHistoryTransaction(workItemBeanNew.getObjectID(), personID, new Date(), null);
						if (newValue==null || "".equals(newValue)) {
							insertFieldChange(commentChangeTransactionID, SystemFields.COMMENT_DELETE_HISTORY_FIELD, newValue, oldValue);
						} else {
							insertFieldChange(commentChangeTransactionID, SystemFields.COMMENT_MODIFY_HISTORY_FIELD, newValue, oldValue);
						}
					}
				}
			} else {
				//fields without explicit history
				if (longFields.contains(fieldID)) {
					//gather the changed long fields to add them at the end 
					historyLongTextMap.put(fieldID, fieldChange);
					mightTriggerEmail = true;
				} else {
					if (newValue!=null && !"".equals(newValue)) {
						if (compoundTextNewBuffer.length()>0) { //some content already present
							compoundTextNewBuffer.append(commonFieldsSeparator + lineBreak);
						}
						compoundTextNewBuffer.append(fieldLabel + fieldLabelSeparator + newValue);
						mightTriggerEmail = true;
					}
					if (oldValue!=null && !"".equals(oldValue)) {
						if (compoundTextOldBuffer.length()>0) { //some content already present
							compoundTextOldBuffer.append(commonFieldsSeparator + lineBreak);
						}
						compoundTextOldBuffer.append(fieldLabel + fieldLabelSeparator + oldValue);
						mightTriggerEmail = true;
					}
				}
			}
		}
		//add the longText changes at the end
		//add the commonFieldsSeparator only after the last short field
		//after long fields (HTML text) it does not make sense (for ex. after a <p>)
		boolean firstLongField = true;
		for (Map.Entry<Integer, FieldChange> entry : historyLongTextMap.entrySet()) {
			FieldChange fieldChange = entry.getValue();
			if (fieldChange!=null) {
				if (compoundTextNewBuffer.length()>0) { //some content already present
					if (firstLongField) {
						compoundTextNewBuffer.append(commonFieldsSeparator + lineBreak);
					} else {
						compoundTextNewBuffer.append(lineBreak);
					}
				}
				if (compoundTextOldBuffer.length()>0) { //some content already present
					if (firstLongField) {
						compoundTextOldBuffer.append(commonFieldsSeparator + lineBreak);
					} else {
						compoundTextOldBuffer.append(lineBreak);
					}
				}
				firstLongField = false;
				String fieldLabel = fieldChange.getLocalizedFieldLabel();
				String newShowValue = fieldChange.getNewShowValue();
				if (newShowValue!=null && !"".equals(newShowValue)) {
					compoundTextNewBuffer.append(fieldLabel + fieldLabelSeparator + newShowValue);
				}
				String oldShowValue = fieldChange.getOldShowValue();
				if (oldShowValue!=null && !"".equals(oldShowValue)) {
					compoundTextOldBuffer.append(fieldLabel + fieldLabelSeparator + oldShowValue);
				}
			}
		}
		saveCompoundField(historyTransactionID, workItemBeanNew.getObjectID(), personID, compoundTextNewBuffer.toString(), compoundTextOldBuffer.toString());
		return mightTriggerEmail;
	}
	
	/**
	 * Saves the compound values with no explicit history
	 * @param historyTransactionID
	 * @param itemID
	 * @param personID
	 * @param newValue
	 * @param oldValue
	 */
	private static void saveCompoundField(Integer historyTransactionID, Integer itemID, Integer personID, String newValue, String oldValue) {
		if ((newValue!=null && newValue.length()>0) || (oldValue!=null && oldValue.length()>0)) {
			if (historyTransactionID==null) {
				//only non explicit fields were saved
				historyTransactionID = HistoryTransactionBL.saveHistoryTransaction(itemID, personID, new Date(), null);
			}
			try {
				HistoryDAOUtils.insertFieldChange(TFieldChangeBean.COMPOUND_HISTORY_FIELD, null, 
						historyTransactionID, newValue, oldValue, ValueType.LONGTEXT, null);
			} catch (ItemPersisterException e) {
				LOGGER.warn("Inserting the compound field for historyTransactionID " + 
						historyTransactionID + " failed with " + e.getMessage(), e);
			}
		}
	}
	
	private static void insertFieldChange(Integer historyTransactionID, Integer fieldID, String newValue, String oldValue) {
		if ((newValue!=null && newValue.length()>0) || (oldValue!=null && oldValue.length()>0)) {
			try {
				HistoryDAOUtils.insertFieldChange(fieldID, null, 
						historyTransactionID, newValue, oldValue, ValueType.LONGTEXT, null);
			} catch (ItemPersisterException e) {
				LOGGER.warn("Inserting the attachment field for historyTransactionID " +
						historyTransactionID + " and field " + fieldID + " failed with " + e.getMessage(), e);
			}
		}
	}
	
	private static void saveExplicitField(Integer fieldID, Integer historyTransactionID,
			TWorkItemBean workItemBeanNew, TWorkItemBean workItemBeanOld, TFieldChangeBean fieldChangeBean) {
		IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
		fieldTypeRT.processHistorySave(fieldID, null, historyTransactionID, 
				workItemBeanNew, workItemBeanOld, fieldChangeBean);
	}
	
	/**
	 * Prepare a workItemContext with the new and old workItem's 
	 * comment fields according to comment change
	 * @param workItemID
	 * @param personID
	 * @param locale
	 * @param commentID
	 * @param newComment
	 */
	public static void changeComment(Integer workItemID, Integer personID, Locale locale, Integer commentID, String newComment,List<ErrorData> errorList) {
		if (newComment!=null) {
			WorkItemContext workItemContext = FieldsManagerRT.inlineComment(workItemID, personID, locale, commentID, newComment);
			FieldsManagerRT.save(workItemContext, false, errorList, true);
		}
	}
	
	/**
	 * Prepare a workItemContext with the new and old workItem's 
	 * comment fields according to comment add
	 * @param workItemID
	 * @param personID
	 * @param locale
	 * @param newComment
	 * @param errorList
	 */
	public static void addComment(Integer workItemID, Integer personID, 
			Locale locale, String newComment, boolean fromEmail, List<ErrorData> errorList) {
		addComment(workItemID,personID, locale, newComment, fromEmail, errorList,null);
	}
	public static void addComment(Integer workItemID, Integer personID,
				Locale locale, String newComment, boolean fromEmail, List<ErrorData> errorList,Integer parentID) {
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("Adding comment to item:" + workItemID + " parentID=" + parentID + " comment:\n" + newComment);
		}
		WorkItemContext workItemContext = FieldsManagerRT.inlineComment(workItemID, personID, locale, null, newComment);
		FieldsManagerRT.save(workItemContext, false, errorList, !fromEmail);
	}
	/**
	 * Prepare a workItemContext with the new and old workItem's 
	 * comment fields according to comment delete
	 * @param workItemID
	 * @param personID
	 * @param locale
	 * @param objectID
	 */
	public static void deleteComment(Integer workItemID, Integer personID, Locale locale, Integer objectID) {
		TFieldChangeBean fieldChangeBean = FieldChangeBL.loadByPrimaryKey(objectID);
		if(fieldChangeBean==null){
			LOGGER.warn("Can't delete comment. No comment found for id:"+objectID);
		}
		WorkItemContext workItemContext = FieldsManagerRT.inlineComment(workItemID, personID, locale, objectID, null);
		List<ErrorData> errorList=new ArrayList<ErrorData>();
		FieldsManagerRT.save(workItemContext, false, errorList, true);
	}
	
	
	public static void addAttachment(Integer workItemID, Integer personID, 
			Locale locale, String fileName, String description, Long size, boolean isURL) {
		String newFileNameDescription = getAttachmentHistoryText(fileName, description, size, locale, isURL);
		WorkItemContext workItemContext = FieldsManagerRT.attachment(workItemID, personID, 
				SystemFields.INTEGER_ATTACHMENT_ADD_HISTORY_FIELD, locale, newFileNameDescription, null);
		List<ErrorData> errorList=new ArrayList<ErrorData>();
		FieldsManagerRT.save(workItemContext, false, errorList, true);	
	}
	
	public static void removeAttachment(Integer workItemID, Integer personID, 
			Locale locale, String oldFileNameDescription) {
		WorkItemContext workItemContext = FieldsManagerRT.attachment(workItemID, personID, 
				SystemFields.INTEGER_ATTACHMENT_DELETE_HISTORY_FIELD, locale, null, oldFileNameDescription);
		List<ErrorData> errorList=new ArrayList<ErrorData>();
		FieldsManagerRT.save(workItemContext, false, errorList, true);
	}
	
	public static void editAttachment(Integer workItemID, Integer personID, 
			Locale locale, String newFileNameDescription, String oldFileNameDescription) {
		WorkItemContext workItemContext = FieldsManagerRT.attachment(workItemID, personID, 
				SystemFields.INTEGER_ATTACHMENT_MODIFY_HISTORY_FIELD, locale, newFileNameDescription, oldFileNameDescription);
		List<ErrorData> errorList=new ArrayList<ErrorData>();
		FieldsManagerRT.save(workItemContext, false, errorList, true);
	}
	
			
	public static String getAttachmentHistoryText(String fileName, String description, Long size, Locale locale, boolean isURL) {
		StringBuffer stringBuffer = new StringBuffer();
		if (fileName!=null) {
			if(isURL) {
				//stringBuffer.append(LocalizeUtil.getLocalizedTextFromApplicationResources(HistoryLoaderBL.ATTACHMENT_FILE, locale) + fieldLabelSeparator);
				stringBuffer.append(HistoryLoaderBL.ATTACHMENT_URL + fieldLabelSeparator);
			}else {
				stringBuffer.append(LocalizeUtil.getLocalizedTextFromApplicationResources(HistoryLoaderBL.ATTACHMENT_FILE, locale) + fieldLabelSeparator);
			}
			stringBuffer.append(fileName);
		}		
		if (size!=null) {
			if (stringBuffer.length()>0) {
				stringBuffer.append(commonFieldsSeparator + lineBreak);
			}
			stringBuffer.append(LocalizeUtil.getLocalizedTextFromApplicationResources(HistoryLoaderBL.ATTACHMENT_SIZE, locale) + fieldLabelSeparator);
			stringBuffer.append(TAttachmentBean.getFileSizeString(size));
		}
		if (description!=null && !"".equals(description)) {
			if (stringBuffer.length()>0) {
				stringBuffer.append(commonFieldsSeparator + lineBreak);
			}
			stringBuffer.append(LocalizeUtil.getLocalizedTextFromApplicationResources(HistoryLoaderBL.ATTACHMENT_DESCRIPTION, locale) + fieldLabelSeparator);
			stringBuffer.append(description);
		}
		return stringBuffer.toString();
	}
	
	public static Set<Integer> getHardCodedExplicitHistoryFields() {
		//at the database level the history flags for these fields are not set(able) so we force them by code
		Set<Integer> explicitHistoryFields = new HashSet<Integer>();
		explicitHistoryFields.add(SystemFields.INTEGER_STATE);
		explicitHistoryFields.add(SystemFields.INTEGER_STARTDATE);
		explicitHistoryFields.add(SystemFields.INTEGER_ENDDATE);
		//explicitHistoryFields.add(SystemFields.INTEGER_COMMENT);
		/*explicitHistoryFields.add(SystemFields.INTEGER_ATTACHMENT_HISTORY_FIELD);
		explicitHistoryFields.add(SystemFields.INTEGER_ATTACHMENT_EDIT_HISTORY_FIELD);
		explicitHistoryFields.add(SystemFields.INTEGER_ATTACHMENT_DELETE_HISTORY_FIELD);*/
		return explicitHistoryFields;
	}
}
