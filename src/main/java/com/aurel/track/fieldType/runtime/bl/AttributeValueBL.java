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


package com.aurel.track.fieldType.runtime.bl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.RACIBean;
import com.aurel.track.beans.TAttributeValueBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.dao.AttributeValueDAO;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.fieldType.types.FieldTypeManager;

public class AttributeValueBL {

	private static AttributeValueDAO attributeValueDAO=DAOFactory.getFactory().getAttributeValueDAO();
	private static final Logger LOGGER = LogManager.getLogger(AttributeValueBL.class);
	
	/**
	 * Gets the custom attribute values filtered by filterSelectsTO and raciBean
	 * @param filterSelectsTO
	 * @param raciBean
	 * @param personID
	 * @return
	 */
	public static List<TAttributeValueBean> loadTreeFilterAttributes(FilterUpperTO filterSelectsTO, RACIBean raciBean, Integer personID) {
		return attributeValueDAO.loadTreeFilterAttributes(filterSelectsTO, raciBean, personID);
	}
	
	/**
	 * Get the attribute values filtered by a TQL expression
	 * @param tqlCriteria
	 * @return
	 */
	public static List<TAttributeValueBean> loadTQLFilterAttributeValues(String tqlExpression, TPersonBean personBean, Locale locale, List<ErrorData> errors) {
		return attributeValueDAO.loadTQLFilterAttributeValues(tqlExpression, personBean, locale, errors);
	}
	
	/**
	 * Loads a list of attributeValueBeans from the TAttributeValue table by an array of workItemIDs 
	 * @param workItemIDs
	 * @return
	 */
	public static List<TAttributeValueBean> loadByWorkItemKeys(int[] workItemIDs) {
		return attributeValueDAO.loadByWorkItemKeys(workItemIDs);
	}
	
	/**
	 * Loads an attributeValueBean from the TAttributeValue table by field, parameter and workItem 
	 * @param field
	 * @param parameterCode
	 * @param workItem
	 * @return
	 */
	public static TAttributeValueBean loadBeanByFieldAndWorkItemAndParameter(Integer field, Integer workItem, Integer parameterCode) {
		return attributeValueDAO.loadBeanByFieldAndWorkItemAndParameter(field, workItem, parameterCode);
	}
	
	/**
	 * Gets the user picker attributes by a list of workItemIDs
	 * @param workItemIDs
	 * @return
	 */
	public static List<TAttributeValueBean> getUserPickerAttributesByWorkItems(List<Integer> workItemIDs) {
		return attributeValueDAO.getUserPickerAttributesByWorkItems(workItemIDs);
	}
	
	/**
	 * Get the custom option type attributeValueBeans for an array of workItemIDs 
	 * @param workItemIDs
	 * @return
	 */
	public static List<TAttributeValueBean> loadLuceneCustomOptionAttributeValues(int[] workItemIDs) {
		return attributeValueDAO.loadLuceneCustomOptionAttributeValues(workItemIDs);
	}
	
	/**
	 * Whether a system option from list appears as custom attribute
	 * The reflection does not work because an additional condition
	 * should be satisfied (no direct foreign key relationship exists)
	 * @param objectIDs
	 * @param fieldID
	 */
	public static boolean isSystemOptionAttribute(List<Integer> objectIDs, Integer fieldID) {
		return attributeValueDAO.isSystemOptionAttribute(objectIDs, fieldID);
	}
	
	
	/**
	 * Saves an attributeValueBean in the TAttributeValue table
	 * @param tAttributeValueBean
	 * @return
	 */
	public static Integer save(TAttributeValueBean attributeValueBean) {
		return attributeValueDAO.save(attributeValueBean);
	}
	
	/**
	 * Deletes an attributeValueBean(s) from the TAttributeValue table by field and workItem
	 * @param fieldID
	 * @param parameterCode
	 * @param workItemID 
	 */
	public static void delete(Integer fieldID, Integer parameterCode, Integer workItemID) {
		attributeValueDAO.delete(fieldID, parameterCode, workItemID);
	}
	
	/**
	 * Deletes an attributeValueBean from the TAttributeValue table by ID
	 * @param objectID
	 */
	public static void deleteByObjectID(Integer objectID) {
		attributeValueDAO.deleteByObjectID(objectID);
	}
	
	/**
	 * Deletes the attributeValueBean(s) by field and a systemOption
	 * @param fieldID
	 * @param systemOptionID
	 * @param systemOptionType
	 */
	public static void deleteBySystemOption(Integer fieldID, Integer systemOptionID, Integer systemOptionType) {
		attributeValueDAO.deleteBySystemOption(fieldID, systemOptionID, systemOptionType);
	}
	
	public static Object getSpecificAttribute(TAttributeValueBean tAttributeValueBean, int valueType) {
		switch (valueType) {
		case ValueType.BOOLEAN:
			String charValue = tAttributeValueBean.getCharacterValue();
			Boolean booleanValue = new Boolean(false);
			if (charValue!=null) {
				if (BooleanFields.TRUE_VALUE.equals(charValue.trim())) {
					booleanValue = new Boolean(true);
				}
			}
			return booleanValue;
		case ValueType.CUSTOMOPTION:
			return tAttributeValueBean.getCustomOptionID();
		case ValueType.DATE:
		case ValueType.DATETIME:
			return tAttributeValueBean.getDateValue();
		case ValueType.DOUBLE:
			return tAttributeValueBean.getDoubleValue();
		case ValueType.INTEGER:
		case ValueType.EXTERNALID:
			return tAttributeValueBean.getIntegerValue();
		case ValueType.LONGTEXT:
			return tAttributeValueBean.getLongTextValue();
		case ValueType.SYSTEMOPTION:
			return tAttributeValueBean.getSystemOptionID();
		case ValueType.TEXT:
			return tAttributeValueBean.getTextValue();
		default:
			return null;
		}
	}
	
	public static void setSpecificAttribute(TAttributeValueBean tAttributeValueBean, Object attribute, int valueType) {
		switch (valueType) {
		case ValueType.BOOLEAN:
			Boolean booleanAttribute = null;
			try {
				booleanAttribute = (Boolean)attribute;
			} catch (Exception e) {
				LOGGER.error("Wrong attribute type for boolean " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			if (booleanAttribute!=null && booleanAttribute.booleanValue()==true) {
				tAttributeValueBean.setCharacterValue(BooleanFields.TRUE_VALUE);
			} else {
				tAttributeValueBean.setCharacterValue(BooleanFields.FALSE_VALUE);
			}
			break;
		case ValueType.CUSTOMOPTION:
			Integer customOptionAttribute = null;
			try {
				customOptionAttribute = (Integer)attribute;
			} catch (Exception e) {
				LOGGER.error("Wrong attribute type for custom option " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			tAttributeValueBean.setCustomOptionID(customOptionAttribute);
			break;
		case ValueType.DATE:
		case ValueType.DATETIME:
			Date dateAttribute = null;
			try {
				dateAttribute = (Date)attribute;
			} catch (Exception e) {
				LOGGER.error("Wrong attribute type for date option " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}		
			tAttributeValueBean.setDateValue(dateAttribute);
			break;
		case ValueType.DOUBLE:
			Double doubleAttribute = null;
			try {
				doubleAttribute = (Double)attribute;
			} catch (Exception e) {
				LOGGER.error("Wrong attribute type for double option " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			tAttributeValueBean.setDoubleValue(doubleAttribute);
			break;
		case ValueType.INTEGER:
		case ValueType.EXTERNALID:
			Integer integerAttribute = null;
			try {
				integerAttribute = (Integer)attribute;
			} catch (Exception e) {
				LOGGER.error("Wrong attribute type for value type " + valueType + " and integer option " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			tAttributeValueBean.setIntegerValue(integerAttribute);
			break;
		case ValueType.LONGTEXT:
			String longTextAttribute = null;
			try {
				longTextAttribute = (String)attribute;
			} catch (Exception e) {
				LOGGER.error("Wrong attribute type longTextAttribute " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			tAttributeValueBean.setLongTextValue(longTextAttribute);
			break;
		case ValueType.SYSTEMOPTION:
			Integer systemOptionAttribute = null;
			try {
				systemOptionAttribute = (Integer)attribute;
			} catch (Exception e) {
				LOGGER.error("Wrong attribute type for system option " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}		
			tAttributeValueBean.setSystemOptionID(systemOptionAttribute);
			break;
		case ValueType.TEXT:
			String shortTextAttribute = null;
			try {
				shortTextAttribute = (String)attribute;
			} catch (Exception e) {
				LOGGER.error("Wrong attribute type shortTextAttribute " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			tAttributeValueBean.setTextValue(shortTextAttribute);
			break;
		}
	}
	
	/**
	 * Prepares a map of maps with attributeValueBeans:
	 * 	-	key:	workItemKey
	 * 	-	value:	map
	 * 			-	key:	fieldID_parameterCode
	 * 			-	value:	attributeValueBean or list of attributeValueBeans 
	 * 						(custom and system selects because they might be multiple)
	 * attributeValueBeanList contains attributeValueBeans for the more workItems
	 * @param attributeValueBeanList list of attributeValueBeans
	 * @param customOptionIDs output parameter for gathering the optionIDs
	 * @return
	 */	
	public static Map<Integer, Map<String, Object>> prepareAttributeValueMapForWorkItems(
			List<TAttributeValueBean> attributeValueBeanList,
			Set<Integer> customOptionIDs, Map<Integer, Set<Integer>> externalOptionsMap) {
		Map<Integer, Map<String, Object>> workItemsAttributesMap = new HashMap<Integer, Map<String, Object>>();
		//load the custom attributes to workItemBeans
		if (attributeValueBeanList!=null) {
			for (TAttributeValueBean attributeValueBean : attributeValueBeanList) {
				Integer workItemKey = attributeValueBean.getWorkItem();
				Integer fieldID = attributeValueBean.getField();
				Integer customOptionID = attributeValueBean.getCustomOptionID();
				//gather the custom options
				if (customOptionIDs!=null && customOptionID!=null) {
					customOptionIDs.add(customOptionID);
				}
				//gather the external options
				if (externalOptionsMap!=null) {
					Integer integerValue = attributeValueBean.getIntegerValue();
					if (integerValue!=null) {
						IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
						if (fieldTypeRT!=null && fieldTypeRT.getValueType()==ValueType.EXTERNALID) {
							Set<Integer> externalOptionIDs = externalOptionsMap.get(fieldID);
							if (externalOptionIDs==null) {
								externalOptionIDs = new HashSet<Integer>();
								externalOptionsMap.put(fieldID, externalOptionIDs);
							}
							externalOptionIDs.add(integerValue);
						}
					}
				}
				String mergeKey = MergeUtil.mergeKey(fieldID, attributeValueBean.getParameterCode());
				Map<String, Object> attributeValueBeanMap = workItemsAttributesMap.get(workItemKey);
				if (attributeValueBeanMap==null) {
					attributeValueBeanMap = new HashMap<String, Object>();
					workItemsAttributesMap.put(workItemKey, attributeValueBeanMap);
				}
				Integer valueType = attributeValueBean.getValidValue();
				if (valueType==null || (valueType.intValue()!=ValueType.CUSTOMOPTION && valueType.intValue()!=ValueType.SYSTEMOPTION && 
						!SystemFields.INTEGER_COMMENT.equals(attributeValueBean.getField()))) {
					//direct value (not a select)
					attributeValueBeanMap.put(mergeKey, attributeValueBean);
				} else {
					//select value (custom or system): there might be more entries for the same mergeKey (when multiple=true).
					//Each fieldType should know whether it should get the value from 
					//the map as attributeValueBean or as list of attributeValueBeans 
					List<TAttributeValueBean> selectAttributeList = (List<TAttributeValueBean>)attributeValueBeanMap.get(mergeKey);
					if (selectAttributeList==null) {
						selectAttributeList = new LinkedList<TAttributeValueBean>();
						attributeValueBeanMap.put(mergeKey, selectAttributeList);
					}
					selectAttributeList.add(attributeValueBean);
				}
			}
		}
		return workItemsAttributesMap;
	}
	
	/**
	 * Prepares a map of attributeValueBeans for a workItem:
	 * 	-	key: 	fieldID_parameterCode
	 * 	-	value:	attributeValueBean or list of attributeValueBeans 
	 * 				(custom and system selects because they might be multiple)
	 * attributeValueBeanList contains attributeValueBeans for the same workItem 
	 * @param attributeValueBeanList list of attributeValueBeans 
	 * @return
	 */	
	private static Map<String, Object> prepareAttributeValueMapForWorkItem(List<TAttributeValueBean> attributeValueBeanList) {
		Map<String, Object> attributeValueBeanMap = new HashMap<String, Object>();
		//load the custom attributes to workItemBeans
		for (TAttributeValueBean attributeValueBean : attributeValueBeanList) {
			String mergeKey = MergeUtil.mergeKey(attributeValueBean.getField(), attributeValueBean.getParameterCode());
			Integer valueType = attributeValueBean.getValidValue();
			if (valueType==null || (valueType.intValue()!=ValueType.CUSTOMOPTION && valueType.intValue()!=ValueType.SYSTEMOPTION)) {
				//direct value (not a select)
				if (!attributeValueBeanMap.containsKey(mergeKey)) {
					attributeValueBeanMap.put(mergeKey, attributeValueBean);
				} else {
					//somehow (race condition?) there are more entries in db. delete them silently
					deleteByObjectID(attributeValueBean.getObjectID());
				}
			} else {
				//select value (custom or system): there might be more entries for the same mergeKey (when multiple=true).
				//each fieldType should know whether it should get the value from 
				//the map as attributeValueBean or as list of attributeValueBeans 
				List<TAttributeValueBean> selectAttributeList = (List<TAttributeValueBean>)attributeValueBeanMap.get(mergeKey);
				if (selectAttributeList==null) {
					selectAttributeList = new ArrayList<TAttributeValueBean>();
					attributeValueBeanMap.put(mergeKey, selectAttributeList);
				}
				selectAttributeList.add(attributeValueBean);
			}
		}
		return attributeValueBeanMap;
	}	
	
	
	/**
	 * Load the custom attributes for the saved workItem  
	 * @param workItem
	 */
	public static Map<String, Object> loadWorkItemCustomAttributes(TWorkItemBean workItem) {
		//get all attributeValueBeans for the workItem
		List<TAttributeValueBean> attributeValueBeanList = attributeValueDAO.loadByWorkItem(workItem.getObjectID());
		//prepare them in a map keyed by fieldID_parameterCode
		Map<String, Object> attributeValueBeanMap = prepareAttributeValueMapForWorkItem(attributeValueBeanList);
		//gather the fields in a set to avoid calling processLoad for each attributeValueBean 
		//(each part of a composite or each value of a multiple select)
		Set<Integer> fieldSet = getFieldIDSetForWorkItem(attributeValueBeanList);
		return loadWorkItemCustomAttributes(workItem, attributeValueBeanMap, fieldSet);
	}
	
	/**
	 * Load the custom attributes for the saved workItem  
	 * @param workItem
	 */
	public static Map<String, Object> loadWorkItemCustomUserPickerAttributes(TWorkItemBean workItem) {
		//get all attributeValueBeans for the workItem
		List<TAttributeValueBean> attributeValueBeanList = attributeValueDAO.loadUserPickerByWorkItem(workItem.getObjectID());
		//prepare them in a map keyed by fieldID_parameterCode
		Map<String, Object> attributeValueBeanMap = prepareAttributeValueMapForWorkItem(attributeValueBeanList);
		//gather the fields in a set to avoid calling processLoad for each attributeValueBean 
		//(each part of a composite or each value of a multiple select)
		Set<Integer> fieldSet = getFieldIDSetForWorkItem(attributeValueBeanList);
		return loadWorkItemCustomAttributes(workItem, attributeValueBeanMap, fieldSet);
	}
	
	/**
	 * Load the custom attributes for the saved workItem, from a map of attributeValueBeans and for a set of fieldIDs
	 * @param workItem
	 * @param attributeValueBeanMap
	 * @param fieldSet
	 */
	public static Map<String, Object> loadWorkItemCustomAttributes(TWorkItemBean workItem, 
			Map<String, Object> attributeValueBeanMap, Set<Integer> fieldSet) {
		if (fieldSet!=null) { 
			for (Integer fieldID : fieldSet) {
				IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
				if (fieldTypeRT!=null) {
					fieldTypeRT.processLoad(fieldID, null, workItem, attributeValueBeanMap);
				}
			}
		}
		return attributeValueBeanMap;
	}
	
	/**
	 * Gather the fields in a set to avoid calling processLoad for each attributeValueBean 
	 * (each part of a composite or each value of a multiple select)
	 * @param attributeValueBeanList
	 * @return
	 */
	private static Set<Integer> getFieldIDSetForWorkItem(List<TAttributeValueBean> attributeValueBeanList) {
		Set<Integer> fieldSet = new HashSet<Integer>();
		if (attributeValueBeanList!=null) {
			for (TAttributeValueBean attributeValueBean : attributeValueBeanList) {
				fieldSet.add(attributeValueBean.getField());
			}
		}
		return fieldSet;
	}
	
	/**
	 * Prepares a map of sets with fieldIDs:
	 * 	-	key:	workItemKey 
	 * 	-	value:	set of fieldIDs for that workItem
	 * @param attributeValueBeanList list of attributeValueBeans
	 * @return
	 */	
	public static Map<Integer, Set<Integer>> getFieldIDsSetForWorkItems(List<TAttributeValueBean> attributeValueBeanList) {
		Map<Integer, Set<Integer>> workItemFieldsMap = new HashMap<Integer, Set<Integer>>();
		Set<Integer> fieldSet = new HashSet<Integer>();
		Integer workItemID;
		if (attributeValueBeanList!=null) {
			for (TAttributeValueBean attributeValueBean : attributeValueBeanList) {
				workItemID = attributeValueBean.getWorkItem();
				fieldSet = workItemFieldsMap.get(workItemID);
				if (fieldSet==null) {
					fieldSet = new HashSet<Integer>();
					workItemFieldsMap.put(workItemID, fieldSet);
				}
				fieldSet.add(attributeValueBean.getField());
			}
		}
		return workItemFieldsMap;
	}

}
