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

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class ProjectOrSystemAdminAuthenticationInterceptor
	implements Interceptor {

	private static final long serialVersionUID = 340L;
	
	private static final Logger LOGGER = LogManager.getLogger(ProjectOrSystemAdminAuthenticationInterceptor.class);

		public void destroy() {
		}

		public void init() {
		}

		public String intercept(ActionInvocation actionInvocation) throws Exception {
			Map session = actionInvocation.getInvocationContext().getSession();
			HttpServletResponse response=ServletActionContext.getResponse();
			HttpServletRequest request = ServletActionContext.getRequest();
			TPersonBean user = (TPersonBean) session.get(Constants.USER_KEY);
			if (user == null) {
				boolean fromAjax=false;
				try {
					fromAjax="true".equalsIgnoreCase(request.getParameter("fromAjax"));
				}catch (Exception ex){
					LOGGER.error(ExceptionUtils.getStackTrace(ex));
				}
				if(fromAjax){
					Locale locale = (Locale) session.get(Constants.LOCALE_KEY);
					if(locale == null) {
						locale = Locale.getDefault();
					}
					JSONUtility.encodeJSONFailure(response, 
							LocalizeUtil.getLocalizedTextFromApplicationResources("common.noLoggedUser", locale),
							JSONUtility.ERROR_CODE_NO_USER_LOGIN);
					return null;
				}
				AuthenticationBL.storeUrlOnSession(ServletActionContext.getRequest(),session);
				return "logon";//TODO rename to Action.LOGIN;
			} else {
				if (user.isSys() || user.isProjAdmin() ) {
					return actionInvocation.invoke();
				} else {
					return "cockpit";
				}
				
			}
		} 
}
