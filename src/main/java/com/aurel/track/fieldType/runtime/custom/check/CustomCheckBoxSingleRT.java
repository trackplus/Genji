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


package com.aurel.track.fieldType.runtime.custom.check;

import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.workflow.activity.IActivityConfig;
import com.aurel.track.admin.customize.workflow.activity.IActivityExecute;
import com.aurel.track.admin.customize.workflow.activity.IValueConverter;
import com.aurel.track.beans.TAttributeValueBean;
import com.aurel.track.beans.TTextBoxSettingsBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.bulkSetters.BooleanBulkSetter;
import com.aurel.track.fieldType.bulkSetters.IBulkSetter;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.fieldChange.apply.BooleanFieldChangeApply;
import com.aurel.track.fieldType.fieldChange.config.BooleanFieldChangeConfig;
import com.aurel.track.fieldType.fieldChange.converter.BooleanSetterConverter;
import com.aurel.track.fieldType.runtime.base.LocalLookupContainer;
import com.aurel.track.fieldType.runtime.bl.AttributeValueBL;
import com.aurel.track.fieldType.runtime.custom.text.CustomTextBoxBaseRT;
import com.aurel.track.fieldType.runtime.matchers.converter.BooleanMatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.converter.MatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.design.BooleanMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.run.BooleanMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.IMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.EqualUtils;

public class CustomCheckBoxSingleRT extends CustomTextBoxBaseRT {
	private static final Logger LOGGER = LogManager.getLogger(CustomCheckBoxSingleRT.class);
	
	/**
	 * Saves the custom attribute value(s) from the 
	 * workItem's customAttributeValues Map to the database 
	 * Overrides the method from the CustomTextBoxBaseRT because 
	 * it saves the attributeValueBean value even when is should be null 
	 * in this case false.
	 * Thats because only the fields with non-null values (present fields) 
	 * will be shown in the mails
	 * @param fieldID
	 * @param parameterCode neglected for single custom fields
	 * @param workItemBean
	 * @param workItemBeanOriginal
	 */
	@Override
	public void saveAttribute(Integer fieldID, Integer parameterCode, 
			TWorkItemBean workItemBean, TWorkItemBean workItemBeanOriginal) {
		//get attribute from workItem
		Object attributeNew = workItemBean.getAttribute(fieldID, parameterCode);
		Object attributeOld =null;
		if(workItemBeanOriginal!=null){
			attributeOld=workItemBeanOriginal.getAttribute(fieldID, parameterCode);
		}
		if (attributeNew!=null && attributeOld!=null && attributeNew.equals(attributeOld)) {
			return;
		}
		TAttributeValueBean tAttributeValueBean=AttributeValueBL.loadBeanByFieldAndWorkItemAndParameter(fieldID, workItemBean.getObjectID(), parameterCode);
		if(tAttributeValueBean==null){
			tAttributeValueBean=new TAttributeValueBean();
		}
		tAttributeValueBean.setField(fieldID);
		tAttributeValueBean.setParameterCode(parameterCode);
		setSpecificAttribute(tAttributeValueBean, attributeNew);
		tAttributeValueBean.setWorkItem(workItemBean.getObjectID());
		tAttributeValueBean.setParameterCode(parameterCode);
		tAttributeValueBean.setValidValue(Integer.valueOf(getValueType()));
		tAttributeValueBean.setLastEdit(new Date());
		if (attributeNew==null) {
			//if it was null we still save BooleanFields.FALSE_VALUE
			//but then set the new Boolean(false) value back in workItemBean to notice the change
			//from null (previously not present on the screen) to N (not set but at least present) by saving the history
			workItemBean.setAttribute(fieldID, parameterCode, Boolean.FALSE);
		}
		AttributeValueBL.save(tAttributeValueBean);
	}
	
	/**
	 * Set the specific attribute on the TAttributeValueBean
	 */
	public void setSpecificAttribute(TAttributeValueBean tAttributeValueBean, Object attribute) {
		Boolean booleanAttribute = null;
		try {
			booleanAttribute = (Boolean)attribute;
		}	
		catch (Exception e) {
			LOGGER.error("Wrong attribute type " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (booleanAttribute!=null && booleanAttribute.booleanValue()==true) {
			tAttributeValueBean.setCharacterValue(BooleanFields.TRUE_VALUE);
		} else {
			tAttributeValueBean.setCharacterValue(BooleanFields.FALSE_VALUE);
		}
	}
	
	/**
	 * Get the specific attribute from TTextBoxSettingsBean for default value
	 */
	@Override
	public Object getSpecificDefaultAttribute(TTextBoxSettingsBean textBoxSettingsBean) {
		if (BooleanFields.TRUE_VALUE.equals(textBoxSettingsBean.getDefaultChar())) {
			return new Boolean(true);
		}
		if (BooleanFields.FALSE_VALUE.equals(textBoxSettingsBean.getDefaultChar())) {
			return new Boolean(false);
		}
		return null;
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
		Boolean bNew = null;
		if (newValue!=null) {
			try {
				bNew = (Boolean) newValue;
			} catch (Exception e) {
				LOGGER.warn("Converting the new value of type " + 
						newValue.getClass().getName() +  
						" to Boolean failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		Boolean bOld = null;
		if (oldValue!=null) {
			try {
				bOld = (Boolean) oldValue;
			} catch (Exception e) {
				LOGGER.warn("Converting the old value of type " + 
						newValue.getClass().getName() +  
						" to Boolean failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		//especially by grouping to make difference between the null and No
		return EqualUtils.notEqual(bNew, bOld);
	}
	
	/**
	 * The value of the checkbox is stored as a string of length 1 ("Y" or "N") 
	 */
	@Override
	public int getValueType() {
		return ValueType.BOOLEAN;
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
	 * Get the localized string to be shown
	 * The value can be of type Boolean or String depending on the 
	 * previous operation: loaded from the database or set through submit  
	 * @param value
	 * @param locale
	 * @return
	 */
	@Override
	public String getShowValue(Object value, Locale locale) {
		if (value!=null) {
			String keySuffix = BooleanFields.FALSE_VALUE;
			//if the attribute value of the workItem (or workItemOld) was loaded from the database 
			//than it is set to a boolean value (see getSpecificAttribute())
			//So try converting it to a Boolean
			Boolean boolValue = null;
			try {
				boolValue = (Boolean)value;
			} catch (Exception e) {
				LOGGER.debug("Casting the value type " + value.getClass().getName() +
						" to Boolean failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}			
			if (boolValue!=null && boolValue.booleanValue()==true) {
				keySuffix=BooleanFields.TRUE_VALUE;
			}		
			String localizedLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("common.boolean." + keySuffix, locale);
			if (localizedLabel != null && localizedLabel.length() > 0) {
				return localizedLabel;
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
			//if the attribute value of the workItem (or workItemOld) was loaded from the database 
			//than it is set to a boolean value (see getSpecificAttribute())
			//So try converting it to a Boolean
			Boolean boolValue = null;
			try {
				boolValue = (Boolean)value;
			} catch (Exception e) {
				LOGGER.debug("Casting the value type " + value.getClass().getName() +
						" to Boolean failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			if (boolValue!=null) {
				return boolValue.toString();
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
		if (isoStrValue==null) {
			return new Boolean(false);
		} else {
			Boolean boolValue = null;
			try {
				boolValue = new Boolean(isoStrValue.toString());
			} catch (Exception e) {
				LOGGER.debug("Casting the string " + isoStrValue +
						" to Boolean failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			return boolValue;
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
		return new BooleanMatcherDT(fieldID);
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
		return new BooleanMatcherRT(fieldID, relation, matchValue, matcherContext);
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
		return new BooleanBulkSetter(fieldID);
	}
	
	/**
	 * Gets the IFieldChangeConfig object for configuring the field change operation
	 * @param fieldID
	 * @return
	 */
	@Override
	public IActivityConfig getFieldChangeConfig(Integer fieldID) {
		return new BooleanFieldChangeConfig(fieldID);
	}
	
	/**
	 * Gets the IActivityExecute object for applying the field change operation
	 * @param fieldID
	 * @return
	 */
	@Override
	public IActivityExecute getFieldChangeApply(Integer fieldID) {
		return new BooleanFieldChangeApply(fieldID);
	}
	
	/**
	 * Get the converter used for field
	 * @param fieldID
	 * @return
	 */
	@Override
	public IValueConverter getFieldValueConverter(Integer fieldID) {
		return new BooleanSetterConverter(fieldID);
	}
	
	/**
	 * Gets the string value to be stored by lucene
	 * @param value the value of the field
	 * @param workItemBean the lucene value might depend on other fields of the workItem
	 * @return
	 */
	@Override
	public String getLuceneValue(Object value, TWorkItemBean workItemBean) {
		if (value!=null) {
			try {
				//if the attribute value of the workItem (or workItemOld) was loaded from the database 
				//than it is set to a boolean value (see getSpecificAttribute())
				//So try converting it to a Boolean
				Boolean boolValue = (Boolean)value;
				if (boolValue.booleanValue()==true) {
					return  LuceneUtil.BOOLEAN_YES;
				}
			} catch (Exception e) {
				LOGGER.debug("Converting the value " + value +  "of class " + value.getClass().getName() + " to Boolean failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return LuceneUtil.BOOLEAN_NO;
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
		return LuceneUtil.LOOKUPENTITYTYPES.DIRECTBOOLEAN;
	}
}
