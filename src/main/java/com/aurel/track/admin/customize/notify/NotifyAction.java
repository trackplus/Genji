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


package com.aurel.track.admin.customize.notify;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.json.JSONUtility;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * A action for a generic tree configuration based on global,
 * issueType, projectType and project specific scopes: 
 * Currently used by field configuration and screen assignment  
 * 
 */
public class NotifyAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware  {
	private static final long serialVersionUID = 1L;		
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;	
	private Locale locale;		
	
	public void prepare() throws Exception {		
		locale=(Locale) session.get(Constants.LOCALE_KEY);
	}
		
	/**
	 * Expand a tree node from tree
	 * @return
	 */
	public String expand() {
		JSONUtility.encodeJSON(servletResponse,
				JSONUtility.getChildrenJSON(NotifyBL.getChildren(locale)));					
    	return null;		
	}	
		
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
	
}
