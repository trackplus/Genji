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

package com.aurel.track.item;

import java.io.Serializable;

/**
 *
 */
public class FieldConfigTO implements Serializable{
	
	public static final long serialVersionUID = 400L;
	
	private Integer fieldID;
	private String label;
	private String tooltip;

	private boolean required;
	private boolean readonly;
	private boolean invisible;
	private String jsonData;
	private boolean hasDependences;
	//makes sense only if hasDependences is true
	private boolean clientSideRefresh;

	public FieldConfigTO(){
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

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public boolean isInvisible() {
		return invisible;
	}

	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public boolean isHasDependences() {
		return hasDependences;
	}

	public void setHasDependences(boolean hasDependences) {
		this.hasDependences = hasDependences;
	}

	public boolean isClientSideRefresh() {
		return clientSideRefresh;
	}

	public void setClientSideRefresh(boolean clientSideRefresh) {
		this.clientSideRefresh = clientSideRefresh;
	}
	
	
}
