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

package com.aurel.track.linking;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class LinkingAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware {
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private TPersonBean personBean;
	private Locale locale;
	private Integer columnsQueryID;
	private Integer rowsQueryID;
	private String linkTypeWithDirection;

	private boolean hasInitData=true;
	private String initData;
	private String layoutCls="com.trackplus.layout.LinkingLayout";
	private String pageTitle="menu.linking";

	

	public void prepare() throws Exception {
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		locale=(Locale) session.get(Constants.LOCALE_KEY);
	}

	/**
	 * Render the link matrix page
	 */
	@Override
	public String execute(){
		initData= LinkMatrixBL.loadConfig(personBean, locale);
		return SUCCESS;
	}
	
	/**
	 * Load the link matrix
	 * @return
	 */
	public String search(){
		JSONUtility.encodeJSON(servletResponse, LinkMatrixBL.loadLinks(columnsQueryID, rowsQueryID, linkTypeWithDirection, personBean, locale));
		return null;
	}

	public void setSession(Map<String, Object> session) {
		this.session=session;
	}
	
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public boolean isHasInitData() {
		return hasInitData;
	}

	public void setHasInitData(boolean hasInitData) {
		this.hasInitData = hasInitData;
	}

	public String getInitData() {
		return initData;
	}

	
	public void setLinkTypeWithDirection(String linkTypeWithDirection) {
		this.linkTypeWithDirection = linkTypeWithDirection;
	}

	public String getLayoutCls() {
		return layoutCls;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	/*public Integer getRowsQueryID() {
		return rowsQueryID;
	}*/

	public void setRowsQueryID(Integer rowsQueryID) {
		this.rowsQueryID = rowsQueryID;
	}

	/*public Integer getColumnsQueryID() {
		return columnsQueryID;
	}*/

	public void setColumnsQueryID(Integer columnsQueryID) {
		this.columnsQueryID = columnsQueryID;
	}
}
