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

package com.aurel.track.report.datasource.openedVsClosed;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.item.history.HistorySelectValues;
import com.aurel.track.report.datasource.TimeIntervalWithStatusDatasource;
import com.aurel.track.report.datasource.TimeIntervalWithStatusDatasource.TIME_INTERVAL;
import com.aurel.track.util.DateTimeUtils;

public class OpenedClosedBL {
	private static final Logger LOGGER = LogManager.getLogger(OpenedClosedBL.class);		
	
	static Document convertToDOM(Collection<OpenedClosedDateData> openClosedJavaBeans, Locale locale,String personName, int selectedTimeInterval) {		
		Document dom = null;
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
			DocumentBuilder builder = factory.newDocumentBuilder ();
			dom = builder.newDocument ();	
		}catch (FactoryConfigurationError e){
			LOGGER.error ("Creating the DOM document failed with FactoryConfigurationError:" + e.getMessage(), e);
			return null;
		}catch (ParserConfigurationException e){
			LOGGER.error ("Creating the DOM document failed with ParserConfigurationException: " + e.getMessage(), e);
			return null;
		}
		Element root = dom.createElement ("track-report");
		Element createdBy =createDomElement("createdBy",personName,dom);
		root.appendChild(createdBy);    
		Element timeInterval =createDomElement("timeInterval",String.valueOf(selectedTimeInterval),dom);
		root.appendChild(timeInterval);
		for (OpenedClosedDateData openedClosedJavaBean : openClosedJavaBeans) {
			Element period = dom.createElement ("period");
			period.appendChild(createDomElement("date", DateTimeUtils.getInstance().formatISODateTime(openedClosedJavaBean.getDatePeriod()), dom));
			String opened = null;
			if (openedClosedJavaBean.getOpened()!=null) {
				opened = openedClosedJavaBean.getOpened().toString();
			}
			period.appendChild(createDomElement("opened", opened, dom));
			String closed = null;
			if (openedClosedJavaBean.getClosed()!=null) {
				closed = openedClosedJavaBean.getClosed().toString();
			}
			period.appendChild(createDomElement("closed", closed, dom));
			root.appendChild (period);
		}
		dom.appendChild (root);
		return dom;
	}
	
	/**
	 * @param elementName
	 * @param elementValue
	 * @param dom
	 * @return
	 */
	private static Element createDomElement (String elementName, String elementValue, Document dom) {
		Element element = dom.createElement (elementName);
		if (elementValue == null || "".equals(elementValue.trim())) {
			element.appendChild (dom.createTextNode (""));
		} else {
			element.appendChild (dom.createTextNode(elementValue));
		}
		return element;
	}

	static void openedClosedJavaBean(SortedMap<Date, OpenedClosedDateData> openedClosedTimeSliceMap, SortedMap<Date, Object> countMap, boolean opened) {
		//SortedMap openedClosed = new TreeMap();
		if (countMap!=null) {
			for (Date date : countMap.keySet()) {
				OpenedClosedDateData openedClosedTimeSlice = (OpenedClosedDateData)openedClosedTimeSliceMap.get(date);
				if (openedClosedTimeSlice==null) {
					openedClosedTimeSlice = new OpenedClosedDateData();
					openedClosedTimeSliceMap.put(date, openedClosedTimeSlice);
				}
				openedClosedTimeSlice.setDatePeriod(date);
				if (opened) {
					openedClosedTimeSlice.setOpened((Integer)countMap.get(date));
				} else {
					openedClosedTimeSlice.setClosed((Integer)countMap.get(date));
				}
			}
		}
	}


	/**
	 * Get the workItems created between the two dates
	 * @param dateFrom
	 * @param dateTo
	 * @param workItemBeansList should remain unmodified for later use,
	 *  consequently if dateFrom or dateTo is present create a new collection
	 * @return
	 */
	static List<TWorkItemBean> filterNewWorkItemsForPeriod(Date dateFrom, Date dateTo, 
			List<TWorkItemBean> workItemBeansList) {
		List<TWorkItemBean> filteredWorkItemBeansList;
		if (dateFrom==null && dateTo==null) {
			return workItemBeansList;
		} else {
			filteredWorkItemBeansList = new LinkedList<TWorkItemBean>();
		}
		if (workItemBeansList!=null) {
			for (TWorkItemBean workItemBean : workItemBeansList) {
				Date createdDate = workItemBean.getCreated();
				if (!((dateFrom!=null && createdDate.before(dateFrom)) || (dateTo!=null && createdDate.after(dateTo)))) {
					filteredWorkItemBeansList.add(workItemBean);
				}
			}
		}	
		return filteredWorkItemBeansList;
	}

	/**
	* Create a map of hierarchical data with TWorkItemBeans in the periods
	* -	key: year
	* -	value: map
	* 			-	key: period
	* 			-	Set of TStateChangeBeans, one for each workItem   
	* @param statusChangeList
	* @param timeInterval
	* @param sample sample (only last from the period) or action (all statuses during a period) 
	* @param workItemIDs output parameter to gather the workItems involved
	* @return
	*/
	static SortedMap<Integer, SortedMap<Integer, Integer>> getNewWorkItemsMap(List<TWorkItemBean> workItemList, int timeInterval) {
		SortedMap<Integer, SortedMap<Integer, Integer>> yearToIntervalToWorkItemCount = new TreeMap<Integer, SortedMap<Integer, Integer>>();
		if (workItemList!=null) {
			Calendar calendarCreated = Calendar.getInstance();
			int calendarInterval = TimeIntervalWithStatusDatasource.getCalendarInterval(timeInterval);
			
			for (TWorkItemBean workItemBean : workItemList) {
				if (workItemBean.getCreated()!=null) {//normally not needed but somehow the database might contain workItems with no creation date 
					calendarCreated.setTime(workItemBean.getCreated());
					int yearValue = calendarCreated.get(Calendar.YEAR);
					int intervalValue = calendarCreated.get(calendarInterval);
					if (TIME_INTERVAL.WEEK==timeInterval && 
							calendarCreated.get(Calendar.MONTH)==11 && intervalValue==1) {
						yearValue = yearValue+1;
					}
					SortedMap<Integer, Integer> intervalToWorkItemBeans = yearToIntervalToWorkItemCount.get(Integer.valueOf(yearValue));
					if (intervalToWorkItemBeans==null) {
						intervalToWorkItemBeans = new TreeMap<Integer, Integer>();
						yearToIntervalToWorkItemCount.put(Integer.valueOf(yearValue), intervalToWorkItemBeans);
					}
					Integer workItemCountForInterval = intervalToWorkItemBeans.get(Integer.valueOf(intervalValue));
					if (workItemCountForInterval==null) {
						workItemCountForInterval = Integer.valueOf(1);
					} else {
						workItemCountForInterval = Integer.valueOf(workItemCountForInterval.intValue()+1);
					}
					intervalToWorkItemBeans.put(Integer.valueOf(intervalValue), workItemCountForInterval);
				}
			}
		}
		return yearToIntervalToWorkItemCount;
	}


	/**
	* Create a map of hierarchical data with TStateChangeBeans for the workItems in the periods
	* -	key: year
	* -	value: map
	* 			-	key: period
	* 			-	Set of TStateChangeBeans
	* @param statusChangeList
	* @param timeInterval
	* @param sample sample (only last from the period) or action (all statuses during a period) 
	* @param workItemIDs output parameter to gather the workItems involved
	* @return
	*/
	static SortedMap<Integer, SortedMap<Integer, Integer>> getNumbersInTimeIntervalMap(
			List<HistorySelectValues> historySelectValuesList, int timeInterval) {
		SortedMap<Integer, SortedMap<Integer, Integer>> yearToIntervalToStatusChangeBeans = 
			new TreeMap<Integer, SortedMap<Integer, Integer>>();
		Map<Integer, Map<Integer, Set<Integer>>> yearToIntervalToWorkItemIDs = 
			new HashMap<Integer, Map<Integer, Set<Integer>>>();
		Integer yearValue;
		int intervalValue;
		Integer workItemID;
		if (historySelectValuesList!=null && !historySelectValuesList.isEmpty()) {
			Calendar calendarLastModified = Calendar.getInstance();
			//get them in the reverse order (the later first)
			Iterator<HistorySelectValues> iterator = historySelectValuesList.iterator();
			int calendarInterval = OpenedClosedDatasource.getCalendarInterval(timeInterval);
			Set<Integer> workItemsIDsForInterval = new HashSet<Integer>();
			while (iterator.hasNext()) {
				HistorySelectValues historySelectValues = iterator.next();
				workItemID = historySelectValues.getWorkItemID();				
				calendarLastModified.setTime(historySelectValues.getLastEdit());				
				yearValue = Integer.valueOf(calendarLastModified.get(Calendar.YEAR));
				intervalValue = calendarLastModified.get(calendarInterval);	
				//build a parallel map structure to see whether the a statusChange 
				//is already present in a specific interval
				//to avoid counting more times if it was closed more times in a period
				Map<Integer, Set<Integer>> intervalToWorkItems = yearToIntervalToWorkItemIDs.get(yearValue);
				if (intervalToWorkItems==null) {
					yearToIntervalToWorkItemIDs.put(yearValue, new HashMap<Integer, Set<Integer>>());
					intervalToWorkItems = yearToIntervalToWorkItemIDs.get(yearValue);
				}
				workItemsIDsForInterval = intervalToWorkItems.get(new Integer(intervalValue));
				if (workItemsIDsForInterval==null) {
					intervalToWorkItems.put(new Integer(intervalValue), new HashSet<Integer>());
					workItemsIDsForInterval = intervalToWorkItems.get(new Integer(intervalValue));
				}
				
				//add the stateChangeBean if: 
					//1. if not sample add all stateChangeBeans
					//2. if sample: add only one (the latest i.e. the first one from the list) for each period  
				if (!workItemsIDsForInterval.contains(workItemID)) {
					//add workItemID to forbid adding the same workItemID again for the same period
					workItemsIDsForInterval.add(workItemID);
					SortedMap<Integer, Integer> intervalToStatusChangeBeans = yearToIntervalToStatusChangeBeans.get(yearValue);
					if (intervalToStatusChangeBeans==null) {
						yearToIntervalToStatusChangeBeans.put(yearValue, new TreeMap<Integer, Integer>());
						intervalToStatusChangeBeans = yearToIntervalToStatusChangeBeans.get(yearValue);
					}
					Integer statusChangeBeansForInterval = (Integer)intervalToStatusChangeBeans.get(new Integer(intervalValue));
					if (statusChangeBeansForInterval==null) {
						statusChangeBeansForInterval = Integer.valueOf(1);
					} else {
						statusChangeBeansForInterval = Integer.valueOf(statusChangeBeansForInterval.intValue()+1);
					}
					intervalToStatusChangeBeans.put(Integer.valueOf(intervalValue), statusChangeBeansForInterval);
				}
			}
		}
		return yearToIntervalToStatusChangeBeans;
	}
}
