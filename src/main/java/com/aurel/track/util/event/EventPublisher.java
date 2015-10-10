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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * This class handles events related to administrative changes 
 * or changes to issues. It follows the observer pattern and 
 * notifies attached observers in case an event arises.
 * @author Joerg Friedrich <joerg.friedrich@trackplus.com>
 * @version $Revision: 1229 $ $Date: 2014-04-03 14:44:50 +0200 (Do, 03 Apr 2014) $
 */

public class EventPublisher {
	
	private static final Logger LOGGER = LogManager.getLogger(EventPublisher.class);
	//singleton instance
	protected static EventPublisher thisEventPublisher = null;
	/**
	 * map with the event subscribers for the events:
	 * 	- key: eventID
	 * 	- value: list of subscribers interested on that event
	 */
	protected static Map<Integer, List<IEventSubscriber>> eventSubscribersMap = 
		new HashMap<Integer, List<IEventSubscriber>>();
	
	protected EventPublisher() {
		return;
	}
	
	public static EventPublisher getInstance() {
		if (thisEventPublisher == null) {
			eventSubscribersMap.clear();
			thisEventPublisher = new EventPublisher();
		}
		return thisEventPublisher;
	}
	
	/**
	 * Initialize the event subscriber map
	 */
	public static void init() {
		// get the list of event subscribers (observers) known to the system
		List<IEventSubscriber> eventSubscribersList = getEventSubscriberLists();
		if (eventSubscribersList != null) {
			for (IEventSubscriber eventSubscriber : eventSubscribersList) {
				getInstance().attach(eventSubscriber);
				LOGGER.debug("Added event subscriber "
							 + eventSubscriber.getClass().getPackage().getName()
							 + "."
							 + eventSubscriber.getClass().getName()
							 + " to observer list");
			}
		}
		else {
			LOGGER.debug("No event subscribers found.");
		}
	}
	
	/**
	 * Attach a subscriber to an event
	 * @param subscriber
	 */
	public void attach(IEventSubscriber subscriber) {
		List<Integer> events = subscriber.getInterestedEvents();
		if (events!=null) {
			for (Integer event : events) {
				List<IEventSubscriber> subscriberList = eventSubscribersMap.get(event);
				if (subscriberList==null) {
					subscriberList = new ArrayList<IEventSubscriber>();
					eventSubscribersMap.put(event, subscriberList);
				}
				subscriberList.add(subscriber);
			}
		}
	}
	
	/**
	 * Detach a subscriber form an event
	 * @param subscriber
	 */
	public void detach(IEventSubscriber subscriber) {
		List<Integer> events = subscriber.getInterestedEvents();
		if (events!=null) {
			for (Integer event : events) {
				List<IEventSubscriber> subscriberList = eventSubscribersMap.get(event);
				if (subscriberList!=null) {
					subscriberList.remove(subscriber);
				}
			}
		}
	}
	
	/**
	 * Set for each subscriber the events it is interested in from the list of events fired
	 * then notify the subscribers.
	 * More events can be triggered at the same time (by editing an issue more events can be triggered)
	 * and each subscriber may be interested in more than one event 
	 * (for example email notification should be sent for any combination of the 
	 * issue modification events but just one, even if more events was fired)
	 * These events are not instantly sent as they happen, but gathered together 
	 * during a complex operation (ex. editing of an issue), and they are sent all at the same time
	 * just at the end of the operation with a single notify call. 
	 * @param events
	 * @param eventContextObject
	 */
	public void notify(List<Integer> events, Object eventContextObject) {
		if (events!=null) {
			Map<IEventSubscriber, List<Integer>> subscriberMap = new HashMap<IEventSubscriber, List<Integer>>();
			for (Integer event : events) {
				List<IEventSubscriber> subscriberList = eventSubscribersMap.get(event);
				if (subscriberList!=null) {
					for (IEventSubscriber eventSubscriber : subscriberList) {
						List<Integer> subscriberEvents = subscriberMap.get(eventSubscriber);
						if (subscriberEvents==null) {
							subscriberEvents = new LinkedList<Integer>();
							subscriberMap.put(eventSubscriber, subscriberEvents);
						}
						subscriberEvents.add(event);
					}
				}
			}
			for (Map.Entry<IEventSubscriber,  List<Integer>> entry : subscriberMap.entrySet()) {
				IEventSubscriber eventSubscriber = entry.getKey();
				List<Integer> subscriberEvents = entry.getValue();
				try {
					eventSubscriber.update(subscriberEvents, eventContextObject);
				} catch (Exception e) {
					LOGGER.error("Executing update for " + eventSubscriber.getClass() + " failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
		}
	}
	
	/**
	 * Gets a list of defined event subscribers
	 * @return
	 */
	private static List<IEventSubscriber> getEventSubscriberLists() {		   		
		List<IEventSubscriber> eventSubsribers = new LinkedList<IEventSubscriber>();
		eventSubsribers.add(new EventHandlerSystemStarted());
		eventSubsribers.add(new FreemarkerMailHandlerIssueChange());
		eventSubsribers.add(new FreeMarkerMailHandlerBudgetChange());
		eventSubsribers.add(new FreeMarkerMailHandlerReminder());
		eventSubsribers.add(FieldChangeScriptHandler.getInstance());
		return eventSubsribers;
	}
	
}
