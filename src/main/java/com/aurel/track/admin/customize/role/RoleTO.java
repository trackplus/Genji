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

package com.aurel.track.admin.customize.role;

import java.io.Serializable;
import java.util.List;

import com.aurel.track.util.IntegerStringBooleanBean;

/**

 */
public class RoleTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer objectID;
	private String label;
	private Boolean[] unfoldedFlags;
	private List<IntegerStringBooleanBean> fullAccessFlags;
	private List<IntegerStringBooleanBean> raciRoles;

	public Integer getObjectID() {
		return objectID;
	}

	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
	}

	public Boolean[] getUnfoldedFlags() {
		return unfoldedFlags;
	}

	public void setUnfoldedFlags(Boolean[] unfoldedFlags) {
		this.unfoldedFlags = unfoldedFlags;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<IntegerStringBooleanBean> getFullAccessFlags() {
		return fullAccessFlags;
	}

	public void setFullAccessFlags(List<IntegerStringBooleanBean> fullAccessFlags) {
		this.fullAccessFlags = fullAccessFlags;
	}

	public List<IntegerStringBooleanBean> getRaciRoles() {
		return raciRoles;
	}

	public void setRaciRoles(List<IntegerStringBooleanBean> raciRoles) {
		this.raciRoles = raciRoles;
	}
}
