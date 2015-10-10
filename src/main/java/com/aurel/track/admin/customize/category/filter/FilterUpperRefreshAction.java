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

package com.aurel.track.admin.customize.category.filter;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterSelectsListsLoader;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.project.ProjectPickerBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.GeneralUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * refresh methods for reloading upper parts of the filter
 */
public class FilterUpperRefreshAction extends ActionSupport
	implements Preparable, SessionAware, ServletResponseAware {
	private static final long serialVersionUID = 340L;
	
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private TPersonBean personBean;
	private Locale locale;
	private boolean instant;
	private String upperSelectFields;
	private Integer[] projectIDs;
	private Integer[] itemTypeIDs;
	private boolean showClosedReleases;
	
	
	private Integer fieldID;
	//might contain value only if project is changed by setting the filter parameters
	//it means in the original filter it was selected additionally to the $PARAMETER also other projects 
	
	public void prepare() {
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		locale=(Locale) session.get(Constants.LOCALE_KEY);
		if (projectIDs!=null && projectIDs.length==1 && projectIDs[0]==null) {
			//somehow struts transforms the empty projectIDs parameter to [null]
			projectIDs = null;
		}
		if (itemTypeIDs!=null && itemTypeIDs.length==1 && itemTypeIDs[0]==null) {
			//somehow struts transforms the empty itemTypeIDs parameter to [null]
			itemTypeIDs = null;
		}
	}
	/**
	 * Load the project dependent selects after project change 
	 * @return
	 */
	@Override
	public String execute() {
		FilterUpperTO filterUpperTO = new FilterUpperTO();
		if (upperSelectFields!=null && !"".equals(upperSelectFields)) {
			List<Integer> upperFieldsList = GeneralUtils.createIntegerListFromStringArr(upperSelectFields.split(","));
			filterUpperTO.setUpperFields(upperFieldsList);
		}
		FilterSelectsListsLoader.reloadProjectDependentSelects(filterUpperTO, projectIDs, itemTypeIDs, personBean, locale, !instant, showClosedReleases);
		boolean hasViewWatcherRightInAnyProject =
				FilterSelectsListsLoader.hasViewWatcherRightInAnyProject(personBean);
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		sb.append(FilterJSON.getFilterUpperSelectsJSON(filterUpperTO, hasViewWatcherRightInAnyProject, locale, null, true));
		sb.append("}");
		JSONUtility.encodeJSON(servletResponse, sb.toString());
		return null;
	}
	
	/**
	 * Add a new selection field
	 * @return
	 */
	public String addSelectionField() {
		Object dataSource = null;
		if (SystemFields.INTEGER_PROJECT.equals(fieldID)) {
			dataSource = ProjectPickerBL.getTreeNodesForRead(GeneralUtils.createSetFromIntegerArr(projectIDs),
					true, !instant, personBean, locale);
		} else {
			dataSource = FilterSelectsListsLoader.getSelectFieldDataSource(fieldID , projectIDs, itemTypeIDs, personBean, locale, !instant, showClosedReleases);
		}
		List<Integer> upperSelectFieldIDs = new LinkedList<Integer>();
		if (upperSelectFields!=null && !"".equals(upperSelectFields)) {
			upperSelectFieldIDs = GeneralUtils.createIntegerListFromStringArr(upperSelectFields.split(","));
		}
		boolean hasViewWatcherRightInAnyProject =
				FilterSelectsListsLoader.hasViewWatcherRightInAnyProject(personBean);
		JSONUtility.encodeJSON(servletResponse,
				FilterJSON.getAddSelectFieldJSON(fieldID, dataSource, upperSelectFieldIDs, hasViewWatcherRightInAnyProject, locale));
		return null;
	}
	
	/**
	 * Removes a selection field
	 * @return
	 */
	public String removeSelectionField() {
		List<Integer> upperSelectFieldIDs = new LinkedList<Integer>();
		if (upperSelectFields!=null && !"".equals(upperSelectFields)) {
			upperSelectFieldIDs = GeneralUtils.createIntegerListFromStringArr(upperSelectFields.split(","));
		}
		boolean hasViewWatcherRightInAnyProject =
				FilterSelectsListsLoader.hasViewWatcherRightInAnyProject(personBean);
		JSONUtility.encodeJSON(servletResponse,
				FilterJSON.getRemoveSelectFieldJSON(upperSelectFieldIDs, hasViewWatcherRightInAnyProject, locale));
		return null;
		
	}
	
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
	
	public void setInstant(boolean instant) {
		this.instant = instant;
	}

	public void setProjectIDs(Integer[] projectIDs) {
		this.projectIDs = projectIDs;
	}

	public void setItemTypeIDs(Integer[] itemTypeIDs) {
		this.itemTypeIDs = itemTypeIDs;
	}
	public void setUpperSelectFields(String upperSelectFields) {
		this.upperSelectFields = upperSelectFields;
	}
	public void setShowClosedReleases(boolean showClosedReleases) {
		this.showClosedReleases = showClosedReleases;
	}
	public void setFieldID(Integer fieldID) {
		this.fieldID = fieldID;
	}
	
}
