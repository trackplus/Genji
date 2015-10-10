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


package com.aurel.track.fieldType.runtime.system.text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.workflow.activity.IActivityConfig;
import com.aurel.track.admin.customize.workflow.activity.IActivityExecute;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.bulkSetters.IBulkSetter;
import com.aurel.track.fieldType.bulkSetters.RichTextBulkSetter;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.fieldChange.apply.TextAreaFieldChangeApply;
import com.aurel.track.fieldType.fieldChange.config.RichTextFieldChangeConfig;
import com.aurel.track.fieldType.runtime.base.LocalLookupContainer;
import com.aurel.track.fieldType.runtime.validators.StringValidator;
import com.aurel.track.fieldType.runtime.validators.Validator;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.util.TagReplacer;
import com.aurel.track.util.emailHandling.Html2Text;

/**
 * Runtime class for Description and Comment
 * @author Tamas Ruff
 *
 */
public abstract class SystemRichTextBaseRT extends SystemTextBaseRT {
		
	private static final Logger LOGGER = LogManager.getLogger(SystemRichTextBaseRT.class);
	
	/** 
	 * Defines the data type of the field attribute
	 * Used by saving field attributes for custom fields
	 * and saving explicit history for both system and custom fields
	 * Should be a @ValueType constant 
	 * @return
	 */
	@Override
	public int getValueType() {
		return ValueType.LONGTEXT;
	}
	
	@Override
	public Map<Integer, List<Validator>> getValidators(Integer fieldID, 
			TFieldConfigBean fieldConfigBean,Integer parameterCode,
			Object settingsObject, TWorkItemBean workItemBean) {
		Map<Integer, List<Validator>> validatorsMap = super.getValidators(fieldID,fieldConfigBean,parameterCode,
				settingsObject, workItemBean); 		
		List<Validator> validatorsList = new ArrayList<Validator>();
		if(validatorsMap.get(Integer.valueOf(0))!=null){
			validatorsList=validatorsMap.get(Integer.valueOf(0));
		}
		validatorsMap.put(Integer.valueOf(0), validatorsList);
		StringValidator stringValidator=new StringValidator();
		stringValidator.setMaxLength(getDatabaseFieldLength());
		validatorsList.add(stringValidator);
		return validatorsMap;
	}
	
	public abstract int getDatabaseFieldLength();
	
	/**
	 * Loads the IBulkSetter object for configuring the bulk operation
	 * @param fieldID
	 */
	@Override
	public IBulkSetter getBulkSetterDT(Integer fieldID) {
		return new RichTextBulkSetter(fieldID);
	}
	
	/**
	 * Gets the IFieldChangeConfig object for configuring the field change operation
	 * @param fieldID
	 * @return
	 */
	@Override
	public IActivityConfig getFieldChangeConfig(Integer fieldID) {
		return new RichTextFieldChangeConfig(fieldID);
	}
	
	/**
	 * Gets the IActivityExecute object for applying the field change operation
	 * @param fieldID
	 * @return
	 */
	@Override
	public IActivityExecute getFieldChangeApply(Integer fieldID) {
		return new TextAreaFieldChangeApply(fieldID);
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
		try {
			TagReplacer replacer=new TagReplacer(Locale.getDefault());
			replacer.setContextPath(Constants.BaseURL);
			return Html2Text.getCustomInstance().convert(replacer.formatDescription((String)value));
		} catch (Exception e) {
			LOGGER.info("Converting to ISO text failed with " + e);		
			LOGGER.debug("The original text is: " + (value==null ? "null" : value.toString()));
			return (String)value;
		}
	}
	
	
	@Override
	public boolean isoDiffersFromLocaleSpecific() {
		return true;
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
		//works only for basic types: it should be overrided 
		//especially in composite fields, because each part should be compared
		String newStrValue = (String)newValue;
		String oldStrValue = (String)oldValue;
		if (newStrValue==null) {
			newStrValue="";
		}
		if (oldStrValue==null) {
			oldStrValue="";
		}
		newStrValue = newStrValue.replaceAll("(\r\n)", "\n");
		oldStrValue = oldStrValue.replaceAll("(\r\n)", "\n");
		return !newStrValue.equals(oldStrValue);
	}

	/**
	 * Whether the field should appear in the groupable fields list
	 * Typically fields which are typically unique should not be groupable   
	 * @return
	 */
	@Override
	public boolean isGroupable() {
		return false;
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
	 * Gets the string value to be stored by lucene
	 * @param value the value of the field
	 * @param workItemBean the lucene value might depend on other fields of the workItem
	 * @return
	 */
	@Override
	public String getLuceneValue(Object value, TWorkItemBean workItemBean) {
		try {
			return Html2Text.getNewInstance().convert((String)value);
		} catch (Exception e) {
			LOGGER.info("Converting to plain text failed with " + e);
			LOGGER.debug("The original text is: " + (value==null ? "null" : value.toString()));
			return (String)value;
		}
	}

	/**
	 * Whether the field will be used in lucene highlighter
	 */
	@Override
	public boolean isHighlightedField() {
		return true;
	}
	
	/**
	 * Whether this field is a long text because then the read only
	 * rendering (on the report overview, printItem, etc.)
	 * should be made different as the rest of the "short" fields
	 * This can be the case for Description, Comment and longtext custom fields  
	 * @return
	 */
	@Override
	public boolean isLong() {
		return true;
	}
}
