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


package com.aurel.track.admin.customize.notify.trigger;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TNotifyTriggerBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class NotifyTriggerAction extends ActionSupport 
		implements Preparable, SessionAware, ServletResponseAware {
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private TPersonBean personBean;
	private Integer personID;
	private Locale locale;
	//the ID of the selected trigger by edit and copy 
	private Integer notifyTriggerID;
	private String label;	
	private String records;
	private boolean copy = false;
	private Integer replacementID;
	
	private Map<String, String> localeMap;
	
	//whether the default or own (and default) settings are processed
	//depending on that the trigger will be saved as system or private trigger
	private boolean defaultSettings = false;
	
	/**
	 * Prepares the listBean using the listID parameter
	 */
	@Override
	public void prepare() throws Exception {
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		personID = personBean.getObjectID();
		locale = (Locale)session.get(Constants.LOCALE_KEY);
	}

	/**
	 * Loads the trigger list
	 * @return
	 */
	public String loadList() {
		JSONUtility.encodeJSON(servletResponse,
			NotifyTriggerJSON.createTriggerListJSON(
					NotifyTriggerBL.loadNotifyTriggerBeans(defaultSettings, personBean, locale)));
		return null;
	}
	
	/**
	 * Edit/add/copy of a trigger
	 * @return
	 */
	public String edit() {
		List<NotifyTriggerFieldTO> triggerFieldGridRows = NotifyTriggerBL.loadAllNotifyFields(notifyTriggerID, locale);
		boolean disabled = false; 
		if (notifyTriggerID != null) {
			TNotifyTriggerBean notifyTriggerBean = NotifyTriggerBL.loadNotifyTriggerBean(notifyTriggerID);
			if (notifyTriggerBean!=null) {
				label = notifyTriggerBean.getLabel();
				if (copy) {
					List<String> args = new LinkedList<String>();
					args.add(label);
					label = getText("common.lbl.copy", args);
				}
				if (!personBean.isSys() && !personID.equals(notifyTriggerBean.getPerson())) {
					//if no system admin and edit (not add) mode and the person differs disable the controls
					disabled = true;
				}
			}
		}
		JSONUtility.encodeJSON(servletResponse, 
				NotifyTriggerJSON.createEditTriggerJSON(triggerFieldGridRows, label, disabled));
		return null;
	}

	/**
	 * Saves the edited/added/copied trigger
	 * @return
	 */
	public String save() {
		notifyTriggerID = NotifyTriggerBL.saveNotifyTrigger(notifyTriggerID, label, copy, personBean, defaultSettings, records);
		JSONUtility.encodeJSON(servletResponse,
				JSONUtility.encodeJSONSuccessAndID(notifyTriggerID));
		return null;
	}
		
	/**
	 * Deletes the trigger
	 * @return
	 */
	public String delete() {
		String jsonResponse;
		String errorMessage=null;
		if (NotifyTriggerBL.isAllowedToDelete(notifyTriggerID, personBean)) {
			if (NotifyTriggerBL.isDeletable(notifyTriggerID)) {
				NotifyTriggerBL.deleteNotifyTrigger(notifyTriggerID); 
				jsonResponse = JSONUtility.encodeJSONSuccess();
			} else {
				//replacement needed
				jsonResponse = JSONUtility.encodeJSONFailure(JSONUtility.DELETE_ERROR_CODES.NEED_REPLACE);
			}
		} else {
			errorMessage = getText("admin.customize.automail.trigger.err.deleteNotAllowed");
			jsonResponse = JSONUtility.encodeJSONFailure(errorMessage);
		}
		JSONUtility.encodeJSON(servletResponse, jsonResponse);
		return null;
	}
		
	public String renderReplace() {
		String jsonResponse = NotifyTriggerBL.prepareReplacement(notifyTriggerID, defaultSettings, personID, null, locale);
		JSONUtility.encodeJSON(servletResponse, jsonResponse);
		return null;
	}
	
	/**
	 * Replaces and deletes the trigger
	 * @return
	 */
	public String replaceAndDelete() {
		String jsonResponse = null;
			if (replacementID == null) {
				String errorMessage = getText(NotifyTriggerBL.LOCALE_KEYS.PROMPT_REPLACEMENT_REQUIRED.getKey(), 
						new String[] {getText(NotifyTriggerBL.LOCALE_KEYS.PROMPT.getKey())});
				jsonResponse = NotifyTriggerBL.prepareReplacement(notifyTriggerID, defaultSettings, personID, errorMessage, locale); 
			} else {
				NotifyTriggerBL.replaceAndDeleteTreeQuery(notifyTriggerID, replacementID);
				jsonResponse = JSONUtility.encodeJSONSuccess();
			}
		JSONUtility.encodeJSON(servletResponse, jsonResponse);
		return null;
	}

	
	/**
	 * @return the copy
	 */
	public boolean isCopy() {
		return copy;
	}

	/**
	 * Set the copy flag by submitting the hidden copy field
	 * @param copy the copy to set
	 */
	public void setCopy(boolean copy) {
		this.copy = copy;
	}
	
			
	/**
	 * @return the notifyTriggerID
	 */
	public Integer getNotifyTriggerID() {
		return notifyTriggerID;
	}	
	
	/**
	 * @param notifyTriggerID the notifyTriggerID to set
	 */
	public void setNotifyTriggerID(Integer notifyTriggerID) {
		this.notifyTriggerID = notifyTriggerID;
	}

	/**
	 * @param session the session to set
	 */
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

	public Map<String, String> getLocaleMap() {
		return localeMap;
	}

	public void setRecords(String records) {
		this.records = records;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setDefaultSettings(boolean defaultSettings) {
		this.defaultSettings = defaultSettings;
	}	
}
