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

package com.aurel.track.admin.user.filtersInMenu;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.aurel.track.admin.customize.category.CategoryBL;
import com.aurel.track.admin.customize.category.CategoryPickerBL;
import com.aurel.track.admin.customize.category.filter.FilterBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.util.TreeNode;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.itemNavigator.filterInMenu.FilterInMenuJSON;
import com.aurel.track.itemNavigator.filterInMenu.FilterInMenuTO;
import com.aurel.track.json.JSONUtility;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class FiltersInMenuAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware {
	private static final long serialVersionUID = 5153061319998222973L;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private Locale locale;
    private TPersonBean personBean;
    private String filterIDs;
	private String personIDs;
	private String unassign;	
	
	public void prepare() {
		locale=(Locale) session.get(Constants.LOCALE_KEY);
        personBean = (TPersonBean) session.get(Constants.USER_KEY);
	}
	
	@Override
	public String execute() {
		JSONUtility.encodeJSON(servletResponse,
				FiltersInMenuJSON.encodeFiltersForPersonsJSON(FiltersInMenuBL.loadFilterAssignmnetData(personIDs, locale)));
		return null;
	}

    /**
     * Json data for edit form in this case containing only the filter tree
     * @return
     */
	public String edit() {
        List<TreeNode> filterTree = CategoryPickerBL.getPickerNodesEager(personBean, true, true, null,
            true, null, null, locale, CategoryBL.CATEGORY_TYPE.ISSUE_FILTER_CATEGORY);
        JSONUtility.encodeJSON(servletResponse, FiltersInMenuJSON.encodeFilterTreeJSON(filterTree));
        return null;
	}

    /**
     * Saves the selected filters in users' menu
     * @return
     */
	public String save() {
		boolean currentUserAffected = FiltersInMenuBL.addFiltersInMenu(personIDs, filterIDs, personBean);
        if (currentUserAffected) {
            List<FilterInMenuTO> myFilters= FilterBL.loadMyMenuFiltersWithTooltip(personBean, locale);
            session.put(FilterBL.MY_MENU_FILTERS_JSON, FilterInMenuJSON.encodeFiltersInMenu(myFilters));
        }
		JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeJSONSuccess());
		return null;
	}

    /**
     * Deletes the selected filters from users' menu
     * @return
     */
	public String delete() {
        boolean currentUserAffected = FiltersInMenuBL.unassign(unassign, personIDs, personBean);
        if (currentUserAffected) {
            List<FilterInMenuTO> myFilters= FilterBL.loadMyMenuFiltersWithTooltip(personBean, locale);
            session.put(FilterBL.MY_MENU_FILTERS_JSON, FilterInMenuJSON.encodeFiltersInMenu(myFilters));
        }
		JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeJSONSuccess());
		return null;
	}
		
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public void setPersonIDs(String personIDs) {
		this.personIDs = personIDs;
	}

	public void setFilterIDs(String filterIDs) {
		this.filterIDs = filterIDs;
	}

	public void setUnassign(String unassign) {
		this.unassign = unassign;
	}
	
}
