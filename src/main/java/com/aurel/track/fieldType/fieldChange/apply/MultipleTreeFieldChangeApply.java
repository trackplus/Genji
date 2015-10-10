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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.fieldChange.FieldChangeSetters;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.util.GeneralUtils;

/**
 * Bulk setter for project picker
 * Differs from the project bulk setter by the fact that the value is Integer[] instead of Integer
 * @author Tamas
 *
 */
public class MultipleTreeFieldChangeApply extends CustomMultipleSelectFieldChangeApply {		
	private static final Logger LOGGER = LogManager.getLogger(MultipleTreeFieldChangeApply.class);
	public MultipleTreeFieldChangeApply(Integer fieldID) {
		super(fieldID);	
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
		Integer[] selectedValues = (Integer[])value;
		if (getSetter()==FieldChangeSetters.SET_TO) {
			workItemBean.setAttribute(activityType, selectedValues);
			return null;
		}
		Object originalValue = workItemBean.getAttribute(activityType, parameterCode);
		Object[] originalSelections = null;
		if (originalValue!=null) {
			try {
				//multiple values are loaded in the workItem as Object[], not as Integer[] !!! 
				originalSelections = (Object[])originalValue;
			} catch (Exception e) {
				LOGGER.debug("Getting the original object array value for " + value +  " failed with " + e.getMessage(), e);
			}
		}
		Set<Integer> originalSet = new HashSet<Integer>();
		if (originalSelections!=null && originalSelections.length>0) {
			for (int i = 0; i < originalSelections.length; i++) {
				try {
					originalSet.add((Integer)originalSelections[i]);
				} catch (Exception e) {
					LOGGER.debug("Transforming the original object value " + originalSelections[i] +  " to Integer failed with " + e.getMessage(), e);
				}
			}
		}
		Set<Integer> bulkSelectionsSet = GeneralUtils.createSetFromIntegerArr(selectedValues);
		switch (getSetter()) {
			case FieldChangeSetters.ADD_ITEMS:
				originalSet.addAll(bulkSelectionsSet);
				workItemBean.setAttribute(activityType, parameterCode, GeneralUtils.createIntegerArrFromCollection(originalSet));
				break;
			case FieldChangeSetters.REMOVE_ITEMS:
				originalSet.removeAll(bulkSelectionsSet);
				workItemBean.setAttribute(activityType, parameterCode, GeneralUtils.createIntegerArrFromCollection(originalSet));
				break;
			default:
				break;
		}
		return null;
	}
}
