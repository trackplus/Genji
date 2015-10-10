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

import java.util.List;

import com.aurel.track.util.IntegerStringBean;

/**
 * Configuration for a field
 * @author Tamas
 
 */
public class MassOperationExpressionTO extends MassOperationValue  {		
	private static final long serialVersionUID = 1L;
	private List<IntegerStringBean> setterRelations;	
	//private Object possibleValues;
	private String bulkValueTemplate;
	//private Map<Integer, Object> displayValueMap;
	//private Integer fieldID;
	private String fieldLabel;
	private Integer relationID;
	//private Object value;
	
	private boolean valueRequired;
	//whether the field is a rich text field
	//private boolean richText = false;
	//private boolean visible = true;
	//extra added
	private String fieldName; 
	private String relationName;
	private String valueName;
	private String jsonConfig;
	
	public MassOperationExpressionTO(Integer fieldID) {
		super(fieldID);
	}
	
	
	public List<IntegerStringBean> getSetterRelations() {
		return setterRelations;
	}
	public void setSetterRelations(List<IntegerStringBean> matcherRelations) {
		this.setterRelations = matcherRelations;
	}
	
	/*public Object getPossibleValues() {
		return possibleValues;
	}
	public void setPossibleValues(Object possibleValues) {
		this.possibleValues = possibleValues;
	}*/
	public String getBulkValueTemplate() {
		return bulkValueTemplate;
	}
	public void setBulkValueTemplate(String matcherValueJSP) {
		this.bulkValueTemplate = matcherValueJSP;
	}
	/*public Map<Integer, Object> getDisplayValueMap() {
		return displayValueMap;
	}
	public void setDisplayValueMap(Map<Integer, Object> displayValueMap) {
		this.displayValueMap = displayValueMap;
	}*/
	/*public Integer getFieldID() {
		return fieldID;
	}
	public void setFieldID(Integer fieldID) {
		this.fieldID = fieldID;
	}*/
	public Integer getRelationID() {
		return relationID;
	}
	public void setRelationID(Integer matcherID) {
		this.relationID = matcherID;
	}
	/*public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}*/
	public String getFieldLabel() {
		return fieldLabel;
	}
	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}
	
	/*public boolean isRichText() {
		return richText;
	}
	public void setRichText(boolean richText) {
		this.richText = richText;
	}*/
	public boolean isValueRequired() {
		return valueRequired;
	}
	public void setValueRequired(boolean valueRequired) {
		this.valueRequired = valueRequired;
	}
	/*public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}*/
	public String getRelationName() {
		return relationName;
	}
	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}
	public String getValueName() {
		return valueName;
	}
	public void setValueName(String valueName) {
		this.valueName = valueName;
	}
	public String getJsonConfig() {
		return jsonConfig;
	}
	public void setJsonConfig(String jsonConfig) {
		this.jsonConfig = jsonConfig;
	}


	public String getFieldName() {
		return fieldName;
	}


	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}


	
	
}
