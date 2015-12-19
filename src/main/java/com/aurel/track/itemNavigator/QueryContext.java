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


package com.aurel.track.itemNavigator;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Adrian Bojani
 * */
public class QueryContext implements Serializable{
	private Integer id;
	private Integer queryID;
	private Integer queryType;
	private String queryName;
	private String iconCls;

	private Map<String,String> dashboardParams;

	private String filterExpression;

	public static final long serialVersionUID = 400L;

	public QueryContext(){
	}
	
	public Integer getQueryID() {
		return queryID;
	}

	public void setQueryID(Integer queryID) {
		this.queryID = queryID;
	}

	public Integer getQueryType() {
		return queryType;
	}

	public void setQueryType(Integer queryType) {
		this.queryType = queryType;
	}

	public Map<String, String> getDashboardParams() {
		return dashboardParams;
	}

	public void setDashboardParams(Map<String, String> dashboardParams) {
		this.dashboardParams = dashboardParams;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public String getFilterExpression() {
		return filterExpression;
	}

	public void setFilterExpression(String filterExpression) {
		this.filterExpression = filterExpression;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
}
