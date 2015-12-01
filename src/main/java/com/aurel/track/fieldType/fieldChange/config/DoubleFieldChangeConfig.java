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
import java.util.List;
import java.util.Locale;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.fieldChange.FieldChangeSetters;
import com.aurel.track.fieldType.fieldChange.FieldChangeTemplates;
import com.aurel.track.json.JSONUtility;

/**
 * Configure field change for double fields
 * @author Tamas
 *
 */
public class DoubleFieldChangeConfig extends AbstractFieldChangeConfig {
	
	public DoubleFieldChangeConfig(Integer fieldID) {
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
		setters.add(Integer.valueOf(FieldChangeSetters.ADD_IF_SET));
		setters.add(Integer.valueOf(FieldChangeSetters.ADD_OR_SET));
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
		case FieldChangeSetters.ADD_IF_SET:		
		case FieldChangeSetters.ADD_OR_SET:
			return FieldChangeTemplates.NUMBER_TEMPLATE;
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
		if (value!=null) {
			JSONUtility.appendDoubleValue(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, (Double)value);
		}
		JSONUtility.appendBooleanValue(stringBuilder, "allowDecimals", true);
	}
    
 
}
