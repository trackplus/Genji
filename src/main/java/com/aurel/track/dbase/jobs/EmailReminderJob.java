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


package com.aurel.track.dbase.jobs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TPersonBasketBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.cluster.ClusterBL;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.operation.BasketBL;
import com.aurel.track.util.CalendarUtil;
import com.aurel.track.util.event.EventPublisher;
import com.aurel.track.util.event.IEventSubscriber;
import com.aurel.track.util.event.parameters.ReminderEventParam;

public class EmailReminderJob implements Job{

	private static final Logger LOGGER = LogManager.getLogger(EmailReminderJob.class);
	/**
	 * Here is where we do all the work for reminding. This routine will
	 * be called x times per day, depending on the trigger settings.
	 * Execution times are usually whole hours; 
	 * 
	 * Algorithm: For each user that likes to be reminded, and whose last
	 * email was being sent before today minus week/frequency configured
	 * by this user, search all issues
	 * where she is either originator, manager, or responsible, depending
	 * on her settings in her profile, and which have an entry in either the
	 * start date or the due date. Compare these dates with the current date.
	 * If the difference is less or equal the lead (buffer) she has
	 * configured for herself, collect this issue.
	 * Once through, mail her the collection, if it is not empty.
	 */
	@Override
	public void execute(JobExecutionContext context) {
		LOGGER.debug("execute EmailReminderJob....");
		if (ClusterBL.getIAmTheMaster()) {
			Date fireTime = context.getFireTime();
			Date nextFireTime = context.getNextFireTime();
			LOGGER.debug("Process reminder emails ...");
			try {
				getReminders(fireTime, nextFireTime);
			} catch (Exception e) {
				LOGGER.error("Problem with running scheduler " + e.getMessage());
			}
		}
	}
	
	/**
	 * Load all people that have registered for email reminders
	 */
	private static void getReminders(Date fireTime, Date nextFireTime) throws Exception {
		Date today = getToday();
		List<TPersonBean> dailyReminderPeople = PersonBL.loadDailyReminderPersons(today);
		//fromDate is today midnight and not fireTime because the basket reminder might not have time part
		//then after a server start on a specific day the basked reminder might not be sent for the current day 
		List<TPersonBean> basketReminderPeople = PersonBL.loadBasketReminderPersons(today, nextFireTime);
		Map<Integer, TPersonBean> reminderPersonsMap = new HashMap<Integer, TPersonBean>();
		Set<Integer> dailyPersonsSet = new HashSet<Integer>();
		if (dailyReminderPeople!=null) {
			for (TPersonBean personBean : dailyReminderPeople) {
				Integer personID = personBean.getObjectID();
				LOGGER.debug(personBean.getName() + " needs a daily reminder.");
				reminderPersonsMap.put(personID, personBean);
				dailyPersonsSet.add(personID);
			}
		}
		Set<Integer> basketPersonsSet = new HashSet<Integer>();
		if (basketReminderPeople!=null) {
			for (TPersonBean personBean : basketReminderPeople) {
				Integer personID = personBean.getObjectID();
				LOGGER.debug(personBean.getName() + " needs a basket reminder.");
				reminderPersonsMap.put(personID, personBean);
				basketPersonsSet.add(personID);
			}
		}
		// Now walk through the list, one by one
		for (Map.Entry<Integer, TPersonBean> entry : reminderPersonsMap.entrySet()) {
			Integer personID = entry.getKey();
			getReminderEmail(entry.getValue(), dailyPersonsSet.contains(personID), basketPersonsSet.contains(personID), today, fireTime, nextFireTime);
		}
		return ;
	}
	
	/*
	 *  Returns a complete reminder email, containing all database
	 *  entries which are due and not closed
	 */
	private static void getReminderEmail(TPersonBean personBean, boolean dailyReminder, boolean basketReminder, Date today, Date fireTime, Date nextFireTime) {
		boolean itemsFound = false;
		List<TWorkItemBean> originatorList = new ArrayList<TWorkItemBean>();
		List<TWorkItemBean> managerList = new ArrayList<TWorkItemBean>();
		List<TWorkItemBean> responsibleList = new ArrayList<TWorkItemBean>();
		//1. daily reminder as RACI role 
		if (dailyReminder && isRemindDue(personBean)) {
			Date dailyReminderEndDateLimit = getDailyReminderEndDateLimit(personBean.getEmailLead());
			List<TWorkItemBean> reminderTasks = ItemBL.loadReminderWorkItems(
					personBean.getObjectID(), personBean.getRemindMeAsOriginator(),
					personBean.getRemindMeAsManager(), personBean.getRemindMeAsResponsible(), dailyReminderEndDateLimit);
			if (reminderTasks==null || reminderTasks.isEmpty()) {
				LOGGER.debug("There were no due RACI tasks.");
			} else {
				itemsFound = true;
			}
			LOGGER.debug("Now iterating over all due RACI tasks...");
			originatorList = new ArrayList<TWorkItemBean>();
			managerList = new ArrayList<TWorkItemBean>();
			responsibleList = new ArrayList<TWorkItemBean>();
			for (TWorkItemBean task : reminderTasks) {
				if (task.getResponsibleID().equals(personBean.getObjectID())) {
					//responsible has priority if more RACI roles are involved
					LOGGER.trace("Responsible: " + task.getObjectID() + "  " + task.getSynopsis());
					responsibleList.add(task);
				} else {
					if (task.getOwnerID().equals(personBean.getObjectID())) {
						LOGGER.trace("Manager: " + task.getObjectID() + "  " + task.getSynopsis());
						managerList.add(task);
					} else {
						if (task.getOriginatorID().equals(personBean.getObjectID())) {
							LOGGER.trace("Originator: " + task.getObjectID() + "  " + task.getSynopsis());
							originatorList.add(task);
						}
					}
				}
			}
		}
		//2. basket reminder
		Map<Integer, List<TPersonBasketBean>> personDateBasketItemsMap = new HashMap<Integer, List<TPersonBasketBean>>();
		Map<Integer, List<TPersonBasketBean>> personTimeBasketItemsMap = new HashMap<Integer, List<TPersonBasketBean>>();
		if (basketReminder) {
			//a. daily for basket items without time part only date
			Date emailLastReminded = personBean.getEmailLastReminded();
			if (emailLastReminded==null || !CalendarUtil.sameDay(emailLastReminded, today)) {
				//only once a day
				List<TPersonBasketBean> personBasketBeansWithDate = BasketBL.loadPersonBasketItemsByDate(personBean.getObjectID(), today);
				if (personBasketBeansWithDate!=null && !personBasketBeansWithDate.isEmpty()) {
					itemsFound = true;
					for (TPersonBasketBean personBasketBean : personBasketBeansWithDate) {
						Date dueDate = personBasketBean.getReminderDate();
						if (dueDate!=null) {
							Integer basketID = personBasketBean.getBasket();
							List<TPersonBasketBean> basketItemList = personDateBasketItemsMap.get(basketID);
							if (basketItemList==null) {
								basketItemList = new LinkedList<TPersonBasketBean>();
								personDateBasketItemsMap.put(basketID, basketItemList);
							}
							basketItemList.add(personBasketBean);
						}
					}
					LOGGER.debug("There were " + personBasketBeansWithDate.size() + " tasks in person date baskets.");
				} else {
					LOGGER.debug("There were no tasks in person date baskets.");
				}
			} else {
				LOGGER.debug("Person date baskets items already reminded for " + today);
			}
			//b. basket items due between fire times (having a time part also)
			List<TPersonBasketBean> personBasketBeansWithTime = BasketBL.loadPersonBasketItemsByTime(
					personBean.getObjectID(), fireTime, nextFireTime);
			if (personBasketBeansWithTime!=null && !personBasketBeansWithTime.isEmpty()) {
				itemsFound = true;
				for (TPersonBasketBean personBasketBean : personBasketBeansWithTime) {
					Integer basketID = personBasketBean.getBasket();
					List<TPersonBasketBean> basketItemList = personTimeBasketItemsMap.get(basketID);
					if (basketItemList==null) {
						basketItemList = new LinkedList<TPersonBasketBean>();
						personTimeBasketItemsMap.put(basketID, basketItemList);
					}
					basketItemList.add(personBasketBean);
				}
				LOGGER.debug("There were " + personBasketBeansWithTime.size() + " tasks in person time baskets.");
			} else {
				LOGGER.debug("There were no tasks in person time baskets.");
			}
		}
		
		try {
			if (itemsFound) {
				//Map<Integer, TProjectBean> projectBeanMap = GeneralUtils.createMapFromList(projectBeanList);
				ReminderEventParam params = new ReminderEventParam();
				params.setOriginatorItems(originatorList);
				params.setManagerItems(managerList);
				params.setResponsibleItems(responsibleList);
				params.setPersonDateBasketItemsMap(personDateBasketItemsMap);
				params.setPersonTimeBasketItemsMap(personTimeBasketItemsMap);
				//params.setDelegatedBasketItems(delegatedBasketItems);
				//params.setProjects(projectBeanMap);
				params.setReceiver(personBean);
				EventPublisher evp = EventPublisher.getInstance();
				if (evp != null) {
					List<Integer> events = new ArrayList<Integer>();
					events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_USER_REMINDER));
					evp.notify(events, params);
				}
				personBean.setEmailLastReminded(new Date());
				PersonBL.saveSimple(personBean);
			}
		} catch (Exception e) {
			LOGGER.debug("Error occurred while looking for due task list.");
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
	}
	
	/**
	 * Get today without time fields 
	 * @return
	 */
	private static Date getToday() {
		Calendar calendar = Calendar.getInstance();
		CalendarUtil.clearTime(calendar);
		return calendar.getTime();
	}
	
	private static Date getDailyReminderEndDateLimit(Integer lead) {
		Calendar calendar = Calendar.getInstance();
		CalendarUtil.clearTime(calendar);
		if (lead==null) {
			lead = Integer.valueOf(1);
		}
		calendar.add(Calendar.DATE, lead);
		return calendar.getTime();
	}
	
	/**
	 * Whether reminder e-mail is due
	 * @param personBean
	 * @return
	 */
	private static boolean isRemindDue(TPersonBean personBean) {
		List<Integer> reminderDays = personBean.getReminderDays();
		if (reminderDays==null || reminderDays.isEmpty()) {
			return false;
		}
		Calendar calToday = new GregorianCalendar();
		calToday.setTime(new Date());
		int noOfDay = calToday.get(Calendar.DAY_OF_WEEK);
		for (Integer reminderDay : reminderDays) {
			if (reminderDay==noOfDay) {
				//break;
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Reminder is due for person " + personBean.getName());
				}
				return true;
			}
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Reminder is not due for person " + personBean.getName());
		}
		return false;
	}
}
