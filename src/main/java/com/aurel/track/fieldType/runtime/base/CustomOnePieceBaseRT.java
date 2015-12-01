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


package com.aurel.track.fieldType.runtime.base;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TAttributeValueBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.runtime.bl.AttributeValueBL;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;

public abstract class CustomOnePieceBaseRT extends InputFieldTypeRT 
	implements ICustomFieldTypeRT {
	
	private static final Logger LOGGER = LogManager.getLogger(CustomOnePieceBaseRT.class);	
	/**
	 * Whether the field is a custom field
	 * @return
	 */
	public boolean isCustom() {
		return true;
	}
	
	/**
	 * Whether it is a selection field
	 * @return
	 */
	public boolean isCustomSelect() {
		return false;
	}
	
	/**
	 * Loads the saved custom attribute value from the database 
	 * to the workItem customAttributeValues Map
	 * @param fieldID 
	 * @param parameterCode neglected for single custom fields
	 * @param workItemBean
	 * @param attributeValueMap: 
	 * 	-	key: fieldID_parameterCode
	 * 	-	value: TAttributeValueBean or list of TAttributeValueBeans
	 */
	public void loadAttribute(Integer fieldID, Integer parameterCode, 
			TWorkItemBean workItemBean, Map<String, Object> attributeValueMap) {
		if (isMultipleValues()) {
			List<TAttributeValueBean> attributeValueList = null;
			//get the attributes list from the custom attributes map
			try {
				attributeValueList = (List<TAttributeValueBean>)attributeValueMap.get(MergeUtil.mergeKey(fieldID, parameterCode));
			} catch (Exception e) {
				LOGGER.error("Converting the attribute value for field " + fieldID + " and parameterCode " +
						parameterCode + " for workItem " + workItemBean.getObjectID() + " to List failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			//create an array with objects of specific type
			Object[] arrAttributeValues = null;
			if (attributeValueList!=null) {
				List<Object> values = new LinkedList<Object>();
				for (TAttributeValueBean attributeValueBean : attributeValueList) {
					if (attributeValueBean!=null) {
						Object value = AttributeValueBL.getSpecificAttribute(attributeValueBean, getValueType());
						if (value!=null) {
							values.add(value);
						}
					}
				}
				if (!values.isEmpty()) {
					arrAttributeValues = values.toArray();
				}
			}
			//set the attribute on workItem
			if(arrAttributeValues==null || arrAttributeValues.length==0) {
				workItemBean.setAttribute(fieldID, parameterCode, null);
			} else {
				workItemBean.setAttribute(fieldID, parameterCode, arrAttributeValues);
			}
		} else {
			TAttributeValueBean tAttributeValueBean = null;
			try {
				tAttributeValueBean = (TAttributeValueBean)attributeValueMap.get(MergeUtil.mergeKey(fieldID, parameterCode));
			} catch (Exception e) {
				LOGGER.error("Converting the attribute value for field " + fieldID + " and parameterCode " +
						parameterCode + " for workItem " + workItemBean.getObjectID() + " to TAttributeValueBean failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			//set the attribute on workItem
			if (tAttributeValueBean!=null){
				Object attributeValue = AttributeValueBL.getSpecificAttribute(tAttributeValueBean, getValueType());
				workItemBean.setAttribute(fieldID, parameterCode, attributeValue);
			}
		}
	}
	
	/**
	 * Saves the custom attribute value(s) from the 
	 * workItem's customAttributeValues Map to the database 
	 * @param fieldID
	 * @param parameterCode neglected for single custom fields
	 * @param workItemBean
	 * @param workItemBeanOriginal
	 */
	public void saveAttribute(Integer fieldID, Integer parameterCode, 
			TWorkItemBean workItemBean, TWorkItemBean workItemBeanOriginal) {
		if (isMultipleValues()) {
			Object[] newOptionsArr=null;
			Object[] oldOptionsArr=null;
			//get the new attribute from the workItem
			Object attributeNewObj = workItemBean.getAttribute(fieldID, parameterCode);
			Object attributeOldObj=null;
			//get the old attribute from the original workItem
			if(workItemBeanOriginal!=null) {			
				attributeOldObj = workItemBeanOriginal.getAttribute(fieldID, parameterCode);
			}
			//not set in either old or new workItem, do nothing
			if (attributeNewObj==null && attributeOldObj==null) {			
				return;
			}
			Integer workItemID = workItemBean.getObjectID();		
			
			if(attributeNewObj!=null){
				try {
					newOptionsArr=(Object[])attributeNewObj;
				} catch (Exception e) {
					LOGGER.error("The type of the new attribute by saving is " + attributeNewObj.getClass().getName() + 
							". Casting it to Object[] failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
					return;
				}
			}
			if(attributeOldObj!=null){
				try {
					oldOptionsArr=(Object[])attributeOldObj;
				} catch (Exception e) {
					LOGGER.error("The type of the old attribute  by saving is " + attributeOldObj.getClass().getName() + 
							". Casting it to Object[] failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}	
			}		
			//no old value but new value: insert the new value
			if ((oldOptionsArr==null || oldOptionsArr.length==0) && 
					newOptionsArr!=null && newOptionsArr.length>0) {
				insertOptions(fieldID, parameterCode, workItemID, newOptionsArr);							
				return;
			}
			//no new value but old value: delete the old value
			if ((newOptionsArr==null || newOptionsArr.length==0) && 
					oldOptionsArr!=null && oldOptionsArr.length>0) {
				deleteAttribute(fieldID, parameterCode, workItemID);
				return;
			}						
			//both new value and old value
			if (newOptionsArr!=null && oldOptionsArr!=null) {
				if (newOptionsArr.length!=oldOptionsArr.length) {
					//surely modified			
					updateOptions(fieldID, parameterCode, workItemID, newOptionsArr);
				} else {
					//modify only if really needed
					boolean sameOptions = true;
					for (int i = 0; i < newOptionsArr.length; i++) {
						if (!newOptionsArr[i].equals(oldOptionsArr[i])) {
							sameOptions = false;
							break;
						}
					}
					if (!sameOptions) {
						updateOptions(fieldID, parameterCode, workItemID, newOptionsArr);
					}
				}		
			}
		} else {					
			//get attribute from workItem
			Object attributeNew = workItemBean.getAttribute(fieldID, parameterCode);
			Object attributeOld =null;
			if(workItemBeanOriginal!=null){
				attributeOld=workItemBeanOriginal.getAttribute(fieldID, parameterCode);
				if (attributeNew == null && attributeOld == null) {
					//nothing to change at the database level
					return;
				}
			}			
			TAttributeValueBean attributeValueBean=AttributeValueBL.loadBeanByFieldAndWorkItemAndParameter(fieldID, workItemBean.getObjectID(), parameterCode);
			if(attributeNew==null) {
				//delete from database if exists
				if (attributeValueBean!=null) {
					//delete the old value from the database if exists
					deleteAttribute(fieldID, parameterCode, workItemBean.getObjectID());
				}
				//return if no new values is to set
				return;
			}
			if(attributeOld!=null && !valueModified(attributeNew, attributeOld)) {
				//nothing to change at the database level
				return;
			}
			if(attributeValueBean==null){
				//create new if not exists in the database 
				attributeValueBean=new TAttributeValueBean();
			}
			insertOption(attributeValueBean, fieldID, parameterCode, workItemBean.getObjectID(), attributeNew);			
		}
	}
		
	private void insertOptions(Integer fieldID, Integer parameterCode, 
			Integer workItemID, Object[] arrAttributeValues) {
		for (int i = 0; i < arrAttributeValues.length; i++) { 
			insertOption(new TAttributeValueBean(), fieldID, parameterCode, workItemID, arrAttributeValues[i]);
		}		
	}
	
	private void insertOption(TAttributeValueBean attributeValueBean, Integer fieldID, Integer parameterCode, 
			Integer workItemID, Object attributeValue) {
			attributeValueBean.setField(fieldID);
			attributeValueBean.setWorkItem(workItemID);
			AttributeValueBL.setSpecificAttribute(attributeValueBean, attributeValue, getValueType());
			attributeValueBean.setSystemOptionType(getSystemOptionType());
			attributeValueBean.setParameterCode(parameterCode);
			attributeValueBean.setValidValue(new Integer(getValueType()));
			attributeValueBean.setLastEdit(new Date());
			AttributeValueBL.save(attributeValueBean);
	}
	
	/**
	 * Updates a custom option attribute
	 * @param fieldID
	 * @param parameterCode
	 * @param workItemID
	 * @param optionsArr 
	 */
	private void updateOptions(Integer fieldID, Integer parameterCode,
			Integer workItemID, Object[] optionsArr) {
		deleteAttribute(fieldID, parameterCode,workItemID);
		insertOptions(fieldID, parameterCode, workItemID, optionsArr);
	}
	
	/**
	 * Deletes an attribute (independently of type)
	 * @param fieldID
	 * @param parameterCode
     * @param workItemID
	 */
	private void deleteAttribute(Integer fieldID, Integer parameterCode, Integer workItemID) {		
		AttributeValueBL.delete(fieldID, parameterCode, workItemID);
	}
	
	/**
     * Convert a value to string
     * @param value
     * @return
     */
    public String convertToString(Object value){
    	return getShowISOValue(null, null, value, null, null, null);
    }

    /**
     * Convert a string  to object value
     * @param value
     * @return
     */
    public Object convertFromString(String value){
    	return parseISOValue(value);
    }
}
