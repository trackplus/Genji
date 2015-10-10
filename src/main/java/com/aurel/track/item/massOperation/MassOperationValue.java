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

package com.aurel.track.item.massOperation;

import java.io.Serializable;
import java.util.Map;

/**
 * Configuration for a field
 * @author Tamas
 *
 */
public class MassOperationValue implements Serializable {
	private static final long serialVersionUID = 1L;
	protected Integer fieldID;
	//map for context dependent values
	protected Object possibleValues;
	//map for context dependent values
	protected Object value;
	//defined only for context (project/issueType) dependent selects
	protected Map<Integer, String> valueLabelMap;
	//the actual listID
	//protected Integer listID;	
	private boolean fieldDisabled = true;
	
	
	public MassOperationValue(Integer fieldID) {
		super();
		this.fieldID = fieldID;
	}
	public Object getPossibleValues() {
		return possibleValues;
	}
	public void setPossibleValues(Object possibleValues) {
		this.possibleValues = possibleValues;
	}
	
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public Integer getFieldID() {
		return fieldID;
	}
	public void setFieldID(Integer fieldID) {
		this.fieldID = fieldID;
	}
	/*public Integer getListID() {
		return listID;
	}
	public void setListID(Integer listID) {
		this.listID = listID;
	}*/
	public Map<Integer, String> getValueLabelMap() {
		return valueLabelMap;
	}
	public void setValueLabelMap(Map<Integer, String> valueLabelMap) {
		this.valueLabelMap = valueLabelMap;
	}
	
	public boolean isFieldDisabled() {
		return fieldDisabled;
	}
	public void setFieldDisabled(boolean disabled) {
		this.fieldDisabled = disabled;
	}
	
	
	
	
}
