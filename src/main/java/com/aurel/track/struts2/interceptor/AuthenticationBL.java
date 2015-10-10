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

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.Constants;

/**
 *
 */
public class AuthenticationBL {
	protected static Logger LOGGER = LogManager.getLogger(AuthenticationBL.class);
	public static void  storeUrlOnSession(HttpServletRequest request,Map session){
		String uri=request.getRequestURI();
		LOGGER.debug("uri="+uri);
		String forwardURL=uri;
		Enumeration<String> parameterNames = request.getParameterNames();
		StringBuffer paramStr=new StringBuffer();
		if(parameterNames!=null&&parameterNames.hasMoreElements()){
			String param;
			String value;
			while (parameterNames.hasMoreElements()) {
				param =parameterNames.nextElement();
				value=request.getParameter(param);
				if(value!=null&&value.length()>0){
					if(paramStr.length()>0){
						paramStr.append("&");
					}
					paramStr.append(param).append("=").append(value);
				}
			}
		}
		if(paramStr.length()>0){
			forwardURL=forwardURL+"?"+paramStr;
		}
		LOGGER.debug("forwardURL="+forwardURL);
		session.put(Constants.POSTLOGINFORWARD,forwardURL);
	}
}
