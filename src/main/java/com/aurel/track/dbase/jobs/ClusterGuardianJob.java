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

import java.util.Date;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import com.aurel.track.cluster.ClusterBL;

public class ClusterGuardianJob implements Job {
	
	private static final Logger LOGGER = LogManager.getLogger(ClusterGuardianJob.class);
	public static int PASSAWAYTIMEOUT = 5*60; // in seconds

	public void execute(JobExecutionContext context) {
		LOGGER.debug("execute ClusterGuardianJob....");
		CronTrigger trigger = (CronTrigger) context.getTrigger();
		Date nextFire = trigger.getNextFireTime();
		Date now = new Date();
		PASSAWAYTIMEOUT = (int)(nextFire.getTime() - now.getTime())/1000;
		ClusterBL.setPassawayTimeout(PASSAWAYTIMEOUT);
		//TClusterNodePeer.setPassawayTimeout(PASSAWAYTIMEOUT);
		//TClusterNodePeer.cleanInactiveNodes();
		ClusterBL.processCluster(true);
	}
 }
