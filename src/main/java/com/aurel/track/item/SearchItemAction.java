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


package com.aurel.track.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.errors.ErrorHandlerJSONAdapter;
import com.aurel.track.itemNavigator.ItemNavigatorBL;
import com.aurel.track.itemNavigator.QueryContext;
import com.aurel.track.itemNavigator.lastExecuted.LastExecutedBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.tql.TqlBL;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;


public final class SearchItemAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware {
	private static final long serialVersionUID = -4204751459631228705L;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private String querySearchExpression;
	private boolean externalAction;
	private Integer workItemID;
	private Locale locale; 
	private TPersonBean personBean;
	protected Integer personID;
	
	public void prepare(){
		locale = (Locale)session.get(Constants.LOCALE_KEY);
		//tPerson = (TPersonBean) session.get(Constants.USER_KEY);
		if (session.get(Constants.USER_KEY)!=null) {
			personBean = (TPersonBean) session.get(Constants.USER_KEY);
			personID = personBean.getObjectID();
		}
	}
	private void encodeError(List<String> errors){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS,false);
		JSONUtility.appendStringList(sb, "errors", errors,true);
		sb.append("}");
		JSONUtility.encodeJSON(ServletActionContext.getResponse(), sb.toString());
	}
	
	private void encodeItem(Integer workItemID){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS,true);
		JSONUtility.appendIntegerValue(sb, "workItem", workItemID,true);
		sb.append("}");
		JSONUtility.encodeJSON(ServletActionContext.getResponse(), sb.toString());
	}
	
	@Override
	public String execute(){
		List<String> errors=new ArrayList<String>();
		if (querySearchExpression==null || "".equals(querySearchExpression.trim())){
			errors.add(getText("report.reportError.error.noSearchExpression"));
			encodeError(errors);
			return null;
		}
		workItemID=null;
		try {
			workItemID = Integer.valueOf(querySearchExpression);
		} catch(Exception ex){}
		//if searchString is a valid integer, avoid lucene search
		if (workItemID != null){
			TWorkItemBean workItemBean = ItemBL.loadWorkItemSystemAttributes(workItemID);
			//does the workItem exists?
			if (workItemBean!=null) {
				encodeItem(workItemID);
			}else{
				List<Integer> list=new ArrayList<Integer>();
				list.add(workItemID);
				errors.add(getText("report.reportError.error.noSuchItem",list));
				encodeError(errors);
			}
			return null;
		} else {
			List<ErrorData> errorList=new ArrayList<ErrorData>();
			//only verify whether there are some results: no error is returned
			Map<Integer, String> highlightedTextMap = null;
			//FIXME comment this once highlighter rendering is implemented
			//externalAction = false;
			if (externalAction) {
				highlightedTextMap = new HashMap<Integer, String>();
			}
			TqlBL.luceneQuery(querySearchExpression, externalAction, locale, highlightedTextMap, errorList);
			if (highlightedTextMap!=null && highlightedTextMap.size()>0) {
				Map<Integer, String> highlightedFragmentsMap = new HashMap<Integer, String>();
				Map<Integer, String> highlightedTitlesMap = new HashMap<Integer, String>();
				SearchItemBL.getDocumentsToFragments(personID, highlightedTextMap, highlightedFragmentsMap, highlightedTitlesMap);
				JSONUtility.encodeJSON(servletResponse, SearchItemJSON.encodeJson(highlightedFragmentsMap, highlightedTitlesMap));
				return null;
			}
			if(!errorList.isEmpty()){
				for(int i=0;i<errorList.size();i++){
					ErrorData errorData=errorList.get(i);
					errors.add(ErrorHandlerJSONAdapter.createMessage(errorData, locale));
				}
				encodeError(errors);
				return null;
			}
			QueryContext queryContext = new QueryContext();
			queryContext.setQueryType(ItemNavigatorBL.QUERY_TYPE.LUCENE_SEARCH);
			queryContext.setQueryID(1);
			queryContext.setFilterExpression(querySearchExpression);
			LastExecutedBL.storeLastExecutedQuery(personBean.getObjectID(), queryContext);
			JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
			return null;
		}
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
	
	public Integer getWorkItemID() {
		return workItemID;
	}

	public void setWorkItemID(Integer workItemID) {
		this.workItemID = workItemID;
	}
	public void setQuerySearchExpression(String querySearchExpression) {
		this.querySearchExpression = querySearchExpression;
	}
	
	public void setExternalAction(boolean externalAction) {
		this.externalAction = externalAction;
	}
	
	
}
