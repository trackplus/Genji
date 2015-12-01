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

package com.aurel.track.itemNavigator.viewPlugin;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.admin.user.userLevel.UserLevelBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.itemNavigator.QueryContext;
import com.aurel.track.itemNavigator.layout.NavigatorLayoutBL;
import com.aurel.track.plugin.IssueListViewDescriptor;
import com.aurel.track.plugin.PluginManager;
import com.aurel.track.prop.ApplicationBean;

/**
 * Logic for view descriptors
 * @author Tamas
 *
 */
public class ViewDescriptorBL {
	static private Logger LOGGER = LogManager.getLogger(ViewDescriptorBL.class);
	/**
	 * Static cache for item list descriptors 
	 */
	public static List<IssueListViewDescriptor> issueListViewDescriptors=null;
	public static Map<String,IssueListViewDescriptor> issueListViewDescriptorsMap=null;
		
	/**
	 * Gets the views allowed for person
	 * @param personBean
	 * @return
	 */
	public static List<IssueListViewDescriptor> getViewDescriptors(TPersonBean personBean) {
		List<IssueListViewDescriptor> allowedDescriptors = new LinkedList<IssueListViewDescriptor>();
		List<IssueListViewDescriptor> allDescriptors=getIssueListViewDescriptors();
		IssueListViewDescriptor simpleView = null;
		Map<Integer, Boolean> userLevelMap = personBean.getUserLevelMap();
		for (IssueListViewDescriptor issueListViewDescriptor : allDescriptors) {
			String id=issueListViewDescriptor.getId();
			if (IssueListViewDescriptor.SIMPLE_VIEW.equals(id)){
				simpleView=issueListViewDescriptor;
			}
			if (IssueListViewDescriptor.GANTT.equals(id)) {
				if (ApplicationBean.getInstance().getAppType()==ApplicationBean.APPTYPE_FULL) { 
					Map<String, Boolean> licensedFeaturesMap = personBean.getLicensedFeaturesMap();
					if (licensedFeaturesMap!=null) {
						Boolean gantt = licensedFeaturesMap.get("gantt");
						if (gantt!=null && gantt.booleanValue()) {
							allowedDescriptors.add(issueListViewDescriptor);
						}
					}
				}
			} else {
				if (hasIssueListViewAccess(userLevelMap, id, personBean)) {
					allowedDescriptors.add(issueListViewDescriptor);
				}
			}
		}
		if(allowedDescriptors.isEmpty()){
			//no right for any view, use simple view
			allowedDescriptors.add(simpleView);
		}
		return allowedDescriptors;
	}
	
	/**
	 * Whether the view is allowed according to the user levels 
	 * @param userLevelMap
	 * @param id
	 * @param personBean
	 * @return
	 */
	private static boolean hasIssueListViewAccess(Map<Integer, Boolean> userLevelMap, String id, TPersonBean personBean){
		Boolean result=Boolean.TRUE;
		if(IssueListViewDescriptor.COMPLEX_VIEW.equals(id)){
			result=userLevelMap.get(UserLevelBL.USER_LEVEL_ACTION_IDS.ITEM_NAVIGATOR_TREE_GRID_VIEW);
		}else if(IssueListViewDescriptor.WBS.equals(id)){
			result=userLevelMap.get(UserLevelBL.USER_LEVEL_ACTION_IDS.ITEM_NAVIGATOR_HIERARCHICAL_VIEW);
		}else if(IssueListViewDescriptor.SIMPLE_VIEW.equals(id)){
			result=userLevelMap.get(UserLevelBL.USER_LEVEL_ACTION_IDS.ITEM_NAVIGATOR_FLAT_GRID_VIEW);
		}else /*if(IssueListViewDescriptor.GANTT.equals(id)) {	
			if( (personBean.getLoginName().equals(TPersonBean.ADMIN_USER))) {
				result=userLevelMap.get(USER_LEVEL_IDS.ITEM_NAVIGATOR_GANTT_VIEW) ;
			}else {
				// The following needs to be handled by a plugin
				// result=(appBean.getGantt() >=1) && userLevelMap.get(UserLevels.ITEM_NAVIGATOR_HAS_GANTT_VIEW) ;
				result=userLevelMap.get(UserLevelBL.USER_LEVEL_ACTION_IDS.ITEM_NAVIGATOR_GANTT_VIEW) ;
			}
			
		}else*/ if(IssueListViewDescriptor.CARD.equals(id)) {
			result=userLevelMap.get(UserLevelBL.USER_LEVEL_ACTION_IDS.ITEM_NAVIGATOR_AGILE_VIEW);
		}
		return result==null?false:result.booleanValue();
	}

	public synchronized static List<IssueListViewDescriptor> getIssueListViewDescriptors(){
		if(issueListViewDescriptors==null){
			issueListViewDescriptors=PluginManager.getInstance().getIssueListViewDescriptors();
		}
		return issueListViewDescriptors;
	}

	public static IssueListViewDescriptor getDescriptor(String descriptorId){
		if(descriptorId==null){
			return null;
		}
		if(issueListViewDescriptorsMap==null){
			getIssueListViewDescriptors();
			issueListViewDescriptorsMap=new HashMap<String, IssueListViewDescriptor>();
			for (IssueListViewDescriptor descriptor : issueListViewDescriptors) {
				issueListViewDescriptorsMap.put(descriptor.getId(),descriptor);
			}
		}
		return issueListViewDescriptorsMap.get(descriptorId);
	}

	/**
	 * Gets the IssueListViewDescriptor in the person and filter context 
	 * @param personBean
	 * @param descriptors
	 * @param isMobile
	 * @param queryContext
	 * @return
	 */
	public static IssueListViewDescriptor getDescriptor(TPersonBean personBean, List<IssueListViewDescriptor> descriptors, boolean isMobile, QueryContext queryContext) {
		if (isMobile) {
			IssueListViewDescriptor descriptor = getDescriptor(IssueListViewDescriptor.WBS);
			LOGGER.debug("WBS view for mobile access");
			if (descriptor==null && descriptors!=null && !descriptors.isEmpty()) {
				descriptor = descriptors.get(0);
				LOGGER.debug("WBS view not available, take the first available: " + descriptor.getId());
			}
			return descriptor;
		} else {
			String viewID = NavigatorLayoutBL.getSavedItemFilterView(queryContext.getQueryID(), queryContext.getQueryType());
			boolean isFilterView = false;
			if (viewID==null || "".equals(viewID)) {
				viewID = personBean.getLastSelectedView();
				LOGGER.debug("Get the last used view by person: " + personBean.getLabel() + ": " +  viewID);
			} else {
				isFilterView = true;
				LOGGER.debug("Get the view configured in filter: " + viewID);
			}
			if (viewID!=null) {
				if (descriptors!=null) {
					for (IssueListViewDescriptor issueListViewDescriptor : descriptors) {
						if (issueListViewDescriptor.getId().equals(viewID)) {
							return issueListViewDescriptor;
						}
					}
				}
			}
			if (descriptors!=null && !descriptors.isEmpty()) {
				IssueListViewDescriptor descriptor = descriptors.get(0);
				LOGGER.debug("Take the first available: " + descriptor.getId());
				if (!isFilterView) {
					personBean.setLastSelectedView(descriptor.getId());
					PersonBL.saveSimple(personBean);
				}
				return descriptor;
			}
		}
		return null;
	}
}
