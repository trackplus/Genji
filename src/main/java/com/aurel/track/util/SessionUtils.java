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

import java.util.Map;

import javax.servlet.http.HttpSession;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;


public class SessionUtils {

	
	/**
	 * Gets the currently logged on user
	 * @param session
	 * @return
	 */
	public static TPersonBean getCurrentUser(HttpSession session)
	{
		return (TPersonBean) session.getAttribute(Constants.USER_KEY);
	}
	
	public static void removeCurrentUser(HttpSession session)
	{
		session.removeAttribute(Constants.USER_KEY);
	}
	
	/**
	 * Gets the currently logged on user
	 * @param session
	 * @return
	 */
	public static TPersonBean getCurrentUser(Map session)
	{
		return (TPersonBean) session.get(Constants.USER_KEY);
	}
	
	public static void removeCurrentUser(Map session)
	{
		session.remove(Constants.USER_KEY);
	}
	
}
