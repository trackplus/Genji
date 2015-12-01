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

import java.util.Calendar;
import java.util.Date;

public class CalendarUtil {

	/**
	 * Clear the time part from calendar
	 * @param calendar
	 * @return
	 */
	public static Calendar clearTime(Calendar calendar) {
		if (calendar!=null) {
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
		}
		return calendar;
	}

	/**
	 * Whether the calendar has time part
	 * @param calendar
	 * @return
	 */
	public static boolean hasTimePart(Calendar calendar) {
		return calendar.get(Calendar.HOUR_OF_DAY)>0 ||
				calendar.get(Calendar.MINUTE)>0 ||
				calendar.get(Calendar.SECOND)>0;
	}

	/**
	 * Whether the two date are on the same day
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static boolean sameDay(Date date1, Date date2) {
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar1.setTime(date1);
		calendar2.setTime(date2);
		return (calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)) &&
			(calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR));
	}
	
	/**
	 * Get today without time fields 
	 * @return
	 */
	public static Date getToday() {
		Calendar calendar = Calendar.getInstance();
		CalendarUtil.clearTime(calendar);
		return calendar.getTime();
	}
	
}
