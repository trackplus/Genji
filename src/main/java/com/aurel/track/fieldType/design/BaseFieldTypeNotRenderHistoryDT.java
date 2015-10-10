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


package com.aurel.track.fieldType.design;


/**
 * Some system fields does not need the required flag even if 
 * they are not required: for ex. superiorWorkitem or comment
 * Also for priority and lastEditedBy: although they are required at database 
 * level accidentally they was marked as not required at TField table
 * @author Tamas Ruff
 *
 */
public class BaseFieldTypeNotRenderHistoryDT extends BaseFieldTypeDT {

	public BaseFieldTypeNotRenderHistoryDT(Integer parameterCode,
			String pluginID) {
		super(parameterCode, pluginID);	
	}

	public BaseFieldTypeNotRenderHistoryDT(String pluginID) {
		super(pluginID);
	}

	/**   
	 * Whether the "History" check box should be rendered  
	 * It should not be rendered for: 
	 * 1. the status, start/end date 
	 * (the explicit fields from the old history), 
	 * and comment. They are forced by code to true. 
	 * 2. some read only fields issueNo, originator, createDate, 
	 * lastModifiedDate, accessLevel, lastEditedBy, archiveLevel
	 * They are forced by code to false
	 * @return
	 */
	@Override
	public boolean renderHistoryFlag() {
		return false;
	}
	
	
	
		
}
