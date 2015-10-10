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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.customize.category.filter.tree.design.FieldExpressionInTreeTO;
import com.aurel.track.admin.customize.category.filter.tree.design.FieldExpressionSimpleTO;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterSelectsListsLoader;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO.ARCHIVED_FILTER;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO.DELETED_FILTER;
import com.aurel.track.admin.customize.lists.ListBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.project.release.ReleaseBL;
import com.aurel.track.admin.project.release.ReleaseConfigBL;
import com.aurel.track.admin.user.group.GroupConfigBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.runtime.callbackInterfaces.ILookup;
import com.aurel.track.fieldType.runtime.system.select.SystemResponsibleRT;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.TreeNode;

/**
 * JSON utility for filters
 * @author Tamas Ruff
 *
 */
public class FilterJSON {
	
	static interface JSON_FIELDS {
		String MODIFIABLE = "modifiable";
		String PATH_NODES = "pathNodes";
		String STYLE_FIELD = "styleField";
		String STYLE_FIELDS_LIST = "styleFieldsList";
		String INCUDE_IN_MENU = "includeInMenu";
		String VIEW_ID = "viewID";
		String VIEW_LIST = "viewList";
	}
	
	static interface TQL_JSON_FIELDS {
		String FILTER_TYPE = "filterType";
		String TQL_TYPES_LIST = "filterTypesList";
		String TQL_EXPRESSION = "tqlExpression";
	}	
	
	private static String getFilterHeaderJSON(String label,
			Integer styleField, List<IntegerStringBean> styleFieldLabels, 
			String viewID, List<LabelValueBean> viewList,
			boolean includeInMenu, boolean modifiable, boolean last) {
		StringBuilder stringBuilder = new StringBuilder();	
		JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.LABEL, label);
		if (styleFieldLabels!=null) {
			JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.STYLE_FIELD, styleField);
			JSONUtility.appendIntegerStringBeanList(stringBuilder,  JSON_FIELDS.STYLE_FIELDS_LIST, styleFieldLabels);
			JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.INCUDE_IN_MENU, includeInMenu);
		}
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.VIEW_ID, viewID);
		JSONUtility.appendLabelValueBeanList(stringBuilder, JSON_FIELDS.VIEW_LIST, viewList);
		JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.MODIFIABLE, modifiable, last);
		return stringBuilder.toString();
	}
	
	static String getTQLFilterJSON(String label, Integer filterType, 
			List<IntegerStringBean> tqlFilterTypes,
			Integer styleField, List<IntegerStringBean> styleFieldLabels,
			String viewID, List<LabelValueBean> viewList,
			boolean includeInMenu, String queryExpression, boolean modifiable) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{");
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.SUCCESS, true);
		stringBuilder.append(JSONUtility.JSON_FIELDS.DATA).append(":{");
		stringBuilder.append(getFilterHeaderJSON(label, styleField, styleFieldLabels, viewID, viewList, includeInMenu, modifiable, false));
		JSONUtility.appendIntegerValue(stringBuilder,  TQL_JSON_FIELDS.FILTER_TYPE, filterType);
		JSONUtility.appendIntegerStringBeanList(stringBuilder,   TQL_JSON_FIELDS.TQL_TYPES_LIST, tqlFilterTypes);
		JSONUtility.appendStringValue(stringBuilder,  TQL_JSON_FIELDS.TQL_EXPRESSION, queryExpression);
		stringBuilder.append("}");
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	static String getTreeFilterJSON(String label, List<TreeNode> pathNodes,
			Integer styleField, List<IntegerStringBean> styleFieldLabels, 
			boolean includeInMenu, String viewID, List<LabelValueBean> viewList,
			boolean modifiable,
			FilterUpperTO filterSelectsTO, boolean hasViewWatcherRightInAnyProject,
			List<FieldExpressionInTreeTO> fieldExpressionInTreeList,
			Integer personID, Integer projectID, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{");
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(stringBuilder, JSONUtility.JSON_FIELDS.DATA).append(":{");
		stringBuilder.append(getFilterHeaderJSON(label, styleField, styleFieldLabels, viewID, viewList, includeInMenu, modifiable, false));
		if (pathNodes!=null) {
			JSONUtility.appendJSONValue(stringBuilder, JSON_FIELDS.PATH_NODES, JSONUtility.getTreeHierarchyJSON(pathNodes, false, true));		
		}
		stringBuilder.append(getFilterUpperSelectsJSON(filterSelectsTO, hasViewWatcherRightInAnyProject, locale, projectID, false));
		stringBuilder.append(getFieldExpressionSimpleListJSON(FIELD_EXPRESSION_SIMPLE_JSON_FIELDS.FIELD_EXPRESSIONS_SIMPLE,
				filterSelectsTO.getFieldExpressionSimpleList(), false));
		stringBuilder.append(getFieldExpressionInTreeListJSON(fieldExpressionInTreeList, false));
		stringBuilder.append("}");
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	public static String getTreeFilterParametersJSON(Integer[] projectIDs, Integer[] itemTypeIDs,
			FilterUpperTO filterSelectsTO, boolean hasViewWatcherRightInAnyProject,
			List<FieldExpressionSimpleTO> fieldExpressionSimpleListFromTree, Integer personID, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{");
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.SUCCESS, true);
		stringBuilder.append(JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendIntegerArrayAsArray(stringBuilder, FILTER_SELECTS_JSON_FIELDS.ORIGINAL_PROJECTS, projectIDs);
		JSONUtility.appendIntegerArrayAsArray(stringBuilder, FILTER_SELECTS_JSON_FIELDS.ORIGINAL_ITEM_TYPES, itemTypeIDs);
		stringBuilder.append(getFilterUpperSelectsJSON(filterSelectsTO, hasViewWatcherRightInAnyProject, locale, null, false));
		stringBuilder.append(getFieldExpressionSimpleListJSON(FIELD_EXPRESSION_SIMPLE_JSON_FIELDS.FIELD_EXPRESSIONS_SIMPLE,
				filterSelectsTO.getFieldExpressionSimpleList(), false));
		stringBuilder.append(getFieldExpressionSimpleListJSON(FIELD_EXPRESSION_IN_TREE_JSON_FIELDS.FIELD_EXPRESSIONS_IN_TREE,
				fieldExpressionSimpleListFromTree, true));
		stringBuilder.append("}");
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	static String getNotifyFilterJSON(String label, boolean modifiable,
			List<FieldExpressionInTreeTO> fieldExpressionInTreeList,
			Integer personID, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{");
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.SUCCESS, true);
		stringBuilder.append(JSONUtility.JSON_FIELDS.DATA).append(":{");
		stringBuilder.append(getFilterHeaderJSON(label, null, null, null, null, false, modifiable, true));
		stringBuilder.append(getFieldExpressionInTreeListJSON(fieldExpressionInTreeList, true));
		stringBuilder.append("}");
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	static interface FILTER_LINK_FIELDS {
		String FILTER_URL_ISSUE_NAVIGATOR_NO_USER = "filterUrlIssueNavigatorNoUser";
		String FILTER_URL_ISSUE_NAVIGATOR = "filterUrlIssueNavigator";
		String FILTER_URL_ISSUE_NAVIGATOR_KEEP = "filterUrlIssueNavigatorKeep";
		String FILTER_URL_MAVEN_PLUGIN = "filterUrlMavenPlugin";
		String FILTER_PARAMETERS = "filterParams";
	}
	
	public static String getFilterLinkJSON(String encodedFilterUrlIssueNavigatorNoUser,String encodedFilterUrlIssueNavigator,
			String encodedFilterUrlIssueNavigatorKeep, String  encodedFilterUrlMavenPlugin, String filterParams) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{");
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.SUCCESS, true);
		stringBuilder.append(JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendStringValue(stringBuilder, FILTER_LINK_FIELDS.FILTER_URL_ISSUE_NAVIGATOR_NO_USER, encodedFilterUrlIssueNavigatorNoUser);
		JSONUtility.appendStringValue(stringBuilder, FILTER_LINK_FIELDS.FILTER_URL_ISSUE_NAVIGATOR, encodedFilterUrlIssueNavigator);
		JSONUtility.appendStringValue(stringBuilder, FILTER_LINK_FIELDS.FILTER_URL_ISSUE_NAVIGATOR_KEEP, encodedFilterUrlIssueNavigatorKeep);
		JSONUtility.appendStringValue(stringBuilder, FILTER_LINK_FIELDS.FILTER_URL_MAVEN_PLUGIN, encodedFilterUrlMavenPlugin);
		JSONUtility.appendStringValue(stringBuilder, FILTER_LINK_FIELDS.FILTER_PARAMETERS, filterParams);
		stringBuilder.append("}");
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * JSON field names for upper part
	 * @author Tamas
	 *
	 */
	public static interface FILTER_SELECTS_JSON_FIELDS {
		String FILTER_UPPER_PREFIX = "filterUpperTO.";//prefix for fields (used for submitting to the corresponding fields)
		String NAME_SUFFIX = "Name";//suffix for field names
		String STRING_SETTER_NAME = "Str";
		
		String SELECT_FIELDS = "selectFields";
		String SELECT_FIELD = "selectField";
		String MISSING_SELECT_FIELDS = "missingSelectFields";
		String FIELD_ID = "fieldID";
		
		String IS_TREE = "isTree";
		
		/**
		 * The fieldID to get the icon through dynamic download (negative fieldID for status, issuesType, priority, severity)
		 */
		
		/**
		 * project specific filter should contain only this project
		 */
		String EXCLUSEIVE_PROJECT_ID = "exclusiveProjectID";
		
		String ORIGINAL_PROJECTS = "originalProjects";
		String ORIGINAL_ITEM_TYPES = "originalItemTypes";
		
		String SHOW_CLOSED_RELEASES = "showClosedReleases";
		String SHOW_CLOSED_RELEASES_JSON_NAME_FIELD = SHOW_CLOSED_RELEASES + NAME_SUFFIX;
		String SHOW_CLOSED_RELEASES_CONTROL_NAME = FILTER_UPPER_PREFIX + SHOW_CLOSED_RELEASES;
	
		String RELEASE_TYPE_SELECTOR = "releaseTypeSelector";
		String RELEASE_TYPE_SELECTOR_LIST = "releaseTypeSelectorList";
		String RELEASE_TYPE_SELECTOR_JSON_NAME_FIELD = RELEASE_TYPE_SELECTOR + NAME_SUFFIX;
		String RELEASE_TYPE_SELECTOR_CONTROL_NAME = FILTER_UPPER_PREFIX + RELEASE_TYPE_SELECTOR;
		String RELEASE_TYPE_SELECTOR_IS_INCLUDED = "releaseTypeSelectorIsIncluded";
				
		String WATCHER_SELECTOR = "watcherSelector";
		String WATCHER_SELECTOR_LIST = "watcherSelectorList";
		String WATCHER_SELECTOR_JSON_NAME_FIELD = WATCHER_SELECTOR + NAME_SUFFIX;
		String WATCHER_SELECTOR_CONTROL_NAME = FILTER_UPPER_PREFIX + WATCHER_SELECTOR;
		String WATCHER_IS_INCLUDED = "watcherIsIncluded";
		
		String KEYWORD = "keyword";
		String KEYWORD_JSON_NAME_FIELD = KEYWORD + NAME_SUFFIX;
		String KEYWORD_CONTROL_NAME = FILTER_UPPER_PREFIX + KEYWORD;
		String KEYWORD_IS_INCLUDED = "keywordIsIncluded";
		
		String ARCHIVED = "archived";
		String ARCHIVED_LIST = "archivedList";
		String ARCHIVED_JSON_NAME_FIELD = ARCHIVED + NAME_SUFFIX;
		String ARCHIVED_CONTROL_NAME = FILTER_UPPER_PREFIX + ARCHIVED;
		
		String DELETED = "deleted";
		String DELETED_LIST = "deletedList";
		String DELETED_JSON_NAME_FIELD = DELETED + NAME_SUFFIX;
		String DELETED_CONTROL_NAME = FILTER_UPPER_PREFIX + DELETED;
		String ARCHIVED_DELETED_IS_INCLUDED = "archivedDeletedIsIncluded";
		
		String SELECTED_LINK_TYPE_SUPERSET = "linkTypeFilterSuperset";
		String LINK_TYPE_SUPERSET_JSON_NAME_FIELD = SELECTED_LINK_TYPE_SUPERSET + NAME_SUFFIX;
		String LINK_TYPE_SUPERSET_CONTROL_NAME = FILTER_UPPER_PREFIX + SELECTED_LINK_TYPE_SUPERSET;
		String LINK_TYPE_SUPERSET_LIST = "linkTypeFilterSupersetList";
		
		String HAS_SIMPLE_FIELD_EXPRESSIONS = "hasSimpleFieldExpressions";
	}
	
	/**
	 * Build the name for upper selects 
	 * @param fieldID
	 * @return
	 */
	private static String getUpperSelectName(Integer fieldID) {
		String fieldName = null;
		boolean custom = false; 
		switch (fieldID.intValue()) {
		case SystemFields.PROJECT:
			fieldName = "selectedProjects";
			break;
		case SystemFields.RELEASE:
			fieldName = "selectedReleases";
			break;
		case SystemFields.MANAGER:
			fieldName = "selectedManagers";
			break;
		case SystemFields.RESPONSIBLE:
			fieldName = "selectedResponsibles";
			break;
		case SystemFields.ORIGINATOR:
			fieldName = "selectedOriginators";
			break;
		case SystemFields.CHANGEDBY:
			fieldName = "selectedChangedBys";
			break;
		case FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID:
			fieldName = "selectedConsultantsInformants";
			break;
		case SystemFields.STATE:
			fieldName = "selectedStates";
			break;
		case SystemFields.ISSUETYPE:
			fieldName = "selectedIssueTypes";
			break;
		case SystemFields.PRIORITY:
			fieldName = "selectedPriorities";
			break;
		case SystemFields.SEVERITY:
			fieldName = "selectedSeverities";
			break;
		default:
			fieldName = "selectedCustomSelects";
			custom = true;
			break;
		}	
		if (fieldName!=null) {
			StringBuilder stringBuilder = new StringBuilder(FILTER_SELECTS_JSON_FIELDS.FILTER_UPPER_PREFIX);
			stringBuilder.append(fieldName).append(FILTER_SELECTS_JSON_FIELDS.STRING_SETTER_NAME);
			if (custom) {
				stringBuilder.append("[").append(fieldID).append("]");
			}
			return stringBuilder.toString();
		} else {
			return null;
		}
	}
	
	/**
	 * Returns the iconField for getting the icons
	 * @param fieldID
	 * @return
	 */
	
	private static List<Integer> getPossibleUpperSelects(boolean projectChange) {
		List<Integer> upperSelects = new LinkedList<Integer>();
		if (!projectChange) {
			upperSelects.add(SystemFields.INTEGER_PROJECT);
		}
		upperSelects.add(SystemFields.RELEASESCHEDULED);
		upperSelects.add(SystemFields.INTEGER_MANAGER);
		upperSelects.add(SystemFields.INTEGER_RESPONSIBLE);
		upperSelects.add(SystemFields.INTEGER_ORIGINATOR);
		upperSelects.add(SystemFields.INTEGER_CHANGEDBY);
		upperSelects.add(SystemFields.INTEGER_STATE);
		upperSelects.add(SystemFields.INTEGER_ISSUETYPE);
		if (!projectChange) {
			//because project change does not affects the priority and severity fields
			//do not render them (they are not loaded in reloadProjectDependentSelects() )
			upperSelects.add(SystemFields.INTEGER_PRIORITY);
			upperSelects.add(SystemFields.INTEGER_SEVERITY);
		}
		List<TFieldBean> customFieldBeans = FieldBL.loadCustom();
		if (customFieldBeans!=null) {
			for (TFieldBean fieldBean : customFieldBeans) {
				Integer fieldID = fieldBean.getObjectID();
				boolean isCustomSelect = FieldBL.isCustomSelect(fieldID);
				if (isCustomSelect) {
					upperSelects.add(fieldID);
				}
			}
		}
		return upperSelects;
	}
	
	
	
	
	/**
	 * Get the upper selects JSON either entirely or partially
	 * (after a project change only the project dependent selects) 
	 * @param filterUpperTO
	 * @param locale
	 * @param projectChange
	 * @return
	 */
	public static String getFilterUpperSelectsJSON(
			FilterUpperTO filterUpperTO, boolean hasViewWatcherRightInAnyProject, Locale locale,
			Integer exclusiveProjectID, boolean projectChange) {
		StringBuilder stringBuilder = new StringBuilder();
		List<Integer> usedUpperPartFields = filterUpperTO.getUpperFields();
		//the order of upperSelects is important: project should be the first (other depend on project)
		Map<Integer, Boolean> deprecatedMap = FieldBL.getDeprecatedMap(new Object[] {
				SystemFields.INTEGER_RELEASENOTICED, SystemFields.INTEGER_RELEASESCHEDULED});
		boolean releaseNoticedActive = deprecatedMap.get(SystemFields.INTEGER_RELEASENOTICED)==null || 
				!deprecatedMap.get(SystemFields.INTEGER_RELEASENOTICED).booleanValue();
		boolean releaseScheduledActive = deprecatedMap.get(SystemFields.INTEGER_RELEASESCHEDULED)==null ||
				!deprecatedMap.get(SystemFields.INTEGER_RELEASESCHEDULED).booleanValue();
		//if any release appears in filter because either
		//1. "appear in filter" is set for either release noticed or scheduled (new or existing filter)
		//or
		//2. if "appear in filter" is not set but a saved filter contains a
		//release scheduled selected (always scheduled is saved and release type selector differentiates)
		//then render also the release type selector if both are active
		boolean bothReleasesInFilter = releaseNoticedActive && releaseScheduledActive;
		boolean releasesInFilter = usedUpperPartFields!=null && (usedUpperPartFields.contains(SystemFields.RELEASESCHEDULED) ||
				usedUpperPartFields.contains(SystemFields.RELEASENOTICED));
		List<Integer> possibleUpperSelects = getPossibleUpperSelects(projectChange);
		//get the labels for all possible select fields
		Map<Integer, String> localizedLabels = FieldRuntimeBL.getLocalizedDefaultFieldLabels(possibleUpperSelects, locale);
		if (hasViewWatcherRightInAnyProject) {
			possibleUpperSelects.add(FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID);
			localizedLabels.put(FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID,
					LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.queryFilter.lbl.consInf", locale));
		}
		//retain only the (multiple) select fields (system or custom)
		if (usedUpperPartFields!=null) {
			possibleUpperSelects.retainAll(usedUpperPartFields);
		}
		if (bothReleasesInFilter) {
			//add the "general" release label if both noticed and scheduled are active
			localizedLabels.put(SystemFields.INTEGER_RELEASE,
				LocalizeUtil.getLocalizedTextFromApplicationResources(ReleaseConfigBL.RELEAESE_KEY, locale));
		} else {
			//only release noticed is active, release scheduled is deprecated
			//but usedUpperPartFields contains always release scheduled as field ID (release type selector differentiates)
			if (releaseNoticedActive) {
				localizedLabels.put(SystemFields.INTEGER_RELEASE,
					FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_RELEASENOTICED, locale));
			}
		}
		JSONUtility.appendIntegerValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.EXCLUSEIVE_PROJECT_ID, exclusiveProjectID);
		JSONUtility.appendFieldName(stringBuilder, FILTER_SELECTS_JSON_FIELDS.SELECT_FIELDS).append(":[");
		for (Iterator<Integer> iterator = possibleUpperSelects.iterator(); iterator.hasNext();) {
			Integer selectField = iterator.next();
			boolean isTree = false;
			//add only the release scheduled if both releases are set as filter fields: 
			stringBuilder.append("{");
			JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.LABEL + selectField,
					localizedLabels.get(selectField));
			JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.NAME + selectField,
					getUpperSelectName(selectField)); 
			JSONUtility.appendIntegerArrayAsArray(stringBuilder, JSONUtility.JSON_FIELDS.VALUE + selectField,
					filterUpperTO.getSelectedValuesForField(selectField));
			switch (selectField.intValue()) {
			case SystemFields.RELEASENOTICED:
			case SystemFields.RELEASESCHEDULED:
				JSONUtility.appendJSONValue(stringBuilder, JSONUtility.JSON_FIELDS.DATA_SOURCE + selectField,
						JSONUtility.getTreeHierarchyJSON(filterUpperTO.getReleaseTree(), true, true));
				isTree = true;
				break;
			case SystemFields.PROJECT:
				JSONUtility.appendJSONValue(stringBuilder, JSONUtility.JSON_FIELDS.DATA_SOURCE + selectField,
						JSONUtility.getTreeHierarchyJSON(filterUpperTO.getProjectTree(), true, false));
				isTree = true;
				break;
			case FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID: {
				JSONUtility.appendILabelBeanListWithIcons(stringBuilder, selectField, new SystemResponsibleRT(),
						JSONUtility.JSON_FIELDS.DATA_SOURCE + selectField, filterUpperTO.getListDatasourceForField(selectField), false);
				break;
			}
			default:
				if (FieldBL.isTree(selectField)) {
					JSONUtility.appendJSONValue(stringBuilder, JSONUtility.JSON_FIELDS.DATA_SOURCE + selectField,
						JSONUtility.getTreeHierarchyJSON(filterUpperTO.getTreeDatasourceForField(selectField), true, false));
					isTree = true;
				} else {
					if (FieldBL.isCustomSelect(selectField) && !FieldBL.isCustomPicker(selectField)) {
						//do not add icons to custom selects because they might not be set and then the empty icons are ugly by rendering
						JSONUtility.appendILabelBeanList(stringBuilder, JSONUtility.JSON_FIELDS.DATA_SOURCE + selectField, 
								filterUpperTO.getListDatasourceForField(selectField));
					} else {
						JSONUtility.appendILabelBeanListWithIcons(stringBuilder, selectField, (ILookup)FieldTypeManager.getFieldTypeRT(selectField),
							JSONUtility.JSON_FIELDS.DATA_SOURCE + selectField, filterUpperTO.getListDatasourceForField(selectField), false);
					}
				}
				break;
			}
			JSONUtility.appendBooleanValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.IS_TREE, isTree);
			JSONUtility.appendIntegerValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.FIELD_ID, selectField, true);
			stringBuilder.append("}");
			if (iterator.hasNext()) {
				stringBuilder.append(",");
			}
		}
		stringBuilder.append("]");
		String missingSelects = getMissingSelects(localizedLabels, possibleUpperSelects);
		if (missingSelects!=null) {
			stringBuilder.append(",");
			JSONUtility.appendJSONValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.MISSING_SELECT_FIELDS, missingSelects, true);
		}
		if (!projectChange) {
			stringBuilder.append(",");
			//release dealt with explicitly
			if (releasesInFilter) {
				JSONUtility.appendBooleanValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.SHOW_CLOSED_RELEASES,
						filterUpperTO.isShowClosedReleases());
				JSONUtility.appendStringValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.SHOW_CLOSED_RELEASES_JSON_NAME_FIELD,
						FILTER_SELECTS_JSON_FIELDS.SHOW_CLOSED_RELEASES_CONTROL_NAME);
				if (bothReleasesInFilter) {
					JSONUtility.appendIntegerValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.RELEASE_TYPE_SELECTOR,
						filterUpperTO.getReleaseTypeSelector());
					JSONUtility.appendStringValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.RELEASE_TYPE_SELECTOR_JSON_NAME_FIELD,
							FILTER_SELECTS_JSON_FIELDS.RELEASE_TYPE_SELECTOR_CONTROL_NAME);
					JSONUtility.appendIntegerStringBeanList(stringBuilder,  FILTER_SELECTS_JSON_FIELDS.RELEASE_TYPE_SELECTOR_LIST, 
						filterUpperTO.getReleaseTypeSelectors());
					JSONUtility.appendBooleanValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.RELEASE_TYPE_SELECTOR_IS_INCLUDED, true);
				}
			}
			//watchers dealt with explicitly
			if (possibleUpperSelects!=null && possibleUpperSelects.contains(FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID)) {
				JSONUtility.appendIntegerValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.WATCHER_SELECTOR,
						filterUpperTO.getWatcherSelector());
				JSONUtility.appendStringValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.WATCHER_SELECTOR_JSON_NAME_FIELD,
						FILTER_SELECTS_JSON_FIELDS.WATCHER_SELECTOR_CONTROL_NAME);
				JSONUtility.appendIntegerStringBeanList(stringBuilder, FILTER_SELECTS_JSON_FIELDS.WATCHER_SELECTOR_LIST, 
						filterUpperTO.getWatcherSelectorList());
			}
			if (filterUpperTO.isKeywordIncluded()) {
				JSONUtility.appendStringValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.KEYWORD,
					filterUpperTO.getKeyword());
				
				JSONUtility.appendStringValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.KEYWORD_JSON_NAME_FIELD,
						FILTER_SELECTS_JSON_FIELDS.KEYWORD_CONTROL_NAME);		
				JSONUtility.appendBooleanValue(stringBuilder,  FILTER_SELECTS_JSON_FIELDS.KEYWORD_IS_INCLUDED, true);
			}
			if (filterUpperTO.isAdmin()) {
				if (usedUpperPartFields!=null && usedUpperPartFields.contains(SystemFields.INTEGER_ARCHIVELEVEL)) {
					//archive level is included into filter from field configuration
					JSONUtility.appendIntegerValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.ARCHIVED,
						filterUpperTO.getArchived());
					JSONUtility.appendStringValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.ARCHIVED_JSON_NAME_FIELD,
							FILTER_SELECTS_JSON_FIELDS.ARCHIVED_CONTROL_NAME);
					JSONUtility.appendIntegerStringBeanList(stringBuilder, FILTER_SELECTS_JSON_FIELDS.ARCHIVED_LIST, 
						filterUpperTO.getArchivedOptions());
					JSONUtility.appendIntegerValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.DELETED,
						filterUpperTO.getDeleted());
					JSONUtility.appendStringValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.DELETED_JSON_NAME_FIELD,
							FILTER_SELECTS_JSON_FIELDS.DELETED_CONTROL_NAME);
					JSONUtility.appendIntegerStringBeanList(stringBuilder,  FILTER_SELECTS_JSON_FIELDS.DELETED_LIST, 
						filterUpperTO.getDeletedOptions());
					JSONUtility.appendBooleanValue(stringBuilder,  FILTER_SELECTS_JSON_FIELDS.ARCHIVED_DELETED_IS_INCLUDED, true);
				} else {
					//not included from field configuration (more exactly "appears in filter" flag removed from field configuration
					//but has value from the time when "appears in filter" flag was set
					if (usedUpperPartFields!=null && (usedUpperPartFields.contains(FilterUpperTO.PSEUDO_FIELDS.ARCHIVED_FIELD_ID) ||
							usedUpperPartFields.contains(FilterUpperTO.PSEUDO_FIELDS.DELETED_FIELD_ID))) {
						Integer archived = filterUpperTO.getArchived();
						Integer deleted = filterUpperTO.getDeleted();
						if ((archived!=null && archived.intValue()!=ARCHIVED_FILTER.EXCLUDE_ARCHIVED) || (deleted!=null && deleted.intValue()!=DELETED_FILTER.EXCLUDE_DELETED)) {
							//there were a selection set when "appears in filter" was still set: force archive/delete selection to appear on filter if differs from EXCLUDE_ARCHIVED/EXCLUDE_DELETED
							JSONUtility.appendIntegerValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.ARCHIVED, archived);
							JSONUtility.appendStringValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.ARCHIVED_JSON_NAME_FIELD,
								FILTER_SELECTS_JSON_FIELDS.ARCHIVED_CONTROL_NAME);
							JSONUtility.appendIntegerStringBeanList(stringBuilder, FILTER_SELECTS_JSON_FIELDS.ARCHIVED_LIST, 
								filterUpperTO.getArchivedOptions());
							JSONUtility.appendIntegerValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.DELETED, deleted);
							JSONUtility.appendStringValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.DELETED_JSON_NAME_FIELD,
								FILTER_SELECTS_JSON_FIELDS.DELETED_CONTROL_NAME);
							JSONUtility.appendIntegerStringBeanList(stringBuilder,  FILTER_SELECTS_JSON_FIELDS.DELETED_LIST, 
								filterUpperTO.getDeletedOptions());
							JSONUtility.appendBooleanValue(stringBuilder,  FILTER_SELECTS_JSON_FIELDS.ARCHIVED_DELETED_IS_INCLUDED, true);
						}
					}
				}
			}
			JSONUtility.appendStringValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.SELECTED_LINK_TYPE_SUPERSET,
					filterUpperTO.getLinkTypeFilterSuperset());
			JSONUtility.appendStringValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.LINK_TYPE_SUPERSET_JSON_NAME_FIELD,
					FILTER_SELECTS_JSON_FIELDS.LINK_TYPE_SUPERSET_CONTROL_NAME);
			JSONUtility.appendLabelValueBeanList(stringBuilder,  FILTER_SELECTS_JSON_FIELDS.LINK_TYPE_SUPERSET_LIST, 
					filterUpperTO.getLinkTypeFilterSupersetList());
			JSONUtility.appendBooleanValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.HAS_SIMPLE_FIELD_EXPRESSIONS, filterUpperTO.hasSimpleFieldExpressions(), true);
			
		}
		return stringBuilder.toString();
	}
	
	/**
	 * Append the datasource for a select field
	 * @param stringBuilder
	 * @param selectField
	 * @param dataSource
	 * @param selectedValues
	 * @param localizedLabel
	 */
	private static void appendSelectFieldJSON(StringBuilder stringBuilder, Integer selectField,
			Object dataSource, Integer[] selectedValues, String localizedLabel) {
		boolean isTree = false;
		stringBuilder.append("{");
		JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.LABEL + selectField,
				localizedLabel);
		JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.NAME + selectField,
				getUpperSelectName(selectField)); 
		JSONUtility.appendIntegerArrayAsArray(stringBuilder, JSONUtility.JSON_FIELDS.VALUE + selectField,
				selectedValues);
		switch (selectField.intValue()) {
		case SystemFields.RELEASE:
			JSONUtility.appendJSONValue(stringBuilder, JSONUtility.JSON_FIELDS.DATA_SOURCE + selectField,
					JSONUtility.getTreeHierarchyJSON((List<TreeNode>)dataSource, true, true));
			isTree = true;
			break;
		case SystemFields.PROJECT:
			JSONUtility.appendJSONValue(stringBuilder, JSONUtility.JSON_FIELDS.DATA_SOURCE + selectField,
					JSONUtility.getTreeHierarchyJSON((List<TreeNode>)dataSource, true, false));
			isTree = true;
			break;
		case FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID: {
			JSONUtility.appendILabelBeanListWithIcons(stringBuilder, selectField, new SystemResponsibleRT(),
					JSONUtility.JSON_FIELDS.DATA_SOURCE + selectField,  (List<ILabelBean>)dataSource, false);
			break;
		}	
		default:
			if (FieldBL.isTree(selectField)) {
				//project picker
				JSONUtility.appendJSONValue(stringBuilder, JSONUtility.JSON_FIELDS.DATA_SOURCE + selectField,
						JSONUtility.getTreeHierarchyJSON((List<TreeNode>)dataSource, true, false));
				isTree = true;
			} else {
				if (FieldBL.isCustomSelect(selectField) && !FieldBL.isCustomPicker(selectField)) {
					//do not add icons to custom selects because they might not be set and then the empty icons are ugly by rendering
					JSONUtility.appendILabelBeanList(stringBuilder, JSONUtility.JSON_FIELDS.DATA_SOURCE + selectField, 
							(List<ILabelBean>)dataSource);
				} else {
					JSONUtility.appendILabelBeanListWithIcons(stringBuilder, selectField, (ILookup)FieldTypeManager.getFieldTypeRT(selectField),
							JSONUtility.JSON_FIELDS.DATA_SOURCE + selectField,  (List<ILabelBean>)dataSource, false);
				}
			}
			break;
		}
		JSONUtility.appendBooleanValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.IS_TREE, isTree);
		JSONUtility.appendIntegerValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.FIELD_ID, selectField, true);
		stringBuilder.append("}");
	}
	
	
	/**
	 * Gets the datasource for missing selects available to add
	 * @param localizedLabels
	 * @param existingSelects
	 * @return
	 */
	private static String getMissingSelects(Map<Integer, String> localizedLabels, List<Integer> existingSelects) {
		List<IntegerStringBean> missingSelects = new LinkedList<IntegerStringBean>();
		for (Integer fieldID : localizedLabels.keySet()) {
			if (existingSelects!=null && !existingSelects.contains(fieldID)) {
				missingSelects.add(new IntegerStringBean(localizedLabels.get(fieldID), fieldID));
			}
		}
		if (missingSelects.isEmpty()) {
			return null;
		}
		Collections.sort(missingSelects);
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("[");
		for (Iterator<IntegerStringBean> iterator = missingSelects.iterator(); iterator.hasNext();) {
			IntegerStringBean itegerStringBean = iterator.next();
			Integer fieldID = itegerStringBean.getValue();
			stringBuilder.append("{");
			JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ICONCLS, getMissingIconCls(fieldID));
			JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.TEXT, itegerStringBean.getLabel());
			JSONUtility.appendIntegerValue(stringBuilder, JSONUtility.JSON_FIELDS.ID, fieldID, true);
			stringBuilder.append("}");
			if (iterator.hasNext()) {
				stringBuilder.append(",");
			}
		}
		stringBuilder.append("]");//end data
		return stringBuilder.toString();
	}
	
	private static String getMissingIconCls(Integer fieldID) {
		switch (fieldID.intValue()) {
		case SystemFields.PROJECT:
			return ListBL.ICONS_CLS.PROJECT_ICON;
		case SystemFields.ISSUETYPE:
			return ListBL.ICONS_CLS.ISSUETYPE_ICON;
		case SystemFields.STATE:
			return ListBL.ICONS_CLS.STATUS_ICON;
		case SystemFields.PRIORITY:
			return ListBL.ICONS_CLS.PRIORITY_ICON;
		case SystemFields.SEVERITY:
			return ListBL.ICONS_CLS.SEVERITY_ICON;
		case SystemFields.RELEASENOTICED:
		case SystemFields.RELEASESCHEDULED:
			return ReleaseBL.RELEASE_ICON_CLASS;
		case SystemFields.MANAGER:
		case SystemFields.RESPONSIBLE:
		case SystemFields.ORIGINATOR:
		case SystemFields.CHANGEDBY:
		case FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID:
			return GroupConfigBL.USER_ICON_CLASS;
		default:
			return ListBL.ICONS_CLS.CUSTOM_LIST_ICON;
		}
		
	}
	
	/**
	 * Add a new selection field
	 * @param fieldID
	 * @param dataSource
	 * @param usedUpperSelectFieldIDs
	 * @param locale
	 * @return
	 */
	public static String getAddSelectFieldJSON(Integer fieldID, Object dataSource,
			List<Integer> usedUpperSelectFieldIDs, boolean hasViewWatcherRightInAnyProject, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{");
		List<Integer> possibleUpperSelects = getPossibleUpperSelects(false);
		//get the labels for all possible select fields
		Map<Integer, String> localizedLabels = FieldRuntimeBL.getLocalizedDefaultFieldLabels(possibleUpperSelects, locale);
		if (hasViewWatcherRightInAnyProject) {
			possibleUpperSelects.add(FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID);
			localizedLabels.put(FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID,
					LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.queryFilter.lbl.consInf", locale));
		}
		//retain only the (multiple) select fields (system or custom)
		possibleUpperSelects.retainAll(usedUpperSelectFieldIDs);
		//release selectors
		Map<Integer, Boolean> deprecatedMap = FieldBL.getDeprecatedMap(new Object[] {
				SystemFields.INTEGER_RELEASENOTICED, SystemFields.INTEGER_RELEASESCHEDULED});
		boolean releaseNoticedActive = deprecatedMap.get(SystemFields.INTEGER_RELEASENOTICED)==null || 
				!deprecatedMap.get(SystemFields.INTEGER_RELEASENOTICED).booleanValue();
		boolean releaseScheduledActive = deprecatedMap.get(SystemFields.INTEGER_RELEASESCHEDULED)==null ||
				!deprecatedMap.get(SystemFields.INTEGER_RELEASESCHEDULED).booleanValue();
		//if any release appears in filter because either
		//1. "appear in filter" is set for either release noticed or scheduled (new or existing filter)
		//or
		//2. if "appear in filter" is not set but a saved filter contains a
		//release scheduled selected (always scheduled is saved and release type selector differentiates)
		//then render also the release type selector if both are active
		boolean bothReleasesInFilter = releaseNoticedActive && releaseScheduledActive;
		if (bothReleasesInFilter) {
			//add the "general" release label if both noticed and scheduled are active
			localizedLabels.put(SystemFields.INTEGER_RELEASE,
				LocalizeUtil.getLocalizedTextFromApplicationResources(ReleaseConfigBL.RELEAESE_KEY, locale));
		} else {
			//only release noticed is active, release scheduled is deprecated
			//but usedUpperPartFields contains always release scheduled as field ID (release type selector differentiates)
			if (releaseNoticedActive) {
				localizedLabels.put(SystemFields.INTEGER_RELEASE,
					FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_RELEASENOTICED, locale));
			}
		}
		JSONUtility.appendFieldName(stringBuilder, FILTER_SELECTS_JSON_FIELDS.SELECT_FIELD).append(":");
		appendSelectFieldJSON(stringBuilder, fieldID, dataSource, null, localizedLabels.get(fieldID));
		String missingSelects = getMissingSelects(localizedLabels, possibleUpperSelects);
		if (missingSelects!=null) {
			stringBuilder.append(",");
			JSONUtility.appendJSONValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.MISSING_SELECT_FIELDS, missingSelects, true);
		}
		if (SystemFields.INTEGER_RELEASESCHEDULED.equals(fieldID)) {
			stringBuilder.append(",");
			JSONUtility.appendBooleanValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.SHOW_CLOSED_RELEASES, false);
			JSONUtility.appendStringValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.SHOW_CLOSED_RELEASES_JSON_NAME_FIELD,
					FILTER_SELECTS_JSON_FIELDS.SHOW_CLOSED_RELEASES_CONTROL_NAME);
			if (bothReleasesInFilter) {
				JSONUtility.appendIntegerValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.RELEASE_TYPE_SELECTOR,
					FilterUpperTO.RELEASE_SELECTOR.SCHEDULED);
				JSONUtility.appendStringValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.RELEASE_TYPE_SELECTOR_JSON_NAME_FIELD,
						FILTER_SELECTS_JSON_FIELDS.RELEASE_TYPE_SELECTOR_CONTROL_NAME);
				JSONUtility.appendIntegerStringBeanList(stringBuilder, FILTER_SELECTS_JSON_FIELDS.RELEASE_TYPE_SELECTOR_LIST, 
					FilterSelectsListsLoader.getReleaseTypeSelectors(locale));
				JSONUtility.appendBooleanValue(stringBuilder,  FILTER_SELECTS_JSON_FIELDS.RELEASE_TYPE_SELECTOR_IS_INCLUDED, true, true);
			}
		}
		//watchers dealt with explicitly
		if (FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID==fieldID.intValue()) {
			stringBuilder.append(",");
			JSONUtility.appendIntegerValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.WATCHER_SELECTOR,
					FilterUpperTO.CONSINF_SELECTOR.CONSULTANT_OR_INFORMANT);
			JSONUtility.appendStringValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.WATCHER_SELECTOR_JSON_NAME_FIELD,
					FILTER_SELECTS_JSON_FIELDS.WATCHER_SELECTOR_CONTROL_NAME);
			JSONUtility.appendIntegerStringBeanList(stringBuilder, FILTER_SELECTS_JSON_FIELDS.WATCHER_SELECTOR_LIST, 
					FilterSelectsListsLoader.getConsultantInformantTypeSelectors(locale), true);
		}
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * Remove a list field
	 * @param usedUpperSelectFieldIDs
	 * @param locale
	 * @return
	 */
	public static String getRemoveSelectFieldJSON(List<Integer> usedUpperSelectFieldIDs,
			boolean hasViewWatcherRightInAnyProject, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{");
		List<Integer> possibleUpperSelects = getPossibleUpperSelects(false/*, false*/);
		//get the labels for all possible select fields
		Map<Integer, String> localizedLabels = FieldRuntimeBL.getLocalizedDefaultFieldLabels(possibleUpperSelects, locale);
		if (hasViewWatcherRightInAnyProject) {
			possibleUpperSelects.add(FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID);
			localizedLabels.put(FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID,
					LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.queryFilter.lbl.consInf", locale));
		}
		//retain only the (multiple) select fields (system or custom)
		possibleUpperSelects.retainAll(usedUpperSelectFieldIDs);
		
		Map<Integer, Boolean> deprecatedMap = FieldBL.getDeprecatedMap(new Object[] {
				SystemFields.INTEGER_RELEASENOTICED, SystemFields.INTEGER_RELEASESCHEDULED});
		boolean releaseNoticedActive = deprecatedMap.get(SystemFields.INTEGER_RELEASENOTICED)==null || 
				!deprecatedMap.get(SystemFields.INTEGER_RELEASENOTICED).booleanValue();
		boolean releaseScheduledActive = deprecatedMap.get(SystemFields.INTEGER_RELEASESCHEDULED)==null ||
				!deprecatedMap.get(SystemFields.INTEGER_RELEASESCHEDULED).booleanValue();
		//if any release appears in filter because either
		//1. "appear in filter" is set for either release noticed or scheduled (new or existing filter)
		//or
		//2. if "appear in filter" is not set but a saved filter contains a
		//release scheduled selected (always scheduled is saved and release type selector differentiates)
		//then render also the release type selector if both are active
		boolean bothReleasesInFilter = releaseNoticedActive && releaseScheduledActive;
		if (bothReleasesInFilter) {
			//add the "general" release label if both noticed and scheduled are active
			localizedLabels.put(SystemFields.INTEGER_RELEASE,
				LocalizeUtil.getLocalizedTextFromApplicationResources(ReleaseConfigBL.RELEAESE_KEY, locale));
		} else {
			//only release noticed is active, release scheduled is deprecated
			//but usedUpperPartFields contains always release scheduled as field ID (release type selector differentiates)
			if (releaseNoticedActive) {
				localizedLabels.put(SystemFields.INTEGER_RELEASE,
					FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_RELEASENOTICED, locale));
			}
		}
		String missingSelects = getMissingSelects(localizedLabels, possibleUpperSelects);
		if (missingSelects!=null) {
			JSONUtility.appendJSONValue(stringBuilder, FILTER_SELECTS_JSON_FIELDS.MISSING_SELECT_FIELDS, missingSelects, true);
		}
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * JSON field names for FieldExpressionSimple
	 * @author Tamas
	 *
	 */
	public static interface FIELD_EXPRESSION_SIMPLE_JSON_FIELDS {
		String FIELD_EXPRESSIONS_SIMPLE = "fieldsExpressionsSimple";
		String FIELD = "field";
		String FIELD_LABEL = "fieldLabel";
		String SELECTED_MATCHER = "matcher";
		String MATCHER_LIST = "matcherList";
		String NEED_MATCHER_VALUE = "needMatcherValue";
		String MATCHER_NAME = "matcherName";
		String VALUE_NAME = "valueName";
		String VALUE_RENDEDER = "valueRenderer";
		String JSON_CONFIG = "jsonConfig";
	}
	
	/**
	 * Gets the JSON string for a FieldExpressionSimpleTO list
	 * @param fieldExpressionSimpleTOList
	 * @param personID
	 * @return
	 */
	private static String getFieldExpressionSimpleListJSON(String expressionListName,
			List<FieldExpressionSimpleTO> fieldExpressionSimpleTOList, boolean withIndex) {
		StringBuilder stringBuilder = new StringBuilder();
		if (fieldExpressionSimpleTOList!=null && !fieldExpressionSimpleTOList.isEmpty()) {
			stringBuilder.append(",");
			JSONUtility.appendFieldName(stringBuilder, expressionListName).append(":[");
			for (Iterator<FieldExpressionSimpleTO> iterator = fieldExpressionSimpleTOList.iterator(); iterator.hasNext();) {
				FieldExpressionSimpleTO fieldExpressionSimpleTO = iterator.next();
				stringBuilder.append(getFieldExpressionSimpleJSON(fieldExpressionSimpleTO, withIndex));
				if (iterator.hasNext()) {
					stringBuilder.append(",");
				}
			}
			stringBuilder.append("]");
		}
		return stringBuilder.toString();
	}
	
	/**
	 * Gets the JSON string for a FieldrExpressionSimpleTO
	 * @return
	 */
	private static String getFieldExpressionSimpleJSON(FieldExpressionSimpleTO fieldExpressionSimpleTO, boolean withIndex) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{");		
		stringBuilder.append(getFieldExpressionSimpleJSONBase(fieldExpressionSimpleTO));
		if (withIndex) {
			JSONUtility.appendIntegerValue(stringBuilder, FIELD_EXPRESSION_IN_TREE_JSON_FIELDS.INDEX, fieldExpressionSimpleTO.getIndex());					
		}
		JSONUtility.appendStringValue(stringBuilder, FIELD_EXPRESSION_SIMPLE_JSON_FIELDS.FIELD_LABEL,
			fieldExpressionSimpleTO.getFieldLabel(), true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * Gets the base JSON string for a FieldExpressionSimpleTO
	 * @return
	 */
	private static String getFieldExpressionSimpleJSONBase(FieldExpressionSimpleTO fieldExpressionSimpleTO) {
		StringBuilder stringBuilder = new StringBuilder();
		JSONUtility.appendIntegerValue(stringBuilder, FIELD_EXPRESSION_SIMPLE_JSON_FIELDS.FIELD,
				fieldExpressionSimpleTO.getField());
		JSONUtility.appendStringValue(stringBuilder, FIELD_EXPRESSION_SIMPLE_JSON_FIELDS.MATCHER_NAME, fieldExpressionSimpleTO.getMatcherName());
		JSONUtility.appendIntegerValue(stringBuilder, FIELD_EXPRESSION_SIMPLE_JSON_FIELDS.SELECTED_MATCHER,
				fieldExpressionSimpleTO.getSelectedMatcher());
		JSONUtility.appendIntegerStringBeanList(stringBuilder,  FIELD_EXPRESSION_SIMPLE_JSON_FIELDS.MATCHER_LIST, 
				fieldExpressionSimpleTO.getMatchersList());
		stringBuilder.append(getFieldExpressionValueBaseJSON(fieldExpressionSimpleTO.getValueName(), fieldExpressionSimpleTO.isNeedMatcherValue(),
				fieldExpressionSimpleTO.getValueRenderer(), fieldExpressionSimpleTO.getJsonConfig(), false));
		return stringBuilder.toString();
	}
	
	/**
	 * Gets the JSON for field expression value: used after a matcher relation change 
	 * @param needMatcherValue
	 * @param valueRenderer
	 * @param jsonConfig
	 * @return
	 */
	public static String getFieldExpressionValueJSON(String name, boolean needMatcherValue,	
			String valueRenderer, String jsonConfig) {
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("{");
		stringBuilder.append(getFieldExpressionValueBaseJSON(name, needMatcherValue, valueRenderer, jsonConfig, true));
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * Gets the base JSON for field expression value
	 * @param needMatcherValue
	 * @param valueRenderer
	 * @param jsonConfig
	 * @param last
	 * @return
	 */
	public static String getFieldExpressionValueBaseJSON(String name, boolean needMatcherValue,	
			String valueRenderer, String jsonConfig, boolean last) {
		StringBuilder stringBuilder = new StringBuilder();
		JSONUtility.appendBooleanValue(stringBuilder,
				FIELD_EXPRESSION_SIMPLE_JSON_FIELDS.NEED_MATCHER_VALUE, needMatcherValue);
		JSONUtility.appendJSONValue(stringBuilder,
				FIELD_EXPRESSION_SIMPLE_JSON_FIELDS.JSON_CONFIG, jsonConfig);
		JSONUtility.appendStringValue(stringBuilder,
				FIELD_EXPRESSION_SIMPLE_JSON_FIELDS.VALUE_RENDEDER, valueRenderer);
		JSONUtility.appendStringValue(stringBuilder,
				FIELD_EXPRESSION_SIMPLE_JSON_FIELDS.VALUE_NAME, name, last);
		return stringBuilder.toString();
	}
	
	/**
	 * JSON field names for FieldExpressionInTree
	 * @author Tamas
	 *
	 */
	public static interface FIELD_EXPRESSION_IN_TREE_JSON_FIELDS {
		String FIELD_EXPRESSIONS_IN_TREE = "fieldsExpressionsInTree";
		
		String INDEX = "index";
		
		String WITH_FIELD_MOMENT = "withFieldMoment";
		String SELECTED_FIELD_MOMENT = "fieldMoment";
		String FIELD_MOMENT_LIST = "fieldMomentList";
		String FIELD_MOMENT_NAME = "fieldMomentName";
		
		String FIELD_LIST = "fieldList";
		String FIELD_NAME = "fieldName";
		
		String SELECTED_PARENTHESIS_OPEN = "parenthesisOpen";
		String PARENTHESIS_OPEN_LIST = "parenthesisOpenList";
		String PARENTHESIS_OPEN_NAME = "parenthesisOpenName";
		
		String SELECTED_PARENTHESIS_CLOSED = "parenthesisClosed";
		String PARENTHESIS_CLOSED_LIST = "parenthesisClosedList";
		String PARENTHESIS_CLOSED_NAME = "parenthesisClosedName";
		
		String SELECTED_OPERATION = "operation";
		String OPERATION_LIST = "operationsList";
		String OPERATION_NAME = "operationsName";
		
		String SELECTED_NEGATE = "negation";
		String NEGATE_LIST = "negationList";
		String NEGATE_NAME = "negationName";
		
	}
	
	/**
	 * Gets the JSON string for a FieldExpressionInTreeTO list  
	 * @param fieldExpressionsInTreeList
	 * @param withFieldMoment
	 * @param personID
	 * @return
	 */
	private static String getFieldExpressionInTreeListJSON(
			List<FieldExpressionInTreeTO> fieldExpressionsInTreeList,
			boolean withFieldMoment) {
		StringBuilder stringBuilder = new StringBuilder();
		if (fieldExpressionsInTreeList!=null && !fieldExpressionsInTreeList.isEmpty()) {
			stringBuilder.append(",");
			JSONUtility.appendFieldName(stringBuilder, FIELD_EXPRESSION_IN_TREE_JSON_FIELDS.FIELD_EXPRESSIONS_IN_TREE).append(":[");
			for (Iterator<FieldExpressionInTreeTO> iterator = fieldExpressionsInTreeList.iterator(); iterator.hasNext();) {
				FieldExpressionInTreeTO fieldExpressionInTreeTO = iterator.next();
				stringBuilder.append(getFieldExpressionInTreeJSON(fieldExpressionInTreeTO, withFieldMoment));
				if (iterator.hasNext()) {
					stringBuilder.append(",");
				}
			}
			stringBuilder.append("]");
		}
		return stringBuilder.toString();
	}
	
	
	/**
	 * Gets the base JSON string for a FieldExpressionInTreeTO
	 * @param fieldExpressionInTreeTO
	 * @param withFieldMoment
	 * @return
	 */
	public static String getFieldExpressionInTreeJSON(FieldExpressionInTreeTO fieldExpressionInTreeTO,
			boolean withFieldMoment) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{");

		if (withFieldMoment) {
			JSONUtility.appendBooleanValue(stringBuilder, FIELD_EXPRESSION_IN_TREE_JSON_FIELDS.WITH_FIELD_MOMENT,
					fieldExpressionInTreeTO.isWithFieldMoment());
			JSONUtility.appendIntegerValue(stringBuilder, FIELD_EXPRESSION_IN_TREE_JSON_FIELDS.SELECTED_FIELD_MOMENT,
					fieldExpressionInTreeTO.getFieldMoment());
			JSONUtility.appendIntegerStringBeanList(stringBuilder,  FIELD_EXPRESSION_IN_TREE_JSON_FIELDS.FIELD_MOMENT_LIST, 
					fieldExpressionInTreeTO.getFieldMomentsList());
			JSONUtility.appendStringValue(stringBuilder, FIELD_EXPRESSION_IN_TREE_JSON_FIELDS.FIELD_MOMENT_NAME,
					fieldExpressionInTreeTO.getFieldMomentName());
		}
		JSONUtility.appendIntegerStringBeanList(stringBuilder,  FIELD_EXPRESSION_IN_TREE_JSON_FIELDS.FIELD_LIST, 
				fieldExpressionInTreeTO.getFieldsList());
		JSONUtility.appendStringValue(stringBuilder,  FIELD_EXPRESSION_IN_TREE_JSON_FIELDS.FIELD_NAME, 
				fieldExpressionInTreeTO.getFieldName());
		
		stringBuilder.append(getFieldExpressionSimpleJSONBase(fieldExpressionInTreeTO));
		
		JSONUtility.appendIntegerValue(stringBuilder, FIELD_EXPRESSION_IN_TREE_JSON_FIELDS.SELECTED_PARENTHESIS_OPEN,
				fieldExpressionInTreeTO.getParenthesisOpen());
		JSONUtility.appendIntegerStringBeanList(stringBuilder,  FIELD_EXPRESSION_IN_TREE_JSON_FIELDS.PARENTHESIS_OPEN_LIST, 
				fieldExpressionInTreeTO.getParenthesisOpenList());
		JSONUtility.appendStringValue(stringBuilder, FIELD_EXPRESSION_IN_TREE_JSON_FIELDS.PARENTHESIS_OPEN_NAME,
				fieldExpressionInTreeTO.getParenthesisOpenName());
		
		JSONUtility.appendIntegerValue(stringBuilder, FIELD_EXPRESSION_IN_TREE_JSON_FIELDS.SELECTED_PARENTHESIS_CLOSED,
				fieldExpressionInTreeTO.getParenthesisClosed());
		JSONUtility.appendIntegerStringBeanList(stringBuilder,  FIELD_EXPRESSION_IN_TREE_JSON_FIELDS.PARENTHESIS_CLOSED_LIST, 
				fieldExpressionInTreeTO.getParenthesisClosedList());
		JSONUtility.appendStringValue(stringBuilder, FIELD_EXPRESSION_IN_TREE_JSON_FIELDS.PARENTHESIS_CLOSED_NAME,
				fieldExpressionInTreeTO.getParenthesisClosedName());
		
		JSONUtility.appendIntegerValue(stringBuilder, FIELD_EXPRESSION_IN_TREE_JSON_FIELDS.SELECTED_OPERATION,
				fieldExpressionInTreeTO.getSelectedOperation());
		JSONUtility.appendIntegerStringBeanList(stringBuilder,  FIELD_EXPRESSION_IN_TREE_JSON_FIELDS.OPERATION_LIST, 
				fieldExpressionInTreeTO.getOperationsList());
		JSONUtility.appendStringValue(stringBuilder, FIELD_EXPRESSION_IN_TREE_JSON_FIELDS.OPERATION_NAME,
				fieldExpressionInTreeTO.getOperationName());

		JSONUtility.appendIntegerValue(stringBuilder, FIELD_EXPRESSION_IN_TREE_JSON_FIELDS.INDEX, fieldExpressionInTreeTO.getIndex(),true);

		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * Renders the value part of the field expression: used after a matcher relation change 
	 * @param matcherRelations
	 * @param selectedMatcherRelation
	 * @param valueJSON
	 * @return
	 */
	public static String getFieldExpressionMatcherAndValueValueJSON(
			List<IntegerStringBean> matcherRelations,
			Integer selectedMatcherRelation, String valueJSON) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{");
		JSONUtility.appendIntegerStringBeanList(stringBuilder,  FIELD_EXPRESSION_SIMPLE_JSON_FIELDS.MATCHER_LIST, 
				matcherRelations);
		JSONUtility.appendIntegerValue(stringBuilder, FIELD_EXPRESSION_SIMPLE_JSON_FIELDS.SELECTED_MATCHER,
				selectedMatcherRelation);
		stringBuilder.append(valueJSON);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
}
