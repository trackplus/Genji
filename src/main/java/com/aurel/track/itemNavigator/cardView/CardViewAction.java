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

package com.aurel.track.itemNavigator.cardView;

import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 */
public class CardViewAction extends ActionSupport implements Preparable,SessionAware {

	public static final long serialVersionUID = 400L;

	private Map<String,Object> session;
	private TPersonBean personBean;

	private Integer queryID;
	private Integer queryType;

	private Integer fieldGroup;
	private Integer sortField;
	private boolean sortOrder;
	private String cardViewGroupFilter;
	private Integer optionID;
	private Integer width;

	public void prepare() throws Exception {
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
	}

	public String changeGroupBy(){
		CardViewBL.changeGroupBy(personBean, queryID, queryType, fieldGroup);
		JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		return null;
	}
	public String changeSortBy(){
		CardViewBL.changeSortBy(personBean, queryID, queryType, fieldGroup, sortField, sortOrder);
		JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		return null;
	}
	public String cardFieldOptionsChange(){
		CardViewBL.cardFieldOptionsChange(personBean, queryID, queryType, fieldGroup, cardViewGroupFilter);
		JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		return null;
	}

	public String changeOptionWidth(){
		CardViewBL.changeOptionWidth(personBean, queryID, queryType, fieldGroup, optionID, width);
		JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		return null;
	}
	
	public void setSession(Map<String, Object> stringObjectMap) {
		this.session=stringObjectMap;
	}

	public void setQueryType(Integer queryType) {
		this.queryType = queryType;
	}

	public void setQueryID(Integer queryID) {
		this.queryID = queryID;
	}


	public void setFieldGroup(Integer fieldGroup) {
		this.fieldGroup = fieldGroup;
	}

	public void setSortField(Integer sortField) {
		this.sortField = sortField;
	}

	public void setSortOrder(boolean sortOrder) {
		this.sortOrder = sortOrder;
	}

	public void setCardViewGroupFilter(String cardViewGroupFilter) {
		this.cardViewGroupFilter = cardViewGroupFilter;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public void setOptionID(Integer optionID) {
		this.optionID = optionID;
	}
}

