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

package com.aurel.track.report.datasource.statusOverTime;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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

import com.aurel.track.admin.customize.lists.systemOption.StatusBL;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.report.dashboard.StatusOverTimeGraph;
import com.aurel.track.util.DateTimeUtils;

public class StatusOverTimeBL {
	private static final Logger LOGGER = LogManager.getLogger(StatusOverTimeBL.class);

	static Document convertToDOM(SortedMap<Date, Map<Integer, Integer>> numberOfIssuesInStatusByDate,
			List<Integer> statusIDs, String personName, int selectedTimeInterval,
			Integer selectedCalculationMode, String title, Locale locale,
			Boolean grouping, Map<Integer, String>groupingIDsToLabels,
			Map<Integer, Boolean> usedGroupedValues) {

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
		Element createdAt =createDomElement("createdAt", DateTimeUtils.getInstance().formatISODateTime(new Date()),dom);
		root.appendChild(createdAt);
		Element etitle =createDomElement("title",title,dom);
		root.appendChild(etitle);
		String theType = null;
		if (selectedCalculationMode == StatusOverTimeGraph.CALCULATION_MODE.NEW) {
			theType="newIssues";
		} else if (selectedCalculationMode == StatusOverTimeGraph.CALCULATION_MODE.ACTUAL_SAMPLE) {
			theType = "issuesInStatus";
		} else if (selectedCalculationMode == StatusOverTimeGraph.CALCULATION_MODE.ACTUAL_ACTIVITY) {
			theType = "issuesActivity";
		} else if (selectedCalculationMode == StatusOverTimeGraph.CALCULATION_MODE.ACCUMULATED_ACTIVITY) {
			theType = "issuesAccumulated";
		}
		Element dtype = createDomElement("dataseriesType",theType,dom);
		root.appendChild(dtype);
		Element timeInterval =createDomElement("timeInterval",String.valueOf(selectedTimeInterval),dom);
		root.appendChild(timeInterval);

		//Map<Integer, TStateBean> statusMap = GeneralUtils.createMapFromList(statusBeanList);

		Map<Integer, SortedMap<Date, Integer>> numberOfIssuesInDateByStatus = new HashMap<Integer, SortedMap<Date,Integer>>();
		Iterator<Date> itrIssuesInStatus = numberOfIssuesInStatusByDate.keySet().iterator();
		while (itrIssuesInStatus.hasNext()) {
			Date date = itrIssuesInStatus.next();
			Map<Integer, Integer> numberInStateMap = numberOfIssuesInStatusByDate.get(date);
			if (numberInStateMap!=null && !numberInStateMap.isEmpty()) {
				Iterator<Integer> itrStatus = numberInStateMap.keySet().iterator();
				while (itrStatus.hasNext()) {
					Integer statusID = itrStatus.next();
					Integer numberOfIssues = numberInStateMap.get(statusID);
					SortedMap<Date, Integer> numberOfIssuesInDate = numberOfIssuesInDateByStatus.get(statusID);
					if (numberOfIssuesInDate==null) {
						numberOfIssuesInDate = new TreeMap<Date, Integer>();
						numberOfIssuesInDateByStatus.put(statusID, numberOfIssuesInDate);
					}
					numberOfIssuesInDate.put(date, numberOfIssues);
				}
			}
		}
		if (selectedCalculationMode == StatusOverTimeGraph.CALCULATION_MODE.NEW) {
			Map<Date, Integer> numberOfIssuesInDate = numberOfIssuesInDateByStatus.get(StatusOverTimeGraph.ENTITY_PLACEHOLDER);
			if (numberOfIssuesInDate!=null) {
				//is it a selected status?
				Iterator<Date> itrDate = numberOfIssuesInDate.keySet().iterator();
				while (itrDate.hasNext()) {
					Date date = itrDate.next();
					Integer numberOfIssues = numberOfIssuesInDate.get(date);
					if (numberOfIssues!=null) {
						//String statusLabel = stateBean.getLabel();
						Element statusCount = dom.createElement ("s");
						statusCount.appendChild(createDomElement("label","new",dom));
						statusCount.appendChild(createDomElement("no",numberOfIssues.toString(),dom));
						statusCount.appendChild(createDomElement("date",DateTimeUtils.getInstance().formatISODateTime(date),dom));
						root.appendChild(statusCount);
					}
				}
			}
		} else {
			if(grouping) {
				for(int i = -5; i < 0; i++) {
					Map<Date, Integer> numberOfIssuesInDate = numberOfIssuesInDateByStatus.get(i);
					if (numberOfIssuesInDate!=null && usedGroupedValues.get(i) != null) {
						//is it a selected status?
						Iterator<Date> itrDate = numberOfIssuesInDate.keySet().iterator();
						while (itrDate.hasNext()) {
							Date date = itrDate.next();
							Integer numberOfIssues = numberOfIssuesInDate.get(date);
							if (numberOfIssues!=null) {
								String statusLabel = groupingIDsToLabels.get(i);
								Element statusCount = dom.createElement ("s");
								statusCount.appendChild(createDomElement("label",statusLabel,dom));
								statusCount.appendChild(createDomElement("no",numberOfIssues.toString(),dom));
								statusCount.appendChild(createDomElement("date",DateTimeUtils.getInstance().formatISODateTime(date),dom));
								root.appendChild(statusCount);
							}
						}
					}
				}
			}else {
				List<TStateBean> statusBeanList = (List)StatusBL.loadAll(locale);
				Iterator<TStateBean> itrIssuesAtDate = statusBeanList.iterator();
				while (itrIssuesAtDate.hasNext()) {
					TStateBean stateBean = itrIssuesAtDate.next();
					Integer statusID = stateBean.getObjectID();
					//TStateBean stateBean = statusMap.get(statusID);
					if (stateBean!=null) {
						Map<Date, Integer> numberOfIssuesInDate = numberOfIssuesInDateByStatus.get(statusID);
						if (numberOfIssuesInDate!=null) {
							//is it a selected status?
							Iterator<Date> itrDate = numberOfIssuesInDate.keySet().iterator();
							while (itrDate.hasNext()) {
								Date date = itrDate.next();
								Integer numberOfIssues = numberOfIssuesInDate.get(date);
								if (numberOfIssues!=null) {
									String statusLabel = stateBean.getLabel();
									Element statusCount = dom.createElement ("s");
									statusCount.appendChild(createDomElement("label",statusLabel,dom));
									statusCount.appendChild(createDomElement("no",numberOfIssues.toString(),dom));
									statusCount.appendChild(createDomElement("date",DateTimeUtils.getInstance().formatISODateTime(date),dom));
									root.appendChild(statusCount);
								}
							}
						}
					}
				}

			}

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
		Element element = dom.createElement(elementName.replaceAll("\\s", ""));
		if (elementValue == null || "".equals(elementValue.trim())) {
			element.appendChild (dom.createTextNode (""));
		} else {
			element.appendChild (dom.createTextNode(elementValue));
		}
		return element;
	}
}
