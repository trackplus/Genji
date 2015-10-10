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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.fieldChange.FieldChangeSetters;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.item.ItemBL;

/**
 * Bulk setter for string fields
 * @author Tamas
 *
 */
public class ParentFieldChangeApply extends GenericFieldChangeApply {
	private static final Logger LOGGER = LogManager.getLogger(ParentFieldChangeApply.class);
	public ParentFieldChangeApply(Integer fieldID) {
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
		Integer parentID = null;
		try {
			parentID = (Integer)value;
		} catch (Exception e) {
			LOGGER.debug("Getting the string value for " + value +  " failed with " + e.getMessage(), e);
		}
		//Integer parentID = getFirstNumber(strValue);
		switch (getSetter()) {
		case FieldChangeSetters.SET_TO:
			if (parentID!=null) {
				if (ItemBL.isAscendant(workItemBean.getObjectID(), parentID)) {
					List<ErrorData> errorList = new LinkedList<ErrorData>();
					errorList.add(new ErrorData("itemov.massOperation.err.parentIsDescendant"));
					return errorList;
				} else {
					workItemBean.setAttribute(activityType, parameterCode, parentID);
				}
			}
			break;
		case FieldChangeSetters.SET_NULL:
			return super.setWorkItemAttribute(workItemContext, workItemBean, parameterCode, value);
		}
		//no error
		return null;
	}
	
	/**
	 * The strValue contains the workItemID and the Synopsis
	 * Only the workItemID is needed
	 * @param strValue
	 * @return
	 */
	/*private static Integer getFirstNumber(String strValue) {
		if (strValue==null || "".equals(strValue)) {
			return null;
		}
		StringBuffer strBuff = new StringBuffer();
		char c;
		for (int i = 0; i < strValue.length() ; i++) {
			c = strValue.charAt(i);
			if (Character.isDigit(c)) {
				strBuff.append(c); 
			} else {
				break;
			}
		}
		if (strBuff.length()>0) {
			try {
				return Integer.valueOf(strBuff.toString());
			} catch (Exception e) {
			}
		} 
		return null;
	}*/
}
