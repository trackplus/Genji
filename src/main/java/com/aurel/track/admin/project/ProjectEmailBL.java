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

package com.aurel.track.admin.project;

import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.json.ControlError;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.emailHandling.MailReader;
import com.aurel.track.util.PropertiesHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 */
public class ProjectEmailBL {
	private ProjectEmailBL(){
	}

	public static ProjectEmailTO createProjectEmailTO(TProjectBean projectBean) {
		ProjectEmailTO projectEmailTO;
		projectEmailTO = new ProjectEmailTO();
		projectEmailTO.setEnabled(PropertiesHelper.getBooleanProperty(projectBean.getMoreProps(),
				TProjectBean.MOREPPROPS.EMAIL_ENABLED));
		projectEmailTO.setProtocol(ProjectConfigBL.EMAIL_PROTOCOL.POP3);
		projectEmailTO.setSecurityConnection(Integer.valueOf(TSiteBean.SECURITY_CONNECTIONS_MODES.NEVER));
		Integer port = Integer.valueOf(ProjectConfigBL.EMAIL_DEFAULT_PORTS.POP3);
		projectEmailTO.setPort(port);
		return projectEmailTO;
	}
	public static ProjectEmailTO getProjectEmailTO(TProjectBean projectBean){
		ProjectEmailTO projectEmailTO=new ProjectEmailTO();
		Properties moreProps=projectBean.getMoreProperties();
		projectEmailTO.setEnabled(PropertiesHelper.getBooleanProperty(projectBean.getMoreProps(),
				TProjectBean.MOREPPROPS.EMAIL_ENABLED));
		String protocol = PropertiesHelper.getProperty(moreProps, TProjectBean.MOREPPROPS.RECEIVING_PROTOCOL_PROPERTY);
		if (protocol==null) {
			protocol = ProjectConfigBL.EMAIL_PROTOCOL.POP3;
		}
		projectEmailTO.setProtocol(protocol);
		projectEmailTO.setServerName(PropertiesHelper.getProperty(moreProps, TProjectBean.MOREPPROPS.RECEIVING_SERVER_PROPERTY));
		Integer securityConnection = PropertiesHelper.getIntegerProperty(moreProps, TProjectBean.MOREPPROPS.RECEIVING_SECURITY_PROPERTY);
		if (securityConnection==null) {
			securityConnection = Integer.valueOf(TSiteBean.SECURITY_CONNECTIONS_MODES.NEVER);
		}
		projectEmailTO.setSecurityConnection(securityConnection);
		Integer port = PropertiesHelper.getIntegerProperty(moreProps, TProjectBean.MOREPPROPS.RECEIVING_PORT_PROPERTY);
		if (port==null){
			if (protocol.equals(ProjectConfigBL.EMAIL_PROTOCOL.POP3)){
				if (securityConnection.intValue()==TSiteBean.SECURITY_CONNECTIONS_MODES.SSL){
					port = Integer.valueOf(ProjectConfigBL.EMAIL_DEFAULT_PORTS.POP3_SSL);
				} else {
					port = Integer.valueOf(ProjectConfigBL.EMAIL_DEFAULT_PORTS.POP3);
				}
			}
			if (protocol.equals(ProjectConfigBL.EMAIL_PROTOCOL.IMAP)){
				if (securityConnection.intValue()==TSiteBean.SECURITY_CONNECTIONS_MODES.SSL){
					port = Integer.valueOf(ProjectConfigBL.EMAIL_DEFAULT_PORTS.IMAP_SSL);
				}else{
					port = Integer.valueOf(ProjectConfigBL.EMAIL_DEFAULT_PORTS.IMAP);
				}
			}
		}
		projectEmailTO.setPort(port);
		projectEmailTO.setUser(PropertiesHelper.getProperty(moreProps, TProjectBean.MOREPPROPS.RECEIVING_USER_PROPERTY));
		projectEmailTO.setPassword(PropertiesHelper.getProperty(moreProps, TProjectBean.MOREPPROPS.RECEIVING_PASSWORD_PROPERTY));
		projectEmailTO.setKeepMessagesOnServer(PropertiesHelper.getBooleanProperty(moreProps, TProjectBean.MOREPPROPS.KEEP_MESSAGES_ON_SERVER));
		projectEmailTO.setProjectFromEmail(PropertiesHelper.getProperty(moreProps, TProjectBean.MOREPPROPS.TRACK_EMAIL_PROPERTY));
		projectEmailTO.setProjectFromEmailName(PropertiesHelper.getProperty(moreProps, TProjectBean.MOREPPROPS.EMAIL_PERSONAL_NAME));
		projectEmailTO.setSendFromProjectEmail(PropertiesHelper.getBooleanProperty(moreProps, TProjectBean.MOREPPROPS.USE_TRACK_FROM_ADDRESS_DISPLAY));
		projectEmailTO.setSendFromProjectAsReplayTo(PropertiesHelper.getBooleanProperty(moreProps, TProjectBean.MOREPPROPS.USE_TRACK_FROM_AS_REPLAY_TO));
		return projectEmailTO;
	}


	public static void verifyProjectEmail(Integer projectID,ProjectEmailTO projectEmailTO,List<ControlError> errors,Locale locale ){
		TProjectBean projectBean;
		if(projectID!=null){
			projectBean = ProjectBL.loadByPrimaryKey(projectID);
		}else{
			projectBean=new TProjectBean();
		}
		verifyProjectEmail(projectBean,projectEmailTO,errors,locale);
	}
	public static void verifyProjectEmail(TProjectBean projectBean,ProjectEmailTO projectEmailTO,List<ControlError> errors,Locale locale ){
		final String emailTabStr = "emailTab";
		final String fieldSetServerStr = "fseserv";

		if(projectEmailTO.getServerName() == null || projectEmailTO.getServerName().trim().length()==0) {
			List<String> controlPath = new LinkedList<String>();
			controlPath.add(emailTabStr);
			controlPath.add(fieldSetServerStr);
			controlPath.addAll(JSONUtility.getPathInHelpWrapper("projectEmailTO.serverName"));
			errors.add(new ControlError(controlPath,
					LocalizeUtil.getLocalizedTextFromApplicationResources("admin.server.config.err.invalidPOPServerName", locale)));
			return;
		}
		Integer popPort = projectEmailTO.getPort();
		if (popPort == null || popPort.intValue() < 1) {
			if("pop3".equals(projectEmailTO.getProtocol())){
				projectEmailTO.setPort(Integer.valueOf(110));// set port to default value
			}
			if("imap".equals(projectEmailTO.getProtocol())){
				projectEmailTO.setPort(Integer.valueOf(143));// set port to default value
			}
		}
		String password=projectEmailTO.getPassword();
		if((password==null||password.trim().length()==0)&&projectBean!=null){
			password=PropertiesHelper.getProperty(projectBean.getMoreProperties(), TProjectBean.MOREPPROPS.RECEIVING_PASSWORD_PROPERTY);
		}

		String err = MailReader.verifyMailSetting(projectEmailTO.getProtocol(),
				projectEmailTO.getServerName(),
				projectEmailTO.getSecurityConnection(),
				projectEmailTO.getPort(),
				projectEmailTO.getUser(),
				password);
		if (err != null) {
			final String fieldSetAuthStr = "fsauth";

			List<String> controlPath = new LinkedList<String>();
			controlPath.add(emailTabStr);
			controlPath.add(fieldSetServerStr);
			controlPath.addAll(JSONUtility.getPathInHelpWrapper(ProjectEmailJSON.JSON_FIELDS.SERVER_NAME));
			String localizedErrorMessage = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.server.config.err.invalidMailSettings", locale);
			errors.add(new ControlError(controlPath,localizedErrorMessage));

			controlPath = new LinkedList<String>();
			controlPath.add(emailTabStr);
			controlPath.add(fieldSetServerStr);
			controlPath.addAll(JSONUtility.getPathInHelpWrapper(ProjectEmailJSON.JSON_FIELDS.PORT));
			errors.add(new ControlError(controlPath,localizedErrorMessage));

			controlPath = new LinkedList<String>();
			controlPath.add(emailTabStr);
			controlPath.add(fieldSetAuthStr);
			controlPath.addAll(JSONUtility.getPathInHelpWrapper(ProjectEmailJSON.JSON_FIELDS.USER));
			errors.add(new ControlError(controlPath,localizedErrorMessage));

			controlPath = new LinkedList<String>();
			controlPath.add(emailTabStr);
			controlPath.add(fieldSetAuthStr);
			controlPath.addAll(JSONUtility.getPathInHelpWrapper(ProjectEmailJSON.JSON_FIELDS.PASSWORD));
			errors.add(new ControlError(controlPath,localizedErrorMessage));
		}
	}


	public static void saveProjectEmailToProperties(ProjectEmailTO projectEmailTO, Properties properties) {
		if(projectEmailTO==null || !projectEmailTO.isSendFromProjectEmail()){
			PropertiesHelper.setBooleanProperty(properties, TProjectBean.MOREPPROPS.USE_TRACK_FROM_ADDRESS_DISPLAY, false);
		}else{
			updateProjectNotificationEmail(properties, projectEmailTO);
		}
		if(projectEmailTO==null || !projectEmailTO.isEnabled()){
			PropertiesHelper.setBooleanProperty(properties, TProjectBean.MOREPPROPS.EMAIL_ENABLED, false);
		}else{
			updateProjectEmail(properties,projectEmailTO);
		}
	}


	private static void  updateProjectEmail(Properties properties,ProjectEmailTO projectEmailTO){
		PropertiesHelper.setBooleanProperty(properties,TProjectBean.MOREPPROPS.EMAIL_ENABLED, projectEmailTO.isEnabled());
		PropertiesHelper.setProperty(properties,TProjectBean.MOREPPROPS.RECEIVING_PROTOCOL_PROPERTY, projectEmailTO.getProtocol());
		PropertiesHelper.setProperty(properties,TProjectBean.MOREPPROPS.RECEIVING_SERVER_PROPERTY, projectEmailTO.getServerName());
		PropertiesHelper.setIntegerProperty(properties,TProjectBean.MOREPPROPS.RECEIVING_SECURITY_PROPERTY, projectEmailTO.getSecurityConnection());
		PropertiesHelper.setIntegerProperty(properties,TProjectBean.MOREPPROPS.RECEIVING_PORT_PROPERTY, projectEmailTO.getPort());
		PropertiesHelper.setProperty(properties,TProjectBean.MOREPPROPS.RECEIVING_USER_PROPERTY, projectEmailTO.getUser());
		if(projectEmailTO.getPassword()!=null||projectEmailTO.getPassword().trim().length()==0){
			PropertiesHelper.setProperty(properties,TProjectBean.MOREPPROPS.RECEIVING_PASSWORD_PROPERTY, projectEmailTO.getPassword());
		}
		PropertiesHelper.setBooleanProperty(properties,TProjectBean.MOREPPROPS.KEEP_MESSAGES_ON_SERVER, projectEmailTO.isKeepMessagesOnServer());

	}

	private static  void updateProjectNotificationEmail(Properties properties,ProjectEmailTO projectEmailTO){
		PropertiesHelper.setProperty(properties,TProjectBean.MOREPPROPS.TRACK_EMAIL_PROPERTY, projectEmailTO.getProjectFromEmail());
		PropertiesHelper.setProperty(properties,TProjectBean.MOREPPROPS.EMAIL_PERSONAL_NAME, projectEmailTO.getProjectFromEmailName());
		PropertiesHelper.setBooleanProperty(properties,TProjectBean.MOREPPROPS.USE_TRACK_FROM_ADDRESS_DISPLAY, projectEmailTO.isSendFromProjectEmail());
		PropertiesHelper.setBooleanProperty(properties,TProjectBean.MOREPPROPS.USE_TRACK_FROM_AS_REPLAY_TO, projectEmailTO.isSendFromProjectAsReplayTo());
	}

}
