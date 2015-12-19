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

package com.trackplus.track.constants;

/**
 * This interface contains constants for time spent
 * This constants are in milliseconds
 * @author  Adrian Bojani
 */
public interface CriticalTimeSpent {
	/**
	 * Default critical time spent :5000 milliseconds ( 5 seconds);
	 */
	public final int DEFAULT=5000;

	/**
	 * critical time in milliseconds for save item
	 */
	public final int ITEM_SAVE=DEFAULT;

	/**
	 * critical time in milliseconds for execute query 100000 ms(10 s)
	 */
	public final int ITEM_NAVIGATOR_EXECUTE_QUERY=10000;

}

