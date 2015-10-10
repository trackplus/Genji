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

//
// This file is part of the Genji application, a
// tool for project and change management.
//
// Copyright (C) 2004 Joerg Friedrich
//
// Author: Joerg Friedrich <joerg.friedrich@computer.org>
//
// $Id: DateTimeUtils.java 1359 2015-09-15 16:15:32Z tamas $

package com.aurel.track.util;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.joda.time.DateTime;
import org.joda.time.Days;



/**
 * This class contains support methods to print and convert date and time
 * @author Joerg Friedrich <joerg.friedrich@computer.org>
 * @author tamas ruff <tamas.ruff@ambo.ro> heavily refactorized to
 * cache the DateFormats and avoid the expensive  locale specific fetching all the time
 * @version $Revision: 1359 $ $Date: 2014-05-19 12:25:38 +0200 (Mo, 19 Mai 2014) $
 */
public class DateTimeUtils implements Serializable {

	private static final long serialVersionUID = 5841714163676914596L;

	public static final String ISO_DATE_TIME_FORMAT="yyyy-MM-dd H:mm:ss";
	public static final String ISO_DATE_FORMAT="yyyy-MM-dd";


	public static final long SECOND = 1000;

	//locale independent DateFormats for standard (ISO) formats
	private Calendar gmtCalendar;
	private DateFormat isoDateFormat;
	private DateFormat isoDateTimeFormat;

	private static final Logger LOGGER = LogManager.getLogger(DateTimeUtils.class);

//	static {
//		gmtCalendar = Calendar.getInstance();
//		gmtCalendar.set(Calendar.ZONE_OFFSET, 0);
//		isoDateTimeFormat = new SimpleDateFormat(ISO_DATE_TIME_FORMAT);
//		isoDateTimeFormat.setCalendar(gmtCalendar);
//		isoDateFormat = new SimpleDateFormat(ISO_DATE_FORMAT);
//		isoDateFormat.setCalendar(gmtCalendar);
//	}

	//the singleton instance
	private static DateTimeUtils instance;

	/*
	 * the cache map for the different locales
	 * 	-	key: locale
	 * 	-	value: DateFormatsBean initialized with different DateFormats
	 */
	private Map<Locale, DateFormatsBean> guiDateFormatsMap = new HashMap<Locale, DateFormatsBean>();

	/**
	 * Private constructor.
	 * Initializes the ISO DateFormats
	 */
	private DateTimeUtils() {
		super();
		gmtCalendar = Calendar.getInstance();
		gmtCalendar.set(Calendar.ZONE_OFFSET, 0);
		isoDateTimeFormat = new SimpleDateFormat(ISO_DATE_TIME_FORMAT);
		isoDateTimeFormat.setCalendar(gmtCalendar);
		isoDateFormat = new SimpleDateFormat(ISO_DATE_FORMAT);
		isoDateFormat.setCalendar(gmtCalendar);
	}

	/**
	 * Gets a singleton instance
	 * @return
	 */
	public static DateTimeUtils getInstance() {
		if (instance == null) {
			instance = new DateTimeUtils();
		}
		return instance;
	}


	/**
	 * Used for struts1 pages (because no automatic and locale dependent type conversion is made)
	 * @param locale
	 * @return
	 */
	public DateFormatsBean getDateFormatsByLocale(Locale locale) {
		if (locale==null) {
			locale = Locale.ENGLISH;
		}
		DateFormatsBean dateFormatsBean = guiDateFormatsMap.get(locale);
		if (dateFormatsBean==null) {
			dateFormatsBean = new DateFormatsBean();
			// at the GUI, present local times respecting time zone of Server
			// of course, would be better to respect time zone of current
			// user, but this would have to be configured for each user
			// this.currentLocale = cLocale;
			TimeZone tzone = TimeZone.getDefault();
			Calendar calendar = Calendar.getInstance(tzone, locale);
			DateFormat guiDateTimeFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG, locale);
			guiDateTimeFormat.setCalendar(calendar);
			//GUI calendar date time format: 4 digits year
			String guiDateTimePattern = ((SimpleDateFormat)guiDateTimeFormat).toPattern();
			//transform the Hour in am/pm in hour in day (0-23)
			guiDateTimePattern=guiDateTimePattern.replaceAll("h","H");
			//remove java Am/pm marker
			guiDateTimePattern = guiDateTimePattern.replaceAll("a", "");
			//remove java time zone
			guiDateTimePattern = guiDateTimePattern.replaceAll("z", "");
			//be sure the format is short
			guiDateTimePattern=guiDateTimePattern.replaceAll("dd","d");
			guiDateTimePattern=guiDateTimePattern.replaceAll("MM","M");
			guiDateTimePattern=guiDateTimePattern.replaceAll("YYYY", "YY");
			guiDateTimePattern=guiDateTimePattern.replaceAll("yyyy", "yy");
			//replace the format to be dd.MM.yyyy
			guiDateTimePattern=guiDateTimePattern.replaceAll("d","dd");
			guiDateTimePattern=guiDateTimePattern.replaceAll("M","MM");
			guiDateTimePattern=guiDateTimePattern.replaceAll("YY", "YYYY");
			guiDateTimePattern=guiDateTimePattern.replaceAll("yy", "yyyy");//most important: 4 digits year
			DateFormat guiCalendarDateTimeFormat=new SimpleDateFormat(guiDateTimePattern.trim());
			dateFormatsBean.setGuiDateTimeFormat(guiCalendarDateTimeFormat);
			//replace the seconds
			String guiDateTimePatternNoSeconds = guiDateTimePattern.replaceAll(":ss", "");
			DateFormat guiCalendarDateTimeNoSecondsFormat=new SimpleDateFormat(guiDateTimePatternNoSeconds.trim());
			dateFormatsBean.setGuiDateTimeNoSecondsFormat(guiCalendarDateTimeNoSecondsFormat);

			DateFormat guiDateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);
			guiDateFormat.setCalendar(calendar);

			//GUI calendar date format: 4 digits year
			String s=((SimpleDateFormat)guiDateFormat).toPattern();
			//be sure the format is short
			s=s.replaceAll("dd","d");
			s=s.replaceAll("MM","M");
			s=s.replaceAll("YYYY", "YY");
			s=s.replaceAll("yyyy", "yy");
			//replace the format to be dd.MM.yyyy
			s=s.replaceAll("d","dd");
			s=s.replaceAll("M","MM");
			s=s.replaceAll("YY", "YYYY");
			s=s.replaceAll("yy", "yyyy");//most imoportant: 4 digits year
			DateFormat guiCalendarDateFormat=new SimpleDateFormat(s);
			dateFormatsBean = convertJavaToExtJSFormats(dateFormatsBean, locale, tzone);
			dateFormatsBean.setShortDateFormat(guiDateFormat);
			dateFormatsBean.setShortDateTimeFormat(guiDateTimeFormat);
			dateFormatsBean.setGuiDateFormat(guiCalendarDateFormat);
			guiDateFormatsMap.put(locale, dateFormatsBean);
		}
		return dateFormatsBean;
	}

	public DateFormat getGuiDateFormat(Locale locale) {
		DateFormatsBean dateFormatsBean = getDateFormatsByLocale(locale);
		return dateFormatsBean.getShortDateFormat();
	}

	public String getExtJSDateFormat(Locale locale) {
		DateFormatsBean dateFormatsBean = getDateFormatsByLocale(locale);
		return dateFormatsBean.getExtJsDateFormat();
	}

	public String getExtJsTwoDigitsYearDateFormat(Locale locale) {
		DateFormatsBean dateFormatsBean = getDateFormatsByLocale(locale);
		return dateFormatsBean.getExtJsSubmitDateFormat();
	}


	public String getExtJsLongDateTimeFormat(Locale locale) {
		DateFormatsBean dateFormatsBean = getDateFormatsByLocale(locale);
		return dateFormatsBean.getExtJsDateTimeFormat();
	}

	public String getExtJSTimeFormat(Locale locale) {
		DateFormatsBean dateFormatsBean = getDateFormatsByLocale(locale);
		return dateFormatsBean.getExtJsTimeFormat();
	}

	public String getExtJSTimeFormatNoSeconds(Locale locale) {
		DateFormatsBean dateFormatsBean = getDateFormatsByLocale(locale);
		return dateFormatsBean.getExtJsTimeFormatNoSeconds();
	}


	/**
	 * Formats a Date object by the GUI date format
	 * @param date
	 * @return
	 */
	public String formatShortDate(Date date, Locale locale) {
		if (date != null) {
			DateFormatsBean dateFormatsBean = getDateFormatsByLocale(locale);
			return dateFormatsBean.getShortDateFormat().format(date);
		} else {
			return "";
		}
	}

	/**
	 * Parses a GUI date format string to a Date object
	 * @param guiDate
	 * @return
	 */
	public Date parseShortDate(String guiDate, Locale locale) {
		Date date = null;
		try {
			DateFormatsBean dateFormatsBean = getDateFormatsByLocale(locale);
			date = dateFormatsBean.getShortDateFormat().parse(guiDate);
		} catch (Exception e) {
		}
		return date;
	}

	/**
	 * Formats a Date object by the date-time format
	 * @param date
	 * @return
	 */
	public String formatShortDateTime(Date date, Locale locale) {
		if (date != null) {
			DateFormatsBean dateFormatsBean = getDateFormatsByLocale(locale);
			return dateFormatsBean.getShortDateTimeFormat().format(date);
		} else {
			return " ";
		}
	}

	/**
	 * Parses a GUI date format string to a Date object
	 * @param guiDateTime
	 * @param locale
	 * @return
	 */
	public Date parseShortDateTime(String guiDateTime, Locale locale) {
		Date date = null;
		DateFormatsBean dateFormatsBean = null;
		try {
			dateFormatsBean = getDateFormatsByLocale(locale);
			date = dateFormatsBean.getShortDateTimeFormat().parse(guiDateTime);
		} catch (Exception e) {
			LOGGER.warn(e.getMessage(), e);
			LOGGER.warn("Pattern should be " + dateFormatsBean.getShortDateTimeFormat().format(new Date()));
		}
		return date;
	}


	/**
	 * Whether the string can be parsed to a valid date
	 * @param date
	 * @return
	 */
	public boolean isValidGUIDate(String date, Locale locale) {
		if (parseShortDate(date, locale) != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Formats a Date object by the calendar date format
	 * @param date
	 * @return
	 */
	public String formatGUIDate(Date date, Locale locale){
		if(date==null){
			return "";
		}
		DateFormatsBean dateFormatsBean = getDateFormatsByLocale(locale);
		return dateFormatsBean.getGuiDateFormat().format(date);
	}

	/**
	 * Parse the date from a component gui that use a caledar js to edit
	 * @param sDate
	 * @return
	 */
	public Date parseGUIDate(String sDate, Locale locale){
		if(sDate==null || sDate.length()==0){
			return null;
		}
		DateFormatsBean dateFormatsBean = getDateFormatsByLocale(locale);
		try {
			return dateFormatsBean.getGuiDateFormat().parse(sDate);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Gets the date pattern for the gui date
	 * @param locale
	 * @return
	 */
	public String getGUIDatePattern(Locale locale) {
		DateFormatsBean dateFormatsBean = getDateFormatsByLocale(locale);
		return ((SimpleDateFormat)dateFormatsBean.getGuiDateFormat()).toPattern();
	}

	/**
	 * Formats a Date object by the date-time format
	 * @param date
	 * @return
	 */
	public String formatGUIDateTime(Date date, Locale locale) {
		if (date != null) {
			DateFormatsBean dateFormatsBean = getDateFormatsByLocale(locale);
			return dateFormatsBean.getGuiDateTimeFormat().format(date);
		} else {
			return " ";
		}
	}

	/**
	 * Parses a GUI date format string to a Date object
	 * @param guiDateTime
	 * @param locale
	 * @return
	 */
	public Date parseGUIDateTime(String guiDateTime, Locale locale) {
		Date date = null;
		try {
			DateFormatsBean dateFormatsBean = getDateFormatsByLocale(locale);
			date = dateFormatsBean.getGuiDateTimeFormat().parse(guiDateTime);
		} catch (Exception e) {
		}
		return date;
	}

	/**
	 * Parses a GUI date format string to a Date object
	 * @param guiDateTime
	 * @param locale
	 * @return
	 */
	public Date parseGUIDateTimeNoSeconds(String guiDateTime, Locale locale) {
		Date date = null;
		try {
			DateFormatsBean dateFormatsBean = getDateFormatsByLocale(locale);
			date = dateFormatsBean.getGuiDateTimeNoSecondsFormat().parse(guiDateTime);
		} catch (Exception e) {
		}
		return date;
	}

	public String getGUIDateTimePattern(Locale locale) {
		DateFormatsBean dateFormatsBean = getDateFormatsByLocale(locale);
		return ((SimpleDateFormat)dateFormatsBean.getGuiDateTimeFormat()).toPattern();
	}

	/**
	 * Formats a Date object by the ISO date format
	 * @param date
	 * @return
	 */
	public String formatISODate(Date date) {
		if (date != null) {
			return isoDateFormat.format(date);
		} else {
			return "null";
		}
	}

	/**
	 * Parses an ISO date format string to a Date object
	 * @param isoDateString
	 * @return
	 */
	public Date parseISODate(String isoDateString) {
		Date date = null;
		try {
			date = isoDateFormat.parse(isoDateString);
		} catch (Exception e) {
		}
		return date;
	}
	/**
	 * Parses an ISO date format string to a Date object
	 * @param isoDateString
	 * @return
	 */
	public Date parseISODateEx(String isoDateString) throws ParseException {
		return isoDateFormat.parse(isoDateString);
	}


	/**
	 * Formats a Date object by the ISO date-time format
	 * @param date
	 * @return
	 */
	public String formatISODateTime(Date date) {
		if (date != null) {
		   return isoDateTimeFormat.format(date);
		} else {
		   return null;
		}
	}

	/**
	 * Parses an ISO date-time format string to a Date object
	 * @param isoDateTimeString
	 * @return
	 */
	public Date parseISODateTime(String isoDateTimeString) {
		if (isoDateTimeString != null) {
			try {
				return isoDateTimeFormat.parse(isoDateTimeString);
			} catch (ParseException e) {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * Gets a list of LabelValueBeans with the localized days
	 * @param locale the locale for the week days
	 * @return a list of LabelValueBeans with the localized days
	 */
	public static List<LabelValueBean> getDaysOfTheWeek(Locale locale) {
		return getDaysOfTheWeek(locale,true);
	}
	public static List<LabelValueBean> getDaysOfTheWeek(Locale locale,boolean includeEmpty) {
		List<LabelValueBean> dayLabelValueBeans = new ArrayList<LabelValueBean>();
		if(includeEmpty){
			dayLabelValueBeans.add(new LabelValueBean("-", ""));
		}
		LabelValueBean dayBean;
		String dayName;
		DateFormat dayFormat = new SimpleDateFormat("EEEE", locale);
		Calendar calendar = new GregorianCalendar();
		for (int i=1;i<=7;i++) {
			calendar.set(Calendar.DAY_OF_WEEK, i);
			dayName = dayFormat.format(calendar.getTime());
			dayBean = new LabelValueBean(dayName, String.valueOf(i));
			dayLabelValueBeans.add(dayBean);
		}
		return dayLabelValueBeans;
	}

	public static TimeZone getServerTimeZone() {
		return TimeZone.getDefault();
	}

	public static List<LabelValueBean> getTimeZones(Locale locale) {
		List<LabelValueBean> timeZones = new ArrayList<LabelValueBean>();
		String[] zones = TimeZone.getAvailableIDs();
		TimeZone timeZone;
		LabelValueBean tz;
		for (int i=0; i < zones.length; ++i){
			timeZone = TimeZone.getTimeZone(zones[i]);
			tz = new LabelValueBean(timeZone.getDisplayName(true, TimeZone.LONG, locale) + " " + zones[i],zones[i]);
			timeZones.add(tz);
		}
		return timeZones;
	}

/**
	 * Converts the Java date or date/time formats to corresponding ExtJS or PHP formats.
	 * The year is always written as yyyy. There may be some wrong conversions for some
	 * unusual formats like some Asian calendars.
	 *
	 * @param dbean - A bean containing the formats
	 * @param locale - The locale
	 * @param tzone - The time zone
	 * @return the augmented format bean
	 */
	private DateFormatsBean convertJavaToExtJSFormats(DateFormatsBean dbean, Locale locale, TimeZone tzone) {
		Calendar calendar = Calendar.getInstance(tzone, locale);
		DateFormat guiDateTimeFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG, locale);
		guiDateTimeFormat.setCalendar(calendar);
		DateFormat guiDateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);
		guiDateFormat.setCalendar(calendar);
		DateFormat guiTimeFormat = DateFormat.getTimeInstance(DateFormat.LONG, locale);
		guiTimeFormat.setCalendar(calendar);

		/**
		 * date time
		 */
		String s=((SimpleDateFormat)guiDateTimeFormat).toPattern();
		s = javaToExtJsYears(s);
		s = javaToExtJsMinutes(s);
		s = javaToExtJsMonths(s);
		s = javaToExtJsDays(s);
		s = javaToExtJsHours(s);
		s= javaToExtJsSeconds(s);
		//remove java  	Am/pm marker
		s = s.replaceAll("a", "");
		//remove java time zone
		s = s.replaceAll("z", "");
		s = s.replaceAll(" {2,}", " ");
		dbean.setExtJsDateTimeFormat(s.trim());

		/**
		 * date
		 */
		s =((SimpleDateFormat)guiDateFormat).toPattern();
		s = javaToExtJsYears(s);
		s = javaToExtJsMonths(s);
		s = javaToExtJsDays(s);
		//remove java time zone
		s = s.replaceAll("z", "");
		s = s.replaceAll(" {2,}", " ");
		dbean.setExtJsDateFormat(s.trim());

		/**
		 * time
		 */
		s =((SimpleDateFormat)guiTimeFormat).toPattern();
		s = javaToExtJsMinutes(s);
		s = javaToExtJsHours(s);
		s= javaToExtJsSeconds(s);
		//remove java  	Am/pm marker
		s = s.replaceAll("a", "");
		//remove java time zone
		s = s.replaceAll("z", "");
		s = s.replaceAll(" {2,}", " ");
		dbean.setExtJsTimeFormat(s.trim());

		//remove seconds
		s = s.replaceAll(":s", "");
		dbean.setExtJsTimeFormatNoSeconds(s.trim());

		/**
		 * submit date
		 */
		s=((SimpleDateFormat)guiDateFormat).toPattern();
		s = s.replaceAll("hh", "H");
		s = s.replaceAll("HH", "H");
		s = s.replaceAll("h(.)", "G"+"$1");
		s = s.replaceAll("mm", "i");
		s = s.replaceAll("m", "i");
		s = s.replaceAll("MM", "m");
		s = s.replaceAll("M", "n");
		s = s.replaceAll("dd", "XX");
		s = s.replaceAll("d", "j");
		s = s.replaceAll("XX", "d");
		s = s.replaceAll("y{1,4}", "y"); //for two digit year to be interpretable by struts (DateFormat.SHORT)
		s = s.replaceAll("ss", "s");
		s = s.replaceAll("a", "");
		s = s.replaceAll("z", "");
		s = s.replaceAll(" {2,}", " ");
		dbean.setExtJsSubmitDateFormat(s);


		return dbean;
	}

	/**
	 * Transform the java years representation to ext js year representation
	 * java: y or (yy or yyyy)
 	 * ext js: Y A full numeric representation of a year, 4 digits
	 * @param s
	 * @return
	 */
	private static String javaToExtJsYears(String s) {
		return s.replaceAll("y{1,4}", "Y");
	}

	/**
	 * Transform the java months representation to ext js months representation
	 * java: M Month in year
	 * ext js: m Numeric representation of a month, with leading zeros (n Numeric representation of a month, without leading zeros)
	 * @param s
	 * @return
	 */
	private static String javaToExtJsMonths(String s) {
		s = s.replaceAll("MM", "m");
		//s = s.replaceAll("M", "n");
		s = s.replaceAll("M", "m");
		return s;
	}

	/**
	 * Transform the java days representation to ext js days representation
	 * java: d Day in month
	 * ext js: d Day of the month, 2 digits with leading zeros (j Day of the month without leading zeros)
	 * @param s
	 * @return
	 */
	private static String javaToExtJsDays(String s) {
		s = s.replaceAll("dd", "d");
		//s = s.replaceAll("d", "j");
		s = s.replaceAll("d", "d");
		return s;
	}

	/**
	 * Transform the java hours representation to ext js hours representation
	 * java: H Hour in day (0-23) (h 	Hour in am/pm (1-12)
	 * ext js: g 12-hour format of an hour without leading zeros
	 * 			G 24-hour format of an hour without leading zeros
	 *			h 12-hour format of an hour with leading zeros
	 *			H 24-hour format of an hour with leading zeros
	 * @param s
	 * @return
	 */
	private static String javaToExtJsHours(String s) {
		s = s.replaceAll("hh", "H");//
		s = s.replaceAll("HH", "H");
		//s = s.replaceAll("h(.)", "G"+"$1");
		s = s.replaceAll("h", "H");
		return s;
	}

	/**
	 * Transform the java minutes representation to ext js minutes representation
	 * java: m 	Minute in hour
		 * ext js: i Minutes, with leading zeros
	 * @param s
	 * @return
	 */
	private static String javaToExtJsMinutes(String s) {
		s = s.replaceAll("mm", "i");
		s = s.replaceAll("m", "i");
		return s;
	}

	/**
	 * Transform the java seconds representation to ext js seconds representation
	 * java s:	Second in minute
	 * ext js: s Seconds, with leading zeros
	 * @param s
	 * @return
	 */
	private static String javaToExtJsSeconds(String s) {
		s = s.replaceAll("ss", "s");
		return s;
	}

	public static  Calendar getCleanCalendar(){
		Calendar calendar=Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MILLISECOND,0);
		return calendar;
	}
	public static Date clearDate(Date date){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MILLISECOND,0);
		return calendar.getTime();
	}
	public static Date clearDateSeconds(Date date){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MILLISECOND,0);
		return calendar.getTime();
	}
	public static Date getToday(){
		return getCleanCalendar().getTime();
	}
	public static Date getTomorrow(){
		Calendar cal=getCleanCalendar();
		cal.add(Calendar.DATE, 1);
		return cal.getTime();
	}
	public static Date getStartOfNextWeek(){
		Calendar calendar=getCleanCalendar();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		calendar.add(Calendar.DATE,7);
		Date d=calendar.getTime();
		return d;
	}
	public static Date getEndOfNextWeek(){
		Calendar calendar=getCleanCalendar();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		calendar.add(Calendar.DATE,13);
		Date d=calendar.getTime();
		return d;
	}
	public static Date getStartOfNextMonth(){
		Calendar calendar=getCleanCalendar();
		calendar.add(Calendar.MONTH,1);
		calendar.set(Calendar.DAY_OF_MONTH,1);
		Date d=calendar.getTime();
		return d;
	}
	public static Date getEndOfNextMonth(){
		Calendar calendar=getCleanCalendar();
		calendar.add(Calendar.MONTH,2);
		calendar.set(Calendar.DAY_OF_MONTH,1);
		calendar.add(Calendar.DATE,-1);
		Date d=calendar.getTime();
		return d;
	}

	/**
	 * Returns number of free days from given interval, start date included always, endDate only if includeLastDay == true!
	 * @param startDateParam
	 * @param endDateParam
	 * @param includeLastDay
	 * @return
	 */
	public static Integer getNumberOfDaysBetweenDates(Date startDateParam, Date endDateParam) {
		DateTime dateTime1 = new DateTime(getZeroTimeDate(startDateParam));
		DateTime dateTime2 = new DateTime(getZeroTimeDate(endDateParam));
		int numberOfDays = Days.daysBetween(dateTime1, dateTime2).getDays();
		return numberOfDays;
	}

	public static Date getZeroTimeDate(Date dateParam) {
	    Date res = dateParam;
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime( dateParam );
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	    res = calendar.getTime();
	    return res;
	}
	
	/**
	 * Add numberOfDays to a date 
	 * @param date
	 * @param numberOfDays
	 * @param jumpOverWeekend
	 * @return
	 */
	public static Date moveByDays(Date date, int numberOfDays, boolean add, boolean onlyWorkdays) {
		DateTime dateTime = new DateTime(getZeroTimeDate(date));
		if (onlyWorkdays) {
			int i=0;
		    while(i<numberOfDays) {
		    	if (add) {
		    		dateTime = dateTime.plusDays(1);
		    	} else {
		    		dateTime = dateTime.minusDays(1);
		    	}
		        if(dateTime.getDayOfWeek()<=5) {
		            i++;
		        }
		    }
		} else {
			if (add) {
				dateTime = dateTime.plusDays(numberOfDays);
			} else {
	    		dateTime = dateTime.minusDays(numberOfDays);
	    	}
		}
		return dateTime.toDate();
	}
	
	
	/**
	 * Returns number of free days from given interval, start date included always, endDate only if includeLastDay == true!
	 * @param startDateParam
	 * @param endDateParam
	 * @param includeLastDay
	 * @return
	 */
	public static int getDurationBetweenDates(Date startDateParam, Date endDateParam, boolean onlyWorkdays) {
		DateTime dateTime1 = new DateTime(getZeroTimeDate(startDateParam));
		DateTime dateTime2 = new DateTime(getZeroTimeDate(endDateParam));
		int i=0;
		if (onlyWorkdays) {
			//boolean isFirst = true;
			while (dateTime1.isBefore(dateTime2)) {
				dateTime1 = dateTime1.plusDays(1);
				int dayOfWeek = dateTime1.getDayOfWeek();
				if(dayOfWeek<=5) {
					//weekday
		            i++;
		        } else {
		        	//end date explicitly set on Saturday or Sunday: take this week end day(s) as working day
		        	if (!dateTime1.isBefore(dateTime2)) {
		        		if (dayOfWeek==6) {
		        			//add one day for task ending on Saturday
		        			i++;
		        		} else {
		        			//add two days for task ending on Sunday 
		        			i = i+2;
		        		}
		        	}
		        	/*if (isFirst) {
						dayOfWeek = dateTime1.getDayOfWeek();
						//start on Saturday or Sunday
						if (dayOfWeek==1 || dayOfWeek==6) {
							i++;
							dateTime1 = dateTime1.plusDays(1);
							if (dateTime1.isBefore(dateTime2)) {
								i++;
							}
						} else {
							//start on Sunday
							i++;
						}
					}*/
		        }
				//isFirst = false;
			}
			return i;
		} else {
			return Days.daysBetween(dateTime1, dateTime2).getDays() + 1;
		}
	}
	

	public static DateTime getZeroTimeDateTime(DateTime dateParam) {
	    Calendar calendar = dateParam.toCalendar(Locale.getDefault());
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	    return new DateTime(calendar.getTime());
	}



	/**
	 * This method compare only  the date values: 2015-07-17 (Discards time values).
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int compareTwoDatesWithoutTimeValue(Date d1, Date d2) {
		if ((d1==null) && (d2==null)) {
			return 0;
		}
		if (d1==null) {
			return -1;
		}
		if (d2==null) {
			return 1;
		}
		return getZeroTimeDate(d1).compareTo(getZeroTimeDate(d2));
	}

	/**
	 * This method return TRUE: first parameter >= second parameter otherwise false (discards time value)
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean greaterOrEqual(Date d1, Date d2) {
		if(getZeroTimeDate(d1).equals(getZeroTimeDate(d2)) || getZeroTimeDate(d1).after(getZeroTimeDate(d2))) {
			return true;
		}
		return false;
	}

	/**
	 * This method return TRUE: first parameter > second parameter otherwise false (discards time value)
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean greater(Date d1, Date d2) {
		if(getZeroTimeDate(d1).after(getZeroTimeDate(d2))) {
			return true;
		}
		return false;
	}

	/**
	 * This method return TRUE: first parameter <= second parameter otherwise false (discards time value)
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean lessOrEqual(Date d1, Date d2) {
		if(getZeroTimeDate(d1).equals(getZeroTimeDate(d2)) ||
			getZeroTimeDate(d1).before(getZeroTimeDate(d2))) {
				return true;
		}
		return false;
	}

	/**
	 * This method return TRUE: first parameter < second parameter otherwise false (discards time value)
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean less(Date d1, Date d2) {
		if(getZeroTimeDate(d1).before(getZeroTimeDate(d2))) {
				return true;
		}
		return false;
	}
}
