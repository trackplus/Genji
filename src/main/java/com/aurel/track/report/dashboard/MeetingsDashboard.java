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


package com.aurel.track.report.dashboard;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadItemIDListItems;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.report.execute.ReportBean;

public class MeetingsDashboard extends BasePluginDashboardView{
	private static final Logger LOGGER = LogManager.getLogger(MeetingsDashboard.class);

	private static interface LINK_PARAMETERS {
		static String ISSUE = "issue";
		static String ISRESOLVED = "resolved";
		static String SHOWALL = "showAll";
		static String REPORT_MEETING = "reportMeeting";
	}
	private static final int REPORT_MEETING=0;

	@Override
	protected String encodeJSONExtraData(Integer dashboardID,Map<String, Object> session, Map<String, String> parameters,
										 Integer projectID, Integer releaseID,Map<String,String> ajaxParams){
		TPersonBean user = (TPersonBean) session.get(Constants.USER_KEY);
		StringBuilder sb=new StringBuilder();

		Locale locale=(Locale) session.get(Constants.LOCALE_KEY);

		List<MeetingTO> meetings=MeetingsBL.getMeetings(user,locale,projectID,releaseID);

		JSONUtility.appendJSONValue(sb,"meetings",MeetingsBL.encodeJSONMeetings(meetings));
		return sb.toString();
	}

	@Override
	public List<ReportBean> getIssues(Map<String, String> configParams, Map<String, String> filterParams, TPersonBean personBean, Locale locale){
		Integer issueID = BasePluginDashboardBL.parseInteger(filterParams,LINK_PARAMETERS.ISSUE);
		Integer personID = personBean.getObjectID();
		boolean isResolved = BasePluginDashboardBL.parseBoolean(filterParams, LINK_PARAMETERS.ISRESOLVED);
		boolean showAll = BasePluginDashboardBL.parseBoolean(filterParams,LINK_PARAMETERS.SHOWALL);
		boolean reportMeeting=BasePluginDashboardBL.parseBoolean(filterParams,LINK_PARAMETERS.REPORT_MEETING);

		LOGGER.debug("getIssues()  issueID="+issueID+", isResolved="+isResolved+", showAll="+showAll+", reportMeeting="+reportMeeting);

		List<TWorkItemBean> allDescInds=MeetingsBL.getAllDescendends(issueID, reportMeeting, personID);
		List<TWorkItemBean> workItemBeans;
		if(!showAll){
			workItemBeans=MeetingsBL.filterItems(allDescInds, isResolved);
		}else{
			workItemBeans=allDescInds;
		}
		List<ReportBean> reportBeanList= LoadItemIDListItems.getReportBeansByWorkItems(workItemBeans, personID, locale,false,false,false,false,false,false,false,false,false);
		workItemBeans.clear();
		workItemBeans=null;
		return reportBeanList;
	}

}
