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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.beans.TPersonBean;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.runtime.base.SelectContext;
import com.aurel.track.item.ItemBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.GeneralUtils;

/**
 * Bulk setter for string fields
 * @author Tamas
 *
 */
public class ItemPickerBulkSetter extends AbstractBulkSetter {
	private static final Logger LOGGER = LogManager.getLogger(ParentBulkSetter.class);
	public ItemPickerBulkSetter(Integer fieldID) {
		super(fieldID);
	}
	
	@Override
	public List <Integer> getPossibleRelations(boolean required) {
		List <Integer> relations = new ArrayList<Integer>();
		relations.add(Integer.valueOf(BulkRelations.SET_TO));
		relations.add(Integer.valueOf(BulkRelations.ADD_ITEMS));
		relations.add(Integer.valueOf(BulkRelations.REMOVE_ITEMS));
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
		case BulkRelations.ADD_ITEMS:
		case BulkRelations.REMOVE_ITEMS:	
			return BulkValueTemplates.PARENT_BULK_VALUE_TEMPLATE;
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
			JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, (String)value);
		}		
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.DISABLED, disabled, true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}

	@Override
	public Object fromDisplayString(Map<String, String> displayStringMap, Locale locale) {
		if (displayStringMap == null) {
			return null;
		}
		switch (getRelation()) {
		case BulkRelations.SET_TO:
		case BulkRelations.ADD_ITEMS:
		case BulkRelations.REMOVE_ITEMS:
			String displayString = displayStringMap.get(getKeyPrefix());
			if (displayString!=null) {
				try {
					return Integer.decode(displayString);
				} catch (NumberFormatException ex) {
					LOGGER.info("Parsing the parentID " + displayString + " failed with ");
				}
			}
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
		Integer itemID = null;
		try {
			itemID = (Integer)value;
		} catch (Exception e) {
			LOGGER.info("Getting the string value for " + value +  " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		switch (getRelation()) {
		case BulkRelations.SET_TO:
			if (itemID!=null) {
				if (itemID.equals(workItemBean.getObjectID())) {
					return new ErrorData("itemov.massOperation.err.parentIsDescendant");
				} else {
					workItemBean.setAttribute(fieldID, parameterCode, new Object[] {itemID});
				}	
			}
			break;
		case BulkRelations.ADD_ITEMS:
			if (itemID!=null) {
				if (itemID.equals(workItemBean.getObjectID())) {
					return new ErrorData("itemov.massOperation.err.recursiveItemPicker");
				} else {
					Object[] actualValue = (Object[])workItemBean.getAttribute(fieldID);
					Object[] newValue = addItem(actualValue, itemID);
					workItemBean.setAttribute(fieldID, parameterCode, newValue);
				}
			}
			break;
		case BulkRelations.REMOVE_ITEMS:
			if (itemID!=null) {
				Object[] actualValue = (Object[])workItemBean.getAttribute(fieldID);
				Object[] newValue = removeItem(actualValue, itemID);
				workItemBean.setAttribute(fieldID, parameterCode, newValue);
			}
			break;
		case BulkRelations.SET_NULL:
			workItemBean.setAttribute(fieldID, parameterCode, null);
			break;
		}
		//no error
		return null;
	}
	
	/**
	 * Add the value to the existing ones
	 * @param actualValue
	 * @param addedValue
	 * @return
	 */
	private static Object[] addItem(Object[] actualValue, Integer addedValue) {
		Set<Integer> set = new HashSet<Integer>();
		if (actualValue!=null) {
			for (Object value : actualValue) {
				set.add((Integer)value);
			}
		}
		set.add(addedValue);
		return set.toArray();
	}
	
	/**
	 * Add the value to the existing ones
	 * @param actualValue
	 * @param addedValue
	 * @return
	 */
	private static Object[] removeItem(Object[] actualValue, Integer removeValue) {
		Set<Integer> set = new HashSet<Integer>();
		if (actualValue!=null) {
			for (Object value : actualValue) {
				if (!removeValue.equals(value)) {
					set.add((Integer)value);
				}
			}
		}
		return set.toArray();
	}
	
}
