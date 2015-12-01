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

package com.aurel.track.admin.server.motd;

import java.util.HashMap;
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
 *
 */
public class MotdAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware {
	private static final long serialVersionUID = 1L;	

	private Map<String,Object> session;
	private HttpServletResponse servletResponse;
	private Locale locale; 

	private String theLocale;
	private String theMessage;
	private String teaserText;

	@Override
	public void prepare() {
		locale = (Locale) session.get(Constants.LOCALE_KEY);
	}
	
	@Override
	public String execute() {
		if (theLocale==null || "".equals(theLocale)) {
			if (locale == null) {
				locale = Locale.getDefault();
			}
			theLocale = locale.getLanguage();
		}
		JSONUtility.encodeJSON(servletResponse, MotdBL.encodeMotd(MotdBL.loadMotd(theLocale)));		
		return null;
	}
	
	/**
	 * 	Save the login page text and teaser text for the given locale.
	 */
	public String save(){		
		Integer objectID=MotdBL.saveMotd(theLocale, theMessage, teaserText);
		if (objectID==null) {
			JSONUtility.encodeJSONFailure(servletResponse,"Error saving motd", 2);			
		} else {
			JSONUtility.encodeJSONSuccess(servletResponse);
		}
		return null;
	}
	

	public void setTheLocale(String theLocale) {
		this.theLocale = theLocale;
	}

	public void setTheMessage(String theMessage) {
		this.theMessage = theMessage;
	}
	
	public void setTeaserText(String teaserText) {
		this.teaserText = teaserText;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
}
