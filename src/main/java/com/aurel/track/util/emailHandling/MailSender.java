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

package com.aurel.track.util.emailHandling;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.activation.DataSource;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang3.exception.ExceptionUtils;
import javax.mail.util.ByteArrayDataSource;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.GeneralSettings;
import com.aurel.track.admin.server.siteConfig.OutgoingEmailBL;
import com.aurel.track.attachment.AttachBL;
import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.util.LabelValueBean;


public class MailSender extends Thread {
	protected static Logger LOGGER = LogManager.getLogger(MailSender.class);
	protected static Date infoThrottleTimer = new Date();
	
	private static Object lock = new Object();
	protected static String DEFAULTENCODINGT= "UTF-8";

	private Integer timeout=null;
	
	private Email email = null;

	private List<LabelValueBean> attachments;
	private boolean  notIncludeImages;

	private SMTPMailSettings smtpMailSettings;

	/**
	 * Simple constructor for MailSender class.
	 * @param internetAddressFrom
	 * @param internetAddressesTo
	 * @param subject
	 * @param messageBody
	 * @param isPlain
	 */
	public MailSender(InternetAddress internetAddressFrom, InternetAddress internetAddressesTo, String subject,
			          String messageBody, boolean isPlain) {
		this(internetAddressFrom,internetAddressesTo,subject,messageBody, isPlain,null);
	}

	/**
	 * Constructor with attachments.
	 * @param internetAddressFrom
	 * @param internetAddressesTo
	 * @param subject
	 * @param messageBody
	 * @param isPlain
	 * @param attachments
	 */
	public MailSender(InternetAddress internetAddressFrom,InternetAddress internetAddressesTo,String subject,String messageBody, boolean isPlain,List<LabelValueBean> attachments) {
		this(internetAddressFrom, new InternetAddress[]{internetAddressesTo},subject,messageBody, isPlain,attachments);
	}

	/**
	 * Constructor with an array of receivers.
	 * @param internetAddressFrom
	 * @param internetAddressesTo array of receivers
	 * @param subject
	 * @param messageBody
	 * @param isPlain
	 * @param attachments
	 */
	public MailSender(InternetAddress internetAddressFrom,InternetAddress[] internetAddressesTo,String subject,String messageBody, boolean isPlain,List<LabelValueBean> attachments) {
		this(internetAddressFrom, internetAddressesTo, subject, messageBody, isPlain, attachments,null,null,null);
	}
	
	/**
	 * Constructor with array of receivers, cc, and bcc
	 * @param internetAddressFrom
	 * @param internetAddressesTo
	 * @param subject
	 * @param messageBody
	 * @param isPlain
	 * @param attachments
	 * @param internetAddressesCC
	 * @param internetAddressesBCC
	 * @param internetAddressesReplayTo
	 */
	public MailSender(InternetAddress internetAddressFrom,InternetAddress[] internetAddressesTo,String subject,String messageBody, boolean isPlain,
			List<LabelValueBean> attachments,InternetAddress[] internetAddressesCC,InternetAddress[] internetAddressesBCC,InternetAddress[] internetAddressesReplayTo) {
		TSiteBean siteBean = ApplicationBean.getInstance().getSiteBean();
		SMTPMailSettings smtpMailSettings= OutgoingEmailBL.getSMTPMailSettings(siteBean);
		this.init(smtpMailSettings,internetAddressFrom, internetAddressesTo, subject, messageBody, isPlain, attachments,internetAddressesCC, internetAddressesBCC,internetAddressesReplayTo);
	}
	
	/**
	 * Constructor including SMTP mail configuration settings.
	 * @param smtpMailSettings
	 * @param internetAddressFrom
	 * @param internetAddressesTo
	 * @param subject
	 * @param messageBody
	 * @param isPlain
	 */
	public MailSender(SMTPMailSettings smtpMailSettings,InternetAddress internetAddressFrom,InternetAddress internetAddressesTo,String subject,String messageBody, boolean isPlain) {
		this(smtpMailSettings,internetAddressFrom, new InternetAddress[]{internetAddressesTo},subject,messageBody, isPlain,null);
	}
	
	/**
	 * 
	 * @param smtpMailSettings
	 * @param internetAddressFrom
	 * @param internetAddressesTo
	 * @param subject
	 * @param messageBody
	 * @param isPlain
	 * @param attachments
	 */
	public MailSender(SMTPMailSettings smtpMailSettings, InternetAddress internetAddressFrom,InternetAddress[] internetAddressesTo,
			String subject,String messageBody, boolean isPlain,List<LabelValueBean> attachments) {
		this.init(smtpMailSettings,internetAddressFrom, internetAddressesTo, subject, messageBody, isPlain, attachments,null,null,null);
	}
	
	/**
	 * 
	 * @param smtpMailSettings
	 * @param internetAddressFrom
	 * @param internetAddressesTo
	 * @param subject
	 * @param messageBody
	 * @param isPlain
	 * @param attachments
	 * @param internetAddressesCC
	 * @param internetAddressesBCC
	 * @param internetAddressesReplayTo
	 */
	private void init(SMTPMailSettings smtpMailSettings, InternetAddress internetAddressFrom,InternetAddress[] internetAddressesTo,
			String subject,String messageBody, boolean isPlain,List<LabelValueBean> attachments,
			InternetAddress[] internetAddressesCC,InternetAddress[] internetAddressesBCC,InternetAddress[] internetAddressesReplayTo){
		this.smtpMailSettings=smtpMailSettings;
		try {
			if (isPlain) {
				email = new SimpleEmail();
				if (attachments != null && attachments.size() > 0) {
					email = new MultiPartEmail();
				}
			} else {
				email = new HtmlEmail();
			}
			if (LOGGER.isTraceEnabled()) {
				email.setDebug(true);
			}
			email.setHostName(smtpMailSettings.getHost());
			email.setSmtpPort(smtpMailSettings.getPort());
			email.setCharset(smtpMailSettings.getMailEncoding()!=null?smtpMailSettings.getMailEncoding():DEFAULTENCODINGT);
			if(timeout!=null){
				email.setSocketConnectionTimeout(timeout);
				email.setSocketTimeout(timeout);
			}
			if (smtpMailSettings.isReqAuth()) {
				email = setAuthMode(email);
			}
			email=setSecurityMode(email);

			for (int i=0; i < internetAddressesTo.length; ++i) {
				email.addTo(internetAddressesTo[i].getAddress(), internetAddressesTo[i].getPersonal());
			}
			if(internetAddressesCC!=null){
				for (int i=0; i < internetAddressesCC.length; ++i) {
					email.addCc(internetAddressesCC[i].getAddress(), internetAddressesCC[i].getPersonal());
				}
			}
			if(internetAddressesBCC!=null){
				for (int i=0; i < internetAddressesBCC.length; ++i) {
					email.addBcc(internetAddressesBCC[i].getAddress(), internetAddressesBCC[i].getPersonal());
				}
			}
			if(internetAddressesReplayTo!=null){
				for (int i=0; i < internetAddressesReplayTo.length; ++i) {
					email.addReplyTo(internetAddressesReplayTo[i].getAddress(), internetAddressesReplayTo[i].getPersonal());
				}
			}
			email.setFrom(internetAddressFrom.getAddress(), internetAddressFrom.getPersonal());
			email.setSubject(subject);
			String cid = null;
			if (isPlain) {
				email.setMsg(messageBody);
			} else {
				if (messageBody.contains("cid:$$CID$$")) {
					URL imageURL = null;
					InputStream in=null;
					in=MailSender.class.getClassLoader().getResourceAsStream("tracklogo.gif");
					
					if (in != null) {
						imageURL = new URL(ApplicationBean.getInstance().getServletContext().getResource("/") + "logoAction.action?type=m");
						DataSource ds = new ByteArrayDataSource(in, "image/"+"gif");
						cid = ((HtmlEmail)email).embed(ds, "Genji");
					} else {
						String theResource = "/WEB-INF/classes/resources/MailTemplates/tracklogo.gif";
						imageURL = ApplicationBean.getInstance().getServletContext().getResource(theResource);
						cid = ((HtmlEmail)email).embed(imageURL, "Genji");
					}
					messageBody = messageBody.replaceFirst("\\$\\$CID\\$\\$", cid);
				}
				Set<String> attachSet=new HashSet<String>();
				messageBody=MailImageBL.replaceInlineImages(messageBody,attachSet);
				if(!attachSet.isEmpty()){
					Iterator<String> it=attachSet.iterator();
					while (it.hasNext()){
						String key=it.next();
						StringTokenizer st=new StringTokenizer(key,"_");
						String workItemIDStr=st.nextToken();
						String attachmentKeyStr=st.nextToken();
						TAttachmentBean attachmentBean=null;
						try{
							attachmentBean=AttachBL.loadAttachment(Integer.valueOf(attachmentKeyStr),Integer.valueOf(workItemIDStr),true);
						}catch (Exception ex){}
						if(attachmentBean!=null){
							String fileName=attachmentBean.getFullFileNameOnDisk();
							File file = new File(fileName);
							InputStream is=new FileInputStream(file);
							DataSource ds = new ByteArrayDataSource(is, "image/"+"gif");
							cid = ((HtmlEmail)email).embed(ds, key);
							messageBody = messageBody.replaceAll("cid:"+key, "cid:"+cid);
						}
					}
				}
				((HtmlEmail)email).setHtmlMsg(messageBody);
			}
			if(attachments!=null && !attachments.isEmpty()){
				includeAttachments(attachments);
			}
		} catch (Exception e) {
			LOGGER.error("Something went wrong trying to assemble an e-mail: " + e.getMessage());
		}

	}

	@Override
	public void run(){
		send();
	}
	
	/**
	 * The run method of the sender thread for e-mails. This actually sends
	 * out the e-mails to the SMTP server.
	 */
	public boolean send() {
		boolean  result=true;
		if (smtpMailSettings.getHost() == null || "".equals(smtpMailSettings.getHost().trim() ) ) {
			//It's acceptable that there is no SMTP server :)
			if (new Date().getTime()  > infoThrottleTimer.getTime() + 12*60*60*1000 ) {
				LOGGER.info("There is no SMTP host configured, thus no e-mails will be sent.");
				infoThrottleTimer = new Date();
			}
			return false;
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetch a session using the following settings ");
			LOGGER.debug("SMTP host: " + email.getHostName());
			LOGGER.debug("SMTP port: " + smtpMailSettings.getPort());
			LOGGER.debug("MAIL encoding: " + smtpMailSettings.getMailEncoding());
			LOGGER.debug("SMTP requires authentication " + smtpMailSettings.isReqAuth());
			LOGGER.debug("SMTP authentication mode " + smtpMailSettings.getAuthMode());
			LOGGER.debug("SMTP user " + smtpMailSettings.getUser());
			LOGGER.debug("SMTP password specified " + new Boolean(smtpMailSettings.getPassword()!=null && smtpMailSettings.getPassword().length()>0).toString());
		}
		boolean limitSMTPConnections = GeneralSettings.getLimitSMTPConnections();
		if (limitSMTPConnections) {
			synchronized(lock){
				Date start = null;
				if (LOGGER.isDebugEnabled()) {
					start = new Date();
				}
				try {
					email.send();
				} catch (Exception e) {
					LOGGER.warn("Sending the mail with limited e-mail connections failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
					result = false;
				}
				if (LOGGER.isDebugEnabled() && start!=null) {
					Date end = new Date();
					LOGGER.debug("Mail sent in  " + new Long(end.getTime()-start.getTime()).toString() + " ms at " + new Date());
				}
			}
		} else {
			try {
				email.send();
			} catch (Exception e) {
				
				LOGGER.warn(e.getMessage());
				LOGGER.warn(e.getCause().getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
				result = false;
			}
		}

		return result;
	}
	
	/*
	 * Configure the authentication mode
	 */
	private Email setAuthMode(Email email) {
		Integer smtpAuthMode = new Integer(-1);
		try{
			smtpAuthMode=Integer.valueOf(smtpMailSettings.getAuthMode());
		} catch (Exception e){}


		switch (smtpAuthMode){
			case TSiteBean.SMTP_AUTHENTICATION_MODES.CONNECT_TO_INCOMING_MAIL_SERVER_BEFORE_SENDING:{
				IncomingMailSettings incomingMailSettings=smtpMailSettings.getIncomingMailSettings();
				String mailReceivingServerName=null;
				String mailReceivingUser=null;
				String mailReceivingPassword=null;
				if(incomingMailSettings!=null){
					mailReceivingServerName=incomingMailSettings.getServerName();
					mailReceivingUser=incomingMailSettings.getUser();
					mailReceivingPassword=incomingMailSettings.getPassword();
				}
				boolean newPopBeforeSmtp = true;  // TODO We should check in our POP3 session first
				email.setPopBeforeSmtp(newPopBeforeSmtp,mailReceivingServerName,mailReceivingUser,mailReceivingPassword);
				break;
			}

			case TSiteBean.SMTP_AUTHENTICATION_MODES.CONNECT_USING_SMTP_SETTINGS:{
				LOGGER.debug("Connect to SMTP server using SMTP user/password ...");
				if (smtpMailSettings.getUser() == null || "".equals(smtpMailSettings.getUser().trim())) {
					LOGGER.warn("No SMTP user found by 'Connect using SMTP settings'");
				}
				email.setAuthenticator(new DefaultAuthenticator(smtpMailSettings.getUser(), smtpMailSettings.getPassword()));
				break;
			}

			case TSiteBean.SMTP_AUTHENTICATION_MODES.CONNECT_WITH_SAME_SETTINGS_AS_INCOMING_MAIL_SERVER:{
				IncomingMailSettings incomingMailSettings=smtpMailSettings.getIncomingMailSettings();
				String mailReceivingUser=null;
				String mailReceivingPassword=null;
				if(incomingMailSettings!=null){
					mailReceivingUser=incomingMailSettings.getUser();
					mailReceivingPassword=incomingMailSettings.getPassword();
				}
				LOGGER.debug("Connect to SMTP server using incoming mail (POP3 or IMAP) user " + mailReceivingUser +
						" passord specified " +  new Boolean(mailReceivingPassword!=null && mailReceivingPassword.length()>0).toString());
				if (mailReceivingUser == null || "".equals(mailReceivingUser.trim())) {
					LOGGER.warn("No incoming mail user (POP3 or IMAP) found by 'Connect with same settings as incoming mail server'");
				}
				email.setAuthenticator(new DefaultAuthenticator(mailReceivingUser, mailReceivingPassword));
				break;
			}
		}
		return email;
	}
	
	
	private Email setSecurityMode(Email email) {
		Integer smtpSecurity = smtpMailSettings.getSecurity();
		String oldTrustStore=(String)System.clearProperty("javax.net.ssl.trustStore");
		LOGGER.debug("oldTrustStore="+oldTrustStore);
		switch (smtpSecurity) {
			case TSiteBean.SECURITY_CONNECTIONS_MODES.NEVER:
				LOGGER.debug("SMTP security connection mode is NEVER");
				break;
			case TSiteBean.SECURITY_CONNECTIONS_MODES.TLS_IF_AVAILABLE:
				LOGGER.debug("SMTP security connection mode is TLS_IF_AVAILABLE");
				email.setStartTLSEnabled(true);
				MailBL.setTrustKeyStore(smtpMailSettings.getHost());
				break;
			case TSiteBean.SECURITY_CONNECTIONS_MODES.TLS:
				LOGGER.debug("SMTP security connection mode is TLS");
				email.setStartTLSEnabled(true);
				email.setStartTLSRequired(true);
				MailBL.setTrustKeyStore(smtpMailSettings.getHost());
				break;
			case TSiteBean.SECURITY_CONNECTIONS_MODES.SSL:{
				LOGGER.debug("SMTP security connection mode is SSL");
				MailBL.setTrustKeyStore(smtpMailSettings.getHost());
				email.setSSLOnConnect(true);
				break;
			}
			default:
				break;
		}
		return email;
	}

	private void includeAttachments(List<LabelValueBean> attachments) throws Exception {
		for (int i = 0; i < attachments.size(); i++) {
			LabelValueBean lvb=attachments.get(i);
			String fullFileName=lvb.getValue();
			String fileName=lvb.getLabel();
			File f=new File(fullFileName);
			if(f!=null&&f.exists()){
				LOGGER.debug("Use attachment file:"+f.getAbsolutePath());
				org.apache.commons.mail.EmailAttachment attachment = new org.apache.commons.mail.EmailAttachment();
				attachment.setPath(f.getAbsolutePath());
				attachment.setName(fileName);
				attachment.setDisposition(org.apache.commons.mail.EmailAttachment.ATTACHMENT);
				((MultiPartEmail) email).attach(attachment);
			}else{
				LOGGER.debug("Attachment file \"" + lvb.getValue() + "\" does not not exist!");
			}
		}
	}




	public List<LabelValueBean> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<LabelValueBean> attachments) {
		this.attachments = attachments;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public boolean isNotIncludeImages() {
		return notIncludeImages;
	}

	public void setNotIncludeImages(boolean notIncludeImages) {
		this.notIncludeImages = notIncludeImages;
	}

}
