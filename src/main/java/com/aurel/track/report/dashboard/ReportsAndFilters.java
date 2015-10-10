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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.PredefinedQueryBL;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.FilterUpperConfigUtil;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadTreeFilterItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.report.ReportBL;
import com.aurel.track.beans.TExportTemplateBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.json.JSONUtility.JSON_FIELDS;
import com.aurel.track.report.dashboard.grouping.AbstractGrouping;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.StringArrayParameterUtils;

public class ReportsAndFilters extends BasePluginDashboardView {
	private static final Logger LOGGER = LogManager.getLogger(ReportsAndFilters.class);


	//Configuration page constants
	private static interface CONFIGURATION_PARAMETERS {
		static String SELECTED_PROJECT_OR_RELEASE = "selectedProjectOrRelease";
		//static String SELECTED_PROJECT_OR_RELEASE_NAME = "selectedProjectOrReleaseName";
		static String SELECTED_REPORTS = "selectedReports";
		//atic String REPORTS = "reports";
	}

	//Runtime page constants
	//private static String RESULTS = "results";
	//private static String RESULTP = "resultp";
	//private static String RESULTR = "resultr";
	private static String RELEASE = "release";
	//private static String FILTERS = "filters";

	private static String PROJECT_ID = "projectID";
	private static String ENTITY_FLAG = "entityFlag";

	//private static String TEMPLATE_14_EXISTS = "template14Exists";

	//Runtime link attribute names
	protected static interface ADDITIONAL_LINK_PARAMETERS {
		static String FILTER_IDENTIFIER = "filterIdentifier";
	}

	private static final int ALL_ISSUES=0;
	private static final int OUTSTANDING=1;
	private static final int UNSCHEDULED=2;

	private static final int RESOLVED_RECENTLY=6;
	private static final int ADDED_RECENTLY=7;
	private static final int UPDATED_RECENTLY=8;

	private static final int ME_RESPONSIBLE=20;
	private static final int ME_MANAGER=21;
	private static final int ME_AUTHOR=22;

	protected boolean isUseConfig(){
		return true;
	}

	@Override
	protected String encodeJSONExtraData(Integer dashboardID,Map<String, Object> session, Map<String, String> parameters,
				Integer projectID, Integer releaseID,Map<String,String> ajaxParams) throws TooManyItemsToLoadException{
		TPersonBean user = (TPersonBean) session.get(Constants.USER_KEY);
		StringBuilder sb=new StringBuilder();
		Locale locale=(Locale) session.get(Constants.LOCALE_KEY);
		boolean isProjectDashboard=true;
		if(projectID==null&&releaseID==null){
			//from cockpit dashboard: projectID is negative
			projectID =BasePluginDashboardBL.parseInteger(parameters,CONFIGURATION_PARAMETERS.SELECTED_PROJECT_OR_RELEASE);
			if (projectID!=null) {
				if (projectID.intValue()>0) {
					releaseID = projectID;
					projectID = null;
				} else {
					projectID = Integer.valueOf(-projectID.intValue());
					releaseID=null;
				}
			}
			isProjectDashboard=false;
		}
		JSONUtility.appendBooleanValue(sb,"isProjectDashboard", Boolean.valueOf(isProjectDashboard));
		if(projectID!=null||releaseID!=null){
			String selectedReportsStr = parameters.get(CONFIGURATION_PARAMETERS.SELECTED_REPORTS);
			List<Integer> selectedReportsArr = StringArrayParameterUtils.splitSelectionAsInteger(selectedReportsStr);
			if (selectedReportsArr==null || selectedReportsArr.isEmpty()) {
				selectedReportsArr = getDefaultReports();
			}
			List<TExportTemplateBean> existingSelectedExportTemplatesList = ReportBL.loadByIDs(selectedReportsArr);
			//TODO filter by rights: if the dashboard template configuration was copied from another person
			//can be that the copied to person can't see some of selectedReportTemplates
			//Map<Integer, TExportTemplateBean> existingSelectedExportTemplatesMap = GeneralUtils.createMapFromList(existingSelectedExportTemplatesList);
			//List<TExportTemplateBean> allReadableTemplatesList = getReports(user.getObjectID(), (Locale) session.get(Constants.LOCALE_KEY));
			/*List<TExportTemplateBean> selectedReadableReports = new ArrayList<TExportTemplateBean>();
			for (Iterator iterator = allReadableTemplatesList.iterator(); iterator.hasNext();) {
				TExportTemplateBean readableExportTemplateBean = (TExportTemplateBean) iterator.next();
				Integer templateID = readableExportTemplateBean.getObjectID();
				if (existingSelectedExportTemplatesMap.containsKey(templateID)) {
					//if the dashboard template configuration was copied from another person
					//can be that the copied to person can't see some of selectedReportTemplates
					selectedReadableReports.add(readableExportTemplateBean);
				}
			}*/
			//List<LabelValueBean> selectedReportsLVB=new ArrayList<LabelValueBean>();
			if(existingSelectedExportTemplatesList!=null && !existingSelectedExportTemplatesList.isEmpty()){
				StringBuilder selectedReports = new StringBuilder();
				selectedReports.append("[");
				for (Iterator<TExportTemplateBean> iterator = existingSelectedExportTemplatesList.iterator(); iterator.hasNext();) {
					TExportTemplateBean exportTemplateBean=iterator.next();
					boolean reportConfigNeeded = ReportBL.configNeededFormDashboard(exportTemplateBean.getObjectID());
					selectedReports.append("{");
					JSONUtility.appendStringValue(selectedReports, JSON_FIELDS.LABEL, exportTemplateBean.getLabel());
					JSONUtility.appendBooleanValue(selectedReports, "reportConfigNeeded", reportConfigNeeded);
					if (releaseID!=null) {
						JSONUtility.appendIntegerValue(selectedReports, "projectID", releaseID);
						JSONUtility.appendIntegerValue(selectedReports, "entityFlag", SystemFields.INTEGER_RELEASE);
					} else {
						 if (projectID!=null) {
							JSONUtility.appendIntegerValue(selectedReports, "projectID", Integer.valueOf(-projectID));
							JSONUtility.appendIntegerValue(selectedReports, "entityFlag", SystemFields.INTEGER_PROJECT);
						}
					}
					JSONUtility.appendIntegerValue(selectedReports, JSON_FIELDS.ID, exportTemplateBean.getObjectID(), true);
					selectedReports.append("}");
					if (iterator.hasNext()) {
						selectedReports.append(",");
					}
				}
				selectedReports.append("]");
				/*for (int i = 0; i < existingSelectedExportTemplatesList.size(); i++) {
					TExportTemplateBean exportTemplateBean=existingSelectedExportTemplatesList.get(i);

					String url="reportDatasource.action?templateID="+exportTemplateBean.getObjectID()+
							"&projectID="+projectID+"&entityFlag="+1+"&dashboardID="+dashboardID;
					selectedReportsLVB.add(new LabelValueBean(exportTemplateBean.getName(),url));
				}*/
				JSONUtility.appendJSONValue(sb, "selectedReports", selectedReports.toString());
			}
			//JSONUtility.appendJSONValue(sb, "selectedReports", JSONUtility.encodeJSONLabelValueBeanList(selectedReportsLVB));



			List<IntegerStringBean> filters=new ArrayList<IntegerStringBean>();

			filters.add(new IntegerStringBean(
					BasePluginDashboardBL.getText("reportsAndFilters.filters.allIssues",locale),
					Integer.valueOf(ALL_ISSUES)));
			filters.add(new IntegerStringBean(
					BasePluginDashboardBL.getText("reportsAndFilters.filters.unresolved",locale),
					Integer.valueOf(OUTSTANDING)));
			filters.add(new IntegerStringBean(
					BasePluginDashboardBL.getText("reportsAndFilters.filters.unplanned",locale),
					Integer.valueOf(UNSCHEDULED)));

			filters.add(new IntegerStringBean(
					BasePluginDashboardBL.getText("reportsAndFilters.filters.meResponsible",locale),
					Integer.valueOf(ME_RESPONSIBLE)));
			filters.add(new IntegerStringBean(
					BasePluginDashboardBL.getText("reportsAndFilters.filters.meManager",locale),
					Integer.valueOf(ME_MANAGER)));
			filters.add(new IntegerStringBean(
					BasePluginDashboardBL.getText("reportsAndFilters.filters.meAuthor",locale),
					Integer.valueOf(ME_AUTHOR)));

			filters.add(new IntegerStringBean(
					BasePluginDashboardBL.getText("reportsAndFilters.filters.recentlyResolved",locale),
					Integer.valueOf(RESOLVED_RECENTLY)));
			filters.add(new IntegerStringBean(
					BasePluginDashboardBL.getText("reportsAndFilters.filters.recentlyAdded",locale),
					Integer.valueOf(ADDED_RECENTLY)));
			filters.add(new IntegerStringBean(
					BasePluginDashboardBL.getText("reportsAndFilters.filters.recentlyUpdated",locale),
					Integer.valueOf(UPDATED_RECENTLY)));


			JSONUtility.appendJSONValue(sb, "filters", JSONUtility.encodeJSONIntegerStringBeanList(filters));

			List<ProjectWrapper> slist = ProjectSummaryBL.calculateProjects(parameters, user,SystemFields.STATE, true,projectID,releaseID, false, null);
			JSONUtility.appendJSONValue(sb, "results", ProjectWrapperJSON.encodeProjectWrapperList(slist));

			List<ProjectWrapper> resultp = ProjectSummaryBL.calculateProjects(parameters, user, SystemFields.PRIORITY, true,projectID,releaseID, false, null);
			JSONUtility.appendJSONValue(sb, "resultp", ProjectWrapperJSON.encodeProjectWrapperList(resultp));

			List<ProjectWrapper> resultr = ProjectSummaryBL.calculateProjects(parameters, user, SystemFields.RESPONSIBLE, true,projectID,releaseID, false, null);
			JSONUtility.appendJSONValue(sb, "resultr", ProjectWrapperJSON.encodeProjectWrapperList(resultr));
			if (!slist.isEmpty()) {
				JSONUtility.appendStringValue(sb,RELEASE, slist.get(0).getLabel());
			}
		}
		return sb.toString();
	}

	@Override
	public String encodeJSONExtraDataConfig(Map<String,String> parameters, TPersonBean user, Integer entityId, Integer entityType){
		StringBuilder sb=new StringBuilder();

		Integer selectedEntityID=BasePluginDashboardBL.parseInteger(parameters,CONFIGURATION_PARAMETERS.SELECTED_PROJECT_OR_RELEASE);
		/*String entityName=null;
		if(selectedEntityID!=null){
			if(selectedEntityID.intValue()>0){//release
				TReleaseBean releaseBean=DAOFactory.getFactory().getReleaseDAO().loadByPrimaryKey(selectedEntityID.intValue());
				entityName=releaseBean.getLabel();
			}else{
				TProjectBean projectBean= ProjectBL.loadByPrimaryKey(selectedEntityID * -1);
				entityName=projectBean.getLabel();
			}
		}*/
		//JSONUtility.appendStringValue(sb,CONFIGURATION_PARAMETERS.SELECTED_PROJECT_OR_RELEASE_NAME,entityName);
		JSONUtility.appendIntegerValue(sb,CONFIGURATION_PARAMETERS.SELECTED_PROJECT_OR_RELEASE,selectedEntityID);

		List<Integer> selectedIDs = StringArrayParameterUtils.splitSelectionAsInteger(parameters.get(CONFIGURATION_PARAMETERS.SELECTED_REPORTS));
		if (selectedIDs==null || selectedIDs.isEmpty()) {
			selectedIDs = getDefaultReports();
		}
		JSONUtility.appendIntegerListAsArray(sb, CONFIGURATION_PARAMETERS.SELECTED_REPORTS, selectedIDs);
		int prefWidth=550;
		int prefHeight=350;
		JSONUtility.appendIntegerValue(sb,"prefWidth",prefWidth);
		JSONUtility.appendIntegerValue(sb,"prefHeight",prefHeight);

		return sb.toString();
	}


	private List<Integer> getDefaultReports() {
		List<Integer> selectedIDs = new ArrayList<Integer>();
		selectedIDs.add(Integer.valueOf(12));
		selectedIDs.add(Integer.valueOf(13));
		selectedIDs.add(Integer.valueOf(14));
		selectedIDs.add(Integer.valueOf(15));
		return selectedIDs;
	}

	protected List<LabelValueBean> getReportsAsLabelValueBeans(List<TExportTemplateBean> allTemplates){
		List<LabelValueBean> result=new ArrayList<LabelValueBean>();
		if(allTemplates!=null){
			TExportTemplateBean report;
			for(int i=0;i<allTemplates.size();i++){
				report=allTemplates.get(i);
				result.add(new LabelValueBean(report.getLabel(),report.getObjectID().toString()));
			}
		}
		return result;
	}

	@Override
	public List<ReportBean> getIssues(Map<String, String> configParams, Map<String, String> filterParams, TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException{
		Integer projectID=BasePluginDashboardBL.parseInteger(filterParams,PROJECT_ID);
		Integer entityFlag=BasePluginDashboardBL.parseInteger(filterParams, ENTITY_FLAG);
		Integer groupByFieldType = BasePluginDashboardBL.parseInteger(filterParams,AbstractGrouping.LINK_PARAMETERS.GROUPBYFIELDTYPE);
		Integer groupByFieldID = BasePluginDashboardBL.parseInteger(filterParams, AbstractGrouping.LINK_PARAMETERS.GROUPBYFIELDID);
		boolean fromProject = false;
		if (entityFlag!=null) {
			fromProject = SystemFields.INTEGER_PROJECT.equals(entityFlag);
		}
		if (projectID==null){
			projectID =BasePluginDashboardBL.parseInteger(configParams,CONFIGURATION_PARAMETERS.SELECTED_PROJECT_OR_RELEASE);
			if (projectID!=null) {
				if (projectID.intValue()>0) {
					entityFlag = SystemFields.INTEGER_RELEASESCHEDULED;
				} else {
					projectID = Integer.valueOf(-projectID.intValue());
					entityFlag = SystemFields.INTEGER_PROJECT;
					fromProject = true;
				}
			}
		}
		Integer filterIdentifier = BasePluginDashboardBL.parseInteger(filterParams,ADDITIONAL_LINK_PARAMETERS.FILTER_IDENTIFIER);

		LOGGER.debug(" getIssues() providerParams: projectID " + projectID +  "  entityFlag " + entityFlag +
				" filterIdentifier " + filterIdentifier+", groupByFieldType="+groupByFieldType+", groupByFieldID="+groupByFieldID);

		if (filterIdentifier!=null) {
			Integer queryID=null;
			switch (filterIdentifier.intValue()) {
				case ALL_ISSUES:
					queryID=PredefinedQueryBL.PREDEFINED_QUERY.ALL_ITEMS;
					break;
				case OUTSTANDING:
					queryID=PredefinedQueryBL.PREDEFINED_QUERY.OUTSTANDING;
					break;
				case UNSCHEDULED:
					queryID=PredefinedQueryBL.PREDEFINED_QUERY.UNSCHEDULED;
					break;
				case RESOLVED_RECENTLY:
					queryID=PredefinedQueryBL.PREDEFINED_QUERY.CLOSED_RECENTLY;
					break;
				case ADDED_RECENTLY:
					queryID=PredefinedQueryBL.PREDEFINED_QUERY.ADDED_RECENTLY;
					break;
				case UPDATED_RECENTLY:
					queryID=PredefinedQueryBL.PREDEFINED_QUERY.UPDATED_RECENTLY;
					break;
				case ME_AUTHOR:
					queryID=PredefinedQueryBL.PREDEFINED_QUERY.AUTHOR_ITEMS;
					break;
				case ME_MANAGER:
					queryID=PredefinedQueryBL.PREDEFINED_QUERY.MANAGERS_ITEMS;
					break;
				case ME_RESPONSIBLE:
					queryID=PredefinedQueryBL.PREDEFINED_QUERY.RESPONSIBLES_ITEMS;
					break;
				default:
					queryID=PredefinedQueryBL.PREDEFINED_QUERY.ALL_ITEMS;
					break;
			}
			List<ReportBean> reportBeanList=PredefinedQueryBL.getReportBeans(personBean, queryID, projectID, entityFlag, locale);
			return reportBeanList;
		}else{
			FilterUpperTO filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(fromProject, projectID, true, true, false);
			filterUpperTO.setSelectedValuesForField(groupByFieldType, new Integer[] {groupByFieldID});
			return LoadTreeFilterItems.getTreeFilterReportBeans(filterUpperTO, null, null, personBean, locale);
		}
	}
}

