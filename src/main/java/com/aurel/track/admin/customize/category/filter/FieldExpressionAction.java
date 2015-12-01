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

import javax.servlet.http.HttpServletResponse;

import com.aurel.track.beans.TPersonBean;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.GeneralUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * Action executed as a result of changing a fieldExpression
 */
public class FieldExpressionAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware {
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private Locale locale;
	private TPersonBean personBean;
	private boolean issueFilter;
	private boolean instant; 
	private Integer fieldID;
	private Integer index;
	private String stringValue;
	private Integer matcherID;
	private boolean withParameter;
	//selected projectIDs used for getting the datasource for selects depending on projects.
	//the projectIDs should be sent as request parameters (only for select matcher, and select field)
	private Integer[] projectIDs;
	private Integer[] itemTypeIDs;
	//whether the matcher change comes from a simple or an "in tree" filter expression
	private boolean inTree;
	
	@Override
	public void prepare() throws Exception {
		locale = (Locale)session.get(Constants.LOCALE_KEY);
		personBean = ((TPersonBean) session.get(Constants.USER_KEY));
		withParameter = issueFilter && !instant;	
		if (projectIDs==null || projectIDs.length==0 || (projectIDs.length==1 && projectIDs[0]==null)) {
			//somehow struts transforms the empty projectIDs parameter to [null]
			List<TProjectBean> projectBeans = ProjectBL.loadProjectsWithReadIssueRight(personBean.getObjectID());
			projectIDs = GeneralUtils.createIntegerArrFromCollection(GeneralUtils.createIntegerListFromBeanList(projectBeans));
		}
		if (itemTypeIDs!=null && itemTypeIDs.length==1 && itemTypeIDs[0]==null) {
			//somehow struts transforms the empty itemTypeIDs parameter to [null]
			itemTypeIDs = null;
		}
	}
		
	/**
	 * Get the JSON data after a matcher relation change: only the value part is actualized
	 * This can be called from a simple or from an "in tree" filter expression
	 * depending on filter expression type
	 */
	public String selectMatcher() {
		String baseValueName;
		String baseValueItemId;
		Integer indexValue;
		//the name differs: simple are fieldID based, the "in tree"s are index based
		if (inTree) {
			baseValueName = FieldExpressionBL.IN_TREE + FieldExpressionBL.VALUE_BASE_NAME;
			baseValueItemId = FieldExpressionBL.IN_TREE + FieldExpressionBL.VALUE_BASE_ITEM_ID;
			indexValue = index;
		} else {
			baseValueName = FieldExpressionBL.SIMPLE + FieldExpressionBL.VALUE_BASE_NAME;
			baseValueItemId = FieldExpressionBL.SIMPLE + FieldExpressionBL.VALUE_BASE_ITEM_ID;
			indexValue = fieldID;
		}
		JSONUtility.encodeJSON(servletResponse,  FieldExpressionBL.selectMatcher(
				projectIDs, itemTypeIDs, baseValueName, baseValueItemId, indexValue, stringValue, fieldID, matcherID, true, personBean, locale));
		return null;
	}
	
	/**
	 * Get the JSON data after a field change: the matcher list and the value part is actualized 
	 * This can be called only from an "in tree" filter expression
	 */
	public String selectField() {
		//stringValue parameter is null instead of submitted stringValue: it could be initialized with an optionID if field 
		//is changed form a select type to an integer type which is not the expected behavior
		String baseValueName = FieldExpressionBL.IN_TREE + FieldExpressionBL.VALUE_BASE_NAME;
		String baseValueItemId = FieldExpressionBL.IN_TREE + FieldExpressionBL.VALUE_BASE_ITEM_ID;
		String expessionJSON = FieldExpressionBL.selectField(projectIDs, itemTypeIDs, baseValueName, baseValueItemId, index,  null,
				fieldID, matcherID, true, personBean, locale, withParameter);
		JSONUtility.encodeJSON(servletResponse,  expessionJSON);
		return null;
	}
	
	/**
	 * Get the JSON data after a new field is added: prepare a full FieldExpressionInTreeTO JSON
	 * This can be called only for an "in tree" filter expression
	 */
	public String addField() {
		String expessionJSON = FilterJSON.getFieldExpressionInTreeJSON(
				FieldExpressionBL.loadFilterExpressionInTree(projectIDs, itemTypeIDs, fieldID, index, true, 
						personBean, locale, withParameter, !issueFilter), !issueFilter);
		JSONUtility.encodeJSON(servletResponse, expessionJSON);
		return null;
	}	

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	public void setIssueFilter(boolean issueFilter) {
		this.issueFilter = issueFilter;
	}

	public Integer getFieldID() {
		return fieldID;
	}

	public void setFieldID(Integer fieldID) {
		this.fieldID = fieldID;
	}

	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public void setMatcherID(Integer matcherID) {
		this.matcherID = matcherID;
	}

	public void setInstant(boolean instant) {
		this.instant = instant;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public boolean isInTree() {
		return inTree;
	}

	public void setInTree(boolean inTree) {
		this.inTree = inTree;
	}

	public void setProjectIDs(Integer[] projectIDs) {
		this.projectIDs = projectIDs;
	}

	public void setItemTypeIDs(Integer[] itemTypeIDs) {
		this.itemTypeIDs = itemTypeIDs;
	}
	
}
