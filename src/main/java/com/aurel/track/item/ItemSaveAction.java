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

package com.aurel.track.item;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.item.lock.ItemLockBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.mobile.MobileBL;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.IntegerStringBean;
import com.opensymphony.xwork2.Preparable;

public class ItemSaveAction implements Preparable, SessionAware,ApplicationAware{
	private static final Logger LOGGER = LogManager.getLogger(ItemSaveAction.class);
	private Integer workItemID;
	private Integer projectID;
	private Integer issueTypeID;
	private Integer actionID;
	private Map<String, Object> session;
	private Integer personID;
	private Locale locale;
	private String lastModified;
	private Map<String,String> fieldValues;
	private Map application;
	private TPersonBean personBean;
	private boolean confirm = false;
	
	private Integer workItemIDAbove;
	private boolean inlineEditInNavigator;
	
	public Integer getWorkItemIDAbove() {
		return workItemIDAbove;
	}
	public void setWorkItemIDAbove(Integer workItemIDAbove) {
		this.workItemIDAbove = workItemIDAbove;
	}
	@Override
	public void prepare() throws Exception {
		locale = (Locale) session.get(Constants.LOCALE_KEY);
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		if (personBean!=null) {
			personID = personBean.getObjectID();
		}
	}
	public String execute(){
		boolean useProjectSpecificID=false;
		ApplicationBean appBean =(ApplicationBean)application.get(Constants.APPLICATION_BEAN);
		if(appBean.getSiteBean().getProjectSpecificIDsOn()!=null){
			useProjectSpecificID=appBean.getSiteBean().getProjectSpecificIDsOn().booleanValue();
		}		
		String jsonResponse="";
		boolean isNew = ItemBL.itemIsNew(actionID);
		LOGGER.debug("execute(): isNew="+isNew+" actionID="+actionID);
		WorkItemContext workItemContext=null;
		if(isNew){
			workItemContext= (WorkItemContext) session.get("workItemContext");
		}else{
			Boolean accessLevelFlag = Boolean.FALSE;
			Integer stateID=null;
			workItemContext=ItemBL.getWorkItemContext(actionID, workItemID, projectID, issueTypeID, stateID, accessLevelFlag, personID, locale);
			if (ItemBL.isMove(actionID)) {
			 	Boolean moveChildren = (Boolean)session.get("moveChildren" + workItemID);
			 	if (moveChildren!=null) {
			 		workItemContext.getWorkItemBean().setMoveChildren(moveChildren);
			 	}
			}
		}
		LOGGER.debug("lastModified="+lastModified);
		Date lastModifiedDate= DateTimeUtils.getInstance().parseISODateTime(lastModified);

		List<IntegerStringBean> conversionErrors=new ArrayList<IntegerStringBean>();
		Map<Integer,Object> fieldValuesObject = null;
		if (inlineEditInNavigator) {
			fieldValuesObject = ItemSaveBL.unwrapContextNavigatorInlineEdit(workItemContext, fieldValues, conversionErrors);
		} else {
			fieldValuesObject = ItemSaveBL.unwrapContext(workItemContext,fieldValues,conversionErrors, MobileBL.isMobileApp(session));
		}
		ItemSaveBL.updateCtx(workItemContext,fieldValuesObject);
		if(!conversionErrors.isEmpty()){
			jsonResponse=ItemSaveJSON.encodeConversionErrors(conversionErrors);
		}else{
			if(workItemContext.getWorkItemBean().getObjectID()!=null&&ItemSaveBL.isOutOfDate(lastModifiedDate,workItemContext)){
				LOGGER.info("workitem is out of date: "+workItemContext.getWorkItemBean().getObjectID()+" "+workItemContext.getWorkItemBean().getSynopsis());
				Boolean accessLevelFlag = Boolean.FALSE;
				Integer stateID=null;
				//reload the workItem
				workItemContext=ItemBL.getWorkItemContext(actionID, workItemID, projectID, issueTypeID, stateID, accessLevelFlag, personID, locale);
				jsonResponse=ItemSaveJSON.encodeErrorOutOfDate(workItemContext,locale, useProjectSpecificID,personBean, MobileBL.isMobileApp(session));
			}else{
				HttpSession httpSession = ServletActionContext.getRequest().getSession();
				workItemContext.setSessionID(httpSession.getId());				
				List<ErrorData> errorDataList=new ArrayList<ErrorData>();
				Integer newWorkItemID=ItemBL.saveWorkItem(workItemContext, errorDataList, actionID, confirm);
				if(!errorDataList.isEmpty()) {
					LOGGER.warn("There were " + errorDataList.size() + " error(s) by saving the item");
					if(LOGGER.isDebugEnabled()){
						debugErrors(errorDataList);
					}
					boolean onlyConfirm = true;
					for (ErrorData errorData : errorDataList) {
						if (!errorData.isConfirm()) {
							onlyConfirm = false;
							break;
						}
					}
					int errorCode = ItemSaveBL.ERROR_CODE.GENERAL;
					if (onlyConfirm) {
						errorCode =  ItemSaveBL.ERROR_CODE.CONFIRMATION;
					}
					jsonResponse=ItemSaveJSON.encodeErrorData(errorDataList, errorCode, locale);
				}else{
					String workItemIDDisplay=null;
					if(workItemContext.getWorkItemBean().getObjectID()!=null){
						if(useProjectSpecificID){
							workItemIDDisplay=ItemBL.getSpecificProjectID(workItemContext);
						}else{
							workItemIDDisplay=workItemContext.getWorkItemBean().getObjectID().toString();
						}
					}
					if (!isNew) {
						ItemLockBL.removeLockedIssueBySession(workItemContext.getWorkItemBean().getObjectID(), ServletActionContext.getRequest().getSession().getId());
					}
					jsonResponse=ItemSaveJSON.encodeSuccess(newWorkItemID,workItemIDDisplay, workItemContext.getWorkItemBean().getSynopsis());
					
					/* The newly created work item, will be inserted after workItemIDAbove */
					if(workItemIDAbove != null) {
						DAOFactory.getFactory().getWorkItemDAO().dropNearWorkItem(newWorkItemID, workItemIDAbove, false);
					}
				}
			}
		}
		

		return encodeResult(jsonResponse);
	}
	
	
	private void  debugErrors(List<ErrorData> errorDataList){
		for (int i=0;i<errorDataList.size();i++) {
			ErrorData errorData=errorDataList.get(i);
			LOGGER.debug(i+" key="+errorData.getResourceKey() + " field " + errorData.getFieldID());
		}
	}
	private String encodeResult(String jsonResponse){
		HttpServletResponse httpServletResponse=ServletActionContext.getResponse();
		try {
			JSONUtility.prepareServletResponseJSON(httpServletResponse, true);
			PrintWriter out = httpServletResponse.getWriter();
			out.println(jsonResponse);
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	@Override
	public void setSession(Map<String, Object> session) {
		this.session=session;
	}
	public void setWorkItemID(Integer workItemID) {
		this.workItemID = workItemID;
	}
	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}
	public void setIssueTypeID(Integer issueTypeID) {
		this.issueTypeID = issueTypeID;
	}
	public void setActionID(Integer actionID) {
		this.actionID = actionID;
	}
	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public void setConfirm(boolean confirm) {
		this.confirm = confirm;
	}
	public Map<String, String> getFieldValues() {
		return fieldValues;
	}

	public void setFieldValues(Map<String, String> fieldValues) {
		this.fieldValues = fieldValues;
	}

	public Map getApplication() {
		return application;
	}

	@Override
	public void setApplication(Map application) {
		this.application = application;
	}
	public void setInlineEditInNavigator(boolean inlineEditInNavigator) {
		this.inlineEditInNavigator = inlineEditInNavigator;
	}
	
	
}
