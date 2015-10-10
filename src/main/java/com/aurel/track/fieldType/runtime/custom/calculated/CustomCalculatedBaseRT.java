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


package com.aurel.track.fieldType.runtime.custom.calculated;

import java.util.Map;

import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.ICustomFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.OutputFieldTypeRT;
import com.aurel.track.lucene.LuceneUtil;

public class CustomCalculatedBaseRT extends OutputFieldTypeRT implements ICustomFieldTypeRT {

	@Override
	public boolean isCustomSelect() {
		return false;
	}
	
	/**
	 * Calculates the custom attribute value and sets  
	 * the result to the workItem customAttributeValues Map
	 */
	public void loadAttribute(Integer fieldID, Integer parameterCode, 
			TWorkItemBean workItemBean, Map<String, Object> attributeValueMap) {
		//calculate the field depending on any workItem attribute
		//it might go to the database if it needs some extra info regarding the workItem 
		String attribute = null; 		
		//calculate the attribute value...

		//save the attribute on workItem
		workItemBean.setAttribute(fieldID, parameterCode, attribute); 
		//other business logic if needed	
	}
	
	/**
	 * Whether this field is a calculated field or a stored field
	 * If calculated and dependent from other fields then changing 
	 * the dependency should trigger the recalculation of this field
	 * i.e. loadAttribute() should be called also for refreshing a calculated field
	 * (but should not be called by refreshing when it means a loading of a stored 
	 * value from a database)   
	 * @param fieldID
	 * @param parameterCode
	 */
	@Override
	/*public boolean isCalculated(Integer fieldID, Integer parameterCode) {
		return true;
	}*/
	
	public int getValueType() {
		return ValueType.NOSTORE;
	}

	/**!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
	/**the calculated fields will not be searchable*/
	/**!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
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
		return LuceneUtil.LOOKUPENTITYTYPES.NOSTORE;
	}
	/**
	 * Convert a value to string
	 * @param value
	 * @return
	 */
	public String convertToString(Object value){
		return getShowISOValue(null, null, value, null, null, null);
	}

	/**
	 * Convert a string  to object value
	 * @param value
	 * @return
	 */
	public Object convertFromString(String value){
		return parseISOValue(value);
	}

	
}
