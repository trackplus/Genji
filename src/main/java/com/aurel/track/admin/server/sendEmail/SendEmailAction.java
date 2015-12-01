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


package com.aurel.track.admin.server.sendEmail;

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
import com.aurel.track.admin.server.siteConfig.OutgoingEmailBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.item.SendItemEmailBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.util.emailHandling.SMTPMailSettings;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;


public class SendEmailAction extends ActionSupport implements Preparable, SessionAware/*, RequestAware */{
	private static final long serialVersionUID = 1009768020080913361L;

	private static final Logger LOGGER = LogManager.getLogger(SendEmailAction.class);

	//session map
	private Map<String,Object> session;

	private Locale locale;
	private TPersonBean tPerson;
	private String toCustom;
	private String ccCustom;
	private String bccCustom;
	private String subject,mailBody;

	@Override
	public void prepare() throws Exception {
		LOGGER.debug("SendMailAction.prepare()");
		locale = (Locale) session.get(Constants.LOCALE_KEY);
		tPerson = (TPersonBean) session.get(Constants.USER_KEY);
		if (tPerson==null) {
			return;
		}
	}
	@Override
	public void setSession(Map<String, Object> sess) {
		this.session=sess;
	}

	@Override
	public String execute() throws Exception {
		LOGGER.debug("SendMailAction.execute()");
		subject="";
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		sb.append(JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendStringValue(sb,"subject",subject,true);
		sb.append("}}");
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.println(sb);
		} catch (IOException e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	
	/**
	 * Send an e-mail to the selected persons or all persons in the selected departments
	 * @return
	 */
	public String sendEmail(){
		LOGGER.debug("SendMailAction.sendMail()");
		TSiteBean siteBean = ApplicationBean.getInstance().getSiteBean();
		SMTPMailSettings smtpMailSettings= OutgoingEmailBL.getSMTPMailSettings(siteBean);
		if (smtpMailSettings.getHost() == null || "".equals(smtpMailSettings.getHost().trim() ) ) {
			//It's acceptable that there is no SMTP server :)
			LOGGER.info("No SMTP host found, e-mail sending is impossible");
			return encodeFailure("No SMTP host found, e-mail sending is impossible",SendItemEmailBL.ERROR_EMAIL_NOT_SEND);
		}
		List<TPersonBean> groups= PersonBL.loadGroups();

		List<TPersonBean> recipients=SendItemEmailBL.validateEmails(toCustom,groups);
		if(recipients==null){
			return encodeFailure(getText("item.action.sendItemEmail.err.invalidEmail"),SendItemEmailBL.ERROR_INVALID_EMAIL);
		}
		if(recipients.isEmpty()){
			return encodeFailure(getText("admin.server.sendEmail.err.needPerson"),SendItemEmailBL.ERROR_NEED_PERSON);
		}
		if (subject==null || "".equals(subject.trim())){
			return encodeFailure(getText("admin.server.sendEmail.err.needSubject"),SendItemEmailBL.ERROR_NEED_SUBJECT);
		}
		if (mailBody==null || "".equals(mailBody.trim())){
			return encodeFailure(getText("admin.server.sendEmail.err.needBody"),SendItemEmailBL.ERROR_NEED_BODY);
		}
		List<TPersonBean> recipientsCC=SendItemEmailBL.validateEmails(ccCustom,groups);
		List<TPersonBean> recipientsBCC=SendItemEmailBL.validateEmails(bccCustom,groups);

		Boolean emailSent=SendEmailBL.sendEmail(tPerson,recipients,recipientsCC, recipientsBCC, subject,mailBody);

		if(emailSent!=null){
			if(emailSent.booleanValue()) {
				JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
				return null;
			}else{
				return encodeFailure(getText("item.action.sendItemEmail.err.mailNotSent"), SendItemEmailBL.ERROR_EMAIL_NOT_SEND);
			}
		}
	
		return encodeFailure(getText("item.action.sendItemEmail.lbl.toMuchTimeNeed"),SendItemEmailBL.ERROR_NEED_MORE_TIME);
	}

	
	private String encodeFailure(String error,Integer errorCode){
		JSONUtility.encodeJSONFailure(ServletActionContext.getResponse(),error,errorCode);
		return null;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	public void setMailBody(String mailBody) {
		this.mailBody = mailBody;
	}

	public void setToCustom(String toCustom) {
		this.toCustom = toCustom;
	}

	public void setCcCustom(String ccCustom) {
		this.ccCustom = ccCustom;
	}

	public void setBccCustom(String bccCustom) {
		this.bccCustom = bccCustom;
	}
}
