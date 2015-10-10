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

package com.aurel.track.item;

import java.util.List;
import java.util.Map;

import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.TreeNode;

public class ItemLocationForm {
	//private List<IntegerStringBean> projectList;
    private List<TreeNode>  projectTree;
	private Integer projectID;
	//private String projectDisplayValue;
	private String projectLabel;
	private String projectTooltip;
	
	private List<IntegerStringBean> issueTypeList;
	private Integer issueTypeID;
	private String issueTypeLabel;
	private String issueTypeTooltip;
	
	private Integer parentID;
	private String synopsis;
	private String description;

	private boolean accessLevelVisible;
	private boolean accessLevelFlag;
	private String accessLevelLabel;
	private String accessLevelTooltip;
	private boolean fixedIssueType;
	
	private Map<String, Object>newlyCreatedLinkSettings;
	

	public Integer getProjectID() {
		return projectID;
	}
	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}
	public Integer getIssueTypeID() {
		return issueTypeID;
	}
	public void setIssueTypeID(Integer issueTypeID) {
		this.issueTypeID = issueTypeID;
	}
	public List<IntegerStringBean> getIssueTypeList() {
		return issueTypeList;
	}
	public void setIssueTypeList(List<IntegerStringBean> issueTypeList) {
		this.issueTypeList = issueTypeList;
	}
	public String getProjectLabel() {
		return projectLabel;
	}
	public void setProjectLabel(String projectLabel) {
		this.projectLabel = projectLabel;
	}
	public String getProjectTooltip() {
		return projectTooltip;
	}
	public void setProjectTooltip(String projectTooltip) {
		this.projectTooltip = projectTooltip;
	}
	public String getIssueTypeLabel() {
		return issueTypeLabel;
	}
	public void setIssueTypeLabel(String issueTypeLabel) {
		this.issueTypeLabel = issueTypeLabel;
	}
	public String getIssueTypeTooltip() {
		return issueTypeTooltip;
	}
	public void setIssueTypeTooltip(String issueTypeTooltip) {
		this.issueTypeTooltip = issueTypeTooltip;
	}
	public Integer getParentID() {
		return parentID;
	}
	public void setParentID(Integer parentID) {
		this.parentID = parentID;
	}
	public boolean isAccessLevelVisible() {
		return accessLevelVisible;
	}
	public void setAccessLevelVisible(boolean accessLevelVisible) {
		this.accessLevelVisible = accessLevelVisible;
	}
	public boolean isAccessLevelFlag() {
		return accessLevelFlag;
	}
	public void setAccessLevelFlag(boolean accessLevelFlag) {
		this.accessLevelFlag = accessLevelFlag;
	}
	public String getAccessLevelLabel() {
		return accessLevelLabel;
	}
	public void setAccessLevelLabel(String accessLevelLabel) {
		this.accessLevelLabel = accessLevelLabel;
	}
	public String getAccessLevelTooltip() {
		return accessLevelTooltip;
	}
	public void setAccessLevelTooltip(String accessLevelTooltip) {
		this.accessLevelTooltip = accessLevelTooltip;
	}

    public List<TreeNode> getProjectTree() {
        return projectTree;
    }

    public void setProjectTree(List<TreeNode> projectTree) {
        this.projectTree = projectTree;
    }

	public boolean isFixedIssueType() {
		return fixedIssueType;
	}

	public void setFixedIssueType(boolean fixedIssueType) {
		this.fixedIssueType = fixedIssueType;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Map<String, Object> getNewlyCreatedLinkSettings() {
		return newlyCreatedLinkSettings;
	}
	public void setNewlyCreatedLinkSettings(
			Map<String, Object> newlyCreatedLinkSettings) {
		this.newlyCreatedLinkSettings = newlyCreatedLinkSettings;
	}
}
