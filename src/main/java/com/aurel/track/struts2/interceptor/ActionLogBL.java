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

import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.aurel.track.Constants;
import com.aurel.track.GeneralSettings;
import com.aurel.track.beans.TPersonBean;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;

public class ActionLogBL {
	
	static String logActionTime(ActionInvocation actionInvocation, Logger logger) throws Exception {
		long startTimeStamp = System.currentTimeMillis();
		String invocationResult = actionInvocation.invoke();
		long endTimeStamp = System.currentTimeMillis();
		if (endTimeStamp-startTimeStamp>GeneralSettings.getActionLogTimeout()) {
			ActionContext actionContext = actionInvocation.getInvocationContext();
			Map<String, Object> session = actionContext.getSession();
			TPersonBean personBean = (TPersonBean) session.get(Constants.USER_KEY);
			HttpServletRequest request = ServletActionContext.getRequest();
			logger.debug("The action " + request.getRequestURI() + " executed by " + personBean.getLoginName() + " took " + (endTimeStamp-startTimeStamp)/1000 + " seconds" );
			Map<String, String[]> parametersMap = request.getParameterMap();
			if (parametersMap!=null) {
				for (Map.Entry<String, String[]> paramEntry : parametersMap.entrySet()) {
					String paramName = paramEntry.getKey();
					String[] paramValue = paramEntry.getValue();
					if (paramName!=null && !"fromAjax".equals(paramName) && paramValue!=null && paramValue.length>0) {
						logger.debug("Param: " + paramName + " value: " +paramValue[0]);
					}
				}
			}
		}
		return invocationResult;
	}
}
