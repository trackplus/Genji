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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.FilterUpperConfigUtil;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadTreeFilterItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.treeConfig.field.FieldConfigBL;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.report.dashboard.StatusOverTimeGraph.CONFIGURATION_PARAMETERS;
import com.aurel.track.report.dashboard.grouping.AbstractGrouping;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.StringArrayParameterUtils;

public class ProjectSummary extends BasePluginDashboardView{
	private static final Logger LOGGER = LogManager.getLogger(ProjectSummary.class);
	//Configuration page constants
	public static interface CONFIGURATION_PARAMETERS {
		static String SELECTED_PROJECTS_OR_RELEASES = "SelectedProjects";
		static String GROUP_BY_FIELDS = "groupByFields";
		static String SELECTED_GROUP_BY_BY_FIELD = "selectedGroupByField";
		static String ISSUE_TYPE = "issueType";
		static String SELECTED_ISSUE_TYPE = "selectedIssueType";
	}

	//Runtime link attribute names
	public static interface LINK_PARAMETERS {
		static String PROJECT = "projectID";
		static String ENTITY_FLAG="entityFlag";
		static String GROUPBYFIELDTYPE = "groupByFieldType";
		static String OPEN_ONLY = "openOnly";
	}

	public static interface ISSUE_TYPE {
		static int GENERAL = 1;
		static int DOCUMENT = 2;
	}

	@Override
	protected boolean isUseConfig(){
		return true;
	}

	@Override
	protected String encodeJSONExtraData(Integer dashboardID,Map<String, Object> session,
			Map<String, String> parameters, Integer projectID, Integer releaseID, Map<String,String> ajaxParams) throws TooManyItemsToLoadException{
		TPersonBean user = (TPersonBean) session.get(Constants.USER_KEY);
		StringBuilder sb=new StringBuilder();
		String strSelectedGroupByField = parameters.get(CONFIGURATION_PARAMETERS.SELECTED_GROUP_BY_BY_FIELD);
		int selectedGroupByField;
		if (strSelectedGroupByField==null) {
			selectedGroupByField = SystemFields.STATE;
		} else {
			try {
				selectedGroupByField = Integer.parseInt(strSelectedGroupByField);
			} catch (Exception e) {
				LOGGER.warn("Wrong group by field " + strSelectedGroupByField);
				selectedGroupByField = SystemFields.STATE;
			}
		}
		int issueTypeFlag = parseInteger(parameters, CONFIGURATION_PARAMETERS.ISSUE_TYPE, ISSUE_TYPE.GENERAL);
		List<ProjectWrapper> result=ProjectSummaryBL.calculateProjects(parameters, user, selectedGroupByField, false,projectID,releaseID, true, issueTypeFlag);
		JSONUtility.appendIntegerValue(sb, "issueTypeFlag", issueTypeFlag);
		if (projectID!=null) {
			if(result!=null && !result.isEmpty()){
				JSONUtility.appendJSONValue(sb,"project",ProjectWrapperJSON.encodeProjectWrapper(result.get(0)));
			}
		} else {
			JSONUtility.appendJSONValue(sb,"projects",ProjectWrapperJSON.encodeProjectWrapperList(result));
		}
		return sb.toString();
	}

	@Override
	public String encodeJSONExtraDataConfig(Map<String,String> parameters, TPersonBean user, Integer entityId, Integer entityType){
		StringBuilder sb=new StringBuilder();
		JSONUtility.appendIntegerListAsArray(sb,CONFIGURATION_PARAMETERS.SELECTED_PROJECTS_OR_RELEASES,
				StringArrayParameterUtils.splitSelectionAsInteger(parameters.get(CONFIGURATION_PARAMETERS.SELECTED_PROJECTS_OR_RELEASES)));
		JSONUtility.appendILabelBeanList(sb,CONFIGURATION_PARAMETERS.GROUP_BY_FIELDS, (List)getGroupByFields(user.getLocale()));
		String groupByFieldStr = parameters.get(CONFIGURATION_PARAMETERS.SELECTED_GROUP_BY_BY_FIELD);
		Integer groupByField=null;
		if(groupByFieldStr!=null){
			try{
				groupByField=Integer.parseInt(groupByFieldStr);
			}catch (Exception e){}
		}
		if (groupByField==null) {
			groupByField = SystemFields.INTEGER_STATE;
		}

		JSONUtility.appendIntegerValue(sb, CONFIGURATION_PARAMETERS.SELECTED_ISSUE_TYPE,  parseInteger(parameters, CONFIGURATION_PARAMETERS.ISSUE_TYPE, ISSUE_TYPE.GENERAL));
		boolean userHasWiki = false;
	    if(user.getLicensedFeaturesMap().get("wiki") != null) {
	    	userHasWiki = user.getLicensedFeaturesMap().get("wiki");
	    }
	    JSONUtility.appendBooleanValue(sb, "userHasWiki",  userHasWiki);
		JSONUtility.appendIntegerValue(sb,CONFIGURATION_PARAMETERS.SELECTED_GROUP_BY_BY_FIELD, groupByField);
		return sb.toString();
	}

	/**
	 * Get the list of available group by fields
	 * @param locale
	 * @return
	 */
	protected List<TFieldConfigBean> getGroupByFields(Locale locale) {
		List<Integer> fieldIDList = new ArrayList<Integer>();
		fieldIDList.add(SystemFields.INTEGER_STATE);
		fieldIDList.add(SystemFields.INTEGER_ISSUETYPE);
		fieldIDList.add(SystemFields.INTEGER_ORIGINATOR);
		fieldIDList.add(SystemFields.INTEGER_MANAGER);
		fieldIDList.add(SystemFields.INTEGER_RESPONSIBLE);
		fieldIDList.add(SystemFields.INTEGER_PRIORITY);
		fieldIDList.add(SystemFields.INTEGER_SEVERITY);
		return LocalizeUtil.localizeFieldConfigs(FieldConfigBL.loadDefaultForFields(fieldIDList), locale);
	}

	@Override
	public List<ReportBean> getIssues(Map<String, String> configParams, Map<String, String> filterParams, TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException{
		Integer projectID=BasePluginDashboardBL.parseInteger(filterParams,LINK_PARAMETERS.PROJECT);
		Integer entityFlag=BasePluginDashboardBL.parseInteger(filterParams,LINK_PARAMETERS.ENTITY_FLAG);
		boolean fromProject = false;

		if (entityFlag==null || SystemFields.INTEGER_PROJECT.equals(entityFlag)) {
			fromProject = true;
		}
		boolean openOnly=BasePluginDashboardBL.parseBoolean(filterParams, LINK_PARAMETERS.OPEN_ONLY);
		Integer groupByFieldType = BasePluginDashboardBL.parseInteger(filterParams,LINK_PARAMETERS.GROUPBYFIELDTYPE);
		Integer groupByFieldID = BasePluginDashboardBL.parseInteger(filterParams,AbstractGrouping.LINK_PARAMETERS.GROUPBYFIELDID);

		LOGGER.debug(" getIssues() providerParams: projectID " + projectID +  "  entityFlag " + entityFlag +
				" groupByFieldType " + groupByFieldType + " groupByFieldID " + groupByFieldID);
		FilterUpperTO filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(fromProject, projectID, true, true, !openOnly);
		filterUpperTO.setSelectedValuesForField(groupByFieldType, new Integer[] {groupByFieldID});
		filterUpperTO.setIncludeResponsiblesThroughGroup(false);
		return LoadTreeFilterItems.getTreeFilterReportBeans(filterUpperTO, null, null, personBean, locale);
	}
}
