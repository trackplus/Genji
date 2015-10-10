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

package com.aurel.track.item;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.callbackInterfaces.IRemoteFiltering;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.json.JSONUtility;
import com.opensymphony.xwork2.Preparable;

/**
 * Used for remote filtering of combo data 
 */
public class RemoteFilteringAction implements Preparable, SessionAware, ServletResponseAware {
	private static final Logger LOGGER = LogManager.getLogger(RemoteFilteringAction.class);

	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private Integer actionID;
	//private Integer personID;
	//private Locale locale;
	private Integer fieldID;
	private String query;
	
	public void prepare() throws Exception {
		/*locale = (Locale) session.get(Constants.LOCALE_KEY);
		TPersonBean tPerson = (TPersonBean) session.get(Constants.USER_KEY);
		if (tPerson!=null) {
			personID = tPerson.getObjectID();
		}*/
	}
	public String execute(){
		
		boolean isNew = ItemBL.itemIsNew(actionID);
		LOGGER.debug("execute(): isNew="+isNew+" actionID="+actionID);
		IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
		IRemoteFiltering remoteFiltering = (IRemoteFiltering)fieldTypeRT;
		JSONUtility.encodeJSON(servletResponse, remoteFiltering.getFilteredDataSourceJSON(fieldID, query));
		return null;
	}
	

	public void setSession(Map<String, Object> session) {
		this.session=session;
	}
	public void setFieldID(Integer fieldID) {
		this.fieldID = fieldID;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
}

