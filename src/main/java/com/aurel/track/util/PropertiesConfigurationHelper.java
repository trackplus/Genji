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

package com.aurel.track.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.dbase.HandleHome;

/**
 * Helper class for loading property files from different paths
 * @author Tamas
 *
 */
public class PropertiesConfigurationHelper {
	private static final Logger LOGGER = LogManager.getLogger(HandleHome.class);
	
	/**
	 * Obtain the Torque.properties from TRACKPLUS_HOME or if not available from the WAR.
	 * @return
	 */
	public static PropertiesConfiguration getPathnamePropFile(String absolutePath, String propFile) {
		PropertiesConfiguration propertiesConfiguration = null;
		File props = null;
		InputStream in = null;
		try	{
			// First check if we have a configuration file pointed to by the environment
			if (absolutePath!= null && !"".equals(absolutePath)) {
				props = new File(absolutePath + File.separator + propFile);
				LOGGER.debug("Trying file " + absolutePath + File.separator + propFile);
				if (props.exists() && props.canRead()) {
					LOGGER.info("Retrieving configuration from " + absolutePath + File.separator + propFile);
					in = new FileInputStream(props);
					propertiesConfiguration = new PropertiesConfiguration();
					propertiesConfiguration.load(in);
					in.close();
				}
			}

		} catch (Exception e) {
			LOGGER.warn("Could not read " + propFile+ " from " + absolutePath + ". Exiting. " + e.getMessage(), e);
		}
		return propertiesConfiguration;
	}
	
	/**
	 * Gets the PropertiesConfiguration for a property file from servlet context
	 * @param servletContext
	 * @param pathWithinContext
	 * @param propFile
	 * @return
	 * @throws ServletException
	 */
	public static PropertiesConfiguration loadServletContextPropFile(ServletContext servletContext, String pathWithinContext, String propFile) throws ServletException {
		PropertiesConfiguration propertiesConfiguration = null;
		InputStream in = null;
		URL propFileURL = null;
		try	{
			if (servletContext!=null && pathWithinContext!=null) {
				if (!pathWithinContext.startsWith("/")) {
					pathWithinContext = "/" + pathWithinContext;
				}
				if (!pathWithinContext.endsWith("/")) {
					pathWithinContext = pathWithinContext + "/";
				}
				propFileURL = servletContext.getResource(pathWithinContext + propFile);
				in = propFileURL.openStream();
				propertiesConfiguration = new PropertiesConfiguration();
				propertiesConfiguration.load(in);
				in.close();
			}
		} catch (Exception e) {
			LOGGER.error("Could not read " + propFile + " from servlet context " + propFileURL==null?"":propFileURL.toExternalForm() + ". Exiting. " + e.getMessage(), e);
			throw new ServletException(e);
		}
		return propertiesConfiguration;
	}
}
