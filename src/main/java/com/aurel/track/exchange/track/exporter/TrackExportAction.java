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


package com.aurel.track.exchange.track.exporter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadItemIDListItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.itemNavigator.ItemNavigatorBL;
import com.aurel.track.itemNavigator.QueryContext;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.util.DownloadUtil;
import com.aurel.track.util.GeneralUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class TrackExportAction extends ActionSupport implements Preparable, SessionAware {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(TrackExportAction.class);
	private Map<String, Object> session;
	private HttpServletResponse response;
	private Integer personID;
	private TPersonBean personBean;
	private Locale locale;
	private String workItemIDs;
	public void prepare() throws Exception {
		response = ServletActionContext.getResponse();
		personID = ((TPersonBean) session.get(Constants.USER_KEY)).getObjectID();
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		locale = (Locale) session.get(Constants.LOCALE_KEY);
	}


	@Override
	public String execute() {
		List<ReportBean> reportBeanList = null;
		List<Integer> selectedWorkItemIDs = null;
		if (workItemIDs!=null && !"".equals(workItemIDs)) {
			//selections in item navigator
			selectedWorkItemIDs = GeneralUtils.createIntegerListFromStringArr(workItemIDs.split(","));
			reportBeanList = LoadItemIDListItems.getReportBeansByWorkItemIDs(
					GeneralUtils.createIntArrFromIntegerList(selectedWorkItemIDs), false, personID, locale,
					true, true, true, true, true, true, false, false, false);
		} else {
			//no selections, get the result of the last executed query
			QueryContext queryContext=ItemNavigatorBL.loadLastQuery(personBean.getObjectID(),locale);
			if (queryContext!=null){
				try{
					reportBeanList = ItemNavigatorBL.executeQuery(personBean,locale,queryContext);
				}catch (TooManyItemsToLoadException e){
					LOGGER.info("Number of items to load " + e.getItemCount());
				}
			}
		}
		DownloadUtil.prepareResponse(ServletActionContext.getRequest(), response, "TrackReport.zip", "application/zip");
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
		} catch (IOException e) {
			LOGGER.warn("Getting the output stream failed with " + e.getMessage(), e);
		}
		if (outputStream!=null) {
			TrackExportBL.exportWorkItemsWithAttachments(reportBeanList, personID, outputStream);
		}
		return null;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}


	public void setWorkItemIDs(String workItemIDs) {
		this.workItemIDs = workItemIDs;
	}

}
