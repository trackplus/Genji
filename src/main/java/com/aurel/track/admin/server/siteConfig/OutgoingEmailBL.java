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

package com.aurel.track.admin.server.siteConfig;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.mail.internet.InternetAddress;




import org.apache.commons.lang3.exception.ExceptionUtils;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TSiteBean;
import com.aurel.track.json.ControlError;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.emailHandling.IncomingMailSettings;
import com.aurel.track.util.emailHandling.MailSender;
import com.aurel.track.util.emailHandling.SMTPMailSettings;

/**
 * It manages handling of the server configuration for outgoing e-mail connections.
 *
 * @author Adrian Bojani
 */
public class OutgoingEmailBL {

	private static final Logger LOGGER = LogManager.getLogger(OutgoingEmailBL.class);
	
	/**
	 * This method returns a transfer object for the outgoing e-mail
	 * configuration (SMTP). The transfer object can be used to move
	 * data between the application layer and the user interface.
	 *
	 * @param siteBean contains all data for the server configuration
	 * @param locale the locale used for translating the radio group texts.
	 *
	 * @return the transfer object for the outgoing e-mail configuration
	 */
	static OutgoingEmailTO getOutgoingEmailTO(TSiteBean siteBean, Locale locale){
		OutgoingEmailTO outgoingEmailTO=new OutgoingEmailTO();
		outgoingEmailTO.setTrackEmail(siteBean.getTrackEmail());
		outgoingEmailTO.setEmailPersonalName(siteBean.getEmailPersonalName());
		outgoingEmailTO.setSendFromMode(siteBean.getUseTrackFromAddressDisplay());
		outgoingEmailTO.setMailEncoding(siteBean.getMailEncoding());
		outgoingEmailTO.setServerName(siteBean.getSmtpServerName());
		outgoingEmailTO.setSecurityConnection(siteBean.getSmtpSecurityConnection());
		outgoingEmailTO.setPort(siteBean.getSmtpPort());
		outgoingEmailTO.setReqAuth(siteBean.getBoolSmtpReqAuth());
		Integer authMode=TSiteBean.SMTP_AUTHENTICATION_MODES.CONNECT_USING_SMTP_SETTINGS;
		if(siteBean.getSmtpAuthMode()!=null){
			try{
				authMode=Integer.valueOf(siteBean.getSmtpAuthMode());
			}catch (Exception e){}
		}
		outgoingEmailTO.setAuthMode(authMode);
		outgoingEmailTO.setUser(siteBean.getSmtpUser());
		outgoingEmailTO.setAuthenticationModes(getSmtpAuthenticationModes(locale));
		outgoingEmailTO.setSecurityConnectionsModes(getSecurityConnectionsModes(locale));
		outgoingEmailTO.setSendFromModes(getSendFromModes(locale));
		return outgoingEmailTO;
	}

	public static SMTPMailSettings getSMTPMailSettings(TSiteBean site){
		SMTPMailSettings smtpMailSettings=new SMTPMailSettings();

		smtpMailSettings.setAuthMode(site.getIntSmtpAuthMode());
		smtpMailSettings.setHost(site.getSmtpServerName());
		smtpMailSettings.setPassword(site.getSmtpPassWord());
		if(site.getSmtpPort()!=null){
			smtpMailSettings.setPort(site.getSmtpPort());
		}
		smtpMailSettings.setReqAuth(site.getBoolSmtpReqAuth());
		smtpMailSettings.setSecurity(site.getSmtpSecurityConnection());
		smtpMailSettings.setUser(site.getSmtpUser());
		if (site.getMailEncoding() != null
				&& !"".equals(site.getMailEncoding().trim())){
			smtpMailSettings.setMailEncoding(site.getMailEncoding());
		}


		return smtpMailSettings;
	}


	/**
	 * Updates the site bean (server configuration bean) with new information from
	 * the outgoing e-mail configuration transfer object.
	 *
	 * @param siteBean contains all data for the server configuration
	 * @param outgoingEmailTO transfer object with SMTP server configuration data
	 *
	 */
	static void updateOutgoingEmail(TSiteBean siteBean, OutgoingEmailTO outgoingEmailTO) {
		siteBean.setEmailPersonalName(outgoingEmailTO.getEmailPersonalName());
		siteBean.setTrackEmail(outgoingEmailTO.getTrackEmail());
		siteBean.setSmtpServerName(outgoingEmailTO.getServerName());
		siteBean.setSmtpSecurityConnection(outgoingEmailTO.getSecurityConnection());
		siteBean.setSmtpReqAuth(Boolean.toString(outgoingEmailTO.isReqAuth()));
		if(outgoingEmailTO.getAuthMode()==null){
			siteBean.setSmtpAuthMode(Integer.toString(TSiteBean.SMTP_AUTHENTICATION_MODES.CONNECT_USING_SMTP_SETTINGS));
		}else{
			siteBean.setSmtpAuthMode(outgoingEmailTO.getAuthMode().toString());
		}
		siteBean.setSmtpUser(outgoingEmailTO.getUser());
		if (outgoingEmailTO.getPassword() != null && outgoingEmailTO.getPassword().trim().length() > 0) {
			siteBean.setSmtpPassWord(outgoingEmailTO.getPassword());
		}
		siteBean.setUseTrackFromAddressDisplay(outgoingEmailTO.getSendFromMode());
		siteBean.setMailEncoding(outgoingEmailTO.getMailEncoding());

		if (outgoingEmailTO.getPort() == null || outgoingEmailTO.getPort().intValue() < 1) {
			outgoingEmailTO.setPort(new Integer(25));  // set port to default value
		}
		siteBean.setSmtpPort(outgoingEmailTO.getPort());
	}

	static void validateOutgoingEmail(OutgoingEmailTO outgoingEmailTO,
			List<ControlError> errors, Locale locale) {
		if (outgoingEmailTO==null) {
			return;
		}
		if(outgoingEmailTO.getTrackEmail()==null || !SiteConfigBL.validateEmail(outgoingEmailTO.getTrackEmail())) {
			List<String> controlPath = new LinkedList<String>();
			controlPath.add(OutgoingEmailTO.JSONFIELDS.tabOutgoingEmail);
			controlPath.add(OutgoingEmailTO.JSONFIELDS.fsTrackEmail);
			controlPath.addAll(JSONUtility.getPathInHelpWrapper(OutgoingEmailTO.JSONFIELDS.trackEmail));
			errors.add(new ControlError(controlPath, SiteConfigBL.getText("admin.server.config.err.invalidSMTPEmail", locale)));
		}
		if(outgoingEmailTO.getServerName()==null || outgoingEmailTO.getTrackEmail().trim().length() < 1){
			List<String> controlPath = new LinkedList<String>();
			controlPath.add(OutgoingEmailTO.JSONFIELDS.tabOutgoingEmail);
			controlPath.add(OutgoingEmailTO.JSONFIELDS.fsSmtpServer);
			controlPath.addAll(JSONUtility.getPathInHelpWrapper(OutgoingEmailTO.JSONFIELDS.serverName));
			errors.add(new ControlError(controlPath, SiteConfigBL.getText("admin.server.config.err.invalidSMTPServerName", locale)));
		}
		if(outgoingEmailTO.getAuthMode()==null){
			outgoingEmailTO.setAuthMode(TSiteBean.SMTP_AUTHENTICATION_MODES.CONNECT_USING_SMTP_SETTINGS);
		}
		if (outgoingEmailTO.getPort() == null || outgoingEmailTO.getPort().intValue() < 1) {
			outgoingEmailTO.setPort(new Integer(25));  // set port to default value
		}
	}

	/**
	 * This routine tries to connect to the SMTP server with the current
	 * configuration parameters in <code>siteApp</code>, the <code>TSiteBean</code> object stored
	 * in the application context.
	 * @param siteBean
	 * @param errors
	 * @return the protocol of the connection attempt
	 */
	public static String testOutgoingEmail(TSiteBean siteBean,OutgoingEmailTO outgoingEmailTO,String emailTestTo, List<ControlError> errors,Locale locale){
		SiteConfigBL.LOGGER.debug("Test sending email to:"+emailTestTo);
		SMTPMailSettings smtpMailSettings=new SMTPMailSettings();
		smtpMailSettings.setReqAuth(outgoingEmailTO.isReqAuth());
		smtpMailSettings.setAuthMode(outgoingEmailTO.getAuthMode());
		smtpMailSettings.setHost(outgoingEmailTO.getServerName());
		smtpMailSettings.setSecurity(outgoingEmailTO.getSecurityConnection());
		smtpMailSettings.setPort(outgoingEmailTO.getPort());
		smtpMailSettings.setUser(outgoingEmailTO.getUser());
		smtpMailSettings.setPassword(outgoingEmailTO.getPassword()!=null&&outgoingEmailTO.getPassword().trim().length()>0?outgoingEmailTO.getPassword():siteBean.getSmtpPassWord());
		smtpMailSettings.setMailEncoding(outgoingEmailTO.getMailEncoding());

		
		InternetAddress internetAddressFrom=null;
		try{
			internetAddressFrom = new InternetAddress(outgoingEmailTO.getTrackEmail(),outgoingEmailTO.getEmailPersonalName());
		}catch (Exception ex){
			LOGGER.error(ExceptionUtils.getStackTrace(ex));
			List<String> controlPath = new LinkedList<String>();
			controlPath.add(OutgoingEmailTO.JSONFIELDS.tabOutgoingEmail);
			controlPath.add(OutgoingEmailTO.JSONFIELDS.fsTrackEmail);
			controlPath.addAll(JSONUtility.getPathInHelpWrapper(OutgoingEmailTO.JSONFIELDS.trackEmail));
			errors.add(new ControlError(controlPath, SiteConfigBL.getText("admin.server.config.err.invalidSMTPEmail", locale)));
		}
		InternetAddress internetAddressesTo=null;
		try{
			internetAddressesTo=new InternetAddress(emailTestTo);
		}catch (Exception ex){
			LOGGER.error(ExceptionUtils.getStackTrace(ex));
			List<String> controlPath = new LinkedList<String>();
			controlPath.add(OutgoingEmailTO.JSONFIELDS.tabOutgoingEmail);
			controlPath.add("fsSmtpTest");
			controlPath.add("emailTestTo");
			errors.add(new ControlError(controlPath, SiteConfigBL.getText("dmin.server.config.err.invalidSMTPEmailTest", locale)));
		}
		String subject= SiteConfigBL.getText("admin.server.config.trackEmailTestSubject", locale);
		String messageBody= SiteConfigBL.getText("admin.server.config.trackEmailTestBody", locale);
		boolean isPlain=true;
		MailSender mailSender=new MailSender(smtpMailSettings,internetAddressFrom, internetAddressesTo, subject, messageBody, isPlain);
		mailSender.setTimeout(new Integer(60000));
		boolean ok=mailSender.send();
		SiteConfigBL.LOGGER.debug("Test sending email ready");
		if(!ok){
			List<String> controlPath = new LinkedList<String>();
			controlPath.add(OutgoingEmailTO.JSONFIELDS.tabOutgoingEmail);
			controlPath.add(OutgoingEmailTO.JSONFIELDS.fsSmtpServer);
			controlPath.addAll(JSONUtility.getPathInHelpWrapper(OutgoingEmailTO.JSONFIELDS.serverName));
			errors.add(new ControlError(controlPath, "Can't send email"));
		}
		return "";
	}

	private static List<IntegerStringBean> getSmtpAuthenticationModes(Locale locale) {
		List<IntegerStringBean> smtpAuthenticationModesList = new ArrayList<IntegerStringBean>();
		String keyPrefix = "admin.server.config.smtpConnect.";
		smtpAuthenticationModesList.add(new IntegerStringBean(SiteConfigBL.getText(keyPrefix + "smtpSettings", locale),
				new Integer(TSiteBean.SMTP_AUTHENTICATION_MODES.CONNECT_USING_SMTP_SETTINGS)));
		smtpAuthenticationModesList.add(new IntegerStringBean(SiteConfigBL.getText(keyPrefix + "incomingMailSettings", locale),
				new Integer(TSiteBean.SMTP_AUTHENTICATION_MODES.CONNECT_WITH_SAME_SETTINGS_AS_INCOMING_MAIL_SERVER)));
		smtpAuthenticationModesList.add(new IntegerStringBean(SiteConfigBL.getText(keyPrefix + "connectToIncomingMailBeforeSend", locale),
				new Integer(TSiteBean.SMTP_AUTHENTICATION_MODES.CONNECT_TO_INCOMING_MAIL_SERVER_BEFORE_SENDING)));
		return smtpAuthenticationModesList;
	}

	public static List<IntegerStringBean> getSecurityConnectionsModes(Locale locale) {
		List<IntegerStringBean> securityConnectionsModes = new ArrayList<IntegerStringBean>();
		String keyPrefix = "admin.server.config.securityConnections.";
		securityConnectionsModes.add(new IntegerStringBean(SiteConfigBL.getText(keyPrefix + "never", locale),
				new Integer(TSiteBean.SECURITY_CONNECTIONS_MODES.NEVER)));
		securityConnectionsModes.add(new IntegerStringBean(SiteConfigBL.getText(keyPrefix + "tlsIsAvailable", locale),
				new Integer(TSiteBean.SECURITY_CONNECTIONS_MODES.TLS_IF_AVAILABLE)));
		securityConnectionsModes.add(new IntegerStringBean(SiteConfigBL.getText(keyPrefix + "tls", locale),
				new Integer(TSiteBean.SECURITY_CONNECTIONS_MODES.TLS)));
		securityConnectionsModes.add(new IntegerStringBean(SiteConfigBL.getText(keyPrefix + "ssl", locale),
				new Integer(TSiteBean.SECURITY_CONNECTIONS_MODES.SSL)));
		return securityConnectionsModes;
	}

	private static List<IntegerStringBean> getSendFromModes(Locale locale) {
		List<IntegerStringBean> sendFromModes = new ArrayList<IntegerStringBean>();
		String keyPrefix = "admin.server.config.sendFromMode.";
		sendFromModes.add(new IntegerStringBean(SiteConfigBL.getText(keyPrefix + "userFromAddress", locale),
				new Integer(TSiteBean.SEND_FROM_MODE.USER)));
		sendFromModes.add(new IntegerStringBean(SiteConfigBL.getText(keyPrefix + "systemFromAddress", locale),
				new Integer(TSiteBean.SEND_FROM_MODE.SYSTEM)));
		return sendFromModes;
	}


}
