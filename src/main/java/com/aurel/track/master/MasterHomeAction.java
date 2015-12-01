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
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.util.LabelValueBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;


/**
 */
public class MasterHomeAction extends ActionSupport implements Preparable, SessionAware, ApplicationAware{

	private static final long serialVersionUID = 500L;

	private static final Logger LOGGER = LogManager.getLogger(MasterHomeAction.class);

	//session map
	private Map<String,Object> session;

	private Locale locale;
	private TPersonBean tPerson;
	private Integer personID;

	private boolean hasInitData=true;
	private String initData;
	private String layoutCls="com.trackplus.layout.MasterHomeLayout";
	private String pageTitle="home.title";

	private List<LabelValueBean> dependentModules;


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
		this.session=session;
	}

	/**
	 * prepare the item
	 */
	@Override
	public void prepare() throws Exception {
		locale = (Locale) session.get(Constants.LOCALE_KEY);
		tPerson = (TPersonBean) session.get(Constants.USER_KEY);
		if (tPerson!=null) {
			personID = tPerson.getObjectID();
		}
	}

	/**
	 * Render the first screen: wizard or item depending on action
	 * @return
	 */
//	@Override


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

	public List<LabelValueBean> getDependentModules() {
		return dependentModules;
	}

	public void setDependentModules(List<LabelValueBean> dependentModules) {
		this.dependentModules = dependentModules;
	}
}
