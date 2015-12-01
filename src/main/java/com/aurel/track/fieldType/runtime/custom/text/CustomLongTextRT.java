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


package com.aurel.track.fieldType.runtime.custom.text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;

import com.aurel.track.admin.customize.workflow.activity.IActivityConfig;
import com.aurel.track.admin.customize.workflow.activity.IActivityExecute;
import com.aurel.track.admin.customize.workflow.activity.IValueConverter;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.bulkSetters.IBulkSetter;
import com.aurel.track.fieldType.bulkSetters.TextAreaBulkSetter;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.fieldChange.apply.TextAreaFieldChangeApply;
import com.aurel.track.fieldType.fieldChange.config.TextAreaFieldChangeConfig;
import com.aurel.track.fieldType.fieldChange.converter.StringSetterConverter;
import com.aurel.track.fieldType.runtime.base.LocalLookupContainer;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.validators.StringValidator;
import com.aurel.track.fieldType.runtime.validators.Validator;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.util.emailHandling.Text2HTML;

/**
 * Long text field
 * @author Tamas Ruff
 *
 */
public class CustomLongTextRT extends CustomTextBaseRT {
	
	/**
	 * The value type of a textbox for long text 
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
		stringValidator.setMaxLength(ApplicationBean.getInstance().getCommentMaxLength());
		validatorsList.add(stringValidator);
		return validatorsMap;
	}
	
	/**
	 * Loads the IBulkSetter object for configuring the bulk operation
	 * @param fieldID
	 */
	@Override
	public IBulkSetter getBulkSetterDT(Integer fieldID) {
		return new TextAreaBulkSetter(fieldID);
	}
	
	/**
	 * Gets the IFieldChangeConfig object for configuring the field change operation
	 * @param fieldID
	 * @return
	 */
	@Override
	public IActivityConfig getFieldChangeConfig(Integer fieldID) {
		return new TextAreaFieldChangeConfig(fieldID);
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
	 * Get the converter used for field
	 * @param fieldID
	 * @return
	 */
	@Override
	public IValueConverter getFieldValueConverter(Integer fieldID) {
		return new StringSetterConverter(fieldID);
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
		String strValue="";
		try {
			strValue = (String)value;
			String escapedHTML = StringEscapeUtils.escapeHtml(strValue);
			return Text2HTML.addHTMLBreaks(escapedHTML);
		} catch(Exception e) {
			return strValue;
		}
	}
	
	@Override
	public String getShowValue(Object value, WorkItemContext workItemContext,Integer fieldID){
		String strValue="";
		try {
			strValue = (String)value;
			String escapedHTML = StringEscapeUtils.escapeHtml(strValue);
			return Text2HTML.addHTMLBreaks(escapedHTML);
		} catch(Exception e) {
			return strValue;
		}
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
			return value.toString();
		}
		return "";
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
		return LuceneUtil.TOKENIZE.YES;
	}
	
	/**
	 * Returns the lookup entity type related to the fieldType
	 * @return
	 */
	@Override
	public int getLookupEntityType() {
		return LuceneUtil.LOOKUPENTITYTYPES.DIRECTTEXT;
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
	
	/**
	 * Whether the field should appear in the groupable fields list
	 * Typically fields which are typically unique should not be groupable   
	 * @return
	 */
	@Override
	public boolean isGroupable() {
		return false;
	}
}
