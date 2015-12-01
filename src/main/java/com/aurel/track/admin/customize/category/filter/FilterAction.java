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

package com.aurel.track.admin.customize.category.filter;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.CategoryBL;
import com.aurel.track.admin.customize.category.CategoryBL.CATEGORY_TYPE;
import com.aurel.track.admin.customize.category.CategoryBL.TYPE;
import com.aurel.track.admin.customize.category.CategoryFacadeFactory;
import com.aurel.track.admin.customize.category.CategoryJSON;
import com.aurel.track.admin.customize.category.CategoryTokens;
import com.aurel.track.admin.customize.category.filter.tree.design.FieldExpressionInTreeTO;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterSubmitHandler;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.itemNavigator.ItemNavigatorBL.QUERY_TYPE;
import com.aurel.track.itemNavigator.filterInMenu.FilterInMenuJSON;
import com.aurel.track.itemNavigator.filterInMenu.FilterInMenuTO;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * A action for configuring an issue filter (tree or TQL(Plus))
 * (executing the filter is directed to issueNavigator action because it handles
 * centrally the loading and rendering of issue results to avoid storing the results in session for different actions)
 */
public class FilterAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware  {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(FilterAction.class);

    private String node;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private TPersonBean personBean;
	private Locale locale;
	private String label;
	private boolean includeInMenu;
	private Integer styleField;
	private String viewID;

	//add or edit
	private boolean add;
	/*
	 * instant used only by filter load (edit) and not save: an instant filter from filter dialog can't be saved
	 */
	private boolean instant;
	/**
	 * whether the saved filter is modifiable
	 */
	private boolean modifiable;
	/**
	 * Clears the filter conditions to start from an empty filter
	 * Used to clean up the last executed instant or saved filter
	 */
    private boolean clearFilter;
    
	private Integer filterSubType;
	private String tqlExpression;
	
	//tree filter specific fields
	private FilterUpperTO filterUpperTO;
	//in simpleDisplayValueMap key is also string because for composite fields it is fieldID_parameterCode
	private Map<String, String> simpleDisplayValueMap;
	private Map<Integer, Integer> simpleMatcherRelationMap;
	
	//in inTreeDisplayValueMap key is also string because for composite fields it is index_parameterCode
	private Map<String, String> inTreeDisplayValueMap;
	private Map<Integer, Integer> inTreeMatcherRelationMap;
	
	private Map<Integer, Integer> fieldMap;
	private Map<Integer, Integer> fieldMomentMap;
	private Map<Integer, Integer> operationMap;
	private Map<Integer, Integer> parenthesisOpenedMap;
	private Map<Integer, Integer> parenthesisClosedMap;	
	private SortedMap<Integer, Integer> fieldExpressionOrderMap = new TreeMap<Integer, Integer>();
	
    /**
     * Used in:
     * 1. edit() method: 
     * a. the filter is loaded in item navigator upper part as the last executed filter
     * b. the filter is loaded in dialog from the filter part of the item navigator accordion (context menu -> edit filter)
     * 2. save() method: "saved" button (but not "saved as")
     */
    private Integer filterID;
    private Integer filterType;
    private Integer queryContextID;
    //right click on filter in item navigator accordion, filter section
    private boolean fromNavigatorContextMenu;
    
	@Override
	public void prepare() {
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		locale=(Locale) session.get(Constants.LOCALE_KEY);
	}
	
	/**
	 * Add/remove a licensed feature
	 * @return
	 */
	public String changeSubscribe() {
		if (node!=null) {
			CategoryTokens categoryTokens = CategoryTokens.decodeNode(node);
			Integer type = categoryTokens.getType();
			if (type!=null && type.intValue() == CategoryBL.TYPE.LEAF) {
				Integer filterID = categoryTokens.getObjectID();
				if (filterID!=null) {
					if (MenuitemFilterBL.actualizeMenuItemQuery(personBean.getObjectID(), filterID, includeInMenu)) {
						List<FilterInMenuTO> myFilters=FilterBL.loadMyMenuFiltersWithTooltip(personBean, locale);
						session.put(FilterBL.MY_MENU_FILTERS_JSON, FilterInMenuJSON.encodeFiltersInMenu(myFilters));
					}
				}
			}
		}
		JSONUtility.encodeJSONSuccess(servletResponse);
		return null;
	}
	/**
	 * Loads the filter detail for add or edit 
	 * @return
	 */	
	public String edit() {
		boolean modifiable = false;
		FilterFacade filterFacade = null;
		Integer objectID = null;
		Integer projectID = null;
		boolean includeHeader = false;
		boolean includePathPicker = false;
		boolean applyInstant = false;
		if (instant) {
			//load for instant filter dialog
			filterFacade = TreeFilterFacade.getInstance();
			modifiable = true;
			includeHeader = false;
			add=false;
			LOGGER.debug("Load for instant filter dialog");
		} else {
			Integer type = null;
			if (node!=null) {
				includeHeader = true;
				//load dialog from manage filters (add or edit)
				CategoryTokens categoryTokens = CategoryTokens.decodeNode(node);
				String categoryType = categoryTokens.getCategoryType();
				Integer repository = categoryTokens.getRepository();
				type = categoryTokens.getType();
				if (repository!=null && repository.equals(CategoryBL.REPOSITORY_TYPE.PROJECT)) {
					projectID = CategoryBL.getProjectID(categoryType, repository, type, objectID);
				}
				objectID = categoryTokens.getObjectID();
				if (add) {
					LOGGER.debug("Add new filter from manage filters");
					filterFacade = (FilterFacade)CategoryFacadeFactory.getInstance().getLeafFacade(categoryType);
				} else {
					LOGGER.debug("Edit filter " + objectID  + " from manage filters");
					filterFacade = (FilterFacade)CategoryFacadeFactory.getInstance().getLeafFacade(categoryType, objectID);
				}
				modifiable = CategoryBL.isModifiable(filterFacade, categoryType, repository, type, objectID, add, personBean);
			} else {
				if (filterID!=null && filterType!=null) {
					if (fromNavigatorContextMenu) {
						//possible enter/change name etc.
						includeHeader = true;
					} else {
						//from instant in the navigator upper part
						applyInstant = true;
						includeHeader = false;	
					}
					//load filter in item navigator upper area
					//in this case modifiable has another meaning: the filter is freely editable anyway (for "instant apply" and "save as"),
					//but the save button is available only if there is already a saved filter behind and this is modifiable by the current user
					//so modifiable in this case means whether "Save" button is active ("Save as" is always active)
					modifiable = false;
					String categoryType = CATEGORY_TYPE.ISSUE_FILTER_CATEGORY;
					switch (filterType) {
					case QUERY_TYPE.BASKET:
					case QUERY_TYPE.DASHBOARD:
						//basket and dashboard can't be translated to a tree filter: render as a new filter
						add = true;
						break;
					case QUERY_TYPE.INSTANT:
						//render the last instant
						instant = true;
						LOGGER.debug("Load last instant for item navigator");
						break;
					case QUERY_TYPE.LUCENE_SEARCH:
						//load later in getDetailJSON() using queryContextID
						break;
					case QUERY_TYPE.PROJECT_RELEASE:
						//transform the project/release to a tree filter
						//filterID is actually a projectID or releaseID
						objectID = filterID;
						LOGGER.debug("Load project/release based (" + filterID + ") for item navigator");
						break;
					case QUERY_TYPE.SAVED:
						objectID = filterID;
						TQueryRepositoryBean queryRepositoryBean = FilterBL.loadByPrimaryKey(objectID);
						Integer repository = null;
						if (queryRepositoryBean!=null) {
							repository = queryRepositoryBean.getRepositoryType();
							type = TYPE.LEAF;
							if (repository!=null && repository.equals(CategoryBL.REPOSITORY_TYPE.PROJECT)) {
								projectID = CategoryBL.getProjectID(categoryType, repository, type, objectID);
							}
						}
						filterFacade = (FilterFacade)CategoryFacadeFactory.getInstance().getLeafFacade(categoryType, objectID);
						//a saved filter is behind
						modifiable = CategoryBL.isModifiable(filterFacade, categoryType, repository, type, objectID, false, personBean);
						LOGGER.debug("Load saved filter based (" + filterID + ") for item navigator");
						break;
					case QUERY_TYPE.STATUS:
						objectID = filterID;
						LOGGER.debug("Load status based (" + filterID + ") for item navigator");
						break;
					}
					if (filterFacade==null) {
						filterFacade = TreeFilterFacade.getInstance();
					}
				} else {
					if (add) {
						//add from item navigator menu (accordion left), filter context menu 
						filterFacade = TreeFilterFacade.getInstance();
						modifiable = true;
						includeHeader = true;
						includePathPicker = true;
					}
				}
			}
		}
		JSONUtility.encodeJSON(servletResponse, 
				filterFacade.getDetailJSON(includeHeader, includePathPicker,  objectID, filterType, queryContextID, add, modifiable, instant, clearFilter, applyInstant, personBean, projectID, locale));
		return null;
	}
	
	/**
	 * Whether the label is valid
	 * @return
	 */
	public String isValidLabel() {
		CategoryTokens categoryTokens = CategoryTokens.decodeNode(node);
		String categoryType = categoryTokens.getCategoryType();
		FilterFacade filterFacade  = (FilterFacade)CategoryFacadeFactory.getInstance().getLeafFacade(categoryType);
		String errorKey  = filterFacade.isValidLabel(node, personBean.getObjectID(), filterSubType, label, add);
		if (errorKey==null) {
			JSONUtility.encodeJSONSuccess(servletResponse);
		} else {
			String filterKey = null;
			if (categoryType.equals(CategoryBL.CATEGORY_TYPE.ISSUE_FILTER_CATEGORY)) {
				filterKey = "admin.customize.queryFilter.lbl.issueFilter"; 
			} else {
				filterKey = "admin.customize.automail.filter.lblUniqueRequired";
			}
			String errorMessage = LocalizeUtil.getParametrizedString(errorKey, 
						new Object[] {LocalizeUtil.getLocalizedTextFromApplicationResources(filterKey, locale)} , locale);
			JSONUtility.encodeJSON(servletResponse, 
					CategoryJSON.createNodeResultJSON(false, node, errorMessage));
		}
		return null;
	}
	
	/**
	 * Saves a filter (new or edited)
	 * @return
	 */
	public String save() {
		CategoryTokens categoryTokens = null;
		FilterFacade filterFacade=null;
		String categoryType = null;
		Integer repository = null;
		Integer type = null;
		Integer objectID = null;
		if (node!=null) {
			//add or edit a filter in manage filters or "save as" from filter in item navigator
			categoryTokens = CategoryTokens.decodeNode(node);
			categoryType = categoryTokens.getCategoryType();
			objectID = categoryTokens.getObjectID();
			repository = categoryTokens.getRepository();
			type = categoryTokens.getType();
			if (add) {
				filterFacade = (FilterFacade)CategoryFacadeFactory.getInstance().getLeafFacade(categoryType);
			} else {
				filterFacade = (FilterFacade)CategoryFacadeFactory.getInstance().getLeafFacade(categoryType, objectID);
			}
		} else {
			if (filterID!=null && filterType!=null) {
				//"save" from filter in upper item navigator or from add/edit filter from filter context menu
				categoryType = CATEGORY_TYPE.ISSUE_FILTER_CATEGORY;
				switch (filterType) {
				case QUERY_TYPE.SAVED:
					objectID = filterID;
					TQueryRepositoryBean queryRepositoryBean = FilterBL.loadByPrimaryKey(objectID);
					if (queryRepositoryBean!=null) {
						repository = queryRepositoryBean.getRepositoryType();
						//filter is always a leaf in the filter tree
						type = Integer.valueOf(TYPE.LEAF);
					}
					if (!fromNavigatorContextMenu && queryRepositoryBean!=null) {
						//save from upper item navigator
						//viewID and label is not submitted, but in order to maintain the actual values we need to set it to the actual value before save
						viewID = queryRepositoryBean.getViewID();
						label = queryRepositoryBean.getLabel();
					}
					categoryTokens = new CategoryTokens(categoryType, repository, TYPE.LEAF, objectID);
					filterFacade = (FilterFacade)CategoryFacadeFactory.getInstance().getLeafFacade(categoryType, objectID);
					break;
				}
			}
		}
		if (filterSubType==null) {
			//"save" or "save as" from item navigator: filterSubType not submitted
			filterSubType = TQueryRepositoryBean.QUERY_PURPOSE.TREE_FILTER;
		}
		if (filterID==null && categoryType.equals(CategoryBL.CATEGORY_TYPE.ISSUE_FILTER_CATEGORY) && !modifiable  && !add) {
			//save from "manage filters": only menu item/style field/view change for a not modifiable issue filter
			if (MenuitemFilterBL.actualizeMenuItemQuery(personBean.getObjectID(), objectID, includeInMenu, styleField)) {
				List<FilterInMenuTO> myFilters=FilterBL.loadMyMenuFiltersWithTooltip(personBean, locale);
				session.put(FilterBL.MY_MENU_FILTERS_JSON, FilterInMenuJSON.encodeFiltersInMenu(myFilters));
			}
			JSONUtility.encodeJSON(servletResponse, CategoryJSON.createSaveResultJSON(true, node, objectID));
		} else {
			String errorKey = null;
			if (filterID==null && filterFacade!=null) {
				errorKey = filterFacade.isValidLabel(node, personBean.getObjectID(), filterSubType, label, add);
			}
			if (errorKey==null) {
				String filterExpression = null;
				try {
					if (categoryType.equals(CategoryBL.CATEGORY_TYPE.ISSUE_FILTER_CATEGORY)) {
						if (filterUpperTO==null) {
							//nothing was submitted to selects
							filterUpperTO = new FilterUpperTO();
						}
						filterUpperTO.setFieldExpressionSimpleList(FilterSubmitHandler.createFieldExpressionSimpleListAfterSubmit(
								simpleMatcherRelationMap, simpleDisplayValueMap, locale));
					}
					List<FieldExpressionInTreeTO> fieldExpressionsInTreeList =
						FilterSubmitHandler.createFieldExpressionInTreeListAfterSubmit(
								inTreeDisplayValueMap,
								inTreeMatcherRelationMap,
								fieldMap,
								fieldMomentMap,
								operationMap,
								parenthesisOpenedMap,
								parenthesisClosedMap,
								fieldExpressionOrderMap,
								locale);
					filterExpression = filterFacade.getFilterExpression(tqlExpression, filterUpperTO, fieldExpressionsInTreeList);
					if (CategoryBL.isModifiable(filterFacade, categoryType, repository, type, objectID, add, personBean)) {
						categoryTokens = FilterBL.save(filterFacade,
								categoryTokens, label, filterSubType, filterExpression, viewID, add, personBean.getObjectID(), locale);
						Integer savedFilterID = categoryTokens.getObjectID();
						JSONUtility.encodeJSON(servletResponse,
								CategoryJSON.createSaveResultJSON(true, CategoryTokens.encodeNode(categoryTokens), savedFilterID));
						if (filterID==null || fromNavigatorContextMenu) {
							//change the  menu entry or style field only if from manage filter or "save as" from item navigator
							if (MenuitemFilterBL.actualizeMenuItemQuery(personBean.getObjectID(), savedFilterID, includeInMenu, styleField)) {
								List<FilterInMenuTO> myFilters=FilterBL.loadMyMenuFiltersWithTooltip(personBean, locale);
								session.put(FilterBL.MY_MENU_FILTERS_JSON, FilterInMenuJSON.encodeFiltersInMenu(myFilters));
							}
						}
					} else {
						JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeJSONFailure(
							LocalizeUtil.getParametrizedString("common.err.noModifyRight", 
								new Object[] {LocalizeUtil.getLocalizedTextFromApplicationResources(filterFacade.getLabelKey(), locale)}, locale)));
					}
				} catch (Exception e) {
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug(ExceptionUtils.getStackTrace(e));
						}
						errorKey = e.getMessage();
						if (errorKey==null) {
							errorKey = "common.err.failure";
						}
						String errorMessage = LocalizeUtil.getLocalizedTextFromApplicationResources(errorKey, locale);
						JSONUtility.encodeJSON(servletResponse,
								CategoryJSON.createNodeResultJSON(false, node, errorMessage));
					}
				} else {
					String filterKey = null;
					if (categoryType.equals(CategoryBL.CATEGORY_TYPE.ISSUE_FILTER_CATEGORY)) {
						filterKey = "admin.customize.queryFilter.lbl.issueFilter"; 
					} else {
						filterKey = "admin.customize.automail.filter.lblUniqueRequired";
					}
					String errorMessage = LocalizeUtil.getParametrizedString(errorKey, 
								new Object[] {LocalizeUtil.getLocalizedTextFromApplicationResources(filterKey, locale)} , locale);
					JSONUtility.encodeJSON(servletResponse, 
							CategoryJSON.createNodeResultJSON(false, node, errorMessage));
				}
		}
		return null;
	}
	
	
	/**
	 * Called from context menu for filters from item navigator
	 * @return
	 */
	public String filterIsModifiable(){
		boolean editFilter = false;
		if (filterID!=null) {
			FilterFacade filterFacade = TreeFilterFacade.getInstance();
			String categoryType = CATEGORY_TYPE.ISSUE_FILTER_CATEGORY;
			TQueryRepositoryBean queryRepositoryBean = FilterBL.loadByPrimaryKey(filterID);
			if (queryRepositoryBean!=null) {
				Integer repository = queryRepositoryBean.getRepositoryType();
				Integer type = TYPE.LEAF;
				if (CategoryBL.isModifiable(filterFacade, categoryType, repository, type, filterID, add, personBean)) {
					editFilter = true;
				}
			}
		}
		JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeJSONBoolean(editFilter));
		return null;
	}
	/**
	 * @param node the nodeId to set
	 */
	public void setNode(String node) {
		this.node = node;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

	public void setTqlExpression(String tqlExpression) {
		this.tqlExpression = tqlExpression;
	}

	public void setFilterSubType(Integer filterSubType) {
		this.filterSubType = filterSubType;
	}

	public void setIncludeInMenu(boolean includeInMenu) {
		this.includeInMenu = includeInMenu;
	}

	public void setStyleField(Integer styleField) {
		this.styleField = styleField;
	}

	public void setViewID(String viewID) {
		this.viewID = viewID;
	}

	public void setInstant(boolean instant) {
		this.instant = instant;
	}	

	public FilterUpperTO getFilterUpperTO() {
		return filterUpperTO;
	}

	public void setFilterUpperTO(FilterUpperTO filterSelectsTO) {
		this.filterUpperTO = filterSelectsTO;
	}

	public Map<String, String> getSimpleDisplayValueMap() {
		return simpleDisplayValueMap;
	}

	public void setSimpleDisplayValueMap(Map<String, String> simpleDisplayValueMap) {
		this.simpleDisplayValueMap = simpleDisplayValueMap;
	}

	public Map<Integer, Integer> getSimpleMatcherRelationMap() {
		return simpleMatcherRelationMap;
	}

	public void setSimpleMatcherRelationMap(
			Map<Integer, Integer> simpleMatcherRelationMap) {
		this.simpleMatcherRelationMap = simpleMatcherRelationMap;
	}

	public Map<String, String> getInTreeDisplayValueMap() {
		return inTreeDisplayValueMap;
	}

	public void setInTreeDisplayValueMap(Map<String, String> inTreeDisplayValueMap) {
		this.inTreeDisplayValueMap = inTreeDisplayValueMap;
	}

	public Map<Integer, Integer> getInTreeMatcherRelationMap() {
		return inTreeMatcherRelationMap;
	}

	public void setInTreeMatcherRelationMap(
			Map<Integer, Integer> inTreeMatcherRelationMap) {
		this.inTreeMatcherRelationMap = inTreeMatcherRelationMap;
	}

	public Map<Integer, Integer> getFieldMap() {
		return fieldMap;
	}

	public void setFieldMap(Map<Integer, Integer> fieldMap) {
		this.fieldMap = fieldMap;
	}

	public Map<Integer, Integer> getFieldMomentMap() {
		return fieldMomentMap;
	}

	public void setFieldMomentMap(Map<Integer, Integer> fieldMomentMap) {
		this.fieldMomentMap = fieldMomentMap;
	}

	public Map<Integer, Integer> getOperationMap() {
		return operationMap;
	}

	public void setOperationMap(Map<Integer, Integer> operationMap) {
		this.operationMap = operationMap;
	}

	public Map<Integer, Integer> getParenthesisOpenedMap() {
		return parenthesisOpenedMap;
	}

	public void setParenthesisOpenedMap(Map<Integer, Integer> parenthesisOpenedMap) {
		this.parenthesisOpenedMap = parenthesisOpenedMap;
	}

	public Map<Integer, Integer> getParenthesisClosedMap() {
		return parenthesisClosedMap;
	}

	public void setParenthesisClosedMap(Map<Integer, Integer> parenthesisClosedMap) {
		this.parenthesisClosedMap = parenthesisClosedMap;
	}

	public SortedMap<Integer, Integer> getFieldExpressionOrderMap() {
		return fieldExpressionOrderMap;
	}

	public void setFieldExpressionOrderMap(
			SortedMap<Integer, Integer> fieldExpressionOrderMap) {
		this.fieldExpressionOrderMap = fieldExpressionOrderMap;
	}

	public void setModifiable(boolean modifiable) {
		this.modifiable = modifiable;
	}

    public void setClearFilter(boolean clearFilter) {
        this.clearFilter = clearFilter;
    }

	public void setFilterID(Integer filterID) {
		this.filterID = filterID;
	}

	public void setFilterType(Integer filterType) {
		this.filterType = filterType;
	}

	public void setQueryContextID(Integer queryContextID) {
		this.queryContextID = queryContextID;
	}

	public void setFromNavigatorContextMenu(boolean fromNavigatorContextMenu) {
		this.fromNavigatorContextMenu = fromNavigatorContextMenu;
	}

    
	
	
}
