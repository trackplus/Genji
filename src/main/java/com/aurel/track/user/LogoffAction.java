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


package com.aurel.track.user;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.ApplicationStarter;
import com.aurel.track.Constants;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TMotdBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.prop.BanProcessor;
import com.aurel.track.prop.LoginBL;
import com.aurel.track.util.LocaleHandler;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * Implementation of <strong>Action</strong> that processes a user logoff.
 *
 * @author Joerg Friedrich <joerg.friedrich@computer.org>
 *
 */
public final class LogoffAction extends ActionSupport implements Preparable, SessionAware {

	private static final long serialVersionUID = 400L;

	private static final Logger LOGGER = LogManager.getLogger(LogoffAction.class);
	private static final Logger accessLogger = LogManager.getLogger("Access");

	private String hasLoggedOut = "t";

	private Map<String, Object> session;
	private TMotdBean motd;

	private String nonce;

	private Boolean mayBeMobile = false;
	private Boolean isMobileApplication = false;
	private Boolean logOutMobile = false;

	private String mobileApplicationVersion;
	private Integer mobileApplicationVersionNo;

	private boolean hasInitData = true;
	private String initData;
	private String layoutCls = "com.trackplus.layout.LogonLayout";
	private String pageTitle = "logon.title";
	private HttpSession httpSession;
	private boolean logOff = false;

	/**
	 * This method is automatically called by the framework before any other
	 * method
	 */
	@Override
	public void prepare() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		httpSession = request.getSession();
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

	@Override
	public String execute() throws Exception {
		// If the user was logged in via container and NOT mobile application
		// and the user clicked to log off then
		// a log off procedure will be executed
		if (httpSession.getAttribute("containerBasedAuthentication") != null && logOff && !isMobileApplication) {
			if ((Boolean) httpSession.getAttribute("containerBasedAuthentication")) {
				return execute2(true);
			}
		}

		String cbaUserName = ServletActionContext.getRequest().getRemoteUser();
		if(cbaUserName == null) {
			cbaUserName = getApacheRemoteUser(ServletActionContext.getRequest());
		}
		// if container based auth. is enabled and the user logged in
		// successfully the system will be redirected to the users home page.
		if (cbaUserName != null
			&& ApplicationBean.getInstance().getSiteBean().getIsCbaAllowed() && !isMobileApplication) {
			String username = cbaUserName;
			boolean usingContainerBasedAuthentication = true;
			Locale locale = getLocale();
			try {
				LoginBL.login(null, isMobileApplication, username, usingContainerBasedAuthentication, null, "forwardToLogin", false, 220, locale);
				httpSession.setAttribute("containerBasedAuthentication", true);
				TPersonBean personBean = PersonBL.loadByLoginName(cbaUserName);
				String homePage = null;
				if (personBean != null) {
					homePage = personBean.getHomePage();
				} else {
					return execute2(true);
				}
				return homePage;
			} catch (Exception e) {
				httpSession.setAttribute("containerBasedAuthentication", false);
				return execute2(true);
			}
			// the deffault log off procedure
		} else {
			httpSession.setAttribute("containerBasedAuthentication", false);
			return execute2(true);
		}
	}

	private void loginForm(HttpServletResponse response, String customLoginFullPath) throws Exception {
		Writer writer = null;
		BufferedReader reader = null;
		try {
			URL url = new URL(customLoginFullPath);
			URLConnection conn = url.openConnection();
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			writer = response.getWriter();
			String line;
			while ((line = reader.readLine()) != null) {
				writer.append(line);
			}
		} finally {
			if (writer != null) {
				writer.flush();
			}
			if (reader != null) {
				reader.close();
			}
		}
	}

	/**
	 * This method is automatically called if the associated action is
	 * triggered.
	 */
	private String execute2(boolean checkForMobile) throws Exception {
		Boolean ready = (Boolean) ServletActionContext.getServletContext().getAttribute(ApplicationStarter.READY);
		if (ready == null || ready.booleanValue() == false) {
			return "loading";
		}
		TPersonBean user = null;
		if (session != null) {
			user = (TPersonBean) session.get(Constants.USER_KEY);
		}
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession httpSession = request.getSession();

		Locale locale = request.getLocale();

		if (locale == null) {
			locale = Locale.getDefault();
		}

		if (user != null) {
			locale = user.getLocale();
		}
		// Process this users logoff
		LogoffBL.doLogoff(session, httpSession, locale);
		StringBuilder sb = LogoffBL.createInitData(httpSession, checkForMobile, request, isMobileApplication, mobileApplicationVersionNo, locale);
		initData = sb.toString();
		if (ApplicationBean.getInstance().getInstallProblem() != null) {
			String extJSLocale = LocaleHandler.getExistingExtJSLocale(locale);
			httpSession.setAttribute("EXTJSLOCALE", extJSLocale);
			List<String> errors = ApplicationBean.getInstance().getInstallProblem();
			setActionErrors(errors);
			return ERROR;
		}
		BanProcessor bp = BanProcessor.getBanProcessor();
		if (bp.isBanned(request.getRemoteAddr())) {
			clearFieldErrors();
			accessLogger.info("LOGON: Access attempt from banned IP " + request.getRemoteAddr() + " at " + new Date().toString());
			return "banned";
		}
		if (isMobileApplication) {
			if (logOutMobile) {
				StringBuilder sbMobile = new StringBuilder();
				sbMobile.append("{");
				JSONUtility.appendBooleanValue(sbMobile, "success", true, true);
				sbMobile.append("}");
				JSONUtility.encodeJSON(ServletActionContext.getResponse(), sbMobile.toString());
			} else {
				ServletActionContext.getResponse().addHeader("Access-Control-Allow-Origin", "*");
				JSONUtility.encodeJSON(ServletActionContext.getResponse(), sb.toString());
			}
			return null;
		}

		if (mayBeMobile) {
			return "successMobile";
		}
		return SUCCESS;
	}

	public String backToDesktop() throws Exception {
		return execute2(false);
	}

	public Map<String, Object> getSession() {
		return session;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getHasLoggedOut() {
		return hasLoggedOut;
	}

	public void setHasLoggedOut(String lof) {
		this.hasLoggedOut = lof;
	}

	public TMotdBean getMotd() {
		return motd;
	}

	public Boolean isMayBeMobile() {
		return mayBeMobile;
	}

	public boolean isHasInitData() {
		return hasInitData;
	}

	public String getInitData() {
		return initData;
	}

	public String getNonce() {
		return nonce;
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

	public String getMobileApplicationVersion() {
		return mobileApplicationVersion;
	}

	public void setMobileApplicationVersion(String mobileApplicationVersion) {
		this.mobileApplicationVersion = mobileApplicationVersion;
	}

	public Boolean getLogOutMobile() {
		return logOutMobile;
	}

	public void setLogOutMobile(Boolean logOutMobile) {
		this.logOutMobile = logOutMobile;
	}

	public String getLayoutCls() {
		return layoutCls;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public boolean isLogOff() {
		return logOff;
	}

	public void setLogOff(boolean logOff) {
		this.logOff = logOff;
	}
}
