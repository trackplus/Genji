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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import com.aurel.track.Constants;
import com.aurel.track.GeneralSettings;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.util.PluginUtils;

public class HandleHome {

	public final static String TRACKPLUS_HOME="TRACKPLUS_HOME";
	public final static String LATEX_HOME = "LATEX_HOME";
	public final static String IMAGEMAGICK_HOME = "IMAGEMAGICK_HOME";
	public final static String PHANTOMJS_HOME = "PHANTOMJS_HOME";
	public final static String USER_LEVELS_FILE = "UserLevels.properties";
	public final static String GENERAL_SETTINGS_FILE = "GeneralSettings.properties";
	public final static String FILTER_SUBSCRIPTIONS_FILE = "FilterSubscriptions.properties";
	public final static String PDF_EXCEL_EXPORT_FILE = "PdfExcelExport.properties";
	public final static String TORQUE_FILE = "Torque.properties";
	public final static String QUARTZ_JOBS_FILE = "quartz-jobs.xml";
	public final static String LOG4J2_FILE = "log4j2.xml";

	//first label directories in TRACKPLUS_HOME
	public static final String DB_BACKUP_DIR = "dbBackup";
	public final static String LOG_DIR = "log";
	public final static String LOGOS_DIR = "logos";
	public final static String PLUGINS_DIR = "plugins";
	public static final String REPORT_TEMPLATES_DIR ="reportTemplates";
	public static final String WORD_TEMPLATES_DIR ="ExportTemplates"+File.separator+"wordTemplates";
	public static final String LATEX_TEMPLATES_DIR ="ExportTemplates"+File.separator+"latexTemplates";
	public static final String LANGUAGE_PROFILES_DIR ="LanguageDetectionProfiles";
	public static final String TEMP_DIR = "tmp";
	public static final String DATA_DIR = "trackdata";
	public final static String XRESOURCES_DIR = "xresources";
	public final static String SSO_DIR = "SSO";
	public final static String GANTT_EXPORT_RENDERER = "GanttExportRenderer";




	public final static String INIT_DATA = "/initData";
	private static String Trackplus_Home = null;

	private static final Logger LOGGER = LogManager.getLogger(HandleHome.class);



	private HandleHome() {
	}


	/**
	 * Initialize the pointer to the TRACKPLUS_HOME directory and copy
	 * the database connection configuration file there if required.
	 * @param context
	 * @throws ServletException
	 */
	public static void initTrackplus_Home(ServletContext context) throws ServletException {
		String home = getTrackplus_Home();
		LOGGER.info("Server current working directory is " + System.getProperty("user.dir"));
		copyPropertiesFile(context, TORQUE_FILE);
		copyPropertiesFile(context, FILTER_SUBSCRIPTIONS_FILE);
		copyPropertiesFile(context, GENERAL_SETTINGS_FILE);
		copyPropertiesFile(context, PDF_EXCEL_EXPORT_FILE);
		GeneralSettings.loadGeneralConfigs();
		copyAndMergeQuartzJobsFile(context, QUARTZ_JOBS_FILE);
		copyAndMergeLog4j2File(context, LOG4J2_FILE);

		copyObject(context, "resources/MailTemplates", "tracklogo.gif", LOGOS_DIR);  // old mail templates

		String logoSourceDir = INIT_DATA;

		switch (ApplicationBean.getInstance().getAppType()) {
		case ApplicationBean.APPTYPE_FULL:
			logoSourceDir  = logoSourceDir+"/full";
			break;
		case ApplicationBean.APPTYPE_DESK:
			logoSourceDir  = logoSourceDir+"/desk";
			break;
		case ApplicationBean.APPTYPE_BUGS:
			logoSourceDir  = logoSourceDir+"/bugs";
			break;
		default:
			logoSourceDir  = logoSourceDir+"/full";
		}

		LOGGER.info("Init data directory is " + logoSourceDir);

		logoSourceDir = logoSourceDir+"/Logos";

		copyObject(context, logoSourceDir, "trackLogo.png", LOGOS_DIR);
		copyObject(context, logoSourceDir, "logo-254x105.png", LOGOS_DIR); // report templates
		copyObject(context, logoSourceDir, "logo-98x40.png", LOGOS_DIR);  // mail templates
		copyObject(context, logoSourceDir, "logo-68x28.png", LOGOS_DIR);  // tool bar

		moveWordTemplates();
		copyExportTemplates(context, WORD_TEMPLATES_DIR);
		copyExportTemplates(context, LATEX_TEMPLATES_DIR);

		copyFAQTemplates(context);
		copySSOFolder(context);

		copyLanguageProfiles(context, LANGUAGE_PROFILES_DIR);

		File pluginDir = new File(home+File.separator+HandleHome.PLUGINS_DIR);
		PluginUtils.extractArchiveFromClasspath("/WEB-INF/classes/plugins",".*\\.tpx", pluginDir);
		PluginUtils.addPluginLocationsToClassPath(home);
		//init torque again to get the database access for external databases configured in plugins (included before in classpath. see addPluginLocationsToClassPath)
		InitDatabase.initTorque();
	}

	/**
	 * Copy the Torque.properties file to the TRACKPLUS_HOME directory
	 * @param context
	 * @throws ServletException
	 */
	public static void copyTorquePropertiesToHome(ServletContext context) throws ServletException {
		copyPropertiesFile(context, TORQUE_FILE);
	}

	private static void copyExportTemplates(ServletContext context, String templateBaseDir ) {
		String tpHome = getTrackplus_Home();
		File templatesDir = new File(tpHome+File.separator+templateBaseDir);

		URL rootTillFolder = null;
		String rootPathTillFolder = null;
		String templatePath = templateBaseDir.replace("\\", "/");
		try {
			if (context.getResource("/WEB-INF/classes/resources/"+ templatePath) != null) {
				rootTillFolder = context.getResource("/WEB-INF/classes/resources/"+ templatePath);
			} else if (context.getResource("/WEB-INF/classes/"+ templatePath) != null) {
				rootTillFolder = context.getResource("/WEB-INF/classes/"+ templatePath);
			} else  {
				rootTillFolder = new URL(context.getRealPath("../."));
			}
		}catch(IOException ioEx) {
			LOGGER.error(ExceptionUtils.getStackTrace(ioEx));
		}
		if (rootTillFolder != null) {
			rootPathTillFolder = rootTillFolder.getPath();
			if (rootPathTillFolder.contains("/WEB-INF")) {
				rootPathTillFolder = rootPathTillFolder.substring( rootPathTillFolder.indexOf( "/WEB-INF" ), rootPathTillFolder.length() );
			}
			Set<String> folderContent = context.getResourcePaths( rootPathTillFolder );
			if (folderContent!=null) {
				for (String fileNameWithPath : folderContent) {
					String fileName = fileNameWithPath.replace("/WEB-INF/classes/resources/"+templatePath+"/", "");
					if(fileName.endsWith(".docx") || fileName.endsWith(".tex")
						|| fileName.endsWith(".jpg") || fileName.endsWith(".png")
						|| fileName.endsWith(".tlx") || fileName.endsWith(".sh") || fileName.endsWith(".cmd")
						|| fileName.endsWith(".pdf")) {
						try {
							copyObject(context, "resources/"+templatePath, fileName, templateBaseDir);
						}catch(ServletException servEx) {
							LOGGER.error(ExceptionUtils.getStackTrace(servEx));
						}
					}

					if(fileName.endsWith(".sh") || fileName.endsWith(".cmd") ) {
						File fileToCopyInHome = new File(tpHome + File.separator + templateBaseDir + File.separator  + fileName);
						fileToCopyInHome.setExecutable(true);
					}

					if(fileName.endsWith(".zip") || fileName.endsWith(".tlx") ) {
						try {
							File fileToCopyInHome = new File(tpHome + File.separator + templateBaseDir + File.separator  + fileName);
								copyObject(context, "resources/"+templatePath, fileName, templateBaseDir);
								File fileToUnzip = new File(tpHome + File.separator + templateBaseDir + File.separator  + fileName);
								PluginUtils.unzipFileIntoDirectory(fileToUnzip, templatesDir);
						}catch(ServletException servEx) {
							LOGGER.error(ExceptionUtils.getStackTrace(servEx));
						}
					}
				}
			}
		}
	}

	private static void copyLanguageProfiles(ServletContext context, String templateBaseDir ) {
		String tpHome = getTrackplus_Home();
		File templatesDir = new File(tpHome+File.separator+templateBaseDir);

		URL rootTillFolder = null;
		String rootPathTillFolder = null;
		String templatePath = templateBaseDir.replace("\\", "/");
		try {
			if (context.getResource("/WEB-INF/classes/resources/"+ templatePath) != null) {
				rootTillFolder = context.getResource("/WEB-INF/classes/resources/"+ templatePath);
			} else if (context.getResource("/WEB-INF/classes/"+ templatePath) != null) {
				rootTillFolder = context.getResource("/WEB-INF/classes/"+ templatePath);
			} else  {
				rootTillFolder = new URL(context.getRealPath("../."));
			}
		}catch(IOException ioEx) {
			LOGGER.error(ExceptionUtils.getStackTrace(ioEx));
		}
		if (rootTillFolder != null) {
			rootPathTillFolder = rootTillFolder.getPath();
			if (rootPathTillFolder.contains("/WEB-INF")) {
				rootPathTillFolder = rootPathTillFolder.substring( rootPathTillFolder.indexOf( "/WEB-INF" ), rootPathTillFolder.length() );
			}
			Set<String> folderContent = context.getResourcePaths( rootPathTillFolder );

			for (String fileNameWithPath : folderContent) {
				String fileName = fileNameWithPath.replace("/WEB-INF/classes/resources/"+templatePath+"/", "");
				try {
					copyObject(context, "resources/"+templatePath, fileName, templateBaseDir);
				}catch(ServletException servEx) {
					LOGGER.error(ExceptionUtils.getStackTrace(servEx));
				}
			}
			try {
				FileUtils.deleteQuietly(new File(HandleHome.getTrackplus_Home()
						               +File.separator+LANGUAGE_PROFILES_DIR+File.separator+"hash.txt"));
			}catch(Exception e) {
				LOGGER.error(e.getMessage());
			}
		}
	}



	/**
	 * Returns the path to the TRACKPLUS_HOME directory, where
	 * configuration data is being kept.
	 * @return
	 */
	public static String getTrackplus_Home() {
		if (Trackplus_Home == null) {
			Connection con = null;
			Trackplus_Home = ""; // block  further processing from here by null length path
			// initialize from environment
			if (System.getProperty(TRACKPLUS_HOME) != null) { // first look at -DTRACKPLUS_HOME
				Trackplus_Home = System.getProperty(TRACKPLUS_HOME);
				LOGGER.info("Taking initial TRACKPLUS_HOME from Java VM argument -DTRACKPLUS_HOME="+Trackplus_Home);
			} else if (System.getenv(TRACKPLUS_HOME) != null){
				Trackplus_Home = System.getenv(TRACKPLUS_HOME); // then look at environ. variable
				LOGGER.info("Taking TRACKPLUS_HOME from environment variable as " + Trackplus_Home);
			} else {
				try {
					PropertiesConfiguration tcfg = loadServletContextPropFile(TORQUE_FILE,ApplicationBean.getInstance().getServletContext());
					Class.forName(tcfg.getString("torque.dsfactory.track.connection.driver"));
					con = DriverManager.getConnection(tcfg.getString("torque.dsfactory.track.connection.url"),
							tcfg.getString("torque.dsfactory.track.connection.user"),
							tcfg.getString("torque.dsfactory.track.connection.password"));
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT ATTACHMENTROOTDIR FROM TSITE");
					while (rs.next()) {
						Trackplus_Home = rs.getString(1);
					}
				} catch (Exception e) {
					LOGGER.error("Cannot access database table TSITE");
				} finally {
					if (con!=null) {
						try {
							con.close();
						} catch (SQLException e) {
							LOGGER.info("Closing the connection failed with " + e.getMessage());
							LOGGER.debug(ExceptionUtils.getStackTrace(e));
						}
					}
				}
			}
			if (Trackplus_Home != null && !"".equals(Trackplus_Home)) {
				File tph = new File(Trackplus_Home);
				if (!tph.exists()) { // try to create it
					try {
						tph.mkdirs();
					} catch (Exception e) {
						LOGGER.debug("We could not create directory " + Trackplus_Home + ".");
					}
				}
				if (!tph.isDirectory() || !tph.canWrite()) { // we need to be able to write to this directory
					LOGGER.error("We cannot write to the given TRACKPLUS_HOME directory at " + Trackplus_Home + ". Ignoring environment.");
					Trackplus_Home = ""; // block again by null length path
				}
			}

			if (Trackplus_Home == null || "".equals(Trackplus_Home)) {
				Trackplus_Home = System.getProperty("user.home")+File.separator+"trackplus";
				LOGGER.info("Creating TRACKPLUS_HOME at " + Trackplus_Home);
			}
		}
		return Trackplus_Home;
	}




	/**
	 * Set the TRACKPLUS_HOME directory, where configuration data is being kept.
	 * @param home
	 */
	public static void setTrackplus_Home(String home) {
		LOGGER.info("Setting TRACKPLUS_HOME to " + home);
		File f = new File(home);
		try {
			if (!f.exists()) {
				new File(home).mkdirs();
				LOGGER.info("Created new directory for TRACKPLUS_HOME: " + home);
				Trackplus_Home = home;
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}

	/**
	 * Get the path to the Groovy plugins
	 * @return
	 */
	public static String getGroovyPluginPath() {
		return getTrackplus_Home()
				+ File.separator + PLUGINS_DIR
				+ File.separator + "groovy";
	}

	/**
	 * Get the path to the workflow graphs temporary directory
	 * @return
	 */

	/*
	 * Cleans up the workflow directory from any old pictures that might
	 * not have been caught by the cleanup when the session was closed.
	 */

	/**
	 * Obtain the Torque.properties from TRACKPLUS_HOME or if not available from the WAR.
	 * @return
	 * @throws ServletException
	 */
	public static PropertiesConfiguration getTorqueProperties(ServletContext servletContext, boolean includePluginDatasources) throws ServletException{
		return getTorqueProperties(servletContext, TORQUE_FILE, includePluginDatasources);
	}

	/**
	 * Load the properties from the fileName properties file in TRACKPLUS_HOME, or if not available from the WAR.
	 * @return
	 * @throws ServletException
	 */
	public static PropertiesConfiguration getProperties(String fileName, ServletContext servletContext) throws ServletException{
		return loadProperties(fileName, servletContext);
	}

	/**
	 * Obtain the Torque.properties from TRACKPLUS_HOME or if not available from the WAR.
	 * @return
	 * @throws ServletException
	 */
	private static HashMap<String,PropertiesConfiguration> propConfigurations = new HashMap<String,PropertiesConfiguration>();

	/**
	 * This is a cached access!
	 * @param propFile
	 * @param servletContext
	 * @return
	 * @throws ServletException
	 */
	public static PropertiesConfiguration loadProperties(String propFile, ServletContext servletContext) throws ServletException{
		if (propConfigurations.get(propFile) != null) {
			return propConfigurations.get(propFile);
		}
		PropertiesConfiguration pc = getTrackplusHomePropFile(propFile);
		if (pc==null && servletContext!=null) { // if we could not get it from the environment
			pc = loadServletContextPropFile(propFile, servletContext);
		}
		propConfigurations.put(propFile,pc);
		return pc;
	}


	public static PropertiesConfiguration reloadProperties(String propFile, ServletContext servletContext) throws ServletException{
		PropertiesConfiguration pc = getTrackplusHomePropFile(propFile);
		if (pc==null && servletContext!=null) { // if we could not get it from the environment
			pc = loadServletContextPropFile(propFile, servletContext);
		}
		propConfigurations.put(propFile,pc);
		return pc;
	}



	/**
	 * Gets the PropertiesConfiguration for a property file from TRACKPLUS_HOME
	 * @param propFile
	 * Load a properties file from the class path
	 * @param propFile
	 * @param servletContext
	 * @return
	 * @throws ServletException
	 */
	public static Properties loadPropertiesFromClassPath(String propFile, ServletContext servletContext) throws ServletException{
		Properties props = null;
		InputStream in = null;
		try	{

			URL torqueURL = servletContext.getResource("/WEB-INF/"+propFile);
			in = torqueURL.openStream();
			props = new Properties();
			props.load(in);
			in.close();

		} catch (Exception e) {
			LOGGER.error("Could not read " + propFile+". Exiting. " + e.getMessage());
			System.err.println("Could not read " + propFile+". Exiting. " + e.getMessage());
			throw new ServletException(e);
		}
		return props;
	}

	/**
	 * Gets the PropertiesConfiguration for a property file from TRACKPLUS_HOME
	 * @param propFile
	 * Load a properties file from the class path
	 * @param propFile
	 * @param servletContext
	 * @return
	 * @throws ServletException
	 */
	public static List<String> loadFileFromClassPath(String propFile, ServletContext servletContext) throws ServletException{
		ArrayList<String> lines = new ArrayList<String>(100);
		InputStream in = null;
		try	{

			URL fileURL = servletContext.getResource("/WEB-INF/"+propFile);
			in = fileURL.openStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));

			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
			reader.close();

			in.close();

		} catch (Exception e) {
			LOGGER.error("Could not read " + propFile+". Exiting. " + e.getMessage());
			System.err.println("Could not read " + propFile+". Exiting. " + e.getMessage());
			throw new ServletException(e);
		}
		return lines;
	}

	/**
	 * Load a properties file from TRACKPLUS_HOME
	 * @param propFile
	 * @param servletContext
	 * @return
	 * @throws ServletException
	 */
	public static Properties loadPropertiesFromHome(String propFile) {
		Properties props = null;
		File fprops = null;
		InputStream in = null;
		String trackplusHome = HandleHome.getTrackplus_Home();
		try	{
			// First check if we have a configuration file pointed to by the environment
			if (trackplusHome!= null && !"".equals(trackplusHome)) {
				String fileName = trackplusHome + File.separator + propFile;
				fprops = new File(fileName);
				LOGGER.debug("Read file " + fileName);
				if (fprops.exists() && fprops.canRead()) {
					LOGGER.info("Retrieving configuration from " + fileName);
					in = new FileInputStream(fprops);
					props = new Properties();
					props.load(in);
					in.close();
				}
			}
		} catch (Exception e) {
			LOGGER.error("Could not read " + propFile+". Exiting. " + e.getMessage());
			System.err.println("Could not read " + propFile+". Exiting. " + e.getMessage());
		}
		return props;
	}

	/**
	 * Obtain the Torque.properties from TRACKPLUS_HOME or if not available from the WAR.
	 * @return
	 */
	public static PropertiesConfiguration getTrackplusHomePropFile(String propFile) {
		PropertiesConfiguration pc = null;
		File props = null;
		InputStream in = null;
		String trackplusHome = HandleHome.getTrackplus_Home();
		try	{
			// First check if we have a configuration file pointed to by the environment
			if (trackplusHome!= null && !"".equals(trackplusHome)) {
				String fileName = trackplusHome + File.separator + propFile;
				props = new File(fileName);
				LOGGER.debug("Read file " + fileName);
				if (props.exists() && props.canRead()) {
					LOGGER.info("Retrieving configuration from " + fileName);
					in = new FileInputStream(props);
					pc = new PropertiesConfiguration();
					pc.load(in);
					in.close();
				}
			}
		} catch (Exception e) {
			LOGGER.error("Could not read " + propFile+" from TRACKPLUS_HOME " + trackplusHome +". Exiting. " + e.getMessage());
		}
		return pc;
	}

	/**
	 * Gets the PropertiesConfiguration for a property file from servlet context
	 * @param propFile
	 * @param servletContext
	 * @return
	 * @throws ServletException
	 */
	public static PropertiesConfiguration loadServletContextPropFile(String propFile, ServletContext servletContext) throws ServletException {
		PropertiesConfiguration pc = null;
		InputStream in = null;
		URL propFileURL = null;
		try	{
			if (pc==null && servletContext!=null) {
				propFileURL = servletContext.getResource("/WEB-INF/"+propFile);
				in = propFileURL.openStream();
				pc = new PropertiesConfiguration();
				pc.load(in);
				in.close();
			}
		} catch (Exception e) {
			LOGGER.error("Could not read " + propFile + " from servlet context " + propFileURL==null?"":propFileURL.toExternalForm() + ". Exiting. " + e.getMessage());
			throw new ServletException(e);
		}
		return pc;
	}

	/**
	 * Obtain the Torque.properties from TRACKPLUS_HOME or if not available from the WAR.
	 * @param servletContext
	 * @param propFile
	 * @param includePluginDatasources whether the plugin datasources should be included
	 * @return
	 * @throws ServletException
	 */
	private static PropertiesConfiguration getTorqueProperties(ServletContext servletContext, String propFile, boolean includePluginDatasources) throws ServletException{
		PropertiesConfiguration dbcfg = loadProperties(propFile, servletContext);
		if (includePluginDatasources) {
			dbcfg = getMergedCrmConfiguration(dbcfg);
		}
		// Here we need to call plugins that need their own database configuration.
		return dbcfg;
	}


	/**
	 * Merge an existing Torque properties configuration with the one for the CRM custom
	 * field database.
	 * @param dbcfg the existing, original Genji Torque configuration
	 * @return
	 */
	private static boolean firstTimeMerge = true;
	public static PropertiesConfiguration getMergedCrmConfiguration(PropertiesConfiguration dbcfg) {
		String propFile = "CrmTorque.properties";
		ClassLoader cl = HandleHome.class.getClassLoader();
		InputStream in = null;
		try	{
			URL torqueURL = cl.getResource(propFile);
			in = torqueURL.openStream();
			if (dbcfg == null) {
				dbcfg = new PropertiesConfiguration();
			}
			dbcfg.load(in);
			in.close();
			if (firstTimeMerge) {
				LOGGER.info("Obtained another database configuration from " + propFile+".");
				firstTimeMerge = false;
			}
		}
		catch (Exception e) {
			String emsg = e.getMessage();
			if (emsg == null) {
				emsg = "";
			}
		}
		return dbcfg;
	}


	/**
	 * Obtain the Torque.properties from from TRACKPLUS_HOME or if not available from the WAR.
	 * @return
	 * @throws ServletException
	 */
	public static InputStream getStream(ServletContext servletContext, String fileName) throws ServletException{
		File file = null;
		InputStream in = null;
		try	{
			// First check if we have a configuration file pointed to by the environment
			if (HandleHome.getTrackplus_Home() != null && !"".equals(HandleHome.getTrackplus_Home())) {
				String fname = HandleHome.getTrackplus_Home();
				file = new File(fname + File.separator + fileName);
				LOGGER.debug("Probing for file " + fname + File.separator + fileName);
				if (file.exists() && file.canRead()) {
					LOGGER.info("Retrieving configuration from " + fname+File.separator+fileName);
					in = new FileInputStream(file);  // must be closed by caller
				}
			}

			if (in == null) { // if we could not get it from the environment
				URL fileURL = servletContext.getResource("/WEB-INF/" + fileName);
				in = fileURL.openStream();
			}
		} catch (Exception e) {
			LOGGER.error("Could not read " + fileName + ". Exiting. " + e.getMessage());
			System.err.println("Could not read " + fileName + ". Exiting. " + e.getMessage());
			throw new ServletException(e);
		}
		return in;
	}

	/**
	 * Copies properties file from the WAR to TRACKPLUS_HOME if it does not exist there yet.
	 * @return
	 * @throws ServletException
	 */
	public static void copyPropertiesFile(ServletContext servletContext, String propFile) throws ServletException{
		copyPropertiesFile (servletContext, "/WEB-INF", propFile);

	}

	/**
	 * Copies properties file from the WAR to TRACKPLUS_HOME if it does not exist there yet.
	 * @return
	 * @throws ServletException
	 */
	public static void copyPropertiesFile(ServletContext servletContext, String rootDir, String propFile) throws ServletException{
		Boolean markForCopy = false;
		File props = null;
		File propsDir = null;
		try	{
			// First check if we have a configuration file pointed to by the environment
			URL propFileURL = null;
			if (HandleHome.getTrackplus_Home() != null 	&& !"".equals(HandleHome.getTrackplus_Home())) {
				props = new File(HandleHome.getTrackplus_Home()+File.separator+propFile);
				propsDir = new File(HandleHome.getTrackplus_Home());
				if (props.exists() && props.canRead()) {
					propFileURL = new URL("file://"+HandleHome.getTrackplus_Home()+File.separator+propFile);
				} else {
					markForCopy = true; // We have a TRACKPLUS_HOME but no property file. We will copy it there.
					propFileURL = null;
				}
			}
			if (propFileURL == null) {
				propFileURL = servletContext.getResource(rootDir+"/"+propFile);
			}

			if (markForCopy) { // we copy the property file here when it does not exist yet
				if (propsDir != null && !propsDir.canWrite()) {
					LOGGER.error(propFile +" not writable to " + propsDir.getAbsolutePath() + " by user " + System.getProperty("user.name"));
					return;
				}
				if (props != null) {
					LOGGER.info("Copying configuration file " + propFile + " to " + props.getAbsolutePath());
					InputStream from = null;
					FileOutputStream to = null; // Stream to write to destination
					try {
						from = propFileURL.openStream(); // Create input stream
						to = new FileOutputStream(props); // Create output stream
						byte[] buffer = new byte[4096]; // To hold file contents
						int bytes_read; // How many bytes in buffer
						while ((bytes_read = from.read(buffer)) != -1)
							// Read until EOF
							to.write(buffer, 0, bytes_read); // write
					}
					// Always close the streams, even if exceptions were thrown
					finally {
						if (from != null)
							try {
								from.close();
							} catch (IOException e) {
							}
						if (to != null)
							try {
								to.close();
							} catch (IOException e) {
							}
					}
				}
			}
		}
		catch (Exception e) {
			LOGGER.error("Could not read " + propFile +". Exiting." + e.getMessage());
			throw new ServletException(e);
		}
	}

	/**
	 * This method copies resources/SSO folder and content from war into Trackplus_Home.
	 * @param context
	 * @throws IOException
	 * @throws ServletException
	 */
	public static void copySSOFolder(ServletContext context) {
		File ssoDirInHome = new File(HandleHome.getTrackplus_Home() + File.separator + SSO_DIR);
		if(!ssoDirInHome.exists()) {
			ssoDirInHome.mkdir();
			LOGGER.info("Creating folder for SSO in Track home: " + ssoDirInHome.getPath());
			URL rootTillFolder = null;
			String rootPathTillFolder = null;
			try {
				rootTillFolder = context.getResource("/WEB-INF/classes/resources/SSO");
			}catch(IOException ioEx) {
				LOGGER.error("Some errors happened while copying SSO folder from war into Track_Home!");
				LOGGER.error(ExceptionUtils.getStackTrace(ioEx));
			}
			rootPathTillFolder = rootTillFolder.getPath();
			rootPathTillFolder = rootPathTillFolder.substring( rootPathTillFolder.indexOf( "/WEB-INF" ), rootPathTillFolder.length() );
			Set<String> folderContent = context.getResourcePaths(rootPathTillFolder);
			String folderNameFound = null;
			for (String fileNameWithPath : folderContent) {
				if(fileNameWithPath.endsWith(".jar")) {
					LOGGER.info("Copying following jar file into Track_home/" + SSO_DIR + ": " +   fileNameWithPath);
					String fileName = fileNameWithPath.replace("/WEB-INF/classes/resources/SSO/", "");
					try {
						copyObject(context, "resources/SSO", fileName, SSO_DIR);
					}catch(ServletException servEx) {
						LOGGER.error("Some errors happened while copying SSO folder from war into Track_Home!");
						LOGGER.error(ExceptionUtils.getStackTrace(servEx));
					}
				}else {
					folderNameFound = fileNameWithPath.replace("/WEB-INF/classes/resources/SSO/", "");
				}
			}

			try {
				rootTillFolder = context.getResource("/WEB-INF/classes/resources/SSO/" + folderNameFound);
			}catch(IOException ioEx) {
				LOGGER.error("Some errors happened while copying SSO folder from war into Track_Home!");
				LOGGER.error(ExceptionUtils.getStackTrace(ioEx));
			}
			rootPathTillFolder = rootTillFolder.getPath();
			rootPathTillFolder = rootPathTillFolder.substring( rootPathTillFolder.indexOf( "/WEB-INF" ), rootPathTillFolder.length());
			folderContent = context.getResourcePaths(rootPathTillFolder);
			File ssoSubDirInHome = new File(HandleHome.getTrackplus_Home() + File.separator + SSO_DIR + File.separator + folderNameFound);
			if(!ssoSubDirInHome.exists()) {
				LOGGER.info("Creating sub folder in Track_home/SSO: " + ssoSubDirInHome.getPath());
				ssoSubDirInHome.mkdir();
			}
			for (String fileNameWithPath : folderContent) {
				String fileName = fileNameWithPath.replace("/WEB-INF/classes/resources/SSO/" + folderNameFound, "");
				LOGGER.info("Copying following file into  Track_home/SSO/" + folderNameFound + ": "  + fileNameWithPath);
				try {
					copyObject(context, "resources/SSO" + "/" + folderNameFound, fileName, SSO_DIR + "/" + folderNameFound);
				}catch(ServletException servEx) {
					LOGGER.error("Some errors happened while copying SSO folder from war into Track_Home!");
					LOGGER.error(ExceptionUtils.getStackTrace(servEx));
				}
			}
		}
	}

	/**
	 * Copies FAQ template structure from the WAR to TRACKPLUS_HOME if it does not exist there yet.
	 * @return
	 * @throws ServletException
	 */
	public static void copyFAQTemplates(ServletContext servletContext) throws ServletException{
		File faqTemplateDir = null;
		File faqDir = null;
		ZipFile templateZipFile = null;
		try	{
			// First check if we have a configuration file pointed to by the environment
			URL templateURL = null;
			if (HandleHome.getTrackplus_Home() != null 	&& !"".equals(HandleHome.getTrackplus_Home())) {
				faqTemplateDir = new File(HandleHome.getTrackplus_Home()+File.separator + "Faqs" + File.separator + "whc_template");
				faqDir = new File(HandleHome.getTrackplus_Home() + File.separator + "Faqs");

				if (!faqDir.exists() || !faqDir.isDirectory()) {
					if (faqDir != null && !faqDir.exists()) {
						faqDir.mkdirs();
					}
					if (faqDir != null && !faqDir.canWrite()) {
						LOGGER.error(faqDir +" not writable to " + faqDir.getAbsolutePath() + " by user " + System.getProperty("user.name"));
						return;
					}
				}

				if (!faqTemplateDir.exists() || !faqTemplateDir.isDirectory()) {
					templateURL = PluginUtils.class.getResource("/resources/reportTemplates/whc_template.zip");

					File faqTemplate = new File(HandleHome.getTrackplus_Home()+File.separator + "Faqs" + File.separator + "whc_template.zip");
					FileUtils.copyURLToFile(templateURL, faqTemplate);

					templateZipFile = new ZipFile(faqTemplate);
					Enumeration<? extends ZipEntry> entries = templateZipFile.getEntries();
					while (entries.hasMoreElements()) {
						ZipEntry entry = entries.nextElement();
						File entryDestination = new File(faqDir,  entry.getName());
						entryDestination.getParentFile().mkdirs();
						if (!entryDestination.isDirectory()) {
							InputStream in = templateZipFile.getInputStream(entry);
							OutputStream out = new FileOutputStream(entryDestination);
							IOUtils.copy(in, out);
							IOUtils.closeQuietly(in);
							IOUtils.closeQuietly(out);
						}
					}
				}
			}
		}
		catch (Exception e) {
			LOGGER.error("Could not read " + templateZipFile +". Ignoring. " + e.getMessage());
		}
	}

	/**
	 * Copies images, templates, etc. from the WAR to TRACKPLUS_HOME. Original files are
	 * overwritten unless they have been modified. Modification is checked via an MD5 digest.
	 * @throws ServletException
	 */
	public static void copyObject(ServletContext servletContext, String sourcePath, String file, String targetDir) throws ServletException{
		File targetFile = null;
		File targetDirAbsolutePath = null;
		try	{
			// First check if we already have a file there
			if (HandleHome.getTrackplus_Home() != null 	&& !"".equals(HandleHome.getTrackplus_Home())) {
				targetFile = new File(HandleHome.getTrackplus_Home()+File.separator + targetDir + File.separator + file);
				targetDirAbsolutePath = new File(HandleHome.getTrackplus_Home() + File.separator + targetDir);
			}

				if (targetDirAbsolutePath != null && !targetDirAbsolutePath.exists()) {
					targetDirAbsolutePath.mkdirs();
				}

				if (targetDirAbsolutePath != null && !targetDirAbsolutePath.canWrite()) {
					LOGGER.error(file +" not writable to " + targetDirAbsolutePath.getAbsolutePath() + " by user " + System.getProperty("user.name"));
					return;
			}

			URL sourceURL = null;
			try {
				sourceURL = servletContext.getResource(sourcePath + "/" + file);
			} catch (Exception e) {
				LOGGER.debug("Could not get file " + sourcePath +  "/" + file + " from context. Now trying classpath.");
			}

			if (sourceURL == null) {
				ClassLoader cl = HandleHome.class.getClassLoader();
				sourceURL = cl.getResource(sourcePath + "/" + file);
			}

			String hashTarget = "";
			String hashSource = computeHash(sourceURL);
			boolean copy = true;

			if (targetFile.exists() && targetFile.canRead()) {
				hashTarget = computeHash(targetFile);
				if (!fileIsTheOriginal(targetFile, hashTarget)
					|| hashSource.equals(hashTarget)) {
						copy = false;
				}
				}

			writeHash(targetFile,hashSource);

			if (copy) {
					LOGGER.info("Copying file " + sourcePath+"/"+file + " to " + targetFile.getAbsolutePath());
					InputStream from = null;
					FileOutputStream to = null; // Stream to write to destination

					try {
					from = sourceURL.openStream(); // Create input stream
					to = new FileOutputStream(targetFile); // Create temporary output stream
						byte[] buffer = new byte[4096]; // To hold file contents
						int bytes_read; // How many bytes in buffer
						while ((bytes_read = from.read(buffer)) != -1)
							// Read until EOF
							to.write(buffer, 0, bytes_read); // write
					}
					// Always close the streams, even if exceptions were thrown
					finally {
						if (from != null)
							try {
								from.close();
							} catch (IOException e) {
							}
						if (to != null)
							try {
								to.close();
							} catch (IOException e) {
							}
					}
				}
		}
		catch (Exception e) {
			LOGGER.error("Could not read " + file +". Exiting: " + e.getMessage());
			LOGGER.error(ExceptionUtils.getStackTrace(e));
			throw new ServletException(e);
		}
	}

	public static void initGroovyPlugins(ServletContext servletContext) {
		URL groovyURL = null;
		try {
			groovyURL = servletContext.getResource("/WEB-INF/" + TORQUE_FILE);
			String path = groovyURL.toExternalForm();
			path = path.substring(0,path.lastIndexOf(TORQUE_FILE)-1)
					+ File.separator+"classes"
					+ File.separator+PLUGINS_DIR
					+ File.separator+"groovy"
					+ File.separator;
			groovyURL = new URL(path);
		}
		catch (Exception ge) {
			System.err.println("Can't get the Groovy URL"); // What can we do here?
		}

		Constants.setGroovyURL(groovyURL);
	}

	/**
	 * We reorganized the directory structure from 5.0.1 to 5.0.2
	 */
	private static void moveWordTemplates() {

		File sourceDir = new File(HandleHome.getTrackplus_Home()+File.separator+"wordTemplates");
		File targetDir = new File(HandleHome.getTrackplus_Home()+File.separator+WORD_TEMPLATES_DIR);

		try {
			if (sourceDir.exists() && ! targetDir.exists()) {
				FileUtils.moveDirectory(sourceDir, targetDir);
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage());;
		}
	}



	/**
	 * Copies quartz-jobs.xml file from the WAR to TRACKPLUS_HOME if it does not exist there yet.
	 * If it exists there check if it contains the password token clean job and if not add it
	 * to the file.
	 * @return
	 * @throws ServletException
	 */
	public static void copyAndMergeQuartzJobsFile(ServletContext servletContext,  String propFile) throws ServletException {
		Boolean markForCopy = false;
		Boolean markForMerge = false;
		File props = null;
		File propsDir = null;
		try	{
			// First check if we have a configuration file pointed to by the environment
			if (HandleHome.getTrackplus_Home() != null 	&& !"".equals(HandleHome.getTrackplus_Home())) {
				props = new File(HandleHome.getTrackplus_Home()+File.separator+propFile);
				propsDir = new File(HandleHome.getTrackplus_Home());
				if (props.exists() && props.canRead()) {
					markForMerge = true;
				} else {
					markForCopy = true; // We have a TRACKPLUS_HOME but no property file. We will copy it there.
				}
			}

			if (markForCopy) {
				copyPropertiesFile(servletContext,propFile);
				return;
			}

			if (markForMerge) {
				if (propsDir != null && !propsDir.canWrite()) {
					LOGGER.error(propFile +" not writable to " + propsDir.getAbsolutePath() + " by user " + System.getProperty("user.name"));
					return;
				}
				String propFilePath = HandleHome.getTrackplus_Home()+File.separator+propFile;

				LOGGER.info("Merging configuration file " + propFile + " to " + props.getAbsolutePath());
				try {
					String fileData = FileUtils.readFileToString(new File(propFilePath), "UTF-8");

					if (fileData.indexOf("<name>RegistrationPasswordCleanerJob</name>") <= 0) {

						String replacement =
								"      <job>\n"
										+"            <name>RegistrationPasswordCleanerJob</name>\n"
										+"            <group>DefaultJobGroup</group>\n"
										+"            <description>Remove unconfirmed users and password reset requests</description>\n"
										+"            <job-class>com.aurel.track.dbase.jobs.RegistrationPasswordCleanerJob</job-class>\n"
										+"            <job-data-map>\n"
										+"                <entry>\n"
										+"                    <key>dummyParam1</key>\n"
										+"                    <value>dummyValue</value>\n"
										+"                </entry>\n"
										+"            </job-data-map>\n"
										+"		  </job>\n"
										+"        <trigger>\n"
										+"            <cron>\n"
										+"                <name>RegistrationPasswordCleanerTrigger</name>\n"
										+"                <group>DefaultTriggerGroup</group>\n"
										+"                <job-name>RegistrationPasswordCleanerJob</job-name>\n"
										+"                <job-group>DefaultJobGroup</job-group>\n"
										+"                <cron-expression>0 30 23 * * ?</cron-expression>\n"
										+"            </cron>\n"
										+"        </trigger>\n"
										+"    </schedule>";

						fileData = fileData.replace("</schedule>", replacement);

						FileUtils.write(new File(propFilePath), fileData, "UTF-8", false);

					}

				} catch (Exception e) {
					LOGGER.error(e.getMessage());
				}
			}

		}
		catch (Exception e) {
			LOGGER.error("Could not read " + propFile +". Exiting." + e.getMessage());
			throw new ServletException(e);
		}
	}

	/**
	 * Copies log4j2.xml file from the WAR to TRACKPLUS_HOME if it does not exist there yet.
	 * In this process it changes the location of the logger output.
	 * @return
	 * @throws ServletException
	 */
	public static void copyAndMergeLog4j2File(ServletContext servletContext,  String propFile) throws ServletException {
		Boolean markForCopy = false;
		Boolean markForMerge = false;
		File props = null;
		File propsDir = null;
		try	{
			// First check if we have a configuration file pointed to by the environment
			if (HandleHome.getTrackplus_Home() != null 	&& !"".equals(HandleHome.getTrackplus_Home())) {
				props = new File(HandleHome.getTrackplus_Home()+File.separator+propFile);
				propsDir = new File(HandleHome.getTrackplus_Home());
				if (props.exists() && props.canRead()) {
					return;
				} else {
					markForCopy = true; // We have a TRACKPLUS_HOME but no property file. We will copy it there.
				}
			}

			if (markForCopy) {
				copyObject(servletContext,"",propFile,"");

				if (propsDir != null && !propsDir.canWrite()) {
					LOGGER.error(propFile +" not writable to " + propsDir.getAbsolutePath() + " by user " + System.getProperty("user.name"));
					return;
				}
				String propFilePath = HandleHome.getTrackplus_Home()+File.separator+propFile;

				LOGGER.info("Merging configuration file " + propFile + " to " + props.getAbsolutePath());
				try {
					String fileData = FileUtils.readFileToString(new File(propFilePath), "UTF-8");
					fileData = fileData.replace("${sys:java.io.tmpdir}", getTrackplus_Home());
					FileUtils.write(new File(propFilePath), fileData, "UTF-8", false);
				} catch (Exception e) {
					LOGGER.error(e.getMessage());
				}

			}
		}
		catch (Exception e) {
			LOGGER.error("Could not read " + propFile +". Exiting." + e.getMessage());
			throw new ServletException(e);
		}

	}

	/*
	 * Compute an  MD5 digest for a file.
	 *
	 */
	public static String computeHash(File file) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");

			InputStream is = new FileInputStream(file);
			byte[] buffer = new byte[4096]; // To hold file contents

			DigestInputStream dis = new DigestInputStream(is, md);
			while (dis.read(buffer) != -1) {

			}

			byte[] digest = md.digest();

			dis.close();

			String hash = DatatypeConverter.printHexBinary(digest);

			return hash;

		} catch (Exception e) {

		}
		return null;
	}

	/*
	 * Compute an  MD5 digest for a file.
	 *
	 */
	public static String computeHash(URL url) {

		InputStream from = null;
		String hash = "";

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			from = url.openStream(); // Create input stream
			byte[] buffer = new byte[4096]; // To hold file contents
			DigestInputStream dis = new DigestInputStream(from, md);
			while (dis.read(buffer) != -1) {

			}

			byte[] digest = md.digest();

			dis.close();
			from.close();

			hash = DatatypeConverter.printHexBinary(digest);

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		// Always close the stream, even if exceptions were thrown
		finally {
			if (from != null)
				try {
					from.close();
				} catch (IOException e) {
				}
		}
		return hash;
	}


	/**
	 * Checks if the file with this name has been modified in the meantime. For this we compute the files
	 * hash value and look if it exists in the hash.txt file where we keep them.
	 * @param existingFile
	 * @param targetDirAbsolutePath
	 * @return
	 */
	public static boolean fileIsTheOriginal(File existingFile, String hash) {
		BufferedReader br = null;
		try {
			File hashFile = new File(existingFile.getParent()+File.separator+"hash.txt");
			if (hashFile.exists()) {
				br = new BufferedReader(new FileReader(hashFile));
				String line;
				String fname = "";
			while ((line = br.readLine()) != null) {
					String[] keyv = line.split("=");
					if (keyv != null && keyv.length > 1 && keyv[1] != null) {
						fname = keyv[0];
						if (fname.equals(existingFile.getName()) && hash.equals(keyv[1])) {
					br.close();
					return true;
				}
			}
				}
			br.close();
			}
		} catch (Exception e) {
			LOGGER.warn(e.getMessage());
		}
		return false;
	}

	/*
	 * Update the hash.txt file
	 */
	public static void writeHash(File existingFile, String hash) {
		try {
			File hashFile = new File(existingFile.getParent()+File.separator+"hash.txt");
			Map<String,Object> hashMap= new HashMap<String,Object>();
			if (hashFile.exists()) {
				String fname = "";
				List<String> hashes = FileUtils.readLines(hashFile);
				for (String fhash: hashes) {
					String[] keyv = fhash.split("=");
					if (keyv != null && keyv.length > 1 && keyv[1] != null) {
						fname = keyv[0];
						hashMap.put(fname, keyv[1]);
					}
				}
			}
			hashMap.put(existingFile.getName(),hash);
			PrintWriter output;
			output = new PrintWriter(new FileWriter(hashFile, false));

			for (String key: hashMap.keySet()) {
				output.println(key + "=" + hashMap.get(key));
			}
			output.close();
		} catch (Exception e) {
			LOGGER.warn(e.getMessage());
		}
	}

	public static File getMissingLaTeXPdf() {
		return new File(getTrackplus_Home()+File.separator+LATEX_TEMPLATES_DIR+File.separator+"missing.pdf");
	}
}
