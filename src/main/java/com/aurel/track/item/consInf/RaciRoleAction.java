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


package com.aurel.track.item.consInf;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.item.ItemDetailBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.StringArrayParameterUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * Action class with "load", "save" (add from popup) operations
 * for consultant/informant groups/persons  
 * @author Tamas Ruff
 *
 */
public class RaciRoleAction extends ActionSupport implements Preparable,SessionAware {
		
	private static final long serialVersionUID = 3809471451325218395L;
	private Map<String, Object> session;
	private Locale locale;
	private boolean create;
	private String raciRole;
	private Integer workItemID;
	private Integer projectID;
	private Integer issueTypeID;
	private ConsInfEdit consInfEdit;
	
	//internals
	private ConsInfShow consInfShow;
	private Integer person;
	
	public void prepare() throws Exception {
		locale = (Locale) session.get(Constants.LOCALE_KEY);
		create=(workItemID==null);
		WorkItemContext ctx=(WorkItemContext)session.get("workItemContext");
		person = ((TPersonBean) session.get(Constants.USER_KEY)).getObjectID();
		if(ctx!=null){
			consInfShow = ctx.getConsInfShow();
		}else{
			consInfShow = new ConsInfShow();
			ConsInfBL.loadConsInfFromDb(workItemID, person, consInfShow);
		}
		consInfEdit = new ConsInfEdit();
	}
	
	/**
	 * Loads the not yet selected raci-persons.
	 * Depending on create or edit mode 
	 * it loads from session or from database 
	 */
	public String load() {
		List<ErrorData> errors;
		if (create) {
			errors = RaciRoleBL.loadFromSession(projectID, 
					issueTypeID, raciRole, consInfEdit, consInfShow);
		}
		else {
			errors = RaciRoleBL.loadFromDb(projectID, 
					issueTypeID, workItemID, raciRole, consInfEdit);
		}
		if (errors!=null && !errors.isEmpty()) {
			Iterator<ErrorData> iterator = errors.iterator();
			ErrorData errorData = iterator.next();
			addActionError(getText(errorData.getResourceKey()));
		}
		JSONUtility.encodeJSON(ServletActionContext.getResponse(), ItemDetailBL.encodeJSON_AddRole(consInfEdit));
		return null;
		
	}
	
	/**
	 * Saves the checked raci-persons.
	 * Depending on create or edit mode 
	 * it saves in session or in database 
	 * @return
	 */
	public String save() {
		String selectedPersonsStr=consInfEdit.getSelectedPersonsStr();
		if(selectedPersonsStr!=null){
			consInfEdit.setSelectedPersons(StringArrayParameterUtils.splitSelectionAsIntegerArray(selectedPersonsStr,","));
		}
		List<ErrorData> errors;
		if (create) {
			errors = RaciRoleBL.saveToSession(projectID,issueTypeID, person, raciRole, consInfEdit, consInfShow);
		} else {
			errors = RaciRoleBL.saveToDb(projectID,issueTypeID,  workItemID, raciRole, consInfEdit, person, locale);
		}
		String result;
		if (errors!=null && !errors.isEmpty()) {
			Iterator<ErrorData> iterator = errors.iterator();
			ErrorData errorData = iterator.next();
			result=JSONUtility.encodeJSONFailure(getText(errorData.getResourceKey()));
		}else{
			result=JSONUtility.encodeJSONSuccess();
		}
		JSONUtility.encodeJSON(ServletActionContext.getResponse(), result);
		return null;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
		
	}

	public String getRaciRole() {
		return raciRole;
	}

	public void setRaciRole(String raciRole) {
		this.raciRole = raciRole;
	}

	public Integer getWorkItemID() {
		return workItemID;
	}

	public void setWorkItemID(Integer workItemID) {
		this.workItemID = workItemID;
	}

	public Integer getProjectID() {
		return projectID;
	}

	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	public Integer getIssueTypeID() {
		return issueTypeID;
	}

	public void setIssueTypeID(Integer issueTypeID) {
		this.issueTypeID = issueTypeID;
	}

	public ConsInfEdit getConsInfEdit() {
		return consInfEdit;
	}

	public void setConsInfEdit(ConsInfEdit consInfEdit) {
		this.consInfEdit = consInfEdit;
	}
	
}
