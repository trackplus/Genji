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

import java.util.Map;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class AuthenticationInterceptor
	implements Interceptor {

	private static final long serialVersionUID = 340L;

		public void destroy() {
		}

		public void init() {
		}

		public String intercept(ActionInvocation actionInvocation) throws Exception {
			Map<String, Object> session = actionInvocation.getInvocationContext().getSession();
			TPersonBean personBean = (TPersonBean) session.get(Constants.USER_KEY);
			if (personBean == null) {
				return "logon";
			} else {
				return actionInvocation.invoke();
			}
		} 
}
