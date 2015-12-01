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


package com.aurel.track;

import java.io.File;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.dbase.JobScheduler;
import com.aurel.track.lucene.index.LuceneIndexer;
import com.aurel.track.prop.ApplicationBean;

/**
 * This servlet class together with a Struts2 Action servlet
 * constitutes the root of the Genji application. It is being
 * called by the servlet container as defined in <code>web.xml</code>.
 * <p>
 * This servlet does not run the application itself but returns
 * to the container quickly. To initialize the application it starts
 * a new thread via {@link ApplicationStarter}.
 *
 */
public class StartServlet extends HttpServlet {

	private static final long serialVersionUID = 500L;
	private static transient ExecutorService executor = null;
	private static final Logger LOGGER = LogManager.getLogger(StartServlet.class);

	// ---------------------------------------------------- HttpServlet Methods

	/** Initializes the Genji application */
	@Override
	public void init() throws ServletException {

		executor = Executors.newFixedThreadPool(100); // Max 100 threads.
		ApplicationStarter root = ApplicationStarter.getInstance();
		root.init(getServletConfig(), executor);
		System.err.println("\n\n======================================== Genji starting ... ========================================");
		System.err.println("Genji Start servlet initiates application start...");
		System.err.println("System io temp dir is " + System.getProperty("java.io.tmpdir"));
		System.err.println("Container work dir is " + getServletConfig().getServletContext().getAttribute("javax.servlet.context.tempdir"));
		try {
			File workDir = (File)getServletConfig().getServletContext().getAttribute("javax.servlet.context.tempdir");
			if (!workDir.canWrite()) {
				System.err.println("Potentially serious installation problem: Can't write to container work dir at " + workDir.getAbsolutePath());
			}
		} catch (Exception e){

		}

		executor.execute(root);

	}

	public ExecutorService getExecutor() {
		return executor;
	}

	// ======================================================================
	// ======================================================================
	// ======================================================================
	/**
	 * Gracefully shut down this database servlet, releasing any resources that
	 * were allocated at initialization
	 */
	@Override
	public void destroy() {
		LOGGER.info("StartServlet going down...");
		ApplicationBean.getInstance().removeClusterNode();
		//lucene: close all the IndexModifiers to release the locks
		LuceneIndexer.closeWriters();
		JobScheduler.stopScheduler();
		if (executor != null) {
			executor.shutdownNow();
			LOGGER.info("Shutting down executor...");
			Date begin = new Date();
			Date now = new Date();
			while (!executor.isTerminated()) {
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					break;
				}
				now = new Date();
				if ((now.getTime() - begin.getTime()) > 30 * 1000) {
					break;
				}
			}
		}
		super.destroy();
		System.out.println("System down!");
	}

}
