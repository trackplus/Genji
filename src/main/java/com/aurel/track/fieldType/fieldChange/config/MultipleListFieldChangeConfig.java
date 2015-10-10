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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.fieldChange.FieldChangeSetters;
import com.aurel.track.fieldType.fieldChange.FieldChangeTemplates;
import com.aurel.track.json.JSONUtility;

/**
 * Configure field change custom select multiple fields
 * @author Tamas
 *
 */
public class MultipleListFieldChangeConfig extends SingleListFieldChangeConfig {
	
	public MultipleListFieldChangeConfig(Integer fieldID, boolean dynamicIcons, boolean isCustom) {
		super(fieldID, dynamicIcons, isCustom);
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
		setters.add(Integer.valueOf(FieldChangeSetters.ADD_ITEMS));
		setters.add(Integer.valueOf(FieldChangeSetters.REMOVE_ITEMS));
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
		case FieldChangeSetters.ADD_ITEMS:
		case FieldChangeSetters.REMOVE_ITEMS:
			return FieldChangeTemplates.MULTIPLE_LIST_TEMPLATE;
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
		Integer[] listValues = (Integer[])value;
		if (listValues!=null) {
			JSONUtility.appendIntegerArrayAsArray(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, listValues);
		}
		appendDatasource(listDataSource, stringBuilder);
	}
	
}
