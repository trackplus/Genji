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

package com.aurel.track.prop;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.torque.Torque;

import com.aurel.track.ApplicationStarter;
import com.aurel.track.Constants;
import com.aurel.track.GeneralSettings;
import com.aurel.track.admin.customize.category.filter.FilterBL;
import com.aurel.track.admin.customize.category.filter.execute.TreeFilterExecuterFacade;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadTreeFilterItemCounts;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.customize.scripting.BINDING_PARAMS;
import com.aurel.track.admin.customize.scripting.GroovyScriptExecuter;
import com.aurel.track.admin.server.motd.MotdBL;
import com.aurel.track.admin.server.siteConfig.LicenseTO;
import com.aurel.track.admin.server.siteConfig.SiteConfigBL;
import com.aurel.track.admin.shortcut.ShortcutBL;
import com.aurel.track.admin.user.avatar.AvatarBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.admin.user.userLevel.UserLevelsProxy;
import com.aurel.track.attachment.AttachBL;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TMotdBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dbase.HandleHome;
import com.aurel.track.item.lock.ItemLockBL;
import com.aurel.track.itemNavigator.filterInMenu.FilterInMenuBL;
import com.aurel.track.itemNavigator.filterInMenu.FilterInMenuJSON;
import com.aurel.track.itemNavigator.filterInMenu.FilterInMenuTO;
import com.aurel.track.json.ControlError;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.master.MasterHomeJSON;
import com.aurel.track.mobile.MobileBL;
import com.aurel.track.plugin.ModuleDescriptor;
import com.aurel.track.plugin.PluginManager;
import com.aurel.track.resources.LocalizeJSON;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.user.LogoffBL;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.LocaleHandler;
import com.aurel.track.util.SessionUtils;
import com.aurel.track.util.Support;
import com.aurel.track.util.emailHandling.Html2Text;
import com.aurel.track.util.event.IEventSubscriber;
import com.opensymphony.xwork2.ActionContext;
import com.trackplus.license.LicenseManager;

public class LoginBL {

	private static final Logger LOGGER = LogManager.getLogger(LoginBL.class);
	private static final Logger ACCESSLOGGER = LogManager.getLogger("Access");

	private static final String SUCCESS = "success";
	private static final String ISMOBILEAPP = "isMobileApplication";
	private static final String DATABRACE = "\"data\":{";
	private static final String DOTACTION = ".action";

	/**
	 * Hide the constructor
	 */
	private LoginBL() {
	}

	/**
	 * This method controls entire login procedure.
	 *
	 * @param isInTestMode
	 * @param isMobileApplication
	 * @param username
	 * @param usingContainerBasedAuthentication
	 * @param password
	 * @param forwardUrl
	 * @param springAuthenticated
	 * @param mobileApplicationVersionNo
	 * @param locale
	 * @return
	 */
	public static String login(String isInTestMode, boolean isMobileApplication, String username, boolean usingContainerBasedAuthentication, String password,
			String forwardUrl, boolean springAuthenticated, Integer mobileApplicationVersionNo, Locale _locale) {
		Boolean ready = (Boolean) ServletActionContext.getServletContext().getAttribute(ApplicationStarter.READY);
		if (ready == null || !ready.booleanValue() ) {
			return "loading";
		}
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession httpSession = request.getSession();
		String nonce = (String) httpSession.getAttribute("NONCE");

		if ("true".equals(isInTestMode)) {
			nonce = null; // accept clear text passwords
		}

		httpSession.setAttribute(ISMOBILEAPP, isMobileApplication);

		Locale locale = _locale;
		if (locale == null) {
			locale = Locale.getDefault();
			LOGGER.debug("Requested locale is null. Using default:" + locale.getDisplayName());
		} else {
			LOGGER.debug("Requested locale " + locale.getDisplayName());
		}
		httpSession.setAttribute("localizationJSON", LocalizeJSON.encodeLocalization(locale));
		TMotdBean motd = MotdBL.loadMotd(locale.getLanguage());

		if (motd == null) {
			motd = MotdBL.loadMotd("en");
		}
		// if already logged in forward to home page
		if (SessionUtils.getCurrentUser(httpSession) != null) {

			String redirectMapEntry = "itemNavigator";
			TPersonBean personBean = (TPersonBean) httpSession.getAttribute(Constants.USER_KEY);
			if (personBean != null && personBean.getHomePage() != null && personBean.getHomePage().trim().length() > 0) {
				redirectMapEntry = personBean.getHomePage();
			}
			StringBuilder sb = new StringBuilder();
			sb.append("{");
			JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
			sb.append(DATABRACE);
			JSONUtility.appendStringValue(sb, "jsonURL", redirectMapEntry + DOTACTION, true);
			sb.append("}");
			sb.append("}");
			return LoginBL.writeJSONResponse(sb); // The redirect is done by the
													// client JavaScript
		}
		// if Container Based Authentication is enabled and we can get a remote
		// user we use that one, no more questions asked. However, a local login
		// always overrules.

		if ((username == null || "".equals(username) || password == null || "".equals(password))
			&& (request.getRemoteUser() != null
			&& ApplicationBean.getInstance().getSiteBean().getIsCbaAllowed())) {
				username = request.getRemoteUser();
				usingContainerBasedAuthentication = true;
		}

		List<LabelValueBean> errors = new ArrayList<LabelValueBean>();
		StringBuilder sb = new StringBuilder();
		String redirectMapEntry = "";
		sb = LoginBL.createLoginResponseJSON(username, password, nonce, usingContainerBasedAuthentication, springAuthenticated, request, errors, httpSession,
				forwardUrl, motd, isMobileApplication, locale, mobileApplicationVersionNo, redirectMapEntry);

		if (errors != null && errors.size() > 0 && usingContainerBasedAuthentication) {
			return "forwardToLogin"; // could not verify container registered
										// user with Genji
		}

		if (usingContainerBasedAuthentication && !isMobileApplication) {
			ACCESSLOGGER.info("User was authenticated via container.");
			if (redirectMapEntry.isEmpty())
				return SUCCESS;
			return redirectMapEntry;
		}
		return writeJSONResponse(sb); // The redirect is done by the client
										// JavaScript
	}

	/**
	 * This method controls entire login procedure.
	 *
	 * @param isInTestMode
	 * @param isMobileApplication
	 * @param username
	 * @param usingContainerBasedAuthentication
	 * @param password
	 * @param forwardUrl
	 * @param springAuthenticated
	 * @param mobileApplicationVersionNo
	 * @param locale
	 * @return
	 */
	public static String restLogin(String username, String password, Locale locale) {
		Map envResult = null;
		ArrayList<LabelValueBean> errors = new ArrayList<LabelValueBean>();

		TPersonBean user = userIdentifiedByToken(username, password);
		if (user == null) {
			HttpServletRequest request = ServletActionContext.getRequest();

			if (locale == null) {
				locale = Locale.getDefault();
				LOGGER.debug("Requested locale is null. Using default:" + locale.getDisplayName());
			} else {
				LOGGER.debug("Requested locale " + locale.getDisplayName());
			}

			envResult = LoginBL.setEnvironment(username, password, null, request, ActionContext.getContext().getSession(), false, false, false);

			user = (TPersonBean) envResult.get("user");

			String tokenPasswd = DigestUtils.md5Hex(Long.toString(new Date().getTime())).substring(0, 7);

			if (user != null) {
				user.setTokenPasswd(tokenPasswd);
			}

			Integer mappingEnum = (Integer) envResult.get("mappingEnum");

			errors = (ArrayList<LabelValueBean>) envResult.get("errors");
		}

		Boolean ready = (Boolean) ServletActionContext.getServletContext().getAttribute(ApplicationStarter.READY);
		if (ready == null || ready.booleanValue() == false) {
			errors.add(new LabelValueBean("notReady", "Server not ready"));
		}
		StringBuilder sb = new StringBuilder("{");
		if (user == null || (errors != null && errors.size() > 0)) {
			JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, false);
			sb.append(DATABRACE);
			JSONUtility.appendLabelValueBeanList(sb, "errors", errors);
		} else {
			user.setTokenExpDate(new Date(new Date().getTime() + 120 * 60 * 1000));
			DAOFactory.getFactory().getPersonDAO().save(user);

			JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
			sb.append(DATABRACE);
			JSONUtility.appendStringValue(sb, "token", user.getTokenPasswd());
		}
		sb.append("}}");

		return writeJSONResponse(sb); // The redirect is done by the client
										// JavaScript
	}

	/**
	 * Return a bean with the user if the user with this username has a valid
	 * unexpired token. This is to prevent using the clear text password all the
	 * time. The token is created when using the restLogin routine for the first
	 * time.
	 *
	 * @param username
	 * @param token
	 */
	public static TPersonBean userIdentifiedByToken(String username, String token) {
		TPersonBean user = DAOFactory.getFactory().getPersonDAO().loadByLoginName(username);
		if (user != null && token != null && token.equals(user.getTokenPasswd())) {
			if (user.getTokenExpDate() != null && new Date().getTime() < user.getTokenExpDate().getTime()) {
				user.setTokenExpDate(new Date(new Date().getTime() + 120 * 60 * 1000));
				DAOFactory.getFactory().getPersonDAO().save(user);
				return user;
			}
		}
		return null;
	}

	public static String writeJSONResponse(StringBuilder sb) {
		try {
			ServletActionContext.getResponse().addHeader("Access-Control-Allow-Origin", "*");
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.println(sb);
		} catch (IOException e) {
			// can't do much here
			LOGGER.error(e);
		}
		return null;
	}

	/**
	 *
	 * @param username
	 * @param userPwd
	 * @param nonce
	 * @param request
	 * @param anonymousLogin
	 * @return Map with two entries: 1. "errors": ArrayList<LabelValueBean>; 2.
	 *         "mappingEnum": Integer with 2: bad credentials, 6: license
	 *         problems, 7: forward to URL, 8: first time admin user, 18:
	 *         request license, 9: standard login
	 *
	 */
	public static Map<String, Object> setEnvironment(String username, String userPwd, String nonce, HttpServletRequest request, Map<String, Object> sessionMap,
			boolean anonymousLogin, boolean usingContainerBasedAuthentication, boolean springAuthenticated) {
		HttpSession httpSession = request.getSession();
		ArrayList<LabelValueBean> errors = new ArrayList<LabelValueBean>();
		HashMap<String, Object> result = new HashMap<String, Object>();
		Integer mappingEnum = 0;

		// Make things robust
		if (username == null) {
			username = "x";
		}
		if (userPwd == null) {
			userPwd = "x";
		}
		// Move locale to one that we actually have, in case there
		// was a request for a locale that we do not have
		Locale locale = LocaleHandler.getExistingLocale(request.getLocales());
		LocaleHandler.exportLocaleToSession(sessionMap, locale);
		Support support = new Support();
		support.setURIs(request);
		if (username != null) {
			ACCESSLOGGER.info("LOGON: User '" + username.trim() + "' trying to log on" + " at " + new Date().toString() + " from " + request.getRemoteAddr());
		}
		ServletContext servletContext = org.apache.struts2.ServletActionContext.getServletContext();
		try {
			if (!Torque.isInit()) {
				Torque.init(HandleHome.getTorqueProperties(servletContext, true));
				LOGGER.debug("Database is " + Torque.getDefaultDB());
				LOGGER.info("Torque was re-initialized.");
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			LOGGER.error("Could not initialize Torque (1)");
			LOGGER.error(ExceptionUtils.getStackTrace(e));
			errors.add(new LabelValueBean("errGeneralError", getText("logon.err.noDataBase", locale) + ":" + e.getMessage()));
			mappingEnum = 1;
			result.put("errors", errors);
			result.put("mappingEnum", mappingEnum);
			return result;
		}
		TPersonBean personBean = null;
		if (anonymousLogin) {
			personBean = PersonBL.getAnonymousIfActive();
		} else {
			try {
				String pwd = "";
				if (nonce == null || nonce.length() == 0) {
					pwd = userPwd; // clear text
				} else {
					pwd = decrypt(nonce.charAt(0), userPwd); // key is first
																// character of
																// nonce
				}
				personBean = PersonBL.loadByLoginNameWithRights(username);

				if (personBean != null) {
					personBean.setPlainPwd(pwd);

					if (personBean.isDisabled()) {
						errors.add(new LabelValueBean("errCredentials", getText("logon.err.user.disabled", locale)));
						ACCESSLOGGER.warn("LOGON: User " + personBean.getLoginName() + " is disabled, login refused!");
					} else if (usingContainerBasedAuthentication == false && springAuthenticated == false && !personBean.authenticate(pwd)) {
						ACCESSLOGGER.warn("LOGON: Wrong password given for user " + personBean.getFullName() + " at " + new Date().toString() + " from "
								+ request.getRemoteAddr());
						errors.add(new LabelValueBean("errCredentials", getText("logon.err.password.mismatch", locale)));
					}
				} else {
					ACCESSLOGGER.warn("LOGON: No such user: " + username + " at " + new Date().toString() + " from " + request.getRemoteAddr());
					errors.add(new LabelValueBean("errCredentials", getText("logon.err.password.mismatch", locale)));
					LOGGER.debug("User '" + username + "' is not in database...");
				}
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
				LOGGER.error("Could not initialize Torque (2)");
				LOGGER.error(ExceptionUtils.getStackTrace(e));
				errors.add(new LabelValueBean("errGeneralError", getText("logon.err.noDataBase", locale)));
			}
		}

		if (errors.size() > 0 || personBean == null) {
			mappingEnum = 2;
			result.put("errors", errors);
			result.put("mappingEnum", mappingEnum);
			return result;
		}

		// At this point, we have successfully identified the user.
		// Try to set the users preferred locale
		if (personBean.getPrefLocale() != null && !"".equals(personBean.getPrefLocale())) {
			// get as stored in user profile
			locale = LocaleHandler.getExistingLocale(LocaleHandler.getLocaleFromString(personBean.getPrefLocale()));
		}
		if (locale == null) {
			// rely on browser settings
			locale = LocaleHandler.getExistingLocale(request.getLocales());
		}
		personBean.setLocale(locale);

		// set the bean with the last saved login date and save the actual date
		// as
		// last login date in the database
		personBean.setLastButOneLogin(personBean.getLastLogin());
		personBean.setLastLogin(new Date());
		PersonBL.saveSimple(personBean);
		LocaleHandler.exportLocaleToSession(sessionMap, locale);

		// -----------------------------------------------------

		// check if opState
		// (reject users, but not admin, in maintenance state)
		ApplicationBean appBean = ApplicationBean.getInstance();

		if (appBean == null) {
			LOGGER.error("appBean == null: this should never happen");
			mappingEnum = 3;
			result.put("errors", errors);
			result.put("mappingEnum", mappingEnum);
			return result;
		}

		httpSession.setAttribute(Constants.APPLICATION_BEAN, appBean);

		TSiteBean siteBean = DAOFactory.getFactory().getSiteDAO().load1();

		if (ApplicationBean.OPSTATE_MAINTENNANCE.equals(siteBean.getOpState()) && !personBean.getIsSysAdmin()) {
			// print error, refuse login
			errors.add(new LabelValueBean("errGeneralError", getText("logon.err.maintenance", locale)));
			mappingEnum = 4;
			result.put("errors", errors);
			result.put("mappingEnum", mappingEnum);
			return result;
		}

		Runtime rt = Runtime.getRuntime();
		long mbyte = 1024 * 1024;
		long freeMemoryMB = rt.freeMemory() / mbyte;
		if (freeMemoryMB < 50 && !personBean.getIsSysAdmin()) {
			rt.gc();
			freeMemoryMB = rt.freeMemory() / mbyte;
			if (freeMemoryMB < 50) {
				errors.add(new LabelValueBean("errGeneralError", getText("logon.err.freeMemory", locale)));
				mappingEnum = 19;
				result.put("errors", errors);
				result.put("mappingEnum", mappingEnum);
				return result;
			}
		}

		// Save our logged-in user in the session
		// and set a cookie so she can conveniently point
		// directly to issues without having to log on for
		// the next CookieTimeout seconds

		httpSession.setAttribute(Constants.USER_KEY, personBean);

		int maxItemsProUser = GeneralSettings.getMaxItems();
		FilterUpperTO filterUpperTO = new FilterUpperTO();
		TreeFilterExecuterFacade.prepareFilterUpperTO(filterUpperTO, personBean, locale, null, null);
		int noOfProjectRoleItemsProUser = LoadTreeFilterItemCounts.countTreeFilterProjectRoleItems(filterUpperTO, personBean, locale, maxItemsProUser);
		int noOfRACIRoleItemsProUser = LoadTreeFilterItemCounts.countTreeFilterRACIRoleItems(filterUpperTO, personBean, locale, maxItemsProUser);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Maximum number of items per user " + maxItemsProUser);
			LOGGER.debug("Number of project role items accessible by " + username + ": " + noOfProjectRoleItemsProUser);
			LOGGER.debug("Number of RACI role items accessible by " + username + ": " + noOfRACIRoleItemsProUser);
		}
		boolean projectRoleItemsAboveLimit = noOfProjectRoleItemsProUser >= maxItemsProUser;
		boolean raciRoleItemsAboveLimit = noOfRACIRoleItemsProUser >= maxItemsProUser;
		personBean.setProjectRoleItemsAboveLimit(Boolean.valueOf(projectRoleItemsAboveLimit));
		personBean.setRaciRoleItemsAboveLimit(Boolean.valueOf(raciRoleItemsAboveLimit));
		PersonBL.setLicensedFeatures(personBean);

		List<TListTypeBean> issueTypes = IssueTypeBL.loadAllByPerson(personBean.getObjectID(), locale);
		httpSession.setAttribute("issueTypesJSON", JSONUtility.encodeIssueTypes(issueTypes));
		Integer sessionTimeoutMinutes = personBean.getSessionTimeoutMinutes();
		if (sessionTimeoutMinutes != null && sessionTimeoutMinutes.intValue() != 0) {
			httpSession.setMaxInactiveInterval(sessionTimeoutMinutes * 60);
		}
		// load the my filters in the menu
		List<FilterInMenuTO> myFilters = FilterBL.loadMyMenuFiltersWithTooltip(personBean, locale);

		httpSession.setAttribute(FilterBL.MY_MENU_FILTERS_JSON, FilterInMenuJSON.encodeFiltersInMenu(myFilters));

		List<FilterInMenuTO> lastQueries = FilterInMenuBL.getLastExecutedQueries(personBean, locale);

		httpSession.setAttribute(FilterBL.LAST_EXECUTED_FILTERS_JSON, FilterInMenuJSON.encodeFiltersInMenu(lastQueries));
		httpSession.setAttribute(ShortcutBL.SHORTCUTS_JSON, ShortcutBL.encodeShortcutsJSON());

		// modules
		List modules = getModuleDescriptors(personBean);
		httpSession.setAttribute("usedModules", modules);
		httpSession.setAttribute("usedModulesJSON", MasterHomeJSON.encodeModules(modules, personBean));
		httpSession.setAttribute("loggedInPersonUserLevel", personBean.getUserLevel());
		httpSession.setAttribute("clientUserLevelID", TPersonBean.USERLEVEL.CLIENT);

		// maxFileSize
		int maxFileSize = AttachBL.getMaxFileSize(siteBean);
		httpSession.setAttribute("MAXFILESIZE", maxFileSize);

		// ------------------------------------------------------
		// Create a new SessionBean for this session and bind it to the session

		SessionBean sBean = new SessionBean();
		httpSession.setAttribute(Constants.SESSION_BEAN, sBean);

		ItemLockBL.removeLockedIssuesByUser(personBean.getObjectID());

		ACCESSLOGGER.info("LOGON: User '" + personBean.getLoginName().trim() + "' (" + personBean.getFullName() + ")" + " logged in at "
				+ new Date().toString() + " from " + request.getRemoteAddr());

		LicenseManager lm = appBean.getLicenseManager();
		if (lm != null) {
			int rf = lm.getErrorCode();
			boolean haveLicenseErrors = false;
			switch (rf) {
			case 1:
				haveLicenseErrors = true;
				errors.add(new LabelValueBean("errLicenseError", getText("logon.err.license.needCommercial", locale)));
				break;
			case 2:
				haveLicenseErrors = true;
				errors.add(new LabelValueBean("errLicenseError", getText("logon.err.license.expired", locale)));
				break;
			case 3:
				haveLicenseErrors = true;
				errors.add(new LabelValueBean("errLicenseError", getText("logon.err.license.full.exceeded", locale)));
				break;
			case 4:
				haveLicenseErrors = true;
				errors.add(new LabelValueBean("errLicenseError", getText("logon.err.license.invalid", new String[] { ApplicationBean.getIpNumbersString() },
						locale)));
				break;
			case 7:
				haveLicenseErrors = true;
				errors.add(new LabelValueBean("errLicenseError", getText("logon.err.license.limited.exceeded", locale)));
				break;
			case 8:
				haveLicenseErrors = true;
				errors.add(new LabelValueBean("errLicenseError", getText("logon.err.license.gantt.exceeded", locale)));
				break;
			default:
				break;
			}

			if (haveLicenseErrors == true) {
				mappingEnum = 6;
				result.put("errors", errors);
				result.put("mappingEnum", mappingEnum);
				return result;
			}
		}

		result.put("errors", errors);

		httpSession.setAttribute("DESIGNPATH", personBean.getDesignPath());

		Boolean isMobileDevice = LogoffBL.isThisAMobileDevice(request);
		httpSession.setAttribute("mobile", isMobileDevice);

		LOGGER.debug("Mobile is " + httpSession.getAttribute("mobile"));

		// check for post-login forward
		String forwardUrl = (String) httpSession.getAttribute(Constants.POSTLOGINFORWARD);
		if (forwardUrl != null) {
			LOGGER.debug("Forward URL found :" + forwardUrl);
			mappingEnum = 7;
			result.put("mappingEnum", mappingEnum);
			return result;

		}

		Map ret = new GroovyScriptExecuter().handleEvent(IEventSubscriber.EVENT_POST_USER_LOGGED_IN, new HashMap());
		if (ret.get(BINDING_PARAMS.CONTINUE).equals(Boolean.FALSE)) {
			mappingEnum = 10;
			result.put("mappingEnum", mappingEnum);
			return result;
		}

		String extendedKey = ApplicationBean.getInstance().getExtendedKey();

		if (extendedKey == null || extendedKey.length() < 10) { // no empty keys
																// allowed
			mappingEnum = 18;
			result.put("mappingEnum", mappingEnum);
			return result;

		}

		String firstTime = (String) servletContext.getAttribute("FirstTime");

		result.put("user", personBean);

		if (personBean.getIsSysAdmin() && firstTime != null && firstTime.equals("FT")) {

			servletContext.removeAttribute("FirstTime");
			mappingEnum = 8;
			result.put("mappingEnum", mappingEnum);
			return result;

		} else {
			// Forward control to the specified success URI
			mappingEnum = 9;
			result.put("mappingEnum", mappingEnum);
			return result;
		}
	}

	private static List getModuleDescriptors(TPersonBean personBean) {
		// modules
		List modules = PluginManager.getInstance().getModuleDescriptors();

		// exclude wikimodule if there is no license
		Map<String, Boolean> licensedFearturesMap = personBean.getLicensedFeaturesMap();
		String licenseWiki = "wiki";
		Boolean haveWikiLicense = licensedFearturesMap.get(licenseWiki);
		String licenseAlm = "alm";
		Boolean haveAlmLicense = licensedFearturesMap.get(licenseAlm);
		if (haveAlmLicense != null && haveAlmLicense.booleanValue()) {
			// if a user have alm license then the wiki shouuld be enabled aso
			haveWikiLicense = true;
		}
		if (haveWikiLicense == null || haveWikiLicense.booleanValue() == false) {
			// no license for wiki, remove license module
			if (modules != null && !modules.isEmpty()) {
				ModuleDescriptor moduleDescriptor;
				for (int i = 0; i < modules.size(); i++) {
					moduleDescriptor = (ModuleDescriptor) modules.get(i);
					if (moduleDescriptor.getId().equals(ModuleDescriptor.WIKI_MODULE)) {
						modules.remove(i);
						break;
					}
				}
			}
		}
		return modules;
	}

	/**
	 * Some simple XOR decryption algorithm
	 *
	 * @param key
	 *            a the for decryption
	 * @param ciphertext
	 *            hexadecimal number of password, each character encoded in four
	 *            digits
	 * @return the decrypted password
	 */
	public static String decrypt(int key, String ciphertext) {

		StringBuffer plaintext = new StringBuffer();
		String s;
		int ccode;

		for (int i = 0; i < ciphertext.length() / 4; ++i) {
			s = ciphertext.substring(4 * i, 4 * i + 4);
			try {
				ccode = Integer.valueOf(s, 16).intValue();
				ccode = ccode ^ key;
				plaintext.append((char) ccode);
			} catch (Exception e) {
				plaintext = new StringBuffer("xxx");
			}
		}

		return plaintext.toString();
	}

	private static String getText(String key, Locale locale) {
		return LocalizeUtil.getLocalizedTextFromApplicationResources(key, locale);
	}

	private static String getText(String key, String param, Locale locale) {
		Object[] params = { param };
		return LocalizeUtil.getParametrizedString(key, params, locale);
	}

	private static String getText(String key, String[] param, Locale locale) {
		return LocalizeUtil.getParametrizedString(key, param, locale);
	}

	/**
	 * Create a JSON string as a response to a login attempt or calling the login page.
	 * @param username
	 * @param password
	 * @param nonce
	 * @param usingContainerBasedAuthentication
	 * @param springAuthenticated
	 * @param request
	 * @param errors
	 * @param httpSession
	 * @param forwardUrl
	 * @param motd
	 * @param isMobileApplication
	 * @param locale
	 * @param mobileApplicationVersionNo
	 * @param redirectMapEntry
	 * @return the JSON response
	 */
	public static StringBuilder createLoginResponseJSON(String _username,
			                                            String password,
			                                            String nonce,
			                                            boolean usingContainerBasedAuthentication,
			                                            boolean springAuthenticated,
			                                            HttpServletRequest request,
			                                            List<LabelValueBean> errors,
			                                            HttpSession httpSession,
			                                            String forwardUrl,
			                                            TMotdBean motd,
			                                            boolean isMobileApplication,
			                                            Locale locale,
			                                            Integer mobileApplicationVersionNo,
			                                            String redirectMapEntry) {

		StringBuilder sb = new StringBuilder();
		sb.append("{");
		Integer mappingEnum = 0;

		Map envResult = LoginBL.setEnvironment(_username, password, nonce, request, ActionContext.getContext().getSession(), false,
				usingContainerBasedAuthentication, springAuthenticated);

		mappingEnum = (Integer) envResult.get("mappingEnum");
		errors = (List<LabelValueBean>) envResult.get("errors");

		if (errors != null && errors.size() > 0) {
			JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, false);
			sb.append(DATABRACE);
			JSONUtility.appendLabelValueBeanList(sb, "errors", errors);
		} else {
			JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
			sb.append(DATABRACE);
		}

		JSONUtility.appendStringValue(sb, "nonce", nonce);
		JSONUtility.appendStringValue(sb, "j_username", _username);


		BanProcessor bp = BanProcessor.getBanProcessor();
		String ipNumber = request.getRemoteAddr();
		redirectMapEntry = "itemNavigator";
		TPersonBean personBean = (TPersonBean) httpSession.getAttribute(Constants.USER_KEY);
		if (personBean != null && personBean.getHomePage() != null && personBean.getHomePage().trim().length() > 0) {
			redirectMapEntry = personBean.getHomePage();
		}
		String redirectURL = redirectMapEntry + DOTACTION;
		Boolean firstTimeEver = false;

		switch (mappingEnum) {
		case 1:
			redirectURL = "";
			break;
		case 2: // could not authenticate user
			bp.markBadAttempt(ipNumber);
			if (bp.isBanned(ipNumber)) {
				ACCESSLOGGER.warn("LOGON: Access attempt from banned IP " + ipNumber + " at " + new Date().toString());
				JSONUtility.appendBooleanValue(sb, "banned", true);
				redirectMapEntry = "banned";
				redirectURL = redirectMapEntry + DOTACTION;
			}
			break;
		case 3:
		case 4:
			// can't login, maintenance mode
			break;
		case 5:
		case 6:
			// license expired, too many login attempts
			// and not admin user. Admin can always log in
			// even with expired license.
			JSONUtility.appendBooleanValue(sb, "continyou", false);
			break;
		case 7:
			forwardUrl = (String) httpSession.getAttribute(Constants.POSTLOGINFORWARD);
			if (forwardUrl != null) {
				LOGGER.debug("Forward URL found :" + forwardUrl);
				httpSession.removeAttribute(Constants.POSTLOGINFORWARD);
			}
			redirectURL = forwardUrl;
			break;

		case 8:
			// First time and system administrator
			bp.removeBanEntry(ipNumber);
			redirectMapEntry = "admin";
			redirectURL = redirectMapEntry + DOTACTION;
			JSONUtility.appendBooleanValue(sb, "continyou", true);
			break;
		case 9:
			// Everything is fine, we go to the itemNavigator
			bp.removeBanEntry(ipNumber);
			redirectURL = redirectMapEntry + DOTACTION;
			JSONUtility.appendBooleanValue(sb, "continyou", true);
			break;

		case 10:
			// Event handler binding continue ??
			redirectURL = "";
			break;

		case 18:
			// First login ever
			bp.removeBanEntry(ipNumber);
			firstTimeEver = true;
			if (ApplicationBean.getInstance().getLicenseManager() != null) {
				firstTimeEver = false;
			}
			redirectMapEntry = "admin";
			redirectURL = redirectMapEntry + DOTACTION;
			JSONUtility.appendBooleanValue(sb, "continyou", true);
			break;
		case 19:
			// out of memory (<50 MB)
			break;
		default:
			break;
		}

		return assembleJSONPart2(sb, locale,
				    			firstTimeEver,
				    			personBean,
				    			httpSession,
				    			redirectURL,
				    			mobileApplicationVersionNo,
				    			motd);
	}

	private static StringBuilder assembleJSONPart2(StringBuilder sb,
												   Locale locale,
												   boolean firstTimeEver,
												   TPersonBean personBean,
												   HttpSession httpSession,
												   String redirectURL,
												   Integer mobileApplicationVersionNo,
												   TMotdBean motd
												   ){
		String licURL = "";
		if (ApplicationBean.getInstance().getLicenseManager() != null) {
			licURL = ApplicationBean.getInstance().getLicenseManager().getLicenseUrl(locale);
		}

		JSONUtility.appendStringValue(sb, "licURL", licURL, false);
		JSONUtility.appendBooleanValue(sb, "ftever", firstTimeEver, false);

		boolean isld = true;

		JSONUtility.appendBooleanValue(sb, "isLicenseDerfined", isld, false);

		JSONUtility.appendStringValue(sb, "jsonURL", redirectURL, false);
		if (httpSession.getAttribute(ISMOBILEAPP) != null) {
			if ((Boolean) httpSession.getAttribute(ISMOBILEAPP)) {
				// This property is added for mobile client,
				if (personBean != null && personBean.getLocale() != null) {
					JSONUtility.appendStringValue(sb, "locale", personBean.getLocale().toString());
					JSONUtility.appendStringValue(sb, "datePattern", getLocaleDatePattern(personBean.getLocale()));
					JSONUtility.appendIntegerValue(sb, "userLevel", personBean.getUserLevel());
					JSONUtility.appendIntegerValue(sb, "sessionTimeoutMinutes", httpSession.getMaxInactiveInterval() / 60);
					JSONUtility.appendJSONValue(sb, "userSettingsProperties", getUserProperties(personBean));
					JSONUtility.appendIntegerValue(sb, "userObjectID", personBean.getObjectID());
					JSONUtility.appendStringValue(sb, "serverVersion", ApplicationBean.getInstance().getVersion());
					JSONUtility.appendIntegerValue(sb, "serverVersionNo", ApplicationBean.getInstance().getVersionNo());
					JSONUtility.appendIntegerValue(sb, "clientCompatibility", MobileBL.checkClientCompatibility(mobileApplicationVersionNo, true));
					JSONUtility.appendStringValue(sb, "sessionId", httpSession.getId());

					Integer iconKey = Integer.valueOf(-1);
					try {
						byte[] oneAvatar = AvatarBL.getAvatarInByteArray(personBean.getObjectID(), iconKey);
						MessageDigest md = MessageDigest.getInstance("MD5");
						byte[] thedigest = md.digest(oneAvatar);
						String checksum = DatatypeConverter.printBase64Binary(thedigest);
						JSONUtility.appendStringValue(sb, "checkSum", checksum);
					} catch (Exception ex) {
					}
				}
			}
		}
		String motdMsg = motd.getTheMessage();
		if (motdMsg == null) {
			motdMsg = "&nbsp;";
		}
		try {
			JSONUtility.appendStringValue(sb, "teaserText", Html2Text.getNewInstance().convert(motd.getTeaserText()));
		} catch (Exception ex) {
		}
		JSONUtility.appendStringValue(sb, "motd", motdMsg, true);

		sb.append("}");
		sb.append("}");
		return sb;
	}


	private static String getUserProperties(TPersonBean personBean) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		Map<Integer, Boolean> userLevelSpecificMap = UserLevelsProxy.getInstance().getMapByUserLevel(personBean.getUserLevel());
		for (Integer propertyKey : userLevelSpecificMap.keySet()) {
			String convertedValue = convertUserPropIDToUserPropString(propertyKey);
			if (convertedValue != null) {
				JSONUtility.appendBooleanValue(sb, convertedValue, userLevelSpecificMap.get(propertyKey));
			}
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("}");
		return sb.toString();
	}

	private static String convertUserPropIDToUserPropString(Integer userPropID) {
		final HashMap<Integer, String> userPropIDToUserPropString = new HashMap<Integer, String>();
		userPropIDToUserPropString.put(Integer.valueOf(314), "HasBaskets");
		userPropIDToUserPropString.put(Integer.valueOf(314), "ItemNavigator.HasFilters.HasBaskets");
		userPropIDToUserPropString.put(Integer.valueOf(313), "ItemNavigator.HasFilters.HasStates");
		userPropIDToUserPropString.put(Integer.valueOf(311), "ItemNavigator.HasFilters.HasWorkspaces");
		userPropIDToUserPropString.put(Integer.valueOf(312), "ItemNavigator.HasFilters.HasFilters");

		userPropIDToUserPropString.put(Integer.valueOf(331), "HasBasket.PlannedItems");
		userPropIDToUserPropString.put(Integer.valueOf(332), "HasBasket.NextActions");
		userPropIDToUserPropString.put(Integer.valueOf(333), "HasBasket.Reminder");
		userPropIDToUserPropString.put(Integer.valueOf(334), "HasBasket.Delegated");
		userPropIDToUserPropString.put(Integer.valueOf(335), "HasBasket.Trash");
		userPropIDToUserPropString.put(Integer.valueOf(336), "HasBasket.Incubator");
		userPropIDToUserPropString.put(Integer.valueOf(337), "HasBasket.Reference");

		if (userPropIDToUserPropString.containsKey(userPropID)) {
			return userPropIDToUserPropString.get(userPropID);
		}
		return null;

	}

	private static String getLocaleDatePattern(Locale locale) {
		DateFormat guiDateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);
		// GUI calendar date format: 4 digits year
		String s = ((SimpleDateFormat) guiDateFormat).toPattern();
		// be sure the format is short
		s = s.replaceAll("dd", "d");
		s = s.replaceAll("MM", "M");
		s = s.replaceAll("YYYY", "YY");
		s = s.replaceAll("yyyy", "yy");
		// replace the format to be dd.MM.yyyy
		s = s.replaceAll("d", "dd");
		s = s.replaceAll("M", "MM");
		s = s.replaceAll("YY", "YYYY");
		s = s.replaceAll("yy", "yyyy");// most imoportant: 4 digits year
		return s;
	}
}
