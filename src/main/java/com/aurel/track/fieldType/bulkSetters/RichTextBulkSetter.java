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

import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.runtime.base.SelectContext;

/**
 * Bulk setter for string fields
 * @author Tamas
 *
 */
public class RichTextBulkSetter extends TextAreaBulkSetter {
	private static final Logger LOGGER = LogManager.getLogger(RichTextBulkSetter.class);
	public RichTextBulkSetter(Integer fieldID) {		
		super(fieldID);
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
		case BulkRelations.ADD_TEXT_TO_BEGIN:
		case BulkRelations.ADD_TEXT_TO_END:
		case BulkRelations.ADD_COMMENT:	
		case BulkRelations.REMOVE_HTML_TEXT:	
			return BulkValueTemplates.RICH_TEXT_BULK_VALUE_TEMPLATE;
		}
		
		return BulkValueTemplates.NONE_BULK_VALUE_TEMPLATE;			
	}
	
    @Override
	public Object fromDisplayString(Map<String, String> displayStringMap, Locale locale) {
    	if (displayStringMap == null) {
    		return null;
    	}
    	switch (getRelation()) {
    	case BulkRelations.SET_TO:
    	case BulkRelations.ADD_TEXT_TO_BEGIN:
		case BulkRelations.ADD_TEXT_TO_END:
		case BulkRelations.ADD_COMMENT:	
		case BulkRelations.REMOVE_HTML_TEXT:
			String displayString = displayStringMap.get(getKeyPrefix());			
			return displayString;
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
    		//workItemChangesMap.put(fieldID, null);
    		workItemBean.setAttribute(fieldID, parameterCode, null);
    		return null;
    	}    	
    	String strValue = null;
    	try { 	    		
    		strValue = (String)value;
    	} catch (Exception e) {
    		LOGGER.debug("Getting the string value for " + value +  " failed with " + e.getMessage(), e);
		}    	
    	switch (getRelation()) {
    	case BulkRelations.SET_TO:
    	case BulkRelations.ADD_COMMENT:	
    		workItemBean.setAttribute(fieldID, parameterCode, strValue);
    		break;
		case BulkRelations.ADD_TEXT_TO_BEGIN:
			if (strValue!=null) {
				String originalValue = (String)workItemBean.getAttribute(fieldID, parameterCode);
				if (originalValue!=null) {
					String newValue = strValue + " " + originalValue;					
					//workItemChangesMap.put(fieldID, newValue);
					workItemBean.setAttribute(fieldID, parameterCode, newValue);
				} else {
					workItemBean.setAttribute(fieldID, parameterCode, strValue);
				}
			}
			break;		    	
    		case BulkRelations.ADD_TEXT_TO_END:
    			if (strValue!=null) {
    				String originalValue = (String)workItemBean.getAttribute(fieldID, parameterCode);
    				if (originalValue!=null) {
    					String newValue = originalValue + " " +  strValue;					
    					workItemBean.setAttribute(fieldID, parameterCode, newValue);
    				} else {
    					//workItemChangesMap.put(fieldID, strValue);
    					workItemBean.setAttribute(fieldID, parameterCode, strValue);
    				}
    			}
    			break;
    		case BulkRelations.REMOVE_HTML_TEXT:
    			if (strValue!=null) {
    				String originalValue = (String)workItemBean.getAttribute(fieldID, parameterCode);
    				if (originalValue!=null) {    					    										
    					workItemBean.setAttribute(fieldID, parameterCode, originalValue.replaceAll(strValue, ""));
    				} 
    			}
    			break;
        	}
    	return null;
    }
}
