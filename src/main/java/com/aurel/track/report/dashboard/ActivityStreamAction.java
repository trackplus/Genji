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

package com.aurel.track.report.dashboard;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.beans.TDashboardFieldBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.item.history.FlatHistoryBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.screen.dashboard.bl.runtime.DashboardFieldRuntimeBL;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 */
public class ActivityStreamAction extends ActionSupport implements Preparable, SessionAware,ApplicationAware {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(ActivityStreamAction.class);
	private Map<String,Object> session;
	private Map<String, Object> application;
	private Locale locale;
	private TPersonBean person;
	private Integer personID;
	private String iconsPath;
	private Integer projectID;
	private Integer releaseID;
	private Integer dashboardID;

	List<FlatHistoryBean> allActivityStreamItems;
	private boolean tooManyItems=false;
	private String errorMessage=null;

	@Override
	public void prepare() throws Exception {
		locale = (Locale) session.get(Constants.LOCALE_KEY);
		person = (TPersonBean) session.get(Constants.USER_KEY);
		if (person!=null) {
			personID = person.getObjectID();
		}
	}

	public String flatActivity(){
		HttpServletRequest request = org.apache.struts2.ServletActionContext.getRequest();
		String personPath=person.getDesignPath();
		//if(personPath==null){
		personPath="silver";
		//}
		iconsPath= request.getContextPath()+"/design/"+personPath;
		ActivityStream activityStream=new ActivityStream();
		TDashboardFieldBean dashboardItem=(TDashboardFieldBean) DashboardFieldRuntimeBL.getInstance().loadField(dashboardID);
		Map<String,String> parameters=dashboardItem.getParametres();
		if (locale!=null) {
			parameters.put(BasePluginDashboardView.CONFIGURATION_PARAMS.LOCALE_STRING, locale.toString());
		}
		try {
			allActivityStreamItems=activityStream.getActivityItemStreamsHTML(parameters,projectID, releaseID,person,locale);
		} catch (TooManyItemsToLoadException e) {
			LOGGER.info("Number of items to load " + e.getItemCount());
			tooManyItems=true;
			errorMessage= LocalizeUtil.getLocalizedTextFromApplicationResources("cockpit.err.tooManyItems",locale);
		}
		return SUCCESS;
	}

	@Override
	public void setApplication(Map<String, Object> stringObjectMap) {
		this.application=stringObjectMap;
	}

	@Override
	public void setSession(Map<String, Object> stringObjectMap) {
		this.session=stringObjectMap;
	}

	public List<FlatHistoryBean> getAllActivityStreamItems() {
		return allActivityStreamItems;
	}

	public String getIconsPath() {
		return iconsPath;
	}

	public Integer getProjectID() {
		return projectID;
	}

	public Integer getReleaseID() {
		return releaseID;
	}

	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	public void setReleaseID(Integer releaseID) {
		this.releaseID = releaseID;
	}

	public void setDashboardID(Integer dashboardID) {
		this.dashboardID = dashboardID;
	}

	public boolean isTooManyItems() {
		return tooManyItems;
	}

	public void setTooManyItems(boolean tooManyItems) {
		this.tooManyItems = tooManyItems;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
