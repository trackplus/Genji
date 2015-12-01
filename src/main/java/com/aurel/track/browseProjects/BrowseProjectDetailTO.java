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

package com.aurel.track.browseProjects;


public class BrowseProjectDetailTO {
	
	private Integer projectID;
	private String projectLabel;
	private String projectDescription;
	private String projectType;
	private String projectState;
	private String defaultIssueType;
	private Integer defaultIssueTypeID;
	private boolean projectLinking;
	private boolean projectWorkCost;
	private String defaultManager;
	private String defaultResponsible;
	private String defaultItemState;
	private Integer defaultItemStateID;
	private String defaultPriority;
	private Integer defaultPriorityID;
	private String defaultSeverity;
	private Integer defaultSeverityID;
	private boolean active;

	private boolean canEdit;
	
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	public String getProjectState() {
		return projectState;
	}
	public void setProjectState(String projectState) {
		this.projectState = projectState;
	}
	public String getDefaultIssueType() {
		return defaultIssueType;
	}
	public void setDefaultIssueType(String defaultIssueType) {
		this.defaultIssueType = defaultIssueType;
	}
	
	public Integer getDefaultIssueTypeID() {
		return defaultIssueTypeID;
	}
	public void setDefaultIssueTypeID(Integer defaultIssueTypeID) {
		this.defaultIssueTypeID = defaultIssueTypeID;
	}
	public boolean isProjectLinking() {
		return projectLinking;
	}
	public void setProjectLinking(boolean projectLinking) {
		this.projectLinking = projectLinking;
	}
	public boolean isProjectWorkCost() {
		return projectWorkCost;
	}
	public void setProjectWorkCost(boolean projectWorkCost) {
		this.projectWorkCost = projectWorkCost;
	}
	public String getDefaultManager() {
		return defaultManager;
	}
	public void setDefaultManager(String defaultManager) {
		this.defaultManager = defaultManager;
	}
	public String getDefaultResponsible() {
		return defaultResponsible;
	}
	public void setDefaultResponsible(String defaultResponsible) {
		this.defaultResponsible = defaultResponsible;
	}
	public String getDefaultItemState() {
		return defaultItemState;
	}
	public void setDefaultItemState(String defaultItemState) {
		this.defaultItemState = defaultItemState;
	}
	public String getDefaultPriority() {
		return defaultPriority;
	}
	public void setDefaultPriority(String defaultPriority) {
		this.defaultPriority = defaultPriority;
	}
	public String getDefaultSeverity() {
		return defaultSeverity;
	}
	public void setDefaultSeverity(String defaultSeverity) {
		this.defaultSeverity = defaultSeverity;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public Integer getProjectID() {
		return projectID;
	}
	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}
	public String getProjectLabel() {
		return projectLabel;
	}
	public void setProjectLabel(String projectLabel) {
		this.projectLabel = projectLabel;
	}
	public String getProjectDescription() {
		return projectDescription;
	}
	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	public boolean isCanEdit() {
		return canEdit;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}
	public Integer getDefaultItemStateID() {
		return defaultItemStateID;
	}
	public void setDefaultItemStateID(Integer defaultItemStateID) {
		this.defaultItemStateID = defaultItemStateID;
	}
	public Integer getDefaultPriorityID() {
		return defaultPriorityID;
	}
	public void setDefaultPriorityID(Integer defaultPriorityID) {
		this.defaultPriorityID = defaultPriorityID;
	}
	public Integer getDefaultSeverityID() {
		return defaultSeverityID;
	}
	public void setDefaultSeverityID(Integer defaultSeverityID) {
		this.defaultSeverityID = defaultSeverityID;
	}
}
