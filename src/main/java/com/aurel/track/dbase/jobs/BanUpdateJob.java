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

package com.aurel.track.dbase.jobs;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;

import com.aurel.track.prop.BanProcessor;

public class BanUpdateJob implements Job {
	
	private static final Logger LOGGER = LogManager.getLogger(BanUpdateJob.class);
	
	private static JobDataMap jobDataMap = null;
	private static Integer banTime = 10;
	private static Integer noOfBadAttempts = 15;

	public void execute(JobExecutionContext context) {
		LOGGER.debug("Executing BanUpdateJob...");
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		if ("false".equals(jobDataMap.getString("enabled"))) {
			return;
		}
		BanProcessor bp = BanProcessor.getBanProcessor();
		initParams();
		BanProcessor.setBanTime(banTime.intValue());
		BanProcessor.setMaxNoOfBadAttempts(noOfBadAttempts.intValue());		
		bp.updateBanMap();
	}
	
	public static void setJobDataMap(JobDataMap jdm) {
		jobDataMap = jdm;
		initParams();
		LOGGER.info("Initialized ban update job mapping table from quartz-jobs.xml");
	}
	
	private static void initParams() {
		try {
			banTime = jobDataMap.getIntegerFromString("banTime");
		}
		catch (Exception e) {
			LOGGER.info("No banTime found. Using default of 10 minutes");
		}
		if (banTime == null) {
			banTime = new Integer(10);
		}
		
		try {
			noOfBadAttempts = jobDataMap.getIntegerFromString("noOfBadAttempts");
		}
		catch (Exception e) {
			LOGGER.info("No noOfBadAttempts found. Using default of 15");			
		}
		if (noOfBadAttempts == null) {
			noOfBadAttempts = new Integer(15);
		}	
	}
	
	public static int getNoOfBadAttempts() {
		return noOfBadAttempts;
	}
	
	public static int getBanTime() {
		return banTime;
	}
 }
