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


package com.aurel.track.fieldType.runtime.matchers.run;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.project.ProjectAccountingTO;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.resources.LocalizeUtil;

/**
 * Contains a context with the actual replacement values 
 * for the possible symbolic values for matchers
 * The replacement should take place before the match() method is called 
 * currently the context contains only the actual logged in user
 * and the last logged date of the user
 * but later on also other symbolic constants could be added
 * @author Tamas Ruff
 *
 */
public class MatcherContext {
	//the key in context map for LOGGED_USER_SYMBOLIC
	public static Integer LOGGED_USER = Integer.valueOf(1);	
	public static Integer LOGGED_USER_SYMBOLIC = Integer.valueOf(0);
	public static Integer PARAMETER = Integer.valueOf(-100);
	public static String LOGGED_USER_SHOW_VALUE_KEY = "admin.customize.queryFilter.opt.currentUser";
	public static String PARAMETER_KEY = "admin.customize.queryFilter.opt.parameter";
	//TODO
	//now hardcoded but it can be made symbolic with a negative value but then it should be replaced from the person in the execution time
	public static Integer DAYS_BEFORE = Integer.valueOf(7);
	
	private Map<Integer, Map<Integer, Integer>> contextMap = new HashMap<Integer, Map<Integer, Integer>>();		
	
	/**
	 * The last logged date (before the actual login) 
	 */
	private Date lastLoggedDate;
	
	/**
	 * The locale of the logged user
	 */
	private Locale locale;
	
	/**
	 * the release type selector (scheduled or noticed or both)
	 */
	private Integer releaseTypeSelector;
	
	/**
	 * whether to include the responsibles through group
	 */
	private boolean includeResponsiblesThroughGroup;
	
	/**
	 * projectID to hoursPerWorkDay map
	 */
	private Map<Integer, ProjectAccountingTO> projectAccountingMap;
	
	/**
	 * The actually matched item
	 */
	private TWorkItemBean workItemBean;
	
	/**
	 * @param loggedInUserActual the loggedInUser to set
	 */
	public void setLoggedInUser(Integer loggedInUserActual) {
		Map <Integer,Integer> personMap = contextMap.get(LOGGED_USER); 
		if (personMap==null) {
			personMap = new HashMap <Integer,Integer>();
			personMap.put(LOGGED_USER_SYMBOLIC, loggedInUserActual);
			contextMap.put(LOGGED_USER, personMap);
		} else {
			personMap.put(LOGGED_USER_SYMBOLIC, loggedInUserActual);
		}
	}

	public static String getLocalizedParameter(Locale locale) {
		return LocalizeUtil.getLocalizedTextFromApplicationResources(
				MatcherContext.PARAMETER_KEY, locale);
	}
	
	public static String getLocalizedLoggedInUser(Locale locale) {
		return LocalizeUtil.getLocalizedTextFromApplicationResources(
				MatcherContext.LOGGED_USER_SHOW_VALUE_KEY, locale);
	}
	
	/**
	 * @return the contextMap
	 */
	public Map<Integer, Map<Integer, Integer>> getContextMap() {
		return contextMap;
	}

	/**
	 * @return the lastLoggedDate
	 */
	public Date getLastLoggedDate() {
		return lastLoggedDate;
	}

	/**
	 * @param lastLoggedDate the lastLoggedDate to set
	 */
	public void setLastLoggedDate(Date lastLoggedDate) {
		this.lastLoggedDate = lastLoggedDate;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Integer getReleaseTypeSelector() {
		return releaseTypeSelector;
	}

	public void setReleaseTypeSelector(Integer releaseTypeSelector) {
		this.releaseTypeSelector = releaseTypeSelector;
	}

	public boolean isIncludeResponsiblesThroughGroup() {
		return includeResponsiblesThroughGroup;
	}

	public void setIncludeResponsiblesThroughGroup(
			boolean includeResponsiblesThroughGroup) {
		this.includeResponsiblesThroughGroup = includeResponsiblesThroughGroup;
	}

	public Map<Integer, ProjectAccountingTO> getProjectAccountingMap() {
		return projectAccountingMap;
	}

	public void setProjectAccountingMap(
			Map<Integer, ProjectAccountingTO> projectAccountingMap) {
		this.projectAccountingMap = projectAccountingMap;
	}

	public TWorkItemBean getWorkItemBean() {
		return workItemBean;
	}

	public void setWorkItemBean(TWorkItemBean workItemBean) {
		this.workItemBean = workItemBean;
	}

	
}
