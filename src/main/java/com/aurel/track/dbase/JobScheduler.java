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


package com.aurel.track.dbase;

import static org.quartz.CronScheduleBuilder.cronSchedule;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.simpl.SimpleClassLoadHelper;
import org.quartz.xml.XMLSchedulingDataProcessor;

public class JobScheduler {
	
	private static final Logger LOGGER = LogManager.getLogger(JobScheduler.class);
	private static Properties quartzProperties = null;
	private static Scheduler sched = null;
	private static ServletContext servletContext = null;
	private static JobScheduler theScheduler = null;
	
	/**
	 * @param application
	 * @param torqueConfig
	 * 
	 * This will instantiate and configure a Quartz scheduler. Database related
	 * configuration items are taken from the Torque configuration such as not
	 * to duplicate JDBC URLs, database type, user, and password entries.
	 * Some properties can be configured in file /WEB-INF/Quartz.properties,
	 * for example configuration parameters for clustering.
	 * 
	 */
	public static void init(ServletContext application, PropertiesConfiguration torqueConfig) {

		Properties qcfg = null;
		servletContext = application;
		try {
			URL quartzURL = application.getResource("/WEB-INF/Quartz.properties");
			qcfg = new Properties();
			InputStream in = quartzURL.openStream();
			qcfg.load(in);
			in.close();
		} catch (Exception e) {
			LOGGER.error("Getting the Quartz.properties failed with " + e.getMessage(), e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (qcfg==null) {
			LOGGER.error("qcfg is null");
			return;
		}
		quartzProperties = qcfg;
	}

	
	/**
	 * Routine to start the Quartz scheduler. This reads the configuration file
	 * /WEB-INF/quartz-jobs.xml and configures all jobs and triggers with
	 * the appropriate job classes. Hence, all configuration is done via this
	 * xml file, there is no coding necessary to add any additional jobs,
	 * triggers and listeners.
	 * 
	 */
	public static void startScheduler() {
		try {
			StdSchedulerFactory sf = new StdSchedulerFactory();
			LOGGER.debug("Initialize scheduler factory...");
			sf.initialize(quartzProperties);
			LOGGER.debug("Getting the scheduler from factory");
			sched = sf.getScheduler();
		} catch (SchedulerException se) {
			LOGGER.error(se.getMessage(), se);
			LOGGER.error("Trying recovery...");
			clearJobs();  // clean out any debris...
			try { // ... again
				sched = new StdSchedulerFactory(quartzProperties).getScheduler();
			} catch (SchedulerException se2) {
				LOGGER.error(ExceptionUtils.getStackTrace(se2), se2); // Emergency call, not much we can do here anymore
			}
		}
		
		//We get all jobs and triggers from the configuration file /WEB-INF/quartz-jobs.xml
		SimpleClassLoadHelper clh = new SimpleClassLoadHelper();
		InputStream in = null;
		try {
			XMLSchedulingDataProcessor xmlProcessor = new XMLSchedulingDataProcessor(clh);
			LOGGER.debug("Getting the quartz-jobs.xml configuration file");
			in = HandleHome.getStream(servletContext, "quartz-jobs.xml");
			// URL quartzJobsURL = servletContext.getResource("/WEB-INF/quartz-jobs.xml");
			// InputStream in = quartzJobsURL.openStream();
			LOGGER.debug("Process the quartz-jobs.xml configuration file");
			xmlProcessor.processStreamAndScheduleJobs(in, "quartz-jobs.xml", sched);
			LOGGER.debug("quartz-jobs.xml processed");
			in.close();
		} catch (Exception e) {
			LOGGER.error("Processing the configuration file failed with " + e.getMessage(), e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (in!=null) {
					in.close();
				}
			} catch (Exception fe) {
			}
		}
		
		try {
			LOGGER.info("Starting Quartz scheduler...");
			sched.start();
			LOGGER.info("Quartz scheduler started");
			initializeJabDataMapsInJobs();  // for all jobs with "Public static void setJobDataMap(JobDataMap jdm)
			LOGGER.info("Job data maps initialized");
		} catch (Exception se) {
			LOGGER.error(se.getMessage(), se);
			LOGGER.error("Trying recovery...");
			clearJobs();  // clean out any debris...
			try { // ... again
				sched = new StdSchedulerFactory(quartzProperties).getScheduler();
				sched.start();
				System.err.println("Recovery was successful");
			} catch (SchedulerException se2) {
				LOGGER.error(ExceptionUtils.getStackTrace(se2), se2); // Emergency call, not much we can do here anymore
			}
		}
	}

	
	/**
	 * Clears all triggers of this scheduler.
	 */
	public static void clearScheduler() {
		try {
			for(String tgroup: sched.getTriggerGroupNames()) {
				// enumerate each trigger in group
				for (TriggerKey triggerKey: sched.getTriggerKeys(GroupMatcher.triggerGroupEquals(tgroup))) {
					sched.unscheduleJob(triggerKey);
				}
			}
		}
		catch (Exception sse) {
			LOGGER.debug(ExceptionUtils.getStackTrace(sse));
		}
	}
	
	/**
	 * Clears all triggers of this scheduler.
	 */
	public static void clearJobs() {
		try {
			for (String groupName: sched.getJobGroupNames()) {
				for (JobKey jobKey : sched.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
					sched.deleteJob(jobKey);
				}
			}
		}
		catch (Exception sse) {
			LOGGER.debug(ExceptionUtils.getStackTrace(sse));
		}		
	}	
	
	
	/**
	 * Stops the scheduler. It will wait until all jobs have been completed.
	 */
	public static void stopScheduler() {
		try {
			if (sched != null) {
				LOGGER.info("Stopping Quartz scheduler");
				sched.shutdown(false);
				LOGGER.info("Quartz scheduler stopped");
			}
		}
		catch (Exception sse) {
			LOGGER.debug(ExceptionUtils.getStackTrace(sse));
		}
	}
	
	/**
	 * Get the current cron expression for the database backup job.
	 * @return a String with the cron expression
	 */
	public static String getDBJobCronExpression() {
		String cronExpression = "";
		try {
			CronTrigger trigger = (CronTrigger)sched.getTrigger(new TriggerKey("DatabaseBackupJobInitialTrigger", "DefaultTriggerGroup"));
			if (trigger != null) {
				cronExpression = trigger.getCronExpression();
			}			
		} catch(Exception e) {
			
		}
		return cronExpression;
	}
	
	/**
	 * Replaces for the database trigger the cron expression with a new one.
	 * @param cronExpression
	 * @return true if successful
	 */
	public static boolean setDBJobCronExpression(String cronExpression) {
		LOGGER.debug("setDBJobCronExpression:cronExpression="+cronExpression);
		try {
		/*	
			JobDetail jobDetail = sched.getJobDetail(new JobKey("DatabaseBackupJob", "DefaultJobGroup"));
			
			CronTrigger trigger = newTrigger()
					.withIdentity("DatabaseBackupJobInitialTrigger", "DefaultTriggerGroup")
					.withSchedule(cronSchedule(cronExpression))
					.build();
			
			
			sched.unscheduleJob(new TriggerKey("DatabaseBackupJobInitialTrigger", "DefaultTriggerGroup"));
			sched.scheduleJob(trigger,jobDetail.getJobBuilder().);
		 */
			
			// retrieve the trigger
			Trigger oldTrigger = sched.getTrigger(new TriggerKey("DatabaseBackupJobInitialTrigger", "DefaultTriggerGroup"));

			// obtain a builder that would produce the trigger
			TriggerBuilder tb = oldTrigger.getTriggerBuilder();

			// update the schedule associated with the builder, and build the new trigger

			Trigger newTrigger = tb
					.withIdentity("DatabaseBackupJobInitialTrigger", "DefaultTriggerGroup")
					.withSchedule(cronSchedule(cronExpression))
					.build();

			sched.rescheduleJob(oldTrigger.getKey(), newTrigger);
			
		} catch(Exception e) {
			LOGGER.info(e.getMessage(), e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return false;
		}
		return true;
	}

	/*
	 * For all jobs having a method
	 * public static void setJobDataMap(JobDataMap jdm)
	 * set the corresponding job data map during system startup
	 */
	private static void initializeJabDataMapsInJobs() {
		try {
			for (String groupName: sched.getJobGroupNames()) {
				for (JobKey jobKey : sched.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {

					String jobName = jobKey.getName();
					String jobGroup = jobKey.getGroup();

					JobDetail jobDetail = sched.getJobDetail(new JobKey(jobName, jobGroup));
					Class<?> jobClass = jobDetail.getJobClass();
					JobDataMap jobDataMap = jobDetail.getJobDataMap();
					try {
						LOGGER.debug("Trying to call method in " + jobName);
						Method setJobDataMapMethod = jobClass.getMethod("setJobDataMap", JobDataMap.class);
						Object o = setJobDataMapMethod.invoke(null, jobDataMap);
						LOGGER.debug("Called method in " + jobName);
					} catch (NoSuchMethodException nsme) {
						// ignore
					} catch (InvocationTargetException ite) {
						LOGGER.debug(ExceptionUtils.getStackTrace(ite), ite);
					} catch (IllegalArgumentException e) {
						LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
					} catch (IllegalAccessException e) {
						LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
					} catch (Exception e) {
						LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
					}
				}
			}
		}catch (Exception e) {
			// ignore
		}
	}

}
