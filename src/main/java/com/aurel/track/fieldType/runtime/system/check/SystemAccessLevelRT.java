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


package com.aurel.track.fieldType.runtime.system.check;

import java.util.Locale;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.workflow.activity.IActivityConfig;
import com.aurel.track.admin.customize.workflow.activity.IActivityExecute;
import com.aurel.track.admin.customize.workflow.activity.IValueConverter;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.bulkSetters.AccessLevelBulkSetter;
import com.aurel.track.fieldType.bulkSetters.IBulkSetter;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.fieldChange.apply.AccessLevelFieldChangeApply;
import com.aurel.track.fieldType.fieldChange.config.AccessLevelFieldChangeConfig;
import com.aurel.track.fieldType.fieldChange.converter.BooleanSetterConverter;
import com.aurel.track.fieldType.fieldChange.converter.IntegerSetterConverter;
import com.aurel.track.fieldType.runtime.base.LocalLookupContainer;
import com.aurel.track.fieldType.runtime.base.SystemInputBaseRT;
import com.aurel.track.fieldType.runtime.matchers.converter.BooleanMatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.converter.MatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.design.IntegerBasedBooleanMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.run.IMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.IntegerBasedBooleanMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.resources.LocalizeUtil;

public class SystemAccessLevelRT extends SystemInputBaseRT {
	
	private static final Logger LOGGER = LogManager.getLogger(SystemAccessLevelRT.class);
	
	/** 
	 * Defines the data type of the field attribute
	 * Used by saving field attributes for custom fields
	 * and saving explicit history for both system and custom fields
	 * Should be a @ValueType constant
	 * Here not really meaningful because this field will be never historized 
	 * @return
	 */
	public int getValueType() {
		return ValueType.INTEGER;
	}
	
	/**
	 * Whether the value have been modified
	 * The old value is loaded from the database, so it has a boolean value
	 * but the new value is set as a result of a submit so it has a string value ("true" or "false") 
	 * @param newValue
	 * @param oldValue
	 * @return
	 */
	@Override
	public boolean valueModified(Object newValue, Object oldValue) {
		if (newValue==null) {
			newValue=Integer.valueOf(TWorkItemBean.ACCESS_LEVEL_PUBLIC);
		}
		if (oldValue==null) {
			oldValue=Integer.valueOf(TWorkItemBean.ACCESS_LEVEL_PUBLIC);
		}
		return !newValue.equals(oldValue);
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
	public String getShowValue(Integer fieldID, Integer parameterCode, Object value, 
			Integer workItemID, LocalLookupContainer localLookupContainer, Locale locale) {
		return getShowValue(value, locale);
	}
	
	/**
	 * Get the value to be shown from the database and then try to localize  
	 * @param value
	 * @param locale
	 * @return
	 */
	@Override
	public String getShowValue(Object value, Locale locale) {
		String keySuffix = BooleanFields.FALSE_VALUE;
		if (value==null) {
			value=TWorkItemBean.ACCESS_LEVEL_PUBLIC;
		}
		Integer intValue = null;
		try {
			intValue = (Integer)value;
		} catch (Exception e) {
			LOGGER.warn("Casting the value type " + value.getClass().getName() +
				" to Integer failed with " + e.getMessage(), e);
		}
		if (intValue!=null && TWorkItemBean.ACCESS_LEVEL_PRIVATE.equals(intValue)) {
			keySuffix=BooleanFields.TRUE_VALUE;
		}
		String localizedLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("common.boolean." + keySuffix, locale);			
		if (localizedLabel != null && localizedLabel.length() > 0) {
			return localizedLabel;
		}
		return keySuffix;
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
	public String getShowISOValue(Integer fieldID, Integer parameterCode, Object value, 
			Integer workItemID, LocalLookupContainer localLookupContainer, Locale locale) {
		if (value==null) {
			value=TWorkItemBean.ACCESS_LEVEL_PUBLIC;
		}
		Integer intValue = null;
		try {
			intValue = (Integer)value;
		} catch (Exception e) {
			LOGGER.error("Casting the value type " + value.getClass().getName() +
				" to Integer failed with " + e.getMessage(), e);
		}
		if (intValue!=null && TWorkItemBean.ACCESS_LEVEL_PRIVATE.equals(intValue)) {
			return new Boolean(true).toString();
		} else {
			return new Boolean(false).toString();
		}
	}
	
	/**
	 * Parses the string value into the corresponding object value
	 * @param isoStrValue
	 * @return
	 */
	@Override
	public Object parseISOValue(Object isoStrValue) {
		if (isoStrValue==null) {
			return TWorkItemBean.ACCESS_LEVEL_PUBLIC;
		} else {
			Boolean accessLevel = new Boolean(isoStrValue.toString());
			if (accessLevel.booleanValue()) {
				return TWorkItemBean.ACCESS_LEVEL_PRIVATE;
			}
			return TWorkItemBean.ACCESS_LEVEL_PUBLIC;
		}
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
	
	/**
	 * Creates the matcher object for configuring the matcher
	 * @param fieldID 
	 */
	@Override
	public IMatcherDT getMatcherDT(Integer fieldID) {
		return new IntegerBasedBooleanMatcherDT(fieldID);
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
		return new IntegerBasedBooleanMatcherRT(fieldID, relation, matchValue, matcherContext);
	}
	
	@Override
	public MatcherConverter getMatcherConverter() {
		return BooleanMatcherConverter.getInstance();
	}

	/**
	 * Loads the IBulkSetter object for configuring the bulk operation
	 * @param fieldID
	 */
	@Override
	public IBulkSetter getBulkSetterDT(Integer fieldID) {
		return new AccessLevelBulkSetter(fieldID);
	}
	
	/**
	 * Gets the IFieldChangeConfig object for configuring the field change operation
	 * @param fieldID
	 * @return
	 */
	public IActivityConfig getFieldChangeConfig(Integer fieldID) {
		return new AccessLevelFieldChangeConfig(fieldID);
	}
	
	/**
	 * Gets the IActivityExecute object for applying the field change operation
	 * @param fieldID
	 * @return
	 */
	public IActivityExecute getFieldChangeApply(Integer fieldID) {
		return new AccessLevelFieldChangeApply(fieldID);
	}
	
	/**
	 * Get the converter used for field
	 * @param fieldID
	 * @return
	 */
	public IValueConverter getFieldValueConverter(Integer fieldID) {
		return new IntegerSetterConverter(fieldID);
	}
	
	/**
	 * Whether the field can be rendered on a form
	 * @return
	 */
	@Override
	public boolean mightAppearOnForm() {
		return false;
	}
	
	/**
	 * Whether the field might be matched in for an excel column
	 * @return
	 */
	public boolean mightMatchExcelColumn() {
		return false;
	}
	
	/**
	 * Gets the string value to be stored by lucene
	 * @param value the value of the field
	 * @param workItemBean the lucene value might depend on other fields of the workItem
	 * @return
	 */
	public String getLuceneValue(Object value, TWorkItemBean workItemBean) {
		String luceneValue = LuceneUtil.BOOLEAN_NO;
		if (value==null) {
			value=new Integer(0);
		}
		Integer intValue = null;
		try {
			intValue = (Integer)value;
		} catch (Exception e) {
			LOGGER.error("Converting the value " + value +  "of class " + value.getClass().getName() + " to Integer failed with " + e.getMessage(), e);
		}
		if (intValue!=null && intValue.intValue()==1) {
			luceneValue=LuceneUtil.BOOLEAN_YES;
		}
		return luceneValue;
	}
	
	/**
	 * Whether the field should be stored
	 * @return
	 */
	public int getLuceneStored() {
		return LuceneUtil.STORE.NO;
	}
	
	/**
	 * Whether the field should be tokenized
	 * @return
	 */
	public int getLuceneTokenized() {
		return LuceneUtil.TOKENIZE.NO;
	}
	
	/**
	 * Returns the lookup entity type related to the fieldType
	 * @return
	 */
	public int getLookupEntityType() {
		return LuceneUtil.LOOKUPENTITYTYPES.DIRECTBOOLEAN;
	}
}
