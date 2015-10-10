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


package com.aurel.track.fieldType.runtime.callbackInterfaces;

import java.util.Map;

import com.aurel.track.beans.TWorkItemBean;

/**
 * Interface for loading a saved custom field value from the database
 * or calculating a calculated value   
 * Used only by custom fields
 * (The system fields are loaded directly into the workItem)
 * @author Tamas Ruff
 *
 */
public interface ILoad {
	
	/**
	 * Loads or calculates the saved custom attribute value from the 
	 * attributeValueMap into the workItemBean's customAttributeValues Map  
	 * @param fieldID 
	 * @param parameterCode neglected for single custom fields
	 * @param workItemBean
	 * @param attributeValueMap map
	 */
	public void loadAttribute(Integer fieldID, Integer parameterCode, TWorkItemBean workItemBean, Map<String, Object> attributeValueMap);

	/**
	 * Whether this field is a calculated field or a stored field
	 * If calculated and dependent from other fields then changing 
	 * the dependency should trigger the recalculation of this field
	 * i.e. loadAttribute() should be called also for refreshing a calculated field
	 * (but should not be called by refreshing when it means a loading of a stored 
	 * value from a database)   
	 * @param fieldID
	 * @param parameterCode
	 */
	//public boolean isCalculated(Integer fieldID, Integer parameterCode);
	
}
