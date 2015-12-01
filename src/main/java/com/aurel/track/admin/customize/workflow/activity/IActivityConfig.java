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


package com.aurel.track.admin.customize.workflow.activity;

import java.util.List;
import java.util.Locale;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.fieldChange.FieldChangeValue;
import com.aurel.track.item.workflow.execute.WorkflowContext;


/**
 * Base interface for configuring a field change
 * @author Tamas Ruff
 */
public interface IActivityConfig extends IActivity {	
	
	/**
	 * Gets the possible setters for an activity type
	 * @param required
	 * @param withParameter
	 */
	List<Integer> getPossibleSetters(boolean required, boolean withParameter);
	
	/**
	 * Whether the activity has value parameters
	 * (typically the setter is parameterized but some activity types do not have setter but can have parameters in the value part like "send e-mail")
	 * @param value
	 * @return
	 */
	boolean hasValueParams(String value);
	
	/**
	 * Loads the datasource and value for configuring the activity
	 * @param workflowContext
	 * @param fieldChangeValue
	 * @param parameterCode
	 * @param personBean
	 * @param locale
	 * @param withParameter whether to include $PARAMETER in datasource
	 */
	void loadDatasourceAndValue(WorkflowContext workflowContext,
			FieldChangeValue fieldChangeValue, Integer parameterCode, TPersonBean personBean, Locale locale, boolean withParameter);
	/**
	 * Gets the name of the jsp fragment which contains 
	 * the control for rendering the bulk value
	 * @return
	 */
	String getValueRendererJsClass();
	
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
	String getValueRendererJsonConfig(Object configuredValue, Object assignedValue, Object dataSource,
			boolean withParameter, TPersonBean personBean, Locale locale);
		
}
