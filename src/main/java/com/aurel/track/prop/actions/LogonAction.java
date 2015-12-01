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


package com.aurel.track.prop.actions;


import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TMotdBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.prop.LoginBL;
import com.aurel.track.util.LabelValueBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * Implementation of <strong>Action</strong> that validates a user logon.
 * @author Joerg Friedrich <joerg.friedrich@computer.org>
 * @version $Revision: 1695 $ $Date: 2015-10-29 08:06:44 +0100 (Thu, 29 Oct 2015) $
 */
//@ParentPackage("struts-track-base")
//	@InterceptorRef("editNoAuth")
//})
//    @Result(name="loading", location="logon.jsp"),
//	@Result(name="admin", type="redirect", location="admin.action"),
//	@Result(name="banned", type="redirect", location="banned.action"),
//	@Result(name="cockpit", type="redirect", location="cockpit.action"),
//	@Result(name="itemNavigator", type="redirect", location="itemNavigator.action"),
//	// @Result(name="input", type="tiles", location="logonTile"),
//	@Result(name="forwardTo", type="redirect", location="${forwardUrl}"),
//	@Result(name="forwardToLogin", type="redirect", location="logoff.action")
//})
public final class LogonAction extends ActionSupport implements Preparable/*, SessionAware*/ {

	private static final long serialVersionUID = 340L;
	//	 Get the appropriate logger
	private static final Logger LOGGER = LogManager.getLogger(LogonAction.class);
	private static final Logger accessLogger = LogManager.getLogger("Access");

	/* The password. */
	private String password = null;

	/* The user name. */
	private String username = null;

	/* For test mode definition (automated regression testing) */
	private String isInTestMode = null;

	private boolean usingContainerBasedAuthentication = false;

	private boolean springAuthenticated = false;
	private String forwardUrl;
	private TMotdBean motd;


	private List<LabelValueBean> errors = new ArrayList<LabelValueBean>();

	private Boolean isMobileApplication = false;
	private Integer mobileApplicationVersionNo;


	private boolean hasInitData=true;
	private String initData;
	//redirect to login


	/**
	 * This method is called automatically by the framework
	 * before any other method is called.
	 */
	@Override
	public void prepare() throws Exception {
	}


	/**
	 * This method is called by the Spring security filter in case the form based
	 * login does not succeed.
	 * @return
	 */
	public String failLogin() {
		errors.add(new LabelValueBean("j_username",getText("logon.err.password.mismatch")));
		return INPUT;
	}
	 private String getApacheRemoteUser(HttpServletRequest request) {
		 Enumeration headerNames = request.getHeaderNames();
		 String userName = null;
		 while (headerNames.hasMoreElements()) {
			 String key = (String) headerNames.nextElement();
			 String value = request.getHeader(key);
			 if(key.equals("authorization")) {
				 String decodedString = null;
				 value = value.replaceAll("Basic ", "");
				 decodedString = new String(Base64.decodeBase64(value), StandardCharsets.UTF_8);
				if(decodedString != null && decodedString.split(":").length > 0) {
					 String[] userPassArr = decodedString.split(":");
					 userName = userPassArr[0];
				}
			 }
		 }
		 return userName;
	 }

	/**
	 * This just forwards to the login screen. If we really want to log in we
	 * need to explicitly call the login action.
	 * If we use container based authentication, we skip the login screen
	 * and first try to use the container registered user.
	 */

//	@Override
//		@Action("/logon"),
//	})
	@Override
	public String execute(){
		// if Container Based Authentication is enabled and we can get a remote
		// user we use that one, no more questions asked. However, a local login
		// always overrules.
		//If container based authentication was successfully executed, must be stored into session.
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession httpSession = request.getSession();
		if (username != null && !"".equals(username)
			&& password != null && !"".equals(password)) {
			httpSession.setAttribute("containerBasedAuthentication", false);
			return "forwardToLogin";
		}

		String cbaUserName = ServletActionContext.getRequest().getRemoteUser();
		if(cbaUserName == null) {
			cbaUserName = getApacheRemoteUser(ServletActionContext.getRequest());
		}
		if (cbaUserName != null && ApplicationBean.getInstance().getSiteBean().getIsCbaAllowed()) {
			username = cbaUserName;
			usingContainerBasedAuthentication = true;
			httpSession.setAttribute("containerBasedAuthentication", true);
			try {
				TPersonBean personBean = PersonBL.loadByLoginName(cbaUserName);
				LoginBL.login(null, false, username, true, null, null, false, null, getLocale());
				String homePage = null;
				if(personBean != null) {
					homePage = personBean.getHomePage();
					return homePage;
				}else {
					return "itemNavigator";
				}

			} catch (Exception e) {
				httpSession.setAttribute("containerBasedAuthentication", false);
				return "forwardToLogin";
			}
		} else {
			httpSession.setAttribute("containerBasedAuthentication", false);
			return "forwardToLogin";
		}
	}

//	@Action("/login")
	public String login() throws Exception {
		Locale locale = getLocale();
		return LoginBL.login(isInTestMode, isMobileApplication, username, usingContainerBasedAuthentication, password,
				forwardUrl, springAuthenticated, mobileApplicationVersionNo, locale);
	}


	public String restLogin() throws Exception {
		Locale locale = getLocale();
		return LoginBL.restLogin(username, password, locale);
	}

	/***
	 * This method returns if server support self registration, ldap, and force ldap.
	 * Used by mobile client
	 */
	public void getRegistrationRights() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"data\":{");
		boolean selfReg = ApplicationBean.getInstance().getSiteBean().getIsSelfRegisterAllowedBool();
	    JSONUtility.appendBooleanValue(sb, "selfRegistration", selfReg);
	    boolean isLDAPOn = ApplicationBean.getInstance().getSiteBean().getIsLDAPOnBool();
	    JSONUtility.appendBooleanValue(sb, "isLDAPOn", isLDAPOn);
	    boolean isForceLDAP =  ApplicationBean.getInstance().getSiteBean().getIsForceLdap();
	    JSONUtility.appendBooleanValue(sb, "isForceLDAP", isForceLDAP, true);
		sb.append("}");
		sb.append("}");
		JSONUtility.encodeJSON(ServletActionContext.getResponse(), sb.toString());
	}




	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getJ_password() {
		return password;
	}

	public void setJ_password(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getJ_username() {
		return username;
	}

	public void setJ_username(String username) {
		this.username = username;
	}

	public String getTestMode() {
		return this.isInTestMode;
	}

	public void setTestMode(String inTestMode) {
		this.isInTestMode = inTestMode;
	}


	/**
	 * @return the forwardUrl
	 */
	public String getForwardUrl() {
		return forwardUrl;
	}


	public TMotdBean getMotd() {
		return motd;
	}


	public boolean isHasInitData() {
		return hasInitData;
	}


	public String getInitData() {
		return initData;
	}

	public Boolean getIsMobileApplication() {
		return isMobileApplication;
	}


	public void setIsMobileApplication(Boolean isMobileApplication) {
		this.isMobileApplication = isMobileApplication;
	}



	public Integer getMobileApplicationVersionNo() {
		return mobileApplicationVersionNo;
	}


	public void setMobileApplicationVersionNo(Integer mobileApplicationVersionNo) {
		this.mobileApplicationVersionNo = mobileApplicationVersionNo;
	}
}
