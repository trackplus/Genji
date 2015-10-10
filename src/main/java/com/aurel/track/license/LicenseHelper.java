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

package com.aurel.track.license;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.server.siteConfig.LicenseTO;
import com.aurel.track.admin.server.siteConfig.SiteConfigBL;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.json.ControlError;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.prop.ApplicationBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.trackplus.license.LicenseManager;

public class LicenseHelper  extends ActionSupport implements Preparable, SessionAware, ServletResponseAware, ApplicationAware{


	private Map<String,Object> session;
	private HttpServletResponse servletResponse;
	private Map application;
	private Locale locale;
	private TSiteBean siteApp;
	private ApplicationBean appBean;

	private static final Logger LOGGER = LogManager.getLogger(LicenseHelper.class);

	private Integer gender;
	private String firstName;
	private String lastName;
	private String phone;
	private String mail;
	private String company;

	private String last_name;


	public String getLast_name() {
		return last_name;
	}


	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}


	public void prepare() throws Exception {
		locale = (Locale) session.get(Constants.LOCALE_KEY);
		appBean = ApplicationBean.getApplicationBean();
		siteApp = (TSiteBean) application.get("SITECONFIG");
	}


	@Override
	public void setServletResponse(HttpServletResponse arg0) {
		this.servletResponse = arg0;

	}

	@Override
	public void setSession(Map<String, Object> arg0) {
		this.session = arg0;
	}

	
	public String check() {
		System.out.println(last_name);
		System.out.println(company);
		return null;
	}


	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}


	@Override
	public void setApplication(Map<String, Object> arg0) {
		this.application = arg0;
	}



}
