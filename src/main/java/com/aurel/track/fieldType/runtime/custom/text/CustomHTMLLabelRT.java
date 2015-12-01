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

import com.aurel.track.beans.TTextBoxSettingsBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.ICustomFieldTypeRT;
import com.aurel.track.lucene.LuceneUtil;

/**
 * Rich text label field
 * @author Tamas Ruff
 *
 */
public class CustomHTMLLabelRT extends CustomTextBoxBaseRT implements ICustomFieldTypeRT {
	
	/*public void loadAttribute(Integer fieldID, Integer parameterCode, 
			TWorkItemBean workItemBean, Map attributeValueMap) {
		//TODO re-think the method signature to allow for to include also fieldConfigID and fieldSettings    
		TFieldConfigBean fieldConfigBean = FieldRuntimeBL.getValidConfig(fieldID, 
				workItemBean.getListTypeID(), workItemBean.getProjectID());
		Map<Integer,TFieldConfigBean> fieldConfigsMap = new HashMap<Integer, TFieldConfigBean>();
		fieldConfigsMap.put(fieldID, fieldConfigBean);
		Map<String, Object> fieldSettings = FieldRuntimeBL.getFieldSettings(fieldConfigsMap);
		String mergedKey = MergeUtil.mergeKey(fieldConfigBean.getObjectID(), parameterCode);
		if (fieldSettings!=null) {
		Object settingsObject = fieldSettings.get(mergedKey);
			if (settingsObject!=null) {
				TTextBoxSettingsBean textBoxSettingsBean = null;
				try {
					textBoxSettingsBean = (TTextBoxSettingsBean)settingsObject;
				} catch(Exception ex) {
					//LOGGER.error("Getting the settings object for CustomTextBaseRT failed with " + ex.getMessage());
				}
				if (textBoxSettingsBean!=null) {
					Object defaultValue = textBoxSettingsBean.getDefaultText();					 
					//set the attribute on workItem
					workItemBean.setAttribute(fieldID, parameterCode, defaultValue);
				}
			}		
		}
	}*/
	
	@Override
	public void saveAttribute(Integer fieldID, Integer parameterCode, 
			TWorkItemBean workItemBean, TWorkItemBean workItemBeanOriginal) {		
	}
	
	/**
	 * Get the specific attribute from TTextBoxSettingsBean for default value
	 */
	@Override
	public Object getSpecificDefaultAttribute(TTextBoxSettingsBean textBoxSettingsBean) {
		return textBoxSettingsBean.getDefaultText();
	}
	
	/**
	 * Whether the value of this field can be changed
	 * @return
	 */
	@Override
	public boolean isReadOnly() {
		return true;
	}
	
	/**
	 * Whether this field is a custom label field
	 * @param fieldID
	 * @param parameterCode
	 */
	@Override
	public boolean isComputed(Integer fieldID, Integer parameterCode) {
		return true;
	}	
	
	/**
	 * Whether the value have been modified
	 * Used by assembling the history and mail
	 * @param newValue
	 * @param oldValue
	 * @return
	 */
	@Override
	public boolean valueModified(Object newValue, Object oldValue) {
		//the label newer changes during editing a workItem
		return false;
	}
	
	/**
	 * The value type of a textbox for long text 
	 */
	@Override
	public int getValueType() {		
		return ValueType.NOSTORE;
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
	 * Whether the field might be matched in for an excel column
	 * @return
	 */
	@Override
	public boolean mightMatchExcelColumn() {
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
		return LuceneUtil.LOOKUPENTITYTYPES.NOSTORE;
	}
	
}
