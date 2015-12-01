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


package com.aurel.track.master;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.plugin.ModuleDescriptor;
import com.aurel.track.plugin.PluginManager;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 */
public class ExternalAction extends ActionSupport implements Preparable, SessionAware, ApplicationAware {
	private static final Logger LOGGER = LogManager.getLogger(MasterHomeAction.class);

	// session map
	private Map<String, Object> session;

	private Locale locale;
	private TPersonBean tPerson;
	private Integer personID;

	private boolean hasInitData = true;
	private String initData;
	private String layoutCls = "com.trackplus.layout.ExternalActionLayout";
	private String pageTitle = "home.title";
	private String moduleID;
	private String vcRevisionNo;
	private String vcRepName;
	private String vcPath;

	/**
	 * prepare the item
	 */
	@Override
	public void prepare() throws Exception {
		locale = (Locale) session.get(Constants.LOCALE_KEY);
		tPerson = (TPersonBean) session.get(Constants.USER_KEY);
		if (tPerson != null) {
			personID = tPerson.getObjectID();
		}
	}

	/**
	 * Render the first screen: wizard or item depending on action
	 *
	 * @return
	 */
	@Override
	public String execute() {
		List modules = PluginManager.getInstance().getModuleDescriptors();
		ModuleDescriptor moduleDescriptor = null;
		for (int i = 0; i < modules.size(); i++) {
			ModuleDescriptor md = (ModuleDescriptor) modules.get(i);
			if (md.getId().equals(moduleID)) {
				moduleDescriptor = md;
			}
		}
		pageTitle = moduleDescriptor.getName();
		moduleDescriptor.setUrl(moduleDescriptor.getCleanedUrl());
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		JSONUtility.appendJSONValue(sb, "module", MasterHomeJSON.encodeModule(moduleDescriptor, false, tPerson));
		JSONUtility.appendBooleanValue(sb, "externalAction", true, true);
		sb.append("}");
		initData = sb.toString();
		return SUCCESS;
	}

	public String getWebSVNModuleJSONForVersionControlActivity() {
		ModuleDescriptor moduleDescriptor = PluginManager.getInstance().getModuleByID(ModuleDescriptor.WEB_SVN_MODULE);
		if (vcRepName != null && vcRevisionNo != null) {
			pageTitle = moduleDescriptor.getName();
			moduleDescriptor.setUrl(moduleDescriptor.getCleanedUrl());
			StringBuilder sb = new StringBuilder();
			sb.append("{");
			JSONUtility.appendJSONValue(sb, "module", MasterHomeJSON.encodeWebSVNModuleAfterOperation(moduleDescriptor, vcRepName, vcRevisionNo, null));
			JSONUtility.appendBooleanValue(sb, "externalAction", true, true);
			sb.append("}");
			initData = sb.toString();
			return SUCCESS;
		}
		return null;
	}

	public String getViewGitModuleJSONForVersionControlActivity() {
		ModuleDescriptor moduleDescriptor = PluginManager.getInstance().getModuleByID(ModuleDescriptor.VIEWGIT);
		if (vcRevisionNo != null) {
			pageTitle = moduleDescriptor.getName();
			moduleDescriptor.setUrl(moduleDescriptor.getCleanedUrl());
			StringBuilder sb = new StringBuilder();
			sb.append("{");
			JSONUtility.appendJSONValue(sb, "module", MasterHomeJSON.encodeViewGitModuleAfterOperation(moduleDescriptor, vcRepName, vcRevisionNo, null));
			JSONUtility.appendBooleanValue(sb, "externalAction", true, true);
			sb.append("}");
			initData = sb.toString();
			return SUCCESS;
		}
		return null;
	}

	public String getTrackProjectsForWebSVN() {
		List<TProjectBean> projectsBeanList = ProjectBL.loadProjectsFlatByRight(personID,
				new int[] { AccessBeans.AccessFlagIndexes.MODIFYANYTASK, AccessBeans.AccessFlagIndexes.CREATETASK }, true);
		JSONUtility.encodeJSON(ServletActionContext.getResponse(), ExternalActionJSON.encodeProjectsForWebSVN(projectsBeanList, locale, "svn"));
		return null;
	}

	public String getTrackProjectsForViewGit() {
		List<TProjectBean> projectsBeanList = ProjectBL.loadProjectsFlatByRight(personID,
				new int[] { AccessBeans.AccessFlagIndexes.MODIFYANYTASK, AccessBeans.AccessFlagIndexes.CREATETASK }, true);
		JSONUtility.encodeJSON(ServletActionContext.getResponse(), ExternalActionJSON.encodeProjectsForWebSVN(projectsBeanList, locale, "git"));
		return null;
	}

	public boolean isHasInitData() {
		return hasInitData;
	}

	public String getInitData() {
		return initData;
	}

	public String getLayoutCls() {
		return layoutCls;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public String getModuleID() {
		return moduleID;
	}

	public void setModuleID(String moduleID) {
		this.moduleID = moduleID;
	}

	public Map getApplication() {
		return application;
	}

	@Override
	public void setApplication(Map application) {
		this.application = application;
	}

	private Map application;

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getVcRevisionNo() {
		return vcRevisionNo;
	}

	public void setVcRevisionNo(String vcRevisionNo) {
		this.vcRevisionNo = vcRevisionNo;
	}

	public String getVcRepName() {
		return vcRepName;
	}

	public void setVcRepName(String vcRepName) {
		this.vcRepName = vcRepName;
	}

	public String getVcPath() {
		return vcPath;
	}

	public void setVcPath(String vcPath) {
		this.vcPath = vcPath;
	}
}
