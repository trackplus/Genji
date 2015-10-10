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


package com.aurel.track.fieldType.runtime.base;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TWorkItemBean;

public class SelectContext {
	private Integer personID;
	private Integer fieldID;
	private Integer parameterCode;
	//private Integer configID;
	private TFieldConfigBean fieldConfigBean;
	private Object fieldSettings;
	private TWorkItemBean workItemBean;
	private Locale locale;
	private boolean create;
	private boolean dependencyRefresh;
	private boolean view;
	//cache for datasources in context
	private Map<Integer, Map<Integer, Map<Integer, List<ILabelBean>>>> datasourceCacheForNotSurelyAllowed;
	private Map<Integer, Map<Integer, Map<Integer, String>>> fieldLabelCacheForNotSurelyAllowed;
	
	public void addDatasourceCache(Integer fieldID, Integer projectID, Integer issueTypeID, List<ILabelBean> dataSource) {
		if (datasourceCacheForNotSurelyAllowed==null) {
			datasourceCacheForNotSurelyAllowed = new HashMap<Integer, Map<Integer,Map<Integer,List<ILabelBean>>>>();
		}
		Map<Integer, Map<Integer,List<ILabelBean>>> fieldDatasources = datasourceCacheForNotSurelyAllowed.get(fieldID);
		if (fieldDatasources==null) {
			fieldDatasources = new HashMap<Integer, Map<Integer,List<ILabelBean>>>();
			datasourceCacheForNotSurelyAllowed.put(fieldID, fieldDatasources);
		}
		Map<Integer,List<ILabelBean>> projectDataSource = fieldDatasources.get(projectID);
		if (projectDataSource==null) {
			projectDataSource = new HashMap<Integer, List<ILabelBean>>();
			fieldDatasources.put(projectID, projectDataSource);
		}
		projectDataSource.put(issueTypeID, dataSource);
	}
	
	public List<ILabelBean> getDatasourceCache(Integer fieldID, Integer projectID, Integer issueTypeID) {
		if (datasourceCacheForNotSurelyAllowed==null) {
			return null;
		}
		Map<Integer, Map<Integer,List<ILabelBean>>> fieldDatasources = datasourceCacheForNotSurelyAllowed.get(fieldID);
		if (fieldDatasources==null) {
			return null;
		}
		Map<Integer,List<ILabelBean>> projectDataSource = fieldDatasources.get(projectID);
		if (projectDataSource==null) {
			return null;
		}
		return projectDataSource.get(issueTypeID);
	}
		
	public void addFieldLabelCache(Integer fieldID, Integer projectID, Integer issueTypeID, String fieldLabel) {
		if (fieldLabelCacheForNotSurelyAllowed==null) {
			fieldLabelCacheForNotSurelyAllowed = new HashMap<Integer, Map<Integer, Map<Integer, String>>>();
		}
		Map<Integer, Map<Integer, String>> fieldDatasources = fieldLabelCacheForNotSurelyAllowed.get(fieldID);
		if (fieldDatasources==null) {
			fieldDatasources = new HashMap<Integer, Map<Integer, String>>();
			fieldLabelCacheForNotSurelyAllowed.put(fieldID, fieldDatasources);
		}
		Map<Integer, String> projectDataSource = fieldDatasources.get(projectID);
		if (projectDataSource==null) {
			projectDataSource = new HashMap<Integer, String>();
			fieldDatasources.put(projectID, projectDataSource);
		}
		projectDataSource.put(issueTypeID, fieldLabel);
	}
	
	public String getFieldLabelCache(Integer fieldID, Integer projectID, Integer issueTypeID) {
		if (fieldLabelCacheForNotSurelyAllowed==null) {
			return null;
		}
		Map<Integer, Map<Integer, String>> fieldDatasources = fieldLabelCacheForNotSurelyAllowed.get(fieldID);
		if (fieldDatasources==null) {
			return null;
		}
		Map<Integer, String> projectDataSource = fieldDatasources.get(projectID);
		if (projectDataSource==null) {
			return null;
		}
		return projectDataSource.get(issueTypeID);
	}
	
	public Integer getFieldID() {
		return fieldID;
	}
	public void setFieldID(Integer fieldID) {
		this.fieldID = fieldID;
	}
	public Integer getParameterCode() {
		return parameterCode;
	}
	public void setParameterCode(Integer parameterCode) {
		this.parameterCode = parameterCode;
	}
	public Integer getPersonID() {
		return personID;
	}
	public void setPersonID(Integer personID) {
		this.personID = personID;
	}
	public TWorkItemBean getWorkItemBean() {
		return workItemBean;
	}
	public void setWorkItemBean(TWorkItemBean workItemBean) {
		this.workItemBean = workItemBean;
	}
	public Locale getLocale() {
		return locale;
	}
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	/**
	 * @return the create
	 */
	public boolean isCreate() {
		return create;
	}
	/**
	 * @param create the create to set
	 */
	public void setCreate(boolean create) {
		this.create = create;
	}
	/**
	 * @return the fieldConfigBean
	 */
	public TFieldConfigBean getFieldConfigBean() {
		return fieldConfigBean;
	}
	/**
	 * @param fieldConfigBean the fieldConfigBean to set
	 */
	public void setFieldConfigBean(TFieldConfigBean fieldConfigBean) {
		this.fieldConfigBean = fieldConfigBean;
	}
	public Object getFieldSettings() {
		return fieldSettings;
	}
	public void setFieldSettings(Object fieldSettings) {
		this.fieldSettings = fieldSettings;
	}
	public boolean isView() {
		return view;
	}
	public void setView(boolean print) {
		this.view = print;
	}

	public boolean isDependencyRefresh() {
		return dependencyRefresh;
	}

	public void setDependencyRefresh(boolean dependencyRefresh) {
		this.dependencyRefresh = dependencyRefresh;
	}
	
	
}
