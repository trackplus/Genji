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


package com.aurel.track.report.dashboard.grouping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.Constants;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.report.dashboard.ElementWrapper;

/**
 * An abstract implementation for dashbord that use summary
 * for different kind of grouping by e.g: group by priority, user, etc
 * 
 * @author Adrian Bojani
 *
 */
public abstract class AbstractGrouping{
	
	//private Map<Integer, TStateBean> statusBeansMap;
	protected int projectIndex;
	
	//Runtime link constants
	public static interface LINK_PARAMETERS {
		public static String GROUPBYFIELDID = "groupByField";
		public static String GROUPBYFIELDTYPE = "groupByFieldType";
	}
	
	/**
	 * Constructor
	 * @param statusBeansMap the list<TStateBean> of all status
	 */
	public AbstractGrouping(/*Map<Integer, TStateBean> statusBeansMap,*/ int projectIndex){
		//this.statusBeansMap = statusBeansMap;
		this.projectIndex = projectIndex;
	}
	
	/**
	 * Grouping the items for each labelBean from labelBean list
	 * E.g grouping by priority
	 * 
	 * @param workItemBeansList -the list of items that are want to group
	 * @param locale -can be null if this locale is present than the labelBeans must be an List<ILocalizedLabelBean>
	 * @return a List<ElementWrapper> with items grouped 
	 */
	public final List<ElementWrapper> groupItems(List<TWorkItemBean> workItemBeansList, Locale locale) {
		List<ElementWrapper> result = new ArrayList<ElementWrapper>();
		List<ILabelBean> groupByFieldBeansList = groupByFieldList(locale);
		if (workItemBeansList==null) {
			workItemBeansList = new ArrayList<TWorkItemBean>();
		}
		int totalSize = workItemBeansList.size();
		Map<Integer, Integer> totalMap = new HashMap<Integer, Integer>();
		Map<Integer, Integer> lateMap = new HashMap<Integer, Integer>();
		for (TWorkItemBean workItemBean : workItemBeansList) {
			Integer groupByID = getWorkItemFieldValue(workItemBean);
			//count the items
			Integer count = totalMap.get(groupByID);
			if (count == null) {
				// Create first entry for this priority
				count = Integer.valueOf(1);
			} else {
				// Add this entry to existing priority
				count = Integer.valueOf(count.intValue() + 1);
			}
			totalMap.put(groupByID, count);
			
			//count the late items
			boolean late = isLate(workItemBean);
			Integer countLate = lateMap.get(groupByID);
			if (countLate == null) {
				// Create first entry for this status
				if (late) {
					countLate = Integer.valueOf(1);
				}
				else {
					countLate = Integer.valueOf(0);
				}
			} else {
				// Add this entry to existing status
				if (late) {
					countLate = Integer.valueOf(countLate.intValue() + 1);
				}
			}
			lateMap.put(groupByID, countLate);
			
			// Add this issue to the list for this labelBean
			/*String keyInTheListMap=formatKeyInTheListMap(labelBeanID);
			List issueListForThisGrouping = (List) theListMap.get(keyInTheListMap);
			issueListForThisGrouping.add(twi.getObjectID());
			theListMap.put(keyInTheListMap, issueListForThisGrouping);*/
		}
		
		Iterator<ILabelBean> iterator = groupByFieldBeansList.iterator();
		while (iterator.hasNext()) {
			ILabelBean groupByBean = iterator.next();
			Integer groupByID=groupByBean.getObjectID();
			String localizedLabel =groupByBean.getLabel();
			/*if(locale!=null && groupByBean instanceof ILocalizedLabelBean){// localize the label
				localizedLabel=LocalizeUtil.localizeDropDownEntry((ILocalizedLabelBean)groupByBean,locale);
			}*/
			Integer count = totalMap.get(groupByID);
			Integer countLate = lateMap.get(groupByID);
			if (count != null) {
				if (countLate == null) {
					countLate = Integer.valueOf(0);
				} 
				//make a copy of the link parameters map for each link  
				int width = (int) Math.round((count.doubleValue()-countLate.doubleValue()) / totalSize
						* Constants.DASHBOARDBARLENGTH.doubleValue());
				int widthLate = (int) Math.round(countLate.doubleValue() / totalSize
						* Constants.DASHBOARDBARLENGTH.doubleValue());				
				int percent = (int) Math.round((count.doubleValue() / totalSize) * 100);
				int percentLate = (int) Math.round(((count.doubleValue()-countLate.doubleValue()) / totalSize) * 100);
				
				ElementWrapper elementWrapper = new ElementWrapper(localizedLabel, count.intValue(),
						width, percent, widthLate, percentLate);
				elementWrapper.setGroupByField(groupByID);
				result.add(elementWrapper);
			}
		}
		return result;
	}
	
	/**
	 * Gets the workItemIDs array for the link
	 * @param workItemBeansList
	 * @param groupByFieldID
	 * @return
	 */
	public final List<TWorkItemBean> getWorkItemIDsForLink(List<TWorkItemBean> workItemBeansList, Integer groupByFieldID) {
		List<TWorkItemBean> result = new ArrayList<TWorkItemBean>();
		Iterator<TWorkItemBean> iterator = workItemBeansList.iterator();
		while (iterator.hasNext()) {
			TWorkItemBean workItemBean = iterator.next();
			Integer currentGroupByFieldID = getWorkItemFieldValue(workItemBean);
			if (groupByFieldID.equals(currentGroupByFieldID)) {
				result.add(workItemBean);
			}
		}
		return workItemBeansList;
	}
	
	/**
	 * Gets the workItemIDs array for the link
	 * @param workItemBeansList
	 * @param groupByFieldID
	 * @return
	 */
	public final List<TWorkItemBean> filtertWorkItems(List<TWorkItemBean> workItemBeansList, Integer groupByFieldID) {
		List<TWorkItemBean> result = new ArrayList<TWorkItemBean>();
		Iterator<TWorkItemBean> iterator = workItemBeansList.iterator();
		while (iterator.hasNext()) {
			TWorkItemBean workItemBean = iterator.next();
			Integer currentGroupByFieldID = getWorkItemFieldValue(workItemBean);
			if (groupByFieldID.equals(currentGroupByFieldID)) {
				result.add(workItemBean);
			}
		}
		return result;
	}
	
	
	/**
	 * Get the ID of the labelBean from the work item bean
	 * @param workItemBean
	 * @return
	 */
	protected abstract Integer getWorkItemFieldValue(TWorkItemBean workItemBean);
	
	/**
	 * Contributes to the uniqueness of the link and the variable 
	 * which stores the parameters map in the session
	 * @param labelBeanID
	 * @return
	 */
	protected abstract String formatKey(Integer labelBeanID);
	
	protected abstract List<ILabelBean> groupByFieldList(Locale locale);
	

	protected boolean isLate(TWorkItemBean workItemBean) {
		TStateBean statusBean = LookupContainer.getStatusBean(workItemBean.getStateID());
		if (statusBean!=null) {
			int bottomUpDateDueFlag = workItemBean.calculateBottomUpDueDateOnPlan(statusBean.getStateflag(), null);
			int topDownDateDueFlag = workItemBean.calculateTopDownDueDateOnPlan(statusBean.getStateflag(), null);
			return TWorkItemBean.isDateConflict(bottomUpDateDueFlag) || TWorkItemBean.isDateConflict(topDownDateDueFlag);
		} else {
			return false;
		}
	}
}
