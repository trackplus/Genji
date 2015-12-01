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

import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.runtime.base.SelectContext;

/**
 * Bulk setter for custom boolean fields 
 * @author Tamas
 *
 */
public class BooleanBulkSetter extends AbstractBulkSetter {	
	public BooleanBulkSetter(Integer fieldID) {
		super(fieldID);
	}

	@Override
	public List <Integer> getPossibleRelations(boolean required) {
		List <Integer> relations = new ArrayList<Integer>();		
		relations.add(Integer.valueOf(BulkRelations.SET_TRUE));
		relations.add(Integer.valueOf(BulkRelations.SET_FALSE));		
		return relations;
	}
	
	
	/**
	 * Gets the name of the jsp fragment which contains 
	 * the control for rendering the bulk value
	 * @return
	 */
	@Override
	public String getSetterValueControlClass() {
		return BulkValueTemplates.NONE_BULK_VALUE_TEMPLATE;
	}		

	/**
	 * No ftl, never called
	 */
    @Override
    public Object fromDisplayString(Map<String, String> displayStringMap, Locale locale) {
    	if (getRelation()==BulkRelations.SET_TRUE) {
    		return BooleanFields.TRUE_VALUE;
    	} else {
    		return BooleanFields.FALSE_VALUE;
    	}    	
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
    	Boolean valueByRelation = null;
    	switch (getRelation()) {
		case BulkRelations.SET_TRUE:
			valueByRelation = Boolean.TRUE;
			break;
		default:
			valueByRelation = Boolean.FALSE;
			break;
		}   
    	workItemBean.setAttribute(fieldID, valueByRelation);
    	return null;    	     			        
    }
}
