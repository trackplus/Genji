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


package com.aurel.track.fieldType.fieldChange.config;

import java.util.List;
import java.util.Locale;

import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.fieldChange.FieldChangeSetters;
import com.aurel.track.fieldType.fieldChange.FieldChangeTemplates;
import com.aurel.track.fieldType.runtime.callbackInterfaces.ILookup;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.json.JSONUtility;

/**
 * Configure field change for custom select fields
 * Differs from the system select by the fact that the value is Integer[] instead of Integer 
 * @author Tamas
 *
 */
public class SingleListFieldChangeConfig extends AbstractFieldChangeConfig {
	protected boolean dynamicIcons;
	protected boolean isCustom;
	public SingleListFieldChangeConfig(Integer fieldID, boolean dynamicIcons, boolean isCustom) {
		super(fieldID);
		this.dynamicIcons = dynamicIcons;
		this.isCustom = isCustom;
	}
	
	/**
	 * Gets the name of the jsp fragment which contains 
	 * the control for rendering the bulk value
	 * @return
	 */
	public String getValueRendererJsClass() {
		switch (setter) {
		case FieldChangeSetters.SET_TO:
			return FieldChangeTemplates.SINGLE_LIST_TEMPLATE;
		}
		return FieldChangeTemplates.NONE_TEMPLATE;
	}
	
	/**
	 * Adds the field type specific json content to stringBuilder (without curly brackets)
	 * @param stringBuilder
	 * @param value
	 * @param dataSource
	 * @param personBean
	 * @param locale
	 */
	public void addSpecificJsonContent(StringBuilder stringBuilder, Object value,
			Object dataSource, TPersonBean personBean, Locale locale) {
		List<ILabelBean> listDataSource = (List<ILabelBean>)dataSource;
		if (value!=null) {
			Integer listValue = null;
			if (isCustom) {
				Integer[] valueArr = (Integer[])value;
				if (valueArr!=null && valueArr.length>0) {
					listValue = valueArr[0];
				}
			} else {
				listValue = (Integer)value;
			}
			if (listValue==null) {
				//preselect the list with the first entry
				if (listDataSource!=null && !listDataSource.isEmpty()) {
					listValue = listDataSource.get(0).getObjectID();
				}
			}
			if (listValue!=null) {
				JSONUtility.appendIntegerValue(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, listValue);
			}
		}
		appendDatasource(listDataSource, stringBuilder);
	}
	
	protected void appendDatasource(List<ILabelBean> listDataSource, StringBuilder stringBuilder) {
		if (FieldBL.isCustomSelect(activityType) && !FieldBL.isCustomPicker(activityType)) {
			//do not add icons to custom selects because they might not be set and then the empty icons are ugly by rendering
			JSONUtility.appendILabelBeanList(stringBuilder, JSONUtility.JSON_FIELDS.DATA, listDataSource);
		} else {
			JSONUtility.appendILabelBeanListWithIcons(stringBuilder, activityType, (ILookup)FieldTypeManager.getFieldTypeRT(activityType),
				JSONUtility.JSON_FIELDS.DATA, listDataSource, false);
		}
	}
	
	

}
