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

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TPersonBean.USERLEVEL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.mobile.MobileBL;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.BypassLoginHelper;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.SessionMap;

public class AuthenticationItemNavigator 
	implements Interceptor {
	
	private static final long serialVersionUID = 340L;
	private static final Logger LOGGER = LogManager.getLogger(AuthenticationItemNavigator.class);

	@Override
	public void destroy() {
	}

	@Override
	public void init() {
	}
	
	/**
	 * Standard authentication procedure including extra checking: If the user 
	 * is CLIENT user and not from Teamgeist then item navigator is disabled. 
	 *	Client user will be redirected to cockpits. 
	 *	In case of a CLIENT user from Teamgeist the procedure is allowed
	 * 
	 */
	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		//get the actual request
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response=ServletActionContext.getResponse();
		//force the creation of a new session if it does not exist yet
		request.getSession();
		Map<String, Object> session = actionInvocation.getInvocationContext().getSession();
		//if session is new set the newly created session map into the Action context
		//(instead of null). Otherwise the SessionAware interface sets the wrong session object
		//which does not contain the session attributes (user, etc.)
		if (session == null || session.isEmpty()) {
			session = new SessionMap(ServletActionContext.getRequest());
			ActionContext.getContext().setSession(session);
		}
		TPersonBean personBean = (TPersonBean) session.get(Constants.USER_KEY);
		if (personBean == null&& ApplicationBean.getInstance().getSiteBean()!=null) {
			if (!BypassLoginHelper.loginAsGuest(request, session)) {
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
				AuthenticationBL.storeUrlOnSession(request,session);
				return "logon";//TODO rename to Action.LOGIN;
			}
		}else {
			if(personBean.getUserLevel().intValue() == USERLEVEL.CLIENT.intValue() && 
					!MobileBL.isMobileApp(session)) {
				LOGGER.debug("Client user tries to access item navigator. Will be redirected to cockpits!");
				return "cockpit";
			}
		}
		return actionInvocation.invoke();
	} 


}
