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

import com.aurel.track.beans.TWorkItemBean;

/**
 * Interface for saving a custom field attribute value from 
 * the workItem's customAttributeValues Map to the database
 * Should be implemented only by 
 * single custom fields or parts of a composite custom field,
 * if they are not calculated fields
 * (For system fields the saving of the attributes is made at once for all system fields) 
 * @author Tamas Ruff
 *
 */
public interface ISave {
	
	/**
	 * Saves the custom attribute value(s) from the workItem customAttributeValues Map
	 * to the database  
	 * @param fieldID
	 * @param parameterCode neglected for single custom fields
	 * @param workItemBean
	 * @param workItemBeanOriginal
	 */
	public void saveAttribute(Integer fieldID, Integer parameterCode, 
			TWorkItemBean workItemBean, TWorkItemBean workItemBeanOriginal);
	
}
