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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockServletConfig;

import com.aurel.track.StartServlet;



public class ProvideDerbyDb extends Mockito {

	public StartServlet servlet;
	public static String realPath;
	private ServletContext ctx = DatabaseHandler.getMockServletContext(); //mock(ServletContext.class); 
	// new MockServletContext(realPath, new FileSystemResourceLoader());
	private MockServletConfig config = null;
    private File homeDir = null;
	
	@BeforeClass
	public void setUp() throws Exception {
		
		System.out.println("Starting JUnit test ProvideDerbyDb ");
		
		DatabaseHandler.cleanDatabase();

		DatabaseHandler.startDbServer();
		
		DatabaseHandler.createDBFromScratch();

		config = new MockServletConfig(DatabaseHandler.getMockServletContext());
		config.addInitParameter("attachmentRootDir", homeDir.getAbsolutePath());
		config.addInitParameter("key2", "value2");
		config.addInitParameter("list", "value1, value2");
		config.addInitParameter("listesc", "value1\\,value2");

	}

	@AfterClass
	public void tearDown() throws Exception {
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
	 * @generatedBy CodePro at 05.01.15 23:30
	 */
	@Test
	public void testInit() throws MalformedURLException, ServletException, InterruptedException {
		servlet = new StartServlet() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public ServletContext getServletContext() {
				return ctx; // return the mock
			}
		};

		when(ctx.getResource(anyString())).thenAnswer(new Answer<URL>() {
			@Override
			public URL answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();

				String ppath = (String)args[0];
				String base = "file:///"+ realPath;
				String path = base + ppath;

				if (ppath.startsWith("/WEB-INF/classes/resources")) {
					path = path.replace("/webapp/WEB-INF/classes/resources", "/resources/resources");
				} 
				return new URL(path);
			}
		});
		when (ctx.getAttribute("javax.servlet.context.tempdir")).thenReturn(homeDir.getAbsolutePath());


		servlet.init(config);
		ExecutorService taskExecutor = servlet.getExecutor();
		taskExecutor.awaitTermination(120, TimeUnit.SECONDS);
		taskExecutor.shutdown();
		try {
			taskExecutor.awaitTermination(5, TimeUnit.SECONDS);

		} catch (InterruptedException e) {
			System.err.println("Couldn't shut down...");
		}
		System.out.println("Stopped JUnit test ProvideDerbyDb ");
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
