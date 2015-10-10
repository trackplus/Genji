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

package com.aurel.track.admin.customize.category;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.FilterBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.itemNavigator.filterInMenu.FilterInMenuJSON;
import com.aurel.track.itemNavigator.filterInMenu.FilterInMenuTO;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * A action for a generic hierarchical categories 
 * Currently used by filter categories and report categories  
 * 
 */
public class CategoryAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware {
	private static final long serialVersionUID = 340L;
	private String node;
	private String nodeFrom;
	private String nodeTo;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private TPersonBean personBean;
	private Locale locale;
	private String label;
	//add or edit
	private boolean add;
	//cut or copy - paste
	private boolean copy;
	private boolean deleteConfirmed;
	private Integer replacementID;
	
	//inside project configuration (to limit the filters/reports shown to the project specific ones)
	private Integer projectID;
	//whether to exclude the private nodes (by default automail assignments it is true) 
	private boolean excludePrivate;

	//for report templates: whether we are coming here from an issue navigator
	private boolean fromIssueNavigator;
	/**
	 * Whether or not the checked checkbox should appear near the node
	 * Should be used in filter/report tree (when multiple select possible) not filter/report picker
	 */
	
	
	public void prepare() throws Exception {
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		locale=(Locale) session.get(Constants.LOCALE_KEY);
	}
	
	/**
	 * Expand a tree node from tree
	 * @return
	 */
	public String expand(){
		if (node!=null) {
			//by drag and drop during removing of the node from the old location execute 
			//is also called but then with node == null			
			List<CategoryNodeTO> nodes = (List)CategoryBL.getChildren(
					node, personBean, true, projectID, excludePrivate, false, fromIssueNavigator, locale);
				JSONUtility.encodeJSON(servletResponse, 
					CategoryJSON.getChildrenJSON(nodes));
		}
		return null;
	}

	/**
	 * Loads the grid data for a node
	 * @return
	 */
	public String loadList() {
		if (node!=null) {
			List<CategoryGridRowTO> nodes = (List)CategoryBL.getChildren(
					node, personBean, false, projectID, excludePrivate, false, fromIssueNavigator, locale);
				JSONUtility.encodeJSON(servletResponse, 
					CategoryJSON.getGridRowJSON(nodes));
		}
		return null;
	}
	
	/**
	 * The leaf details for the node
	 * @return
	 */	
	public String leafDetail() {
		if (node!=null) {
			String leafDetail = CategoryBL.getLeafDetail(node, locale);
			JSONUtility.encodeJSON(servletResponse, 
				JSONUtility.encodeJSONSuccess(leafDetail));
		}
		return null;
	}
	
	/**
	 * Loads the detail for add or edit 
	 * @return
	 */
	public String edit() {
		CategoryTokens categoryTokens = CategoryTokens.decodeNode(node);
		String categoryType = categoryTokens.getCategoryType();
		CategoryFacade categoryFacade = CategoryFacadeFactory.getInstance().getCategoryFacade(categoryType);
		Integer repository = categoryTokens.getRepository();
		Integer type = categoryTokens.getType();
		Integer objectID = categoryTokens.getObjectID();
		boolean modifiable = CategoryBL.isModifiable(categoryFacade, categoryType, repository, type, objectID, add, personBean);
		JSONUtility.encodeJSON(servletResponse,
				categoryFacade.getDetailJSON(objectID, add, modifiable, personBean.getObjectID(), locale));
		return null;
	}	
	
	/**
	 * Saves a category (new or edited)
	 * @return
	 */
	public String save() {
		CategoryTokens categoryTokens = CategoryTokens.decodeNode(node);
		String categoryType = categoryTokens.getCategoryType();
		CategoryFacade categoryFacade = CategoryFacadeFactory.getInstance().getCategoryFacade(categoryType);
		Integer repository = categoryTokens.getRepository();
		Integer type = categoryTokens.getType();
		Integer objectID = categoryTokens.getObjectID();
		String errorKey = categoryFacade.isValidLabel(node, personBean.getObjectID(), null, label, add);
		if (errorKey==null) {
			if (CategoryBL.isModifiable(categoryFacade, categoryType, repository, type, objectID, add, personBean)) {
				JSONUtility.encodeJSON(servletResponse, CategoryBL.save(node, label, add, personBean.getObjectID()));
			} else {
				JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeJSONFailure(
						LocalizeUtil.getParametrizedString("common.err.noModifyRight", 
							new Object[] {LocalizeUtil.getLocalizedTextFromApplicationResources(categoryFacade.getLabelKey(), locale)}, locale)));
			}
		} else {
			String errorMessage = LocalizeUtil.getParametrizedString(errorKey, 
						new Object[] {LocalizeUtil.getLocalizedTextFromApplicationResources(
								"admin.customize.queryFilter.lbl.category", locale)} , locale);
			JSONUtility.encodeJSON(servletResponse, 
					CategoryJSON.createNodeResultJSON(false, node, errorMessage));
		}
		return null;
	}
	
	/**
	 * Copy/cut a leaf or a branch
	 * @return
	 */
	public String copy() {
		String result = CategoryBL.copy(nodeFrom, nodeTo, personBean.getObjectID(), copy, locale);
		CategoryTokens categoryTokenFrom = CategoryTokens.decodeNode(nodeFrom);
		String categoryType = categoryTokenFrom.getCategoryType();
		if (categoryType.equals(CategoryBL.CATEGORY_TYPE.ISSUE_FILTER_CATEGORY)) {
			//rearrange the menu filters 
			//(independently whether a menu entry is affected or not, because it is too complicated to test)
			//TODO what about other users' session?
			List<FilterInMenuTO> myFilters=FilterBL.loadMyMenuFiltersWithTooltip(personBean, locale);
			session.put(FilterBL.MY_MENU_FILTERS_JSON, FilterInMenuJSON.encodeFiltersInMenu(myFilters));
		}
		JSONUtility.encodeJSON(servletResponse, result);
		return null;
	}
	
	/**
	 * Deletes a node without dependences
	 * @return
	 */
	public String delete() {
		CategoryBL.delete(node, deleteConfirmed, personBean, locale, servletResponse);
		return null;
	}

	/**
	 * Render the replacement
	 * @return
	 */
	public String renderReplace() {
		String jsonResponse = CategoryBL.getReplacementString(node, null, personBean, locale);
		JSONUtility.encodeJSON(servletResponse, jsonResponse);
		return null;
	}

	/**
	 * Replaces and deletes 
	 * @return
	 */
	public String replaceAndDelete() {
		String jsonResponse = null;
		if (replacementID == null) {
			String errorMessage = getText("common.err.replacementRequired", 
					new String[] {LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.automail.filter.lblReplace", locale)});
			jsonResponse = CategoryBL.getReplacementString(node, errorMessage, personBean, locale);
		} else {
			CategoryBL.replaceAndDelete(node, replacementID);
			jsonResponse = JSONUtility.encodeJSONSuccess();
		}
		JSONUtility.encodeJSON(servletResponse, jsonResponse);
		return null;
	}
	
	/**
	 * @return the nodeId
	 */
	public String getNode() {
		return node;
	}	
	
	/**
	 * @param node the nodeId to set
	 */
	public void setNode(String node) {
		this.node = node;
	}
	
	public void setNodeFrom(String nodeFrom) {
		this.nodeFrom = nodeFrom;
	}

	public void setNodeTo(String nodeTo) {
		this.nodeTo = nodeTo;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

	public void setDeleteConfirmed(boolean deleteConfirmed) {
		this.deleteConfirmed = deleteConfirmed;
	}

	public void setCopy(boolean copy) {
		this.copy = copy;
	}

	public void setReplacementID(Integer replacementID) {
		this.replacementID = replacementID;
	}

	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	public void setExcludePrivate(boolean excludePrivate) {
		this.excludePrivate = excludePrivate;
	}

	public void setFromIssueNavigator(boolean fromIssueNavigator) {
		this.fromIssueNavigator = fromIssueNavigator;
	}
}
