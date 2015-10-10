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

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.Support;
import com.aurel.track.util.emailHandling.JavaMailBean;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *  This class provides the thread that runs the backup.
 */
class BackupThread  implements Runnable {

	private static final Logger LOGGER = LogManager.getLogger(BackupThread.class);

	private TPersonBean person;
	private ApplicationBean appBean;
	private PropertiesConfiguration torqueProperties;
	private String backupName;
	private boolean includeAttachments;
	private Locale locale;
	private StringBuilder messageBody;
	private boolean isDone = false;


	public BackupThread(ApplicationBean appBean, String backupName,
						boolean includeAttachments) {
		this(appBean, backupName, includeAttachments,null,null,null);
	}
	public BackupThread(ApplicationBean appBean, String backupName,
						boolean includeAttachments, TPersonBean person,
						Locale locale,StringBuilder messageBody) {
		super();
		this.appBean = appBean;
		try {
			this.torqueProperties = appBean.getDbConfig();
		}
		catch (Exception e) {
			LOGGER.debug(e.getMessage(),e);
		}
		this.backupName = backupName;
		this.includeAttachments = includeAttachments;
		this.person = person;
		this.locale=locale;
		this.messageBody=messageBody;
		LOGGER.debug("Initialized BackupThread...");
	}

	/**
	 *
	 * This the is thread that runs the backup.
	 *
	 */
	@Override
	public void run() {
		try {
			LOGGER.debug("Started BackupThread...");
			Date start = new Date();
			DatabaseBackupBL.zipDatabase(backupName,includeAttachments,torqueProperties);
			Date end = new Date();
			LOGGER.info("Backup took " + (end.getTime() - start.getTime() ) / 1000 + " seconds");
			if(person!=null){
				sendEmail(person,messageBody.toString());
			}
		} catch (DatabaseBackupBLException e) {
			if(LOGGER.isDebugEnabled()){
				e.printStackTrace();
			}
			LOGGER.error(Support.readStackTrace(e));
			StringBuffer message=new StringBuffer();
			if(e.getLocalizedKey()!=null){
				message.append(LocalizeUtil.getParametrizedString(e.getLocalizedKey(), e.getLocalizedParams(), locale));
			}else{
				message.append(e.getMessage());
			}
			if(e.getCause()!=null){
				message.append(": ").append(e.getCause().getMessage());
			}
			List<LabelValueBean> errors = new ArrayList<LabelValueBean>();
			errors.add(new LabelValueBean(message.toString(),""));
			appBean.setBackupErrors(errors);  // overwrite previous errors, if any

			LOGGER.debug("Something went wrong during backup: " + message);
			if(person!=null){
				sendEmail(person,message.toString());
			}
		}finally{
			appBean.setBackupInProgress(false);
		}
		isDone = true;
	}


	public boolean isDone() {
		return isDone;
	}

	private void sendEmail(TPersonBean person,String messageBody){
		JavaMailBean javaMailBean2 = JavaMailBean.getInstance();
		boolean isPlain=true;
		String subjectMail=LocalizeUtil.getLocalizedTextFromApplicationResources("admin.server.databaseBackup.lbl.subjectNotification",locale);
		javaMailBean2.sendMailInThread(person, subjectMail, person, messageBody, isPlain);
	}
}
