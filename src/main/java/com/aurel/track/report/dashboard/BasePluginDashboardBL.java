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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.category.filter.execute.loadItems.FilterUpperConfigUtil;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadTreeFilterItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.project.release.ReleaseBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.itemNavigator.ItemNavigatorBL;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.resources.ResourceBundleManager;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.StringArrayParameterUtils;

/**
 *
 */
public class BasePluginDashboardBL {
	
	private static final Logger LOGGER = LogManager.getLogger(BasePluginDashboardBL.class);
	public static final String DASHBOARD_ID="dashboardID";

	public static List<Integer> getSelectedProjectsAndReleases(String selections,Integer projectID,Integer releaseID){
		List<Integer> result=new ArrayList<Integer>();
		if(projectID!=null){
			if(releaseID!=null){
				result.add(releaseID);
			}else{
				result.add(projectID*-1);
			}
		}else{
			result=StringArrayParameterUtils.splitSelectionAsInteger(selections);
		}
		return  result;
	}
	private static Map<Integer,TProjectBean> prepareProjectMap(List<Integer> projectsAndRelease, Map<Integer, TProjectBean> readableProjectsMap) {
		Map<Integer, TProjectBean> projectMap = new HashMap<Integer, TProjectBean>();
		for (int i=0; i < projectsAndRelease.size(); ++ i) {
			Integer projectID = projectsAndRelease.get(i);
			if (projectID.intValue() < 0) {
				// project is negative, get the positive value 
				projectID = Integer.valueOf(-projectID.intValue());
				// Get all projects w/o releases
				TProjectBean projectBean = readableProjectsMap.get(projectID);
				if (projectBean!=null) { //right for project revoked, or project deleted
					projectMap.put(projectBean.getObjectID(), projectBean);
				}
			}
		}
		return projectMap;
	}
	public static List<TWorkItemBean> loadItems(List<Integer> projectsAndRelease,
			List<Integer> selectedStatesList, List<Integer> selectedPrioritiesList,
			TPersonBean personBean, Locale locale, boolean openOnly) throws TooManyItemsToLoadException{
		List<TProjectBean> readableProjectsList = ProjectBL.loadProjectsWithReadIssueRight(personBean.getObjectID());
		Map<Integer, TProjectBean> readableProjectsMap = GeneralUtils.createMapFromList(readableProjectsList);
		Map<Integer, TProjectBean> selectedProjectsMap = prepareProjectMap(projectsAndRelease, readableProjectsMap);
		//get the ID of all selected projects
		Integer[] selectedStateIDs = GeneralUtils.createIntegerArrFromCollection(selectedStatesList);
		Integer[] selectedPriorityIDs = GeneralUtils.createIntegerArrFromCollection(selectedPrioritiesList);
		List<Integer> projectIDsList = GeneralUtils.createListFromSet(selectedProjectsMap.keySet());
		FilterUpperTO filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseIDs(true,
				GeneralUtils.createIntegerArrFromCollection(projectIDsList), true, true, !openOnly);
		filterUpperTO.setSelectedStates(selectedStateIDs);
		filterUpperTO.setSelectedPriorities(selectedPriorityIDs);
		List<TWorkItemBean> workItemBeans = LoadTreeFilterItems.getTreeFilterWorkItemBeans(filterUpperTO, personBean, locale, false);
		if (workItemBeans==null) {
			workItemBeans = new LinkedList<TWorkItemBean>();
		}
		List<Integer> releaseIDsList = new ArrayList<Integer>();
		for (int i = 0; i < projectsAndRelease.size(); ++i) {
			Integer objectID = projectsAndRelease.get(i);
			if (objectID.intValue() > 0) {
				//it is release
				releaseIDsList.add(objectID);
			}
		}
		if(!releaseIDsList.isEmpty()){
			Map<Integer, TReleaseBean> releaseBeansMap = new HashMap<Integer, TReleaseBean>();
			if (releaseIDsList!=null && !releaseIDsList.isEmpty()) {
				releaseBeansMap = GeneralUtils.createMapFromList(ReleaseBL.loadByReleaseIDs(releaseIDsList));
			}
			//remove releases that already included in the project
			Iterator<Integer> it=releaseBeansMap.keySet().iterator();
			while(it.hasNext()){
				Integer releaseID=it.next();
				TReleaseBean releaseBean=releaseBeansMap.get(releaseID);
				if(projectIDsList.contains(releaseBean.getProjectID())){
					releaseIDsList.remove(releaseID);
				}
			}
			if(!releaseIDsList.isEmpty()) {
				filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseIDs(false,
						GeneralUtils.createIntegerArrFromCollection(releaseIDsList), true, true, !openOnly);
				filterUpperTO.setSelectedStates(selectedStateIDs);
				filterUpperTO.setSelectedPriorities(selectedPriorityIDs);
				List<TWorkItemBean> workItemsRelease = LoadTreeFilterItems.getTreeFilterWorkItemBeans(filterUpperTO, personBean, locale, false);
				if (workItemsRelease!=null) {
					workItemBeans.addAll(workItemsRelease);
				}
			}
		}
		return  workItemBeans;
	}
	public static Map<Integer,List<TWorkItemBean>> groupItemsByListType(List<TWorkItemBean> issues,Integer field){
		Map<Integer,List<TWorkItemBean>> map=new HashMap<Integer, List<TWorkItemBean>>();
		TWorkItemBean item;
		Integer fieldValue=null;
		List<TWorkItemBean> list=null;
		for(int i=0;i<issues.size();i++){
			item=issues.get(i);
			fieldValue=(Integer)item.getAttribute(field);
			list=map.get(fieldValue);
			if(list==null){
				list=new ArrayList<TWorkItemBean>();
			}
			list.add(item);
			map.put(fieldValue,list);
		}
		return map;
	}

	public static String getFilterTitle(String filterIdStr, Locale locale){
		Integer filterID=null;
		try{
			filterID=Integer.parseInt(filterIdStr);
		}catch (Exception ex){
			//LOGGER.error(org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(ex));
		}
		return ItemNavigatorBL.getFilterTitle(filterID, locale);
	}
	public static Integer parseInteger(Map<String,String> params,String key){
		Integer result=null;
		if(params!=null&&key!=null){
			String strValue=params.get(key);
			if(strValue!=null){
				try{
					result=Integer.valueOf(strValue);
				}catch (Exception ex){
					//ignore
				}
			}
		}
		return result;
	}
	public static boolean parseBoolean(Map<String,String> params,String key,boolean defaultValue){
		boolean result=defaultValue;
		if(params!=null&&key!=null){
			String strValue=params.get(key);
			if(strValue!=null){
				try{
					result=Boolean.valueOf(strValue);
				}catch (Exception ex){
					//ignore
				}
			}
		}
		return result;
	}
	public static boolean parseBoolean(Map<String,String> params,String key){
		return parseBoolean(params,key,false);
	}
	public static String getText(String s,Locale locale){
		return LocalizeUtil.getLocalizedText(s, locale, ResourceBundleManager.DASHBOARD_RESOURCES);
	}


	public static int parseIntegerValue(Map configParameters, String fieldName, int defaultValue) {
		int result = defaultValue;
		try {
			result = Integer.parseInt((String)configParameters.get(fieldName));
		} catch (Exception e) {
		}
		return result;
	}
}
