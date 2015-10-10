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


package com.aurel.track.fieldType.runtime.custom.text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.customize.workflow.activity.IActivityConfig;
import com.aurel.track.admin.customize.workflow.activity.IActivityExecute;
import com.aurel.track.admin.customize.workflow.activity.IValueConverter;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TTextBoxSettingsBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.bulkSetters.IBulkSetter;
import com.aurel.track.fieldType.bulkSetters.IntegerBulkSetter;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.fieldChange.apply.IntegerFieldChangeApply;
import com.aurel.track.fieldType.fieldChange.config.IntegerFieldChangeConfig;
import com.aurel.track.fieldType.fieldChange.converter.IntegerSetterConverter;
import com.aurel.track.fieldType.runtime.base.LocalLookupContainer;
import com.aurel.track.fieldType.runtime.matchers.converter.IntegerMatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.converter.MatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.design.IntegerMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.run.IMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.IntegerMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.fieldType.runtime.validators.IntegerValidator;
import com.aurel.track.fieldType.runtime.validators.Validator;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.util.numberFormatter.IntegerNumberFormatUtil;

public class CustomIntegerRT extends CustomTextBoxBaseRT {
	
	/**
	 * Get the specific attribute from TTextBoxSettingsBean for default value
	 */
	@Override
	public Object getSpecificDefaultAttribute(TTextBoxSettingsBean textBoxSettingsBean) {
		return textBoxSettingsBean.getDefaultInteger();
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
		if (value!=null) {
			try {
				Integer integerValue = (Integer)value;
				return IntegerNumberFormatUtil.formatISO(integerValue);
			} catch (Exception e) {
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
			return IntegerNumberFormatUtil.parseISO(isoStrValue.toString());
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
	
	/**
	 * The value type of a textbox for integer 
	 */
	public int getValueType() {
		return ValueType.INTEGER;
	}
	
	@Override
	public Map<Integer, List<Validator>> getValidators(Integer fieldID,
			TFieldConfigBean fieldConfigBean,Integer parameterCode,
			Object settingsObject, TWorkItemBean workItemBean) {
		Map<Integer, List<Validator>> validatorsMap = super.getValidators(fieldID,fieldConfigBean,parameterCode,
				settingsObject, workItemBean); 
		TTextBoxSettingsBean textBoxSettingsBean = (TTextBoxSettingsBean)settingsObject;
		if(textBoxSettingsBean==null){
			return validatorsMap;
		}
		if (parameterCode==null) {
			parameterCode = Integer.valueOf(0);
		}
		List<Validator> validatorsList = new ArrayList<Validator>();
		if(validatorsMap.get(parameterCode)!=null){
			validatorsList=validatorsMap.get(parameterCode);
		}
		IntegerValidator intValidator=new IntegerValidator();
		Integer minValue = textBoxSettingsBean.getMinInteger();
		Integer maxValue = textBoxSettingsBean.getMaxInteger();
		intValidator.setMinValue(minValue);
		intValidator.setMaxValue(maxValue);
		validatorsList.add(intValidator);
		validatorsMap.put(parameterCode, validatorsList);
		return  validatorsMap;
	}
	
	/**
	 * Creates the matcher object for configuring the matcher
	 * @param fieldID 
	 */
	@Override
	public IMatcherDT getMatcherDT(Integer fieldID) {
		return new IntegerMatcherDT(fieldID);
	}
	
	/**
	 * Creates the  matcher object for executing the matcher
	 * @param fieldID
	 * @param relation
	 * @param matchValue
	 */
	@Override
	public IMatcherRT getMatcherRT(Integer fieldID, int relation,
			Object matchValue, MatcherContext matcherContext){
		return new IntegerMatcherRT(fieldID, relation, matchValue, matcherContext);
	}
	
	@Override
	public MatcherConverter getMatcherConverter() {
		return IntegerMatcherConverter.getInstance();
	}
	
	/**
	 * Loads the IBulkSetter object for configuring the bulk operation
	 * @param fieldID
	 */
	@Override
	public IBulkSetter getBulkSetterDT(Integer fieldID) {
		return new IntegerBulkSetter(fieldID);
	}
	
	/**
	 * Gets the IFieldChangeConfig object for configuring the field change operation
	 * @param fieldID
	 * @return
	 */
	public IActivityConfig getFieldChangeConfig(Integer fieldID) {
		return new IntegerFieldChangeConfig(fieldID);
	}
	
	/**
	 * Gets the IActivityExecute object for applying the field change operation
	 * @param fieldID
	 * @return
	 */
	public IActivityExecute getFieldChangeApply(Integer fieldID) {
		return new IntegerFieldChangeApply(fieldID);
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
		return LuceneUtil.LOOKUPENTITYTYPES.DIRECTINTEGER;
	}
}
