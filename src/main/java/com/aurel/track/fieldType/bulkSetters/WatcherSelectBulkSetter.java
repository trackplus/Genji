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

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.GeneralUtils;

/**
 * Bulk setter for custom select fields
 * Differs from the system select by the fact that the value is Integer[] instead of Integer 
 * @author Tamas
 *
 */
public class WatcherSelectBulkSetter extends CustomMultipleSelectBulkSetter {	
	
	public WatcherSelectBulkSetter(Integer fieldID) {
		super(fieldID);	
	}
	
	/**
	 * Gets the name of the jsp fragment which contains 
	 * the control for rendering the bulk value
	 * @return
	 */
	@Override
	public String getSetterValueControlClass() {
		switch (relation) {
		case BulkRelations.SET_TO:
		case BulkRelations.ADD_ITEMS:
		case BulkRelations.REMOVE_ITEMS:
			return BulkValueTemplates.WATCHER_BULK_VALUE_TEMPLATE;
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
		//name of the main panel (to enable/disable)
		JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.NAME, getName(baseName));						 				 	
		List<ILabelBean> dataSourceList = (List<ILabelBean>)dataSource;
		if (dataSourceList!=null) {																								
			//JSONUtility.appendILabelBeanList(stringBuilder, JSONUtility.JSON_FIELDS.DATA_SOURCE, dataSourceList);
			JSONUtility.appendJSONValue(stringBuilder, "datasource", encodeListWithIconCls(dataSourceList));
			JSONUtility.appendBooleanValue(stringBuilder, "useIconCls", true);
			Integer[] selectedValues = (Integer[])value;
			if (selectedValues!=null) {											
				JSONUtility.appendIntegerArrayAsArray(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, selectedValues);				
			}
		}
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.DISABLED, disabled, true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	protected String encodeListWithIconCls(List<ILabelBean> labelBeanList){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(labelBeanList!=null){
			for (Iterator<ILabelBean> iterator = labelBeanList.iterator(); iterator.hasNext();) {
				ILabelBean labelBean = iterator.next();
				sb.append("{");
				JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ID, labelBean.getObjectID().toString());
				JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.LABEL, labelBean.getLabel());
				JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ICONCLS, getIconCls(labelBean), true);
				sb.append("}");
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * Gets the iconCls class if dynamicIcons is false
	 * @param labelBean
	 */
	protected String getIconCls(ILabelBean labelBean) {
		TPersonBean personBean = (TPersonBean)labelBean;
		if (personBean.isGroup()) {
			return PersonBL.GROUP_ICON_CLASS;
		}else{
			return PersonBL.USER_ICON_CLASS;
		}
	}
	
	/**
	 * Transform on value of corresponding type from string
	 */
    @Override
	public Object fromDisplayString(Map<String, String> displayStringMap, Locale locale) {    	
    	if (displayStringMap == null) {
    		return null;
    	}    	
    	switch (getRelation()) {
    	case BulkRelations.SET_TO:
    	case BulkRelations.ADD_ITEMS:
    	case BulkRelations.REMOVE_ITEMS:    		      		
    			String displayStringValue = displayStringMap.get(getKeyPrefix());
				if (displayStringValue!=null) {						
						return GeneralUtils.createIntegerArrFromCollection(
			    				GeneralUtils.createIntegerListFromStringArr(displayStringValue.split(", ")));					
				}    		 			   
    	}
    	return null;
    }
    
}
