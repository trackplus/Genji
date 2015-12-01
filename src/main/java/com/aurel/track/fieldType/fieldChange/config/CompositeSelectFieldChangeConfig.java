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


package com.aurel.track.fieldType.fieldChange.config;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.fieldChange.FieldChangeSetters;
import com.aurel.track.fieldType.fieldChange.FieldChangeTemplates;
import com.aurel.track.fieldType.runtime.base.CustomCompositeBaseRT;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.json.JSONUtility;

/**
 * Configure field change for composite select fields 
 * @author Tamas
 *
 */
public class CompositeSelectFieldChangeConfig extends AbstractFieldChangeConfig {
	private static String LINK_CHAR = "_";
	public CompositeSelectFieldChangeConfig(Integer fieldID) {
		super(fieldID);
	}
	
	/**
	 * Gets the name of the jsp fragment which contains 
	 * the control for rendering the bulk value
	 * @return
	 */
	@Override
	public String getValueRendererJsClass() {
		switch (setter) {
		case FieldChangeSetters.SET_TO:
			return FieldChangeTemplates.COMPOSITE_SELECT_TEMPLATE;
		}
		return FieldChangeTemplates.NONE_TEMPLATE;
	}
	
	/**
	 * Builds the name of the client side controls for submit
	 * @param baseName
	 * @param index
	 * @return
	 */
	/*protected String getNameWithMergedKey(String baseName, Integer parameterCode) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(baseName).append(".").append(encodeListSpecificKey(parameterCode));
		return stringBuilder.toString();
	}*/
	
	
	/*protected String encodeListSpecificKey(Integer parameterCode) {
		//the key should start with string otherwise it is not set by struts!!! (bug?)
		StringBuilder stringBuilder = new StringBuilder(getKeyPrefix());
		//stringBuilder.append(LINK_CHAR);
		//stringBuilder.append(listID);
		stringBuilder.append(LINK_CHAR);
		stringBuilder.append(parameterCode);
		return stringBuilder.toString();
	}*/
	
	
	
	/**
	 * Adds the field type specific json content to stringBuilder (without curly brackets)
	 * @param stringBuilder
	 * @param value
	 * @param dataSource
	 * @param personBean
	 * @param locale
	 */
	@Override
	public void addSpecificJsonContent(StringBuilder stringBuilder, Object value,
			Object dataSource, TPersonBean personBean, Locale locale) {
		//Integer fieldID = getActivityType();
		CustomCompositeBaseRT compositeBaseRT = (CustomCompositeBaseRT)FieldTypeManager.getFieldTypeRT(activityType);
		if (compositeBaseRT!=null) {
			SortedMap<Integer, List<ILabelBean>> compositeListDataSource = (SortedMap<Integer, List<ILabelBean>>)dataSource;
			SortedMap<Integer, Integer[]> compositeListValue = (SortedMap<Integer, Integer[]>)value;
			JSONUtility.appendIntegerValue(stringBuilder, "fieldID", activityType);
			JSONUtility.appendFieldName(stringBuilder, "parts").append(": [");
			if (compositeListDataSource!=null) {
				for (Iterator<Integer> itrPart = compositeListDataSource.keySet().iterator(); itrPart.hasNext();) {
					Integer parameterCode = itrPart.next();
					List<ILabelBean> partDataSource = compositeListDataSource.get(parameterCode);
					stringBuilder.append("{");
					/*JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.NAME,
							getNameWithMergedKey(baseN, parameterCode));*/
					//JSONUtility.appendIntegerValue(stringBuilder, "fieldID", fieldID);
					JSONUtility.appendILabelBeanList(stringBuilder, JSONUtility.JSON_FIELDS.DATA_SOURCE, partDataSource);
					Integer[] listValues = null;
					Integer listValue = null;
					if (compositeListValue!=null) {
						listValues = compositeListValue.get(parameterCode);
						if (listValues!=null && listValues.length>0) {
							listValue = listValues[0];
						}
					}
					if (listValue==null) {
						if (partDataSource!=null && !partDataSource.isEmpty()) {
							listValue = partDataSource.get(0).getObjectID();
						}
					}
					if (listValue!=null) {
						JSONUtility.appendIntegerValue(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, listValue, true);
					}
					stringBuilder.append("}");
					if (itrPart.hasNext()) {
						stringBuilder.append(",");
					}
				}
				stringBuilder.append("],");
			}
		}
	}

	
}
