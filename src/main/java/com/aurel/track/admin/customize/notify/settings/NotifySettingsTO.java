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

package com.aurel.track.admin.customize.notify.settings;

public class NotifySettingsTO {	
	private Integer objectID;
	private Integer projectID;
	private String projectLabel;
	private String triggerLabel;
	private String filterLabel;	
	private boolean inherited;
	
	
	public NotifySettingsTO(Integer objectID) {
		super();
		this.objectID = objectID;
	}
	public Integer getObjectID() {
		return objectID;
	}
	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
	}
	public String getProjectLabel() {
		return projectLabel;
	}
	public void setProjectLabel(String projectLabel) {
		this.projectLabel = projectLabel;
	}
	public String getTriggerLabel() {
		return triggerLabel;
	}
	public void setTriggerLabel(String triggerLabel) {
		this.triggerLabel = triggerLabel;
	}
	public String getFilterLabel() {
		return filterLabel;
	}
	public void setFilterLabel(String filterLabel) {
		this.filterLabel = filterLabel;
	}
	public boolean isInherited() {
		return inherited;
	}
	public void setInherited(boolean inherited) {
		this.inherited = inherited;
	}
	public Integer getProjectID() {
		return projectID;
	}
	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}
	
	
}
