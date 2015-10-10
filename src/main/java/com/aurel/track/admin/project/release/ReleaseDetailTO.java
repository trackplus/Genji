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

package com.aurel.track.admin.project.release;

import java.util.Date;
import java.util.List;

import com.aurel.track.beans.TSystemStateBean;

/**
 * Transfer object used for release details
 * @author Tamas Ruff
 *
 */
public class ReleaseDetailTO {
	protected String label;
	protected String description;		
	protected Date dueDate;
	private boolean defaultNoticed;
	private boolean defaultScheduled;	
	private Integer releaseStatusID;
	private List<TSystemStateBean> releaseStatusList;
	//by closing a release having not closed descendants or opening a release having closed ancestors
	//the user should be asked whether to close the descendants respectively open the ancestors 	
	
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
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public boolean isDefaultNoticed() {
		return defaultNoticed;
	}
	public void setDefaultNoticed(boolean defaultNoticed) {
		this.defaultNoticed = defaultNoticed;
	}
	public boolean isDefaultScheduled() {
		return defaultScheduled;
	}
	public void setDefaultScheduled(boolean defaultScheduled) {
		this.defaultScheduled = defaultScheduled;
	}
	public Integer getReleaseStatusID() {
		return releaseStatusID;
	}
	public void setReleaseStatusID(Integer releaseStatusID) {
		this.releaseStatusID = releaseStatusID;
	}
	public List<TSystemStateBean> getReleaseStatusList() {
		return releaseStatusList;
	}
	public void setReleaseStatusList(List<TSystemStateBean> releaseStatusList) {
		this.releaseStatusList = releaseStatusList;
	}	
}
