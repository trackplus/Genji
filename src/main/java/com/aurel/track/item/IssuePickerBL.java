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


package com.aurel.track.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.category.filter.execute.TreeFilterExecuterFacade;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.FilterUpperConfigUtil;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadTreeFilterItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;

/**
 *
 */
public class IssuePickerBL {
	private static final Logger LOGGER = LogManager.getLogger(IssuePickerBL.class);
	public static  List<TWorkItemBean> searchByWorkItemID(TPersonBean personBean,  Integer workItemID){
		Integer userID=personBean.getObjectID();
		List<TWorkItemBean> issues=null;
		issues=new ArrayList<TWorkItemBean>();
		TWorkItemBean workItemBean=null;
		try{
			workItemBean=ItemBL.loadWorkItem(workItemID);
		}catch (ItemLoaderException ex){
			LOGGER.warn("Item not found for key:"+workItemID);
		}
		boolean allowToRead;
		if(workItemBean!=null){
			allowToRead=AccessBeans.isAllowedToRead(workItemBean, userID);
			if(!allowToRead){
				LOGGER.warn("User:"+userID+" isAllowedToRead issue:"+workItemID);
				workItemBean=null;
			}else{
				issues.add(workItemBean);
			}
		}
		return issues;
	}
	public static  List<TWorkItemBean> searchByProjectSpecificID(TPersonBean personBean,  String projectSpecificID){
		Integer userID=personBean.getObjectID();
		List<TWorkItemBean> issues=null;
		issues=new ArrayList<TWorkItemBean>();
		TWorkItemBean workItemBean=null;
		try{
			workItemBean=ItemBL.loadWorkItemByProjectSpecificID(personBean.getObjectID(),projectSpecificID);
		}catch (ItemLoaderException ex){
			LOGGER.warn("Item not found for key:"+projectSpecificID);
		}
		boolean allowToRead;
		if(workItemBean!=null){
			allowToRead=AccessBeans.isAllowedToRead(workItemBean, userID);
			if(!allowToRead){
				LOGGER.warn("User:"+userID+" isAllowedToRead issue:"+projectSpecificID);
				workItemBean=null;
			}else{
				issues.add(workItemBean);
			}
		}
		return issues;
	}
	public static List<TWorkItemBean> search(TPersonBean personBean, Locale locale, Integer workItemID, boolean parent,
			Integer	projectID, Integer filterID, String searchIssueKey, boolean  useProjectSpecificID,boolean includeClosed){
		List<TWorkItemBean> issues=null;
		if (searchIssueKey!=null && searchIssueKey.trim().length()>0) {
			if (useProjectSpecificID){
				issues=searchByProjectSpecificID(personBean,searchIssueKey.trim());
			} else {
				Integer searchWorkItemID=null;
				try{
					searchWorkItemID=Integer.parseInt(searchIssueKey.trim());
				}catch (Exception ex){
					LOGGER.error("Invalid issue number:"+searchIssueKey);
				}
				if(searchWorkItemID!=null){
					issues=searchByWorkItemID(personBean,searchWorkItemID);
				}
			}
		} else {
			if (projectID!=null) {
				FilterUpperTO filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(projectID, true, true, includeClosed);
				try {
					issues = LoadTreeFilterItems.getTreeFilterWorkItemBeans(filterUpperTO, personBean, locale, false);
				} catch (TooManyItemsToLoadException e) {
					LOGGER.info("Number of items to load " + e.getItemCount());
				}
			} else {
				if (filterID!=null){
					try {
						issues = TreeFilterExecuterFacade.getSavedFilterWorkItemBeans(filterID, locale, personBean, new LinkedList<ErrorData>(), false);
					} catch (TooManyItemsToLoadException e) {
						LOGGER.info("Number of items to load " + e.getItemCount());
					}
				}
			}
		}
		if (workItemID!=null && issues!=null){
			if(parent){
				issues= removeRelatedParent(issues, workItemID);
			}else{
				issues= removeIssue(issues, workItemID);
			}
		}
		return issues;
	}
	private static List<TWorkItemBean> removeIssue(List<TWorkItemBean> issues, Integer workItemID){
		TWorkItemBean workItemBean=null;
		for (int i=0;i<issues.size();i++){
			workItemBean=issues.get(i);
			if(workItemBean.getObjectID().equals(workItemID)){
				issues.remove(i);
				break;
			}
		}
		return issues;
	}
	private static List<TWorkItemBean> removeRelatedParent(List<TWorkItemBean> issues, Integer parentID){
		List<TWorkItemBean> result=new ArrayList<TWorkItemBean>();
		Map<Integer,Integer> parentMap=initParentMap(issues);
		Set<Integer> issuesToRemove=new HashSet<Integer>();
		issuesToRemove.add(parentID);
		Integer myParent=parentMap.get(parentID);
		if(myParent!=null){
			issuesToRemove.add(myParent);
		}
		Integer workItemID;
		for(TWorkItemBean workItemBean:issues){
			workItemID=workItemBean.getObjectID();
			if(isDescendant(parentMap,workItemID,parentID,new HashSet<Integer>())){
				issuesToRemove.add(workItemID);
			}
		}
		for(TWorkItemBean workItemBean:issues){
			if(!issuesToRemove.contains( workItemBean.getObjectID())){
				result.add(workItemBean);
			}
		}
		return result;
	}
	private static Map<Integer,Integer> initParentMap(List<TWorkItemBean> issues){
		Map<Integer,Integer> map=new HashMap<Integer, Integer>();
		for(TWorkItemBean workItemBean:issues){
			if(workItemBean.getSuperiorworkitem()!=null){
				map.put(workItemBean.getObjectID(),workItemBean.getSuperiorworkitem());
			}
		}
		return map;
	}
	private static boolean isDescendant(Map<Integer,Integer> parentMap, Integer workItemID,Integer parentID,Set<Integer> checkSet){
		Integer myParent=parentMap.get(workItemID);
		if(checkSet.contains(myParent)){
			LOGGER.error("Recursive parent child");
			return false;
		}
		if(myParent==null){
			return false;
		}
		if(myParent.equals(parentID)){
			return  true;
		}
		checkSet.add(workItemID);
		return isDescendant(parentMap,myParent,parentID,checkSet);
	}
}
