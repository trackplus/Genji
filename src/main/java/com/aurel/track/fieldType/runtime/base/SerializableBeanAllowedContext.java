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


package com.aurel.track.fieldType.runtime.base;

import com.aurel.track.beans.TWorkItemBean;

public class SerializableBeanAllowedContext {
	private Integer personID;
	private Integer projectID; 
	private Integer issueTypeID; 
	private TWorkItemBean workItemBeanOriginal;
	private boolean isNew;
	
	public Integer getPersonID() {
		return personID;
	}
	public void setPersonID(Integer personID) {
		this.personID = personID;
	}
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
	public boolean isNew() {
		return isNew;
	}
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	public TWorkItemBean getWorkItemBeanOriginal() {
		return workItemBeanOriginal;
	}
	public void setWorkItemBeanOriginal(TWorkItemBean workItemBeanOriginal) {
		this.workItemBeanOriginal = workItemBeanOriginal;
	}
}
