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

package com.aurel.track.admin.customize.role;

import com.jgoodies.common.base.Objects;

public class FieldForRoleBean implements Comparable<FieldForRoleBean> {
	private Integer objectID;
	private String fieldLabel;
	private Integer fieldID;
	private boolean hidden = false;
	/**
	 * Whether the user configured this field to be read only
	 */
	private boolean configuredReadOnly = false;
	//whether the field is read only (like itemID, originator, createDate)
	//for these fields the editable checkbox is disabled
	private boolean forcedReadOnly = false;



	public FieldForRoleBean(String fieldLabel, Integer fieldID) {
		super();
		this.fieldLabel = fieldLabel;
		this.fieldID = fieldID;
	}
	public Integer getFieldID() {
		return fieldID;
	}
	public void setFieldID(Integer fieldID) {
		this.fieldID = fieldID;
	}
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}


	public boolean isConfiguredReadOnly() {
		return configuredReadOnly;
	}
	public void setConfiguredReadOnly(boolean configuredReadOnly) {
		this.configuredReadOnly = configuredReadOnly;
	}

	public boolean isForcedReadOnly() {
		return forcedReadOnly;
	}
	public void setForcedReadOnly(boolean forcedReadOnly) {
		this.forcedReadOnly = forcedReadOnly;
	}
	public String getFieldLabel() {
		return fieldLabel;
	}
	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}
	public Integer getObjectID() {
		return objectID;
	}
	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
	}

	@Override
	public int compareTo(FieldForRoleBean fieldForRoleBean) {
		return this.fieldLabel.compareTo(fieldForRoleBean.getFieldLabel());
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(!(obj instanceof FieldForRoleBean)) {
			return false;
		}
		return Objects.equals(((FieldForRoleBean)obj).getFieldLabel(), this.getFieldLabel());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fieldLabel == null) ? 0 : fieldLabel.hashCode());
		return result;
	}
}
