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

package com.aurel.track.teamgeist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.StrutsTestCase;
import org.junit.Test;

import com.opensymphony.xwork2.ActionProxy;

public class TeamgeistServicesTest extends StrutsTestCase {	
	
	private static final Logger LOGGER = LogManager.getLogger(TeamgeistServicesTest.class);
	
	/**
	 * This method iterate through the Set which contains all action names used by Teamgeist.
	 * If an action missing the test fails.
	 */
	@Test
	public void testServices() {
		LinkedHashSet<String>actionNamesSet = getActinNames();
		if(actionNamesSet != null && actionNamesSet.size() > 0) {
			for (String actionName : actionNamesSet) {
				try {
					ActionProxy actionProxy = getActionProxy("/" + actionName);
					assertNotNull(actionProxy);
					Class<?> actionClass = Class.forName(actionProxy.getAction().getClass().getName());									
					actionClass.getMethod(actionProxy.getMethod());
				}catch(NoSuchMethodException noMethodEX) {
					fail("No such method exception at: " + actionName);
					System.out.println("No such method: " + actionName);
				}
				catch(ClassNotFoundException noClassEx) {
					fail("Class not found exception at: " + actionName);
					System.out.println("No such class: " + actionName);
				}catch(NullPointerException nullEx) {
					fail("Null pointer exception at: " + actionName);
					System.out.println("Null pointer at: " + actionName);
				}
			}
			System.out.println("Number of actions: " + actionNamesSet.size() + ". " + "The test was executed successfully!");
		}else {
			fail("Error while parsing Services file!");
		}
	}
	
	/**
	 * This method returns Teamgeist source path till Services.cpp.
	 * The source base directory is provided by: buildwin or buildux properties (Windows or mac)
	 * @return
	 */
	private String getSrcPath() {
		String propertiesFileName = null;
		if(System.getProperty("os.name").toLowerCase().contains("windows")) {
			propertiesFileName = "buildwin.properties";
		}else if(System.getProperty("os.name").toLowerCase().contains("mac") ||
				System.getProperty("os.name").toLowerCase().contains("linux")) {
			propertiesFileName = "buildux.properties";
		}else {
			return null;
		}		
		final String COM_TRACKPLUS_RELATIVE_PATH = "/com.trackplus/" + propertiesFileName;		
		File file = new java.io.File(""); 
		String propertiesPath = file.getAbsoluteFile().getParentFile().getAbsolutePath();		
		String srcPath = null;
		if(propertiesPath.contains("\\")) {
			propertiesPath = propertiesPath.replace("\\", "/");
			if(propertiesPath.lastIndexOf("/") == propertiesPath.length() - 1) {
				propertiesPath = propertiesPath.substring(0, propertiesPath.length() - 1);
			}
		}
		propertiesPath += COM_TRACKPLUS_RELATIVE_PATH;
		try {
			final String RELATIVE_PART_OF_PATH = "/services/Services.cpp";
			System.err.println(propertiesPath);
			InputStream input = new FileInputStream(propertiesPath);
			Properties prop = new Properties();
			prop.load(input);
			srcPath = prop.get("teamgeist.srcAbsolutePath").toString();
			srcPath = srcPath.replace("\\", "/");
			if(srcPath.lastIndexOf("/") == srcPath.length() - 1) {
				srcPath = srcPath.substring(0, srcPath.length() - 1);
			}
			srcPath += RELATIVE_PART_OF_PATH;
		}catch(Exception ex) {
			LOGGER.error(ExceptionUtils.getStackTrace(ex));
		}
		System.out.println("Using Teamgeist source from " + srcPath);
		return srcPath;
	}
	
	/**
	 * This method returns a Set containing all action names used by Teamgeist.
	 * This actions are in: TEAMGEIST_SRC_DIR/services/Services.cpp
	 * @return
	 */
	LinkedHashSet<String> getActinNames() {		
		String srcPath = getSrcPath();
		File file = new File(srcPath);		
		LinkedHashSet<String>actionNameSet = null;
		try {
			if(file.exists() && !file.isDirectory()) {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line;
				Pattern pattern = Pattern.compile(".*[\"]{1}((.*[.]{1}action)[?]?.*)\".*");
				Matcher matcher;
				while ((line = br.readLine()) != null) {
					if(line.contains(".action")) {		
						if(actionNameSet == null) {
							actionNameSet = new LinkedHashSet<String>();
						}
						matcher = pattern.matcher(line);
						if(matcher.find()) {
							String actionName = matcher.group(2);								
							actionNameSet.add(actionName);
						}
					}
				}
				br.close();
			}
		}catch(IOException ioEx) {
			ioEx.printStackTrace();
		}
		return actionNameSet;
	}
	
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(TeamgeistServicesTest.class);
	}
}
