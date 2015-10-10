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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;

import com.aurel.track.cluster.ClusterBL;
import com.aurel.track.prop.ApplicationBean;

/**
 * This job checks for software updates on a regular basis.
 * It also sends some bookkeeping information to the update
 * server. This can be shut off by removing the job from
 * quartz-jobs.xml.
 * 
 *
 */
public class CheckForUpdateJob implements Job{
	
	private static final Logger LOGGER = LogManager.getLogger(CheckForUpdateJob.class);

	private static Properties updateProps = new Properties();
	
	public void execute(JobExecutionContext context) {
		LOGGER.debug("execute CheckForUpdateJob...");
		if (!ClusterBL.getIAmTheMaster()) {
			return;
		}
		try {
			
	        JobDetail jobDetail = context.getJobDetail();
			JobDataMap jobDataMap = jobDetail.getJobDataMap();
			Boolean doThis = jobDataMap.getBooleanFromString("enabled");
			if (doThis.booleanValue() == false) {
				return;
			}
			String server = jobDataMap.getString("server");
			if (!server.endsWith("/")) {
				server = server + "/";
			}
			String updateUrl = server + jobDataMap.getString("updateUrl") 
			                   +"?p=" + URLEncoder.encode(ApplicationBean.getApplicationBean().getProperties(), "UTF-8");

			URL url = new URL(updateUrl);
			URLConnection conn = url.openConnection();
		

			conn.setRequestProperty(
					"User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)" );

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String strLine = "";
			StringBuffer sb = new StringBuffer();
			
			while ((strLine = in.readLine()) != null) {
				sb.append(strLine).append("\n");
			}
			in.close();
			
			Properties props = new Properties();
			try {
				props.load(new ByteArrayInputStream(sb.toString().getBytes()));
			}
			catch (Exception exp) {
				// What shall we do here?
			}
			updateProps = props;

			ApplicationBean.getApplicationBean().setMostActualVersionString(updateProps.getProperty("version"));
			ApplicationBean.getApplicationBean().setMostActualVersion(new Integer(updateProps.getProperty("app.version")));

		}
		catch (Exception ex) {
			LOGGER.info("Could not establish connection to update server. No problem.");
		}
		
		// System.err.println(updateProps.get("versionString"));
	}
	
	public static Properties getUpdateProperties() {
		return updateProps;
	}

}
