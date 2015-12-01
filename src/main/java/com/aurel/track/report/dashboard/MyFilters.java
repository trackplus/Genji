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


package com.aurel.track.report.dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.FilterBL;
import com.aurel.track.admin.customize.category.filter.execute.TreeFilterExecuterFacade;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.util.LabelValueBean;

public class MyFilters extends BasePluginDashboardView {
	public static final String QUERY_ID="queryID";
	public static final String PROJECT_ID="projectID";
	public static final String ENTITY_FLAG="entityFlag";


	@Override
	public String getPluginID() {
		//for backward compatibility (it is stored in the TDashboardField)
		return "com.aurel.track.report.dashboard.TqlItems";
	}

	@Override
	protected boolean isUseConfig(){
		return true;
	}

	@Override
	protected String encodeJSONExtraData(Integer dashboardID,Map<String, Object> session, Map<String, String> parameters,
										  Integer projectID, Integer releaseID,Map<String,String> ajaxParams){
		TPersonBean user = (TPersonBean) session.get(Constants.USER_KEY);
		Locale locale = (Locale) session.get(Constants.LOCALE_KEY);
		StringBuilder sb=new StringBuilder();
		JSONUtility.appendLabelValueBeanList(sb, "tqlList",getFilters(user, locale));
		return sb.toString();
	}
	
	private static List<LabelValueBean> getFilters(TPersonBean user, Locale locale) {
		//reports filter only
		//List<TQueryRepositoryBean> allFilters = queryRepositoryDAO.loadMenuitemQueries(user.getObjectID(),queryTypes,true);
		List<TQueryRepositoryBean> allFilters =  FilterBL.loadMyMenuFilters(user.getObjectID(), locale);
		List<LabelValueBean> filterView = new ArrayList<LabelValueBean>();
		for (int i = 0; i < allFilters.size(); ++i) {
			TQueryRepositoryBean filterBean = allFilters.get(i);
			LabelValueBean lvBean = new LabelValueBean(filterBean.getLabel(),filterBean.getObjectID().toString());
			filterView.add(lvBean);
		}
		return filterView;
	}
	@Override
	public List<ReportBean> getIssues(Map<String, String> configParams, Map<String, String> filterParams, TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException{
		List<ReportBean> result=new ArrayList<ReportBean>();
		Integer queryID=BasePluginDashboardBL.parseInteger(filterParams,QUERY_ID);
		Integer projectID=BasePluginDashboardBL.parseInteger(filterParams,PROJECT_ID);
		Integer entityFlag=BasePluginDashboardBL.parseInteger(filterParams,ENTITY_FLAG);
		if(queryID!=null){
			List<ErrorData> errors=new ArrayList<ErrorData>();
			result= TreeFilterExecuterFacade.getSavedFilterReportBeanList(queryID,
					locale, personBean, errors, projectID, entityFlag);
		}
		return result;
	}

}
