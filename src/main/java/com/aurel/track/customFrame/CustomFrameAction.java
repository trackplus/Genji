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

package com.aurel.track.customFrame;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.SessionAware;

import java.util.*;

/**
 */
public class CustomFrameAction extends ActionSupport implements Preparable, SessionAware,ApplicationAware/*, RequestAware */{
	private static final Logger LOGGER = LogManager.getLogger(CustomFrameAction.class);
	private Map<String,Object> session;
	private TPersonBean personBean;
	private Locale locale;
	private Map application;
	private String pluginID;
	private String url;
	private String title;

	private boolean hasInitData=true;
	private String initData;
	private String layoutCls="com.trackplus.layout.CustomFrameLayout";
	private String pageTitle="Custom frame";

	public boolean isHasInitData() {
		return hasInitData;
	}

	public void setHasInitData(boolean hasInitData) {
		this.hasInitData = hasInitData;
	}
	public void setSession(Map<String, Object> session) {
		this.session=session;
	}

	public void prepare() throws Exception {
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		locale=(Locale) session.get(Constants.LOCALE_KEY);
	}

	@Override
	public String execute(){
		LOGGER.debug("execute: custom action");
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendStringValue(sb,"title",title);
		JSONUtility.appendStringValue(sb,"url",url);
		JSONUtility.appendStringValue(sb,"pluginID",pluginID,true);
		sb.append("}");

		initData=sb.toString();
		return SUCCESS;
	}
	public Map getApplication() {
		return application;
	}

	public void setApplication(Map application) {
		this.application = application;
	}

	public String getInitData() {
		return initData;
	}

	public void setInitData(String initData) {
		this.initData = initData;
	}

	public String getPluginID() {
		return pluginID;
	}

	public void setPluginID(String pluginID) {
		this.pluginID = pluginID;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLayoutCls() {
		return layoutCls;
	}

	public void setLayoutCls(String layoutCls) {
		this.layoutCls = layoutCls;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
}


