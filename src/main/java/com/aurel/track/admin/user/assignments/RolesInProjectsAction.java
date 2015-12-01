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

package com.aurel.track.admin.user.assignments;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.aurel.track.admin.customize.category.CategoryBL;
import com.aurel.track.admin.customize.category.CategoryPickerBL;
import com.aurel.track.admin.customize.category.filter.NotifyFilterFacade;
import com.aurel.track.admin.customize.notify.settings.NotifySettingsJSON;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.beans.TRoleBean;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.TreeNode;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class RolesInProjectsAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware {
	private static final long serialVersionUID = 5153061319998222973L;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private Locale locale;
    private TPersonBean personBean;
	private Integer personID;
	private String unassign;
	private boolean group;
	private Integer projectID;
	private Integer roleID;
	private Integer projectIDOld;
	private Integer roleIDOld;
	private boolean add;
	
	
	@Override
	public void prepare() {
		locale=(Locale) session.get(Constants.LOCALE_KEY);
        personBean = (TPersonBean) session.get(Constants.USER_KEY);
	}
	
	@Override
	public String execute() {
		JSONUtility.encodeJSON(servletResponse,
				RolesInProjectsJSON.encodeRolesInProjectsJSON(RolesInProjectsBL.getRolesInProjects(personID, group, locale)));
		return null;
	}
	
	/**
	 * Loads the group detail for add or edit 
	 * @return
	 */
	public String edit() {
		JSONUtility.encodeJSON(servletResponse, 
				RolesInProjectsBL.editAssignment(projectID, roleID, personBean, locale));
		return null;
	}

	public String save() {
		RolesInProjectsBL.addTacl(personID, projectID, roleID, projectIDOld, roleIDOld, add);
		JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeJSONSuccess());
		return null;
	}
	
	public String delete() {
		RolesInProjectsBL.unassign(personID, unassign);
		JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeJSONSuccess());
		return null;
	}
		
	/**
	 * Restrict the roles after changing the project: the project type specific roles should be returned
	 * @return
	 */
	public String projectChange() {
		List<ILabelBean> roleBeans = RolesInProjectsBL.getRoleBeans(projectID, roleID, locale);
        JSONUtility.encodeJSON(servletResponse,  RolesInProjectsJSON.encodeRoleListJSON(roleBeans, roleID));
        return null;
    }
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public void setPersonID(Integer personID) {
		this.personID = personID;
	}

	public void setGroup(boolean group) {
		this.group = group;
	}

	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	public void setRoleID(Integer roleID) {
		this.roleID = roleID;
	}

	public void setUnassign(String unassign) {
		this.unassign = unassign;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

	public void setProjectIDOld(Integer projectIDOld) {
		this.projectIDOld = projectIDOld;
	}

	public void setRoleIDOld(Integer roleIDOld) {
		this.roleIDOld = roleIDOld;
	}
	
}
