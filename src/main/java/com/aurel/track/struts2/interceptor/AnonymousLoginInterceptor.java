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

package com.aurel.track.struts2.interceptor;

import com.aurel.track.Constants;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TPersonBean;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.Map;

public class AnonymousLoginInterceptor implements Interceptor {
	private static final Logger LOGGER = LogManager.getLogger(AnonymousLoginInterceptor.class);
	public static final String USER = "user";
	public void destroy() {
	}

	public void init() {
	}

	public String intercept(ActionInvocation actionInvocation) throws Exception {
		LOGGER.debug("AnonymousLoginInterceptor");
		Map<String, Object> session = actionInvocation.getInvocationContext().getSession();
		TPersonBean personBean = (TPersonBean) session.get(Constants.USER_KEY);
		if(personBean==null){
			TPersonBean anonymous = PersonBL.loadByLoginName(TPersonBean.GUEST_USER);
			if (anonymous==null) {
				LOGGER.warn("Can't find 'guest' user: anonymous logon disabled");
				return "logon";
			}
		}
		return actionInvocation.invoke();
	}
}
