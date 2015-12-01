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

import java.util.List;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TSystemStateBean;

/**
 * Transfer object used for project base fields
 * @author Tamas Ruff
 *
 */
public class ProjectBaseTO {
	private String label;
	private String description;
	private Integer projectTypeID;
	private List<ILabelBean> projectTypeList;	
	private Integer projectStatusID;
	private List<TSystemStateBean> projectStatusList;
	private boolean linking;
	//whether from project type the project might has releases
	private boolean hasRelease;
	//whether from project the project has releases
	private boolean useRelease;
	private String prefix;
	private boolean hasPrefix;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getProjectTypeID() {
		return projectTypeID;
	}
	public void setProjectTypeID(Integer projectTypeID) {
		this.projectTypeID = projectTypeID;
	}
	public List<ILabelBean> getProjectTypeList() {
		return projectTypeList;
	}
	public void setProjectTypeList(List<ILabelBean> projectTypeList) {
		this.projectTypeList = projectTypeList;
	}
	public Integer getProjectStatusID() {
		return projectStatusID;
	}
	public void setProjectStatusID(Integer projectStatusID) {
		this.projectStatusID = projectStatusID;
	}
	public List<TSystemStateBean> getProjectStatusList() {
		return projectStatusList;
	}
	public void setProjectStatusList(List<TSystemStateBean> projectStatusList) {
		this.projectStatusList = projectStatusList;
	}
	public boolean isLinking() {
		return linking;
	}
	public void setLinking(boolean linking) {
		this.linking = linking;
	}
	public boolean isUseRelease() {
		return useRelease;
	}
	public void setUseRelease(boolean useRelease) {
		this.useRelease = useRelease;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public boolean isHasPrefix() {
		return hasPrefix;
	}
	public void setHasPrefix(boolean hasPrefix) {
		this.hasPrefix = hasPrefix;
	}
	public boolean isHasRelease() {
		return hasRelease;
	}
	public void setHasRelease(boolean hasRelease) {
		this.hasRelease = hasRelease;
	}
}
