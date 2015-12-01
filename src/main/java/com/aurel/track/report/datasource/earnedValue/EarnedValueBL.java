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

package com.aurel.track.report.datasource.earnedValue;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
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

import com.aurel.track.beans.TBudgetBean;
import com.aurel.track.beans.TCostBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.item.budgetCost.AccountingBL;
import com.aurel.track.item.history.HistoryLoaderBL;
import com.aurel.track.item.history.HistoryValues;
import com.aurel.track.report.execute.ReportBeanWithHistory;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.DateTimeUtils;



public class EarnedValueBL {
	private static final Logger LOGGER = LogManager.getLogger(EarnedValueBL.class);
	private static final double EPSILON = Float.MIN_NORMAL;
	/**
	 * Remove the report beans without plan or with dates out of selected interval
	 * @param reportBeanWithHistoryList
	 * @param isTime
	 * @param dateFrom
	 * @param dateTo
	 * @return
	 */
	public static Set<Integer> prefilterReportBeansGetProjectIDs(List<ReportBeanWithHistory> reportBeanWithHistoryList,
			boolean isTime, Date dateFrom, Date dateTo) {
		Set<Integer> projectIDs = new HashSet<Integer>();
		if (reportBeanWithHistoryList!=null) {
			Iterator<ReportBeanWithHistory> iterator = reportBeanWithHistoryList.iterator();
			while (iterator.hasNext()) {
				ReportBeanWithHistory reportBeanWithHistory = iterator.next();
				List<TBudgetBean> budgetList = reportBeanWithHistory.getPlannedValueHistory();
				if (budgetList==null || budgetList.isEmpty()) {
					//no budget exists
					iterator.remove();
					continue;
				} else {
					//get the first entry which should be the most recent one
					TBudgetBean budgetBean = budgetList.get(0);
					if (isTime) {
						Double effortValue = budgetBean.getEstimatedHours();
						if (effortValue==null || Math.abs(effortValue.doubleValue()) <= EPSILON) {
							//no planned effort
							iterator.remove();
							continue;
						}
					} else {
						Double costValue = budgetBean.getEstimatedCost();
						if (costValue==null || Math.abs(costValue.doubleValue()) <= EPSILON) {
							//no planned cost
							iterator.remove();
							continue;
						}
					}
				}
				TWorkItemBean workItemBean = reportBeanWithHistory.getWorkItemBean();
				Date startDate = workItemBean.getStartDate();
				Date endDate = workItemBean.getEndDate();
				if (dateFrom!=null && startDate!=null && startDate.before(dateFrom)) {
					//starts before dateFrom
					iterator.remove();
					continue;
				}
				if (dateTo!=null && endDate!=null && endDate.after(dateTo)) {
					//ends after dateTo
					iterator.remove();
					continue;
				}
				projectIDs.add(workItemBean.getProjectID());
			}
		}
		return projectIDs;
	}

	/**
	 * Gets earliest start date or the latest end date
	 * @param reportBeanWithHistoryList
	 * @param isStartDate
	 * @return
	 */
	public static Date getMinMaxStartEndDate(List<ReportBeanWithHistory> reportBeanWithHistoryList, boolean isStartDate) {
		Date minMaxDate = null;
		if (reportBeanWithHistoryList!=null) {
			Iterator<ReportBeanWithHistory> iterator = reportBeanWithHistoryList.iterator();
			while (iterator.hasNext()) {
				ReportBeanWithHistory reportBeanWithHistory = iterator.next();
				TWorkItemBean workItemBean = reportBeanWithHistory.getWorkItemBean();
				Date currentDate;
				if (isStartDate) {
					currentDate = workItemBean.getStartDate();
				} else {
					currentDate = workItemBean.getEndDate();
				}
				if (currentDate!=null) {
					if (minMaxDate == null) {
						minMaxDate = currentDate;
					} else {
						if (isStartDate) {
							if (currentDate.before(minMaxDate)) {
								minMaxDate = currentDate;
							}
						} else {
							if (currentDate.after(minMaxDate)) {
								minMaxDate = currentDate;
							}
						}
					}
				}
			}
		}
		return minMaxDate;
	}

	/**
	 * Calculate planned values
	 * @param reportBeanWithHistoryList
	 * @param isTime
	 * @param minStartDate
	 * @param maxEndDate
	 * @param timeInterval
	 * @param hoursPerWorkingDayMap
	 * @return
	 */
	public static SortedMap<Integer, SortedMap<Integer, Double>> calculatePlannedValues(
			List<ReportBeanWithHistory> reportBeanWithHistoryList,
			boolean isTime, Date minStartDate, Date maxEndDate, Integer timeInterval, Map<Integer, Double> hoursPerWorkingDayMap) {
		SortedMap<Integer, SortedMap<Integer, Double>> yearToIntervalToPlannedValue = new TreeMap<Integer, SortedMap<Integer, Double>>();
		if (reportBeanWithHistoryList!=null) {
			Calendar calendarStartDate = Calendar.getInstance();
			Calendar calendarEndDate = Calendar.getInstance();
			Calendar calendar = Calendar.getInstance();
			//to avoid that the last days of the year to be taken as the first
			//week of the now year but the year remains the old one
			int calendarInterval = EarnedValueDatasource.getCalendarInterval(timeInterval);
			for (ReportBeanWithHistory reportBeanWithHistory : reportBeanWithHistoryList) {
				TWorkItemBean workItemBean = reportBeanWithHistory.getWorkItemBean();
				Integer projectID = workItemBean.getProjectID();
				Date startDate = workItemBean.getStartDate();
				if (startDate==null) {
					startDate = minStartDate;
				}
				Date endDate = workItemBean.getEndDate();
				if (endDate==null) {
					endDate = maxEndDate;
				}
				calendarStartDate.setTime(startDate);
				calendarEndDate.setTime(endDate);
				List<TBudgetBean> budgetBeans = reportBeanWithHistory.getPlannedValueHistory();
				if (budgetBeans==null || budgetBeans.isEmpty()) {
					continue;
				}
				TBudgetBean budgetBean = budgetBeans.get(0);
				Double effort;
				if (isTime) {
					effort = budgetBean.getEstimatedHours();
					Integer timeUnits = budgetBean.getTimeUnit();
					if (timeUnits!=null &&  AccountingBL.TIMEUNITS.WORKDAYS.equals(timeUnits.intValue())) {
						//transform to hours because the actual costs are always on hours
						effort = AccountingBL.transformToTimeUnits(effort, hoursPerWorkingDayMap.get(projectID), timeUnits, AccountingBL.TIMEUNITS.HOURS);
					}
				} else {
					effort = budgetBean.getEstimatedCost();
				}
				if (effort==null || Math.abs(effort.doubleValue()) <= EPSILON) {
					continue;
				}
				int noOfPeriods = getNumberOfPeriods(calendarStartDate, calendarEndDate, timeInterval);
				Double deltaPerPeriod;
				if (noOfPeriods==0) {
					//probably startDate == endDate
					deltaPerPeriod = effort;
				} else {
					deltaPerPeriod = AccountingBL.roundToDecimalDigits(effort/noOfPeriods, isTime);
				}
				calendar.setTime(startDate);
				int yearValue = calendarStartDate.get(Calendar.YEAR);
				int intervalValue = calendarStartDate.get(calendarInterval);
				boolean isFirst = true;
				while (calendar.before(calendarEndDate) || isFirst) {
					if (isFirst) {
						isFirst = false;
					} else {
						calendar.add(calendarInterval, 1);
					}
					yearValue = calendar.get(Calendar.YEAR);
					intervalValue = calendar.get(calendarInterval);
					SortedMap<Integer, Double> intervalToPlannedValues = yearToIntervalToPlannedValue.get(Integer.valueOf(yearValue));
					if (intervalToPlannedValues==null) {
						yearToIntervalToPlannedValue.put(new Integer(yearValue), new TreeMap<Integer, Double>());
						intervalToPlannedValues = yearToIntervalToPlannedValue.get(Integer.valueOf(yearValue));
					}
					Double totalPlannedValueForInterval = intervalToPlannedValues.get(Integer.valueOf(intervalValue));
					if (totalPlannedValueForInterval==null) {
						totalPlannedValueForInterval = deltaPerPeriod;
					} else {
						totalPlannedValueForInterval = new Double(totalPlannedValueForInterval.doubleValue()+deltaPerPeriod.doubleValue());
					}
					intervalToPlannedValues.put(Integer.valueOf(intervalValue), totalPlannedValueForInterval);
				}
			}
		}
		return yearToIntervalToPlannedValue;
	}

	/**
	 * Get the number of periods between two dates
	 * @param startDate
	 * @param endDate
	 * @param timeInterval
	 * @return
	 */
	static int getNumberOfPeriods(Calendar startDate, Calendar endDate, int timeInterval) {
		Calendar date = (Calendar) startDate.clone();
		int calendarInterval = EarnedValueDatasource.getCalendarInterval(timeInterval);
		int periods = 0;
		while (date.before(endDate)) {
			date.add(calendarInterval, 1);
			periods++;
		}
		return periods;
	}

	/**
	 * Calculate actual values
	 * @param reportBeanWithHistoryList
	 * @param isTime
	 * @param timeInterval
	 * @return
	 */
	public static SortedMap<Integer, SortedMap<Integer, Double>> calculateActualValues(
			List<ReportBeanWithHistory> reportBeanWithHistoryList,
			boolean isTime, int timeInterval) {
		SortedMap<Integer, SortedMap<Integer, Double>> yearToIntervalToActualValue = new TreeMap<Integer, SortedMap<Integer, Double>>();
		if (reportBeanWithHistoryList!=null) {
			Calendar calendar = Calendar.getInstance();
			//to avoid that the last days of the year to be taken as the first
			//week of the now year but the year remains the old one
			int calendarInterval = EarnedValueDatasource.getCalendarInterval(timeInterval);
			for (ReportBeanWithHistory reportBeanWithHistory : reportBeanWithHistoryList) {
				List<TCostBean> costBeans = reportBeanWithHistory.getCosts();
				if (costBeans==null || costBeans.isEmpty()) {
					continue;
				}
				Iterator<TCostBean> itrCostBeans = costBeans.iterator();
				while (itrCostBeans.hasNext()) {
					TCostBean costBean = itrCostBeans.next();
					Double effortValue;
					if (isTime) {
						effortValue = costBean.getHours();
					} else {
						effortValue = costBean.getCost();
					}
					if (effortValue==null || Math.abs(effortValue.doubleValue()) <= EPSILON) {
						continue;
					}
					Date effortDate = costBean.getEffortdate();
					if (effortDate==null) {
						effortDate = costBean.getLastEdit();
					}
					if (effortDate!=null) {
						calendar.setTime(effortDate);
						int yearValue = calendar.get(Calendar.YEAR);
						int intervalValue = calendar.get(calendarInterval);
						SortedMap<Integer, Double> intervalToActualValues = yearToIntervalToActualValue.get(Integer.valueOf(yearValue));
						if (intervalToActualValues==null) {
							yearToIntervalToActualValue.put(new Integer(yearValue), new TreeMap<Integer, Double>());
							intervalToActualValues = yearToIntervalToActualValue.get(Integer.valueOf(yearValue));
						}
						Double totalActualEffortForInterval = intervalToActualValues.get(Integer.valueOf(intervalValue));
						if (totalActualEffortForInterval==null) {
							totalActualEffortForInterval = effortValue;
						} else {
							totalActualEffortForInterval = new Double(totalActualEffortForInterval.doubleValue()+effortValue.doubleValue());
						}
						intervalToActualValues.put(Integer.valueOf(intervalValue), totalActualEffortForInterval);
					}
				}
			}
		}
		return yearToIntervalToActualValue;
	}

	/**
	 * Calculate the earned values
	 * @param reportBeanWithHistoryList
	 * @param isTime
	 * @param minStartDate
	 * @param maxEndDate
	 * @param timeInterval
	 * @param hoursPerWorkingDayMap
	 * @param finalStates
	 * @return
	 */
	public static SortedMap<Integer, SortedMap<Integer, Double>> calculateEarnedValues(List<ReportBeanWithHistory> reportBeanWithHistoryList,
			boolean isTime, Date minStartDate, Date maxEndDate, Integer timeInterval, Map<Integer, Double> hoursPerWorkingDayMap, Set<Integer> finalStates) {
		SortedMap<Integer, SortedMap<Integer, Double>> yearToIntervalToPlannedValue = new TreeMap<Integer, SortedMap<Integer, Double>>();
		if (reportBeanWithHistoryList!=null) {
			Calendar calendarStartDate = Calendar.getInstance();
			Calendar calendarEndDate = Calendar.getInstance();
			Calendar calendar = Calendar.getInstance();
			//to avoid that the last days of the year to be taken as the first
			//week of the now year but the year remains the old one
			int calendarInterval = EarnedValueDatasource.getCalendarInterval(timeInterval);
			for (ReportBeanWithHistory reportBeanWithHistory : reportBeanWithHistoryList) {
				TWorkItemBean workItemBean = reportBeanWithHistory.getWorkItemBean();
				Integer projectID = workItemBean.getProjectID();
				Date startDate = workItemBean.getStartDate();
				if (startDate==null) {
					startDate = minStartDate;
				}
				Date endDate = workItemBean.getEndDate();
				if (endDate==null) {
					endDate = maxEndDate;
				}
				calendarStartDate.setTime(startDate);
				calendarEndDate.setTime(endDate);
				Map<Integer, Map<Integer, HistoryValues>> historyMap = reportBeanWithHistory.getHistoryValuesMap();
				List<HistoryValues> historyValuesList = HistoryLoaderBL.getHistoryValuesList(historyMap, false);
				if (historyValuesList==null || historyValuesList.isEmpty()) {
					continue;
				}
				//the first HistoryValue should be the most recent because of sorting in HistoryLoaderBL.getHistoryValuesList()
				HistoryValues historyValues = historyValuesList.get(0);
				if (!SystemFields.INTEGER_STATE.equals(historyValues.getFieldID())) {
					//should never happen
					continue;
				}
				Integer stateID = (Integer)historyValues.getNewValue();
				if (stateID==null || !finalStates.contains(stateID)) {
					continue;
				}
				Date lastStateChangeDate = historyValues.getLastEdit();

				//state is a final state get the earned value as the total effort
				List<TBudgetBean> budgetBeans = reportBeanWithHistory.getPlannedValueHistory();
				if (budgetBeans==null || budgetBeans.isEmpty()) {
					continue;
				}
				TBudgetBean budgetBean = budgetBeans.get(0);
				Double effort;
				if (isTime) {
					effort = budgetBean.getEstimatedHours();
					Integer timeUnits = budgetBean.getTimeUnit();
					if (timeUnits!=null &&  AccountingBL.TIMEUNITS.WORKDAYS.equals(timeUnits.intValue())) {
						//transform to hours because the actual costs are always on hours
						effort = AccountingBL.transformToTimeUnits(effort, hoursPerWorkingDayMap.get(projectID), timeUnits, AccountingBL.TIMEUNITS.HOURS);
					}
				} else {
					effort = budgetBean.getEstimatedCost();
				}
				if (effort==null || Math.abs(effort.doubleValue()) <= EPSILON) {
					continue;
				}
				calendar.setTime(lastStateChangeDate);
				int yearValue = calendar.get(Calendar.YEAR);
				int intervalValue = calendar.get(calendarInterval);
				SortedMap<Integer, Double> intervalToPlannedValues = yearToIntervalToPlannedValue.get(Integer.valueOf(yearValue));
				if (intervalToPlannedValues==null) {
					yearToIntervalToPlannedValue.put(new Integer(yearValue), new TreeMap<Integer, Double>());
					intervalToPlannedValues = yearToIntervalToPlannedValue.get(Integer.valueOf(yearValue));
				}
				Double totalEarnedValueForInterval = intervalToPlannedValues.get(Integer.valueOf(intervalValue));
				if (totalEarnedValueForInterval==null) {
					totalEarnedValueForInterval = new Double(effort);
				} else {
					totalEarnedValueForInterval = new Double(totalEarnedValueForInterval.doubleValue()+effort);
				}
				intervalToPlannedValues.put(Integer.valueOf(intervalValue), totalEarnedValueForInterval);
			}
		}
		return yearToIntervalToPlannedValue;
	}

	/**
	 * Accumulate the values over the time intervals
	 * @param yearToIntervalToValues
	 */
	public static void accumulateValues(SortedMap<Integer, SortedMap<Integer, Double>> yearToIntervalToValues) {
		if (yearToIntervalToValues!=null) {
			Double acumulatedValue = new Double(0.0);
			for (Map.Entry<Integer, SortedMap<Integer, Double>> yearEntry : yearToIntervalToValues.entrySet()) {
				SortedMap<Integer, Double> intervalToValues = yearEntry.getValue();
				if (intervalToValues!=null) {
					for (Map.Entry<Integer, Double> intervalEntry : intervalToValues.entrySet()) {
						 Integer interval = intervalEntry.getKey();
						 Double actualValue = intervalEntry.getValue();
						 if (actualValue!=null) {
							 acumulatedValue = new Double(acumulatedValue.doubleValue() + actualValue.doubleValue());
						 }
						 intervalToValues.put(interval, acumulatedValue);
					}
				}
			}
		}
	}

	/**
	 * Add a value type to the EarnedValueTimeSlice map
	 * @param timeSliceMap
	 * @param valuesMap
	 * @param valueType
	 */
	public static void addValueTypeToBeans(SortedMap<Date, EarnedValueTimeSlice> timeSliceMap,
			SortedMap<Date, Object> valuesMap, int valueType) {
		if (valuesMap!=null) {
			for (Map.Entry<Date, Object> entry : valuesMap.entrySet()) {
				Date date = entry.getKey();
				Double doubleValue = (Double)entry.getValue();
				EarnedValueTimeSlice earnedValueTimeSlice = timeSliceMap.get(date);
				if (earnedValueTimeSlice==null) {
					earnedValueTimeSlice = new EarnedValueTimeSlice();
					timeSliceMap.put(date, earnedValueTimeSlice);
				}
				earnedValueTimeSlice.setDate(date);
				switch (valueType) {
				case EarnedValueDatasource.PLANNED_VALUE:
					earnedValueTimeSlice.setPlannedValue(doubleValue);
					break;
				case EarnedValueDatasource.ACTUAL_EFFORT:
					earnedValueTimeSlice.setActualCost(doubleValue);
					break;
				case EarnedValueDatasource.EARNED_VALUE:
					earnedValueTimeSlice.setEarnedvalue(doubleValue);
					break;

				}
			}
		}
	}

	/**
	 * CReate the dom for xml
	 * @param openClosedJavaBeans
	 * @param locale
	 * @param bundleName
	 * @param personName
	 * @param selectedTimeInterval
	 * @param effortType
	 * @return
	 */
	public static Document convertToDOM(Collection<EarnedValueTimeSlice> openClosedJavaBeans, Locale locale, String bundleName,
			String personName, int selectedTimeInterval, Integer effortType) {
		Document dom = null;
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
			DocumentBuilder builder = factory.newDocumentBuilder ();
			dom = builder.newDocument ();
		}catch (FactoryConfigurationError e){
			LOGGER.error ("Creating the DOM document failed with FactoryConfigurationError:" + e.getMessage());
			return null;
		}catch (ParserConfigurationException e){
			LOGGER.error ("Creating the DOM document failed with ParserConfigurationException: " + e.getMessage());
			return null;
		}
		Element root = dom.createElement ("track-report");
		Element createdBy =createDomElement("createdBy",personName,dom);
		root.appendChild(createdBy);
		Element timeInterval =createDomElement("timeInterval",
				EarnedValueDatasource.getLocalizedTimeInterval(locale, bundleName, selectedTimeInterval), dom);
		root.appendChild(timeInterval);
		Element effortTypeElement =createDomElement("effortType",
				getLocalizedEffortType(locale, bundleName, effortType), dom);
		root.appendChild(effortTypeElement);
		if (openClosedJavaBeans!=null) {
			for (EarnedValueTimeSlice earnedValueTimeSlice : openClosedJavaBeans) {
				Element period = dom.createElement ("period");
				period.appendChild(createDomElement("date", DateTimeUtils.getInstance().formatISODateTime(earnedValueTimeSlice.getDate()), dom));
				boolean isTime = false;
				if (effortType==EarnedValueDatasource.EFFORT_TYPE.TIME) {
					isTime = true;
				}
				if (earnedValueTimeSlice.getPlannedValue()!=null) {
					String plannedValue = AccountingBL.getFormattedDouble(earnedValueTimeSlice.getPlannedValue(), Locale.ENGLISH, isTime);
					period.appendChild(createDomElement("plannedValue", plannedValue, dom));
				}
				if (earnedValueTimeSlice.getActualCost()!=null) {
					String actualValue = AccountingBL.getFormattedDouble(earnedValueTimeSlice.getActualCost(), Locale.ENGLISH, isTime);
					period.appendChild(createDomElement("actualValue", actualValue, dom));
				}
				if (earnedValueTimeSlice.getEarnedvalue()!=null) {
					String earnedValue = AccountingBL.getFormattedDouble(earnedValueTimeSlice.getEarnedvalue(), Locale.ENGLISH, isTime);
					period.appendChild(createDomElement("earnedValue", earnedValue, dom));
				}

				root.appendChild (period);
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
		Element element = dom.createElement (elementName);
		if (elementValue == null || "".equals(elementValue.trim())) {
			element.appendChild (dom.createTextNode (""));
		} else {
			element.appendChild (dom.createTextNode(elementValue));
		}
		return element;
	}

	/**
	 * Localized list of possible localized effort types.
	 * @param locale
	 * @return
	 */
	static String getLocalizedEffortType(Locale locale, String bundleName, int timeInterval) {
		switch (timeInterval) {
		case EarnedValueDatasource.EFFORT_TYPE.TIME:
			return LocalizeUtil.getLocalizedText("common.effort", locale, bundleName);
		case EarnedValueDatasource.EFFORT_TYPE.COST:
			return LocalizeUtil.getLocalizedText("common.cost", locale, bundleName);
		}
		return "";
	}
}
