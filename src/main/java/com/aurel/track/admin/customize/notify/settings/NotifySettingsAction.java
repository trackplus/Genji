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


package com.aurel.track.admin.customize.notify.settings;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.CategoryBL;
import com.aurel.track.admin.customize.category.CategoryPickerBL;
import com.aurel.track.admin.customize.category.filter.NotifyFilterFacade;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.TreeNode;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class NotifySettingsAction extends ActionSupport 
		implements Preparable, SessionAware, ServletResponseAware {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private Locale locale;
	private TPersonBean personBean;
	private Integer personID;
	private Integer notifySettingsID;
	//set by automail assignment in project configuration (only the actually configured project is available)
	private Integer exclusiveProjectID;
	private Integer trigger;
	private Integer filter;
	//the selected project of the edited record 
	private Integer project;
	//whether the default or the own (and default) settings are processed 
	private boolean defaultSettings = false;
	
	/**
	 * Prepares the listBean using the listID parameter
	 */
	@Override
	public void prepare() throws Exception {
		locale = (Locale)session.get(Constants.LOCALE_KEY);
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		personID = personBean.getObjectID();
	}
	
	/**
	 * Loads the notification settings list
	 * @return
	 */
	public String loadList() {
		JSONUtility.encodeJSON(servletResponse, 
				NotifySettingsJSON.createNotifySettingsListJSON(personBean, defaultSettings, exclusiveProjectID, locale));
		return null;
	}
	
	/**
	 * Adds/edits/overwrites a notification setting 
	 * @return
	 */
	public String edit() {
		JSONUtility.encodeJSON(servletResponse, 
				NotifySettingsBL.createNotifySettingsEditJSON(notifySettingsID, exclusiveProjectID, personBean, defaultSettings, locale));
		return null;
	}

	/**
	 * Restrict the notification filters after changing the project: the project specific filters should be refreshed
	 * @return
	 */
	public String projectChange() {
        List<TreeNode> categoryTree = CategoryPickerBL.getPickerNodesEager(
                personBean, defaultSettings, false, null, false, project, null, locale, CategoryBL.CATEGORY_TYPE.NOTIFICATION_FILTER_CATEGORY);
        if (filter!=null) {
            TQueryRepositoryBean queryRepositoryBean = (TQueryRepositoryBean)NotifyFilterFacade.getInstance().getByKey(filter);
            if (queryRepositoryBean.getRepositoryType().intValue()==CategoryBL.REPOSITORY_TYPE.PROJECT) {
                //it was a project specific filter: since project has changed, the filter is not valid any more, set it to null
                filter = null;
            }
        }
        JSONUtility.encodeJSON(servletResponse,  NotifySettingsJSON.createNotifySettingsFilterTreeJSON(categoryTree, filter));
        return null;
    }

	/**
	 * Saves the new/modified/overwritten notification setting
	 * @return
	 */
	public String save() {
		notifySettingsID =  NotifySettingsBL.save(notifySettingsID, defaultSettings, project, trigger, filter, personID);
		JSONUtility.encodeJSON(servletResponse,
				JSONUtility.encodeJSONSuccessAndID(notifySettingsID));
		return null;
	}

	/**
	 * Deletes a notification setting
	 * @return
	 */
	public String delete() {
		String jsonResponse;
		if (NotifySettingsBL.deleteNotifySettingsAllowed(notifySettingsID, personBean, defaultSettings)) {
			NotifySettingsBL.deleteNotifySettingsByPrimaryKey(notifySettingsID);
			jsonResponse = JSONUtility.encodeJSONSuccess();
		} else {
			String errorMessage = getText("admin.customize.automail.assignments.err.deleteNotAllowed");
			jsonResponse = JSONUtility.encodeJSONFailure(errorMessage);
		}
		JSONUtility.encodeJSON(servletResponse, jsonResponse);
		return null;
	}
	
	public boolean isDefaultSettings() {
		return defaultSettings;
	}
	
	public void setDefaultSettings(boolean defaultSettings) {
		this.defaultSettings = defaultSettings;
	}

	/**
	 * @param notifySettingsID the notifySettingsID to set
	 */
	public void setNotifySettingsID(Integer notifySettingsID) {
		this.notifySettingsID = notifySettingsID;
	}

	public void setExclusiveProjectID(Integer exclusiveProjectID) {
		this.exclusiveProjectID = exclusiveProjectID;
	}

	public void setTrigger(Integer trigger) {
		this.trigger = trigger;
	}

	public void setFilter(Integer filter) {
		this.filter = filter;
	}

	public void setProject(Integer project) {
		this.project = project;
	}

	/**
	 * @param session the session to set
	 */
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getProjectLabel() {
		return FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_PROJECT, locale);
	}
	
	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
}
