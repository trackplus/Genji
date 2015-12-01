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

package com.aurel.track.admin.user.person;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.user.ActionLogger;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class PersonAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware{

	private static final long serialVersionUID = 1L;
	private Map<String,Object> session;
	private TPersonBean currentUser;
	private Locale locale;
	private HttpServletResponse servletResponse;
	private Integer replacementID;
	private String selectedPersonIDs;
	private Integer userLevel;
	private Integer resetID;
	private String loginName;
	private String field;
	private String value;
	private List<Integer> selectedPersonIDList;
	private boolean isUser;
	//set/reset feature for a user
	private Integer personID;
	private String featureID;
	private boolean featureValue;

	@Override
	public void prepare() {
		locale = (Locale) session.get(Constants.LOCALE_KEY);
		currentUser = (TPersonBean) session.get(Constants.USER_KEY);
		selectedPersonIDList = PersonConfigBL.getPersonIDList(selectedPersonIDs);
	}

	@Override
	public String execute() {
		String result = PersonConfigBL.loadAll(locale, isUser);
		if(result == null) {
			result = "{}";
		}
		JSONUtility.encodeJSON(servletResponse, result);
		return null;
	}

	/**
	 * Deletes a person without dependencies
	 * @return
	 */
	public String delete() {
		if (PersonBL.remianAdminUserAfterDelete(selectedPersonIDs)) {
			JSONUtility.encodeJSON(servletResponse,
					PersonConfigBL.delete(selectedPersonIDList));
			ActionLogger.log(session,"Deleted users " + selectedPersonIDs);
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append("{");
			JSONUtility.appendBooleanValue(sb, "success", false);
			JSONUtility.appendStringValue(sb, "title", LocalizeUtil.getLocalizedTextFromApplicationResources("common.err.failure", locale));
			JSONUtility.appendStringValue(sb, "message", LocalizeUtil.getLocalizedTextFromApplicationResources("admin.user.manage.err.deleteAdmins", locale), true);
			sb.append("}");
			ActionLogger.log(session,"Deleted users not allowed. One system administrator must exists! Selected IDs: " + selectedPersonIDs);
			JSONUtility.encodeJSON(servletResponse, sb.toString());
		}
		return null;
	}

	/**
	 * Renders the replacement persons
	 * @return
	 */
	public String renderReplace() {
		JSONUtility.encodeJSON(servletResponse,
			PersonConfigBL.prepareReplacement(selectedPersonIDList, null, locale));
		return null;
	}

	/**
	 * Replaces and deletes the person
	 * @return
	 */
	public String replaceAndDelete() {
		String jsonResponse = null;
		if (replacementID == null) {
			String errorMessage = getText("common.err.replacementRequired",
					new String[] {getText("admin.user.lbl.user")});
			jsonResponse = PersonConfigBL.prepareReplacement(selectedPersonIDList, errorMessage, locale);
		} else {
			PersonConfigBL.replaceAndDeletePerson(selectedPersonIDList, replacementID, currentUser.getObjectID());
			jsonResponse = JSONUtility.encodeJSONSuccess();
			ActionLogger.log(session,"Deleted users " + selectedPersonIDs + " and replaced with user " + replacementID);
		}
		JSONUtility.encodeJSON(servletResponse, jsonResponse);
		return null;
	}

	/**
	 * Activate persons
	 * @return
	 */
	public String activate() {
		JSONUtility.encodeJSON(servletResponse,
				PersonConfigBL.activateDeactivate(selectedPersonIDs, locale, false));
		ActionLogger.log(session,"Activated users " + selectedPersonIDs);
		return null;
	}

	/**
	 * Deactivate persons
	 * @return
	 */
	public String deactivate() {
		JSONUtility.encodeJSON(servletResponse,
				PersonConfigBL.activateDeactivate(selectedPersonIDs, locale, true));
		ActionLogger.log(session,"Deactivated users " + selectedPersonIDs);
		return null;
	}

	/**
	 * Add/remove a licensed feature
	 * @return
	 */
	public String changeFeature() {
		JSONUtility.encodeJSON(servletResponse,
				PersonConfigBL.changeFeature(personID, featureID, featureValue, locale));
		ActionLogger.log(session,"Change feature for " + personID);
		return null;
	}

	/**
	 * Assign cockpit to user
	 * @return
	 */
	public String cokpitAssignment(){
		JSONUtility.encodeJSON(servletResponse,
				PersonConfigBL.cokpitAssignment(selectedPersonIDs, locale, resetID));
		ActionLogger.log(session,"Assigned new cockpits " + resetID + " to " + selectedPersonIDs);
		return null;
	}

	/**
	 * Synchronize with the ldap server
	 * @return
	 */
	public String syncldap() {
		JSONUtility.encodeJSON(servletResponse,
				PersonConfigBL.syncLdap());
		ActionLogger.log(session,"Started LDAP synchronization");
		return null;
	}

	/**
	 * Is ldap activated
	 * @return
	 */
	public String getLdapIsOn() {
		JSONUtility.encodeJSON(servletResponse,
				PersonConfigBL.getLdapIsOn());
		return null;
	}

	/**
	 * Get the user possible levels
	 * @return
	 */
	public String getUserConfigs() {
		JSONUtility.encodeJSON(servletResponse,
				PersonConfigBL.getUserConfigs(locale));
		return null;
	}

	/**
	 * Set the user level for persons
	 * @return
	 */
	public String userLevel() {
		JSONUtility.encodeJSON(servletResponse,
				PersonConfigBL.setUserLevel(selectedPersonIDs, currentUser, locale, userLevel));
		ActionLogger.log(session,"Changed user level for users " + selectedPersonIDs + " to " + userLevel);
		return null;
	}

	/**
	 * Set properties for all users
	 * @return
	 */
	public String setUserProperty() {
		PersonBL.setUserProperty(this.field, this.value);
		ActionLogger.log(session,"User property " + this.field + " was changed to " + this.value + " for all users");
		return SUCCESS;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public void setReplacementID(Integer replacementID) {
		this.replacementID = replacementID;
	}

	public void setSelectedPersonIDs(String selectedPersonIDs) {
		this.selectedPersonIDs = selectedPersonIDs;
	}

	public void setUserLevel(Integer userLevel) {
		this.userLevel = userLevel;
	}

	public Integer getResetID() {
		return resetID;
	}

	public void setResetID(Integer resetID) {
		this.resetID = resetID;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	/**
	 * For the bulk configuration of user properties
	 * @param _field
	 */
	public void setField(String _field) {
		this.field = _field;
	}

	/**
	 * For the bulk configuration of user properties
	 * @param _value
	 */
	public void setValue(String _value) {
		this.value = _value;
	}

	public void setPersonID(Integer personID) {
		this.personID = personID;
	}

	public void setFeatureID(String featureID) {
		this.featureID = featureID;
	}

	public void setFeatureValue(boolean featureValue) {
		this.featureValue = featureValue;
	}

	public void setIsUser(boolean isUser) {
		this.isUser = isUser;
	}


}
