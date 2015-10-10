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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.PredefinedQueryBL;
import com.aurel.track.admin.customize.category.filter.execute.TreeFilterExecuterFacade;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.report.dashboard.grouping.AbstractGrouping;
import com.aurel.track.report.dashboard.grouping.GroupingFactory;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.SimpleTreeNode;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.HierarchicalBeanUtils;
import com.aurel.track.util.IntegerStringBean;



public class ManagerView extends BasePluginDashboardView {
	
	private static final Logger LOGGER = LogManager.getLogger(ManagerView.class);
	
	//private static WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();
	//private static final String PARAMSKEY="pI";
	
	//private Map theListMap = new HashMap();
   
	//Runtime link attribute names
	private static interface LINK_PARAMETERS {
		static String PROJECT = "projectID";
		static String ENTITY_FLAG="entityFlag";
		static String GROUPBYFIELDTYPE = "groupByFieldType";
	}

	@Override
	protected String encodeJSONExtraData(Integer dashboardID,Map<String, Object> session, 
			Map<String, String> parameters, Integer projectID, Integer releaseID,Map<String,String> ajaxParams) throws TooManyItemsToLoadException{
		TPersonBean user = (TPersonBean) session.get(Constants.USER_KEY);
		StringBuilder sb=new StringBuilder();
		Locale locale = user.getLocale();
		List<ProjectWrapper> projects=calculateProjects(user,locale,projectID,releaseID);
		if(projectID!=null){
			ProjectWrapper result;
			if(!projects.isEmpty()){
				result=projects.get(0);
			}else{
				result=new ProjectWrapper("");
			}
			JSONUtility.appendJSONValue(sb,"result", ProjectWrapperJSON.encodeProjectWrapper(result));
		}else{
			JSONUtility.appendJSONValue(sb,"projects",ProjectWrapperJSON.encodeProjectWrapperList(projects));
		}
		return sb.toString();
	}
	
	


	private List<ProjectWrapper> calculateProjects(TPersonBean personBean, Locale locale, Integer project, Integer release) throws TooManyItemsToLoadException {
		List<ProjectWrapper> projectWrappersList = new ArrayList<ProjectWrapper>();
		List<Integer> projectBeanIDs;
		if (project!=null) {
			projectBeanIDs = ProjectBL.getDescendantProjectIDsAsList(new Integer[] {project});
		} else {
			projectBeanIDs = GeneralUtils.createIntegerListFromBeanList(ProjectBL.loadUsedProjectsFlat(personBean.getObjectID()));
		}
		// Get all projects for where I have issues as a manager...
		if (projectBeanIDs == null || projectBeanIDs.isEmpty()) {
			return projectWrappersList;
		}
		Integer entityFlag = null;
		Integer projectOrReleaseID = null;
		if (project!=null) {
			entityFlag = SystemFields.INTEGER_PROJECT;
			projectOrReleaseID = project;
		}
		if (release!=null) {
			entityFlag = SystemFields.INTEGER_RELEASE;
			projectOrReleaseID = release;
		}				
		//get all not closed manager items from all not closed projects  
		List<TWorkItemBean> workItemsList = TreeFilterExecuterFacade.getSavedFilterWorkItemBeans(
				PredefinedQueryBL.PREDEFINED_QUERY.MANAGERS_ITEMS, locale, personBean, new LinkedList<ErrorData>(), false, projectOrReleaseID, entityFlag, null, null);
		List<IntegerStringBean> projectsList = null;
		boolean generalCockpit = false;
		Map<Integer, Set<Integer>> projectDescendantsMap = new HashMap<Integer, Set<Integer>>();
		if (projectOrReleaseID!=null) {
			projectsList = new LinkedList<IntegerStringBean>();
			projectsList.add(new IntegerStringBean("", project));
		} else {
			List<SimpleTreeNode> projectTree = HierarchicalBeanUtils.getSimpleProjectTreeWithCompletedPath(
					projectBeanIDs, SystemFields.INTEGER_PROJECT, new HashMap<Integer, SimpleTreeNode>());
			projectsList = ProjectBL.getPlainListFromTree(projectTree, 0);
			ProjectBL.loadProjectToDecendentIDsMap(projectTree, projectDescendantsMap);
			generalCockpit = true;
		}
		int projectIndex = 0;
		List<TWorkItemBean> managerItems;
		for (IntegerStringBean projectBean : projectsList) {
			++projectIndex;
			// For each project where I have issues as manager do...
			Integer projectID = projectBean.getValue();
			ProjectWrapper prjWrapper = new ProjectWrapper(projectBean.getLabel());
			if (generalCockpit) {
				Set<Integer> projDescendants=projectDescendantsMap.get(projectID);
				managerItems = new LinkedList<TWorkItemBean>();
				if (workItemsList!=null) {
					for (TWorkItemBean workItemBean : workItemsList) {
						if (doesMatchProj(workItemBean, projectID, projDescendants)) {
							managerItems.add(workItemBean);
						}
					}
				}
			} else {
				managerItems = workItemsList;
			}
			prjWrapper.setProjectID(projectID);
			if (release!=null) {
				prjWrapper.setReleaseID(release);
			}
			prjWrapper.setGroupByFieldType(SystemFields.PRIORITY);
			prjWrapper.setGroupByFieldType2(SystemFields.RESPONSIBLE);
			AbstractGrouping abstractGrouping;
			// Get the total size for scaling; we scale for each project
			if (managerItems != null) {
				// Calculate the total number of issues grouped by priorities...
				abstractGrouping = GroupingFactory.getGrouping(SystemFields.PRIORITY, projectIndex);
				//Map linkParametersMap = prepareLinkParametersMap(personID, projectID,  new Integer(SystemFields.PRIORITY),release);
				List groupByPriorities=abstractGrouping.groupItems(managerItems,locale);
				prjWrapper.setList(groupByPriorities);

				// Calculate the total number of issues grouped by responsibles...
				abstractGrouping = GroupingFactory.getGrouping(SystemFields.RESPONSIBLE, projectIndex);
				//linkParametersMap = prepareLinkParametersMap(personID, projectID, new Integer(SystemFields.RESPONSIBLE),release);
				List groupByUser=abstractGrouping.groupItems(managerItems,locale);
				prjWrapper.setSecondList(groupByUser);

				if (prjWrapper.getList().size() > 0
						|| prjWrapper.getSecondList().size() > 0) {
					projectWrappersList.add(prjWrapper);
				}
			}
		}
		return projectWrappersList;
	}

	private boolean doesMatchProj(TWorkItemBean workItemBean, Integer projKey, Set<Integer> projDescendants) {
		if(projKey==null){
			return true;
		}
		Integer workItemProjectID=workItemBean.getProjectID();
		return workItemProjectID.equals(projKey) || (projDescendants!=null && projDescendants.contains(workItemProjectID));
	}
	
	@Override
	public List<ReportBean> getIssues(Map<String, String> configParams, Map<String, String> filterParams, TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException{
		Integer projectID=BasePluginDashboardBL.parseInteger(filterParams,LINK_PARAMETERS.PROJECT);
		Integer entityFlag=BasePluginDashboardBL.parseInteger(filterParams,LINK_PARAMETERS.ENTITY_FLAG);
		Integer groupByFieldType = BasePluginDashboardBL.parseInteger(filterParams,LINK_PARAMETERS.GROUPBYFIELDTYPE);
		Integer groupByFieldID = BasePluginDashboardBL.parseInteger(filterParams,AbstractGrouping.LINK_PARAMETERS.GROUPBYFIELDID);
		LOGGER.debug("providerParams: projectID " + projectID +  "  entityFlag " + entityFlag +
				" groupByFieldType " + groupByFieldType + " groupByFieldID " + groupByFieldID);
		return TreeFilterExecuterFacade.getTreeFilterReportBeanListWithAdditionalCondition(PredefinedQueryBL.PREDEFINED_QUERY.MANAGERS_ITEMS, locale, personBean, projectID, entityFlag, groupByFieldType, groupByFieldID, false);
	}

}
