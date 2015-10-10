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


package com.aurel.track.item.massOperation;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.util.GeneralUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.interceptor.I18nInterceptor;

/**
 * Main action for mass operation
 * @author Tamas
 *
 */
public class MassOperationAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware, ApplicationAware  {
	
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private Map<String, Object> application;
	private HttpServletResponse servletResponse;
	private Locale locale;
	private TPersonBean personBean;
	private Integer personID;
	private String selectedIssueIDs;
	//when errors are detected during the mass operation for some workItems 
	//the user still can confirm to go on with the mass operation for the rest of the workItems 
	private boolean confirmSave;
	//whether the last submit was a project refresh
	private boolean projectRefresh;
	private Integer projectID;	
	private boolean issueTypeRefresh;
	private Integer issueTypeID;
	
	//whether it is bulk edit or bulk copy
	private boolean bulkCopy = false;
	//in case of copy whether it is a deep copy
	private boolean deepCopy;
	//in case of copy whether to copy attachments
	private boolean copyAttachments;
	//in case of copy whether to copy also the children
	private boolean copyChildren;
	private boolean copyWatchers;
	
	/**
	 * the key is string (not integer) because submitting a
	 * negative numbered key does not populate the map (struts2 bug?)
	 */
	private Map<String, Boolean> selectedFieldMap;
	private Map<String, Integer> setterRelationMap;
	private Map<String, String> displayValueMap;
	private int[] issueIDsArr;
	
	
	/**
	 * Prepare the data 
	 */
	public void prepare() throws Exception {
		locale = (Locale)session.get(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE);
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		personID = personBean.getObjectID();
		if (selectedIssueIDs!=null) {
			issueIDsArr = GeneralUtils.createIntArrFromIntegerList(GeneralUtils.createIntegerListFromStringArr(selectedIssueIDs.split(",")));
		}
	}

	/**
	 * Render the mass operation page first time or after project/issueType refresh 
	 */
	@Override
	public String execute() {
		String expressions = MassOperationBL.getMassOperationExpressions(
				issueIDsArr, personBean, locale, projectRefresh, projectID, issueTypeRefresh, issueTypeID,
				selectedFieldMap, setterRelationMap, displayValueMap, bulkCopy);
		JSONUtility.encodeJSON(servletResponse, expressions);
		return null;
	}
	
	/**
	 * Save the modifications for the selected issues
	 * @return
	 */
	public String save() {
		boolean useProjectSpecificID = false;
		ApplicationBean appBean =(ApplicationBean)application.get(Constants.APPLICATION_BEAN);
		if(appBean.getSiteBean().getProjectSpecificIDsOn()!=null){
			useProjectSpecificID=appBean.getSiteBean().getProjectSpecificIDsOn().booleanValue();
		}
		if (!bulkCopy) {
			if (selectedFieldMap==null || selectedFieldMap.isEmpty()) {
				JSONUtility.encodeJSONFailure(servletResponse, getText("itemov.massOperation.err.noFieldChangeSelected"), null);
				return null;
			}
		}	
		try {
			MassOperationBL.saveFromAction(issueIDsArr, selectedFieldMap, setterRelationMap, displayValueMap,
					bulkCopy, confirmSave, deepCopy, copyAttachments, copyChildren, copyWatchers, personID, locale, useProjectSpecificID);
			JSONUtility.encodeJSONSuccess(servletResponse);
		} catch (MassOperationException e) {
			JSONUtility.encodeJSON(servletResponse, MassOperationJSON.getErrorMessagesJSON(e, locale));
		}
		return null;
	}
	
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
	
	public void setConfirmSave(boolean confirmSave) {
		this.confirmSave = confirmSave;
	}

	/**
	 * THe locale of the rich text editor 
	 * @return
	 */
	public String getLocaleShort() {
		return locale.getLanguage();
	}

	public boolean isBulkCopy() {
		return bulkCopy;
	}

	public void setBulkCopy(boolean bulkCopy) {
		this.bulkCopy = bulkCopy;
	}

	public boolean isDeepCopy() {
		return deepCopy;
	}

	public void setDeepCopy(boolean deepCopy) {
		this.deepCopy = deepCopy;
	}

	public boolean isCopyAttachments() {
		return copyAttachments;
	}

	public void setCopyAttachments(boolean copyAttachments) {
		this.copyAttachments = copyAttachments;
	}

	public void setCopyChildren(boolean copyChildren) {
		this.copyChildren = copyChildren;
	}	
	
	public void setCopyWatchers(boolean copyWatchers) {
		this.copyWatchers = copyWatchers;
	}

	public void setSelectedIssueIDs(String selectedIssueIDs) {
		this.selectedIssueIDs = selectedIssueIDs;
	}

	public Map<String, String> getDisplayValueMap() {
		return displayValueMap;
	}

	public void setDisplayValueMap(Map<String, String> displayValueMap) {
		this.displayValueMap = displayValueMap;
	}

	public Map<String, Integer> getSetterRelationMap() {
		return setterRelationMap;
	}

	public void setSetterRelationMap(Map<String, Integer> setterRelationMap) {
		this.setterRelationMap = setterRelationMap;
	}

	public Map<String, Boolean> getSelectedFieldMap() {
		return selectedFieldMap;
	}

	public void setSelectedFieldMap(Map<String, Boolean> selectedFieldMap) {
		this.selectedFieldMap = selectedFieldMap;
	}

	public void setProjectRefresh(boolean projectRefresh) {
		this.projectRefresh = projectRefresh;
	}

	
	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	public void setIssueTypeRefresh(boolean issueTypeRefresh) {
		this.issueTypeRefresh = issueTypeRefresh;
	}

	public void setIssueTypeID(Integer issueTypeID) {
		this.issueTypeID = issueTypeID;
	}

	public void setApplication(Map<String, Object> application) {
		this.application = application; 
	}
	
}
