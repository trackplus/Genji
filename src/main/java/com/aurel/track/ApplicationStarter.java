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


package com.aurel.track;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.torque.Torque;
import org.apache.torque.TorqueException;

import com.aurel.track.admin.server.dbbackup.DatabaseBackupBL;
import com.aurel.track.beans.TClusterNodeBean;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.cluster.ClusterBL;
import com.aurel.track.cluster.ClusterUpdateBL;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.SiteDAO;
import com.aurel.track.dbase.HandleHome;
import com.aurel.track.dbase.InitDatabase;
import com.aurel.track.dbase.InitReportTemplateBL;
import com.aurel.track.dbase.JobScheduler;
import com.aurel.track.dbase.UpdateDbSchema;
import com.aurel.track.dbase.jobs.FileMonitor;
import com.aurel.track.fieldType.types.FieldTypeDescriptorUtil;
import com.aurel.track.item.lock.ItemLockBL;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.index.LuceneIndexer;
import com.aurel.track.persist.BaseTSitePeer;
import com.aurel.track.persist.TLoggingLevelPeer;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.ResourceBundleManager;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.PluginUtils;
import com.aurel.track.util.Support;
import com.aurel.track.util.event.EventPublisher;
import com.aurel.track.util.event.IEventSubscriber;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.trackplus.license.LicenseManager;

/**
 * ApplicationStarter initializes the Genji application. This includes
 * <ul>
 * <li>Creating the database structure if it is not available
 * <li>Updating the database structure in case it has changed
 * <li>Filling the database with default values
 * <li>Copying files like logos and templates to their proper places
 * <li>Initializing the full text search engine
 * <li>Initializing the job scheduling subsystem
 * </ul>
 * After ApplicationStarter is done, the Struts2 Action servlet serves the user
 * at the browser or web service interface as defined in file
 * <code>struts.xml</code>.
 *
 */
public final class ApplicationStarter implements Runnable {

	private static final Logger LOGGER = LogManager.getLogger(ApplicationStarter.class);

	private static ApplicationStarter instance = null;

	// The second number in the arrays below are the total percentage
	// complete allocated to this initialization step. The first number
	// is usually not used.
	public static final int[] DB_TRACK_SCHEMA = { 0, 20 };

	public static final int[] INIT_DB_DATA = { 0, 10 };
	// Increment percentage complete with each call by this amount
	public static final int INIT_DB_DATA_STEP = INIT_DB_DATA[1] / 8;

	public static final int[] RESOURCE_UPGRADE = { 0, 60 };

	public static final int[] DB_DATA_UPGRADE = { 0, 10 };

	// Increment percentage complete with each call by this amount
	public static final int DB_DATA_UPGRADE_STEP = DB_DATA_UPGRADE[1] / 10;

	public static final String PERCENT_COMPLETE = "PERCENT_COMPLETE";
	public static final String PROGRESS_TEXT = "PROGRESS_TEXT";
	public static final String READY = "READY";

	public static String DB_SCHEMA_UPGRADE_SCRIPT_TEXT = "Executing script "; // "starter.dbSchemaScript";
	public static String DB_SCHEMA_UPGRADE_READY_TEXT = "Database schema is up to date";// "starter.dbSchema";
	public static String INIT_DB_DATA_TEXT = "Initialize database data";// "starter.initData";
	public static String RESOURCE_UPGRADE_LOCALE_TEXT = "Loading resource for locale ";// "starter.resourceForLocale";
	public static String RESOURCE_UPGRADE_DEFAULT_LOCALE_TEXT = "Loading resources for default locale";// "starter.resourceForDefaultLocale";
	public static String RESOURCE_UPGRADE_READY_TEXT = "Localized resources are loaded ";// "starter.resource";
	public static String DATA_UPGRADE_TO_TEXT = "Migrating data to ";// "starter.dbDataUpgradeTo";
	public static String DATA_UPGRADE_READY_TEXT = "Migrating data ready";// "starter.dbDataUpgrade";
	public static String REPORT_COPY_READY_TEXT = "Copying report templates...";// "starter.reportTemplates";
	public static String READY_TEXT = "starter.ready";
	public static String PLEASE_TEXT = "Please give us a moment...";
	public static String WAIT_TEXT = "to initialize the system. This can take a few minutes.";
	public static String TITLE = "Application initializing";

	private int actualProgress = 0;

	private static final int REPORT_COPY = 2;// resource upgrade is ready

	private SiteDAO siteDAO = DAOFactory.getFactory().getSiteDAO();

	private transient ExecutorService executor = null;

	private ServletConfig servletConfig = null;

	private ApplicationBean appBean = null;

	private Boolean serverIsReady = false;

	// ---------------------------------------------------- HttpServlet Methods

	/*
	 * Hidden constructor for singleton pattern
	 */
	private ApplicationStarter() {

	}

	/**
	 * Get a singleton instance of this class.
	 * 
	 * @return the one and only ApplicationStarter instance of this JVM.
	 */
	public static ApplicationStarter getInstance() {
		if (instance == null) {
			instance = new ApplicationStarter();
		}
		return instance;
	}

	/**
	 * Initialize before use.
	 * 
	 * @param scfg
	 *            the servlet configuration
	 * @param _executor
	 *            the executor running everything in its own thread.
	 */
	public void init(ServletConfig scfg, ExecutorService _executor) {
		setServletConfig(scfg);
		executor = _executor;
	}

	/*
	 * starter.dbSchema=Database schema is up-to-date
	 * starter.dbSchemaScript=Executing script {0}...
	 * starter.dbDataUpgrade=Migrating data ready
	 * starter.dbDataUpgradeTo=Migrating data to {0}...
	 * starter.ready=Application is ready starter.resource=Resources are loaded
	 * starter.reportTemplates=Copying report templates...
	 * starter.resourceForLocale=Loading resource for locale {0}...
	 */

	/** Initializes the application */
	@Override
	public void run() {
		setLoaderResourceBundleMessages();
		ServletContext servletContext = getServletConfig().getServletContext();
		appBean = initApplicationBeanStep1(servletContext);
		
		LOGGER.info("-------------------------------------------------------------");
		LOGGER.info(ApplicationBean.getInstance().getAppTypeString() + ": System initializaton for version " + appBean.getVersion() + " build " + appBean.getBuild() + " started...");
		LOGGER.info("TRACKPLUS_HOME set to " + HandleHome.getTrackplus_Home());
		
		updateOrCreateDbSchema(servletContext);
		TSiteBean site = initDatabaseAndAdjustHome(appBean.getVersion(), servletContext);
		
		initExtraLoggers(servletContext);
		
		printSystemInfo();
		Support.loadLastURIs();
		try {
			setColumnSizes();
			InitDatabase.upgradeDatabase(site, servletContext, appBean.getVersion(), appBean.getBuild());
		} catch (Exception e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			ApplicationBean.getInstance().setInstallProblem(createInstallProblemMessage(e));
			LOGGER.error("Problem when printing system info or upgrading database.");
			emergencyExit(e);
		}
		// Initialize the length of short descriptions in the report page
		initDescriptionLength();
		initEmailSubsystem();
		initCookieTimeout();
		initLDAP();
		initWebService();
		// Update the versions of the application and database scheme.
		updateVersions(/* site, */servletContext);
		LuceneUtil.initLuceneParameters();

		site = siteDAO.load1();
		appBean = initLicSys(site);

		ClusterBL.processCluster(false);
		String myIp = ClusterBL.getIPAddress();
		if (ApplicationBean.getInetAddress() != null) {
			TClusterNodeBean clusterNodeBean = ClusterBL.loadByIP(myIp);
			appBean.setClusterNodeBean(clusterNodeBean);
		}

		HandleHome.initGroovyPlugins(servletContext);
		Constants.setGroovyScriptEngine();
		servletContext.setAttribute("SITECONFIG", site);

		servletContext.setAttribute(Constants.APPLICATION_BEAN, appBean);

		initJobs(servletContext);
	
		getDesignPaths(servletContext);
		servletContext.setAttribute("FirstTime", "FT");
		
		initLucene(myIp);

		EventPublisher evp = EventPublisher.getInstance();
		EventPublisher.init(); // only first time, don't repeat

		if (evp != null) {
			List<Integer> events = new ArrayList<Integer>();
			events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_SYSTEM_STARTED));
			evp.notify(events, site);
		}

		// --------------------------------------------------------------
		// Here we provide a chance to do some neat stuff after the
		// system initialization is complete
		// binding = new Binding();
		// binding.setVariable("site", site);
		// binding.setVariable("servletContext", servletContext);
		// try {
		// Constants.getGroovyScriptEngine().run("system/initOnExit.groovy",
		// binding);
		// LOGGER.info("Groovy test: TRACKPLUS_HOME variable set to " +
		// binding.getVariable("output"));
		// }
		// catch (Exception ge) {
		// LOGGER.warn("Could not read Groovy exit script at " +
		// Constants.getGroovyURL());
		// }

		//
		// Verify the reportTemplates
		//
		actualizePercentComplete(ApplicationStarter.REPORT_COPY, ApplicationStarter.REPORT_COPY_READY_TEXT);
		InitReportTemplateBL.verifyReportTemplates();

		// remove stuck locks
		ItemLockBL.removeAllLocks();

		Set<String> fieldTypeResourceBundleNames = FieldTypeDescriptorUtil.getCustomFieldTypeResourceBundles();
		if (fieldTypeResourceBundleNames != null) {
			for (String resourceBundleName : fieldTypeResourceBundleNames) {
				// add the resource bundles specified in the fieldType
				// plugins
				// to the struts' default resource bundles.
				// needed for example to report the conversion errors
				// during configuring the custom fields
				// (by conversion error text there is not possible to
				// specify the bundle explicitly)
				LocalizedTextUtil.addDefaultResourceBundle(resourceBundleName);
			}
		}
		actualizePercentComplete(100 - getPercentComplete(), ApplicationStarter.READY_TEXT);
		String version = site.getTrackVersion();
		LOGGER.info(appBean.getAppTypeString() + " system version " + version + " Build " + appBean.getBuild() + " started...");

		// ------------------------------------------------------------
		// Initialize general logging with settings from the database
		initGeneralLogging(servletContext);

		servletContext.setAttribute(READY, new Boolean(true));
		serverIsReady = true;
		try {
			FileMonitor.monitor();
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}

		// This is for unit test purposes
		File lock = new File(HandleHome.getTrackplus_Home() + "/lock");
		if (lock.exists()) {
			lock.delete();
		}
		try {
			DetectorFactory.loadProfile(HandleHome.getTrackplus_Home()+File.separator+"LanguageDetectionProfiles");
		} catch (Exception e) {
			LOGGER.warn("Unable to load language profiles: " + e.getMessage());
		}
	}
	
	// -------------------- End of startup, details follow----------------------------- //

	/**
	 * This method updates the percent complete bar shown during system startup
	 * @param percentComplete
	 * @param progressText
	 */
	public void actualizePercentComplete(Integer percentComplete, String progressText) {
		actualProgress = actualProgress + percentComplete;
		ApplicationBean.getInstance().getServletContext().setAttribute(PERCENT_COMPLETE, actualProgress);
		ApplicationBean.getInstance().getServletContext().setAttribute(PROGRESS_TEXT, progressText);
	}

	/**
	 * Get the progress text shown during system initialization in the browser
	 * @return
	 */
	public String getProgressText() {
		ServletContext scx = ApplicationBean.getInstance().getServletContext();
		if (scx != null) {
			return (String) scx.getAttribute(PROGRESS_TEXT);
		}
		return "";
	}

	public int getPercentComplete() {
		ServletContext scx = ApplicationBean.getInstance().getServletContext();
		if (scx != null) {
			Integer pc = (Integer) scx.getAttribute(PERCENT_COMPLETE);
			if (pc != null) {
				return pc;
			}
		}
		return 0;
	}

	private void setLoaderResourceBundleMessages() {
		// request not available yet
		ResourceBundle rb = ResourceBundle.getBundle(ResourceBundleManager.LOADER_RESOURCES, Locale.getDefault()); // getLoaderResourceBundle
																													// ResourceBundle.getBundle("resources/UserInterface/LoaderResources",
																													// locale);
		DB_SCHEMA_UPGRADE_SCRIPT_TEXT = rb.getString("DB_SCHEMA_UPGRADE_SCRIPT_TEXT"); // "Executing
																						// script
																						// ";
		// //"starter.dbSchemaScript";
		DB_SCHEMA_UPGRADE_READY_TEXT = rb.getString("DB_SCHEMA_UPGRADE_READY_TEXT"); // "Database
																						// schema
																						// is
																						// actual";//"starter.dbSchema";
		INIT_DB_DATA_TEXT = rb.getString("INIT_DB_DATA_TEXT"); // "Initialize
																// database
																// data";//"starter.initData";
		RESOURCE_UPGRADE_LOCALE_TEXT = rb.getString("RESOURCE_UPGRADE_LOCALE_TEXT"); // "Loading
																						// resource
																						// for
																						// locale
																						// ";//"starter.resourceForLocale";
		RESOURCE_UPGRADE_DEFAULT_LOCALE_TEXT = rb.getString("RESOURCE_UPGRADE_DEFAULT_LOCALE_TEXT"); // "Loading
																										// resources
																										// for
																										// default
																										// locale";//"starter.resourceForDefaultLocale";
		RESOURCE_UPGRADE_READY_TEXT = rb.getString("RESOURCE_UPGRADE_READY_TEXT"); // "Localized
																					// resources
																					// are
																					// loaded
																					// ";//"starter.resource";
		DATA_UPGRADE_TO_TEXT = rb.getString("DATA_UPGRADE_TO_TEXT"); // "Migrating
																		// data
																		// to
																		// ";//"starter.dbDataUpgradeTo";
		DATA_UPGRADE_READY_TEXT = rb.getString("DATA_UPGRADE_READY_TEXT"); // "Migrating
																			// data
																			// ready";//"starter.dbDataUpgrade";
		REPORT_COPY_READY_TEXT = rb.getString("REPORT_COPY_READY_TEXT"); // "Copying
																			// report
																			// templates...";//"starter.reportTemplates";
		READY_TEXT = rb.getString("READY_TEXT"); // "starter.ready";
		PLEASE_TEXT = rb.getString("please"); // "starter.ready";
		WAIT_TEXT = rb.getString("waitMinutes"); // "starter.ready";
		TITLE = rb.getString("title"); // "starter.ready";
	}

	public Boolean isServerReady() {
		return serverIsReady;
	}

	// ======================================================================
	// ======================================================================
	// ======================================================================
	/**
	 * Gracefully shut down this thread, releasing any resources that were
	 * allocated at initialization
	 */
	public void destroy() {
		System.out.println("Going down...");
		LOGGER.info("Going down...");

		JobScheduler.stopScheduler();
		FileMonitor.removeFileWatcher();

		appBean.removeClusterNode();

		ApplicationBean.getInstance().getExecutor().shutdown();

		getServletConfig().getServletContext().removeAttribute(Constants.DATABASE_KEY);
		getServletConfig().getServletContext().removeAttribute(Constants.APPLICATION_BEAN);
		getServletConfig().getServletContext().removeAttribute("SITECONFIG");
		// lucene: close all the IndexModifiers to release the locks
		LuceneIndexer.closeWriters();
		// shutdown torque
		try {
			LOGGER.debug("Shutdown torque");
			Torque.shutdown();
		} catch (TorqueException e) {
			LOGGER.error("Stopping torque failed! " + e);
		}

		EventPublisher evp = EventPublisher.getInstance();
		if (evp != null) {
			List<Integer> events = new LinkedList<Integer>();
			events.add(Integer.valueOf(IEventSubscriber.EVENT_PRE_SYSTEM_STOPPED));
			evp.notify(events, null);
		}
		System.out.println("System down!");
	}

	// -----------------------------------------------------------------------
	/*
	 * Initializes the logging system
	 */
	private void initExtraLoggers(ServletContext servletContext) {
		try {			
			Configurator.initialize(null, HandleHome.getTrackplus_Home()+File.separator+HandleHome.LOG4J2_FILE);	
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
	}

	/*
	 * Initialize general logging with settings from the database
	 */
	private void initGeneralLogging(ServletContext servletContext) {
		try {
			
			TLoggingLevelPeer.load(); // make sure the database settings prevail
			LOGGER.info("Loaded logging settings from database");
			
		} catch (Exception e) {
			LOGGER.error("Logging initialization failed: " + e.getMessage(), e);
		}
	}


	private ApplicationBean initApplicationBeanStep1(ServletContext servletContext) {
		ApplicationBean applicationBean = ApplicationBean.getInstance();
		setVersionFromVersionProperties(servletContext);
		applicationBean.setServletContext(servletContext);
		applicationBean.setExecutor(executor);
		actualizePercentComplete(0, "");

		if (servletContext.getAttribute("INTEST") != null && (Boolean) servletContext.getAttribute("INTEST")) {
			applicationBean.setInTestMode(true);
		}

		return applicationBean;
	}

	// -------------------------------------------------------------------------
	/*
	 * Set the short description length in reports, has historic reasons.
	 */
	private void initDescriptionLength() {
		int dlength = 0;
		if (InitDatabase.getFirstTime()) {
			dlength = 160;
			Map<Integer, Object> siteBeanValues = new HashMap<Integer, Object>();
			siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.DESCRIPTIONLENGTH, new Integer(dlength));
			siteDAO.loadAndSaveSynchronized(siteBeanValues);
		}
	}

	// --------------------------------------------------------------------
	/*
	 *
	 */
	private void initEmailSubsystem() {
		Map<Integer, Object> siteBeanValues = new HashMap<Integer, Object>();
		TSiteBean siteBean = siteDAO.load1();
		// --------------------------------------------------------------------
		// Initialize the Genji email account and domain. Under this
		// e-mail account e-mails are being sent
		//
		String sendFrom = null;
		if (InitDatabase.getFirstTime() && (siteBean.getTrackEmail() == null || "".equals(siteBean.getTrackEmail()))) {
			sendFrom = "";
			siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.TRACKEMAIL, sendFrom);
		}
		sendFrom = siteBean.getTrackEmail();

		// ---------------------------------------------------------------------
		// Initialize the email subsystem
		//
		if (InitDatabase.getFirstTime()) {
			siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.SMTPSERVERNAME, "");
			siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.SMTPPORT, new Integer(25));
			siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.SMTPUSER, "");
			siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.SMTPPASSWORD, "");
			siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.POPSERVERNAME, "");
			siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.POPPORT, new Integer(110));
			siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.POPUSER, "");
			siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.POPPASSWORD, "");
			siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.MAILENCODING, "UTF8");
		}

		// ---------------------------------------------------------------------
		// Initialize the allowed email domain pattern
		//
		String emailPattern = "\\w[\\-.\\w]+\\@\\w[\\-.\\w]+\\.\\w{2,3}";
		if (InitDatabase.getFirstTime()) {
			siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.ALLOWEDEMAILPATTERN, emailPattern);
		}

		if (siteBean.getAllowedEmailPattern() == null || "".equals(siteBean.getAllowedEmailPattern())) {
			siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.ALLOWEDEMAILPATTERN, emailPattern);

		}
		siteDAO.loadAndSaveSynchronized(siteBeanValues);
		if (InitDatabase.getFirstTime()) {
			siteBean = siteDAO.load1();
			siteBean.setUseLucene("true");
			siteBean.setReindexOnStartup("true");
			siteBean.setIsVersionReminderOn(true);
			siteBean.setSummaryItemsBehavior(true);
		}
	}

	// ------------------------------------------------------------------------
	/*
	 * Initializes the cookie timeout for automated authentication.
	 */
	private void initCookieTimeout() {
		Constants.setCookieTimeout(24 * 7 * 3600);
	}

	// ------------------------------------------------------------------
	/*
	 * Initializes LDAP related stuff
	 */
	private void initLDAP() {
		Boolean ldapOn = null;
		if (InitDatabase.getFirstTime()) {
			String sldapOn = "false";
			ldapOn = new Boolean(sldapOn);
			Map<Integer, Object> siteBeanValues = new HashMap<Integer, Object>();
			if (ldapOn.booleanValue() == true) {
				siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.ISLDAPON, "Y");
			} else {
				siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.ISLDAPON, "N");
			}
			siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.LDAPSERVERURL, "");
			siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.LDAPATTRIBUTELOGINNAME, "");
			siteDAO.loadAndSaveSynchronized(siteBeanValues);
		}
	}

	// ---------------------------------------------------------------------
	/*
	 *
	 */
	private void initWebService() {

	}

	/*
	 * Print IP address information for this host
	 */
	private void printInetAdressInfo() {

		InetAddress[] allAds = ApplicationBean.getInetAddress();
		if (allAds == null || allAds.length == 0) {
			LOGGER.error("No IP addresses found for this host!");
			LOGGER.error("Most likely your DNS configuration is faulty.");
		}

		LOGGER.info("All IP addresses found for this host: ");
		for (int i = 0; i < allAds.length; ++i) {
			LOGGER.info("IP[" + i + "] = " + allAds[i].getHostAddress());
		}
	}

	// ---------------------------------------------------------------
	/*
	 * Initialize the licensing subsystem.
	 */
	private ApplicationBean initLicSys(TSiteBean site) {
		ApplicationBean applicationBean = ApplicationBean.getInstance();
		try {
			Class<LicenseManager> clazz = (Class<LicenseManager>) Class.forName("com.trackplus.license.LicenseManagerImpl");
			if (clazz != null) {
				Class<Object>[] args = null;
				Object[] params = null;
				Method m = clazz.getDeclaredMethod("getInstance", args);
				LicenseManager lm = (LicenseManager) m.invoke(null, params);
				applicationBean.setLicenseManager(lm);
			}
		} catch (Exception e) {
			// This is quite normal, the plug-in may not be there.
		}
		if (InitDatabase.getFirstTime()) {
			Map<Integer, Object> siteBeanValues = new HashMap<Integer, Object>();
			siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.LICENSEKEY, "");
			siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.NUMBEROFUSERS, new Integer(5));
			try {
				siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.EXPDATE, DateTimeUtils.getInstance().parseISODate("2012-12-01"));
			} catch (Exception e) {
				LOGGER.error("Problem converting license expiration date");
			}
			siteDAO.loadAndSaveSynchronized(siteBeanValues);
		}

		applicationBean.setActualUsers();
		printInetAdressInfo();

		if (site.getInstDate() == null || "".equals(site.getInstDate())) {
			site.setInstDate(new Long(new Date().getTime()).toString());
		}
		try {
			applicationBean.setInstDate(new Long(site.getInstDate()).longValue());
		} catch (Exception e) {
			LOGGER.info("Setting install date to " + new Date());
		}

		try {
			applicationBean.initLic(site.getLicenseExtension());
		} catch (Exception ex) {
			LOGGER.error(ExceptionUtils.getStackTrace(ex));
		}

		site.setNumberOfUsers(new Integer(applicationBean.getMaxNumberOfFullUsers() + applicationBean.getMaxNumberOfLimitedUsers()));
		site.setNumberOfFullUsers(applicationBean.getMaxNumberOfFullUsers());
		site.setNumberOfLimitedUsers(applicationBean.getMaxNumberOfLimitedUsers());
		if (applicationBean.getLicenseHolder() != null) {
			site.setLicenseHolder(applicationBean.getLicenseHolder());
		}
		site.setExpDate(applicationBean.getExpDate());

		applicationBean.setSiteParams(site);
		applicationBean.setSiteBean(site);

		getServletConfig().getServletContext().setAttribute(Constants.APPLICATION_BEAN, applicationBean);
		return applicationBean;
	}

	// -----------------------------------------------------------------------
	/**
	 * Initialize the Lucene indexing system.
	 *
	 * @param myIp
	 * @param indexing
	 *            whether to initialize for both indexing and searching or only
	 *            searching
	 */
	private void initLucene(String myIp, boolean indexing) {
		// init lucene
		// get found analysers list
		List<LabelValueBean> analyzerList = LuceneUtil.getAnalyzersList(getServletConfig().getServletContext());
		LuceneUtil.setFoundAnalysers(analyzerList);
		// get config from database
		TSiteBean siteBean = siteDAO.load1();
		LuceneUtil.configLuceneParameters(siteBean);
		// if lucene is active
		if (indexing && LuceneUtil.isUseLucene()) {
			LuceneIndexer luceneIndexer = new LuceneIndexer();
			ApplicationBean.getInstance().setLuceneIndexer(luceneIndexer);
			if (LuceneUtil.isReindexOnStartup()) {
				LOGGER.debug("Reindex on startup on " + myIp);
				// reindex in a new thread (the modifiers are initialized inside
				// run)
				ApplicationBean.getInstance().getExecutor().execute(luceneIndexer);
			} else {
				// initialize index for modifications
				try {
					LuceneIndexer.initIndexWriters(false);
					LOGGER.debug("Init lucene on append mode " + myIp);
				} catch (Exception e) {
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
		}

	}

	private void updateVersions(ServletContext servletContext) {
		
		try {
			LabelValueBean versionBean = new LabelValueBean(appBean.getVersion(), "");
			LabelValueBean versionDateBean = new LabelValueBean(appBean.getVersionDate(), "");
			// Update the version in the database
			Map<Integer, Object> siteBeanValues = new HashMap<Integer, Object>();
			siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.TRACKVERSION, appBean.getVersion());
			siteDAO.loadAndSaveSynchronized(siteBeanValues);

			servletContext.setAttribute("TVERSION", versionBean);
			servletContext.setAttribute("TVERSIONDATE", versionDateBean);

		} catch (Exception e) {
			// no version
			servletContext.setAttribute("TVERSION", new LabelValueBean("4.X", ""));
			servletContext.setAttribute("TVERSIONDATE", new LabelValueBean("?", ""));
		}

	}

	private void setVersionFromVersionProperties(ServletContext servletContext) {
		ApplicationBean applicationBean = ApplicationBean.getInstance();
		String appTypeString = "";
		Integer appType = -1;
		String theVersion = "";
		String theBuild = "";
		String theVersionDate = "";
		Integer theVersionNo = 370;
		try {
			URL versionURL = servletContext.getResource("/WEB-INF/Version.properties");
			PropertiesConfiguration vcfg = new PropertiesConfiguration();
			InputStream in = versionURL.openStream();
			vcfg.load(in);
			theVersion = (String) vcfg.getProperty("version");
			theBuild = (String) vcfg.getProperty("build");
			if (theVersion == null) {
				theVersion = "4.X";
			}
			if (theBuild == null) {
				theBuild = "1";
			}
			theVersionDate = (String) vcfg.getProperty("date");
			if (theVersionDate == null) {
				theVersionDate = "2015-01-01";
			}
			theVersionNo = new Integer((String) vcfg.getProperty("app.version"));
			try {
				appType = new Integer((String) vcfg.getProperty("ntype"));
			} catch (Exception e) {
				appType = ApplicationBean.APPTYPE_FULL;
			}

			appTypeString = (String) vcfg.getProperty("type");
			if (appTypeString == null) {
				appTypeString = "Genji";
			}
		} catch (Exception e) {
			theVersion = "1.x";
			theBuild = "0";
			theVersionDate = "2015-01-01";
		}
		applicationBean.setServletContext(servletContext);
		applicationBean.setVersion(theVersion);
		applicationBean.setVersionNo(theVersionNo);
		applicationBean.setBuild(theBuild);
		applicationBean.setVersionDate(theVersionDate);
		applicationBean.setAppType(appType);
		applicationBean.setAppTypeString(appTypeString);

	}

	// ----------------------------------------------------------------------
	/**
	 * Gets the list of graphical skins defined
	 *
	 * @return
	 */
	private static void getDesignPaths(ServletContext servletContext) {
		List<LabelValueBean> designPaths = new ArrayList<LabelValueBean>();
		String designDirectoryName = "design";
		File designDirectory = PluginUtils.getResourceFileFromWebAppRoot(servletContext, designDirectoryName);
		if (designDirectory == null || !designDirectory.exists() || !designDirectory.isDirectory()) {
			designPaths.add(new LabelValueBean(Constants.DEFAULTDESIGNPATH, Constants.DEFAULTDESIGNPATH));
		} else {
			File[] files = designDirectory.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					designPaths.add(new LabelValueBean(files[i].getName(), files[i].getName()));
				}
			}
		}
		ApplicationBean.getInstance().setDesigns(designPaths);
	}

	private void printSystemInfo() {
		LOGGER.info("Java: " + System.getProperty("java.vendor") + " " + System.getProperty("java.version"));
		LOGGER.info("Operating System: " + System.getProperty("os.name") + " " + System.getProperty("os.arch"));
		Locale loc = Locale.getDefault();
		LOGGER.info("Default locale: " + loc.getDisplayName());

		ServletContext application = ApplicationBean.getInstance().getServletContext();
		try {
			LOGGER.info("Servlet real path: " + application.getRealPath(File.separator));
		} catch (Exception ex) {
			LOGGER.error("Error trying to obtain getRealPath()");
		}
		LOGGER.info("Servlet container: " + application.getServerInfo());

		Connection conn = null;
		try {
			PropertiesConfiguration pc = ApplicationBean.getInstance().getDbConfig();
			LOGGER.info("Configured database type: " + pc.getProperty("torque.database.track.adapter"));
			LOGGER.info("Configured database driver: " + pc.getProperty("torque.dsfactory.track.connection.driver"));
			LOGGER.info("Configured JDBC URL: " + pc.getProperty("torque.dsfactory.track.connection.url"));
			conn = Torque.getConnection(BaseTSitePeer.DATABASE_NAME);
			DatabaseMetaData dbm = conn.getMetaData();
			LOGGER.info("Database type: " + dbm.getDatabaseProductName() + " " + dbm.getDatabaseProductVersion());
			LOGGER.info("Driver info:   " + dbm.getDriverName() + " " + dbm.getDriverVersion());
			Statement stmt = conn.createStatement();
			Date d1 = new Date();
			stmt.executeQuery("SELECT * FROM TSTATE");
			Date d2 = new Date();
			stmt.close();
			LOGGER.info("Database test query done in " + (d2.getTime() - d1.getTime()) + " milliseconds ");
		} catch (Exception e) {
			System.err.println("Problem retrieving meta data");
			LOGGER.error("Problem retrieving meta data");
		} finally {
			if (conn != null) {
				Torque.closeConnection(conn);
			}
		}
	}

	private void setColumnSizes() {
		Connection conn = null;
		int descSize = 32000;// Firebird
		int cSize = 10000;// Firebird
		int sizeMySQL = 16777215;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = Torque.getConnection(BaseTSitePeer.DATABASE_NAME);
			stmt = conn.createStatement();
			stmt.setMaxRows(2);
			rs = stmt.executeQuery("SELECT PACKAGEDESCRIPTION FROM TWORKITEM");
			if (rs != null) {
				descSize = rs.getMetaData().getColumnDisplaySize(1);
				if (descSize <= 0) {
					// MySQL bug (MySql 5.0.26 for linux returns -1)
					System.err.println("Incorrect maximum description length retrieved: " + descSize);
					descSize = sizeMySQL;
				}
				rs.close();
			}
			rs = stmt.executeQuery("SELECT CHANGEDESCRIPTION FROM TSTATECHANGE");
			if (rs != null) {
				cSize = rs.getMetaData().getColumnDisplaySize(1);
				if (cSize <= 0) {
					// MySQL bug (MySql 5.0.26 for linux returns -1)
					System.err.println("Incorrect maximum comment length retrieved: " + cSize);
					cSize = sizeMySQL;
				}
			}
		} catch (Exception e) {
			System.err.println("Could not retrieve column sizes from database: " + e.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					LOGGER.info("Closing the resultset failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					LOGGER.info("Closing the statement failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
			if (conn != null) {
				Torque.closeConnection(conn);
			}
		}
		LOGGER.info("Maximum description length: " + descSize);
		LOGGER.info("Maximum comment length: " + cSize);
		ApplicationBean.getInstance().setDescriptionMaxLength(descSize);
		ApplicationBean.getInstance().setCommentMaxLength(cSize);
	}

	private void emergencyExit(Exception e) {
		// We need this to show a proper install problem page
		ApplicationBean applicationBean = ApplicationBean.getInstance();
		getServletConfig().getServletContext().setAttribute(Constants.APPLICATION_BEAN, applicationBean);
		PropertiesConfiguration pc = ApplicationBean.getInstance().getDbConfig();

		LOGGER.error("Something went wrong here.");
		LOGGER.error("Please have a look at the previous error messagesand stack traces.");
		LOGGER.error("Most likely the database connection does not work.");
		LOGGER.error("Please check if the user name, password and JDBC URL in the ");
		LOGGER.error("WEB-INF/Torque.properties file are set correctly.");
		LOGGER.error("Please check that you have only one Database type enabled in the");
		LOGGER.error("Torque.properties file. Please also check that the database server");
		LOGGER.error("is running and is accessible from this machine. If the database server is");
		LOGGER.error("running on a different machine, check that there are no firewall issues.");
		LOGGER.error("Please also check that you have run all SQL scripts to set up the database.");
		LOGGER.error("");
		LOGGER.error("For your information, your settings in Torque.properties are: ");
		LOGGER.error("Database user name: " + pc.getProperty("torque.dsfactory.track.connection.user"));
		String password = (String)pc.getProperty("torque.dsfactory.track.connection.password");
		if (password!=null) {
			password = password.replaceAll(".", "*");
			LOGGER.error("Database password:  " + password + "(see Torque.properties file)");
		}
		LOGGER.error("Database type: " + pc.getProperty("torque.database.track.adapter"));
		LOGGER.error("Database JDBC driver: " + pc.getProperty("torque.dsfactory.track.connection.driver"));
		LOGGER.error("Database connection URL: " + pc.getProperty("torque.dsfactory.track.connection.url"));
		LOGGER.error("Exiting...");
		LOGGER.error("");
		LOGGER.error("");
		LOGGER.error(ExceptionUtils.getStackTrace(e), e);
	}

	private ArrayList<String> createInstallProblemMessage(Exception e) {
		ArrayList<String> msg = new ArrayList<String>();
		try {
			PropertiesConfiguration pc = ApplicationBean.getInstance().getDbConfig();
			msg.add("Database user name: (see Torque.properties file)");
			msg.add("Database password:  (see Torque.properties file)");
			msg.add("Database type: " + pc.getProperty("torque.database.track.adapter"));
			msg.add("Database JDBC driver: " + pc.getProperty("torque.dsfactory.track.connection.driver"));
			msg.add("Database connection URL: " + pc.getProperty("torque.dsfactory.track.connection.url"));
			msg.add("The system gives this error message: " + e.getMessage());
		} catch (Exception ex) {
			// Nothing to be done about this here
		}
		return msg;
	}

	public void setServletConfig(ServletConfig ctx) {
		servletConfig = ctx;
	}

	public ServletConfig getServletConfig() {
		return servletConfig;
	}

	private void updateOrCreateDbSchema(ServletContext servletContext) {
		try {
			//
			boolean dbUpdateOrCreateSuccessfull = UpdateDbSchema.doUpdateOrCreateFromScratch(servletContext);
			if (!dbUpdateOrCreateSuccessfull) {
				// Unfortunately we do not have a database connection
				// so we try to copy the configuration file to TRACKPLUS_HOME
				// before we terminate the attempt to start
				HandleHome.copyTorquePropertiesToHome(servletContext);
			}
		} catch (Exception e) {
			LOGGER.error("Problem creating or updating database schema: " + e.getMessage(), e);
		}
	}

	private TSiteBean initDatabaseAndAdjustHome(String version, ServletContext servletContext) {
		TSiteBean site = null;
		try {
			site = InitDatabase.initDatabase(version, servletContext);
			HandleHome.initTrackplus_Home(servletContext);
		} catch (Exception e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			ApplicationBean.getInstance().setInstallProblem(createInstallProblemMessage(e));
			emergencyExit(e);
		}
		return site;
	}

	private void initJobs(ServletContext servletContext) {
		// finally starting the Quartz scheduler ...
		LOGGER.info("Initializing Quartz scheduler...");

		JobScheduler.init(servletContext, ApplicationBean.getInstance().getDbConfig());
		JobScheduler.startScheduler();
		// update Database backup job
		TSiteBean site = ApplicationBean.getInstance().getSiteBean();
		String backupTime = site.getBackupTime();
		List<Integer> backupOnDays = site.getBackupOnDays();
		if (backupTime != null && backupOnDays != null) {
			DatabaseBackupBL.setDBJobCronExpression(backupTime, backupOnDays);
		} else {
			backupTime = "23:15:00";
			backupOnDays = new ArrayList<Integer>();
			for (int i = 2; i <= 6; i++) {
				backupOnDays.add(i);
			}
			site.setBackupTime(backupTime);
			site.setBackupOnDays(backupOnDays);
			siteDAO.save(site);
		}
	}
	
	public void initLucene(String myIp) {
		// --------------------------------------------------------------------
		// Initialize Lucene indexing system.
		//
		if (ClusterBL.isCluster()) {
			if (ClusterBL.isSharedLuceneIndex()) {
				if (ClusterBL.getIAmTheMaster()) {
					// only the master might write on index
					initLucene(myIp, true);
				} else {
					// the other nodes only read
					initLucene(myIp, false);
				}
			} else {
				// each node has own index files
				initLucene(myIp, true);
			}
			ClusterUpdateBL.updateClusterNode();
		} else {
			// no cluster, single instance node
			initLucene(myIp, true);
		}
	}
}
