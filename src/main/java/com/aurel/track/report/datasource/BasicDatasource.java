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

package com.aurel.track.report.datasource;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.category.CategoryBL;
import com.aurel.track.admin.customize.category.CategoryPickerBL;
import com.aurel.track.admin.customize.category.filter.execute.FilterExecuterFacade;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.FilterUpperConfigUtil;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadItemIDListItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadTreeFilterItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.ReportBeanLoader;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.project.release.ReleasePickerBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TReportPersonSettingsBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.ReportPersonSettingsDAO;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.itemNavigator.ItemNavigatorBL;
import com.aurel.track.itemNavigator.QueryContext;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.plugin.DatasourceDescriptor;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.PropertiesHelper;
import com.aurel.track.util.StringArrayParameterUtils;
import com.aurel.track.util.TreeNode;

public abstract class BasicDatasource implements IPluggableDatasource {

	private static final Logger LOGGER = LogManager.getLogger(BasicDatasource.class);
	protected static ReportPersonSettingsDAO reportPersonSettingsDAO = DAOFactory.getFactory().getReportPersonSettingsDAO();

	/**
	 * In some cases the selectable projects are limited by extra rights
	 * @param personBean
	 * @return returns the base projects having the implicit right.
	 * 		The project/release picker adds the subprojects/subreleases to this base projects
	 */
	protected List<Integer> getSelectableBaseProjectIDs(TPersonBean personBean) {
		return null;
	}

	/**
	 * Get the ReportBean list according to the selected source
	 * @param datasourceType
     * @param projectOrReleaseID
     * @param filterID
	 * @param contextMap
     * @param includeOpen
     * @param includeClosed
     * @param includeSubs
	 * @param personBean
	 * @param locale
	 * @return
	 * @throws TooManyItemsToLoadException
	 */
	protected List<ReportBean> getReportBeanList(Integer datasourceType,
			Integer projectOrReleaseID, Integer filterID, Map<String, Object> contextMap,
			boolean includeOpen, boolean includeClosed, boolean includeSubs,
			TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException {
		List<ReportBean> reportBeanList = null;
		Boolean fromIssueNavigator = (Boolean)contextMap.get(CONTEXT_ATTRIBUTE.FROM_ISSUE_NAVIGATOR);
		Integer dashboardProjectOrReleaseID = (Integer)contextMap.get(CONTEXT_ATTRIBUTE.DASHBOARD_PROJECT_RELEASE_ID);
		if (fromIssueNavigator!=null && fromIssueNavigator.booleanValue()) {
			List<Integer> workItemIDs = (List<Integer>)contextMap.get(CONTEXT_ATTRIBUTE.WORKITEMIDS);
			if (workItemIDs!=null && !workItemIDs.isEmpty()) {
				reportBeanList = LoadItemIDListItems.getReportBeansByWorkItemIDs(GeneralUtils.createIntArrFromIntegerList(workItemIDs), false,
						personBean.getObjectID(), locale, true, true, true, true, true, true, false, true, false);
			} else {
					QueryContext queryContext=ItemNavigatorBL.loadLastQuery(personBean.getObjectID(),locale);
					if (queryContext!=null){
						reportBeanList = ItemNavigatorBL.executeQuery(personBean, locale, queryContext);
					}
			}
		} else {
			if (dashboardProjectOrReleaseID!=null) {
				FilterUpperTO filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(dashboardProjectOrReleaseID, true, includeOpen, includeClosed);
				reportBeanList = LoadTreeFilterItems.getTreeFilterReportBeansForReport(filterUpperTO, null, filterID, personBean, locale);
			} else {
				//saves the parameters into the database
				if (datasourceType==null) {
					LOGGER.warn("No datasourceType was selected");
					return null;
				}
				if (datasourceType.intValue()==DATASOURCE_TYPE.PROJECT_RELEASE) {
					if (projectOrReleaseID==null) {
						LOGGER.warn("No project/release was selected");
						return null;
					} else {
						FilterUpperTO filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(projectOrReleaseID, true, includeOpen, includeClosed);
						reportBeanList = LoadTreeFilterItems.getTreeFilterReportBeansForReport(filterUpperTO, null, filterID, personBean, locale);
					}
				} else {
					if (filterID==null) {
						LOGGER.warn("No filter was selected");
						return null;
					} else {
						reportBeanList = FilterExecuterFacade.getSavedFilterReportBeanList(filterID, locale,
								personBean, new LinkedList<ErrorData>(), null, null, true);
					}
				}
			}
		}
		return ReportBeanLoader.addISOValuesToReportBeans(reportBeanList, personBean.getObjectID(), locale);
	}

	/**
	 * Get the workItemBeans according to the selected source:
	 * if project_release: if selected number is negative then take it as a projectID
	 * 						if selected number is positive then take it as a releaseID
	 * if query: result of the corresponding query depending on query type and repository type
	 * @param datasourceType
     * @param projectOrReleaseID
     * @param filterID
	 * @param contextMap
     * @param includeOpen
     * @param includeClosed
     * @param includeSubs
	 * @param personBean
	 * @param locale
	 * @return
	 * @throws TooManyItemsToLoadException
	 */
	public List<TWorkItemBean> getWorkItemBeans(Integer datasourceType, Integer projectOrReleaseID, Integer filterID,
			Map<String, Object> contextMap, boolean includeOpen, boolean includeClosed, boolean includeSubs, TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException {
		List<TWorkItemBean> workItemBeanList = null;
		Boolean fromIssueNavigator = (Boolean)contextMap.get(CONTEXT_ATTRIBUTE.FROM_ISSUE_NAVIGATOR);
		Integer dashboardProjectOrReleaseID = (Integer)contextMap.get(CONTEXT_ATTRIBUTE.DASHBOARD_PROJECT_RELEASE_ID);
		if (fromIssueNavigator!=null && fromIssueNavigator.booleanValue()) {
			List<Integer> workItemIDs = (List<Integer>)contextMap.get(CONTEXT_ATTRIBUTE.WORKITEMIDS);
			if (workItemIDs!=null && !workItemIDs.isEmpty()) {
				workItemBeanList = LoadItemIDListItems.getWorkItemBeansByWorkItemIDs(GeneralUtils.createIntArrFromIntegerList(workItemIDs), personBean.getObjectID(), true, true, false);
			} else {
					QueryContext queryContext=ItemNavigatorBL.loadLastQuery(personBean.getObjectID(),locale);
					if (queryContext!=null){
						//FIXME: get direct getWorkItemBeans from ItemNavigatorBL.executeQuery()
						List<ReportBean> reportBeanList = ItemNavigatorBL.executeQuery(personBean, locale, queryContext);
						workItemBeanList = new LinkedList<TWorkItemBean>();
						for (ReportBean reportBean : reportBeanList) {
							workItemBeanList.add(reportBean.getWorkItemBean());
						}
					}
			}
		} else {
			if (dashboardProjectOrReleaseID!=null) {
				FilterUpperTO filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(dashboardProjectOrReleaseID, true, includeOpen, includeClosed);
				workItemBeanList = LoadTreeFilterItems.getTreeFilterWorkItemBeans(filterUpperTO, personBean, locale, true);
			} else {
				//saves the parameters into the database
				if (datasourceType==null) {
					LOGGER.warn("No datasourceType was selected");
					return null;
				}
				if (datasourceType.intValue()==DATASOURCE_TYPE.PROJECT_RELEASE) {
					if (projectOrReleaseID==null) {
						LOGGER.warn("No project/release was selected");
						return null;
					} else {
						FilterUpperTO filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(projectOrReleaseID, true, includeOpen, includeClosed);
						workItemBeanList = LoadTreeFilterItems.getTreeFilterWorkItemBeans(filterUpperTO, personBean, locale, true);
					}
				} else {
					if (filterID==null) {
						LOGGER.warn("No filter was selected");
						return null;
					} else {
						workItemBeanList = FilterExecuterFacade.getSavedFilterWorkItemBeans(
								filterID, locale, personBean, new LinkedList<ErrorData>(), true);

					}
				}
			}
		}
		return workItemBeanList;
	}



	/**
	 * Gets the extra parameters from the datasource
	 * @param params
	 * @param datasourceDescriptor
	 * @param contextMap
	 * @param templateDescriptionMap
	 * @param templateID
	 * @param personBean
	 * @param locale
	 * @return
	 */
	@Override
	public Map<String, Object> getJasperReportParameters(Map<String, String[]> params, DatasourceDescriptor datasourceDescriptor,
			Map<String, Object> contextMap, Map<String, Object> templateDescriptionMap,
			Integer templateID, TPersonBean personBean, Locale locale) {
		return new HashMap<String, Object>();
	}

	/**
	 * Whether the getSerializedDatasource(OutputStream outputStream, Object datasource)
	 * returns null or not
	 * @return
	 */
	@Override
	public boolean implementSerialization() {
		return true;
	}

	/**
	 * Prepares a map for rendering the config page
	 * @param templateID
	 * @param datasourceDescriptor
	 * @param contextMap
	 * @param personBean
	 * @param locale
	 * @return
	 */
	@Override
	public String prepareParameters(Integer templateID,
			DatasourceDescriptor datasourceDescriptor, Map<String, Object> contextMap,
			TPersonBean personBean, Locale locale) {
		Map<String, Object> savedParamsMap = loadParameters(personBean.getObjectID(), templateID);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{");
		JSONUtility.appendBooleanValue(stringBuilder, CONFIG_FILEDS.IMPLEMENT_SERIALIZATION, implementSerialization());
		String configClass = null;
		if (datasourceDescriptor!=null) {
			configClass = datasourceDescriptor.getJsConfigClass();
		} else {
			configClass = DatasourceTemplates.BASIC_DATASOURCE;
		}
		Integer dashboardProjectOrReleaseID = (Integer)contextMap.get(CONTEXT_ATTRIBUTE.DASHBOARD_PROJECT_RELEASE_ID);
		Boolean fromIssueNavigator = (Boolean)contextMap.get(CONTEXT_ATTRIBUTE.FROM_ISSUE_NAVIGATOR);
		List<Integer> workItemIDs = (List<Integer>)contextMap.get(CONTEXT_ATTRIBUTE.WORKITEMIDS);
		Integer dashboardID = (Integer)contextMap.get(CONTEXT_ATTRIBUTE.DASHBOARD_ID);

		JSONUtility.appendStringValue(stringBuilder, CONFIG_FILEDS.CONFIG_CLASS, configClass);
		JSONUtility.appendFieldName(stringBuilder, CONFIG_FILEDS.CONFIG_DATA).append(":{");
		JSONUtility.appendIntegerValue(stringBuilder, CONTEXT_ATTRIBUTE.TEMPLATE_ID, templateID);
		if (fromIssueNavigator!=null) {
			JSONUtility.appendBooleanValue(stringBuilder, CONTEXT_ATTRIBUTE.FROM_ISSUE_NAVIGATOR, fromIssueNavigator.booleanValue());
		}
		if (workItemIDs!=null) {
			JSONUtility.appendIntegerListAsArray(stringBuilder, CONTEXT_ATTRIBUTE.WORKITEMIDS, workItemIDs);
		}
		if (dashboardProjectOrReleaseID!=null) {
			JSONUtility.appendIntegerValue(stringBuilder, CONTEXT_ATTRIBUTE.DASHBOARD_ID, dashboardID);
			JSONUtility.appendIntegerValue(stringBuilder, CONTEXT_ATTRIBUTE.DASHBOARD_PROJECT_RELEASE_ID, dashboardProjectOrReleaseID);
		}
		stringBuilder.append(getDatasourceParams(datasourceDescriptor, contextMap, savedParamsMap, personBean, locale, fromIssueNavigator));
		stringBuilder.append("},");
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.SUCCESS, true, true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}

	/**
	 * Gets the datasource parameters
	 * @param datasourceDescriptor
	 * @param contextMap
	 * @param savedParamsMap
	 * @param personBean
	 * @param locale
	 * @return
	 */
	protected String getDatasourceParams(DatasourceDescriptor datasourceDescriptor,
			Map<String, Object> contextMap, Map<String, Object> savedParamsMap, TPersonBean personBean, Locale locale, Boolean fromIssueNavigator) {
		StringBuilder stringBuilder = new StringBuilder();
		Integer dashboardProjectOrReleaseID = (Integer)contextMap.get(CONTEXT_ATTRIBUTE.DASHBOARD_PROJECT_RELEASE_ID);
		Integer selectedDatasourceType = (Integer)savedParamsMap.get(PARAMETER_NAME.DATASOURCETYPE);
		if (selectedDatasourceType==null || dashboardProjectOrReleaseID!=null) {
			//if report is called from a dashboard force to project/release
			selectedDatasourceType = Integer.valueOf(DATASOURCE_TYPE.PROJECT_RELEASE);
		}
		List<Integer> selectableBaseProjectIDs = getSelectableBaseProjectIDs(personBean);
        Integer projectOrReleaseID = null;
        if (fromIssueNavigator==null || !fromIssueNavigator.booleanValue())  {
            //do not preselect the last saved project/release if coming form item navigator
		    projectOrReleaseID = (Integer)savedParamsMap.get(PARAMETER_NAME.PROJECT_OR_RELEASE_ID);
        }
		Integer filterID = null;
		if (dashboardProjectOrReleaseID==null) {
			//get the saved filter value only if report is not called from a dashboard
			//(project/release specific dashboard or global dashboard but with project/release datasource set)
			filterID = (Integer)savedParamsMap.get(PARAMETER_NAME.FILTER_ID);
		}
		stringBuilder.append(getDatasourceBaseParams(selectedDatasourceType, projectOrReleaseID,
				selectableBaseProjectIDs, filterID, dashboardProjectOrReleaseID, personBean, locale));
		String extraDatasource = getDatasourceExtraParams(savedParamsMap, datasourceDescriptor, contextMap, personBean, locale);
		if (extraDatasource!=null && !"".equals(extraDatasource)) {
			stringBuilder.append(",").append(extraDatasource);
		}
		return stringBuilder.toString();
	}

	/**
	 * Gets the base datasource JSON
	 * @param selectedDatasourceType
	 * @param projectOrReleaseID
	 * @param selectableBaseProjectIDs
	 * @param filterID
	 * @param dashboardProjectOrReleaseID
	 * @return
	 */
	protected String getDatasourceBaseParams(Integer selectedDatasourceType, Integer projectOrReleaseID,
			List<Integer> selectableBaseProjectIDs, Integer filterID,
			Integer dashboardProjectOrReleaseID, TPersonBean personBean, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder();
		JSONUtility.appendIntegerValue(stringBuilder, PARAMETER_NAME.DATASOURCETYPE, selectedDatasourceType);
		JSONUtility.appendStringValue(stringBuilder, PARAMETER_NAME.DATASOURCETYPE_NAME_FIELD, PARAMETER_NAME.DATASOURCETYPE_NAME_VALUE);
        String projectReleaseTree =  ReleasePickerBL.getTreeJSON(
                selectableBaseProjectIDs, null, false, true,
                false, true, true, true,
                false, null, personBean, locale);
        JSONUtility.appendJSONValue(stringBuilder, PARAMETER_NAME.PROJECT_RELEASE_TREE, projectReleaseTree);
		if (projectOrReleaseID!=null) {
            JSONUtility.appendStringValue(stringBuilder, PARAMETER_NAME.PROJECT_OR_RELEASE_ID, projectOrReleaseID.toString());
		}
		JSONUtility.appendStringValue(stringBuilder, PARAMETER_NAME.PROJECT_OR_RELEASE_NAME_FIELD, PARAMETER_NAME.PROJECT_OR_RELEASE_NAME_VALUE);
        List<TreeNode> filterTree = CategoryPickerBL.getPickerNodesEager(
                personBean, false, false, null,
                false, null, null, locale, CategoryBL.CATEGORY_TYPE.ISSUE_FILTER_CATEGORY);
        JSONUtility.appendJSONValue(stringBuilder, PARAMETER_NAME.FILTER_TREE, JSONUtility.getTreeHierarchyJSON(filterTree, false, true));
		if (filterID!=null) {
			JSONUtility.appendStringValue(stringBuilder, PARAMETER_NAME.FILTER_ID, filterID.toString());
		}
		JSONUtility.appendStringValue(stringBuilder, PARAMETER_NAME.FILTER_NAME_FIELD, PARAMETER_NAME.FILTER_NAME_VALUE);
		List<IntegerStringBean> datasourceOptions = new LinkedList<IntegerStringBean>();
		//do not localize here because these are coming from the datasource ReportBundles
		datasourceOptions.add(new IntegerStringBean("common.datasource.projectRelease", DATASOURCE_TYPE.PROJECT_RELEASE));
		datasourceOptions.add(new IntegerStringBean("common.datasource.filter", DATASOURCE_TYPE.FILTER));
		JSONUtility.appendIntegerStringBeanList(stringBuilder, PARAMETER_NAME.DATASOURCETYPE_OPTIONS, datasourceOptions, true);
		return stringBuilder.toString();
	}

	/**
	 * Loads the parameters saved in the database
	 * @param personID
	 * @param templateID
	 * @return
	 */
	protected Map<String, Object> loadParameters(Integer personID, Integer templateID) {
		String paramSettings = loadParameterSettings(personID, templateID);
		return loadParamObjectsFromPropertyStrings(paramSettings);
	}

	/**
	 * Loads the parameters saved in the database
	 * @param personID
	 * @param templateID
	 * @return
	 */
	private String loadParameterSettings(Integer personID, Integer templateID) {
		List<TReportPersonSettingsBean> reportPersonSettingsList = reportPersonSettingsDAO.loadByPersonAndTemplate(personID, templateID);
		if (reportPersonSettingsList!=null && !reportPersonSettingsList.isEmpty()) {
			TReportPersonSettingsBean reportPersonSettingsBean = reportPersonSettingsList.get(0);
			return reportPersonSettingsBean.getParamSettings();
		}
		return null;
	}

	/**
	 * Loads the parameters from the property strings
	 * @param paramSettings
	 * @return
	 */
	protected Map<String, Object> loadParamObjectsFromPropertyStrings(String paramSettings) {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		if (paramSettings!=null) {
			//first execution, no saved properties
			Integer datasourceType = PropertiesHelper.getIntegerProperty(paramSettings, PARAMETER_NAME.DATASOURCETYPE);
			if (datasourceType==null) {
				datasourceType = Integer.valueOf(DATASOURCE_TYPE.PROJECT_RELEASE);
			}
			paramsMap.put(PARAMETER_NAME.DATASOURCETYPE, datasourceType);
			Integer projectOrReleaseID = PropertiesHelper.getIntegerProperty(paramSettings, PARAMETER_NAME.PROJECT_OR_RELEASE_ID);
			paramsMap.put(PARAMETER_NAME.PROJECT_OR_RELEASE_ID, projectOrReleaseID);
			Integer filterID = PropertiesHelper.getIntegerProperty(paramSettings, PARAMETER_NAME.FILTER_ID);
			paramsMap.put(PARAMETER_NAME.FILTER_ID, filterID);
		} else {
			paramsMap.put(PARAMETER_NAME.DATASOURCETYPE, DATASOURCE_TYPE.PROJECT_RELEASE);
		}
		return paramsMap;
	}

	/**
	 * Gets the parameter settings for report to be executed and the parameters to be saved
	 * @param params parameters as String[]
	 * @param locale
	 * @param savedParamsMap output parameter: parameters transformed to the actual types
	 * @return
	 */
	protected String loadParamObjectsAndPropertyStringsAndFromStringArrParams(
			Map<String, String[]> params, Locale locale, Map<String, Object> savedParamsMap) {
		String paramSettings = "";
		Integer datasourceType = StringArrayParameterUtils.parseIntegerValue(params,
				PARAMETER_NAME.DATASOURCETYPE, DATASOURCE_TYPE.PROJECT_RELEASE);
		savedParamsMap.put(PARAMETER_NAME.DATASOURCETYPE, datasourceType);
		Integer projectOrReleaseID = null;
		Integer filterID = null;
		if (DATASOURCE_TYPE.PROJECT_RELEASE==datasourceType) {
			projectOrReleaseID = StringArrayParameterUtils.parseIntegerValue(params,
				PARAMETER_NAME.PROJECT_OR_RELEASE_ID);
			savedParamsMap.put(PARAMETER_NAME.PROJECT_OR_RELEASE_ID, projectOrReleaseID);
		} else {
			filterID = StringArrayParameterUtils.parseIntegerValue(params,
				PARAMETER_NAME.FILTER_ID);
			savedParamsMap.put(PARAMETER_NAME.FILTER_ID, filterID);
		}
		paramSettings = PropertiesHelper.setIntegerProperty(paramSettings, PARAMETER_NAME.DATASOURCETYPE, datasourceType);
		paramSettings = PropertiesHelper.setIntegerProperty(paramSettings, PARAMETER_NAME.PROJECT_OR_RELEASE_ID, projectOrReleaseID);
		paramSettings = PropertiesHelper.setIntegerProperty(paramSettings, PARAMETER_NAME.FILTER_ID, filterID);
		return paramSettings;
	}

	/**
	 * Saves the parameters in the database
	 * @param paramSettings
	 * @param personID
	 * @param templateID
	 */
	protected void saveParameters(String paramSettings, Integer personID, Integer templateID) {
		List<TReportPersonSettingsBean> reportPersonSettingsList = reportPersonSettingsDAO.loadByPersonAndTemplate(personID, templateID);
		TReportPersonSettingsBean reportPersonSettingsBean = null;
		if (reportPersonSettingsList!=null && !reportPersonSettingsList.isEmpty()) {
			reportPersonSettingsBean = reportPersonSettingsList.get(0);
		}
		if (reportPersonSettingsBean==null) {
			reportPersonSettingsBean = new TReportPersonSettingsBean();
			reportPersonSettingsBean.setPerson(personID);
			reportPersonSettingsBean.setReportTemplate(templateID);
		}
		reportPersonSettingsBean.setParamSettings(paramSettings);
		reportPersonSettingsDAO.save(reportPersonSettingsBean);
	}

	/**
	 * Refreshing of some parameters through ajax: only a part of the parameters should be recalculated
	 * @param params
	 * @param templateID
	 * @param datasourceDescriptor
	 * @param contextMap
	 * @param personBean
	 * @param locale
	 * @return
	 */
	@Override
	public String refreshParameters(Map<String, String[]> params, Integer templateID,
			DatasourceDescriptor datasourceDescriptor, Map<String, Object> contextMap,
			TPersonBean personBean, Locale locale) {
		return null;
	}

	/**
	 * Extra datasource configurations
	 * Whether beyond the item list also other datasource configuration parameters are needed (important when coming from item navigator)
	 * @param savedParamsMap
	 * @param datasourceDescriptor
	 * @param personBean
	 * @param locale
	 * @return
	 */
	public String getDatasourceExtraParams(Map<String, Object> savedParamsMap,
			DatasourceDescriptor datasourceDescriptor, Map<String, Object> contextMap,
			TPersonBean personBean, Locale locale) {
		return null;
	}

}
