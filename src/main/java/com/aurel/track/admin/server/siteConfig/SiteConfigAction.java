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


package com.aurel.track.admin.server.siteConfig;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;


import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.json.ControlError;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.util.Support;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;


/** 
* Implementation of <strong>SiteConfigAction</strong> that processes the site
* configuration requests 
*/
public class SiteConfigAction extends ActionSupport implements Preparable, SessionAware, ApplicationAware, ServletResponseAware {

	private static final long serialVersionUID = 400L;
	private static final Logger LOGGER = LogManager.getLogger(SiteConfigAction.class);
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private Locale locale; 
	private Map application;
	private TSiteBean siteApp;
	private ApplicationBean appBean;
	private TPersonBean user;
	

	// The transfer objects to and from the user interface
	private LicenseTO licenseTo;
	private OutgoingEmailTO outgoingEmailTo;


	private FullTextSearchTO fullTextSearchTo;
	private LdapTO ldapTo;
	private OtherSiteConfigTO otherSiteConfigTo;
	private String emailTestTo;


	private boolean hasInitData=true;
	private String initData;
	private String layoutCls="com.trackplus.layout.AdminLayout";
	private String pageTitle="menu.admin";


	/**
	 * This routine is called first by Struts2 on any action.
	 */
	public void prepare() throws Exception {
		appBean = ApplicationBean.getApplicationBean();
		siteApp = (TSiteBean) application.get("SITECONFIG");
		user = (TPersonBean) session.get(Constants.USER_KEY);
		locale = (Locale) session.get(Constants.LOCALE_KEY);
	}
	
	/**
	 * This routine is being called after prepare() in case of GET, 
	 * e.g. before the load action
	 */
	@Override
	public String execute() throws Exception {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendStringValue(sb,"sectionSelected","serverSection");
		JSONUtility.appendStringValue(sb,"selectedNodeID","serverConfiguration",true);
		sb.append("}");
		initData=sb.toString();
		return INPUT;
	}
	
	/**
	 * This is the <code>load</code> action. It prints a suitable
	 * JSON object directly into the response stream. The site and application
	 * have already been loaded by the <code>prepare</code> method.
	 * 
	 * @return usually nothing, response is directly printed into the response stream. Can also
	 * throw back to logon page in case of insufficient authorization.
	 */
	public String load () {
		LOGGER.debug("Processing load");
		JSONUtility.encodeJSON(servletResponse, SiteConfigJSON.buildJSON(appBean, siteApp, user, locale));
		return null;
	}


	/**
	 * This saves the server configuration coming from the user interface to
	 * the database. Some validation is performed when it cannot be directly
	 * done at the user interface.
	 * 
	 * @return usually nothing, output is directly printed into response stream
	 */
	public String save (){
		LOGGER.debug("Processing save()");		
		if (appBean == null) {
			LOGGER.error("FATAL: No applicationBean found");
			return "done";
		}			
		List<ControlError> errors = SiteConfigBL.save(siteApp, licenseTo,
				outgoingEmailTo,
				fullTextSearchTo, ldapTo, otherSiteConfigTo,
				appBean, application, locale);		
		if (errors.isEmpty()) {
			Support support = new Support();
			support.setURIs(ServletActionContext.getRequest());
			application.remove("FirstTime");

			JSONUtility.encodeJSON(servletResponse, SiteConfigJSON.buildLicenseJSON(appBean, siteApp, locale)); 
		} else {
			LOGGER.debug("We got validation errors when saving the server configuration");
			JSONUtility.encodeJSON(servletResponse, SiteConfigJSON.encodeJSONSiteConfigErrorList(errors, locale));
		}		
		return null;
	}
	
	/**
	 * This action clears the SMTP password. 
	 * 
	 * @return usually nothing, output is directly printed into response stream
	 */
	public String clearSMTPPassword() {
		if(user == null || !user.getIsSysAdmin()) {
			// just throw her to the logon page
			return "logon";
		}
		StringBuilder sb=new StringBuilder();

		//set the SMTPUser and SMTP password to null in the database
		SiteConfigBL.clearSMTPPassword();
		sb.append(JSONUtility.encodeJSONSuccess("SMTP password cleared"));

		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.println(sb);
		} catch (IOException e) {
			// not much can be done here
			LOGGER.error(Support.readStackTrace(e));
		}
		return null;
	}

	/**
	 * This action tests if outgoing e-mail connection is configured correctly.
	 * @return nothing, writes directly into the response stream.
	 */
	public String testOutgoingEmail(){
		if(user == null || !user.getIsSysAdmin()) {
			// just throw her to the logon page
			return "logon";
		}
		
		List<ControlError> errors = new LinkedList<ControlError>();
		
		// use the current configuration
		OutgoingEmailBL.validateOutgoingEmail(outgoingEmailTo, errors, locale);
		if(errors.isEmpty()){
			OutgoingEmailBL.testOutgoingEmail(siteApp, outgoingEmailTo, emailTestTo, errors, locale);
		}
		if (errors.isEmpty()) {
			JSONUtility.encodeJSONSuccess(servletResponse);
		} else {
			JSONUtility.encodeJSON(servletResponse, SiteConfigJSON.encodeJSONSiteConfigErrorList(errors, locale));
		}
		return null;
	}


	/**
	 * This action tests if LDAP is configured correctly.
	 * @return nothing, writes directly into the response stream.
	 */
	// TODO
	public String testLdap(){
		if(user == null || !user.getIsSysAdmin()) {
			// just throw her to the logon page
			return "logon";
		}

		List<ControlError> errors = new LinkedList<ControlError>();

		// use the current configuration
		SiteConfigBL.validateLdapTO(siteApp, ldapTo, errors, locale);
		if(errors.isEmpty()){
			String loginName=ldapTo.getLoginNameTest();
			String ppassword=ldapTo.getPasswordTest();
			SiteConfigBL.updateLdapTO(siteApp,ldapTo);
			SiteConfigBL.testLdap(siteApp,loginName,ppassword,errors,locale);
		}
		if (errors.isEmpty()) {
			JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		} else {
			JSONUtility.encodeJSON(servletResponse, SiteConfigJSON.encodeJSONSiteConfigErrorList(errors, locale));
		}
		return null;
	}


	//-------------------------------------------------------------------------------------
	//
	// The setters and getters for Struts2 follow...
	
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
	
	/**
	 * @param session the session to set
	 */
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	/**
	 * @return the application
	 */
	public Map getApplication() {
		return application;
	}

	/**
	 * @param application the application to set
	 */
	public void setApplication(Map application) {
		this.application = application;
	}
	
	public boolean isHasInitData() {
		return hasInitData;
	}

	public String getInitData() {
		return initData;
	}
	
	public LicenseTO getLicense() {
		return licenseTo;
	}

	public void setLicense(LicenseTO license) {
		this.licenseTo = license;
	}

	public OutgoingEmailTO getOutgoingEmail() {
		return outgoingEmailTo;
	}

	public void setOutgoingEmail(OutgoingEmailTO outgoingEmail) {
		this.outgoingEmailTo = outgoingEmail;
	}


	public FullTextSearchTO getFullTextSearch() {
		return fullTextSearchTo;
	}

	public void setFullTextSearch(FullTextSearchTO fullTextSearch) {
		this.fullTextSearchTo = fullTextSearch;
	}

	public LdapTO getLdap() {
		return ldapTo;
	}

	public void setLdap(LdapTO ldap) {
		this.ldapTo = ldap;
	}

	public OtherSiteConfigTO getOtherSiteConfig() {
		return otherSiteConfigTo;
	}

	public void setOtherSiteConfig(OtherSiteConfigTO otherSiteConfig) {
		this.otherSiteConfigTo = otherSiteConfig;
	}

	public void setEmailTestTo(String emailTestTo) {
		this.emailTestTo = emailTestTo;
	}

	public String getLayoutCls() {
		return layoutCls;
	}

	public String getPageTitle() {
		return pageTitle;
	}
}
