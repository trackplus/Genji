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

package com.aurel.track.itemNavigator;

import java.util.HashSet;
import java.util.Map;

public class ItemNavigatorTaskBean {

	private boolean isNew;
	private Integer workItemID;
	private Integer projectID;
	private Integer issueTypeID;
	private Integer workItemIDAbove;
	private Integer rowIndex;

	private Map<String,String> fieldValues;
	private HashSet<Integer>presentedFields;

	public ItemNavigatorTaskBean(boolean isNew, Integer workItemID, Integer projectID, Integer issueTypeID,
			Map<String, String> fieldValues) {
		this.isNew = isNew;
		this.workItemID = workItemID;
		this.projectID = projectID;
		this.fieldValues = fieldValues;
		this.issueTypeID = issueTypeID;
	}

	public boolean isNew() {
		return isNew;
	}
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	public Integer getWorkItemID() {
		return workItemID;
	}
	public void setWorkItemID(Integer workItemID) {
		this.workItemID = workItemID;
	}
	public Integer getProjectID() {
		return projectID;
	}
	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}
	public Map<String, String> getFieldValues() {
		return fieldValues;
	}
	public void setFieldValues(Map<String, String> fieldValues) {
		this.fieldValues = fieldValues;
	}
	public Integer getIssueTypeID() {
		return issueTypeID;
	}

	public void setIssueTypeID(Integer issueTypeID) {
		this.issueTypeID = issueTypeID;
	}

	public Integer getWorkItemIDAbove() {
		return workItemIDAbove;
	}

	public void setWorkItemIDAbove(Integer workItemIDAbove) {
		this.workItemIDAbove = workItemIDAbove;
	}

	public HashSet<Integer> getPresentedFields() {
		return presentedFields;
	}

	public void setPresentedFields(HashSet<Integer> presentedFields) {
		this.presentedFields = presentedFields;
	}

	public Integer getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
	}


}
