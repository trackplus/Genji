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


package com.aurel.track.admin.customize.treeConfig.field;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TGeneralSettingsBean;
import com.aurel.track.beans.TOptionSettingsBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TTextBoxSettingsBean;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.Validateable;
import com.opensymphony.xwork2.ValidationAware;

/**
 * Action class for field config detail operations
 * @author Tamas Ruff
 *
 */
public class FieldConfigDetailAction extends ActionSupport 
		implements Preparable, Validateable, ValidationAware, ServletResponseAware, SessionAware {
	
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private TPersonBean person;
	private Locale locale;
	private List<TOptionSettingsBean> optionSettingsList = new ArrayList<TOptionSettingsBean>();
	private List<TTextBoxSettingsBean> textBoxSettingsList = new ArrayList<TTextBoxSettingsBean>();
	private List<TGeneralSettingsBean> generalSettingsList = new ArrayList<TGeneralSettingsBean>();
	//workaround for multiple select generalSettings: 
	//because struts accepts just specifying a list of beans (not a list of bean arrays)
	//the multipleIntegerList is used .
	//This String (comma separated integers) will be transformed in the background to a list of TGeneralSettingsBean
	private List<String> multipleIntegerList = new ArrayList<String>();
	private boolean add;
	private String node;
	private String name;
	private String fieldType;
	private boolean filterField;
	private boolean deprecated;	
	private String description;
	private String label;
	private String tooltip;
	private boolean required;
	private boolean history;
	private boolean deleteConfirmed;
	private boolean renameConfirmed;
		
	/**
	 * Load the settings if nodeId specified
	 */
	@Override
	public void prepare() {
		person = (TPersonBean) session.get(Constants.USER_KEY);
		locale = (Locale)session.get(Constants.LOCALE_KEY);
	}
	
	
	public String load() {
	 	FieldDesignBL.loadFieldConfigDetail(node, name, fieldType, filterField, deprecated, description, 
				label, tooltip, required, history, 
				add, person, locale, servletResponse);
	 	return null;
	}
	
	public String executeCommand() {
		FieldDesignBL.executeCommand(node, servletResponse);
		return null;
	}
	
	/**
	 * Saves the new/modified field, configs and settings
	 * @return
	 */
	public String save() {
		Map<Integer, Object> gatheredSettings = FieldDesignBL.gatherSettings(
				optionSettingsList, textBoxSettingsList, generalSettingsList, multipleIntegerList);
		FieldDesignBL.saveFieldConfigDetail(node, name, renameConfirmed, fieldType, filterField, deprecated, description, 
				label, tooltip, required, history, gatheredSettings, add, person, locale, servletResponse);		
		return null;
	}

	/**
	 * Deletes a field
	 * @return
	 */
	public String delete() {
		FieldDesignBL.deleteField(node, deleteConfirmed, person, locale, servletResponse);
		return null;
	}
	
	/**
	 * @return the node
	 */
	public String getNode() {
		return node;
	}
	/**
	 * @param node the node to set
	 */
	public void setNode(String node) {
		this.node = node;
	}	
	
	/**
	 * @return the generalSettingsList
	 */
	public List<TGeneralSettingsBean> getGeneralSettingsList() {
		return generalSettingsList;
	}

	/**
	 * @param generalSettingsList the generalSettingsList to set
	 */
	public void setGeneralSettingsList(List<TGeneralSettingsBean> generalSettingsList) {
		this.generalSettingsList = generalSettingsList;
	}	

	/**
	 * @return the multipleIntegerList
	 */
	public List<String> getMultipleIntegerList() {
		return multipleIntegerList;
	}

	/**
	 * @param multipleIntegerList the multipleIntegerList to set
	 */
	public void setMultipleIntegerList(List<String> multipleIntegerList) {
		this.multipleIntegerList = multipleIntegerList;
	}

	/**
	 * @return the optionSettingsList
	 */
	public List<TOptionSettingsBean> getOptionSettingsList() {
		return optionSettingsList;
	}

	/**
	 * @param optionSettingsList the optionSettingsList to set
	 */
	public void setOptionSettingsList(List<TOptionSettingsBean> optionSettingsList) {
		this.optionSettingsList = optionSettingsList;
	}

	/**
	 * @return the textBoxSettingsList
	 */
	public List<TTextBoxSettingsBean> getTextBoxSettingsList() {
		return textBoxSettingsList;
	}

	/**
	 * @param textBoxSettingsList the textBoxSettingsList to set
	 */
	public void setTextBoxSettingsList(List<TTextBoxSettingsBean> textBoxSettingsList) {
		this.textBoxSettingsList = textBoxSettingsList;
	}

	public boolean isAdd() {
		return add;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}
		   
	/**
	 * @param session the session to set
	 */
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	public void setDeprecated(boolean deprecated) {
		this.deprecated = deprecated;
	}

	public void setFilterField(boolean filterField) {
		this.filterField = filterField;
	}


	public void setRequired(boolean required) {
		this.required = required;
	}

	public void setHistory(boolean history) {
		this.history = history;
	}

	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public void setDeleteConfirmed(boolean deleteConfirmed) {
		this.deleteConfirmed = deleteConfirmed;
	}

	public void setRenameConfirmed(boolean renameConfirmed) {
		this.renameConfirmed = renameConfirmed;
	}

}
