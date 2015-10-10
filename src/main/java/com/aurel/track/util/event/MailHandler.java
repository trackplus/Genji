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

import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.notify.settings.NotifySettingsBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TMailTemplateBean;
import com.aurel.track.beans.TNotifySettingsBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.runtime.base.FieldChange;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.SendItemEmailBL;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.LocaleHandler;
import com.aurel.track.util.PropertiesHelper;
import com.aurel.track.util.emailHandling.Html2Text;
import com.aurel.track.util.emailHandling.JavaMailBean;
import com.aurel.track.util.emailHandling.MailBoxUsers;
import com.aurel.track.util.event.parameters.AfterItemSaveEventParam;

import freemarker.template.Template;

public abstract class MailHandler extends Thread {

	private static final Logger LOGGER = LogManager.getLogger(MailHandler.class);

	protected AfterItemSaveEventParam afterItemSaveEventParam;

	protected static JavaMailBean mailBean = JavaMailBean.getInstance();

	protected TWorkItemBean workItemBean = null;
	protected TWorkItemBean workItemBeanOld = null;
	protected Locale locale;
	protected boolean isCreated = false;
	protected Recipients recipients = null;
	protected TPersonBean changedByPerson = null;

	/**
	 * Parameters in the root context. They can be referenced by ${<parameterName>} in e-mail subject or body
	 * @author Tamas
	 *
	 */
	public static interface ROOT_PARAMETERS {
		/**
		 * direct item fields
		 */
		static String ITEM_NO = "itemNo";
		static String TITLE = "title";
		static String PROJECT = "project";
		static String ITEM_TYPE = "itemType";
		static String STATUS = "status";
		static String CHANGED_BY = "changedBy";
		static String SUBMITTER_EMAIL = "submitterEmail";
		/**
		 * derived fields
		 */
		//the complete subject in the previous versions: equivalent of ${marker}[${{project}] ${changeDetail}
		static String SUBJECT = "subject";
		//this will be parsed at a possible replay
		static String MARKER = "marker";
		//explanation of what was changed
		static String CHANGE_DETAIL = "changeDetail";
		static String CHANGE_DETAIL_KEY = "changeDetailKey";
		//HTML link to the item
		static String MORE_INFO = "moreInfo";
	}

	public static interface MAIL_PART {
		public static final int SUBJECT = 1;
		public static final int BODY = 2;
	}

	/**
	 * Gets the field change map for a specific locale
	 * This can be reused for different users to be notified having the same locale
	 * @param afterItemSaveEventParam
	 * @param locale
	 * @return
	 */
	protected abstract SortedMap<Integer, FieldChange> getLocalizedFieldChangesMap(AfterItemSaveEventParam afterItemSaveEventParam, Locale locale);

	/**
	 * Prepares the root context for freemarker template
	 * @param personBean
	 * @param personLocale
	 * @param isPlain
	 * @return
	 */
	protected abstract Map<String, Object> getRootContext(TPersonBean personBean,
			Locale personLocale, boolean isPlain, Set<Integer> pickerRaciRolesSet, SortedMap<Integer, FieldChange> localizedFieldChangesMap);

	/**
	 * Prepares the common variables in the root context
	 * @param fieldChangesMap
	 * @param locale
	 * @return
	 */
	protected Map<String, Object> prepareRootContext(SortedMap<Integer, FieldChange> fieldChangesMap, Locale locale) {
		Map<String, Object> rootContext = new HashMap<String, Object>();
		String itemID = ItemBL.getItemNo(workItemBean);
		rootContext.put(ROOT_PARAMETERS.ITEM_NO, itemID);
		rootContext.put(ROOT_PARAMETERS.TITLE, workItemBean.getSynopsis());
		String projectLabel = null;
		ILabelBean projectBean = LookupContainer.getProjectBean(workItemBean.getProjectID());
		if (projectBean!=null) {
			projectLabel = projectBean.getLabel();
		}
		rootContext.put(ROOT_PARAMETERS.PROJECT, projectLabel);
		ILabelBean itemTypeBean = LookupContainer.getItemTypeBean(workItemBean.getListTypeID(), locale);
		if (itemTypeBean!=null) {
			rootContext.put(ROOT_PARAMETERS.ITEM_TYPE, itemTypeBean.getLabel());
		}
		ILabelBean statusBean = LookupContainer.getStatusBean(workItemBean.getStateID(), locale);
		if (statusBean!=null) {
			rootContext.put(ROOT_PARAMETERS.STATUS, statusBean.getLabel());
		}
		rootContext.put(ROOT_PARAMETERS.CHANGED_BY, getChangedByPerson().getPureFullName());
		rootContext.put(ROOT_PARAMETERS.SUBMITTER_EMAIL, workItemBean.getSubmitterEmail());
		String marker = SendItemEmailBL.getMarker(workItemBean, locale);
		rootContext.put(ROOT_PARAMETERS.MARKER, marker);
		String getChangeDetailKey = getChangeDetailKey();
		String changeDetail = null;
		if (getChangeDetailKey!=null) {
			changeDetail = LocalizeUtil.getParametrizedString(getChangeDetailKey(), getChangeDetailParameters(itemID, workItemBean, fieldChangesMap, locale), locale);
			rootContext.put(ROOT_PARAMETERS.CHANGE_DETAIL, changeDetail);
			rootContext.put(ROOT_PARAMETERS.CHANGE_DETAIL_KEY, getChangeDetailKey());
		}
		String subject = marker + "["+projectLabel+"] " + changeDetail;
		rootContext.put(ROOT_PARAMETERS.SUBJECT, subject);
		rootContext.put(ROOT_PARAMETERS.MORE_INFO, Constants.Hyperlink + workItemBean.getObjectID());
		return rootContext;
	}

	/**
	 * Get the resource key of the detailed explanation
	 * @return
	 */
	protected abstract String getChangeDetailKey();

	/**
	 * Get the parameters of the parameterized detailed explanation
	 * @param itemID
	 * @param workItemBean
	 * @param fieldChangesMap
	 * @param locale
	 * @return
	 */
	protected abstract  Object[] getChangeDetailParameters(String itemID, TWorkItemBean workItemBean, SortedMap<Integer, FieldChange> fieldChangesMap,  Locale locale);

	/**
	 * the workItem's projectID (both the old and the new projectID if project was changed)
	 */
	protected List<Integer> projectIDs = new LinkedList<Integer>();

	protected MailHandler(TWorkItemBean workItemBean, TWorkItemBean workItemBeanOld, Locale locale) {
		this.workItemBean = workItemBean;
		this.workItemBeanOld = workItemBeanOld;
		this.locale = locale;
		projectIDs.add(workItemBean.getProjectID());
	}

	@Override
	public void run() {
		sendEntireMail();
	}

	/**
	 * Gets the e-mail templatr
	 * @return
	 */
	protected abstract TMailTemplateBean getTemplate();

	/**
	 * Gather the recipients
	 */
	protected void gatherRecipients() {
		recipients = new Recipients(workItemBean, workItemBeanOld);
		recipients.buildRaciList(projectIDs, isCreated);
	}

	/**
	 * Whether the email sending is really needed
	 * @return
	 */
	protected abstract boolean sendCondition();



	/**
	 * The full name of the person who made the action
	 * @return
	 */
	protected abstract TPersonBean getChangedByPerson();

	/**
	 * The project bean the modified issue belongs to
	 * @return
	 */
	protected abstract TProjectBean getProjectBean();

	public static Locale getLocaleForPerson(TPersonBean sendToPerson, TPersonBean changedByPersonBean, Locale detectedLocale) {
		if (sendToPerson!=null && sendToPerson.getObjectID()==null && detectedLocale!=null) {
			//for unknown user change to the detected language 
			return detectedLocale;
		}
		Locale personLocale = getLocaleForPerson(sendToPerson);
		if (personLocale==null) {
			Integer personID = sendToPerson.getObjectID();
			if (personID!=null) {
				//if track+ registered person (not POP3 email submitter person)
				if (personLocale==null) {
					//fall back to changer person's locale
					personLocale = getLocaleForPerson(changedByPersonBean);
				}
			}
			if (personLocale==null) {
				//set it back to English
				personLocale = Locale.getDefault();
			}
		}
		return personLocale;
	}

	/**
	 * Gets the locale set for a person
	 * @param personBean
	 * @return
	 */
	private static Locale getLocaleForPerson(TPersonBean personBean) {
		if (personBean==null) {
			return null;
		}
		String localeString = personBean.getPrefLocale();
		if (localeString==null || "".equals(localeString.trim())) {
			return null;
		}
		return LocaleHandler.getLocaleFromString(localeString);
	}

	/**
	 * This method finally sends the entire mail from this transaction.
	 */
	public void sendEntireMail() {
		//when send condition is not satisfied do not send email
		if (!sendCondition()) {
			return;
		}
		//gather the recipients
		gatherRecipients();
		//get the sender-address
		TPersonBean sendFrom= getSendFrom(getProjectBean(), getChangedByPerson());
		TPersonBean replayToPerson=MailHandler.getReplayTo(getProjectBean(), getChangedByPerson());
		TMailTemplateBean mailTemplateBean=getTemplate();
		if (mailTemplateBean==null){
			LOGGER.warn("No mail template found");
			return;
		}
		//all persons to notify through direct RACI roles
		List<TPersonBean> allDirectPersons = recipients.getPersonsBeansToNotify();
		//direct RACI persons to substitute persons
		Map<Integer, Integer> personToSubstituteMap = recipients.getPersonToSubstituteMap();
		Collection<Integer> allSubstituteIDs = personToSubstituteMap.values();
		//RACI persons to notify directly (only To:) who are not substitutes for other person
		List<TPersonBean> personsToNotifyDirectly = new LinkedList<TPersonBean>();
		//RACI persons to notify directly (only To:) who are at the same  time substitutes for other person
		List<TPersonBean> substitutePersonsToNotifyDirectly = new LinkedList<TPersonBean>();
		Set<Integer> substitutesIDsToNotifyDirectly = new HashSet<Integer>();
		//split direct RACI persons to those who are also substitute at the same time for other direct RACI person, and those who are not substitutes
		for (TPersonBean personBean : allDirectPersons) {
			Integer personID = personBean.getObjectID();
			if (allSubstituteIDs.contains(personID)) {
				LOGGER.debug("Person to be notified directly " + personBean.getLabel()  + "("+  personID + ") is also substitute person");
				substitutePersonsToNotifyDirectly.add(personBean);
				substitutesIDsToNotifyDirectly.add(personID);
			} else {
				personsToNotifyDirectly.add(personBean);
			}
		}
		//get those substitutes who are not RACI persons for item and put them in the Cc list
		Set<Integer> substitutesIDsToNotifyByCc = new HashSet<Integer>();
		if (allSubstituteIDs!=null && !allSubstituteIDs.isEmpty()) {
			for (Integer substituteID : allSubstituteIDs) {
				if (!substitutesIDsToNotifyDirectly.contains(substituteID)) {
					LOGGER.debug("Substitute person "+  substituteID + " added for Cc");
					substitutesIDsToNotifyByCc.add(substituteID);
				}
			}
		}
		Set<String> mailboxUsersSet = MailBoxUsers.getMailboxUsersSet();
		Map<Locale, SortedMap<Integer, FieldChange>> localizedFieldChangesMapForLocale = new HashMap<Locale, SortedMap<Integer,FieldChange>>();
		//send e-mails to those RACI persons who are at the same time substitutes for other RACI person
		if (substitutesIDsToNotifyDirectly!=null && !substitutesIDsToNotifyDirectly.isEmpty()) {
			for (TPersonBean personBean : substitutePersonsToNotifyDirectly) {
				String emailTo = personBean.getEmail();
				if (emailTo!=null && mailboxUsersSet!=null && mailboxUsersSet.contains(emailTo)) {
					LOGGER.error("The e-mail address " + emailTo +  " set for person " + personBean.getName() +
							" is used a mailbox for email submission. The notification e-mail will not be sent to avoid infinite cycle of e-mailing");
					continue;
				}
				Integer personID = personBean.getObjectID();
				Locale personLocale = getLocaleForPerson(personBean, sendFrom, locale);
				SortedMap<Integer, FieldChange> localizedFieldChangesMap = localizedFieldChangesMapForLocale.get(personLocale);
				if (localizedFieldChangesMap==null) {
					localizedFieldChangesMap = getLocalizedFieldChangesMap(afterItemSaveEventParam, personLocale);
					localizedFieldChangesMapForLocale.put(personLocale, localizedFieldChangesMap);
				}
				boolean plainEmail = personBean.isPreferredEmailTypePlain();
				Set<Integer> pickerRoles = recipients.getPickerRaciRolesForPerson(personID);
				Map<String, Object> root = getRootContext(personBean, personLocale, plainEmail, pickerRoles, localizedFieldChangesMap);

				boolean emailSent = sendEmailOnPerson(personBean, personLocale, sendFrom, null,replayToPerson, mailTemplateBean.getObjectID(), root);
				if (emailSent) {
					LOGGER.debug("E-mail sent to the subtitute person to be notified directly " + personBean.getLabel()  + "("+  personID + ")");
					//emailsSentToSubstitutesToNotifyDirectly.add(personBean.getObjectID());
				} else {
					//sending the e-mail was not successful, add it to the Cc list
					LOGGER.debug("E-mail not  sent to the subtitute person to be notified directly " + personBean.getLabel()  + "("+  personID + "). Added as Cc");
					substitutesIDsToNotifyByCc.add(personBean.getObjectID());
				}
			}
		}

		for (TPersonBean personBean : personsToNotifyDirectly) {
			String emailTo = personBean.getEmail();
			if (emailTo!=null && mailboxUsersSet!=null && mailboxUsersSet.contains(emailTo)) {
				LOGGER.error("The e-mail address " + emailTo +  " set for person " + personBean.getName() +
						" is used a mailbox for email submission. The notification e-mail will not be sent to avoid infinite cycle of e-mailing");
				continue;
			}
			Integer personID = personBean.getObjectID();
			Integer substituteID = null;
			TPersonBean ccPerson = null;
			if (personToSubstituteMap!=null) {
				substituteID = personToSubstituteMap.get(personID);
				if (substituteID!=null && substitutesIDsToNotifyByCc.contains(substituteID)) {
					ccPerson = LookupContainer.getPersonBean(substituteID);
				}
			}
			Locale personLocale = getLocaleForPerson(personBean, sendFrom, locale);
			SortedMap<Integer, FieldChange> localizedFieldChangesMap = localizedFieldChangesMapForLocale.get(personLocale);
			if (localizedFieldChangesMap==null) {
				localizedFieldChangesMap = getLocalizedFieldChangesMap(afterItemSaveEventParam, personLocale);
				localizedFieldChangesMapForLocale.put(personLocale, localizedFieldChangesMap);
			}
			boolean plainEmail = personBean.isPreferredEmailTypePlain();
			Set<Integer> pickerRoles = recipients.getPickerRaciRolesForPerson(personID);
			Map<String, Object> root = getRootContext(personBean, personLocale, plainEmail, pickerRoles, localizedFieldChangesMap);
			boolean emailSent = sendEmailOnPerson(personBean, personLocale, sendFrom, ccPerson,replayToPerson, mailTemplateBean.getObjectID(), root);
			if (emailSent) {
				if (substituteID!=null) {
					//if the person is substitute for two different RACI persons then add for only for one of them into Cc (to not receive two or more e-mails as Cc)
					substitutesIDsToNotifyByCc.remove(substituteID);
				}
			}
		}
	}

	/**
	 * Send e-mail to a person
	 * @param sendToPerson
	 * @param personLocale
	 * @param sendFromPerson
	 * @param ccPerson
	 * @param replayToPerson
	 * @param mailTemplateID
	 * @param root
	 * @return
	 */
	public static boolean sendEmailOnPerson(TPersonBean sendToPerson, Locale personLocale, TPersonBean sendFromPerson, TPersonBean ccPerson,TPersonBean replayToPerson,
			Integer mailTemplateID, Map<String, Object> root) {
		boolean plainEmail = sendToPerson.isPreferredEmailTypePlain();
		Integer personID = sendToPerson.getObjectID();
		MailPartTemplates mailPartTemplates = EmailTemplatesContainer.getMailPartTemplates(mailTemplateID, plainEmail, personLocale);
		if (mailPartTemplates==null) {
			LOGGER.warn("No mail template found for mailTemplateID " + mailTemplateID + " plain " + plainEmail + " locale " + personLocale);
			return false;
		}
		Template subjectTemplate = mailPartTemplates.getSubjectTemplate();
		if (subjectTemplate==null) {
			if (plainEmail) {
				LOGGER.warn("No plain subject template found for person " + sendToPerson.getObjectID());
			} else {
				LOGGER.warn("No HTML subject template found for person " + sendToPerson.getObjectID());
			}
			return false;
		}
		Template bodyTemplate = mailPartTemplates.getBodyTemplate();
		if (bodyTemplate==null) {
			if (plainEmail) {
				LOGGER.warn("No plain body template found for person " + sendToPerson.getObjectID() + ": " + sendToPerson.getFullName() + ": " + personLocale);
			} else {
				LOGGER.warn("No HTML body template found for person " + sendToPerson.getObjectID() + ": " + sendToPerson.getFullName() + ": " + personLocale);
			}
			return false;
		}
		//Map<String, Object> root = getRootContext(sendToPerson, personLocale, plainEmail, pickerRoles, localizedFieldChangesMap);
		if (root==null) {
			//do not send mail (no notification needed)
			LOGGER.debug("Root context is null for person " + sendToPerson.getName() + " (" + personID + ")");
			return false;
		}
		StringWriter subjectWriter = new StringWriter();
		try {
			LOGGER.debug("Processing the subject template...");
			subjectTemplate.process(root, subjectWriter);
			LOGGER.debug("Subject template processed.");
		} catch (Exception e) {
			LOGGER.warn("Processing the subject template " + bodyTemplate.getName() + " failed with " + e.getMessage(), e);
			LOGGER.debug("Processed template: " + subjectWriter.toString());
			return false;
		}
		subjectWriter.flush();
		String messageSubject = subjectWriter.toString();
		StringWriter bodyWriter = new StringWriter();
		try {
			LOGGER.debug("Processing the body template...");
			bodyTemplate.process(root, bodyWriter);
			LOGGER.debug("Body template processed.");
		} catch (Exception e) {
			LOGGER.warn("Processing the body template " + bodyTemplate.getName() + " failed with " + e.getMessage(), e);
			LOGGER.debug("Processed template: " + bodyWriter.toString());
			return false;
		}
		bodyWriter.flush();
		String messageBody = bodyWriter.toString();
		String emailTo = sendToPerson.getEmail();
		//Assemble and send the mail
		try {
			LOGGER.debug("Just before sending...");
			LOGGER.debug("From: " + sendFromPerson.getLabel() + " " + sendFromPerson.getEmail());
			LOGGER.debug("To: " + emailTo);
			LOGGER.debug("Subject: " + messageSubject);
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("Body " + messageBody);
			}
			LOGGER.debug("Is plain: " + plainEmail);
			mailBean.sendMailInThread(sendToPerson, messageSubject, sendFromPerson, ccPerson, replayToPerson, messageBody, plainEmail);
			LOGGER.debug("Mail sent to " + emailTo);
			return true;
		} catch (Exception e) { // SMTPException
			// add code to propagate Email problems to user interface or
			// system log
			LOGGER.error("Sending the e-mail failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return false;
		}
	}

	/**
	 * Gets the sender text
	 * @param projectBean
	 * @param changedByPerson
	 * @return
	 */
	public static TPersonBean getSendFrom(TProjectBean projectBean, TPersonBean changedByPerson) {
		if (projectBean!=null) {
			String projectTrackSystemEmail=PropertiesHelper.getProperty(projectBean.getMoreProperties(), TProjectBean.MOREPPROPS.TRACK_EMAIL_PROPERTY);
			String projectEmailPersonName=PropertiesHelper.getProperty(projectBean.getMoreProperties(), TProjectBean.MOREPPROPS.EMAIL_PERSONAL_NAME);
			boolean useTrackFromAddress="true".equalsIgnoreCase(PropertiesHelper.getProperty(projectBean.getMoreProperties(), TProjectBean.MOREPPROPS.USE_TRACK_FROM_ADDRESS_DISPLAY));

			if (useTrackFromAddress && projectTrackSystemEmail!=null && projectTrackSystemEmail.length()>0){
				boolean useTrackFromAddressAsReplay="true".equalsIgnoreCase(PropertiesHelper.getProperty(projectBean.getMoreProperties(), TProjectBean.MOREPPROPS.USE_TRACK_FROM_AS_REPLAY_TO));
				if(useTrackFromAddressAsReplay==false){
					//verify address
					try {
						InternetAddress tmp = new InternetAddress(projectTrackSystemEmail);
						LOGGER.info(tmp.getAddress() + " is valid!");
						TPersonBean sendFrom = new TPersonBean("",projectEmailPersonName,projectTrackSystemEmail);
						return sendFrom;
					} catch (AddressException e) {
						LOGGER.error("The email address:'"+projectTrackSystemEmail+"' set in project" + projectBean.getLabel() + " is invalid!");
					}
				}
			}
		}
		String emailAddress=ApplicationBean.getInstance().getSiteBean().getSendFrom().getEmail();
		TPersonBean sendFromPersonbean;
		if(ApplicationBean.getInstance().getSiteBean().getUseTrackFromAddressDisplay().equals(TSiteBean.SEND_FROM_MODE.SYSTEM)) {
			//use the track-email address as sender address
			sendFromPersonbean = ApplicationBean.getInstance().getSiteBean().getSendFrom();
			sendFromPersonbean.setEmail(emailAddress);
		} else {
			// we use the one who changed the item as sender address
			//sendFromPersonbean = changedByPerson;
			sendFromPersonbean = new TPersonBean(changedByPerson.getFirstName(), changedByPerson.getLastName(), changedByPerson.getEmail()) ;
			if (sendFromPersonbean.getLastName()==null) {
				sendFromPersonbean.setLastName("");
			}
			sendFromPersonbean.setLastName(sendFromPersonbean.getLastName() + ApplicationBean.getInstance().getSiteBean().getTrackEmail());
		}
		LOGGER.debug("send from:"+sendFromPersonbean.getFullName()+": "+sendFromPersonbean.getEmail());
		return sendFromPersonbean;
	}
	public static TPersonBean getReplayTo(TProjectBean projectBean, TPersonBean changedByPerson) {
		if (projectBean!=null) {
			String projectTrackSystemEmail=PropertiesHelper.getProperty(projectBean.getMoreProperties(), TProjectBean.MOREPPROPS.TRACK_EMAIL_PROPERTY);
			String projectEmailPersonName=PropertiesHelper.getProperty(projectBean.getMoreProperties(), TProjectBean.MOREPPROPS.EMAIL_PERSONAL_NAME);
			boolean useTrackFromAddressDisplay="true".equalsIgnoreCase(PropertiesHelper.getProperty(projectBean.getMoreProperties(), TProjectBean.MOREPPROPS.USE_TRACK_FROM_ADDRESS_DISPLAY));

			if (useTrackFromAddressDisplay && projectTrackSystemEmail!=null && projectTrackSystemEmail.length()>0){
				boolean useTrackFromAddressAsReplay="true".equalsIgnoreCase(PropertiesHelper.getProperty(projectBean.getMoreProperties(), TProjectBean.MOREPPROPS.USE_TRACK_FROM_AS_REPLAY_TO));
				if(useTrackFromAddressAsReplay){
					//verify address
					try {
						InternetAddress tmp = new InternetAddress(projectTrackSystemEmail);
						LOGGER.info(tmp.getAddress() + " is valid!");
						TPersonBean sendFrom = new TPersonBean("",projectEmailPersonName,projectTrackSystemEmail);
						return sendFrom;
					} catch (AddressException e) {
						LOGGER.error("The email address:'"+projectTrackSystemEmail+"' set in project" + projectBean.getLabel() + " is invalid!");
					}
				}
			}
		}
		return null;
	}

	/**
	 * Converts the HTML long text to plain text
	 * @param text
	 * @return
	 */
	protected static String convertHTML2Text(String text) {
		if (text==null) {
			return "";
		}
		try {
			return Html2Text.getNewInstance().convert(text);
		} catch (Exception e) {
			LOGGER.warn("Converting the HTML to plain text failed with " + e.getMessage(), e);
			return text;
		}
	}

	/**
	 * Set the localized change detail strings depending on the operation (add/change/remove)
	 * and on field length (short/long field, where long field can be description or coomment)
	 * @param fieldChange
	 * @param longFields
	 * @param locale
	 * @param isNew whether creating a new issue (isCreate || isCopy) or editing an existing one
	 */
	protected static void setLocalizedChangeDetail(FieldChange fieldChange, List<Integer> longFields, Locale locale, boolean isNew) {
		//if fieldID==null for example the budget/expense description has no explicit fieldID: in this case it is considered longField
		Integer fieldID = fieldChange.getFieldID();
		String fieldLabel = fieldChange.getLocalizedFieldLabel();
		String newValue = fieldChange.getNewShowValue();
		String oldValue = fieldChange.getOldShowValue();
		Object[] msgArguments;
		if (newValue==null) {
			newValue="";
		}
		if (oldValue==null) {
			oldValue="";
		}
		if (!newValue.equals(oldValue)) {
			if ("".equals(oldValue)) {
				//new value (by new issue, existing issue or by copy)
				if (fieldID==null || longFields.contains(fieldID)) {
						//long value added (description/comment/attachment)
						if (isNew) {
							//if new issue write only "Description" not "Description was added"
							fieldChange.setLocalizedChangeDetail(fieldLabel);
						} else {
							msgArguments = new Object[] {fieldLabel};
							fieldChange.setLocalizedChangeDetail(LocalizeUtil.getParametrizedString("item.history.wasAddedLong", msgArguments, locale));
						}
				} else {
					//short value added, used only in plain e-mails
					msgArguments = new Object[] {fieldLabel, newValue};
					fieldChange.setLocalizedChangeDetail(LocalizeUtil.getParametrizedString("item.history.wasAddedShort", msgArguments, locale));
				}
			} else {
				if ("".equals(newValue)) {
					//remove value (only by existing issue or by copy)
					if (fieldID==null || longFields.contains(fieldID)) {
						//long value removed (description or comment)
						msgArguments = new Object[] {fieldLabel};
						fieldChange.setLocalizedChangeDetail(LocalizeUtil.getParametrizedString("item.history.wasRemovedLong", msgArguments, locale));
					} else {
						//short value removed, used only in plain e-mails
						msgArguments = new Object[] {fieldLabel, oldValue};
						fieldChange.setLocalizedChangeDetail(LocalizeUtil.getParametrizedString("item.history.wasRemovedShort", msgArguments, locale));
					}
				} else {
					//change value (only by existing issue or by copy)
					if (fieldID==null || longFields.contains(fieldID)) {
							//long value changed (description or comment)
							msgArguments = new Object[] {fieldLabel};
							fieldChange.setLocalizedChangeDetail(LocalizeUtil.getParametrizedString("item.history.wasChangedLong", msgArguments, locale));
					} else {
						//short value changed, used only in plain e-mails
						msgArguments = new Object[] {fieldLabel, oldValue, newValue};
						fieldChange.setLocalizedChangeDetail(LocalizeUtil.getParametrizedString("item.history.wasChangedShort", msgArguments, locale));
					}
				}
			}
		}
	}

	/**
	 * Gets the project's or the nearest ancestor project's notify settings
	 * @param personID for this person if specified, otherwise the default project settings of the project or the nearest ancestor project
	 * @param projectID
	 * @return
	 */
	protected static TNotifySettingsBean getNearestNotifySettings(Integer personID, Integer projectID) {
		while (projectID!=null) {
			TNotifySettingsBean notifySettingsBean = null;
			if (personID==null) {
				notifySettingsBean = NotifySettingsBL.loadDefaultByProject(projectID);
			} else {
				notifySettingsBean = NotifySettingsBL.loadOwnByPersonAndProject(personID, projectID);
			}
			if (notifySettingsBean!=null) {
				LOGGER.debug("Notify setting found for projectID " + projectID + " and person " + (personID==null?"no person":personID));
				return notifySettingsBean;
			} else {
				TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
				if (projectBean==null) {
					projectID = null;
					break;
				} else {
					projectID = ((TProjectBean)projectBean).getParent();
				}
			}
		}
		return null;
	}
}
