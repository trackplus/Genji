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

package com.aurel.track.report.dashboard;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.aurel.track.report.datasource.TimeIntervalWithStatusDatasource.TIME_INTERVAL;
import com.aurel.track.util.CalendarUtil;

public class BurnDownChartBL {

	/**
	 * Add the time series to the timeSeriesCollection
	 * SortedMap at first and second level (year and period)
	 * (Sorted because the accumulated should be calculated in the right order)
	 * @param timeSeriesCollection
	 * @param yearToPeriodToOpenedWorkItemCountMap
	 * @param selectedTimeInterval
	*/
	public static SortedMap<Date, Object/*Double or Integer or Map<Integer, Integer>*/> transformPeriodsToDates(
			SortedMap/*<Integer, SortedMap<Integer, Object>>*/ yearToPeriodToValuesMap,
			int selectedTimeInterval) {
		SortedMap<Date, Object> dateToValue = new TreeMap<Date, Object>();
		for (Iterator iterator = yearToPeriodToValuesMap.keySet().iterator(); iterator.hasNext();) {
			Integer year  = (Integer) iterator.next();
			SortedMap<Integer, Object> intervalToStatusChangeBeans = (SortedMap<Integer, Object>)yearToPeriodToValuesMap.get(year);
			Iterator<Integer> periodIterator = intervalToStatusChangeBeans.keySet().iterator();
			while (periodIterator.hasNext()) {
				Integer period = periodIterator.next();
				Object periodValue = intervalToStatusChangeBeans.get(period);
				if (periodValue!=null) {
					dateToValue.put(createDate(period.intValue(), year.intValue(), selectedTimeInterval), periodValue);
				}
			}
		}
		return dateToValue;
	}

	/**
	 * Created a regular time period
	 * @param period
	 * @param year
	 * @param timeInterval
	 * @return
	 */
	public static Date createDate(int period, int year, int timeInterval) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setLenient(true);
		calendar.set(Calendar.YEAR, year);
		switch (timeInterval) {
		case TIME_INTERVAL.DAY:
			calendar.set(Calendar.DAY_OF_YEAR, period);
			break;
		case TIME_INTERVAL.WEEK:
			calendar.set(Calendar.WEEK_OF_YEAR, period);
			break;
		default:
			calendar.set(Calendar.MONTH, period);
		}
		return calendar.getTime();
	}

	/**
	 * Set 0 values for the empty time intervals
	 * @param dateFrom
	 * @param dateTo
	 * @param selectedTimeInterval
	 * @param yearToPeriodToValue
	 * @param entityIDs
	 */
	 public static void addZerosForEmptyIntervals(Date dateFrom, Date dateTo, int selectedTimeInterval,
			SortedMap/*<Integer, SortedMap<Integer, Double or Integer>>*/ yearToPeriodToValue, boolean isInteger) {
		int calendarInterval = getCalendarInterval(selectedTimeInterval);
		Calendar calendarFrom = calculateDateFrom(dateFrom, yearToPeriodToValue, calendarInterval);
		if (calendarFrom==null) {
			return;
		}
		Calendar calendarTo = calculateDateTo(dateTo, yearToPeriodToValue, calendarInterval);
		if (calendarTo==null) {
			return;
		}
		//previous-next issue: to avoid adding the first week of the new year as the first week of the old year,
		//because it can be that the year is the old one but the last days of the year belong to the first week of the next year
		//and that would add an entry with the first week of the old year
		int previousYearValue = calendarFrom.get(Calendar.YEAR);
		int nextYearValue = previousYearValue;
		int previousCalendarIntervalValue = calendarFrom.get(calendarInterval);
		int nextCalendarIntervalValue = previousCalendarIntervalValue;
		boolean jumpYearFirstTime = false;
		if (selectedTimeInterval==TIME_INTERVAL.WEEK && calendarFrom.get(Calendar.MONTH)==11 && nextCalendarIntervalValue==1) {
			jumpYearFirstTime = true;
		}
		while (calendarFrom.before(calendarTo)) {
			if ((nextCalendarIntervalValue<previousCalendarIntervalValue && nextYearValue==previousYearValue)
					|| jumpYearFirstTime) {
				nextYearValue +=1;
			}
			jumpYearFirstTime = false;
			setZeroIfMissing(yearToPeriodToValue, Integer.valueOf(nextYearValue),
					Integer.valueOf(nextCalendarIntervalValue), isInteger);
			previousYearValue = nextYearValue;
			previousCalendarIntervalValue = nextCalendarIntervalValue;
			calendarFrom.add(calendarInterval, 1);
			nextYearValue = calendarFrom.get(Calendar.YEAR);
			nextCalendarIntervalValue = calendarFrom.get(calendarInterval);

		}
	}

	 public static Calendar calculateDateFrom(Date dateFrom,
	    		SortedMap<Integer, SortedMap<Integer, Map>> yearToPeriodToEntityNumbersMap,
	    		int calendarInterval) {

	    	Calendar calendar = Calendar.getInstance();
			calendar.setLenient(true);
	    	if (dateFrom!=null) {
				calendar.setTime(dateFrom);
			} else {
				Set<Integer> yearSet = yearToPeriodToEntityNumbersMap.keySet();
		    	if (yearSet.isEmpty()) {
		    		return null;
		    	}
				Iterator<Integer> yearIterator = yearSet.iterator();
				Integer firstYear = yearIterator.next();
				SortedMap<Integer, Map<Integer, Integer>> periodToEntityNumbersMap = (SortedMap)yearToPeriodToEntityNumbersMap.get(firstYear);
				if (periodToEntityNumbersMap==null || periodToEntityNumbersMap.isEmpty()) {
					return null;
				}
				Set<Integer> periodSet = periodToEntityNumbersMap.keySet();
				if (periodSet.isEmpty()) {
					return null;
				}
				Iterator<Integer> periodIterator = periodSet.iterator();
				Integer firstPeriode = (Integer)periodIterator.next();

				calendar.set(Calendar.YEAR, firstYear.intValue());
				calendar.set(calendarInterval, firstPeriode.intValue());
			}
			CalendarUtil.clearTime(calendar);
	    	if (calendarInterval==Calendar.MONTH) {
	    		//go to the beginning of current month to guarantee that even for the last
	    		//period calendarFrom.before(calendarTo) the zeros can be added
	    		calendar.set(Calendar.DAY_OF_MONTH, 1);
	    	}
	    	if (calendarInterval==Calendar.WEEK_OF_YEAR) {
	    		//go to the beginning of current week to guarantee that even for the last
	    		//period calendarFrom.before(calendarTo) the zeros can be added
	    		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
	    	}
	    	return calendar;
	    }

		public static Calendar calculateDateTo(Date dateTo,
	    		SortedMap<Integer, SortedMap<Integer, Map>> yearToPeriodToEntityNumbersMap,
	    		int calendarInterval) {

	    	Calendar calendar = Calendar.getInstance();
			calendar.setLenient(true);
	    	if (dateTo!=null) {
				calendar.setTime(dateTo);
			} else {
		    	Set<Integer> yearSet = yearToPeriodToEntityNumbersMap.keySet();
		    	if (yearSet.isEmpty()) {
		    		return null;
		    	}
		    	Integer lastYear = null;
				Iterator<Integer> yearIterator = yearSet.iterator();
				while (yearIterator.hasNext()) {
					lastYear = yearIterator.next();
				}
				SortedMap<Integer, Map<Integer, Integer>> periodToEntityNumbersMap = (SortedMap)yearToPeriodToEntityNumbersMap.get(lastYear);
				if (periodToEntityNumbersMap==null || periodToEntityNumbersMap.isEmpty()) {
					return null;
				}
				Set<Integer> periodSet = periodToEntityNumbersMap.keySet();
				if (periodSet.isEmpty()) {
					return null;
				}
				Integer lastPeriode = null;
				Iterator<Integer> periodIterator = periodSet.iterator();
				while (periodIterator.hasNext()) {
					lastPeriode = periodIterator.next();
				}
				calendar.set(Calendar.YEAR, lastYear.intValue());
				calendar.set(calendarInterval, lastPeriode.intValue());
			}
			CalendarUtil.clearTime(calendar);

	    	if (calendarInterval==Calendar.MONTH) {
	    		//go to the end of current month to guarantee that even for the last
	    		//period calendarFrom.before(calendarTo) the zeros can be added
	    		calendar.add(Calendar.MONTH, 1);
	    		calendar.set(Calendar.DAY_OF_MONTH, 1);
	    		calendar.add(Calendar.DATE, -1);
	    	}
	    	if (calendarInterval==Calendar.WEEK_OF_YEAR) {
	    		//go to the end of current week to guarantee that even for the last
	    		//period calendarFrom.before(calendarTo) the zeros can be added
	    		calendar.add(Calendar.WEEK_OF_YEAR, 1);
	    		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
	    		calendar.add(Calendar.DATE, -1);
	    	}

			return calendar;
	    }


		/**
		 * Get the corresponding calendar constant
		 * @param timeInterval
		 * @return
		 */
		public static int  getCalendarInterval(int timeInterval) {
			switch (timeInterval) {
			case TIME_INTERVAL.DAY:
				return Calendar.DAY_OF_YEAR;
			case TIME_INTERVAL.WEEK:
				return Calendar.WEEK_OF_YEAR;
			default:
				return Calendar.MONTH;
			}
		}

		/**
		 * Set zero value for time
		 * @param yearToPeriodToWorkItemsCount
		 * @param year
		 * @param period
		 * @param entityID
		 */
		public static void setZeroIfMissing(SortedMap<Integer, SortedMap<Integer, Object/*Double or Integer*/>> yearToPeriodToWorkItemsCount,
			   Integer year, Integer period, boolean isInteger) {
		   if (yearToPeriodToWorkItemsCount==null ||
				   year==null || period==null) {
			   return;
		   }
		   SortedMap<Integer, Object/*Double or Integer*/> periodToWorkItemsCountMap = yearToPeriodToWorkItemsCount.get(year);
		   if (periodToWorkItemsCountMap==null) {
			   yearToPeriodToWorkItemsCount.put(year, new TreeMap<Integer, Object>());
			   periodToWorkItemsCountMap = yearToPeriodToWorkItemsCount.get(year);
		   }
		   Object workItemNumbersInPeriod = periodToWorkItemsCountMap.get(period);
		   if (workItemNumbersInPeriod==null) {
			   if (isInteger) {
				   periodToWorkItemsCountMap.put(period, new Integer(0));
			   } else {
				  periodToWorkItemsCountMap.put(period, new Double(0.0));
			   }
		   }
		}

}
