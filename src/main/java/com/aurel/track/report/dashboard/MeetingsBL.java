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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadItemIDListItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadMeetingItems;
import com.aurel.track.admin.customize.lists.systemOption.StatusBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.project.release.ReleaseBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.item.ItemBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.SortOrderUtil;


public class MeetingsBL {
	
	public static List<MeetingTO> getMeetings(TPersonBean user,Locale locale,Integer projectID,Integer releaseID){
		List<MeetingTO> result=new ArrayList<MeetingTO>();
		//Map<Integer, TStateBean> allStates = GeneralUtils.createMapFromList(StatusBL.loadAll());
		DateTimeUtils dtu=DateTimeUtils.getInstance();	
		Integer[] projectIDs = null;
		Integer[] releaseIDs = null;
		if (releaseID!=null) {
			releaseIDs = ReleaseBL.getDescendantReleaseIDs(new Integer[] {releaseID});
		} else {
			if (projectID!=null) {
				projectIDs = ProjectBL.getDescendantProjectIDs(new Integer[] {projectID});
			}
		}
		List<TWorkItemBean> meetingBeansList = LoadMeetingItems.getAllMeetings(user.getObjectID(), projectIDs, releaseIDs);
		Collections.sort(meetingBeansList, new Comparator<TWorkItemBean>() {
			public int compare(TWorkItemBean workItemBean1, TWorkItemBean workItemBean2) {
				Date date1;
				if(workItemBean1.getStartDate()!=null){
					date1=workItemBean1.getStartDate();
				}else{
					date1=workItemBean1.getEndDate();
				}
				Date date2;
				if(workItemBean2.getStartDate()!=null){
					date2=workItemBean2.getStartDate();
				}else{
					date2=workItemBean2.getEndDate();
				}
				String title1 = workItemBean1.getSynopsis();
				String title2 = workItemBean2.getSynopsis();
				int compareResult = SortOrderUtil.compareValue(date1, date2, 1);
				if (compareResult!=0) {
					return compareResult;
				}
				return SortOrderUtil.compareValue(title1, title2, 1);
			}
		});
		
		List<Integer> meetingIDs = GeneralUtils.createIntegerListFromBeanList(meetingBeansList);
		Set<Integer> children = ItemBL.getChildHierarchy(GeneralUtils.createIntArrFromIntegerList(meetingIDs));
		int[] workItemKeys=GeneralUtils.createIntArrFromSet(children);
		List<TWorkItemBean> descendantList = LoadItemIDListItems.getWorkItemBeansByWorkItemIDs(workItemKeys, user.getObjectID(), false, false, false);
		Map<Integer, TWorkItemBean> descendantMap = GeneralUtils.createMapFromList(descendantList);
		Map<Integer, List<Integer>> parentToChildrenMap = ItemBL.getParentToChildrenMap(descendantList);
		for (int i = 0; i < meetingBeansList.size(); i++) {
			TWorkItemBean item=meetingBeansList.get(i);
			List<TWorkItemBean> descendends = new LinkedList<TWorkItemBean>();
			getDescendents(item.getObjectID(), parentToChildrenMap, descendantMap, descendends); //getAllDescendends(item.getObjectID(),false,user.getObjectID());
			int number=descendends.size();
			int resolved=countResolved(descendends/*, allStates*/);
			int opened=number-resolved;
			if(opened>0){
				MeetingTO m=new MeetingTO();
				Date d;
				if(item.getStartDate()!=null){
					d=item.getStartDate();
				}else{
					d=item.getEndDate();
				}
				m.setDate(dtu.formatGUIDate(d,locale));
				m.setObjectID(item.getObjectID());
				m.setSynopsis(item.getSynopsis());
				m.setNumber(number);
				m.setNumberOpen(opened);
				m.setNumberResolved(resolved);
				
				int widthOpen = opened * Constants.DASHBOARDBARLENGTH /number;
				int widthResolved = resolved * Constants.DASHBOARDBARLENGTH /number;
				
				m.setWidthOpen(widthOpen);
				m.setWidthResolved(widthResolved);
				
				result.add(m);
			}
		}
		return result;
	}
	
	/**
	 * Gets the descendants of an item
	 * @param parentID
	 * @param parentToChildrenMap
	 * @param descendantMap
	 * @param myDescendantsList
	 */
	private static void getDescendents(Integer parentID, Map<Integer, List<Integer>> parentToChildrenMap, Map<Integer, TWorkItemBean> descendantMap,  List<TWorkItemBean> myDescendantsList) {
		List<Integer> childIDs = parentToChildrenMap.get(parentID);
		if (childIDs!=null) {
			for (Integer childID : childIDs) {
				TWorkItemBean childWorkItemBean = descendantMap.get(childID);
				if (childWorkItemBean!=null) {
					myDescendantsList.add(childWorkItemBean);
					getDescendents(childID, parentToChildrenMap, descendantMap, myDescendantsList);
				}
			}
		}
	}
	
	/**
	 * TODO change with this method to avoid accessing the DB for each meeting
	 * but this method does not work yet correctly
	 * @param meetingID
	 * @param meetingsAndDescendentsMap
	 * @return
	 */
	/*private static List<TWorkItemBean> getDescendants(Integer meetingID, Map<Integer, TWorkItemBean> meetingsAndDescendentsMap) {
		List<TWorkItemBean> descendantsList = new ArrayList<TWorkItemBean>();
		TWorkItemBean workItemBean = meetingsAndDescendentsMap.get(meetingID);
		Set<Integer> descendentsSet = new HashSet<Integer>();
		if (workItemBean!=null) {
			descendentsSet.add(workItemBean.getObjectID());
			descendantsList.add(workItemBean);
			int i=0;
			while (i<descendantsList.size()) {
				workItemBean = descendantsList.get(i);
				Integer superiorID = workItemBean.getObjectID();
				for (Iterator itr = meetingsAndDescendentsMap.keySet().iterator(); itr.hasNext();) {
					Integer childID = (Integer) itr.next();
					TWorkItemBean childWorkItemBean = meetingsAndDescendentsMap.get(childID);
					Integer parentID = childWorkItemBean.getSuperiorworkitem();
					if (parentID!=null && parentID.equals(superiorID)) {
						if (!descendentsSet.contains(childWorkItemBean.getObjectID())) {
							descendantsList.add(childWorkItemBean);
							descendentsSet.add(childWorkItemBean.getObjectID());
						}
					}
				}
				i++;
			}
			descendantsList.remove(0);
		}		
		return descendantsList;
		
	}*/
	
	private static int countResolved(List<TWorkItemBean> items/*, Map<Integer, TStateBean> allStates*/){
		int count=0;
		for (Iterator<TWorkItemBean> iterator = items.iterator(); iterator.hasNext();) {
			TWorkItemBean tWorkItemBean = (TWorkItemBean) iterator.next();
			Integer stateID=tWorkItemBean.getStateID();
			TStateBean stateBean=LookupContainer.getStatusBean(stateID);
			if(stateBean!=null && stateBean.getStateflag().intValue()==TStateBean.STATEFLAGS.CLOSED){
				count++;
			}
		}
		return count;
	}
	
	public static List<TWorkItemBean> getAllDescendends(Integer issueID,boolean includeSelf, Integer personID){
		int[] baseWorkItemIDsArr=new int[]{issueID.intValue()};
		Set<Integer> allDescInds=ItemBL.getChildHierarchy(baseWorkItemIDsArr);
		if(includeSelf){
			allDescInds.add(issueID);
		}
		int[] workItemKeys=GeneralUtils.createIntArrFromSet(allDescInds);
		return LoadItemIDListItems.getWorkItemBeansByWorkItemIDs(workItemKeys, personID, false, false, false);
	}
	public static List<TWorkItemBean> filterItems(List<TWorkItemBean> items, boolean resolved){
		Set<Integer> ids=new TreeSet<Integer>();
		Map<Integer, TStateBean> allStates=GeneralUtils.createMapFromList(StatusBL.loadAll());
		for (Iterator iterator = items.iterator(); iterator.hasNext();) {
			TWorkItemBean tWorkItemBean = (TWorkItemBean) iterator.next();
			Integer stateID=tWorkItemBean.getStateID();
			TStateBean stateBean=allStates.get(stateID);
			boolean closedIssue=(stateBean!=null&&stateBean.getStateflag().intValue()==TStateBean.STATEFLAGS.CLOSED);
			if((resolved&&closedIssue)||(!resolved&&!closedIssue)){
				ids.add(tWorkItemBean.getObjectID());
			}
		}
		List<TWorkItemBean> result=new ArrayList<TWorkItemBean>();
		for(TWorkItemBean item:items){
			if(ids.contains(item.getObjectID())){
				result.add(item);
			}
		}
		return result;
	}
	
	public static String encodeJSONMeetings(List<MeetingTO> list){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(list!=null){
			for (Iterator<MeetingTO> iterator = list.iterator(); iterator.hasNext();) {
				MeetingTO meetingTO = iterator.next();
				sb.append(encodeJSONMeeting(meetingTO));
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");

		return sb.toString();
	}
	public static String encodeJSONMeeting(MeetingTO meetingTO){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendStringValue(sb,"date",meetingTO.getDate());
		JSONUtility.appendStringValue(sb,"synopsis",meetingTO.getSynopsis());
		JSONUtility.appendIntegerValue(sb,"objectID",meetingTO.getObjectID());
		JSONUtility.appendIntegerValue(sb,"number",meetingTO.getNumber());
		JSONUtility.appendIntegerValue(sb,"numberResolved",meetingTO.getNumberResolved());
		JSONUtility.appendIntegerValue(sb,"numberOpen",meetingTO.getNumberOpen());
		JSONUtility.appendIntegerValue(sb,"widthResolved",meetingTO.getWidthResolved());
		JSONUtility.appendIntegerValue(sb,"widthOpen",meetingTO.getWidthOpen(),true);
		sb.append("}");
		return sb.toString();
	}
}
