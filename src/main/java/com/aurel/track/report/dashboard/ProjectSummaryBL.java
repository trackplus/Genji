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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.admin.customize.category.filter.execute.loadItems.FilterUpperConfigUtil;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadTreeFilterItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.project.release.ReleaseBL;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.report.dashboard.grouping.AbstractGrouping;
import com.aurel.track.report.dashboard.grouping.GroupingFactory;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.HierarchicalBeanUtils;
import com.aurel.track.util.StringArrayParameterUtils;

/**
 *
 */
public class ProjectSummaryBL {

	public static List<ProjectWrapper> calculateProjects(Map<String, String> configParameters, TPersonBean personBean,
			int selectedGroupByField, boolean openOnly,Integer project,Integer release, boolean isForProjectSummary, Integer issueType) throws TooManyItemsToLoadException {
		List<ProjectWrapper> projectViews = new ArrayList<ProjectWrapper>();
		Locale locale = personBean.getLocale();
		List<Integer> selectedProjectsAndReleases = getSelectedProjectAndReleases(configParameters, project, release);
		if (selectedProjectsAndReleases==null || selectedProjectsAndReleases.isEmpty()) {
			return projectViews;
		}
		List<Integer> projectIDs = new LinkedList<Integer>();
		List<Integer> releaseIDs = new LinkedList<Integer>();
		separateProjectsFromReleases(selectedProjectsAndReleases, projectIDs, releaseIDs);
		boolean loadOnlyDocuments;
        if(issueType==null || issueType.intValue() == ProjectSummary.ISSUE_TYPE.GENERAL) {
        	loadOnlyDocuments = false;
        }else {
        	loadOnlyDocuments = true;
        }
		int projectIndex = 0;
		if (!projectIDs.isEmpty()) {
			List<TProjectBean> selectedProjectList = ProjectBL.loadByProjectIDs(projectIDs);
			List<TProjectBean> allProjectDescendants = ProjectBL.getDescendantProjects(selectedProjectList,true);
			Map<Integer,Set<Integer>> projectDescendantsMap=HierarchicalBeanUtils.getDescendantMap(selectedProjectList,allProjectDescendants);
			List<Integer> allProjectDescendantIDs=GeneralUtils.createIntegerListFromBeanList(allProjectDescendants);
			List<TWorkItemBean> workItemsList = null;
			if(!isForProjectSummary) {
				FilterUpperTO filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseIDs(true, GeneralUtils.createIntegerArrFromCollection(allProjectDescendantIDs), false, true, !openOnly);
				workItemsList = LoadTreeFilterItems.getTreeFilterWorkItemBeans(filterUpperTO, personBean, locale, false);
			}else {
		        int datasourceType = BasePluginDashboardBL.parseIntegerValue(configParameters, DataSourceDashboardBL.CONFIGURATION_PARAMETERS.SELECTED_DATASOURCE_TYPE, DataSourceDashboardBL.DATASOURCE_TYPE.PROJECT_RELEASE);
		        workItemsList = DataSourceDashboardBL.getGeneralOrDocumentWorkItems(projectIDs, datasourceType, personBean, locale, loadOnlyDocuments, false, false);
			}
			Map<Integer, List<TWorkItemBean>> workItemsProProjectDescendants = groupItemsByProjectDescendants(workItemsList,projectDescendantsMap);
			for (int i = 0; i < selectedProjectList.size(); i++) {
				++projectIndex;
				TProjectBean projectBean = selectedProjectList.get(i);
				Integer projectID = projectBean.getObjectID();
				ProjectWrapper projectWrapper = new ProjectWrapper(projectBean.getLabel());
				projectWrapper.setProjectID(projectID);
				projectWrapper.setGroupByFieldType(selectedGroupByField);
				projectWrapper.setOpenOnly(openOnly);
				workItemsList = workItemsProProjectDescendants.get(projectID);
				// Calculate the total number of issues grouped by status...
				AbstractGrouping abstractGrouping = GroupingFactory.getGrouping(selectedGroupByField, projectIndex);
				List<ElementWrapper> groupByList=abstractGrouping.groupItems(workItemsList, locale);
				projectWrapper.setList(groupByList);
				if (projectWrapper.getList().size() > 0) {
					projectViews.add(projectWrapper);
				}
			}
		}
		if (!releaseIDs.isEmpty()) {
			//releases
			List<TReleaseBean> selectedReleaseList = ReleaseBL.loadByReleaseIDs(releaseIDs);
			List<TReleaseBean> allReleaseDescendants= ReleaseBL.getDescendantReleases(selectedReleaseList, true);
			Map<Integer, Set<Integer>> releaseDescendantsMap=HierarchicalBeanUtils.getDescendantMap(selectedReleaseList,allReleaseDescendants);
			List<Integer> allReleasesDescendantIDs=GeneralUtils.createIntegerListFromBeanList(allReleaseDescendants);
			FilterUpperTO filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseIDs(false, GeneralUtils.createIntegerArrFromCollection(allReleasesDescendantIDs), false, true, !openOnly);
			if(loadOnlyDocuments) {
				List<TListTypeBean> documentItemTypeBeans = IssueTypeBL.loadStrictDocumentTypes();
				List<Integer> selectedItemTypeIDs = new ArrayList<Integer>();
				if(documentItemTypeBeans != null && !documentItemTypeBeans.isEmpty()) {
					List<Integer> documentItemTypes = GeneralUtils.createIntegerListFromBeanList(documentItemTypeBeans);
					selectedItemTypeIDs.addAll(documentItemTypes);
					filterUpperTO.setSelectedIssueTypes(GeneralUtils.createIntegerArrFromCollection(selectedItemTypeIDs));
				}
			}
			List<TWorkItemBean> workItemsList = LoadTreeFilterItems.getTreeFilterWorkItemBeans(filterUpperTO, personBean, locale, false);
			Map<Integer, List<TWorkItemBean>> workItemsProReleaseDescendants = groupItemsByReleaseDescendants(workItemsList,releaseDescendantsMap);
			//then process releases that are not part of any previous project
			for (int i = 0; i < selectedProjectsAndReleases.size(); ++i) {
				Integer releaseID = new Integer(selectedProjectsAndReleases.get(i));
				if (releaseID.intValue() > 0) {
					//it is release
					++projectIndex;
					// This is a release, go and get it
					//TReleaseBean releaseBean = releaseDAO.loadByPrimaryKey1(releaseID);
					TReleaseBean releaseBean = LookupContainer.getReleaseBean(releaseID); //releaseBeansMap.get(releaseID);
					if (releaseBean!=null) { //if the release was not deleted
						// Try to find the project. If it exists, forget about the release
						TProjectBean projectBean = LookupContainer.getProjectBean(releaseBean.getProjectID()); //readableProjectsMap.get(releaseBean.getProjectID());
						if (projectBean == null) {
							//right for project revoked
							continue;
						}

						ProjectWrapper projectWrapper = new ProjectWrapper(projectBean.getLabel() + " " + releaseBean.getLabel());
						workItemsList = workItemsProReleaseDescendants.get(releaseID);
						projectWrapper.setReleaseID(releaseBean.getObjectID());
						projectWrapper.setGroupByFieldType(selectedGroupByField);
						projectWrapper.setOpenOnly(openOnly);

						// Calculate the total number of issues grouped by status...
						AbstractGrouping abstractGrouping = GroupingFactory.getGrouping(selectedGroupByField, projectIndex);
						List<ElementWrapper> groupByList=abstractGrouping.groupItems(workItemsList, locale);
						projectWrapper.setList(groupByList);

						if (projectWrapper.getList().size() > 0) {
							projectViews.add(projectWrapper);
						}
					}
				}
			}
		}
		return projectViews;
	}

	/**
	 * Group items in project keyed map
	 * @param workItemsList
	 * @return
	 */
	private static Map<Integer, List<TWorkItemBean>> groupItemsByProject(List<TWorkItemBean> workItemsList) {
		Map<Integer, List<TWorkItemBean>> workItemsProProject = new HashMap<Integer, List<TWorkItemBean>>();
		if (workItemsList!=null) {
			for (Iterator<TWorkItemBean> iterator = workItemsList.iterator(); iterator.hasNext();) {
				TWorkItemBean workItemBean = iterator.next();
				Integer projectID = workItemBean.getProjectID();
				if (projectID!=null) {
					List<TWorkItemBean> projectItems = workItemsProProject.get(projectID);
					if (projectItems==null) {
						projectItems = new LinkedList<TWorkItemBean>();
						workItemsProProject.put(projectID, projectItems);
					}
					projectItems.add(workItemBean);
				}
			}
		}
		return workItemsProProject;
	}

	/**
	 * Add the items from descendant projects to the items from direct project
	 * @param workItemsList
	 * @param projectDescendantsMap
	 * @return
	 */
	private static Map<Integer, List<TWorkItemBean>> groupItemsByProjectDescendants(List<TWorkItemBean> workItemsList, Map<Integer,Set<Integer>> projectDescendantsMap) {
		Map<Integer, List<TWorkItemBean>> result=new HashMap<Integer, List<TWorkItemBean>>();
		Map<Integer, List<TWorkItemBean>> groupItemsByProject = groupItemsByProject(workItemsList);
		for (Map.Entry<Integer, Set<Integer>> entry : projectDescendantsMap.entrySet()) {
			Integer projectID = entry.getKey();
			List<TWorkItemBean> projectItems = result.get(projectID);
			if (projectItems==null) {
				projectItems = new LinkedList<TWorkItemBean>();
				result.put(projectID, projectItems);
			}
			List<TWorkItemBean> directProjectItems = groupItemsByProject.get(projectID);
			if(directProjectItems!=null){
				projectItems.addAll(directProjectItems);
			}
			//add descendants project
			Set<Integer> descendants=entry.getValue();
			for (Integer descendantProjectID : descendants) {
				List<TWorkItemBean> descendantProjectItems = groupItemsByProject.get(descendantProjectID);
				if (descendantProjectItems!=null) {
					projectItems.addAll(descendantProjectItems);
				}
			}
		}
		return result;
	}

	/**
	 * Group items in release keyed map
	 * @param workItemsList
	 * @return
	 */
	private static Map<Integer, List<TWorkItemBean>> groupItemsByRelease(List<TWorkItemBean> workItemsList) {
		Map<Integer, List<TWorkItemBean>> workItemsProRelease = new HashMap<Integer, List<TWorkItemBean>>();
		if (workItemsList!=null) {
			for (TWorkItemBean workItemBean : workItemsList) {
				Integer releaseID = workItemBean.getReleaseScheduledID();
				if (releaseID!=null) {
					List<TWorkItemBean> releaseItems = workItemsProRelease.get(releaseID);
					if (releaseItems==null) {
						releaseItems = new LinkedList<TWorkItemBean>();
						workItemsProRelease.put(releaseID, releaseItems);
					}
					releaseItems.add(workItemBean);
				}
			}
		}
		return workItemsProRelease;
	}

	/**
	 *  Add the items from descendant releases to the items from direct release
	 * @param workItemsList
	 * @param releaseDescendantsMap
	 * @return
	 */
	private static Map<Integer, List<TWorkItemBean>> groupItemsByReleaseDescendants(List<TWorkItemBean> workItemsList, Map<Integer, Set<Integer>> releaseDescendantsMap) {
		Map<Integer, List<TWorkItemBean>> result = new HashMap<Integer, List<TWorkItemBean>>();
		Map<Integer, List<TWorkItemBean>> groupItemsByRelease=groupItemsByRelease(workItemsList);
		for (Map.Entry<Integer, Set<Integer>> entry : releaseDescendantsMap.entrySet()) {
			Integer releaseID = entry.getKey();
			List<TWorkItemBean> releaseItems = result.get(releaseID);
			if (releaseItems==null) {
				releaseItems = new LinkedList<TWorkItemBean>();
				result.put(releaseID, releaseItems);
			}
			List<TWorkItemBean> directReleaseItems=groupItemsByRelease.get(releaseID);
			if(directReleaseItems!=null){
				releaseItems.addAll(directReleaseItems);
			}
			//add descendants release
			Set<Integer> descendants = entry.getValue();
			for (Integer descendantReleaseID : descendants) {
				List<TWorkItemBean> descendantReleaseItems=groupItemsByRelease.get(descendantReleaseID);
				if(descendantReleaseItems!=null){
					releaseItems.addAll(descendantReleaseItems);
				}
			}
		}
		return result;
	}

	/**
	 * Gets the selected projects and releases
	 * @param configParameters
	 * @param project
	 * @param release
	 * @return
	 */
	private static List<Integer> getSelectedProjectAndReleases(Map<String, String> configParameters, Integer project, Integer release) {
		List<Integer> selectedProjectsAndReleases;
		if(project!=null || release!=null){
			selectedProjectsAndReleases=new ArrayList<Integer>();
			if(release!=null){
				selectedProjectsAndReleases.add(release);
			}else{
				selectedProjectsAndReleases.add(project.intValue()*-1);
			}
		}else{
			selectedProjectsAndReleases = StringArrayParameterUtils.splitSelectionAsInteger(
					(String)configParameters.get(ProjectSummary.CONFIGURATION_PARAMETERS.SELECTED_PROJECTS_OR_RELEASES));
		}
		return selectedProjectsAndReleases;
	}

	/**
	 * Separates the projects from releases
	 * @param selectedProjectsAndReleases
	 * @param projectsList
	 * @param releaseList
	 */
	private static void separateProjectsFromReleases(List<Integer> selectedProjectsAndReleases, List<Integer> projectsList, List<Integer> releaseList) {
		if (selectedProjectsAndReleases!=null) {
			for (Integer projectReleasID : selectedProjectsAndReleases) {
				if (projectReleasID.intValue()<0) {
					projectsList.add(Integer.valueOf(-projectReleasID.intValue()));
				} else {
					releaseList.add(projectReleasID);
				}
			}
		}
	}
}
