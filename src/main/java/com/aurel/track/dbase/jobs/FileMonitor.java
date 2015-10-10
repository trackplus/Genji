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

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyListener;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.dbase.HandleHome;
import com.aurel.track.dbase.JobScheduler;
import com.aurel.track.prop.ApplicationBean;

public class FileMonitor {

	private static int watchID = 0 ;
	private static final Logger LOGGER = LogManager.getLogger(FileMonitor.class);
	
	public static void monitor() throws Exception {
		// path to watch
		String path = HandleHome.getTrackplus_Home();

		// watch mask, specify events you care about,
		// or JNotify.FILE_ANY for all events.
		int mask = JNotify.FILE_CREATED  | 
				JNotify.FILE_DELETED  | 
				JNotify.FILE_MODIFIED | 
				JNotify.FILE_RENAMED;

		// watch subtree?
		boolean watchSubtree = false;

		// add actual watch
		try {		

			if (System.getProperty("os.name").startsWith("Win") && 
					System.getProperty("os.arch").equals("amd64"))
			{
				System.loadLibrary("jnotify_64bit");
			}
			else
			{
				System.loadLibrary("jnotify");
			}
			LOGGER.info("Watching files in " + path);
			watchID = JNotify.addWatch(path, mask, watchSubtree, new Listener());
		}
		catch (UnsatisfiedLinkError e)
		{
			LOGGER.info("Could not find jnotify library in java.library.path=" + System.getProperty("java.library.path"));
			LOGGER.info("This means configuration files in TRACKPLUS_HOME are not monitored for changes.");
			LOGGER.info("You can avoid having to restart the server after configuration changes by ");
			LOGGER.info("placing the library from WEB-INF/classes/plugins/filemonitor into any of the above directories.");
		}
	}

	public static void removeFileWatcher() {
		// to remove watch the watch
		try {
			boolean res = JNotify.removeWatch(watchID);
		} catch (Exception e) {
			System.err.println("Could not stop file monitor");
		}
	}
	

	static class Listener implements JNotifyListener {
		
		public void fileRenamed(int wd, String rootPath, String oldName,
				String newName) {
			    handleChange(oldName);
		}
		
		public void fileModified(int wd, String rootPath, String name) {
		    handleChange(name);
		}
		
		public void fileDeleted(int wd, String rootPath, String name) {
		    handleChange(name);
		}
		
		public void fileCreated(int wd, String rootPath, String name) {
		    handleChange(name);
		}
		
		void print(String msg) {
			LOGGER.info(msg);
		}

		private void handleChange(String name) {
			if ("quartz-jobs.xml".equals(name)) {
				JobScheduler.stopScheduler();
				JobScheduler.startScheduler();
			} else if (HandleHome.FILTER_SUBSCRIPTIONS_FILE.equals(name)) {
				LOGGER.warn(HandleHome.FILTER_SUBSCRIPTIONS_FILE + " has been modified outside of Genji...");
				LOGGER.info("Updating filter subscription configuration ");
				try {
					HandleHome.reloadProperties(HandleHome.FILTER_SUBSCRIPTIONS_FILE,
							ApplicationBean.getApplicationBean().getServletContext());
				} catch (Exception e) {
					LOGGER.warn("Could not reload " + HandleHome.FILTER_SUBSCRIPTIONS_FILE);
				}
			} else if (HandleHome.PDF_EXCEL_EXPORT_FILE.equals(name)) {
				LOGGER.warn(HandleHome.PDF_EXCEL_EXPORT_FILE + " has been modified outside of Genji...");
				LOGGER.info("Updating jasper export configuration ");
				try {
					HandleHome.reloadProperties(HandleHome.PDF_EXCEL_EXPORT_FILE,
							ApplicationBean.getApplicationBean().getServletContext());
				} catch (Exception e) {
					LOGGER.warn("Could not reload " + HandleHome.PDF_EXCEL_EXPORT_FILE);
				}
			}
		}
	}
}
