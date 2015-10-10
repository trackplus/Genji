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

package com.aurel.track.report.datasource;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.plugin.DatasourceDescriptor;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.CalendarUtil;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.PropertiesHelper;
import com.aurel.track.util.StringArrayParameterUtils;

public abstract class TimeIntervalWithStatusDatasource extends TimePeriodDatasource {
	
	public static interface TIME_INTERVAL_PARAMETER_NAME {
		/**
		 * Time intervals
		 */
		public static String TIME_INTERVAL = "timeInterval";
		public static String TIME_INTERVAL_OPTIONS = TIME_INTERVAL + PARAMETER_NAME.OPTION_SUFFIX;
		public static String TIME_INTERVAL_NAME_FIELD = TIME_INTERVAL + PARAMETER_NAME.NAME_SUFFIX;
		public static String TIME_INTERVAL_NAME_VALUE = PARAMETER_NAME.MAP_PREFIX + TIME_INTERVAL;
		
		/**
		 * Status
		 */
		public static String STATUSES = "statuses";
		public static String STATUS_OPTIONS = "statusOptions";
		public static String STATUS_NAME_FIELD = STATUSES + PARAMETER_NAME.NAME_SUFFIX;
		public static String STATUS_NAME_VALUE = PARAMETER_NAME.MAP_PREFIX + STATUSES;
		//although status appears in more charts the labeling differs from chart to chart 
		public static String STATUS_LABEL_KEY = "statusLabelKey";
		
		public static String STATUSES_SECOND = "statusesSecond";
		public static String STATUS_SECOND_OPTIONS = "statusSecondOptions";
		public static String STATUS_SECOND_NAME_FIELD = STATUSES_SECOND + PARAMETER_NAME.NAME_SUFFIX;
		public static String STATUS_SECOND_NAME_VALUE = PARAMETER_NAME.MAP_PREFIX + STATUSES_SECOND;
		
		public static String STATUSES_THIRD = "statusesThird";
		public static String STATUS_THIRD_OPTIONS = "statusThirdOptions";
		public static String STATUS_THIRD_NAME_FIELD = STATUSES_THIRD + PARAMETER_NAME.NAME_SUFFIX;
		public static String STATUS_THIRD_NAME_VALUE = PARAMETER_NAME.MAP_PREFIX + STATUSES_THIRD;
		
		public static String STATUSES_FOURTH = "statusesFourth";
		public static String STATUS_FOURTH_OPTIONS = "statusFourthOptions";
		public static String STATUS_FOURTH_NAME_FIELD = STATUSES_FOURTH + PARAMETER_NAME.NAME_SUFFIX;
		public static String STATUS_FOURTH_NAME_VALUE = PARAMETER_NAME.MAP_PREFIX + STATUSES_FOURTH;
		
		public static String STATUSES_FIFTH = "statusesFifth";
		public static String STATUS_FIFTH_OPTIONS = "statusFifthOptions";
		public static String STATUS_FIFTH_NAME_FIELD = STATUSES_FIFTH + PARAMETER_NAME.NAME_SUFFIX;
		public static String STATUS_FIFTH_NAME_VALUE = PARAMETER_NAME.MAP_PREFIX + STATUSES_FIFTH;
		
		public static String IS_STATUS_OVER_TIME = "isStatusOverTime"; 
		
		public static String GROUPING = "grouping";
		public static String GROUPING_NAME_FIELD = GROUPING + PARAMETER_NAME.NAME_SUFFIX;
		public static String GROUPING_NAME_VALUE = PARAMETER_NAME.MAP_PREFIX + GROUPING;
		
		public static String GROUPING_FIRST_LABEL = "groupingFirstLabel";
		public static String GROUPING_FIRST_LABEL_FIELD = GROUPING_FIRST_LABEL + PARAMETER_NAME.NAME_SUFFIX;
		public static String GROUPING_FIRST_LABEL_VALUE = PARAMETER_NAME.MAP_PREFIX + GROUPING_FIRST_LABEL;
		
		public static String GROUPING_SECOND_LABEL = "groupingSecondLabel";
		public static String GROUPING_SECOND_LABEL_FIELD = GROUPING_SECOND_LABEL + PARAMETER_NAME.NAME_SUFFIX;
		public static String GROUPING_SECOND_LABEL_VALUE = PARAMETER_NAME.MAP_PREFIX + GROUPING_SECOND_LABEL;
		
		public static String GROUPING_THIRD_LABEL = "groupingThirdLabel";
		public static String GROUPING_THIRD_LABEL_FIELD = GROUPING_THIRD_LABEL + PARAMETER_NAME.NAME_SUFFIX;
		public static String GROUPING_THIRD_LABEL_VALUE = PARAMETER_NAME.MAP_PREFIX + GROUPING_THIRD_LABEL;
		
		public static String GROUPING_FOURTH_LABEL = "groupingFourthLabel";
		public static String GROUPING_FOURTH_LABEL_FIELD = GROUPING_FOURTH_LABEL + PARAMETER_NAME.NAME_SUFFIX;
		public static String GROUPING_FOURTH_LABEL_VALUE = PARAMETER_NAME.MAP_PREFIX + GROUPING_FOURTH_LABEL;
		
		public static String GROUPING_FIFTH_LABEL = "groupingFifthLabel";
		public static String GROUPING_FIFTH_LABEL_FIELD = GROUPING_FIFTH_LABEL + PARAMETER_NAME.NAME_SUFFIX;
		public static String GROUPING_FIFTH_LABEL_VALUE = PARAMETER_NAME.MAP_PREFIX + GROUPING_FIFTH_LABEL;
		
	}
	
	public static interface TIME_INTERVAL {
		/**
		 * The reporting period is one day.
		 */
		static int DAY = 1;
		/**
		 * The reporting period is one week.
		 */
		static int WEEK = 2;
		/**
		 * The reporting period is one month.
		 */
		static int MONTH = 3;
	}		
	
	/**
	 * Localized list of possible localized time intervals.
	 * @param locale
	 * @return
	 */
	public static String getLocalizedTimeInterval(Locale locale, String bundleName, int timeInterval) {
		switch (timeInterval) {
		case TIME_INTERVAL.DAY:
			return LocalizeUtil.getLocalizedText("common.timeInterval.day", locale, bundleName);
		case TIME_INTERVAL.WEEK:
			return LocalizeUtil.getLocalizedText("common.timeInterval.week", locale, bundleName);
		case TIME_INTERVAL.MONTH:
			return LocalizeUtil.getLocalizedText("common.timeInterval.month", locale, bundleName);
		}
		return "";
	}
	
	protected String getTimePeriodExtraParams(Map<String, Object> savedParamsMap,
			DatasourceDescriptor datasourceDescriptor,
			TPersonBean personBean, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder();
		JSONUtility.appendIntegerArrayAsArray(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.STATUSES, 
				(Integer[])savedParamsMap.get(TIME_INTERVAL_PARAMETER_NAME.STATUSES));
		
		JSONUtility.appendIntegerArrayAsArray(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.STATUSES_SECOND, 
				(Integer[])savedParamsMap.get(TIME_INTERVAL_PARAMETER_NAME.STATUSES_SECOND));
		
		JSONUtility.appendIntegerArrayAsArray(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.STATUSES_THIRD, 
				(Integer[])savedParamsMap.get(TIME_INTERVAL_PARAMETER_NAME.STATUSES_THIRD));
		
		JSONUtility.appendIntegerArrayAsArray(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.STATUSES_FIFTH, 
				(Integer[])savedParamsMap.get(TIME_INTERVAL_PARAMETER_NAME.STATUSES_FIFTH));
		
		JSONUtility.appendIntegerArrayAsArray(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.STATUSES_FOURTH, 
				(Integer[])savedParamsMap.get(TIME_INTERVAL_PARAMETER_NAME.STATUSES_FOURTH));
		
		JSONUtility.appendIntegerValue(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.TIME_INTERVAL, 
				(Integer)savedParamsMap.get(TIME_INTERVAL_PARAMETER_NAME.TIME_INTERVAL));
		
		if (savedParamsMap.get(TIME_INTERVAL_PARAMETER_NAME.GROUPING)!=null) {
			JSONUtility.appendBooleanValue(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.GROUPING, 
				(Boolean)savedParamsMap.get(TIME_INTERVAL_PARAMETER_NAME.GROUPING));
		}
		
		JSONUtility.appendBooleanValue(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.IS_STATUS_OVER_TIME, 
				isStatusOverTime());
		
		JSONUtility.appendStringValue(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.GROUPING_FIRST_LABEL, 
				(String)savedParamsMap.get(TIME_INTERVAL_PARAMETER_NAME.GROUPING_FIRST_LABEL));
		
		JSONUtility.appendStringValue(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.GROUPING_SECOND_LABEL, 
				(String)savedParamsMap.get(TIME_INTERVAL_PARAMETER_NAME.GROUPING_SECOND_LABEL));
		
		JSONUtility.appendStringValue(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.GROUPING_THIRD_LABEL, 
				(String)savedParamsMap.get(TIME_INTERVAL_PARAMETER_NAME.GROUPING_THIRD_LABEL));
		
		JSONUtility.appendStringValue(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.GROUPING_FOURTH_LABEL, 
				(String)savedParamsMap.get(TIME_INTERVAL_PARAMETER_NAME.GROUPING_FOURTH_LABEL));
		
		JSONUtility.appendStringValue(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.GROUPING_FIFTH_LABEL, 
				(String)savedParamsMap.get(TIME_INTERVAL_PARAMETER_NAME.GROUPING_FIFTH_LABEL));
		
		JSONUtility.appendStringValue(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.TIME_INTERVAL_NAME_FIELD,
				TIME_INTERVAL_PARAMETER_NAME.TIME_INTERVAL_NAME_VALUE);
		
		JSONUtility.appendStringValue(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.GROUPING_NAME_FIELD,
				TIME_INTERVAL_PARAMETER_NAME.GROUPING_NAME_VALUE);
		
		JSONUtility.appendStringValue(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.GROUPING_FIRST_LABEL_FIELD,
				TIME_INTERVAL_PARAMETER_NAME.GROUPING_FIRST_LABEL_VALUE);
		
		JSONUtility.appendStringValue(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.GROUPING_SECOND_LABEL_FIELD,
				TIME_INTERVAL_PARAMETER_NAME.GROUPING_SECOND_LABEL_VALUE);
		
		JSONUtility.appendStringValue(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.GROUPING_THIRD_LABEL_FIELD,
				TIME_INTERVAL_PARAMETER_NAME.GROUPING_THIRD_LABEL_VALUE);
		
		JSONUtility.appendStringValue(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.GROUPING_FOURTH_LABEL_FIELD,
				TIME_INTERVAL_PARAMETER_NAME.GROUPING_FOURTH_LABEL_VALUE);
		
		JSONUtility.appendStringValue(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.GROUPING_FIFTH_LABEL_FIELD,
				TIME_INTERVAL_PARAMETER_NAME.GROUPING_FIFTH_LABEL_VALUE);
		
		
		List<IntegerStringBean> timeIntervalOptions = new LinkedList<IntegerStringBean>();
		timeIntervalOptions.add(new IntegerStringBean("common.timeInterval.day", Integer.valueOf(TIME_INTERVAL.DAY)));
		timeIntervalOptions.add(new IntegerStringBean("common.timeInterval.week", Integer.valueOf(TIME_INTERVAL.WEEK)));
		timeIntervalOptions.add(new IntegerStringBean("common.timeInterval.month", Integer.valueOf(TIME_INTERVAL.MONTH)));
		JSONUtility.appendIntegerStringBeanList(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.TIME_INTERVAL_OPTIONS, timeIntervalOptions, true);
		String extraTimePeriodWithTimeIntervalDatasource = getTimeIntervalExtraParams(savedParamsMap, datasourceDescriptor, personBean, locale);
		if (extraTimePeriodWithTimeIntervalDatasource!=null && !"".equals(extraTimePeriodWithTimeIntervalDatasource)) {
			stringBuilder.append(",").append(extraTimePeriodWithTimeIntervalDatasource);
		}
		return stringBuilder.toString();
	}
	
	protected String getTimeIntervalExtraParams(Map<String, Object> params,
			DatasourceDescriptor datasourceDescriptor,
			TPersonBean personBean, Locale locale) {
		return null;
	}
	
	/**
	 * 
	 * @param paramSettings
	 * @return
	 */
	protected Map<String, Object> loadParamObjectsFromPropertyStrings(String paramSettings) {
		Map<String, Object> paramsMap = super.loadParamObjectsFromPropertyStrings(paramSettings);
		if (paramSettings!=null) {
			Integer timeInterval = PropertiesHelper.getIntegerProperty(paramSettings, TIME_INTERVAL_PARAMETER_NAME.TIME_INTERVAL);
			if (timeInterval==null) {
				timeInterval = TIME_INTERVAL.MONTH;
			}
			paramsMap.put(TIME_INTERVAL_PARAMETER_NAME.TIME_INTERVAL, timeInterval);
			
			Boolean grouping = PropertiesHelper.getBooleanProperty(paramSettings, TIME_INTERVAL_PARAMETER_NAME.GROUPING);
			if(grouping==null) {
				grouping = false;
			}			
			paramsMap.put(TIME_INTERVAL_PARAMETER_NAME.GROUPING, grouping);
			
			
			paramsMap.put(TIME_INTERVAL_PARAMETER_NAME.GROUPING, isStatusOverTime());
			
			String groupingFirstLabel = PropertiesHelper.getStringProperty(paramSettings, TIME_INTERVAL_PARAMETER_NAME.GROUPING_FIRST_LABEL);
			if(groupingFirstLabel==null) {
				groupingFirstLabel = "";
			}			
			paramsMap.put(TIME_INTERVAL_PARAMETER_NAME.GROUPING_FIRST_LABEL, groupingFirstLabel);
			
			String groupingSecondLabel = PropertiesHelper.getStringProperty(paramSettings, TIME_INTERVAL_PARAMETER_NAME.GROUPING_SECOND_LABEL);
			if(groupingSecondLabel==null) {
				groupingSecondLabel = "";
			}			
			paramsMap.put(TIME_INTERVAL_PARAMETER_NAME.GROUPING_SECOND_LABEL, groupingSecondLabel);
			
			String groupingThirdLabel = PropertiesHelper.getStringProperty(paramSettings, TIME_INTERVAL_PARAMETER_NAME.GROUPING_THIRD_LABEL);
			if(groupingThirdLabel==null) {
				groupingThirdLabel = "";
			}			
			paramsMap.put(TIME_INTERVAL_PARAMETER_NAME.GROUPING_THIRD_LABEL, groupingThirdLabel);
			
			String groupingFourthLabel = PropertiesHelper.getStringProperty(paramSettings, TIME_INTERVAL_PARAMETER_NAME.GROUPING_FOURTH_LABEL);
			if(groupingFourthLabel==null) {
				groupingFourthLabel = "";
			}			
			paramsMap.put(TIME_INTERVAL_PARAMETER_NAME.GROUPING_FOURTH_LABEL, groupingFourthLabel);
			
			String groupingFifthLabel = PropertiesHelper.getStringProperty(paramSettings, TIME_INTERVAL_PARAMETER_NAME.GROUPING_FIFTH_LABEL);
			if(groupingFifthLabel==null) {
				groupingFifthLabel = "";
			}			
			paramsMap.put(TIME_INTERVAL_PARAMETER_NAME.GROUPING_FIFTH_LABEL, groupingFifthLabel);
			
			String statusesArr = PropertiesHelper.getProperty(paramSettings, TIME_INTERVAL_PARAMETER_NAME.STATUSES);
			Integer[] statuses = GeneralUtils.createIntegerArrFromCommaSeparatedString(statusesArr);
			paramsMap.put(TIME_INTERVAL_PARAMETER_NAME.STATUSES, statuses);
			
			String statusesSecondArr = PropertiesHelper.getProperty(paramSettings, TIME_INTERVAL_PARAMETER_NAME.STATUSES_SECOND);
			Integer[] statusesSecond = GeneralUtils.createIntegerArrFromCommaSeparatedString(statusesSecondArr);
			paramsMap.put(TIME_INTERVAL_PARAMETER_NAME.STATUSES_SECOND, statusesSecond);

			String statusesThirdArr = PropertiesHelper.getProperty(paramSettings, TIME_INTERVAL_PARAMETER_NAME.STATUSES_THIRD);
			Integer[] statusesThird = GeneralUtils.createIntegerArrFromCommaSeparatedString(statusesThirdArr);
			paramsMap.put(TIME_INTERVAL_PARAMETER_NAME.STATUSES_THIRD, statusesThird);
			
			String statusesFourthArr = PropertiesHelper.getProperty(paramSettings, TIME_INTERVAL_PARAMETER_NAME.STATUSES_FOURTH);
			Integer[] statusesFourth = GeneralUtils.createIntegerArrFromCommaSeparatedString(statusesFourthArr);
			paramsMap.put(TIME_INTERVAL_PARAMETER_NAME.STATUSES_FOURTH, statusesFourth);
			
			String statusesFifthArr = PropertiesHelper.getProperty(paramSettings, TIME_INTERVAL_PARAMETER_NAME.STATUSES_FIFTH);
			Integer[] statusesFifth = GeneralUtils.createIntegerArrFromCommaSeparatedString(statusesFifthArr);
			paramsMap.put(TIME_INTERVAL_PARAMETER_NAME.STATUSES_FIFTH, statusesFifth);
			
		} else {
			paramsMap.put(TIME_INTERVAL_PARAMETER_NAME.TIME_INTERVAL, TIME_INTERVAL.MONTH);
			paramsMap.put(TIME_INTERVAL_PARAMETER_NAME.GROUPING, false);
			paramsMap.put(TIME_INTERVAL_PARAMETER_NAME.GROUPING_FIRST_LABEL, "");
			paramsMap.put(TIME_INTERVAL_PARAMETER_NAME.GROUPING_SECOND_LABEL, "");
			paramsMap.put(TIME_INTERVAL_PARAMETER_NAME.GROUPING_THIRD_LABEL, "");
			paramsMap.put(TIME_INTERVAL_PARAMETER_NAME.GROUPING_FOURTH_LABEL, "");
		}
		return paramsMap;
	}
	
	/**
	 * Gets the parameter settings for report to be executed and the parameters to be saved 
	 * @param parameters parameters as String[]
	 * @param locale
	 * @param savedParamsMap output parameter: parameters transformed to the actual types
	 * @return
	 */
	protected String loadParamObjectsAndPropertyStringsAndFromStringArrParams(Map<String, String[]> params, Locale locale, Map<String, Object> savedParamsMap) {
		String paramSettings = super.loadParamObjectsAndPropertyStringsAndFromStringArrParams(params, locale, savedParamsMap);
		
		Integer timeInterval = StringArrayParameterUtils.parseIntegerValue(params, 
				TIME_INTERVAL_PARAMETER_NAME.TIME_INTERVAL, TIME_INTERVAL.MONTH);
		
		
		Boolean grouping = StringArrayParameterUtils.parseBooleanValue(params, 
				TIME_INTERVAL_PARAMETER_NAME.GROUPING, false);
		
		String groupingFirstLabel = StringArrayParameterUtils.parseStringValue(params, 
				TIME_INTERVAL_PARAMETER_NAME.GROUPING_FIRST_LABEL, "");
		
		String groupingSecondLabel = StringArrayParameterUtils.parseStringValue(params, 
				TIME_INTERVAL_PARAMETER_NAME.GROUPING_SECOND_LABEL, "");
		
		String groupingThirdLabel = StringArrayParameterUtils.parseStringValue(params, 
				TIME_INTERVAL_PARAMETER_NAME.GROUPING_THIRD_LABEL, "");
		
		String groupingFourthLabel = StringArrayParameterUtils.parseStringValue(params, 
				TIME_INTERVAL_PARAMETER_NAME.GROUPING_FOURTH_LABEL, "");
		
		String groupingFifthLabel = StringArrayParameterUtils.parseStringValue(params, 
				TIME_INTERVAL_PARAMETER_NAME.GROUPING_FIFTH_LABEL, "");
		
		savedParamsMap.put(TIME_INTERVAL_PARAMETER_NAME.TIME_INTERVAL, timeInterval);
		savedParamsMap.put(TIME_INTERVAL_PARAMETER_NAME.GROUPING, grouping);
		savedParamsMap.put(TIME_INTERVAL_PARAMETER_NAME.GROUPING_FIRST_LABEL, groupingFirstLabel);
		savedParamsMap.put(TIME_INTERVAL_PARAMETER_NAME.GROUPING_SECOND_LABEL, groupingSecondLabel);
		savedParamsMap.put(TIME_INTERVAL_PARAMETER_NAME.GROUPING_THIRD_LABEL, groupingThirdLabel);
		savedParamsMap.put(TIME_INTERVAL_PARAMETER_NAME.GROUPING_FOURTH_LABEL, groupingFourthLabel);
		savedParamsMap.put(TIME_INTERVAL_PARAMETER_NAME.GROUPING_FIFTH_LABEL, groupingFifthLabel);
		
		paramSettings = PropertiesHelper.setIntegerProperty(paramSettings, TIME_INTERVAL_PARAMETER_NAME.TIME_INTERVAL,
				timeInterval);
		
		paramSettings = PropertiesHelper.setBooleanProperty(paramSettings, TIME_INTERVAL_PARAMETER_NAME.GROUPING,
				grouping);
		
		paramSettings = PropertiesHelper.setStringProperty(paramSettings, TIME_INTERVAL_PARAMETER_NAME.GROUPING_FIRST_LABEL,
				groupingFirstLabel);
		
		paramSettings = PropertiesHelper.setStringProperty(paramSettings, TIME_INTERVAL_PARAMETER_NAME.GROUPING_SECOND_LABEL,
				groupingSecondLabel);
		
		paramSettings = PropertiesHelper.setStringProperty(paramSettings, TIME_INTERVAL_PARAMETER_NAME.GROUPING_THIRD_LABEL,
				groupingThirdLabel);
		
		paramSettings = PropertiesHelper.setStringProperty(paramSettings, TIME_INTERVAL_PARAMETER_NAME.GROUPING_FOURTH_LABEL,
				groupingFourthLabel);
		
		paramSettings = PropertiesHelper.setStringProperty(paramSettings, TIME_INTERVAL_PARAMETER_NAME.GROUPING_FIFTH_LABEL,
				groupingFifthLabel);
		
		Integer[] statuses = GeneralUtils.createIntegerArrFromCommaSeparatedString(
				StringArrayParameterUtils.getStringValue(params, TIME_INTERVAL_PARAMETER_NAME.STATUSES));
		savedParamsMap.put(TIME_INTERVAL_PARAMETER_NAME.STATUSES, statuses);
		paramSettings = PropertiesHelper.setProperty(paramSettings, TIME_INTERVAL_PARAMETER_NAME.STATUSES,
				GeneralUtils.createCommaSeparatedStringFromIntegerArr(statuses));
		
		Integer[] statusesSecond = GeneralUtils.createIntegerArrFromCommaSeparatedString(
				StringArrayParameterUtils.getStringValue(params, TIME_INTERVAL_PARAMETER_NAME.STATUSES_SECOND));
		savedParamsMap.put(TIME_INTERVAL_PARAMETER_NAME.STATUSES_SECOND, statusesSecond);
		paramSettings = PropertiesHelper.setProperty(paramSettings, TIME_INTERVAL_PARAMETER_NAME.STATUSES_SECOND,
				GeneralUtils.createCommaSeparatedStringFromIntegerArr(statusesSecond));
		
		Integer[] statusesThird = GeneralUtils.createIntegerArrFromCommaSeparatedString(
				StringArrayParameterUtils.getStringValue(params, TIME_INTERVAL_PARAMETER_NAME.STATUSES_THIRD));
		savedParamsMap.put(TIME_INTERVAL_PARAMETER_NAME.STATUSES_THIRD, statusesThird);
		paramSettings = PropertiesHelper.setProperty(paramSettings, TIME_INTERVAL_PARAMETER_NAME.STATUSES_THIRD,
				GeneralUtils.createCommaSeparatedStringFromIntegerArr(statusesThird));
		
		Integer[] statusesFourth = GeneralUtils.createIntegerArrFromCommaSeparatedString(
				StringArrayParameterUtils.getStringValue(params, TIME_INTERVAL_PARAMETER_NAME.STATUSES_FOURTH));
		savedParamsMap.put(TIME_INTERVAL_PARAMETER_NAME.STATUSES_FOURTH, statusesFourth);
		paramSettings = PropertiesHelper.setProperty(paramSettings, TIME_INTERVAL_PARAMETER_NAME.STATUSES_FOURTH,
				GeneralUtils.createCommaSeparatedStringFromIntegerArr(statusesFourth));
		
		Integer[] statusesFifth = GeneralUtils.createIntegerArrFromCommaSeparatedString(
				StringArrayParameterUtils.getStringValue(params, TIME_INTERVAL_PARAMETER_NAME.STATUSES_FIFTH));
		savedParamsMap.put(TIME_INTERVAL_PARAMETER_NAME.STATUSES_FIFTH, statusesFifth);
		paramSettings = PropertiesHelper.setProperty(paramSettings, TIME_INTERVAL_PARAMETER_NAME.STATUSES_FIFTH,
				GeneralUtils.createCommaSeparatedStringFromIntegerArr(statusesFifth));
		
		
		return paramSettings;
	}
	
	/**
	 * Add the time series to the timeSeriesCollection
	 * SortedMap at first and second level (year and period)
	 * (Sorted because the accumulated should be calculated in the right order)
	 * @param timeSeriesCollection
	 * @param yearToPeriodToOpenedWorkItemCountMap
	 * @param selectedTimeInterval
	*/
	public SortedMap<Date, Object/*Double or Integer or Map<Integer, Integer>*/> transformPeriodsToDates(
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
	protected Date createDate(int period, int year, int timeInterval) {
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
	 protected void addZerosForEmptyIntervals(Date dateFrom, Date dateTo, int selectedTimeInterval,
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

	/**
	 * Set zero value for time
	 * @param yearToPeriodToWorkItemsCount
	 * @param year
	 * @param period
	 * @param entityID
	 */
	protected void setZeroIfMissing(SortedMap<Integer, SortedMap<Integer, Object/*Double or Integer*/>> yearToPeriodToWorkItemsCount, 
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
	
	public boolean isStatusOverTime() {
		return false;
	}
}
