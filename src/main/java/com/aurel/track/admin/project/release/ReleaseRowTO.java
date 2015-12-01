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

package com.aurel.track.admin.project.release;

import java.util.Date;

/**
 * Transfer object used for release rows
 * @author Tamas Ruff
 *
 */
public class ReleaseRowTO {
	private String node; 
	private String label;
	private Integer statusFlag;
	private String releaseStatusLabel;	
	private Date dueDate;	
	private boolean defaultNoticed;
	private boolean defaultScheduled;
	//whether this row is release or phase
	private boolean child;
		
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
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
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public String getReleaseStatusLabel() {
		return releaseStatusLabel;
	}
	public void setReleaseStatusLabel(String releaseStatusLabel) {
		this.releaseStatusLabel = releaseStatusLabel;
	}
	public Integer getStatusFlag() {
		return statusFlag;
	}
	public void setStatusFlag(Integer statusFlag) {
		this.statusFlag = statusFlag;
	}
	public boolean isChild() {
		return child;
	}
	public void setChild(boolean child) {
		this.child = child;
	}
	
	
}
