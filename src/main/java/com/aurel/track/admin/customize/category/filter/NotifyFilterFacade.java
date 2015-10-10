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


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

import com.aurel.track.admin.customize.category.CategoryBL;
import com.aurel.track.admin.customize.category.CategoryGridRowTO;
import com.aurel.track.admin.customize.category.CategoryNodeTO;
import com.aurel.track.admin.customize.category.CategoryTO;
import com.aurel.track.admin.customize.category.CategoryTokens;
import com.aurel.track.admin.customize.category.filter.tree.design.FieldExpressionInTreeTO;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.TreeFilterLoaderBL;
import com.aurel.track.admin.customize.category.filter.tree.design.TreeFilterSaverBL;
import com.aurel.track.admin.customize.category.filter.tree.io.TreeFilterReader;
import com.aurel.track.admin.customize.category.filter.tree.io.TreeFilterString;
import com.aurel.track.admin.customize.category.filter.tree.io.TreeFilterWriter;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;

/**
 * An implementation of FilterFacade for notification filters (tree or TQL)
 * @author Tamas Ruff
 *
 */
public class NotifyFilterFacade extends FilterFacade {
	
	private static NotifyFilterFacade instance;
	
	private NotifyFilterFacade() {
		super();
	}
	
	/**
	 * Gets the label key for this type
	 */
	@Override
	public String getLabelKey() {
		return "admin.customize.automail.filter.lblOperation";
	}
	
	/**
	 * Get the human readable format of the field expression
	 * @param filterExpression
	 * @param locale
	 */	
	@Override
	public String getFilterExpressionString(String filterExpression, Locale locale) {
		QNode qNode = TreeFilterReader.getInstance().readQueryTree(filterExpression);
		return TreeFilterString.toQueryString(qNode, locale);
	}
	
	/**
	 * Does the category has content (subcategories or leafs)
	 * @param objectID
	 * @return
	 */
	@Override
	public boolean replaceNeeded(Integer objectID) {
		return queryRepositoryDAO.hasDependentData(objectID);
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
		queryRepositoryDAO.replaceAndDelete(objectID, replacementID);
	}
	
	/**
	 * Return a FieldConfigItemFacade instance which implements the ConfigItemFacade 
	 * @return
	 */
	public static NotifyFilterFacade getInstance(){
		if(instance==null){
			instance=new NotifyFilterFacade();
		}
		return instance;
	}
	
	@Override
	public List<Integer> getReportQueryTypes() {
		List<Integer> queryTypes = new LinkedList<Integer>();
		queryTypes.add(TQueryRepositoryBean.QUERY_PURPOSE.NOTIFY_FILTER);
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
			boolean modifiable, boolean tree, Integer projectID,  Locale locale, List<CategoryTO> nodes) {
		if (leafBeans!=null) {
			for (TQueryRepositoryBean queryRepositoryBean : (List<TQueryRepositoryBean>)(List)leafBeans) {
				CategoryTokens categoryTokens = new CategoryTokens(CategoryBL.CATEGORY_TYPE.NOTIFICATION_FILTER_CATEGORY,
						repository, Integer.valueOf(CategoryBL.TYPE.LEAF), queryRepositoryBean.getObjectID());
				CategoryTO categoryTO = null;
				if (tree) {
					categoryTO = new CategoryNodeTO(CategoryTokens.encodeNode(categoryTokens),
					CategoryBL.CATEGORY_TYPE.NOTIFICATION_FILTER_CATEGORY, queryRepositoryBean.getLabel(), modifiable, modifiable, true, true);
					((CategoryNodeTO)categoryTO).setIconCls(getIconCls(queryRepositoryBean));
				} else {
					categoryTO = new CategoryGridRowTO(CategoryTokens.encodeNode(categoryTokens), CategoryBL.CATEGORY_TYPE.NOTIFICATION_FILTER_CATEGORY,
							queryRepositoryBean.getLabel(), getTypeLabel(queryRepositoryBean.getQueryType(), locale), modifiable, true);
					categoryTO.setIconCls(getIconCls(queryRepositoryBean));
				}
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
		return FilterBL.ICONS.TREE_FILTER;
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
			case TQueryRepositoryBean.QUERY_PURPOSE.NOTIFY_FILTER:
				return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.automail.filter.lblAlone", locale);
			}
		}
		return "";
	}
	
	/**
	 * Gets the detail json for a leaf
	 * @param includeHeader whether to include the header part (name and other settings)
	 * @param includePathPicker whether to include the patch picker
	 * @param objectID
	 * @param filterType
	 * @param queryContextID
	 * @param add
	 * @param modifiable
	 * @param instant
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
		//Integer filterSubtype = TQueryRepositoryBean.QUERY_PURPOSE.NOTIFY_FILTER;
		if (!add) {
			TQueryRepositoryBean queryRepositoryBean = (TQueryRepositoryBean)getByKey(objectID);
			if (queryRepositoryBean!=null) {
				label = queryRepositoryBean.getLabel();
				filterExpression = loadFilterExpression(queryRepositoryBean.getQueryKey());
			}
		}
		return getFilterJSON(/*includeHeader, includePathPicker,*/ label, /*filterType, filterSubtype, null,*/
				/*false, null,*/ filterExpression, add, modifiable, /*false,*/ personBean, /*projectID,*/ locale);
	}
	
	/**
	 * Gets the detail json for a notify filter
	 * @param includeHeader whether to include the header part (name and other settings)
	 * @param includePathPicker whether to include the patch picker
	 * @param label
	 * @param filterType
     * @param filterSubtype
     * @param filterSubtype
     * @param includeInMenu
     * @param filterExpression
	 * @param add
	 * @param modifiable
     * @param instant
	 * @param personBean
     * @param projectID
	 * @param locale
	 * @return
	 */
	private String getFilterJSON(/*boolean includeHeader, boolean includePathPicker,*/ String label, /*Integer filterType, Integer filterSubtype, Integer styleField,*/
			/*boolean includeInMenu,  String viewID,*/ String filterExpression, boolean add, boolean modifiable,
			/*boolean withParameter,*/ TPersonBean personBean, /*Integer projectID,*/ Locale locale) {
		List<FieldExpressionInTreeTO> fieldExpressionInTreeList = new ArrayList<FieldExpressionInTreeTO>();
		if (!add) {
			//edit existing query
			//load the other, project dependent lists' datasources  for all accessible projects 
			List<TProjectBean> projectBeans = ProjectBL.loadUsedProjectsFlat(personBean.getObjectID());
			Integer[] projectIDs = GeneralUtils.createIntegerArrFromCollection(GeneralUtils.createIntegerListFromBeanList(projectBeans));			
			QNode rootNode = TreeFilterReader.getInstance().readQueryTree(filterExpression);
			fieldExpressionInTreeList = new ArrayList<FieldExpressionInTreeTO>();
			TreeFilterLoaderBL.transformTreeToExpressionList(rootNode, 
					new Stack<QNode>(), new Stack<FieldExpressionInTreeTO>(), fieldExpressionInTreeList);
			FieldExpressionBL.loadFilterExpressionInTreeList(fieldExpressionInTreeList, projectIDs, null, modifiable, personBean, locale, false, true);
		}
		return FilterJSON.getNotifyFilterJSON(label,
			modifiable, fieldExpressionInTreeList,
			personBean.getObjectID(), locale);
	}
	
	/**
	 * Gets the specific expression
	 * @param filterExpression
	 * @param filterSelectsTO
	 * @param fieldExpressionInTreeList
	 * @return
	 */
	@Override
	public String getFilterExpression(String filterExpression,
			FilterUpperTO filterSelectsTO, List<FieldExpressionInTreeTO> fieldExpressionInTreeList) throws Exception {
		QNode node = TreeFilterSaverBL.transformExpressionListToTree(fieldExpressionInTreeList, new Stack<QNode>());		
		return TreeFilterWriter.getInstance().toXML(node);
	}
}
