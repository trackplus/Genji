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
import java.util.LinkedList;
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
import com.aurel.track.admin.customize.category.filter.execute.ExecuteMatcherBL;
import com.aurel.track.admin.customize.mailTemplate.MailTemplateBL;
import com.aurel.track.admin.customize.notify.settings.NotifySettingsBL;
import com.aurel.track.admin.customize.notify.trigger.NotifyTriggerBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.user.group.GroupMemberBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TMailTemplateBean;
import com.aurel.track.beans.TNotifyFieldBean;
import com.aurel.track.beans.TNotifySettingsBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TRoleFieldBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.FieldChange;
import com.aurel.track.fieldType.runtime.base.FieldsManagerRT;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.item.consInf.ConsInfBL;
import com.aurel.track.item.consInf.RaciRole;
import com.aurel.track.item.history.HistorySaverBL;
import com.aurel.track.util.EqualUtils;
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
public class FreemarkerMailHandlerIssueChange implements IEventSubscriber {

	private static final Logger LOGGER = LogManager.getLogger(FreemarkerMailHandlerIssueChange.class);
	/**
	 * Get the events this class is interested in
	 * At the same time can be more events in the list
	 * (IEventSubscriber.getInterestedEvents())
	 */
	public List<Integer> getInterestedEvents() {
		List<Integer> events = new ArrayList<Integer>();
		events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_CREATE));
		events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_COPY));
		events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_REOPEN));
		events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_CLOSE));
		events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_CHANGESTATUS));
		events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_MOVE));
		events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_CHANGEDATE));
		events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_ASSIGNRESPONSIBLE));
		events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_ASSIGNMANAGER));
		events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_UPDATE));
		events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_ADDCOMMENT));
		events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_EDITCOMMENT));
		events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_DELETECOMMENT));
		events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_ADDATTACHMENT));
		events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_MODIFYATTACHMENT));
		events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_REMOVEATTACHMENT));
		events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_ADD_INFORMED));
		events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_DELETE_INFORMED));
		events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_ADD_CONSULTED));
		events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_DELETE_CONSULTED));
		return events;
	}

	/**
	 * The operations to be executed on event
	 * IEventSubscriber.update()
	 */
	public boolean update(List<Integer> events, Object eventContextObject) {
		AfterItemSaveEventParam afterItemSaveEventParam =
			(AfterItemSaveEventParam)eventContextObject;
		MailHandlerIssueChange mailHandler = new MailHandlerIssueChange(events, afterItemSaveEventParam);
		//start the handler thread
		mailHandler.start();
		return true;
	}

	/**
	 * Mail handler for issue change
	 * @author Tamas Ruff
	 */
	public static class MailHandlerIssueChange extends MailHandler {
		//private AfterItemSaveEventParam afterItemSaveEventParam;
		private TWorkItemBean workItemNew = null;
		private TWorkItemBean workItemOld = null;
		private Locale locale = null;
		//to not to load it from the database for every email
		protected TPersonBean createdByPerson = null;
		private boolean isStateChanged; //other than isClose/isReopen
		private boolean isDateChanged;
		private boolean isOtherChanged;
		private boolean isCopy;
		private boolean isClose;
		private boolean isReopen;
		private boolean isAddComment;
		private boolean isEditComment;
		private boolean isDeleteComment;
		private boolean isResponsibleChanged;
		private boolean isManagerChanged;
		private boolean isMove;
		private boolean isAddAttachment;
		private boolean isEditAttachment;
		private boolean isDeleteAttachment;
		private boolean isAddInformed;
		private boolean isDeleteInformed;
		private boolean isAddConsulted;
		private boolean isDeleteConsulted;

	private MailHandlerIssueChange(List<Integer> events, AfterItemSaveEventParam afterItemSaveEventParam) {
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
		this.locale = afterItemSaveEventParam.getLocale();
		this.isCreated = events.contains(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_CREATE));
		this.isCopy = events.contains(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_COPY));
		this.isStateChanged = events.contains(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_CHANGESTATUS));
		this.isDateChanged = events.contains(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_CHANGEDATE));
		this.isOtherChanged = events.contains(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_UPDATE));
		this.isClose = events.contains(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_CLOSE));
		this.isReopen = events.contains(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_REOPEN));
		this.isAddComment = events.contains(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_ADDCOMMENT));
		this.isEditComment = events.contains(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_EDITCOMMENT));
		this.isDeleteComment = events.contains(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_DELETECOMMENT));
		this.isResponsibleChanged = events.contains(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_ASSIGNRESPONSIBLE));
		this.isManagerChanged = events.contains(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_ASSIGNMANAGER));
		this.isMove = events.contains(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_MOVE));
		this.isAddAttachment = events.contains(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_ADDATTACHMENT));
		this.isEditAttachment = events.contains(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_MODIFYATTACHMENT));
		this.isDeleteAttachment = events.contains(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_REMOVEATTACHMENT));
		this.isAddInformed = events.contains(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_ADD_INFORMED));
		this.isDeleteInformed = events.contains(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_DELETE_INFORMED));
		this.isAddConsulted = events.contains(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_ADD_CONSULTED));
		this.isDeleteConsulted = events.contains(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_DELETE_CONSULTED));
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
		//TProjectBean project=getProjectBean();
		Integer projectID = workItemBean.getProjectID();
		Integer issuTypeID = workItemBean.getListTypeID();
		Integer templateID = MailTemplateBL.findMailTemplateID(eventID, projectID, issuTypeID);
		return MailTemplateBL.getMailTemplate(templateID);
	}

	@Override
	protected boolean sendCondition() {
		return	isDateChanged ||
				isOtherChanged ||
				isStateChanged ||
				isCreated ||
				isCopy ||
				isClose ||
				isReopen ||
				isAddComment ||
				isEditComment ||
				isDeleteComment ||
				isResponsibleChanged ||
				isManagerChanged ||
				isMove ||
				isAddAttachment ||
				isEditAttachment ||
				isDeleteAttachment ||
				isAddConsulted ||
				isDeleteConsulted ||
				isAddInformed ||
				isDeleteInformed;
	}

	/**
	 * Get the resource key of the detailed explanation
	 * @return
	 */
	protected String getChangeDetailKey() {
		if (isCreated || isCopy) {
			return "item.mail.subject.new";
		}
		if (isClose) {
			return "item.mail.subject.closed";
		}
		if (isReopen) {
			return "item.mail.subject.reopen";
		}
		if (isStateChanged) {
			return "item.mail.subject.stateChange";
		}
		if (isMove) {
			return "item.mail.subject.move";
		}
		if (isDateChanged) {
			return "item.mail.subject.dateChange";
		}
		if (isResponsibleChanged) {
			return "item.mail.subject.responsibleChanged";
		}
		if (isManagerChanged) {
			return "item.mail.subject.managerChanged";
		}
		if (isOtherChanged) {
			return "item.mail.subject.trail";
		}
		if (isAddComment) {
			return "item.mail.subject.addComment";
		}
		if (isEditComment) {
			return "item.mail.subject.editComment";
		}
		if (isDeleteComment) {
			return "item.mail.subject.deleteComment";
		}
		if (isAddAttachment) {
			return "item.mail.subject.addAttachment";
		}
		if (isEditAttachment) {
			return "item.mail.subject.editAttachment";
		}
		if (isDeleteAttachment) {
			return "item.mail.subject.deleteAttachment";
		}
		if (isAddInformed) {
			return "item.mail.subject.addInformed";
		}
		if (isDeleteInformed) {
			return "item.mail.subject.deleteInformed";
		}
		if (isAddConsulted) {
			return "item.mail.subject.addConsulted";
		}
		if (isDeleteConsulted) {
			return "item.mail.subject.deleteConsulted";
		}
		return null;
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
	 * @param root
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
					//interestingFields.remove(fieldID);
					restrictedFields.add(fieldID);
				}
			}
		}
		SortedMap<Integer, FieldChange> localizedFieldChangesCloneMap = new TreeMap<Integer, FieldChange>();
		for (Map.Entry<Integer, FieldChange> entry : localizedFieldChangesMap.entrySet()) {
			Integer fieldID = entry.getKey();
			if (!restrictedFields.contains(fieldID)) {
				localizedFieldChangesCloneMap.put(fieldID, entry.getValue());
			}
		}
		
		/**
		 * get the localized fieldChanges for actualLocale
		 */
		LOGGER.debug("The number of fields for person " + personID + " is " + localizedFieldChangesCloneMap.size());
		/**
		 * get the fields for which needs notification and see whether a notification is really needed
		 */
		Set<Integer> fieldsRegisteredForNotify = getFieldsForNotify(personID,
				workItemOld, workItemNew,
				isCreated, actualLocale, pickerRaciRolesSet);
		if (isCreated) {
			if (fieldsRegisteredForNotify==null) {
				//notify needed by create
				LOGGER.debug("Notification needed for person " + personID + " by creating an item");
			} else {
				LOGGER.debug("Notification not needed for person " + personID + " by creating an item");
				return null;
			}
		} else {
			//notify needed by edit
			boolean notifyNeeded = false;
			for (Integer fieldID : localizedFieldChangesCloneMap.keySet()) {
				if (fieldsRegisteredForNotify==null ||  fieldsRegisteredForNotify.contains(fieldID)) {
					FieldChange fieldChange = localizedFieldChangesCloneMap.get(fieldID);
					if (fieldChange.isChanged()) {
						//at least one field with notify demand has changed
						notifyNeeded = true;
						LOGGER.debug("Notification needed for person " + personID + " by editing an item for change on field " + fieldID);
						break;
					}
				}
			}
			if (!notifyNeeded) {
				List<Integer> watcherPersonsAdded = afterItemSaveEventParam.getSelectedPersons();
				if (watcherPersonsAdded!=null && !watcherPersonsAdded.isEmpty()) {
					//consulted/informed person added
					notifyNeeded = watcherPersonsAdded.contains(personID) || GroupMemberBL.isPersonMemberInAnyGroup(personID, watcherPersonsAdded);
				}
				if (!notifyNeeded) {
					//no notification is needed for this changes
					LOGGER.debug("No notification needed for person " + personID + " by editing an item. None of the fields registered for notification was changed");
					return null;
				}
			}
        }
		List<Integer> longFields = FieldsManagerRT.getLongFields(afterItemSaveEventParam.getInterestingFields());
		Collection<FieldChange> shortFieldChangesList = new ArrayList<FieldChange>();
		Collection<FieldChange> longFieldChangesList = new ArrayList<FieldChange>();
		Map<Integer, FieldChange> longFieldChangesMap;
		if (isPlain) {
			/**
			 * prepare lists for plain mail
			 */
			longFieldChangesMap = getPlainMailMaps(localizedFieldChangesCloneMap, longFields, actualLocale, isCreated||isCopy);
			LOGGER.debug("Preparing the plain mail data model for person " + personID);
		} else {
			/**
			 * prepare lists for HTML mail
			 */
			longFieldChangesMap = getHTMLMailMaps(localizedFieldChangesCloneMap, longFields, actualLocale, isCreated||isCopy);
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
		root.put("isCopy", Boolean.valueOf(isCopy));
		if (isCopy && workItemOld!=null) {
			root.put("oldIssue", Constants.Hyperlink + workItemOld.getObjectID().toString());
		}
		root.put("isAddComment", Boolean.valueOf(isAddComment));
		root.put("isEditComment", Boolean.valueOf(isEditComment));
		root.put("isDeleteComment", Boolean.valueOf(isDeleteComment));
		root.put("isOtherChanged", Boolean.valueOf(isOtherChanged));
		root.put("isManagerChanged", Boolean.valueOf(isManagerChanged));
		root.put("isResponsibleChanged", Boolean.valueOf(isResponsibleChanged));
		root.put("isDateChanged", Boolean.valueOf(isDateChanged));
		root.put("isMove", Boolean.valueOf(isMove));
		root.put("isStateChanged", Boolean.valueOf(isStateChanged));
		root.put("isResponsibleChanged", Boolean.valueOf(isResponsibleChanged));
		root.put("isClose", Boolean.valueOf(isClose));
		root.put("isReopen", Boolean.valueOf(isReopen));
		root.put("isAddAttachment", Boolean.valueOf(isAddAttachment));
		root.put("isEditAttachment", Boolean.valueOf(isEditAttachment));
		root.put("isDeleteAttachment", Boolean.valueOf(isDeleteAttachment));
		root.put("isAddInformed", Boolean.valueOf(isAddInformed));
		root.put("isDeleteInformed", Boolean.valueOf(isDeleteInformed));
		root.put("isAddConsulted", Boolean.valueOf(isAddConsulted));
		root.put("isDeleteConsulted", Boolean.valueOf(isDeleteConsulted));
		root.put("createdBy", getCreatedByPersonFullName());
		root.put("shortFieldChanges", shortFieldChangesList);
		if (shortFieldChangesList!=null) {
			for (FieldChange fieldChange : shortFieldChangesList) {
				//to possibly add any field into the mail subject
				root.put(fieldChange.getLocalizedFieldLabel(), fieldChange.getNewShowValue());
			}
		}
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
	public static SortedMap<Integer, FieldChange> getPlainMailMaps(SortedMap<Integer, FieldChange> allFieldChangesMap,
			List<Integer> longFields, Locale locale, boolean isNew) {
		SortedMap<Integer, FieldChange> longFieldChangesMap = new TreeMap<Integer, FieldChange>();
		FieldChange fieldChange;
		for (Iterator<Integer> iterator = allFieldChangesMap.keySet().iterator(); iterator.hasNext();) {
			Integer fieldID = iterator.next();
			fieldChange =  allFieldChangesMap.get(fieldID);
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
	public static SortedMap<Integer, FieldChange> getHTMLMailMaps(SortedMap<Integer, FieldChange> allFieldChangesMap,
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

	/**
	 * Get the set of fields for which the person in a RACI role (or more roles) needs notification
	 * IMPORTANT: There is a strict difference between returning null or an empty set:
	 * null means any change triggers email, an empty set means no change will trigger
	 * Both the filter and the trigger will be applied: first the filter, then the trigger
	 * @param personID the person who might receive the email (not the actual logged in person)
	 * @param workItemBeanOld
	 * @param workItemBeanNew
	 * @param isCreated is that a new issue or a modified one
	 * @param locale
	 * @param pickerRaciRolesSet
	 * @return
	 */
	private Set<Integer> getFieldsForNotify(Integer personID, TWorkItemBean workItemBeanOld, TWorkItemBean workItemBeanNew,
			boolean isCreated, Locale locale, Set<Integer> pickerRaciRolesSet) {
		Set<Integer> fields = new HashSet<Integer>();
		boolean notifySubmitter = false;
		if (personID==null) {
			//unknown submitter (POP3/IMAP)
			if (isCreated) {
				// by create trigger a notification mail
				 //null -> trigger email
				LOGGER.debug("Trigger confirmation mail to the e-mail submitter for a new issue");
				return null;
			} else {
				//the unknown submitter person will be notified at submitterEmail according to the guest user
				TPersonBean personBean = PersonBL.loadByLoginName(TPersonBean.GUEST_USER);
				if (personBean==null) {
					//if guest user does not exists then notify according to originator of the item:
					//this could be also the guest user if it was e-mail submission, otherwise the helpdesk technician entered the e-mail address manually
					personBean = LookupContainer.getPersonBean(workItemBeanNew.getOriginatorID());
					LOGGER.debug("The e-mail to submitter address is computed based on item originator's notification settings");
				} else {
					LOGGER.debug("The e-mail to submitter address is computed based on guest user's notification settings");
				}
				if (personBean==null) {
					//the guest user was deleted (probably after this issue was created) -> empty set (do not trigger email)
					LOGGER.debug("Do not trigger confirmation mail for the e-mail submitter for an existing issue");
					return fields;
				} else {
					//notify as the unknown submitter as it would be the guest
					personID = personBean.getObjectID();
					notifySubmitter = true;
				}
			}
		}
		LOGGER.debug("Get the most specific notification setting(s) for issue number " + workItemBeanNew.getObjectID()  + " and person " + personID);
		//boolean genericProjectIsMostSpecific = false;
		//boolean defaultSettingsIsMostSpecific = false;
		Integer projectIDNew = workItemBeanNew.getProjectID();
		//makes sense only if project was changed
		Integer projectIDOld = null;
		boolean oldProjectDiffers = false;
		if (workItemBeanOld!=null) {
			projectIDOld = workItemBeanOld.getProjectID();
			oldProjectDiffers = !isCreated && projectIDOld!=null && !projectIDOld.equals(projectIDNew);
			if (oldProjectDiffers) {
				LOGGER.debug("A project change occured: get the notification settings according to both new project " + projectIDNew + " and old project " + projectIDOld);
			}
		}
		//own notifySettingsBean for the actual (new if changed) project
		/*
		 * the most specific notifySettingsBean for:
		 * 	1. the new project if project changed and specific project settings found for the new project (own or default)
		 * 	2. the actual project if the project didn't change but specific project settings found (own or default)
		 * 	3. the generic project if no specific project settings found (own or default)
		 */
		TNotifySettingsBean notifySettingsBean = getNearestNotifySettings(personID, projectIDNew);
		/*
		 * the most specific notifySettingsBean for the old project: is not null only if project changed
		 * and specific project settings found for the old project (own or default)
		 */
		TNotifySettingsBean oldProjectNotifySettingsBean = null;
		//own notifySettingsBean for the old project (only if it differs from the new project)
		if (oldProjectDiffers) {
			oldProjectNotifySettingsBean = getNearestNotifySettings(personID, projectIDOld);
		}
		if (notifySettingsBean==null || (oldProjectDiffers && oldProjectNotifySettingsBean==null)) {
				if (notifySettingsBean==null) {
					//try own notifySettingsBean for 'other projects'
					notifySettingsBean = NotifySettingsBL.loadOwnByPersonAndProject(personID, NotifySettingsBL.OTHERPROJECTSID);
					if (notifySettingsBean==null) {
						//default notifySettingsBean for the actual (new if changed) project
						notifySettingsBean = getNearestNotifySettings(null, projectIDNew);
						//default notifySettingsBean for the old project (only if it differs from the new project)
						if (oldProjectDiffers && oldProjectNotifySettingsBean==null) {
							oldProjectNotifySettingsBean = getNearestNotifySettings(null, projectIDOld);
						}
						if (notifySettingsBean==null) {
							//default notifySettingsBean for the generic project
							notifySettingsBean = NotifySettingsBL.loadDefaultByProject(NotifySettingsBL.OTHERPROJECTSID);
							if (notifySettingsBean!=null) {
								//found default generic
								LOGGER.debug("Default 'other projects' settings found");
							}
						} else {
							//found default specific
							if (notifySettingsBean!=null) {
								LOGGER.debug("Default project settings found for the actual/new project " + projectIDNew);
							}
							if (oldProjectNotifySettingsBean!=null) {
								LOGGER.debug("Default project specific settings found for the old project " + projectIDOld);
							}
						}
					} else {
						//found own generic
						LOGGER.debug("Own settings found for 'other projects'");
					}
				} else {
					if (oldProjectDiffers && oldProjectNotifySettingsBean==null) {
						//own settings found for new project but not for old project try own settings for for 'other projects'
						oldProjectNotifySettingsBean = NotifySettingsBL.loadOwnByPersonAndProject(personID, NotifySettingsBL.OTHERPROJECTSID);
						if (oldProjectDiffers && oldProjectNotifySettingsBean==null) {
							//try default settings for old project
							oldProjectNotifySettingsBean = getNearestNotifySettings(null, projectIDOld);
						}
					}
				}
		} else {
			//found own specific
			if (notifySettingsBean!=null) {
				LOGGER.debug("Own settings found for the actual/new project " + projectIDNew + " (or an ancestor project)");
			}
			if (oldProjectNotifySettingsBean!=null) {
				LOGGER.debug("Own settings found for the old project " + projectIDOld + " (or an ancestor project)");
			}
		}
		if (notifySettingsBean==null && oldProjectNotifySettingsBean==null) {
			//no trigger or filter defined for this project: neither the new nor the (eventually different) old,
			//nor for generic project, nether for own nor for default
			//return null which means any change will trigger a mail (the default behaviour before the 3.4 versions of track+)
			LOGGER.debug("No trigger or filter defined at all for the person " + personID + " -> trigger e-mail");
			return null;
		}
		/*********************************************************************************************************************************/
		/**************apply the filter and return an empty set if doesn't match, otherwise go further to applying the trigger************/
		/*********************************************************************************************************************************/
		//load the matcher context with actual values
		MatcherContext matcherContext = new MatcherContext();
		//this is not the logged in person just the next person
		//from the list who might receive notification email
		matcherContext.setLoggedInUser(personID);
		TPersonBean personBean = LookupContainer.getPersonBean(personID);
		matcherContext.setLastLoggedDate(personBean.getLastButOneLogin());
		matcherContext.setLocale(locale);
		boolean filterMatch = false;
		Integer notifyFilter;
		//try the filter associated with the new/generic project
		if (notifySettingsBean!=null) {
			notifyFilter = notifySettingsBean.getNotifyFilter();
			if (notifyFilter==null) {
				//no filter specified for this notify settings means match any
				LOGGER.debug("No filter set for for the (new/generic) project " +
						notifySettingsBean.getProject() + " and person " + personID + " -> trigger e-mail");
				filterMatch = true;
			} else {
				//apply the filter specified for this notify settings
				filterMatch = ExecuteMatcherBL.matchNotifyFilter(notifyFilter, workItemBeanOld, workItemBeanNew,
						matcherContext);
				if (filterMatch) {
					LOGGER.debug("Filter match for new/generic project, filter number " + notifyFilter);
				}
			}
		}
		//try the filter associated with the old project (if it differs from the new project )
		if (!filterMatch && oldProjectNotifySettingsBean!=null) {
			notifyFilter = oldProjectNotifySettingsBean.getNotifyFilter();
			if (notifyFilter==null) {
				//no filter specified for this notify settings means match any
				LOGGER.debug("No filter set for the old project " +
						oldProjectNotifySettingsBean.getProject() + " and person " + personID + " -> trigger e-mail");
				filterMatch = true;
			} else {
				//apply the filter specified for this notify settings
				filterMatch = ExecuteMatcherBL.matchNotifyFilter(notifyFilter, workItemBeanOld, workItemBeanNew,
						matcherContext);
				if (filterMatch) {
					LOGGER.debug("Filter match for old project, filter number " + notifyFilter);
				}
			}
		}
		if (!filterMatch) {
			//no filter match, return an empty set
			LOGGER.debug("The best matching filter set for person " + personID + " doesn't match -> no not trigger e-mail");
			return fields;
		}
		/*********************************************************************************************************************************/
		/*********************************************************apply the trigger*******************************************************/
		/*********************************************************************************************************************************/
		if (notifySettingsBean!=null && notifySettingsBean.getNotifyTrigger()==null) {
			//no trigger specified for this notify settings means trigger any
			LOGGER.debug("No trigger set for for the new/generic project " +
					notifySettingsBean.getProject() + " and person " + personID + " -> trigger e-mail");
			return null;
		}
		if (oldProjectNotifySettingsBean!=null && oldProjectNotifySettingsBean.getNotifyTrigger()==null) {
			//no trigger specified for this notify settings means trigger any
			LOGGER.debug("No trigger set for for the old project " +
					oldProjectNotifySettingsBean.getProject() + " and person " + personID + " -> trigger e-mail");
			return null;
		}
		//trigger surely specified
		/*List<Integer> projectIDs = new LinkedList<Integer>();
		if (genericProjectIsMostSpecific) {
			projectIDs.add(NotifySettingsBL.OTHERPROJECTSID);
		} else {
			if (notifySettingsBean!=null && oldProjectNotifySettingsBean!=null) {
				//not necessarily projectIDNew but it can be an ancestor of that
				projectIDs.add(notifySettingsBean.getProject());
				//not necessarily projectIDOld but it can be an ancestor of that
				projectIDs.add(oldProjectNotifySettingsBean.getProject());
			} else {
				if (notifySettingsBean!=null) {
					//not necessarily projectIDNew but it can be an ancestor of that
					projectIDs.add(notifySettingsBean.getProject());
				} else {
					if (oldProjectNotifySettingsBean!=null) {
					//not necessarily projectIDOld but it can be an ancestor of that
						projectIDs.add(oldProjectNotifySettingsBean.getProject());
					}
				}
			}
		}*/
		/*
		 * the settings person should be null if a default setting is the most specific
		 */
		Integer personForNewProject = null;
		List<Integer> newProjectIDList = new LinkedList<Integer>();
		if (notifySettingsBean!=null) {
			personForNewProject = notifySettingsBean.getPerson();
			//not necessarily projectIDNew but it can be an ancestor of that
			newProjectIDList.add(notifySettingsBean.getProject());
		}
		Integer personForOldProject = null;
		List<Integer> oldProjectIDList = new LinkedList<Integer>();
		//Integer projectIDForOld = null;
		if (oldProjectNotifySettingsBean!=null) {
			personForOldProject = oldProjectNotifySettingsBean.getPerson();
			//not necessarily projectIDOld but it can be an ancestor of that
			oldProjectIDList.add(oldProjectNotifySettingsBean.getProject());
		}
		List<TNotifyFieldBean> notifyFieldBeans = null;
		if (isCreated) {
			//add
			notifyFieldBeans = NotifyTriggerBL.getTriggerFieldsForRaciRole(personForNewProject, newProjectIDList,
					Integer.valueOf(NotifyTriggerBL.ACTIONTYPE.CREATE_ISSUE), null);
		} else {
			//edit
			notifyFieldBeans = NotifyTriggerBL.getTriggerFieldsForRaciRole(personForNewProject, newProjectIDList,
				Integer.valueOf(NotifyTriggerBL.ACTIONTYPE.EDIT_ISSUE),
				Integer.valueOf(NotifyTriggerBL.FIELDTYPE.ISSUE_FIELD));
			if (oldProjectNotifySettingsBean!=null) {
				notifyFieldBeans.addAll(NotifyTriggerBL.getTriggerFieldsForRaciRole(personForOldProject, oldProjectIDList,
				Integer.valueOf(NotifyTriggerBL.ACTIONTYPE.EDIT_ISSUE),
				Integer.valueOf(NotifyTriggerBL.FIELDTYPE.ISSUE_FIELD)));
			}
		}
		List<Integer> personIDs = new LinkedList<Integer>();
		personIDs.add(personID);
		if (isCreated) {
			if (notifyFieldBeans!=null && !notifyFieldBeans.isEmpty()) {
				TNotifyFieldBean notifyFieldBean = notifyFieldBeans.get(0);
				if (BooleanFields.TRUE_VALUE.equals((notifyFieldBean.getOriginator())) &&
						(personID.equals(workItemBeanNew.getOriginatorID()) ||
						pickerRaciRolesSet.contains(Integer.valueOf(NotifyTriggerBL.RACI_ROLES.ORIGINATOR)))) {
					return null;
				}
				if (BooleanFields.TRUE_VALUE.equals((notifyFieldBean.getManager())) &&
						(personID.equals(workItemBeanNew.getOwnerID()) ||
						pickerRaciRolesSet.contains(Integer.valueOf(NotifyTriggerBL.RACI_ROLES.MANAGER)))) {
					return null;
				}
				if (BooleanFields.TRUE_VALUE.equals((notifyFieldBean.getResponsible())) &&
						(personID.equals(workItemBeanNew.getResponsibleID()) ||
						GroupMemberBL.isPersonMemberInGroup(personID, workItemBeanNew.getResponsibleID()) ||
						pickerRaciRolesSet.contains(Integer.valueOf(NotifyTriggerBL.RACI_ROLES.RESPONSIBLE)))) {
					return null;
				}
				if (BooleanFields.TRUE_VALUE.equals(notifyFieldBean.getConsultant()) &&
						(ConsInfBL.hasRaciRole(workItemBeanNew.getObjectID(), personIDs, RaciRole.CONSULTANT) ||
								pickerRaciRolesSet.contains(Integer.valueOf(NotifyTriggerBL.RACI_ROLES.CONSULTANT)))) {
					return null;
				}
				if (BooleanFields.TRUE_VALUE.equals((notifyFieldBean.getInformant())) &&
						(ConsInfBL.hasRaciRole(workItemBeanNew.getObjectID(), personIDs, RaciRole.INFORMANT) ||
						pickerRaciRolesSet.contains(Integer.valueOf(NotifyTriggerBL.RACI_ROLES.INFORMANT)))) {
					return null;
				}
				if (BooleanFields.TRUE_VALUE.equals((notifyFieldBean.getObserver()))) {
					return null;
				}
				LOGGER.debug("No raci role for person " + personID + " by creating a new issue " + "  -> reject e-mail");
				return fields;
			}
		} else {
			Map<Integer, List<Integer>> notifyFieldsMap = NotifyTriggerBL.organizeByRaciRole(notifyFieldBeans);
			if (personID.equals(workItemBeanNew.getOriginatorID()) || notifySubmitter ||
					pickerRaciRolesSet.contains(Integer.valueOf(NotifyTriggerBL.RACI_ROLES.ORIGINATOR))) {
				List<Integer> originatorFields = notifyFieldsMap.get(Integer.valueOf(NotifyTriggerBL.RACI_ROLES.ORIGINATOR));
				fields.addAll(originatorFields);
				if (notifySubmitter) {
					//notify the unknown submitter according to originator settings for guest user
					return fields;
				}
			}
			if (personID.equals(workItemBeanNew.getOwnerID()) || (workItemBeanOld!=null && personID.equals(workItemBeanOld.getOwnerID())) ||
					pickerRaciRolesSet.contains(Integer.valueOf(NotifyTriggerBL.RACI_ROLES.MANAGER))) {
				List<Integer> managerFields = notifyFieldsMap.get(Integer.valueOf(NotifyTriggerBL.RACI_ROLES.MANAGER));
				fields.addAll(managerFields);
			}
			if (personID.equals(workItemBeanNew.getResponsibleID()) ||
					GroupMemberBL.isPersonMemberInGroup(personID, workItemBeanNew.getResponsibleID()) ||
					(workItemBeanOld!=null && EqualUtils.notEqual(workItemBeanNew.getResponsibleID(), workItemBeanOld.getResponsibleID()) &&
						(personID.equals(workItemBeanOld.getResponsibleID()) || GroupMemberBL.isPersonMemberInGroup(personID, workItemBeanOld.getResponsibleID()))) ||
					pickerRaciRolesSet.contains(Integer.valueOf(NotifyTriggerBL.RACI_ROLES.RESPONSIBLE))) {
				List<Integer> responsibleFields = notifyFieldsMap.get(Integer.valueOf(NotifyTriggerBL.RACI_ROLES.RESPONSIBLE));
				fields.addAll(responsibleFields);
			}
			if (ConsInfBL.hasRaciRole(workItemBeanNew.getObjectID(), personIDs, RaciRole.CONSULTANT) ||
					pickerRaciRolesSet.contains(Integer.valueOf(NotifyTriggerBL.RACI_ROLES.CONSULTANT))) {
				List<Integer> consultantFields = notifyFieldsMap.get(Integer.valueOf(NotifyTriggerBL.RACI_ROLES.CONSULTANT));
				fields.addAll(consultantFields);
			}
			if (ConsInfBL.hasRaciRole(workItemBeanNew.getObjectID(), personIDs, RaciRole.INFORMANT) ||
					pickerRaciRolesSet.contains(Integer.valueOf(NotifyTriggerBL.RACI_ROLES.INFORMANT))) {
				List<Integer> informantFields = notifyFieldsMap.get(Integer.valueOf(NotifyTriggerBL.RACI_ROLES.INFORMANT));
				fields.addAll(informantFields);
			}
			List<Integer> observantFields = notifyFieldsMap.get(Integer.valueOf(NotifyTriggerBL.RACI_ROLES.OBSERVER));
			fields.addAll(observantFields);
			LOGGER.debug("The number of potentially triggering fields found for person " + personID + " is " + fields.size());
		}
		return fields;
	}
	//end MailHandler class
	}
}


