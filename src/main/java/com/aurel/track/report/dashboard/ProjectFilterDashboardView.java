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

import com.aurel.track.admin.customize.category.filter.FilterBL;
import com.aurel.track.admin.customize.category.filter.FilterFacade;
import com.aurel.track.admin.customize.category.filter.MenuitemFilterBL;
import com.aurel.track.admin.customize.category.filter.PredefinedQueryBL;
import com.aurel.track.admin.customize.category.filter.QNode;
import com.aurel.track.admin.customize.category.filter.TreeFilterFacade;
import com.aurel.track.admin.customize.category.filter.execute.FilterExecuterFacade;
import com.aurel.track.admin.customize.category.filter.execute.TreeFilterExecuterFacade;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.FilterUpperConfigUtil;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadTreeFilterItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperFromQNodeTransformer;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.RACIBean;
import com.aurel.track.admin.customize.category.filter.tree.io.TreeFilterReader;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.report.dashboard.DataSourceDashboardBL.CONFIGURATION_PARAMETERS;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.LabelValueBean;

/**
 * Dasboard having Project/release or filter to select in configuration
 * @author Tamas
 *
 */
public class ProjectFilterDashboardView extends BasePluginDashboardView {

	public static interface DATASOURCE_TYPE {
		static int PROJECT_RELEASE = 1;
		static int QUERY = 2;
	}

	public static interface DATASOURCE_PARAMETERS {
		static String DATASOURCE_TYPES = "datasourceTypes";
		static String SELECTED_DATASOURCE_TYPE = "selectedDatasourceType";
		static String SELECTED_PROJECT_OR_RELEASE = "selectedProjectOrRelease";
		static String SELECTED_QUERY = "selectedQueryID";
	}
	/**
	 * Gets the datasource configuration
	 * @param parameters
	 * @param locale
	 * @return
	 */
	protected String getDatasourceConfig(Map<String,String> parameters, Integer entityId, Integer entityType, Locale locale) {
		StringBuilder sb = new StringBuilder();
		JSONUtility.appendIntegerStringBeanList(sb, DATASOURCE_PARAMETERS.DATASOURCE_TYPES, getDatasourceTypes(locale));
		Integer selectedDatasourceType = parseInteger(parameters, DATASOURCE_PARAMETERS.SELECTED_DATASOURCE_TYPE);
		Integer selectedEntityID=parseInteger(parameters,DATASOURCE_PARAMETERS.SELECTED_PROJECT_OR_RELEASE);
		Integer selectedQuery = parseInteger(parameters, DATASOURCE_PARAMETERS.SELECTED_QUERY);
		if (selectedDatasourceType==null) {
			//no configutation: probably new
			if (entityId==null) {
				//global cockpit
				if (selectedQuery==null) {
					selectedQuery = getDefaultFilter();
				}
				selectedDatasourceType = DATASOURCE_TYPE.QUERY;
			} else {
				//browse project/release cockpit
				selectedEntityID = entityId;
				selectedDatasourceType = DATASOURCE_TYPE.PROJECT_RELEASE;
			}
		}
		JSONUtility.appendIntegerValue(sb,DATASOURCE_PARAMETERS.SELECTED_DATASOURCE_TYPE, selectedDatasourceType);
		JSONUtility.appendIntegerValue(sb,DATASOURCE_PARAMETERS.SELECTED_PROJECT_OR_RELEASE, selectedEntityID);
		JSONUtility.appendIntegerValue(sb,DATASOURCE_PARAMETERS.SELECTED_QUERY,  selectedQuery);
		return sb.toString();
	}

	/**
	 * Gets the default filter
	 * @return
	 */
	protected Integer getDefaultFilter() {
		List<Integer> predefinedFilters = MenuitemFilterBL.getFilterIDsToSubscribe();
		if (predefinedFilters!=null) {
			if (predefinedFilters.contains(PredefinedQueryBL.PREDEFINED_QUERY.ALL_ITEMS)) {
				return PredefinedQueryBL.PREDEFINED_QUERY.ALL_ITEMS;
			} else {
				if (predefinedFilters.contains(PredefinedQueryBL.PREDEFINED_QUERY.OUTSTANDING)) {
					return PredefinedQueryBL.PREDEFINED_QUERY.OUTSTANDING;
				} else {
					if (predefinedFilters.contains(PredefinedQueryBL.PREDEFINED_QUERY.MY_ITEMS)) {
						return PredefinedQueryBL.PREDEFINED_QUERY.MY_ITEMS;
					} else {
						if (predefinedFilters.contains(PredefinedQueryBL.PREDEFINED_QUERY.MANAGERS_ITEMS)) {
							return PredefinedQueryBL.PREDEFINED_QUERY.MANAGERS_ITEMS;
						} else {
							if (predefinedFilters.contains(PredefinedQueryBL.PREDEFINED_QUERY.RESPONSIBLES_ITEMS)) {
								return PredefinedQueryBL.PREDEFINED_QUERY.RESPONSIBLES_ITEMS;
							} else {
								if (predefinedFilters.contains(PredefinedQueryBL.PREDEFINED_QUERY.AUTHOR_ITEMS)) {
									return PredefinedQueryBL.PREDEFINED_QUERY.AUTHOR_ITEMS;
								} else {
									if (predefinedFilters.contains(PredefinedQueryBL.PREDEFINED_QUERY.WATCHER_ITEMS)) {
										return PredefinedQueryBL.PREDEFINED_QUERY.WATCHER_ITEMS;
									} else {
										if (predefinedFilters.contains(PredefinedQueryBL.PREDEFINED_QUERY.UNSCHEDULED)) {
											return PredefinedQueryBL.PREDEFINED_QUERY.UNSCHEDULED;
										} else {
											if (predefinedFilters.contains(PredefinedQueryBL.PREDEFINED_QUERY.CLOSED_RECENTLY)) {
												return PredefinedQueryBL.PREDEFINED_QUERY.CLOSED_RECENTLY;
											} else {
												if (predefinedFilters.contains(PredefinedQueryBL.PREDEFINED_QUERY.ADDED_RECENTLY)) {
													return PredefinedQueryBL.PREDEFINED_QUERY.ADDED_RECENTLY;
												} else {
													if (predefinedFilters.contains(PredefinedQueryBL.PREDEFINED_QUERY.UPDATED_RECENTLY)) {
														return PredefinedQueryBL.PREDEFINED_QUERY.UPDATED_RECENTLY;
													} else {
														if (predefinedFilters.contains(PredefinedQueryBL.PREDEFINED_QUERY.MEETINGS)) {
															return PredefinedQueryBL.PREDEFINED_QUERY.MEETINGS;
														} else {

														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Get the list of possible datasource types
	 * @param locale
	 * @return
	 */
	protected List<IntegerStringBean> getDatasourceTypes(Locale locale) {
		List<IntegerStringBean> datasourceTypes = new ArrayList<IntegerStringBean>();
		datasourceTypes.add(new IntegerStringBean(
				localizeText("common.datasource.projectRelease", locale),
				Integer.valueOf(DATASOURCE_TYPE.PROJECT_RELEASE)));
		datasourceTypes.add(new IntegerStringBean(
				localizeText("common.datasource.filter", locale),
				Integer.valueOf(DATASOURCE_TYPE.QUERY)));
		return datasourceTypes;
	}

	/**
	 * Validates the submitted configuration parameters and add possible errors into errors list
	 * @param parameters
	 * @param personBean
	 * @param locale
	 * @param entityId
	 * @param entityType
	 * @param errors
	 */
	@Override
	protected void validateConfig(Map<String,String> parameters, TPersonBean personBean,
			Locale locale, Integer entityId, Integer entityType, List<LabelValueBean> errors) {
		if(entityId!=null){
			//if project or release config, shoould not check dataSouurce
			return;
		}
		int datasourceType = BasePluginDashboardBL.parseIntegerValue(parameters,
				CONFIGURATION_PARAMETERS.SELECTED_DATASOURCE_TYPE, DATASOURCE_TYPE.PROJECT_RELEASE);
		Integer selectedProjectReleaseID=null;
		Integer selectedQueryID=null;
		if (datasourceType==DATASOURCE_TYPE.PROJECT_RELEASE) {
			selectedProjectReleaseID = new Integer(BasePluginDashboardBL.parseIntegerValue(parameters, CONFIGURATION_PARAMETERS.SELECTED_PROJECT_OR_RELEASE, 0));
			selectedProjectReleaseID = validateSelectedProjectOrReleaseID(selectedProjectReleaseID, personBean.getObjectID());
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
	 * Validate the selected project/release
	 * @param selectedProjectOrReleaseID
	 * @param personID
	 */
	protected Integer validateSelectedProjectOrReleaseID(Integer selectedProjectOrReleaseID, Integer personID) {
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
		}
		return null;
	}

	/**
	 *
	 * @param parameters
	 * @param projectID
	 * @param releaseID
	 * @param personBean
	 * @param locale
	 * @return
	 * @throws TooManyItemsToLoadException
	 */
	protected List<TWorkItemBean> getWorkItemBeans(Map<String, String> parameters,
			 Integer projectID, Integer releaseID, TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException {
		Integer selectedProjectOrReleaseID = null;
		Integer filterID = null;

		if (projectID==null && releaseID==null) {
			Integer selectedDatasource = parseInteger(parameters,DATASOURCE_PARAMETERS.SELECTED_DATASOURCE_TYPE);
			if (selectedDatasource!=null){
				if (selectedDatasource.intValue()==DATASOURCE_TYPE.PROJECT_RELEASE) {
					//project/release selected
					selectedProjectOrReleaseID = parseInteger(parameters,DATASOURCE_PARAMETERS.SELECTED_PROJECT_OR_RELEASE);
				} else {
					//filter selected
					filterID =  parseInteger(parameters, DATASOURCE_PARAMETERS.SELECTED_QUERY);
				}
			}else{
				selectedDatasource = DATASOURCE_TYPE.QUERY;
				filterID = getDefaultFilter();
			}

		} else {
			if (releaseID!=null) {
				//release dashboard
				selectedProjectOrReleaseID = releaseID;
			} else {
				//project dashboard
				selectedProjectOrReleaseID = Integer.valueOf(-projectID.intValue());
			}
		}
		return getWorkItemBeans(selectedProjectOrReleaseID, true, true, filterID, personBean, locale);
	}

	/**
	 * Gets the workItem beans for selected datasource
	 * @param selectedProjectOrReleaseID
	 * @param includeOpen
	 * @param includeClosed
	 * @param filterID
	 * @param personBean
	 * @param locale
	 * @return
	 * @throws TooManyItemsToLoadException
	 */
	protected List<TWorkItemBean> getWorkItemBeans(Integer selectedProjectOrReleaseID, boolean includeOpen, boolean includeClosed, Integer filterID, TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException {
		if (selectedProjectOrReleaseID!=null) {
			//project or release
			FilterUpperTO filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(selectedProjectOrReleaseID, true, includeOpen, includeClosed);
			return LoadTreeFilterItems.getTreeFilterWorkItemBeans(filterUpperTO, personBean, locale, false);
		} else {
			if (filterID!=null) {
				//filter
				return FilterExecuterFacade.getSavedFilterWorkItemBeans(filterID, locale, personBean, new LinkedList<ErrorData>(), false);
			} else {
				//no project/release or filter selected: fall back to my items
				return new LinkedList<TWorkItemBean>();
			}
		}
	}

	/**
	 * Gets the FilterUpperTO for selected datasource
	 * @param selectedProjectOrReleaseID
	 * @param includeOpen
	 * @param includeClosed
	 * @param filterID
	 * @param personBean
	 * @param locale
	 * @return
	 */
	protected FilterUpperTO getFilterUpperTO(Map<String, String> parameters,
			 Integer projectID, Integer releaseID, TPersonBean personBean, Locale locale, boolean includeOpen, boolean includeClosed) {
		FilterUpperTO filterUpperTO = null;
		Integer selectedProjectOrReleaseID = null;
		Integer filterID = null;
		Integer entityFlag = null;
		if (projectID==null && releaseID==null) {
			Integer selectedDatasource = parseInteger(parameters,DATASOURCE_PARAMETERS.SELECTED_DATASOURCE_TYPE);
			if (selectedDatasource!=null){
				if (selectedDatasource.intValue()==DATASOURCE_TYPE.PROJECT_RELEASE) {
					//project/release selected
					selectedProjectOrReleaseID = parseInteger(parameters,DATASOURCE_PARAMETERS.SELECTED_PROJECT_OR_RELEASE);
				} else {
					//filter selected
					filterID =  parseInteger(parameters, DATASOURCE_PARAMETERS.SELECTED_QUERY);
				}
			} else {
				selectedDatasource = DATASOURCE_TYPE.QUERY;
				filterID = getDefaultFilter();
			}
		} else {
			if (releaseID!=null) {
				//release dashboard
				selectedProjectOrReleaseID = releaseID;
				entityFlag = SystemFields.INTEGER_RELEASE;
			} else {
				//project dashboard
				selectedProjectOrReleaseID = Integer.valueOf(-projectID.intValue());
				entityFlag = SystemFields.INTEGER_PROJECT;
			}
		}
		if (selectedProjectOrReleaseID!=null) {
			//project or release selected
			filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(selectedProjectOrReleaseID, true, includeOpen, includeClosed);
			TreeFilterExecuterFacade.prepareFilterUpperTO(filterUpperTO, personBean, locale, null, null);
			return filterUpperTO;
		} else {
			if (filterID!=null) {
				//filter selected
				FilterFacade filterFacade = TreeFilterFacade.getInstance();
				TQueryRepositoryBean queryRepositoryBean = (TQueryRepositoryBean)filterFacade.getByKey(filterID);
				String filterExpression = FilterBL.getFilterExpression(queryRepositoryBean);
				QNode extendedRootNode = TreeFilterReader.getInstance().readQueryTree(filterExpression);
				filterUpperTO = FilterUpperFromQNodeTransformer.getFilterSelectsFromTree(extendedRootNode, true, false, personBean, locale, true);
				TreeFilterExecuterFacade.prepareFilterUpperTO(filterUpperTO, personBean, locale, projectID, entityFlag);
				return filterUpperTO;
			}
		}
		return null;
	}

	/**
	 * Gets the RACIBean beans for selected datasource. It should be not null only for MY_ITEMS
	 * @param parameters
	 * @param projectID
	 * @param releaseID
	 * @param personBean
	 * @param filterUpperTO
	 * @return
	 */
	protected RACIBean getRACIBean(Map<String, String> parameters,
			 Integer projectID, Integer releaseID, TPersonBean personBean, FilterUpperTO filterUpperTO) {
		RACIBean raciBean = null;
		Integer filterID = null;
		if (projectID==null && releaseID==null) {
			Integer selectedDatasource = parseInteger(parameters,DATASOURCE_PARAMETERS.SELECTED_DATASOURCE_TYPE);
			if (selectedDatasource!=null){
				if (selectedDatasource.intValue()==DATASOURCE_TYPE.QUERY) {
					//filter selected
					filterID =  parseInteger(parameters, DATASOURCE_PARAMETERS.SELECTED_QUERY);
				}
			} else {
				selectedDatasource = DATASOURCE_TYPE.QUERY;
				filterID = getDefaultFilter();
			}
		}
		if (filterID!=null) {
			//filter selected
			raciBean = TreeFilterExecuterFacade.getRACIBean(filterID, personBean.getObjectID(), filterUpperTO);

		}
		return raciBean;
	}
}
