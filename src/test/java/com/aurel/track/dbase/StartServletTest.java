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

package com.aurel.track.dbase;

import java.io.File;
import java.net.MalformedURLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.junit.AfterClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockServletConfig;

import com.aurel.track.StartServlet;
import com.aurel.track.prop.ApplicationBean;


public class StartServletTest extends Mockito {

	private static final Logger LOGGER = LogManager.getLogger(StartServletTest.class);
	
	private static StartServlet servlet;
	private static String realPath;
	private static ServletContext ctx = mock(ServletContext.class); // new MockServletContext(realPath, new FileSystemResourceLoader());
	private static MockServletConfig config = null;
    private static File homeDir = null;

	
	public static void setUpEnvironment() throws Exception {
		
		System.out.println("Starting JUnit test StartServletTest...");
		
		DatabaseHandler.cleanDatabase();
		DatabaseHandler.writeTorquePropsToHome();
		DatabaseHandler.startDbServer();

		realPath = System.getProperty("user.dir")+"/src/main/webapp";

		homeDir = new File(System.getProperty("user.dir")+"/homet");

		if (!homeDir.isDirectory()) {
			homeDir.delete();
		}
		if (!homeDir.exists()) {
			homeDir.mkdirs();
		}

		System.setProperty("TRACKPLUS_HOME", System.getProperty("user.dir")+"/homet");

		DatabaseHandler.setupContext();
		ctx = DatabaseHandler.getMockServletContext();
		
		config = new MockServletConfig(ctx);
		config.addInitParameter("attachmentRootDir", homeDir.getAbsolutePath());
		config.addInitParameter("key2", "value2");
		config.addInitParameter("list", "value1, value2");
		config.addInitParameter("listesc", "value1\\,value2");

	}

	@AfterClass
	public static void tearDown() throws Exception {
		DatabaseHandler.stopDbServer();
	}

	/**
	 * Run the void init() method test.
	 * @throws MalformedURLException 
	 * @throws ServletException 
	 * @throws InterruptedException 
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testInit() throws MalformedURLException, ServletException, InterruptedException {
		try {
			setUpEnvironment();
		} catch (Exception e){
			LOGGER.error("Could not set up environment",e);
		}
		
		servlet = new StartServlet() {
			private static final long serialVersionUID = 1L;

			public ServletContext getServletContext() {
				return ctx; // return the mock
			}
		};

		servlet.init(config);
		
		ApplicationBean.getInstance().setInTestMode(true);
		
		ExecutorService taskExecutor = servlet.getExecutor();
		
		taskExecutor.awaitTermination(90, TimeUnit.SECONDS);
		taskExecutor.shutdown();
		try {
			taskExecutor.awaitTermination(5, TimeUnit.SECONDS);

		} catch (InterruptedException e) {
			System.err.println("Couldn't shut down...");
		}
	}


	/**
	 * Launch the test.
	 *
	 * @param args the command line arguments
	 *
	 * @generatedBy CodePro at 05.01.15 23:30
	 */
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(StartServletTest.class);
	}



}
