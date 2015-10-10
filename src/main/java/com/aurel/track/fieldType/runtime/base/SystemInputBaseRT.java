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


package com.aurel.track.fieldType.runtime.base;

import java.util.Map;

import com.aurel.track.beans.TWorkItemBean;

/**
 * 
 * Base class for read/write runtime system fields 
 * @author Tamas Ruff
 *
 */
public abstract class SystemInputBaseRT extends InputFieldTypeRT {
	
	/**
	 * Empty body: no extra save for system fields
	 */
	public void saveAttribute(Integer fieldID, Integer parameterCode, TWorkItemBean workItemBean, TWorkItemBean workItemBeanOriginal) {		 	
	}

	/**
	 * Empty body: no extra load for system fields
	 */
	public void loadAttribute(Integer fieldID, Integer parameterCode, TWorkItemBean workItemBean, Map<String, Object> attributeValueMap) {
	}
	
}
