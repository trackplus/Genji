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


package com.aurel.track.item.history;

import java.util.Date;

/**
 * <p>
 * Some fields are defined by pointers into other tables, like priority, severity,
 * issue type or status.</p>
 * <p>
 * Objects of this class contain a single such change, where the fieldID describes the 
 * attribute, and the newValue is the object id of the new value for this field.
 * </p>
 */
public class HistorySelectValues {
	private Date lastEdit;
	private Integer workItemID;
	private Integer fieldID;
	private Integer newValue;
	
	public Date getLastEdit() {
		return lastEdit;
	}
	public void setLastEdit(Date lastEdit) {
		this.lastEdit = lastEdit;
	}
	public Integer getWorkItemID() {
		return workItemID;
	}
	public void setWorkItemID(Integer workItemID) {
		this.workItemID = workItemID;
	}
	public Integer getFieldID() {
		return fieldID;
	}
	public void setFieldID(Integer fieldID) {
		this.fieldID = fieldID;
	}
	public Integer getNewValue() {
		return newValue;
	}
	public void setNewValue(Integer newValue) {
		this.newValue = newValue;
	}
}
