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

package com.aurel.track.exchange.track;


/**
 * Information for field mapping
 * @author Tamas
 *
 */
public class ExchangeField {
	//the Uuid from the TField table
	private String fieldUuid;
	//the fieldID from the TField table
	private Integer fieldID;
	//the class name of the field type from the TField table
	private String fieldType;
	//the name of the field from the TField table: 
	//by importing from external sources (for example excel)  
	//this might be the only specified value
	private String fieldName;
	
	public String getFieldUuid() {
		return fieldUuid;
	}
	public void setFieldUuid(String fieldUuid) {
		this.fieldUuid = fieldUuid;
	}
	public Integer getFieldID() {
		return fieldID;
	}
	public void setFieldID(Integer fieldID) {
		this.fieldID = fieldID;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
}
