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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.PredefinedQueryBL;
import com.aurel.track.admin.customize.category.filter.execute.FilterExecuterFacade;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.SimpleTreeNode;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.HierarchicalBeanUtils;
import com.aurel.track.util.IntegerStringBean;

public class MyItemsBL {
	
	
	public static interface RACI_ROLES {
		static int MANAGER = 1;
		static int RESPONSIBLE = 2;
		static int ORIGINATOR = 3;
		static int WATCHER = 4;
	}
	
	/**
	 * The summary of project on category (resp,manager,myItems)
	 */
	private List<MyItemsProjSummaryBean> projResponsibleItemsSum;
	private List<MyItemsProjSummaryBean> projManagerItemsSum;
	private List<MyItemsProjSummaryBean> projReporterItemsSum;
	private List<MyItemsProjSummaryBean> projWatcherItemsSum;
	
	/**
	 * total number of items on category
	 */
	private int totalNumOfResponsibleItems;
	private int totalNumOfManagerItems;
	private int totalNumOfReporterItems;
	private int totalNumOfWatcherItems;
	
	public MyItemsBL(TPersonBean user,Locale locale) throws TooManyItemsToLoadException {
		this(user,locale,null,null, null);
	}
	public MyItemsBL(TPersonBean user,Locale locale,Integer projectID,Integer releaseID, Set<Integer> raciFields) throws TooManyItemsToLoadException {
		populate(user,locale,projectID,releaseID, raciFields);
	}
	
	/**
	 * Get the list of available raci fields
	 * @param locale
	 * @return
	 */
	static List<IntegerStringBean> getRaciFields(Locale locale) {
		List<IntegerStringBean> raciList = new ArrayList<IntegerStringBean>();
		raciList.add(new IntegerStringBean(BasePluginDashboardBL.getText("myItems.managersItems", locale), RACI_ROLES.MANAGER));
		raciList.add(new IntegerStringBean(BasePluginDashboardBL.getText("myItems.responsiblesItems", locale), RACI_ROLES.RESPONSIBLE));
		raciList.add(new IntegerStringBean(BasePluginDashboardBL.getText("myItems.reporterItems", locale), RACI_ROLES.ORIGINATOR));
		raciList.add(new IntegerStringBean(BasePluginDashboardBL.getText("myItems.watcherItems", locale), RACI_ROLES.WATCHER));
		return raciList;
	}
	
	/**
	 * Populate the summary beans
	 * @param personBean
	 * @param locale
	 * @param projectID
	 * @param releaseID
	 * @param raciFields
	 * @throws TooManyItemsToLoadException 
	 */
	private void populate(TPersonBean personBean, Locale locale, Integer projectID, Integer releaseID, Set<Integer> raciFields) throws TooManyItemsToLoadException {
		Integer personID=personBean.getObjectID();
		boolean showResponsibles = raciFields.contains(RACI_ROLES.RESPONSIBLE);
		boolean showOriginator = raciFields.contains(RACI_ROLES.ORIGINATOR);
		boolean showManager = raciFields.contains(RACI_ROLES.MANAGER);
		boolean showWatchers = raciFields.contains(RACI_ROLES.WATCHER);
		Integer entityFlag = null;
		Integer projectOrReleaseID = null;
		if (projectID!=null) {
			entityFlag = SystemFields.INTEGER_PROJECT;
			projectOrReleaseID = projectID;
		}
		if (releaseID!=null) {
			entityFlag = SystemFields.INTEGER_RELEASE;
			projectOrReleaseID = releaseID;
		}
		List<ReportBean> responsibleItems = null;
		if (showResponsibles) {
			responsibleItems = FilterExecuterFacade.getSavedFilterReportBeanList(PredefinedQueryBL.PREDEFINED_QUERY.RESPONSIBLES_ITEMS,
					locale, personBean, false, new LinkedList<ErrorData>(), projectOrReleaseID, entityFlag, false, false, false, true, true, true, false, false, false);
		}
		List<ReportBean> managerItems = null;
		if (showManager) {
			managerItems = FilterExecuterFacade.getSavedFilterReportBeanList(PredefinedQueryBL.PREDEFINED_QUERY.MANAGERS_ITEMS,
					locale, personBean, false, new LinkedList<ErrorData>(), projectOrReleaseID, entityFlag, false, false, false, true, true, true, false, false, false);
		}
		List<ReportBean> authorItems = null;
		if (showOriginator) {
			authorItems = FilterExecuterFacade.getSavedFilterReportBeanList(PredefinedQueryBL.PREDEFINED_QUERY.AUTHOR_ITEMS,
					locale, personBean, false, new LinkedList<ErrorData>(), projectOrReleaseID, entityFlag, false, false, false, true, true, true, false, false, false);
		}
		List<ReportBean> watcherItems = null;
		if (showWatchers) {
			watcherItems = FilterExecuterFacade.getSavedFilterReportBeanList(PredefinedQueryBL.PREDEFINED_QUERY.WATCHER_ITEMS,
					locale, personBean, false, new LinkedList<ErrorData>(), projectOrReleaseID, entityFlag, false, false, false, true, true, true, false, false, false);
		}
		// the project specific results go inhere
		projResponsibleItemsSum = new LinkedList<MyItemsProjSummaryBean>();
		projManagerItemsSum = new LinkedList<MyItemsProjSummaryBean>();
		projReporterItemsSum=new LinkedList<MyItemsProjSummaryBean>();
		projWatcherItemsSum =new LinkedList<MyItemsProjSummaryBean>();
		// load the project list
		if (projectOrReleaseID!=null) {
			//browse project/release
			int projectTotal = 0;
			MyItemsProjSummaryBean projResponsibleSumBean = null;
			if (showResponsibles) {
				projResponsibleSumBean=createProjSummaryBean(responsibleItems, projectOrReleaseID, PredefinedQueryBL.PREDEFINED_QUERY.RESPONSIBLES_ITEMS);
				projectTotal+=projResponsibleSumBean.getNumItems();
			}
			//manager items
			MyItemsProjSummaryBean projManagerSumBean = null;
			if (showManager) {
				projManagerSumBean = createProjSummaryBean(managerItems, projectOrReleaseID, PredefinedQueryBL.PREDEFINED_QUERY.MANAGERS_ITEMS);
				projectTotal+=projManagerSumBean.getNumItems();
			}
			//reporter issues
			MyItemsProjSummaryBean projReporterItemSumBean = null;
			if (showOriginator) {
				projReporterItemSumBean = createProjSummaryBean(authorItems, projectOrReleaseID, PredefinedQueryBL.PREDEFINED_QUERY.AUTHOR_ITEMS);
				projectTotal+=projReporterItemSumBean.getNumItems();
			}
			//watcher issues
			if (showWatchers) {
				MyItemsProjSummaryBean projWatcherItemSumBean=createProjSummaryBean(watcherItems, projectOrReleaseID, PredefinedQueryBL.PREDEFINED_QUERY.WATCHER_ITEMS);
				if (projWatcherItemSumBean.getNumItems()>0) {
					projWatcherItemsSum.add(projWatcherItemSumBean);
				}
			}
			if(projectTotal>0){
				if (projResponsibleSumBean!=null && projResponsibleSumBean.getNumItems()>0) {
					projResponsibleItemsSum.add(projResponsibleSumBean);
				}
				if (projManagerSumBean!=null && projManagerSumBean.getNumItems()>0) {
					projManagerItemsSum.add(projManagerSumBean);
				}
				if (projReporterItemSumBean!=null && projReporterItemSumBean.getNumItems()>0) {
					projReporterItemsSum.add(projReporterItemSumBean);
				}
			}
		} else {
			//general cockpit
			List<Integer> projectBeanIDs = GeneralUtils.createIntegerListFromBeanList(ProjectBL.loadUsedProjectsFlat(personID));
			List<SimpleTreeNode> projectTree = HierarchicalBeanUtils.getSimpleProjectTreeWithCompletedPath(
					projectBeanIDs, SystemFields.INTEGER_PROJECT, new HashMap<Integer, SimpleTreeNode>());
			List<IntegerStringBean> projectsList = ProjectBL.getPlainListFromTree(projectTree, 0);
			Map<Integer, Set<Integer>> projectDescendantsMap = new HashMap<Integer, Set<Integer>>();
			ProjectBL.loadProjectToDecendentIDsMap(projectTree, projectDescendantsMap); 
			for (IntegerStringBean projectBean : projectsList) {
				Integer projId = projectBean.getValue();
				String projectLabel = projectBean.getLabel();
				int projectTotal = 0;
				Set<Integer> projDescendants=projectDescendantsMap.get(projId);
				//responsible items
				MyItemsProjSummaryBean projResponsibleSumBean = null;
				if (showResponsibles) {
					projResponsibleSumBean=createProjSummaryBean(responsibleItems, projId, projectLabel, projDescendants, personID,
						PredefinedQueryBL.PREDEFINED_QUERY.RESPONSIBLES_ITEMS);
					projectTotal+=projResponsibleSumBean.getNumItems();
				}
				//manager items
				MyItemsProjSummaryBean projManagerSumBean = null;
				if (showManager) {
					projManagerSumBean = createProjSummaryBean(managerItems, projId, projectLabel, projDescendants, personID,
						PredefinedQueryBL.PREDEFINED_QUERY.MANAGERS_ITEMS);
					projectTotal+=projManagerSumBean.getNumItems();
				}
				//reporter issues
				MyItemsProjSummaryBean projReporterItemSumBean = null;
				if (showOriginator) {
					projReporterItemSumBean = createProjSummaryBean(authorItems, projId, projectLabel, projDescendants, personID,
						PredefinedQueryBL.PREDEFINED_QUERY.AUTHOR_ITEMS);
					projectTotal+=projReporterItemSumBean.getNumItems();
				}
				//watcher issues
				if (showWatchers) {
					MyItemsProjSummaryBean projWatcherItemSumBean=createProjSummaryWatcherBean(watcherItems, projId, projectLabel, projDescendants, personID,
							PredefinedQueryBL.PREDEFINED_QUERY.WATCHER_ITEMS);
					if (projWatcherItemSumBean.getNumItems()>0) {
						projWatcherItemsSum.add(projWatcherItemSumBean);
					}
				}
				if(projectTotal>0){
					if (projResponsibleSumBean!=null  && projResponsibleSumBean.getNumItems()>0) {
						projResponsibleItemsSum.add(projResponsibleSumBean);
					}
					if (projManagerSumBean!=null && projManagerSumBean.getNumItems()>0) {
						projManagerItemsSum.add(projManagerSumBean);
					}
					if (projReporterItemSumBean!=null && projReporterItemSumBean.getNumItems()>0) {
						projReporterItemsSum.add(projReporterItemSumBean);
					}
				}
			}
		}
		if (showResponsibles) {
			totalNumOfResponsibleItems = countItems(responsibleItems, null,null, false);
		}
		if (showManager) {
			totalNumOfManagerItems = countItems(managerItems, null,null, false);
		}
		if (showOriginator) {
			totalNumOfReporterItems = countItems(authorItems, null,null, false);
		}
		if (showWatchers) {
			totalNumOfWatcherItems = countWatcherItems(watcherItems, null, null, false);
		}
		int totalSize=0;
		if(responsibleItems!=null){
			totalSize+=responsibleItems.size();
			responsibleItems.clear();
			responsibleItems=null;
		}
		if(managerItems!=null){
			totalSize+=managerItems.size();
			managerItems.clear();
			managerItems=null;
		}
		if(authorItems!=null){
			totalSize+=authorItems.size();
			authorItems.clear();
			authorItems=null;
		}
		if(watcherItems!=null){
			totalSize+=watcherItems.size();
			watcherItems.clear();
			watcherItems=null;
		}
	}
	
	/**
	 * Create project summary for project/release specific cockpit
	 * @param myItems
	 * @param projectOrReleaseID
	 * @param filterID
	 * @return
	 */
	private MyItemsProjSummaryBean createProjSummaryBean(List<ReportBean> myItems, Integer projectOrReleaseID, Integer filterID){
		MyItemsProjSummaryBean projSummaryBean = new MyItemsProjSummaryBean();
		projSummaryBean.setFilterID(filterID);
		projSummaryBean.setProjId(projectOrReleaseID);
		projSummaryBean.setNumItems(countLate(myItems, false));
		projSummaryBean.setNumItemsOverdue(countLate(myItems, true));
		return projSummaryBean;
	}
	
	/**
	 * Create project summary for general cockpit
	 * @param myItems
	 * @param projectID
	 * @param projecLabel
	 * @param projDescendants
	 * @param personID
	 * @param filterID
	 * @return
	 */
	private MyItemsProjSummaryBean createProjSummaryBean(List<ReportBean> myItems, Integer projectID,
			String projecLabel, Set<Integer> projDescendants, Integer personID, Integer filterID) {
		MyItemsProjSummaryBean projSummaryBean = new MyItemsProjSummaryBean();
		projSummaryBean.setFilterID(filterID);
		projSummaryBean.setProjName(projecLabel);
		projSummaryBean.setProjId(projectID);
		projSummaryBean.setNumItems(countItems(myItems, projectID, projDescendants, false));
		projSummaryBean.setNumItemsOverdue(countItems(myItems, projectID, projDescendants, true));
		return projSummaryBean;
	}

	/**
	 * Create project summary for general cockpit watcher part
	 * @param myItems
	 * @param projectID
	 * @param projecLabel
	 * @param projDescendants
	 * @param personID
	 * @param filterID
	 * @return
	 */
	private MyItemsProjSummaryBean createProjSummaryWatcherBean(List<ReportBean> myItems, Integer projectID,
			String projecLabel, Set<Integer> projDescendants, Integer personID, Integer filterID){
		MyItemsProjSummaryBean projSummaryBean = new MyItemsProjSummaryBean();
		projSummaryBean.setFilterID(filterID);
		projSummaryBean.setProjName(projecLabel);
		projSummaryBean.setProjId(projectID);
		projSummaryBean.setNumItems(countWatcherItems(myItems, projectID, projDescendants, false));
		projSummaryBean.setNumItemsOverdue(countWatcherItems(myItems, projectID, projDescendants, true));
		return projSummaryBean;
	}
	
	/**
	 * Get watcher items
	 * @param personID
	 * @param locale
	 * @param projectID
	 * @param releaseID
	 * @return
	 */
	
	public List<MyItemsProjSummaryBean> getProjResponsibleItemsSum() {
		return projResponsibleItemsSum;
	}

	public List<MyItemsProjSummaryBean> getProjManagerItemsSum() {
		return projManagerItemsSum;
	}
	public int getTotalNumOfResponsibleItems() {
		return totalNumOfResponsibleItems;
	}

	public int getTotalNumOfManagerItems() {
		return totalNumOfManagerItems;
	}
	
	/**
	 * Count the late items from browse project/release
	 * @param resultList
	 * @param lateOnly
	 * @return
	 */
	private int countLate(List<ReportBean> resultList, boolean lateOnly) {
		int count = 0;
		if (resultList!=null) {
			for (ReportBean reportBean : resultList) {
				if (doesMatchLate(reportBean, lateOnly)) {
					count++;
				}
			}
		}
		return count;
	}
	
	/**
	 * Count the items for general cockpit
	 * @param resultList
	 * @param projectID
	 * @param projDescendants
	 * @param lateOnly
	 * @return
	 */
	private int countItems(List<ReportBean> resultList, Integer projectID, Set<Integer> projDescendants, boolean lateOnly) {
		int count = 0;
		if (resultList!=null) {
			for (ReportBean reportBean : resultList) {
				if (doesMatchProj(reportBean, projectID, projDescendants)
						&& doesMatchLate(reportBean, lateOnly)) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * 
	 * @param resultList
	 * @param projectID
	 * @param projDescendants
	 * @param lateOnly
	 * @return
	 */
	private int countWatcherItems(List<ReportBean> resultList, Integer projectID, Set<Integer> projDescendants,
			boolean lateOnly) {
		int count = 0;
		if (resultList!=null) {
			for (ReportBean reportBean : resultList) {
				if (doesMatchProj(reportBean, projectID, projDescendants)
						&& doesMatchLate(reportBean, lateOnly))
					count++;
				}
			}
		return count;
	}
	
	/**
	 * @param reportBean
	 * @param projKey
	 * @return
	 */
	private boolean doesMatchProj(ReportBean reportBean, Integer projKey, Set<Integer> projDescendants) {
		if(projKey==null){
			return true;
		}
		Integer workItemProjectID=reportBean.getWorkItemBean().getProjectID();
		return workItemProjectID.equals(projKey) || (projDescendants!=null && projDescendants.contains(workItemProjectID));
	}

	/**
	 * @param reportBean
	 * @param lateOnly
	 * @return
	 */
	private boolean doesMatchLate(ReportBean reportBean, boolean lateOnly) {
		return !lateOnly || reportBean.isDateConflict() ||
				reportBean.isBudgetOrPlanConflict();
	}
		
	public static String encodeJSON_ProjectSummary(MyItemsProjSummaryBean projectSummary){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendIntegerValue(sb, "queryID", projectSummary.getFilterID());
		JSONUtility.appendStringValue(sb, "projName", projectSummary.getProjName());
		JSONUtility.appendIntegerValue(sb, "projId", projectSummary.getProjId());
		JSONUtility.appendIntegerValue(sb, "numItems", projectSummary.getNumItems());
		JSONUtility.appendIntegerValue(sb, "numItemsOverdue", projectSummary.getNumItemsOverdue());
		JSONUtility.appendDoubleValue(sb, "dlate", projectSummary.getDlate());
		JSONUtility.appendIntegerValue(sb, "greenWidth", projectSummary.getGreenWidth());
		JSONUtility.appendIntegerValue(sb, "redWidth", projectSummary.getRedWidth(),true);
		sb.append("}");
		return sb.toString();
	}
	public static String encodeJSON_ProjectSummaryList(List<MyItemsProjSummaryBean> list){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(list!=null){
			for (int i = 0; i < list.size(); i++) {
				MyItemsProjSummaryBean projectSummary=list.get(i);
				if(i>0){
					sb.append(",");
				}
				sb.append(encodeJSON_ProjectSummary(projectSummary));
			}
		}
		sb.append("]");
		return sb.toString();
	}

	public List<MyItemsProjSummaryBean> getProjReporterItemsSum() {
		return projReporterItemsSum;
	}
	public int getTotalNumOfReporterItems() {
		return totalNumOfReporterItems;
	}
	public List<MyItemsProjSummaryBean> getProjWatcherItemsSum() {
		return projWatcherItemsSum;
	}
	public int getTotalNumOfWatcherItems() {
		return totalNumOfWatcherItems;
	}
}
