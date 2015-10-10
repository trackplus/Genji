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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.FilterUpperConfigUtil;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadItemIDListItems;
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
import com.aurel.track.json.JSONUtility;
import com.aurel.track.report.datasource.releaseNotes.ReleaseNotesDatasource;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.HierarchicalBeanUtils;
import com.aurel.track.util.StringArrayParameterUtils;

public class ReleaseNotes extends BasePluginDashboardView{
	private static final Logger LOGGER = LogManager.getLogger(ReleaseNotes.class);

	//Configuration page constants
	private static interface CONFIGURATION_PARAMETERS {
		static String SELECTED_PROJECTS = "SelectedProjects";
		static String ISSUE_TYPES = "issueTypes";
		static String SELECTED_ISSUE_TYPES = "selectedIssueTypes";
	}
	
	//Runtime page constants	
	private static String RESULT = "result"; 
	
	//Runtime link attribute names
	private static interface LINK_PARAMETERS {
		static String PROJECT = "projectID";
		static String RELEASE_ID="releaseID";
		static String OPEN_ONLY = "openOnly";
		static String SHOWALL = "showAll";
	}
	
	private HashMap<Integer,Integer> projectIds = new HashMap<Integer,Integer>();

	protected boolean isUseConfig(){
		return true;
	}

	@Override
	protected String encodeJSONExtraData(Integer dashboardID,Map<String, Object> session, Map<String, String> parameters,
										 Integer projectID, Integer releaseID,Map<String,String> ajaxParams) throws TooManyItemsToLoadException{
		TPersonBean user = (TPersonBean) session.get(Constants.USER_KEY);
		StringBuilder sb=new StringBuilder();


		Locale locale=(Locale) session.get(Constants.LOCALE_KEY);
		//save the issueProvider on the session under the theProvider attribute:
		//it will be used by ReportLoadAction, the theProvider attribute will be set from the link

		List<ProjectWrapper> result=assembleReleaseNotes(parameters, user, projectID,releaseID);


		JSONUtility.appendJSONValue(sb,RESULT,ReleaseNoteJSON.encodeProjectWrapperList(result));
		return sb.toString();
	}

	@Override
	public String encodeJSONExtraDataConfig(Map<String,String> parameters, TPersonBean user, Integer entityId, Integer entityType){
		StringBuilder sb=new StringBuilder();
		JSONUtility.appendIntegerListAsArray(sb,"selectedProjects", StringArrayParameterUtils.splitSelectionAsInteger(parameters.get(CONFIGURATION_PARAMETERS.SELECTED_PROJECTS)));
		JSONUtility.appendIssueTypeBeanList(sb,CONFIGURATION_PARAMETERS.ISSUE_TYPES, getIssueTypes(user.getLocale()));
		JSONUtility.appendIntegerListAsArray(sb,CONFIGURATION_PARAMETERS.SELECTED_ISSUE_TYPES, StringArrayParameterUtils.splitSelectionAsInteger(parameters.get(CONFIGURATION_PARAMETERS.SELECTED_ISSUE_TYPES)));
		int prefWidth=600;
		int prefHeight=450;
		JSONUtility.appendIntegerValue(sb,"prefWidth",prefWidth);
		JSONUtility.appendIntegerValue(sb,"prefHeight",prefHeight);
		return sb.toString();
	}

	/**
	 * Get the list of available issue types for display on the configuration page.
	 * @param locale
	 * @return
	 */
	private List<TListTypeBean> getIssueTypes(Locale locale) {
		List<TListTypeBean> issueTypeList = IssueTypeBL.loadAllowedByProjectTypes(GeneralUtils.createIntegerArrFromCollection(projectIds.values()));		
		return LocalizeUtil.localizeDropDownList(issueTypeList, locale);
	}
	
	/**
	 * Assemble the release notes. This is a structure of ProjectWrapper elements, containing ReleaseNoteWrapper
	 * elements for each issue type, containing list with workitems.
	 * @param configParameters
	 * @param personBean
	 * @return
	 * @throws TooManyItemsToLoadException 
	 */
	private List<ProjectWrapper> assembleReleaseNotes(Map configParameters, TPersonBean personBean,
			Integer project,Integer release) throws TooManyItemsToLoadException {
		//List selectedProjects, Integer userId, String theProvider, String theParams, Map session, Locale locale) {
		List<ProjectWrapper> projectViews = new ArrayList<ProjectWrapper>();
		Locale locale = personBean.getLocale();
		/*List selectedProjectsAndReleases;
		if(project!=null){
			selectedProjectsAndReleases=new ArrayList();
			if(release!=null){
				selectedProjectsAndReleases.add(release.toString());
			}else{
				selectedProjectsAndReleases.add((project.intValue()*-1)+"");
			}
		}else{
			selectedProjectsAndReleases = StringArrayParameterUtils.splitSelection((String)configParameters.get(CONFIGURATION_PARAMETERS.SELECTED_PROJECTS));
		}*/
		
		List<Integer> selectedProjectsAndReleases = getSelectedProjectAndReleases(configParameters, project, release);
		
		List selectedIssueTypes = StringArrayParameterUtils.splitSelection((String)configParameters.get(CONFIGURATION_PARAMETERS.SELECTED_ISSUE_TYPES));
		String bundleName = getDescriptor().getBundleName();
		String resolved = LocalizeUtil.getLocalizedText("releaseNotes.resolved", locale, bundleName);
		String unresolved = LocalizeUtil.getLocalizedText("releaseNotes.unresolved", locale, bundleName); 
		
		// TODO Handle empty selections
		
		//Map selectedProjectsMap = new HashMap();
		//List selectedProjectList = ProjectBL.loadProjectsWithReadIssueRight(personBean.getObjectID());
		/*Map readableProjectsMap = GeneralUtils.createMapFromList(selectedProjectList);
		if (selectedProjectsAndReleases.size() > 0) {
			selectedProjectsMap = prepareProjectMap(selectedProjectsAndReleases, readableProjectsMap);
			selectedProjectList = new ArrayList(selectedProjectsMap.values());
		}*/
		//int projectIndex = 0;
		//Map statusBeansMap = GeneralUtils.createMapFromList(StatusBL.loadAll());
		//List items;
		
		List<Integer> projectIDs = new LinkedList<Integer>();
		List<Integer> releaseIDs = new LinkedList<Integer>();
		separateProjectsFromReleases(selectedProjectsAndReleases, projectIDs, releaseIDs);
		//first process projects
		if (!projectIDs.isEmpty()) {
			List<TProjectBean> selectedProjectList = ProjectBL.loadByProjectIDs(projectIDs);
			List<TProjectBean> allProjectDescendants = ProjectBL.getDescendantProjects(selectedProjectList,true);
			Map<Integer,Set<Integer>> projectDescendantsMap=HierarchicalBeanUtils.getDescendantMap(selectedProjectList,allProjectDescendants);
			List<Integer> allProjectDescendantIDs=GeneralUtils.createIntegerListFromBeanList(allProjectDescendants);
			FilterUpperTO filterUpperTOResolved = FilterUpperConfigUtil.getByProjectReleaseIDs(true, GeneralUtils.createIntegerArrFromCollection(allProjectDescendantIDs), false, false, true);
			List<TWorkItemBean> workItemsListResolved = LoadTreeFilterItems.getTreeFilterWorkItemBeans(filterUpperTOResolved, personBean, locale, false);
			Map<Integer, List<TWorkItemBean>> workItemsProProjectDescendantsResolved = groupItemsByProjectDescendants(workItemsListResolved, projectDescendantsMap);
			FilterUpperTO filterUpperTOUnresolved = FilterUpperConfigUtil.getByProjectReleaseIDs(true, GeneralUtils.createIntegerArrFromCollection(allProjectDescendantIDs), false, true, false);
			List<TWorkItemBean> workItemsListUnresolved = LoadTreeFilterItems.getTreeFilterWorkItemBeans(filterUpperTOUnresolved, personBean, locale, false);
			Map<Integer, List<TWorkItemBean>> workItemsProProjectDescendantsUnresolved = groupItemsByProjectDescendants(workItemsListUnresolved, projectDescendantsMap);
			for (TProjectBean projectBean : selectedProjectList) {
				List<TWorkItemBean> itemsResolved = workItemsProProjectDescendantsResolved.get(projectBean.getObjectID());
				ProjectWrapper projectWrapperResolved = new ProjectWrapper(projectBean.getLabel() + "   " + resolved);
				List<ReleaseNoteWrapper> relNoteList = getReleaseNotesForSingleRelease(itemsResolved, locale, selectedIssueTypes);
				int noOfResolvedIssues = computeTotalEntries(relNoteList);
				projectWrapperResolved.setList(relNoteList);
				projectWrapperResolved.setSymbol("release_ok.png");
				// and the open ones...
				ProjectWrapper projectWrapperOpen = new ProjectWrapper(projectBean.getLabel() + "   " + unresolved);
				projectWrapperOpen.setSymbol("release_error.png");
				List<TWorkItemBean> itemsUnresolved = workItemsProProjectDescendantsUnresolved.get(projectBean.getObjectID());
				relNoteList = getReleaseNotesForSingleRelease(itemsUnresolved, locale, selectedIssueTypes);
				int noOfOpenIssues = computeTotalEntries(relNoteList);
				projectWrapperOpen.setList(relNoteList);
				projectWrapperOpen.setNumberResolved(noOfResolvedIssues);
				projectWrapperOpen.setNumberOpen(noOfOpenIssues);
				projectWrapperOpen.setProjectID(projectBean.getObjectID());
				projectWrapperOpen.setAreResolved(false);
				projectWrapperResolved.setNumberResolved(noOfResolvedIssues);
				projectWrapperResolved.setNumberOpen(noOfOpenIssues);
				projectWrapperResolved.setProjectID(projectBean.getObjectID());
				projectWrapperResolved.setAreResolved(true);
				if (projectWrapperResolved.getList().size() > 0) {
					projectViews.add(projectWrapperResolved);
				}
				if (projectWrapperOpen.getList().size() > 0) {
					projectViews.add(projectWrapperOpen);
				}
			}
		}
		if (!releaseIDs.isEmpty()) {
			List<TReleaseBean> selectedReleaseList = ReleaseBL.loadByReleaseIDs(releaseIDs);
			List<TReleaseBean> allReleaseDescendants = ReleaseBL.getDescendantReleases(selectedReleaseList, true);
			Map<Integer, Set<Integer>> releaseDescendantsMap = HierarchicalBeanUtils.getDescendantMap(selectedReleaseList,allReleaseDescendants);
			List<Integer> allReleasesDescendantIDs = GeneralUtils.createIntegerListFromBeanList(allReleaseDescendants);
			FilterUpperTO filterUpperTOResolved = FilterUpperConfigUtil.getByProjectReleaseIDs(false, GeneralUtils.createIntegerArrFromCollection(allReleasesDescendantIDs), false, false, true);
			List<TWorkItemBean> workItemsListResolved = LoadTreeFilterItems.getTreeFilterWorkItemBeans(filterUpperTOResolved, personBean, locale, false);
			Map<Integer, List<TWorkItemBean>> workItemsProReleaseDescendantsResolved = groupItemsByReleaseDescendants(workItemsListResolved, releaseDescendantsMap);
			FilterUpperTO filterUpperTOUnresolved = FilterUpperConfigUtil.getByProjectReleaseIDs(false, GeneralUtils.createIntegerArrFromCollection(allReleasesDescendantIDs), false, true, false);
			List<TWorkItemBean> workItemsListUnresolved = LoadTreeFilterItems.getTreeFilterWorkItemBeans(filterUpperTOUnresolved, personBean, locale, false);
			Map<Integer, List<TWorkItemBean>> workItemsProReleasetDescendantsUnresolved = groupItemsByReleaseDescendants(workItemsListUnresolved, releaseDescendantsMap);
			for (TReleaseBean releaseBean : selectedReleaseList) {
					if (releaseBean!=null) { //if the release was not deleted
						// Try to find the project. If it exists, forget about the release
						//TProjectBean projectBean = (TProjectBean) readableProjectsMap.get(releaseBean.getProjectID());
						TProjectBean projectBean = LookupContainer.getProjectBean(releaseBean.getProjectID());
						if (projectBean == null) {
							//right for project revoked
							continue;
						}
						ProjectWrapper projectWrapperResolved = new ProjectWrapper(projectBean.getLabel() + " " + releaseBean.getLabel() + "   " + resolved);
						projectWrapperResolved.setSymbol("box_closed.png");
	
						List<TWorkItemBean> itemsResolved = workItemsProReleaseDescendantsResolved.get(releaseBean.getObjectID());
						List<ReleaseNoteWrapper> relNoteList = getReleaseNotesForSingleRelease(itemsResolved, locale, selectedIssueTypes);
						int noOfResolvedIssues = computeTotalEntries(relNoteList);
	
						projectWrapperResolved.setList(relNoteList);
	
						// and the open ones...
						ProjectWrapper projectWrapperOpen = new ProjectWrapper(projectBean.getLabel() + " " + releaseBean.getLabel() + "   " + unresolved);
						projectWrapperOpen.setSymbol("box.png");
	
						List<TWorkItemBean> itemsUnresolved = workItemsProReleasetDescendantsUnresolved.get(releaseBean.getObjectID());
						relNoteList = getReleaseNotesForSingleRelease(itemsUnresolved, locale, selectedIssueTypes);
						int noOfOpenIssues = computeTotalEntries(relNoteList);
						projectWrapperOpen.setList(relNoteList);
	
						// and finally process both to compute open/resolved bar
	
						projectWrapperOpen.setNumberResolved(noOfResolvedIssues);
						projectWrapperOpen.setNumberOpen(noOfOpenIssues);
						projectWrapperOpen.setReleaseID(releaseBean.getObjectID());
						projectWrapperOpen.setAreResolved(false);
	
						projectWrapperResolved.setNumberResolved(noOfResolvedIssues);
						projectWrapperResolved.setNumberOpen(noOfOpenIssues);
						projectWrapperResolved.setReleaseID(releaseBean.getObjectID());
						projectWrapperResolved.setAreResolved(true);
	
						if (projectWrapperResolved.getList().size() > 0) {
							projectViews.add(projectWrapperResolved);
						}
	
						if (projectWrapperOpen.getList().size() > 0) {
							projectViews.add(projectWrapperOpen);
						}
					}
				//}
			}
		}
		return projectViews;
	}

	/*private static Map prepareProjectMap(List selectedProjects, Map readableProjectsMap) {
		Map projectMap = new HashMap();
		for (int i=0; i < selectedProjects.size(); ++ i) {
			Integer projectID = new Integer((String)selectedProjects.get(i)); 
			if (projectID.intValue() < 0) {
				//if project it is negative, get the positive value 
				projectID = new Integer(-projectID.intValue());
				// Get all projects w/o releases
				TProjectBean projectBean = (TProjectBean) readableProjectsMap.get(projectID);
				if (projectBean!=null) { //right for project revoked, or project deleted
					projectMap.put(projectBean.getObjectID(), projectBean);
				}
			}
		}
		return projectMap;
	}*/
	
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
					(String) configParameters.get(ProjectSummary.CONFIGURATION_PARAMETERS.SELECTED_PROJECTS_OR_RELEASES));
		}
		return selectedProjectsAndReleases;
	}
	
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
	
	/**
	 * Function to group the workitems by issue types.
	 * @param items
	 * @return List with ReleaseNoteWrapper elements, which themselves contain a list with
	 * issues for a specific issue type.
	 */
	private List<ReleaseNoteWrapper> getReleaseNotesForSingleRelease(List<TWorkItemBean> items, Locale locale, List selectedIssueTypes) {
		return ReleaseNotesDatasource.getReleaseNotesForSingleRelease(items, locale, selectedIssueTypes);
	}

	@Override
	public List<ReportBean> getIssues(Map<String, String> configParams, Map<String, String> filterParams, TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException{
		List<ReportBean> result=null;
		Integer personID = personBean.getObjectID();
		Integer projectID=BasePluginDashboardBL.parseInteger(filterParams,LINK_PARAMETERS.PROJECT);
		Integer releaseID=BasePluginDashboardBL.parseInteger(filterParams,LINK_PARAMETERS.RELEASE_ID);
		List selectedIssueTypes = StringArrayParameterUtils.splitSelection(configParams.get(CONFIGURATION_PARAMETERS.SELECTED_ISSUE_TYPES));
		boolean openOnly =BasePluginDashboardBL.parseBoolean(filterParams,LINK_PARAMETERS.OPEN_ONLY);
		boolean showAll =BasePluginDashboardBL.parseBoolean(filterParams, LINK_PARAMETERS.SHOWALL);

		LOGGER.debug(" getIssues() providerParams: projectID " + projectID +  "  releaseID " + releaseID +
				" openOnly " + openOnly + " showAll " + showAll);
		List<TWorkItemBean> items=null;
		if(projectID!=null) {
			FilterUpperTO filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(true, projectID, true, showAll || openOnly, showAll || !openOnly);
			items = LoadTreeFilterItems.getTreeFilterWorkItemBeans(filterUpperTO, personBean, locale, false);
		}
		if(releaseID!=null){
			FilterUpperTO filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(false, releaseID, true, showAll || openOnly, showAll || !openOnly);
			items = LoadTreeFilterItems.getTreeFilterWorkItemBeans(filterUpperTO, personBean, locale, false);
		}		
		if(selectedIssueTypes!=null && !selectedIssueTypes.isEmpty()){
			items = getFilteredItems(items,  selectedIssueTypes);
		}

		if (items==null || items.isEmpty()) {
			LOGGER.warn("Empty link datasource before filtering");
			result=null;
		}else{
			int [] workItemIDs = new int[items.size()];
			for (int i=0; i < items.size(); ++i) {
				TWorkItemBean wib = items.get(i);
				workItemIDs[i] =  wib.getObjectID().intValue();
			}
			result=LoadItemIDListItems.getReportBeansByWorkItemIDs(workItemIDs, false, personID, locale, true);
		}
		return result;
	}

	private int computeTotalEntries (List<ReleaseNoteWrapper> relWrap) {
		if (relWrap == null || relWrap.isEmpty()) {
			return 0;
		}
		int size = 0;
		for (int i=0; i<relWrap.size(); ++i) {
			size = size + relWrap.get(i).getWorkItems().size();
		}
		return size;
	}
	
	/**
	 * Function to filter work items by issue types.
	 * @param items
	 * @return List with TWorkItemBean
	 */
	private List<TWorkItemBean> getFilteredItems(List<TWorkItemBean> items, List selectedIssueTypes) {
		List<TWorkItemBean> filteredList = new ArrayList<TWorkItemBean>();
		if (items!=null) {
			Iterator<TWorkItemBean> it = items.iterator();
			while (it.hasNext()) {
				TWorkItemBean wib = it.next();
				if (selectedIssueTypes.contains(wib.getListTypeID().toString())) {
					filteredList.add(wib);
				}
			}
		}
		return filteredList;
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
}
