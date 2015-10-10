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

import javax.servlet.http.HttpServletRequest;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.prop.LoginBL;

public class BypassLoginHelper {
	
	public static boolean loginAsGuest(HttpServletRequest request, Map<String, Object> sessionMap){
		//actually can be any password for guest because anonymousLogin is true
		Map result = LoginBL.setEnvironment(TPersonBean.GUEST_USER, "trackplus", "nonce", request, sessionMap, true, false, false);
		int mappingEnum = (Integer) (result.get("mappingEnum"));
		if (mappingEnum == 9||//success
		 	mappingEnum==18||//request license
		 	mappingEnum==6||//license problems
			mappingEnum == 7//forwardUrl
		 	){
			return true;
		}
		else {
			return false;
		}
	}
}
