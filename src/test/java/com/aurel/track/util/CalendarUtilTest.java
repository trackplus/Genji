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

package com.aurel.track.util;

import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


public class CalendarUtilTest {

	java.util.Calendar cal; 

	@Before
	public void setUp() throws Exception {
		cal = Calendar.getInstance();
		
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 23);
		cal.set(Calendar.SECOND, 45);
		cal.set(Calendar.MILLISECOND, 1);
	}

	@Test
	public void testClearTime(){
		assertNotNull(cal);
		CalendarUtil.clearTime(cal);
		assertTrue("Hour:"+Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) == 0);
		assertTrue("Minute:"+Calendar.MINUTE, cal.get(Calendar.MINUTE) == 0);
		assertTrue("Second:"+Calendar.SECOND, cal.get(Calendar.SECOND) == 0);
		assertTrue("Millisecond:"+Calendar.MILLISECOND, cal.get(Calendar.MILLISECOND) == 0);
		
	}

	@Test
	public void testHasTimePart(){
		assertNotNull(cal);
		CalendarUtil.clearTime(cal);
		assertFalse(CalendarUtil.hasTimePart(cal));
		cal.set(Calendar.SECOND, 1);
		assertTrue(CalendarUtil.hasTimePart(cal));
	}
	
	

	@Test
	public void testSameDate() {
		Date date1 = cal.getTime();
		Date date2 = date1;

		assertTrue(CalendarUtil.sameDay(date1,date2));
	}
	

}
