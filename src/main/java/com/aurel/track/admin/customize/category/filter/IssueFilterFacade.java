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

package com.aurel.track.admin.customize.category.filter;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.category.CategoryBL;
import com.aurel.track.admin.customize.category.CategoryGridRowTO;
import com.aurel.track.admin.customize.category.CategoryNodeTO;
import com.aurel.track.admin.customize.category.CategoryTO;
import com.aurel.track.admin.customize.category.CategoryTokens;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.FilterUpperConfigUtil;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterSelectsListsLoader;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.lists.ListBL;
import com.aurel.track.admin.customize.lists.systemOption.StatusBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TCLOBBean;
import com.aurel.track.beans.TLastExecutedQueryBean;
import com.aurel.track.beans.TListBean;
import com.aurel.track.beans.TMenuitemQueryBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.itemNavigator.ItemNavigatorBL;
import com.aurel.track.itemNavigator.ItemNavigatorBL.QUERY_TYPE;
import com.aurel.track.itemNavigator.lastExecuted.LastExecutedBL;
import com.aurel.track.itemNavigator.viewPlugin.ViewDescriptorBL;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.plugin.IssueListViewDescriptor;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.LabelValueBean;

/**
 * An implementation of FilterFacade for issue filters (tree or TQL)
 * @author Tamas Ruff
 *
 */
public abstract class IssueFilterFacade extends FilterFacade {
	private static final Logger LOGGER = LogManager.getLogger(IssueFilterFacade.class);
	public static Integer NONE_STYLE_FIELD = Integer.valueOf(0); 
	/**
	 * Does the category has content (subcategories or leafs)
	 * @param objectID
	 * @return
	 */
	@Override
	public boolean replaceNeeded(Integer objectID) {
		return false;
	}
	
	/**
	 * Replace the node (leaf) or leafs below the node (category)
	 * by deleting the node
	 * @param objectID: can be a categoryID or a leafID
	 * @param replacementID: is always a leafID
	 * @return
	 */
	@Override
	public void replace(Integer objectID, Integer replacementID) {
	}
	
	@Override
	public List<Integer> getReportQueryTypes() {
		List<Integer> queryTypes = new LinkedList<Integer>();
		queryTypes.add(TQueryRepositoryBean.QUERY_PURPOSE.TREE_FILTER);
		queryTypes.add(TQueryRepositoryBean.QUERY_PURPOSE.TQLPLUS_FILTER);
		queryTypes.add(TQueryRepositoryBean.QUERY_PURPOSE.TQL_FILTER);
		return queryTypes;
	}
	
	/**
	 * Add leaf nodes made from leaf beans
	 * @param repository
	 * @param leafBeans
	 * @param personID
	 * @param modifiable
	 * @param tree
	 * @param projectID
	 * @param locale
	 * @param nodes 
	 */
	@Override
	public void addLeafs(Integer repository, List<ILabelBean> leafBeans, Integer personID,
			boolean modifiable, boolean tree, Integer projectID, Locale locale, List<CategoryTO> nodes) {
		if (leafBeans!=null) {
			List<Integer> filterIDs = GeneralUtils.createIntegerListFromBeanList(leafBeans);
			List<TMenuitemQueryBean> menuItemQueries = MenuitemFilterBL.loadByPersonAndQueries(
					personID, filterIDs);
			Map<Integer, TMenuitemQueryBean> menuItemQueryMap = new HashMap<Integer, TMenuitemQueryBean>();
			for (TMenuitemQueryBean menuitemQueryBean : menuItemQueries) {
				menuItemQueryMap.put(menuitemQueryBean.getQueryKey(), menuitemQueryBean);
			}
			Map<Integer, String> styleFieldLabelsMap = getStyleFieldLabelsMap(projectID, locale);
			Map<String, String> viewLabelsMap = getViewLabelMap(LookupContainer.getPersonBean(personID), locale);
			for (TQueryRepositoryBean queryRepositoryBean : (List<TQueryRepositoryBean>)(List)leafBeans) {
				Integer filterID = queryRepositoryBean.getObjectID();
				boolean isPredefined = FilterBL.filterIsPredefined(filterID);
				CategoryTokens categoryTokens = new CategoryTokens(CategoryBL.CATEGORY_TYPE.ISSUE_FILTER_CATEGORY,
						repository, Integer.valueOf(CategoryBL.TYPE.LEAF), filterID);
				CategoryTO categoryTO = null;
				Integer type = queryRepositoryBean.getQueryType();
				if (tree) {
					categoryTO = new CategoryNodeTO(CategoryTokens.encodeNode(categoryTokens),
						CategoryBL.CATEGORY_TYPE.ISSUE_FILTER_CATEGORY, queryRepositoryBean.getLabel(), modifiable, modifiable, true, true);
					((CategoryNodeTO)categoryTO).setIconCls(getIconCls(queryRepositoryBean));
				} else {
					categoryTO = new CategoryGridRowTO(CategoryTokens.encodeNode(categoryTokens), CategoryBL.CATEGORY_TYPE.ISSUE_FILTER_CATEGORY,
							queryRepositoryBean.getLabel(), getTypeLabel(type, locale), modifiable, true);
					categoryTO.setIconCls(getIconCls(queryRepositoryBean));
					String viewID = queryRepositoryBean.getViewID();
					if (viewID!=null) {
						((CategoryGridRowTO)categoryTO).setViewName(viewLabelsMap.get(viewID));
					}
					TMenuitemQueryBean menuitemQueryBean = menuItemQueryMap.get(filterID);
					if (menuitemQueryBean!=null) {
						((CategoryGridRowTO)categoryTO).setIncludeInMenu(menuitemQueryBean.isIncludeInMenu());
						Integer styleField = menuitemQueryBean.getCSSStyleField();
						if (styleField!=null) {
							((CategoryGridRowTO)categoryTO).setStyleFieldLabel(
									styleFieldLabelsMap.get(styleField));
						}
					}
				}
				if (type!=null && TQueryRepositoryBean.QUERY_PURPOSE.TREE_FILTER==type.intValue()) {
					categoryTO.setTreeFilter(Boolean.TRUE);
				} else {
					categoryTO.setTreeFilter(Boolean.FALSE);
				}
				//predefined filters should not be deleted
				categoryTO.setDeletable(modifiable && !isPredefined);
				nodes.add(categoryTO);
			}
		}
	}
	
	/**
	 * Get the icon for a leaf
	 * @param labelBean
	 * @return
	 */
	@Override
	public String getIconCls(ILabelBean labelBean) {
		return FilterBL.getItemFilterIconCls((TQueryRepositoryBean)labelBean);
	}
	
	/**
	 * Gets the style field labels map
	 * @param projectID
	 * @param locale
	 * @return
	 */
	private static Map<Integer, String> getStyleFieldLabelsMap(Integer projectID, Locale locale) {
		Map<Integer, String> fieldLabelsMap = new HashMap<Integer, String>();
		List<IntegerStringBean> styleFields = getStyleFields(projectID, locale);
		for (IntegerStringBean integerStringBean : styleFields) {
			fieldLabelsMap.put(integerStringBean.getValue(), integerStringBean.getLabel());
		}
		return fieldLabelsMap;
	}
	
	/**
	 * Get the list of styleFields (order is important none first)
	 * @param locale
	 * @return
	 */
	protected static List<IntegerStringBean> getStyleFields(Integer projectID, Locale locale) {
		List<Integer> fieldIDs = new LinkedList<Integer>();
		//gets the system lists
		fieldIDs.add(SystemFields.INTEGER_STATE);
		fieldIDs.add(SystemFields.INTEGER_ISSUETYPE);
		fieldIDs.add(SystemFields.INTEGER_PRIORITY);
		fieldIDs.add(SystemFields.INTEGER_SEVERITY);
		Map<Integer, String> fieldLabels = FieldRuntimeBL.getLocalizedDefaultFieldLabels(fieldIDs, locale);
		List<IntegerStringBean> styleFieldLabelsList = new LinkedList<IntegerStringBean>();
		styleFieldLabelsList.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources("common.btn.none", locale), NONE_STYLE_FIELD));
		styleFieldLabelsList.add(new IntegerStringBean(fieldLabels.get(SystemFields.INTEGER_STATE), ListBL.RESOURCE_TYPES.STATUS));
		styleFieldLabelsList.add(new IntegerStringBean(fieldLabels.get(SystemFields.INTEGER_ISSUETYPE), ListBL.RESOURCE_TYPES.ISSUETYPE));
		styleFieldLabelsList.add(new IntegerStringBean(fieldLabels.get(SystemFields.INTEGER_PRIORITY), ListBL.RESOURCE_TYPES.PRIORITY));
		styleFieldLabelsList.add(new IntegerStringBean(fieldLabels.get(SystemFields.INTEGER_SEVERITY), ListBL.RESOURCE_TYPES.SEVERITY));
		if (projectID!=null) {
			//gets the project specific custom lists
			List<TListBean> projectLists = ListBL.getListsByProject(projectID, true);
			if (projectLists!=null) {
				for (TListBean listBean : projectLists) {
					styleFieldLabelsList.add(new IntegerStringBean(listBean.getLabel(), listBean.getObjectID()));
				}
			}
		}
		//gets the global custom lists
		List<TListBean> publicLists = ListBL.loadPublicLists();
		if (publicLists!=null) {
			for (TListBean listBean : publicLists) {
				styleFieldLabelsList.add(new IntegerStringBean(listBean.getLabel(), listBean.getObjectID()));
			}
		}
		return styleFieldLabelsList;
	}
	
	/**
	 * Gets the view names map
	 */
	private static Map<String, String> getViewLabelMap(TPersonBean personBean, Locale locale) {
		Map<String, String> viewNamesMap = new HashMap<String, String>();
		List<LabelValueBean> viewList = getViews(personBean, locale);
		for (LabelValueBean viewBean : viewList) {
			viewNamesMap.put(viewBean.getValue(), viewBean.getLabel());
		}
		return viewNamesMap;
	}
	
	/**
	 * Gets the views available for person
	 * @param personBean
	 * @param appBean
	 * @param locale
	 * @return
	 */
	protected static List<LabelValueBean> getViews(TPersonBean personBean, Locale locale) {
		List<LabelValueBean> viewList = new LinkedList<LabelValueBean>();
		viewList.add(new LabelValueBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources("itemov.viewMode.notLinked", locale), null));
		List<IssueListViewDescriptor> viewDescriptors = ViewDescriptorBL.getViewDescriptors(personBean);
		if (viewDescriptors!=null) {
			for (IssueListViewDescriptor issueListViewDescriptor : viewDescriptors) {
				viewList.add(new LabelValueBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
					"itemov.viewMode."+issueListViewDescriptor.getName(),locale), issueListViewDescriptor.getId()));
			}
		}
		
		return viewList;
	}
	
	/**
	 * Gets the filter type label
	 * @param filtertype
	 * @param locale
	 * @return
	 */
	private static String getTypeLabel(Integer filtertype, Locale locale) {
		if (filtertype!=null) {
			switch (filtertype) {
			case TQueryRepositoryBean.QUERY_PURPOSE.TREE_FILTER:
				return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.queryFilter.lbl.issueFilter", locale);
			case TQueryRepositoryBean.QUERY_PURPOSE.TQLPLUS_FILTER:
				return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.queryFilter.lbl.tqlPlus", locale);
			case TQueryRepositoryBean.QUERY_PURPOSE.TQL_FILTER:
				return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.queryFilter.lbl.tql", locale);
			}
		}
		return "";
	}
	
	/**
	 * Gets the detail json for a leaf
	 * @param includeHeader whether to include the header part (name and other settings)
	 * @param includePathPicker whether to include the patch picker
	 * @param objectID
	 * @param filterType ItemNavigatorBL.QUERY_TYPE
	 * @param queryContextID
	 * @param add
	 * @param modifiable
	 * @param instant
	 * @param clearFilter
	 * @param applyInstant instantly applied from item navigator upper part
	 * @param personBean
	 * @param projectID
	 * @param locale
	 * @return
	 */
	@Override
	public String getDetailJSON(boolean includeHeader, boolean includePathPicker, Integer objectID, Integer filterType, Integer queryContextID, boolean add,
			boolean modifiable, boolean instant, boolean clearFilter, boolean applyInstant,
			TPersonBean personBean, Integer projectID, Locale locale) {
		String label = null;
		String filterExpression = null;
		Integer filterSubtype = null;
		Integer styleField = null;
		String viewID = null;
		boolean includeInMenu = false;
		boolean savedTreeFilter = !(add || instant);
		FilterUpperTO filterUpperTO = null;
		if (filterType!=null) {
			//instant filter in item navigator
			savedTreeFilter = false;
			switch (filterType) {
			case QUERY_TYPE.SAVED:
				savedTreeFilter = true;
				break;
			case QUERY_TYPE.PROJECT_RELEASE:
				filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(objectID, false, true, false);
				filterUpperTO.setUpperFields(FilterBL.getUpperFilterFields());
				Integer[] selectedProjects =  filterUpperTO.getSelectedProjects();
				if (selectedProjects!=null && selectedProjects.length>0) {
					projectID = selectedProjects[0];
				}
			 	List<TStateBean> notClosedStateBeans = StatusBL.loadNotClosedStates();
				List<Integer> notClosedStateIDs = GeneralUtils.createIntegerListFromBeanList(notClosedStateBeans);
				Integer[] notClosedStatesArr = GeneralUtils.createIntegerArrFromCollection(notClosedStateIDs);
				filterUpperTO.setSelectedStates(notClosedStatesArr);
				FilterSelectsListsLoader.loadFilterSelects(filterUpperTO, personBean, locale, true, false);
				LOGGER.debug("Load project/relase based (" + objectID + ") for item navigator");
				return FilterJSON.getTreeFilterJSON(label, null,
						styleField, null, 
						includeInMenu, viewID, null, modifiable,
						filterUpperTO, FilterSelectsListsLoader.hasViewWatcherRightInAnyProject(personBean), 
						null,
						personBean.getObjectID(), projectID, locale);
			case QUERY_TYPE.LUCENE_SEARCH:
				if (queryContextID!=null) {
					TLastExecutedQueryBean lastExecutedQueryBean = LastExecutedBL.loadByPrimaryKey(queryContextID);
					TCLOBBean clobBean = ClobBL.loadByPrimaryKey(lastExecutedQueryBean.getQueryClob());
					if (clobBean!=null) {
						filterExpression = clobBean.getCLOBValue();
						if (filterExpression!=null) {
							filterUpperTO = new FilterUpperTO();
							filterUpperTO.setKeyword(filterExpression);
							filterUpperTO.setKeywordIncluded(LuceneUtil.isUseLucene());
							FilterSelectsListsLoader.loadFilterSelects(filterUpperTO, personBean, locale, true, false);
							LOGGER.debug("Load full text search based (" + filterExpression + ") for item navigator");
							return FilterJSON.getTreeFilterJSON(label, null,
									styleField, null, 
									includeInMenu, viewID, null, modifiable,
									filterUpperTO, FilterSelectsListsLoader.hasViewWatcherRightInAnyProject(personBean), 
									null,
									personBean.getObjectID(), projectID, locale);
						}
					}
				}
				break;
			case QUERY_TYPE.STATUS:
				filterUpperTO = new FilterUpperTO();
				filterUpperTO.setSelectedStates(new Integer[] {objectID});
				filterUpperTO.setUpperFields(FilterBL.getUpperFilterFields());
				FilterSelectsListsLoader.loadFilterSelects(filterUpperTO, personBean, locale, false, false);
				LOGGER.debug("Load status based (" + objectID + ") for item navigator");
				return FilterJSON.getTreeFilterJSON(label, null,
						styleField, null, 
						includeInMenu, viewID, null, modifiable,
						filterUpperTO, FilterSelectsListsLoader.hasViewWatcherRightInAnyProject(personBean), 
						null,
						personBean.getObjectID(), projectID, locale);
			default:
				break;
			}
		}
		if (add) {
			//only report filters can be added (no TQL or TQLPLus)
			//set default filter subtype		
			filterSubtype = TQueryRepositoryBean.QUERY_PURPOSE.TREE_FILTER;
		} else {
			if (objectID!=null && (filterType==null || savedTreeFilter)) {
				//edit existing filter (from manage filters or from item navigator)
				TQueryRepositoryBean queryRepositoryBean = (TQueryRepositoryBean)getByKey(objectID);
				if (queryRepositoryBean!=null) {
					label = LocalizeUtil.localizeDropDownEntry(queryRepositoryBean, locale);
					if (queryRepositoryBean.getQueryType()==null) {
						filterSubtype = TQueryRepositoryBean.QUERY_PURPOSE.TREE_FILTER;
					} else {
						filterSubtype = queryRepositoryBean.getQueryType().intValue();
					}
					TMenuitemQueryBean menuitemQueryBean = MenuitemFilterBL.loadByPersonAndQuery(
							personBean.getObjectID(), objectID);
					if (menuitemQueryBean!=null){
						includeInMenu = menuitemQueryBean.isIncludeInMenu();
						styleField = menuitemQueryBean.getCSSStyleField();
					}
					filterExpression = loadFilterExpression(queryRepositoryBean.getQueryKey());
					viewID = queryRepositoryBean.getViewID();
				}
			}
		}
        if (clearFilter)    {
            filterExpression = null;
        } else {
        	if (instant==true) {
    			filterExpression=getLastExecutedInstantQuery(personBean.getObjectID());
    		}
        }
		if (styleField==null) {
			//NONE by default
			styleField = NONE_STYLE_FIELD;
		}
		boolean withParameter = !instant && !applyInstant;
		return getFilterJSON(includeHeader, includePathPicker, label, filterType, filterSubtype, styleField,
				includeInMenu, viewID, filterExpression, add, modifiable, withParameter, personBean, projectID, locale);
	}
	
	/**
	 * Gets the filter JSON for different item filter implementations
	 * @param includeHeader whether to include the header part (name and other settings)
	 * @param includePathPicker whether to include the patch picker
	 * @param label
	 * @param filterType
	 * @param filterSubtype
	 * @param styleField
	 * @param includeInMenu
	 * @param viewID
	 * @param queryExpression
	 * @param add
	 * @param modifiable
	 * @param withParameter
	 * @param personBean
	 * @param projectID
	 * @param locale
	 * @return
	 */
	protected abstract String getFilterJSON(boolean includeHeader, boolean includePathPicker, String label, Integer filterType, Integer filterSubtype, Integer styleField,
			boolean includeInMenu, String viewID, String queryExpression,
			boolean add, boolean modifiable, boolean withParameter,
			TPersonBean personBean, Integer projectID, Locale locale);

	/**
	 * Gets the last executed instant query
	 * @param personID
	 * @return
	 */
	public static String getLastExecutedInstantQuery(Integer personID){
		String result=null;
		List<TLastExecutedQueryBean> lastQueries=LastExecutedBL.loadByPerson(personID);
		if(lastQueries!=null){
			for (int i=0;i<lastQueries.size();i++){
				TLastExecutedQueryBean query=lastQueries.get(i);
				Integer queryType=query.getQueryType();
				if(queryType!=null&&queryType.intValue()==ItemNavigatorBL.QUERY_TYPE.INSTANT){
					TCLOBBean clobBean = ClobBL.loadByPrimaryKey(query.getQueryClob());
					if(clobBean!=null){
						result=clobBean.getCLOBValue();
						break;
					}
				}
			}
		}
		return result;
	}

}
