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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.cluster.ClusterBL;

public class RegistrationPasswordCleanerJob implements Job{

	private static final Logger LOGGER = LogManager.getLogger(RegistrationPasswordCleanerJob.class);
	/**
	 * Here we clean out users that have registered but have not confirmed their
	 * registration within a given period. We also clean out tokens for
	 * password resets that have been requested but not been used within a certain
	 * time.
	 */
	@Override
	public void execute(JobExecutionContext context) {
		LOGGER.debug("execute RegistrationPasswordCleanerJob....");
		if (ClusterBL.getIAmTheMaster()) {
			LOGGER.debug("Process inactive registrations and password reset tokens ...");
			try {
				cleanRegistrationAndPasswordTokens();
			} catch (Exception e) {
				LOGGER.error("Problem with running scheduler " + e.getMessage());
			}
		}
	}
	
	/**
	 * Remove all users that have registered themselves and have never confirmed their registration
	 * during the grace period.
	 */
	private static void cleanRegistrationAndPasswordTokens() throws Exception {
		PersonBL.cancelForgotPasswordTokens();
		PersonBL.removeUnconfirmedUsers();
		return ;
	}
	
}
