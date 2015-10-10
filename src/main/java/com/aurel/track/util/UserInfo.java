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
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;



public final class UserInfo implements Serializable {
	
	private static final long serialVersionUID = 8210773744778514880L;
	String userFirstName = "";
	String userLastName = "";
	public String userDelimeter = " ";
	private static final Logger LOGGER = LogManager.getLogger(UserInfo.class);

	public void setRequest( HttpServletRequest request ) {
		HttpSession session = request.getSession();
		TPersonBean user = (TPersonBean) session.getAttribute(Constants.USER_KEY);
		userFirstName = "";
		userLastName = "";
		if( user != null )
		{
			userFirstName = user.getFirstName();
			userLastName = user.getLastName();
			LOGGER.debug("User name is : >" + userFirstName + userDelimeter + userLastName  + "<");
		}
	}

	public String getFirstName() {

		return userFirstName;
	}

	public String getLastName() {
		return userLastName;
	}

}
