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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TFieldChangeBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.callbackInterfaces.IDefault;
import com.aurel.track.fieldType.runtime.callbackInterfaces.ISave;
import com.aurel.track.fieldType.runtime.callbackInterfaces.IValidators;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.fieldType.runtime.validators.RequiredValidator;
import com.aurel.track.fieldType.runtime.validators.Validator;
import com.aurel.track.item.ItemPersisterException;
import com.aurel.track.item.history.HistoryDAOUtils;
import com.aurel.track.util.EqualUtils;

public abstract class InputFieldTypeRT extends OutputFieldTypeRT 
	implements IDefault,ISave,IValidators{
	
	private static final Logger LOGGER = LogManager.getLogger(InputFieldTypeRT.class);
	/**
	 * Saves a custom attribute to the database
	 * @param fieldID
	 * @param parameterCode
	 * @param workItemBean
	 * @param workItemBeanOriginal
	 */
	@Override
	public void processSave(Integer fieldID, Integer parameterCode, 
			TWorkItemBean workItemBean, TWorkItemBean workItemBeanOriginal){
		this.saveAttribute(fieldID,parameterCode, 
			workItemBean,workItemBeanOriginal);
	}
	
	/**
	 * Loads the field change attributes from the the history  
	 * @param fieldID
	 * @param parameterCode
	 * @param fieldChangeMap: 
	 * 		- key: fieldID_parameterCode
	 * 		- value: fieldChangeBean or list of fieldChangeBeans (for multiple selects)
	 * @param newValuesHistoryMap:
	 * 		- key: fieldID_parameterCode
	 * 		- value: the valid field value from fieldChangeBean for the new value: Object or Object[] (for multiple selects)
	 * @param oldValuesHistoryMap 
	 * 		- key: fieldID_parameterCode
	 * 		- value: the valid field value from fieldChangeBean for the old value: Object or Object[] (for multiple selects)
	 */
	@Override
	public void processHistoryLoad(Integer fieldID, Integer parameterCode, 
			Map<String, Object> fieldChangeMap, Map<String, Object> newValuesHistoryMap, Map<String, Object> oldValuesHistoryMap) {
		String mergeKey = MergeUtil.mergeKey(fieldID, parameterCode);
		if (isMultipleValues()) {
			List<TFieldChangeBean> fieldChangeBeanList = null;
			try {
				fieldChangeBeanList = (List<TFieldChangeBean>)fieldChangeMap.get(mergeKey);
			} catch (Exception e) {
				LOGGER.error("The type of the history fieldChanges for multiple values by loading is " + 
						fieldChangeMap.get(mergeKey).getClass().getName() + 
						". Casting it to List failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
				return;
			}			
			if (fieldChangeBeanList==null ) {			
				return;
			}
			List<Object> newValues = new ArrayList<Object>();
			List<Object> oldValues = new ArrayList<Object>();			 
			Iterator<TFieldChangeBean> iterator = fieldChangeBeanList.iterator();
			while (iterator.hasNext()) {
				TFieldChangeBean fieldChangeBean = iterator.next();
				Object newValue = HistoryDAOUtils.getSpecificNewAttribute(fieldChangeBean, getValueType());
				if (newValue!=null) {
					newValues.add(newValue);
				}
				Object oldValue = HistoryDAOUtils.getSpecificOldAttribute(fieldChangeBean, getValueType());
				if (oldValue!=null) {
					oldValues.add(oldValue);
				}				
			}	
			//the values are Object[] see CustomSelectUtil.getSelectedOptions()
			newValuesHistoryMap.put(mergeKey, newValues.toArray());
			oldValuesHistoryMap.put(mergeKey, oldValues.toArray());			
		} else {				
			TFieldChangeBean fieldChangeBean = (TFieldChangeBean)fieldChangeMap.get(mergeKey);
			if (fieldChangeBean!=null) {
				newValuesHistoryMap.put(mergeKey, 
						HistoryDAOUtils.getSpecificNewAttribute(fieldChangeBean, getValueType()));
				oldValuesHistoryMap.put(mergeKey, 
						HistoryDAOUtils.getSpecificOldAttribute(fieldChangeBean, getValueType()));
			}
		}
	}
	
	/**
	 * Saves the field changes explicitly into the history   
	 * @param fieldID
	 * @param parameterCode neglected for single fields
	 * @param historyTransactionID
	 * @param workItemBean
	 * @param workItemBeanOriginal
	 * @param fieldChangeBean the existing TFieldChange entry (for editing/deleting comments, and updating history changes in the last x minutes)
	 */
	@Override
	public void processHistorySave(Integer fieldID, Integer parameterCode, Integer historyTransactionID,
			TWorkItemBean workItemBean, TWorkItemBean workItemBeanOriginal, TFieldChangeBean fieldChangeBean) {
		if (SystemFields.INTEGER_COMMENT.equals(fieldID) && fieldChangeBean!=null) {
			//edit or delete history entry: comment or changes from the least x minutes
			Object attributeNew = workItemBean.getAttribute(fieldID, parameterCode);	
			if (attributeNew==null) {
				//new value null is considered delete for comment
				HistoryDAOUtils.deleteFieldChange(fieldChangeBean);		
			} else {
				//otherwise edit
				try {
					HistoryDAOUtils.updateFieldChange(fieldChangeBean, attributeNew, getValueType());
				} catch (ItemPersisterException e) {
					LOGGER.warn("Updating the field " + fieldID + " failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
		} else {
			//add new FieldChange entry
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
				if (oldOptionsArr==null) {
					oldOptionsArr = new Object[0];
				}
				if (newOptionsArr==null) {
					newOptionsArr = new Object[0];
				}
				int lessValue = 0;
				int greatherValue = 0;
				boolean lessOld;
				if (oldOptionsArr.length<=newOptionsArr.length) {
					lessValue = oldOptionsArr.length;
					greatherValue = newOptionsArr.length;
					lessOld = true;
				} else {
					lessValue = newOptionsArr.length;
					greatherValue = oldOptionsArr.length;
					lessOld = false;
				}			
				for (int i = 0; i < lessValue; i++) {
					try {
						HistoryDAOUtils.insertFieldChange(fieldID, parameterCode, historyTransactionID, 
								newOptionsArr[i], oldOptionsArr[i], getValueType(), getSystemOptionType());
					} catch (ItemPersisterException e) {
						LOGGER.warn("Inserting the field " + fieldID + " with historyTransactionID " + 
								historyTransactionID + " failed with " + e.getMessage());
						LOGGER.debug(ExceptionUtils.getStackTrace(e));
					}	
				}
				for (int i = lessValue; i < greatherValue; i++) {
					if (lessOld) {
						try {
							HistoryDAOUtils.insertFieldChange(fieldID, parameterCode, historyTransactionID, 
									newOptionsArr[i], null, getValueType(), getSystemOptionType());
						} catch (ItemPersisterException e) {
							LOGGER.warn("Inserting the field " + fieldID + " with historyTransactionID " + 
									historyTransactionID + " failed with " + e.getMessage());
							LOGGER.debug(ExceptionUtils.getStackTrace(e));
						}
					} else {
						try {
							HistoryDAOUtils.insertFieldChange(fieldID, parameterCode, historyTransactionID, 
									null, oldOptionsArr[i], getValueType(), getSystemOptionType());
						} catch (ItemPersisterException e) {
							LOGGER.warn("Inserting the field " + fieldID + " with historyTransactionID " + 
									historyTransactionID + " failed with " + e.getMessage());
							LOGGER.debug(ExceptionUtils.getStackTrace(e));
						}
					}
				}							
			} else {
				boolean resetLastChange = false;
				Object attributeNew = workItemBean.getAttribute(fieldID, parameterCode);
				Object oldValue = null;
				boolean recentHistoryChangeExists = false;
				if (fieldChangeBean!=null) {
					recentHistoryChangeExists = true;
					//set the old value from the last saved history entry's old value because the workItemBeanOriginal's 
					//old value is "too new" (changed within x minutes) which should not kept in the history at all
					//because it is overwritten within x minutes
					oldValue = HistoryDAOUtils.getSpecificOldAttribute(fieldChangeBean, getValueType());
					if (!EqualUtils.valueModified(attributeNew, oldValue)) {
						//the old value in the history is the same as the new value. The history entry should be deleted
						//without adding a new history entry. Cancels the last change on the item and in the history 
						LOGGER.debug("Reset the last fieldChange for field " + fieldID);
						resetLastChange = true;
					}
					LOGGER.debug("Delete the last fieldChange for field " + fieldID);
					HistoryDAOUtils.deleteFieldChange(fieldChangeBean);
				}
				if (!resetLastChange) {
					Object attributeOld = null;
					if (recentHistoryChangeExists) {
						attributeOld = oldValue;
					} else {
						if (workItemBeanOriginal!=null) {
							attributeOld=workItemBeanOriginal.getAttribute(fieldID, parameterCode);				
						}
					}
					if (valueModified(attributeNew, attributeOld)) {
						try {
							HistoryDAOUtils.insertFieldChange(fieldID, parameterCode, historyTransactionID,
									attributeNew, attributeOld, getValueType(), getSystemOptionType());
						} catch (ItemPersisterException e) {
							LOGGER.warn("Inserting the field " + fieldID + " with historyTransactionID " + 
									historyTransactionID + " failed with " + e.getMessage());
							LOGGER.debug(ExceptionUtils.getStackTrace(e));
						}				
					}
				}
			}
		}
	}
		
	/**
	 * Loads a default attribute for a new issue
	 * @param fieldID
	 * @param parameterCode
	 * @param validConfig
	 * @param fieldSettings
	 * @param workItemBean
	 */
	@Override
	public void processDefaultValue(Integer fieldID, Integer parameterCode, 
			Integer validConfig, Map<String, Object> fieldSettings, TWorkItemBean workItemBean){
		setDefaultAttribute(fieldID,parameterCode, 
				validConfig, fieldSettings,workItemBean);
	}
	
	/**
	 * Override it when needed
	 */
	@Override
	public void setDefaultAttribute(Integer fieldID, Integer parameterCode, Integer validConfig, Map<String, Object> fieldSettings, TWorkItemBean workItemBean) {			
	}
	
	/**
	 * Returns a map of validators to be applied
	 */
	@Override
	public Map<Integer, List<Validator>> getValidators(Integer fieldID, 
			TFieldConfigBean fieldConfigBean,Integer parameterCode,
			Object settingsObject, TWorkItemBean workItemBean) {
		Map<Integer, List<Validator>> validatorsMap = new HashMap<Integer, List<Validator>>(); 
		if(BooleanFields.TRUE_VALUE.equals(fieldConfigBean.getRequired())){
			List<Validator> validatorsList = new LinkedList<Validator>();
			validatorsList.add(new RequiredValidator());
			if (parameterCode==null) {
				parameterCode = Integer.valueOf(0);
			}
			validatorsMap.put(parameterCode, validatorsList);
		}
		return validatorsMap;
	}
}
