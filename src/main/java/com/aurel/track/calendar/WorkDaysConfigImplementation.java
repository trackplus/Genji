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

package com.aurel.track.calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class WorkDaysConfigImplementation implements WorkDaysConfig{
	
	public static void add(String name, Date dateParam) {
		if(!WorkDaysConfig.exceptionFromWorkDays.containsKey(dateParam)) {
			WorkDaysConfig.exceptionFromWorkDays.put(dateParam, name);
		}
	}
	
	/**
	 * Returns true if given parameter is Saturday
	 * @param dateParam
	 * @return
	 */
	public static boolean isSaturday(Date dateParam) {
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(dateParam);
        if ( calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ) {
        	return true;
        }
		return false;
	}
	
	/**
	 * Returns true if given parameter is Sunday
	 * @param dateParam
	 * @return
	 */
	public static boolean isSunday(Date date) {
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    if ( calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ) {
	    	return true;
        }
		return false;
	}
	
	/**
	 * Returns true if given date parameter is NOT working day
	 * @param dateParam
	 * @return
	 */
	public static boolean isFreeDay(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDateParam = dateFormat.format(date);
		for (Map.Entry<Date, String> entry : WorkDaysConfig.exceptionFromWorkDays.entrySet()) {
			if(dateFormat.format(entry.getKey().getTime()).equals(formattedDateParam)) {
				return true;
			}else {
				return false;
			}
		}
		return false;
	}
	
	public static boolean isNonWorkingDay(Date date, Integer personID) {
		/*if(isSaturday(date) || isSunday(date) || isFreeDay(date)) {
			return true;
		}*/
		return false;
	}

}
