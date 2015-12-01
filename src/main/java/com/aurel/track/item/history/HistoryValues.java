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

public class HistoryValues implements HistoryBean {
	private Integer objectID;	
	private Date lastEdit;
	private Integer changedByID;
	private String changedByName;
	private Integer transactionID;
	private Integer workItemID;
	private String fieldName;
	private String newShowValue;
	private String oldShowValue;	
	private boolean newTransction;
	private Integer timesEdited;
	private Integer fieldID;
	private Object oldValue;
	private Object newValue;
	private boolean isDate;
	private boolean longField;
	private String transactionComment;
	private String transactionUuid;
	
	@Override
	public Integer getObjectID() {
		return objectID;
	}
	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	@Override
	public Date getLastEdit() {
		return lastEdit;
	}
	public void setLastEdit(Date lastEdit) {
		this.lastEdit = lastEdit;
	}
	@Override
	public Integer getChangedByID() {
		return changedByID;
	}
	public void setChangedByID(Integer changedByID) {
		this.changedByID = changedByID;
	}
	@Override
	public String getChangedByName() {
		return changedByName;
	}
	@Override
	public void setChangedByName(String changeByName) {
		this.changedByName = changeByName;
	}
	@Override
	public Integer getWorkItemID() {
		return workItemID;
	}
	public void setWorkItemID(Integer workItemID) {
		this.workItemID = workItemID;
	}
	public String getNewShowValue() {
		return newShowValue;
	}
	public void setNewShowValue(String newShowValue) {
		this.newShowValue = newShowValue;
	}
	public String getOldShowValue() {
		return oldShowValue;
	}
	public void setOldShowValue(String oldShowValue) {
		this.oldShowValue = oldShowValue;
	}
	
	@Override
	public int getType() {		
		return HistoryBean.HISTORY_TYPE.COMMON_HISTORYVALUES;
	}
	
	@Override
	public String getDescription() {
		return null;
	}
	@Override
	public void setDescription(String description) {	
	}
	
	public Integer getTransactionID() {
		return transactionID;
	}
	public void setTransactionID(Integer transactionID) {
		this.transactionID = transactionID;
	}
	public boolean isNewTransction() {
		return newTransction;
	}
	
	public void setNewTransction(boolean newTransction) {
		this.newTransction = newTransction;
	}
	public Integer getTimesEdited() {
		return timesEdited;
	}
	public void setTimesEdited(Integer timesEdited) {
		this.timesEdited = timesEdited;
	}
	public Object getOldValue() {
		return oldValue;
	}
	public void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
	}
	public Object getNewValue() {
		return newValue;
	}
	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}
	/**
	 * @return the fieldID
	 */
	public Integer getFieldID() {
		return fieldID;
	}
	/**
	 * @param fieldID the fieldID to set
	 */
	public void setFieldID(Integer fieldID) {
		this.fieldID = fieldID;
	}
	public boolean isDate() {
		return isDate;
	}
	public void setDate(boolean isDate) {
		this.isDate = isDate;
	}
	public String getTransactionUuid() {
		return transactionUuid;
	}
	public void setTransactionUuid(String uuid) {
		this.transactionUuid = uuid;
	}
	public String getTransactionComment() {
		return transactionComment;
	}
	public void setTransactionComment(String transactionComment) {
		this.transactionComment = transactionComment;
	}

	public boolean isLongField() {
		return longField;
	}

	public void setLongField(boolean longField) {
		this.longField = longField;
	}
}
