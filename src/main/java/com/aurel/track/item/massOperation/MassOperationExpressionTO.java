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
	private String bulkValueTemplate;
	private String fieldLabel;
	private Integer relationID;
	private boolean valueRequired;
	//extra added
	private String fieldName;
	private String fieldItemId; 
	private String relationName;
	private String relationItemId;
	private String valueName;
	private String valueItemId;
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
	
	public String getBulkValueTemplate() {
		return bulkValueTemplate;
	}
	public void setBulkValueTemplate(String matcherValueJSP) {
		this.bulkValueTemplate = matcherValueJSP;
	}
	
	public Integer getRelationID() {
		return relationID;
	}
	public void setRelationID(Integer matcherID) {
		this.relationID = matcherID;
	}
	
	public String getFieldLabel() {
		return fieldLabel;
	}
	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}
	
	public boolean isValueRequired() {
		return valueRequired;
	}
	public void setValueRequired(boolean valueRequired) {
		this.valueRequired = valueRequired;
	}
	
	public String getRelationName() {
		return relationName;
	}
	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}
	
	public String getRelationItemId() {
		return relationItemId;
	}

	public void setRelationItemId(String relationItemId) {
		this.relationItemId = relationItemId;
	}

	public String getValueName() {
		return valueName;
	}
	
	public void setValueName(String valueName) {
		this.valueName = valueName;
	}
	
	public String getValueItemId() {
		return valueItemId;
	}

	public void setValueItemId(String valueItemId) {
		this.valueItemId = valueItemId;
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


	public String getFieldItemId() {
		return fieldItemId;
	}


	public void setFieldItemId(String fieldItemId) {
		this.fieldItemId = fieldItemId;
	}

	

	
	
}
