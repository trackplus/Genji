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

package com.aurel.track.admin.server.dbbackup;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.dbase.HandleHome;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.Support;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class DatabaseRestoreAction extends ActionSupport implements Preparable, SessionAware,ServletRequestAware {
	
	private static final Logger LOGGER = LogManager.getLogger(DatabaseRestoreAction.class);
	
	private static final long serialVersionUID = 400L;
	private HttpServletRequest servletRequest;
	private Map<String, Object> session;
	private Locale locale;
	private boolean includeAttachments;
	private String includeAttachmentsTooltip;
	private String driverClassName;
	private String url;
	private String user;
	private String password;
	private boolean restoreSuccess;
	
	private List<BackupTO> backups;
	private String backupFile;
	private String attachmentDir;
	
	private boolean sendNotifyEmail;
	
	PropertiesConfiguration torqueProperties;

	public void prepare() throws Exception {
		locale = (Locale) session.get(Constants.LOCALE_KEY);
		try {
			torqueProperties = HandleHome.getTorqueProperties(ServletActionContext.getServletContext(), false);
		} catch (Exception e) {
			LOGGER.error("Problem reading Torque.properties");
		}
		try {
			backups=DatabaseBackupBL.getBackups();
			includeAttachmentsTooltip=getText("datbaseBackup.prompt.includeAttachments.tt");
			sendNotifyEmail=true;
		} catch (Exception e) {
			LOGGER.error(Support.readStackTrace(e));
		}
	}
	
	
	@Override
	public String execute() {
		if(backups!=null && !backups.isEmpty()){
			backupFile=backups.get(0).getFile().getName();
		}
		driverClassName=(String) torqueProperties.getProperty("torque.dsfactory.track.connection.driver");
		url=(String) torqueProperties.getProperty("torque.dsfactory.track.connection.url");
		user=(String) torqueProperties.getProperty("torque.dsfactory.track.connection.user");
		password=(String) torqueProperties.getProperty("torque.dsfactory.track.connection.password");
		StringBuilder sb=new StringBuilder();
		try {
			sb.append("{");
			JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
			sb.append("data:{");
			JSONUtility.appendStringValue(sb, "driverClassName", driverClassName);
			JSONUtility.appendStringValue(sb, "turl", url);
			JSONUtility.appendStringValue(sb, "user", user);
			JSONUtility.appendStringValue(sb, "password", password);
			JSONUtility.appendStringValue(sb, "backupDir", getBackupDir());
			JSONUtility.appendJSONValue(sb,"availableBackups",BackupJSON.encodeJSONBackupList(backups),true);
			sb.append("}}");

			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.println(sb);
		} catch (IOException e) {
			LOGGER.error(Support.readStackTrace(e));
		}
		return null;
	}
	
	/**
	 * Tests the connection parameters for the database into which the backup is to be restored.
	 * @return nothing, writes directly into the output stream
	 */
	public String testConnection() {

		List<LabelValueBean> errors = new ArrayList<LabelValueBean>();
		
		errors = checkConnectionParameters(errors);

		if (errors.isEmpty()) {
			JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		} else {
			LOGGER.debug("We got validation errors when testing the database restore connection");
			JSONUtility.encodeJSONErrorsExtJS(ServletActionContext.getResponse(), errors);
		}
		return null;
	}

	/**
	 * Restore a database from a backup file.
	 * @return nothing, writes directly into output stream
	 */
	public String restore(){
		List<LabelValueBean> errors = new ArrayList<LabelValueBean>();
		
		errors = checkConnectionParameters(errors);

		if (!errors.isEmpty()) {
			LOGGER.debug("We got validation errors when testing the database restore connection");
			JSONUtility.encodeJSONErrorsExtJS(ServletActionContext.getResponse(), errors);
			return null;
		}

		restoreSuccess=true;
		TPersonBean person=null;
		StringBuilder messageBody=new StringBuilder();
		if(sendNotifyEmail){
			person=(TPersonBean) session.get(Constants.USER_KEY);
			messageBody.append(getText("admin.server.databaseBackup.lbl.backupSuccessfully"));
			messageBody.append("\n\n");
			messageBody.append(getText("admin.server.databaseBackup.lbl.backupName")).append(":");
			messageBody.append(" ").append(backupFile).append("\n\n");
		
			messageBody.append(getText("admin.server.databaseRestore.lbl.driverClassName")).append(":");
			messageBody.append(" ").append(driverClassName).append("\n\n");
			
			messageBody.append(getText("admin.server.databaseRestore.lbl.url")).append(":");
			messageBody.append(" ").append(url).append("\n\n");
			
			messageBody.append(getText("admin.server.databaseRestore.lbl.user")).append(":");
			messageBody.append(" ").append(user).append("\n\n");
			
			messageBody.append(getText("admin.server.databaseBackup.lbl.includeAttachments")).append(":");
			messageBody.append(" ").append(includeAttachments).append("\n\n");
			
			messageBody.append(getText("admin.server.databaseRestore.lbl.attachmentRestoreDir")).append(":");
			messageBody.append(" ").append(attachmentDir).append("\n\n");
		}


		ApplicationBean appBean = ApplicationBean.getInstance();
		synchronized (appBean) {
			if(appBean.isRestoreInProgress()){
				errors.add(new LabelValueBean(getText("admin.server.databaseRestore.err.restoreInProgress"),"errorMessage"));
				JSONUtility.encodeJSONErrorsExtJS(ServletActionContext.getResponse(), errors);
				return null;
			}
			appBean.setRestoreInProgress(true);
			RestoreThread restoreThread=new RestoreThread(appBean,attachmentDir,backupFile,includeAttachments,driverClassName,locale,
					password,person,url,user,messageBody);
			new Thread(restoreThread).start();

			JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
			return null;
		}
	}
	
	/**
	 * Deletes a backup from the file system.
	 * @return nothing, writes directly into output stream
	 */
	public String delete(){
		List<LabelValueBean> errors = new ArrayList<LabelValueBean>();
		
		String fs = ApplicationBean.getInstance().getSiteBean().getBackupDir()
		+ File.separator + getBackupFile();
		
		File f = new File (fs);
		boolean success = false;
		try {
			success = f.delete();
			if (!success) {
				errors.add(new LabelValueBean(getText("admin.server.databaseRestore.err.noDelete", locale),"errorMessage"));
			}
		}
		catch (Exception e) {
			LOGGER.error(Support.readStackTrace(e));
			errors.add(new LabelValueBean(e.getMessage(), "errorList"));
		}

		if (errors.isEmpty()) {
			StringBuilder sb=new StringBuilder();
			sb.append("{");
			JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
			try{
				backups=DatabaseBackupBL.getBackups();
			}catch (Exception ex){
				backups=new ArrayList<BackupTO>();
			}
			JSONUtility.appendJSONValue(sb,"data",BackupJSON.encodeJSONBackupList(backups),true);
			sb.append("}");

			try {
				JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
				PrintWriter out = ServletActionContext.getResponse().getWriter();
				out.println(sb);
			} catch (IOException e) {
				LOGGER.error(Support.readStackTrace(e));
			}

		} else {
			LOGGER.debug("We got validation errors when testing the database restore connection");
			JSONUtility.encodeJSONErrors(ServletActionContext.getResponse(), errors);
		}
		return null;
	}
	
	/*
	 * Check whatever can be checked before we start...
	 */
	private List<LabelValueBean> checkConnectionParameters(List<LabelValueBean> errors) {
		
		String appDatabaseURL=(String) torqueProperties.getProperty("torque.dsfactory.track.connection.url");
		if (appDatabaseURL.indexOf('?') > 0) {  // remove the parameters
			appDatabaseURL = appDatabaseURL.substring(0,appDatabaseURL.indexOf('?'));
		}
		String urlToCheck=url;
		if (url.indexOf('?') > 0) {  // remove the parameters
			urlToCheck = url.substring(0,url.indexOf('?'));
		}
		if(appDatabaseURL.equalsIgnoreCase(urlToCheck)){
			errors.add(new LabelValueBean(getText("admin.server.databaseRestore.err.restoreOnSameDatabase", locale),"turl"));
			return errors;
		}

		errors.addAll(DatabaseBackupBL.checkConnection(driverClassName, url, user, password, locale));

		if(includeAttachments){
			if(attachmentDir.equals(HandleHome.getTrackplus_Home())){
				errors.add(new LabelValueBean(getText("admin.server.databaseRestore.err.attachmentRestoreDirAsCurrent", locale),"attachmentDir"));
			}

			try {			
				File f = new File (attachmentDir);
				if (!f.exists()&&!f.mkdirs()) {
					errors.add(new LabelValueBean(getText("admin.server.databaseRestore.err.attachmentRestoreDirNotCreated", locale), "attachmentDir"));
				}
			}
			catch (Exception e) {
				LOGGER.error(Support.readStackTrace(e));
				errors.add(new LabelValueBean(e.getMessage(), "attachmentDir"));
			}
		}
		
		return errors;
	}

	

	
	private String getText(String s, Locale locale){
		return LocalizeUtil.getLocalizedTextFromApplicationResources(s, locale);
	}
	
	public boolean isIncludeAttachments() {
		return includeAttachments;
	}
	public void setIncludeAttachments(boolean includeAttachments) {
		this.includeAttachments = includeAttachments;
	}

	public String getIncludeAttachmentsTooltip() {
		return includeAttachmentsTooltip;
	}

	public void setIncludeAttachmentsTooltip(String includeAttachmentsTooltip) {
		this.includeAttachmentsTooltip = includeAttachmentsTooltip;
	}

	public HttpServletRequest getServletRequest() {
		return servletRequest;
	}

	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
	}
	public String getDriverClassName() {
		return driverClassName;
	}
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	public String getTurl() {
		return url;
	}
	public void setTurl(String url) {
		this.url = url;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public void setSession(Map<String, Object> session) {
		this.session=session;
	}
	
	public Map<String, Object> getSession() {
		return session;
	}
	
	public boolean isRestoreSuccess() {
		return restoreSuccess;
	}
	public void setRestoreSuccess(boolean restoreSuccess) {
		this.restoreSuccess = restoreSuccess;
	}
	public List<BackupTO> getBackups() {
		return backups;
	}
	public void setBackups(List<BackupTO> backups) {
		this.backups = backups;
	}
	public String getBackupFile() {
		return backupFile;
	}
	public void setBackupFile(String backupFile) {
		this.backupFile = backupFile;
	}
	public String getAttachmentDir() {
		return attachmentDir;
	}
	public void setAttachmentDir(String attachmentDir) {
		this.attachmentDir = attachmentDir;
	}
	public boolean isSendNotifyEmail() {
		return sendNotifyEmail;
	}
	public void setSendNotifyEmail(boolean sendNotifyEmail) {
		this.sendNotifyEmail = sendNotifyEmail;
	}

	public String getBackupDir() {
		return ApplicationBean.getInstance().getSiteBean().getBackupDir();

	}

}
