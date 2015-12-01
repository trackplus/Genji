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



package com.aurel.track.util.event;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.internet.InternetAddress;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.mailTemplate.MailTemplateBL;
import com.aurel.track.admin.user.profile.ProfileBL;
import com.aurel.track.beans.TBasketBean;
import com.aurel.track.beans.TMailTemplateBean;
import com.aurel.track.beans.TMailTemplateDefBean;
import com.aurel.track.beans.TPersonBasketBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.item.ItemBL;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.emailHandling.MailSender;
import com.aurel.track.util.event.parameters.ReminderEventParam;

import freemarker.template.Configuration;
import freemarker.template.Template;


/**
 * Mail handler listener for reminder e-mails for due or overdue items.
 * It is being called from the EmailReminderJob. 
 * @author Joerg Friedrich
 *
 */
public class FreeMarkerMailHandlerReminder implements IEventSubscriber {

	private static final Logger LOGGER = LogManager.getLogger(FreeMarkerMailHandlerReminder.class);
	private List<TWorkItemBean> originatorItems;
	private List<TWorkItemBean> managerItems; 
	private List<TWorkItemBean> responsibleItems;
	private Map<Integer, List<TPersonBasketBean>> personDateBasketItemsMap;
	private Map<Integer, List<TPersonBasketBean>> personTimeBasketItemsMap;
	private TPersonBean personBean;
	private Locale locale;

	private static String ORIGINATOR_ITEMS = "originatorItems";
	private static String RESPONSIBLE_ITEMS = "responsibleItems";
	private static String MANAGER_ITEMS = "managerItems";
	private static String REMINDER_BASKET_ITEMS = "reminderBasketItems";
	private static String DELEGATED_BASKET_ITEMS = "delegatedBasketItems";
	
	private static String HAS_ORIGINATOR_ITEMS = "hasOriginatorItems";
	private static String HAS_RESPONSIBLE_ITEMS = "hasResponsibleItems";
	private static String HAS_MANAGER_ITEMS = "hasManagerItems";
	private static String HAS_REMINDER_BASKET_ITEMS = "hasReminderBasketItems";
	private static String HAS_DELEGATED_BASKET_ITEMS = "hasDelegatedBasketItems";
	
	/**
	 * Get the events this class is interested in
	 * Always one event is present at the same time 
	 * (IEventSubscriber.getInterestedEvents())
	 */
	@Override
	public List<Integer> getInterestedEvents() {
		List<Integer> events = new ArrayList<Integer>();
		events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_USER_REMINDER));
		return events;
	}

	/**
	 * The operations to be executed on event
	 * IEventSubscriber.update()
	 */
	@Override
	public boolean update(List<Integer> events, Object eventContextObject) {
		ReminderEventParam reminderEventParam = (ReminderEventParam)eventContextObject;
		personBean = reminderEventParam.getReceiver();
		locale = personBean.getLocale();
		boolean isPlain=false;
		if (ProfileBL.EMAIL_TYPE.PLAIN.equals(personBean.getPrefEmailType())) {
			isPlain = true;
		}
		originatorItems = reminderEventParam.getOriginatorItems();
		managerItems = reminderEventParam.getManagerItems();
		responsibleItems = reminderEventParam.getResponsibleItems();
		personDateBasketItemsMap = reminderEventParam.getPersonDateBasketItemsMap();
		personTimeBasketItemsMap = reminderEventParam.getPersonTimeBasketItemsMap();
		TMailTemplateBean mailTemplateBean = getMailTemplateBean();
		TMailTemplateDefBean mailTemplateDefBean = getMailTemplateDefBean(mailTemplateBean, personBean);
		Boolean hasOriginatorItems = (originatorItems != null && originatorItems.size() > 0);
		Boolean hasManagerItems = (managerItems != null && managerItems.size() > 0);
		Boolean hasResponsibleItems = (responsibleItems != null && responsibleItems.size() > 0);
		List<TPersonBasketBean> reminderDateBasketItems = personDateBasketItemsMap.get(TBasketBean.BASKET_TYPES.CALENDAR);
		List<TPersonBasketBean> reminderTimeBasketItems = personTimeBasketItemsMap.get(TBasketBean.BASKET_TYPES.CALENDAR);
		List<TPersonBasketBean> delegatedDateBasketItems = personDateBasketItemsMap.get(TBasketBean.BASKET_TYPES.DELEGATED);
		List<TPersonBasketBean> delegatedTimeBasketItems = personTimeBasketItemsMap.get(TBasketBean.BASKET_TYPES.DELEGATED);
		String serverurl = ApplicationBean.getInstance().getSiteBean().getServerURL();
		String contextName = ApplicationBean.getInstance().getServletContext().getContextPath();
		serverurl = serverurl + contextName + "/";
		List<ReminderRow> reminderDateBasketRows = getBasketReminderRows(reminderDateBasketItems, false, serverurl);
		List<ReminderRow> reminderTimeBasketRows = getBasketReminderRows(reminderTimeBasketItems, true, serverurl);
		List<ReminderRow> reminderBasketRows = new LinkedList<ReminderRow>();
		if (reminderDateBasketRows!=null) {
			reminderBasketRows.addAll(reminderDateBasketRows);
		}
		if (reminderTimeBasketRows!=null) {
			reminderBasketRows.addAll(reminderTimeBasketRows);
		}
		Boolean hasReminderBasketItems = !reminderBasketRows.isEmpty();
		List<ReminderRow> delegatedDateBasketRows = getBasketReminderRows(delegatedDateBasketItems, false, serverurl);
		List<ReminderRow> delegatedTimeBasketRows = getBasketReminderRows(delegatedTimeBasketItems, true, serverurl);
		List<ReminderRow> delegatedBasketRows = new LinkedList<ReminderRow>();
		if (delegatedDateBasketRows!=null) {
			delegatedBasketRows.addAll(delegatedDateBasketRows);
		}
		if (delegatedTimeBasketRows!=null) {
			delegatedBasketRows.addAll(delegatedTimeBasketRows);
		}
		Boolean hasDelegatedBasketItems = !delegatedBasketRows.isEmpty();
		Map<String, Object> root = new HashMap<String, Object>();
		root.put(ORIGINATOR_ITEMS, getReminderRows(originatorItems, serverurl, SystemFields.ORIGINATOR));
		root.put(RESPONSIBLE_ITEMS, getReminderRows(responsibleItems, serverurl, SystemFields.RESPONSIBLE));
		root.put(MANAGER_ITEMS, getReminderRows(managerItems, serverurl, SystemFields.MANAGER));
		root.put(REMINDER_BASKET_ITEMS, reminderBasketRows);
		root.put(DELEGATED_BASKET_ITEMS, delegatedBasketRows);
		root.put(HAS_ORIGINATOR_ITEMS, hasOriginatorItems.toString());
		root.put(HAS_RESPONSIBLE_ITEMS, hasResponsibleItems.toString());
		root.put(HAS_MANAGER_ITEMS, hasManagerItems.toString());
		root.put(HAS_REMINDER_BASKET_ITEMS, hasReminderBasketItems.toString());
		root.put(HAS_DELEGATED_BASKET_ITEMS, hasDelegatedBasketItems.toString());
		String messageSubject = prepareSubject(root, mailTemplateDefBean, personBean);
		String messageBody = prepareMessage(root, mailTemplateDefBean, personBean);
		try {
			InternetAddress from = new InternetAddress(ApplicationBean.getInstance().getSiteBean().getTrackEmail(),
					ApplicationBean.getInstance().getSiteBean().getEmailPersonalName());
			InternetAddress to = new InternetAddress(personBean.getEmail(), personBean.getFullName());
			MailSender mailSender = new MailSender(from, to, messageSubject, messageBody, isPlain);
			mailSender.start();

		} catch (Exception e) {
			LOGGER.error("Can't create from/to addresses " + e.getMessage());
		}
		return true;
	}

	private String prepareSubject(Map<String, Object> root, TMailTemplateDefBean mailTemplateDefBean, TPersonBean personBean) {
		Template fmtemplate = getFreemarkerTemplateSubject(mailTemplateDefBean);
		StringWriter w = new StringWriter();
		try {
			fmtemplate.process(root, w);
		} catch (Exception e) {
			LOGGER.error("Processing reminder template " + fmtemplate.getName() + " failed with " + e.getMessage());
		}
		w.flush();
		return w.toString();
	}

	private String prepareMessage(Map<String, Object> root, TMailTemplateDefBean mailTemplateDefBean, TPersonBean personBean) {
		Template fmtemplate = getFreemarkerTemplateBody(mailTemplateDefBean);
		StringWriter w = new StringWriter();
		try {
			fmtemplate.process(root, w);
		} catch (Exception e) {
			LOGGER.error("Processing reminder template " + fmtemplate.getName() + " failed with " + e.getMessage());
		}
		w.flush();
		return w.toString();
	}
	
	protected Collection<ReminderRow> getReminderRows(List<TWorkItemBean> items, String serverurl, int fieldID) {
		Collection<ReminderRow> ritems = new ArrayList<ReminderRow>();
		if (items != null) {
			String projectName = "";
			//String prefix = "";
			long lead = 0;
			if (personBean != null && personBean.getEmailLead() != null) {
				lead = personBean.getEmailLead().longValue()*24*60*60*1000;
			}
			Date now = new Date();
			Date leadDate = new Date(now.getTime()+lead);
			for (TWorkItemBean twib: items) {
				TProjectBean projectBean =  LookupContainer.getProjectBean(twib.getProjectID());//projects.get(twib.getProjectID());
				if (projectBean != null) {
					projectName = projectBean.getLabel();
				}
				String itemID = ItemBL.getItemNo(twib);
				boolean isLateCommitted = false;
				boolean isDueSoonCommitted = false;
				boolean isLateTarget = false;
				boolean isDueSoonTarget = false;
				Date endDate = twib.getEndDate();
				Date topDownEndDate = twib.getTopDownEndDate();
				
				if (endDate!=null) {
					isLateCommitted = endDate.before(now);
					isDueSoonCommitted = endDate.before(leadDate);
					
				} else {
					if (topDownEndDate!=null) {
						isLateTarget = topDownEndDate.before(now);
						isDueSoonTarget = topDownEndDate.before(leadDate);
					}
				}
				Date closestDate = null;
				if (isLateCommitted && isLateTarget) {
					if (endDate.before(topDownEndDate)) {
						closestDate = endDate;
					} else {
						closestDate = topDownEndDate;
					}
				} else {
					if (isLateCommitted) {
						closestDate = endDate;
					} else {
						if (isLateTarget) {
							closestDate = topDownEndDate;
						} else {
							if (isDueSoonCommitted && isDueSoonTarget) {
								if (endDate.before(topDownEndDate)) {
									closestDate = endDate;
								} else {
									closestDate = topDownEndDate;
								}
							} else {
								if (isDueSoonCommitted) {
									closestDate = endDate;
								} else {
									if (isDueSoonTarget) {
										closestDate = topDownEndDate;
									}
								}
							}
						}
					}
				}
				if (closestDate!=null) {
					String formattedDate = DateTimeUtils.getInstance().formatGUIDate(closestDate, locale);
					Integer raciPersonID = null;
					switch (fieldID) {
					case SystemFields.RESPONSIBLE:
						raciPersonID = twib.getOriginatorID();
						break;
					case SystemFields.ORIGINATOR:
					case SystemFields.MANAGER:
						raciPersonID = twib.getResponsibleID();
						break;
					}
					ReminderRow row = new ReminderRow(projectName, 
							itemID,
							twib.getSynopsis(), 
							formattedDate,
							"<a href='"+ serverurl + "printItem.action?key="+twib.getObjectID().toString() +"'>"+twib.getSynopsis()+"</a>",
							serverurl+"printItem.action?key="+twib.getObjectID().toString(),
							getRACIPersonMailTo(raciPersonID),
							isLateCommitted || isLateTarget , isDueSoonCommitted || isDueSoonTarget);
					ritems.add(row);
				}
			}	
		}
		return ritems;
	}

	private static String getRACIPersonMailTo(Integer raciPersonID) {
		String raciPersonName = "";
		if (raciPersonID!=null) {
			TPersonBean personBean = LookupContainer.getPersonBean(raciPersonID);
			if (personBean!=null) {
				raciPersonName = personBean.getName();
				String eMail = personBean.getEmail();
				if (eMail!=null) {
					raciPersonName = "<a href='mailto:" + eMail + "' target='_top'>" + raciPersonName + "</a>";
				}
			}
		}
		return raciPersonName;
	}
	
	/**
	 * Gets the basket reminder items with date or date and time
	 * @param personBasketItems
	 * @param withTime
	 * @param serverurl
	 * @return
	 */
	protected List<ReminderRow> getBasketReminderRows(List<TPersonBasketBean> personBasketItems, boolean withTime, String serverurl) {
		List<ReminderRow> reminderRows = null;
		if (personBasketItems!=null && !personBasketItems.isEmpty()) {
			reminderRows = new ArrayList<ReminderRow>();
			List<Integer> basketWorkItemIDs = new ArrayList<Integer>();
			Map<Integer, Date> dueDateMap = new HashMap<Integer, Date>();
			for (TPersonBasketBean personBasketBean : personBasketItems) {
				Integer workItemID = personBasketBean.getWorkItem();
				dueDateMap.put(workItemID, personBasketBean.getReminderDate());
				basketWorkItemIDs.add(workItemID);
			}
			List<TWorkItemBean> items = new LinkedList<TWorkItemBean>();
			if (!basketWorkItemIDs.isEmpty()) {
				items = ItemBL.loadByWorkItemKeys(GeneralUtils.createIntArrFromIntegerList(basketWorkItemIDs));
				if (items!=null) {
					for (TWorkItemBean item: items) {
						Integer workItemID = item.getObjectID();
						String projectName = "";
						TProjectBean projectBean =  LookupContainer.getProjectBean(item.getProjectID());
						if (projectBean != null) {
							projectName = projectBean.getLabel();
						}
						String itemID = ItemBL.getItemNo(item);
						Date date = dueDateMap.get(workItemID);
						if (date!=null) {
							String raciPersonName = getRACIPersonMailTo(item.getOriginatorID());
							String formattedDate = null;
							if (withTime) {
								formattedDate = DateTimeUtils.getInstance().formatGUIDateTime(date, locale);
							} else {
								formattedDate = DateTimeUtils.getInstance().formatGUIDate(date, locale);
							}
							ReminderRow row = new ReminderRow(projectName, 
															itemID,
															item.getSynopsis(), 
															formattedDate,
															"<a href='"+ serverurl+"printItem.action?key="+item.getObjectID().toString() +"'>"+item.getSynopsis()+"</a>",
															serverurl+"printItem.action?key="+item.getObjectID().toString(),
															raciPersonName,
															false, true);
							reminderRows.add(row);
						}
					}
				}
			}	
		}
		return reminderRows;
	}

	protected TMailTemplateBean getMailTemplateBean() {
		Integer eventID=IEventSubscriber.EVENT_POST_USER_REMINDER;
		Integer templateID= MailTemplateBL.findMailTemplateID(eventID, null, null);
		TMailTemplateBean mailTemplateBean=MailTemplateBL.getMailTemplate(templateID);
		return mailTemplateBean;
	}

	protected TMailTemplateDefBean getMailTemplateDefBean(TMailTemplateBean mailTemplateBean, TPersonBean personBean) {	
		return MailTemplateBL.getSystemMailTemplateDefBean(IEventSubscriber.EVENT_POST_USER_REMINDER, personBean);
	}


	/**
	 * Gets the localized Freemarker template for the message body
	 * @return
	 */
	private Template getFreemarkerTemplateBody(TMailTemplateDefBean mailTemplateDefBean) {
		String templateStr=mailTemplateDefBean.getMailBody();
		Template t=null;
		try {
			t = new Template("name", new StringReader(templateStr),new Configuration());
		} catch (Exception e) {
			LOGGER.error("Loading the template body for " + mailTemplateDefBean.getTheLocale() 
						+ ", textOnly = " + mailTemplateDefBean.getPlainEmail()
						+ ", subject = "  + mailTemplateDefBean.getMailSubject()
						+ " failed with " + e.getMessage(), e);
		}
		return t;
	}

	/**
	 * Gets the localized Freemarker template for the subject
	 * @return
	 */
	private Template getFreemarkerTemplateSubject(TMailTemplateDefBean mailTemplateDefBean) {
		String templateStr=mailTemplateDefBean.getMailSubject();
		Template t=null;
		try {
			t = new Template("name", new StringReader(templateStr), new Configuration());
		} catch (Exception e) {
			LOGGER.debug("Loading the template subject failed with " + e.getMessage(), e);
		}
		return t;
	}

	public class ReminderRow {
		private String project;
		private String itemId;
		private String title;
		private String endDate;
		private String titleWithLink;
		private String link;
		private Boolean isLate;
		private Boolean isDueSoon;
		//raci person is responsible for the author and manager items and author for responsible items
		private String raciPerson;

		public ReminderRow(String project, String itemId, String title,
				String endDate, String titleWithLink, String link, String raciPerson,
				boolean isLate, boolean isDueSoon) {
			this.project = project;
			this.itemId = itemId;
			this.title = title;
			this.endDate = endDate;
			this.titleWithLink = titleWithLink;
			this.link = link;
			this.raciPerson = raciPerson;
			this.isLate = isLate;
			this.isDueSoon = isDueSoon;
		}
		
		public String getProject() {
			return project;
		}

		public void setProject(String project) {
			this.project = project;
		}

		public String getItemId() {
			return itemId;
		}

		public void setItemId(String itemId) {
			this.itemId = itemId;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getEndDate() {
			return endDate;
		}

		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}

		public String getTitleWithLink() {
			return titleWithLink;
		}

		public void setTitleWithLink(String titleWithLink) {
			this.titleWithLink = titleWithLink;
		}

		public String getLink() {
			return link;
		}

		public void setLink(String link) {
			this.link = link;
		}
		
		public Boolean getIsLate() {
			return isLate;
		}

		public void setIsLate(Boolean isLate) {
			this.isLate = isLate;
		}
		
		public Boolean getIsDueSoon() {
			return isDueSoon;
		}

		public void setIsDueSoon(Boolean isDueSoon) {
			this.isDueSoon = isDueSoon;
		}

		public String getRaciPerson() {
			return raciPerson;
		}

		public void setRaciPerson(String raciPerson) {
			this.raciPerson = raciPerson;
		}
		
		

	}

}


