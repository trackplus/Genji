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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.PredefinedQueryBL;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.json.JSONUtility.JSON_FIELDS;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.resources.ResourceBundleManager;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.StringArrayParameterUtils;

public class MyItems extends BasePluginDashboardView{

	public static final String QUERY_ID="queryID";
	public static final String PROJECT_ID="projectID";
	public static final String ENTITY_FLAG="entityFlag";
	public static final String LATE_ONLY="lateOnly";
	
	public static interface CONFIGURATION_PARAMETERS {
		static String RACI_FIELDS = "raciFields";
		static String SELECTED_RACI_FIELDS = "selectedRaciFields";
	}

	@Override
	protected boolean isUseConfig(){
		return true;
	}

	@Override
	protected String encodeJSONExtraData(Integer dashboardID,Map<String, Object> session, Map<String, String> parameters,
				Integer projectID, Integer releaseID, Map<String,String> ajaxParams) throws TooManyItemsToLoadException{
		TPersonBean user = (TPersonBean)session.get(Constants.USER_KEY);
		StringBuilder sb=new StringBuilder();
		Locale locale=(Locale) session.get(Constants.LOCALE_KEY);
		int[] selectedRaciRoles=null;
		String selectedRaciRolesStr = parameters.get(CONFIGURATION_PARAMETERS.SELECTED_RACI_FIELDS);
		if (selectedRaciRolesStr!=null) {
			try{
				selectedRaciRoles=StringArrayParameterUtils.splitSelectionAsIntArray(selectedRaciRolesStr);
			}catch (Exception e){}
		}
		Set<Integer> raciRolesSet = GeneralUtils.createSetFromIntArr(selectedRaciRoles);
		if (raciRolesSet.isEmpty()) {
			//for backward compatibility
			raciRolesSet.add(MyItemsBL.RACI_ROLES.MANAGER);
			raciRolesSet.add(MyItemsBL.RACI_ROLES.ORIGINATOR);
			raciRolesSet.add(MyItemsBL.RACI_ROLES.RESPONSIBLE);
		}
		boolean responsible = raciRolesSet.contains(MyItemsBL.RACI_ROLES.RESPONSIBLE);
		boolean reporter = raciRolesSet.contains(MyItemsBL.RACI_ROLES.ORIGINATOR);
		boolean manager = raciRolesSet.contains(MyItemsBL.RACI_ROLES.MANAGER);
		boolean watcher = raciRolesSet.contains(MyItemsBL.RACI_ROLES.WATCHER);
		MyItemsBL dashBL = new MyItemsBL(user,locale,projectID,releaseID, raciRolesSet);
		Integer responsibleNumber= Integer.valueOf(dashBL.getTotalNumOfResponsibleItems());
		Integer reporterNumber= Integer.valueOf(dashBL.getTotalNumOfReporterItems());
		Integer managerNumber= Integer.valueOf(dashBL.getTotalNumOfManagerItems());
		Integer watcherNumber = Integer.valueOf(dashBL.getTotalNumOfWatcherItems());
		List<MyItemsProjSummaryBean> responsibleProjects= dashBL.getProjResponsibleItemsSum();
		List<MyItemsProjSummaryBean> reporterProjects= dashBL.getProjReporterItemsSum();
		List<MyItemsProjSummaryBean> managerProjects= dashBL.getProjManagerItemsSum();
		List<MyItemsProjSummaryBean> watcherProjects= dashBL.getProjWatcherItemsSum();
		JSONUtility.appendIntegerValue(sb, "projectID", projectID);
		if(projectID!=null) {
			//browse project dashboard
			List<MyItemsProjSummaryBean> projectSummaryBeans = new LinkedList<MyItemsProjSummaryBean>();
			if (responsible && responsibleNumber>0 && responsibleProjects!=null && !responsibleProjects.isEmpty()){
				MyItemsProjSummaryBean responsibleSummaryBean = responsibleProjects.get(0);
				responsibleSummaryBean.setProjName(
						LocalizeUtil.getLocalizedText("myItems.responsiblesItems", locale, ResourceBundleManager.DASHBOARD_RESOURCES));
				projectSummaryBeans.add(responsibleSummaryBean);
			}
			if (reporter && reporterNumber>0 && reporterProjects!=null && !reporterProjects.isEmpty()) {
				MyItemsProjSummaryBean reporterSummaryBean = reporterProjects.get(0);
				reporterSummaryBean.setProjName(
						LocalizeUtil.getLocalizedText("myItems.reporterItems", locale, ResourceBundleManager.DASHBOARD_RESOURCES));
				projectSummaryBeans.add(reporterSummaryBean);
			}
			if (manager && managerNumber>0 && managerProjects!=null && !managerProjects.isEmpty()){
				MyItemsProjSummaryBean managerSummaryBean = managerProjects.get(0);
				managerSummaryBean.setProjName(
						LocalizeUtil.getLocalizedText("myItems.managersItems", locale, ResourceBundleManager.DASHBOARD_RESOURCES));
				projectSummaryBeans.add(managerSummaryBean);
			}
			if (watcher && watcherNumber>0 && watcherProjects!=null && !watcherProjects.isEmpty()) {
				MyItemsProjSummaryBean watcherSummaryBean = watcherProjects.get(0);
				watcherSummaryBean.setProjName(
						LocalizeUtil.getLocalizedText("myItems.watcherItems", locale, ResourceBundleManager.DASHBOARD_RESOURCES));
				projectSummaryBeans.add(watcherSummaryBean);
			}
			JSONUtility.appendJSONValue(sb, "projectJSON",
					MyItemsBL.encodeJSON_ProjectSummaryList(projectSummaryBeans));
		}else{
			//general cockpit
			List<MyItemsRaciSummaryBean> myItems = new LinkedList<MyItemsRaciSummaryBean>();
			if (responsible) {
				myItems.add(new MyItemsRaciSummaryBean(
						LocalizeUtil.getLocalizedText("myItems.responsiblesItems", locale, ResourceBundleManager.DASHBOARD_RESOURCES),
						responsibleNumber, PredefinedQueryBL.PREDEFINED_QUERY.RESPONSIBLES_ITEMS, responsibleProjects));
			}
			if (reporter) {
				myItems.add(new MyItemsRaciSummaryBean(
						LocalizeUtil.getLocalizedText("myItems.reporterItems", locale, ResourceBundleManager.DASHBOARD_RESOURCES),
						reporterNumber, PredefinedQueryBL.PREDEFINED_QUERY.AUTHOR_ITEMS, reporterProjects));
			}
			if (manager) {
				myItems.add(new MyItemsRaciSummaryBean(
						LocalizeUtil.getLocalizedText("myItems.managersItems", locale, ResourceBundleManager.DASHBOARD_RESOURCES),
						managerNumber, PredefinedQueryBL.PREDEFINED_QUERY.MANAGERS_ITEMS, managerProjects));
			}
			if (watcher) {
				myItems.add(new MyItemsRaciSummaryBean(
						LocalizeUtil.getLocalizedText("myItems.watcherItems", locale, ResourceBundleManager.DASHBOARD_RESOURCES),
						watcherNumber, PredefinedQueryBL.PREDEFINED_QUERY.WATCHER_ITEMS, watcherProjects));
			}
			if (!myItems.isEmpty()) {
				sb.append("\"myItemsList\":[");
				if(myItems!=null){
					for (Iterator<MyItemsRaciSummaryBean> iterator = myItems.iterator(); iterator.hasNext();) {
						MyItemsRaciSummaryBean myItemsRaciSummaryBean = iterator.next();
						sb.append("{");
						JSONUtility.appendStringValue(sb, JSON_FIELDS.LABEL, myItemsRaciSummaryBean.getRaciLabel());
						JSONUtility.appendIntegerValue(sb, "totalInRaciRole", myItemsRaciSummaryBean.getTotalInRaciRole());
						JSONUtility.appendIntegerValue(sb, "queryID", myItemsRaciSummaryBean.getQueryID());
						JSONUtility.appendJSONValue(sb, "projectJSON", MyItemsBL.encodeJSON_ProjectSummaryList(myItemsRaciSummaryBean.getProjectSummaries()), true);
						sb.append("}");
						if (iterator.hasNext()) {
							sb.append(",");
						}
					}
				}
				sb.append("],");
			}
		}
		responsibleProjects.clear();
		responsibleProjects=null;
		reporterProjects.clear();
		reporterProjects=null;
		managerProjects.clear();
		managerProjects=null;
		watcherProjects.clear();
		watcherProjects=null;
		return sb.toString();
	}
	
	@Override
	public String encodeJSONExtraDataConfig(Map<String,String> parameters, TPersonBean user, Integer entityId, Integer entityType){
		StringBuilder sb=new StringBuilder();
		JSONUtility.appendIntegerStringBeanList(sb, CONFIGURATION_PARAMETERS.RACI_FIELDS, (List)MyItemsBL.getRaciFields(user.getLocale()));
		String selectedRaciRolesStr = parameters.get(CONFIGURATION_PARAMETERS.SELECTED_RACI_FIELDS);
		int[] selectedRaciRoles=null;
		if (selectedRaciRolesStr!=null) {
			try{
				selectedRaciRoles=StringArrayParameterUtils.splitSelectionAsIntArray(selectedRaciRolesStr);
			}catch (Exception e){}
		} else {
			//for backward compatibility
			selectedRaciRoles = new int[] {MyItemsBL.RACI_ROLES.MANAGER, MyItemsBL.RACI_ROLES.RESPONSIBLE, MyItemsBL.RACI_ROLES.ORIGINATOR};
		}
		JSONUtility.appendIntArrayAsArray(sb,CONFIGURATION_PARAMETERS.SELECTED_RACI_FIELDS, selectedRaciRoles);
		return sb.toString();
	}
	
	@Override
	public List<ReportBean> getIssues(Map<String, String> configParams, Map<String, String> filterParams, TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException{
		List<ReportBean> result=new ArrayList<ReportBean>();
		Integer queryID=BasePluginDashboardBL.parseInteger(filterParams,QUERY_ID);
		Integer projectID=BasePluginDashboardBL.parseInteger(filterParams,PROJECT_ID);
		Integer entityFlag=BasePluginDashboardBL.parseInteger(filterParams,ENTITY_FLAG);
		boolean lateOnly=BasePluginDashboardBL.parseBoolean(filterParams, LATE_ONLY);
		if(queryID!=null){
			result= PredefinedQueryBL.getReportBeans(personBean, queryID, projectID, entityFlag, locale);
			if (lateOnly) {
				for (Iterator<ReportBean> iterator = result.iterator(); iterator.hasNext();) {
					ReportBean reportBean = (ReportBean) iterator.next();
					if (!(reportBean.isDateConflict() || reportBean.isBudgetOrPlanConflict())) {
						iterator.remove();
					}
				}
			}
		}
		return result;
	}
}
