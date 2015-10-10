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

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.Constants;
import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.category.filter.execute.TreeFilterExecuterFacade;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterSelectsListsLoader;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.StringArrayParameterUtils;

public class QuickSearchView extends BasePluginDashboardView{
	@Override
	protected String encodeJSONExtraData(Integer dashboardID,Map<String, Object> session, Map<String, String> parameters,
										 Integer projectID, Integer releaseID,Map<String,String> ajaxParams){
		TPersonBean user = (TPersonBean) session.get(Constants.USER_KEY);
		StringBuilder sb=new StringBuilder();

		FilterUpperTO filterUpperTO = new FilterUpperTO();
		boolean refreshProject=false;
		if(ajaxParams!=null){
			Integer[] selectedProjects= StringArrayParameterUtils.splitSelectionAsIntegerArray(ajaxParams.get("selectedProjects"),",");
			Integer[] selectedIssueTypes=StringArrayParameterUtils.splitSelectionAsIntegerArray(ajaxParams.get("selectedIssueTypes"),",");
			Integer[] selectedStates=StringArrayParameterUtils.splitSelectionAsIntegerArray(ajaxParams.get("selectedStates"),",");
			filterUpperTO.setSelectedProjects(selectedProjects);
			filterUpperTO.setSelectedIssueTypes(selectedIssueTypes);
			filterUpperTO.setSelectedStates(selectedStates);
			refreshProject="true".equalsIgnoreCase(ajaxParams.get("refreshProject"));
		}

		List<Integer> accessRights = new LinkedList<Integer>();
		accessRights.add(Integer.valueOf(AccessBeans.AccessFlagIndexes.PROJECTADMIN));
		accessRights.add(Integer.valueOf(AccessBeans.AccessFlagIndexes.READANYTASK));
		accessRights.add(Integer.valueOf(AccessBeans.AccessFlagIndexes.MODIFYANYTASK));
		accessRights.add(Integer.valueOf(AccessBeans.AccessFlagIndexes.CREATETASK));
		int[] rights = GeneralUtils.createIntArrFromIntegerList(accessRights);
		TPersonBean personBean = (TPersonBean) session.get(Constants.USER_KEY);

		JSONUtility.appendJSONValue(sb,"projects",
				JSONUtility.getTreeHierarchyJSON(ProjectBL.getProjectNodesByRightEager(
						true, personBean, false, rights, true, false), true, false));


		Locale locale=(Locale) session.get(Constants.LOCALE_KEY);
		FilterSelectsListsLoader.loadFilterSelects(filterUpperTO, user, locale, true, false);

		JSONUtility.appendIntegerArrayAsArray(sb,"selectedProjects",filterUpperTO.getSelectedProjects());

		JSONUtility.appendILabelBeanList(sb,"issueTypes",filterUpperTO.getIssueTypes());
		JSONUtility.appendIntegerArrayAsArray(sb,"selectedIssueTypes",filterUpperTO.getSelectedIssueTypes());

		JSONUtility.appendILabelBeanList(sb,"states",filterUpperTO.getStates());
		JSONUtility.appendIntegerArrayAsArray(sb,"selectedStates",filterUpperTO.getSelectedStates());



		JSONUtility.appendBooleanValue(sb,"refreshProject",refreshProject);
		JSONUtility.appendIntegerValue(sb,"projectID",projectID);
		JSONUtility.appendIntegerValue(sb,"releaseID",releaseID);
		JSONUtility.appendStringValue(sb,"projectLabel", FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_PROJECT, locale));
		JSONUtility.appendStringValue(sb,"issueTypeLabel", FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_ISSUETYPE, locale));
		JSONUtility.appendStringValue(sb,"statusLabel", FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_STATE, locale));

		return sb.toString();
	}
	public List<ReportBean> getIssues(Map<String, String> configParams, Map<String, String> filterParams, TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException{
		FilterUpperTO filterUpperTO = new FilterUpperTO();
		if(filterParams!=null){
			Integer[] selectedProjects= StringArrayParameterUtils.splitSelectionAsIntegerArray(filterParams.get("selectedProjects"),",");
			Integer[] selectedIssueTypes=StringArrayParameterUtils.splitSelectionAsIntegerArray(filterParams.get("selectedIssueTypes"),",");
			Integer[] selectedStates=StringArrayParameterUtils.splitSelectionAsIntegerArray(filterParams.get("selectedStates"),",");

			filterUpperTO.setSelectedProjects(selectedProjects);
			filterUpperTO.setSelectedIssueTypes(selectedIssueTypes);
			filterUpperTO.setSelectedStates(selectedStates);
		}

		Integer projectID=BasePluginDashboardBL.parseInteger(filterParams,"projectID");
		Integer entityFlag=BasePluginDashboardBL.parseInteger(filterParams,"entityFlag");

		return TreeFilterExecuterFacade.getInstantTreeFilterReportBeanListNoReport(filterUpperTO, null, null,personBean, locale, projectID, entityFlag);
	}
}
