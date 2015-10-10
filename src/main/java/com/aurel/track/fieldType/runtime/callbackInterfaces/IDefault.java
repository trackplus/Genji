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
 * Load default values in workItem by creating a new issue
 * @author Tamas Ruff
 *
 */
public interface IDefault {
		
	/**
	 * Sets the default value in the workItemBean for a field (and parameterCode)
	 * according to the validConfig and fieldSettings
	 * @param fieldID 
	 * @param parameterCode 
	 * @param validConfig
	 * @param fieldSettings
	 * @param workItemBean
	 */
	public void setDefaultAttribute(Integer fieldID, Integer parameterCode, 
			Integer validConfig, Map<String, Object> fieldSettings, TWorkItemBean workItemBean);
}
