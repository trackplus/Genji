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


package com.aurel.track.admin.project.release;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.GeneralUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * This class is used for both release picker (single select), release tree (multiple select) and project/release tree or picker
 * release picker or release tree might contain projects but only to hierarchize the releases, the projects are not selectable
 * project/release tree or picker contains projects and releases, both projects and releases are selectable 
 * @author Tamas Ruff
 *
 */
public class ReleasePickerAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private TPersonBean personBean;
	private Locale locale;
	private HttpServletResponse servletResponse;
	private boolean closedFlag = false;
	private String projectIDs;
	
	/**
	 * Whether or not the checked checkbox should appear near the node
	 * Should be used in release- or projectRelease- tree (when multiple select possible) not release- or projectRelease picker
	 * if useChecked is true (multiple select) then useSelectable does not have any importance because then the selectable option
	 * is controlled by whether the checkbox is rendered or not i.e. checked flag is null or not null
	 */
	private boolean useChecked;
	
	/**
	 * Whether the project is selectable:
	 * 	if true project/release picker/tree
	 * 	if false only release picker/tree with projects only to only build up the release hierarchy
	 */
	private boolean projectIsSelectable = true;
	
	@Override
	public void prepare() throws Exception {
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		locale = (Locale)session.get(Constants.LOCALE_KEY);
	}

    /**
     * Gets all projects and releases based on used base projects (project specific read right or RACI rights)
     * @return
     */
    @Override
    public String execute() {
    	List<Integer> projectIDList = null;
    	if (projectIDs!=null) {
    		String[] selectedProjectIDArr = projectIDs.split(",");
			projectIDList = GeneralUtils.createIntegerListFromStringArr(selectedProjectIDArr);
    	}
        String projectReleaseTree =  ReleasePickerBL.getTreeJSON(
        		projectIDList, null, useChecked, projectIsSelectable,
                closedFlag, true, true, true,
                false, null, personBean, locale);
        JSONUtility.encodeJSON(servletResponse, projectReleaseTree);
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

    public void setUseChecked(boolean useChecked) {
        this.useChecked = useChecked;
    }

	public void setProjectIsSelectable(boolean projectIsSelectable) {
		this.projectIsSelectable = projectIsSelectable;
	}

	public void setClosedFlag(boolean closedFlag) {
		this.closedFlag = closedFlag;
	}

	public void setProjectIDs(String projectIDs) {
		this.projectIDs = projectIDs;
	}

	
}
