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

import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.runtime.base.SelectContext;
import com.aurel.track.item.ItemBL;
import com.aurel.track.json.JSONUtility;

/**
 * Bulk setter for string fields
 * @author Tamas
 *
 */
public class ParentBulkSetter extends AbstractBulkSetter {
	private static final Logger LOGGER = LogManager.getLogger(ParentBulkSetter.class);
	public ParentBulkSetter(Integer fieldID) {
		super(fieldID);
	}
	
	/**
	 * Gets the name of the jsp fragment which contains 
	 * the control for rendering the bulk value
	 * @return
	 */
	public String getSetterValueControlClass() {
		switch (relation) {
		case BulkRelations.SET_TO:
			return BulkValueTemplates.PARENT_BULK_VALUE_TEMPLATE;
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
		if (value!=null) {
			JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, (String)value);
		}		
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.DISABLED, disabled, true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}

	public Object fromDisplayString(Map<String, String> displayStringMap, Locale locale) {
		if (displayStringMap == null) {
			return null;
		}
		switch (getRelation()) {
		case BulkRelations.SET_TO:
			String displayString = displayStringMap.get(getKeyPrefix());
			if (displayString!=null) {
				try {
					return Integer.decode(displayString);
				} catch (NumberFormatException ex) {
					LOGGER.info("Parsing the parentID " + displayString + " failed with ");
				}
				//displayString = StringEscapeUtils.escapeXml(displayString);
			}
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
		Integer parentID = null;
		try {
			parentID = (Integer)value;
		} catch (Exception e) {
			LOGGER.debug("Getting the string value for " + value +  " failed with " + e.getMessage(), e);
		}
		switch (getRelation()) {
		case BulkRelations.SET_TO:
			if (parentID!=null) {
				if (ItemBL.isAscendant(workItemBean.getObjectID(), parentID)) {
					return new ErrorData("itemov.massOperation.err.parentIsDescendant");
				} else {
					workItemBean.setAttribute(fieldID, parameterCode, parentID);
				}
			}
			break;
		case BulkRelations.SET_NULL:
			workItemBean.setAttribute(fieldID, parameterCode, null);
			break;
		}
		//no error
		return null;
	}
	
	/**
	 * The strValue contains the workItemID and the Synopsis
	 * Only the workItemID is needed
	 * @param strValue
	 * @return
	 */
	/*private static Integer getFirstNumber(String strValue) {
		if (strValue==null || "".equals(strValue)) {
			return null;
		}
		StringBuffer strBuff = new StringBuffer();
		char c;
		for (int i = 0; i < strValue.length() ; i++) {
			c = strValue.charAt(i);
			if (Character.isDigit(c)) {
				strBuff.append(c); 
			} else {
				break;
			}
		}
		if (strBuff.length()>0) {
			try {
				return Integer.valueOf(strBuff.toString());
			} catch (Exception e) {
			}
		} 
		return null;
	}*/
}
