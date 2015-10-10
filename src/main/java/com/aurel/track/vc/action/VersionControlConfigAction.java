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

package com.aurel.track.vc.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.plugin.VersionControlDescriptor;
import com.aurel.track.plugin.VersionControlDescriptor.BrowserDescriptor;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.vc.VersionControlJSON;
import com.aurel.track.vc.VersionControlTO;
import com.aurel.track.vc.bl.VersionControlBL;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class VersionControlConfigAction extends ActionSupport implements Preparable, SessionAware {

	private static final Logger LOGGER = LogManager.getLogger(VersionControlConfigAction.class);
	private static final long serialVersionUID = 340L;
	private Map session;
	private String pluginID;
	private Integer projectID;
	private Map<String,String> vcmap;
	private VersionControlTO vc;
	private Locale locale;
	public void prepare() throws Exception {
		locale = (Locale) session.get(Constants.LOCALE_KEY);
	}
	@Override
	public String execute(){
		List versionControlDescriptors=VersionControlBL.getVersionControlPlugins();
		VersionControlTO vcTO=VersionControlBL.loadVersionControl(projectID);
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		sb.append("data:{");
		JSONUtility.appendJSONValue(sb,"vcPluginList", VersionControlJSON.encodeVersionControlDescriptors(versionControlDescriptors));
		String vcType=null;
		boolean useVC=false;
		if(vcTO!=null){
			vcType=vcTO.getVersionControlType();
			useVC=vcTO.isUseVersionControl();
		}
		JSONUtility.appendStringValue(sb,"vc.versionControlType",vcType);
		JSONUtility.appendBooleanValue(sb,"vc.useVersionControl",useVC);
		JSONUtility.appendIntegerValue(sb,"projectID",projectID);
		sb.append("}}");
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.println(sb);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	public String loadVCPlugin(){
		VersionControlDescriptor descriptor=VersionControlBL.getVersionControlDescriptor(pluginID);
		VersionControlBL.setIntegratedVersionControlBrowserValues(descriptor, projectID);
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		sb.append("data:{");
		List<VersionControlDescriptor.BrowserDescriptor> browsers= descriptor.getBrowsers();
		JSONUtility.appendJSONValue(sb,"browsers",VersionControlJSON.encodeBrowserDescriptor(browsers));
		VersionControlTO vcTO=VersionControlBL.loadVersionControl(projectID);
		if(vcTO.getVersionControlType()!=null&&vcTO.getVersionControlType().equals(pluginID)){
			VersionControlJSON.appendVersionControlTO(sb,vcTO,true);
		}
		sb.append("}}");
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.println(sb);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	public String save(){
		List<LabelValueBean> errors=VersionControlBL.validate(vc,vcmap,locale);
		VersionControlBL.save(projectID,vc,vcmap);
		if(errors!=null && !errors.isEmpty()){
			JSONUtility.encodeJSONErrors(ServletActionContext.getResponse(),errors);
		}else{
			JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		}
		return null;
	}
	public String test(){
		List<LabelValueBean> errors=VersionControlBL.validate(vc,vcmap,locale);
		if(errors!=null && !errors.isEmpty()){
			JSONUtility.encodeJSONErrors(ServletActionContext.getResponse(),errors);
		}else{
			JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		}
		return null;
	}

	/*public String getConfigTemplate(){
		VersionControlDescriptor descriptor=VersionControlBL.getVersionControlDescriptor(pluginID);
		Object o=session.get("projectForm");
		if(o!=null){
			vcmap=((ProjectForm)o).getVcmap();
		}
		return "configTemplate";
	}*/
	public String getVcBrowser(){
		List<BrowserDescriptor> options=VersionControlBL.getBrowserList(pluginID);
		return "browserList";
	}
	public Map getSession() {
		return session;
	}

	public void setSession(Map session) {
		this.session = session;
	}

	public String getPluginID() {
		return pluginID;
	}

	public void setPluginID(String pluginID) {
		this.pluginID = pluginID;
	}

	public Map<String,String> getVcmap() {
		return vcmap;
	}

	public void setVcmap(Map<String,String> vcmap) {
		this.vcmap = vcmap;
	}

	public Integer getProjectID() {
		return projectID;
	}

	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	public VersionControlTO getVc() {
		return vc;
	}

	public void setVc(VersionControlTO vc) {
		this.vc = vc;
	}
}
