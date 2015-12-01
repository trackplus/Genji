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

package com.aurel.track.admin.project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TPersonBean;

/**
 * Transfer object used for project specific default values
 * @author Tamas Ruff
 *
 */
public class ProjectDefaultsTO {
	private Integer defaultManagerID;
	private List<TPersonBean> managerList;
	private Integer defaultResponsibleID;
	private List<TPersonBean> responsibleList;
	private Integer initialStatusID;
	private List<ILabelBean> statusList;
	private Integer defaultIssueTypeID;
	private List<ILabelBean> issueTypeList;
	private Integer defaultPriorityID;
	private List<ILabelBean> priorityList;
	private Integer defaultSeverityID;
	private List<ILabelBean> severityList;
	private Integer prefillBy;
	private Map<Integer, Integer> issueTypeToInitStatusMap = new HashMap<Integer, Integer>();
	private Map<Integer, List<ILabelBean>> issueTypeToStatusListMap = new HashMap<Integer, List<ILabelBean>>();
	//extra map for negatively keyed issueTypes (like task, sprint) for avoiding the struts2 bug
	//which does not populate a map from the submitted parameters of the key contains a negative value
	//this map does not penetrates in the business logic level, 
	//the submitted values from the two maps are merged first 
	//the parameters keyed for this map are prepared on the client
	private Map<Integer, Integer> issueTypeNegativeToInitStatusMap = new HashMap<Integer, Integer>();
	 
	public Integer getDefaultManagerID() {
		return defaultManagerID;
	}
	public void setDefaultManagerID(Integer defaultManagerID) {
		this.defaultManagerID = defaultManagerID;
	}
	public List<TPersonBean> getManagerList() {
		return managerList;
	}
	public void setManagerList(List<TPersonBean> managerList) {
		this.managerList = managerList;
	}
	public Integer getDefaultResponsibleID() {
		return defaultResponsibleID;
	}
	public void setDefaultResponsibleID(Integer defaultResponsibleID) {
		this.defaultResponsibleID = defaultResponsibleID;
	}
	public List<TPersonBean> getResponsibleList() {
		return responsibleList;
	}
	public void setResponsibleList(List<TPersonBean> responsibleList) {
		this.responsibleList = responsibleList;
	}
	public Integer getInitialStatusID() {
		return initialStatusID;
	}
	public void setInitialStatusID(Integer initialStatusID) {
		this.initialStatusID = initialStatusID;
	}
	public List<ILabelBean> getStatusList() {
		return statusList;
	}
	public void setStatusList(List<ILabelBean> statusList) {
		this.statusList = statusList;
	}
	public Integer getDefaultIssueTypeID() {
		return defaultIssueTypeID;
	}
	public void setDefaultIssueTypeID(Integer defaultIssueTypeID) {
		this.defaultIssueTypeID = defaultIssueTypeID;
	}
	public List<ILabelBean> getIssueTypeList() {
		return issueTypeList;
	}
	public void setIssueTypeList(List<ILabelBean> issueTypeList) {
		this.issueTypeList = issueTypeList;
	}
	public Integer getDefaultPriorityID() {
		return defaultPriorityID;
	}
	public void setDefaultPriorityID(Integer defaultPriorityID) {
		this.defaultPriorityID = defaultPriorityID;
	}
	public List<ILabelBean> getPriorityList() {
		return priorityList;
	}
	public void setPriorityList(List<ILabelBean> priorityList) {
		this.priorityList = priorityList;
	}
	public Integer getDefaultSeverityID() {
		return defaultSeverityID;
	}
	public void setDefaultSeverityID(Integer defaultSeverityID) {
		this.defaultSeverityID = defaultSeverityID;
	}
	public List<ILabelBean> getSeverityList() {
		return severityList;
	}
	public void setSeverityList(List<ILabelBean> severityList) {
		this.severityList = severityList;
	}
	public Integer getPrefillBy() {
		return prefillBy;
	}
	public void setPrefillBy(Integer prefillBy) {
		this.prefillBy = prefillBy;
	}
	public Map<Integer, Integer> getIssueTypeToInitStatusMap() {
		return issueTypeToInitStatusMap;
	}
	public void setIssueTypeToInitStatusMap(
			Map<Integer, Integer> issueTypeToInitStatusMap) {
		this.issueTypeToInitStatusMap = issueTypeToInitStatusMap;
	}
	public Map<Integer, Integer> getIssueTypeNegativeToInitStatusMap() {
		return issueTypeNegativeToInitStatusMap;
	}
	public void setIssueTypeNegativeToInitStatusMap(
			Map<Integer, Integer> issueTypeNegativeToInitStatusMap) {
		this.issueTypeNegativeToInitStatusMap = issueTypeNegativeToInitStatusMap;
	}
	public Map<Integer, List<ILabelBean>> getIssueTypeToStatusListMap() {
		return issueTypeToStatusListMap;
	}
	public void setIssueTypeToStatusListMap(
			Map<Integer, List<ILabelBean>> issueTypeToStatusListMap) {
		this.issueTypeToStatusListMap = issueTypeToStatusListMap;
	}
	
	
}
