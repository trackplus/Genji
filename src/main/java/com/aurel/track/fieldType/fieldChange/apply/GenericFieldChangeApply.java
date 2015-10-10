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


package com.aurel.track.fieldType.fieldChange.apply;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aurel.track.admin.customize.workflow.activity.AbstractActivity;
import com.aurel.track.admin.customize.workflow.activity.IActivityExecute;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.errors.ErrorParameter;
import com.aurel.track.fieldType.fieldChange.FieldChangeSetters;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.validators.RequiredValidator;
import com.aurel.track.fieldType.runtime.validators.Validator;

/**
 * Base class for executing a field change
 * @author Tamas Ruff
 *
 */
public class GenericFieldChangeApply extends AbstractActivity implements IActivityExecute {
	private static final Logger LOGGER = LogManager.getLogger(GenericFieldChangeApply.class);
	
	public GenericFieldChangeApply(Integer fieldID) {
		super(fieldID);
	}
	
	/**
	 * Executes an activity
	 * @param workItemContext
	 * @param value	the value converted from string
	 * @param personBean the person executing the activity
	 * @return ErrorData list if an error is found
	 */
	@Override
	public List<ErrorData> executeActivity(WorkItemContext workItemContext,
			Object value, TPersonBean personBean)  {
		return setWorkItemAttribute(workItemContext, workItemContext.getWorkItemBean(), null, value);
	}
	
	/**
	 * Sets the workItemBean's attribute
	 * @param workItemBean
	 * @param fieldID
	 * @param parameterCode
	 * @param value	
	 * @return ErrorData if an error is found
	 */
	public List<ErrorData> setWorkItemAttribute(WorkItemContext workItemContext, TWorkItemBean workItemBean,
			Integer parameterCode, Object value) {
		switch (getSetter()) {
		case FieldChangeSetters.SET_TO:
			//the correct value is prepared in fromDisplayString()
			workItemBean.setAttribute(activityType, value);
			break;
		case FieldChangeSetters.SET_NULL:
			//set the value to null 	
			workItemBean.setAttribute(activityType, parameterCode, null);
			break;
		case FieldChangeSetters.SET_REQUIRED:
			Validator validator = new RequiredValidator();
			Integer fieldID = getActivityType();
			Map<Integer, TFieldConfigBean> fieldConfigs = workItemContext.getFieldConfigs();
			if (fieldConfigs!=null) {
				TFieldConfigBean validConfig = fieldConfigs.get(fieldID);
				if (validConfig!=null) {
					Object actualValue = workItemBean.getAttribute(fieldID,parameterCode);
					ErrorData errorData=validator.validateField(actualValue);
					if (errorData!=null) {
						List<ErrorData> validationErrors = new LinkedList<ErrorData>();
						errorData.setFieldID(fieldID);
						List<ErrorParameter> resourceParameters=errorData.getResourceParameters();
						if (resourceParameters==null) {
							resourceParameters = new LinkedList<ErrorParameter>();
							errorData.setResourceParameters(resourceParameters);
						}
						resourceParameters.add(0, new ErrorParameter(validConfig.getLabel()));
						validationErrors.add(errorData);
						return validationErrors;
					}
				} else {
					LOGGER.debug("Field " + fieldID + " not present on form. Could not be enforced as required");
				}
			} 
			break;
		}
		//no error
		return null;
	}
	
}
