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

package com.aurel.track.struts2.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.prop.LoginBL;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class RestLoginInterceptor implements Interceptor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 503L;
	private static final Logger LOGGER = LogManager.getLogger(RestLoginInterceptor.class);
	public static final String USER = "user";
	@Override
	public void destroy() {
	}

	@Override
	public void init() {
	}

	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		LOGGER.debug("RestLoginInterceptor");
		Map<String, Object> session = actionInvocation.getInvocationContext().getSession();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String username = request.getParameter("username");
		String token = request.getParameter("token");
		String password = request.getParameter("password");
		if ( username != null ) {
			TPersonBean user = null;
			if ( token != null ) {
				user = LoginBL.userIdentifiedByToken(username, token);
			} else if ( password !=  null) {
				Map envResult = LoginBL.setEnvironment(username, password, null, request, ActionContext.getContext().getSession(),
						false, false, false);
				user = (TPersonBean) envResult.get("user");
			}
			if (user != null) {
				session.put(Constants.USER_KEY, user);
				return actionInvocation.invoke();
			} else {
				JSONUtility.encodeJSONFailure(response, 
						"Please login first",
						JSONUtility.ERROR_CODE_NO_USER_LOGIN);
				return null;
			}
		}
		
		return actionInvocation.invoke();
		
	}
}
