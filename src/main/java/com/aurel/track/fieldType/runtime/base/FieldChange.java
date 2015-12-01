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

public class FieldChange {
	
	/**
	 * resource key of the field label
	 */
	private Integer fieldID;
	private String localizedFieldLabel;	
	private String newShowValue;
	private String oldShowValue;
	private String localizedChangeDetail;
	private boolean explicitHistory;
	
	private boolean changed;
	
	public Integer getFieldID() {
		return fieldID;
	}
	public void setFieldID(Integer fieldID) {
		this.fieldID = fieldID;
	}
	public boolean isChanged() {
		return changed;
	}
	public void setChanged(boolean changed) {
		this.changed = changed;
	}
	public String getLocalizedFieldLabel() {
		return localizedFieldLabel;
	}
	public void setLocalizedFieldLabel(String localizedFieldLabel) {
		this.localizedFieldLabel = localizedFieldLabel;
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
	/**
	 * @return the localizedChangeDetail
	 */
	public String getLocalizedChangeDetail() {
		return localizedChangeDetail;
	}
	/**
	 * @param localizedChangeDetail the localizedChangeDetail to set
	 */
	public void setLocalizedChangeDetail(String localizedChangeDetail) {
		this.localizedChangeDetail = localizedChangeDetail;
	}
	public boolean isExplicitHistory() {
		return explicitHistory;
	}
	public void setExplicitHistory(boolean explicitHistory) {
		this.explicitHistory = explicitHistory;
	}
}
