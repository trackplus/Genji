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

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.SelectContext;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.fieldType.types.system.text.SystemTextBoxDate.HIERARCHICAL_BEHAVIOR_OPTIONS;
import com.aurel.track.item.ItemBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.CalendarUtil;
import com.aurel.track.util.DateTimeUtils;

/**
 * Bulk setter for start and end date field 
 * @author Tamas
 *
 */
public class SystemDateBulkSetter extends DateBulkSetter {	
	private static final Logger LOGGER = LogManager.getLogger(SystemDateBulkSetter.class);
	public SystemDateBulkSetter(Integer fieldID) {
		super(fieldID);
	}
	
	/**
	 * the name of the date field if the setter relation is either
	 * BulkRelations.SET_EARLIEST_STARTING_FROM or BulkRelations.SET_LATEST_ENDING_AT
	 */
	//private static String DATE_FIELD_NAME = "dateFieldName";
	/**
	 * the name of the adjust checkbox if the setter relation is either
	 * BulkRelations.SET_EARLIEST_STARTING_FROM or BulkRelations.SET_LATEST_ENDING_AT
	 */
	private static String DATE_ITEM_ID = "dateItemId";
	private static String ADJUST_CHECKBBOX_NAME = "adjustCheckboxName";
	private static String ADJUST_CHECKBBOX_ITEM_ID = "adjustCheckboxItemId";
	private static String ADJUST_CHECKBBOX_VALUE = "adjustCheckboxValue";
	private static String ADJUST_CHECKBOX_LABEL = "adjustCheckboxLabel";
	private static String OPPOSITE_FIELD = "oppositeField";
	
	
	
	/**
	 * Gets the name of the jsp fragment which contains 
	 * the control for rendering the bulk value
	 * @return
	 */
	@Override
	public String getSetterValueControlClass() {
		switch (relation) {
		case BulkRelations.SET_TO:
		case BulkRelations.SET_EARLIEST_STARTING_FROM:
		case BulkRelations.SET_LATEST_ENDING_AT:
			//date picker and adjustBothDates checkbox
			return BulkValueTemplates.SYSTEM_DATE_EARLIEST_LATEST_BULK_VALUE_TEMPLATE;
		case BulkRelations.MOVE_BY_DAYS:
			//integer field
			return BulkValueTemplates.SYSTEM_DATE_MOVE_BY_DAYS_BULK_VALUE_TEMPLATE;	
		}
		return BulkValueTemplates.NONE_BULK_VALUE_TEMPLATE;
	}
	
	/**
	 * Builds the name of the adjust checkbox
	 * @param baseName
	 * @param fieldID
	 * @return
	 */
	private static String getAdjustCheckBoxName(String baseName, Integer fieldID) {
		StringBuilder stringBuilder = new StringBuilder();
		return stringBuilder.append(baseName).append(".").append(getAdjustCheckBoxKey(fieldID)).toString();
	}
	
	/**
	 * Builds the itemId of the adjust checkbox
	 * @param baseName
	 * @param fieldID
	 * @return
	 */
	private static String getAdjustCheckBoxItemId(String baseName, Integer fieldID) {
		StringBuilder stringBuilder = new StringBuilder();
		return stringBuilder.append(baseName).append(getAdjustCheckBoxKey(fieldID)).toString();
	}
	
	/**
	 * The map key for adjust ckeckbox
	 * @param fieldID
	 * @return
	 */
	private static String getAdjustCheckBoxKey(Integer fieldID) {
		StringBuilder stringBuilder = new StringBuilder();
		return stringBuilder.append(ADJUST_CHECKBBOX_NAME).append(fieldID).toString();
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
		JSONUtility.appendStringValue(stringBuilder, DATE_ITEM_ID, getItemId(baseName));
			switch (getRelation()) {
			case BulkRelations.MOVE_BY_DAYS:
				Map<Integer, Object> daysAndCheckbox = null;
				if (value!=null) {
					daysAndCheckbox = (Map<Integer, Object>)value;
					try {
						JSONUtility.appendIntegerValue(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, (Integer)daysAndCheckbox.get(Integer.valueOf(0)));
					} catch (Exception e) {
					}
				}
				if (SystemFields.INTEGER_STARTDATE.equals(fieldID) || SystemFields.INTEGER_ENDDATE.equals(fieldID) ||
						SystemFields.INTEGER_TOP_DOWN_START_DATE.equals(fieldID) || SystemFields.INTEGER_TOP_DOWN_END_DATE.equals(fieldID)) {
					JSONUtility.appendStringValue(stringBuilder, ADJUST_CHECKBBOX_NAME, getAdjustCheckBoxName(baseName, fieldID));
					JSONUtility.appendStringValue(stringBuilder, ADJUST_CHECKBBOX_ITEM_ID, getAdjustCheckBoxItemId(baseName, fieldID));
					if (daysAndCheckbox!=null) {
						JSONUtility.appendBooleanValue(stringBuilder, ADJUST_CHECKBBOX_VALUE, (Boolean)daysAndCheckbox.get(Integer.valueOf(1)));
					}
					Integer oppositeField = getOppositeField(fieldID);
					JSONUtility.appendIntegerValue(stringBuilder, OPPOSITE_FIELD, oppositeField);
					String label  = FieldRuntimeBL.getLocalizedDefaultFieldLabel(oppositeField, locale);
					String adjustCheckBoxLabel = LocalizeUtil.getParametrizedString("itemov.massOperation.lbl.adjust", new Object[] {label}, locale);
					JSONUtility.appendStringValue(stringBuilder, ADJUST_CHECKBOX_LABEL, adjustCheckBoxLabel);
				}
				break;
			case BulkRelations.SET_TO:
				if (value!=null) {
					try {
						JSONUtility.appendDateValue(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, (Date)value);
					} catch (Exception e) {
					}
				}
				/*JSONUtility.appendStringValue(stringBuilder,
						"format", DateTimeUtils.getInstance().getExtJSDateFormat(locale));*/
				break;
			case BulkRelations.SET_EARLIEST_STARTING_FROM:
			case BulkRelations.SET_LATEST_ENDING_AT:
				Map<Integer, Object> dateAndCheckbox = null;
				if (value!=null) {
					dateAndCheckbox = (Map<Integer, Object>)value;
					try {
						JSONUtility.appendDateValue(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, (Date)dateAndCheckbox.get(Integer.valueOf(0)));
					} catch (Exception e) {
					}
				}
				if (SystemFields.INTEGER_STARTDATE.equals(fieldID) || SystemFields.INTEGER_ENDDATE.equals(fieldID) ||
						SystemFields.INTEGER_TOP_DOWN_START_DATE.equals(fieldID) || SystemFields.INTEGER_TOP_DOWN_END_DATE.equals(fieldID)) {
					String adjustCheckboxName = getAdjustCheckBoxName(baseName, fieldID);
					JSONUtility.appendStringValue(stringBuilder, ADJUST_CHECKBBOX_NAME, adjustCheckboxName);
					if (dateAndCheckbox!=null) {
						JSONUtility.appendBooleanValue(stringBuilder, ADJUST_CHECKBBOX_VALUE, (Boolean)dateAndCheckbox.get(Integer.valueOf(1)));
					}
					Integer oppositeField = getOppositeField(fieldID);;
					JSONUtility.appendIntegerValue(stringBuilder, OPPOSITE_FIELD, oppositeField);
					String label  = FieldRuntimeBL.getLocalizedDefaultFieldLabel(oppositeField, locale);
					String adjustCheckBoxLabel = LocalizeUtil.getParametrizedString("itemov.massOperation.lbl.adjust", new Object[] {label}, locale);
					JSONUtility.appendStringValue(stringBuilder, ADJUST_CHECKBOX_LABEL, adjustCheckBoxLabel);
				}
				break;
			}
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.DISABLED, disabled, true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}	

	/**
	 * Gets the opposite field to set the value also
	 * @param fieldID
	 * @return
	 */
	private static Integer getOppositeField(Integer fieldID) {
		Integer oppositeField = null;
		if (fieldID!=null) {
			switch (fieldID.intValue()) {
			case SystemFields.STARTDATE:
				oppositeField = SystemFields.INTEGER_ENDDATE;
				break;
			case SystemFields.ENDDATE:
				oppositeField = SystemFields.INTEGER_STARTDATE;
				break;
			case SystemFields.TOP_DOWN_START_DATE:
				oppositeField = SystemFields.INTEGER_TOP_DOWN_END_DATE;
				break;
			case SystemFields.TOP_DOWN_END_DATE:
				oppositeField = SystemFields.INTEGER_TOP_DOWN_START_DATE;
				break;
			}
		}
		return oppositeField;
	}
	/**
	 * Gets the value from the submitted display value
	 * The value should be either a date or a map with date and boolean or integer with boolean 
	 * (when shift both start date and end date at the same time) 
	 * @param displayStringMap
	 * @param locale
	 */
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
		case BulkRelations.MOVE_BY_DAYS:
			Map<Integer, Object> daysAndCheckbox = new HashMap<Integer, Object>();
			Integer intValue = null;
			try {
				intValue = Integer.valueOf(value);
			} catch (Exception e) {
				//remove the value with the wrong type (if for example the old value, according the old relation was a Date)
				displayStringMap.clear();
				LOGGER.info("Converting the " + value +  " to Integer from display string failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			daysAndCheckbox.put(Integer.valueOf(0), intValue);
			Boolean adjustByDays = Boolean.FALSE;
			try {
				adjustByDays = Boolean.valueOf(displayStringMap.get(getAdjustCheckBoxKey(getFieldID())));
			} catch (Exception e) {
			}
			daysAndCheckbox.put(Integer.valueOf(1), adjustByDays);
			return daysAndCheckbox;
		case BulkRelations.SET_TO:
			//Date dateValue = DateTimeUtils.getInstance().parseISODateTime(value);
			Date dateValue = DateTimeUtils.getInstance().parseGUIDate(value, locale);
			if (dateValue==null) {
				//remove the value with the wrong type (if for example the old value, according the old relation was an Integer)
				displayStringMap.clear();
			}
			return dateValue;
		case BulkRelations.SET_EARLIEST_STARTING_FROM:
		case BulkRelations.SET_LATEST_ENDING_AT:
			Map<Integer, Object> dateAndCheckbox = new HashMap<Integer, Object>();
			//String dateValueStr = displayStringMap.get(getDateFieldKey(getFieldID()));
			Date date = DateTimeUtils.getInstance().parseGUIDate(value, locale);
			if (date==null) {
				//remove the value with the wrong type (if for example the old value, according the old relation was an Integer)
				displayStringMap.clear();
			} else {
				dateAndCheckbox.put(Integer.valueOf(0), date);
			}
			Boolean adjustByDate = Boolean.FALSE;
			try {
				adjustByDate = Boolean.valueOf(displayStringMap.get(getAdjustCheckBoxKey(getFieldID())));
			} catch (Exception e) {
			}
			dateAndCheckbox.put(Integer.valueOf(1), adjustByDate);
			return dateAndCheckbox;
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
		IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
		if (fieldTypeRT.getHierarchicalBehavior(fieldID, selectContext.getFieldConfigBean(), selectContext.getFieldSettings())==HIERARCHICAL_BEHAVIOR_OPTIONS.COMPUTE_BOTTOM_UP &&
				ItemBL.hasChildren(workItemBean.getObjectID())) {
			//if the current workItem has children the start and end date should be derived and not directly set ->
			//silently ignore the value to set and leave it unchanged.
			//The dates will be computed starting from the leaf workItems
			return null;
		}
		Boolean adjustBothDates = Boolean.FALSE;
		int bulkRelation = getRelation();
		if (bulkRelation==BulkRelations.SET_EARLIEST_STARTING_FROM || 
				bulkRelation==BulkRelations.SET_LATEST_ENDING_AT ||
				bulkRelation==BulkRelations.MOVE_BY_DAYS) {
			//for these bulkRelation the value is a map
			Map<Integer, Object> dateWithAdjustBothDates = null;
			try {
				dateWithAdjustBothDates = (Map<Integer, Object>)value;
			} catch (Exception e) {
				LOGGER.warn("Converting the " + value +  
						" to Map for setting the attribute failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			if (dateWithAdjustBothDates==null) {
				return null;
			}
			try {
				//!!!!!change the value from a map to a date 
				//because super.setWorkItemAttribute() works only with dates!!!!!
				value = dateWithAdjustBothDates.get(Integer.valueOf(0));
			} catch (Exception e) {
				LOGGER.warn("Converting the " + dateWithAdjustBothDates.get(Integer.valueOf(0)) +
						" to Date for setting the attribute failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			try {
				adjustBothDates = (Boolean)dateWithAdjustBothDates.get(Integer.valueOf(1));
			} catch (Exception e) {
				LOGGER.warn("Converting the " + dateWithAdjustBothDates.get(Integer.valueOf(1)) +
						" to Boolean for setting the attribute failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		
		super.setWorkItemAttribute(workItemBean, fieldID,
				parameterCode, bulkTranformContext, selectContext, value);
		Set<Integer> selectedFieldsSet = bulkTranformContext.getSelectedFieldsSet();
		
		if (adjustBothDates!=null && adjustBothDates.booleanValue()) {
			Integer oppositeDate = getOppositeField(fieldID);
			if ((bulkRelation==BulkRelations.SET_EARLIEST_STARTING_FROM || 
					bulkRelation==BulkRelations.SET_LATEST_ENDING_AT)) {
				//combined shift by days:  if adjust both dates checkbox is checked 
				//only one date can be selected (either start or end date)
				//the offset is already calculated in the valueCache of the BulkTranformContext
				//so use the same offset for the other date to shift days by
				Map<Integer, Object> valueCache = bulkTranformContext.getValueCache();
				if (valueCache!=null && valueCache.get(fieldID)!=null) {
					valueCache.put(oppositeDate, valueCache.get(fieldID));
				}
				super.setWorkItemAttribute(workItemBean, oppositeDate, 
						parameterCode, bulkTranformContext, selectContext, value);
			} else {
				if (bulkRelation==BulkRelations.MOVE_BY_DAYS) {
					Integer moveByDays = null;
					try {
						moveByDays = (Integer)value;
					} catch (Exception e) {
						LOGGER.warn("Converting the move by days " + value +  " to Integer failed with " + e.getMessage());
						LOGGER.debug(ExceptionUtils.getStackTrace(e));
					}
					if (moveByDays!=null) {
						//start dates already shifted: now shift also end dates but with the same offset as the one for start dates
						Object originalValue = workItemBean.getAttribute(oppositeDate, parameterCode);
						if (originalValue==null) {
							return null;
						}
						Date originalDate = null;
						try {
							originalDate = (Date)originalValue;
						} catch (Exception e) {
							LOGGER.warn("Converting the original value " + originalValue +  " to Date failed with " + e.getMessage());
							LOGGER.debug(ExceptionUtils.getStackTrace(e));
						}
						workItemBean.setAttribute(oppositeDate, parameterCode, 
								shiftByDays(originalDate, moveByDays));
					}
				}
			}
		} else {
			//when the start date is set already (super.setWorkItemAttribute()) and the endDate is not yet set 
			//but it is selected for mass operation (will be set in a the next call to setWorkItemAttribute())
			//then do not validate for 'startDateAfterEndDate' now. The validation should take place after the end date is also set
			//WARNING: we suppose that the startDate is set before the endDate if both are selected for bulk operation
			if (SystemFields.INTEGER_STARTDATE.equals(fieldID) && selectedFieldsSet.contains(SystemFields.INTEGER_ENDDATE)) {
				//setWorkItemAttribute() will be called soon also for endDate
				return null;
			}
			if (SystemFields.INTEGER_TOP_DOWN_START_DATE.equals(fieldID) && selectedFieldsSet.contains(SystemFields.INTEGER_TOP_DOWN_END_DATE)) {
				//setWorkItemAttribute() will be called soon also for top down endDate
				return null;
			}
		}
		Date startDate = workItemBean.getStartDate();
		Date endDate = workItemBean.getEndDate();
		if (startDate!=null && endDate!=null && 
				startDate.after(endDate) && !CalendarUtil.sameDay(startDate, endDate)) {
				return new ErrorData("itemov.massOperation.err.startDateAfterEndDate");
		}
		
		Date topDownStartDate = workItemBean.getTopDownStartDate();
		Date topDownEndDate = workItemBean.getTopDownEndDate();
		if (topDownStartDate!=null && topDownEndDate!=null && 
				topDownStartDate.after(topDownEndDate) && !CalendarUtil.sameDay(topDownStartDate, topDownEndDate)) {
				return new ErrorData("itemov.massOperation.err.startDateAfterEndDate");
		}
		
		return null;
	}
}
