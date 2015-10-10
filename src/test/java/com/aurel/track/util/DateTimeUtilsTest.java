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

package com.aurel.track.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.ISOChronology;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class DateTimeUtilsTest {

	private DateTimeUtils dtu = null;
	private Locale locale = new Locale("en");
	private Date now = new Date(1399666666000L); // 2014-05-09 22:17:46
	private Date nowMidnight = new Date(1399586400000L);
	private DateTime nowdt = new DateTime(1399666666000L); // 2014-05-09 22:17:46
	private DateTime nowMidnightdt = new DateTime(1399586400000L);
	private long nowtime = 1399666666000L;
	private long difftime = 80266000L;
	private DateTimeZone dtz = DateTimeZone.getDefault();
	private Chronology chronology = ISOChronology.getInstance(dtz);
	
	@Before
	public void setUp() throws Exception {
		dtu = DateTimeUtils.getInstance();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetInstance() {
		DateTimeUtils dtup = DateTimeUtils.getInstance();
		assertNotNull(dtup);
	}

	@Test
	public void testGetDateFormatsByLocale() {
		DateFormatsBean dfb = dtu.getDateFormatsByLocale(locale);
		assertEquals("m/d/Y",dfb.getExtJsDateFormat());
		assertEquals("m/d/Y H:i:s", dfb.getExtJsDateTimeFormat());		
		assertEquals("n/j/y", dfb.getExtJsSubmitDateFormat());
	}

	@Test
	public void testGetGuiDateFormat() {
		assertEquals("5/9/14", dtu.getGuiDateFormat(locale).format(now));
	}

	@Test
	public void testGetExtJSDateFormat() {
		assertEquals(dtu.getExtJSDateFormat(locale),"m/d/Y");
	}

	@Test
	public void testGetExtJsTwoDigitsYearDateFormat() {
		assertEquals(dtu.getExtJsTwoDigitsYearDateFormat(locale), "n/j/y");
	}

	@Test
	public void testGetExtJSLongDateTimeFormat() {
		assertEquals(dtu.getExtJsLongDateTimeFormat(locale),"m/d/Y H:i:s");
	}

	@Test
	public void testFormatShortDate() {
		assertEquals("5/9/14", dtu.formatShortDate(now,locale));
	}

	@Test
	public void testParseShortDate() {
		assertTrue(dtu.parseShortDate("05/09/2014",locale).getTime() + difftime - nowtime == 0); 
	}

	@Test
	public void testFormatShortDateTime() {
		// System.err.println(dtu.formatGUIDateTime(now,locale));
		assertEquals("05/09/2014 22:17:46", dtu.formatGUIDateTime(now,locale));
	}

	@Test
	public void testParseShortDateTime() {
		DateFormat df = dtu.getDateFormatsByLocale(locale).getShortDateTimeFormat();	
		// System.err.println(df.format(now));
		assertTrue(dtu.parseShortDateTime("5/9/14 10:17:46 PM CEST",locale).getTime() - nowtime == 0);
	}

	@Test
	public void testIsValidGUIDate() {
		assertTrue(dtu.isValidGUIDate("5/9/14", locale));
		assertFalse(dtu.isValidGUIDate("14/14/XX", locale));		
	}

	@Test
	public void testFormatGUIDate() {
		assertEquals("05/09/2014", dtu.formatGUIDate(now,locale));
	}

	@Test
	public void testParseGUIDate() {
		String gdate = dtu.formatGUIDate(now, locale);
		assertTrue(dtu.parseGUIDate(gdate,locale).getTime() < now.getTime()); 
	}

	@Test
	public void testGetGUIDatePattern() {
		assertEquals("MM/dd/yyyy", dtu.getGUIDatePattern(locale));
	}

	@Test
	public void testFormatGUIDateTime() {
		assertEquals(dtu.formatGUIDateTime(now,locale).trim(),"05/09/2014 22:17:46");
	}

	@Test
	public void testParseGUIDateTime() {
		String gdate = dtu.formatGUIDateTime(now, locale);
		assertTrue(dtu.parseGUIDateTime(gdate,locale).getTime() <= now.getTime()); 
	}

	@Test
	public void testGetGUIDateTimePattern() {
		assertEquals("MM/dd/yyyy H:mm:ss", dtu.getGUIDateTimePattern(locale).trim());
	}

	@Test
	public void testFormatISODate() {
		assertEquals("2014-05-09", dtu.formatISODate(now));
	}

	@Test
	public void testParseISODate() {
		assertTrue(dtu.parseISODate("2014-05-09").getTime() + difftime == nowtime );
	}

	@Test
	public void testParseISODateEx() {
		assertTrue(dtu.parseISODate("2014-05-09").getTime() + difftime == nowtime );
		try {
			dtu.parseISODate("2014-05-XX");
		} catch (Exception e) {
			assertNotNull(e);
		}
	}

	@Test
	public void testFormatISODateTime() {
		assertEquals(dtu.formatISODateTime(now),"2014-05-09 22:17:46");
	}

	@Test
	public void testParseISODateTime() {
		// System.out.println(dtu.parseISODateTime("2014-05-09 22:17:46").getTime() - nowtime); 
		assertTrue(dtu.parseISODateTime("2014-05-09 22:17:46").getTime() == nowtime );
	}

	@Test
	public void testGetDaysOfTheWeek() {
		assertEquals(DateTimeUtils.getDaysOfTheWeek(locale).get(0).getLabel(),"-");
		assertEquals(DateTimeUtils.getDaysOfTheWeek(locale).get(0).getValue().trim(),"");
		assertEquals(DateTimeUtils.getDaysOfTheWeek(locale).get(1).getLabel(),"Sunday");
		assertEquals(DateTimeUtils.getDaysOfTheWeek(locale).get(1).getValue(),"1");
		assertEquals(DateTimeUtils.getDaysOfTheWeek(locale).get(2).getLabel(),"Monday");
		assertEquals(DateTimeUtils.getDaysOfTheWeek(locale).get(2).getValue(),"2");
		assertEquals(DateTimeUtils.getDaysOfTheWeek(locale).get(7).getLabel(),"Saturday");
		assertEquals(DateTimeUtils.getDaysOfTheWeek(locale).get(7).getValue(),"7");
	}

	@Test
	public void testGetServerTimeZone() {
		assertEquals(DateTimeUtils.getServerTimeZone().getID().trim(),"Europe/Berlin");
	}

	@Test
	public void testGetTimeZones() {
		assertEquals(DateTimeUtils.getTimeZones(locale).get(0).getValue(),"Etc/GMT+12");
	}

	@Test
	public void testGetCleanCalendar() {
		Calendar calendar=Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MILLISECOND,0);
		assertTrue(dtu.getCleanCalendar().getTime().getTime() - calendar.getTime().getTime() < 2);
	}

	@Test
	public void testClearDate() {
		assertTrue(dtu.clearDate(now).getTime() == nowMidnight.getTime());
	}

	@Test
	public void testClearDateSeconds() {
		assertTrue(dtu.clearDateSeconds(now).getTime() == dtu.parseISODateTime("2014-05-09 22:17:00").getTime());		
	}

	@Test
	public void testGetToday() {
		Calendar calendar=getCleanCalendar();
		Date d=calendar.getTime();
		assertTrue(dtu.getToday().getTime() == d.getTime());
	}

	@Test
	public void testGetTomorrow() {
		Calendar calendar=getCleanCalendar();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		calendar.add(Calendar.DATE,7);
		Date d=calendar.getTime();
		assertTrue(dtu.getTomorrow().getTime() - d.getTime() < 2);
	}

	@Test
	public void testGetStartOfNextWeek() {
		Calendar calendar=getCleanCalendar();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		calendar.add(Calendar.DATE,7);
		Date d=calendar.getTime();
		assertTrue(dtu.getStartOfNextWeek().getTime() - d.getTime() < 2);
	}

	@Test
	public void testGetEndOfNextWeek() {
		Calendar calendar=getCleanCalendar();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		calendar.add(Calendar.DATE,13);
		Date d=calendar.getTime();
		// System.err.println(dtu.getEndOfNextWeek().getTime() - d.getTime());
		assertTrue(dtu.getEndOfNextWeek().getTime() - d.getTime() < 2);
	}

	@Test
	public void testGetStartOfNextMonth() {
		Calendar calendar=getCleanCalendar();
		calendar.add(Calendar.MONTH,1);
		calendar.set(Calendar.DAY_OF_MONTH,1);
		Date d=calendar.getTime();
		assertTrue(dtu.getStartOfNextMonth().getTime() - d.getTime() < 2);
	}

	@Test
	public void testGetEndOfNextMonth() {
		Calendar calendar=getCleanCalendar();
		calendar.add(Calendar.MONTH,2);
		calendar.set(Calendar.DAY_OF_MONTH,1);
		calendar.add(Calendar.DATE,-1);
		Date d=calendar.getTime();
		assertTrue(dtu.getEndOfNextMonth().getTime() - d.getTime() < 2);
	}
	
	private static  Calendar getCleanCalendar(){
		Calendar calendar=Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MILLISECOND,0);
		return calendar;
	}

}
