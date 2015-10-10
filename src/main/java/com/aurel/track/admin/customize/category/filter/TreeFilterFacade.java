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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import com.aurel.track.admin.customize.category.CategoryBL.CATEGORY_TYPE;
import com.aurel.track.admin.customize.category.CategoryPathPickerBL;
import com.aurel.track.admin.customize.category.filter.tree.design.FieldExpressionInTreeTO;
import com.aurel.track.admin.customize.category.filter.tree.design.FieldExpressionSimpleTO;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterSelectsListsLoader;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperFromQNodeTransformer;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.TreeFilterLoaderBL;
import com.aurel.track.admin.customize.category.filter.tree.design.TreeFilterSaverBL;
import com.aurel.track.admin.customize.category.filter.tree.io.TreeFilterReader;
import com.aurel.track.admin.customize.category.filter.tree.io.TreeFilterString;
import com.aurel.track.admin.customize.category.filter.tree.io.TreeFilterWriter;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.TreeNode;

/**
 * An implementation of FilterFacade for Tree filters
 * @author Tamas Ruff
 *
 */
public class TreeFilterFacade extends IssueFilterFacade {
	
	private static TreeFilterFacade instance;
	
	private TreeFilterFacade() {
		super();
	}

	/**
	 * Return a FieldConfigItemFacade instance which implements the ConfigItemFacade 
	 * @return
	 */
	public static TreeFilterFacade getInstance(){
		if(instance==null){
			instance=new TreeFilterFacade();
		}
		return instance;
	}
		
	/**
	 * Gets the label key for this type
	 */
	@Override
	public String getLabelKey() {
		return "admin.customize.queryFilter.lbl.issueFilter";
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
	 * Gets the detail json for a tree filter
	 * @param includeHeader whether to include the header part (name and other settings)
	 * @param includePathPicker whether to include the patch picker
	 * @param label
	 * @param filterType
	 * @param filterSubtype
	 * @param styleField
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
	@Override
	public String getFilterJSON(boolean includeHeader, boolean includePathPicker, String label, Integer filterType, Integer filterSubtype, Integer styleField,
			boolean includeInMenu,  String viewID, String filterExpression,
			boolean add, boolean modifiable, boolean withParameter,
			TPersonBean personBean, Integer projectID, Locale locale) {
		FilterUpperTO filterUpperTO;
		List<FieldExpressionInTreeTO> fieldExpressionInTreeList = new ArrayList<FieldExpressionInTreeTO>();
		List<Integer> upperFilterFields = FilterBL.getUpperFilterFields();
		Map<Integer, Integer> upperCustomSelectSimpleFields = FilterSelectsListsLoader.getUpperCustomSelectSimpleFieldsMap(upperFilterFields);
		if (add || (!withParameter && filterExpression==null)) {
			filterUpperTO = new FilterUpperTO();
			//create a new or instant filter
			//set upper fields to load only those selects which are set as filter fields
			filterUpperTO.setUpperFields(upperFilterFields);
			filterUpperTO.setCustomSelectSimpleFields(upperCustomSelectSimpleFields);
			//gather the non simple select upper fields for getting the labels for them at once
			Set<Integer> nonSelectUpperFields = new HashSet<Integer>();
			Set<Integer> directProcessFields = FilterBL.getDirectProcessFields();
			for (Integer fieldID: upperFilterFields) {
				if (!directProcessFields.contains(fieldID) && !upperCustomSelectSimpleFields.keySet().contains(fieldID)) {
					nonSelectUpperFields.add(fieldID);
				}
			}
			FilterSelectsListsLoader.loadFilterSelects(filterUpperTO, personBean, locale, true, withParameter);
			//load the not select type other upper fields as FieldExpressionSimpleTO 
			List<FieldExpressionSimpleTO> fieldExpressionSimpleTOList =
				new LinkedList<FieldExpressionSimpleTO>();
			//get the labels for them
			Map<Integer, String> fieldLabelsMap = FieldRuntimeBL.getLocalizedDefaultFieldLabels(
					GeneralUtils.createListFromSet(nonSelectUpperFields), locale);
			for (Integer fieldID: upperFilterFields) {
				if (!directProcessFields.contains(fieldID) &&
						(filterUpperTO.getCustomSelectSimpleFields()==null || !filterUpperTO.getCustomSelectSimpleFields().keySet().contains(fieldID))) {
					FieldExpressionSimpleTO filterExpressionSimpleTO = 
						FieldExpressionBL.loadFilterExpressionSimple(fieldID, filterUpperTO.getSelectedProjects(), filterUpperTO.getSelectedIssueTypes(), null, modifiable,
								withParameter, personBean, locale, fieldLabelsMap, null);
					fieldExpressionSimpleTOList.add(filterExpressionSimpleTO);
				}
			}
			filterUpperTO.setFieldExpressionSimpleList(fieldExpressionSimpleTOList);
			//TODO add filterExpressions in tree for instant from the session
			if (!withParameter) {
				
			}
		} else {
			//edit existing filter
			QNode extendedRootNode = TreeFilterReader.getInstance().readQueryTree(filterExpression);
			filterUpperTO = FilterUpperFromQNodeTransformer.getFilterSelectsFromTree(extendedRootNode, modifiable || !withParameter, withParameter, personBean, locale, false);
			Map<Integer, Integer> upperCustomSelectSimpleFieldsWithValues = filterUpperTO.getCustomSelectSimpleFields();
			if (upperCustomSelectSimpleFields!=null && !upperCustomSelectSimpleFields.isEmpty()) {
				if (upperCustomSelectSimpleFieldsWithValues==null || upperCustomSelectSimpleFieldsWithValues.isEmpty()) {
					filterUpperTO.setCustomSelectSimpleFields(upperCustomSelectSimpleFields);
				} else {
					for (Integer upperSelectField : upperCustomSelectSimpleFields.keySet()) {
						if (!upperCustomSelectSimpleFieldsWithValues.keySet().contains(upperSelectField)) {
							upperCustomSelectSimpleFieldsWithValues.put(upperSelectField, upperCustomSelectSimpleFields.get(upperSelectField));
						}
					}
				}
			}
			FilterSelectsListsLoader.loadFilterSelects(filterUpperTO, personBean, locale, false, withParameter);
			QNode rootNode = TreeFilterLoaderBL.getOriginalTree(extendedRootNode);
			fieldExpressionInTreeList = new ArrayList<FieldExpressionInTreeTO>();
			TreeFilterLoaderBL.transformTreeToExpressionList(rootNode, 
					new Stack<QNode>(), new Stack<FieldExpressionInTreeTO>(), fieldExpressionInTreeList);
			Integer[] projectIDs = filterUpperTO.getSelectedProjects();
			Integer[] itemTypeIDs = filterUpperTO.getSelectedIssueTypes();
			FieldExpressionBL.loadFilterExpressionInTreeList(fieldExpressionInTreeList, projectIDs, itemTypeIDs, modifiable || !withParameter, personBean, locale, withParameter, false);
		}
		boolean hasViewWatcherRightInAnyProject = FilterSelectsListsLoader.hasViewWatcherRightInAnyProject(personBean);
		List<IntegerStringBean> styleFields = null;
		List<LabelValueBean> views = null;
		if (includeHeader) {
			//the style and view are not rendered in item navigator
			styleFields = getStyleFields(projectID, locale);
			views = getViews(personBean, locale);
		}
		List<TreeNode> pathNodes = null;
		if (includePathPicker) {
			pathNodes = CategoryPathPickerBL.getPickerNodesEager(personBean, locale, CATEGORY_TYPE.ISSUE_FILTER_CATEGORY);
		}
		return FilterJSON.getTreeFilterJSON(label, pathNodes,
			styleField, styleFields, 
			includeInMenu, viewID, views, modifiable,
			filterUpperTO, hasViewWatcherRightInAnyProject, 
			fieldExpressionInTreeList,
			personBean.getObjectID(), projectID, locale);
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
		QNode rootNode = TreeFilterSaverBL.transformExpressionListToTree(fieldExpressionInTreeList, new Stack<QNode>());
		QNode extendedNode = TreeFilterSaverBL.createQNodeWithQueryListsTO(rootNode, filterSelectsTO, null);
		return TreeFilterWriter.getInstance().toXML(extendedNode);
	}
}
