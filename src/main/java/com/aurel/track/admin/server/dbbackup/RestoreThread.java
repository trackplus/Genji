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

package com.aurel.track.admin.server.dbbackup;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.emailHandling.JavaMailBean;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.Date;
import java.util.Locale;

/**
 */
class RestoreThread implements Runnable{

	private static final Logger LOGGER = LogManager.getLogger(RestoreThread.class);

	private TPersonBean person;
	private Locale locale;
	private String backupFile, driverClassName, url, user, password,attachmentDir;
	private StringBuilder messageBody;
	private boolean includeAttachments;
	private ApplicationBean appBean;
	public RestoreThread(ApplicationBean appBean, String attachmentDir, String backupFile,boolean includeAttachments,
						 String driverClassName, Locale locale, String password,
						 TPersonBean person, String url, String user,StringBuilder messageBody) {
		super();
		this.appBean=appBean;
		this.attachmentDir = attachmentDir;
		this.backupFile = backupFile;
		this.includeAttachments=includeAttachments;
		this.driverClassName = driverClassName;
		this.locale = locale;
		this.password = password;
		this.person = person;
		this.url = url;
		this.user = user;
		this.messageBody=messageBody;
		LOGGER.debug("Initialized RestoreThread...");
	}

	@Override
	public void run() {
		try {
			LOGGER.info("Starting RestoreThread...");
			Date start = new Date();
			if(includeAttachments){
				DatabaseBackupBL.restoreDatabase(backupFile,driverClassName, url, user, password,attachmentDir);
			}else{
				DatabaseBackupBL.restoreDatabase(backupFile, driverClassName, url, user, password,null);
			}
			Date end = new Date();
			LOGGER.info("Restore took " + (end.getTime() - start.getTime() ) / 1000 + " seconds");
			if(person!=null){
				LOGGER.debug("Sending email to:"+person.getFullName());
				sendEmail(person,messageBody.toString());
			}

		} catch (DatabaseBackupBLException e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			LOGGER.error("Something went wrong during restore: " + e.getMessage());
			StringBuffer message=new StringBuffer();
			if(e.getLocalizedKey()!=null){
				message.append(LocalizeUtil.getParametrizedString(e.getLocalizedKey(), e.getLocalizedParams(), locale));
			}else{
				message.append(e.getMessage());
			}
			if(e.getCause()!=null){
				message.append(":").append(e.getCause().getMessage());
			}
			if(person!=null){
				sendEmail(person,message.toString());
			}
		}finally{
			appBean.setRestoreInProgress(false);
		}

	}

	private void sendEmail(TPersonBean person,String messageBody){
		JavaMailBean javaMailBean2 = JavaMailBean.getInstance();
		boolean isPlain=true;
		String subjectMail=LocalizeUtil.getLocalizedTextFromApplicationResources("admin.server.databaseRestore.lbl.subjectNotification",locale);
		javaMailBean2.sendMailInThread(person, subjectMail, person, messageBody, isPlain);
	}
}
