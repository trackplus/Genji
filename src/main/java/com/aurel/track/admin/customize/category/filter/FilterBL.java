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

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.aurel.track.admin.customize.category.CategoryBL;
import com.aurel.track.admin.customize.category.CategoryTokens;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.io.TreeFilterReader;
import com.aurel.track.admin.customize.localize.LocalizeBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.ISortedBean;
import com.aurel.track.beans.TCLOBBean;
import com.aurel.track.beans.TNavigatorLayoutBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.QueryRepositoryDAO;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.itemNavigator.ItemNavigatorBL.QUERY_TYPE;
import com.aurel.track.itemNavigator.filterInMenu.FilterInMenuTO;
import com.aurel.track.itemNavigator.layout.NavigatorLayoutBL;
import com.aurel.track.itemNavigator.viewPlugin.ViewDescriptorBL;
import com.aurel.track.plugin.IssueListViewDescriptor;
import com.aurel.track.resources.LocalizationKeyPrefixes;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;

/**
 * Class for filter utility methods
 * @author Tamas
 *
 */
public class FilterBL {
    public static final String MY_MENU_FILTERS_JSON = "myMenuFiltersJSON";
    public static final String LAST_EXECUTED_FILTERS_JSON = "lastExecutedFiltersJSON";

    private static QueryRepositoryDAO queryRepositoryDAO = DAOFactory.getFactory().getQueryRepositoryDAO();
	
    /**
	 * The repository type
	 * @author Tamas
	 *
	 */
	public static interface FILTER_TYPE {
		public static final int ISSUE_FILTER = 0;
		public static final int NOTIFY_FILTER = 1;
	}

	/**
	 * Icons classes for different filter types
	 * @author Tamas
	 *
	 */
	public static interface ICONS {
		static String TREE_FILTER = "treeFilter-ticon";
		static String TQL_PLUS_FILTER = "tqlPlusFilter-ticon";
		static String TQL_FILTER = "tqlFilter-ticon";
	}

	/**
	 * to different FilterUpperTO and QNode objects: one for execute and one for rendering, because 
	 *	1. during execute the filterUpperTO will be changed
	 *	(select child projects of selected projects, select issue types, replace symbolic values)
	 *	which is not desired by re-rendering the filter in the upper part
	 *	2. the submitted values does not contain the lookup values for list entries, matchers etc.
	 *	so the filterUpperApplyEdit and treeApplyEdit should be prepared for re-rendering after apply
	 *	filterUpperApplyEdit and treeApplyEdit would be not needed if the filter configuration in the item navigator would not be reloaded after each apply
	 */
	public static final String FILTER_UPPER_APPLY_EXECUTE = "filterUpperApplyExecute";
	//public static final String FILTER_UPPER_APPLY_EDIT = "filterUpperApplyEdit";
	public static final String TREE_APPLY_EXECUTE = "treeApplyExecute";
	//public static final String TREE_APPLY_EDIT = "treeApplyEdit";
		
	/**
	 * Loads a queryRepositoryBean by primary key
	 * @param objectID
	 * @return
	 */
	public static TQueryRepositoryBean loadByPrimaryKey(Integer objectID) {
		return queryRepositoryDAO.loadByPrimaryKey(objectID);
	}
	
	/**
	 * Loads a queryRepositoryBean by primary keys
	 * @param objectIDs
	 * @return
	 */
	public static List<TQueryRepositoryBean> loadByPrimaryKeys(List<Integer> objectIDs) {
		return queryRepositoryDAO.loadByPrimaryKeys(objectIDs);
	}
	
	/**
	 * Loads the hardcoded filters
	 * @return
	 */
	public static List<TQueryRepositoryBean> loadHardcodedFilters() {
		return queryRepositoryDAO.loadHardcodedFilters();
	}

	public static boolean filterIsPredefined(Integer filterID) {
		 return filterID!=null && filterID.intValue()<0;
	}
	
	/**
	 * Gets the iconCls for item filters
	 * @param labelBean
	 * @return
	 */
	public static String getItemFilterIconCls(TQueryRepositoryBean queryRepositoryBean) {
		Integer queryType = queryRepositoryBean.getQueryType();
		String iconName = null;
		if (queryType!=null) {
			switch (queryType.intValue()) {
			case TQueryRepositoryBean.QUERY_PURPOSE.TREE_FILTER:
				String viewID = queryRepositoryBean.getViewID();
				if (viewID!=null) {
					IssueListViewDescriptor issueListViewDescriptor = ViewDescriptorBL.getDescriptor(viewID);
					if (issueListViewDescriptor!=null) {
						iconName = issueListViewDescriptor.getIconCls();
					}
				}
				if (iconName==null) {
					iconName = FilterBL.ICONS.TREE_FILTER;
				}
				break;
			case TQueryRepositoryBean.QUERY_PURPOSE.TQLPLUS_FILTER:
				iconName = FilterBL.ICONS.TQL_PLUS_FILTER;
				break;
			case TQueryRepositoryBean.QUERY_PURPOSE.TQL_FILTER:
				iconName = FilterBL.ICONS.TQL_FILTER;
				break;
			}
		}
		return iconName;
	}
	
	/**
	 * Localize and sort the filter list
	 * @param filterBeans
	 * @param locale
	 * @return
	 */
	public static List<ILabelBean> localizeAndSort(List filterBeans, Locale locale) {
		LocalizeUtil.localizeDropDownList(filterBeans, locale);
		//TODO remove sort if sortOrder added as DB field
		Collections.sort(filterBeans, new Comparator<ISortedBean>() {
			public int compare(ISortedBean filterBean1, ISortedBean filterBean2) {
				Comparable sortOrderValue1 = filterBean1.getSortOrderValue();
				Comparable sortOrderValue2 = filterBean2.getSortOrderValue();
				if ((sortOrderValue1==null) && (sortOrderValue2==null)) {
					return 0;
				}
				if (sortOrderValue1==null) {
					return 1;
				}
				if (sortOrderValue2==null) {
					return -1;
				}
				return sortOrderValue1.compareTo(sortOrderValue2);
				}});
		return filterBeans;
	}
	/**
	 * Saves a new or existing filter
	 * @param filterFacade
     * @param categoryTokens
	 * @param label
	 * @param filterSubtype
     * @param filterExpression
     * @param viewID
	 * @param add
	 * @param personID
	 * @param locale
	 */
	static CategoryTokens save(FilterFacade filterFacade, CategoryTokens categoryTokens, String label, Integer filterSubtype,
			String filterExpression, String viewID, boolean add, Integer personID, Locale locale) {
		TQueryRepositoryBean queryRepositoryBean = (TQueryRepositoryBean)filterFacade.prepareBeanAfterAddEdit(
				categoryTokens, label, personID, add);
		queryRepositoryBean.setQueryType(filterSubtype);
		if (viewID!=null && "".equals(viewID)) {
			viewID = null;
		}
		if (!add) {
			String oldViewID = queryRepositoryBean.getViewID();
			if (oldViewID!=null) {
				if (viewID==null || !oldViewID.equals(viewID)) {
					//delete the old filter and view based layout
					TNavigatorLayoutBean oldLayoutBean = NavigatorLayoutBL.loadByFilterWithView(queryRepositoryBean.getObjectID(), QUERY_TYPE.SAVED);
					if (oldLayoutBean!=null) {
						NavigatorLayoutBL.delete(oldLayoutBean.getObjectID());
					}
				}
			}
		}
		queryRepositoryBean.setViewID(viewID);
		Integer queryKey = saveFilterExpression(queryRepositoryBean.getQueryKey(), filterExpression);
		queryRepositoryBean.setQueryKey(queryKey);
		Integer filterID = filterFacade.save(queryRepositoryBean);
		//default locale
		LocalizeBL.saveLocalizedResource(
				new TQueryRepositoryBean().getKeyPrefix(), filterID, label, null);
		//actual locale
		LocalizeBL.saveLocalizedResource(
				new TQueryRepositoryBean().getKeyPrefix(), filterID, label, locale);
		categoryTokens.setObjectID(filterID);
		categoryTokens.setType(CategoryBL.TYPE.LEAF);
		return categoryTokens;
	}
	
	/**
	 * Saves a clob
	 * @param clobID
	 * @param filterExpression
	 * @return
	 */
	private static Integer saveFilterExpression(Integer clobID, String filterExpression) {
		TCLOBBean clobBean = null;
		if (clobID!=null) {
			clobBean = ClobBL.loadByPrimaryKey(clobID);
		}
		if (clobBean==null) {
			clobBean = new TCLOBBean();
		}
		clobBean.setCLOBValue(filterExpression);
		return ClobBL.save(clobBean);
	}	
	
	/**
	 * Gets the configured upper fields
	 * @return
	 */
	public static List<Integer> getUpperFilterFields() {
		return GeneralUtils.createIntegerListFromBeanList(FieldBL.loadFilterFields());
	}

	/**
	 * Loads the filters set to appear in menu
	 * @param personID
	 * @param locale
	 * @return
	 */
	public static List<TQueryRepositoryBean> loadMyMenuFilters(Integer personID, Locale locale) {
		int[] queryTypes=new int[] {
				TQueryRepositoryBean.QUERY_PURPOSE.TREE_FILTER,
				TQueryRepositoryBean.QUERY_PURPOSE.TQL_FILTER,
				TQueryRepositoryBean.QUERY_PURPOSE.TQLPLUS_FILTER};
		return (List)localizeAndSort(queryRepositoryDAO.loadMenuitemQueries(personID, queryTypes, true), locale);
	}
	
	/**
	 * Loads all filters to be shown in the menu flat
	 * @param personID
	 * @param locale
	 * @return
	 */
	public static List<FilterInMenuTO> loadMyMenuFiltersWithTooltip(TPersonBean personBean, Locale locale) {
		Integer personID = personBean.getObjectID();
		List<TQueryRepositoryBean> myMenuFilters = loadMyMenuFilters(personID, locale);
		List<FilterInMenuTO> filterInMenuTOList = new LinkedList<FilterInMenuTO>();
		for (TQueryRepositoryBean queryRepositoryBean : myMenuFilters) {
			Integer filterID = queryRepositoryBean.getObjectID();
			FilterInMenuTO filterInMenuTO = new FilterInMenuTO();
			filterInMenuTO.setObjectID(filterID);
			filterInMenuTO.setType(QUERY_TYPE.SAVED);
			filterInMenuTO.setIconCls(getItemFilterIconCls(queryRepositoryBean));
			filterInMenuTO.setMaySaveFilterLayout(queryRepositoryBean.getViewID()!=null && NavigatorLayoutBL.isModifiable(queryRepositoryBean, personBean));
			String localizedLabel = LocalizeUtil.getLocalizedEntity(
					LocalizationKeyPrefixes.FILTER_LABEL_PREFIX, filterID, locale);
			String label = null;
			if (localizedLabel!=null && localizedLabel.length()>0) {
				label = localizedLabel;
			} else {
				label = queryRepositoryBean.getLabel();
			}
			filterInMenuTO.setLabel(label);
			String localizedTooltip = LocalizeUtil.getLocalizedEntity(
					LocalizationKeyPrefixes.FILTER_TOOLTIP_PREFIX, filterID, locale);
			if (localizedTooltip!=null && localizedTooltip.length()>0) {
				filterInMenuTO.setTooltip(localizedTooltip);
			} else {
				String filterExpression = getFilterExpression(queryRepositoryBean);
				String tooltip = TreeFilterFacade.getInstance().getFilterExpressionString(filterExpression, locale);
				if (tooltip!=null) {
					filterInMenuTO.setTooltip(tooltip);
				}
			}
			filterInMenuTOList.add(filterInMenuTO);
		}
		return filterInMenuTOList;
	}
	
	/**
	 * Those fields whose matcher values are processed directly 
	 * (not according to the matcher linked with the field type)
	 * because they are either pseudo fields (no matcher for pseudo fields)
	 * or they are selects but in this case multiple selects while the 
	 * select field's matcher implementation works with single values 
	 * @return
	 */
	public static Set<Integer> getDirectProcessFields() {
		Set<Integer> processedFields = new HashSet<Integer>();
		processedFields.add(SystemFields.INTEGER_PROJECT);
		processedFields.add(SystemFields.INTEGER_RELEASESCHEDULED);
		processedFields.add(SystemFields.INTEGER_RELEASENOTICED);
		processedFields.add(SystemFields.INTEGER_MANAGER);
		processedFields.add(SystemFields.INTEGER_RESPONSIBLE);
		processedFields.add(SystemFields.INTEGER_ORIGINATOR);
		processedFields.add(SystemFields.INTEGER_CHANGEDBY);
		processedFields.add(SystemFields.INTEGER_CHANGEDBY);
		processedFields.add(SystemFields.INTEGER_STATE);
		processedFields.add(SystemFields.INTEGER_ISSUETYPE);
		processedFields.add(SystemFields.INTEGER_PRIORITY);
		processedFields.add(SystemFields.INTEGER_SEVERITY);
		processedFields.add(SystemFields.INTEGER_ARCHIVELEVEL);
		processedFields.add(Integer.valueOf(FilterUpperTO.PSEUDO_FIELDS.RELEASE_TYPE_SELECTOR));
		processedFields.add(Integer.valueOf(FilterUpperTO.PSEUDO_FIELDS.SHOW_CLOSED_RELEASES));
		processedFields.add(Integer.valueOf(FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID));
		processedFields.add(Integer.valueOf(FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_OR_INFORMANT_SELECTOR));
		processedFields.add(Integer.valueOf(FilterUpperTO.PSEUDO_FIELDS.KEYWORD_FIELD_ID));
		processedFields.add(Integer.valueOf(FilterUpperTO.PSEUDO_FIELDS.ARCHIVED_FIELD_ID));
		processedFields.add(Integer.valueOf(FilterUpperTO.PSEUDO_FIELDS.DELETED_FIELD_ID));
		processedFields.add(Integer.valueOf(FilterUpperTO.PSEUDO_FIELDS.LINKTYPE_FILTER_SUPERSET));
		return processedFields;
	}
	
	/**
	 * Gets the filter expression by filterID
	 * @param queryRepositoryBean
	 * @return
	 */
	public static String getFilterExpression(TQueryRepositoryBean queryRepositoryBean) {
		if (queryRepositoryBean!=null) {
			Integer clobID = queryRepositoryBean.getQueryKey();
			FilterFacade filterFacade = TreeFilterFacade.getInstance();
			return filterFacade.loadFilterExpression(clobID);
		}
		return null;
	}

	/**
	 * Loads the filter expression by clobID
	 * @param queryRepositoryBean
	 * @return
	 */
	public static QNode loadNode(TQueryRepositoryBean  queryRepositoryBean) {
		String filterExpression = getFilterExpression(queryRepositoryBean);
		return TreeFilterReader.getInstance().readQueryTree(filterExpression);
	}

	
	
}
