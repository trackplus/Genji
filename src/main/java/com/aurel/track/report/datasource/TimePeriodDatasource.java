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

package com.aurel.track.report.datasource;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.plugin.DatasourceDescriptor;
import com.aurel.track.util.CalendarUtil;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.PropertiesHelper;
import com.aurel.track.util.StringArrayParameterUtils;

public abstract class TimePeriodDatasource extends BasicDatasource {
	
	public static interface TIME_PERIOD_PARAMETER_NAME {
		/**
		 * Period type
		 */
		public static String PERIOD_TYPE = "periodType";
		public static String PERIOD_TYPE_OPTIONS = PERIOD_TYPE + PARAMETER_NAME.OPTION_SUFFIX;
		public static String PERIOD_TYPE_NAME_FIELD = PERIOD_TYPE + PARAMETER_NAME.NAME_SUFFIX;
		public static String PERIOD_TYPE_NAME_VALUE = PARAMETER_NAME.MAP_PREFIX + PERIOD_TYPE;
		
		/**
		 * The date from
		 */
		public static String DATE_FROM = "dateFrom";
		public static String DATE_FROM_NAME_FIELD = DATE_FROM + PARAMETER_NAME.NAME_SUFFIX;
		public static String DATE_FROM_NAME_VALUE = PARAMETER_NAME.MAP_PREFIX + DATE_FROM;
		
		/**
		 * The date to
		 */
		public static String DATE_TO = "dateTo";
		public static String DATE_TO_NAME_FIELD = DATE_TO + PARAMETER_NAME.NAME_SUFFIX;
		public static String DATE_TO_NAME_VALUE = PARAMETER_NAME.MAP_PREFIX + DATE_TO;
		
		/**
		 * Days before
		 */
		public static String DAYS_BEFORE = "daysBefore";
		public static String DAYS_BEFORE_NAME_FIELD = DAYS_BEFORE + PARAMETER_NAME.NAME_SUFFIX;
		public static String DAYS_BEFORE_NAME_VALUE = PARAMETER_NAME.MAP_PREFIX + DAYS_BEFORE;
	}
	
	/**
	 * Many datasources can be configured such that they consider only a certain
	 * time interval, for example the last 60 days, or alternatively an interval
	 * specified by a start and an end date. 
	 *
	 */
	public static interface PERIOD_TYPE {
		/**
		 * This time interval is defined by a start date and an end date.
		 */
		static int FROM_TO = 1;
		/**
		 * This time interval is defined by the current date as end date, and
		 * x days back as a start date.
		 */
		static int DAYS_BEFORE = 2;
		
		/**
		 * Current month
		 */
		static int CURRENT_MONTH = 3;
		
		/**
		 * Last month
		 */
		static int LAST_MONTH = 4;
	}
		
	
	/**
	 * Extra datasource configurations
	 * @param savedParamsMap
	 * @param datasourceDescriptor
	 * @param personBean
	 * @param locale
	 * @return
	 */
	@Override
	public String getDatasourceExtraParams(Map<String, Object> savedParamsMap,
			DatasourceDescriptor datasourceDescriptor, Map<String, Object> contextMap,
			TPersonBean personBean, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder();
		JSONUtility.appendIntegerValue(stringBuilder, TIME_PERIOD_PARAMETER_NAME.PERIOD_TYPE, 
				(Integer)savedParamsMap.get(TIME_PERIOD_PARAMETER_NAME.PERIOD_TYPE));
		JSONUtility.appendStringValue(stringBuilder, TIME_PERIOD_PARAMETER_NAME.PERIOD_TYPE_NAME_FIELD,
				TIME_PERIOD_PARAMETER_NAME.PERIOD_TYPE_NAME_VALUE);
		Date dateFrom = (Date)savedParamsMap.get(TIME_PERIOD_PARAMETER_NAME.DATE_FROM);
		if (dateFrom!=null) {
			JSONUtility.appendLocaleFormattedDateValue(stringBuilder, TIME_PERIOD_PARAMETER_NAME.DATE_FROM, dateFrom, locale);
		}
		JSONUtility.appendStringValue(stringBuilder, TIME_PERIOD_PARAMETER_NAME.DATE_FROM_NAME_FIELD,
				TIME_PERIOD_PARAMETER_NAME.DATE_FROM_NAME_VALUE);
		
		Date dateTo = (Date)savedParamsMap.get(TIME_PERIOD_PARAMETER_NAME.DATE_TO);
		if (dateTo!=null) {
			JSONUtility.appendLocaleFormattedDateValue(stringBuilder, TIME_PERIOD_PARAMETER_NAME.DATE_TO, dateTo, locale);
		}
		JSONUtility.appendStringValue(stringBuilder, TIME_PERIOD_PARAMETER_NAME.DATE_TO_NAME_FIELD,
				TIME_PERIOD_PARAMETER_NAME.DATE_TO_NAME_VALUE);
		JSONUtility.appendIntegerValue(stringBuilder, TIME_PERIOD_PARAMETER_NAME.DAYS_BEFORE,
				(Integer)savedParamsMap.get(TIME_PERIOD_PARAMETER_NAME.DAYS_BEFORE));
		JSONUtility.appendStringValue(stringBuilder, TIME_PERIOD_PARAMETER_NAME.DAYS_BEFORE_NAME_FIELD,
				TIME_PERIOD_PARAMETER_NAME.DAYS_BEFORE_NAME_VALUE);
		List<IntegerStringBean> periodTypeOptions = new LinkedList<IntegerStringBean>();
		periodTypeOptions.add(new IntegerStringBean("common.timePeriod.currentMonth", Integer.valueOf(PERIOD_TYPE.CURRENT_MONTH)));
		periodTypeOptions.add(new IntegerStringBean("common.timePeriod.lastMonth", Integer.valueOf(PERIOD_TYPE.LAST_MONTH)));
		periodTypeOptions.add(new IntegerStringBean("common.timePeriod.fromTo", Integer.valueOf(PERIOD_TYPE.FROM_TO)));
		periodTypeOptions.add(new IntegerStringBean("common.timePeriod.daysBefore", Integer.valueOf(PERIOD_TYPE.DAYS_BEFORE)));
		JSONUtility.appendIntegerStringBeanList(stringBuilder, TIME_PERIOD_PARAMETER_NAME.PERIOD_TYPE_OPTIONS, periodTypeOptions, true);
		
		String extraTimePeriodDatasource = getTimePeriodExtraParams(savedParamsMap, datasourceDescriptor, personBean, locale);
		if (extraTimePeriodDatasource!=null && !"".equals(extraTimePeriodDatasource)) {
			stringBuilder.append(",").append(extraTimePeriodDatasource);
		}
		return stringBuilder.toString();
	}
	
	protected String getTimePeriodExtraParams(Map<String, Object> savedParamsMap,
			DatasourceDescriptor datasourceDescriptor,
			TPersonBean personBean, Locale locale) {
		return null;
	}
	
	/**
	 * Gets the parameter settings for report to be executed and the parameters to be saved 
	 * @param parameters parameters as String[]
	 * @param locale
	 * @param savedParamsMap output parameter: parameters transformed to the actual types
	 * @return
	 */
	@Override
	protected String loadParamObjectsAndPropertyStringsAndFromStringArrParams(
			Map<String, String[]> params, Locale locale, Map<String, Object> savedParamsMap) {
		String paramSettings = super.loadParamObjectsAndPropertyStringsAndFromStringArrParams(params, locale, savedParamsMap);
		Integer periodType = StringArrayParameterUtils.parseIntegerValue(params, 
				TIME_PERIOD_PARAMETER_NAME.PERIOD_TYPE, PERIOD_TYPE.CURRENT_MONTH);
		paramSettings = PropertiesHelper.setIntegerProperty(paramSettings, TIME_PERIOD_PARAMETER_NAME.PERIOD_TYPE, periodType);
		String dateFromStr = StringArrayParameterUtils.getStringValue(params, TIME_PERIOD_PARAMETER_NAME.DATE_FROM);
		Date dateFrom = null;
		if (dateFromStr!=null) {
			dateFrom = DateTimeUtils.getInstance().parseGUIDate(dateFromStr, locale);
		}
		paramSettings = PropertiesHelper.setProperty(paramSettings, TIME_PERIOD_PARAMETER_NAME.DATE_FROM,
				DateTimeUtils.getInstance().formatISODate(dateFrom));
		String dateToStr = StringArrayParameterUtils.getStringValue(params, TIME_PERIOD_PARAMETER_NAME.DATE_TO);
		Date dateTo = null;
		if (dateToStr!=null) {
			dateTo = DateTimeUtils.getInstance().parseGUIDate(dateToStr, locale);
		}
		paramSettings = PropertiesHelper.setProperty(paramSettings, TIME_PERIOD_PARAMETER_NAME.DATE_TO,
				DateTimeUtils.getInstance().formatISODate(dateTo));
		Integer daysBefore = StringArrayParameterUtils.parseIntegerValue(params,
				TIME_PERIOD_PARAMETER_NAME.DAYS_BEFORE);
		Date calculatedDateFrom = getDateFrom(periodType, dateFrom, daysBefore);
		savedParamsMap.put(TIME_PERIOD_PARAMETER_NAME.DATE_FROM, calculatedDateFrom);
		Date calculatedDateTo = getDateTo(periodType, dateTo);
		savedParamsMap.put(TIME_PERIOD_PARAMETER_NAME.DATE_TO, calculatedDateTo);
		paramSettings = PropertiesHelper.setIntegerProperty(paramSettings,
				TIME_PERIOD_PARAMETER_NAME.DAYS_BEFORE, daysBefore);
		return paramSettings;
	}
	
	/**
	 * 
	 * @param paramSettings
	 * @return
	 */
	@Override
	protected Map<String, Object> loadParamObjectsFromPropertyStrings(String paramSettings) {
		Map<String, Object> paramsMap = super.loadParamObjectsFromPropertyStrings(paramSettings);
		if (paramSettings!=null) {
			Integer periodType = PropertiesHelper.getIntegerProperty(paramSettings, TIME_PERIOD_PARAMETER_NAME.PERIOD_TYPE);
			if (periodType==null) {
				periodType = PERIOD_TYPE.CURRENT_MONTH;
			}
			paramsMap.put(TIME_PERIOD_PARAMETER_NAME.PERIOD_TYPE, periodType);
			Date dateFrom = null;
			String dateFromStr = PropertiesHelper.getProperty(paramSettings, TIME_PERIOD_PARAMETER_NAME.DATE_FROM);
			if (dateFromStr!=null) {
				dateFrom = DateTimeUtils.getInstance().parseISODate(dateFromStr);
			}
			paramsMap.put(TIME_PERIOD_PARAMETER_NAME.DATE_FROM, dateFrom);
			Date dateTo = null;
			String dateToStr = PropertiesHelper.getProperty(paramSettings, TIME_PERIOD_PARAMETER_NAME.DATE_TO);
			if (dateToStr!=null) {
				dateTo = DateTimeUtils.getInstance().parseISODate(dateToStr);
			}
			paramsMap.put(TIME_PERIOD_PARAMETER_NAME.DATE_TO, dateTo);
			Integer daysBefore = PropertiesHelper.getIntegerProperty(paramSettings, TIME_PERIOD_PARAMETER_NAME.DAYS_BEFORE);
			paramsMap.put(TIME_PERIOD_PARAMETER_NAME.DAYS_BEFORE, daysBefore);
		} else {
			paramsMap.put(TIME_PERIOD_PARAMETER_NAME.PERIOD_TYPE, PERIOD_TYPE.CURRENT_MONTH);
		}
		return paramsMap;
	}
	
	/**
	 * Get the dateFrom value
	 * @param parameters
	 * @param locale
	 * @return
	 */
	protected Date getDateFrom(Integer periodType, Date dateFrom, Integer daysBefore) {
		Date calculatedDateFrom = null;
		Calendar calendar = Calendar.getInstance();
		CalendarUtil.clearTime(calendar);
		switch (periodType) {
		case PERIOD_TYPE.FROM_TO:
			if (dateFrom==null) {
				calculatedDateFrom = calendar.getTime();
			} else {
				calculatedDateFrom = dateFrom;
			}
			break;
		case PERIOD_TYPE.DAYS_BEFORE:
			if (daysBefore!=null) {
				calendar.add(Calendar.DATE, -daysBefore.intValue());
				calculatedDateFrom = calendar.getTime();
			}
			break;
		case PERIOD_TYPE.CURRENT_MONTH:
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
			calculatedDateFrom = calendar.getTime();
			break;
		case PERIOD_TYPE.LAST_MONTH:
			calendar.add(Calendar.MONTH, -1);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
			calculatedDateFrom = calendar.getTime();
			break;
		}		
		return calculatedDateFrom;
	}
	
	/**
	 * Get the dateTo value: actually the beginning of the day after toDate
	 * @param parameters
	 * @param loaderResourceBundleMessages
	 * @return
	 */
	protected Date getDateTo(Integer periodType, Date dateTo) {
		Date calculatedDateTo = null;
		Calendar calendar = Calendar.getInstance();
		CalendarUtil.clearTime(calendar);
		switch (periodType) {
		case PERIOD_TYPE.FROM_TO:
			if (dateTo==null) {
				calculatedDateTo = calendar.getTime();
			} else {
				calculatedDateTo = dateTo;
			}
			calendar.setTime(calculatedDateTo);
			calendar.add(Calendar.DATE, 1);
			//the end of toDate day (actually the beginning of the day after toDate)
			calculatedDateTo = calendar.getTime();
			break;
		case PERIOD_TYPE.DAYS_BEFORE:
			calendar.add(Calendar.DATE, 1);
			calculatedDateTo = calendar.getTime();
			break;
		case PERIOD_TYPE.CURRENT_MONTH:
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
			calendar.add(Calendar.MONTH, 1);
			calculatedDateTo = calendar.getTime();
			break;
		case PERIOD_TYPE.LAST_MONTH:
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
			calculatedDateTo = calendar.getTime();
			break;
		}
		return calculatedDateTo;
	}
	
}
