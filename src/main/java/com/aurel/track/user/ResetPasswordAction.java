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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.admin.customize.mailTemplate.MailTemplateBL;
import com.aurel.track.admin.server.motd.MotdBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TMailTemplateDefBean;
import com.aurel.track.beans.TMotdBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.prop.LoginBL;
import com.aurel.track.resources.LocalizeJSON;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.LocaleHandler;
import com.aurel.track.util.emailHandling.MailSender;
import com.aurel.track.util.event.EventPublisher;
import com.aurel.track.util.event.IEventSubscriber;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.ValidationAware;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 
 */
public final class ResetPasswordAction extends ActionSupport implements Preparable, SessionAware, ValidationAware {

	private static final long serialVersionUID = 340L;
	private Map<String, Object> session;
	/** The email address. */
	private String email = null;
	private static final Logger LOGGER = LogManager.getLogger(ResetPasswordAction.class);
	private static final Logger accessLogger = LogManager.getLogger("Access");
	private String ctk;
	private String passwd;
	private String passwd2;

	private boolean hasInitData=false;
	private String initData;
	private String layoutCls="com.trackplus.layout.ResetPasswordLayout";
	private String pageTitle="logon.resetPassword.title";

	@Override
	public void prepare() throws Exception {
	}

	@Override
	public String execute(){
		List<TPersonBean> personList = null;
		//HttpSession httpSession = request.getSession();
		ApplicationBean appBean = ApplicationBean.getInstance();
		if(appBean == null) {
			LOGGER.error("No ApplicationBean found, this should never happen");
			return null;
		}
		boolean haveErrors=false;
		boolean emailSent=false;
		StringBuilder errorsMessage=new StringBuilder();
		try {
			// set mail address
			personList = PersonBL.loadByEmail(email);
			List loginnameArray = new ArrayList(5);
			boolean isldap = false;
			if (personList != null) {
				Iterator i = personList.iterator();
				TPersonBean person = null;
				LOGGER.debug("Now looping through responses...");
				while (i.hasNext())	{
					person = (TPersonBean) i.next();
					LOGGER.debug("Could retrieve person with login name "
								 + person.getLoginName());
					if (!isldap && person.isLdapUser()) {
						isldap = true;
						addFieldError("newpassword",getText("logon.newpassword.error.isLdap"));
						errorsMessage.append(getText("logon.newpassword.error.isLdap")+"\n");
						haveErrors = true;
					}
					if (!person.isLdapUser()) {
						loginnameArray.add(person);
						Date texpDate = new Date(new Date().getTime() + 4*3600*1000); // 4 hours
						person.setTokenExpDate(texpDate);
						String tokenPasswd = DigestUtils.md5Hex(Long.toString(texpDate.getTime()));
						person.setForgotPasswordKey(tokenPasswd);
						person.setLastEdit(new Date());
						if (!appBean.getSiteBean().getIsDemoSiteBool() || person.getIsSysAdmin()) {
							PersonBL.saveSimple(person);
						}
					}
				}
			}
			// Not found
			if (loginnameArray.isEmpty() && !isldap) {
				addFieldError("newpassword",getText("logon.newpassword.error.email.missing"));
				haveErrors=true;
				errorsMessage.append(getText("logon.newpassword.error.email.missing")+"\n");
			}
			if (loginnameArray.size() > 0 && !isldap){
				// send password to the user
				try {
					emailSent=sendResetPassword(loginnameArray, email);
				}
				catch (Exception e) { // send could fail if SMTP server cannot
					addFieldError("newpassword",getText("logon.newpassword.error.email.sendFailed"));
					errorsMessage.append(getText("logon.newpassword.error.email.sendFailed")+"\n");
					haveErrors=true;
				}
			}
		}
		catch (Exception e) {
			LOGGER.debug(e.getMessage(), e);
			LOGGER.error("Cannot mail new password.");
			addFieldError("newpassword",getText("logon.err.noDataBase"));
			errorsMessage.append(getText("logon.err.noDataBase")+"\n");
			haveErrors=true;
		}

		// After successful registration the user is logged off
		if (haveErrors) {
			try {
				JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
				PrintWriter out = ServletActionContext.getResponse().getWriter();
				out.println(JSONUtility.encodeJSONErrors("0", errorsMessage.toString()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}
			return null;
		}
		else {
			LOGGER.debug(" Forwarding to welcome");
			try {
				StringBuilder sb=new StringBuilder();
				sb.append("{");
				JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
				sb.append("\"data\":{");
				JSONUtility.appendBooleanValue(sb,"emailSent",emailSent);
				sb.append("}");
				sb.append("}");
				JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
				PrintWriter out = ServletActionContext.getResponse().getWriter();
				out.println(sb);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}
			return null;
		}
	}

	/**
	 * Processes the confirmation link for newly registered users.
	 * @return
	 */
	public String confirm() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession httpSession = request.getSession();
		Locale locale = request.getLocale();
		if (locale == null) {
			locale = Locale.getDefault();
		}
		LogoffBL.doLogoff(session, httpSession, locale);
		TPersonBean personBean = PersonBL.loadByForgotPasswordToken(ctk);
		boolean userFound=false;
		if (personBean != null && personBean.getTokenExpDate().getTime() > new Date().getTime()) {
			this.getSession().put("CUSER", personBean.getLoginName());
			String prefLocale = personBean.getPrefLocale();
			if (prefLocale!=null) {
				locale = LocaleHandler.getLocaleFromString(prefLocale);
			}
			hasInitData=true;
			StringBuilder sb=new StringBuilder();
			sb.append("{");
			JSONUtility.appendStringValue(sb, "token", ctk);
			JSONUtility.appendStringValue(sb,"loginName",personBean.getLoginName(),true);
			sb.append("}");
			initData=sb.toString();
			userFound=true;
		}
		httpSession.setAttribute("localizationJSON", LocalizeJSON.encodeLocalization(locale));
		String extJSLocale = LocaleHandler.getExistingExtJSLocale(locale);
		httpSession.setAttribute("EXTJSLOCALE", extJSLocale);

		if(userFound){
			layoutCls="com.trackplus.layout.ResetPasswordLayout";
			pageTitle="logon.resetPassword.title";
			return "resetPassword";
		}else {
			layoutCls="com.trackplus.layout.ResetPasswordExpiredLayout";
			pageTitle="logon.register.expired.title";
			return "expired";
		}
	}


	/**
	 *In case of changing password succeeded, the user will be logged in
	 *automatically and redirected to default page.
	 * @return
	 */
	public String reset() {
		TPersonBean personBean = PersonBL.loadByForgotPasswordToken(ctk);
		if (personBean != null && personBean.getTokenExpDate().getTime() > new Date().getTime()) {
			personBean.setDisabled(false);
			personBean.setTokenExpDate(null);
			personBean.setTokenPasswd(null);
			personBean.setForgotPasswordKey(null);
			personBean.setPasswdEncrypted(passwd);
			PersonBL.save(personBean);
			String forwardUrl = "";
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession httpSession = request.getSession();
			Locale locale = getLocale();
			if(locale==null){
				locale = Locale.getDefault();
				LOGGER.debug("Requested locale is null. Using default:"+locale.getDisplayName());
			}else{
				LOGGER.debug("Requested locale " + locale.getDisplayName());
			}
			
			TMotdBean motd = MotdBL.loadMotd(locale.getLanguage());
			String redirectMapEntry = "";
			ArrayList<LabelValueBean> errors = new ArrayList<LabelValueBean>();
			StringBuilder sb = LoginBL.createLoginResponseJSON (personBean.getLoginName(), passwd, null, 
					false, false, 
					request,  errors, httpSession, 
					forwardUrl, motd, false, locale, 
					null, redirectMapEntry);	
			return  LoginBL.writeJSONResponse(sb);  // The redirect is done by the client JavaScript
		}

		layoutCls="com.trackplus.layout.ResetPasswordExpiredLayout";
		pageTitle="logon.register.expired.title";
		return "expired";
	}

	private boolean sendResetPassword(List<TPersonBean> persons, String mail)throws Exception {
		boolean emailSent = true;
		TPersonBean personBean;
		boolean b;
		for (int i = 0; i < persons.size(); i++) {
			personBean = persons.get(i);
			b=sendEmail(personBean);
			if(!b){
				emailSent=false;
			}
		}
		return emailSent;
	}
	private boolean sendEmail(TPersonBean personBean){
		boolean emailSent = false;
		StringWriter w = new StringWriter();
		Template template = null;
		Map<String,String> root = new HashMap<String,String>();

		TMailTemplateDefBean mailTemplateDefBean = MailTemplateBL.getSystemMailTemplateDefBean(IEventSubscriber.EVENT_POST_USER_FORGOTPASSWORD, personBean);

		String subject;
		if(mailTemplateDefBean!=null){
			String templateStr=mailTemplateDefBean.getMailBody();
			subject = mailTemplateDefBean.getMailSubject();
			try {
				template = new Template("name", new StringReader(templateStr), new Configuration());
			} catch (IOException e) {
				LOGGER.debug("Loading the template failed with " + e.getMessage(), e);
			}
		} else {
			LOGGER.error("Can't get mail template for forget password");
			return false;
		}

		if (template==null) {
			LOGGER.error("No valid template found for registration e-mail (maybe compilation errors).");
			return false;
		}

		TSiteBean siteBean = ApplicationBean.getInstance().getSiteBean();
		//The path starts with a "/" character but does not end with a "/"
		String contextPath=ApplicationBean.getInstance().getServletContext().getContextPath();
		String siteURL=siteBean.getServerURL();
		if(siteURL==null){
			siteURL="";
		}else if(siteURL.endsWith("/")){
			siteURL=siteURL.substring(0,siteURL.lastIndexOf("/"));
		}
		String confirmURL = siteURL + contextPath+"/resetPassword!confirm.action?ctk=" + personBean.getForgotPasswordKey();
		String serverURL = siteURL + contextPath;

		root.put("loginname", personBean.getUsername());
		root.put("firstname", personBean.getFirstName());
		root.put("lastname", personBean.getLastName());
		root.put("serverurl", serverURL);
		root.put("confirmUrl", confirmURL);
		root.put("ldap", new Boolean(personBean.isLdapUser()).toString());

		try {
			template.process(root, w);
		} catch (Exception e) {
			LOGGER.error("Processing registration template " + template.getName() + " failed with " + e.getMessage());
		}
		w.flush();
		String messageBody = w.toString();

		try {
			// Send mail to newly registered user
			MailSender ms = new MailSender(new InternetAddress(siteBean.getTrackEmail(), siteBean.getEmailPersonalName()),new InternetAddress(personBean.getEmail(), personBean.getFullName()), subject, messageBody, mailTemplateDefBean.isPlainEmail());
			emailSent=ms.send(); // wait for sending email
			LOGGER.debug("emailSend:"+emailSent);

			EventPublisher evp = EventPublisher.getInstance();
			if (evp != null) {
				List<Integer> events = new LinkedList<Integer>();
				events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_USER_FORGOTPASSWORD));
				evp.notify(events, personBean);
			}
		}
		catch (Exception t) {
			LOGGER.error("SendMail reset password", t);
		}
		return emailSent;
	}




	public Map<String, Object> getSession() {
		return session;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getInitData() {
		return initData;
	}

	public void setInitData(String initData) {
		this.initData = initData;
	}

	public boolean isHasInitData() {
		return hasInitData;
	}

	public void setHasInitData(boolean hasInitData) {
		this.hasInitData = hasInitData;
	}

	public String getCtk() {
		return ctk;
	}

	public void setCtk(String ctk) {
		this.ctk = ctk;
	}

	public String getPasswd2() {
		return passwd2;
	}

	public void setPasswd2(String passwd2) {
		this.passwd2 = passwd2;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getLayoutCls() {
		return layoutCls;
	}

	public String getPageTitle() {
		return pageTitle;
	}
}
