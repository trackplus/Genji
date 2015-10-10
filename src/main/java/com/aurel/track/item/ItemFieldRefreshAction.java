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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.treeConfig.field.FieldConfigBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.runtime.base.FieldsManagerRT;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.mobile.MobileBL;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.StringArrayParameterUtils;
import com.opensymphony.xwork2.Preparable;

/**
 * Used for notify field value change, like cascading select.
 */
public class ItemFieldRefreshAction implements Preparable, SessionAware,ApplicationAware {
	private static final Logger LOGGER = LogManager.getLogger(ItemFieldRefreshAction.class);

	private Integer workItemID;
	private Integer projectID;
	private Integer issueTypeID;
	private Integer actionID;


	private Map<String, Object> session;
	private TPersonBean personBean;
	private Integer personID;
	private Locale locale;
	private String fieldValue;
	private Integer fieldID;
	private Integer statusID;
	private Map application;
	private Map<String,String> fieldValues;
	private String fields;

	public void prepare() throws Exception {
		locale = (Locale) session.get(Constants.LOCALE_KEY);
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		if (personBean!=null) {
			personID = personBean.getObjectID();
		}
	}
	public String execute(){
		String jsonResponse="";
		boolean isNew = ItemBL.itemIsNew(actionID);
		LOGGER.debug("execute(): isNew="+isNew+" actionID="+actionID);
		WorkItemContext workItemContext=null;
		if(isNew){
			workItemContext= (WorkItemContext) session.get("workItemContext");
		}else{
			Boolean accessLevelFlag = Boolean.FALSE;
			workItemContext=ItemBL.getWorkItemContext(actionID, workItemID, projectID, issueTypeID, statusID, accessLevelFlag, personID, locale);
		}
		List<IntegerStringBean> errors=new ArrayList<IntegerStringBean>();

		Map<Integer,Object> fieldValuesObject=ItemSaveBL.unwrapContextExclusiveFields(workItemContext,fieldValues,errors, MobileBL.isMobileApp(session));
		if(!errors.isEmpty()){
			jsonResponse=ItemSaveJSON.encodeConversionErrors(errors);
		}else{
			ItemSaveBL.updateCtx(workItemContext,fieldValuesObject);
			Set<Integer> dependences=ItemFieldRefreshBL.getDependences(fieldID,workItemContext, personBean, locale);
			Set<Integer> modifiedFields = ItemFieldRefreshBL.getModifiedFields(fieldID, workItemContext, personBean, locale);
			if (modifiedFields==null) {
				modifiedFields = dependences;
			}
			StringBuilder sb=new StringBuilder();
			sb.append("{");
			JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
			sb.append("\"data\":{");
			appendFields(workItemContext, sb, dependences, modifiedFields);
			sb.append("}");//end data
			sb.append("}");
			jsonResponse=sb.toString();
		}
		return encodeResult(jsonResponse);
	}

	private void appendFields(WorkItemContext workItemContext, StringBuilder sb, Set<Integer> dependences, Set<Integer> modifiedFields) {
		Iterator<Integer> it=dependences.iterator();
		while(it.hasNext()){
			Integer depFieldID=it.next();
			FieldsManagerRT.refreshField(depFieldID, workItemContext);
		}
		Map<Integer,FieldConfigTO> allFieldConfigTOMap= FieldConfigBL.extractFieldConfigTO(workItemContext, locale);
		Map<Integer,FieldConfigTO> fieldConfigTOMap=new HashMap<Integer, FieldConfigTO>();
		it=modifiedFields.iterator();
		while(it.hasNext()){
			Integer depFieldID=it.next();
			FieldConfigTO fieldConfigTO = allFieldConfigTOMap.get(depFieldID);
			if (fieldConfigTO!=null) {
				fieldConfigTOMap.put(depFieldID,allFieldConfigTOMap.get(depFieldID));
			}
		}
		JSONUtility.appendStringValue(sb, "lastModified", DateTimeUtils.getInstance().formatISODateTime(workItemContext.getWorkItemBean().getLastEdit()));
		JSONUtility.appendJSONValue(sb,"fieldConfigs", ItemActionJSON.encodeFieldConfigTOMap(fieldConfigTOMap));
		JSONUtility.appendJSONValue(sb,"fieldValues", ItemActionJSON.encodeFieldValues(modifiedFields,workItemContext,false, MobileBL.isMobileApp(session)));
		JSONUtility.appendJSONValue(sb,"fieldDisplayValues", ItemActionJSON.encodeFieldDisplayValues(modifiedFields, workItemContext,false, MobileBL.isMobileApp(session)),true);
	}

	public String refreshFields(){
		List<Integer> fieldIdList= StringArrayParameterUtils.splitSelectionAsInteger(fields);
		Set<Integer> fieldIdSet=new HashSet<Integer>();
		for(Integer id:fieldIdList){
			fieldIdSet.add(id);
		}

		WorkItemContext workItemContext=null;
		boolean isNew = ItemBL.itemIsNew(actionID);
		if(isNew){
			workItemContext= (WorkItemContext) session.get("workItemContext");
		}else{
			Boolean accessLevelFlag = Boolean.FALSE;
			workItemContext=ItemBL.getWorkItemContext(actionID, workItemID, projectID, issueTypeID, statusID, accessLevelFlag, personID, locale);
		}

		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		sb.append("data:{");
		appendFields(workItemContext, sb, fieldIdSet, fieldIdSet);
		sb.append("}");//end data
		sb.append("}");
		return encodeResult(sb.toString());
	}
	private static String localizeError(String key, String fieldLabel,Locale locale){
		return LocalizeUtil.getParametrizedString(key,new String[]{fieldLabel},locale);
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
	public void setStatusID(Integer statusID) {
		this.statusID = statusID;
	}


	public Map getApplication() {
		return application;
	}

	public void setApplication(Map application) {
		this.application = application;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public Integer getFieldID() {
		return fieldID;
	}

	public void setFieldID(Integer fieldID) {
		this.fieldID = fieldID;
	}

	public Map<String, String> getFieldValues() {
		return fieldValues;
	}

	public void setFieldValues(Map<String, String> fieldValues) {
		this.fieldValues = fieldValues;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}
}

