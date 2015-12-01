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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.fieldChange.FieldChangeSetters;
import com.aurel.track.fieldType.fieldChange.FieldChangeTemplates;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.IntegerStringBean;

/**
 * Configure field change for date fields
 * @author Tamas
 *
 */
public class DateFieldChangeConfig extends AbstractFieldChangeConfig {
	
	public DateFieldChangeConfig(Integer fieldID) {
		super(fieldID);
	}
	
	/**
	 * Gets the possible setters for an activity type
	 * @param required
	 * @param withParameter
	 */
	@Override
	public List<Integer> getPossibleSetters(boolean required, boolean withParameter) {
		List <Integer> setters = new ArrayList<Integer>();
		setters.add(Integer.valueOf(FieldChangeSetters.SET_TO));
		setters.add(Integer.valueOf(FieldChangeSetters.MOVE_BY_DAYS));
		setters.add(Integer.valueOf(FieldChangeSetters.SET_TO_DATE_FIELD_VALUE));
		if (!required) {
			setters.add(Integer.valueOf(FieldChangeSetters.SET_NULL));
			setters.add(Integer.valueOf(FieldChangeSetters.SET_REQUIRED));
		}
		if (withParameter) {
			setters.add(Integer.valueOf(FieldChangeSetters.PARAMETER));
		}
		return setters;
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
			return FieldChangeTemplates.DATE_TEMPLATE;
		case FieldChangeSetters.MOVE_BY_DAYS:
			return FieldChangeTemplates.NUMBER_TEMPLATE;
		case FieldChangeSetters.SET_TO_DATE_FIELD_VALUE:
			return FieldChangeTemplates.SELECT_TEMPLATE;	
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
	@Override
	public void addSpecificJsonContent(StringBuilder stringBuilder, Object value,
			Object dataSource, TPersonBean personBean, Locale locale) {
		switch (getSetter()) {
		case FieldChangeSetters.MOVE_BY_DAYS:
			JSONUtility.appendBooleanValue(stringBuilder, "allowDecimals", false);
			if (value!=null) {
				try {
					JSONUtility.appendIntegerValue(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, (Integer)value);
				} catch (Exception e) {
				}
			}
			break;
		case FieldChangeSetters.SET_TO:
			if (value!=null) {
				try {
					JSONUtility.appendDateValue(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, (Date)value);
				} catch (Exception e) {
				}
			}
			break;
		case FieldChangeSetters.SET_TO_DATE_FIELD_VALUE:
			Integer intValue = (Integer)value;
			if (intValue!=null) {
				JSONUtility.appendIntegerValue(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, intValue);
			}
			List<IntegerStringBean> dateFieldsDataSource = (List<IntegerStringBean>)dataSource;
			if (dateFieldsDataSource!=null) {
				JSONUtility.appendIntegerStringBeanList(stringBuilder, JSONUtility.JSON_FIELDS.DATA_SOURCE, dateFieldsDataSource);
			}
		}
	}

}
