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

import java.util.Date;
import java.util.HashMap;

public class LogThrottle {
	
	private static HashMap<String,Date> lastTimes = new HashMap<String,Date>();
	private static HashMap<String,Integer> intervals = new HashMap<String,Integer>();
	
	public static boolean isReady(String messageKey, int minutes) {
		Date now = new Date();
		Date last = lastTimes.get(messageKey);
		if (last == null) {
			lastTimes.put(messageKey, now);
			last = now;
			intervals.put(messageKey, minutes*1000*60);
			return true;
		}
		int interval = intervals.get(messageKey);
		if (now.getTime() > last.getTime() + interval) {
			lastTimes.put(messageKey, now);
			intervals.put(messageKey, minutes);
			return true;
		}
		return false;
	}
}
