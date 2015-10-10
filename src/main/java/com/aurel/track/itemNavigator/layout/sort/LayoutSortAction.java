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


package com.aurel.track.itemNavigator.layout.sort;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * Sorts the reportBeans
 * @author Tamas Ruff
 *
 */
public final class LayoutSortAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware {

	private static final long serialVersionUID = 2633936687094616772L;

	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private TPersonBean personBean;
	private Integer sortField = null;
	//false = ascending, true = descending 
	private boolean sortOrder = false;
	private Integer filterID;
	private Integer filterType;
	
	/**
	 * Prepares the fields
	 */
	public void prepare() throws Exception {
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
	}
	
	/**
	 * Saves the sort settings into layout
	 */
	public String execute() {
		if (sortField != null) {
			LayoutSortBL.saveSorting(personBean, filterType, filterID, sortField, sortOrder);
		}
		JSONUtility.encodeJSONSuccess(servletResponse);
		return null;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	/**
	 * @param sortField the sortField to set
	 */
	public void setSortField(Integer sortField) {
		this.sortField = sortField;
	}

	public void setSortOrder(boolean sortOrder) {
		this.sortOrder = sortOrder;
	}


	public void setFilterID(Integer filterID) {
		this.filterID = filterID;
	}


	public void setFilterType(Integer filterType) {
		this.filterType = filterType;
	}
	
	
}
