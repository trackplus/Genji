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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.category.filter.FilterBL;
import com.aurel.track.admin.customize.category.filter.QNode;
import com.aurel.track.admin.customize.category.filter.TreeFilterFacade;
import com.aurel.track.admin.customize.category.filter.execute.FilterExecuterFacade;
import com.aurel.track.admin.customize.category.filter.execute.TreeFilterExecuterFacade;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.FilterUpperConfigUtil;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadTreeFilterItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperFromQNodeTransformer;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.TreeFilterLoaderBL;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.customize.projectType.ProjectTypesBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.plugin.DashboardDescriptor;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.LabelValueBean;

/**
 *BL used for dashboard with datasource project/release or query
 */
public class DataSourceDashboardBL {
	private static final Logger LOGGER = LogManager.getLogger(DataSourceDashboardBL.class);

	public static interface DATASOURCE_TYPE {
		static int PROJECT_RELEASE = 1;
		static int QUERY = 2;
	}

	public static String INCLUDE_OPEN = "inlcudeOpen";
	public static String INCLUDE_CLOSED = "inlcudeClosed";

	public static interface CONFIGURATION_PARAMETERS {
		static String DATASOURCE_TYPES = "datasourceTypes";
		static String SELECTED_DATASOURCE_TYPE = "selectedDatasourceType";
		static String SELECTED_PROJECT_OR_RELEASE = "selectedProjectOrRelease";
		//static String SELECTED_PROJECT_OR_RELEASE_NAME = "selectedProjectOrReleaseName";
		static String SELECTED_QUERY = "selectedQueryID";
	}
	/**
	 * Get the list of possible datasource types
	 * @param locale
	 * @return
	 */
	private static List<IntegerStringBean> getDatasourceTypes(Locale locale, String bundleName) {
		List<IntegerStringBean> datasourceTypes = new ArrayList<IntegerStringBean>();
		datasourceTypes.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedText("statusOverTime.datasourceType.projectRelease", locale, bundleName),
				new Integer(DATASOURCE_TYPE.PROJECT_RELEASE)));
		datasourceTypes.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedText("statusOverTime.datasourceType.query", locale, bundleName),
				new Integer(DATASOURCE_TYPE.QUERY)));
		return datasourceTypes;
	}

	public static void validate(Map<String,String> parameters,Integer personID,Locale locale,List<LabelValueBean> errors){
		int datasourceType = BasePluginDashboardBL.parseIntegerValue(parameters,
				CONFIGURATION_PARAMETERS.SELECTED_DATASOURCE_TYPE, DATASOURCE_TYPE.PROJECT_RELEASE);
		Integer selectedProjectReleaseID=null;
		Integer selectedQueryID=null;
		if (datasourceType==DATASOURCE_TYPE.PROJECT_RELEASE) {
			selectedProjectReleaseID = new Integer(BasePluginDashboardBL.parseIntegerValue(parameters, CONFIGURATION_PARAMETERS.SELECTED_PROJECT_OR_RELEASE, 0));
			selectedProjectReleaseID = validateSelectedProjectOrReleaseID(selectedProjectReleaseID, personID);
			if (selectedProjectReleaseID==null) {
				String localizedErr=LocalizeUtil.getParametrizedString("common.err.required",new Object[]{BasePluginDashboardBL.getText("common.prompt.projectsReleases",locale)}, locale);
				errors.add(new LabelValueBean(localizedErr ,"params."+CONFIGURATION_PARAMETERS.SELECTED_PROJECT_OR_RELEASE));
			}
		}
		if (datasourceType==DATASOURCE_TYPE.QUERY) {
			selectedQueryID = BasePluginDashboardBL.parseInteger(parameters, CONFIGURATION_PARAMETERS.SELECTED_QUERY);
			if(selectedQueryID==null){
				String localizedErr=LocalizeUtil.getParametrizedString("common.err.required",new Object[]{BasePluginDashboardBL.getText("common.lbl.filter",locale)}, locale);
				errors.add(new LabelValueBean(localizedErr,"params."+CONFIGURATION_PARAMETERS.SELECTED_QUERY));
			}
		}
	}

	/**
	 * Append data source configuration
	 * @param sb
	 * @param dashboardDescriptor
	 * @param parameters
	 * @param user
	 * @param entityId
	 * @param entityType
	 */
	public static void appendJSONExtraDataConfig_DataSource(StringBuilder sb,DashboardDescriptor dashboardDescriptor, Map<String,String> parameters, TPersonBean user, Integer entityId, Integer entityType){
		Locale locale = user.getLocale();
		JSONUtility.appendIntegerStringBeanList(sb, CONFIGURATION_PARAMETERS.DATASOURCE_TYPES, getDatasourceTypes(locale, dashboardDescriptor.getBundleName()));
		JSONUtility.appendIntegerValue(sb,CONFIGURATION_PARAMETERS.SELECTED_DATASOURCE_TYPE,
				new Integer(BasePluginDashboardBL.parseIntegerValue(parameters,
						CONFIGURATION_PARAMETERS.SELECTED_DATASOURCE_TYPE, DATASOURCE_TYPE.PROJECT_RELEASE)));
		Integer selectedEntityID=BasePluginDashboardBL.parseInteger(parameters,CONFIGURATION_PARAMETERS.SELECTED_PROJECT_OR_RELEASE);
		JSONUtility.appendIntegerValue(sb,CONFIGURATION_PARAMETERS.SELECTED_PROJECT_OR_RELEASE,selectedEntityID);
		JSONUtility.appendIntegerValue(sb,CONFIGURATION_PARAMETERS.SELECTED_QUERY, new Integer(BasePluginDashboardBL.parseIntegerValue(parameters,
				CONFIGURATION_PARAMETERS.SELECTED_QUERY,0)));
	}

	/**
	 * Get the workItems according to the datasource type:
	 * if project_release: if selected number is negative then take it as a projectID
	 * 						if selected number is positive then take it as a releaseID
	 * if query: result of the corresponding query depending on query type and repository type
	 * @param configParameters
	 * @param personBean
	 * @param locale
	 * @return
	 * @throws TooManyItemsToLoadException
	 */
	public static List<TWorkItemBean> getWorkItemBeans(Map configParameters, TPersonBean personBean, Locale locale,Integer projectID, Integer releaseID) throws TooManyItemsToLoadException {
		return getWorkItemBeans(configParameters,personBean,locale,projectID,releaseID,null,null);
	}
	public static boolean haveDataSource(Map configParameters,TPersonBean personBean){
		int datasourceType = BasePluginDashboardBL.parseIntegerValue(configParameters,
				CONFIGURATION_PARAMETERS.SELECTED_DATASOURCE_TYPE, DATASOURCE_TYPE.PROJECT_RELEASE);
		Integer selectedProjectReleaseID=null;
		Integer selectedQueryID=null;
		if (datasourceType==DATASOURCE_TYPE.PROJECT_RELEASE) {
			selectedProjectReleaseID = new Integer(BasePluginDashboardBL.parseIntegerValue(configParameters, CONFIGURATION_PARAMETERS.SELECTED_PROJECT_OR_RELEASE, 0));
			if (selectedProjectReleaseID==null || selectedProjectReleaseID.intValue()==0) {
				return false;
			}
			selectedProjectReleaseID = validateSelectedProjectOrReleaseID(selectedProjectReleaseID, personBean.getObjectID());
			if (selectedProjectReleaseID==null) {
				return false;
			}
		}
		if (datasourceType==DATASOURCE_TYPE.QUERY) {
			selectedQueryID = BasePluginDashboardBL.parseInteger(configParameters, CONFIGURATION_PARAMETERS.SELECTED_QUERY);
		}
		return (selectedProjectReleaseID!=null||selectedQueryID!=null);
	}
	public static List<TWorkItemBean> getWorkItemBeans(Map configParameters, TPersonBean personBean, Locale locale,Integer projectID, Integer releaseID,Date startDate,Date endDate) throws TooManyItemsToLoadException {
		List<TWorkItemBean> workItemBeans = new ArrayList<TWorkItemBean>();
		int datasourceType = BasePluginDashboardBL.parseIntegerValue(configParameters,
				CONFIGURATION_PARAMETERS.SELECTED_DATASOURCE_TYPE, DATASOURCE_TYPE.PROJECT_RELEASE);
		//project or release specific dashboard
		boolean includeOpen = BasePluginDashboardBL.parseBoolean(configParameters, INCLUDE_OPEN,true);
		boolean includeClosed = BasePluginDashboardBL.parseBoolean(configParameters, INCLUDE_CLOSED,false);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("getWorkItemBeans(): datasourceType="+datasourceType+", includeOpen="+includeOpen+", includeClosed="+includeClosed);
			LOGGER.debug("getWorkItemBeans(): projectID="+projectID+", releaseID="+releaseID);
			LOGGER.debug("getWorkItemBeans(): startDate="+startDate+", endDate="+endDate);
		}
		if (projectID!=null){
			Integer projectReleaseID = null;
			boolean fromProject;
			if(releaseID!=null) {
				fromProject = false;
				projectReleaseID = releaseID;
			}else{
				fromProject = true;
				projectReleaseID = projectID;
			}
			FilterUpperTO filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(fromProject, projectReleaseID, true, includeOpen, includeClosed);
			workItemBeans = LoadTreeFilterItems.getTreeFilterWorkItemBeans(filterUpperTO, null, null, personBean, locale, false, startDate, endDate);
			return  workItemBeans;
		}
		if (datasourceType==DATASOURCE_TYPE.PROJECT_RELEASE) {
			//"cockpit" dashboard with selected project or release
			Integer selectedProjectReleaseID = new Integer(BasePluginDashboardBL.parseIntegerValue(configParameters, CONFIGURATION_PARAMETERS.SELECTED_PROJECT_OR_RELEASE, 0));
			LOGGER.debug("getWorkItemBeans(): selectedProjectReleaseID="+selectedProjectReleaseID);
			selectedProjectReleaseID = validateSelectedProjectOrReleaseID(selectedProjectReleaseID, personBean.getObjectID());
			if (selectedProjectReleaseID==null) {
				return workItemBeans;
			}
			FilterUpperTO filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(selectedProjectReleaseID, true, includeOpen, includeClosed);
			workItemBeans = LoadTreeFilterItems.getTreeFilterWorkItemBeans(filterUpperTO, null, null, personBean, locale, false, startDate, endDate);
		} else {
			//query
			Integer selectedQueryID = new Integer(BasePluginDashboardBL.parseIntegerValue(configParameters, CONFIGURATION_PARAMETERS.SELECTED_QUERY, 0));
			workItemBeans = FilterExecuterFacade.getSavedFilterWorkItemBeans(selectedQueryID, locale, personBean, new LinkedList<ErrorData>(), false,null, null,startDate,endDate);
		}
		return workItemBeans;
	}

	/**
	 * Helper method: If isFilter is true returns FilterUpperTO object for the specified filterID (=filterOrProjectOrReleaseID)
	 * If isFilter is false returns a FilterUpperTO object for the specified project or release ID (=filterOrProjectOrReleaseID)
	 * @param isFilter
	 * @param filterOrProjectOrReleaseID
	 * @param personBean
	 * @param locale
	 * @return
	 */
	private static FilterUpperTO getFilterUpperTo(boolean isFilter, Integer filterOrProjectOrReleaseID, TPersonBean personBean, Locale locale) {
		FilterUpperTO filterUpperTO = null;
		if(isFilter) {
			TQueryRepositoryBean queryRepositoryBean = (TQueryRepositoryBean)TreeFilterFacade.getInstance().getByKey(filterOrProjectOrReleaseID);
			Integer queryType = queryRepositoryBean.getQueryType();
			QNode extendedRootNode = FilterBL.loadNode(queryRepositoryBean);
			filterUpperTO = FilterUpperFromQNodeTransformer.getFilterSelectsFromTree(extendedRootNode, true, true, personBean, locale, true);
		}else {
			filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(filterOrProjectOrReleaseID, true, true, false);
		}
		return filterUpperTO;
	}
	/**
	 * This method returns for a filter project or release ID all general work items OR document work items.
	 * If loadOnlyDocuments is true then all document issue types are returned, otherwise all general
	 * issue types are returned.
	 * @param configParameters
	 * @param personBean
	 * @param locale
	 * @param loadOnlyDocuments
	 * @return
	 * @throws TooManyItemsToLoadException
	 */
	public static List<TWorkItemBean> getGeneralOrDocumentWorkItems(List<Integer> selectedProjectOrReleaseOrQueryID, int datasourceType, TPersonBean personBean, Locale locale, boolean loadOnlyDocuments, boolean isRelease, boolean isSavedFilter) throws TooManyItemsToLoadException {
		FilterUpperTO filterUpperTO = new FilterUpperTO();
		if(!isSavedFilter) {
			if(isRelease) {
				filterUpperTO.setSelectedReleases(GeneralUtils.createIntegerArrFromCollection(selectedProjectOrReleaseOrQueryID));
			}else {
				filterUpperTO.setSelectedProjects(GeneralUtils.createIntegerArrFromCollection(selectedProjectOrReleaseOrQueryID));
			}
			TreeFilterExecuterFacade.prepareFilterUpperTO(filterUpperTO, personBean, locale, null, null);
			Integer[] selectedItemTypes = filterUpperTO.getSelectedIssueTypes();
			List<TListTypeBean> documentItemTypeBeans = IssueTypeBL.loadStrictDocumentTypes();
			List<Integer> selectedItemTypeIDs = new ArrayList<Integer>();
	
			if(!loadOnlyDocuments) {
				if (selectedItemTypes==null || selectedItemTypes.length==0) {
					Integer[] projectTypeIDs = ProjectTypesBL.getProjectTypeIDsForProjectIDs(filterUpperTO.getSelectedProjects());
					List<TListTypeBean> selectableItemTypeBeans = null;
					if (projectTypeIDs == null || projectTypeIDs.length==0) {
						selectableItemTypeBeans = IssueTypeBL.loadAllSelectable();
					} else {
						selectableItemTypeBeans = IssueTypeBL.loadAllowedByProjectTypes(projectTypeIDs);
					}
					selectedItemTypeIDs = GeneralUtils.createIntegerListFromBeanList(selectableItemTypeBeans);
					LOGGER.debug("No item types explicitly selected. Add all " + LookupContainer.getLocalizedLabelBeanListLabels(
							SystemFields.INTEGER_ISSUETYPE, GeneralUtils.createIntegerSetFromIntegerList(selectedItemTypeIDs), locale));
				} else {
					selectedItemTypeIDs = GeneralUtils.createIntegerListFromIntegerArr(selectedItemTypes);
					LOGGER.debug("Item types already selected: " + LookupContainer.getLocalizedLabelBeanListLabels(
							SystemFields.INTEGER_ISSUETYPE, GeneralUtils.createIntegerSetFromIntegerList(selectedItemTypeIDs), locale));
				}
			}else {
				if(documentItemTypeBeans != null && !documentItemTypeBeans.isEmpty()) {
					List<Integer> documentItemTypes = GeneralUtils.createIntegerListFromBeanList(documentItemTypeBeans);
					selectedItemTypeIDs.addAll(documentItemTypes);
				}
			}
			filterUpperTO.setSelectedIssueTypes(GeneralUtils.createIntegerArrFromCollection(selectedItemTypeIDs));
			return TreeFilterExecuterFacade.getInstantTreeFilterWorkItemBeans(filterUpperTO, null, selectedProjectOrReleaseOrQueryID.get(0), personBean, locale, false, null, null, null, null);
		}else {
			return TreeFilterExecuterFacade.getSavedFilterWorkItemBeans(selectedProjectOrReleaseOrQueryID.get(0), locale, personBean, new LinkedList<ErrorData>(), false);
		}
	}

	/**
	 * Validate the selected project/release
	 * @param selectedProjectOrReleaseID
	 * @param personID
	 */
	private static Integer validateSelectedProjectOrReleaseID(Integer selectedProjectOrReleaseID, Integer personID) {
		if (selectedProjectOrReleaseID!=null) {
			Map allowedProjectsMap = GeneralUtils.createMapFromList(ProjectBL.loadProjectsWithReadIssueRight(personID));
			if (selectedProjectOrReleaseID.intValue()<0) {
				if (allowedProjectsMap.containsKey(new Integer(-selectedProjectOrReleaseID.intValue()))) {
					return selectedProjectOrReleaseID;
				}
			} else {
				TReleaseBean releaseBean = LookupContainer.getReleaseBean(selectedProjectOrReleaseID);
				if (releaseBean!=null && allowedProjectsMap.containsKey(releaseBean.getProjectID())) {
					return selectedProjectOrReleaseID;
				}
			}
			LOGGER.debug("Invalid selectedProjectOrReleaseID:"+selectedProjectOrReleaseID+" for personID:"+personID);
		}
		return null;
	}


}
