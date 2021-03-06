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

package com.aurel.track.user;

import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;

/**
 * This class serves to log administrative actions like adding or deleting users, workspaces, configurations, etc.
 *
 */
public class ActionLogger {
	
	public static Logger LOGGER = LogManager.getLogger("Actions");
	
	public static void log(Map<String,Object> session, String action) {
		TPersonBean user = (TPersonBean) session.get(Constants.USER_KEY);
		if (user != null) {
			LOGGER.info( (user.getFirstName() +" " + user.getLastName()).trim() + ": " + action);
		}
	}

}
