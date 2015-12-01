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
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import com.aurel.track.util.GeneralUtils;

/**
 * This class implements a cockpit view that display the number of issues each user is
 * currently responsible for, i.e. the current workload. The list of users is created 
 * from all users that are members of the projects of the current user, and which have
 * either "responsible" rights in these projects, or "modify any" rights. This
 * leaves out such cases where a user had these rights, but which were withdrawn in the
 * meantime, but the user is still responsible for some items.
 *
 */
public class UserLoad extends BasePluginDashboardView {
	private static final Logger LOGGER = LogManager.getLogger(UserLoad.class);
	
	//Runtime link attribute names
	protected static interface LINK_PARAMETERS {
		static String PROJECT = "projectID";
		static String ENTITYFLAG = "entityFlag";
	}

	@Override
	protected String encodeJSONExtraData(Integer dashboardID,Map<String, Object> session, Map<String, String> parameters,
										 Integer projectID, Integer releaseID,Map<String,String> ajaxParams) throws TooManyItemsToLoadException{
		TPersonBean user = (TPersonBean) session.get(Constants.USER_KEY);
		Locale locale = user.getLocale();
		StringBuilder sb=new StringBuilder();
		List<ElementWrapper> result = calculateOpenIssueCounts(user, locale, projectID, releaseID);
		JSONUtility.appendJSONValue(sb,"items",ProjectWrapperJSON.encodeElementWrapperList(result));
		return sb.toString();
	}
	
	/**
	 * 
	 * @param currentUser - the current user running the application
	 * @param projectID - optional, if present, this is on a project specific cockpit view
	 * @param releaseID - optional, if present, this is on a release specific cockpit view
	 * @return
	 * @throws TooManyItemsToLoadException 
	 */
	private List<ElementWrapper> calculateOpenIssueCounts(TPersonBean currentUser, Locale locale, Integer projectID, Integer releaseID ) throws TooManyItemsToLoadException {
		List<ElementWrapper> projectWrappersList = new ArrayList<ElementWrapper>();
		List<Integer> projectBeanIDs;
		if (projectID!=null) {
			projectBeanIDs = ProjectBL.getDescendantProjectIDsAsList(new Integer[] {projectID});
		} else {
			projectBeanIDs = GeneralUtils.createIntegerListFromBeanList(ProjectBL.loadUsedProjectsFlat(currentUser.getObjectID()));
		}
		// Get all projects for where I have issues as a manager...
		if (projectBeanIDs == null || projectBeanIDs.isEmpty()) {
			return projectWrappersList;
		}
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
		List<TWorkItemBean> workItemsList = TreeFilterExecuterFacade.getSavedFilterWorkItemBeans(
					PredefinedQueryBL.PREDEFINED_QUERY.OUTSTANDING, locale, currentUser, new LinkedList<ErrorData>(), false, projectOrReleaseID, entityFlag, null, null);
		AbstractGrouping abstractGrouping = GroupingFactory.getGrouping(SystemFields.RESPONSIBLE, 0);
		return abstractGrouping.groupItems(workItemsList,locale);
				
	}

	@Override
	public List<ReportBean> getIssues(Map<String, String> configParams, Map<String, String> filterParams, TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException{
		Integer projectID=BasePluginDashboardBL.parseInteger(filterParams,LINK_PARAMETERS.PROJECT);
		Integer entityFlag=BasePluginDashboardBL.parseInteger(filterParams,LINK_PARAMETERS.ENTITYFLAG);
		Integer groupByFieldID = BasePluginDashboardBL.parseInteger(filterParams, AbstractGrouping.LINK_PARAMETERS.GROUPBYFIELDID);
		LOGGER.debug("providerParams: projectID " + projectID +  "  entityFlag " + entityFlag +
				" groupByFieldID " + groupByFieldID);
		
		return TreeFilterExecuterFacade.getTreeFilterReportBeanListWithAdditionalCondition(PredefinedQueryBL.PREDEFINED_QUERY.OUTSTANDING, locale, personBean, projectID, entityFlag, SystemFields.RESPONSIBLE, groupByFieldID, false);
	}
}
