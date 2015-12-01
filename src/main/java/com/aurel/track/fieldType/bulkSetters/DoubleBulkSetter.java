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


package com.aurel.track.fieldType.bulkSetters;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.beans.TPersonBean;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.runtime.base.SelectContext;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.numberFormatter.DoubleNumberFormatUtil;

/**
 * Bulk setter for double fields
 * @author Tamas
 *
 */
public class DoubleBulkSetter extends AbstractBulkSetter {
	private static final Logger LOGGER = LogManager.getLogger(DoubleBulkSetter.class);
	
	public DoubleBulkSetter(Integer fieldID) {
		super(fieldID);
	}

	@Override
	public List <Integer> getPossibleRelations(boolean required) {
		List <Integer> relations = new ArrayList<Integer>();				
		relations.add(Integer.valueOf(BulkRelations.SET_TO));
		relations.add(Integer.valueOf(BulkRelations.ADD_IF_SET));
		relations.add(Integer.valueOf(BulkRelations.ADD_OR_SET));
		if (!required) {
			relations.add(Integer.valueOf(BulkRelations.SET_NULL));
		}
		return relations;
	}

	/**
	 * Gets the name of the jsp fragment which contains 
	 * the control for rendering the bulk value
	 * @return
	 */
	@Override
	public String getSetterValueControlClass() {
		switch (relation) {
		case BulkRelations.SET_TO:
		case BulkRelations.ADD_IF_SET:		
		case BulkRelations.ADD_OR_SET:
			return BulkValueTemplates.NUMBER_BULK_VALUE_TEMPLATE;
		}
		return BulkValueTemplates.NONE_BULK_VALUE_TEMPLATE;
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
	@Override
	public String getSetterValueJsonConfig(String baseName, Object value,
		Object dataSource, Map<Integer, String> labelMap, boolean disabled, TPersonBean personBean, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder("{");
		JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.NAME, getName(baseName));
		if (value!=null) {
			JSONUtility.appendDoubleValue(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, (Double)value);
		}
		JSONUtility.appendBooleanValue(stringBuilder, "allowDecimals", true);
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.DISABLED, disabled, true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
		

    @Override
    public Object fromDisplayString(Map<String, String> displayStringMap, Locale locale) {
    	if (displayStringMap == null) {
    		return null;
    	}
    	String value = displayStringMap.get(getKeyPrefix());
    	if (value==null) {
    		return null;
    	}
    	switch (getRelation()) {
    	case BulkRelations.SET_TO:
		case BulkRelations.ADD_IF_SET:		
		case BulkRelations.ADD_OR_SET:
			return DoubleNumberFormatUtil.getInstance().parseGUI(value, locale);
    	}  		     	    		
        return null;
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
    @Override
	public ErrorData setWorkItemAttribute(TWorkItemBean workItemBean,     		
    		Integer fieldID, Integer parameterCode, 
    		BulkTranformContext bulkTranformContext, 
    		SelectContext selectContext, Object value) {
    	if (getRelation()==BulkRelations.SET_NULL) {    		
    		workItemBean.setAttribute(fieldID, parameterCode, null);
    		return null;
    	}    	
    	Double doubleValue = null;
    	try {
    		doubleValue = (Double)value;
    	} catch (Exception e) {
    		LOGGER.warn("Getting the double value for " + value +  " failed with " + e.getMessage());
    		LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
    	switch (getRelation()) {
    	case BulkRelations.SET_TO:     		
    		workItemBean.setAttribute(fieldID, parameterCode, doubleValue);
    		break;
		case BulkRelations.ADD_IF_SET:
			if (doubleValue!=null) {
				Double originalValue = (Double)workItemBean.getAttribute(fieldID, parameterCode);
				if (originalValue!=null) {
					double newValue = originalValue.doubleValue() + doubleValue.doubleValue();					
					workItemBean.setAttribute(fieldID, parameterCode, Double.valueOf(newValue));
				}
			}
			break;
		case BulkRelations.ADD_OR_SET:
			if (doubleValue!=null) {
				Double originalValue = (Double)workItemBean.getAttribute(fieldID, parameterCode);
				if (originalValue!=null) {
					double newValue = originalValue.doubleValue() + doubleValue.doubleValue();					
					workItemBean.setAttribute(fieldID, parameterCode, new Double(newValue));
				} else {				
					workItemBean.setAttribute(fieldID, parameterCode, doubleValue);
				}
			}
			break;
    	}
    	return null;
    }
}
