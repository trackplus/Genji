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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.server.siteConfig.SiteConfigBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.ControlError;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.Support;
import com.opensymphony.xwork2.ActionSupport;

/**
 * A Struts2 action used to backup the database and attachments
 * @author Adrian Bojani
 *
 */
public class DatabaseBackupAction extends ActionSupport implements ServletRequestAware,SessionAware,ApplicationAware{
	
	private static final Logger LOGGER = LogManager.getLogger(DatabaseBackupAction.class);
	
	private static final long serialVersionUID = 400L;
	private boolean autoBackup;
	private boolean includeAttachments;
	private boolean includeAttachmentsConf;
	private HttpServletRequest servletRequest;
	private Map application;
	private Map<String, Object> session;
	private Locale locale; 
	private String backupName;
	private String backupDir;
	private boolean backupSuccess;
	private boolean sendNotifyEmail;
	private List<Integer> backupOnDays;
	private List<LabelValueBean> backupOnDaysList;
	private String backupTime;
	private Integer noOfBackups;
	private List<LabelValueBean> errors;
	private boolean loadFirst = false;

	public String prepare() {
		locale = (Locale) session.get(Constants.LOCALE_KEY);
		Date now=new Date();
		backupName=DatabaseBackupBL.formatBackupName(now);
		includeAttachments=true;
		sendNotifyEmail=true;
		backupOnDays = new ArrayList<Integer>();
		backupOnDays.add(Integer.valueOf(1));

		// Get errors from last backup attempt, if any
		if (ApplicationBean.getInstance().getBackupErrors() != null) { 
			if (errors == null) {
				errors = ApplicationBean.getInstance().getBackupErrors();
			} else {
				errors.addAll(ApplicationBean.getInstance().getBackupErrors());
			}	
		}
		return null;
	}
	
	@Override
	public String execute() {
		loadFirst = true;
		ApplicationBean appBean = ApplicationBean.getInstance();
		if(appBean.isBackupInProgress()){
			loadFirst = false;
		}
		prepare();
		return createResponse();
	}
	
	/**
	 * Construct a proper JSON response string
	 * @return
	 */
	public String createResponse() {
		List<BackupTO> availableBackups=null;
		try{
			availableBackups=DatabaseBackupBL.getBackups();
		}catch (DatabaseBackupBLException ex){
			LabelValueBean er=new LabelValueBean();
			er.setLabel(getText(ex.getLocalizedKey(),locale));
			if(errors==null){
				errors=new ArrayList<LabelValueBean>();
			}
			errors.add(er);
		}
		backupOnDaysList = DateTimeUtils.getDaysOfTheWeek(locale,false);
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		if (errors != null && !errors.isEmpty() && !loadFirst) {
			JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, false);
		} else {
			JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		}
		JSONUtility.appendErrorsToHTMLList(sb, "msg", errors, false);
		sb.append("data:{");
		JSONUtility.appendStringValue(sb,"backupName",backupName);
		JSONUtility.appendStringValue(sb,"backupDir", ApplicationBean.getInstance().getSiteBean().getBackupDir());
		JSONUtility.appendBooleanValue(sb, "autoBackup", ApplicationBean.getInstance().getSiteBean().getIsDatabaseBackupJobOn());
		JSONUtility.appendLabelValueBeanList(sb, "backupOnDaysList", backupOnDaysList);
		JSONUtility.appendIntegerListAsArray(sb, "backupOnDays", ApplicationBean.getInstance().getSiteBean().getBackupOnDays());
		JSONUtility.appendStringValue(sb, "backupTime", ApplicationBean.getInstance().getSiteBean().getBackupTime());
		JSONUtility.appendIntegerValue(sb, "noOfBackups", ApplicationBean.getInstance().getSiteBean().getNoOfBackups());
		JSONUtility.appendBooleanValue(sb, "includeAttachmentsConf", ApplicationBean.getInstance().getSiteBean().getIncludeAttachments());
		JSONUtility.appendJSONValue(sb,"availableBackups",BackupJSON.encodeJSONBackupList(availableBackups),true);
		sb.append("}");
		sb.append("}");
		
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.println(sb);
		} catch (IOException e) {
			LOGGER.error(Support.readStackTrace(e));
		}
		return null;
	}
	
	/**
	 * Get a list with all available backups
	 * @return
	 */
	public String getAvailableBackups(){
		List<BackupTO> availableBackups=null;
		try{
			availableBackups=DatabaseBackupBL.getBackups();
		}catch (DatabaseBackupBLException ex){
			LOGGER.error(ExceptionUtils.getStackTrace(ex));
		}
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.println(BackupJSON.encodeJSONBackupList(availableBackups));
		} catch (IOException e) {
			LOGGER.error(Support.readStackTrace(e));
		}
		return null;
	}

	/**
	 * Start the backup background thread
	 * @return
	 */
	public String backup(){
		loadFirst = false;
		errors = new ArrayList<LabelValueBean>();
		ApplicationBean.getInstance().setBackupErrors(errors); // reset error list
		
		ApplicationBean appBean = ApplicationBean.getInstance();
		synchronized (appBean) {
			if(appBean.isBackupInProgress()){
				errors.add(new LabelValueBean(getText("admin.server.databaseBackup.error.backupInProgress"),""));
				backupSuccess=false;
				return createResponse();
			}
			appBean.setBackupInProgress(true);

			BackupThread backupThread;
			if(!sendNotifyEmail){
				backupThread=new BackupThread(appBean,backupName,includeAttachments);
			}else{
				Locale locale=(Locale) session.get(Constants.LOCALE_KEY);
				TPersonBean person=(TPersonBean) session.get(Constants.USER_KEY);
				StringBuilder messageBody=new StringBuilder();
				messageBody.append(getText("admin.server.databaseBackup.lbl.backupSuccessfully"));
				messageBody.append("\n\n");
				messageBody.append(getText("admin.server.databaseBackup.lbl.backupName")).append(":");
				messageBody.append(" ").append(backupName).append(".zip").append("\n\n");
				messageBody.append(getText("admin.server.databaseBackup.lbl.includeAttachments")).append(":");
				messageBody.append(" ").append(includeAttachments).append("\n\n");

				backupThread=new BackupThread(appBean,backupName,includeAttachments,person,locale,messageBody);
			}

			// Catch early errors
			Object monitor = new Object();
			try {
				new Thread(backupThread).start();
				boolean backupIsRunning = !backupThread.isDone();
				int i = 0;

				synchronized (monitor) {
					while(backupIsRunning && i < 5){
						++i;
						monitor.wait(1000);
						backupIsRunning = !backupThread.isDone();
					}
				}
			}
			catch(InterruptedException ie){
				LOGGER.error(ie.getMessage(),ie);
			}

			backupSuccess=ApplicationBean.getInstance().getBackupErrors() == null||ApplicationBean.getInstance().getBackupErrors().isEmpty();
			prepare();
			return createResponse();
		}
	}
	
	/**
	 * Save the automated backup configuration
	 * @return
	 */
	public String saveConfig() {
		loadFirst = false;
		locale = (Locale) session.get(Constants.LOCALE_KEY);
		// Delete all previous errors
		errors = new ArrayList<LabelValueBean>();
		ApplicationBean.getInstance().getSiteBean().setIsDatabaseBackupJobOn(autoBackup);
		if (!autoBackup) {
			return createResponse();
		}		
		List<ControlError> tmperr = new LinkedList<ControlError>();
		String errorMessage = SiteConfigBL.createAndCheckDirectory(backupDir, locale);
		if (errorMessage!=null) {
			errors.add(new LabelValueBean(errorMessage, "backupDir"));
		}else{
			ApplicationBean.getInstance().getSiteBean().setBackupDir(backupDir);
		}
		ApplicationBean.getInstance().getSiteBean().setIncludeAttachments(includeAttachmentsConf);
		ApplicationBean.getInstance().getSiteBean().setBackupOnDays(backupOnDays);
		ApplicationBean.getInstance().getSiteBean().setBackupTime(backupTime);
		ApplicationBean.getInstance().getSiteBean().setNoOfBackups(noOfBackups);
		SiteConfigBL.saveTSite(ApplicationBean.getInstance().getSiteBean(), 
							   ApplicationBean.getInstance(), tmperr, application);
		if (errors.isEmpty()){
			if (!DatabaseBackupBL.setDBJobCronExpression(backupTime,backupOnDays)) {
				errors.add(new LabelValueBean(getText("admin.server.databaseBackup.err.cronBad", locale), 
						"backupTime"));
			}
		}
		return createResponse();
	}
	

	private static String getText(String s, Locale locale){
		return LocalizeUtil.getLocalizedTextFromApplicationResources(s, locale);
	}
	

	
	public boolean isIncludeAttachments() {
		return includeAttachments;
	}
	public void setIncludeAttachments(boolean includeAttachments) {
		this.includeAttachments = includeAttachments;
	}

	public HttpServletRequest getServletRequest() {
		return servletRequest;
	}
	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
	}
	public String getBackupName() {
		return backupName;
	}
	public void setBackupName(String backupName) {
		this.backupName = backupName;
	}
	public boolean isBackupSuccess() {
		return backupSuccess;
	}
	public void setBackupSuccess(boolean backupSuccess) {
		this.backupSuccess = backupSuccess;
	}
	public Map getApplication() {
		return application;
	}
	public void setApplication(Map application) {
		this.application = application;
	}
	public boolean isSendNotifyEmail() {
		return sendNotifyEmail;
	}
	public void setSendNotifyEmail(boolean sendNotifyEmail) {
		this.sendNotifyEmail = sendNotifyEmail;
	}
	public Map<String, Object> getSession() {
		return session;
	}
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	public List<Integer> getBackupOnDays() {
		return backupOnDays;
	}
	
	public void setBackupOnDays(List<Integer> backupOnDays) {
		this.backupOnDays = backupOnDays;
	}
	
	public List<LabelValueBean> getBackupOnDaysList() {
		return backupOnDaysList;
	}
	
	public void setBackupOnDaysList(List<LabelValueBean> backupOnDaysList) {
		this.backupOnDaysList = backupOnDaysList;
	}
	
	public String getBackupDir() {
		return this.backupDir;
	}
	
	public void setBackupDir(String dir) {
		this.backupDir = dir;
	}
	
	public String getBackupTime() {
		return backupTime;
	}

	public void setBackupTime(String backupTime) {
		this.backupTime = backupTime;
	}
	
	public boolean isAutoBackup() {
		return autoBackup;
	}


	public void setAutoBackup(boolean automatedBackup) {
		this.autoBackup = automatedBackup;
	}


	public boolean isIncludeAttachmentsConf() {
		return includeAttachmentsConf;
	}


	public void setIncludeAttachmentsConf(boolean includeAttachmentsConf) {
		this.includeAttachmentsConf = includeAttachmentsConf;
	}


	public Integer getNoOfBackups() {
		return noOfBackups;
	}


	public void setNoOfBackups(Integer noOfBackups) {
		this.noOfBackups = noOfBackups;
	}
	
}
