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

package com.aurel.track.admin.server.siteConfig.incomingEmail;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.aurel.track.admin.server.siteConfig.OutgoingEmailBL;
import com.aurel.track.admin.server.siteConfig.SiteConfigBL;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Perl5Compiler;

import com.aurel.track.beans.TSiteBean;
import com.aurel.track.json.ControlError;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.emailHandling.MailReader;

/**
 * It manages handling of the server configuration for incoming e-mail connections.
 */
public class IncomingEmailBL {
	protected static final Logger LOGGER = LogManager.getLogger(IncomingEmailBL.class);
	public static final String ADMIN_SERVER_CONFIG_ERR_INVALID_MAIL_SETTINGS = "admin.server.config.err.invalidMailSettings";

	private IncomingEmailBL(){
	}

	public static IncomingEmailTO getIncomingEmailTO(TSiteBean siteBean){
		return  getIncomingEmailTO(siteBean,null);
	}

	/**
	 * This method returns a transfer object for the incoming e-mail
	 * configuration (POP3 or IMAP). The transfer object can be used to move
	 * data between the application layer and the user interface.
	 *
	 * @param siteBean contains all data for the server configuration
	 * @param locale the locale used for translating the radio group texts.
	 *
	 * @return the transfer object for the incoming e-mail configuration
	 */
	public static IncomingEmailTO getIncomingEmailTO(TSiteBean siteBean, Locale locale){
		IncomingEmailTO incomingEmailTO=new IncomingEmailTO();
		incomingEmailTO.setEmailSubmissionEnabled(siteBean.getIsEmailSubmissionOn());
		String protocol=siteBean.getMailReceivingProtocol();
		if(protocol==null||protocol.length()==0){
			protocol="pop3";
			siteBean.setMailReceivingProtocol(protocol);
		}
		Integer popPort = siteBean.getMailReceivingPort();
		if (popPort == null || popPort.intValue() < 1) {
			if("pop3".equals(protocol)){
				siteBean.setMailReceivingPort(new Integer(110));  // set port to default value
			}
			if("imap".equals(protocol)){
				siteBean.setMailReceivingPort(new Integer(143));  // set port to default value
			}
		}
		incomingEmailTO.setProtocol(siteBean.getMailReceivingProtocol());
		incomingEmailTO.setServerName(siteBean.getMailReceivingServerName());
		incomingEmailTO.setPort(siteBean.getMailReceivingPort());
		incomingEmailTO.setUser(siteBean.getMailReceivingUser());
		incomingEmailTO.setPassword(siteBean.getMailReceivingPassword());

		Integer mailReceivingSecurityConnection=siteBean.getMailReceivingSecurityConnection();
		if(mailReceivingSecurityConnection!=null){
			incomingEmailTO.setSecurityConnection(siteBean.getMailReceivingSecurityConnection());
		}else{
			incomingEmailTO.setSecurityConnection(TSiteBean.SECURITY_CONNECTIONS_MODES.NEVER);
		}

		incomingEmailTO.setKeepMessagesOnServer(new Boolean(siteBean.getPreferenceProperty(TSiteBean.KEEP_MESSAGES_ON_SERVER)));
		incomingEmailTO.setUnknownSenderEnabled(siteBean.getIsUnknownSenderOn());
		incomingEmailTO.setUnknownSenderRegistrationEnabled(siteBean.getIsUnknownSenderRegistrationOn());
		incomingEmailTO.setDefaultProject(siteBean.getDefaultProject());
		incomingEmailTO.setAllowedEmailPattern(siteBean.getAllowedEmailPattern());
		if(locale!=null){
			incomingEmailTO.setSecurityConnectionsModes(OutgoingEmailBL.getSecurityConnectionsModes(locale));
		}
		return incomingEmailTO;
	}

	/**
	 * Updates the site bean (server configuration bean) with new information from
	 * the incoming e-mail configuration transfer object.
	 *
	 * @param siteBean contains all data for the server configuration
	 * @param incomingEmailTO transfer object with POP3 or IMAP server configuration data
	 *
	 */
	public static void updateIncomingEmail(TSiteBean siteBean, IncomingEmailTO incomingEmailTO){
		siteBean.setIsEmailSubmissionOn(incomingEmailTO.isEmailSubmissionEnabled());
		if (siteBean.getIsEmailSubmissionOn()) {
			siteBean.setMailReceivingProtocol(incomingEmailTO.getProtocol());
			siteBean.setMailReceivingServerName(incomingEmailTO.getServerName());
			siteBean.setMailReceivingSecurityConnection(incomingEmailTO.getSecurityConnection());
			Integer popPort = incomingEmailTO.getPort();
			if (popPort == null || popPort.intValue() < 1) {
				if("pop3".equals(incomingEmailTO.getProtocol())){
					incomingEmailTO.setPort(new Integer(110));  // set port to default value
				}
				if("imap".equals(incomingEmailTO.getProtocol())){
					incomingEmailTO.setPort(new Integer(143));  // set port to default value
				}
			}
			siteBean.setMailReceivingPort(incomingEmailTO.getPort());
			siteBean.setMailReceivingUser(incomingEmailTO.getUser());
			if(incomingEmailTO.getPassword() !=null && incomingEmailTO.getPassword().trim().length() > 0){
				siteBean.setMailReceivingPassword(incomingEmailTO.getPassword());
			}
			siteBean.setIsUnknownSenderOn(incomingEmailTO.isUnknownSenderEnabled());
			if(siteBean.getIsUnknownSenderOn()) {
				siteBean.setIsUnknownSenderRegistrationOn(incomingEmailTO.isUnknownSenderRegistrationEnabled());
			}
			siteBean.setDefaultProject(incomingEmailTO.getDefaultProject());
			siteBean.setKeepMessagesOnServer(Boolean.toString(incomingEmailTO.isKeepMessagesOnServer()));
		}
		siteBean.setAllowedEmailPattern(incomingEmailTO.getAllowedEmailPattern());
	}

	/*
		 * Does some checking for the incoming e-mail server connection
		 *
		 * @param incomingEmailTO transfer object with POP3 or IMAP server configuration data
		 * @param site contains all data for the server configuration
		 * @param errors the incoming list of errors
		 * @return the augmented list of errors
		 */
	public static List<ControlError> validateIncomingEmail(IncomingEmailTO incomingEmailTO,
	                                                        TSiteBean siteBean, List<ControlError> errors, Locale locale) {
		String patternString = incomingEmailTO.getAllowedEmailPattern();
		Perl5Compiler pc = new Perl5Compiler();
		if (patternString == null) {
			patternString = "";
		}
		try {
			pc.compile(patternString,Perl5Compiler.CASE_INSENSITIVE_MASK |Perl5Compiler.SINGLELINE_MASK);
		}
		catch (MalformedPatternException e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			List<String> controlPath = new LinkedList<String>();
			controlPath.add(IncomingEmailTO.JSONFIELDS.TAB_INCOMING_EMAIL);
			controlPath.add(IncomingEmailTO.JSONFIELDS.FS_MAILBOX_MORE);
			controlPath.addAll(JSONUtility.getPathInHelpWrapper(IncomingEmailTO.JSONFIELDS.ALLOWED_EMAIL_PATTERN));
			errors.add(new ControlError(controlPath, SiteConfigBL.getText("admin.server.config.err.malformedEmailPattern", locale)));
		}
		if(incomingEmailTO.isEmailSubmissionEnabled()){
			if(incomingEmailTO.getServerName() == null || incomingEmailTO.getServerName().trim().length()==0) {
				List<String> controlPath = new LinkedList<String>();
				controlPath.add(IncomingEmailTO.JSONFIELDS.TAB_INCOMING_EMAIL);
				controlPath.add(IncomingEmailTO.JSONFIELDS.FS_INCOMING_SERVER);
				controlPath.addAll(JSONUtility.getPathInHelpWrapper(IncomingEmailTO.JSONFIELDS.SERVER_NAME));
				errors.add(new ControlError(controlPath, SiteConfigBL.getText("admin.server.config.err.invalidPOPServerName", locale)));
			}
			String password=null;
			if(incomingEmailTO.getPassword() !=null && incomingEmailTO.getPassword().trim().length() > 0){
				password=incomingEmailTO.getPassword();
			}else{
				password=siteBean.getMailReceivingPassword();
			}
			String err = MailReader.verifyMailSetting(incomingEmailTO.getProtocol(),
					incomingEmailTO.getServerName(),
					incomingEmailTO.getSecurityConnection(),
					incomingEmailTO.getPort(),
					incomingEmailTO.getUser(),
					password);
			if (err != null) {
				List<String> controlPath = new LinkedList<String>();
				controlPath.add(IncomingEmailTO.JSONFIELDS.TAB_INCOMING_EMAIL);
				controlPath.add(IncomingEmailTO.JSONFIELDS.FS_INCOMING_SERVER);
				controlPath.addAll(JSONUtility.getPathInHelpWrapper(IncomingEmailTO.JSONFIELDS.SERVER_NAME));
				errors.add(new ControlError(controlPath, SiteConfigBL.getText(ADMIN_SERVER_CONFIG_ERR_INVALID_MAIL_SETTINGS, locale)));

				controlPath = new LinkedList<String>();
				controlPath.add(IncomingEmailTO.JSONFIELDS.TAB_INCOMING_EMAIL);
				controlPath.add(IncomingEmailTO.JSONFIELDS.FS_INCOMING_SERVER);
				controlPath.addAll(JSONUtility.getPathInHelpWrapper(IncomingEmailTO.JSONFIELDS.PORT));
				errors.add(new ControlError(controlPath, SiteConfigBL.getText(ADMIN_SERVER_CONFIG_ERR_INVALID_MAIL_SETTINGS, locale)));

				controlPath = new LinkedList<String>();
				controlPath.add(IncomingEmailTO.JSONFIELDS.TAB_INCOMING_EMAIL);
				controlPath.add(IncomingEmailTO.JSONFIELDS.FS_MAILBOX_AUTH);
				controlPath.addAll(JSONUtility.getPathInHelpWrapper(IncomingEmailTO.JSONFIELDS.user));
				errors.add(new ControlError(controlPath, SiteConfigBL.getText(ADMIN_SERVER_CONFIG_ERR_INVALID_MAIL_SETTINGS, locale)));

				controlPath = new LinkedList<String>();
				controlPath.add(IncomingEmailTO.JSONFIELDS.TAB_INCOMING_EMAIL);
				controlPath.add(IncomingEmailTO.JSONFIELDS.FS_MAILBOX_AUTH);
				controlPath.add(IncomingEmailTO.JSONFIELDS.PASSWORD);
				errors.add(new ControlError(controlPath, SiteConfigBL.getText(ADMIN_SERVER_CONFIG_ERR_INVALID_MAIL_SETTINGS, locale)));
			}
		}
		return errors;
	}
}
