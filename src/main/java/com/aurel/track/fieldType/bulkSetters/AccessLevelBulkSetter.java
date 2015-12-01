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
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.runtime.base.SelectContext;
import com.aurel.track.json.JSONUtility;

/**
 * Bulk setter for access level field (private/public issue)
 * @author Tamas
 *
 */
public class AccessLevelBulkSetter extends AbstractBulkSetter {	
	public AccessLevelBulkSetter(Integer fieldID) {
		super(fieldID);
	}

	@Override
	public List <Integer> getPossibleRelations(boolean required) {
		List <Integer> relations = new ArrayList<Integer>();		
		relations.add(Integer.valueOf(BulkRelations.SET_PRIVATE));
		relations.add(Integer.valueOf(BulkRelations.SET_PUBLIC));		
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

    @Override
    public String getSetterValueJsonConfig(String baseName, Object value,
            Object dataSource, Map<Integer, String> labelMap, boolean disabled, TPersonBean personBean, Locale locale) {
        StringBuilder stringBuilder = new StringBuilder("{");
        JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.NAME, getName(baseName));
        JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.DISABLED, disabled, true);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

	/**
	 * No ftl for boolean, never called
	 */
    @Override
    public Object fromDisplayString(Map<String, String> displayStringMap, Locale locale) {
    	switch (getRelation()) {
		case BulkRelations.SET_PRIVATE:
			return TWorkItemBean.ACCESS_LEVEL_PRIVATE;			
		default:
			return TWorkItemBean.ACCESS_LEVEL_PUBLIC;				
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
    	Integer valueByRelation = null;
    	switch (getRelation()) {
		case BulkRelations.SET_PRIVATE:
			valueByRelation = TWorkItemBean.ACCESS_LEVEL_PRIVATE;
			break;
		default:
			valueByRelation = TWorkItemBean.ACCESS_LEVEL_PUBLIC;
			break;
		}   
    	workItemBean.setAttribute(fieldID, valueByRelation);
    	return null;    	    	    	    
    }
}
