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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.category.filter.execute.FilterExecuterFacade;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeans;

public class MyFilterView extends AbstractIssueFilterDashboard {
	
	static Logger LOGGER = LogManager.getLogger(MyFilterView.class);
	
	private static final String SELECTED_FILTER = "selFilter";

	protected boolean isUseConfig(){
		return true;
	}

	@Override
	protected FilterInfo getFilterInfo(Map session,Map parameters, TPersonBean user,Integer projectID,Integer releaseID){
		String filterId = (String) parameters.get(SELECTED_FILTER);
		if (filterId == null || "".equals(filterId)) {
			return null;
		}
		FilterInfo filter=new FilterInfo();
		String filterTitle=BasePluginDashboardBL.getFilterTitle(filterId, user.getLocale());
		
		filter.setId(new Integer(filterId));
		filter.setName(filterTitle);
		return filter;
	}

	@Override
	public String encodeJSONExtraDataConfig(Map<String,String> parameters, TPersonBean user, Integer entityId, Integer entityType){
		StringBuilder sb=new StringBuilder();
		String filter = parameters.get(SELECTED_FILTER);
		List projects = ProjectBL.loadUsedProjectsFlat(user.getObjectID());
		List projectIdList = new ArrayList();
		if (projects != null) {
			Iterator it = projects.iterator();
			while (it.hasNext()) {
				TProjectBean prjBean = (TProjectBean) it.next();
				projectIdList.add(prjBean.getObjectID());
			}
		}

		String maxIssuesToShowStr= parameters.get("maxIssuesToShow");
		Integer maxIssuesToShow=null;
		if(maxIssuesToShowStr!=null){
			try{
				maxIssuesToShow=Integer.parseInt(maxIssuesToShowStr);
			}catch (Exception e){}
		}
		if(maxIssuesToShow==null){
			maxIssuesToShow=MAX_NUMBER_OF_ISSUES_TO_SHOW;
		}
		JSONUtility.appendStringValue(sb,SELECTED_FILTER, filter);
		String filterTitle=null;
		if(filter!=null){
			filterTitle=BasePluginDashboardBL.getFilterTitle(filter, user.getLocale());
		}
		JSONUtility.appendStringValue(sb,"selFilterName", filterTitle);
		JSONUtility.appendIntegerValue(sb,"maxIssuesToShow", maxIssuesToShow);
		return sb.toString();
	}


	
	private static List<ReportBean> getAllIssues(Integer filterId, TPersonBean user, Locale locale,Integer projectID,Integer releaseID,List errors) throws TooManyItemsToLoadException {
		int entityFlag = SystemFields.PROJECT;
		Integer projectOrReleaseID = null;
		if (releaseID!=null) {
			projectOrReleaseID = releaseID;
			entityFlag = SystemFields.RELEASESCHEDULED;
		} else {
			if (projectID!=null) {
				projectOrReleaseID = projectID;
			}
		}
		List<ReportBean> reportBeansList = new ArrayList<ReportBean>();
		if (filterId != null) {			
			ReportBeans reportBeans = FilterExecuterFacade.getSavedFilterReportBeans(
					filterId, locale, user, errors, null, projectOrReleaseID, entityFlag);
			if (reportBeans!= null) {
				Collection<ReportBean> collection = reportBeans.getReportBeansMap().values();
				reportBeansList.addAll(collection);
			}
		}
		return reportBeansList;
	}
   

	@Override
	protected List<ReportBean> getReportBeanList(FilterInfo filter,TPersonBean user,Integer projectID,Integer releaseID,List<ErrorData> errors) throws TooManyItemsToLoadException{
		Locale locale=user.getLocale();
		List<ReportBean> reportBeanList = getAllIssues(filter.getId(), user, locale,projectID,releaseID,errors);
		return reportBeanList;
	}

	
}
