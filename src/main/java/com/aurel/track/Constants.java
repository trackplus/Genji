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

import groovy.lang.GroovyClassLoader;
import groovy.util.GroovyScriptEngine;

import java.io.File;
import java.net.URL;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aurel.track.dbase.HandleHome;
import com.aurel.track.prop.ApplicationBean;


/**
 * Class <b>Constants</b> defines some constants being used throughout the
 * application and particularly in the JSP code.<br>
 * @author Joerg Friedrich <joerg.friedrich@computer.org>
 * @version $Revision: 1903 $ $Date: 2015-12-18 17:37:00 +0100 (Fr, 18 Dez 2015) $
 */
public final class Constants {
	private static final Logger LOGGER = LogManager.getLogger(Constants.class);

	private Constants() {

	}
    /**
     * <b>DATABASE_KEY</b>: the application scope attribute under
     * which the database controller (datasource) is stored.
     */
    public static final String DATABASE_KEY = "database";

    /**
     * <b>USER_KEY</b>: the session scope attribute under which the User object
     * for the currently logged in user is stored.
     */
    public static final String USER_KEY = "user";

    /**
     * The key for locale stored in session (struts2 needs this hardcoded key)
     */
    public static final String LOCALE_KEY="WW_TRANS_I18N_LOCALE";

	/**
     * The application bean contains all information that needs
     * to be kept across the lifetime of the application, e.g. the
	 * number of currently logged on users, the uptime, etc.
     */
	public static final String APPLICATION_BEAN = "APPLICATION_BEAN";
    public static final String SESSION_BEAN = "SESSION_BEAN";

	/// name of the forward URL that might be used after login
	public static final String POSTLOGINFORWARD = "POSTLOGINFORWARD";

	public static final String DESIGN_DIRECTORY = "design";
	public static final String DEFAULTDESIGNPATH  = "silver";//"classic"

	public static final int MAX_ISSUE_NUMBER_GC = 10000;

	public static String Hyperlink = null;

	public static String BaseURL = null;

	public static void setHyperlink(String baseurl) {
		BaseURL = baseurl;
		Hyperlink = baseurl + "/printItem.action?key=";
	}

	/*
	 * This returns the complete URL for this instance, including context.
	 * It takes into account the manual setting of the URL in the server
	 * configuration page. This is helpful if the server is operated behind
	 * a proxy.
	 *
	 * Example: https://www.trackplus.com/track-demo
	 */
	public static String getBaseURL() {
		return BaseURL;
	}

    public static String getHyperLink() {
        return Hyperlink;
    }

	public static int CookieTimeout = 7*24*60*60; // Seconds

	public static void setCookieTimeout(int timeout) {
		CookieTimeout = timeout;
	}


	public static String RealPath = null;

	public static void setRealPath(String path) {
		RealPath = path;
	}

	public static int MAXFILESIZE = 4 * 1024* 1024;

	public static void setMAXFILESIZE(int fsize) {
		MAXFILESIZE = fsize;
	}


	public static final String LdapUserPwd = "_LdapUser_";

	private static boolean parameterizedQuery = true;

	public static boolean isParameterizedQuery() {
		return parameterizedQuery;
	}

	public static void setParameterizedQuery(boolean parameterizedQuery) {
		Constants.parameterizedQuery = parameterizedQuery;
	}


	public static Integer DASHBOARDBARLENGTH = new Integer(60);

	private static GroovyScriptEngine groovyScriptEngine = null;

	public static GroovyScriptEngine getGroovyScriptEngine() {
		return groovyScriptEngine;
	}

	private static URL groovyURL = null;

	public static void setGroovyURL(URL _groovyURL) {
		groovyURL = _groovyURL;
	}

	public static URL getGroovyURL() {
		return groovyURL;
	}

	public static void setGroovyScriptEngine() {
        // Create a plugin directory for Groovy scripts if it does not exist
        // already anyways.
        File groovyPluginDir = new File(HandleHome.getGroovyPluginPath());

        if (!groovyPluginDir.exists() || !groovyPluginDir.isDirectory()) {
        	LOGGER.info("Creating " + HandleHome.getGroovyPluginPath());
        	if (HandleHome.getGroovyPluginPath().length() > 1) {
        		if (!groovyPluginDir.mkdirs()) {
        			System.err.println("Create failed.");
        		}
        	}
        }

        try {
        	URL extern = groovyPluginDir.toURL();
        	ClassLoader parent = ApplicationBean.getInstance().getClass().getClassLoader();
        	GroovyClassLoader loader = new GroovyClassLoader(parent);

        	// external plugins have precedence; they can cover internal
        	// plugins with the same name!
        	URL[] roots = new URL[] {extern/*,
                                     groovyURL*/
        	                        };
        	groovyScriptEngine = null;
        	groovyScriptEngine = new GroovyScriptEngine(roots,loader);
        }
        catch (Exception ioe) {
        	System.err.println("Cannot initialize Groovy script engine");
        	LOGGER.error(ExceptionUtils.getStackTrace(ioe));
        }
	}



}
