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

package com.aurel.track.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.FilterUpperConfigUtil;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadTreeFilterItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.report.dashboard.ReleaseNoteWrapper;
import com.aurel.track.report.datasource.releaseNotes.ReleaseNotesDatasource;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.user.ActionLogger;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class GetCustomerInfoAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware, ServletRequestAware{
	private static final long serialVersionUID = 1543744965855309661L;
	private static final Logger LOGGER = LogManager.getLogger(GetCustomerInfoAction.class);

	private Map<String,Object> session;
	private HttpServletResponse servletResponse;
	private HttpServletRequest servletRequest;
	private TPersonBean personBean;
	private Locale locale;
	private String email;
	private String itemNo;


	@Override
	public void prepare() throws Exception {
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		locale=(Locale) session.get(Constants.LOCALE_KEY);
		if (personBean == null) {
			personBean = PersonBL.loadByLoginName("guest");
		}
	}

	@Override
	public String execute() {
		TWorkItemBean itemBean = null;
		try {
			itemBean = ItemBL.loadWorkItemByProjectSpecificID(itemNo);
		} catch (ItemLoaderException e) {
			LOGGER.info("Attempt to load with invalid item no");
			return null;
		}
		String subm;
		if (itemBean.getSubmitterEmail() != null) {
			subm = itemBean.getSubmitterEmail();
		} else {
			return "";
		}
		
		if ((subm.equals(email.trim().toLowerCase()))
			  || (itemBean.getTPersonBeanRelatedByOriginatorID().getEmail().equals(email.trim().toLowerCase()))
			   ) {
			StringBuilder sb = new StringBuilder();
			sb.append("{");
			JSONUtility.appendBooleanValue(sb, "success", true);
			JSONUtility.appendStringValue(sb, "title", itemBean.getSynopsis());
			JSONUtility.appendStringValue(sb, "message", itemBean.getDescription(),true);
			sb.append("}");
			JSONUtility.encodeJSON(servletResponse, sb.toString());
		}

		return null;
	}



	public void setItemNo(String _itemNo) {
		this.itemNo = _itemNo;
	}
	
	public String getItemNo() {
		return this.itemNo;
	}
	
	public void setEmail(String _email) {
		this.email = _email;
	}
	
	public String getEmail() {
		return this.email;
	}


	@Override
	public void setSession(Map<String, Object> session) {
		this.session=session;
	}
	
	
	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
	
	@Override
	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
	}


}
