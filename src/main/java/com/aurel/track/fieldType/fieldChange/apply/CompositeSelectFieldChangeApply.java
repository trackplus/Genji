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


package com.aurel.track.fieldType.fieldChange.apply;

import java.util.List;
import java.util.SortedMap;

import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.fieldChange.FieldChangeSetters;
import com.aurel.track.fieldType.runtime.base.CustomCompositeBaseRT;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.types.FieldTypeManager;

/**
 * Apply field change for composite select fields 
 * @author Tamas
 *
 */
public class CompositeSelectFieldChangeApply extends GenericFieldChangeApply {
	
	public CompositeSelectFieldChangeApply(Integer fieldID) {
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
		if (value==null) {
			return null;
		}
		SortedMap<Integer, Integer[]> valueMap = (SortedMap<Integer, Integer[]>)value;
		CustomCompositeBaseRT customCompositeBaseRT = null;
		try {
			customCompositeBaseRT = (CustomCompositeBaseRT)FieldTypeManager.getFieldTypeRT(activityType);
		} catch (Exception e) {
		}
		if (customCompositeBaseRT!=null) {
			int noOfParts = customCompositeBaseRT.getNumberOfParts();
			if (getSetter()==FieldChangeSetters.SET_NULL) {
				for (int i = 0; i < noOfParts; i++) {
					workItemBean.setAttribute(activityType, Integer.valueOf(i+1), null);
				}
				return null;
			} else {
				if (getSetter()==FieldChangeSetters.SET_TO && value!=null) {
					for (int i = 0; i < noOfParts; i++) {
						Integer localParameterCode = Integer.valueOf(i+1);
						workItemBean.setAttribute(activityType, localParameterCode, valueMap.get(localParameterCode));
					}			
				} else {
					if (getSetter()==FieldChangeSetters.SET_REQUIRED) {
						return super.setWorkItemAttribute(workItemContext, workItemBean, parameterCode, valueMap);
					}
				}
			}
		}
		return null;
	}
}
