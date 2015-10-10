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

package com.aurel.track.admin.customize.lists;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
/**
 * Action without authentication intercepter to be executable also from jasper reports 
 * @author Tamas Ruff
 *
 */
public class OptionIconStreamAction extends ActionSupport 
	implements ServletResponseAware, ServletRequestAware  {

	private static final long serialVersionUID = 1L;
	private HttpServletResponse servletResponse;
	private HttpServletRequest servletRequest;
	private Integer optionID;
	//executed from issue navigator: the listID and fieldID is equal for issueType, status, priority and severity
	//but for custom selects this is not the case: but in issue navigator the fieldID has no importance if it is not a system field
	//because all custom options can be identified through optionID (listID/fieldID is not important)
	private Integer fieldID;

	/**
	 * Renders the upload form
	 */
	@Override
	public String execute() {
		ListOptionIconBL.downloadForField(servletRequest, servletResponse, fieldID, optionID);
		return null;
	}
	
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
	}

	public Integer getOptionID() {
		return optionID;
	}

	public void setOptionID(Integer optionID) {
		this.optionID = optionID;
	}

	public void setFieldID(Integer fieldID) {
		this.fieldID = fieldID;
	}
	
}
