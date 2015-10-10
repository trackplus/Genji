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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.beans.TPersonBean;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.calendar.WorkDaysConfigImplementation;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.runtime.base.SelectContext;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.DateTimeUtils;

/**
 * Bulk setter for date fields
 * @author Tamas
 *
 */
public class DateBulkSetter extends AbstractBulkSetter {
	private static final Logger LOGGER = LogManager.getLogger(DateBulkSetter.class);
	
	public DateBulkSetter(Integer fieldID) {
		super(fieldID);
	}
	
	@Override
	public List <Integer> getPossibleRelations(boolean required) {
		List <Integer> relations = new ArrayList<Integer>();
		relations.add(Integer.valueOf(BulkRelations.SET_TO));
		relations.add(Integer.valueOf(BulkRelations.SET_EARLIEST_STARTING_FROM));
		relations.add(Integer.valueOf(BulkRelations.SET_LATEST_ENDING_AT));
		relations.add(BulkRelations.MOVE_BY_DAYS);
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
	public String getSetterValueControlClass() {
		switch (relation) {
		case BulkRelations.SET_TO:
		case BulkRelations.SET_EARLIEST_STARTING_FROM:
		case BulkRelations.SET_LATEST_ENDING_AT:
			return BulkValueTemplates.DATE_BULK_VALUE_TEMPLATE;
		case BulkRelations.MOVE_BY_DAYS:
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
			switch (getRelation()) {
			case BulkRelations.MOVE_BY_DAYS:
				JSONUtility.appendBooleanValue(stringBuilder, "allowDecimals", false);
				if (value!=null) {
					try {
						JSONUtility.appendIntegerValue(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, (Integer)value);
					} catch (Exception e) {
					}
				}
				break;
			case BulkRelations.SET_EARLIEST_STARTING_FROM:
			case BulkRelations.SET_LATEST_ENDING_AT:
			case BulkRelations.SET_TO:
				if (value!=null) {
					try {
					JSONUtility.appendDateValue(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, (Date)value);
					} catch (Exception e) {
					}
				}
				/*JSONUtility.appendStringValue(stringBuilder,
						"format", DateTimeUtils.getInstance().getExtJSDateFormat(locale));*/
				/*JSONUtility.appendStringValue(stringBuilder,
						"submitFormat", "Y-m-d H:i:s.u"*);*/
				break;
			}
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.DISABLED, disabled, true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
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
			Integer intValue = null;
			try {
				intValue = Integer.valueOf(value);
			} catch (Exception e) {
				//remove the value with the wrong type (if for example the old value, according the old relation was a Date)
				displayStringMap.clear();
				LOGGER.debug("Converting the " + value +  " to Integer from display string failed with " + e.getMessage(), e);
			}
			return intValue;
		case BulkRelations.SET_TO:
		case BulkRelations.SET_EARLIEST_STARTING_FROM:
		case BulkRelations.SET_LATEST_ENDING_AT:
			//Date dateValue = DateTimeUtils.getInstance().parseDateFromCalendarGUI(value, locale);
			Date dateValue = DateTimeUtils.getInstance().parseGUIDate(value, locale);
			if (dateValue==null) {
				//remove the value with the wrong type (if for example the old value, according the old relation was an Integer)
				displayStringMap.clear();
			}
			return dateValue;
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
		if (getRelation()==BulkRelations.SET_TO) {
			Date dateValue = null;
			try {
				dateValue = (Date)value;
			} catch (Exception e) {
				LOGGER.warn("Getting the date value for " + value +  " failed with " + e.getMessage(), e);
			}
			workItemBean.setAttribute(fieldID, parameterCode, dateValue);
			return null;
		}
		//the other relations depend on original date
		Object originalValue = workItemBean.getAttribute(fieldID, parameterCode);
		if (originalValue==null) {
			return null;
		}
		Date originalDate = null;
		try {
			originalDate = (Date)originalValue;
		} catch (Exception e) {
			LOGGER.warn("Converting the original value " + originalValue +  " to Date failed with " + e.getMessage(), e);
		}
		if (originalDate==null) {
			return null;
		}
		
		switch (getRelation()) {
		case BulkRelations.MOVE_BY_DAYS:
			Integer intValue = null;
			try {
				intValue = (Integer)value;
			} catch (Exception e) {
				LOGGER.warn("Getting the integer value for " + value +  " failed with " + e.getMessage(), e);
			}
			workItemBean.setAttribute(fieldID, parameterCode, 
					shiftByDays(originalDate, intValue));
			break;
		case BulkRelations.SET_EARLIEST_STARTING_FROM:
			Integer earlyOffset = (Integer)bulkTranformContext.getValueCache().get(fieldID);
			if (earlyOffset==null) {
				calculateOffset(bulkTranformContext, fieldID, parameterCode, value, true);
				earlyOffset = (Integer)bulkTranformContext.getValueCache().get(fieldID);
			}
			workItemBean.setAttribute(fieldID, parameterCode, 
					shiftByDays(originalDate, earlyOffset));
			break;
		case BulkRelations.SET_LATEST_ENDING_AT:
			Integer lateOffset = (Integer)bulkTranformContext.getValueCache().get(fieldID);
			if (lateOffset==null) {
				calculateOffset(bulkTranformContext, fieldID, parameterCode, value, false);
				lateOffset = (Integer)bulkTranformContext.getValueCache().get(fieldID);
			}
			workItemBean.setAttribute(fieldID, parameterCode,
					shiftByDays(originalDate, lateOffset));
			break;
		}
		return null;
	}
	
	/**
	 * Shift a date by a number of days 
	 * @param originalDate
	 * @param daysOffset
	 * @return
	 */
	protected static Date shiftByDays(Date originalDate, Integer daysOffset) {
		if (daysOffset!=null && daysOffset.intValue()!=0) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(originalDate);
			calendar.add(Calendar.DATE, daysOffset.intValue());
			while (WorkDaysConfigImplementation.isNonWorkingDay(calendar.getTime(), null)) {
				calendar.add(Calendar.DATE, 1);
			}
			return calendar.getTime();
		} 
		return originalDate;
	}
	
	private static void calculateOffset(BulkTranformContext bulkTranformContext, Integer fieldID, 
			Integer parameterCode, Object targetValue, boolean earliest) {				
		List<TWorkItemBean> selectedWorkItems = bulkTranformContext.getSelectedWorkItems();
		Map<Integer, Object> valueCache = bulkTranformContext.getValueCache();
		Date targetDate = null;
		try {
			targetDate = (Date)targetValue;
		} catch (Exception e) {
			LOGGER.warn("Converting the target value " + targetValue +  " to Date failed with " + e.getMessage(), e);
		}
		if (targetDate==null || selectedWorkItems==null) {
			//set to 0 to mark it as calculated (avoid finding the offset for each workItem). 0 means no change anyway
			valueCache.put(fieldID, Integer.valueOf(0));
			return;
		}
		Date extremeDate = null;
		for (TWorkItemBean workItemBean : selectedWorkItems) {
			Object value = workItemBean.getAttribute(fieldID, parameterCode);
			if (value!=null) {
				Date actualDate = (Date)value;
				if (extremeDate==null) {
					extremeDate = actualDate;
				} else {
					if ((earliest && actualDate.before(extremeDate)) ||
							!earliest && actualDate.after(extremeDate)) { 
						extremeDate = actualDate;
					}
				}
			}
		}
		if (extremeDate==null) {
			//set to 0 to mark it as calculated (avoid finding the offset for each workItem). 0 means no change anyway
			valueCache.put(fieldID, Integer.valueOf(0));   	
		} else {
			Calendar targetCalendar = Calendar.getInstance();
			targetCalendar.setTime(targetDate);			
			Calendar extremeCalendar = Calendar.getInstance();
			extremeCalendar.setTime(extremeDate);
			long diff = targetCalendar.getTimeInMillis() - extremeCalendar.getTimeInMillis();
			//daylight saving correction
			double days = diff*1.0/(1000 * 60 * 60 * 24);
			int intDays = (int)Math.round(days);	
			valueCache.put(fieldID, Integer.valueOf(intDays));
		}		
	}
}
