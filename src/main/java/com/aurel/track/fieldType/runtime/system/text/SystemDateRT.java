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


package com.aurel.track.fieldType.runtime.system.text;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.customize.workflow.activity.IActivityConfig;
import com.aurel.track.admin.customize.workflow.activity.IActivityExecute;
import com.aurel.track.admin.customize.workflow.activity.IValueConverter;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TTextBoxSettingsBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.bulkSetters.IBulkSetter;
import com.aurel.track.fieldType.bulkSetters.SystemDateBulkSetter;
import com.aurel.track.fieldType.constants.DateOptions;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.fieldChange.FieldChangeSetters;
import com.aurel.track.fieldType.fieldChange.FieldChangeValue;
import com.aurel.track.fieldType.fieldChange.apply.DateFieldChangeApply;
import com.aurel.track.fieldType.fieldChange.config.DateFieldChangeConfig;
import com.aurel.track.fieldType.fieldChange.converter.DateSetterConverter;
import com.aurel.track.fieldType.runtime.base.LocalLookupContainer;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.runtime.matchers.converter.DateMatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.converter.MatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.design.DateMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.run.DateMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.IMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.fieldType.runtime.validators.CalendarValidator;
import com.aurel.track.fieldType.runtime.validators.DateHierarchyValidator;
import com.aurel.track.fieldType.runtime.validators.DateValidator;
import com.aurel.track.fieldType.runtime.validators.Validator;
import com.aurel.track.fieldType.types.system.text.SystemTextBoxDate.HIERARCHICAL_BEHAVIOR_OPTIONS;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.workflow.execute.WorkflowContext;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.IntegerStringBean;

/**
 * Runtime class for StartDate and EndDate
 * can't derive from the SystemTextBaseRT because of  getValidators 
 * @author Tamas Ruff
 *
 */
public class SystemDateRT extends SystemTextBoxBaseRT {
	private static final Logger LOGGER = LogManager.getLogger(SystemDateRT.class);
	
	/**
	 * Whether bottom up computing is activated for field
	 * @param fieldID
	 * @param fieldConfigBean
	 * @param fieldSettings
	 * @return a COMPUTE_PARENT_OPTIONS value
	 */
	@Override
	public int getHierarchicalBehavior(Integer fieldID, TFieldConfigBean fieldConfigBean, Object fieldSettings) {
		if (fieldSettings==null) {
			//no field settings fall back to previous behavior
			if (ApplicationBean.getInstance().getSiteBean().getSummaryItemsBehavior()) {
				if (SystemFields.INTEGER_STARTDATE.equals(fieldID) || SystemFields.INTEGER_ENDDATE.equals(fieldID)) {
					return HIERARCHICAL_BEHAVIOR_OPTIONS.COMPUTE_BOTTOM_UP;
				} else {
					return HIERARCHICAL_BEHAVIOR_OPTIONS.VALIDATE;
				}
			} else {
				return HIERARCHICAL_BEHAVIOR_OPTIONS.NO_RESTRICTION;
			}
		} else {
			TTextBoxSettingsBean textBoxSettingsBean = (TTextBoxSettingsBean)fieldSettings;
			Integer bottomUpBehavior = textBoxSettingsBean.getMinInteger();
			if (bottomUpBehavior==null) {
				if (ApplicationBean.getInstance().getSiteBean().getSummaryItemsBehavior()) {
					if (SystemFields.INTEGER_STARTDATE.equals(fieldID) || SystemFields.INTEGER_ENDDATE.equals(fieldID)) {
						//the default is bottom up for start/end date
						return HIERARCHICAL_BEHAVIOR_OPTIONS.COMPUTE_BOTTOM_UP;
					} else {
						return HIERARCHICAL_BEHAVIOR_OPTIONS.VALIDATE;
					}
				} else {
					return HIERARCHICAL_BEHAVIOR_OPTIONS.NO_RESTRICTION;
				}
			} else {
				return bottomUpBehavior.intValue();
			}
		}
	}
	
	/**
	 * Sets the parent value derived from child value
	 * @param parentWorkItem
	 * @param fieldID
	 * @param childValueNew
	 * @param childValueOriginal
	 * @param fieldConfigBean
	 * @param fieldSettings
	 * @return true if parent value was really modified
	 */
	@Override
	public boolean setBottomUpParentValue(TWorkItemBean parentWorkItem, Integer fieldID,
			Object childValueNew, Object childValueOriginal, TFieldConfigBean fieldConfigBean, Object fieldSettings) {
		if (getHierarchicalBehavior(fieldID, fieldConfigBean, fieldSettings)==HIERARCHICAL_BEHAVIOR_OPTIONS.COMPUTE_BOTTOM_UP) {
			Integer parentID = parentWorkItem.getObjectID();
			boolean earliest = SystemFields.INTEGER_STARTDATE.equals(fieldID) || SystemFields.INTEGER_TOP_DOWN_START_DATE.equals(fieldID);
			Date parentDate = (Date)parentWorkItem.getAttribute(fieldID);
			Date outermostChildDate = getOutermostChildDate(parentID, fieldID, earliest);
			if (EqualUtils.notEqual(parentDate, outermostChildDate)) {
				parentWorkItem.setAttribute(fieldID, outermostChildDate);
				return true;
			}
			return false;
		}
		return false;
	}
	
	/**
	 * Get the earliest start date or the latest endDate of all children
	 * @param parentID
	 * @param earliest
	 * @return
	 */
	private static Date getOutermostChildDate(Integer parentID, Integer fieldID, boolean earliest) {
		Date date = null;
		List<TWorkItemBean> siblingWorkItemBeans = ItemBL.getChildren(parentID);
		if (siblingWorkItemBeans!=null && !siblingWorkItemBeans.isEmpty()) {
			for (TWorkItemBean siblingWorkItemBean : siblingWorkItemBeans) {
				Date siblingDate = (Date)siblingWorkItemBean.getAttribute(fieldID);
				if (siblingDate!=null &&
						(date==null || (earliest && siblingDate.before(date)) || (!earliest && siblingDate.after(date)))) {
					date = siblingDate;
				}
			}
		}
		return date;
	}
	
	/** 
	 * Defines the data type of the field attribute
	 * Used by saving field attributes for custom fields
	 * and saving explicit history for both system and custom fields
	 * Should be a @ValueType constant
	 * @return
	 */
	@Override
	public int getValueType() {
		return ValueType.DATE;
	}
	
	/**
	 * Whether the value have been modified
	 * Considered by assembling the history and mail
	 * @param newValue
	 * @param oldValue
	 * @return
	 */
	@Override
	public boolean valueModified(Object newValue, Object oldValue) {
		Date dateNew = null;
		if (newValue!=null) {
			try {
				dateNew = (Date) newValue;
			} catch (Exception e) {
				LOGGER.error("Converting the new value of type " + 
						newValue.getClass().getName() +
						" to Date failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		Date dateOld = null;
		if (oldValue!=null) {
			try {
				dateOld = (Date) oldValue;
			} catch (Exception e) {
				LOGGER.error("Converting the old value of type " + 
						newValue.getClass().getName() +  
						" to Boolean failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		//StartDate and EndDate contain typically no time value only date value
		return EqualUtils.notEqualDateNeglectTime(dateNew, dateOld);
	}
	
	/**
	 * Get the specific attribute from TTextBoxSettingsBean for default value
	 */
	@Override
	public Object getSpecificDefaultAttribute(TTextBoxSettingsBean textBoxSettingsBean) {
		Date defaultDate = null;
		Integer defaultOption = textBoxSettingsBean.getDefaultOption();
		if (defaultOption!=null)
		switch (defaultOption.intValue())
		{
		case DateOptions.EMPTY:
			break;
		case DateOptions.DATE:
			defaultDate = textBoxSettingsBean.getDefaultDate();
			break;
		case DateOptions.NOW:
			defaultDate = new Date();
			break;
		case DateOptions.CREATED_PLUS_DAYS:
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.DATE, textBoxSettingsBean.getDefaultInteger());
			defaultDate = calendar.getTime();
			break;
		}
		return defaultDate;
	}
		
	/**
	 * Get the show value called when an item result set is implied
	 * @param fieldID
	 * @param parameterCode
	 * @param value
	 * @param workItemID
	 * @param localLookupContainer
	 * @param locale 
	 * @return
	 */
	@Override
	public String getShowValue(Integer fieldID, Integer parameterCode, Object value, 
			Integer workItemID, LocalLookupContainer localLookupContainer, Locale locale) {
		return getShowValue(value, locale);
	}
	
	/**
	 * Get the value to be shown 
	 */
	@Override
	public String getShowValue(Object value, Locale locale) {
		if (value!=null) {
			try {
				Date dateValue = (Date)value;
				return DateTimeUtils.getInstance().formatGUIDate(dateValue, locale);
			} catch (Exception e) {
				//it can happen when the value is an Integer (days relative in treeQueries)
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Getting the showValue for date " + value + " failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
				return value.toString();
			}
		}
		return "";
	}

	
	/**
	 * Get the ISO show value for locale independent exporting to xml
	 * typically same as show value, date and number values are formatted by iso format 
	 * @param fieldID
	 * @param parameterCode
	 * @param value
	 * @param workItemID
	 * @param localLookupContainer
	 * @param locale
	 * @return
	 */
	@Override
	public String getShowISOValue(Integer fieldID, Integer parameterCode, Object value, 
			Integer workItemID, LocalLookupContainer localLookupContainer, Locale locale) {
		if (value!=null) {
			try {
				Date dateValue = (Date)value;
				return DateTimeUtils.getInstance().formatISODateTime(dateValue);
			} catch (Exception e) {
				return value.toString();
			}
		}
		return "";
	}

	/**
	 * Parses the string value into the corresponding object value
	 * @param isoStrValue
	 * @return
	 */
	@Override
	public Object parseISOValue(Object isoStrValue) {
		if (isoStrValue!=null) {
			return DateTimeUtils.getInstance().parseISODate(isoStrValue.toString());
		}
		return null; 
	}
	
	/**
	 * Returns whether the iso show value differs from 
	 * the locale specific show value for this field type.
	 * This is needed to avoid loading the iso values 
	 * by repeating the load for locale specific show value
	 * (which takes surprisingly long time)   
	 * @return
	 */
	@Override
	public boolean isoDiffersFromLocaleSpecific() {
		return true;
	}
	
	@Override
	public Map<Integer, List<Validator>> getValidators(Integer fieldID, 
			TFieldConfigBean fieldConfigBean, Integer parameterCode,
			Object settingsObject, TWorkItemBean workItemBean) {
		Map<Integer, List<Validator>> validatorsMap = super.getValidators(fieldID,fieldConfigBean,parameterCode,
				settingsObject, workItemBean);
		List<Validator> validatorsList = validatorsMap.get(Integer.valueOf(0));
		if(validatorsList==null){
			validatorsList = new LinkedList<Validator>();
			validatorsMap.put(Integer.valueOf(0), validatorsList);
		}
		TTextBoxSettingsBean textBoxSettingsBean = (TTextBoxSettingsBean)settingsObject;
		if (textBoxSettingsBean!=null){
			DateValidator dateValidator=new DateValidator();
			Integer minOption = textBoxSettingsBean.getMinOption();
			Date minDate = null;
			if (minOption != null && DateOptions.EMPTY != minOption.intValue()){
				if (DateOptions.NOW == minOption.intValue()){
					dateValidator.setMinNow(true);
				}
				else{
					if (DateOptions.DATE == minOption.intValue() && textBoxSettingsBean.getMinDate()!=null){
						minDate = textBoxSettingsBean.getMinDate();
					}
				}
			}
			dateValidator.setMinDate(minDate);
			Integer maxOption = textBoxSettingsBean.getMaxOption();
			Date maxDate = null;
			if (maxOption != null && DateOptions.EMPTY != maxOption.intValue()){
				if (DateOptions.NOW == maxOption.intValue()){
					dateValidator.setMaxNow(true);
				}
				else{
					if (DateOptions.DATE == maxOption.intValue() && textBoxSettingsBean.getMaxDate()!=null){
						maxDate = textBoxSettingsBean.getMaxDate();
					}
				}
			}
			dateValidator.setMaxDate(maxDate);
			validatorsList.add(dateValidator);
		}
		//hierarchical date validator
		if (getHierarchicalBehavior(fieldID, fieldConfigBean, settingsObject)==HIERARCHICAL_BEHAVIOR_OPTIONS.VALIDATE) {
			DateHierarchyValidator dateHierarchyValidator = new DateHierarchyValidator();
			dateHierarchyValidator.setFieldID(fieldID);
			dateHierarchyValidator.setWorkItemID(workItemBean.getObjectID());
			dateHierarchyValidator.setParentID(workItemBean.getSuperiorworkitem());
			validatorsList.add(dateHierarchyValidator);
		}
		CalendarValidator calendarValidator = new CalendarValidator();
		validatorsList.add(calendarValidator);
		return validatorsMap;
	}

	

	
	/**
	 * Creates the matcher object for configuring the matcher
	 * @param fieldID 
	 */
	@Override
	public IMatcherDT getMatcherDT(Integer fieldID) {
		return new DateMatcherDT(fieldID);
	}
	
	/**
	 * Creates the  matcher object for executing the matcher
	 * @param fieldID
	 * @param relation
	 * @param matchValue
	 */
	@Override
	public IMatcherRT getMatcherRT(Integer fieldID, int relation, Object matchValue, 
			MatcherContext matcherContext){
		return new DateMatcherRT(fieldID, relation, matchValue, matcherContext);
	}

	@Override
	public MatcherConverter getMatcherConverter() {
		return DateMatcherConverter.getInstance();
	}

	/**
	 * Loads the IBulkSetter object for configuring the bulk operation
	 * @param fieldID
	 */
	@Override
	public IBulkSetter getBulkSetterDT(Integer fieldID) {
		return new SystemDateBulkSetter(fieldID);
	}
	
	/**
	 * Gets the IFieldChangeConfig object for configuring the field change operation
	 * @param fieldID
	 * @return
	 */
	@Override
	public IActivityConfig getFieldChangeConfig(Integer fieldID) {
		return new DateFieldChangeConfig(fieldID);
	}
	
	/**
	 * Gets the IActivityExecute object for applying the field change operation
	 * @param fieldID
	 * @return
	 */
	@Override
	public IActivityExecute getFieldChangeApply(Integer fieldID) {
		return new DateFieldChangeApply(fieldID);
	}
	
	/**
	 * Get the converter used for field
	 * @param fieldID
	 * @return
	 */
	@Override
	public IValueConverter getFieldValueConverter(Integer fieldID) {
		return new DateSetterConverter(fieldID);
	}
	
	/**
	 * Loads the datasource and value for configuring the field change
	 * @param workflowContext
	 * @param fieldChangeValue
	 * @param parameterCode
	 * @param personBean
	 * @param locale
	 */
	@Override
	public void loadFieldChangeDatasourceAndValue(WorkflowContext workflowContext,
			FieldChangeValue fieldChangeValue, 
			Integer parameterCode, TPersonBean personBean, Locale locale) {
		Integer setter = fieldChangeValue.getSetter();
		if (setter!=null && setter.intValue()==FieldChangeSetters.SET_TO_DATE_FIELD_VALUE) {
			List<Integer> dateFieldIDs = new LinkedList<Integer>();
			dateFieldIDs.add(SystemFields.INTEGER_STARTDATE);
			dateFieldIDs.add(SystemFields.INTEGER_ENDDATE);
			dateFieldIDs.add(SystemFields.INTEGER_TOP_DOWN_START_DATE);
			dateFieldIDs.add(SystemFields.INTEGER_TOP_DOWN_END_DATE);
			dateFieldIDs.add(SystemFields.INTEGER_CREATEDATE);
			dateFieldIDs.addAll(FieldBL.getCustomDateFieldIDs());
			//remove the actual field
			dateFieldIDs.remove(fieldChangeValue.getFieldID());
			Map<Integer, String> dateFieldsMap = FieldRuntimeBL.getLocalizedDefaultFieldLabels(dateFieldIDs, locale);
			List<IntegerStringBean> dateFieldsList = new LinkedList<IntegerStringBean>();
			if (dateFieldsMap!=null) {
				for (Map.Entry<Integer, String> entry : dateFieldsMap.entrySet()) {
					dateFieldsList.add(new IntegerStringBean(entry.getValue(), entry.getKey()));
				}
			}
			fieldChangeValue.setPossibleValues(dateFieldsList);
			/*if (!dateFieldsList.isEmpty()) {
				Integer actualFieldID = null;
				if (fieldChangeValue.getValue()!=null) {
					try {
						actualFieldID = (Integer)fieldChangeValue.getValue();
					} catch(Exception e) {
					}
					if (actualFieldID!=null) {
						for (IntegerStringBean labelBean : dateFieldsList) {
							if (actualFieldID.equals(labelBean.getValue())) {
								return;
							}
						}
					}
				}
				fieldChangeValue.setValue(dateFieldsList.get(0).getValue());
			}*/
		}
	}
	
	/**
	 * Whether this field is a date field by rendering it after exporting to xml
	 * Exporting it to xml should be made in a locale independent way but by creating 
	 * the document using the jasper report it should be specified wheteher a field is date or not
	 * to be interpreted correctly according to the reports's locale  
	 * @return
	 */
	
	/**
	 * Whether the field should appear in the groupable fields list
	 * Typically fields which are typically unique should not be groupable   
	 * @return
	 */
	
	/**
	 * Gets the string value to be stored by lucene
	 * @param value the value of the field
	 * @param workItemBean the lucene value might depend on other fields of the workItem
	 * @return
	 */
	@Override
	public String getLuceneValue(Object value, TWorkItemBean workItemBean) {
		return LuceneUtil.getLuceneDateValue(value);
	}
	
	/**
	 * Whether the field should be stored
	 * @return
	 */
	@Override
	public int getLuceneStored() {
		return LuceneUtil.STORE.NO;
	}
	
	/**
	 * Whether the field should be tokenized
	 * @return
	 */
	@Override
	public int getLuceneTokenized() {
		return LuceneUtil.TOKENIZE.NO;
	}

	/**
	 * Returns the lookup entity type related to the fieldType
	 * @return
	 */
	@Override
	public int getLookupEntityType() {
		return LuceneUtil.LOOKUPENTITYTYPES.DIRECTDATE;
	}
}
