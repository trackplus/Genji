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

package com.aurel.track.report.dashboard;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.CalendarUtil;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.IntegerStringBean;

public class TimePeriodDashboardView extends ProjectFilterDashboardView {

	public static interface PERIOD_TYPE {
		static int FROM_TO = 1;
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
	public static interface TIMEPERIOD_PARAMETERS {
		static String PERIOD_TYES = "periodTypes";
		static String SELECTED_PERIOD_TYPE = "selectedPeriodType";
		static String DATE_FROM = "dateFrom";
		static String DATE_TO = "dateTo";
		static String DAYS_BEFORE = "daysBefore";
	}
	
	/**
	 * Gets the time period configuration
	 * @param parameters
	 * @param locale
	 * @return
	 */
	protected String getTimePeriodConfig(Map<String,String> parameters, Locale locale) {
		StringBuilder sb = new StringBuilder();
		JSONUtility.appendIntegerStringBeanList(sb,TIMEPERIOD_PARAMETERS.PERIOD_TYES, possiblePeriodTypes(locale));
		JSONUtility.appendIntegerValue(sb,TIMEPERIOD_PARAMETERS.SELECTED_PERIOD_TYPE,
				new Integer(BasePluginDashboardBL.parseIntegerValue(parameters, TIMEPERIOD_PARAMETERS.SELECTED_PERIOD_TYPE, PERIOD_TYPE.DAYS_BEFORE)));
		Date dateFrom=DateTimeUtils.getInstance().parseISODate(parameters.get(TIMEPERIOD_PARAMETERS.DATE_FROM));
		JSONUtility.appendLocaleFormattedDateValue(sb,TIMEPERIOD_PARAMETERS.DATE_FROM,dateFrom,locale);
		Date dateTo=DateTimeUtils.getInstance().parseISODate(parameters.get(TIMEPERIOD_PARAMETERS.DATE_TO));
		JSONUtility.appendLocaleFormattedDateValue(sb,TIMEPERIOD_PARAMETERS.DATE_TO,dateTo,locale);
		JSONUtility.appendIntegerValue(sb,TIMEPERIOD_PARAMETERS.DAYS_BEFORE,
				new Integer(BasePluginDashboardBL.parseIntegerValue(parameters, TIMEPERIOD_PARAMETERS.DAYS_BEFORE, 30)));
		return sb.toString();
	}
	
	/**
	 * Get the list of possible period types
	 * @param locale
	 * @return
	 */
	private List<IntegerStringBean> possiblePeriodTypes(Locale locale) {
		List<IntegerStringBean> periodTypes = new ArrayList<IntegerStringBean>();
		periodTypes.add(new IntegerStringBean(
				localizeText("common.timePeriod.fromTo", locale),
				new Integer(PERIOD_TYPE.FROM_TO)));
		periodTypes.add(new IntegerStringBean(
				localizeText("common.timePeriod.daysBefore", locale),
				new Integer(PERIOD_TYPE.DAYS_BEFORE)));
		return periodTypes;
	}
	
	/**
	 * THe dates should be stored in local independent format
	 */
	protected String convertValueByKey(String key, String value, Locale locale) {
		if (key.equals(TIMEPERIOD_PARAMETERS.DATE_FROM)||
				key.equals(TIMEPERIOD_PARAMETERS.DATE_TO)){
			Date date=DateTimeUtils.getInstance().parseGUIDate(value, locale);
			return DateTimeUtils.getInstance().formatISODate(date);
		}
		return value;
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
	 * @param locale
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
