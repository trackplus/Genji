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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

// import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.Torque;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.aurel.track.ApplicationStarter;
import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.PredefinedQueryBL;
import com.aurel.track.admin.customize.lists.BlobBL;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.customize.lists.systemOption.PriorityBL;
import com.aurel.track.admin.customize.lists.systemOption.SeverityBL;
import com.aurel.track.admin.customize.lists.systemOption.StatusBL;
import com.aurel.track.admin.customize.localize.LocalizeBL;
import com.aurel.track.admin.customize.mailTemplate.MailTemplateBL;
import com.aurel.track.admin.customize.mailTemplate.MailTemplateConfigBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.server.logging.LoggingConfigBL;
import com.aurel.track.admin.server.motd.MotdBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.admin.user.userLevel.UserLevelBL;
import com.aurel.track.beans.TBasketBean;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TMailTemplateConfigBean;
import com.aurel.track.beans.TMotdBean;
import com.aurel.track.beans.TPriorityBean;
import com.aurel.track.beans.TSeverityBean;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.configExchange.importer.EntityImporter;
import com.aurel.track.configExchange.importer.ImportContext;
import com.aurel.track.configExchange.importer.ImportResult;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.SiteDAO;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.persist.TLoggingLevelPeer;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.screen.dashboard.bl.design.DashboardScreenDesignBL;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.LocaleHandler;
import com.aurel.track.util.event.IEventSubscriber;

public class InitDatabase {

	private final static Logger LOGGER = LogManager.getLogger(InitDatabase.class);

	private static final String STACKTRACE = "Stacktrace: ";

	private static boolean isFirstStartEver = false;
	private static boolean haveDBProblem = false;
	private static PropertiesConfiguration tcfg = null;
	private static SiteDAO siteDAO = DAOFactory.getFactory().getSiteDAO();
	private static Connection con = null;
	// private static EntityManager entityManager = null;

	private static String initDataDir = "/initData/full";
	private static String populateSql = initDataDir+"/populate.sql";
	private static String postloadSql = initDataDir+"/postload.sql";
	private static String workspaceSamplesSql = initDataDir+"/workspaceSamples.sql";


	public static TSiteBean initDatabase(String theVersion, ServletContext servletContext) throws ServletException {
		LoggingConfigBL.setLevel(LOGGER, Level.INFO);
		switch (ApplicationBean.getInstance().getAppType()) {
		case ApplicationBean.APPTYPE_FULL:
			initDataDir = "/initData/full";
			break;
		case ApplicationBean.APPTYPE_DESK:
			initDataDir = "/initData/desk";
			break;
		case ApplicationBean.APPTYPE_BUGS:
			initDataDir = "/initData/bugs";
			break;
		default:
			initDataDir = "/initData/full";
		}
		populateSql = initDataDir+"/populate.sql";
		postloadSql = initDataDir+"/postload.sql";
		workspaceSamplesSql = initDataDir+"/workspaceSamples.sql";

		ApplicationStarter.getInstance().actualizePercentComplete(0, ApplicationStarter.INIT_DB_DATA_TEXT);
		isFirstStartEver = synchronizeIdTable();
		initFixedTables();
		InitReportTemplateBL.addReportTemplates();

		insertNullObjectsAndSampleData();

		initTorque();

		// TpEm.initEntityManagerFactory(tcfg, "com.trackplus.core");

		if (isFirstStartEver) {
			setFilterFields(false, false);
		}

		TSiteBean site = getSiteConfiguration(isFirstStartEver);
		site = setDirectories(site);  // for attachments, indices, backups

		boolean isGenji = true;

		if (isGenji && (site.getLicenseExtension() != null || "".equals(site.getLicenseExtension()))) {
			site.setLicenseExtension("Genji Open Source License");
		}
		loadResourcesFromPropertiesFiles(true, servletContext);
		if (site.getTrackVersion() == null || !site.getTrackVersion().equals(theVersion)) {
			loadResourcesFromPropertiesFiles(false, servletContext);
			loadMailTemplates();
			//FIXME load form templates after modify the database to allow modified by user
			//loadFormTemplates();
			addIconsToDatabase();
			if (isFirstStartEver) {
				site.setSummaryItemsBehavior(true);
				setMessageOfTheDay(site, theVersion);
			}
		}
		if (isFirstStartEver) {
			InitProjectTypesBL.addProjectTypes(initDataDir);
			insertPostLoadData(workspaceSamplesSql);
			PredefinedQueryBL.addHardcodedFilters();
			UserLevelBL.migrateFromProperyFileToDatabase(initDataDir);
			DashboardScreenDesignBL.getInstance().checkAndCreateClientDefaultCockpit();
			site.setProjectSpecificIDsOn(true);
			site.setIsVersionReminderOn(true);
		}
		LocaleHandler.getLocales();
		if (site.getAttachmentRootDir() != null && !"".equals(site.getAttachmentRootDir().trim())) {
			// In case we have TRACKPLUS_HOME set in the database it overwrites the environment setting
			// Otherwise we set the value in the database from the environment variable
			if (!HandleHome.getTrackplus_Home().equals(site.getAttachmentRootDir())) {
				HandleHome.setTrackplus_Home(site.getAttachmentRootDir());
			}
		}
		return site;
	}



	/**
	 * Set a new PropertiesConfiguration for Torque.
	 * @param pc
	 */
	public static void setPropertiesConfiguration(PropertiesConfiguration pc) {
		tcfg = pc;
	}



	/**
	 * This method loads resource strings from properties files (ApplicationResources.properties)
	 * into the database.
	 * @throws ServletException
	 */
	public static void loadResourcesFromPropertiesFiles(Boolean customOnly, ServletContext servletContext) {
		Boolean useProjects = true;
		Connection con = null;
		try {
			con = getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM ID_TABLE WHERE TABLE_NAME = 'USESPACES'");
			if (rs.next()) {
				useProjects = false;  // We have an installation that uses the workspace terminology
			}
		} catch (Exception e) {
			LOGGER.error("Problem reading from ID_TABLE when checking for USESPACES");
			LOGGER.debug(STACKTRACE, e);
		} finally {
			if (con != null)  {
				try {
					con.close();
				} catch (Exception e) {
					// Nothing to be done here
				}
			}
		}
		List<Locale> locs = LocaleHandler.getPropertiesLocales();
		int numberOfLocales = locs.size();
		int step = Math.round(ApplicationStarter.getInstance().RESOURCE_UPGRADE[1]/numberOfLocales);
		float delta = new Float(ApplicationStarter.getInstance().RESOURCE_UPGRADE[1]/new Float(numberOfLocales))-step;
		Iterator<Locale> it = locs.iterator();
		//int i = 0;
		while (it.hasNext()) {
			//i++;
			Locale loc = it.next();
			String locCode = loc.getLanguage();
			if (ApplicationBean.getInstance().isInTestMode()	&& locCode != "en") {
				continue;
			}
			if (loc.getCountry() != null && !"".equals(loc.getCountry())) {
				locCode = locCode + "_" + loc.getCountry();
			}
			if (!customOnly) {
				ApplicationStarter.getInstance().actualizePercentComplete(step, ApplicationStarter.RESOURCE_UPGRADE_LOCALE_TEXT + loc.getDisplayName() + "...");
				addResourceToDatabase(loc, locCode, useProjects);
			}
			addCustomResourceToDatabase(loc, locCode);
		}
		if (!customOnly) {
			ApplicationStarter.getInstance().actualizePercentComplete(step, ApplicationStarter.RESOURCE_UPGRADE_DEFAULT_LOCALE_TEXT);
			addResourceToDatabase(Locale.getDefault(), null, useProjects);
		}
		addCustomResourceToDatabase(Locale.getDefault(), null);
		if (!customOnly) {
			ApplicationStarter.getInstance().actualizePercentComplete(Math.round(delta), ApplicationStarter.RESOURCE_UPGRADE_DEFAULT_LOCALE_TEXT);
		}
	}

	/*
	 * This routine loads the resources into the database
	 */
	private static void addResourceToDatabase(Locale loc, String locCode, Boolean useProjects) {
		String pfix = "_"+locCode;
		String lang = loc.getDisplayName();
		if (locCode == null || "".equals(locCode.trim())) {
			pfix = "";
			lang = "Standard";
		}
		try {
			LOGGER.info("Synchronizing resources for " + lang);
			URL propURL = ApplicationBean.getInstance().getServletContext()
					.getResource("/WEB-INF/classes/resources/UserInterface/ApplicationResources"+pfix+".properties");
			InputStream in = propURL.openStream();
			LocalizeBL.saveResources(in, locCode, false, LocalizeBL.RESOURCE_CATEGORIES.UI_TEXT, false);
		}catch(Exception e) {
			LOGGER.warn("Can't read ApplicationResources.properties from context for " + lang);
			try {
				LOGGER.info("Trying to use class loader to synchronize resources for " + lang);
				ClassLoader cl = InitDatabase.class.getClassLoader();
				URL propURL = cl.getResource("resources/UserInterface/ApplicationResources"+pfix+".properties");
				InputStream in = propURL.openStream();
				LocalizeBL.saveResources(in, locCode, false, LocalizeBL.RESOURCE_CATEGORIES.UI_TEXT, false);
			}catch(Exception ee) {
				LOGGER.error("No chance to get ApplicationResources.properties for " + lang);
				LOGGER.debug(STACKTRACE, ee);
			}
		}
		try {
			URL propURL = ApplicationBean.getInstance().getServletContext()
					.getResource("/WEB-INF/classes/resources/UserInterface/BoxResources"+pfix+".properties");
			InputStream in = propURL.openStream();
			LocalizeBL.saveResources(in, locCode, false, LocalizeBL.RESOURCE_CATEGORIES.DB_ENTITY, true);
		}catch(Exception e) {
			LOGGER.warn("Can't read BoxResources.properties from context for " + lang);
			try {
				LOGGER.info("Trying to use class loader to synchronize resources for " + lang);
				ClassLoader cl = InitDatabase.class.getClassLoader();
				URL propURL = cl.getResource("resources/UserInterface/BoxResources"+pfix+".properties");
				InputStream in = propURL.openStream();
				LocalizeBL.saveResources(in, locCode, false, LocalizeBL.RESOURCE_CATEGORIES.DB_ENTITY, true);
			}catch(Exception ee) {
				LOGGER.error("No chance to read BoxResources.properties for " + lang);
				LOGGER.debug(STACKTRACE, ee);
			}
		}
		// Now overwrite the "spaces" with "projects" if this is an upgraded installation
		if (useProjects) {
			try {
				URL propURL = ApplicationBean.getInstance().getServletContext()
						.getResource("/WEB-INF/classes/resources/UserInterfaceProj/ApplicationResources"+pfix+".properties");
				InputStream in = propURL.openStream();
				LocalizeBL.saveResources(in, locCode, false, LocalizeBL.RESOURCE_CATEGORIES.UI_TEXT, false);
			}catch(Exception e) {
				LOGGER.warn("Can't read UserInterfaceProj/ApplicationResources.properties for " + lang);
				try {
					ClassLoader cl = InitDatabase.class.getClassLoader();
					URL propURL = cl.getResource("/WEB-INF/classes/resources/UserInterfaceProj/ApplicationResources"+pfix+".properties");
					InputStream in = propURL.openStream();
					LocalizeBL.saveResources(in, locCode, false, LocalizeBL.RESOURCE_CATEGORIES.UI_TEXT, false);
				}catch(Exception ee) {
					LOGGER.error("Can't read UserInterfaceProj/ApplicationResources.properties for " + lang);
					LOGGER.debug(STACKTRACE, e);
				}
			}
		}
	}

	/*
	 * This routine loads the resources into the database
	 */
	private static void addCustomResourceToDatabase(Locale loc, String locCode) {
		ClassLoader cl = HandleHome.class.getClassLoader();
		File res = new File(HandleHome.getTrackplus_Home() + File.separator + HandleHome.XRESOURCES_DIR);
		if (res.exists() && res.isDirectory() && res.canWrite()) {
			String pfix = "_"+locCode;
			String lang = loc.getDisplayName();
			if (locCode == null) {
				pfix = "";
				lang = "Standard";
			}
			try {
				URL propURL = cl.getResource("MyApplicationResources"+pfix+".properties");
				InputStream in = propURL.openStream();
				LocalizeBL.saveResources(in, locCode, false, LocalizeBL.RESOURCE_CATEGORIES.UI_TEXT, false);
				LOGGER.info("Synchronized custom user interface resources for " + lang);
			}catch(Exception e) {
				// LOGGER.info("Can't read custom MyApplicationResources.properties for " + lang);
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			try {
				URL propURL = cl.getResource("MyBoxResources"+pfix+".properties");
				InputStream in = propURL.openStream();
				LocalizeBL.saveResources(in, locCode, false, LocalizeBL.RESOURCE_CATEGORIES.DB_ENTITY, false);
				LOGGER.info("Synchronized custom database entity resources for " + lang);
			}catch(Exception e) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e),e);
			}
		}
	}

	/**
	 * This method loads mail templates from resources/MailTemplates
	 * into the database.
	 * @throws ServletException
	 */
	private static void loadMailTemplates() {
		List<LabelValueBean> templates = new ArrayList<LabelValueBean>(7);
		templates.add(new LabelValueBean("ItemCreated.xml", new Integer(IEventSubscriber.EVENT_POST_ISSUE_CREATE).toString()));
		templates.add(new LabelValueBean("ItemCreatedByEmail.xml", new Integer(IEventSubscriber.EVENT_POST_ISSUE_CREATE_BY_EMAIL).toString()));
		templates.add(new LabelValueBean("ItemChanged.xml", new Integer(IEventSubscriber.EVENT_POST_ISSUE_UPDATE).toString()));
		templates.add(new LabelValueBean("BudgetChanged.xml", new Integer(IEventSubscriber.EVENT_POST_ISSUE_UPDATEPLANNEDVALUE).toString()));
		templates.add(new LabelValueBean("Welcome.xml", new Integer(IEventSubscriber.EVENT_POST_USER_REGISTERED).toString()));
		templates.add(new LabelValueBean("WelcomeSelf.xml", new Integer(IEventSubscriber.EVENT_POST_USER_SELF_REGISTERED).toString()));
		templates.add(new LabelValueBean("ForgotPassword.xml", new Integer(IEventSubscriber.EVENT_POST_USER_FORGOTPASSWORD).toString()));
		templates.add(new LabelValueBean("Reminder.xml", new Integer(IEventSubscriber.EVENT_POST_USER_REMINDER).toString()));
		templates.add(new LabelValueBean("ClientCreatedByEmail.xml", new Integer(IEventSubscriber.EVENT_POST_USER_CREATED_BY_EMAIL).toString()));

		List<ImportResult> importResultList;
		ImportResult importResult;
		int code;
		for (LabelValueBean template: templates) {
			try {
				LOGGER.info("Synchronizing mail template " + template.getLabel());
				URL propURL = ApplicationBean.getInstance().getServletContext()
						.getResource("/WEB-INF/classes/resources/MailTemplates/" +  template.getLabel());
				if (propURL == null) {
					ClassLoader cl = InitDatabase.class.getClassLoader();
					propURL = cl.getResource("resources/MailTemplates/" +  template.getLabel());
				}
				InputStream in = propURL.openStream();
				ImportContext importContext=new ImportContext();
				importContext.setOverrideExisting(true);
				importContext.setOverrideOnlyNotModifiedByUser(true);
				importContext.setClearChildren(false);
				importResultList=MailTemplateBL.importFile(in,importContext);
				in.close();
				if (importResultList != null && importResultList.size() > 0) {
					importResult=importResultList.get(0);
					code=importResult.getCode();
					if(importResult.isError()){
						LOGGER.warn("Error importing template:"+template.getLabel()+":"+importResult.getErrorMessage()+". Error code="+importResult.getCode());
					}else{
						switch (code){
						case ImportResult.SUCCESS_NEW_ENTITY:{
							createMailTemplateConfigEntry(new Integer(template.getValue()), importResult.getNewObjectID());
							LOGGER.info("Mail template " + template.getLabel() + " added.");
							break;
						}
						case ImportResult.SUCCESS_OVERRIDE:{
							LOGGER.info("Mail template " + template.getLabel() + " overridden.");
							break;
						}
						case ImportResult.SUCCESS_TAKE_EXISTING:{
							LOGGER.info("Mail template " + template.getLabel() + " not updated. Take existing version from DB");
							break;
						}
						}
					}
				}else{
					LOGGER.warn("No template found in file: "+template.getLabel());
				}
			} catch(Exception e) {
				LOGGER.error("Can't read mail template " + template.getLabel() + ": " + e.getMessage(), e);
				LOGGER.debug(STACKTRACE, e);
			}
		}
	}

	private static void createMailTemplateConfigEntry(Integer eventType, Integer templateID) {
		TMailTemplateConfigBean mtcDB= MailTemplateConfigBL.loadByEvent(eventType);
		if(mtcDB!=null){
			LOGGER.debug("Mail template config already in DB");
		}else{
			TMailTemplateConfigBean mtc = new TMailTemplateConfigBean();
			mtc.setEventKey(eventType);
			mtc.setMailTemplate(templateID);
			MailTemplateConfigBL.save(mtc);
		}
	}

	/**
	 * This method loads form templates from resources/FormTemplates
	 * into the database.
	 * @throws ServletException
	 */
	public static void loadFormTemplates() {

		List<String> templates = new ArrayList<String>(5);

		templates.add("DefaultForms.xml");

		List<ImportResult> importResultList;
		ImportResult importResult;
		int code;
		for (String template: templates) {
			try {
				LOGGER.info("Synchronizing form template " + template);
				URL propURL = ApplicationBean.getInstance().getServletContext()
						.getResource(initDataDir+"/Forms/" +  template);
				if (propURL == null) {
					ClassLoader cl = InitDatabase.class.getClassLoader();
					propURL = cl.getResource(initDataDir+"/Forms/" +  template);
				}
				InputStream in = propURL.openStream();
				EntityImporter entityImporter = new EntityImporter();
				ImportContext importContext=new ImportContext();
				importContext.setEntityType("TScreenBean");
				importContext.setOverrideExisting(true);
				importContext.setClearChildren(true);
				importContext.setOverrideOnlyNotModifiedByUser(true);


				importResultList = entityImporter.importFile(in, importContext);

				in.close();
				if (importResultList != null && importResultList.size() > 0) {
					importResult=importResultList.get(0);
					code=importResult.getCode();
					if(importResult.isError()){
						LOGGER.warn("Error importing template:"+template+":"+importResult.getErrorMessage()+". Error code="+importResult.getCode());
					}else{
						switch (code){
						case ImportResult.SUCCESS_NEW_ENTITY:{
							LOGGER.info("Form template " + template + " added.");
							break;
						}
						case ImportResult.SUCCESS_OVERRIDE:{
							LOGGER.info("Form template " + template + " overridden.");
							break;
						}
						case ImportResult.SUCCESS_TAKE_EXISTING:{
							LOGGER.info("Form template " + template + " not updated. Take existing version from DB");
							break;
						}
						}
					}
				}else{
					LOGGER.warn("No template found in file: "+template);
				}
			} catch(Exception e) {
				LOGGER.error("Can't read form template " + template + ": " + e.getMessage(), e);
				LOGGER.debug(STACKTRACE, e);
			}
		}	// We have loaded the forms
		//TODO The screen to action assignments should be moved out of the SQL as well
		insertPostLoadData(postloadSql);
	}

	/*
	 * This routine loads the icons for system lists into the database
	 */
	private static void addIconsToDatabase() {
		URL iconURL = null;
		InputStream in = null;
		String iconName = null;
		Integer iconKey = null;

		LOGGER.info("Synchronizing state icons.");
		List<TStateBean> stateBeans = StatusBL.loadAll();
		for (TStateBean stateBean : stateBeans) {
			try{
				if (stateBean.getSymbol() != null && !"".equals(stateBean.getSymbol() )) {
					iconName = stateBean.getSymbol();
					iconURL = ApplicationBean.getInstance().getServletContext()
							.getResource("/design/silver/sysListIcons/" + iconName);
					in = iconURL.openStream();

					if (stateBean.getIconChanged() == null || "N".equals(stateBean.getIconChanged())) {
						iconKey = BlobBL.saveFileInDB(stateBean.getIconKey(), in);
						stateBean.setIconKey(iconKey);
						StatusBL.saveSimple(stateBean);
					}
				}
			}catch(Exception e) {
				LOGGER.debug("Can't find icon " + iconName);
			} finally {
				try {

					if (in != null) {
						in.close();
					}
				} catch (Exception e) { /* nothing to be done here */	}
			}
		}

		LOGGER.info("Synchronizing item type icons.");
		List<TListTypeBean> ltBeans = IssueTypeBL.loadAll();
		for (TListTypeBean ltBean : ltBeans) {
			try {
				if (ltBean.getSymbol() != null && !"".equals(ltBean.getSymbol() )) {
					iconName = ltBean.getSymbol();
					iconURL = ApplicationBean.getInstance().getServletContext()
							.getResource("/design/silver/sysListIcons/" + iconName);
					in = iconURL.openStream();

					if (ltBean.getIconChanged() == null || "N".equals(ltBean.getIconChanged())) {
						iconKey=BlobBL.saveFileInDB(ltBean.getIconKey(), in);
						ltBean.setIconKey(iconKey);
						IssueTypeBL.saveSimple(ltBean);
					}
				}
			}catch(Exception e) {
				LOGGER.debug("Can't find icon " + iconName);
			} finally {
				try {

					if (in != null) {
						in.close();
					}
				} catch (Exception e) { /* nothing to be done here */	}
			}
		}

		LOGGER.info("Synchronizing priority icons.");
		List<TPriorityBean> priorityBeans = PriorityBL.loadAll();
		for (TPriorityBean ltBean : priorityBeans) {
			try {
				if (ltBean.getSymbol() != null && !"".equals(ltBean.getSymbol() )) {
					iconName = ltBean.getSymbol();
					iconURL = ApplicationBean.getInstance().getServletContext()
							.getResource("/design/silver/sysListIcons/" + iconName);
					in = iconURL.openStream();

					if (ltBean.getIconChanged() == null || "N".equals(ltBean.getIconChanged())) {
						iconKey = BlobBL.saveFileInDB(ltBean.getIconKey(), in);
						ltBean.setIconKey(iconKey);
						PriorityBL.saveSimple(ltBean);
					}
				}
			}catch(Exception e) {
				LOGGER.debug("Can't find icon " + iconName);
			} finally {
				try {

					if (in != null) {
						in.close();
					}
				} catch (Exception e) { /* nothing to be done here */	}
			}
		}

		LOGGER.info("Synchronizing severity icons.");
		List<TSeverityBean> severityBeans = SeverityBL.loadAll();
		for (TSeverityBean ltBean : severityBeans) {
			try {
				if (ltBean.getSymbol() != null && !"".equals(ltBean.getSymbol() )) {
					iconName = ltBean.getSymbol();
					iconURL = ApplicationBean.getInstance().getServletContext()
							.getResource("/design/silver/sysListIcons/" + iconName);
					in = iconURL.openStream();

					if (ltBean.getIconChanged() == null || "N".equals(ltBean.getIconChanged())) {
						iconKey=BlobBL.saveFileInDB(ltBean.getIconKey(), in);
						ltBean.setIconKey(iconKey);
						SeverityBL.saveSimple(ltBean);
					}
				}
			} catch(Exception e) {
				LOGGER.debug("Can't find icon " + iconName);
			} finally {
				try {
					if (in != null) {
						in.close();
					}
				} catch (Exception e) { /* nothing to be done here */	}
			}
		}

	}

	//-------------------------------------------------------------------------
	/*
	 * Initializes the Torque persistence subsystem
	 */
	public static void initTorque() throws ServletException {
		try	{
			tcfg = HandleHome.getTorqueProperties(ApplicationBean.getInstance().getServletContext(), true);
			tcfg.setProperty("torque.applicationRoot",".");
			tcfg.setProperty("torque.database.default","track");
			tcfg.setProperty("torque.idbroker.clever.quantity",new Boolean(false));
			tcfg.setProperty("torque.idbroker.prefetch",new Boolean(false));
			tcfg.setProperty("torque.manager.useCache",new Boolean(true));

			// check if we should switch to the embedded derby-database
			// String dbAdapter = tcfg.getProperty("torque.database.track.adapter").toString();

			if (Torque.isInit()) {
				LOGGER.info("Restarting database connection (Torque)...");
				Torque.shutdown();
				Thread.sleep(3000);
			}

			Torque.init(tcfg);  // Now really do it: initialize Torque

			LOGGER.debug("Default database is " + Torque.getDefaultDB());

			if (Torque.isInit()) {
				LOGGER.info("Database connection (Torque) is initialized.");
				ApplicationBean.getInstance().getServletContext().setAttribute(
						Constants.DATABASE_KEY, "X");
				//ApplicationBean.getInstance().setApplicationContext();

				Integer count = PersonBL.countFullActive(); //TPersonPeer.count();
				if (count == null || count.intValue()==0) {
					throw new ServletException("Can't access table TPERSON.");
				}

				// Let's check, if there are any logging level configurations
				// in the database. If so, we load them. If not, we will initialize
				// them the first time we change a value.
				TLoggingLevelPeer.load();
				if (LOGGER.getLevel().isMoreSpecificThan(Level.INFO)) {
					LoggingConfigBL.setLevel(LOGGER, Level.INFO); // always make sure we have at least INFO level
				}
			}
		}
		catch (Exception e) {
			LOGGER.error("Torque init failed with " + e.getMessage(), e);
			throw new ServletException(e);
		}
	}


	/**
	 * Returns true if we had problems accessing the database
	 * @return
	 */
	public static boolean getHaveDBProblem() {
		return haveDBProblem;
	}

	/**
	 * Returns true if this is completely new install
	 * @return
	 */
	public static boolean getFirstTime() {
		return isFirstStartEver;
	}

	/**
	 * Get the site configuration from the database. If the database
	 * is empty, initialize it with some default values.
	 * @return
	 * @throws ServletException
	 */
	public static TSiteBean getSiteConfiguration(boolean isFirstStartEver) throws ServletException {
		TSiteBean site = null;
		haveDBProblem = false;
		try {
			try {
				site =siteDAO.load1();
			}
			catch (Exception e) {
				// We seem to have a database problem.
				haveDBProblem = true;
				System.err.println();
				System.err.println("-----------------------------------------");
				System.err.println("ERROR: could not access database.");
				System.err.println("If you are upgrading from a previous "
						+ "release, make sure you have upgraded the "
						+ "database as well.");
				System.err.println("-----------------------------------------");
				System.err.println();
				throw new ServletException();
			}
			if (site == null || site.getExpDate() == null) {
				if (site == null) {
					site = new TSiteBean();  // only for the very first time
					
				}
				if(isFirstStartEver) {
					site.setDbVersion(new Integer(ApplicationBean.getInstance().getDbVersion()).toString());
				}
				LOGGER.info(ApplicationBean.getInstance().getAppTypeString() + ": initializing site configuration first time ever.");
				site.setIsDemoSite("N");
				site.setExpDate(DateTimeUtils.getInstance().parseISODate("2099-12-31"));
				site.setIsSelfRegisterAllowed("Y");
				site.setTrackEmail("trackplus@yourdomain.com");
				site.setInstDate(new Long(new Date().getTime()).toString());
				siteDAO.save(site);
			}
			else {
				LOGGER.info("Getting configuration from database");
				if (site.getDbVersion() != null && !"".equals(site.getDbVersion()) &&
						ApplicationBean.getInstance().getDbVersion() != new Integer(site.getDbVersion()).intValue()) {
					site.setInstDate(new Long(new Date().getTime()).toString());
					siteDAO.save(site);
				}
				if (site.getInstDate() == null) {
					site.setInstDate(new Long(new Date().getTime()).toString());
					siteDAO.save(site);
				}
				ApplicationBean.getInstance().setInstDate(new Long(site.getInstDate()).longValue());
			}
		}
		catch (Exception e){
			System.err.println(ExceptionUtils.getStackTrace(e));
			System.err.println("Problem getting site configuration");
			System.err.println("This is most likely due to a misconfigured"
					+ " Torque.properties file.");
			System.err.println("The stack trace above should provide some hints");
			throw new ServletException();
		}
		ApplicationBean.getInstance().setSiteParams(site);
		return site;
	}

	//--------------------------------------------------------------------
	/*
	 * Upgrades the database if required
	 */
	public static void upgradeDatabase(TSiteBean site, ServletContext servletContext,
			String theVersion, String theBuild)
					throws ServletException {
		if (isFirstStartEver && !haveDBProblem) {
			try {
				siteDAO.save(site);
				LOGGER.info("Saving parameters from web.xml into TSite");
			}
			catch (Exception e) {
				LOGGER.error("Could not save web.xml parameters");
				throw new ServletException();
			}
		}


		if (!haveDBProblem) {
			// we always check if we need to upgrade the database
			// this does not cost much
			try {
				UpgradeDatabase upgradeDatabase = new UpgradeDatabase(theVersion, theBuild);
				upgradeDatabase.upgrade(servletContext);
				//TODO unfortunately this does not work if part of upgrade code runs in different thread
				site=DAOFactory.getFactory().getSiteDAO().load1();// refresh after upgrade
			}
			catch (Exception e) {
				System.err.println("***************************************");
				System.err.println(e.getMessage());
				System.err.println("");
				System.err.println("We are in serious trouble.");
				System.err.println(
						"We tried to upgrade the database but in this process we");
				System.err.println(
						"encountered a problem. Have you properly installed the");
				System.err.println("database with all required SQL scripts?");
				System.err.println("");
				System.err.println(ExceptionUtils.getStackTrace(e));
				throw new ServletException();
			}
		}
	}

	/*
	 * Patch the IdTable and at the same time check if the database structure is
	 * consistent. This is just a rough check if all required columns are present.
	 * This also sets variable firstTime.
	 */
	public static boolean synchronizeIdTable() throws ServletException {
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;
		Map<String,Integer> tmap = new HashMap<String, Integer>();
		boolean isVeryFirstStart = false;
		int maxId = 0;
		try {
			con = getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM ID_TABLE");
			// First read all tables already in ID_TABLE. Put them in tmap.
			while (rs.next()) {
				// ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY
				String tname = rs.getString("TABLE_NAME");
				Integer tid = rs.getInt("ID_TABLE_ID");
				if (tid > maxId) maxId = tid;
				tmap.put(tname, 0);
			}
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
			try {
				if (rs != null) rs.close();
				if (stmt!=null) stmt.close();
				if (con != null) con.close();
			} catch (Exception e2) {
				LOGGER.error(STACKTRACE, e2);
			}
			LOGGER.error("It looks like we cannot establish a connection to the database.");
			throw new ServletException(e);
		}


		try {

			// Check if this is a completely new install and not an upgrade
			rs = stmt.executeQuery("SELECT * FROM TSITE");
			boolean em = rs.next(); // get the first record if any.
			if (em == false
					|| rs.getString("DBVERSION") == null
					|| "".equals(rs.getString("DBVERSION"))) {
				isVeryFirstStart = true;
			}

			// Now let us look what's in the schema definition file.
			// Put all tables from the schema definition file into tables.
			SchemaParser sp = new InitDatabase().new SchemaParser();
			List<InitDatabase.Table> tables = sp.parseSchemaFile();

			String nextId = "1000";
			String quantity = "10";

			for (int i=0; i < tables.size(); ++i) {
				if (tmap.containsKey(tables.get(i).getName())) {
				}
				else {
					++maxId;
					nextId = "1000";
					quantity = "10";
					if ("TWORKITEM".equals(tables.get(i).getName())) {
						nextId = "1";
						quantity = "1";
					}
					if ("THISTORYTRANSACTION".equals(tables.get(i).getName())
							|| "TFIELDCHANGE".equals(tables.get(i).getName())
							|| "TATTRIBUTEVALUE".equals(tables.get(i).getName())) {
						quantity = "100";
					}
					if ("TEXPORTTEMPLATE".equals(tables.get(i).getName())) {
						nextId = "101";
						quantity = "1";
					}
					if ("TLOCALIZEDRESOURCES".equals(tables.get(i).getName())) {
						nextId = "101";
						quantity = "200";
					}

					String insertStmt = "INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY)"
							+ " VALUES (" +  maxId + ", '" + tables.get(i).getName() + "', "+ nextId +", " + quantity  + ")";
					LOGGER.info("Creating ID_TABLE entry for " + tables.get(i).getName() + ": " + insertStmt);
					stmt.executeUpdate(insertStmt);
				}

				// For each table in table-schema.xml, try to read each column.
				// This is a rough check that the database structure conforms to track-schema.xml

				try {
					StringBuffer testStmt = new StringBuffer("SELECT ");
					List<Column> columns = tables.get(i).getColumns();
					Column col = null;

					String pkColumn = "";
					for (int k=0; k < columns.size() - 1; ++k) {
						col = columns.get(k);
						testStmt.append(col.getName()).append(", ");
						if (col.isPrimaryKey()) {
							pkColumn = col.getName();
						}
					}
					col = columns.get(columns.size()-1);
					testStmt.append(col.getName()).append(" FROM ").append(tables.get(i).getName());
					testStmt.append(" WHERE " + pkColumn + " < 1001");

					rs = stmt.executeQuery(testStmt.toString());

				} catch (Exception e) {
					// We have a problem, now pinpoint the exact column since some drivers already
					// bail out with the first faulty column
					List<Column> columns = tables.get(i).getColumns();
					for (int m=0; m < columns.size(); ++m) {
						try {
							StringBuffer testStmt = new StringBuffer("SELECT ");
							Column col = columns.get(m);
							testStmt.append(col.getName()).append(" FROM ").append(tables.get(i).getName());
							rs = stmt.executeQuery(testStmt.toString());
						} catch (Exception ex) {
							LOGGER.error("Problem with table " + tables.get(i).getName() + ": " + e.getMessage());
							LOGGER.debug(STACKTRACE, ex);
						}
					}
				}
			}
			// For completely new installs and their future, use "spaces" terminology
			rs = stmt.executeQuery("SELECT * FROM ID_TABLE WHERE TABLE_NAME = 'USESPACES'");
			if (!rs.next() && isVeryFirstStart) {
				String insertStmt = "INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY)"
						+ " VALUES ( -1000, 'USESPACES', 0, 1)";
				stmt.executeUpdate(insertStmt);
			}

		} catch (Exception e) {
			System.err.println(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) rs.close();
				if (con != null) con.close();
			} catch (Exception e) {
				System.err.println(ExceptionUtils.getStackTrace(e));
			}
			LOGGER.info("Done filling ID_TABLE.");
		}
		return isVeryFirstStart;
	}

	/*
	 * Add entries required by the application logic   */
	public static void initFixedTables() {
		ResultSet rs = null;
		Connection coni = null;
		Connection cono = null;

		LOGGER.info("Check if we need to fill the fixed tables...");
		List<String> basketStms = new ArrayList<String>();
		basketStms.add("INSERT INTO TBASKET (OBJECTID,LABEL,TPUUID) VALUES (1, 'basket.label.1','1001')");
		basketStms.add("INSERT INTO TBASKET (OBJECTID,LABEL,TPUUID) VALUES (2, 'basket.label.2','1002')");
		basketStms.add("INSERT INTO TBASKET (OBJECTID,LABEL,TPUUID) VALUES (3, 'basket.label.3','1003')");
		basketStms.add("INSERT INTO TBASKET (OBJECTID,LABEL,TPUUID) VALUES (4, 'basket.label.4','1004')");
		basketStms.add("INSERT INTO TBASKET (OBJECTID,LABEL,TPUUID) VALUES (5, 'basket.label.5','1005')");
		basketStms.add("INSERT INTO TBASKET (OBJECTID,LABEL,TPUUID) VALUES (6, 'basket.label.6','1006')");
		basketStms.add("INSERT INTO TBASKET (OBJECTID,LABEL,TPUUID) VALUES (7, 'basket.label.7','1007')");
		basketStms.add("INSERT INTO TBASKET (OBJECTID,LABEL,TPUUID) VALUES (8, 'basket.label.8','1008')");
		basketStms.add("INSERT INTO TBASKET (OBJECTID,LABEL,TPUUID) VALUES ("+TBasketBean.BASKET_TYPES.DELETED+", 'basket.label.-1','-1001')");

		// list types
		List<String> listTypeStms = new ArrayList<String>();
		if (ApplicationBean.getInstance().getAppType() == ApplicationBean.APPTYPE_DESK) {
			// listTypeStms.add("INSERT INTO TCATEGORY (PKEY, LABEL, TYPEFLAG, SORTORDER, TPUUID) VALUES (0, 'any list',0, -1,'2001')");

		} else if (ApplicationBean.getInstance().getAppType() == ApplicationBean.APPTYPE_BUGS) {
			// listTypeStms.add("INSERT INTO TCATEGORY (PKEY, LABEL, TYPEFLAG, SORTORDER, TPUUID) VALUES (0, 'any list',0, -1,'2001')");
			listTypeStms.add("INSERT INTO TCATEGORY (PKEY, LABEL, TYPEFLAG, SORTORDER, SYMBOL, TPUUID) VALUES (1,'ProblemReport',0,1,'problemReport.png','2002')");
			listTypeStms.add("INSERT INTO TCATEGORY (PKEY, LABEL, TYPEFLAG, SORTORDER, SYMBOL, TPUUID) VALUES (8,'Requirement',0,8,'requirements.png','2004')");
			listTypeStms.add("INSERT INTO TCATEGORY (PKEY, LABEL, TYPEFLAG, SORTORDER, SYMBOL, TPUUID) VALUES (-3,'Meeting',3,-1,'meeting.gif','2006')");
			listTypeStms.add("INSERT INTO TCATEGORY (PKEY, LABEL, TYPEFLAG, SORTORDER, SYMBOL, TPUUID) VALUES (5,'ActionItem',0,5,'actionItem.png','2003')");
			listTypeStms.add("INSERT INTO TCATEGORY (PKEY, LABEL, TYPEFLAG, SORTORDER, SYMBOL, TPUUID) VALUES (-1,'Task',1,-3,'task.png','2005')");

		} else if (ApplicationBean.getInstance().getAppType() == ApplicationBean.APPTYPE_FULL) {
			// listTypeStms.add("INSERT INTO TCATEGORY (PKEY, LABEL, TYPEFLAG, SORTORDER, TPUUID) VALUES (0, 'any list',0, -1,'2001')");
			listTypeStms.add("INSERT INTO TCATEGORY (PKEY, LABEL, TYPEFLAG, SORTORDER, SYMBOL, TPUUID) VALUES (1,'ProblemReport',0,1,'problemReport.png','2002')");
			listTypeStms.add("INSERT INTO TCATEGORY (PKEY, LABEL, TYPEFLAG, SORTORDER, SYMBOL, TPUUID) VALUES (5,'ActionItem',0,5,'actionItem.png','2003')");
			listTypeStms.add("INSERT INTO TCATEGORY (PKEY, LABEL, TYPEFLAG, SORTORDER, SYMBOL, TPUUID) VALUES (8,'Requirement',0,8,'requirements.png','2004')");

						listTypeStms.add("INSERT INTO TCATEGORY (PKEY, LABEL, TYPEFLAG, SORTORDER, SYMBOL, TPUUID) VALUES (-1,'Task',1,-3,'task.png','2005')");
						listTypeStms.add("INSERT INTO TCATEGORY (PKEY, LABEL, TYPEFLAG, SORTORDER, SYMBOL, TPUUID) VALUES (-3,'Meeting',3,-1,'meeting.gif','2006')");


		} else {
			// listTypeStms.add("INSERT INTO TCATEGORY (PKEY, LABEL, TYPEFLAG, SORTORDER, TPUUID) VALUES (0, 'any list',0, -1,'2001')");
			listTypeStms.add("INSERT INTO TCATEGORY (PKEY, LABEL, TYPEFLAG, SORTORDER, SYMBOL, TPUUID) VALUES (1,'ProblemReport',0,1,'problemReport.png','2002')");
			listTypeStms.add("INSERT INTO TCATEGORY (PKEY, LABEL, TYPEFLAG, SORTORDER, SYMBOL, TPUUID) VALUES (5,'ActionItem',0,5,'actionItem.png','2003')");
			listTypeStms.add("INSERT INTO TCATEGORY (PKEY, LABEL, TYPEFLAG, SORTORDER, SYMBOL, TPUUID) VALUES (8,'Requirement',0,8,'requirements.png','2004')");
			listTypeStms.add("INSERT INTO TCATEGORY (PKEY, LABEL, TYPEFLAG, SORTORDER, SYMBOL, TPUUID) VALUES (-1,'Task',1,-3,'task.png','2005')");
			listTypeStms.add("INSERT INTO TCATEGORY (PKEY, LABEL, TYPEFLAG, SORTORDER, SYMBOL, TPUUID) VALUES (-3,'Meeting',3,-1,'meeting.gif','2006')");
		}
		/*List<String> childItemTypesStmts = new ArrayList<String>();
		childItemTypesStmts.add("INSERT INTO TCHILDISSUETYPE (OBJECTID, ISSUETYPEPARENT, ISSUETYPECHILD, TPUUID) VALUES (1, -4, -5, '2100')");
		childItemTypesStmts.add("INSERT INTO TCHILDISSUETYPE (OBJECTID, ISSUETYPEPARENT, ISSUETYPECHILD, TPUUID) VALUES (2, -3, 5, '2200')");
		childItemTypesStmts.add("INSERT INTO TCHILDISSUETYPE (OBJECTID, ISSUETYPEPARENT, ISSUETYPECHILD, TPUUID) VALUES (3, -6, -4, '2200')");*/

		// issue states
		List<String> issueStatesStms = new ArrayList<String>();
		if (ApplicationBean.getInstance().getAppType() == ApplicationBean.APPTYPE_DESK) {
			//issueStatesStms.add("INSERT INTO TSTATE (PKEY, LABEL, STATEFLAG, SORTORDER, TPUUID) VALUES (0, 'any status', 0, -1,'3001')");
			issueStatesStms.add("INSERT INTO TSTATE (PKEY, LABEL, STATEFLAG, SORTORDER, SYMBOL, CSSSTYLE, TPUUID) VALUES (1, 'opened', 0, 1,'new.png','bgrColor__11CC33|color__FFFFFF', '3002')");
			issueStatesStms.add("INSERT INTO TSTATE (PKEY, LABEL, STATEFLAG, SORTORDER, SYMBOL, CSSSTYLE, TPUUID) VALUES (3, 'assigned', 0, 3,'delegated.png','bgrColor__FFFF00','3004')");
			issueStatesStms.add("INSERT INTO TSTATE (PKEY, LABEL, STATEFLAG, SORTORDER, SYMBOL, CSSSTYLE, TPUUID) VALUES (8, 'closed', 1, 8,'closed.png','bgrColor__FF0000|color__FFFFFF','3009')");
		} else {
			//issueStatesStms.add("INSERT INTO TSTATE (PKEY, LABEL, STATEFLAG, SORTORDER, TPUUID) VALUES (0, 'any status', 0, -1,'3001')");
			issueStatesStms.add("INSERT INTO TSTATE (PKEY, LABEL, STATEFLAG, SORTORDER, SYMBOL, CSSSTYLE, TPUUID) VALUES (1, 'opened', 0, 1,'new.png','bgrColor__11CC33|color__FFFFFF','3002')");
			issueStatesStms.add("INSERT INTO TSTATE (PKEY, LABEL, STATEFLAG, SORTORDER, SYMBOL,           TPUUID) VALUES (2, 'analyzed', 0, 2,'analyzed.gif','3003')");
			issueStatesStms.add("INSERT INTO TSTATE (PKEY, LABEL, STATEFLAG, SORTORDER, SYMBOL, CSSSTYLE, TPUUID) VALUES (3, 'assigned', 0, 3,'delegated.png','bgrColor__FFFF00','3004')");
			issueStatesStms.add("INSERT INTO TSTATE (PKEY, LABEL, STATEFLAG, SORTORDER, SYMBOL, CSSSTYLE, TPUUID) VALUES (4, 'suspended', 1, 9,'parking.png','bgrColor__00CCFF|color__FFFFFF','3005')");
			issueStatesStms.add("INSERT INTO TSTATE (PKEY, LABEL, STATEFLAG, SORTORDER, SYMBOL, CSSSTYLE, TPUUID) VALUES (5, 'processing', 0, 4,'gearwheel.png','bgrColor__0000FF|color__FFFFFF','3006')");
			issueStatesStms.add("INSERT INTO TSTATE (PKEY, LABEL, STATEFLAG, SORTORDER, SYMBOL, CSSSTYLE, TPUUID) VALUES (6, 'implemented', 0, 5,'implemented.gif','bgrColor__FFDDAA','3007')");
			issueStatesStms.add("INSERT INTO TSTATE (PKEY, LABEL, STATEFLAG, SORTORDER, SYMBOL,           TPUUID) VALUES (7, 'integrated', 2, 6,'integrated.gif','3008')");
			issueStatesStms.add("INSERT INTO TSTATE (PKEY, LABEL, STATEFLAG, SORTORDER, SYMBOL, CSSSTYLE, TPUUID) VALUES (8, 'closed', 1, 8,'closed.png','bgrColor__FF0000|color__FFFFFF','3009')");
			issueStatesStms.add("INSERT INTO TSTATE (PKEY, LABEL, STATEFLAG, SORTORDER, SYMBOL,           TPUUID) VALUES (9, 'assessing', 0, 7,'assess.png','3010')");

			issueStatesStms.add("INSERT INTO TSTATE (PKEY, LABEL, STATEFLAG, SORTORDER, SYMBOL, TPUUID) VALUES (10, 'unverified', 0, 9,'unverified.png','3011')");
			issueStatesStms.add("INSERT INTO TSTATE (PKEY, LABEL, STATEFLAG, SORTORDER, SYMBOL, TPUUID) VALUES (11, 'verified', 0, 10,'verified.png','3012')");
			issueStatesStms.add("INSERT INTO TSTATE (PKEY, LABEL, STATEFLAG, SORTORDER, SYMBOL, TPUUID) VALUES (12, 'approved', 0, 11,'approved.png','3013')");
			issueStatesStms.add("INSERT INTO TSTATE (PKEY, LABEL, STATEFLAG, SORTORDER, SYMBOL, TPUUID) VALUES (13, 'faulty', 0, 12,'faulty.png','3014')");
			issueStatesStms.add("INSERT INTO TSTATE (PKEY, LABEL, STATEFLAG, SORTORDER, SYMBOL, TPUUID) VALUES (14, 'frozen', 3, 13,'frozen.png','3015')");

		}
		// project states
		List<String> projStatesStms = new ArrayList<String>();
		projStatesStms.add("INSERT INTO TSYSTEMSTATE (OBJECTID, LABEL, STATEFLAG, ENTITYFLAG, SORTORDER, TPUUID) VALUES (3, 'in progress', 0, 1, 3,'4001')");
		projStatesStms.add("INSERT INTO TSYSTEMSTATE (OBJECTID, LABEL, STATEFLAG, ENTITYFLAG, SORTORDER, TPUUID) VALUES (4, 'on hold', 1, 1, 4,'4002')");
		projStatesStms.add("INSERT INTO TSYSTEMSTATE (OBJECTID, LABEL, STATEFLAG, ENTITYFLAG, SORTORDER, TPUUID) VALUES (6, 'archived', 2, 1, 6,'4003')");

		// release states
		List<String> relStatesStms = new ArrayList<String>();
		relStatesStms.add("INSERT INTO TSYSTEMSTATE (OBJECTID, LABEL, STATEFLAG, ENTITYFLAG, SORTORDER, TPUUID) VALUES (8, 'planned', 3, 3, 1,'5001')");
		relStatesStms.add("INSERT INTO TSYSTEMSTATE (OBJECTID, LABEL, STATEFLAG, ENTITYFLAG, SORTORDER, TPUUID) VALUES (9, 'in progress', 0, 2, 2,'5002')");
		relStatesStms.add("INSERT INTO TSYSTEMSTATE (OBJECTID, LABEL, STATEFLAG, ENTITYFLAG, SORTORDER, TPUUID) VALUES (10, 'on hold', 1, 2, 3,'5003')");
		relStatesStms.add("INSERT INTO TSYSTEMSTATE (OBJECTID, LABEL, STATEFLAG, ENTITYFLAG, SORTORDER, TPUUID) VALUES (11, 'released', 1, 2, 4,'5004')");
		relStatesStms.add("INSERT INTO TSYSTEMSTATE (OBJECTID, LABEL, STATEFLAG, ENTITYFLAG, SORTORDER, TPUUID) VALUES (12, 'archived', 2, 2, 5,'5005')");



		// account
		List<String> accStms = new ArrayList<String>();
		accStms.add("INSERT INTO TSYSTEMSTATE (OBJECTID, LABEL, STATEFLAG, ENTITYFLAG, SORTORDER, TPUUID) VALUES (13, 'opened', 0, 3, 1,'6001')");
		accStms.add("INSERT INTO TSYSTEMSTATE (OBJECTID, LABEL, STATEFLAG, ENTITYFLAG, SORTORDER, TPUUID) VALUES (14, 'closed', 2, 3, 3,'6002')");

		// department
		List<String> depStms = new ArrayList<String>();
		depStms.add("INSERT INTO TDEPARTMENT (PKEY, LABEL, TPUUID) VALUES (9,'Support','7001')");
		depStms.add("INSERT INTO TDEPARTMENT (PKEY, LABEL, TPUUID) VALUES (10,'Development','7002')");
		depStms.add("INSERT INTO TDEPARTMENT (PKEY, LABEL, TPUUID) VALUES (11,'Customer','7003')");

		// password is "tissi", change right after install!
		List<String> persStms = new ArrayList<String>();
		persStms.add("INSERT INTO TPERSON (PKEY, FIRSTNAME, LASTNAME, LOGINNAME, EMAIL," +
				"PASSWD, SALT, DEPKEY, DELETED, USERLEVEL,PREFERENCES,PREFEMAILTYPE," +
				"REMINDMEASMANAGER, REMINDMEASRESPONSIBLE," +
				"EMAILREMINDPLEVEL, EMAILREMINDSLEVEL, EMAILLEAD, PREFLOCALE, TPUUID) VALUES (" +
				"1, 'Genji System', 'Administrator', 'admin', 'admin@mydomain.com'," +
				"'ae77cce012870ee258543926feb497446acaa6c6787c38c69d37bc9ae2033891afc240c3beff6317e1bbdcd3616e6c2f30f5e78a48b733c6eae7dc50032d39ab', '322a6ed1-797e-4eab-86e6-f6124f9396ef', 9, 'N', 2," +
				"'ReminderDays=23456\nenableQueryLayout=true\nLoc=browser\nCsvEncoding=UTF-8\nCsvChar=,'," +
				"'HTML', 'Y', 'Y', 1, 1, 7, ''" +
				",'8001')");
		// password is "trackplus"
		persStms.add("INSERT INTO TPERSON (PKEY, FIRSTNAME, LASTNAME, LOGINNAME, EMAIL, "
				+"PASSWD, SALT, DEPKEY, DELETED, USERLEVEL,PREFLOCALE, PREFEMAILTYPE,TPUUID) VALUES ("
				+"101, 'John', 'Guest', 'guest','guest@somedomain.com', "
				+"'f590ab6709167409cfff9e1f8d5d86a0d740a25bdfa56ecdcf2743315a6aec0185bcb069b1bd14a2c9fa0a49c14af3a4f01b857781062091c8f2bf785fe2093c', 'e79818ce-d53e-400b-ae9f-fcd2a192c4db', 11, 'N', 4, '','HTML','8002')");

		persStms.add("INSERT INTO TPERSON (PKEY, FIRSTNAME, LASTNAME, LOGINNAME, EMAIL, "
				+"PASSWD, DEPKEY, DELETED, USERLEVEL,PREFLOCALE, PREFEMAILTYPE,TPUUID) VALUES ("
				+"102, 'Mary', 'Client', 'mclient','client@myclient.com', "
				+"'600df958bf1bc5ef32a3c48f8d8656456af2c4f5',11, 'N', 4, '','HTML','8003')");

		persStms.add("INSERT INTO TPERSON (PKEY, FIRSTNAME, LASTNAME, LOGINNAME, EMAIL, "
				+"PASSWD, DEPKEY, DELETED, USERLEVEL,PREFLOCALE, PREFEMAILTYPE,TPUUID) VALUES ("
				+"103, 'Peter', 'Technician', 'ptech','technician@mydomain.com', "
				+"'600df958bf1bc5ef32a3c48f8d8656456af2c4f5',9, 'N', 8, '','HTML','8004')");

		persStms.add("INSERT INTO TPERSON (PKEY, FIRSTNAME, LASTNAME, LOGINNAME, EMAIL, "
				+"PASSWD, DEPKEY, DELETED, USERLEVEL,PREFLOCALE, PREFEMAILTYPE,TPUUID) VALUES ("
				+"-1, 'Genji System', 'Workflow', 'workflow','workflow@mydomain.com', "
				+"'600df958bf1bc5ef32a3c48f8d8656456af2c4f5',9, 'N', 8, '','HTML','8005')");

		// Add some groups
		persStms.add("INSERT INTO TPERSON (PKEY, LOGINNAME, DELETED, ISGROUP, TPUUID) VALUES ("
				+ "104,'Technicians','N','Y','8004')");

		persStms.add("INSERT INTO TPERSON (PKEY, LOGINNAME, DELETED, ISGROUP, TPUUID) VALUES ("
				+ "105,'Clients','N','Y','8005')");

		persStms.add("INSERT INTO TGROUPMEMBER (OBJECTID, THEGROUP, PERSON, TPUUID) VALUES "
				+"(100,105,102,'10000')");
		persStms.add("INSERT INTO TGROUPMEMBER (OBJECTID, THEGROUP, PERSON, TPUUID) VALUES "
				+"(101,104,103,'101000')");


		// the actions: new,edit and extra action "Other"
		List<String> actStms = new ArrayList<String>();
		actStms.add("INSERT INTO TACTION (OBJECTID,NAME,LABEL,DESCRIPTION, TPUUID) VALUES (1,'New','New','New item','9001')");
		actStms.add("INSERT INTO TACTION (OBJECTID,NAME,LABEL,DESCRIPTION, TPUUID) VALUES (2,'Edit','Edit','Edit item','9002')");
		actStms.add("INSERT INTO TACTION (OBJECTID,NAME,LABEL,DESCRIPTION, TPUUID) VALUES (3,'Move','Move','Move item','9003')");
		actStms.add("INSERT INTO TACTION (OBJECTID,NAME,LABEL,DESCRIPTION, TPUUID) VALUES (4,'NewChild','NewChild','New child item','9004')");
		actStms.add("INSERT INTO TACTION (OBJECTID,NAME,LABEL,DESCRIPTION, TPUUID) VALUES (5,'ChangeStatus','ChangeStatus','Change status','9005')");


		// priorities
		List<String> prioStms = new ArrayList<String>();
		prioStms.add("INSERT INTO TPRIORITY (PKEY, LABEL, SORTORDER, WLEVEL, SYMBOL, CSSSTYLE, TPUUID) VALUES (1, 'occasionally', 1, 0,'occasionally.png','bgrColor__CCFFCC','10001')");
		prioStms.add("INSERT INTO TPRIORITY (PKEY, LABEL, SORTORDER, WLEVEL, SYMBOL, CSSSTYLE, TPUUID) VALUES (2, 'soon', 2, 1,'soon.png','bgrColor__FFE000','10002')");
		prioStms.add("INSERT INTO TPRIORITY (PKEY, LABEL, SORTORDER, WLEVEL, SYMBOL, CSSSTYLE, TPUUID) VALUES (3, 'immediate', 3, 2,'immediate.png','bgrColor__FF0000|color__FFFFFF','10003')");

		// severities
		List<String> sevStms = new ArrayList<String>();
		sevStms.add("INSERT INTO TSEVERITY (PKEY, LABEL, SORTORDER, WLEVEL, SYMBOL, CSSSTYLE, TPUUID) VALUES (1, 'non-critical', 1, 0,'minor.png','bgrColor__CCFFCC','11001')");
		sevStms.add("INSERT INTO TSEVERITY (PKEY, LABEL, SORTORDER, WLEVEL, SYMBOL, CSSSTYLE, TPUUID) VALUES (2, 'serious', 2, 1,'major.png','bgrColor__FFE000','11002')");
		sevStms.add("INSERT INTO TSEVERITY (PKEY, LABEL, SORTORDER, WLEVEL, SYMBOL, CSSSTYLE, TPUUID) VALUES (3, 'critical', 3, 2,'blocker.png','bgrColor__FF0000|color__FFFFFF','11003')");

		try {
			coni = getConnection();
			cono = getConnection();
			cono.setAutoCommit(true);

			Statement istmt = coni.createStatement();
			Statement ostmt = cono.createStatement();


			rs = istmt.executeQuery("SELECT * FROM TBASKET");
			if (rs == null || !rs.next()) {
				LOGGER.info("Filling TBASKET...");
				for (int i=0; i < basketStms.size(); ++i) {
					try {
						ostmt.executeUpdate(basketStms.get(i));
					} catch (Exception exc) {}
				}
			}

			ApplicationStarter.getInstance().actualizePercentComplete(ApplicationStarter.INIT_DB_DATA_STEP,
					ApplicationStarter.INIT_DB_DATA_TEXT);

			if (rs != null) {
				rs.close();
			}

			rs = istmt.executeQuery("SELECT * FROM TCATEGORY");
			if (rs == null || !rs.next()) {
				LOGGER.info("Filling TCATEGORY...");
				for (int i=0; i < listTypeStms.size(); ++i) {
					try {
						ostmt.executeUpdate(listTypeStms.get(i));
					} catch (Exception exc) {}
				}
			}

			ApplicationStarter.getInstance().actualizePercentComplete(ApplicationStarter.getInstance().INIT_DB_DATA_STEP,
					ApplicationStarter.INIT_DB_DATA_TEXT);

			if (rs != null) {
				rs.close();
			}

			rs = istmt.executeQuery("SELECT * FROM TSTATE");
			if (rs == null || !rs.next()) {
				LOGGER.info("Filling TSTATE...");
				for (int i=0; i < issueStatesStms.size(); ++i) {
					try {
						ostmt.executeUpdate(issueStatesStms.get(i));
					} catch (Exception exc) {
						LOGGER.error(exc.getMessage());
						LOGGER.debug(STACKTRACE, exc);
					}
				}
			}

			ApplicationStarter.getInstance().actualizePercentComplete(ApplicationStarter.getInstance().INIT_DB_DATA_STEP,
					ApplicationStarter.INIT_DB_DATA_TEXT);

			if (rs != null) {
				rs.close();
			}

			rs = istmt.executeQuery("SELECT * FROM TSYSTEMSTATE");
			if (rs == null || !rs.next()) {
				LOGGER.info("Filling TSYSTEMSTATE...");
				for (int i=0; i < projStatesStms.size(); ++i) {
					try {
						ostmt.executeUpdate(projStatesStms.get(i));
					} catch (Exception exc) {LOGGER.error(exc.getMessage());}
				}

				for (int i=0; i < relStatesStms.size(); ++i) {
					try {
						ostmt.executeUpdate(relStatesStms.get(i));
					} catch (Exception exc) {
						LOGGER.error(exc.getMessage());
						LOGGER.debug(STACKTRACE, exc);
					}
				}

				for (int i=0; i < accStms.size(); ++i) {
					try {
						ostmt.executeUpdate(accStms.get(i));
					} catch (Exception exc) {
						LOGGER.error(exc.getMessage());
						LOGGER.debug(STACKTRACE, exc);
					}
				}
			}


			ApplicationStarter.getInstance().actualizePercentComplete(ApplicationStarter.getInstance().INIT_DB_DATA_STEP,
					ApplicationStarter.INIT_DB_DATA_TEXT);

			if (rs != null) {
				rs.close();
			}

			rs = istmt.executeQuery("SELECT * FROM TDEPARTMENT");
			if (rs == null || !rs.next()) {
				LOGGER.info("Filling TDEPARTMENT...");
				for (int i=0; i < depStms.size(); ++i) {
					try {
						ostmt.executeUpdate(depStms.get(i));
					} catch (Exception exc) {
						LOGGER.error(exc.getMessage());
						LOGGER.debug(STACKTRACE, exc);
					}
				}
			}

			if (rs != null) {
				rs.close();
			}

			rs = istmt.executeQuery("SELECT * FROM TPERSON");
			if (rs == null || !rs.next()) {
				LOGGER.info("Filling TPERSON...");
				for (int i=0; i < persStms.size(); ++i) {
					try {
						ostmt.executeUpdate(persStms.get(i));
					} catch (Exception exc) {
						LOGGER.error(exc.getMessage());
						LOGGER.debug(STACKTRACE, exc);
					}
				}
			} else {
				ostmt.executeUpdate("UPDATE TPERSON SET USERLEVEL=2 WHERE LOGINNAME='admin'");
			}

			if (rs != null) {
				rs.close();
			}

			rs = istmt.executeQuery("SELECT * FROM TACTION");
			if (rs == null || !rs.next()) {
				LOGGER.info("Filling TACTION...");
				for (int i=0; i < actStms.size(); ++i) {
					try {
						ostmt.executeUpdate(actStms.get(i));
					} catch (Exception exc) {
						LOGGER.error(exc.getMessage());
						LOGGER.debug(STACKTRACE, exc);
					}
				}
			}

			if (rs != null) {
				rs.close();
			}

			rs = istmt.executeQuery("SELECT * FROM TPRIORITY");
			if (rs == null || !rs.next()) {
				LOGGER.info("Filling TPRIORITY...");
				for (int i=0; i < prioStms.size(); ++i) {
					try {
						ostmt.executeUpdate(prioStms.get(i));
					} catch (Exception exc) {
						LOGGER.error(exc.getMessage());
						LOGGER.debug(STACKTRACE, exc);
					}
				}
			}

			if (rs != null) {
				rs.close();
			}

			ApplicationStarter.getInstance().actualizePercentComplete(ApplicationStarter.getInstance().INIT_DB_DATA_STEP,
					ApplicationStarter.INIT_DB_DATA_TEXT);

			rs = istmt.executeQuery("SELECT * FROM TSEVERITY");
			if (rs == null || !rs.next()) {
				LOGGER.info("Filling TSEVERITY...");
				for (int i=0; i < sevStms.size(); ++i) {
					try {
						ostmt.executeUpdate(sevStms.get(i));
					} catch (Exception exc) {
						LOGGER.error(exc.getMessage());
						LOGGER.debug(STACKTRACE, exc);
					}
				}
			}

		} catch (Exception e) {
			System.err.println(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) rs.close();
				if (coni != null) coni.close();
				if (cono != null) cono.close();
			} catch (Exception e) {
				System.err.println(ExceptionUtils.getStackTrace(e));
			}
		}
	}

	//  Let's add objects with object ids (primary keys) of "0" to
	//  all tables that might be used in a wild card mode. "0" has
	//  the meaning of "don't care". We try to avoid nulls in the
	//  database wherever possible since this can give many problems
	//  with SQL joins.
	private static void insertNullObjectsAndSampleData() {
		ResultSet rs = null;
		Connection coni = null;
		Connection cono = null;
		try {
			coni = getConnection();
			cono = getConnection();
			Statement istmt = coni.createStatement();
			Statement ostmt = cono.createStatement();
			LOGGER.info("Testing for NULL objects...");
			// --------------- T S I T E ----------------------
			rs = istmt.executeQuery("SELECT * FROM TSITE");
			if (rs == null || !rs.next()) {
				try {
					ostmt.execute("INSERT INTO TSITE " + "(OBJECTID) " +
							"VALUES (1)");
					LOGGER.info("Inserted TSITE");
				} catch (Exception exc) {
					LOGGER.error("Problem inserting TSITE object: " + exc.getMessage());
					LOGGER.debug(STACKTRACE, exc);
				}
			}

			// --------------- T P R O J E C T T Y P E ----------------------
			rs = istmt.executeQuery("SELECT * FROM TPROJECTTYPE WHERE OBJECTID = 0");
			if (rs == null || !rs.next()) {
				try {
					ostmt.execute("INSERT INTO TPROJECTTYPE " + "(OBJECTID, LABEL, NOTIFYOWNERLEVEL, NOTIFYMANAGERLEVEL) " +
							"VALUES (0, 'Generic Space', 0, 1)");
					LOGGER.info("Inserted NULL project (PKEY = 0) into TPROJECTTYPE");
				} catch (Exception exc) {
					LOGGER.error("Problem inserting NULL object for TPROJECTTYPE: " + exc.getMessage());
					LOGGER.debug(STACKTRACE, exc);
				}
			}

			rs = istmt.executeQuery("SELECT * FROM TPROJECTTYPE WHERE OBJECTID = -1");
			if (rs == null || !rs.next()) {
				try {
					ostmt.execute("INSERT INTO TPROJECTTYPE " + "(OBJECTID, LABEL, DEFAULTFORPRIVATE) " +
							"VALUES (-1, 'Private Project', 'Y')");
					LOGGER.info("Inserted Private project (PKEY = -1) into TPROJECTTYPE");
				} catch (Exception exc) {
					LOGGER.error("Problem inserting private space in TPROJECTTYPE: " + exc.getMessage());
					LOGGER.debug(STACKTRACE, exc);
				}
			}


			rs = istmt.executeQuery("SELECT * FROM TPROJECT WHERE PKEY = 0");
			// ------------------- T P R O J E C T  -----------------------
			if (rs == null || !rs.next()) {
				try {
					ostmt.execute("INSERT INTO TPROJECT " + "(PKEY, LABEL, DEFOWNER, DEFMANAGER, PROJECTTYPE) " +
							"VALUES (0, 'Generic Space', 1, 1, 0)");
					LOGGER.info("Inserted NULL project (PKEY = 0) into TPROJECT");
				} catch (Exception exc) {LOGGER.error("Problem inserting NULL object for TPROJECT: " + exc.getMessage());}
			}

			// ----------------------- T R O L E ------------------------------
			rs = istmt.executeQuery("SELECT * FROM TROLE WHERE PKEY = -1");
			if (rs == null || !rs.next()) {
				try {
					ostmt.execute("INSERT INTO TROLE " +
							"(PKEY, LABEL, ACCESSKEY, EXTENDEDACCESSKEY, PROJECTTYPE) " +
							"VALUES (-1, 'PrivateRole', 0, '111111111111', 0)");
					LOGGER.info("Inserted private role (PKEY = -1) into TROLE");
				} catch (Exception exc) {
					LOGGER.error("Problem inserting NULL object for TROLE: " + exc.getMessage());
					LOGGER.debug(STACKTRACE, exc);
				}
			}

			LOGGER.info("NULL objects are okay.");


			if (isFirstStartEver) {
				LOGGER.info("Filling some sample data...");
				try {

					URL populateURL = ApplicationBean.getInstance().getServletContext()
							.getResource(populateSql);
					if (populateURL == null) {
						ClassLoader cl = InitDatabase.class.getClassLoader();
						populateURL = cl.getResource(populateSql);
					}
					InputStream in = populateURL.openStream();
					java.util.Scanner s = new java.util.Scanner(in, "UTF-8");
					s.useDelimiter(";");
					String st = null;
					StringBuffer stb = new StringBuffer();
					int line = 0;

					ApplicationStarter.getInstance().actualizePercentComplete(ApplicationStarter.getInstance().INIT_DB_DATA_STEP,
							ApplicationStarter.INIT_DB_DATA_TEXT);

					while (s.hasNext()) {
						stb.append(s.nextLine().trim());
						st = stb.toString();
						++line;
						if (!st.isEmpty() && !st.startsWith("--") && !st.startsWith("/*")) {
							if (st.endsWith(";")) {
								stb = new StringBuffer(); // clear buffer
								st = st.substring(0, st.length()-1); // remove the semicolon
								try {
									ostmt.executeUpdate(st);
								} catch (Exception exc) {
									LOGGER.error("Problem inserting sample data: " + exc.getMessage());
									LOGGER.error("Line " + line + ": " + st);
								}
							} else {
								stb.append(" ");
							}
						} else {
							stb = new StringBuffer();
						}
					}
					s.close();
					in.close();

				}catch(Exception e) {
					System.err.println(ExceptionUtils.getStackTrace(e));
				}
				LOGGER.info("Sample data is okay.");

				ApplicationStarter.getInstance().actualizePercentComplete(ApplicationStarter.getInstance().INIT_DB_DATA_STEP,
						ApplicationStarter.INIT_DB_DATA_TEXT);
			}
		} catch (Exception e) {
			LOGGER.error("Problem inserting null objects: " + e.getMessage());
			LOGGER.debug(STACKTRACE, e);
		} finally {
			if (coni!=null) {
				try {
					coni.close();
				} catch (SQLException e) {
					LOGGER.info("Closing connection failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
			if (cono!=null) {
				try {
					cono.close();
				} catch (SQLException e) {
					LOGGER.info("Closing connection failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
		}
	}

	//  Let's add objects from SQL script which require other objects from XML imports
	private static void insertPostLoadData(String file) {
		Connection coni = null;
		Connection cono = null;
		try {
			coni = getConnection();
			cono = getConnection();
			Statement ostmt = cono.createStatement();
			if (isFirstStartEver) {
				LOGGER.info("Filling some post load data...");
				try {

					URL populateURL = ApplicationBean.getInstance().getServletContext()
							.getResource(file);
					InputStream in = populateURL.openStream();
					java.util.Scanner s = new java.util.Scanner(in, "UTF-8");
					s.useDelimiter(";");
					String st = null;
					StringBuffer stb = new StringBuffer();
					int line = 0;

					while (s.hasNext()) {
						stb.append(s.nextLine().trim());
						st = stb.toString();
						++line;
						if (!st.isEmpty() && !st.startsWith("--") && !st.startsWith("/*")) {
							if (st.endsWith(";")) {
								stb = new StringBuffer(); // clear buffer
								st = st.substring(0, st.length()-1); // remove the semicolon
								try {
									ostmt.executeUpdate(st);
									LOGGER.info(st);
								} catch (Exception exc) {
									LOGGER.error("Problem inserting post-load data: " + exc.getMessage());
									LOGGER.error("Line " + line + ": " + st);
								}
							} else {
								stb.append(" ");
							}
						} else {
							stb = new StringBuffer();
						}
					}
					s.close();
					in.close();

				}catch(Exception e) {
					System.err.println(ExceptionUtils.getStackTrace(e));
				}
				LOGGER.info("Post-load data is okay.");
			}
			ApplicationStarter.getInstance().actualizePercentComplete(ApplicationStarter.getInstance().INIT_DB_DATA_STEP, ApplicationStarter.INIT_DB_DATA_TEXT);
		} catch (Exception e) {
			LOGGER.error("Problem inserting post-load objects: " + e.getMessage(), e);
			LOGGER.debug(STACKTRACE, e);
		} finally {
			if (coni!=null) {
				try {
					coni.close();
				} catch (SQLException e) {
					LOGGER.info("Closing connection failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
			if (cono!=null) {
				try {
					cono.close();
				} catch (SQLException e) {
					LOGGER.info("Closing connection failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
		}
	}





	/*
	 * Class represents a database table
	 */
	class Table {
		private String name = "";
		private List<Column> columns = null;

		public Table(String _name) {
			this.name = _name;
		}

		public void setName(String _name) {
			this.name = _name;
		}

		public String getName() {
			return this.name;
		}

		public void setColumns(List<Column> _columns) {
			this.columns = _columns;
		}

		public List<Column> getColumns() {
			return this.columns;
		}
	}

	/*
	 * Class represents a database column
	 */
	class Column {
		private String name = "";
		private String type = "";
		private Boolean requ = false;
		private Boolean isPrimaryKey = false;

		public Column(String _name) {
			this.name=_name;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public Boolean getRequ() {
			return requ;
		}
		public void setRequ(Boolean requ) {
			this.requ = requ;
		}

		public Boolean isPrimaryKey() {
			return isPrimaryKey;
		}
		public void setIsPrimaryKey(Boolean _isPrimaryKey) {
			this.isPrimaryKey = _isPrimaryKey;
		}
	}

	/*
	 * Class that can parse the Torque XML schema file.
	 */
	class SchemaParser {

		Document dom = null;

		public List<InitDatabase.Table> parseSchemaFile(){
			//get the factory
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

			try {

				//Using factory get an instance of document builder
				DocumentBuilder db = dbf.newDocumentBuilder();
				db.setEntityResolver(new EntityResolver() {
					public InputSource resolveEntity(String publicId, String systemId)
							throws SAXException, IOException {
						URL dtdURL = ApplicationBean.getInstance().getServletContext()
								.getResource("/WEB-INF/schema/database.dtd");
						InputStream isd = dtdURL.openStream();;
						return new InputSource(isd);
					}
				});
				URL schemaURL = ApplicationBean.getInstance().getServletContext()
						.getResource("/WEB-INF/schema/track-schema.xml");
				InputStream in = schemaURL.openStream();
				//parse using builder to get DOM representation of the XML file
				dom = db.parse(in);
				in.close();

			}catch(ParserConfigurationException pce) {
				System.err.println(ExceptionUtils.getStackTrace(pce));
			}catch(SAXException se) {
				System.err.println(ExceptionUtils.getStackTrace(se));
			}catch(IOException ioe) {
				System.err.println(ExceptionUtils.getStackTrace(ioe));
			}

			return parseDocument();
		}

		private List<InitDatabase.Table> parseDocument(){
			//get the root element
			Element docEle = dom.getDocumentElement();
			List<InitDatabase.Table> tables = null;
			List<Column> columns = null;

			//get a nodelist of elements
			NodeList nl = docEle.getElementsByTagName("table");
			NodeList nlc = null;
			if(nl != null && nl.getLength() > 0) {
				tables = new ArrayList<InitDatabase.Table>();
				for(int i = 0 ; i < nl.getLength();i++) {

					//get the table element
					Element el = (Element)nl.item(i);
					Table table = new Table(el.getAttribute("name"));
					nlc = el.getElementsByTagName("column");
					if(nlc != null && nlc.getLength() > 0) {
						columns = new ArrayList<Column>();
						for(int j = 0 ; j < nlc.getLength(); ++j) {
							Element elc = (Element)nlc.item(j);
							Column column = new Column(elc.getAttribute("name"));
							String spk = elc.getAttribute("primaryKey");
							if (spk != null) {
								column.setIsPrimaryKey(new Boolean(spk));
							}
							columns.add(column);
						}
					}
					table.setColumns(columns);
					tables.add(table);
				}
			}
			return tables;
		}
	}
	/*
	 * Patch the IdTable and at the same time check if the database structure is
	 * consistent. This is just a rough check if all required columns are present.
	 */
	public static Connection getConnection() throws Exception {
		try {
			if (con != null && ! con.isClosed()) {
				return con;
			}
			PropertiesConfiguration tcfg = HandleHome.getTorqueProperties(ApplicationBean.getInstance().getServletContext(), false);
			Class.forName(tcfg.getString("torque.dsfactory.track.connection.driver"));
			con = DriverManager.getConnection(tcfg.getString("torque.dsfactory.track.connection.url"),
					tcfg.getString("torque.dsfactory.track.connection.user"),
					tcfg.getString("torque.dsfactory.track.connection.password"));
		} catch (Exception e) {
			LOGGER.error("Could not establish a database connection to " + tcfg.getString("torque.dsfactory.track.connection.url")
					+ " with user " + tcfg.getString("torque.dsfactory.track.connection.user") + ": " + e.getMessage(), e);
			LOGGER.debug(STACKTRACE, e);
			throw e;
		}
		return con;
	}



	/**
	 * Sets the fields to appear on the tree query for backward
	 * compatibility to preserve the layout of the tree query
	 */
	public static void setFilterFields(boolean includeSubproject, boolean includeClass) {
		LOGGER.info("Setting filter fields");
		List<Integer> defaultFilterFields = new ArrayList<Integer>();
		defaultFilterFields.add(SystemFields.INTEGER_PROJECT);
		//set also the subproject and class even if they will be transformed to custom selects
		//(they should not silently disappear from the tree filter)
		if (includeSubproject) {
			defaultFilterFields.add(SystemFields.INTEGER_SUBPROJECT);
		}
		if (includeClass) {
			defaultFilterFields.add(SystemFields.INTEGER_CLASS);
		}
		defaultFilterFields.add(SystemFields.INTEGER_RESPONSIBLE);
		defaultFilterFields.add(SystemFields.INTEGER_STATE);
		defaultFilterFields.add(SystemFields.INTEGER_ISSUETYPE);
		List<TFieldBean> systemFields = FieldBL.loadByFieldIDs(defaultFilterFields.toArray());
		for (TFieldBean fieldBean : systemFields) {
			fieldBean.setFilterFieldString(true);
			FieldBL.save(fieldBean);
		}
	}

	/**
	 * Update the message of the day shown on the front page
	 * @param site
	 */
	private static void setMessageOfTheDay(TSiteBean site, String theVersion) {

		Locale[] availableLocales = Locale.getAvailableLocales(); // all on this system
		ResourceBundle messages = null;
		HashMap<String,String> languages = new HashMap<String,String>();

		for (int i=0; i < availableLocales.length; ++i) {
			Locale locale = availableLocales[i];
			locale = new Locale(locale.getLanguage()); // just reduce the locale to the language specific one (no country)
			messages = ResourceBundle.getBundle("resources.UserInterface.ApplicationResources",
					locale);
			Locale theRealLoc = messages.getLocale(); // the existing locale found here
			String test = languages.get(theRealLoc.getLanguage()); // check if the language has already been treated
			if (test == null) {

				languages.put(theRealLoc.getLanguage(), "x");

				String motdMessage = messages.getString("motd.message");
				String motdTeaser = messages.getString("motd.teaser");

				TMotdBean motd = MotdBL.loadMotd(theRealLoc.getLanguage()); //TMotdPeer.load(theRealLoc);
				try {
					if (motd == null && motdMessage != null && motdMessage.trim().length() > 0) {
						motd = new TMotdBean();
						motd.setTheLocale(theRealLoc.getLanguage());
						motd.setTheMessage(motdMessage);
						motd.setTeaserText(motdTeaser);
						MotdBL.saveMotd(motd);
						LOGGER.info("Created new MOTD for language " + theRealLoc.getLanguage());
					} else if (motd != null && motdMessage != null && motdMessage.trim().length() > 0) {
						motd.setTheMessage(motdMessage + " " + motd.getTheMessage());
						motd.setTeaserText(motdTeaser);
						MotdBL.saveMotd(motd);
						LOGGER.info("Updated MOTD for language " + theRealLoc.getLanguage());
					}
				}
				catch (Exception e) {
					LOGGER.error("Error updating MOTD: " + e.getMessage());
					LOGGER.debug(STACKTRACE, e);
				}
			}
		}
	}

	public static TSiteBean setDirectories(TSiteBean site) {
		String tphome = HandleHome.getTrackplus_Home();

		if (site.getAttachmentRootDir() == null || "".equals(site.getAttachmentRootDir())) {
			site.setAttachmentRootDir(tphome);
		}
		if (site.getIndexPath() == null || "".equals(site.getIndexPath())) {
			site.setIndexPath(tphome+File.separator+"index");
		}
		if (site.getBackupDir() == null || "".equals(site.getBackupDir())) {
			site.setBackupDir(tphome+File.separator+HandleHome.DB_BACKUP_DIR);
		}
		return site;
	}

	/**
	 * Load predefined sample workflows
	 */
	public static void loadWorkflows() {

		try {
			LOGGER.info("Loading sample workflows.");
			URL propURL = ApplicationBean.getInstance().getServletContext()
					.getResource(initDataDir+"/Workflows/Workflows.xml");
			InputStream in = propURL.openStream();
			//TODO load workflows into database
			in.close();
		} catch(Exception e) {
			LOGGER.error("Can't read workflow definition file: " + e.getMessage());
			LOGGER.debug(STACKTRACE, e);
		}
	}

}
