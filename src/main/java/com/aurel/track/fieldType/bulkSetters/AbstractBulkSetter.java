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


package com.aurel.track.fieldType.bulkSetters;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.runtime.base.SelectContext;
import com.aurel.track.item.massOperation.MassOperationBL;
import com.aurel.track.json.JSONUtility;

/**
 * Base class for configuring a bulk setter for a field
 * @author Tamas Ruff
 *
 */
public abstract class AbstractBulkSetter implements IBulkSetter {
	
	//the relation for the bulk value
	protected int relation;	
	//the field id
	protected Integer fieldID;

	//has meaning only for select types 
	protected boolean selectValueSurelyAllowed = true;
	
	public AbstractBulkSetter(Integer fieldID) {
		this.fieldID = fieldID;
	}
	
	/**
	 * Builds the name of the client side controls for submit
	 * @param baseName
	 * @return
	 */
	protected String getName(String baseName) {
		StringBuilder stringBuilder = new StringBuilder();
		return stringBuilder.append(baseName).append(".").append(getKeyPrefix()).toString();
	}
	
	/**
	 * The prefix for the map keys
	 * @return
	 */
	protected String getKeyPrefix() {
		return MassOperationBL.getKeyPrefix(getFieldID());
	}
	
	/**
	 * The JSON configuration object for configuring the js control(s) containing the value
	 * @param baseName: the name of the control: important by submit
	 * @param value: the value to be set by rendering (first time or after a submit)
	 * @param dataSource: defined only for lists (list for global lists, map for context dependent lists)
	 * @param labelMap: defined only for context (project/issuType) dependent lists
	 * @param disabled: whether the control is disabled
     * @param personBean
	 * @param locale
	 * @return
	 */
	public String getSetterValueJsonConfig(String baseName, Object value,
		Object dataSource, Map<Integer, String> labelMap, boolean disabled, TPersonBean personBean, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder("{");
		JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.NAME, getName(baseName));
		if (value!=null) {
			JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, (String)value);
		}
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.DISABLED, disabled, true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * @return the fieldID
	 */
	public Integer getFieldID() {
		return fieldID;
	}

	/**
	 * @return the relation
	 */
	public int getRelation() {
		return relation;
	}

	/**
	 * @param relation the relation to set
	 */
	public void setRelation(int relation) {
		this.relation = relation;
	}
	
	public boolean isSelectValueSurelyAllowed() {
		return selectValueSurelyAllowed;
	}

	public void setSelectValueSurelyAllowed(boolean selectValueSurelyAllowed) {
		this.selectValueSurelyAllowed = selectValueSurelyAllowed;
	}

	public List <Integer> getPossibleRelations(boolean required) {
		List <Integer> relations = new ArrayList<Integer>();
		relations.add(Integer.valueOf(BulkRelations.SET_TO));
		if (!required) {
			relations.add(Integer.valueOf(BulkRelations.SET_NULL));
		}
		return relations;
	}
	
	/**
	 * Sets the workItemBean's attribute depending on the value and bulkRelation
	 * @param workItemBean
	 * @param fieldID
	 * @param parameterCode
	 * @param bulkTranformContext
	 * @param selectContext
	 * @param value	
	 * @return ErrorData if an error is found
	 */
	public ErrorData setWorkItemAttribute(TWorkItemBean workItemBean,
			Integer fieldID, Integer parameterCode, 
			BulkTranformContext bulkTranformContext, 
			SelectContext selectContext, Object value) {
		switch (getRelation()) {
		case BulkRelations.SET_TO:
			//the correct value is prepared in fromDisplayString()
			workItemBean.setAttribute(fieldID, value);
			break;
		case BulkRelations.SET_NULL:
			//set the value to null 	
			workItemBean.setAttribute(fieldID, parameterCode, null);
			break;
		}
		//no error
		return null;
	}
	
}
