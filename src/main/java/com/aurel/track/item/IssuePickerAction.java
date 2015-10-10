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


package com.aurel.track.item;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.prop.ApplicationBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 *
 */
public class IssuePickerAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware, ApplicationAware {
    private Map<String,Object> session;
    private HttpServletResponse servletResponse;
    private Locale locale;
    private TPersonBean personBean;
	private Integer workItemID = null;
	private boolean parent;
	private Integer projectID= null;
	private Integer queryID= null;
	private String searchIssueKey = null;
	private boolean includeClosed;
	
	public static final long serialVersionUID = 400L;


	private Map application;

    public void prepare() {
        locale=(Locale) session.get(Constants.LOCALE_KEY);
        personBean = (TPersonBean) session.get(Constants.USER_KEY);
    }



	@Override
	public String execute() throws Exception {
		return search();
	}

	public String search(){
		TPersonBean personBean=(TPersonBean) session.get(Constants.USER_KEY);
		Locale locale=(Locale) session.get(Constants.LOCALE_KEY);
		boolean  useProjectSpecificID=false;
		Map<Integer,TProjectBean> projectsMap=null;
		ApplicationBean appBean =(ApplicationBean)application.get(Constants.APPLICATION_BEAN);
		if(appBean.getSiteBean().getProjectSpecificIDsOn()!=null){
			useProjectSpecificID=appBean.getSiteBean().getProjectSpecificIDsOn().booleanValue();
		}
		List<TWorkItemBean> issues=IssuePickerBL.search(personBean,locale,workItemID,parent,projectID,queryID,searchIssueKey,useProjectSpecificID, includeClosed);

		if(useProjectSpecificID){
			//TODO refactor this code with drop-down cache
			projectsMap=ProjectBL.loadByWorkItemBeansAsMap(issues);
		}
		JSONUtility.encodeJSON(servletResponse, IssuePickerJSON.encodeIssues(issues, useProjectSpecificID,projectsMap));
		return null;

	}

	public void setWorkItemID(Integer workItemID) {
		this.workItemID = workItemID;
	}

	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	public void setSearchIssueKey(String searchIssueKey) {
		this.searchIssueKey = searchIssueKey;
	}

	public void setIncludeClosed(boolean includeClosed) {
		this.includeClosed = includeClosed;
	}

	public void setQueryID(Integer queryID) {
		this.queryID = queryID;
	}
	public Map getApplication() {
		return application;
	}

	public void setApplication(Map application) {
		this.application = application;
	}

    public void setSession(Map<String, Object> stringObjectMap) {
        this.session=stringObjectMap;
    }

    public void setServletResponse(HttpServletResponse servletResponse) {
        this.servletResponse = servletResponse;
    }

	public boolean isParent() {
		return parent;
	}

	public void setParent(boolean parent) {
		this.parent = parent;
	}
}
