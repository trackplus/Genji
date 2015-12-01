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


package com.aurel.track.vc;

import com.aurel.track.util.LabelValueBean;

import java.util.List;
import java.util.Locale;
import java.util.Map;
/**
 * An plugin class for version control
 * This class verify a map of parameters if are valid for the version control and
 * obtain the logs that have comments with "#<issueNumber"
 * 
 * @author Adrian Bojani
 *
 */
public interface VersionControlPlugin {
	public static final String UNKNOWN_HOST="plugins.versionControl.error.unknownHost";
	public static final String AUTHENTICATION_ERROR="plugins.versionControl.error.authentication";
	public static final String CONNECTION_ERROR="plugins.versionControl.error.connection";
	public static final String GENERAL_ERROR="plugins.versionControl.error.general";
	
	public static final String BROWSER="viewVc.browser";
	
	/**
	* Get an map of revision for each issueNumber
	* @param parameters
	*
	* @return
	*/
	public Map<Integer,List<Revision>> getLogs(Map<String,String> parameters,String prefixIssueNumber);
	
	/**
	 * Verify if the map  of parameters is valid or not
	 * return a list of strings that are key for a localized error
	 * @param parameteres
	 * @return
	 */
	public List<LabelValueBean> verify(Map<String,String> parameteres,Locale locale);
	
	public String getRepository(Map<String,String> parameters);
	
  }
