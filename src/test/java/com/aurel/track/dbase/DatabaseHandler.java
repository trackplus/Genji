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
import java.net.InetAddress;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletContext;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.derby.drda.NetworkServerControl;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.Torque;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockServletConfig;

import com.aurel.track.StartServlet;
import com.aurel.track.prop.ApplicationBean;

/**
 * This class provides a method to create a clean Derby database
 * initialized using the applications init database procedure.
 *
 */

public class DatabaseHandler extends Mockito {

	final static ServletContext ctx = org.mockito.Mockito.mock(ServletContext.class); // new MockServletContext(realPath, new FileSystemResourceLoader());

	private static final Logger LOGGER = LogManager.getLogger(DatabaseHandler.class);
	private static Connection con;
	
	private static NetworkServerControl dbServer;

	private static StartServlet servlet;
	private static String realPath;
	private static MockServletConfig config = null;
    private static File homeDir = null;
    private static DatabaseHandler databaseHandler = null;
    private static PropertiesConfiguration props;
    
    private static String dbName = "TestDB";
    private static String dbOptions = ";create=true";
    
    private static String jdbcDriver = "org.apache.derby.jdbc.EmbeddedDriver";
    private static String jdbcUrl = "jdbc:derby:memory:";

	private static String user = "trackp";
	private static String password = "tissi";
	
	private static boolean databaseIsInitialized = false;

	private DatabaseHandler() {

	}

	/**
	 * Delete the test database in case there is one. First
	 * stop the database server in case it is running.
	 */
	public static void cleanDatabase() {
		try {
			stopDbServer();
			File testDB = new File("TestDB");
			if (testDB.exists() && testDB.isDirectory()) {
				FileUtils.deleteDirectory(testDB);
				LOGGER.info("TestDB deleted.");
			}
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
	}


	/**
	 * Start the database server. If it was running, stop it first.
	 */
	public static void startDbServer() {
		System.setProperty("derby.drda.startNetworkServer", "true");
		stopDbServer();
		try {
			dbServer = new NetworkServerControl(
					InetAddress.getByName("localhost"), 15270, user,
					password);
			java.io.PrintWriter consoleWriter = new java.io.PrintWriter(
					System.out, true);
			dbServer.start(consoleWriter);
			checkForDatabase();
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
	}
	
	/**
	 * Stop the database server
	 */
	public static void stopDbServer() {

		System.setProperty("derby.drda.startNetworkServer", "true");
		databaseIsInitialized= false;
		if (dbServer == null) {
			try {
				dbServer = new NetworkServerControl(
						InetAddress.getByName("localhost"), 15270, user,
						password);
			} catch (Exception e) {
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}
		}
		try {
			Torque.shutdown();
			dbServer.shutdown();
		} catch (Exception e) {
			if (!e.getMessage().contains("Connection refused")) {
				LOGGER.info("Derby server shutdown failed. Ignoring this, but you should check! " + e.getMessage());
				LOGGER.debug("Stack trace: ",e);
			}
		}
	}
	


	public static ServletContext getMockServletContext() {
		return ctx;
	}
	
    
	public static String getJdbcDriver() {
		return jdbcDriver;
	}

	public static void setJdbcDriver(String jdbcDriver) {
		DatabaseHandler.jdbcDriver = jdbcDriver;
	}

	public static String getJdbcUrl() {
		return jdbcUrl + dbName + dbOptions;
	}

	public static void setJdbcUrl(String jdbcUrl) {
		DatabaseHandler.jdbcUrl = jdbcUrl;
	}

	/**
	 * This method initialize the Torque.
	 */
	public static void initTorque() {
        try	{
        	Torque.init(props);
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
	public static void writeTorquePropsToHome() {
		realPath = System.getProperty("user.dir")+"/src/main/webapp";

		homeDir = new File(System.getProperty("user.dir")+"/homet");

		if (!homeDir.isDirectory()) {
			homeDir.delete();
		}
		if (!homeDir.exists()) {
			homeDir.mkdirs();
		}

		System.setProperty("TRACKPLUS_HOME", System.getProperty("user.dir")+"/homet");

		props = new PropertiesConfiguration();

		props.setProperty("torque.dsfactory.track.connection.user",user);
		props.setProperty("torque.dsfactory.track.connection.password",password);

		props.setProperty("torque.database.track.adapter","derby");
		props.setProperty("torque.dsfactory.track.connection.driver",getJdbcDriver());
		//props.put("torque.dsfactory.track.connection.url","jdbc:derby://localhost:15270/TestDB;create=true");
		props.setProperty("torque.dsfactory.track.connection.url",getJdbcUrl());

		props.setProperty("torque.dsfactory.track.factory","org.apache.torque.dsfactory.SharedPoolDataSourceFactory");
		props.setProperty("torque.dsfactory.track.pool.maxActive","10");
		props.setProperty("torque.dsfactory.track.pool.testOnBorrow","true");
		props.setProperty("torque.dsfactory.track.pool.validationQuery","SELECT PKEY FROM TSTATE");

		// Write properties file.
		try {
			props.save(new FileOutputStream(homeDir.getAbsolutePath()+"/Torque.properties"), null);
		} catch (Exception e) {
		}
	}
	
	public static void setupContext() throws Exception {
		ApplicationBean.getApplicationBean().setServletContext(ctx);
		String resPath = realPath.replace("/webapps", "");
		
		when (ctx.getResource("/dbase/Derby/track-schema.sql")).thenReturn(new URL("file:///" +  realPath + "/dbase/Derby/track-schema.sql"));
		when (ctx.getResource("/dbase/Derby/id-table-schema.sql")).thenReturn(new URL("file:///" +  realPath + "/dbase/Derby/id-table-schema.sql"));
		when (ctx.getResource("/dbase/Derby/quartz.sql")).thenReturn(new URL("file:///" +  realPath + "/dbase/Derby/quartz.sql"));
		when (ctx.getResource("/WEB-INF/schema/track-schema.xml")).thenReturn(new URL("file:///" +  realPath + "/WEB-INF/schema/track-schema-flat.xml"));
		when (ctx.getResource("/WEB-INF/schema/database.dtd")).thenReturn(new URL("file:///" +  realPath + "/WEB-INF/schema/database.dtd"));

		when (ctx.getResource("/WEB-INF/UserLevels.properties")).thenReturn(new URL("file:///" +  realPath + "/WEB-INF/UserLevels.properties"));

		when (ctx.getResource("/initData/full/ProjectTypes/ProjectType-ElectronicsProject.xml")).thenReturn(new URL("file:///" +  realPath + "/initData/full/ProjectTypes/ProjectType-ElectronicsProject.xml"));
		when (ctx.getResource("/initData/full/ProjectTypes/ProjectType-Helpdesk.xml")).thenReturn(new URL("file:///" +  realPath + "/initData/full/ProjectTypes/ProjectType-Helpdesk.xml"));
		when (ctx.getResource("/initData/full/ProjectTypes/ProjectType-Simple.xml")).thenReturn(new URL("file:///" +  realPath + "/initData/full/ProjectTypes/ProjectType-Simple.xml"));
		when (ctx.getResource("/initData/full/ProjectTypes/ProjectType-Software.xml")).thenReturn(new URL("file:///" +  realPath + "/initData/full/ProjectTypes/ProjectType-Software.xml"));
		when (ctx.getResource("/initData/full/ProjectTypes/ProjectType-Standard.xml")).thenReturn(new URL("file:///" +  realPath + "/initData/full/ProjectTypes/ProjectType-Standard.xml"));
		when (ctx.getResource("/initData/full/ProjectTypes/ProjectType-VerySimple")).thenReturn(new URL("file:///" +  realPath + "/initData/full/ProjectTypes/ProjectType-VerySimple"));

		when (ctx.getResource("/initData/full/Forms/DefaultForms.xml")).thenReturn(new URL("file:///" +  realPath + "/initData/full/Forms/DefaultForms.xml"));
		when (ctx.getResource("/initData/full/Forms/ScreenData.xml")).thenReturn(new URL("file:///" +  realPath + "/initData/full/Forms/ScreenData.xml"));

		when (ctx.getResource("/initData/full/Logos/logo-68x28.png")).thenReturn(new URL("file:///" +  realPath + "/initData/full/Logos/logo-68x28.png"));
		when (ctx.getResource("/initData/full/Logos/logo-98x40.png")).thenReturn(new URL("file:///" +  realPath + "/initData/full/Logos/logo-98x40.png"));
		when (ctx.getResource("/initData/full/Logos/logo-254x105.png")).thenReturn(new URL("file:///" +  realPath + "/initData/full/Logos/logo-254x105.png"));
		when (ctx.getResource("/initData/full/Logos/tracklogo.png")).thenReturn(new URL("file:///" +  realPath + "/initData/full/Logos/tracklogo.png"));
		when (ctx.getResource("/initData/full/Logos/trackLogo.png")).thenReturn(new URL("file:///" +  realPath + "/initData/full/Logos/trackLogo.png"));
		when (ctx.getResource("resources/MailTemplates/tracklogo.gif")).thenReturn(
				new URL("file:///" +  resPath + "/resources/MailTemplates/tracklogo.gif"));

		when (ctx.getResource("/WEB-INF/classes/resources/ExportTemplates/wordTemplates")).thenReturn(
				new URL("file:///" +  resPath + "/resources/ExportTemplates/wordTemplates"));
		when (ctx.getResource("/WEB-INF/classes/resources/ExportTemplates/latexTemplates")).thenReturn(
				new URL("file:///" +  resPath + "/resources/ExportTemplates/latexTemplates"));
		
		when (ctx.getResource("/initData/full/Workflows/Workflows.xml")).thenReturn(new URL("file:///" +  realPath + "/initData/full/Workflows/Workflows.xml"));

		when (ctx.getResource("/initData/full/populate.sql")).thenReturn(new URL("file:///" +  realPath + "/initData/full/populate.sql"));
		when (ctx.getResource("/initData/full/FilterSubscriptions.properties")).thenReturn(new URL("file:///" +  realPath + "/initData/FilterSubscriptions.properties"));
		when (ctx.getResource("/initData/full/FilterSubscriptions.properties")).thenReturn(new URL("file:///" +  realPath + "/initData/FilterSubscriptions.properties"));
		when (ctx.getResource("/initData/full/postload.sql")).thenReturn(new URL("file:///" +  realPath + "/initData/full/postload.sql"));
		when (ctx.getResource("/initData/full/trackplus-plugin.xml")).thenReturn(new URL("file:///" +  realPath + "/initData/full/trackplus-plugin.xml"));
		when (ctx.getResource("/initData/full/workspaceSamples.sql")).thenReturn(new URL("file:///" +  realPath + "/initData/full/workspaceSamples.sql"));
		when (ctx.getResource("/WEB-INF/classes/resources/UserInterface/ApplicationResources.properties")).thenReturn(new URL("file:///" +  realPath + "/WEB-INF/classes/resources/UserInterface/ApplicationResources.properties"));
		
		when (ctx.getResource("/initData/bugs/ProjectTypes/ProjectType-ElectronicsProject.xml")).thenReturn(new URL("file:///" +  realPath + "/initData/bugs/ProjectTypes/ProjectType-ElectronicsProject.xml"));
		when (ctx.getResource("/initData/bugs/ProjectTypes/ProjectType-Helpdesk.xml")).thenReturn(new URL("file:///" +  realPath + "/initData/bugs/ProjectTypes/ProjectType-Helpdesk.xml"));
		when (ctx.getResource("/initData/bugs/ProjectTypes/ProjectType-Simple.xml")).thenReturn(new URL("file:///" +  realPath + "/initData/bugs/ProjectTypes/ProjectType-Simple.xml"));
		when (ctx.getResource("/initData/bugs/ProjectTypes/ProjectType-Software.xml")).thenReturn(new URL("file:///" +  realPath + "/initData/bugs/ProjectTypes/ProjectType-Software.xml"));
		when (ctx.getResource("/initData/bugs/ProjectTypes/ProjectType-Standard.xml")).thenReturn(new URL("file:///" +  realPath + "/initData/bugs/ProjectTypes/ProjectType-Standard.xml"));
		when (ctx.getResource("/initData/bugs/ProjectTypes/ProjectType-VerySimple")).thenReturn(new URL("file:///" +  realPath + "/initData/bugs/ProjectTypes/ProjectType-VerySimple"));

		when (ctx.getResource("/initData/bugs/Forms/DefaultForms.xml")).thenReturn(new URL("file:///" +  realPath + "/initData/bugs/Forms/DefaultForms.xml"));
		when (ctx.getResource("/initData/bugs/Forms/ScreenData.xml")).thenReturn(new URL("file:///" +  realPath + "/initData/bugs/Forms/ScreenData.xml"));

		when (ctx.getResource("/initData/bugs/Logos/logo-68x28.png")).thenReturn(new URL("file:///" +  realPath + "/initData/bugs/Logos/logo-68x28.png"));
		when (ctx.getResource("/initData/bugs/Logos/logo-98x40.png")).thenReturn(new URL("file:///" +  realPath + "/initData/bugs/Logos/logo-98x40.png"));
		when (ctx.getResource("/initData/bugs/Logos/logo-254x105.png")).thenReturn(new URL("file:///" +  realPath + "/initData/bugs/Logos/logo-254x105.png"));
		when (ctx.getResource("/initData/bugs/Logos/tracklogo.png")).thenReturn(new URL("file:///" +  realPath + "/initData/bugs/Logos/tracklogo.png"));
		when (ctx.getResource("/initData/bugs/Logos/trackLogo.png")).thenReturn(new URL("file:///" +  realPath + "/initData/bugs/Logos/trackLogo.png"));

		
		when (ctx.getResource("/initData/bugs/Workflows/Workflows.xml")).thenReturn(new URL("file:///" +  realPath + "/initData/bugs/Workflows/Workflows.xml"));

		when (ctx.getResource("/initData/bugs/populate.sql")).thenReturn(new URL("file:///" +  realPath + "/initData/bugs/populate.sql"));
		when (ctx.getResource("/initData/bugs/FilterSubscriptions.properties")).thenReturn(new URL("file:///" +  realPath + "/initData/FilterSubscriptions.properties"));
		when (ctx.getResource("/initData/bugs/FilterSubscriptions.properties")).thenReturn(new URL("file:///" +  realPath + "/initData/FilterSubscriptions.properties"));
		when (ctx.getResource("/initData/bugs/postload.sql")).thenReturn(new URL("file:///" +  realPath + "/initData/bugs/postload.sql"));
		when (ctx.getResource("/initData/bugs/trackplus-plugin.xml")).thenReturn(new URL("file:///" +  realPath + "/initData/bugs/trackplus-plugin.xml"));
		when (ctx.getResource("/initData/bugs/workspaceSamples.sql")).thenReturn(new URL("file:///" +  realPath + "/initData/bugs/workspaceSamples.sql"));
		when (ctx.getResource("/WEB-INF/classes/resources/UserInterface/ApplicationResources.properties")).thenReturn(new URL("file:///" +  realPath + "/WEB-INF/classes/resources/UserInterface/ApplicationResources.properties"));

		when(ctx.getResource(anyString())).thenAnswer(new Answer<URL>() {
			@Override
			public URL answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();

				String ppath = (String)args[0];
				String base = "file:///"+ realPath;
				String path = base + ppath;

				// We need to map some resources here
				if (ppath.startsWith("resources/")) {
					path = base + "/resources/" + ppath;
					path = path.replace("/webapp","");
				}
				
				if (ppath.startsWith("/WEB-INF/classes/resources/MailTemplates")) {
					ppath = ppath.replace("/WEB-INF/classes","");
					path = base + "/resources/" + ppath;
					path = path.replace("/webapp","");
				}
				
				if (ppath.startsWith("/WEB-INF/classes/resources/ExportTemplates")) {
					ppath = ppath.replace("/WEB-INF/classes","");
					path = base + "/resources/" + ppath;
					path = path.replace("/webapp","");
				}
				
//				if (ppath.startsWith("/WEB-INF/classes/resources") && !ppath.startsWith("/WEB-INF/classes/resources/ExportTemplates")) {
//					path = "file:///" + realPath.substring(0,realPath.lastIndexOf("/"))+"/"+ppath;
//					path = path.replace("/WEB-INF/classes/resources", "/resources/resources");
//				} else if (path.startsWith("file://") && path.contains("webapp/resources/")){
//					path = "file:///" + realPath + "/" + ppath;
//					path = path.replace("/webapp/resources/", "/resources/resources/");
//				}
				return new URL(path);
			}
		});
		
		when (ctx.getAttribute("javax.servlet.context.tempdir")).thenReturn(homeDir.getAbsolutePath());
		
		when (ctx.getRealPath(File.separator)).thenReturn(realPath);
		
	}
	
	public static boolean databaseIsIntialized() {
		return databaseIsInitialized;
	}
	
	public static void initializeDatabase() {
		if (!databaseIsInitialized) {
			try {
				createDBFromScratch();
			} catch (Exception e) {
				LOGGER.error("Could not intialize database:",e);
			}
		}
	}

	/**
	 * Creates a completely new database erasing previous ones and filled with data just
	 * as a fresh database after the application has been installed and started for the first time
	 * @throws Exception
	 */
	public static void createDBFromScratch() throws Exception {

		cleanDatabase();
		databaseIsInitialized = false;
		
		writeTorquePropsToHome();
		
		setupContext();

		ApplicationBean.getApplicationBean().setInTestMode(true);
		ApplicationBean.getApplicationBean().setAppType(1);
		
		UpdateDbSchema.doUpdateOrCreateFromScratch(ctx);
		InitDatabase.initDatabase("502", ctx);
		databaseIsInitialized = true;
	}

	private static void checkForDatabase() {
		try {
			BasicDataSource ds=new BasicDataSource();
			ds.setUsername(user);
			ds.setPassword(password);
			ds.setDriverClassName(getJdbcDriver());
			ds.setUrl(getJdbcUrl());
			testConnection();
			// createDatabaseInt(ds, fileNameSchema);
			System.out.println("Can access database");
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
	}

	private static boolean testConnection() {
		try {
			Class.forName(getJdbcDriver());
			con = DriverManager.getConnection(
					getJdbcUrl(),
					user, password);
			System.out.println("Sucessfully connected to Derby server.");
		} catch (Exception e) {
			System.out.println("Error getConnection");
			LOGGER.error(ExceptionUtils.getStackTrace(e));
			return false;
		}
		return true;
	}
}

