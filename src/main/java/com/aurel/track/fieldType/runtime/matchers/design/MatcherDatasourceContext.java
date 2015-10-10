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

package com.aurel.track.fieldType.runtime.matchers.design;

import java.util.Locale;

import com.aurel.track.beans.TPersonBean;

/**
 * The context by getting the datasource for a matcher
 * @author Tamas
 *
 */
public class MatcherDatasourceContext {
	//the selected projectIDs
	private Integer[] projectIDs;
	//the ancestors of the selected projects: for loading role based lists (like the content of the RACI lists) investigate the roles in the ancestor projects also
	private Integer[] ancestorProjectIDs;
	//the selected itemTypeIDs
	private Integer[] itemTypeIDs;
	//the logged in person
	private TPersonBean personBean;
	//the actual locale
	private Locale locale;
	//whether to add the $Parameter to the list or not
	private boolean withParameter;
	//whether to initialize the value if nothing is selected
	//typically true for field expression (rendered as single select combo) value but false for upper select (rendered as multiple select)
	private boolean initValueIfNull;
	//whether to show closed entities: used for release.
	//Later it may also be used for projects, persons, states, deleted custom options etc.
	private boolean showClosed;
	//whether it is the first load of a filter (first instant filter or creating a new filter)
	//Meaningful for upper selects but not for field expressions
	private boolean firstLoad;
	
	
	public MatcherDatasourceContext() {
		super();
	}
	
	public MatcherDatasourceContext(Integer[] projectIDs,
			Integer[] ancestorProjectIDs, Integer[] itemTypeIDs,
			TPersonBean personBean, Locale locale, boolean withParameter,
			boolean initValueIfNull, boolean showClosed, boolean firstLoad) {
		super();
		this.projectIDs = projectIDs;
		this.ancestorProjectIDs = ancestorProjectIDs;
		this.itemTypeIDs = itemTypeIDs;
		this.personBean = personBean;
		this.locale = locale;
		this.withParameter = withParameter;
		this.initValueIfNull = initValueIfNull;
		this.showClosed = showClosed;
		this.firstLoad = firstLoad;
	}
	public Integer[] getProjectIDs() {
		return projectIDs;
	}
	public void setProjectIDs(Integer[] projectIDs) {
		this.projectIDs = projectIDs;
	}
	public Integer[] getAncestorProjectIDs() {
		return ancestorProjectIDs;
	}
	public void setAncestorProjectIDs(Integer[] ancestorProjectIDs) {
		this.ancestorProjectIDs = ancestorProjectIDs;
	}
	public Integer[] getItemTypeIDs() {
		return itemTypeIDs;
	}
	public void setItemTypeIDs(Integer[] itemTypeIDs) {
		this.itemTypeIDs = itemTypeIDs;
	}
	public TPersonBean getPersonBean() {
		return personBean;
	}
	public void setPersonBean(TPersonBean personBean) {
		this.personBean = personBean;
	}
	public Locale getLocale() {
		return locale;
	}
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	public boolean isWithParameter() {
		return withParameter;
	}
	public void setWithParameter(boolean withParameter) {
		this.withParameter = withParameter;
	}
	public boolean isInitValueIfNull() {
		return initValueIfNull;
	}
	public void setInitValueIfNull(boolean initValueIfNull) {
		this.initValueIfNull = initValueIfNull;
	}
	public boolean isShowClosed() {
		return showClosed;
	}
	public void setShowClosed(boolean showClosed) {
		this.showClosed = showClosed;
	}
	public boolean isFirstLoad() {
		return firstLoad;
	}
	public void setFirstLoad(boolean firstLoad) {
		this.firstLoad = firstLoad;
	}
	
	
}
