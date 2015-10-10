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
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.PredefinedQueryBL;
import com.aurel.track.admin.customize.category.filter.execute.FilterExecuterFacade;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.item.consInf.ConsInfBL;
import com.aurel.track.item.consInf.RaciRole;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeanComparator;

/**
 * Shows all issues where I am informed or consulted. Provide ability to deregister as a watcher 
 * @author Adi
 *
 */
public class MyWatch extends BasePluginDashboardView{

	@Override
	protected String encodeJSONExtraData(Integer dashboardID,Map<String, Object> session, Map<String, String> parameters,
										 Integer projectID, Integer releaseID,Map<String,String> ajaxParams) throws TooManyItemsToLoadException{
		TPersonBean user = (TPersonBean) session.get(Constants.USER_KEY);
		StringBuilder sb=new StringBuilder();
		if(ajaxParams!=null){
			String itemIDStr=ajaxParams.get("itemID");
			Integer itemID=null;
			if(itemIDStr!=null){
				try{
					itemID=Integer.valueOf(itemIDStr);
				}catch (Exception e) {
				}
			}
			if(itemID!=null){
				ConsInfBL.deleteByWorkItemAndPersonsAndRaciRole(itemID, new Integer[] {user.getObjectID()}, RaciRole.INFORMANT);
				ConsInfBL.deleteByWorkItemAndPersonsAndRaciRole(itemID, new Integer[] {user.getObjectID()}, RaciRole.CONSULTANT);
			}
		}
		Locale locale=(Locale) session.get(Constants.LOCALE_KEY);
		List<ReportBean> items;
		if(projectID!=null){
			items=calculateProjectItems(user, locale, projectID, releaseID);
		}else{
			items=calculateItems(user, locale);
		}
		JSONUtility.appendJSONValue(sb,"items",MyWatchJSON.encodeReportBeanList(items, user.getObjectID()));
		return sb.toString();
	}
	private static List<ReportBean> calculateProjectItems(TPersonBean user, Locale locale,Integer projectID,Integer releaseID) throws TooManyItemsToLoadException {
		//TProjectBean prjBean = ProjectBL.loadByPrimaryKey(projectID);
		Integer objectID=null;
		Integer entityFlag=null;
		if(projectID!=null){
			if(releaseID!=null){
				entityFlag=SystemFields.RELEASESCHEDULED;
				objectID=releaseID;
			}else{
				entityFlag=SystemFields.PROJECT;
				objectID=projectID;
			}
		}
		Collection items = getIssues( user, locale,objectID,entityFlag);
		Iterator itemIter = items.iterator();
		List<ReportBean> reportList4Project=new ArrayList<ReportBean>();
		while (itemIter.hasNext()) {
			ReportBean rb = (ReportBean) itemIter.next();
			//TWorkItemBean item =rb.getWorkItemBean();
			Integer currenProjectID=rb.getWorkItemBean().getProjectID();
			if(currenProjectID.intValue()==projectID.intValue()){
				if(releaseID==null||
						(releaseID!=null&&rb.getWorkItemBean().getReleaseScheduledID()!=null&&
								rb.getWorkItemBean().getReleaseScheduledID().intValue()==releaseID.intValue())){
					reportList4Project.add(rb);	
				}
			}
		}
		// sort reportBeans by comparator
		if (reportList4Project != null) {
			Collections.sort(reportList4Project,new ReportBeanComparator());
		}

		return reportList4Project;
	}
	private List<ReportBean> calculateItems(TPersonBean user, Locale locale) throws TooManyItemsToLoadException {
		List<ReportBean> issues = getIssues(user, locale,null,null);
		ReportBeanComparator comp = new ReportBeanComparator(true, SystemFields.INTEGER_ISSUENO, null, true);
		// sort reportBeans by comparator
		if (issues != null) {
			Collections.sort(issues,comp);
		}
		return issues;
	}
	private static List<ReportBean> getIssues(TPersonBean user, Locale locale,Integer objectID,Integer entityType) throws TooManyItemsToLoadException {
		return FilterExecuterFacade.getSavedFilterReportBeanList(PredefinedQueryBL.PREDEFINED_QUERY.WATCHER_ITEMS, locale, user, false,
				new LinkedList<ErrorData>(), objectID, entityType, false, true, false, false, false, false, false, false, false);
	}
}
