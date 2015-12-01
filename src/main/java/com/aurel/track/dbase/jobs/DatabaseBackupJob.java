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

package com.aurel.track.dbase.jobs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;

import com.aurel.track.admin.server.dbbackup.DatabaseBackupBL;
import com.aurel.track.admin.server.dbbackup.DatabaseBackupBLException;
import com.aurel.track.cluster.ClusterBL;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.util.Support;

public class DatabaseBackupJob implements Job {

	private static final Logger LOGGER = LogManager.getLogger(DatabaseBackupJob.class);

	@Override
	public void execute(JobExecutionContext context) {
		LOGGER.info("DataBaseBackupJob was triggered at " + new Date());
		if (!ClusterBL.getIAmTheMaster()) {
			return;
		}
		LOGGER.info("Execute DatabaseBackupJob at " + new Date() + "...");
		if(!ApplicationBean.getInstance().getSiteBean().getIsDatabaseBackupJobOn()){
			LOGGER.info("Database backup job config is off! Exit DatabaseBackupJob.");
			return;
		}
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();

		Scheduler scheduler = context.getScheduler();

		// Check if a job with the same name is currently running.
		// If so, skip this one.

		try {
			int count = 0;
			for (JobExecutionContext cont: scheduler.getCurrentlyExecutingJobs()){
				JobDetail jd = cont.getJobDetail();
				if (jd.getKey().equals(jobDetail.getKey())) {
					++count;
				}
			}
			if (count > 1) {
				return;
			}
		}
		catch (Exception e) { // Scheduler exception
			LOGGER.error(e.getMessage());
		}

		CronTrigger trigger = (CronTrigger) context.getTrigger();
		Date nextFire = trigger.getNextFireTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		LOGGER.info("Backup job next firing time: " + dateFormat.format(nextFire));
		LOGGER.debug("Cron-Exp.: " + trigger.getCronExpression());

		PropertiesConfiguration tcfg = null;
		try {
			tcfg = ApplicationBean.getInstance().getDbConfig();
		}
		catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

    	Date now=new Date();
		String backupName=DatabaseBackupBL.formatBackupName(now);

		Boolean includeAttachments = ApplicationBean.getInstance().getSiteBean().getIncludeAttachments();

		if (includeAttachments == null) {
			includeAttachments= jobDataMap.getBooleanFromString("includeAttachments").booleanValue();
		}

		boolean keepAllBackups=false;

		Integer backupNumber = ApplicationBean.getInstance().getSiteBean().getNoOfBackups();
		if (backupNumber == null || backupNumber.intValue() <= 0){
			try{
				backupNumber=jobDataMap.getIntFromString("backupNumber");
			} catch(Exception ex){
				backupNumber=15;
			}
		}
		try{
			keepAllBackups=jobDataMap.getBooleanFromString("keepAllBackups").booleanValue();
		} catch (Exception e) {
			keepAllBackups=false;
		}

		ApplicationBean appBean = ApplicationBean.getInstance();
		if(appBean.isBackupInProgress()){
			LOGGER.info("Another backup is already in progress...");
			return;
		}
		appBean.setBackupInProgress(true);
		try {
			DatabaseBackupBL.zipDatabase(backupName,includeAttachments,tcfg);
			LOGGER.info("Backup created succesfully as:"+backupName);
			if(!keepAllBackups){
				DatabaseBackupBL.checkBackupNumber(backupNumber);
			}
		} catch (DatabaseBackupBLException e) {
			LOGGER.error("Can't create backup");
			LOGGER.error(e);
		}
		appBean.setBackupInProgress(false);
		LOGGER.info("Done executing DatabaseBackupJob!");
	}

}
