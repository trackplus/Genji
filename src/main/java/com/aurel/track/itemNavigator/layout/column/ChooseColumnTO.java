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

package com.aurel.track.itemNavigator.layout.column;

/**
 * Transfer object for choose column field
 * @author Tamas
 *
 */
public class ChooseColumnTO {
	private Integer fieldID;
	private String label;
	private boolean used;
	
	public ChooseColumnTO(Integer fieldID, String label) {
		super();
		this.fieldID = fieldID;
		this.label = label;
	}
	
	public Integer getFieldID() {
		return fieldID;
	}
	public void setFieldID(Integer fieldID) {
		this.fieldID = fieldID;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public boolean isUsed() {
		return used;
	}
	public void setUsed(boolean used) {
		this.used = used;
	}
	
	
}
