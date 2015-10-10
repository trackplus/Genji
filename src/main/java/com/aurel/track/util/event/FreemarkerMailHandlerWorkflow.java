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



package com.aurel.track.util.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.Constants;
import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.mailTemplate.MailTemplateBL;
import com.aurel.track.beans.TMailTemplateBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TRoleFieldBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.FieldChange;
import com.aurel.track.fieldType.runtime.base.FieldsManagerRT;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.item.history.HistorySaverBL;
import com.aurel.track.util.TagReplacer;
import com.aurel.track.util.event.parameters.AfterItemSaveEventParam;

/**
 * Mail handler listener for creating/modifying issue
 * It could be called from more places: 
 * -	creating/modifying an issue through web interface (any action which modifies the issue fields)
 * -	creating/adding comment to an issue through email (pop3/imap), 
 * -	creating/modifying an issue through webservice etc.
 * To trigger the mail sending one should call the notify method of 
 * the event publisher with the not empty events list and the eventContextObject set
 * @author Tamas Ruff
 *
 */

	/**
	 * Mail handler for issue change
	 * @author Tamas Ruff
	 */
	public class FreemarkerMailHandlerWorkflow extends MailHandler {
		private static final Logger LOGGER = LogManager.getLogger(FreemarkerMailHandlerWorkflow.class);
		//private AfterItemSaveEventParam afterItemSaveEventParam;
		private TWorkItemBean workItemNew = null;
		private TWorkItemBean workItemOld = null;
		//to not to load it from the database for every email
		protected TPersonBean createdByPerson = null;
	
	private FreemarkerMailHandlerWorkflow(List<Integer> events, AfterItemSaveEventParam afterItemSaveEventParam) {
		super(afterItemSaveEventParam.getWorkItemNew(), afterItemSaveEventParam.getWorkItemOld(), afterItemSaveEventParam.getLocale());
		this.afterItemSaveEventParam = afterItemSaveEventParam;
		//workItemContext = afterItemSaveEventParam.getWorkItemContext();
		this.workItemNew = afterItemSaveEventParam.getWorkItemNew();
		this.workItemOld = afterItemSaveEventParam.getWorkItemOld();
		if (workItemNew!=null && workItemNew.getProjectID()!=null && 
			workItemOld!=null && workItemOld.getProjectID()!=null && 
			!workItemOld.getProjectID().equals(workItemNew.getProjectID())) {
				//add old projectID if project was changed
				projectIDs.add(workItemOld.getProjectID());
		}
		this.isCreated = workItemNew.getObjectID()==null;
	}

	@Override
	protected TPersonBean getChangedByPerson() {
		if (changedByPerson==null) {
			changedByPerson = LookupContainer.getPersonBean(workItemBean.getChangedByID());
		}
		return changedByPerson;
	}
	
	protected String getCreatedByPersonFullName() {
		if (createdByPerson==null) {
			createdByPerson = LookupContainer.getPersonBean(workItemBean.getOriginatorID());
		}
		if (createdByPerson!=null) {
			return createdByPerson.getPureFullName();
		}
		return "";
	}

	@Override
	protected TProjectBean getProjectBean() {
		return LookupContainer.getProjectBean(workItemBean.getProjectID());
	}


	@Override
	protected TMailTemplateBean getTemplate() {
		//get the event for setting the notification template
		Integer eventID = null;
		if (this.isCreated) {
			String submitterEmail = workItemBean.getSubmitterEmail();
			if (submitterEmail!=null && !"".equals(submitterEmail)) {
				eventID = IEventSubscriber.EVENT_POST_ISSUE_CREATE_BY_EMAIL;
			} else {
				eventID = IEventSubscriber.EVENT_POST_ISSUE_CREATE;
			}
		} else {
			eventID = IEventSubscriber.EVENT_POST_ISSUE_UPDATE;
		}
		Integer projectID = workItemBean.getProjectID();
		Integer issuTypeID = workItemBean.getListTypeID();
		Integer templateID = MailTemplateBL.findMailTemplateID(eventID, projectID, issuTypeID);
		return MailTemplateBL.getMailTemplate(templateID);
	}

	@Override
	protected boolean sendCondition() {
		return true;
	}
	
	/**
	 * Get the resource key of the detailed explanation
	 * @return
	 */
	protected String getChangeDetailKey() {
		if (isCreated /*|| isCopy*/) {
			return "item.mail.subject.new";
		} else {
			return "item.mail.subject.trail";
		}
	}
	
	/**
	 * Get the parameters of the parameterized detailed explanation
	 * @param itemID
	 * @param workItemBean
	 * @param fieldChangesMap
	 * @param locale
	 * @return
	 */
	protected Object[] getChangeDetailParameters(String itemID, TWorkItemBean workItemBean, SortedMap<Integer, FieldChange> fieldChangesMap,  Locale locale) {
		FieldChange stateChange = fieldChangesMap.get(SystemFields.INTEGER_STATE);
		String newStateLabel = "";
		if (stateChange!=null) {
			newStateLabel = stateChange.getNewShowValue();
		}
		return new Object[] {itemID, //for new 
				workItemBean.getSynopsis(),
				newStateLabel //for state change
		};
	}
	/**
	 * Gets the field change map for a specific locale
	 * This can be reused for different users to be notified having the same locale
	 * @param afterItemSaveEventParam
	 * @param locale
	 * @return
	 */
	protected SortedMap<Integer, FieldChange> getLocalizedFieldChangesMap(AfterItemSaveEventParam afterItemSaveEventParam, Locale locale) {
		return HistorySaverBL.getLocalizedFieldChanges(afterItemSaveEventParam, locale, false);
	}
	
	/**
	 * Sets the root data model at the first argument and returns the localized subject of the mail
	 * @param personBean
	 * @param actualLocale the locale of the user the e-mail will be sent to 
	 * @param isPlain plain or HTML mail
	 */
	@Override
	protected Map<String, Object> getRootContext(TPersonBean personBean, 
			Locale actualLocale, boolean isPlain, Set<Integer> pickerRaciRolesSet, SortedMap<Integer, FieldChange> localizedFieldChangesMap) {
		Integer personID = personBean.getObjectID();
		//remove all the fieldIDs for which the person has no read right
		Set<Integer> restrictedFields = new HashSet<Integer>();
		Map<Integer, Integer> restrictedFieldsMap = AccessBeans.getFieldRestrictions(
				personID, workItemNew.getProjectID(), workItemNew.getListTypeID(), false);
		if (restrictedFieldsMap!=null && !restrictedFieldsMap.isEmpty()) {
			LOGGER.debug("The number of restricted fields for person " + personID + " is " + restrictedFieldsMap.size());
			for (Integer fieldID : restrictedFieldsMap.keySet()) {
				Integer accessFlag = restrictedFieldsMap.get(fieldID);
				if (accessFlag.intValue()==TRoleFieldBean.ACCESSFLAG.NOACCESS) {
					restrictedFields.add(fieldID);
				}
			}
		}
		SortedMap<Integer, FieldChange> localizedFieldChangesCloneMap = null;
		
		localizedFieldChangesCloneMap = new TreeMap<Integer, FieldChange>();
		for (Map.Entry<Integer, FieldChange> entry : localizedFieldChangesMap.entrySet()) {
			Integer fieldID = entry.getKey();
			if (!restrictedFields.contains(fieldID)) {
				localizedFieldChangesCloneMap.put(fieldID, entry.getValue());
			}
		}
		
		/**
		 * get the localized fieldChanges for actualLocale
		 */
		//SortedMap<Integer, FieldChange> localizedFieldChangesMap = HistorySaverBL.getLocalizedFieldChanges(afterItemSaveEventParam, actualLocale, false);
		LOGGER.debug("The number of fields for person " + personID + " is " + localizedFieldChangesCloneMap.size());
		
		
		List<Integer> longFields = FieldsManagerRT.getLongFields(afterItemSaveEventParam.getInterestingFields());
		Collection<FieldChange> shortFieldChangesList = new ArrayList<FieldChange>();
		Collection<FieldChange> longFieldChangesList = new ArrayList<FieldChange>();
		Map<Integer, FieldChange> longFieldChangesMap;
		if (isPlain) {
			/**
			 * prepare lists for plain mail
			 */
			longFieldChangesMap = getPlainMailMaps(localizedFieldChangesCloneMap, longFields, actualLocale, isCreated);
			LOGGER.debug("Preparing the plain mail data model for person " + personID);
		} else {
			/**
			 * prepare lists for HTML mail
			 */
			longFieldChangesMap = getHTMLMailMaps(localizedFieldChangesCloneMap, longFields, actualLocale, isCreated);
			LOGGER.debug("Preparing the HTML mail data model for person " + personID);
		}
		shortFieldChangesList = localizedFieldChangesCloneMap.values();
		longFieldChangesList = longFieldChangesMap.values();
		
		LOGGER.debug("Preparing the root context...");
		Map<String, Object> root = prepareRootContext(localizedFieldChangesCloneMap, actualLocale);
		/**
		 * set context fields present in both plain- and HTML mail context
		 */
		root.put("isCreated", Boolean.valueOf(isCreated));
		root.put("createdBy", getCreatedByPersonFullName());
		root.put("shortFieldChanges", shortFieldChangesList);
		root.put("longFieldChanges", longFieldChangesList);
		LOGGER.debug("Returning the root context");
		return root;
	}
	
	/**
	 * Builds the text for plain email in two Maps. 
	 * The first map (modifying the first argument): removes the long field FieldChanges
	 * The second map (the return value): builds a map for long fields from the FieldChanges 
	 * 		removed from the allFieldChangesMap map
	 * Sets the localized change detail texts if changed for both short and long fields
	 * @param allFieldChangesMap Map with FieldChanges for both short and long values. It is also an "out" parameter
	 * @param longFields the fieldIDs for longer texts (description, comment).
	 * @param locale
	 * @param isNew whether creating a new issue (isCreate || isCopy) or editing an existing one
	 * @return
	 */ 
	private static SortedMap<Integer, FieldChange> getPlainMailMaps(SortedMap<Integer, FieldChange> allFieldChangesMap, 
			List<Integer> longFields, Locale locale, boolean isNew) {
		SortedMap<Integer, FieldChange> longFieldChangesMap = new TreeMap<Integer, FieldChange>();
		for (Iterator<Integer> iterator = allFieldChangesMap.keySet().iterator(); iterator.hasNext();) {
			Integer fieldID = iterator.next();
			FieldChange fieldChange =  allFieldChangesMap.get(fieldID);
			String newVal = fieldChange.getNewShowValue();
			String oldVal = fieldChange.getOldShowValue();
			if ((newVal==null || "".equals(newVal))   
				&& (oldVal==null || "".equals(oldVal))) {
				iterator.remove();
				continue;
			}
			if (fieldChange.isChanged()) { //refers to edit 			
				//if it has been changed then the localized change detail text will be added (both for short and long texts)
				setLocalizedChangeDetail(fieldChange, longFields, locale, isNew);
			}
			//remove the long field changes, leave only the short field changes in the allFieldChangesMap
			if (longFields.contains(fieldID)) {
				//remove from short fields
				iterator.remove();
				String newShowValue = fieldChange.getNewShowValue();
				String oldShowValue = fieldChange.getOldShowValue();
				//if new the fieldChange.isChanged() is false but nevertheless the Description (or other long fields) should be shown if it was entered
				if (isNew && newShowValue!=null && !"".equals(newShowValue.trim())) { //refers to new
					setLocalizedChangeDetail(fieldChange, longFields, locale, isNew); //it was not added above
					//Description, Comment or other long text field added 
					//get the plain text from the HTML and add to the longFieldChangesMap map 
					TagReplacer replacer = new TagReplacer(locale);
					replacer.setContextPath(Constants.BaseURL);	
					fieldChange.setNewShowValue(convertHTML2Text(replacer.formatDescription(newShowValue)));
					longFieldChangesMap.put(fieldID, fieldChange);
				} else if (fieldChange.isChanged()) {
					//Description or other long text field modified (add/edit/delete) or Comment added: 
					//get the plain text from the HTML and add to the longFieldChangesMap map
					TagReplacer replacer = new TagReplacer(locale);
					replacer.setContextPath(Constants.BaseURL);
					if (newShowValue==null || "".equals(newShowValue) && oldShowValue!=null && !"".equals(oldShowValue)) {
						//remove a long field, show the old value but in the template only the new value is present
						//so set the old value as new value
						newShowValue = oldShowValue;
					}
					fieldChange.setNewShowValue(convertHTML2Text(replacer.formatDescription(newShowValue)));
					longFieldChangesMap.put(fieldID, fieldChange);
				}
			}
		}
		return longFieldChangesMap;
	}
	
	/**
	 * Builds the text for HTML email in two Maps. 
	 * The first map (modifying the first argument): removes the long field FieldChanges
	 * The second map (the return value): builds a map for long fields from the FieldChanges 
	 * 		removed from the allFieldChangesMap map, and sets the localized change detail texts for them
	 * 		(in HTML mail the short fields does not need change detail texts)
	 * @param allFieldChangesMap Map with FieldChanges for both short and long values. It is also an "out" parameter
	 * @param longFields the fieldIDs for longer texts (description, comment).
	 * @param locale
	 * @param isNew whether creating a new issue (isCreate || isCopy) or editing an existing one 
	 * @return
	 */ 
	private static SortedMap<Integer, FieldChange> getHTMLMailMaps(SortedMap<Integer, FieldChange> allFieldChangesMap, 
			List<Integer> longFields, Locale locale, boolean isNew) {
		SortedMap<Integer, FieldChange> longFieldChangesMap = new TreeMap<Integer, FieldChange>();
		for (Iterator<Integer> iterator = allFieldChangesMap.keySet().iterator(); iterator.hasNext();) {
			Integer fieldID = iterator.next();
			//for HTML emails no localizedChangeDetail text is needed for short fields, 
			//the extra case should be taken only for long fields
			FieldChange fieldChange = allFieldChangesMap.get(fieldID);
			String newVal = fieldChange.getNewShowValue();
			String oldVal = fieldChange.getOldShowValue();
			if ((newVal==null || "".equals(newVal))
				&& (oldVal==null || "".equals(oldVal))) {
				iterator.remove();
				continue;
			}
			if (longFields.contains(fieldID)) {
				//remove from short fields
				iterator.remove();
				String newShowValue = fieldChange.getNewShowValue();
				String oldShowValue = fieldChange.getOldShowValue();
				//add the description long field if changed by edit or is not null by create
				if (fieldChange.isChanged() || //refers to edit/delete 
						(isNew && newShowValue!=null) && !"".equals(newShowValue.trim())) { //refers to new
					//add to longFieldChangesMap
					setLocalizedChangeDetail(fieldChange, longFields, locale, isNew);
					TagReplacer replacer = new TagReplacer(locale);
					replacer.setContextPath(Constants.BaseURL);
					if (newShowValue==null || "".equals(newShowValue) && oldShowValue!=null && !"".equals(oldShowValue)) {
						//remove a long field, show the old value but in the template only the new value is present
						//so set the old value as new value
						newShowValue = oldShowValue;
					}
					fieldChange.setNewShowValue(replacer.formatDescription(newShowValue));
					longFieldChangesMap.put(fieldID, fieldChange);
				}
			}
		}
		return longFieldChangesMap;
	}

	
}

	
