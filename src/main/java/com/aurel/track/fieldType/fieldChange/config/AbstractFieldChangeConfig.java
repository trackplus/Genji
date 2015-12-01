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

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.aurel.track.admin.customize.workflow.activity.AbstractActivity;
import com.aurel.track.admin.customize.workflow.activity.IActivityConfig;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.fieldChange.FieldChangeSetters;
import com.aurel.track.fieldType.fieldChange.FieldChangeValue;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.workflow.execute.WorkflowContext;
import com.aurel.track.json.JSONUtility;

/**
 * Base class for configuring a field change
 * @author Tamas Ruff
 *
 */
public abstract class AbstractFieldChangeConfig extends AbstractActivity implements IActivityConfig {
		
	public AbstractFieldChangeConfig(Integer fieldID) {
		super(fieldID);
	}
	
	/**
	 * Whether the activity refers to a field (system or custom) or to an other hardcoded activity
	 * @return
	 */
	@Override
	public boolean hasSetter() {
		return true;
	}
	
	/**
	 * Whether the activity has value parameters
	 * (typically the setter is parameterized but some activity types do not have setter but can have parameters in the value part like "send e-mail")
	 * @param value
	 * @return
	 */
	@Override
	public boolean hasValueParams(String value) {
		return false;
	}
	
	/**
	 * Gets the possible setters for an activity type
	 * @param required
	 * @param withParameter
	 */
	@Override
	public List<Integer> getPossibleSetters(boolean required, boolean withParameter) {
		List<Integer> setters = new LinkedList<Integer>();
		setters.add(Integer.valueOf(FieldChangeSetters.SET_TO));
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
	 * Builds the name of the client side controls for submit
	 * @param baseName
	 * @return
	 */
	
	/**
	 * The prefix for the map keys
	 * @return
	 */
	
	/**
	 * Loads the datasource and value for configuring the activity
	 * @param workflowContext
	 * @param fieldChangeValue
	 * @param parameterCode
	 * @param personBean
	 * @param locale
	 * @param withParameter whether to include $PARAMETER in datasource
	 */
	@Override
	public void loadDatasourceAndValue(WorkflowContext workflowContext,
			FieldChangeValue fieldChangeValue, Integer parameterCode, TPersonBean personBean, Locale locale, boolean withParameter) {
		IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(activityType);
		if (fieldTypeRT!=null) {
			fieldTypeRT.loadFieldChangeDatasourceAndValue(workflowContext, fieldChangeValue, null, personBean, locale);
		}
	}
	
	/**
	 * The JSON configuration object for configuring the js control(s) containing the value
	 * Called by adding/editing an activity by configuring the workflow or 
	 * setting the parameters by assigning a paremeterized workflow into a context
	 * @param configuredValue: the value set by configuring the workflow (add/edit activity)
	 * @param assignedValue: if value contains parameter the assigned value in context
	 * @param dataSource: defined only for lists (list for global lists, map for context dependent lists)
	 * @param withParameter whether to include $PARAMETER in datasource
     * @param personBean
	 * @param locale
	 * @return
	 */
	@Override
	public String getValueRendererJsonConfig(Object configuredValue, Object assignedValue, Object dataSource,
			boolean withParameter, TPersonBean personBean, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder("{");
		Object value = null;
		if (withParameter) {
			value = configuredValue;
		} else {
			value = assignedValue;
		}
		addSpecificJsonContent(stringBuilder, value, dataSource, personBean, locale);
		//this is only a dummy name not used (the name is generated on the client side by assigning parameters).
		//it is used to assure no comma is before "}"
		JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.NAME, JSONUtility.JSON_FIELDS.VALUE+getActivityType(), true);
		stringBuilder.append("}");
		return stringBuilder.toString();
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
		if (value!=null) {
			JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, (String)value);
		}
	}
	

}
