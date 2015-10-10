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

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.fieldChange.FieldChangeSetters;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.types.FieldTypeManager;
/**
 * Apply field change for system select fields
 * Differs from the custom select by the fact that the value is Integer instead of Integer[]
 * @author Tamas
 *
 */
public class SelectFieldChangeApply extends GenericFieldChangeApply {
	private static final Logger LOGGER = LogManager.getLogger(SelectFieldChangeApply.class);
	protected boolean isCustom;
	public SelectFieldChangeApply(Integer fieldID, boolean isCustom) {
		super(fieldID);
		this.isCustom = isCustom;
	}

	/**
	 * Sets the workItemBean's attribute
	 * @param workItemContext
	 * @param workItemBean
	 * @param fieldID
	 * @param parameterCode
	 * @param value	
	 * @return ErrorData if an error is found
	 */
	@Override
	public List<ErrorData> setWorkItemAttribute(WorkItemContext workItemContext,
			TWorkItemBean workItemBean, Integer parameterCode, Object value) {
		if (getSetter()==FieldChangeSetters.SET_NULL || getSetter()==FieldChangeSetters.SET_REQUIRED) {
			return super.setWorkItemAttribute(workItemContext, workItemBean, parameterCode, value);
		}
		switch (getSetter()) {
		case FieldChangeSetters.SET_TO: 
			Object oldValue = workItemBean.getAttribute(activityType);
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(activityType);
			if (oldValue==null || fieldTypeRT.valueModified(value, oldValue)) {
				workItemBean.setAttribute(activityType, parameterCode, value);
				return null;
			}
			break;
		}
		return null;
	}		
}
